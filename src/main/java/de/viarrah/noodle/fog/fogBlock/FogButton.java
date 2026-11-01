package de.viarrah.noodle.fog.fogBlock;

import de.viarrah.noodle.NoodleMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FogButton extends Button {
    public FogButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    private static final ResourceLocation FOG_BUTTON =
            ResourceLocation.fromNamespaceAndPath(
                    NoodleMod.MOD_ID, "widget/fog_button");
    private static final ResourceLocation FOG_BUTTON_HIGHLIGHTED =
            ResourceLocation.fromNamespaceAndPath(
                    NoodleMod.MOD_ID, "widget/fog_button_highlighted");

    @Override
    protected void renderWidget(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        ResourceLocation texture = this.isHoveredOrFocused()
                ? FOG_BUTTON_HIGHLIGHTED
                : FOG_BUTTON;

        guiGraphics.blitSprite(
                texture,
                this.getX(),
                this.getY(),
                this.width,
                this.height
        );

        this.renderString(guiGraphics, Minecraft.getInstance().font, 0xFFFFFF);
    }
}
