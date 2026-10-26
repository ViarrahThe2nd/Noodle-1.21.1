package de.viarrah.noodle.item;

import de.viarrah.noodle.util.ModTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolTiers {
    public static final Tier NOODLUM = new SimpleTier(ModTags.Blocks.INCORRECT_FOR_NOODLUM_TOOL, 1400, 10f, 4f, 28, () -> Ingredient.of(ModItems.NOODLUM));
}
