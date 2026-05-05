package com.premilix.eyestrainmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;

public class EyestrainModClient implements ClientModInitializer {
	private int ticksUntilRest = 200;
	private int restTicksRemaining = 0;
	private int displayedSecondCount = 0;

    @Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			// TODO: add key mappings
			if (client.isPaused() || client.player == null) return;

			manageTicks(client);
		});

		// TODO: add HUD elements for break reminder instead of messages
	}

	private void manageTicks(Minecraft client) {
		if (restTicksRemaining > 0) {
			int newSecondCount = Mth.ceil(((double) restTicksRemaining / 20));

			if (client.player != null && newSecondCount != displayedSecondCount) {
                String restMessage = String.format("[20 20 20 Mod] You should rest your eyes for another: %ds", newSecondCount);
                client.player.sendSystemMessage(Component.literal(restMessage));
				displayedSecondCount = newSecondCount;

				client.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_PLING.value(), 1.0F, 0.5F));
			}

			restTicksRemaining--;

			if (restTicksRemaining == 0) {
				if (client.player != null) {
					client.player.sendSystemMessage(Component.literal("[20 20 20 Mod] Eye break over. Good job!"));
				}
				ticksUntilRest = 200;
				// play sound
				client.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_CHIME.value(), 1.0F, 1.0F));
			}
		}
		else {
			ticksUntilRest--;

			if (ticksUntilRest == 0) {
				restTicksRemaining = 400;
				// play sound
				client.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_CHIME.value(), 1.0F, 1.0F));

			}
		}
	}
}