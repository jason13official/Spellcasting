package io.github.jason13official.spellcasting.api.spell.context;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("all")
public class SpellContext {

  // Required execution fields
  // private final LivingEntity caster;
  // private final Level castLevel;
  // private final ItemStack casterTool;
  private boolean canceled;
  private SpellContext previousContext;

  // Optional contextual data
  private final Optional<MinecraftServer> server;
  private final Optional<Level> level;
  private final Optional<ChunkAccess> chunk;
  private final Optional<BlockPos> blockPos;
  private final Optional<Vec3> pos;
  private final Optional<Entity> entity;
  private Optional<HitResult> hitResult;

  public SpellContext(
      Optional<MinecraftServer> server,
      Optional<Level> level,
      Optional<ChunkAccess> chunk,
      Optional<BlockPos> blockPos,
      Optional<Vec3> pos,
      Optional<Entity> entity,
      Optional<HitResult> hitResult) {
    this.server = server;
    this.level = level;
    this.chunk = chunk;
    this.blockPos = blockPos;
    this.pos = pos;
    this.entity = entity;
    this.hitResult = hitResult;
  }

  public boolean isCanceled() {
    return canceled;
  }

  public void setCanceled(boolean canceled) {
    this.canceled = canceled;
  }

  public SpellContext getPreviousContext() {
    return previousContext;
  }

  public void setPreviousContext(SpellContext previousContext) {
    this.previousContext = previousContext;
  }

  public Optional<MinecraftServer> server() {
    return server;
  }

  public Optional<Level> level() {
    return level;
  }

  public Optional<ChunkAccess> chunk() {
    return chunk;
  }

  public Optional<BlockPos> blockPos() {
    return blockPos;
  }

  public Optional<Vec3> pos() {
    return pos;
  }

  public Optional<Entity> entity() {
    return entity;
  }

  public Optional<HitResult> hitResult() {
    return hitResult;
  }

  public void setHitResult(Optional<HitResult> hitResult) {
    this.hitResult = hitResult;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (SpellContext) obj;
    return Objects.equals(this.server, that.server) &&
        Objects.equals(this.level, that.level) &&
        Objects.equals(this.chunk, that.chunk) &&
        Objects.equals(this.blockPos, that.blockPos) &&
        Objects.equals(this.pos, that.pos) &&
        Objects.equals(this.entity, that.entity) &&
        Objects.equals(this.hitResult, that.hitResult);
  }

  @Override
  public int hashCode() {
    return Objects.hash(server, level, chunk, blockPos, pos, entity, hitResult);
  }

  @Override
  public String toString() {
    return "SpellContext[" +
        "server=" + server + ", " +
        "level=" + level + ", " +
        "chunk=" + chunk + ", " +
        "blockPos=" + blockPos + ", " +
        "pos=" + pos + ", " +
        "entity=" + entity + ", " +
        "hitResult=" + hitResult + ']';
  }
}
