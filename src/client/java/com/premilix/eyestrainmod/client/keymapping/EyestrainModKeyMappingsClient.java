package com.premilix.eyestrainmod.client.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import com.premilix.eyestrainmod.EyestrainMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class EyestrainModKeyMappingsClient implements ClientModInitializer {
    KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(EyestrainMod.MOD_ID, "eyestrain_mod_keybinds")
    );

    KeyMapping openConfigKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.eyestrain-mod.open_config",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_B,
                    CATEGORY
            )
    );


    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
           while(openConfigKey.consumeClick()) {
               if (client.player != null) {
                   client.player.sendSystemMessage(Component.literal("[20 20 20] Mod: Config key pressed."));
               }
           }
        });
    }
}
