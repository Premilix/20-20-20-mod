package com.premilix.eyestrainmod.client.hud;

import net.minecraft.network.chat.Component;

public class HudPromptState {
    private static Component content = null;
    private static boolean active = false;

    public static int totalDurationTicks;
    public static final float ANIM_DURATION_TICKS = 10.0f;

    public static int ticksActive = 0;
    public static int prevTicksActive = 0;

    public static boolean startAnimation = false;
    public static boolean endAnimation = false;

    /**
     * Show the provided prompt to the user
     * @param prompt message to the user
     * @param seconds duration to show prompt for
     * @param startAnimation whether to show the sliding animation upon popup
     * @param endAnimation whether to show the sliding animation upon dismissal
     */
    public static void showPrompt(Component prompt, int seconds, boolean startAnimation, boolean endAnimation) {
        content = prompt;
        totalDurationTicks = seconds * 20;
        ticksActive = 0;
        prevTicksActive = 0;
        active = true;
        HudPromptState.startAnimation = startAnimation;
        HudPromptState.endAnimation = endAnimation;
    }

    public static void tick() {
        if (!active) return;

        prevTicksActive = ticksActive;

        if (ticksActive < totalDurationTicks) {
            ticksActive++;
        }
        else {
            active = false;
        }
    }

    public static Component getContent() {
        return content;
    }

    public static boolean isActive() {
        return active;
    }
}
