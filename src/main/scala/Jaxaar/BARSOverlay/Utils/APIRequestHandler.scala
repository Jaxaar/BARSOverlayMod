package Jaxaar.BARSOverlay.Utils

import Jaxaar.BARSOverlay.BarsOverlayMod
import Jaxaar.BARSOverlay.BarsOverlayMod.{hyAPI, jaxAPI, mc}
import Jaxaar.BARSOverlay.DataStructures.HypixelPlayerData
import Jaxaar.BARSOverlay.HttpRequests.MojangPlayerData
import Jaxaar.BARSOverlay.OverlayManager.{logger, searchedPlayers, updateCurPlayersDict}
import Jaxaar.BARSOverlay.Utils.Helpers.MapAsScala
import com.mojang.api.profiles.minecraft.HttpProfileRepository
import net.hypixel.api.HypixelAPI
import net.hypixel.api.reply.{PlayerReply, PunishmentStatsReply}
import net.hypixel.api.reply.PlayerReply.Player
import net.minecraft.command.ICommandSender
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

	var mojangLoadingPlayers: mutable.Map[String, Boolean] = mutable.Map[String, Boolean]()

	//How long the cache keeps the player data
	val cacheDuration = 600

	def APIKeyIsValid: Boolean = validAPIKey
	def canMakeAPIRequest: Boolean = APIKeyIsValid && (curTimeSeconds - lastRequestLimitReached) > 30

	def getPlayerStats(uuid: UUID): Option[HypixelPlayerTime] = {
		val result: Option[HypixelPlayerTime] = playerCache.get(uuid)
		if ((result.isEmpty || curTimeSeconds > result.get.lastUpdated + cacheDuration) &&
		  canMakeAPIRequest && uuid.version() != 2){
			fetchPlayerStatsByUUID(uuid)
			return null
		}
		if(result.isDefined && !result.get.player.exists()){
			return None
		}
		result
	}

	def isPlayerUpToDate(uuid: UUID): Option[Boolean] = {
		val result: Option[HypixelPlayerTime] = playerCache.get(uuid)
		if(result.isEmpty){
			None
		}
		else {
			Option(curTimeSeconds <= result.get.lastUpdated + cacheDuration)
		}
	}

	def clearPlayerCache(): Unit = {
		playerCache.clear()
		loadingPlayers.clear()
		mojangLoadingPlayers.clear()
	}

	class HypixelPlayerTime(val lastUpdated: Long, val player: Player)
	def curTimeSeconds: Long = System.currentTimeMillis()/1000




	val javaAPIOnErr: Consumer[Throwable] = new Consumer[Throwable]() {
		def accept(e: Throwable): Unit = {
			onErr(e)
		}
	}

	def fetchPlayerStatsByUUID(uuid: UUID, api: HypixelAPI = hyAPI): Unit = {
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

	def testAPIKey(api: HypixelAPI = hyAPI): Unit = {
		val onTestAPIKeyReply: Consumer[PunishmentStatsReply] = new Consumer[PunishmentStatsReply]() {
			def accept(pr: PunishmentStatsReply): Unit = {
				logger.info("APIKey Valid");
	            validAPIKey = true;
			}
		}
		APIRequestTranslator.testAPIKey(api, onTestAPIKeyReply, javaAPIOnErr)
	}

	def fetchPlayerStatsByName(name: String, api: HypixelAPI = hyAPI): Unit = {

	}

	def fetchMojangPlayerStats(name: String, sender: ICommandSender = null): Unit = {
		if(mojangLoadingPlayers.getOrElse(name, false) || !canMakeAPIRequest){
			return
		}
		mojangLoadingPlayers.put(name, true)
		logger.info("Fetching Mojang UUID Data - player: " + name)

		val onfetchPlayerStatsReply: Consumer[MojangPlayerData] = new Consumer[MojangPlayerData]() {
			def accept(mp: MojangPlayerData): Unit = {
				val uuid = mp.getUUID
				if(uuid == null){
					sender.addChatMessage(new ChatComponentTranslation(s"Failed to load: ${name}!"))
					return
				}
				logger.info("LOADED: " + name + " - " + uuid.toString);
				if(!searchedPlayers.contains(uuid)){
					searchedPlayers = searchedPlayers :+ uuid
					logger.info("UUIDS: " + searchedPlayers)
				}
				if(sender != null){
					sender.addChatMessage(new ChatComponentTranslation(s"Loaded ${name}"));
				}
				fetchPlayerStatsByUUID(uuid)

				mojangLoadingPlayers.remove(name)
			}
		}

		val mojangJavaAPIOnErr: Consumer[Throwable] = new Consumer[Throwable]() {
			def accept(e: Throwable): Unit = {
				mc.thePlayer.addChatMessage(new ChatComponentTranslation("Player not found"))
				onErr(e)
			}
		}

		APIRequestTranslator.fetchMojangPlayerData(jaxAPI, name, onfetchPlayerStatsReply, mojangJavaAPIOnErr)
	}


	def onErr(e: Throwable): Unit = {
		logger.info("Error w/ API Req: " + e.getMessage)
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
