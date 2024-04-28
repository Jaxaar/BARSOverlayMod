package Jaxaar.BARSOverlay.Utils

import Jaxaar.BARSOverlay.BarsOverlayMod.mc
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraftforge.client.event.sound.SoundEvent

object SoundHandler {

	import net.minecraft.util.ResourceLocation

	val EEEEE = new ResourceLocation("bars_overlay_mod", "eeeee")
	val TBell = new ResourceLocation("bars_overlay_mod", "tbell")
	val ThatsTheGame = new ResourceLocation("bars_overlay_mod", "thatsthegame")


	def playESound(): Unit = mc.thePlayer.playSound(EEEEE.toString, 1.0F, 1.0F)
	def playTBellSound(): Unit = mc.thePlayer.playSound(TBell.toString, 1.0F, 1.0F)
	def playThatsTheGameSound(): Unit = mc.thePlayer.playSound(ThatsTheGame.toString, 1.0F, 1.0F)
}
