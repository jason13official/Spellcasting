# Spell System

A data-driven spell engine inspired by Ars Nouveau. Spells are flat ordered lists of **spell parts** that flow through a `Propagation → Alteration(s) + Augmentation(s)` pipeline.

---

## Core Concepts

### The Recipe

A `Spell` is a record holding a flat `List<AbstractSpellPart>`. Parts are read left-to-right. The first `AbstractPropagation` drives delivery; every `AbstractAlteration` after it is an effect; `AbstractAugmentation` instances immediately following an alteration modify only that alteration.

```
[Self] [Heal] [Amplify] [Amplify] [Message]
  ↑      ↑    └── modify Heal ──┘    ↑
  Prop  Alter                       Alter (no augments)
```

Augmentations are **positional** — they bind to the nearest preceding alteration, not globally.

### Part Types

| Type | Base Class | `typeIndex()` | Default Cost |
|---|---|---|---|
| Propagation | `AbstractPropagation` | `0` | `10` |
| Alteration | `AbstractAlteration` | `1` | `10` |
| Augmentation | `AbstractAugmentation` | `2` | `10` |

---

## Implementing a Spell Part

### AbstractPropagation

Override `onCast` — determines the target. Call `resolver.onResolveEffect(hitResult)` to write the hit result into the context and trigger the alteration loop.

```java
public class SelfPropagation extends AbstractPropagation {

    public static final Identifier ID = Spellcasting.id("self");
    public static final SelfPropagation INSTANCE = new SelfPropagation();

    public SelfPropagation() { super(ID); }

    @Override
    public CastResolveType onCast(SpellStats stats, SpellContext context, SpellResolver resolver) {
        EntityHitResult selfHit = new EntityHitResult(context.entity().get());
        resolver.onResolveEffect(selfHit);
        return CastResolveType.SUCCESS;
    }

    @Override public Set<Identifier> getCompatibleAugments() { return Set.of(); }
    @Override public Set<Identifier> getIncompatibleAugments() { return Set.of(); }
    @Override public int getCastingCost() { return 5; }
}
```

Optionally override `onCastOnBlock(BlockHitResult, ...)` and `onCastOnEntity(EntityHitResult, ...)` for targeted propagations (default: return `FAILURE`).

**`CastResolveType` values:**

| Value | Meaning |
|---|---|
| `SUCCESS` | Spell executed; mana is expended |
| `FAILURE` | Spell did not execute; mana is not expended |
| `SUCCESS_NO_EXPEND` | Spell executed; mana is not expended |

### AbstractAlteration

Override `onResolveEntity` and/or `onResolveBlock`. `onResolve` dispatches automatically based on the `HitResult` type in `SpellContext`. If no hit result is present, `onResolveNone` is called instead (default: no-op).

The caster parameter is `Entity` — cast to `LivingEntity` or `Player` as needed. Effects that touch the server world should guard with `world instanceof ServerLevel`.

```java
public class HealAlteration extends AbstractAlteration {

    public static final Identifier ID = Spellcasting.id("heal");
    public static final HealAlteration INSTANCE = new HealAlteration();

    public HealAlteration() { super(ID); }

    @Override
    public void onResolveEntity(EntityHitResult hit, Level world, Entity caster,
                                SpellStats stats, SpellContext context, SpellResolver resolver) {
        if (hit.getEntity() instanceof LivingEntity target && world instanceof ServerLevel) {
            target.heal(4.0f + stats.amplification()); // 4.0f == 2 hearts, 1.0f == 0.5 heart
        }
    }

    @Override public Set<Identifier> getCompatibleAugments() { return Set.of(AmplifyAugmentation.ID); }
    @Override public Set<Identifier> getIncompatibleAugments() { return Set.of(); }
    @Override public int getCastingCost() { return 15; }
}
```

Alterations can also read the Optional fields directly from `SpellContext` — useful when the effect depends on context data beyond the hit result:

```java
public class MessageAlteration extends AbstractAlteration {

    public static final Identifier ID = Spellcasting.id("message");
    public static final MessageAlteration INSTANCE = new MessageAlteration();

    @Override
    public void onResolveEntity(EntityHitResult hit, Level world, Entity caster,
                                SpellStats stats, SpellContext context, SpellResolver resolver) {
        context.server().ifPresent(server ->
            server.sendSystemMessage(Component.literal("Logged info to server!"))
        );
    }

    @Override public int getCastingCost() { return 15; }
}
```

**AbstractAlteration hooks:**

| Method | When called |
|---|---|
| `onResolve(HitResult, Level, Entity, SpellStats, SpellContext, SpellResolver)` | Dispatches to block/entity; override only to intercept before dispatch |
| `onResolveEntity(EntityHitResult, ...)` | Hit result is an entity |
| `onResolveBlock(BlockHitResult, ...)` | Hit result is a block face |
| `onResolveNone(Level, Entity, SpellStats, SpellContext, SpellResolver)` | No hit result in context |

### AbstractAugmentation

Override `applyModifiers` to mutate the `SpellStats.Builder`. All modifications are **additive**.

```java
public class AmplifyAugmentation extends AbstractAugmentation {

    public static final Identifier ID = Spellcasting.id("amplify");
    public static final AmplifyAugmentation INSTANCE = new AmplifyAugmentation();

    public AmplifyAugmentation() { super(ID); }

    @Override
    public Builder applyModifiers(Builder builder, AbstractSpellPart spellPart, SpellContext context) {
        return builder.addAmplificationModifier(2.0f);  // +2.0f per instance
    }

    @Override public Set<Identifier> getCompatibleAugments() { return Set.of(AccelerationAugment.ID); }
    @Override public Set<Identifier> getIncompatibleAugments() { return Set.of(); }
    @Override public int getCastingCost() { return 10; }
}
```

---

## SpellStats

A per-alteration snapshot built from trailing augmentations. Built fresh for each alteration during `resume()` — alterations read, never build.

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

---

## SpellContext

Carries execution state and contextual world data. All fields are `Optional` — read what's present, handle absence gracefully.

**Read-only fields** (set at construction):

```java
Optional<MinecraftServer> server()     // present when cast server-side
Optional<Level>           level()
Optional<ChunkAccess>     chunk()
Optional<BlockPos>        blockPos()
Optional<Vec3>            pos()
Optional<Entity>          entity()     // the caster entity
```

**Mutable fields:**

| Field | Accessor | Setter | Set by |
|---|---|---|---|
| `Optional<HitResult> hitResult` | `hitResult()` | `setHitResult(Optional<HitResult>)` | `SpellResolver.onResolveEffect()` |
| `boolean canceled` | `isCanceled()` | `setCanceled(boolean)` | alteration code |
| `SpellContext previousContext` | `getPreviousContext()` | `setPreviousContext(SpellContext)` | child context creation |

**Construction:**

```java
SpellContext context = new SpellContext(
    Optional.ofNullable(level instanceof ServerLevel ? level.getServer() : null),  // server
    Optional.of(level),
    Optional.empty(),                      // chunk
    Optional.of(player.blockPosition()),
    Optional.of(player.position()),
    Optional.of(player),                   // entity — used as cast target by SelfPropagation
    Optional.empty()                       // hitResult — set by onResolveEffect()
);
```

**Cancellation** — call `context.setCanceled(true)` in any alteration to abort remaining alterations.

---

## SpellResolver

Execution engine. Construct with a spell, context, and mana source; call `onCast()`.

```java
IWrappedCaster<Player> caster = new PlayerCaster(player);
SpellContext context           = new SpellContext(...);
Spell spell                    = new Spell(SelfPropagation.INSTANCE, HealAlteration.INSTANCE,
                                           AmplifyAugmentation.INSTANCE);

SpellResolver resolver = new SpellResolver(spell, context, caster);
CastResolveType result = resolver.onCast();
```

**Execution flow:**

```
resolver.onCast()
  → canCast()              IWrappedCaster.enoughMana(spell.getCost())
  → spell.getPropagation() first AbstractPropagation in recipe
  → propagation.onCast(stats, context, resolver)
      → resolver.onResolveEffect(hitResult)   ← propagation calls this on hit
          → context.setHitResult(Optional.of(hitResult))
          → resume()
              Level world   = context.level().orElse(null)
              Entity caster = context.entity().orElse(null)
              for each part at index i:
                skip: AbstractAugmentation, disabled parts
                on AbstractAlteration:
                  augments = spell.getAugments(i)       trailing augments
                  stats = SpellStats.builder()
                    .setAugments(augments)
                    .build(part, context)                calls applyModifiers() on each augment
                  context.hitResult().isPresent()
                    → alteration.onResolve(hit, world, caster, stats, context, resolver)
                  else
                    → alteration.onResolveNone(world, caster, stats, context, resolver)
  → if SUCCESS: IWrappedCaster.expendMana(spell.getCost())
```

### IWrappedCaster\<T\>

Connect the resolver to any mana system. `T` is the caster type.

```java
public interface IWrappedCaster<T> {
    T       getCaster();
    boolean enoughMana(int cost);
    void    expendMana(int cost);
    Vec3    getPosition();
}
```

**`PlayerCaster`** — holds a `WeakReference<Player>`, implements `IWrappedCaster<Player>`. Mana checks are stubs until a mana system is implemented.

---

## Building a Spell

```java
// Self-targeting heal with two amplify augments
Spell spell = new Spell(
    SelfPropagation.INSTANCE,       // cost 5
    HealAlteration.INSTANCE,        // cost 15
    AmplifyAugmentation.INSTANCE,   // cost 10  ─┐ modify Heal
    AmplifyAugmentation.INSTANCE    // cost 10  ─┘
);

spell.getCost();           // 40
spell.getPropagation();    // Optional[SelfPropagation]
spell.getAugments(1);      // [Amplify, Amplify]  (trailing after HealAlteration at index 1)
```

`HealAlteration` receives `SpellStats` with `amplification = 4.0f` (2 augments × 2.0f each), healing for `4.0 + 4.0 = 8.0` HP.

---

## Key Packages

```
api/spell/
  AbstractSpellPart.java       Base for all parts; id, translationKey, augment sets, isEnabled()
  CastResolveType.java         SUCCESS / FAILURE / SUCCESS_NO_EXPEND
  Spell.java                   Immutable record; getPropagation(), getAugments(int), getCost()
  SpellResolver.java           Execution engine; onCast(), onResolveEffect(HitResult), resume()
  caster/
    IWrappedCaster.java        Generic mana abstraction: IWrappedCaster<T>
    PlayerCaster.java          IWrappedCaster<Player> via WeakReference; mana stubs
  context/
    SpellContext.java          All-Optional state; hitResult mutable via setHitResult()
  modifier/
    ISpellModifier.java        applyModifiers(Builder, AbstractSpellPart, SpellContext)
    ISpellModifierItem.java    applyItemModifiers(ItemStack, Builder, ...) — for worn items
  part/
    AbstractPropagation.java   typeIndex=0; onCast() abstract; onCastOnBlock/Entity() optional
    AbstractAlteration.java    typeIndex=1; onResolveEntity/Block/None() hooks
    AbstractAugmentation.java  typeIndex=2; applyModifiers() abstract
  stat/
    SpellStats.java            Per-alteration snapshot + Builder (7 float/bool stats)

impl/common/content/spell/
  propagation/
    SelfPropagation.java       Resolves on caster entity; cost 5
  alteration/
    HealAlteration.java        Heals target: 4.0 + amplification HP; ServerLevel guard
    MessageAlteration.java     Sends system message via context.server()
  augmentation/
    AmplifyAugmentation.java   +2.0f amplification per instance; compatible with Acceleration
    AccelerationAugment.java   +2.0f acceleration per instance

impl/common/content/item/
  HealingWandItem.java         [Self + Heal + Amplify + Amplify] on right-click
  MessageWandItem.java         [Self + Message] on right-click; populates server in context
```