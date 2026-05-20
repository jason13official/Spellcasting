package io.github.jason13official.spellcasting.impl.common.content.spell.alteration;

import io.github.jason13official.spellcasting.Spellcasting;
import io.github.jason13official.spellcasting.api.spell.SpellResolver;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.part.AbstractAlteration;
import io.github.jason13official.spellcasting.api.spell.stat.SpellStats;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class MessageAlteration extends AbstractAlteration {

  public static final Identifier ID = Spellcasting.id("message");
  public static final MessageAlteration INSTANCE = new MessageAlteration();

  public MessageAlteration() {
    super(ID);
  }

  @Override
  public void onResolveEntity(EntityHitResult hit, Level world, Entity caster,
      SpellStats stats, SpellContext context, SpellResolver resolver) {
    if (context.server().isPresent()) {
      MinecraftServer server = context.server().get();
      server.sendSystemMessage(Component.literal("Logged info to server!"));
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
