package com.premilix.eyestrainmod.client.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import com.premilix.eyestrainmod.EyestrainMod;
import com.premilix.eyestrainmod.client.EyestrainModClient;
import com.premilix.eyestrainmod.client.hud.ConfigScreen;
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
                    GLFW.GLFW_KEY_J,
                    CATEGORY
            )
    );

    KeyMapping breakKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.eyestrain-mod.break_key",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_Y,
                    CATEGORY
            )
    );

    KeyMapping snoozeKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.eyestrain-mod.snooze_key",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_N,
                    CATEGORY
            )
    );


    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
           while(openConfigKey.consumeClick()) {
               // avoid enabling narrator by checking Control keybind
               boolean isControlPressed = GLFW.glfwGetKey(client.getWindow().handle(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS
                       || GLFW.glfwGetKey(client.getWindow().handle(), GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;

               if (!isControlPressed && !(client.screen instanceof ConfigScreen)) {
                   client.setScreen(
                           new ConfigScreen(Component.literal("20 20 20 Mod Config"), client.screen)
                   );
               }
           }

            while(breakKey.consumeClick()) {
                EyestrainModClient.toggleBreak();
            }

            while(snoozeKey.consumeClick()) {
                EyestrainModClient.snoozeBreak();
            }
        });
    }
}
