package io.github.jason13official.spellcasting.impl.common.registry;

import io.github.jason13official.spellcasting.Spellcasting;
import io.github.jason13official.spellcasting.impl.common.content.item.WandItem;
import java.util.function.BiConsumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class ModItems {

  public static Item HEALING_WAND;

  public static void register(BiConsumer<Item, Identifier> consumer) {

    HEALING_WAND = new WandItem(new Properties().setId(key(Spellcasting.id("healing_wand"))));
    consumer.accept(HEALING_WAND, Spellcasting.id("healing_wand"));
  }

  private static ResourceKey<Item> key(Identifier id) {
    return ResourceKey.create(Registries.ITEM, id);
  }
}
