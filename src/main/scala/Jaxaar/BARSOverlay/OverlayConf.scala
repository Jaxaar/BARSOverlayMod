package Jaxaar.BARSOverlay

import net.hypixel.api.reply.PlayerReply.Player
import net.minecraft.util.EnumChatFormatting

object OverlayConf {

	class OverlayColumnValues(val title: String, val fieldLength: Int, val getString: HypixelPlayerData => String, val specialFormat: Boolean = false, val colorFunction: Double => Int = (x) => 0, val getValue: HypixelPlayerData => Double = (p) => 0){
		override def toString: String = s"${title} - ${fieldLength}"
	}

	def getColumnValues = {
		List(
			new OverlayColumnValues(
				title = "Player",
				fieldLength = 90,
				getString = (p) => s"[${if(p.playerLoaded) p.player.getIntProperty("achievements.bedwars_level", 0) else 0}✫] ${p.getTrueName}",
				specialFormat = true
			),
			new OverlayColumnValues(
				title = "WLR",
				fieldLength = 40,
				getString = (p) => s"${if(p.playerLoaded) (p.player.getDoubleProperty("stats.Bedwars.wins_bedwars", 0) / p.player.getDoubleProperty("stats.Bedwars.losses_bedwars", 1) *100).round/100.0 else 0}",
//				colorFunction =  {
//					case x < 1 => EnumChatFormatting.DARK_GRAY.getColorIndex
//					case x < 2 => EnumChatFormatting.WHITE.getColorIndex
//					case x < 5 => EnumChatFormatting.GOLD.getColorIndex
//					case x < 7 => EnumChatFormatting.AQUA.getColorIndex
//					case x < 10 => EnumChatFormatting.RED.getColorIndex
//					case _ => EnumChatFormatting.DARK_PURPLE.getColorIndex
//				},
			    getValue = (p) => if(p.playerLoaded) (p.player.getDoubleProperty("stats.Bedwars.wins_bedwars", 0) / p.player.getDoubleProperty("stats.Bedwars.losses_bedwars", 1) *100).round/100.0 else 0
			),
			new OverlayColumnValues(
				title = "FKDR",
				fieldLength = 40,
				getString = (p) => s"${if(p.playerLoaded) (p.player.getDoubleProperty("stats.Bedwars.final_kills_bedwars", 0) / p.player.getDoubleProperty("stats.Bedwars.final_deaths_bedwars", 1) *100).round/100.0 else 0}",
//				colorFunction =  {
//					case x < 1 => EnumChatFormatting.DARK_GRAY.getColorIndex
//					case x < 3 => EnumChatFormatting.WHITE.getColorIndex
//					case x < 5 => EnumChatFormatting.GOLD.getColorIndex
//					case x < 10 => EnumChatFormatting.AQUA.getColorIndex
//					case x < 25 => EnumChatFormatting.RED.getColorIndex
//					case _ => EnumChatFormatting.DARK_PURPLE.getColorIndex
//				},
				getValue = (p) => if(p.playerLoaded) (p.player.getDoubleProperty("stats.Bedwars.final_kills_bedwars", 0) / p.player.getDoubleProperty("stats.Bedwars.final_deaths_bedwars", 1) *100).round/100.0 else 0
			)
		)
	}

}
