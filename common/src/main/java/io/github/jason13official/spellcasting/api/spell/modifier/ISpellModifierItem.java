package io.github.jason13official.spellcasting.api.spell.modifier;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.stat.SpellStats;
import net.minecraft.world.item.ItemStack;

public interface ISpellModifierItem extends ISpellModifier {

  default SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, SpellContext spellContext) {
    return applyModifiers(builder, spellPart, spellContext);
  }
}
