package io.github.jason13official.spellcasting.api.spell.stat;

import io.github.jason13official.spellcasting.api.spell.AbstractSpellPart;
import io.github.jason13official.spellcasting.api.spell.context.SpellContext;

public final class SpellStats {

  private int level;
  private float amplification;

  public SpellStats() {
  }

  public int level() {
    return level;
  }

  public float amplification() {
    return amplification;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private SpellStats stats;

    public Builder() {
      this.stats = new SpellStats();
    }

    public SpellStats build(AbstractSpellPart part, SpellContext context) {
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

    public Builder addAmplificationModifier(float acceleration) {
      stats.amplification += acceleration;
      return this;
    }
  }
}
