package io.github.jason13official.spellcasting.api.data;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;

/// not every spell knows who/what cast it; not every spell has a position,
/// or has an effect on an entity/level. Spell components should
/// declare what they need and "fizzle out" gracefully
public record SpellContext(Optional<MinecraftServer> server,
                           Optional<ServerLevel> level,
                           Optional<ChunkAccess> chunkAccess,
                           Optional<BlockPos> blockPosition,
                           Optional<Vec3> rawPosition,
                           Optional<Entity> caster) {

  public boolean hasEntity() {

    return caster.isPresent();
  }

  public boolean hasLevel() {

    return level.isPresent();
  }

  public boolean hasPosition() {

    return rawPosition.isPresent() || blockPosition.isPresent();
  }

  public Vec3 resolvedPosition() {

    return rawPosition
        .or(() -> blockPosition.map(Vec3::atCenterOf)).orElse(null);
  }

}
