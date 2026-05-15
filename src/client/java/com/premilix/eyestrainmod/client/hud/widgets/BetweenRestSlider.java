package com.premilix.eyestrainmod.client.hud.widgets;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class BetweenRestSlider extends AbstractSliderButton {

    public BetweenRestSlider(final int x, final int y, final int width, final int height, final int initialMinutes) {
        double initialValue = getSliderValueFromMinutes(initialMinutes);
        super(x, y, width, height, computeMessage(initialValue), initialValue);
    }

    private static Component computeMessage(final double sliderValue) {
        return Component.literal(interpolate(sliderValue) + " min");
    }

    private static int interpolate(final double sliderValue) {
        return (int) Math.round(59 * sliderValue + 1);
    }

    private static double getSliderValueFromMinutes(int minutes) {
        int clamped = Math.clamp(minutes, 1, 60);
        return (clamped - 1) / 59.0;
    }

    public int getMinutes() {
        return interpolate(this.value);
    }

    @Override
    public boolean keyPressed(final KeyEvent event) {
        if (!event.isSelection() && this.canChangeValue) {
            boolean left = event.isLeft();
            boolean right = event.isRight();
            if (left || right) {
                float direction = left ? -1.0F : 1.0F;
                this.setValue(this.value + direction / 59.0);
                return true;
            }
        }

        return super.keyPressed(event);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(computeMessage(this.value));
    }

    /**
     * Doesn't do anything. Saving is handled in {@code ConfigScreen.java} only upon clicking `save`.
     */
    @Override
    protected void applyValue() {}
}
