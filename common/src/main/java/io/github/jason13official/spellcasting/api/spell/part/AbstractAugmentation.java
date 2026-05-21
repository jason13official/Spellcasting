package io.github.jason13official.spellcasting.api.spell.part;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.modifier.ISpellModifier;
import io.github.jason13official.spellcasting.api.spell.stat.SpellEffectStats.Builder;
import net.minecraft.resources.Identifier;

public abstract class AbstractAugmentation extends AbstractSpellPart implements ISpellModifier {

  public AbstractAugmentation(String internalIdPath) {
    super(internalIdPath);
  }

  public AbstractAugmentation(Identifier id) {
    super(id);
  }

  @Override
  public int typeIndex() {
    return 2;
  }

  @Override
  public int getCastingCost() {
    return 10;
  }

  @Override
  public abstract Builder applyModifiers(Builder builder, AbstractSpellPart spellPart, SpellContext spellContext);
}
