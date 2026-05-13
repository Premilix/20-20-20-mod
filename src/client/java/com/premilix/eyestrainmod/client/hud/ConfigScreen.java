package com.premilix.eyestrainmod.client.hud;

import com.premilix.eyestrainmod.client.hud.widgets.RestSlider;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ConfigScreen extends Screen {

    @Nullable
    private final Screen parent;

    public ConfigScreen(Component title, @Nullable Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
//        Button buttonWidget = Button.builder(Component.literal("Test Button!"), _ -> this.minecraft.getToastManager().addToast(
//                SystemToast.multiline(this.minecraft, SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
//                        Component.nullToEmpty("Hello world!"), Component.nullToEmpty("This is an example toast!"))
//        )).bounds(40, 40, 120, 20).build();
//
//
//
//        this.addRenderableWidget(buttonWidget);

        Checkbox checkboxWidget = Checkbox.builder(Component.literal("Play sounds to help guide the break"), this.font).pos(40, 80).build();
        RestSlider restSliderWidget = new RestSlider(40, 120, 100, 20, 20);

        this.addRenderableWidget(checkboxWidget);
        this.addRenderableWidget(restSliderWidget);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

//        graphics.text(this.font, "Test button", 40, 40 - this.font.lineHeight - 10, 0xFFFFFFFF, true);
        graphics.text(this.font, "Rest duration", 40, 120 - this.font.lineHeight - 10, 0xFFFFFFFF, true);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
