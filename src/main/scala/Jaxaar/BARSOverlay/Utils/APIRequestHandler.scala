package Jaxaar.BARSOverlay.Utils

import Jaxaar.BARSOverlay.BarsOverlayMod.{hyAPI, mc}
import Jaxaar.BARSOverlay.DataStructures.HypixelPlayerData
import Jaxaar.BARSOverlay.OverlayManager.logger
import net.hypixel.api.HypixelAPI
import net.hypixel.api.reply.PlayerReply
import net.hypixel.api.reply.PlayerReply.Player
import net.minecraft.util.ChatComponentTranslation

import scala.collection.mutable
import java.util.UUID
import scala.concurrent.{Await, ExecutionContext, TimeoutException}
import scala.concurrent.duration.DurationInt
import scala.compat.java8.FutureConverters._
import scala.util.{Failure, Success}

object APIRequestHandler {

	private var validAPIKey = true;
	private var lastRequestLimitReached = 0L;
	private val playerCache: mutable.Map[UUID, HypixelPlayerTime] = mutable.Map()
	private val loadingPlayers: mutable.Map[UUID, Boolean] = mutable.Map()

	def APIKeyIsValid: Boolean = APIRequestHandler.validAPIKey
	def canMakeAPIRequest: Boolean = APIKeyIsValid && (curTimeSeconds - APIRequestHandler.lastRequestLimitReached) > 300


	def getPlayerStats(uuid: UUID): Option[HypixelPlayerTime] = {
		val result: Option[HypixelPlayerTime] = playerCache.get(uuid)
		if ((result.isEmpty || curTimeSeconds > result.get.lastUpdated + 600) &&
		    canMakeAPIRequest && uuid.version() != 2){
			fetchPlayerStats(hyAPI, uuid)
		}
		result
	}

	def clearPlayerCache = {
		playerCache.clear()
		loadingPlayers.clear()
	}

	class HypixelPlayerTime(val lastUpdated: Long, val player: Player)
	def curTimeSeconds: Long = System.currentTimeMillis()/1000


	def fetchPlayerStats(api: HypixelAPI, uuid: UUID): Unit = {
		if(loadingPlayers.getOrElse(uuid, false)){
			return
		}
		logger.info("Fetching player: " + uuid)
		loadingPlayers.put(uuid, true)
		api.getPlayerByUuid(uuid).toScala.onComplete {
			case Failure(e) => onErr(e)
			case Success(value) => {
				loadingPlayers.remove(uuid)
				logger.info(s"LOADED: ${value.getPlayer.getName} - "  + uuid.toString);
				playerCache.put(uuid, new HypixelPlayerTime(curTimeSeconds, value.getPlayer))
			}
		}
	}

	def testAPIKey(api: HypixelAPI): Boolean = {
		api.getPunishmentStats.toScala.onComplete {
			case Failure(e) => onErr(e)
			case Success(value) => {
				logger.info("APIKey Valid");
				validAPIKey = true;
				return true
			}
		}
		false
	}

	def onErr(e: Throwable): Unit ={
		println("Error w/ Hypixel API Req: " + e.getMessage);
		if(e.getMessage.contains("403")){
			println("API-Key expired");
			validAPIKey = false;
		}
		else if (e.getMessage.contains("429")){
			println("Request Limit reached");
			mc.thePlayer.addChatMessage(new ChatComponentTranslation("Your API-Key has reached the request limit, please wait a few minutes and then clear your player cache (Ctrl-Alt-Z)"));
			lastRequestLimitReached = System.currentTimeMillis()/1000;
		}
	}
}
