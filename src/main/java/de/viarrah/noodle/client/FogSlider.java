package de.viarrah.noodle.client;

import de.viarrah.noodle.NoodleMod;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class FogSlider extends AbstractSliderButton {


    private final String label;
    private final double min;
    private final double max;
    private final double step;
    private final Consumer<Double> onChanged;

    public FogSlider(
            int x, int y, int width, int height,
            String label,
            double min, double max, double step,
            double initialValue,
            Consumer<Double> onChanged
    ) {
        super(
                x, y, width, height,
                Component.nullToEmpty(""),
                Mth.clamp((snapToStep(initialValue, min, max, step) - min) / (max - min), 0.0, 1.0)
        );

        if (max <= min) {
            throw new IllegalArgumentException("Maximum must be greater than minimum");
        }

        if (step <= 0) {
            throw new IllegalArgumentException("Step must be greater than zero");
        }

        this.label = label;
        this.min = min;
        this.max = max;
        this.step = step;
        this.onChanged = onChanged;

        updateMessage();
    }

    private static double snapToStep(double value, double min, double max, double step) {
        value = Mth.clamp(value, min, max);
        return min + Math.round((value - min) / step) * step;
    }

    private double getActualValue() {
        double rawValue = min + this.value * (max - min);
        return snapToStep(rawValue, min, max, step);
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.nullToEmpty(
                label + ": " + String.format("%.0f", getActualValue())
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