package Jaxaar.BARSOverlay.DataStructures

import Jaxaar.BARSOverlay.Utils.{ScoreboardSidebarReader}
import net.hypixel.api.reply.PlayerReply.Player
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.{ChatComponentTranslation, IChatComponent}

import java.util.UUID

class HypixelPlayerData(val player: Player){
//	println("New Player")

	def getUUID: UUID = player.getUuid
	def getName: String = player.getName

	def getNameComponent: IChatComponent = {
		new ChatComponentTranslation(getName)
	}

	def getTeamColorStyling: String = {
		ScoreboardSidebarReader.getPlayersTeam(getName)
	}

	def getStars: Int = player.getIntProperty("achievements.bedwars_level", -1)
	def getFKDR: Double = (player.getDoubleProperty("stats.Bedwars.final_kills_bedwars", 0) / player.getDoubleProperty("stats.Bedwars.final_deaths_bedwars", 1) *100).round/100.0

	override def toString: String = {
		s"[HypixelPlayerData: UUID=${getUUID} Name=${getName} ]"
	}

}

