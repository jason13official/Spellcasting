package io.github.jason13official.spellcasting.api.spell.part;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.CastResolveType;
import io.github.jason13official.spellcasting.api.spell.SpellResolver;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.stat.SpellStats;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public abstract class AbstractPropagation extends AbstractSpellPart {

  public AbstractPropagation(String internalIdPath) {
    super(internalIdPath);
  }

  public AbstractPropagation(Identifier id) {
    super(id);
  }

  @Override
  public int typeIndex() {
    return 0;
  }

  @Override
  public int getCastingCost() {
    return 10;
  }

  public abstract CastResolveType onCast(SpellStats stats, SpellContext context, SpellResolver resolver);

  public CastResolveType onCastOnBlock(BlockHitResult hit, SpellStats stats, SpellContext context, SpellResolver resolver) {
    return CastResolveType.FAILURE;
  }

  public CastResolveType onCastOnEntity(EntityHitResult hit, SpellStats stats, SpellContext context, SpellResolver resolver) {
    return CastResolveType.FAILURE;
  }
}
