package io.github.jason13official.spellcasting.api.spell;

import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.stat.SpellStats;

public class SpellResolver {

  public SpellStats stats;
  public SpellContext context;
  public Spell spell;

  public SpellResolver(SpellStats stats, SpellContext context, Spell spell) {
    this.stats = stats;
    this.context = context;
    this.spell = spell;
  }
}
