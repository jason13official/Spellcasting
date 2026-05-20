package io.github.jason13official.spellcasting.api.spell.context;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("all")
public final class SpellContext {

  private final Optional<MinecraftServer> server;
  private final Optional<Level> level;
  private final Optional<ChunkAccess> chunk;
  private final Optional<BlockPos> blockPos;
  private final Optional<Vec3> pos;
  private final Optional<Entity> entity;

  public SpellContext(Optional<MinecraftServer> server,
      Optional<Level> level,
      Optional<ChunkAccess> chunk,
      Optional<BlockPos> blockPos,
      Optional<Vec3> pos,
      Optional<Entity> entity) {
    this.server = server;
    this.level = level;
    this.chunk = chunk;
    this.blockPos = blockPos;
    this.pos = pos;
    this.entity = entity;
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
        Objects.equals(this.entity, that.entity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(server, level, chunk, blockPos, pos, entity);
  }

  @Override
  public String toString() {
    return "SpellContext[" +
        "server=" + server + ", " +
        "level=" + level + ", " +
        "chunk=" + chunk + ", " +
        "blockPos=" + blockPos + ", " +
        "pos=" + pos + ", " +
        "entity=" + entity + ']';
  }
}
