package de.viarrah.noodle.item;

import de.viarrah.noodle.NoodleMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NoodleMod.MOD_ID);

    public static final DeferredItem<Item> NOODLUM = ITEMS.register("noodlum",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COOKED_NOODLUM = ITEMS.register("cooked_noodlum",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus noodleEventBus) {
        ITEMS.register(noodleEventBus);
    }
}
