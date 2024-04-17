package Jaxaar.BARSOverlay.Utils

import Jaxaar.BARSOverlay.BarsOverlayMod.mc
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

	override def toString: String = {
//		print("ts")
		getSidebarList.map(s => "\n"+s).toString()
	}


//	Improve**
	def stripColorCodes(str: String): String = {
		var strOut = str
		var index = 0
		while(index < strOut.length){
			if(strOut(index) == '§'){
				strOut = strOut.substring(0, index) + strOut.substring(index+2, strOut.length)
			}
			index += 1
		}
		strOut
	}


	var bedwarsGameCache = false
	def isBedwarsGame: Boolean = bedwarsGameCache
	def verifyIsBedwarsGame: Boolean ={
		val sbList = getSidebarList
		bedwarsGameCache = (sbList.length >= 2) && sbList(0).contains("BED WARS") && sbList(1).contains("m")
		isBedwarsGame
	}


}
