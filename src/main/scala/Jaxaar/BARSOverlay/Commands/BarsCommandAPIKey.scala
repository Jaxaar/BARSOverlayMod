package Jaxaar.BARSOverlay.Commands

import Jaxaar.BARSOverlay.Utils.BARSConfig.setAPIKey
import net.minecraft.command.{CommandBase, ICommandSender}
import net.minecraft.event.ClickEvent
import net.minecraft.util.{ChatComponentTranslation, ChatStyle, EnumChatFormatting}

class BarsCommandAPIKey extends CommandBase{
	override def getCommandName: String = "apikey"

	override def getRequiredPermissionLevel = 0

//	override def canCommandSenderUseCommand(sender: ICommandSender) = true

	override def getCommandUsage(sender: ICommandSender): String = "bars.commands.apikey.usage"

	override def processCommand(sender: ICommandSender, args: Array[String]): Unit = {
		if(args.length <= 0){
			val style = new ChatStyle()
			style.setColor(EnumChatFormatting.BLUE)
			style.setUnderlined(true)
			style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://developer.hypixel.net/dashboard/"))

			sender.addChatMessage(new ChatComponentTranslation("To Set your api-key: /apikey *HypixelApiKey*"));
			sender.addChatMessage(new ChatComponentTranslation("API Keys can be made at:"));
			sender.addChatMessage(new ChatComponentTranslation("https://developer.hypixel.net/dashboard").setChatStyle(style));
		}
		else if (args.length == 1){
			if(setAPIKey(args(0))) {
				sender.addChatMessage(new ChatComponentTranslation("API Key Saved Successfully"))
			}
		}

	}
}
