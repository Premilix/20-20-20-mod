package com.premilix.eyestrainmod.client.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class ConfigScreen extends Screen {
    // TODO: add parent screen to be able to return to on onClose()
    public ConfigScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        Button buttonWidget = Button.builder(Component.literal("Test Button!"), _ -> {
            this.minecraft.getToastManager().addToast(
                    SystemToast.multiline(this.minecraft, SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                            Component.nullToEmpty("Hello world!"), Component.nullToEmpty("This is an example toast!"))
            );
        }).bounds(40, 40, 120, 20).build();

        this.addRenderableWidget(buttonWidget);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        graphics.text(this.font, "Test button", 40, 40 - this.font.lineHeight - 10, 0xFFFFFF, true);
    }
}
