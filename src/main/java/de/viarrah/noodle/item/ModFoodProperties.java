package de.viarrah.noodle.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.util.function.Supplier;

public class ModFoodProperties {
    public static final FoodProperties NOODLUM = (new FoodProperties.Builder().nutrition(2).saturationModifier(0.25f)
            .fast().effect(() -> new MobEffectInstance(MobEffects.LUCK, 400), 0.25f).build());

    public static final FoodProperties COOKED_NOODLUM = (new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f)
            .fast().effect(() -> new MobEffectInstance(MobEffects.HUNGER, 800), 0.5f).build());

}
