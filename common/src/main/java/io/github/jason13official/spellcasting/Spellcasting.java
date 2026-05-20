package io.github.jason13official.spellcasting;

import net.minecraft.resources.Identifier;

public class Spellcasting {

  public static void init() {
  }

  public static Identifier id(final String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}