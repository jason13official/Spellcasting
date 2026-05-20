package io.github.jason13official.spellcasting.api.spell.part;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import net.minecraft.resources.Identifier;

public abstract class AbstractAlteration extends AbstractSpellPart {

  public AbstractAlteration(String internalIdPath) {
    super(internalIdPath);
  }

  public AbstractAlteration(Identifier id) {
    super(id);
  }

  @Override
  public int getCastingCost() {
    return 10;
  }
}
