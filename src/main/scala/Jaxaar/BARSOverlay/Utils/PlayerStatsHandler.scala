package Jaxaar.BARSOverlay.Utils

import Jaxaar.BARSOverlay.BarsOverlayMod.statsFile
import Jaxaar.BARSOverlay.DataStructures.PlayerStatsDB.{loadStatsFile, stats}
import net.minecraft.util.IChatComponent

object PlayerStatsHandler {

	def handleStats(message: IChatComponent) = {

	}

	def loadStats = loadStatsFile(statsFile)

}
