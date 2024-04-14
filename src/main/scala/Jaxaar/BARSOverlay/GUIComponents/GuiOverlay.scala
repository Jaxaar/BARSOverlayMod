package Jaxaar.BARSOverlay.GUIComponents

import Jaxaar.BARSOverlay.BarsOverlayMod.mc
import Jaxaar.BARSOverlay.OverlayManager.players
import net.minecraft.client.gui.Gui
import net.minecraft.util.ChatComponentTranslation

import scala.collection.JavaConversions._


object GuiOverlay{

	def logPlayers(): Unit = {
		println(players.toString)
		for (i <- players) {
			mc.thePlayer.addChatMessage(i.getNameComponent)
		}
	}

	def renderPlayerlist() {
		//        logPlayers()
		Gui.drawRect(2,  2, 200, 240, Integer.MIN_VALUE)

		mc.fontRendererObj.drawStringWithShadow("Player", 10, 20, -1)
		mc.fontRendererObj.drawStringWithShadow("Stars", 50, 20, -1)
		mc.fontRendererObj.drawStringWithShadow("WLR", 100, 20, -1)
		mc.fontRendererObj.drawStringWithShadow("FKDR", 150, 20, -1)

		if(players.size() <= 0){
			return
		}
		for(i <- players.indices){
			val player = players(i)
			if(player.playerLoaded) {
				mc.fontRendererObj.drawStringWithShadow(player.getNameComponent.getFormattedText, 10, 40 + i * 15, -1)
				mc.fontRendererObj.drawStringWithShadow(player.getStars, 50, 40 + i * 15, -1)
				mc.fontRendererObj.drawStringWithShadow(player.getWLR, 100, 40 + i * 15, -1)
				mc.fontRendererObj.drawStringWithShadow(player.getFKDR, 150, 40 + i * 15, -1)

			}
		}
	}

}
