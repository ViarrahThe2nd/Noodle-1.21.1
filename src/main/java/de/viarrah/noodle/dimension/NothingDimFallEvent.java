package de.viarrah.noodle.dimension;

import de.viarrah.noodle.NoodleMod;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.logging.Level;

@EventBusSubscriber(modid = NoodleMod.MOD_ID)

public class NothingDimFallEvent {

    private static final ResourceLocation NOTHING_DIMENSION = ResourceLocation.fromNamespaceAndPath(
            NoodleMod.MOD_ID,
            "rifts/nothing"
    );

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        // this event also fires clientside but teleports should only happen serverside
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!player.level().dimension().location().equals(NOTHING_DIMENSION)) {
            return;
        }

        if (player.getY() >= -50.0f) {
            return;
        }

        ServerLevel overworld = player.getServer().overworld();
        if(overworld == null) {
            return;
        }

        player.teleportTo(
                overworld,
                player.getX(),
                749.0,
                player.getZ(),
                player.getYRot(),
                player.getXRot()
        );

        player.setDeltaMovement(0.0, 0.0, 0.0);
    }
}
