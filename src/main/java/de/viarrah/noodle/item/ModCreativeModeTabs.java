package de.viarrah.noodle.item;

import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NoodleMod.MOD_ID);


    public static final Supplier<CreativeModeTab> NOODLE_TAB = CREATIVE_MODE_TAB.register("noodles_items",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.NOODLUM.get()))
                    .title(Component.translatable("creativetab.noodlemod.noodles_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        // ITEMS ADDED HERE
                        output.accept(ModItems.NOODLUM);
                        output.accept(ModItems.COOKED_NOODLUM);

                        output.accept(ModItems.CHISEL);
                        output.accept(ModItems.MAGIC_FUEL);
                        output.accept(ModItems.MAGIC_ASHES);
                        output.accept(ModItems.VISOR);

                        output.accept(ModItems.NOODLUM_SWORD);
                        output.accept(ModItems.NOODLUM_PICKAXE);
                        output.accept(ModItems.NOODLUM_AXE);
                        output.accept(ModItems.NOODLUM_SHOVEL);
                        output.accept(ModItems.NOODLUM_HOE);

                        output.accept(ModItems.NOODLUM_HAMMER);

                    })
                    .build());

    public static final Supplier<CreativeModeTab> NOODLE_BLOCK_TAB = CREATIVE_MODE_TAB.register("noodles_blocks",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.NOODLUM_BLOCK))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(NoodleMod.MOD_ID, "noodles_items"))
                    .title(Component.translatable("creativetab.noodlemod.noodles_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        // ITEMS ADDED HERE

                        output.accept(ModBlocks.NOODLUM_BOX);
                        output.accept(ModBlocks.NOODLUM_ORE);
                        output.accept(ModBlocks.DEEPSLATE_NOODLUM_ORE);

                        output.accept(ModBlocks.MAGIC_BLOCK);

                        output.accept(ModBlocks.NOODLUM_BLOCK);
                        output.accept(ModBlocks.NOODLUM_BUTTON);
                        output.accept(ModBlocks.NOODLUM_DOOR);
                        output.accept(ModBlocks.NOODLUM_FENCE);
                        output.accept(ModBlocks.NOODLUM_STAIRS);
                        output.accept(ModBlocks.NOODLUM_FENCE_GATE);
                        output.accept(ModBlocks.NOODLUM_PRESSURE_PLATE);
                        output.accept(ModBlocks.NOODLUM_SLAB);
                        output.accept(ModBlocks.NOODLUM_TRAPDOOR);
                        output.accept(ModBlocks.NOODLUM_WALL);

                        output.accept(ModBlocks.NOODLUM_LAMP);

                    })
                    .build());


    public static void register(IEventBus noodleEventBus) {
        CREATIVE_MODE_TAB.register(noodleEventBus);
    }

}
