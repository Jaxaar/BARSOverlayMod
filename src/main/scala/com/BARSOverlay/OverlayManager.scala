package com.BARSOverlay

import com.BARSOverlay.GUIComponents.GuiOverlay
import net.minecraft.client.gui.Gui
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard

import org.apache.logging.log4j.LogManager
import scala.collection.JavaConverters._

object OverlayManager extends Gui{

	val logger = LogManager.getLogger();
	val overlayRenderer = new GuiOverlay(BarsOverlayMod.mc)



	def getListOfPlayers: List[String] = {
		List("Jaxaar", "Pypeapple", "Gatekeeper", "...")
	}

	def printListToChat = {
		val l = getListOfPlayers
		overlayRenderer.setPlayers(l.asJava)
		overlayRenderer.logPlayers()
	}

	def ShowOverlay = {
		val l = getListOfPlayers
		overlayRenderer.setPlayers(l.asJava)
		overlayRenderer.renderPlayerlist()
	}

	@SubscribeEvent
	def tickRender(event: TickEvent.RenderTickEvent): Unit = {
		if(Keyboard.isKeyDown(Keyboard.KEY_G)) {
			ShowOverlay
		}
	}

}
