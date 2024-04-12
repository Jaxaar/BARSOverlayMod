package com.BARSOverlay

import com.BARSOverlay.ApiHandler.{getHypixelPlayerData, getJSON}
import com.BARSOverlay.BarsOverlayMod.mc
import com.BARSOverlay.GUIComponents.GuiOverlay
import com.BARSOverlay.Utils.OverlayPlayerComparator
import net.minecraft.client.gui.Gui
import net.minecraft.client.network.{NetHandlerPlayClient, NetworkPlayerInfo}
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import org.apache.logging.log4j.LogManager

import java.util
import java.util.{List, UUID}
import scala.collection.JavaConverters._
import com.google.common.collect.Ordering

import java.util.concurrent.Future


object OverlayManager extends Gui{

	val logger = LogManager.getLogger();
	val overlayRenderer = new GuiOverlay(mc)



	class HypixelPlayerData(networkPlayerInfo: NetworkPlayerInfo){

		val future: Future[String] = fetchData()

		def getUUID: UUID = networkPlayerInfo.getGameProfile.getId

		def hasData: Boolean = future.isDone;

		def getHypixelData: String = {
			if(future.isDone){
				return future.get()
			}
			""
		}

		def getHypixelDataJSON = getJSON(getHypixelData)

		private def fetchData() = {
			ApiHandler.getHypixelPlayerData(getUUID)
		}

		def reload() = new HypixelPlayerData(networkPlayerInfo)

	}


	def getListOfPlayers: List[NetworkPlayerInfo] = {
		val ordering = Ordering.from(new OverlayPlayerComparator())
		val nethandlerplayclient: NetHandlerPlayClient = mc.thePlayer.sendQueue
		val list: util.List[NetworkPlayerInfo] = ordering.sortedCopy[NetworkPlayerInfo](nethandlerplayclient.getPlayerInfoMap)
		list
//		List("Jaxaar", "Pypeapple", "Gatekeeper", "...")
	}

	def printListToChat = {
		val l = getListOfPlayers
		overlayRenderer.setPlayers(l)
		overlayRenderer.logPlayers()
	}

	def triggerQuery = {
		val l = getListOfPlayers
		val resp = getHypixelPlayerData(l.get(0).getGameProfile.getId)
		println("here")
		println(resp)
	}


	def ShowOverlay = {
		val l = getListOfPlayers
		overlayRenderer.setPlayers(l)
		overlayRenderer.renderPlayerlist()
	}

	@SubscribeEvent
	def tickRender(event: TickEvent.RenderTickEvent): Unit = {
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
			ShowOverlay
		}
	}

}
