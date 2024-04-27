package Jaxaar.BARSOverlay.DataStructures

import net.minecraft.util.EnumChatFormatting._
import net.minecraft.util.{ChatStyle, EnumChatFormatting}


object OverlayConf {

	def getColumnValues: List[ColumnValues] = {
		List(
			new PlayerColumnValues(
				title = "Player",
				fieldLength = 100,
				colorBreakpoints = List(0),
				stars = new SingleNumericValue("achievements.bedwars_level")
			),
			new StatsColumnValues(
				title = "WS",
				fieldLength = 18,
				isInt = true,
				colorBreakpoints =  List(4, 10, 25, 50, 100),
				value = new SingleNumericValue("stats.Bedwars.winstreak")
			),
			new StatsColumnValues(
				title = "FKDR",
				fieldLength = 30,
				colorBreakpoints =  List(1,3,5,10,25),
				value = new RatioValue("stats.Bedwars.final_kills_bedwars", "stats.Bedwars.final_deaths_bedwars")
			),
			new StatsColumnValues(
				title = "WLR",
				fieldLength = 25,
				colorBreakpoints =  List(1,2,5,7,10),
				value = new RatioValue("stats.Bedwars.wins_bedwars", "stats.Bedwars.losses_bedwars")
			),
			new StatsColumnValues(
				title = "Finals",
				fieldLength = 35,
				isInt = true,
				colorBreakpoints =  List(1000, 5000, 10000, 20000, 30000),
				value = new SingleNumericValue("stats.Bedwars.final_kills_bedwars")
			),
			new StatsColumnValues(
				title = "Wins",
				fieldLength = 30,
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

	def colorFunction(d: Double): EnumChatFormatting = {
		val colors = List(
			GRAY,
			WHITE,
			GOLD,
			AQUA,
			RED,
			DARK_PURPLE
		)

		val numGreater = colorBreakpoints.count(_ >= d)
		colors((colorBreakpoints.length) - numGreater)
	}

	def getVal(player: HypixelPlayerData): Double = {
		value.getValueRoundedMethod()(player)
	}
}

class PlayerColumnValues(val stars: SingleNumericValue = null, override val title: String, override val fieldLength: Int, override val colorBreakpoints: List[Double] = List()) extends ColumnValues(title = title, fieldLength=fieldLength, colorBreakpoints = colorBreakpoints){
	val style = new ChatStyle()

	override def getFormattedString(player: HypixelPlayerData): String = {

		s"${getStyledStars(getStars(player))} ${getPlayerName(player)}"
	}

	def getPlayerName(playerData: HypixelPlayerData): String = {
		playerData.getTeamColorStyling + playerData.getName
	}

	def getStars(player: HypixelPlayerData): Int = {
		stars.getValueRoundedMethod()(player).intValue()
	}

	def getStyledStars(stars: Int): String = {
//		return s"[${stars}✫]"

		return stars match {
			case x if x < 0 => formatStars(s"[-1✫]", List(YELLOW)) //TODO Actually make it [?✫] at some point but not yet
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

			case x if x < 2200 => formatStars(s"[${x}⚝]", List(WHITE, WHITE, YELLOW, YELLOW, GOLD, GOLD, GOLD))
			case x if x < 2300 => formatStars(s"[${x}⚝]", List(GOLD, GOLD, WHITE, WHITE, AQUA, DARK_AQUA, DARK_AQUA))
			case x if x < 2400 => formatStars(s"[${x}⚝]", List(DARK_PURPLE, DARK_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, GOLD, YELLOW, YELLOW))
			case x if x < 2500 => formatStars(s"[${x}⚝]", List(AQUA, AQUA, WHITE, WHITE, GRAY, GRAY, DARK_GRAY))
			case x if x < 2600 => formatStars(s"[${x}⚝]", List(WHITE, WHITE, GREEN, GREEN, DARK_GRAY, DARK_GRAY, DARK_GRAY))
			case x if x < 2700 => formatStars(s"[${x}⚝]", List(DARK_RED, DARK_RED, RED, RED, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE))
			case x if x < 2800 => formatStars(s"[${x}⚝]", List(YELLOW, YELLOW, WHITE, WHITE, DARK_GRAY, DARK_GRAY, DARK_GRAY))
			case x if x < 2900 => formatStars(s"[${x}⚝]", List(GREEN, GREEN, DARK_GREEN, DARK_GREEN, GOLD, GOLD, YELLOW))
			case x if x < 3000 => formatStars(s"[${x}⚝]", List(AQUA, AQUA, DARK_AQUA, DARK_AQUA, BLUE, BLUE, DARK_BLUE))
			case x if x < 3100 => formatStars(s"[${x}⚝]", List(YELLOW, YELLOW, GOLD, GOLD, RED, RED, DARK_RED))

			case x if x < 3200 => formatStars(s"[${x}✥]", List(BLUE, BLUE, AQUA, AQUA, GOLD, GOLD, YELLOW))
			case x if x < 3300 => formatStars(s"[${x}✥]", List(RED, DARK_RED, GRAY, GRAY, DARK_RED, RED, RED))
			case x if x < 3400 => formatStars(s"[${x}✥]", List(BLUE, BLUE, BLUE, LIGHT_PURPLE, RED, RED, DARK_RED))
			case x if x < 3500 => formatStars(s"[${x}✥]", List(DARK_GREEN, GREEN, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE, DARK_PURPLE, DARK_GREEN))
			case x if x < 3600 => formatStars(s"[${x}✥]", List(RED, RED, DARK_RED, DARK_RED, DARK_GREEN, GREEN, GREEN))
			case x if x < 3700 => formatStars(s"[${x}✥]", List(GREEN, GREEN, GREEN, AQUA, BLUE, BLUE, DARK_BLUE))
			case x if x < 3800 => formatStars(s"[${x}✥]", List(DARK_RED, DARK_RED, RED, RED, AQUA, DARK_AQUA, DARK_AQUA))
			case x if x < 3900 => formatStars(s"[${x}✥]", List(DARK_BLUE, DARK_BLUE, BLUE, DARK_PURPLE, DARK_PURPLE, LIGHT_PURPLE, DARK_BLUE))
			case x if x < 4000 => formatStars(s"[${x}✥]", List(RED, RED, GREEN, GREEN, AQUA, BLUE, BLUE))
			case x if x < 4100 => formatStars(s"[${x}✥]", List(DARK_PURPLE, DARK_PURPLE, RED, RED, GOLD, GOLD, YELLOW))
			case x if x < 4200 => formatStars(s"[${x}✥]", List(YELLOW, YELLOW, GOLD, RED, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE))
			case x if x < 4300 => formatStars(s"[${x}✥]", List(DARK_BLUE, BLUE, DARK_AQUA, AQUA, WHITE, GRAY, GRAY))
			case x if x < 4400 => formatStars(s"[${x}✥]", List(BLACK, DARK_PURPLE, DARK_GRAY, DARK_GRAY, DARK_PURPLE, DARK_PURPLE, BLACK))
			case x if x < 4500 => formatStars(s"[${x}✥]", List(DARK_GREEN, DARK_GREEN, GREEN, YELLOW, GOLD, DARK_PURPLE, LIGHT_PURPLE))
			case x if x < 4600 => formatStars(s"[${x}✥]", List(WHITE, WHITE, AQUA, AQUA, DARK_AQUA, DARK_AQUA, DARK_AQUA))
			case x if x < 4700 => formatStars(s"[${x}✥]", List(DARK_AQUA, AQUA, YELLOW, YELLOW, GOLD, LIGHT_PURPLE, DARK_PURPLE))
			case x if x < 4800 => formatStars(s"[${x}✥]", List(WHITE, DARK_RED, RED, RED, BLUE, DARK_BLUE, BLUE))
			case x if x < 4900 => formatStars(s"[${x}✥]", List(DARK_PURPLE, DARK_PURPLE, RED, GOLD, YELLOW, AQUA, DARK_AQUA))
			case x if x < 5000 => formatStars(s"[${x}✥]", List(DARK_GREEN, GREEN, WHITE, WHITE, GREEN, GREEN, DARK_GREEN))
			case _ => formatStars(s"[${stars}✥]", List(DARK_RED, DARK_RED, DARK_PURPLE, BLUE, BLUE, DARK_BLUE, BLACK))
		}
		""
	}


	def formatStars(str: String, colors: List[EnumChatFormatting]): String = {

		val outstr = if(colors.length == 1){
			style.setColor(colors.head)
			style.getFormattingCode + str
		}
		else {
			(for (i <- colors.indices) yield {
				s"${style.setColor(colors(i)).getFormattingCode}${str(i)}"
			}).foldLeft("")(_+_)
		}
		val paddingSpaces = " " * (7 - str.length)
		outstr //+ paddingSpaces
	}
}




class SingleValue(val stringPath: String){
	val typeStr = "String"
	def getValueMethod(): HypixelPlayerData => Any = {
		 (player: HypixelPlayerData) => player.getStringProperty(stringPath, "")
	}
}

class SingleNumericValue(override val stringPath: String) extends SingleValue (stringPath = stringPath){
	override val typeStr = "Double"
	override def getValueMethod(): HypixelPlayerData => Double = {
		(player: HypixelPlayerData) => player.getDoubleProperty(stringPath, -1)
	}
	def getValueRoundedMethod(): HypixelPlayerData => Double = (p: HypixelPlayerData) => (getValueMethod()(p) * 100).round / 100.0
}

class RatioValue(val topPath: String, val botPath: String) extends SingleNumericValue(stringPath = topPath){
	override val typeStr = "Double"
	override def getValueMethod(): HypixelPlayerData => Double = {
		(player: HypixelPlayerData) => player.getDoubleRatio(topPath, botPath, 0, 1)
	}
}
