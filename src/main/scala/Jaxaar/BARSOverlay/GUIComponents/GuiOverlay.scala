package Jaxaar.BARSOverlay.GUIComponents

import Jaxaar.BARSOverlay.BarsOverlayMod.{config, mc}
import Jaxaar.BARSOverlay.DataStructures.OverlayConf
import Jaxaar.BARSOverlay.OverlayManager.players
import Jaxaar.BARSOverlay.Utils.APIRequestHandler.APIKeyIsValid
import Jaxaar.BARSOverlay.Utils.BARSConfig.getLoadFromFirstPlayer
import Jaxaar.BARSOverlay.Utils.{BARSConfig, ScoreboardSidebarReader}
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.{ChatComponentTranslation, ChatStyle, EnumChatFormatting}


object GuiOverlay{

	def logPlayers(): Unit = {
		println(players.toString)
		for (i <- players) {
			mc.thePlayer.addChatMessage(i.getNameComponent)
		}
	}

	def playersToDisplay = players

	def renderPlayerlist() {

		val fontScale = BARSConfig.getGui_scale

		def normalize(coord: Double, scale: Double = fontScale): Int = {
			(coord / scale).round.toInt
		}
		def adjust(coord: Double, scale: Double = fontScale): Int = {
			(coord * scale).round.toInt
		}

		val spacingBetween = 14


		//        logPlayers()
		val raw_left = BARSConfig.getXPos
		val raw_top = BARSConfig.getYPos

		val width = OverlayConf.getColumnValues.map(c => c.fieldLength).sum
		val height = playersToDisplay.map(x => adjust(spacingBetween, fontScale - 0.3)).sum match {
			case x if x == 0 => 200
			case x => x
		}

		val firstPlayerX = 10 + raw_left
		val firstPlayerY = 25 + raw_top

		val raw_right = width + raw_left + 5
		val raw_bottom = height + firstPlayerY + 5


		GlStateManager.scale(fontScale, fontScale, 1)

		Gui.drawRect(raw_left, raw_top, raw_right, raw_bottom, Integer.MIN_VALUE)

//		Gui.drawRect(adjust(raw_left, fontScale + 0.3),  adjust(raw_top, fontScale + 0.3), adjust(raw_right, fontScale + 0.3), adjust(raw_bottom, fontScale + 0.3), Integer.MIN_VALUE)
//		GlStateManager.scale(fontScale + 0.3, fontScale + 0.3, 1)


		var xPosHeader = 10 + raw_left
		val yPosHeader = 10 + raw_top
		for (i <- OverlayConf.getColumnValues){
			mc.fontRendererObj.drawStringWithShadow(i.title, xPosHeader, yPosHeader, -1)
			xPosHeader += i.fieldLength
		}

//		mc.fontRendererObj.drawStringWithShadow("Player", 10, 20, -1)
////		mc.fontRendererObj.drawStringWithShadow("Stars", 50, 20, -1)
//		mc.fontRendererObj.drawStringWithShadow("WLR", 100, 20, -1)
//		mc.fontRendererObj.drawStringWithShadow("FKDR", 150, 20, -1)

		if(!APIKeyIsValid){

			val style = new ChatStyle().setColor(EnumChatFormatting.RED)

			mc.fontRendererObj.drawString(s"${style.getFormattingCode}API Key Expired", raw_left + width/3, raw_top + 70, -1)
			mc.fontRendererObj.drawString(s"${style.getFormattingCode}Use /apikey to set a new one",raw_left + width/4,raw_top + 85, -1)
			return
		}

		if(players.size <= 0){
			return
		}
		//		for(i <- 0 until 15){
//			val player = players.head
		GlStateManager.scale(fontScale - 0.3, fontScale - 0.3, 1)

		for(i <- playersToDisplay.indices){
			val player = players(i)
//			println("out-" + ScoreboardSidebarReader.getPlayersTeam(player.getTrueName))


			var xPos = adjust(normalize(firstPlayerX, fontScale - 0.3), fontScale)
			val yPos = adjust(normalize(firstPlayerY, fontScale - 0.3), fontScale)
			for (colID <- OverlayConf.getColumnValues.indices){
				val col = OverlayConf.getColumnValues(colID)
				mc.fontRendererObj.drawString(col.getFormattedString(player), xPos, yPos + i * spacingBetween, -1)
				xPos += adjust(normalize(col.fieldLength, fontScale - 0.3), fontScale)
			}
//				mc.fontRendererObj.drawStringWithShadow(player.getStars, 10, firstPlayerY + i * spacingBetween, -1)
//				mc.fontRendererObj.drawStringWithShadow(player.getNameComponent.getFormattedText, 40, firstPlayerY + i * spacingBetween, -1)
//				mc.fontRendererObj.drawStringWithShadow(player.getWLR, 80, firstPlayerY + i * spacingBetween, -1)
//				mc.fontRendererObj.drawStringWithShadow(player.getFKDR, 120, firstPlayerY + i * spacingBetween, -1)
//
//				mc.fontRendererObj.drawStringWithShadow("123", 150, firstPlayerY + i * spacingBetween, -1)
//				mc.fontRendererObj.drawStringWithShadow("123", 151, firstPlayerY + i * spacingBetween, -1)

		}
		GlStateManager.scale(1, 1, 1)
	}
}
