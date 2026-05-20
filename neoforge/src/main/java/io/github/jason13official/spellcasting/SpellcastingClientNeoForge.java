package io.github.jason13official.spellcasting;

import java.util.function.Consumer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class SpellcastingClientNeoForge {

  public SpellcastingClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> SpellcastingClient.init());
  }
}
