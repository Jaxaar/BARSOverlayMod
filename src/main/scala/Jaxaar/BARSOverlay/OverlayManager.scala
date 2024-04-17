package Jaxaar.BARSOverlay

import Jaxaar.BARSOverlay.GUIComponents.GuiOverlay
import BarsOverlayMod.{getShowOverlayKey, mc}
import Jaxaar.BARSOverlay.DataStructures.HypixelPlayerData
import Jaxaar.BARSOverlay.Utils.ScoreboardSidebarReader.{isBedwarsGame, verifyIsBedwarsGame}
import Jaxaar.BARSOverlay.listeners.HotkeyShortcuts.showOverlayKeybind
import net.minecraft.client.gui.Gui
import net.minecraft.client.network.{NetHandlerPlayClient, NetworkPlayerInfo}
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import org.apache.logging.log4j.LogManager

import scala.collection.mutable.LinkedList
import java.util
import java.util.{UUID, List => JavaList}
import scala.collection.JavaConverters._
import com.google.common.collect.Ordering
import net.minecraftforge.client.event.ClientChatReceivedEvent
import sun.security.ec.point.ProjectivePoint.Mutable

import java.util.concurrent.Future
import scala.collection.JavaConversions.collectionAsScalaIterable


object OverlayManager extends Gui{

	val logger = LogManager.getLogger();
	val overlayRenderer = GuiOverlay

	var playersDict: Map[UUID, HypixelPlayerData] = Map()
	def players: List[HypixelPlayerData] = playersDict.values.toList.sortBy(sortHypixelPlayers).reverse


	def getListOfPlayers: List[NetworkPlayerInfo] = {
		 mc.thePlayer.sendQueue.getPlayerInfoMap.toList
	}

	def updatePlayerList(): Unit = {
		if (!verifyIsBedwarsGame) {return;}

		val newMap = getListOfPlayers.map(x => {
			val uuid = x.getGameProfile.getId;
			(uuid, playersDict.getOrElse(uuid, new HypixelPlayerData(x)))
		}).toMap
		playersDict = newMap
	}

	def sortHypixelPlayers(p1: HypixelPlayerData): Double = {
		if(p1.getStars < 0){
			return Integer.MAX_VALUE
		}
		p1.getStars * Math.pow(p1.getFKDR, 2)
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
			updatePlayerList()
		}
	}
}
