package io.github.jason13official.spellcasting.impl.common.content.spell.augmentation;

import io.github.jason13official.spellcasting.Spellcasting;
import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.part.AbstractAugmentation;
import io.github.jason13official.spellcasting.api.spell.stat.SpellEffectStats.Builder;
import java.util.Set;
import net.minecraft.resources.Identifier;

public class AmplifyAugmentation extends AbstractAugmentation {

  public static final Identifier ID = Spellcasting.id("amplify");
  public static final AmplifyAugmentation INSTANCE = new AmplifyAugmentation();

  public AmplifyAugmentation() {
    super(ID);
  }

  @Override
  public Set<Identifier> getCompatibleAugments() {
    return Set.of(AccelerationAugment.ID);
  }

  @Override
  public Set<Identifier> getIncompatibleAugments() {
    return Set.of();
  }

  @Override
  public Builder applyModifiers(Builder builder, AbstractSpellPart spellPart, SpellContext spellContext) {
    return builder.addAmplificationModifier(2.0f); // in case of healing alteration, currently adds another heart
  }
}
