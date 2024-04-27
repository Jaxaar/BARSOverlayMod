package Jaxaar.BARSOverlay.Utils

import Jaxaar.BARSOverlay.BarsOverlayMod.{hyAPI, mc}
import Jaxaar.BARSOverlay.DataStructures.HypixelPlayerData
import Jaxaar.BARSOverlay.OverlayManager.{logger, updateCurPlayersDict}
import Jaxaar.BARSOverlay.Utils.Helpers.MapAsScala
import net.hypixel.api.HypixelAPI
import net.hypixel.api.reply.{PlayerReply, PunishmentStatsReply}
import net.hypixel.api.reply.PlayerReply.Player
import net.minecraft.util.ChatComponentTranslation

import java.lang
import java.util.UUID
import java.util.function.Consumer
import scala.collection.mutable

object APIRequestHandler{

	var validAPIKey = true
	var lastRequestLimitReached = 0L
	var playerCache: mutable.Map[UUID, HypixelPlayerTime] = mutable.Map[UUID, HypixelPlayerTime]()
	var loadingPlayers: mutable.Map[UUID, Boolean] = mutable.Map[UUID, Boolean]()

	def APIKeyIsValid: Boolean = validAPIKey
	def canMakeAPIRequest: Boolean = APIKeyIsValid && (curTimeSeconds - lastRequestLimitReached) > 300

	def getPlayerStats(uuid: UUID): Option[HypixelPlayerTime] = {
		val result: Option[HypixelPlayerTime] = playerCache.get(uuid)
		if ((result.isEmpty || curTimeSeconds > result.get.lastUpdated + 600) &&
		  canMakeAPIRequest && uuid.version() != 2){
			fetchPlayerStats(hyAPI, uuid)
			return null
		}
		if(result.isDefined && !result.get.player.exists()){
			return None
		}
		result
	}

	def clearPlayerCache(): Unit = {
		playerCache.clear()
		loadingPlayers.clear()
	}

	class HypixelPlayerTime(val lastUpdated: Long, val player: Player)
	def curTimeSeconds: Long = System.currentTimeMillis()/1000




	val javaAPIOnErr: Consumer[Throwable] = new Consumer[Throwable]() {
		def accept(e: Throwable): Unit = {
			onErr(e)
		}
	}

	def fetchPlayerStats(api: HypixelAPI, uuid: UUID): Unit = {
		if(loadingPlayers.getOrElse(uuid, false) || !canMakeAPIRequest){
			return
		}
		loadingPlayers.put(uuid, true)
		logger.info("Fetching player: " + uuid)
		val onfetchPlayerStatsReply: Consumer[PlayerReply] = new Consumer[PlayerReply]() {
			def accept(reply: PlayerReply): Unit = {
	            loadingPlayers.remove(uuid);
	            logger.info("LOADED: " + reply.getPlayer.getName + " - " + uuid.toString);
	            playerCache.put(uuid, new HypixelPlayerTime(curTimeSeconds, reply.getPlayer));
				updateCurPlayersDict()
			}
		}
		APIRequestTranslator.fetchPlayerStats(api, uuid, onfetchPlayerStatsReply, javaAPIOnErr)
	}

	def testAPIKey(api: HypixelAPI): Unit = {
		val onTestAPIKeyReply: Consumer[PunishmentStatsReply] = new Consumer[PunishmentStatsReply]() {
			def accept(pr: PunishmentStatsReply): Unit = {
				logger.info("APIKey Valid");
	            validAPIKey = true;
			}
		}
		APIRequestTranslator.testAPIKey(api, onTestAPIKeyReply, javaAPIOnErr)
	}


	def onErr(e: Throwable): Unit = {
		logger.info("Error w/ Hypixel API Req: " + e.getMessage)
		if (e.getMessage.contains("403")) {
			logger.error("API-Key expired")
			validAPIKey = false
		}
		else if (e.getMessage.contains("429")) {
			logger.error("Request Limit reached")
			mc.thePlayer.addChatMessage(new ChatComponentTranslation("Your API-Key has reached the request limit, please wait a few minutes and then clear your player cache (Ctrl-Alt-Z)"))
			lastRequestLimitReached = System.currentTimeMillis / 1000
		}
	}
}
