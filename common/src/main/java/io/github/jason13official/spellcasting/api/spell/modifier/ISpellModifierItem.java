package io.github.jason13official.spellcasting.api.spell.modifier;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.stat.SpellEffectStats;
import net.minecraft.world.item.ItemStack;

public interface ISpellModifierItem extends ISpellModifier {

  default SpellEffectStats.Builder applyItemModifiers(ItemStack stack, SpellEffectStats.Builder builder, AbstractSpellPart spellPart, SpellContext spellContext) {
    return applyModifiers(builder, spellPart, spellContext);
  }
}
