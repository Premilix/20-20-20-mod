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
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(EyestrainMod.MOD_ID, "eyestrain_mod_keybinds")
    );

    public static final KeyMapping OPEN_CONFIG_KEY = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.eyestrain-mod.open_config",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_J,
                    CATEGORY
            )
    );

    public static final KeyMapping BREAK_KEY = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.eyestrain-mod.break_key",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_Y,
                    CATEGORY
            )
    );

    public static final KeyMapping SKIP_KEY = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.eyestrain-mod.skip_key",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_N,
                    CATEGORY
            )
    );


    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
           while(OPEN_CONFIG_KEY.consumeClick()) {
               // avoid enabling narrator by checking Control keybind
               boolean isControlPressed = GLFW.glfwGetKey(client.getWindow().handle(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS
                       || GLFW.glfwGetKey(client.getWindow().handle(), GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;

               if (!isControlPressed && !(client.screen instanceof ConfigScreen)) {
                   client.setScreen(
                           new ConfigScreen(Component.translatable("eyestrain-mod.config.title"), client.screen)
                   );
               }
           }

            while(BREAK_KEY.consumeClick()) {
                EyestrainModClient.toggleBreak();
            }

            while(SKIP_KEY.consumeClick()) {
                EyestrainModClient.skipBreak();
            }
        });
    }
}
