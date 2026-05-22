### What is a spell?

- An abstract change to a Minecraft server.
- A collection of spell parts to determine the propagation, alterations, and augmentations of an abstract change to a Minecraft server.

### How is a spell cast?

- When a specific mechanism to initiate the casting of a spell is successfully activated, the spell is valid, and the spell does not fizzle instantly.

---

### How is a spell composed?

- ISpellPart
- ISpellPropagation
- ISpellAlteration
- ISpellAugmentation

Ars Nouveau has these as:

- AbstractSpellPart
- AbstractCastMethod
- AbstractEffect
- AbstractAugment

Thaumcraft 6 has these as :

- FocusNode
- FocusMedium
- FocusEffect
- FocusMod

*ISpellPart/AbstractSpellPart/FocusNode*:

- Generally define a method for getting a unique identifier
- Generally defines a method for getting the "type" (medium, effect, modifier)
- May define compatible modifiers/augments
- May define invalid parts
- May define the "tier" of the effect (apprentice, mage, archmage, etc.)
- May return a default item instance (ars nouveau glyph items)
- May define a or many school's of magic that this part belongs to
- May define a translation key for name and description
- May be disabled based on config
- May define limits for other parts/augments to this part
- May define their own icon

*ISpellPropagation/AbstractCastMethod/FocusMedium*:

- Generally defines an execution method / on cast method(s) for creating an entity to propagate the spell or applying the alteration of a spell directly to a block/entity/other target, 

*ISpellAlteration/AbstractEffect/FocusEffect*: 

- Generally defines a method to apply the augmented alteration of a spell to an entity/block/target.

*ISpellAugmentation/AbstractAugment/FocusMod*:

- Generally defines a method to apply given spell part statistics to a spell alteration or propagation












