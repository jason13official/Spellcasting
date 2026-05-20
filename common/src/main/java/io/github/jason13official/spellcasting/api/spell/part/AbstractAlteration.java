package io.github.jason13official.spellcasting.api.spell.part;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.Spell;
import io.github.jason13official.spellcasting.api.spell.SpellResolver;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.stat.SpellStats;
import java.util.Optional;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

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

  /// inherently flawed; can only resolve spells for instances of LivingEntity
  public void onResolve(SpellStats stats, SpellContext context, Spell spell) {

    SpellResolver resolver = new SpellResolver(stats, context, spell);

    if (allPresent(context.hitResult(), context.level(), context.entity())) {

      if (!(context.entity().get() instanceof LivingEntity living)) {
        return;
      }

      if (context.hitResult().get() instanceof BlockHitResult blockHitResult) {
        onResolveBlock(blockHitResult, context.level().get(), living, stats, context, resolver);
      }
      else if (context.hitResult().get() instanceof EntityHitResult entityHitResult) {
        onResolveEntity(entityHitResult, context.level().get(), living, stats, context, resolver);
      }
    }
  }

  public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
  }

  public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
  }

  private static boolean allPresent(Optional<?>... optionals) {

    for (Optional<?> optional : optionals) {
      if (optional.isEmpty()) return false;
    }

    return true;
  }
}
