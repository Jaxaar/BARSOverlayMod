package Jaxaar.BARSOverlay

import Jaxaar.BARSOverlay.GUIComponents.GuiOverlay
import BarsOverlayMod.{MODID, config, mc}
import Jaxaar.BARSOverlay.DataStructures.{HypixelPlayerData, HypixelPlayerDataIsNone}
import Jaxaar.BARSOverlay.Utils.APIRequestHandler.{clearPlayerCache, getPlayerStats}
import Jaxaar.BARSOverlay.Utils.Helpers.{CollectionAsScala, stripColorCodes}
import Jaxaar.BARSOverlay.Handlers.OnChatHandler.{bedwarsGameStarted, gameStarting, handleFunOnChat, handleStatsOnChat, resetGameProgress}
import Jaxaar.BARSOverlay.Utils.ScoreboardSidebarReader.{isBedwarsGame, isHypixel, verifyIsBedwarsGame}
import Jaxaar.BARSOverlay.Handlers.HotkeyShortcuts.showOverlayKeybind
import net.minecraft.client.gui.Gui
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import org.apache.logging.log4j.{LogManager, Logger}

import java.util.{UUID, List => JavaList}
import net.minecraftforge.client.event.ClientChatReceivedEvent


object OverlayManager extends Gui{

	val logger: Logger = LogManager.getLogger(MODID);
	val overlayRenderer: GuiOverlay.type = GuiOverlay

	private var curPlayers: List[HypixelPlayerData] = List()
	def players: List[HypixelPlayerData] = masterSort(curPlayers).slice(0,Math.min(curPlayers.length, 20))


	def getListOfPlayers: List[NetworkPlayerInfo] = {
		 CollectionAsScala(mc.thePlayer.sendQueue.getPlayerInfoMap)
	}

	def updateCurPlayersDict(): Unit = {
		if (!verifyIsBedwarsGame) {return;}

		val newLst = getListOfPlayers.view
		  .map(x => (x.getGameProfile.getId, getPlayerStats(x.getGameProfile.getId)))
		  .filter(_._2 != null)
		  .filter(_._1.version() != 2)
		  .map(x =>{
			  if(x._2.isDefined){
				  new HypixelPlayerData(x._1, x._2.get.player)
			  }
			  else {
				  new HypixelPlayerDataIsNone(x._1)
			  }
		}).toList
		curPlayers = newLst
	}

	def masterSort(lst: List[HypixelPlayerData]): List[HypixelPlayerData] = {
//		Sort by Stats
		lst.sortBy(sortHypixelPlayersByStats).reverse
//		Sort by team Char - Specifically makes use of Bedwars team styling
		  .sortBy(x => stripColorCodes(x.getTeamColorStyling))
	}

	def sortHypixelPlayersByStats(p1: HypixelPlayerData): Double = {
		if(p1.getStars < 0){
			return Double.MaxValue
		}
		p1.getStars * Math.pow(p1.getFKDR, 2)
	}

	def clearPlayers(): Unit = {
		clearPlayerCache
	}

	def playerInList(uuid: UUID): Boolean = {
		players.count(_.getUUID.equals(uuid)) > 0
	}

	def printListToChat(): Unit = {
		overlayRenderer.logPlayers()
	}

	def ShowOverlay(): Unit = {
		if (!isBedwarsGame) {return;}
		overlayRenderer.renderPlayerlist()
	}

	def getUsersName = mc.thePlayer.getGameProfile.getName

	@SubscribeEvent
	def tickRender(event: TickEvent.RenderTickEvent): Unit = {
		if (isBedwarsGame) {
			if(showOverlayKeybind.isKeyDown) {
				ShowOverlay()
			}
		}
		// NOT in a Bedwars game / queue lobby
		else{
			if(gameStarting || bedwarsGameStarted) resetGameProgress()
		}
	}

	@SubscribeEvent
	def onChatEvent(event: ClientChatReceivedEvent): Unit = {
		if (!isBedwarsGame) {
		return
		}

		if(showOverlayKeybind.isKeyDown) {
			updateCurPlayersDict()
		}

		handleStatsOnChat(event.message)
		handleFunOnChat(event.message)
	}
}
