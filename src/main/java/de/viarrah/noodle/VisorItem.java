package de.viarrah.noodle;

import de.viarrah.noodle.item.ModItems;
import foundry.veil.api.client.render.VeilRenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;


@Mod(value = NoodleMod.MOD_ID, dist = Dist.CLIENT)

@EventBusSubscriber(modid = NoodleMod.MOD_ID, value = Dist.CLIENT)
public class VisorItem {
    @SubscribeEvent
    static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (Minecraft.getInstance().player != null && event.getSlot() == EquipmentSlot.HEAD && event.getEntity().getUUID().equals(Minecraft.getInstance().player.getUUID())) {

            if (event.getTo().getItem() == ModItems.VISOR.get()) {
                // add post effect
                VeilRenderSystem.renderer().getPostProcessingManager().add(100, ResourceLocation.fromNamespaceAndPath("noodlemod", "grayscale"));
            } else {
                // remove post effect
                VeilRenderSystem.renderer().getPostProcessingManager().remove(ResourceLocation.fromNamespaceAndPath("noodlemod", "grayscale"));
            }

            if (event.getTo().getItem() == ModItems.NOODLUM_HELMET.get()) {
                // add post effect
                VeilRenderSystem.renderer().getPostProcessingManager().add(500, ResourceLocation.fromNamespaceAndPath("noodlemod", "warp"));
            } else {
                // remove post effect
                VeilRenderSystem.renderer().getPostProcessingManager().remove(ResourceLocation.fromNamespaceAndPath("noodlemod", "warp"));
            }

        }
    }
}
