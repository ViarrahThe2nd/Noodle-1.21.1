package de.viarrah.noodle.datagen;

import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.block.ModBlocks;
import de.viarrah.noodle.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        List<ItemLike> NOODLUM_SMELTABLES = List.of(
                ModBlocks.NOODLUM_ORE, ModBlocks.DEEPSLATE_NOODLUM_ORE);

        List<ItemLike> COOKED_NOODLUM_SMELTABLES = List.of(
                ModItems.NOODLUM);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CHISEL.get())
                .pattern("  B")
                .pattern(" A ")
                .pattern("A  ")
                .define('A', Items.STICK)
                .define('B', ModItems.NOODLUM.get())
                .unlockedBy("has_noodlum", has(ModItems.NOODLUM)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.NOODLUM_BLOCK.get())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.NOODLUM.get())
                .unlockedBy("has_noodlum", has(ModItems.NOODLUM)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.NOODLUM.get(), 9)
                .requires(ModBlocks.NOODLUM_BLOCK)
                .unlockedBy("has_noodlum_block", has(ModBlocks.NOODLUM_BLOCK)).save(recipeOutput, "noodlemod:noodlum_from_noodlum_block");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.NOODLUM_BOX.get())
                .pattern("ABA")
                .pattern("BBB")
                .pattern("ABA")
                .define('A', ModItems.COOKED_NOODLUM.get())
                .define('B', ModItems.NOODLUM.get())
                .unlockedBy("has_noodlum", has(ModItems.NOODLUM)).save(recipeOutput);

        oreSmelting(recipeOutput, NOODLUM_SMELTABLES, RecipeCategory.MISC, ModItems.NOODLUM.get(), 0.25f, 67, "noodlum");
        oreBlasting(recipeOutput, NOODLUM_SMELTABLES, RecipeCategory.MISC, ModItems.NOODLUM.get(), 0.25f, 33, "noodlum");

        oreSmelting(recipeOutput, COOKED_NOODLUM_SMELTABLES, RecipeCategory.MISC, ModItems.COOKED_NOODLUM.get(), 0.25f, 67, "cooked_noodlum");
        oreCooking(recipeOutput, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, COOKED_NOODLUM_SMELTABLES, RecipeCategory.MISC, ModItems.COOKED_NOODLUM.get(),
                0.25f, 33, "cooked_noodlum", "_from_smoking");


    }

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                                                       float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemLike : pIngredients) {
                SimpleCookingRecipeBuilder.generic(Ingredient.of(itemLike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory)
                        .group(pGroup).unlockedBy(getHasName(itemLike), has(itemLike))
                        .save(recipeOutput, NoodleMod.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemLike));
        }
    }
}
