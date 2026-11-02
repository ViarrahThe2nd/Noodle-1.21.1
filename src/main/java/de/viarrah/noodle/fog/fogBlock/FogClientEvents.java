package de.viarrah.noodle.fog.fogBlock;


import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.blockentity.fogBlock.FogBlockEntity;
import de.viarrah.noodle.client.fogBlock.FogScreen;
import de.viarrah.noodle.network.fogBlock.OpenFogScreen;
import foundry.veil.api.client.render.VeilRenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(
        modid = NoodleMod.MOD_ID,
        value = Dist.CLIENT
)
public class FogClientEvents {

    public static final ResourceLocation FOG_PIPELINE =
            ResourceLocation.fromNamespaceAndPath(
                    NoodleMod.MOD_ID,
                    "fog"
            );

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();

        if (!(mc.level instanceof ClientLevel level) || mc.player == null) {
            FogManager.reset();
            return;
        }

        BlockPos openPos = OpenFogScreen.consumePendingPos();

        if (openPos != null
                && level.getBlockEntity(openPos) instanceof FogBlockEntity fog) {
            Minecraft.getInstance().setScreen(new FogScreen(fog));
        }

        FogSourceRegistry.prune(level, level.getGameTime());

        FogManager.tick(level, mc.player);

        // Ensure our fog pipeline is active.
        var postManager = VeilRenderSystem.renderer()
                .getPostProcessingManager();

        if (!postManager.isActive(FOG_PIPELINE)) {
            postManager.add(10000, FOG_PIPELINE);
        }
    }
}
