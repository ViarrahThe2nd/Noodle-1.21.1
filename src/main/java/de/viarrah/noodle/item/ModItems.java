package de.viarrah.noodle.item;

import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.item.custom.ChiselItem;
import de.viarrah.noodle.item.custom.FuelItem;
import de.viarrah.noodle.item.custom.HammerItem;
import de.viarrah.noodle.util.ModTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
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

    // Noodlum Tools
    public static final DeferredItem<SwordItem> NOODLUM_SWORD = ITEMS.register("noodlum_sword",
            () -> new SwordItem(ModToolTiers.NOODLUM, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.NOODLUM, 5, -2.4f))));
    public static final DeferredItem<PickaxeItem> NOODLUM_PICKAXE = ITEMS.register("noodlum_pickaxe",
            () -> new PickaxeItem(ModToolTiers.NOODLUM, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.NOODLUM, 3.0F, -2.4f))));
    public static final DeferredItem<AxeItem> NOODLUM_AXE = ITEMS.register("noodlum_axe",
            () -> new AxeItem(ModToolTiers.NOODLUM, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolTiers.NOODLUM, 9.0F, -3.6f))));
    public static final DeferredItem<ShovelItem> NOODLUM_SHOVEL = ITEMS.register("noodlum_shovel",
            () -> new ShovelItem(ModToolTiers.NOODLUM, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolTiers.NOODLUM, 2.0F, -2.2f))));
    public static final DeferredItem<HoeItem> NOODLUM_HOE = ITEMS.register("noodlum_hoe",
            () -> new HoeItem(ModToolTiers.NOODLUM, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModToolTiers.NOODLUM, 1.0F, -2f))));

    public static final DeferredItem<HammerItem> NOODLUM_HAMMER = ITEMS.register("noodlum_hammer",
            () -> new HammerItem(ModToolTiers.NOODLUM, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.NOODLUM, 2.0F, -2.8f))));

    public static final DeferredItem<ArmorItem> VISOR = ITEMS.registerItem("visor", p -> new ArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, p),
            new Item.Properties().durability(128));
    public static final DeferredItem<ArmorItem> FOGVISOR = ITEMS.registerItem("fogvisor", p -> new ArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, p),
            new Item.Properties().durability(128));


    // noodlum armor
    public static final DeferredItem<ArmorItem> NOODLUM_HELMET = ITEMS.register("noodlum_helmet",
            () -> new ArmorItem(ModArmorMaterials.NOODLUM_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(83))));
    public static final DeferredItem<ArmorItem> NOODLUM_CHESTPLATE = ITEMS.register("noodlum_chestplate",
            () -> new ArmorItem(ModArmorMaterials.NOODLUM_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(83))));
    public static final DeferredItem<ArmorItem> NOODLUM_LEGGINGS = ITEMS.register("noodlum_leggings",
            () -> new ArmorItem(ModArmorMaterials.NOODLUM_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(83))));
    public static final DeferredItem<ArmorItem> NOODLUM_BOOTS = ITEMS.register("noodlum_boots",
            () -> new ArmorItem(ModArmorMaterials.NOODLUM_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(83))));





    public static void register(IEventBus noodleEventBus) {
        ITEMS.register(noodleEventBus);
    }
}
