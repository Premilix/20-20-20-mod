package com.premilix.eyestrainmod.client.hud;

import com.premilix.eyestrainmod.EyestrainMod;
import com.premilix.eyestrainmod.client.config.Config;
import com.premilix.eyestrainmod.client.hud.widgets.BetweenRestSlider;
import com.premilix.eyestrainmod.client.hud.widgets.RestSlider;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ConfigScreen extends Screen {

    private HeaderAndFooterLayout layout;

    private int draftRestSeconds;
    private int draftMinutesBetween;
    private boolean draftSoundNotifications;

    private RestSlider restSlider;
    private BetweenRestSlider betweenRestSlider;
    private Checkbox soundsCheckbox;

    @Nullable
    private final Screen parent;

    public ConfigScreen(Component title, @Nullable Screen parent) {
        super(title);
        this.parent = parent;

        Config config = Config.getInstance();
        this.draftRestSeconds = config.getRestSeconds();
        this.draftMinutesBetween = config.getMinutesBetweenBreak();
        this.draftSoundNotifications = config.isSoundNotifications();
    }

    @Override
    protected void init() {
        this.layout = new HeaderAndFooterLayout(this);
        this.layout.addToHeader(new StringWidget(this.title, this.font));

        LinearLayout settings = LinearLayout.vertical().spacing(10);

        for (LinearLayout row : this.generateSettings()) {
            settings.addChild(row);
        }
        this.layout.addToContents(settings);

        LinearLayout footer = LinearLayout.horizontal().spacing(5);

        footer.addChild(Button.builder(Component.literal("Reset"), _ -> this.resetDefaults())
                .tooltip(Tooltip.create(Component.literal("Reset to defaults")))
                .build());
        footer.addChild(Button.builder(Component.literal("Cancel"), _ -> this.onClose(false))
                .tooltip(Tooltip.create(Component.literal("Exit the configuration without saving")))
                .build());
        footer.addChild(Button.builder(Component.literal("Save"), _ -> this.onClose(true))
                .tooltip(Tooltip.create(Component.literal("Save and exit the configuration")))
                .build());

        this.layout.addToFooter(footer);

        this.layout.visitWidgets(this::addRenderableWidget);
        this.layout.arrangeElements();
    }

    private void resetDefaults() {
        this.draftRestSeconds = 20;
        this.draftMinutesBetween = 20;
        this.draftSoundNotifications = true;
        this.rebuildWidgets();
    }

    private void onClose(boolean save) {
        if (save) {
            this.updateDraftValues();
            Config.updateConfigFromUI(this.draftRestSeconds, this.draftMinutesBetween, this.draftSoundNotifications);
            EyestrainMod.LOGGER.info("Saved config with new values {} seconds, {} minutes, {} sounds", this.draftRestSeconds, this.draftMinutesBetween, this.draftSoundNotifications);
        }
        this.onClose();
    }

    private void updateDraftValues() {
        this.draftRestSeconds = this.restSlider.getSeconds();
        this.draftMinutesBetween = this.betweenRestSlider.getMinutes();
        this.draftSoundNotifications = this.soundsCheckbox.selected();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    /**
     * Creates the individual mod setting rows
     * @return a list of LinearLayouts containing the mod settings
     */
    private List<LinearLayout> generateSettings() {
        LinearLayout row1 = LinearLayout.horizontal().spacing(5);

        StringWidget restLabel = new StringWidget(Component.literal("Rest duration in seconds"), this.font);
        this.restSlider = new RestSlider(0, 0, 100, 20, draftRestSeconds);

        row1.addChild(restLabel, LayoutSettings::alignVerticallyMiddle);
        row1.addChild(this.restSlider);

        LinearLayout row2 = LinearLayout.horizontal().spacing(5);

        StringWidget betweenRestLabel = new StringWidget(Component.literal("Minutes between break"), this.font);
        this.betweenRestSlider = new BetweenRestSlider(0, 0, 100, 20, draftMinutesBetween);

        row2.addChild(betweenRestLabel, LayoutSettings::alignVerticallyMiddle);
        row2.addChild(this.betweenRestSlider);

        LinearLayout row3 = LinearLayout.horizontal().spacing(5);

        StringWidget soundLabelWidget = new StringWidget(Component.literal("Play sounds to help guide the break"), this.font);
        this.soundsCheckbox = Checkbox.builder(Component.empty(), this.font).selected(draftSoundNotifications).build();

        row3.addChild(soundLabelWidget, LayoutSettings::alignVerticallyMiddle);
        row3.addChild(this.soundsCheckbox);

        return List.of(row1, row2, row3);
    }
}
