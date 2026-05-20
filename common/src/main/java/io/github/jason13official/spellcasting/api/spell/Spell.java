package io.github.jason13official.spellcasting.api.spell;

import java.util.Arrays;
import java.util.List;

public record Spell(List<AbstractSpellPart> definition) {

  public Spell(AbstractSpellPart... spellParts) {
    this(Arrays.asList(spellParts));
  }
}
