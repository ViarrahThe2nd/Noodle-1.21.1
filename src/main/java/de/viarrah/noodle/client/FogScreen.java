package de.viarrah.noodle.client;

import de.viarrah.noodle.fog.FogButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ISystemReportExtender;

import java.util.function.Consumer;

public class FogScreen extends Screen {
    private boolean percentageMode = false;

    protected FogScreen() {
        super(Component.nullToEmpty("Settings"));
    }

    private int currentColor = 0xFFFFFF;

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;


        FogButton modeButton = new FogButton(
                centerX - 150, centerY - 90, 300, 20,
                Component.nullToEmpty("PERCENTAGE"),
                button -> {
                    percentageMode = !percentageMode;

                    button.setMessage(Component.nullToEmpty(
                            percentageMode ? "PERCENTAGE" : "FULL"
                    ));

                }
        );

        this.addRenderableWidget(modeButton);


        FogButton colorButton = new FogButton(
                centerX - 150, centerY - 65, 300, 20,
                Component.nullToEmpty("Fog Color"),
                button -> Minecraft.getInstance().setScreen(
                        new FogColorScreen(this, currentColor, color -> currentColor = color)
                )
        );

        this.addRenderableWidget(colorButton);


        FogButton doneButton = new FogButton(
                centerX - 150, centerY + 110, 300, 20,
                Component.nullToEmpty("Done"),
                button -> this.onClose()
        );

        this.addRenderableWidget(doneButton);


        FogSlider distanceSlider = new FogSlider(
                centerX - 150, centerY - 40,
               300, 20,
                "Influence Distance",
                0, 200,
                15,
                value -> {
                    // store
                }
        );

        this.addRenderableWidget(distanceSlider);


        FogSlider startSlider = new FogSlider(
                centerX - 150, centerY - 15,
                145, 20,
                "Start Distance",
                -30, 200,
                0,
                value -> {
                    // store
                }
        );

        this.addRenderableWidget(startSlider);


        FogSlider endSlider = new FogSlider(
                centerX + 5, centerY - 15,
                145, 20,
                "End Distance",
                0, 300,
                50,
                value -> {
                    // store
                }
        );

        this.addRenderableWidget(endSlider);


        FogSlider startPercentageSlider = new FogSlider(
                centerX - 150, centerY + 10,
                145, (int) 20,
                "minimum Coverage",
                0, 100,
                15,
                value -> {
                    // store
                }
        );

        this.addRenderableWidget(startPercentageSlider);


        FogSlider endPercentageSlider = new FogSlider(
                centerX + 5, centerY + 10,
                145, 20,
                "maximum Coverage",
                0, 100,
                15,
                value -> {
                    // store
                }
        );

        this.addRenderableWidget(endPercentageSlider);


    }


}