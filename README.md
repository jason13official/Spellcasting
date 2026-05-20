# Spellcasting (Abstract Outline/Lore Dump)

*authored by [jason13](https://jason13official.github.io/)*

---

Spellcasting is a library mod and engine for resolving abstract and data-driven spells in Minecraft.

*Inspired by mods such as:*
- [Ars Magicka 2](https://www.curseforge.com/minecraft/mc-mods/ars-magica-2),
- [Thaumcraft](https://www.curseforge.com/minecraft/mc-mods/thaumcraft),
- [EvilCraft](https://www.curseforge.com/minecraft/mc-mods/evilcraft),
- [Blood Magic](https://www.curseforge.com/minecraft/mc-mods/blood-magic),
- [Ars Nouveau](https://www.curseforge.com/minecraft/mc-mods/ars-nouveau),
- [Electroblob's Wizardy](https://www.curseforge.com/minecraft/mc-mods/electroblobs-wizardry),
- [Witchery](https://www.curseforge.com/minecraft/mc-mods/witchery),
- [Iron's Spells 'n Spellbooks](https://www.curseforge.com/minecraft/mc-mods/irons-spells-n-spellbooks),
- [Mahou Tsukai](https://www.curseforge.com/minecraft/mc-mods/mahou-tsukai)
- [Botania](https://www.curseforge.com/minecraft/mc-mods/botania)
- [Mystical Agriculture](https://www.curseforge.com/minecraft/mc-mods/mystical-agriculture)
- [Apotheosis](https://www.curseforge.com/minecraft/mc-mods/apotheosis)
- and more.

---

### Note

**To Modpack Developers, Server Owners, etc.:** 

While this mod does add some content, a configuration file is generated the first time you run the game with this mod installed.

You SHOULD be editing this; the values are provided for you to customize the intended experience for you and your players.

**To Addon/Mod Developers:** Please let me know if you think something should change! I encourage [opening a new issue](https://github.com/jason13official/Spellcasting/issues)!

---

### What is a spell?

Quoting [the 2014 rules of D&d](https://www.dndbeyond.com/sources/dnd/basic-rules-2014/spellcasting), a spell is "*a discrete magical effect, a single shaping of the magical energies that suffuse the multiverse into a specific, limited expression.*"

In terms of Minecraft, a "spell" is *magic having an effect on the world*; a formula for taking raw energy and telling it how to form, where to go, and what to do.

This can be observed in many fashions; some spells may be cast when a player flicks their wand or reads from their grimoire, others might be triggered reactively when an entity activates a ward, maybe cast repeatedly from a spell turret, or invoked from a successful ritual.

Spells answer questions:

- Shape: how does the spell propagate through existence?
  - Examples: Projectile, Self, Touch, Pantomime, Underfoot
- Effect: how does the spell change the world?
  - Examples: Transformation, Force, Life/Death, Space, Information, Binding
- Cost: how do you make it obey?
  - Examples: Environment (Aura, Nodes), Reservoir (Blood network, Source jar), Self (health points)

---

**[How are spells cast?](./README_DESIGN.md)**

[//]: # (### How are spells cast?)

[//]: # ()
[//]: # (This depends on the spell, some are too powerful for mortal bodies to conduct, and must be passed through materials found in the world or specially crafted to conduct the spell.)

[//]: # ()
[//]: # (Depending on the target of the spell &#40;Realm/Server, Dimension/Level, Chunk, Block, Entity&#41;, the cost of an individual spell could use enough energy to burn a caster alive.)

[//]: # ()
[//]: # (With that being said, most spells are invoked via items, at a cost to the player.)
