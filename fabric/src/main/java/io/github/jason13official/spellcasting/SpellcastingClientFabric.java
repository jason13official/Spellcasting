package io.github.jason13official.spellcasting;

import net.fabricmc.api.ClientModInitializer;

public class SpellcastingClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    SpellcastingClient.init();
  }
}
