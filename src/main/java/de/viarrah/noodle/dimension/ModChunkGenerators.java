package de.viarrah.noodle.dimension;

import com.mojang.serialization.MapCodec;
import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.dimension.customChunkGenerators.yellowHousesGenerator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.neoforge.registries.DeferredRegister;
import de.viarrah.noodle.dimension.customChunkGenerators.testCheckerGenerator;

public class ModChunkGenerators {
    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> GENERATORS =
            DeferredRegister.create(
                    BuiltInRegistries.CHUNK_GENERATOR,
                    NoodleMod.MOD_ID
            );

    static {
        GENERATORS.register("testChecker", () -> testCheckerGenerator.CODEC);
        GENERATORS.register("yellowHousesGenerator", () -> yellowHousesGenerator.CODEC);
    }
}
