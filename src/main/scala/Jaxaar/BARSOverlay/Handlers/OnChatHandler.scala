package Jaxaar.BARSOverlay.Handlers

import Jaxaar.BARSOverlay.BarsOverlayMod.mc
import Jaxaar.BARSOverlay.CustomFunctionality.MovementInputFromMod
import Jaxaar.BARSOverlay.DataStructures.PlayerStatsDB.stats
import Jaxaar.BARSOverlay.OverlayManager.{getListOfPlayers, getUsersName, players}
import Jaxaar.BARSOverlay.Utils.APIRequestHandler.fetchPlayerStatsByName
import Jaxaar.BARSOverlay.Utils.BARSConfig.{EEEOn, WWWOn, fun, getGamesStarted, getLoadStatsFromChat, tbOn, ttgOn}
import Jaxaar.BARSOverlay.Utils.Helpers.stripColorCodes
import Jaxaar.BARSOverlay.Utils.ScoreboardSidebarReader.verifyIsBedwarsGame
import Jaxaar.BARSOverlay.Utils.SoundHandler.{playBellDing, playESound, playJustStop, playTBellSound, playThatsTheGameSound}
import net.minecraft.util.{ChatComponentTranslation, ChatStyle, EnumChatFormatting, IChatComponent, MovementInputFromOptions}

object OnChatHandler {

	var gameStarting = false
	var bedwarsGameStarted = false

	val gamesAgainstFlag = true
	val finaledByYouFlag = true
	val brokenYourBedFlag = true
	val gamesBeatenFlag = true
	val gamesWithFlag = true


	def resetGameProgress() = {
		gameStarting = false
		bedwarsGameStarted = false
	}

	def handleStatsOnChat(message: IChatComponent): Unit = {
//		println("-")
		val formattedText = message.getFormattedText
		val unformattedText: String = stripColorCodes(formattedText)
//		println(formattedText)
//		println(unformattedText)

		// Message from the server
		if(!unformattedText.contains(":")){
			if(unformattedText.contains("The game starts in 1 second!") && !gameStarting && !bedwarsGameStarted){
//				say("1s")
				gameStarting = true
			}
			if(unformattedText.contains("Bed Wars") && gameStarting && !bedwarsGameStarted){
				bedwarsGameStarted = true
				if(getGamesStarted){
//					say("Game's Started")
					playBellDing()
				}
				logPlayers
			}
			if(unformattedText.contains("FINAL KILL")){
//				say("FK")

				handleFinals(formattedText, unformattedText)
			}
			if(unformattedText.contains("BED DESTRUCTION") && unformattedText.contains("Your Bed")){
//				say("BD")
				handleBedsBroken(formattedText, unformattedText)
			}
		}
		// Message from a player
		else{
			if(!bedwarsGameStarted && verifyIsBedwarsGame){
				if(getLoadStatsFromChat){
					val name = (if(unformattedText.contains("] ")) (unformattedText.split("]").reverse)(0) else unformattedText).split(":")(0).replaceAll(" ", "")
					fetchPlayerStatsByName(name)
				}
			}
		}


	}

	def logPlayers: Unit = {
		val namesOfPlayers = players.map(_.getName.toLowerCase())
		namesOfPlayers.foreach((str) => {
			stats.getIndividualPlayersStats(str).incrementStat("gamesPlayed")
		})
	}

	def handleFinals(formattedText: String, unformattedText: String): Unit = {
		if(!finaledByYouFlag) {
			return
		}

		val died = unformattedText.split(' ')(0).toLowerCase();
		val killer = unformattedText.split(' ')(unformattedText.split(' ').length-3).replace(".", "").replace("?", "").replace("!", "").toLowerCase(); //Find a better replace method

		if(died == getUsersName.toLowerCase()){
			val opponent = stats.getIndividualPlayersStats(killer)
			playESound()
			opponent.incrementStat("FinaledYou")
		}
		else if(killer == getUsersName.toLowerCase()){
			val opponent = stats.getIndividualPlayersStats(killer)
			playTBellSound()
			opponent.incrementStat("YouFinaled")
		}
	}

	def handleBedsBroken(formattedText: String, unformattedText: String): Unit = {
		if(!brokenYourBedFlag) {
			return
		}
		val breaker = unformattedText.split(' ')(unformattedText.split(' ').length-1).replace(".", "").replace("?", "").replace("!", "").toLowerCase(); //Find a better replace method
		playThatsTheGameSound()
//		say(breaker + "-brokeYourBed")
		stats.getIndividualPlayersStats(breaker).incrementStat("brokeYourBed")
	}



	var wwwww = false

	def handleFunOnChat(message: IChatComponent): Unit = {
		if(!fun){
			return
		}
		val formattedText = message.getFormattedText
		val unformattedText = stripColorCodes(formattedText)
		//		println(formattedText)
		//		println(unformattedText)

		// Message from the server
		if(!unformattedText.contains(":")){
			if(unformattedText.contains("Slumber Tickets! (Win)") && ttgOn){
				playThatsTheGameSound()
			}
			if(unformattedText.contains("You have been eliminated!") && ttgOn){
				playThatsTheGameSound()
			}
			if(tbOn && unformattedText.contains("fell into the void") && (unformattedText.contains("Pypeapple") || unformattedText.contains("Protfire") || unformattedText.contains("Malizma") || unformattedText.contains("allandjust") || unformattedText.contains("x8vi") || (unformattedText.contains(getUsersName) && getUsersName != "Jaxaar"))){
				playTBellSound()
			}
			if(wwwww && unformattedText.contains(getUsersName)){
//				say("Oops")
				wwwww = false
				mc.thePlayer.movementInput =  new MovementInputFromOptions(mc.gameSettings)
			}
		}
		// Message from a player
		else{
			if(EEEOn && unformattedText.contains("E")){
				playESound()
			}
			if(WWWOn && unformattedText.contains("ok.") && unformattedText.contains("Jaxaar")) {
				mc.thePlayer.movementInput =  new MovementInputFromOptions(mc.gameSettings)
			}
			if(WWWOn && unformattedText.contains("WWWWW")) {
				wwwww = true
				mc.thePlayer.movementInput =  new MovementInputFromMod(1, 0, true, false)
			}
			if(WWWOn && unformattedText.contains("Just Stop")) {
				wwwww = true
				mc.thePlayer.movementInput =  new MovementInputFromMod(0, 0, false, false)
				playJustStop()
			}
		}
	}


	def say(str: String): Unit ={
		val style = new ChatStyle().setColor(EnumChatFormatting.RED)
		mc.thePlayer.addChatMessage(new ChatComponentTranslation(str).setChatStyle(style))
	}
}
