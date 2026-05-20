package io.github.jason13official.spellcasting.api;

import io.github.jason13official.spellcasting.api.data.SpellContext;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;

public class SpellContextHelper {

  public static SpellContext ofEntity(Entity entity) {
    return new SpellContext(
        Optional.ofNullable(entity.level().getServer()),
        Optional.of((ServerLevel) entity.level()),
        Optional.of(entity.level().getChunk(entity.blockPosition())),
        Optional.of(entity.blockPosition()),
        Optional.of(entity.position()),
        Optional.of(entity)
    );
  }

  public static SpellContext ofBlock(ServerLevel level, BlockPos pos) {
    return new SpellContext(
        Optional.of(level.getServer()),
        Optional.of(level),
        Optional.of(level.getChunk(pos)),
        Optional.of(pos),
        Optional.of(Vec3.atCenterOf(pos)),
        Optional.empty()
    );
  }

  public static SpellContext ofServer(MinecraftServer server) {
    return new SpellContext(
        Optional.of(server),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty()
    );
  }

  public static SpellContext forServer(MinecraftServer server) {

    return new SpellContext(Optional.of(server), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
  }

  public static SpellContext forServerLevel(MinecraftServer server, ServerLevel level) {

    return new SpellContext(Optional.of(server), Optional.of(level), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
  }

  public static SpellContext forChunk(MinecraftServer server, ServerLevel level, ChunkAccess chunkAccess) {

    return new SpellContext(Optional.of(server), Optional.of(level), Optional.of(chunkAccess), Optional.empty(), Optional.empty(), Optional.empty());
  }

  public static SpellContext forBlock(MinecraftServer server, ServerLevel level, ChunkAccess chunkAccess, BlockPos blockPosition) {

    return new SpellContext(Optional.of(server), Optional.of(level), Optional.of(chunkAccess), Optional.of(blockPosition), Optional.empty(), Optional.empty());
  }

  public static SpellContext forPosition(MinecraftServer server, ServerLevel level, ChunkAccess chunkAccess, BlockPos blockPosition, Vec3 position) {

    return new SpellContext(Optional.of(server), Optional.of(level), Optional.of(chunkAccess), Optional.of(blockPosition), Optional.of(position), Optional.empty());
  }

  public static SpellContext forEntity(MinecraftServer server, ServerLevel level, ChunkAccess chunkAccess, BlockPos blockPosition, Vec3 position, Entity entity) {

    return new SpellContext(Optional.of(server), Optional.of(level), Optional.of(chunkAccess), Optional.of(blockPosition), Optional.of(position), Optional.of(entity));
  }
}
