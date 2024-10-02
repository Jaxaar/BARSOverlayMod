package Jaxaar.BARSOverlay.Commands

import Jaxaar.BARSOverlay.Utils.APIRequestHandler.fetchMojangPlayerStats
import Jaxaar.BARSOverlay.Utils.BARSConfig.setAPIKey
import net.minecraft.command.{CommandBase, ICommandSender}
import net.minecraft.event.ClickEvent
import net.minecraft.util.{ChatComponentTranslation, ChatStyle, EnumChatFormatting}

class BarsCommandBWStats extends CommandBase{
	override def getCommandName: String = "bwstats"

	override def getRequiredPermissionLevel = 0

	//	override def canCommandSenderUseCommand(sender: ICommandSender) = true

	override def getCommandUsage(sender: ICommandSender): String = "bars.commands.bwstats.usage"

	override def processCommand(sender: ICommandSender, args: Array[String]): Unit = {
		if(args.length <= 0){
			sender.addChatMessage(new ChatComponentTranslation("To load a specific player's stats use /bwstats *playername*"));
		}
		else if (args.length == 1){
			fetchMojangPlayerStats(args(0).toLowerCase(), sender)
			sender.addChatMessage(new ChatComponentTranslation(s"Loading ${args(0)}"));
		}
	}
}
