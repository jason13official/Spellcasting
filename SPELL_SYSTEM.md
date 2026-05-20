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

Override `onCast` — this is where you decide what gets hit. Call `resolver.onResolveEffect(hitResult)` to set the target on the context and trigger the alteration loop.

```java
public class SelfPropagation extends AbstractPropagation {

    public static final Identifier ID = Spellcasting.id("self");
    public static final SelfPropagation INSTANCE = new SelfPropagation();

    public SelfPropagation() { super(ID); }

    @Override
    public CastResolveType onCast(SpellStats stats, SpellContext context, SpellResolver resolver) {
        EntityHitResult selfHit = new EntityHitResult(context.entity().get());
        resolver.onResolveEffect(selfHit);  // sets hitResult on context, triggers resume
        return CastResolveType.SUCCESS;
    }

    @Override public Set<Identifier> getCompatibleAugments() { return Set.of(); }
    @Override public Set<Identifier> getIncompatibleAugments() { return Set.of(); }
    @Override public int getCastingCost() { return 5; }
}
```

Optionally override `onCastOnBlock` and `onCastOnEntity` for targeted propagations (default: return `FAILURE`).

**`CastResolveType` values:**

| Value | Meaning |
|---|---|
| `SUCCESS` | Spell executed; mana is expended |
| `FAILURE` | Spell did not execute; mana is not expended |
| `SUCCESS_NO_EXPEND` | Spell executed; mana is not expended |

### AbstractAlteration

Override `onResolveEntity` and/or `onResolveBlock`. The base `onResolve` dispatches automatically based on the `HitResult` type stored in `SpellContext`. If no hit result is present in the context, `onResolveNone` is called instead.

```java
public class HealAlteration extends AbstractAlteration {

    public static final Identifier ID = Spellcasting.id("heal");
    public static final HealAlteration INSTANCE = new HealAlteration();

    public HealAlteration() { super(ID); }

    @Override
    public void onResolveEntity(EntityHitResult hit, Level world, Entity caster,
                                SpellStats stats, SpellContext context, SpellResolver resolver) {
        if (hit.getEntity() instanceof LivingEntity target) {
            target.heal(4.0f + stats.amplification()); // 4.0f == 2 hearts, 1.0f == 0.5 heart
        }
    }

    // onResolveNone() — called when spell has no hit result (position-only delivery)
    // default: no-op — override if needed

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
    public static final AmplifyAugmentation INSTANCE = new AmplifyAugmentation();

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

Carries execution state and contextual world data. All fields are `Optional` — alterations and propagations read only what they need, handle absences gracefully.

```java
Optional<MinecraftServer> server()
Optional<Level>           level()
Optional<ChunkAccess>     chunk()
Optional<BlockPos>        blockPos()
Optional<Vec3>            pos()
Optional<Entity>          entity()
Optional<HitResult>       hitResult()    // set by onResolveEffect(); mutable
```

**Mutable state:**

| Field | Accessor | Setter |
|---|---|---|
| `boolean canceled` | `isCanceled()` | `setCanceled(boolean)` |
| `Optional<HitResult> hitResult` | `hitResult()` | `setHitResult(Optional<HitResult>)` |
| `SpellContext previousContext` | `getPreviousContext()` | `setPreviousContext(SpellContext)` |

**Construction:**

```java
SpellContext context = new SpellContext(
    Optional.empty(),         // server
    Optional.of(level),       // level
    Optional.empty(),         // chunk
    Optional.of(player.blockPosition()),
    Optional.of(player.position()),
    Optional.of(player),      // entity (used by SelfPropagation as cast target)
    Optional.empty()          // hitResult — set automatically by onResolveEffect()
);
```

**Cancellation** — call `context.setCanceled(true)` inside any alteration to abort remaining alterations in the current resolution pass.

---

## SpellResolver

The execution engine. Construct it with a spell, context, and a mana source, then call `onCast()`.

```java
IWrappedCaster<Player> caster = new PlayerCaster(player);
SpellContext context           = new SpellContext(...);
Spell spell                    = new Spell(SelfPropagation.INSTANCE, HealAlteration.INSTANCE);

SpellResolver resolver = new SpellResolver(spell, context, caster);
CastResolveType result = resolver.onCast();
```

**Execution flow:**

```
resolver.onCast()
  → canCast()              checks IWrappedCaster.enoughMana(spell.getCost())
  → spell.getPropagation() finds first AbstractPropagation
  → propagation.onCast(stats, context, resolver)
      → resolver.onResolveEffect(hitResult)   ← called by propagation on hit
          → context.setHitResult(Optional.of(hitResult))
          → resume()
              Level world  = context.level().orElse(null)
              Entity caster = context.entity().orElse(null)
              for each part in spell.definition():
                skip: AbstractAugmentation, disabled parts
                on AbstractAlteration:
                  spell.getAugments(i)          → trailing augments
                  SpellStats.builder()
                    .setAugments(augments)
                    .build(alteration, context)  → calls aug.applyModifiers() per augment
                  if context.hitResult().isPresent():
                    alteration.onResolve(hitResult, world, caster, stats, context, resolver)
                  else:
                    alteration.onResolveNone(world, caster, stats, context, resolver)
  → if SUCCESS: IWrappedCaster.expendMana(spell.getCost())
```

### IWrappedCaster\<T\>

Implement this to connect the resolver to your mana system. `T` is the caster type (e.g. `Player`).

```java
public interface IWrappedCaster<T> {
    T       getCaster();
    boolean enoughMana(int cost);
    void    expendMana(int cost);
    Vec3    getPosition();
}
```

**`PlayerCaster`** — provided implementation for player-held casting:

```java
IWrappedCaster<Player> caster = new PlayerCaster(player);
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
spell.getAugments(1);      // [Amplify, Amplify]  (trailing after HealAlteration at index 1)
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
    IWrappedCaster.java        Mana abstraction (generic: IWrappedCaster<T>)
  context/
    SpellContext.java           All-Optional execution state
  modifier/
    ISpellModifier.java        Modifier interface (used by ISpellModifierItem)
    ISpellModifierItem.java    Item-stack modifier variant
  part/
    AbstractPropagation.java   Spell delivery — calls onResolveEffect(hitResult)
    AbstractAlteration.java    Spell effect — onResolveBlock / onResolveEntity / onResolveNone
    AbstractAugmentation.java  Stat modifier — applyModifiers(builder, part, context)
  stat/
    SpellStats.java            Per-alteration stat snapshot + Builder

impl/common/content/spell/
  caster/PlayerCaster.java
  propagation/SelfPropagation.java
  alteration/HealAlteration.java
  augmentation/AccelerationAugment.java
  augmentation/AmplifyAugmentation.java

impl/common/content/item/
  WandItem.java                Example item using the full pipeline
```
