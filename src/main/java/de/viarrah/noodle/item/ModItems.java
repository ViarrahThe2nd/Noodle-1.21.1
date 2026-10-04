package de.viarrah.noodle.item;

import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.item.custom.ChiselItem;
import de.viarrah.noodle.item.custom.FuelItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NoodleMod.MOD_ID);

    public static final DeferredItem<Item> NOODLUM = ITEMS.register("noodlum",

            // anonymous class

            () -> new Item(new Item.Properties().food(ModFoodProperties.NOODLUM)) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

                    tooltipComponents.add(Component.translatable("tooltip.noodlemod.noodlum"));

                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });


    public static final DeferredItem<Item> COOKED_NOODLUM = ITEMS.register("cooked_noodlum",
            () -> new Item(new Item.Properties().food(ModFoodProperties.COOKED_NOODLUM)));

    public static final DeferredItem<Item> CHISEL = ITEMS.register("chisel",
            () -> new ChiselItem(new Item.Properties().durability(438)));

    public static final DeferredItem<Item> MAGIC_FUEL = ITEMS.register("magic_fuel",
            () -> new FuelItem(new Item.Properties(), 1200));

    public static final DeferredItem<Item> MAGIC_ASHES = ITEMS.register("magic_ashes",
            () -> new Item(new Item.Properties()));
    // fuel register for Magic Ashes under datagen/ModDataMapProvider





    public static void register(IEventBus noodleEventBus) {
        ITEMS.register(noodleEventBus);
    }
}
