package Jaxaar.BARSOverlay.DataStructures

import Jaxaar.BARSOverlay.BarsOverlayMod.statsFile
import Jaxaar.BARSOverlay.OverlayManager.logger
import Jaxaar.BARSOverlay.Utils.Helpers.ListAsScala
import com.google.gson.stream.{JsonReader, JsonToken, JsonWriter}

import java.util.{Scanner, UUID}
import scala.collection.mutable
import com.google.gson.{Gson, GsonBuilder, JsonArray, JsonElement, JsonObject, TypeAdapter}
import net.hypixel.api.util.{UnstableHypixelObject, Utilities}

import java.io.{File, FileWriter, IOException}

object PlayerStatsDB {


	var stats: PlayersStats = null
	private val GSON: Gson = new GsonBuilder().registerTypeAdapter(classOf[PlayersStats], new PlayerStatsAdapter()).create()

	def loadStatsFile(file: File): PlayersStats = {
//		Check File's Existence
		if (file.exists() && file.canRead) logger.info("Stats File Found at: " + file.toString)

//		Load File
		val scanner = new Scanner(statsFile)
		var documentString = ""
		while (scanner.hasNext){
			documentString = documentString + scanner.nextLine()
		}
		scanner.close()
//		println(documentString)
//		println("----")

		stats = GSON.fromJson(documentString, classOf[PlayersStats])

//		println(stats.getRaw)
//		println(stats.getObjectProperty("jaxaar").toString)
//		println(stats.getObjectProperty("jaxaar").get("gamesPlayed"))
//		stats.getObjectProperty("jaxaar").addProperty("Testing", 1223)
//		println(stats.getProperty("jaxaar").toString)
		println(stats.getRaw)

		saveStatsFile(file)
		stats
	}

	def saveStatsFile(file: File): Boolean = {
//		Check File's Existence
		if (file.exists() && file.canWrite) logger.info("Stats File Found at: " + file.toString) else {logger.error("Unable to write PlayerStats to File"); return false}

		val outStr = stats.inRaw.toString

//		Write to File
		val writer = new FileWriter(statsFile)
		writer.write(outStr)
		writer.flush()
		writer.close()
		logger.info("Stats Saved Successfully")

		true
	}
}

//Uses UnstableHypixelObject because it is generic enough to for the base for any JSON Object
class PlayersStats(val inRaw: JsonElement = null) extends UnstableHypixelObject(inRaw) with HasProperties {

	def getIndividualPlayersStats(playerName: String): Option[PlayersStats] = {
		val player = new PlayersStats(getObjectProperty(playerName))
		if (player.inRaw != null){
			Some(player)
		}
		else{
			None
		}
	}

	def getDoubleRatio(topPath: String, botPath: String, topDefault: Double, botDefault: Double): Double = {getDoubleProperty(topPath, topDefault) / getDoubleProperty(botPath, botDefault)}
}


class PlayerStatsAdapter() extends TypeAdapter[PlayersStats]{
	val defaultAdapter: TypeAdapter[JsonElement] = new Gson().getAdapter(classOf[JsonElement])

	@throws[IOException]
	override def write(out: JsonWriter, value: PlayersStats): Unit = {
		defaultAdapter.write(out, value.getRaw)
	}

	@throws[IOException]
	override def read(in: JsonReader): PlayersStats = {
		val `type` = in.peek
		if (`type` eq JsonToken.NULL) {
			in.nextNull()
			return new PlayersStats()
		}
		new PlayersStats(defaultAdapter.read(in))
	}
}