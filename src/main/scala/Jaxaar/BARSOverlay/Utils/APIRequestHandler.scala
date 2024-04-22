package Jaxaar.BARSOverlay.Utils

import Jaxaar.BARSOverlay.BarsOverlayMod.mc
import net.hypixel.api.HypixelAPI
import net.minecraft.util.ChatComponentTranslation

import java.util.UUID
import scala.concurrent.{Await, TimeoutException}
import scala.concurrent.duration.DurationInt
import scala.compat.java8.FutureConverters._
import scala.util.{Failure, Success}

object APIRequestHandler {

	private var validAPIKey = true;
	private var lastRequestLimitReached = 0L;


	def fetchPlayerStats(api: HypixelAPI, uuid: UUID) = {
		Await.ready(api.getPlayerByUuid(uuid).toScala, 5.minute).value match {
			case None => throw new TimeoutException("Hypixel API request timed out")
			case Some(i) => i match{
				case Failure(e) => onErr(e)
				case Success(value) => {
					System.out.println("LOADED: "  + uuid.toString);
//					newlyLoadedPLayers.put(uuid, reply.getPlayer());
					value
				}
			}
		}
	}

	def testAPIKey(api: HypixelAPI): Boolean = {
		Await.ready(api.getPunishmentStats().toScala, 5.minute).value match {
			case None => throw new TimeoutException("Hypixel API request timed out");
			case Some(i) => i match{
				case Failure(e) => onErr(e)
				case Success(value) => {
					System.out.println("APIKey Valid");
					validAPIKey = true;
					return true
				}
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
