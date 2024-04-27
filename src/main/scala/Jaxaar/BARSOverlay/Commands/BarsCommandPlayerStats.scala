package Jaxaar.BARSOverlay.Commands

import Jaxaar.BARSOverlay.Utils.BARSConfig.setAPIKey
import net.minecraft.command.{CommandBase, ICommandSender}
import net.minecraft.event.ClickEvent
import net.minecraft.util.{ChatComponentTranslation, ChatStyle, EnumChatFormatting}

class BarsCommandPlayerStats extends CommandBase{
	override def getCommandName: String = "getplayerstats"

	override def getRequiredPermissionLevel = 0

	//	override def canCommandSenderUseCommand(sender: ICommandSender) = true

	override def getCommandUsage(sender: ICommandSender): String = "bars.commands.getplayerstats.usage"

	override def processCommand(sender: ICommandSender, args: Array[String]): Unit = {
		if(args.length <= 0){
			sender.addChatMessage(new ChatComponentTranslation("Invalid Command syntax, please use ./getplayerstats *playername*"));
		}
		else if (args.length == 1){
			val style = new ChatStyle()
			style.setColor(EnumChatFormatting.BLUE)
			style.setUnderlined(true)
			style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, s"https://www.shmeado.club/player/stats/${args(0)}/bedwars/table/"))

			sender.addChatMessage(new ChatComponentTranslation(s"NOT Loading Stats for ${args(0)} - Coming Sometime"));
			sender.addChatMessage(new ChatComponentTranslation(s"See ${args(0)} on Shmeado").setChatStyle(style));
		}
	}
}
