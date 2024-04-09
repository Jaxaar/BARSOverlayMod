package com.BARSOverlay.listeners

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import com.BARSOverlay.BarsOverlayMod
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentTranslation
import org.lwjgl.input.Keyboard

object HotkeyShortcuts{
	@SubscribeEvent
	def onKeyStroke(event: InputEvent.KeyInputEvent) = {
		println("Event!")
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
			BarsOverlayMod.mc.thePlayer.addChatMessage(new ChatComponentTranslation("*Display Fancy UI*"))
			println("Tab Down")
		}

//		Also needs to handle release... Like hide the menu on release
		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
			BarsOverlayMod.mc.thePlayer.addChatMessage((new ChatComponentTranslation("R")))
			println("RRRRRR")
		}

	}
}



