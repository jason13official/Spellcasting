package io.github.jason13official.spellcasting;

import net.minecraft.resources.Identifier;

public class Spellcasting {

  public static void init() {
  }

  public static Identifier identifier(final String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}