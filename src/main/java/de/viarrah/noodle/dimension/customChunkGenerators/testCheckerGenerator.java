package de.viarrah.noodle.dimension.customChunkGenerators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class testCheckerGenerator extends ChunkGenerator {
    public static final MapCodec<testCheckerGenerator> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    BiomeSource.CODEC
                            .fieldOf("biome_source")
                            .forGetter(testCheckerGenerator::getBiomeSource)
            ).apply(instance, testCheckerGenerator::new));

    //biome source from dimension JSON

    public testCheckerGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess) {
        var chunkPos = chunkAccess.getPos();

        boolean white = Math.floorMod(chunkPos.x + chunkPos.z, 2) == 0;
        BlockState floor = white
                ? Blocks.WHITE_CONCRETE.defaultBlockState()
                : Blocks.BLACK_CONCRETE.defaultBlockState();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        // fill 16x16 blocks belonging to this chunk at Y=0
        for (int localX = 0; localX < 16; localX++)
            for (int localZ = 0; localZ < 16; localZ++) {
                pos.set(
                        chunkPos.getMinBlockX() + localX,
                        0,
                        chunkPos.getMinBlockZ() + localZ
                );

                chunkAccess.setBlockState(pos, floor, false);
            }
        return CompletableFuture.completedFuture(chunkAccess);
    }

    // no caves or carving
    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long l, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {
    }

    // no biome decorations.
    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
    }

    // floor is written directly in fillFromNoise
    @Override
    public void buildSurface(WorldGenRegion worldGenRegion, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {
    }

    // minimum and maximum Y coords
    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getGenDepth() {
        return 256;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    // floor at Y=0, first block above = 1
    @Override
    public int getBaseHeight(int i, int i1, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        return 1;
    }

    @Override
    public NoiseColumn getBaseColumn(int i, int i1, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        BlockState[] states = new BlockState[levelHeightAccessor.getHeight()];
        Arrays.fill(states, Blocks.AIR.defaultBlockState());

        int floorIndex = -levelHeightAccessor.getMinBuildHeight();

        int chunkX = Math.floorDiv(i, 16);
        int chunkZ = Math.floorDiv(i1, 16);
        boolean white = Math.floorMod(chunkX + chunkZ, 2) == 0;

        states[floorIndex] = white
                ? Blocks.WHITE_CONCRETE.defaultBlockState()
                : Blocks.BLACK_CONCRETE.defaultBlockState();

        return new NoiseColumn(levelHeightAccessor.getMinBuildHeight(), states);
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos blockPos) {
    }
}