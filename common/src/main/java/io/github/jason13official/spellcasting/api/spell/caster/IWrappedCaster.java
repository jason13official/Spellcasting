package io.github.jason13official.spellcasting.api.spell.caster;

import net.minecraft.world.phys.Vec3;

public interface IWrappedCaster<T> {

  T getCaster();

  boolean enoughMana(int cost);

  void expendMana(int cost);

  Vec3 getPosition();
}
