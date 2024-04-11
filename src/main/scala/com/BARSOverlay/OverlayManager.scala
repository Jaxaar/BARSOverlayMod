package com.BARSOverlay

import com.BARSOverlay.GUIComponents.GuiOverlay
import net.minecraft.client.gui.Gui
import scala.collection.JavaConverters._

object OverlayManager extends Gui{

	def getListOfPlayers: List[String] = {
		return List("Jaxaar", "Pypeapple", "Gatekeeper", "...")
	}

	def printListToChat = {
		val l = getListOfPlayers
		val overlayRender = new GuiOverlay(BarsOverlayMod.mc)
		overlayRender.setPlayers(l.asJava)
		overlayRender.logPlayers()
	}

	def ShowOverlay = {
		val l = getListOfPlayers
		val overlayRender = new GuiOverlay(BarsOverlayMod.mc)
		overlayRender.setPlayers(l.asJava)
		overlayRender.renderPlayerlist()
	}

}
