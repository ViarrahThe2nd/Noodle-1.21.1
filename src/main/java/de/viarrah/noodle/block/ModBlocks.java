package de.viarrah.noodle.block;

import com.mojang.blaze3d.shaders.Uniform;
import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NoodleMod.MOD_ID);

    // REGISTER BLOCKS HERE

    public static final DeferredBlock<Block> NOODLUM_BOX = registerBlock("noodlum_box",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.ANVIL)));

    public static final DeferredBlock<Block> NOODLUM_BLOCK = registerBlock("noodlum_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    public static final DeferredBlock<Block> NOODLUM_ORE = registerBlock("noodlum_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 5),
                    BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_NOODLUM_ORE = registerBlock("deepslate_noodlum_ore",
            () -> new DropExperienceBlock(UniformInt.of(4, 6),
                    BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

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
