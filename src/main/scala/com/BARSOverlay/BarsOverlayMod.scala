package com.BARSOverlay

import com.BARSOverlay.listeners.HotkeyShortcuts
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent

import java.io.File


@Mod(modid = BarsOverlayMod.MODID, version = BarsOverlayMod.VERSION, modLanguage = "scala")
object BarsOverlayMod {
    final val MODID = "BARS_Overlay_Mod"
    final val MOD_NAME = "BARS Overlay"
    final val VERSION = "0.1.0"
    final val mc = Minecraft.getMinecraft()

    final val modDir = new File(new File(mc.mcDataDir, "config"), MODID)

    @EventHandler
    def preInit(event: FMLInitializationEvent) = {

    }


    @EventHandler
    def init(event: FMLInitializationEvent): Unit = {
        println(s"DIRT BLOCK >> ${Blocks.dirt.getUnlocalizedName}")
        MinecraftForge.EVENT_BUS.register(HotkeyShortcuts)
        HotkeyShortcuts
    }
}