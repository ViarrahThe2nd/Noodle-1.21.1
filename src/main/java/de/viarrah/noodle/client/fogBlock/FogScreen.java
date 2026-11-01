package de.viarrah.noodle.client.fogBlock;

import de.viarrah.noodle.blockentity.fogBlock.FogBlockEntity;
import de.viarrah.noodle.fog.fogBlock.FogButton;
import de.viarrah.noodle.fog.fogBlock.FogSourceRegistry;
import de.viarrah.noodle.network.fogBlock.SaveFogSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public class FogScreen extends Screen {

    private final FogBlockEntity fog;
    private int currentColor;
    private float influenceDistance;
    private float fogStart;
    private float fogEnd;
    private float startPercentage;
    private float endPercentage;

    private boolean keepPreview = false;

    private boolean percentageMode = false;

    private FogBlockEntity.ActivationMode activationMode;

    public FogScreen(FogBlockEntity fog) {
        super(Component.nullToEmpty("Settings"));
        this.fog = fog;
        this.currentColor = this.fog.getFogColor();
        percentageMode = this.fog.getInfluenceMode() == FogBlockEntity.InfluenceMode.PERCENTAGE;
        this.activationMode = fog.getActivationMode();

        this.influenceDistance = fog.getInfluenceDistance();
        this.fogStart = fog.getFogStart();
        this.fogEnd = fog.getFogEnd();
        this.startPercentage = fog.getStartPercentage() * 100;
        this.endPercentage = fog.getEndPercentage() * 100;
    }

    private void updatePreview() {
        if (!(Minecraft.getInstance().level instanceof ClientLevel level)) return;

        FogSourceRegistry.updatePreview(
                level,
                new FogSourceRegistry.FogSource(
                        fog.getBlockPos().immutable(),
                        influenceDistance,
                        fogStart,
                        fogEnd,
                        currentColor,
                        percentageMode
                                ? FogBlockEntity.InfluenceMode.PERCENTAGE
                                : FogBlockEntity.InfluenceMode.FULL,
                        startPercentage / 100.0f,
                        endPercentage / 100.0f,
                        activationMode,
                        level.getGameTime()
                )
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public void onClose() {
        if (!keepPreview && Minecraft.getInstance().level instanceof ClientLevel level) {
            FogSourceRegistry.removePreview(level, fog.getBlockPos());
        }

        super.onClose();
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;


        FogButton modeButton = new FogButton(
                centerX - 150, centerY - 90, 300, 20,
                Component.nullToEmpty(percentageMode ? "PERCENTAGE" : "FULL"),
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
                        new FogColorScreen(this, currentColor, color -> {
                            currentColor = color;
                            updatePreview();
                        })
                )
        );
        this.addRenderableWidget(colorButton);


        FogButton doneButton = new FogButton(
                centerX + 5, centerY + 110, 145, 20,
                Component.nullToEmpty("Done"),
                button -> {
                    PacketDistributor.sendToServer(new SaveFogSettings(
                            fog.getBlockPos(),
                            influenceDistance,
                            fogStart,
                            fogEnd,
                            currentColor,
                            percentageMode
                                    ? FogBlockEntity.InfluenceMode.PERCENTAGE.name()
                                    : FogBlockEntity.InfluenceMode.FULL.name(),
                            startPercentage / 100.0f,
                            endPercentage / 100.0f,
                            activationMode.name()
                    ));

                    keepPreview = true;
                    this.onClose();
                }
        );
        this.addRenderableWidget(doneButton);

        FogButton activationButton = new FogButton(
                centerX - 150, centerY + 35, 300, 20,
                Component.nullToEmpty(String.valueOf(activationMode)),
                button -> {
                    activationMode = switch (activationMode) {
                        case ENABLED -> FogBlockEntity.ActivationMode.DISABLED;
                        case DISABLED -> FogBlockEntity.ActivationMode.REDSTONE;
                        case REDSTONE -> FogBlockEntity.ActivationMode.ENABLED;
                    };

                    button.setMessage(Component.nullToEmpty(String.valueOf(activationMode)));
                    updatePreview();
                }
        );
        this.addRenderableWidget(activationButton);

        FogButton cancelButton = new FogButton(
                centerX - 150, centerY + 110, 145, 20,
                Component.nullToEmpty("Cancel"),
                button -> {
                    keepPreview = false;
                    this.onClose();
                }
        );
        this.addRenderableWidget(cancelButton);


        FogSlider distanceSlider = new FogSlider(
                centerX - 150, centerY - 40,
               300, 20,
                "Influence Distance",
                0, 200, 1.0,
                this.fog != null ? influenceDistance : 30.0,
                value -> {
                    influenceDistance = value.floatValue();
                    updatePreview();
                }
        );
        this.addRenderableWidget(distanceSlider);


        FogSlider startSlider = new FogSlider(
                centerX - 150, centerY - 15,
                145, 20,
                "Start Distance",
                -50, 300, 1.0,
                this.fog != null ? fogStart : -5.0,
                value -> {
                    fogStart = value.floatValue();
                    updatePreview();
                }
        );
        this.addRenderableWidget(startSlider);


        FogSlider endSlider = new FogSlider(
                centerX + 5, centerY - 15,
                145, 20,
                "End Distance",
                0, 500, 1.0,
                this.fog != null ? fogEnd : 60.0,
                value -> {
                    fogEnd = value.floatValue();
                    updatePreview();
                }
        );
        this.addRenderableWidget(endSlider);


        FogSlider startPercentageSlider = new FogSlider(
                centerX - 150, centerY + 10,
                145, (int) 20,
                "minimum Coverage",
                0, 100, 1.0,
                this.fog != null ? startPercentage : 0.0,
                value -> {
                    startPercentage = value.floatValue();
                    updatePreview();
                }
        );
        this.addRenderableWidget(startPercentageSlider);


        FogSlider endPercentageSlider = new FogSlider(
                centerX + 5, centerY + 10,
                145, 20,
                "maximum Coverage",
                0, 100, 1.0,
                this.fog != null ? endPercentage : 100.0,
                value -> {
                    endPercentage = value.floatValue();
                    updatePreview();
                }
        );
        this.addRenderableWidget(endPercentageSlider);


    }


}