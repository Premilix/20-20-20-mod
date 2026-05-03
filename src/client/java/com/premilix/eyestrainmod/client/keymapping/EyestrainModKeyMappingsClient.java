package com.premilix.eyestrainmod.client.keymapping;

import com.premilix.eyestrainmod.EyestrainMod;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class EyestrainModKeyMappingsClient implements ClientModInitializer {
    KeyMapping.Category category = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(EyestrainMod.MOD_ID, "eyestrain_mod_keybinds")
    );

    @Override
    public void onInitializeClient() {

    }
}
