package io.github.jason13official.spellcasting.impl.common.content.spell.caster;

import io.github.jason13official.spellcasting.api.spell.caster.IWrappedCaster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PlayerCaster implements IWrappedCaster<Player> {

  private final Player player;

  public PlayerCaster(Player player) {
    this.player = player;
  }

  @Override
  public Player getCaster() {
    return player;
  }

  @Override
  public boolean enoughMana(int cost) {
    return true; // TODO: implement mana system
  }

  @Override
  public void expendMana(int cost) {
    // TODO: implement mana system
  }

  @Override
  public Vec3 getPosition() {
    return player.position();
  }
}
