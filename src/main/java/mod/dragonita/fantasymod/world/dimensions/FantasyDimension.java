package mod.dragonita.fantasymod.world.dimensions;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FantasyDimension extends Dimension {
	
	public FantasyDimension(World worldIn, DimensionType typeIn) {
		super(worldIn, typeIn, 0.0F);
	}
	
	@Override
	public ChunkGenerator<?> createChunkGenerator() {
		return new FantasyChunkGenerator(world, new FantasyBiomeProvider(), new FantasyGenSettings());
	}
	
	   @Nullable
	   public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid) {
	      for(int i = chunkPosIn.getXStart(); i <= chunkPosIn.getXEnd(); ++i) {
	         for(int j = chunkPosIn.getZStart(); j <= chunkPosIn.getZEnd(); ++j) {
	            BlockPos blockpos = this.findSpawn(i, j, checkValid);
	            if (blockpos != null) {
	               return blockpos;
	            }
	         }
	      }

	      return null;
	   }

	   @Nullable
	   public BlockPos findSpawn(int posX, int posZ, boolean checkValid) {
	      BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(posX, 0, posZ);
	      Biome biome = this.world.getBiome(blockpos$mutable);
	      BlockState blockstate = biome.getSurfaceBuilderConfig().getTop();
	      if (checkValid && !blockstate.getBlock().isIn(BlockTags.VALID_SPAWN)) {
	         return null;
	      } else {
	         Chunk chunk = this.world.getChunk(posX >> 4, posZ >> 4);
	         int i = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, posX & 15, posZ & 15);
	         if (i < 0) {
	            return null;
	         } else if (chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, posX & 15, posZ & 15) > chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR, posX & 15, posZ & 15)) {
	            return null;
	         } else {
	            for(int j = i + 1; j >= 0; --j) {
	               blockpos$mutable.setPos(posX, j, posZ);
	               BlockState blockstate1 = this.world.getBlockState(blockpos$mutable);
	               if (!blockstate1.getFluidState().isEmpty()) {
	                  break;
	               }

	               if (blockstate1.equals(blockstate)) {
	                  return blockpos$mutable.up().toImmutable();
	               }
	            }

	            return null;
	         }
	      }
	   }
	
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		double d0 = MathHelper.frac((double)worldTime / 24000.0D - 0.25D);
		double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
		return (float)(d0 * 2.0D + d1) / 3.0F;
	}
	
	@Override
	public boolean isSurfaceWorld() {
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	public Vec3d getFogColor(float celestialAngle, float partialTicks) {
		float f = MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
	    float f1 = 0.7529412F;
	    float f2 = 0.84705883F;
	    float f3 = 1.0F;
	    f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
	    f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3d((double)f1, (double)f2, (double)f3);
	}
	
	@Override
	public boolean canRespawnHere() {
		return true;
	}
	
	@Override
	public boolean doesXZShowFog(int x, int z) {
		return false;
	}
	
	@Override
	public SleepResult canSleepAt(PlayerEntity player, BlockPos pos) {
		return SleepResult.ALLOW;
	}
	
	@Override
	public int getActualHeight() {
		return 10;
	}
}