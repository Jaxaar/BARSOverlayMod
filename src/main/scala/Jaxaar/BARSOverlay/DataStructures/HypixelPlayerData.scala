package Jaxaar.BARSOverlay.DataStructures

import Jaxaar.BARSOverlay.BarsOverlayMod.mc
import Jaxaar.BARSOverlay.Utils.ScoreboardSidebarReader
import net.hypixel.api.reply.PlayerReply.Player
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.{ChatComponentTranslation, ChatStyle, EnumChatFormatting, IChatComponent}

import java.util.UUID

trait HasProperties {
	def getStringProperty (path:String, default:String): String
	def getDoubleProperty(path:String, default:Double): Double
	def getDoubleRatio(topPath: String, botPath: String, topDefault: Double, botDefault: Double): Double
}

class HypixelPlayerData(val uuid: UUID, val player: Player) extends HasProperties {
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

	def getStringProperty(path:String, default:String): String = player.getStringProperty(path, default)
	def getDoubleProperty(path:String, default:Double): Double = player.getDoubleProperty(path, default)
	def getDoubleRatio(topPath: String, botPath: String, topDefault: Double, botDefault: Double): Double = {player.getDoubleProperty(topPath, topDefault) / player.getDoubleProperty(botPath, botDefault)}

	override def toString: String = {
		s"[HypixelPlayerData: UUID=${getUUID} Name=${getName} ]"
	}
}

class HypixelPlayerDataIsNone(override val uuid: UUID, val name: String = "NameNotFound") extends HypixelPlayerData(uuid, null){
	val networkPlayerInfo: NetworkPlayerInfo = mc.thePlayer.sendQueue.getPlayerInfo(uuid)
	override def getUUID: UUID = uuid
	override def getName: String = if (networkPlayerInfo != null) networkPlayerInfo.getGameProfile.getName else name

	override def getTeamColorStyling: String = {
		new ChatStyle().setColor(EnumChatFormatting.YELLOW).getFormattingCode
	}

	override def getStars: Int = -1
	override def getFKDR: Double = 0

	override def getStringProperty(path:String, default:String): String = "-"
	override def getDoubleProperty(path:String, default:Double): Double = -1
	override def getDoubleRatio(topPath: String, botPath: String, topDefault: Double, botDefault: Double): Double = -1

	override def toString: String = {
		s"[HypixelPlayerData: UUID=${getUUID} Name=${getName} NotInDB]"
	}
}

