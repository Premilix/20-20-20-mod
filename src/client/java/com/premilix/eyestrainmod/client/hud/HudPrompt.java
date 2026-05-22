package com.premilix.eyestrainmod.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.joml.Matrix3x2fStack;

public class HudPrompt {
    public static HudElement render() {
        return (graphics, _) -> {
            int ticks = HudPromptState.getTicksRemaining();
            Component prompt = HudPromptState.getContent();

            if (ticks <= 0 || prompt == null) return;

            Matrix3x2fStack matrices = graphics.pose();
            matrices.pushMatrix();

            // TODO: fix rectangle color
            // Fill a standard rectangle (x1, y1, x2, y2, color)
			graphics.fill(10, 10, 30, 30, 0x42668a);
            graphics.text(Minecraft.getInstance().font, prompt, 10, 10, 0xFFFFFFFF);

            matrices.popMatrix();
        };
    }

}
