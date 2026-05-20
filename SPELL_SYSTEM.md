# Spell System

A data-driven spell engine inspired by Ars Nouveau. Spells are flat ordered lists of **spell parts** that flow through a `Propagation → Alteration(s) + Augmentation(s)` pipeline.

---

## Core Concepts

### The Recipe

A `Spell` is a record holding a flat `List<AbstractSpellPart>`. Parts are read left-to-right. The first `AbstractPropagation` drives delivery; every `AbstractAlteration` after it is an effect; `AbstractAugmentation` instances immediately following an alteration modify only that alteration.

```
[Self] [Heal] [Amplify] [Amplify] [Ignite]
  ↑      ↑    └── modify Heal ──┘    ↑
  Prop  Alter                       Alter (no augments)
```

Augmentations are **positional** — they bind to the nearest preceding alteration, not globally.

### Part Types

| Type | Base Class | `typeIndex()` | Role |
|---|---|---|---|
| Propagation | `AbstractPropagation` | `0` | Determines *how* the spell reaches its target |
| Alteration | `AbstractAlteration` | `1` | Applies an effect at the resolved target |
| Augmentation | `AbstractAugmentation` | `2` | Modifies the `SpellStats` of the preceding alteration |

---

## Implementing a Spell Part

### AbstractPropagation

Override `onCast` — this is where you decide what gets hit and call `resolver.onResolveEffect(hitResult, world)` to trigger the alteration loop.

```java
public class SelfPropagation extends AbstractPropagation {

    public static final Identifier ID = Spellcasting.id("self");

    public SelfPropagation() { super(ID); }

    @Override
    public CastResolveType onCast(SpellStats stats, SpellContext context, SpellResolver resolver) {
        EntityHitResult selfHit = new EntityHitResult(context.getCaster());
        resolver.onResolveEffect(selfHit, context.getCastLevel());
        return CastResolveType.SUCCESS;
    }

    @Override public Set<Identifier> getCompatibleAugments() { return Set.of(); }
    @Override public Set<Identifier> getIncompatibleAugments() { return Set.of(); }
    @Override public int getCastingCost() { return 5; }
}
```

Optionally override `onCastOnBlock` and `onCastOnEntity` for targeted propagations.

**`CastResolveType` values:**

| Value | Meaning |
|---|---|
| `SUCCESS` | Spell executed; mana is expended |
| `FAILURE` | Spell did not execute; mana is not expended |
| `SUCCESS_NO_EXPEND` | Spell executed; mana is not expended |

### AbstractAlteration

Override `onResolveEntity` and/or `onResolveBlock`. The base `onResolve` dispatches automatically based on `HitResult` type.

```java
public class HealAlteration extends AbstractAlteration {

    public static final Identifier ID = Spellcasting.id("heal");

    public HealAlteration() { super(ID); }

    @Override
    public void onResolveEntity(EntityHitResult hit, Level world, LivingEntity caster,
                                SpellStats stats, SpellContext context, SpellResolver resolver) {
        if (hit.getEntity() instanceof LivingEntity target) {
            target.heal(4.0f + stats.amplification());
        }
    }

    @Override public Set<Identifier> getCompatibleAugments() { return Set.of(); }
    @Override public Set<Identifier> getIncompatibleAugments() { return Set.of(); }
    @Override public int getCastingCost() { return 15; }
}
```

### AbstractAugmentation

Override `applyModifiers` to mutate the `SpellStats.Builder` for the alteration this augment trails. All modifications are **additive**.

```java
public class AmplifyAugmentation extends AbstractAugmentation {

    public static final Identifier ID = Spellcasting.id("amplify");

    public AmplifyAugmentation() { super(ID); }

    @Override
    public Builder applyModifiers(Builder builder, AbstractSpellPart spellPart, SpellContext context) {
        return builder.addAmplificationModifier(1.0f);
    }

    @Override public Set<Identifier> getCompatibleAugments() { return Set.of(); }
    @Override public Set<Identifier> getIncompatibleAugments() { return Set.of(); }
    @Override public int getCastingCost() { return 10; }
}
```

---

## SpellStats

A per-alteration snapshot of all modifier values contributed by trailing augmentations.

| Field | Accessor | Builder Method | Default |
|---|---|---|---|
| `int level` | `level()` | `setLevel(int)` | `0` |
| `float amplification` | `amplification()` | `addAmplificationModifier(float)` | `0.0` |
| `float aoe` | `aoe()` | `addAoeModifier(float)` | `0.0` |
| `float acceleration` | `acceleration()` | `addAccelerationModifier(float)` | `0.0` |
| `float duration` | `duration()` | `addDurationModifier(float)` | `0.0` |
| `float damage` | `damage()` | `addDamageModifier(float)` | `0.0` |
| `boolean sensitive` | `isSensitive()` | `setSensitive(boolean)` | `false` |
| `List<AbstractAugmentation>` | `augments()` | `setAugments(List<>)` | `[]` |

`SpellStats` is built automatically by `SpellResolver` before each alteration is resolved. Alterations read stats via their accessors; they never build stats themselves.

---

## SpellContext

Carries execution state and contextual world data into every part.

**Required execution fields** (always populated during a live cast):

| Field | Accessor |
|---|---|
| `LivingEntity caster` | `getCaster()` |
| `Level castLevel` | `getCastLevel()` |
| `ItemStack casterTool` | `getCasterTool()` |
| `boolean canceled` | `isCanceled()` / `setCanceled(boolean)` |

**Optional contextual fields** (populated based on available context):

```java
Optional<MinecraftServer> server()
Optional<Level>           level()
Optional<ChunkAccess>     chunk()
Optional<BlockPos>        blockPos()
Optional<Vec3>            pos()
Optional<Entity>          entity()
Optional<HitResult>       hitResult()
```

**Construction** — use the full constructor for live execution:

```java
SpellContext context = new SpellContext(
    player,               // LivingEntity caster
    world,                // Level castLevel
    heldStack,            // ItemStack casterTool
    Optional.of(server),
    Optional.of(world),
    Optional.empty(),     // chunk
    Optional.empty(),     // blockPos
    Optional.empty(),     // pos
    Optional.of(player),
    Optional.empty()      // hitResult
);
```

**Cancellation** — call `context.setCanceled(true)` inside any alteration to abort remaining alterations in the current `resume()` pass.

---

## SpellResolver

The execution engine. Construct it with a spell, context, and a mana source, then call `onCast`.

```java
IWrappedCaster caster = new PlayerCaster(player);
SpellContext context   = new SpellContext(player, world, stack, ...);
Spell spell            = new Spell(SelfPropagation.INSTANCE, HealAlteration.INSTANCE);

SpellResolver resolver = new SpellResolver(spell, context, caster);
CastResolveType result = resolver.onCast(world);
```

**Execution flow:**

```
resolver.onCast(world)
  → canCast()              checks IWrappedCaster.enoughMana(spell.getCost())
  → spell.getPropagation() finds first AbstractPropagation
  → propagation.onCast(stats, context, resolver)
      → resolver.onResolveEffect(hitResult, world)   ← called by propagation on hit
          → resume(hitResult, world)
              for each part in spell.definition():
                skip: AbstractAugmentation, disabled parts
                on AbstractAlteration:
                  spell.getAugments(i)          → trailing augments
                  SpellStats.builder()
                    .setAugments(augments)
                    .build(alteration, context)  → calls aug.applyModifiers() per augment
                  alteration.onResolve(hitResult, world, caster, stats, context, resolver)
  → if SUCCESS: IWrappedCaster.expendMana(spell.getCost())
```

### IWrappedCaster

Implement this to connect the resolver to your mana system:

```java
public interface IWrappedCaster {
    boolean enoughMana(int cost);
    void    expendMana(int cost);
    Vec3    getPosition();
}
```

---

## Building a Spell

```java
// Self-targeting heal with two amplify augments
Spell spell = new Spell(
    SelfPropagation.INSTANCE,
    HealAlteration.INSTANCE,
    AmplifyAugmentation.INSTANCE,
    AmplifyAugmentation.INSTANCE
);

spell.getCost();           // 5 + 15 + 10 + 10 = 40
spell.getPropagation();    // Optional[SelfPropagation]
spell.getAugments(1);      // [Amplify, Amplify]  (trailing after index 1)
```

When resolved, `HealAlteration` receives `SpellStats` with `amplification = 2.0f`, healing for `4.0 + 2.0 = 6.0` HP.

---

## Key Packages

```
api/spell/
  AbstractSpellPart.java       Base class for all parts
  CastResolveType.java         SUCCESS / FAILURE / SUCCESS_NO_EXPEND
  Spell.java                   Immutable recipe record
  SpellResolver.java           Execution engine
  caster/
    IWrappedCaster.java        Mana abstraction
  context/
    SpellContext.java           Execution state + contextual data
  modifier/
    ISpellModifier.java        Modifier interface (used by ISpellModifierItem)
    ISpellModifierItem.java    Item-stack modifier variant
  part/
    AbstractPropagation.java   Spell delivery
    AbstractAlteration.java    Spell effect
    AbstractAugmentation.java  Stat modifier
  stat/
    SpellStats.java            Per-alteration stat snapshot

impl/common/content/spell/
  propagation/SelfPropagation.java
  alteration/HealAlteration.java
  augmentation/AccelerationAugment.java
```
