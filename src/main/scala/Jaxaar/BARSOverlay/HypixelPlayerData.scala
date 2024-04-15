package Jaxaar.BARSOverlay

import Jaxaar.BARSOverlay.Utils.APIRequestInterpreter
import BarsOverlayMod.hyAPI
import net.hypixel.api.reply.PlayerReply.Player
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.{ChatComponentTranslation, IChatComponent}

import java.util.UUID

class HypixelPlayerData(val networkPlayerInfo: NetworkPlayerInfo){
	println("New Player")

	var player: Player = null
	def getUUID: UUID = networkPlayerInfo.getGameProfile.getId
	def getTrueName: String = networkPlayerInfo.getGameProfile.getName

	reload()

	def playerLoaded: Boolean = {
//		println("Hi")
		if(player != null){
			return true
		}
		player = APIRequestInterpreter.newlyLoadedPLayers.getOrDefault(getUUID, null)
		if(player != null){
			APIRequestInterpreter.newlyLoadedPLayers.remove(getUUID)
			return true
		}
		false
	}

	def getNameComponent: IChatComponent = {
		if(networkPlayerInfo.getDisplayName != null)
			networkPlayerInfo.getDisplayName
		else
			new ChatComponentTranslation(networkPlayerInfo.getGameProfile.getName)
	}

	def reload(): Unit = {
		player = null
		try{
			APIRequestInterpreter.fetchPlayerStats(hyAPI, getUUID)
		} catch {
			case e: Throwable => {
				if (e.getMessage.contains("401")) {
					println("No Authorization")
					println(e.getMessage)
				}
			}
		}
	}

	def getStars: String = s"✫${if(playerLoaded) player.getIntProperty("achievements.bedwars_level", 0) else 0}"
	def getWins: String = s"${if(playerLoaded) player.getIntProperty("stats.Bedwars.wins_bedwars", 0) else 0}"
	def getWLR: String = s"${if(playerLoaded) (player.getDoubleProperty("stats.Bedwars.wins_bedwars", 0) / player.getDoubleProperty("stats.Bedwars.losses_bedwars", 1) *100).round/100.0 else 0}"
	def getFKDR: String = s"${if(playerLoaded) (player.getDoubleProperty("stats.Bedwars.final_kills_bedwars", 0) / player.getDoubleProperty("stats.Bedwars.final_deaths_bedwars", 1) *100).round/100.0 else 0}"

	override def toString: String = {
		s"[HypixelPlayerData: UUID=${getUUID} RealName=${getTrueName} Loaded=${playerLoaded}]"
	}

}