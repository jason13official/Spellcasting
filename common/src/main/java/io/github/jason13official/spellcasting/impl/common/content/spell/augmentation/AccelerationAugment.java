package io.github.jason13official.spellcasting.impl.common.content.spell.augmentation;

import io.github.jason13official.spellcasting.Spellcasting;
import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.part.AbstractAugmentation;
import io.github.jason13official.spellcasting.api.spell.stat.SpellStats.Builder;
import java.util.Set;
import net.minecraft.resources.Identifier;

public class AccelerationAugment extends AbstractAugmentation {

  public static final Identifier ID = Spellcasting.id("acceleration");
  public static final AccelerationAugment INSTANCE = new AccelerationAugment();

  public AccelerationAugment() {
    super(ID);
  }

  @Override
  public Set<Identifier> getCompatibleAugments() {
    return Set.of();
  }

  @Override
  public Set<Identifier> getIncompatibleAugments() {
    return Set.of();
  }

  @Override
  public Builder applyModifiers(Builder builder, AbstractSpellPart spellPart, SpellContext spellContext) {
    builder.addAmplificationModifier(2.0f);
    return super.applyModifiers(builder, spellPart, spellContext);
  }
}
