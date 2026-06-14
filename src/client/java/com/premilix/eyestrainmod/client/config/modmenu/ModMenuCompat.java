package com.premilix.eyestrainmod.client.config.modmenu;

import com.premilix.eyestrainmod.client.hud.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.network.chat.Component;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(Component.translatable("eyestrain-mod.config.title"), parent);
    }
}
