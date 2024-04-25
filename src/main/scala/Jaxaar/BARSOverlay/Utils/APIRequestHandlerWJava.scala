package Jaxaar.BARSOverlay.Utils

import Jaxaar.BARSOverlay.BarsOverlayMod.{hyAPI, mc}
import Jaxaar.BARSOverlay.DataStructures.HypixelPlayerData
import Jaxaar.BARSOverlay.OverlayManager.logger
import net.hypixel.api.HypixelAPI
import net.hypixel.api.reply.PlayerReply
import net.hypixel.api.reply.PlayerReply.Player
import net.minecraft.util.ChatComponentTranslation

import java.lang
import java.util.UUID
import java.util.concurrent.{Executors, ThreadPoolExecutor}
import scala.concurrent.{Await, ExecutionContext, TimeoutException}
import scala.concurrent.duration.DurationInt
import scala.compat.java8.FutureConverters._
import scala.util.{Failure, Success}
//import ExecutionContext.Implicits.global
import scala.collection.JavaConverters.mapAsScalaMapConverter


object APIRequestHandlerWJava {


	def validAPIKey: Boolean = APIRequestTranslator.validAPIKey;
	def lastRequestLimitReached: lang.Long = APIRequestTranslator.lastRequestLimitReached
	def playerCache: Map[UUID, HypixelPlayerTime] = APIRequestTranslator.playerCache.asScala.map((value) => (value._1, new HypixelPlayerTime(curTimeSeconds, value._2.getPlayer))).toMap
	def loadingPlayers: Map[UUID, Boolean] = APIRequestTranslator.loadingPlayers.asScala.toMap.map(x => (x._1, x._2.booleanValue()))

	def APIKeyIsValid: Boolean = validAPIKey
	def canMakeAPIRequest: Boolean = APIKeyIsValid && (curTimeSeconds - lastRequestLimitReached) > 300


	def getPlayerStats(uuid: UUID): Option[HypixelPlayerTime] = {
		val result: Option[HypixelPlayerTime] = playerCache.get(uuid)
		if ((result.isEmpty || curTimeSeconds > result.get.lastUpdated + 600) &&
		  canMakeAPIRequest && uuid.version() != 2){
			fetchPlayerStats(hyAPI, uuid)
		}
		result
	}

	def clearPlayerCache = {
		//		playerCache.clear()
		//		loadingPlayers.clear()
	}

	class HypixelPlayerTime(val lastUpdated: Long, val player: Player)
	def curTimeSeconds: Long = System.currentTimeMillis()/1000



	def fetchPlayerStats(api: HypixelAPI, uuid: UUID): Unit = {
		if(loadingPlayers.getOrElse(uuid, false)){
			return
		}
		logger.info("Fetching player: " + uuid)
		APIRequestTranslator.loadingPlayers.put(uuid, true)
		APIRequestTranslator.fetchPlayerStats(api, uuid)
	}

	def testAPIKey(api: HypixelAPI): Unit = {
		APIRequestTranslator.testAPIKey(api)
	}
}
