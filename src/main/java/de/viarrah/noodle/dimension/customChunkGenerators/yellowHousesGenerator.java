package de.viarrah.noodle.dimension.customChunkGenerators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class yellowHousesGenerator extends ChunkGenerator {

    public static final MapCodec<yellowHousesGenerator> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    BiomeSource.CODEC
                            .fieldOf("biome_source")
                            .forGetter(yellowHousesGenerator::getBiomeSource)
            ).apply(instance, yellowHousesGenerator::new));

    public yellowHousesGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long l, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {

    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess) {

    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {

    }

    @Override
    public int getGenDepth() {
        return 256;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess) {
        return CompletableFuture.completedFuture(chunkAccess);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos blockPos) {

    }

    @Override
    public int getBaseHeight(int i, int i1, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        return levelHeightAccessor.getMinBuildHeight();
    }

    @Override
    public NoiseColumn getBaseColumn(int i, int i1, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        BlockState[] states = new BlockState[levelHeightAccessor.getHeight()];
        Arrays.fill(states, Blocks.AIR.defaultBlockState());

        return new NoiseColumn(levelHeightAccessor.getMinBuildHeight(), states);
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
        if (!(level instanceof WorldGenRegion region)) {
            return;
        }

        var chunkPos = chunk.getPos();

        int gridX = Math.floorMod(chunkPos.x, 16);
        int gridZ = Math.floorMod(chunkPos.z, 16);

        String templatePath;

        if (gridX == 0 && gridZ == 0) {
            templatePath = "yellowhouses/yellowhouses_intersection";
        } else if (gridZ == 0 ) {
            templatePath = "yellowhouses/yellowhouses_street_x";
        } else if (gridX == 0 ) {
            templatePath = "yellowhouses/yellowhouses_street_z";
        } else {
            templatePath = region.getRandom().nextInt(1000) == 0
                    ? "yellowhouses/yellowhouses_house_yellow" //change to rare house variant
                    : "yellowhouses/yellowhouses_house_yellow";
        }

        var templateManager = region.getServer().getStructureManager();

        ResourceLocation templateId = ResourceLocation.fromNamespaceAndPath("noodlemod", templatePath);

        StructureTemplate template = templateManager.get(templateId)
                .orElseThrow(() -> new IllegalStateException("Missing structure template: " + templateId
                ));

        // templates save origin, change if needed
        BlockPos origin = new BlockPos(
                chunkPos.getMinBlockX(),
                0,
                chunkPos.getMinBlockZ()
        );

        template.placeInWorld(region, origin, origin, new StructurePlaceSettings(), region.getRandom(), Block.UPDATE_CLIENTS);
    }
}
