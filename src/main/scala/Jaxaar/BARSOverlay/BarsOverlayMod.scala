package Jaxaar.BARSOverlay

import Jaxaar.BARSOverlay.listeners.HotkeyShortcuts
import net.hypixel.api.HypixelAPI
import net.hypixel.api.apache.ApacheHttpClient
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraftforge.client.GuiIngameForge
import net.minecraftforge.common.MinecraftForge
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

    val apiKey = "373313d3-6c45-4bd1-a72e-8527c6123b71"

    final val hyAPI = new HypixelAPI(new ApacheHttpClient(UUID.fromString(getApiKey)))

    final val modDir = new File(new File(mc.mcDataDir, "config"), MODID)


//    @SidedProxy(
//        clientSide = "com.BARSOverlay.ClientOnlyProxy",
//        serverSide = "com.BARSOverlay.CommonProxy"
//    )
//    // Forge will fill this in during mod loading.
//    var proxy: CommonProxy = null

    @EventHandler
    def preInit(event: FMLPreInitializationEvent) = {
//        proxy.preInit()
    }


    @EventHandler
    def init(event: FMLInitializationEvent): Unit = {
//        proxy.init()
        MinecraftForge.EVENT_BUS.register(HotkeyShortcuts)
        MinecraftForge.EVENT_BUS.register(OverlayManager)

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


    def getApiKey: String = {
        apiKey
    }

}
