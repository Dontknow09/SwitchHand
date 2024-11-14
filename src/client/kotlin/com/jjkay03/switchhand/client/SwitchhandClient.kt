package com.jjkay03.switchhand.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Arm
import org.lwjgl.glfw.GLFW

class SwitchhandClient : ClientModInitializer {

    private var lastPressed = 0L // Store the last press time in milliseconds
    private val delayMillis = 200L // Delay in milliseconds

    private val mainKeyBind = KeyBindingHelper.registerKeyBinding(
        KeyBinding("Switch Main Hand", GLFW.GLFW_KEY_V, KeyBinding.INVENTORY_CATEGORY)
    )

    override fun onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (mainKeyBind.isPressed && System.currentTimeMillis() - lastPressed > delayMillis) {
                switchMainHand(client)
                lastPressed = System.currentTimeMillis() // Save press to handle delay
            }
        }
    }

    private fun switchMainHand(client: MinecraftClient) {
        client.player?.let { player ->
            // Toggle the player's main arm between left and right
            val newMainArm = if (player.mainArm == Arm.RIGHT) Arm.LEFT else Arm.RIGHT

            // Update the game options
            client.options.mainArm.value = newMainArm
            client.options.write()

            // Play sound
            client.player?.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value(), 0.5f, 1.0f)
        }

    }
}
