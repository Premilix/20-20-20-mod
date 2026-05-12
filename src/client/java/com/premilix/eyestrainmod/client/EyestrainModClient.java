package com.premilix.eyestrainmod.client;

import com.premilix.eyestrainmod.EyestrainMod;
import com.premilix.eyestrainmod.client.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class EyestrainModClient implements ClientModInitializer {
	public static int REST_TICKS = Config.getInstance().getRestSeconds() * 20;
	public static int TICKS_BETWEEN_BREAK = Config.getInstance().getMinutesBetweenBreak() * 60 * 20;

	private static int ticksUntilRest = TICKS_BETWEEN_BREAK;
	private static int restTicksRemaining = 0;
	private static int displayedSecondCount = 0;

    @Override
	public void onInitializeClient() {
		Config.load();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.isPaused() || client.player == null) return;

			manageTicks(client);
		});
	}

	private void manageTicks(Minecraft client) {
		if (restTicksRemaining > 0) {
			int newSecondCount = Mth.ceil(((double) restTicksRemaining / 20));

			if (client.player != null && newSecondCount != displayedSecondCount) {
				// TODO: add HUD elements for break reminder instead of messages
                String restMessage = String.format("[20 20 20 Mod] You should rest your eyes for another: %ds", newSecondCount);
                client.player.sendSystemMessage(Component.literal(restMessage));
				displayedSecondCount = newSecondCount;

				notifyPlayer(client, SoundEvents.NOTE_BLOCK_PLING.value(), 0.5F);
			}

			restTicksRemaining--;

			if (restTicksRemaining == 0) {
				if (client.player != null) {
					client.player.sendSystemMessage(Component.literal("[20 20 20 Mod] Eye break over. Good job!"));
				}
				ticksUntilRest = TICKS_BETWEEN_BREAK;

				notifyPlayer(client, SoundEvents.NOTE_BLOCK_CHIME.value(), 1.0F);
			}
		}
		else {
			ticksUntilRest--;

			if (ticksUntilRest == 0) {
				restTicksRemaining = REST_TICKS;

				notifyPlayer(client, SoundEvents.NOTE_BLOCK_CHIME.value(), 1.0F);

			}
		}
	}

	private void notifyPlayer(@NotNull Minecraft client, SoundEvent sound, float volume) {
		client.getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0F, volume));
	}

	public static void reloadConfigParameters() {
		TICKS_BETWEEN_BREAK = Config.getInstance().getMinutesBetweenBreak() * 60 * 20;
		REST_TICKS = Config.getInstance().getRestSeconds() * 20;
		if (ticksUntilRest > 0) {
			ticksUntilRest = TICKS_BETWEEN_BREAK;
		}
	}
}