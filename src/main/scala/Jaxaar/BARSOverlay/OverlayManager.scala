package Jaxaar.BARSOverlay

import Jaxaar.BARSOverlay.GUIComponents.GuiOverlay
import Jaxaar.BARSOverlay.Utils.OverlayPlayerComparator
import BarsOverlayMod.mc
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

//	val ordering = Ordering.from(new OverlayPlayerComparator())
//	def players: List[HypixelPlayerData] = ordering.sortedCopy[HypixelPlayerData](playerDict.values.asJava).asScala.toList
	def players: List[HypixelPlayerData] = playersDict.values.toList


	def getListOfPlayers: List[NetworkPlayerInfo] = {
//		val nethandlerplayclient: NetHandlerPlayClient = mc.thePlayer.sendQueue
//		val list: JavaList[NetworkPlayerInfo] = ordering.sortedCopy[NetworkPlayerInfo](nethandlerplayclient.getPlayerInfoMap)
//		list
//		List("Jaxaar", "Pypeapple", "Gatekeeper", "...")
		val lst = mc.thePlayer.sendQueue.getPlayerInfoMap.toList
		println(lst)
		lst
	}

	def updatePlayerList() = {
		val newList = getListOfPlayers.map(x => {
			val uuid = x.getGameProfile.getId;
			(uuid, playersDict.getOrElse(uuid, new HypixelPlayerData(x)))
		}).toMap
		playersDict = newList
	}

	def playerInList(uuid: UUID) = {
		players.filter(_.getUUID.equals(uuid))
	}

	def printListToChat = {
		overlayRenderer.logPlayers()
	}

	def ShowOverlay = {
		overlayRenderer.renderPlayerlist()
	}

	@SubscribeEvent
	def tickRender(event: TickEvent.RenderTickEvent): Unit = {
		if(Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			ShowOverlay
		}
	}

	@SubscribeEvent
	def onChatEvent(event: ClientChatReceivedEvent): Unit = {
		if(Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			updatePlayerList()
		}
	}

}
