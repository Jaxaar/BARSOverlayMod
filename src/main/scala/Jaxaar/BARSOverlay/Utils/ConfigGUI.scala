package Jaxaar.BARSOverlay.Utils

import Jaxaar.BARSOverlay.BarsOverlayMod._
import Jaxaar.BARSOverlay.OverlayManager.clearPlayers
import Jaxaar.BARSOverlay.Utils.APIRequestHandler.testAPIKey
import Jaxaar.BARSOverlay.Utils.BARSConfig.Categories
import Jaxaar.BARSOverlay.Utils.Helpers.{ListAsJava, ListAsScala}
import net.hypixel.api.HypixelAPI
import net.hypixel.api.apache.ApacheHttpClient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ChatComponentTranslation
import net.minecraftforge.common.config.{ConfigElement, Property}
import net.minecraftforge.fml.client.IModGuiFactory
import net.minecraftforge.fml.client.config.{GuiConfig, IConfigElement}
import org.apache.logging.log4j.{LogManager, Logger}

import java.util
import java.util.UUID



object BARSConfig {

	val logger: Logger = LogManager.getLogger(MODID);
	private var apiKey = "00000000-0000-0000-0000-000000000000"

	object Categories extends Enumeration {
		type main = Value
		val Requirements: Categories.Value =  Value(0, "requirements")
		val shenanigans: Categories.Value =  Value(10, "shenanigans")
		val dev_configs: Categories.Value =  Value(20, "dev_configs")

		val GuiCustomization: Categories.Value =  Value(-1, "gui_customization")

		def REQUIREMENTS: String = Requirements.toString
		def GUI_CUSTOMIZATION: String = GuiCustomization.toString

		def SHENANIGANS: String = shenanigans.toString
		def DEV_CONFIGS: String = dev_configs.toString


	}

	def loadConfig = {
		logger.info("Loading configs...")
		config.load()
		config.addCustomCategoryComment(Categories.REQUIREMENTS, "Values Required for the mod to function");
		config.get(Categories.REQUIREMENTS, "api-key", "00000000-0000-0000-0000-000000000000").getString

		config.addCustomCategoryComment(Categories.GUI_CUSTOMIZATION, "Values to set the format and location of the UI");
		config.get(Categories.GUI_CUSTOMIZATION, "gui_scale", 1)
		config.get(Categories.GUI_CUSTOMIZATION, "Top-Left X Pos", 2)
		config.get(Categories.GUI_CUSTOMIZATION, "Top-Left Y Pos", 2)

		config.addCustomCategoryComment(Categories.SHENANIGANS, ":)");
		config.get(Categories.SHENANIGANS, "hi! :)", 0)

		config.addCustomCategoryComment(Categories.DEV_CONFIGS, "Configs specifically for development, Don't Change unless you know what they do... probably breaks stuff");
		config.get(Categories.DEV_CONFIGS, "bypass_bars_game_requirements", false)
		config.get(Categories.DEV_CONFIGS, "load_from_first_player", false)

		config.save()

		reloadHypixelAPIHandler()
	}

	def getGui_scale: Double = config.get(Categories.GUI_CUSTOMIZATION, "gui_scale", 1).getDouble
	def getXPos: Int = config.get(Categories.GUI_CUSTOMIZATION, "Top-Left X Pos", 2).getInt
	def getYPos: Int = config.get(Categories.GUI_CUSTOMIZATION, "Top-Left Y Pos", 2).getInt

	def getAPIKey: UUID = {
		try {
			UUID.fromString(config.get(Categories.REQUIREMENTS, "api-key", "00000000-0000-0000-0000-000000000000").getString)
		}
		catch {
			case e: Throwable => UUID.fromString("00000000-0000-0000-0000-000000000000")
		}
	}

	def getBypassInGameRequirement: Boolean =  config.get(Categories.DEV_CONFIGS, "bypass_bars_game_requirements", false).getBoolean
	def getLoadFromFirstPlayer: Boolean = config.get(Categories.DEV_CONFIGS, "load_from_first_player", false).getBoolean



	def saveConfig = {
		val oldAPIKeyConf = config.get(Categories.REQUIREMENTS, "api-key", "00000000-0000-0000-0000-000000000000")
		logger.info("Saving apikey")
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
		ListAsJava(Categories.values.toList.filter(_.id > 0).flatMap(c => ListAsScala(new ConfigElement(config.getCategory(c.toString)).getChildElements)))
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
	override def onGuiClosed(): Unit = {
		super.onGuiClosed()
		config.save()
	}
}

class BARSGuiFactory extends IModGuiFactory {

	def initialize(minecraft: Minecraft): Unit = {}

	def mainConfigGuiClass: Class[_ <: GuiScreen] = classOf[BARSConfigGUI]

	def runtimeGuiCategories: Null = null

	def getHandlerFor(runtimeOptionCategoryElement: IModGuiFactory.RuntimeOptionCategoryElement): IModGuiFactory.RuntimeOptionGuiHandler = null
}
