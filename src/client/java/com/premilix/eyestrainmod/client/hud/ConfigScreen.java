package com.premilix.eyestrainmod.client.hud;

import com.premilix.eyestrainmod.EyestrainMod;
import com.premilix.eyestrainmod.client.config.Config;
import com.premilix.eyestrainmod.client.hud.widgets.BetweenRestSlider;
import com.premilix.eyestrainmod.client.hud.widgets.RestSlider;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ConfigScreen extends Screen {

    private HeaderAndFooterLayout layout;

    private int draftRestSeconds;
    private int draftMinutesBetween;
    private boolean draftSoundNotifications;

    private RestSlider restSlider;
    private BetweenRestSlider betweenRestSlider;
    private CycleButton<Boolean> soundsToggle;

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

        GridLayout settings = generateContents();
        this.layout.addToContents(settings);

        LinearLayout footer = generateFooter();
        this.layout.addToFooter(footer);

        this.layout.visitWidgets(this::addRenderableWidget);
        this.layout.arrangeElements();
    }

    private @NonNull GridLayout generateContents() {
        GridLayout grid = new GridLayout().columnSpacing(40).rowSpacing(10);

        // ROW 0
        StringWidget restLabel = new StringWidget(Component.translatable("eyestrain-mod.config.rest_seconds"), this.font);
        grid.addChild(restLabel, 0, 0, settings -> settings.alignHorizontallyLeft().alignVerticallyMiddle());

        this.restSlider = new RestSlider(0, 0, 100, 20, draftRestSeconds);
        grid.addChild(this.restSlider, 0, 1, LayoutSettings::alignHorizontallyRight);

        // ROW 1
        StringWidget betweenRestLabel = new StringWidget(Component.translatable("eyestrain-mod.config.minutes_between_break"), this.font);
        grid.addChild(betweenRestLabel, 1, 0, settings -> settings.alignHorizontallyLeft().alignVerticallyMiddle());

        this.betweenRestSlider = new BetweenRestSlider(0, 0, 100, 20, draftMinutesBetween);
        grid.addChild(this.betweenRestSlider, 1, 1, LayoutSettings::alignHorizontallyRight);

        // ROW 2
        StringWidget soundLabel = new StringWidget(Component.translatable("eyestrain-mod.config.sound_notifications"), this.font);
        grid.addChild(soundLabel, 2, 0, settings -> settings.alignHorizontallyLeft().alignVerticallyMiddle());

        this.soundsToggle = CycleButton.onOffBuilder(this.draftSoundNotifications)
                .create(0, 0, 100, 20, Component.translatable("eyestrain-mod.config.sounds"));
        grid.addChild(this.soundsToggle, 2, 1, LayoutSettings::alignHorizontallyRight);

        return grid;
    }

    private @NonNull LinearLayout generateFooter() {
        LinearLayout footer = LinearLayout.horizontal().spacing(5);

        footer.addChild(Button.builder(Component.translatable("eyestrain-mod.config.reset"), _ -> this.resetDefaults())
                .tooltip(Tooltip.create(Component.translatable("eyestrain-mod.tooltip.reset_defaults")))
                .build());
        footer.addChild(Button.builder(Component.translatable("eyestrain-mod.config.cancel"), _ -> this.onClose(false))
                .tooltip(Tooltip.create(Component.translatable("eyestrain-mod.tooltip.cancel")))
                .build());
        footer.addChild(Button.builder(Component.translatable("eyestrain-mod.config.save"), _ -> this.onClose(true))
                .tooltip(Tooltip.create(Component.translatable("eyestrain-mod.tooltip.save")))
                .build());
        return footer;
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
        this.draftSoundNotifications = this.soundsToggle.getValue();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

}
