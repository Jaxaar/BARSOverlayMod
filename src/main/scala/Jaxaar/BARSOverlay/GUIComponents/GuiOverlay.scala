package Jaxaar.BARSOverlay.GUIComponents

import Jaxaar.BARSOverlay.BarsOverlayMod.mc
import Jaxaar.BARSOverlay.OverlayConf
import Jaxaar.BARSOverlay.OverlayManager.players
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ChatComponentTranslation

import scala.collection.JavaConversions._


object GuiOverlay{

	def logPlayers(): Unit = {
		println(players.toString)
		for (i <- players) {
			mc.thePlayer.addChatMessage(i.getNameComponent)
		}
	}

	def playersToDisplay = players.filter(_.playerLoaded)

	def renderPlayerlist() {
		//        logPlayers()
		val left = 2
		val top = 2
		val right = 240
		val bottom = 240

		val firstPlayerY = 30
		val firstPlayerX = 10
		val spacingBetween = 14
		val fontScale = 0.7



		Gui.drawRect(left,  top, right, bottom, Integer.MIN_VALUE)
		var xPosHeader = firstPlayerX
		for (i <- OverlayConf.getColumnValues){
			mc.fontRendererObj.drawStringWithShadow(i.title, (xPosHeader * fontScale).round, (10 * fontScale).round, -1)
			xPosHeader += i.fieldLength
		}

//		mc.fontRendererObj.drawStringWithShadow("Player", 10, 20, -1)
////		mc.fontRendererObj.drawStringWithShadow("Stars", 50, 20, -1)
//		mc.fontRendererObj.drawStringWithShadow("WLR", 100, 20, -1)
//		mc.fontRendererObj.drawStringWithShadow("FKDR", 150, 20, -1)

		if(players.size() <= 0){
			return
		}

		GlStateManager.scale(fontScale, fontScale, fontScale)
		for(i <- 0 until 15){
			val player = players.head

//		for(i <- playersToDisplay.indices){
//			val player = players(i)
			if(player.playerLoaded) {

				var xPos = firstPlayerX
				for (colID <- OverlayConf.getColumnValues.indices){
					val col = OverlayConf.getColumnValues(colID)
					mc.fontRendererObj.drawString(col.getFormattedString(player), xPos, firstPlayerY + i * spacingBetween, -1)
					xPos += col.fieldLength
				}
//				mc.fontRendererObj.drawStringWithShadow(player.getStars, 10, firstPlayerY + i * spacingBetween, -1)
//				mc.fontRendererObj.drawStringWithShadow(player.getNameComponent.getFormattedText, 40, firstPlayerY + i * spacingBetween, -1)
//				mc.fontRendererObj.drawStringWithShadow(player.getWLR, 80, firstPlayerY + i * spacingBetween, -1)
//				mc.fontRendererObj.drawStringWithShadow(player.getFKDR, 120, firstPlayerY + i * spacingBetween, -1)
//
//				mc.fontRendererObj.drawStringWithShadow("123", 150, firstPlayerY + i * spacingBetween, -1)
//				mc.fontRendererObj.drawStringWithShadow("123", 151, firstPlayerY + i * spacingBetween, -1)

			}
		}
		GlStateManager.scale(1, 1, 1)
	}
}
