package de.viarrah.noodle.client;

import de.viarrah.noodle.NoodleMod;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class FogSlider extends AbstractSliderButton {


    private final String label;
    private final double min;
    private final double max;
    private final Consumer<Double> onChanged;

    public FogSlider(
            int x, int y, int width, int height,
            String label,
            double min, double max,
            double initialValue,
            Consumer<Double> onChanged
    ) {
        super(
                x, y, width, height,
                Component.nullToEmpty(""),
                (initialValue - min) / (max - min)
        );

        this.label = label;
        this.min = min;
        this.max = max;
        this.onChanged = onChanged;

        updateMessage();
    }

    private double getActualValue() {
        return min + this.value * (max - min);
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.nullToEmpty(
                label + ": " + String.format("%.1f", getActualValue())
        ));
    }

    @Override
    protected void applyValue() {
        onChanged.accept(getActualValue());
    }

    private static final ResourceLocation FOG_SLIDER =
            ResourceLocation.fromNamespaceAndPath(
                    NoodleMod.MOD_ID, "widget/fog_slider");
    private static final ResourceLocation FOG_SLIDER_HIGHLIGHTED =
            ResourceLocation.fromNamespaceAndPath(
                    NoodleMod.MOD_ID, "widget/fog_slider_highlighted");
    private static final ResourceLocation FOG_SLIDER_HANDLE =
            ResourceLocation.fromNamespaceAndPath(
                    NoodleMod.MOD_ID, "widget/fog_slider_handle");
    private static final ResourceLocation FOG_SLIDER_HANDLE_HIGHLIGHTED =
            ResourceLocation.fromNamespaceAndPath(
                    NoodleMod.MOD_ID, "widget/fog_slider_handle_highlighted");

    @Override
    protected ResourceLocation getSprite() {
        return this.isHoveredOrFocused()
                ? FOG_SLIDER_HIGHLIGHTED
                : FOG_SLIDER;
    }

    @Override
    protected ResourceLocation getHandleSprite() {
        return this.isHoveredOrFocused()
                ? FOG_SLIDER_HANDLE_HIGHLIGHTED
                : FOG_SLIDER_HANDLE;
    }
}