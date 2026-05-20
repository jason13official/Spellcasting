package io.github.jason13official.spellcasting.api.spell.part;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import net.minecraft.resources.Identifier;

public abstract class AbstractPropagation extends AbstractSpellPart {

  public AbstractPropagation(String internalIdPath) {
    super(internalIdPath);
  }

  public AbstractPropagation(Identifier id) {
    super(id);
  }

  @Override
  public int getCastingCost() {
    return 10;
  }
}
