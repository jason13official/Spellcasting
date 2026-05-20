package io.github.jason13official.spellcasting.api.spell;

import io.github.jason13official.spellcasting.Spellcasting;
import io.netty.util.internal.ConcurrentSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public abstract class AbstractSpellPart implements Comparable<AbstractSpellPart> {

  private final Identifier id;
  private final String translationKey;

  /// {@link ConcurrentSet} from io.netty.util.internal is deprecated and recommends
  /// using {@link ConcurrentHashMap#newKeySet()} instead
  private final Set<Identifier> compatibleAugmentIdSet = ConcurrentHashMap.newKeySet();

  private final Set<Identifier> incompatibleAugmentIdSet = ConcurrentHashMap.newKeySet();

  /// shorthand for registering internal
  public AbstractSpellPart(String internalIdPath) {
    this(Spellcasting.id(internalIdPath));
  }

  public AbstractSpellPart(Identifier id) {
    this.id = id;
    this.translationKey = translationKeyFromId(id);

    this.compatibleAugmentIdSet.addAll(this.getCompatibleAugments());
    this.incompatibleAugmentIdSet.addAll(this.getIncompatibleAugments());
  }

  /// augments must define which augments they are compatible with
  public abstract Set<Identifier> getCompatibleAugments();

  /// augments must define which augments they are incompatible with
  public abstract Set<Identifier> getIncompatibleAugments();

  public abstract int getCastingCost();

  public abstract int typeIndex();

  public boolean isEnabled() {
    return true;
  }

  public Identifier id() {

    return this.id;
  }

  public Identifier getId() {

    return this.id;
  }

  public String getTranslationKey() {

    return this.translationKey;
  }

  @Override
  public int compareTo(@NonNull AbstractSpellPart o) {

    return 0; // TODO compare tier of spell parts? for higher level ordering for client displays
  }

  /// ie. "spell_part.spellcasting.projectile"
  public static String translationKeyFromId(Identifier id) {

    return "spell_part" + "." + id.getNamespace() + "." + id.getPath();
  }

  public static Set<Identifier> createAugmentSet(Identifier... ids) {
    return Set.of(ids);
  }
}
