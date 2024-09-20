package Jaxaar.BARSOverlay

import Jaxaar.BARSOverlay.Utils.BARSConfig.{getAPIKey, loadConfig}
import Jaxaar.BARSOverlay.Commands.{BarsCommandAPIKey, BarsCommandBWStats, BarsCommandShmeado}
import Jaxaar.BARSOverlay.DataStructures.PlayerStatsDB.loadStatsFile
import Jaxaar.BARSOverlay.OverlayManager.clearPlayers
import Jaxaar.BARSOverlay.Utils.APIRequestHandler.testAPIKey
import Jaxaar.BARSOverlay.Utils.APIRequestHandler
import Jaxaar.BARSOverlay.Handlers.HotkeyShortcuts
import Jaxaar.BARSOverlay.Handlers.HotkeyShortcuts.registerKeybinds
import com.mojang.api.profiles.minecraft.HttpProfileRepository
import net.hypixel.api.HypixelAPI
import net.hypixel.api.apache.ApacheHttpClient
import org.lwjgl.input.Keyboard
import net.minecraft.client.Minecraft
import net.minecraft.command.ICommand
import net.minecraft.init.Blocks
import net.minecraft.util.ChatComponentTranslation
import net.minecraftforge.client.{ClientCommandHandler, GuiIngameForge}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.{Configuration, Property}
import net.minecraftforge.fml.common.{Mod, SidedProxy}
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}

import java.io.File
import java.util.UUID


@Mod(modid = BarsOverlayMod.MODID, version = BarsOverlayMod.VERSION, modLanguage = "scala", guiFactory = "Jaxaar.BARSOverlay.Utils.BARSGuiFactory")
object BarsOverlayMod {
    final val MODID = "bars_overlay_mod"
    final val MOD_NAME = "BARS Overlay"
    final val VERSION = "0.3.0"
    final val mc = Minecraft.getMinecraft

    final var hyAPI =  new HypixelAPI(new ApacheHttpClient(getAPIKey))
    final var httpProfiler = new HttpProfileRepository()

    final val modDir = new File(new File(mc.mcDataDir, "config"), MODID)
    val config = new Configuration(new File(modDir, "Bars-Config.cfg"), "conf.v0.1.0")

    val statsFileName = "playerRecord.json"
//    val statsFileName = "playerRecordTEST.json"
    val statsFile = new File(System.getProperty("user.home") + "/AppData/Roaming/bars_overlay/" + statsFileName)

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
        loadStatsFile(statsFile)
//        println("---hi")
//        println(config.getConfigFile)
//        println(getAPIKey.toString)
    }


    @EventHandler
    def init(event: FMLInitializationEvent): Unit = {
//        proxy.init()
        MinecraftForge.EVENT_BUS.register(HotkeyShortcuts)
        MinecraftForge.EVENT_BUS.register(OverlayManager)
        registerCommands
        registerKeybinds
    }

    def registerCommands: ICommand = {
        ClientCommandHandler.instance.registerCommand(new BarsCommandAPIKey());
        ClientCommandHandler.instance.registerCommand(new BarsCommandShmeado());
        ClientCommandHandler.instance.registerCommand(new BarsCommandBWStats());
    }


    def reloadHypixelAPIHandler(): Unit = {
        hyAPI = new HypixelAPI(new ApacheHttpClient(getAPIKey))
        testAPIKey(hyAPI)
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
}
