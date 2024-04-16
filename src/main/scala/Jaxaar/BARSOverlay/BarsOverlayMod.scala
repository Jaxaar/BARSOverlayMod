package Jaxaar.BARSOverlay

import Jaxaar.BARSOverlay.listeners.HotkeyShortcuts
import net.hypixel.api.HypixelAPI
import net.hypixel.api.apache.ApacheHttpClient
import org.lwjgl.input.Keyboard
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraft.util.ChatComponentTranslation
import net.minecraftforge.client.GuiIngameForge
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.{Configuration, Property}
import net.minecraftforge.fml.common.{Mod, SidedProxy}
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import java.io.File
import java.util.UUID


@Mod(modid = BarsOverlayMod.MODID, version = BarsOverlayMod.VERSION, modLanguage = "scala")
object BarsOverlayMod {
    final val MODID = "BARS_Overlay_Mod"
    final val MOD_NAME = "BARS Overlay"
    final val VERSION = "0.1.0"
    final val mc = Minecraft.getMinecraft()

    var apiKey = "61c36e9a-a551-447a-aaaf-8b67a6011fdf"

    final var hyAPI = new HypixelAPI(new ApacheHttpClient(UUID.fromString(getApiKey)))

    final val modDir = new File(new File(mc.mcDataDir, "config"), MODID)

    final val config = new Configuration(new File(modDir, "Bars-Config.cfg"))
//    @SidedProxy(
//        clientSide = "com.BARSOverlay.ClientOnlyProxy",
//        serverSide = "com.BARSOverlay.CommonProxy"
//    )
//    // Forge will fill this in during mod loading.
//    var proxy: CommonProxy = null

    @EventHandler
    def preInit(event: FMLPreInitializationEvent) = {
//        proxy.preInit()
        loadConfig
    }


    @EventHandler
    def init(event: FMLInitializationEvent): Unit = {
//        proxy.init()
        MinecraftForge.EVENT_BUS.register(HotkeyShortcuts)
        MinecraftForge.EVENT_BUS.register(OverlayManager)

    }



    def loadConfig = {
        config.load()
        config.addCustomCategoryComment("requirements", "Values Required for the mod to function");
        apiKey = config.get("requirements", "api-key", "").getString

        println(apiKey)
        config.save()
    }

    def saveConfig = {
        val oldAPIKey: Property = config.get("requirements", "api-key", "")
        println("Saving apikey: " + apiKey)
        oldAPIKey.set(apiKey)
        config.save()
    }

    def setAPIKey(newKey: String) = {
        try{
            hyAPI = new HypixelAPI(new ApacheHttpClient(UUID.fromString(newKey)))
            apiKey = newKey
        } catch{
            case e: IllegalArgumentException => mc.thePlayer.addChatMessage(new ChatComponentTranslation("Invalid Api Key, please double check it"))
            case e: Throwable => mc.thePlayer.addChatMessage(new ChatComponentTranslation(e.getMessage))
        }

        saveConfig
    }

//    //Things to happen on both Server/Client
//    class CommonProxy {
//        def preInit(): Unit = {
//    //        BarsOverlayMod.registerBlocks()
//        }
//
//        def init(): Unit = {
//    //        println(s"DIRT BLOCK >> ${Blocks.dirt.getUnlocalizedName}")
//        }
//    }
//
//    @SideOnly(Side.CLIENT)
//    class ClientOnlyProxy extends CommonProxy {
//        override def preInit(): Unit = {
//            super.preInit()
//        }
//
//        override def init(): Unit = {
//            super.init()
//            MinecraftForge.EVENT_BUS.register(HotkeyShortcuts)
//            mc.ingameGUI = new GuiIngameForge(mc)
//        }
//
//    }

    def getShowOverlayKey = Keyboard.KEY_TAB

    def getApiKey: String = {
        apiKey
    }

}
