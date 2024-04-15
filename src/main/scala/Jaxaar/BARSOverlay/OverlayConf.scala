package Jaxaar.BARSOverlay

import net.hypixel.api.reply.PlayerReply.Player
import net.minecraft.util.{ChatStyle, EnumChatFormatting}

object OverlayConf {

	def getColumnValues = {
		List(
			new OverlayColumnValues(
				title = "Player",
				fieldLength = 90,
				getString = (p) => s"[${if(p.playerLoaded) p.player.getIntProperty("achievements.bedwars_level", 0) else 0}✫] ${p.getTrueName}",
				specialFormat = true,
				colorBreakpoints = List(0),
				value = new SingleValue("achievements.bedwars_level", "Int")
			),
			new OverlayColumnValues(
				title = "WLR",
				fieldLength = 40,
				getString = (p) => s"${if(p.playerLoaded) (p.player.getDoubleProperty("stats.Bedwars.wins_bedwars", 0) / p.player.getDoubleProperty("stats.Bedwars.losses_bedwars", 1) *100).round/100.0 else 0}",
				colorBreakpoints =  List(1,2,5,7,10),
			    getValue = (p) => if(p.playerLoaded) (p.player.getDoubleProperty("stats.Bedwars.wins_bedwars", 0) / p.player.getDoubleProperty("stats.Bedwars.losses_bedwars", 1) *100).round/100.0 else 0,
				value = new RatioValue("stats.Bedwars.wins_bedwars", "stats.Bedwars.wins_bedwars")
			),
			new OverlayColumnValues(
				title = "FKDR",
				fieldLength = 40,
				getString = (p) => s"${if(p.playerLoaded) (p.player.getDoubleProperty("stats.Bedwars.final_kills_bedwars", 0) / p.player.getDoubleProperty("stats.Bedwars.final_deaths_bedwars", 1) *100).round/100.0 else 0}",
				colorBreakpoints =  List(1,3,5,10,25),
				getValue = (p) => if(p.playerLoaded) (p.player.getDoubleProperty("stats.Bedwars.final_kills_bedwars", 0) / p.player.getDoubleProperty("stats.Bedwars.final_deaths_bedwars", 1) *100).round/100.0 else 0,
				value = new RatioValue("stats.Bedwars.final_kills_bedwars", "stats.Bedwars.final_deaths_bedwars")
			)
		)
	}
}

class OverlayColumnValues(val title: String, val fieldLength: Int, val getString: HypixelPlayerData => String, val specialFormat: Boolean = false, val colorBreakpoints: List[Double] = List(), val getValue: HypixelPlayerData => Double = (p) => 0, val value: SingleValue){
	override def toString: String = s"${title} - ${fieldLength}"

	def getFormattedString(player: HypixelPlayerData): String = {
		val str = getString(player)
		val style = new ChatStyle()
		style.setColor(colorFunction(getValue(player)))
		s"${style.getFormattingCode}${str}"
	}

	def colorFunction(d: Double) = {
		caseMatchStatsValue(d, colorBreakpoints)
	}

	def caseMatchStatsValue(d: Double, breakpoints: List[Double]): EnumChatFormatting = {
		val colors = List(
			EnumChatFormatting.GRAY,
			EnumChatFormatting.WHITE,
			EnumChatFormatting.GOLD,
			EnumChatFormatting.AQUA,
			EnumChatFormatting.RED,
			EnumChatFormatting.DARK_PURPLE
		)

		val numGreater = breakpoints.count(_ >= d)
		colors((breakpoints.length) - numGreater)
	}

	def getVal(player: HypixelPlayerData): Any = {
		value.getValueMethod()(player)
	}

}

class SingleValue(val stringPath: String, val typeStr: String){
	def getValueMethod(): HypixelPlayerData => Any = {
		typeStr match {
			case "String" => (player: HypixelPlayerData) => if(player.playerLoaded) player.player.getProperty(stringPath)
			case "Int" => (player: HypixelPlayerData) => if(player.playerLoaded) player.player.getIntProperty(stringPath, 0)
			case "Double" => (player: HypixelPlayerData) => if(player.playerLoaded)player.player.getDoubleProperty(stringPath, 0.0)
		}
	}
}
class RatioValue(val topPath: String, val botPath: String) extends SingleValue(stringPath = "", typeStr = "Double"){
	override def getValueMethod: HypixelPlayerData => Double = {
		(player: HypixelPlayerData) => if(player.playerLoaded) (player.player.getDoubleProperty("stats.Bedwars.final_kills_bedwars", 0.0) / player.player.getDoubleProperty("stats.Bedwars.final_deaths_bedwars", 1.0)) else -1.0
	}
	def getValueRoundedMethod(p: HypixelPlayerData): HypixelPlayerData => Double = (p: HypixelPlayerData) => (getValueMethod(p) * 100).round / 100.0
}
