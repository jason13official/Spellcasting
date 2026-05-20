package io.github.jason13official.spellcasting.impl.common.content.item;

import io.github.jason13official.spellcasting.Constants;
import io.github.jason13official.spellcasting.api.spell.CastResolveType;
import io.github.jason13official.spellcasting.api.spell.Spell;
import io.github.jason13official.spellcasting.api.spell.SpellResolver;
import io.github.jason13official.spellcasting.api.spell.caster.IWrappedCaster;
import io.github.jason13official.spellcasting.api.spell.caster.PlayerCaster;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.impl.common.content.spell.alteration.HealAlteration;
import io.github.jason13official.spellcasting.impl.common.content.spell.augmentation.AmplifyAugmentation;
import io.github.jason13official.spellcasting.impl.common.content.spell.propagation.SelfPropagation;
import java.util.Optional;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class WandItem extends Item {

  public WandItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(Level level, Player player, InteractionHand hand) {

    IWrappedCaster<Player> caster = new PlayerCaster(player);
    // level, blockPos, pos, player
    SpellContext context = new SpellContext(Optional.empty(), Optional.of(level), Optional.empty(), Optional.of(player.blockPosition()), Optional.of(player.position()), Optional.of(player), Optional.empty());

    Spell spell = new Spell(SelfPropagation.INSTANCE, HealAlteration.INSTANCE, AmplifyAugmentation.INSTANCE, AmplifyAugmentation.INSTANCE);

    SpellResolver resolver = new SpellResolver(spell, context, caster);

    if (resolver.onCast(level) != CastResolveType.FAILURE) {
      return InteractionResult.SUCCESS;
    } else {
      Constants.LOG.info("Failed to cast!");
    }

    return InteractionResult.FAIL;
  }
}
