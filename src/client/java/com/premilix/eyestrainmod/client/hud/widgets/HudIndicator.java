package com.premilix.eyestrainmod.client.hud.widgets;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix3x2fStack;

public class HudIndicator {
    private static final float ANIM_DURATION_TICKS = 20.0f;

    private static boolean visible = false;
    private static float totalTickProgress = 0;

    public static void setVisible() {
        if (visible) return;

        totalTickProgress = 0;
        visible = true;
    }

    public static void setInvisible() {
        if (!visible) return;

        visible = false;
        totalTickProgress = 0;
    }

    public static HudElement render() {
        return (graphics, tickDelta) -> {
            if (!visible) return;

            Matrix3x2fStack matrices = graphics.pose();
            matrices.pushMatrix();

            if (!Minecraft.getInstance().isPaused() && Minecraft.getInstance().player != null) {
                totalTickProgress += tickDelta.getGameTimeDeltaPartialTick(false);
            }

            // TODO: add fade in/out
            float scaleAmount = Mth.sin(totalTickProgress / 30F) / 5F + 1.2F;

            int screenWidth = graphics.guiWidth();
            int screenHeight = graphics.guiHeight();

            int iconSize = 16;
            int margin = 10;
            int x = screenWidth - iconSize - margin;
            int y = screenHeight - iconSize - margin;

            ItemStack iconItem = Items.SPYGLASS.getDefaultInstance();

            matrices.translate(x + (iconSize / 2F), y + (iconSize / 2F));
            matrices.scale(scaleAmount, scaleAmount);

            graphics.fakeItem(iconItem, -iconSize / 2, -iconSize / 2);

            matrices.popMatrix();
        };
    }

}
