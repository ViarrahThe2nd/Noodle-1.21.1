package de.viarrah.noodle.client;

import com.mojang.blaze3d.platform.InputConstants;
import de.viarrah.noodle.NoodleMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class FogKeyTemp {
    public static final KeyMapping OPEN_FOG_SCREEN = new KeyMapping(
            "key." + NoodleMod.MOD_ID + ".open_fog_screen",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.misc"
    );

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(OPEN_FOG_SCREEN);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (OPEN_FOG_SCREEN.consumeClick()) {
            Minecraft.getInstance().setScreen(new FogScreen());
        }
    }
}
