package Jaxaar.BARSOverlay.listeners

import Jaxaar.BARSOverlay.CustomFunctionality.MovementInputFromMod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import Jaxaar.BARSOverlay.BarsOverlayMod.{getShowOverlayKey, mc, setAPIKey}
import Jaxaar.BARSOverlay.DataStructures.HypixelPlayerData
import Jaxaar.BARSOverlay.OverlayManager.{clearPlayers, getListOfPlayers, players, updateCurPlayersDict}
import Jaxaar.BARSOverlay.Utils.ScoreboardSidebarReader
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.settings.KeyBinding
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.{ChatComponentTranslation, MovementInputFromOptions}
import net.minecraftforge.fml.client.registry.ClientRegistry
import org.lwjgl.input.Keyboard
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConverters.collectionAsScalaIterableConverter

object HotkeyShortcuts{

	val showOverlayKeybind = new KeyBinding("Toggle Overlay", Keyboard.KEY_RBRACKET, "BARS Overlay")

	def registerKeybinds = {
		ClientRegistry.registerKeyBinding(showOverlayKeybind)
	}


	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onKeyStroke(event: InputEvent.KeyInputEvent): Unit = {
		if(showOverlayKeybind.isKeyDown && !Keyboard.isRepeatEvent) {
			updateCurPlayersDict
		}

		if((Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ||  Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) && Keyboard.isKeyDown(Keyboard.KEY_Z) && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) && !Keyboard.isRepeatEvent){
			clearPlayers()
			mc.thePlayer.addChatMessage(new ChatComponentTranslation("Player Cache cleared"))
		}

//		if(Keyboard.isKeyDown(Keyboard.KEY_V)) {
////			mc.thePlayer.addChatMessage(new ChatComponentTranslation("V"))
//			val scoreboard = mc.theWorld.getScoreboard
////			val sideBarName = scoreboard.getObjectiveInDisplaySlot(1).getName
////			println(scoreboard.getObjectiveInDisplaySlot(1).getDisplayName)
////			println(sideBarName)
////			val score = scoreboard.getValueFromObjective(sideBarName, scoreboard.getObjective(sideBarName))
////			println(scoreboard.getObjectiveNames)
////
//////			scoreboard.getScores.asScala.foreach((str) => {
//////				println(str.getPlayerName)
//////			})
////			scoreboard.getScores.asScala.foreach((score1) => {
////				val scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName)
////				println(score1.getScorePoints)
////				val s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName)
////				println(s1)
////			})
//
//
////			println(score)
////			println(score.getPlayerName)
//
////			println(ScoreboardSidebarReader.toString)
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

