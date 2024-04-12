package com.BARSOverlay

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
import java.util.List
import scala.collection.JavaConverters._
import com.google.common.collect.Ordering


object OverlayManager extends Gui{

	val logger = LogManager.getLogger();
	val overlayRenderer = new GuiOverlay(mc)



	def getListOfPlayers: List[NetworkPlayerInfo] = {
		val ordering = Ordering.from(new OverlayPlayerComparator())
		val nethandlerplayclient: NetHandlerPlayClient = mc.thePlayer.sendQueue
		var list: util.List[NetworkPlayerInfo] = ordering.sortedCopy[NetworkPlayerInfo](nethandlerplayclient.getPlayerInfoMap)
		list
//		List("Jaxaar", "Pypeapple", "Gatekeeper", "...")
	}

	def printListToChat = {
		val l = getListOfPlayers
		overlayRenderer.setPlayers(l)
		overlayRenderer.logPlayers()
	}

	def ShowOverlay = {
		val l = getListOfPlayers
		overlayRenderer.setPlayers(l)
		overlayRenderer.renderPlayerlist()
	}

	@SubscribeEvent
	def tickRender(event: TickEvent.RenderTickEvent): Unit = {
		if(Keyboard.isKeyDown(Keyboard.KEY_G)) {
			ShowOverlay
		}
	}

}
