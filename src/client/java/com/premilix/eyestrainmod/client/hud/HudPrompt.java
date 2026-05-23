package com.premilix.eyestrainmod.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public class HudPrompt {
    public static HudElement render() {
        return (graphics, tickDelta) -> {
            Component prompt = HudPromptState.getContent();
            if (!HudPromptState.isActive() || prompt == null) return;

            float exactTick = Mth.lerp(tickDelta.getGameTimeDeltaPartialTick(false), HudPromptState.prevTicksActive, HudPromptState.ticksActive);
            float smoothedProgress = getSmoothedProgress(exactTick);

            Minecraft client = Minecraft.getInstance();
            int screenWidth = graphics.guiWidth();
            int screenHeight = graphics.guiHeight();

            // setup dimensions
            int textWidth = 180;
            int paddingX = 6;
            int paddingY = 11;
            int boxWidth = textWidth + (paddingX * 2);

            List<FormattedCharSequence> lines = client.font.split(prompt, textWidth);
            int lineCount = Mth.absMax(lines.size(), 2);
            int linePadding = 4;
            int boxHeight = lineCount * client.font.lineHeight + (lineCount - 1) * linePadding +  (paddingY * 2);

            // interpolate the X position based on progress
            int actualX = screenWidth - (int)(smoothedProgress * boxWidth);
            int actualY = screenHeight - 100;

            // calculate fade
            float alpha = Mth.clamp(smoothedProgress, 0.0f, 1.0f);

            int backgroundColor = ARGB.color(Math.min(alpha, 0.75f), 0x28344f);
            int textColor = ARGB.color(alpha, 0xFFFFFF);

			graphics.fill(actualX, actualY, actualX + boxWidth, actualY + boxHeight, backgroundColor);

            int currentY = actualY + paddingY;
            for (FormattedCharSequence line : lines) {
                graphics.text(client.font, line, actualX + paddingX, currentY, textColor);
                currentY += client.font.lineHeight + linePadding;
            }
        };
    }

    private static float getSmoothedProgress(float exactTick) {
        float animationProgress = 1.0f;

        if (exactTick < HudPromptState.ANIM_DURATION_TICKS && HudPromptState.startAnimation) {
            animationProgress = exactTick / HudPromptState.ANIM_DURATION_TICKS;
        } else if (exactTick > HudPromptState.totalDurationTicks - HudPromptState.ANIM_DURATION_TICKS && HudPromptState.endAnimation) {
            animationProgress = (HudPromptState.totalDurationTicks - exactTick) / HudPromptState.ANIM_DURATION_TICKS;
        }

        return Mth.sin(animationProgress * (float)Math.PI / 2.0f);
    }

}
