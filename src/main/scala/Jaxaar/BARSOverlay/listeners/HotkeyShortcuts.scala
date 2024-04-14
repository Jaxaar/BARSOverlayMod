package Jaxaar.BARSOverlay.listeners

import Jaxaar.BARSOverlay.CustomFunctionality.MovementInputFromMod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import Jaxaar.BARSOverlay.HypixelPlayerData
import Jaxaar.BARSOverlay.BarsOverlayMod.mc
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
	def onKeyStroke(event: InputEvent.KeyInputEvent) = {
//		println("Event!")
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB) && !Keyboard.isRepeatEvent) {
			updatePlayerList()
//			mc.thePlayer.addChatMessage(new ChatComponentTranslation("*Display Fancy UI*"))
//			println("Tab Down")
		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
//			mc.thePlayer.addChatMessage(new ChatComponentTranslation("R"))
//			OverlayManager.printListToChat
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_T) && !Keyboard.isRepeatEvent){
//			mc.thePlayer.addChatMessage(new ChatComponentTranslation("T"))
//			updatePlayerList()
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_G)){
//			mc.thePlayer.addChatMessage(new ChatComponentTranslation("G"))
//			OverlayManager.ShowOverlay
//		}
		if(Keyboard.isKeyDown(Keyboard.KEY_V) && !Keyboard.isRepeatEvent){
			mc.thePlayer.addChatMessage(new ChatComponentTranslation("V"))
			playersDict = Map()
		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_B) && !Keyboard.isRepeatEvent){
//			mc.thePlayer.addChatMessage(new ChatComponentTranslation("B"))
////			players = getListOfPlayers.asScala.toList.map(new HypixelPlayerData(_))
//			println("All Players")
//			players.foreach((x) => {
//				if(x.playerLoaded) {
//					println(x.networkPlayerInfo.getGameProfile.getName)
//					println(x.getStars)
//
//					mc.thePlayer.addChatMessage(new ChatComponentTranslation(x.networkPlayerInfo.getGameProfile.getName))
//					mc.thePlayer.addChatMessage(new ChatComponentTranslation("" + x.getStars))
//				}
//			})
//		}
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

