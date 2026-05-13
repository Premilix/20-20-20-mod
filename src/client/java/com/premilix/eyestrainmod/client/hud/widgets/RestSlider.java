package com.premilix.eyestrainmod.client.hud.widgets;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class RestSlider extends AbstractSliderButton {

    public RestSlider(final int x, final int y, final int width, final int height, final int initialSeconds) {
        double initialValue = getSliderValueFromSeconds(initialSeconds);
        super(x, y, width, height, computeMessage(initialValue), initialValue);
    }

    private static Component computeMessage(final double sliderValue) {
        return Component.literal(interpolate(sliderValue) + " s");
    }

    private static int interpolate(final double sliderValue) {
        return (int) Math.round(55 * sliderValue + 5);
    }

    private static double getSliderValueFromSeconds(int seconds) {
        int clamped = Math.clamp(seconds, 5, 60);
        return (clamped - 5) / 55.0;
    }

    @Override
    public boolean keyPressed(final KeyEvent event) {
        if (!event.isSelection() && this.canChangeValue) {
            boolean left = event.isLeft();
            boolean right = event.isRight();
            if (left || right) {
                float direction = left ? -1.0F : 1.0F;
                this.setValue(this.value + direction / 55.0);
                return true;
            }
        }

        return super.keyPressed(event);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(computeMessage(this.value));
    }

    @Override
    protected void applyValue() {
        // TODO: update config parameter by updating a field in the screen
    }
}
