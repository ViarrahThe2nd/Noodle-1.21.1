package de.viarrah.noodle.datagen;

import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.block.ModBlocks;
import de.viarrah.noodle.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {

    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NoodleMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.NOODLUM.get());
        basicItem(ModItems.COOKED_NOODLUM.get());
        basicItem(ModItems.MAGIC_ASHES.get());
        basicItem(ModItems.MAGIC_FUEL.get());
        basicItem(ModItems.CHISEL.get());

        // handhelditems

        handheldItem(ModItems.NOODLUM_SWORD.get());
        handheldItem(ModItems.NOODLUM_PICKAXE.get());
        handheldItem(ModItems.NOODLUM_AXE.get());
        handheldItem(ModItems.NOODLUM_SHOVEL.get());
        handheldItem(ModItems.NOODLUM_HOE.get());

        handheldItem(ModItems.NOODLUM_HAMMER.get());

        // Noodlum family, only these 4 because only these aren't cubeAll items, they require specials or textures.
        buttonItem(ModBlocks.NOODLUM_BUTTON, ModBlocks.NOODLUM_BLOCK);
        fenceItem(ModBlocks.NOODLUM_FENCE, ModBlocks.NOODLUM_BLOCK);
        wallItem(ModBlocks.NOODLUM_WALL, ModBlocks.NOODLUM_BLOCK);
        basicItem(ModBlocks.NOODLUM_DOOR.asItem());

        // noodlum armor
        trimmedArmorItem(ModItems.NOODLUM_HELMET);
        trimmedArmorItem(ModItems.NOODLUM_CHESTPLATE);
        trimmedArmorItem(ModItems.NOODLUM_LEGGINGS);
        trimmedArmorItem(ModItems.NOODLUM_BOOTS);
    }

    // Trimmed armor method. Insanity.
    private void trimmedArmorItem(DeferredItem<ArmorItem> itemDeferredItem) {
        final String MOD_ID = NoodleMod.MOD_ID;

        if(itemDeferredItem.get() instanceof ArmorItem armorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {
                float trimValue = value;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = armorItem.toString();
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemRL = ResourceLocation.parse(armorItemPath);
                ResourceLocation trimRL = ResourceLocation.parse(trimPath);
                ResourceLocation trimNameRL = ResourceLocation.parse(currentTrimName);

                // to avoid IllegalArgumentException
                existingFileHelper.trackGenerated(trimRL, PackType.CLIENT_RESOURCES, ".png", "textures");

                // The Trimmed (armorItem) Files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemRL.getNamespace() + ":item/" + armorItemRL.getPath())
                        .texture("layer1", trimRL);

                // The Untrimmed (armorItem) Files
                this.withExistingParent(itemDeferredItem.getId().getPath(),
                        mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameRL.getNamespace() + ":item/" + trimNameRL.getPath()))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                ResourceLocation.fromNamespaceAndPath(MOD_ID,
                                        "item/" + itemDeferredItem.getId().getPath()));
            });
        }
    }

    // pass in final block first, then base block to take identity from (same for other thingies)
    public void buttonItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/button_inventory"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(NoodleMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));

    }

    public void fenceItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(NoodleMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));

    }

    public void wallItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", ResourceLocation.fromNamespaceAndPath(NoodleMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));

    }
}
