package com.premilix.eyestrainmod.client.hud;

import net.minecraft.network.chat.Component;

public class HudPromptState {
    private static Component content = null;
    private static int displayTicksRemaining = 0;

    public static void showPrompt(Component prompt, int seconds) {
        content = prompt;
        displayTicksRemaining = seconds * 20;
    }

    public static void tick() {
        if (displayTicksRemaining > 0) {
            displayTicksRemaining--;
        }
    }

    public static Component getContent() {
        return content;
    }

    public static int getTicksRemaining() {
        return displayTicksRemaining;
    }
}
