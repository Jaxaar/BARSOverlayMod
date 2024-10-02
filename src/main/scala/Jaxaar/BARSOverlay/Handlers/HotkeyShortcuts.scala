package Jaxaar.BARSOverlay.Handlers

import Jaxaar.BARSOverlay.CustomFunctionality.MovementInputFromMod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import Jaxaar.BARSOverlay.BarsOverlayMod.mc
import Jaxaar.BARSOverlay.DataStructures.HypixelPlayerData
import Jaxaar.BARSOverlay.OverlayManager.{clearPlayers, clearSearchedPlayers, getListOfPlayers, logger, onShowOverlayKeyPress, players, searchedPlayers, updateCurPlayersDict}
import Jaxaar.BARSOverlay.Utils.ScoreboardSidebarReader
import Jaxaar.BARSOverlay.Utils.SoundHandler.{playESound, playTBellSound, playThatsTheGameSound}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.settings.KeyBinding
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.{ChatComponentTranslation, MovementInputFromOptions}
import net.minecraftforge.fml.client.registry.ClientRegistry
import org.lwjgl.input.Keyboard
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object HotkeyShortcuts{

	val showOverlayKeybind = new KeyBinding("Toggle Overlay", Keyboard.KEY_RBRACKET, "BARS Overlay")

	def registerKeybinds = {
		ClientRegistry.registerKeyBinding(showOverlayKeybind)
	}


	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onKeyStroke(event: InputEvent.KeyInputEvent): Unit = {
		if (showOverlayKeybind.isKeyDown && !Keyboard.isRepeatEvent) {
			onShowOverlayKeyPress()
		}

		if (holdingCtrlAndShift && Keyboard.isKeyDown(Keyboard.KEY_Z) && !Keyboard.isRepeatEvent) {
			clearPlayers()
			mc.thePlayer.addChatMessage(new ChatComponentTranslation("Player Cache cleared"))
		}
		if (holdingCtrlAndShift && Keyboard.isKeyDown(Keyboard.KEY_C) && !Keyboard.isRepeatEvent) {
			clearSearchedPlayers()
			mc.thePlayer.addChatMessage(new ChatComponentTranslation("Searched Players cleared"))
		}
	}

	def holdingCtrlAndShift: Boolean = {
		(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
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

