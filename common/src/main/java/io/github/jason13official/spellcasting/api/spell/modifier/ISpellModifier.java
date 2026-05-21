package io.github.jason13official.spellcasting.api.spell.modifier;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.stat.SpellEffectStats;

public interface ISpellModifier {

  default SpellEffectStats.Builder applyModifiers(SpellEffectStats.Builder builder, AbstractSpellPart spellPart, SpellContext spellContext) {
    return builder;
  }
}
