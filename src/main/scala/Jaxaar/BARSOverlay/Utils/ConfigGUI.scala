package Jaxaar.BARSOverlay.Utils

import Jaxaar.BARSOverlay.BarsOverlayMod._
import Jaxaar.BARSOverlay.OverlayManager.clearPlayers
import Jaxaar.BARSOverlay.Utils.APIRequestHandler.testAPIKey
import Jaxaar.BARSOverlay.Utils.BARSConfig.Categories
import net.hypixel.api.HypixelAPI
import net.hypixel.api.apache.ApacheHttpClient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ChatComponentTranslation
import net.minecraftforge.common.config.{ConfigElement, Configuration}
import net.minecraftforge.fml.client.IModGuiFactory
import net.minecraftforge.fml.client.config.{GuiConfig, IConfigElement}

import java.io.File
import java.util
import java.util.UUID
import scala.collection.JavaConverters.{collectionAsScalaIterableConverter, seqAsJavaListConverter}



object BARSConfig {

	private var apiKey = "00000000-0000-0000-0000-000000000000"

	object Categories extends Enumeration {
		type main = Value
		val Requirements: Categories.Value =  Value(2, "requirements")
		val GuiCustomization: Categories.Value =  Value(1, "gui_customization")

		def REQUIREMENTS: String = Requirements.toString
		def GUI_CUSTOMIZATION: String = GuiCustomization.toString
	}

	def loadConfig = {
		println("loading")
		config.load()
		config.addCustomCategoryComment(Categories.REQUIREMENTS, "Values Required for the mod to function");
		println(config.get(Categories.REQUIREMENTS, "api-key", "00000000-0000-0000-0000-000000000000").toString)

		config.addCustomCategoryComment(Categories.GUI_CUSTOMIZATION, "Values to set the format and location of the UI");
		config.get(Categories.GUI_CUSTOMIZATION, "gui_scale", 0.7)
		config.get(Categories.GUI_CUSTOMIZATION, "Top-Left X Pos", 2)
		config.get(Categories.GUI_CUSTOMIZATION, "Top-Left Y Pos", 2)
		config.save()

		reloadHypixelAPIHandler()
	}

	def getGui_scale: Double = config.get(Categories.GUI_CUSTOMIZATION, "gui_scale", 0.7).getDouble
	def getXPos: Int = config.get(Categories.GUI_CUSTOMIZATION, "Top-Left X Pos", 2).getInt
	def getYPos: Int = config.get(Categories.GUI_CUSTOMIZATION, "Top-Left Y Pos", 2).getInt




	def saveConfig = {
		val oldAPIKeyConf = config.get(Categories.REQUIREMENTS, "api-key", "00000000-0000-0000-0000-000000000000")
		println("Saving apikey")
		oldAPIKeyConf.set(apiKey)

		config.save()
	}

	def setAPIKey(newKey: String): Boolean = {
		try{
			hyAPI = new HypixelAPI(new ApacheHttpClient(UUID.fromString(newKey)))
			testAPIKey(hyAPI)
			clearPlayers()
			apiKey = newKey
			saveConfig
			true
		} catch{
			case e: IllegalArgumentException => mc.thePlayer.addChatMessage(new ChatComponentTranslation("Invalid Api Key, please double check it")); false
			case e: Throwable => mc.thePlayer.addChatMessage(new ChatComponentTranslation(e.getMessage)); false
		}
	}

	def getConfigUIElements: util.List[IConfigElement] = {
		println(Categories.values.toList.filter(_.id > 0).flatMap(c => new ConfigElement(config.getCategory(c.toString)).getChildElements.asScala.toList))
		println("-")
		println(Categories.values.toList.filter(_.id > 0).foreach(c => {println(c.toString); println(new ConfigElement(config.getCategory(c.toString)).getChildElements)}))
		val res = Categories.values.toList.filter(_.id > 0).flatMap(c => new ConfigElement(config.getCategory(c.toString)).getChildElements.asScala.toList).asJava
//		val res = new ConfigElement(config.getCategory("requirements")).getChildElements
		println("-----HIIII____")
		println(res)
		res
	}


	def getAPIKey: UUID = {
		try {
			UUID.fromString(config.get(Categories.REQUIREMENTS, "api-key", "00000000-0000-0000-0000-000000000000").toString)
		}
		catch {
			case e => UUID.fromString("00000000-0000-0000-0000-000000000000")
		}
	}
}


class BARSConfigGUI(parent: GuiScreen) extends GuiConfig(
	parent,
	BARSConfig.getConfigUIElements,
	MODID,
	false,
	false,
	MOD_NAME + " Configurations"
){
}

class BARSGuiFactory extends IModGuiFactory {

	def initialize(minecraft: Minecraft): Unit = {}

	def mainConfigGuiClass: Class[_ <: GuiScreen] = classOf[BARSConfigGUI]

	def runtimeGuiCategories: Null = null

	def getHandlerFor(runtimeOptionCategoryElement: IModGuiFactory.RuntimeOptionCategoryElement): IModGuiFactory.RuntimeOptionGuiHandler = null
}
