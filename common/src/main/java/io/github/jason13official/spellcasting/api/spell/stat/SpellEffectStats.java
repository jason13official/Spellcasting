package io.github.jason13official.spellcasting.api.spell.stat;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;
import io.github.jason13official.spellcasting.api.spell.part.AbstractAugmentation;
import java.util.List;

public final class SpellEffectStats {

  private int level;
  private float amplification;
  private float aoe;
  private float acceleration;
  private float duration;
  private float damage;
  private boolean sensitive;
  private List<AbstractAugmentation> augments;

  public SpellEffectStats() {
  }

  public int level() {
    return level;
  }

  public float amplification() {
    return amplification;
  }

  public float aoe() {
    return aoe;
  }

  public float acceleration() {
    return acceleration;
  }

  public float duration() {
    return duration;
  }

  public float damage() {
    return damage;
  }

  public boolean isSensitive() {
    return sensitive;
  }

  public List<AbstractAugmentation> augments() {
    return augments != null ? augments : List.of();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private SpellEffectStats stats;

    public Builder() {
      this.stats = new SpellEffectStats();
    }

    public SpellEffectStats build(AbstractSpellPart part, SpellContext context) {
      if (stats.augments != null) {
        for (AbstractAugmentation aug : stats.augments) {
          aug.applyModifiers(this, part, context);
        }
      }
      return stats;
    }

    public Builder setLevel(int level) {
      stats.level = level;
      return this;
    }

    public Builder setAmplification(float amplification) {
      stats.amplification = amplification;
      return this;
    }

    public Builder addAmplificationModifier(float amount) {
      stats.amplification += amount;
      return this;
    }

    public Builder addAoeModifier(float amount) {
      stats.aoe += amount;
      return this;
    }

    public Builder addAccelerationModifier(float amount) {
      stats.acceleration += amount;
      return this;
    }

    public Builder addDurationModifier(float amount) {
      stats.duration += amount;
      return this;
    }

    public Builder addDamageModifier(float amount) {
      stats.damage += amount;
      return this;
    }

    public Builder setSensitive(boolean sensitive) {
      stats.sensitive = sensitive;
      return this;
    }

    public Builder setAugments(List<AbstractAugmentation> augments) {
      stats.augments = augments;
      return this;
    }
  }
}
