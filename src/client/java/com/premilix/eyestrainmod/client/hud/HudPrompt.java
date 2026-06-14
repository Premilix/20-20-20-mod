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
            int maxTextWidth = 180;
            int paddingX = 7;
            int paddingY = 10;
            int boxWidth = maxTextWidth + (paddingX * 2);

            List<FormattedCharSequence> lines = client.font.split(prompt, maxTextWidth);
            int lineCount = Mth.absMax(lines.size(), 2);
            int linePadding = 8;
            int boxHeight = lineCount * client.font.lineHeight + (lineCount - 1) * linePadding +  (paddingY * 2);

            // interpolate the X position based on progress
            int actualX = screenWidth - (int)(smoothedProgress * boxWidth);
            int actualY = screenHeight - 100;

            // calculate fade
            float alpha = Mth.clamp(smoothedProgress, 0.0f, 1.0f);

            int backgroundColor = ARGB.color(Math.min(alpha, 0.75f), 0x28344f); // dark blue
            int dividerColor = ARGB.color(alpha, 0x3EA6FF); // bright cyan
            int trackColor = ARGB.color(alpha, 0x486FAD); // muddy cyan for the line behind the progress bar
            int textColor = ARGB.color(alpha, 0xFFFFFF); // white

            // background box
			graphics.fill(actualX, actualY, actualX + boxWidth, actualY + boxHeight, backgroundColor);

            // divider line
            int dividerHeight = 2;
            int dividerY = actualY + paddingY + client.font.lineHeight + linePadding/2;
            int span = boxWidth - (int)(getTotalSmoothedProgress(exactTick) * boxWidth);

            // divider track
            graphics.fill(actualX, dividerY, actualX + boxWidth, dividerY + dividerHeight, trackColor);
            // moving progress bar
            graphics.fill(actualX, dividerY, actualX + span, dividerY + dividerHeight, dividerColor);

            int currentY = actualY + paddingY;

            // centered text
            FormattedCharSequence line = !lines.isEmpty() ? lines.getFirst() : FormattedCharSequence.EMPTY;
            int textWidth = client.font.width(line);
            int startX = actualX + (boxWidth - textWidth) / 2;

            graphics.text(client.font, line, startX, currentY, textColor, false);

            // right-aligned text
            currentY += client.font.lineHeight + linePadding + dividerHeight + 2;
            line = lines.size() > 1 ? lines.get(1) : FormattedCharSequence.EMPTY;
            textWidth = client.font.width(line);
            startX = actualX + boxWidth - textWidth - paddingX;

            graphics.text(client.font, line, startX, currentY, textColor, false);

            // left-aligned text
            for (int i = 2; i < lines.size(); i++) {
                currentY += client.font.lineHeight + linePadding / 2;
                graphics.text(client.font, lines.get(i), actualX + paddingX, currentY, textColor, false);
            }
        };
    }

    /**
     * Get smoothed progress for the sliding animation across {@code HudPromptState.ANIM_DURATION_TICKS}
     * @param exactTick exact tick in animation
     * @return smoothed progress between 0 and 1
     */
    private static float getSmoothedProgress(float exactTick) {
        float animationProgress = 1.0f;

        if (exactTick < HudPromptState.ANIM_DURATION_TICKS && HudPromptState.startAnimation) {
            animationProgress = exactTick / HudPromptState.ANIM_DURATION_TICKS;
        } else if (exactTick > HudPromptState.totalDurationTicks - HudPromptState.ANIM_DURATION_TICKS && HudPromptState.endAnimation) {
            animationProgress = (HudPromptState.totalDurationTicks - exactTick) / HudPromptState.ANIM_DURATION_TICKS;
        }

        return Mth.sin(animationProgress * (float)Math.PI / 2.0f);
    }

    /**
     * Get smoothed progress across the entire duration for the divider line animation
     * @param exactTick exact tick in animation
     * @return smoothed progress between 0 and 1
     */
    private static float getTotalSmoothedProgress(float exactTick) {
        return exactTick / HudPromptState.totalDurationTicks;
    }

}
