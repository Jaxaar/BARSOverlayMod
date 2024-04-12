package com.BARSOverlay

import com.BARSOverlay.Utils.HttpRequestHandler
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.HttpUtil
import org.apache.http.HttpHeaders
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.{BasicResponseHandler, DefaultHttpClient, HttpClientBuilder, HttpClients}

import java.io.{BufferedReader, InputStreamReader}
import java.net.{HttpURLConnection, URI, URL}
import java.util.UUID
import java.util.concurrent.{Executors, Future, FutureTask}
import scala.collection.JavaConverters.mapAsJavaMapConverter
import scala.collection.immutable.HashMap
import scala.util.parsing.json.JSON

object ApiHandler {

	val HY_API = "https://api.hypixel.net/v2"
	val HY_HEADER = Map( "api-key" -> "373313d3-6c45-4bd1-a72e-8527c6123b71" )
//	val HY_HEADER = Map("api-key" -> "a" )


	val goodkey = true


	def runTest = {
		println("GetRequest")
		println("Reply")
//
		getURL(new URL("https://api.hypixel.net/v2/punishmentstats"))

		println("done")

	}



	def getHypixelPlayerData(uuid: UUID) = {
		val future: Future[String] = getURL(new URL(s"https://api.hypixel.net/v2/player?uuid=${uuid}"), HY_HEADER)
		future

//		println(future.toString)
//		println(future.get())
//		println(future)
//		val str = future.get()
//		val j = getJSON(str)
//		str
	}

	def getURL(url: URL, header: Map[String, String] = Map()) = {
//		try{
			val response: Future[String] = HttpRequestHandler.asyncGet(url, header.asJava)
			println("Success - Response:")
			println(response)
			response
//		}
//		catch{
//			case e => {
//				println(e)
//				if (e.getMessage().contains(s"Server returned HTTP response code: 403")){
//					println("YES")
//				}
//			}
//			case _ => println("Huh")
//		}

	}

	def getJSON(response: String) = {
		val json = JSON.parseFull(response) match { case Some(i) => i case _ => Map()}
		println("Json")
		println(json)
		json
	}
}
