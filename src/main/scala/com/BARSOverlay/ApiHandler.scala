package com.BARSOverlay

import com.BARSOverlay.Utils.HttpRequestHandler
import net.minecraft.util.HttpUtil
import org.apache.http.HttpHeaders
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.{BasicResponseHandler, DefaultHttpClient, HttpClientBuilder, HttpClients}

import java.io.{BufferedReader, InputStreamReader}
import java.net.{HttpURLConnection, URI, URL}
import scala.collection.JavaConverters.mapAsJavaMapConverter
import scala.collection.immutable.HashMap
import scala.util.parsing.json.JSON

object ApiHandler {

	val HY_API = "https://api.hypixel.net/v2"
	val HY_HEADER = { "api-key" -> "" }

	val goodkey = true


	def runTest = {
		println("GetRequest")
		println("Reply")
//
		getURL(new URL("https://api.hypixel.net/v2/punishmentstats"))
//		println(r.statusMessage)
//		println(r.data)
		println("done")

	}





	def getURL(url: URL) = {
		val response = HttpRequestHandler.get(url, Map(HY_HEADER).asJava)
		val responseJson = JSON.parseFull(response) match { case Some(i) => i case _ => Map()}
		println(responseJson)
	}
}
