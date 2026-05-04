package com.premilix.eyestrainmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.chat.Component;

public class EyestrainModClient implements ClientModInitializer {
	private int ticksUntilRest = 24000;
	private int restTicksRemaining = 0;
	private int secondCount = 0;
	private final String restMessage = "You should rest your eyes for another: ";

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			// TODO: add key mappings
			if (client.isPaused()) return;

			if (restTicksRemaining > 0) {
				int newSecondCount = restTicksRemaining / 20;

				if (client.player != null && newSecondCount != secondCount) {
					client.player.sendSystemMessage(Component.literal(restMessage + newSecondCount + "s"));
					secondCount = newSecondCount;
				}

				restTicksRemaining--;

				if (restTicksRemaining == 0) {
					if (client.player != null) {
						client.player.sendSystemMessage(Component.literal("Eye break over. Good job!"));
					}
					ticksUntilRest = 24000;
				}
			}
			else {
				ticksUntilRest--;
				if (ticksUntilRest == 0) {
					restTicksRemaining = 400;
				}
			}
		});

		// TODO: add HUD elements for break reminder instead of messages
	}
}