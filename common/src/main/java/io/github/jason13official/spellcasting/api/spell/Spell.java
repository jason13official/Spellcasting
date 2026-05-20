package io.github.jason13official.spellcasting.api.spell;

import io.github.jason13official.spellcasting.api.spell.part.AbstractAugmentation;
import io.github.jason13official.spellcasting.api.spell.part.AbstractPropagation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record Spell(List<AbstractSpellPart> definition) {

  public Spell(AbstractSpellPart... spellParts) {
    this(Arrays.asList(spellParts));
  }

  public Optional<AbstractPropagation> getPropagation() {
    return definition.stream()
        .filter(p -> p instanceof AbstractPropagation)
        .map(p -> (AbstractPropagation) p)
        .findFirst();
  }

  public List<AbstractAugmentation> getAugments(int effectIndex) {
    List<AbstractAugmentation> result = new ArrayList<>();
    for (int i = effectIndex + 1; i < definition.size(); i++) {
      if (definition.get(i) instanceof AbstractAugmentation aug) {
        result.add(aug);
      } else {
        break;
      }
    }
    return result;
  }

  public int getCost() {
    return definition.stream().mapToInt(AbstractSpellPart::getCastingCost).sum();
  }
}
