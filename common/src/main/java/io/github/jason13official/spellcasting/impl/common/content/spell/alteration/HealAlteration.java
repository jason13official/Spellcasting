package io.github.jason13official.spellcasting.impl.common.content.spell.alteration;

import io.github.jason13official.spellcasting.Spellcasting;
import io.github.jason13official.spellcasting.api.spell.SpellResolver;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.part.AbstractAlteration;
import io.github.jason13official.spellcasting.api.spell.stat.SpellStats;
import java.util.Set;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class HealAlteration extends AbstractAlteration {

  public static final Identifier ID = Spellcasting.id("heal");
  public static final HealAlteration INSTANCE = new HealAlteration();

  public HealAlteration() {
    super(ID);
  }

  @Override
  public void onResolveEntity(EntityHitResult hit, Level world, Entity caster,
      SpellStats stats, SpellContext context, SpellResolver resolver) {
    if (hit.getEntity() instanceof LivingEntity target) {
      target.heal(4.0f + stats.amplification()); // 4.0f == 2 hearts, 1.0f == 0.5 heart
    }
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
  public int getCastingCost() {
    return 15;
  }
}
