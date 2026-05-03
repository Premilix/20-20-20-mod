package com.premilix.eyestrainmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class EyestrainModClient implements ClientModInitializer {
	private int ticksUntilRest = 24000;
	private int restTicksRemaining = 0;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			// TODO: add key mappings
			if (restTicksRemaining > 0) {
				restTicksRemaining--;

				if (restTicksRemaining == 0) {
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

		// TODO: add HUD elements for break reminder
	}
}