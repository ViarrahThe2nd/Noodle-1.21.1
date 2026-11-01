package de.viarrah.noodle.block;

import com.mojang.blaze3d.shaders.Uniform;
import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.block.custom.MagicBlock;
import de.viarrah.noodle.block.custom.NoodleLampBlock;
import de.viarrah.noodle.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NoodleMod.MOD_ID);

    // REGISTER BLOCKS HERE

    public static final DeferredBlock<Block> NOODLUM_BOX = registerBlock("noodlum_box",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.ANVIL)));

    // Noodlum family -------------------------------------------------------

    public static final DeferredBlock<Block> NOODLUM_BLOCK = registerBlock("noodlum_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    // stairs require defaultblockstate of copied block
    public static final DeferredBlock<StairBlock> NOODLUM_STAIRS = registerBlock("noodlum_stairs",
            () -> new StairBlock(ModBlocks.NOODLUM_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));

    // slab requires only standard block things
    public static final DeferredBlock<SlabBlock> NOODLUM_SLAB = registerBlock("noodlum_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));

    // Pressure plate requires Type (Iron ,Gold, whateva
    public static final DeferredBlock<PressurePlateBlock> NOODLUM_PRESSURE_PLATE = registerBlock("noodlum_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.IRON, BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));

    // button requires Type, tick presstime and behaviour. Maybe include .noCollission() otherwise it might be weird
    public static final DeferredBlock<ButtonBlock> NOODLUM_BUTTON = registerBlock("noodlum_button",
            () -> new ButtonBlock(BlockSetType.IRON, 10, BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops().noCollission()));

    // fence needs only behaviour
    public static final DeferredBlock<FenceBlock> NOODLUM_FENCE = registerBlock("noodlum_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));

    // Fencegate behaviour and.. woodtype..? for some reason. makes no difference
    public static final DeferredBlock<FenceGateBlock> NOODLUM_FENCE_GATE = registerBlock("noodlum_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));

    // wall only behaviour
    public static final DeferredBlock<WallBlock> NOODLUM_WALL = registerBlock("noodlum_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));

    // type (iron/wood), behaviour. Include no Occlusion to stop occlusion culling because of door
    public static final DeferredBlock<DoorBlock> NOODLUM_DOOR = registerBlock("noodlum_door",
            () -> new DoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops().noOcclusion()));

    // type (iron/wood, behaviour. Include no Occlusion to stop occlusion culling because of trapdoor
    public static final DeferredBlock<TrapDoorBlock> NOODLUM_TRAPDOOR = registerBlock("noodlum_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops().noOcclusion()));

    // END OF NOODLUM FAMILY ###########################################################

    // Noodlum LAMP (toggleable)
    public static final DeferredBlock<Block> NOODLUM_LAMP = registerBlock("noodlum_lamp",
            () -> new NoodleLampBlock(BlockBehaviour.Properties.of().strength(2f)
                    .requiresCorrectToolForDrops().lightLevel(state -> state.getValue(NoodleLampBlock.ACTIVE) ? 15 : 0 )));


    public static final DeferredBlock<Block> NOODLUM_ORE = registerBlock("noodlum_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 5),
                    BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_NOODLUM_ORE = registerBlock("deepslate_noodlum_ore",
            () -> new DropExperienceBlock(UniformInt.of(4, 6),
                    BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final DeferredBlock<Block> MAGIC_BLOCK = registerBlock("magic_block",
            () -> new MagicBlock(BlockBehaviour.Properties.of().strength(2f).noLootTable().sound(SoundType.METAL)));


    public static final DeferredBlock<FogBlock> FOG_BLOCK =
            registerBlock("fog_block", () ->
                    new FogBlock(BlockBehaviour.Properties.of().strength(1.5f).noLootTable())
            );



    // END OF REGISTERING

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus noodleEventBus) {
        BLOCKS.register(noodleEventBus);
    }
}
