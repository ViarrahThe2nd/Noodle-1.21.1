package de.viarrah.noodle.client;

import de.viarrah.noodle.fog.FogButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.IntConsumer;

public class FogColorScreen extends Screen {

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    private float hue = 0.0f;
    private float saturation = 1.0f;
    private float brightness = 1.0f;

    private final Screen parent;
    private int selectedColor;

    private final IntConsumer onColorSelected;

    public FogColorScreen(Screen parent, int initialColor, IntConsumer onColorSelected) {
        super(Component.nullToEmpty("Fog Color"));

        this.parent = parent;
        this.selectedColor = initialColor;
        this.onColorSelected = onColorSelected;

        float[] hsb = java.awt.Color.RGBtoHSB(
                (initialColor >> 16) & 0xFF,
                (initialColor >> 8) & 0xFF,
                initialColor & 0xFF,
                null
        );

        this.hue = hsb[0] * 360.0f;
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    @Override
    protected void init() {
        squareX = this.width / 2 - squareSize / 2;
        squareY = this.height / 2 - squareSize / 2;

        hueSliderX = squareX + squareSize + 10;
        hueSliderY = squareY;

        previewX = squareX - previewWidth - 10;
        previewY = squareY;

        int buttonY = squareY + squareSize + 10;
        int centerX = this.width / 2;

        this.addRenderableWidget(new FogButton(
                centerX - 85, buttonY, 80, 20,
                Component.nullToEmpty("Apply"),
                button -> {
                    onColorSelected.accept(selectedColor);
                    this.onClose();
                }
        ));

        this.addRenderableWidget(new FogButton(
                centerX + 5, buttonY, 80, 20,
                Component.nullToEmpty("Cancel"),
                button -> this.onClose()
        ));
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }

    @Override
    public void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        renderColorSquare(guiGraphics, mouseX, mouseY);
        renderHueSlider(guiGraphics);
        renderColorPreview(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void updateColor() {
        selectedColor = java.awt.Color.HSBtoRGB(
                hue / 360.0f,
                saturation,
                brightness
        ) & 0xFFFFFF;
    }

    private int squareX;
    private int squareY;
    private int squareSize = 150;

    private void renderColorSquare(
            GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {
        int baseColor = java.awt.Color.HSBtoRGB(
                hue / 360.0f, 1.0f, 1.0f
        );

        // Base hue
        graphics.fill(
                squareX, squareY,
                squareX + squareSize, squareY + squareSize,
                baseColor
        );

        // White overlay: full strength on the left, transparent on the right
        for (int x = 0; x < squareSize; x++) {
            int alpha = 255 - (x * 255 / squareSize);

            graphics.fill(
                    squareX + x, squareY,
                    squareX + x + 1, squareY + squareSize,
                    (alpha << 24) | 0xFFFFFF
            );
        }

        // Black overlay: transparent at the top, full strength at the bottom
        for (int y = 0; y < squareSize; y++) {
            int alpha = y * 255 / squareSize;

            graphics.fill(
                    squareX, squareY + y,
                    squareX + squareSize, squareY + y + 1,
                    (alpha << 24) | 0x000000
            );
        }
    }

    // HUE SLIDER

    private int hueSliderX;
    private int hueSliderY;
    private int hueSliderWidth = 20;
    private int hueSliderHeight = 150;

    private void renderHueSlider(GuiGraphics graphics) {
        for (int y = 0; y < hueSliderHeight; y++) {
            float currentHue = (float) y / hueSliderHeight;

            int color = java.awt.Color.HSBtoRGB(
                    currentHue, 1.0f, 1.0f
            );

            graphics.fill(
                    hueSliderX,
                    hueSliderY + y,
                    hueSliderX + hueSliderWidth,
                    hueSliderY + y + 1,
                    color
            );
        }
    }

    private boolean dragging = false;

    // PREVIEW

    private int previewX;
    private int previewY;
    private int previewWidth = 20;
    private int previewHeight = 150;

    private void renderColorPreview(GuiGraphics graphics) {
        graphics.fill(
                previewX,
                previewY,
                previewX + previewWidth,
                previewY + previewHeight,
                0xFF000000 | selectedColor
        );
    }


    // MOUSE CONTROLS

    private void updateSquareFromMouse(double mouseX, double mouseY) {
        saturation = (float) Math.clamp(
                (mouseX - squareX) / squareSize,
                0.0, 1.0
        );

        brightness = 1.0f - (float) Math.clamp(
                (mouseY - squareY) / squareSize,
                0.0, 1.0
        );

        updateColor();
    }

    private void updateHueFromMouse(double mouseY) {
        hue = (float) Math.clamp(
                (mouseY - hueSliderY) / hueSliderHeight,
                0.0, 1.0
        ) * 360.0f;

        updateColor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {

            if (mouseX >= squareX && mouseX <= squareX + squareSize &&
                    mouseY >= squareY && mouseY <= squareY + squareSize) {

                dragging = true;
                updateSquareFromMouse(mouseX, mouseY);
                return true;
            }

            if (mouseX >= hueSliderX && mouseX <= hueSliderX + hueSliderWidth &&
                    mouseY >= hueSliderY && mouseY <= hueSliderY + hueSliderHeight) {

                dragging = true;
                updateHueFromMouse(mouseY);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(
            double mouseX, double mouseY,
            int button, double dragX, double dragY
    ) {
        if (dragging && button == 0) {

            if (mouseX >= squareX - 5 && mouseX <= squareX + squareSize + 5) {
                updateSquareFromMouse(mouseX, mouseY);
            }

            if (mouseX >= hueSliderX - 5 && mouseX <= hueSliderX + hueSliderWidth + 5) {
                updateHueFromMouse(mouseY);
            }

            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            dragging = false;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }
}