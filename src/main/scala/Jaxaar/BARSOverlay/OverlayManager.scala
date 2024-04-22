package Jaxaar.BARSOverlay

import Jaxaar.BARSOverlay.GUIComponents.GuiOverlay
import BarsOverlayMod.{getShowOverlayKey, mc}
import Jaxaar.BARSOverlay.DataStructures.HypixelPlayerData
import Jaxaar.BARSOverlay.Utils.APIRequestHandler.{clearPlayerCache, getPlayerStats}
import Jaxaar.BARSOverlay.Utils.Helpers.stripColorCodes
import Jaxaar.BARSOverlay.Utils.ScoreboardSidebarReader.{isBedwarsGame, isHypixel, verifyIsBedwarsGame}
import Jaxaar.BARSOverlay.listeners.HotkeyShortcuts.showOverlayKeybind
import net.minecraft.client.gui.Gui
import net.minecraft.client.network.{NetHandlerPlayClient, NetworkPlayerInfo}
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import org.apache.logging.log4j.LogManager

import java.util.{UUID, List => JavaList}
import net.minecraftforge.client.event.ClientChatReceivedEvent

import scala.collection.JavaConversions.collectionAsScalaIterable



object OverlayManager extends Gui{

	val logger = LogManager.getLogger();
	val overlayRenderer = GuiOverlay

	private var curPlayers: List[HypixelPlayerData] = List()
	def players: List[HypixelPlayerData] = masterSort(curPlayers).slice(0,Math.min(curPlayers.length, 20))


	def getListOfPlayers: List[NetworkPlayerInfo] = {
		 mc.thePlayer.sendQueue.getPlayerInfoMap.toList
	}

	def updateCurPlayersDict: Unit = {
		if (!verifyIsBedwarsGame) {return;}

		val newLst = getListOfPlayers.view.flatMap(x => {
			val uuid = x.getGameProfile.getId;
			getPlayerStats(uuid)
		}).filter(_ != null).map(x => new HypixelPlayerData(x.player)).toList
		curPlayers = newLst
	}


	def masterSort(lst: List[HypixelPlayerData]): List[HypixelPlayerData] = {
//		Sort by Stats
		lst.view.sortBy(sortHypixelPlayersByStats).reverse
//		Sort by team Char - Specifically makes use of Bedwars team styling
		  .sortBy(x => stripColorCodes(x.getTeamColorStyling)).toList
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

	@SubscribeEvent
	def tickRender(event: TickEvent.RenderTickEvent): Unit = {
		if(showOverlayKeybind.isKeyDown) {
			ShowOverlay()
		}
	}

	@SubscribeEvent
	def onChatEvent(event: ClientChatReceivedEvent): Unit = {
		if(showOverlayKeybind.isKeyDown) {
			updateCurPlayersDict
		}
	}
}
