package com.premilix.eyestrainmod.client;

import com.premilix.eyestrainmod.EyestrainMod;
import com.premilix.eyestrainmod.client.config.Config;
import com.premilix.eyestrainmod.client.hud.HudPrompt;
import com.premilix.eyestrainmod.client.hud.HudPromptState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EyestrainModClient implements ClientModInitializer {
	public static int REST_TICKS = Config.getInstance().getRestSeconds() * 20;
	public static int TICKS_BETWEEN_BREAK = Config.getInstance().getMinutesBetweenBreak() * 60 * 20;
	public static boolean SOUND_NOTIFICATIONS = Config.getInstance().isSoundNotifications();

	private static int ticksUntilRest = TICKS_BETWEEN_BREAK;
	private static int restTicksRemaining = 0;
	private static int displayedSecondCount = 0;

	private static boolean breakInitiated = false;

    @Override
	public void onInitializeClient() {
		Config.load();

		HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(EyestrainMod.MOD_ID, "hud_prompt"), HudPrompt.render());

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.isPaused() || client.player == null) return;

			HudPromptState.tick();
			manageTicks(client);
		});
	}

	private void manageTicks(Minecraft client) {
		if (restTicksRemaining > 0) {
			if (!breakInitiated) {
				if (client.player != null) {
					Component restMessage = Component.literal("You can start the eye break now!\n")
							.append(Objects.requireNonNull(KeyMapping.get("key.eyestrain-mod.break_key")).getTranslatedKeyMessage())
							.append(": START      ")
							.append(Objects.requireNonNull(KeyMapping.get("key.eyestrain-mod.snooze_key")).getTranslatedKeyMessage())
							.append(": SKIP");
					if (!Objects.equals(HudPromptState.getContent(), restMessage)) HudPromptState.showPrompt(restMessage, 5, true, true);

				}
				return;
			}

			int newSecondCount = Mth.ceil(((double) restTicksRemaining / 20));

			if (client.player != null && newSecondCount != displayedSecondCount) {
                Component restMessage = Component.literal(String.format("Rest your eyes for another: %ds", newSecondCount))
						.append("\n")
						.append(Objects.requireNonNull(KeyMapping.get("key.eyestrain-mod.break_key")).getTranslatedKeyMessage())
						.append(": CANCEL");

                HudPromptState.showPrompt(restMessage, 1, !HudPromptState.isActive(), false);

				displayedSecondCount = newSecondCount;

				notifyPlayer(client, SoundEvents.NOTE_BLOCK_PLING.value(), 0.5F);
			}

			restTicksRemaining--;

			if (restTicksRemaining == 0) {
				HudPromptState.showPrompt(Component.literal("Eye break over. Good job!"), 5, false, true);

				ticksUntilRest = TICKS_BETWEEN_BREAK;
				breakInitiated = false;

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
		if (SOUND_NOTIFICATIONS) {
			client.getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0F, volume));
		}
	}

	public static void reloadConfigParameters() {
		TICKS_BETWEEN_BREAK = Config.getInstance().getMinutesBetweenBreak() * 60 * 20;
		REST_TICKS = Config.getInstance().getRestSeconds() * 20;
		if (ticksUntilRest > 0) {
			ticksUntilRest = TICKS_BETWEEN_BREAK;
		}

		SOUND_NOTIFICATIONS = Config.getInstance().isSoundNotifications();

	}

	public static void toggleBreak() {
		if (breakInitiated) {
			breakInitiated = false;
			restTicksRemaining = 0;
			ticksUntilRest = TICKS_BETWEEN_BREAK;

			HudPromptState.showPrompt(Component.literal("Eye break cancelled."), 5, false, true);
		}
		else if (ticksUntilRest == 0 && restTicksRemaining > 0) {
			breakInitiated = true;
		}
	}

	public static void snoozeBreak() {
		if (!breakInitiated && ticksUntilRest == 0 && restTicksRemaining > 0) {
			Component message = Component.literal("You have snoozed this eye break.");

			HudPromptState.showPrompt(message, 5, !HudPromptState.isActive(), true);

			restTicksRemaining = 0;
			ticksUntilRest = TICKS_BETWEEN_BREAK;
		}
	}
}