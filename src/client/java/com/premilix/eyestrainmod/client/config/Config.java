package com.premilix.eyestrainmod.client.config;


import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.premilix.eyestrainmod.EyestrainMod;
import com.premilix.eyestrainmod.client.EyestrainModClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("eyestrainmod.json");

    private final int restSeconds;
    private final int minutesBetweenBreak;
    private final boolean soundNotifications;

    private static Config INSTANCE = new Config(20, 20, true);

    private Config(int restSeconds, int minutesBetweenBreak, boolean soundNotifications) {
        this.restSeconds = restSeconds;
        this.minutesBetweenBreak = minutesBetweenBreak;
        this.soundNotifications = soundNotifications;
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public int getRestSeconds() {
        return restSeconds;
    }

    public int getMinutesBetweenBreak() {
        return minutesBetweenBreak;
    }

    public boolean isSoundNotifications() {
        return soundNotifications;
    }

    public static void load() {
        if (Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath)) {
                JsonElement json = JsonParser.parseReader(reader);
                DataResult<Config> result = Config.CODEC.parse(JsonOps.INSTANCE, json);
                result.resultOrPartial(Config::afterParseError)
                        .ifPresent(config -> Config.INSTANCE = config);
            } catch (IOException e) {
                EyestrainMod.LOGGER.error("Failed to read config.", e);
            }

            EyestrainModClient.reloadConfigParameters();
        }
        else {
            save();
        }
    }

    public static void save() {
        DataResult<JsonElement> result = Config.CODEC.encodeStart(JsonOps.INSTANCE, Config.INSTANCE);
        result.resultOrPartial(EyestrainMod.LOGGER::error)
                .ifPresent(json -> {
                    try (Writer writer = Files.newBufferedWriter(configPath)) {
                        new GsonBuilder().setPrettyPrinting().create().toJson(json, writer);
                    } catch (IOException e) {
                        EyestrainMod.LOGGER.error("Failed to write config file", e);
                    }
                });

        EyestrainModClient.reloadConfigParameters();
    }

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(5, 60).fieldOf("rest_seconds").forGetter(Config::getRestSeconds),
            Codec.intRange(1, 60).fieldOf("minutes_between_break").forGetter(Config::getMinutesBetweenBreak),
            Codec.BOOL.fieldOf("sound_notifications").forGetter(Config::isSoundNotifications)
        ).apply(instance, Config::new));

    /**
     * Writes the error message of loading the config to the logs.
     * Marks the old/invalid config file by moving it to {@code eyestrainmod_invalid.json}
     * to preserve old user preferences just in case.
     * @param error the error message after trying to parse
     */
    private static void afterParseError(String error) {
        EyestrainMod.LOGGER.error("Failed to load config. Going back to previous correct or default config. " +
                "Your last config will be marked as invalid. Reason: {}", error);
        Path invalidConfigPath = FabricLoader.getInstance().getConfigDir().resolve("eyestrainmod_invalid.json");

        try {
            Files.deleteIfExists(invalidConfigPath);
            Files.move(configPath, invalidConfigPath);
            save();
        } catch (IOException e) {
            EyestrainMod.LOGGER.error("Failed to rename invalid config", e);
        }
    }
}
