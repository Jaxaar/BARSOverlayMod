package Jaxaar.BARSOverlay.Commands

import Jaxaar.BARSOverlay.Utils.BARSConfig.setAPIKey
import net.minecraft.command.{CommandBase, ICommandSender}
import net.minecraft.event.ClickEvent
import net.minecraft.util.{ChatComponentTranslation, ChatStyle, EnumChatFormatting}

class BarsCommandShmeado extends CommandBase{
	override def getCommandName: String = "shmeado"

	override def getRequiredPermissionLevel = 0

	//	override def canCommandSenderUseCommand(sender: ICommandSender) = true

	override def getCommandUsage(sender: ICommandSender): String = "bars.commands.shmeado.usage"

	override def processCommand(sender: ICommandSender, args: Array[String]): Unit = {
		if(args.length <= 0){
			sender.addChatMessage(new ChatComponentTranslation("To see a specific player's stats use /shmeado *playername*"));
			val style = new ChatStyle()
			style.setColor(EnumChatFormatting.BLUE)
			style.setUnderlined(true)
			style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, s"https://www.shmeado.club/"))
			sender.addChatMessage(new ChatComponentTranslation(s"Shmeado").setChatStyle(style));
		}
		else if (args.length == 1){
			val style = new ChatStyle()
			style.setColor(EnumChatFormatting.BLUE)
			style.setUnderlined(true)
			style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, s"https://www.shmeado.club/player/stats/${args(0)}/bedwars/table/"))

			sender.addChatMessage(new ChatComponentTranslation(s"See ${args(0)} on Shmeado").setChatStyle(style));
		}
	}
}
