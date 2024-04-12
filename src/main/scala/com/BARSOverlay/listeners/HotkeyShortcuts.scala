package com.BARSOverlay.listeners

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import com.BARSOverlay.{BarsOverlayMod, OverlayManager}
import com.BARSOverlay.BarsOverlayMod.mc
import com.BARSOverlay.CustomFunctionality.MovementInputFromMod
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.{ChatComponentTranslation, MovementInputFromOptions}
import org.lwjgl.input.Keyboard
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object HotkeyShortcuts{
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onKeyStroke(event: InputEvent.KeyInputEvent) = {
		println("Event!")
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
			mc.thePlayer.addChatMessage(new ChatComponentTranslation("*Display Fancy UI*"))
			println("Tab Down")
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
			mc.thePlayer.addChatMessage(new ChatComponentTranslation("R"))
			OverlayManager.printListToChat
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_G)){
			mc.thePlayer.addChatMessage(new ChatComponentTranslation("G"))
//			OverlayManager.ShowOverlay
		}
	}
}

































//
//
////		Also needs to handle release... Like hide the menu on release
//if(Keyboard.isKeyDown(Keyboard.KEY_R)){
//	BarsOverlayMod.mc.thePlayer.addChatMessage((new ChatComponentTranslation("R")))
//	println("RRRRRR")
//	BarsOverlayMod.mc.thePlayer.movementInput = new MovementInputFromMod(1,0,true,false)
//}
//
//	if(Keyboard.isKeyDown(Keyboard.KEY_T)){
//	BarsOverlayMod.mc.thePlayer.addChatMessage((new ChatComponentTranslation("T")))
//	println("t")
//	BarsOverlayMod.mc.thePlayer.movementInput = new MovementInputFromOptions(BarsOverlayMod.mc.gameSettings);
//}

