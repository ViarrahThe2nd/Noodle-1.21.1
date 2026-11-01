package de.viarrah.noodle.block;

import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.blockentity.FogBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(
                    Registries.BLOCK_ENTITY_TYPE,
                    NoodleMod.MOD_ID
            );

    public static final Supplier<BlockEntityType<FogBlockEntity>> FOG_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register(
                    "fog_block",
                    () -> BlockEntityType.Builder.of(
                            FogBlockEntity::new,
                            ModBlocks.FOG_BLOCK.get()
                    ).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
