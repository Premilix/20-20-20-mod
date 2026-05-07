package com.premilix.eyestrainmod.client.config;


import com.mojang.serialization.Codec;
import com.premilix.eyestrainmod.EyestrainMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Config {
    public static int restTicks = 400; // 20 seconds in ticks (20 * 20)
    public static int ticksBetweenBreak = 24000; // 20 minutes in ticks (20 * 60 * 20)
    public static boolean soundNotifications = true;

    public static void load() {
        // TODO
        Path configPath =  FabricLoader.getInstance().getConfigDir().resolve("eyestrainmod.json");
        // Use gson here
    }

    public static void save() {
        // TODO
    }
}
