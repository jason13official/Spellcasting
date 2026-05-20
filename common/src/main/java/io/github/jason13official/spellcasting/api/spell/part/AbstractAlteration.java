package io.github.jason13official.spellcasting.api.spell.part;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.SpellResolver;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.stat.SpellStats;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAlteration extends AbstractSpellPart {

  public AbstractAlteration(String internalIdPath) {
    super(internalIdPath);
  }

  public AbstractAlteration(Identifier id) {
    super(id);
  }

  @Override
  public int typeIndex() {
    return 1;
  }

  @Override
  public int getCastingCost() {
    return 10;
  }

  public void onResolve(HitResult hitResult, Level world, Entity caster,
      SpellStats stats, SpellContext context, SpellResolver resolver) {
    if (hitResult instanceof BlockHitResult b) {
      onResolveBlock(b, world, caster, stats, context, resolver);
    } else if (hitResult instanceof EntityHitResult e) {
      onResolveEntity(e, world, caster, stats, context, resolver);
    }
  }

  public void onResolveEntity(EntityHitResult rayTraceResult, Level world, Entity shooter,
      SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
  }

  public void onResolveBlock(BlockHitResult rayTraceResult, Level world, Entity shooter,
      SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
  }
}
