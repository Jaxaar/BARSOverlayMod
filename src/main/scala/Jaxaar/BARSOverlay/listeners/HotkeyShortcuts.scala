package Jaxaar.BARSOverlay.listeners

import Jaxaar.BARSOverlay.CustomFunctionality.MovementInputFromMod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import Jaxaar.BARSOverlay.HypixelPlayerData
import Jaxaar.BARSOverlay.BarsOverlayMod.{getShowOverlayKey, mc, setAPIKey}
import Jaxaar.BARSOverlay.OverlayManager.{getListOfPlayers, players, playersDict, updatePlayerList}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.{ChatComponentTranslation, MovementInputFromOptions}
import org.lwjgl.input.Keyboard
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConverters.collectionAsScalaIterableConverter

object HotkeyShortcuts{

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onKeyStroke(event: InputEvent.KeyInputEvent): Unit = {
		if(Keyboard.isKeyDown(getShowOverlayKey) && !Keyboard.isRepeatEvent) {
			updatePlayerList()
		}
		if((Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ||  Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) && Keyboard.isKeyDown(Keyboard.KEY_Z) && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) && !Keyboard.isRepeatEvent){
			playersDict = Map()
			mc.thePlayer.addChatMessage(new ChatComponentTranslation("Player Cache cleared"))
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_V)) {
			mc.thePlayer.addChatMessage(new ChatComponentTranslation("V"))
			mc.
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

