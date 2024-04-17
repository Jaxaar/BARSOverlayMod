package Jaxaar.BARSOverlay

import net.hypixel.api.reply.PlayerReply.Player
import net.minecraft.util.{ChatStyle, EnumChatFormatting}
import net.minecraft.util.EnumChatFormatting.{AQUA, BLACK, BLUE, DARK_AQUA, DARK_BLUE, DARK_GRAY, DARK_GREEN, DARK_PURPLE, DARK_RED, GOLD, GRAY, GREEN, LIGHT_PURPLE, RED, WHITE, YELLOW}


object OverlayConf {

	def getColumnValues = {
		List(
			new PlayerColumnValues(
				title = "Player",
				fieldLength = 140,
				colorBreakpoints = List(0),
				stars = new SingleNumericValue("achievements.bedwars_level")
			),
			new StatsColumnValues(
				title = "WS",
				fieldLength = 25,
				isInt = true,
				colorBreakpoints =  List(4, 10, 25, 50, 100),
				value = new SingleNumericValue("stats.Bedwars.winstreak")
			),
			new StatsColumnValues(
				title = "FKDR",
				fieldLength = 40,
				colorBreakpoints =  List(1,3,5,10,25),
				value = new RatioValue("stats.Bedwars.final_kills_bedwars", "stats.Bedwars.final_deaths_bedwars")
			),
			new StatsColumnValues(
				title = "WLR",
				fieldLength = 35,
				colorBreakpoints =  List(1,2,5,7,10),
				value = new RatioValue("stats.Bedwars.wins_bedwars", "stats.Bedwars.losses_bedwars")
			),
			new StatsColumnValues(
				title = "Finals",
				fieldLength = 50,
				isInt = true,
				colorBreakpoints =  List(1000, 5000, 10000, 20000, 30000),
				value = new SingleNumericValue("stats.Bedwars.final_kills_bedwars")
			),
			new StatsColumnValues(
				title = "Wins",
				fieldLength = 40,
				isInt = true,
				colorBreakpoints =  List(500, 1000, 3000, 5000, 10000),
				value = new SingleNumericValue("stats.Bedwars.wins_bedwars")
			)
		)
	}
}

abstract class ColumnValues(val title: String, val fieldLength: Int, val colorBreakpoints: List[Double] = List()){
	override def toString: String = s"${title} - ${fieldLength}: Tiers-${colorBreakpoints.toString()}"
	def getFormattedString(player: HypixelPlayerData): String
}

class StatsColumnValues(val value: SingleNumericValue = null, val isInt: Boolean = false, override val title: String, override val fieldLength: Int, override val colorBreakpoints: List[Double] = List()) extends ColumnValues(title = title, fieldLength=fieldLength, colorBreakpoints = colorBreakpoints){

	override def getFormattedString(player: HypixelPlayerData): String = {
//		val str = getString(player)
		val str = getVal(player) match {
			case -1 => "-" // Doesn't exist
			case x if isInt => x.toInt
			case x => x
		}
		val style = new ChatStyle()
		style.setColor(colorFunction(getVal(player)))
		s"${style.getFormattingCode} ${str}"
	}

	def colorFunction(d: Double) = {
		caseMatchStatsValue(d, colorBreakpoints)
	}

	def caseMatchStatsValue(d: Double, breakpoints: List[Double]): EnumChatFormatting = {
		val colors = List(
			GRAY,
			WHITE,
			GOLD,
			AQUA,
			RED,
			DARK_PURPLE
		)

		val numGreater = breakpoints.count(_ >= d)
		colors((breakpoints.length) - numGreater)
	}

	def getVal(player: HypixelPlayerData): Double = {
		value.getValueRoundedMethod()(player)
	}
}

class PlayerColumnValues(val stars: SingleNumericValue = null, override val title: String, override val fieldLength: Int, override val colorBreakpoints: List[Double] = List()) extends ColumnValues(title = title, fieldLength=fieldLength, colorBreakpoints = colorBreakpoints){
	val style = new ChatStyle()

	override def getFormattedString(player: HypixelPlayerData): String = {
		//		val str = getString(player)
//		style.setColor(colorFunction(getVal(player)))
		s"${getStyledStars(getStars(player))} ${getPlayerName(player)}"
	}

	def getPlayerName(playerData: HypixelPlayerData): String = style.setColor(WHITE).getFormattingCode + playerData.getTrueName

	def getStars(player: HypixelPlayerData): Int = {
		stars.getValueRoundedMethod()(player).intValue()
	}

	def getStyledStars(stars: Int): String = {
//		return s"[${stars}✫]"

		return stars match {
			case x if x < 100 => formatStars(s"[${x}✫]", List(GRAY))
			case x if x < 200 => formatStars(s"[${x}✫]", List(WHITE))
			case x if x < 300 => formatStars(s"[${x}✫]", List(GOLD))
			case x if x < 400 => formatStars(s"[${x}✫]", List(AQUA))
			case x if x < 500 => formatStars(s"[${x}✫]", List(DARK_GREEN))
			case x if x < 600 => formatStars(s"[${x}✫]", List(DARK_AQUA))
			case x if x < 700 => formatStars(s"[${x}✫]", List(DARK_RED))
			case x if x < 800 => formatStars(s"[${x}✫]", List(LIGHT_PURPLE))
			case x if x < 900 => formatStars(s"[${x}✫]", List(BLUE))
			case x if x < 1000 => formatStars(s"[${x}✫]", List(DARK_PURPLE))
			case x if x < 1100 => formatStars(s"[${x}✫]", List(RED, GOLD, YELLOW, GREEN, AQUA, LIGHT_PURPLE, DARK_PURPLE))

			case x if x < 1200 => formatStars(s"[${x}✪]", List(GRAY, WHITE, WHITE, WHITE, WHITE, GRAY, GRAY))
			case x if x < 1300 => formatStars(s"[${x}✪]", List(GRAY, YELLOW, YELLOW, YELLOW, YELLOW, GOLD, GRAY))
			case x if x < 1400 => formatStars(s"[${x}✪]", List(GRAY, AQUA, AQUA, AQUA, AQUA, DARK_AQUA, GRAY))
			case x if x < 1500 => formatStars(s"[${x}✪]", List(GRAY, GREEN, GREEN, GREEN, GREEN, DARK_GREEN, GRAY))
			case x if x < 1600 => formatStars(s"[${x}✪]", List(GRAY, DARK_AQUA, DARK_AQUA, DARK_AQUA, DARK_AQUA, BLUE, GRAY))
			case x if x < 1700 => formatStars(s"[${x}✪]", List(GRAY, RED, RED, RED, RED, DARK_RED, GRAY))
			case x if x < 1800 => formatStars(s"[${x}✪]", List(GRAY, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE, GRAY))
			case x if x < 1900 => formatStars(s"[${x}✪]", List(GRAY, BLUE, BLUE, BLUE, BLUE, DARK_BLUE, GRAY))
			case x if x < 2000 => formatStars(s"[${x}✪]", List(GRAY, DARK_PURPLE, DARK_PURPLE, DARK_PURPLE, DARK_PURPLE, DARK_GRAY, GRAY))
			case x if x < 2100 => formatStars(s"[${x}✪]", List(DARK_GRAY, GRAY, WHITE, WHITE, GRAY, GRAY, DARK_GRAY))



			case _ => formatStars(s"[${stars}*]", List(WHITE))
//			case _ => formatStars(s"[${stars}✥]", List(DARK_RED, DARK_RED, DARK_PURPLE, BLUE, BLUE, DARK_BLUE, BLACK))

		}
//		else if (stars < 200) return formatStars(stars, '✫', WHITE)
//		else if (stars < 300) return formatStars(stars, '✫', GOLD)
//		else if (stars < 400) return formatStars(stars, '✫', AQUA)
//		else if (stars < 500) return formatStars(stars, '✫', DARK_GREEN)
//		else if (stars < 600) return formatStars(stars, '✫', DARK_AQUA)
//		else if (stars < 700) return formatStars(stars, '✫', DARK_RED)
//		else if (stars < 800) return formatStars(stars, '✫', LIGHT_PURPLE)
//		else if (stars < 900) return formatStars(stars, '✫', BLUE)
//		else if (stars < 1000) return formatStars(stars, '✫', DARK_PURPLE)
//		else if (stars < 1100) return formatStars(stars, '✫', RED, GOLD, YELLOW, GREEN, AQUA, LIGHT_PURPLE, DARK_PURPLE)
//		else if (stars < 1200) return formatStars(stars, '✪', GRAY, WHITE, WHITE, WHITE, WHITE, GRAY, GRAY)
//		else if (stars < 1300) return formatStars(stars, '✪', GRAY, YELLOW, YELLOW, YELLOW, YELLOW, GOLD, GRAY)
//		else if (stars < 1400) return formatStars(stars, '✪', GRAY, AQUA, AQUA, AQUA, AQUA, DARK_AQUA, GRAY)
//		else if (stars < 1500) return formatStars(stars, '✪', GRAY, GREEN, GREEN, GREEN, GREEN, DARK_GREEN, GRAY)
//		else if (stars < 1600) return formatStars(stars, '✪', GRAY, DARK_AQUA, DARK_AQUA, DARK_AQUA, DARK_AQUA, BLUE, GRAY)
//		else if (stars < 1700) return formatStars(stars, '✪', GRAY, RED, RED, RED, RED, DARK_RED, GRAY)
//		else if (stars < 1800) return formatStars(stars, '✪', GRAY, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE, GRAY)
//		else if (stars < 1900) return formatStars(stars, '✪', GRAY, BLUE, BLUE, BLUE, BLUE, DARK_BLUE, GRAY)
//		else if (stars < 2000) return formatStars(stars, '✪', GRAY, DARK_PURPLE, DARK_PURPLE, DARK_PURPLE, DARK_PURPLE, DARK_GRAY, GRAY)
//		else if (stars < 2100) return formatStars(stars, '✪', DARK_GRAY, GRAY, WHITE, WHITE, GRAY, GRAY, DARK_GRAY)

//		else if (stars < 2200) return formatStars(stars, '⚝', WHITE, WHITE, YELLOW, YELLOW, GOLD, GOLD, GOLD)
//		else if (stars < 2300) return formatStars(stars, '⚝', GOLD, GOLD, WHITE, WHITE, AQUA, DARK_AQUA, DARK_AQUA)
//		else if (stars < 2400) return formatStars(stars, '⚝', DARK_PURPLE, DARK_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, GOLD, YELLOW, YELLOW)
//		else if (stars < 2500) return formatStars(stars, '⚝', AQUA, AQUA, WHITE, WHITE, GRAY, GRAY, DARK_GRAY)
//		else if (stars < 2600) return formatStars(stars, '⚝', WHITE, WHITE, GREEN, GREEN, DARK_GRAY, DARK_GRAY, DARK_GRAY)
//		else if (stars < 2700) return formatStars(stars, '⚝', DARK_RED, DARK_RED, RED, RED, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE)
//		else if (stars < 2800) return formatStars(stars, '⚝', YELLOW, YELLOW, WHITE, WHITE, DARK_GRAY, DARK_GRAY, DARK_GRAY)
//		else if (stars < 2900) return formatStars(stars, '⚝', GREEN, GREEN, DARK_GREEN, DARK_GREEN, GOLD, GOLD, YELLOW)
//		else if (stars < 3000) return formatStars(stars, '⚝', AQUA, AQUA, DARK_AQUA, DARK_AQUA, BLUE, BLUE, DARK_BLUE)
//		else if (stars < 3100) return formatStars(stars, '⚝', YELLOW, YELLOW, GOLD, GOLD, RED, RED, DARK_RED)
//		else if (stars < 3200) return formatStars(stars, '✥', BLUE, BLUE, AQUA, AQUA, GOLD, GOLD, YELLOW)
//		else if (stars < 3300) return formatStars(stars, '✥', RED, DARK_RED, GRAY, GRAY, DARK_RED, RED, RED)
//		else if (stars < 3400) return formatStars(stars, '✥', BLUE, BLUE, BLUE, LIGHT_PURPLE, RED, RED, DARK_RED)
//		else if (stars < 3500) return formatStars(stars, '✥', DARK_GREEN, GREEN, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE, DARK_PURPLE, DARK_GREEN)
//		else if (stars < 3600) return formatStars(stars, '✥', RED, RED, DARK_RED, DARK_RED, DARK_GREEN, GREEN, GREEN)
//		else if (stars < 3700) return formatStars(stars, '✥', GREEN, GREEN, GREEN, AQUA, BLUE, BLUE, DARK_BLUE)
//		else if (stars < 3800) return formatStars(stars, '✥', DARK_RED, DARK_RED, RED, RED, AQUA, DARK_AQUA, DARK_AQUA)
//		else if (stars < 3900) return formatStars(stars, '✥', DARK_BLUE, DARK_BLUE, BLUE, DARK_PURPLE, DARK_PURPLE, LIGHT_PURPLE, DARK_BLUE)
//		else if (stars < 4000) return formatStars(stars, '✥', RED, RED, GREEN, GREEN, AQUA, BLUE, BLUE)
//		else if (stars < 4100) return formatStars(stars, '✥', DARK_PURPLE, DARK_PURPLE, RED, RED, GOLD, GOLD, YELLOW)
//		else if (stars < 4200) return formatStars(stars, '✥', YELLOW, YELLOW, GOLD, RED, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE)
//		else if (stars < 4300) return formatStars(stars, '✥', DARK_BLUE, BLUE, DARK_AQUA, AQUA, WHITE, GRAY, GRAY)
//		else if (stars < 4400) return formatStars(stars, '✥', BLACK, DARK_PURPLE, DARK_GRAY, DARK_GRAY, DARK_PURPLE, DARK_PURPLE, BLACK)
//		else if (stars < 4500) return formatStars(stars, '✥', DARK_GREEN, DARK_GREEN, GREEN, YELLOW, GOLD, DARK_PURPLE, LIGHT_PURPLE)
//		else if (stars < 4600) return formatStars(stars, '✥', WHITE, WHITE, AQUA, AQUA, DARK_AQUA, DARK_AQUA, DARK_AQUA)
//		else if (stars < 4700) return formatStars(stars, '✥', DARK_AQUA, AQUA, YELLOW, YELLOW, GOLD, LIGHT_PURPLE, DARK_PURPLE)
//		else if (stars < 4800) return formatStars(stars, '✥', WHITE, DARK_RED, RED, RED, BLUE, DARK_BLUE, BLUE)
//		else if (stars < 4900) return formatStars(stars, '✥', DARK_PURPLE, DARK_PURPLE, RED, GOLD, YELLOW, AQUA, DARK_AQUA)
//		else if (stars < 5000) return formatStars(stars, '✥', DARK_GREEN, GREEN, WHITE, WHITE, GREEN, GREEN, DARK_GREEN)
//		else return formatStars(stars, '✥', DARK_RED, DARK_RED, DARK_PURPLE, BLUE, BLUE, DARK_BLUE, BLACK)
		""
	}

	def formatStars(str: String, colors: List[EnumChatFormatting]): String = {

		if(colors.length == 1){
			style.setColor(colors.head)
			style.getFormattingCode + str
		}
		else {
			(for (i <- colors.indices) yield {
				s"${style.setColor(colors(i)).getFormattingCode}${str(i)}"
			}).foldLeft("")(_+_)
		}
	}

}




class SingleValue(val stringPath: String){
	val typeStr = "String"
	def getValueMethod(): HypixelPlayerData => Any = {
		 (player: HypixelPlayerData) => if(player.playerLoaded) player.player.getStringProperty(stringPath, "") else ""
	}
}

class SingleNumericValue(override val stringPath: String) extends SingleValue (stringPath = stringPath){
	override val typeStr = "Double"
	override def getValueMethod(): HypixelPlayerData => Double = {
		(player: HypixelPlayerData) => if(player.playerLoaded) player.player.getDoubleProperty(stringPath, -1) else -10
	}
	def getValueRoundedMethod(): HypixelPlayerData => Double = (p: HypixelPlayerData) => (getValueMethod()(p) * 100).round / 100.0
}

class RatioValue(val topPath: String, val botPath: String) extends SingleNumericValue(stringPath = topPath){
	override val typeStr = "Double"
	override def getValueMethod(): HypixelPlayerData => Double = {
		(player: HypixelPlayerData) => if(player.playerLoaded) (player.player.getDoubleProperty(topPath, -1) / player.player.getDoubleProperty(botPath, 1.0)) else -1.0
	}
}
