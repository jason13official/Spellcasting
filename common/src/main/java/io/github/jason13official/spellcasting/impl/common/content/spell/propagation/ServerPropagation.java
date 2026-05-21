package io.github.jason13official.spellcasting.impl.common.content.spell.propagation;

import io.github.jason13official.spellcasting.Spellcasting;
import io.github.jason13official.spellcasting.api.spell.CastResolveType;
import io.github.jason13official.spellcasting.api.spell.SpellResolver;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.part.AbstractPropagation;
import io.github.jason13official.spellcasting.api.spell.stat.SpellStats;
import java.util.Set;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.EntityHitResult;

public class ServerPropagation extends AbstractPropagation {

  public static final Identifier ID = Spellcasting.id("server");
  public static final ServerPropagation INSTANCE = new ServerPropagation();

  public ServerPropagation() {
    super(ID);
  }

  @Override
  public CastResolveType onCast(SpellStats stats, SpellContext context, SpellResolver resolver) {
    resolver.onResolveEffect();
    return CastResolveType.SUCCESS;
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
    return 5;
  }
}
