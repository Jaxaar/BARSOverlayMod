package Jaxaar.BARSOverlay.Utils

import Jaxaar.BARSOverlay.BarsOverlayMod.mc
import Jaxaar.BARSOverlay.Utils.Helpers.stripColorCodes
import net.minecraft.scoreboard.ScorePlayerTeam

import scala.collection.JavaConverters.collectionAsScalaIterableConverter

object ScoreboardSidebarReader {

	def getSidebarList: List[String] = {
		val sbMap = readSidebarToMap
		(for( i <- 15 to 1 by -1 if sbMap.getOrElse(i, "") != "") yield sbMap.getOrElse(i, "")).toList.map((s) => stripColorCodes(s))
	}

	def readSidebarToMap: Map[Int, String] = {
		val scoreboard = mc.theWorld.getScoreboard
		val map = scoreboard.getScores.asScala.map((score1) => {
			val scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName)
			val s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName)
			(score1.getScorePoints, s1)
		}).toMap
		if(scoreboard.getObjectiveInDisplaySlot(1) != null) {
			map + (15 -> stripColorCodes(scoreboard.getObjectiveInDisplaySlot(1).getDisplayName))
		} else {
			map
		}
	}

	def getPlayersTeam(username: String):String = {
		try {
			val scoreboard = mc.theWorld.getScoreboard
			val team = scoreboard.getPlayersTeam(username)
//			println(s"${username}: ${team.getColorPrefix}")
			return team.getColorPrefix
		} catch {
			case e => //print(s"Err- SB: ${username}")
		}
		""
	}

	override def toString: String = {
//		print("ts")
		getSidebarList.map(s => "\n"+s).toString()
	}


	var bedwarsGameCache = false
//	def isBedwarsGame: Boolean = bedwarsGameCache
	def verifyIsBedwarsGame: Boolean = {
		val sbList = getSidebarList
		bedwarsGameCache = (sbList.length >= 2) && sbList(0).contains("BED WARS") && sbList(1).contains("m")
		isBedwarsGame
	}

	def isBedwarsGame = true

	def isHypixel: Boolean = {
		val sbList = getSidebarList
		println(sbList.length)
		(sbList.length >= 15) && sbList(15).contains("hypixel")
	}

}
