package io.github.jason13official.spellcasting.api.spell.caster;

import java.lang.ref.WeakReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PlayerCaster implements IWrappedCaster<Player> {

  private final WeakReference<Player> playerReference;

  public PlayerCaster(Player player) {
    this.playerReference = new WeakReference<>(player);
  }

  public Player player() {
    return playerReference.get();
  }

  @Override
  public Player getCaster() {
    return this.player();
  }

  @Override
  public boolean enoughMana(int cost) {
    return true;
  }

  @Override
  public void expendMana(int cost) {

  }

  @Override
  public Vec3 getPosition() {
    return this.player().position();
  }
}
