package de.viarrah.noodle.dimension;

import de.viarrah.noodle.NoodleMod;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;

import java.io.IOException;
import java.util.Map;

@EventBusSubscriber(
        modid = NoodleMod.MOD_ID,
        value = Dist.CLIENT
)

public class RiftShaderEvents {

    private static ResourceLocation dimensionId(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    private static ResourceLocation lastDimensionLoc = null;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null || minecraft.player == null) {
            return;
        }

        ResourceLocation currentDimension = minecraft.level.dimension().location();
        String expectedPack = SHADERS_BY_DIM.get(currentDimension);

        // Dimensions not mapped keep current shader
        if (expectedPack == null) {
            return;
        }

        // Finish applying shader change, tick after closing menu correct settings
        if (minecraft.screen != null) {
            return;
        }

        String selectedPack = Iris.getIrisConfig().getShaderPackName().orElse("");
        boolean shadersEnabled = Iris.getIrisConfig().areShadersEnabled();

        boolean needsCorrection;
        if(expectedPack.isBlank()) {
            // blank value = shaders disabled
            needsCorrection = shadersEnabled;
        } else {
            needsCorrection = !expectedPack.equals(selectedPack) || !shadersEnabled;
        }

        if (needsCorrection) {
            applyShaderPackFor(expectedPack);
        }
    }

    @SubscribeEvent
    public static void replaceDebugText(CustomizeGuiOverlayEvent.DebugText event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            return;
        }

        ResourceLocation currentDimension = minecraft.level.dimension().location();

        if (!currentDimension.getNamespace().equals(NoodleMod.MOD_ID)) {
            return;
        }

        event.getLeft().clear();
        event.getRight().clear();
        event.getLeft().add("HA! You really thought you could use coordinates? HELL NO.");
    }

    private static final Map<ResourceLocation, String> SHADERS_BY_DIM = Map.of(
            dimensionId("noodlemod", "rifts/nothing"), "Nothing_Kappa.zip",
            dimensionId("minecraft", "overworld"), "Overworld_Hysteria.zip",
            dimensionId("noodlemod", "rifts/yellowhouses"), "Yellowhouses_Unbound.zip"
    );

    private static void applyShaderPackFor(String shaderPackName) {

        try {
            if (shaderPackName.isBlank()) {
                // Empty name = no shader
                IrisApi.getInstance()
                        .getConfig()
                        .setShadersEnabledAndApply(false);
                return;
            }

            Iris.getIrisConfig().setShaderPackName(shaderPackName);
            Iris.getIrisConfig().setShadersEnabled(true);
            //save selected before reload
            Iris.getIrisConfig().save();
            Iris.reload();
        } catch (IOException exception) {
            NoodleMod.LOGGER.error("Could not switch shader to '{}'", shaderPackName, exception);
        }

    }

}
