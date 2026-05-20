package io.github.jason13official.spellcasting.api.spell.modifier;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.stat.SpellStats;

public interface ISpellModifier {

  default SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart, SpellContext spellContext) {
    return builder;
  }
}
