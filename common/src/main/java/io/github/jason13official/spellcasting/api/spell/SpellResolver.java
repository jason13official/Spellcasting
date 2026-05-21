package io.github.jason13official.spellcasting.api.spell;

import io.github.jason13official.spellcasting.api.spell.caster.IWrappedCaster;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.part.AbstractAlteration;
import io.github.jason13official.spellcasting.api.spell.part.AbstractAugmentation;
import io.github.jason13official.spellcasting.api.spell.part.AbstractPropagation;
import io.github.jason13official.spellcasting.api.spell.stat.SpellStats;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class SpellResolver {

  private final Spell spell;
  private final SpellContext context;
  private final IWrappedCaster<?> wrappedCaster;

  public SpellResolver(Spell spell, SpellContext context, IWrappedCaster<?> wrappedCaster) {
    this.spell = spell;
    this.context = context;
    this.wrappedCaster = wrappedCaster;
  }

  public CastResolveType onCast() {
    if (!canCast()) return CastResolveType.FAILURE;
    Optional<AbstractPropagation> prop = spell.getPropagation();
    if (prop.isEmpty()) return CastResolveType.FAILURE;
    SpellStats stats = SpellStats.builder().build(prop.get(), context);
    CastResolveType result = prop.get().onCast(stats, context, this);
    if (result == CastResolveType.SUCCESS) wrappedCaster.expendMana(spell.getCost());
    return result;
  }

  public void onResolveEffect(HitResult hitResult) {
    context.setHitResult(Optional.of(hitResult));
    this.resume();
  }

  public void onResolveEffect() {
    this.resume();
  }

  private boolean canCast() {
    return wrappedCaster.enoughMana(spell.getCost());
  }

  private void resume() {
    Level world = context.level().orElse(null);
    Entity caster = context.entity().orElse(null);
    List<AbstractSpellPart> parts = spell.definition();
    for (int i = 0; i < parts.size(); i++) {
      if (context.isCanceled()) break;
      AbstractSpellPart part = parts.get(i);
      if (!part.isEnabled() || part instanceof AbstractAugmentation) continue;
      if (part instanceof AbstractAlteration alteration) {
        List<AbstractAugmentation> augments = spell.getAugments(i);
        SpellStats stats = SpellStats.builder()
            .setAugments(augments)
            .build(alteration, context);

        Optional<HitResult> hitResult = context.hitResult();
        if (hitResult.isPresent()) {
          alteration.onResolve(hitResult.get(), world, caster, stats, context, this);
        } else {
          alteration.onResolveNone(world, caster, stats, context, this);
        }
      }
    }
  }

  public Spell getSpell() {
    return spell;
  }

  public SpellContext getContext() {
    return context;
  }

  public IWrappedCaster<?> getWrappedCaster() {
    return wrappedCaster;
  }
}
