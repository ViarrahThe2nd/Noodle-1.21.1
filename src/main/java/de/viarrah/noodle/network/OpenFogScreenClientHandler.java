package de.viarrah.noodle.network;

import de.viarrah.noodle.blockentity.FogBlockEntity;
import de.viarrah.noodle.client.FogScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class OpenFogScreenClientHandler {
    private OpenFogScreenClientHandler() {}

    public static void handle(OpenFogScreen message, IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level instanceof ClientLevel level
                && level.getBlockEntity(message.pos()) instanceof FogBlockEntity fog) {
            minecraft.setScreen(new FogScreen(fog));
        }
    }
}