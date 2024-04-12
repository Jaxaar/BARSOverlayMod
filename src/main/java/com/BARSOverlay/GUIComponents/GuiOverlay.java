package com.BARSOverlay.GUIComponents;

import com.google.common.collect.ComparisonChain;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class GuiOverlay extends Gui {
    Minecraft mc;
    List<NetworkPlayerInfo> players = new ArrayList<>();

    public GuiOverlay(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public void setPlayers(List<NetworkPlayerInfo> lst){
        players = lst;
    }

    public void logPlayers(){
        System.out.println(players.toString());
        for (NetworkPlayerInfo i: players){
            if(i.getDisplayName() != null)
                mc.thePlayer.addChatMessage(i.getDisplayName());
            else
                mc.thePlayer.addChatMessage(new ChatComponentTranslation(i.getGameProfile().getName()));
        }
    }


    public void renderPlayerlist() {
//        logPlayers();
        if(players.size() <= 0){
            return;
        }

        drawRect(2,  2, 200, 240, Integer.MIN_VALUE);
        for(int i = 0; i < players.size(); i++){
            NetworkPlayerInfo player = players.get(i);
//            System.out.println(player);
            if(player.getDisplayName() != null)
                this.mc.fontRendererObj.drawStringWithShadow(players.get(i).getDisplayName().getFormattedText(), (float)10, (float)10 + i*15, -1);
            else
                this.mc.fontRendererObj.drawStringWithShadow(players.get(i).getGameProfile().getName(), (float)10, (float)10 + i*15, -1);

        }


//        NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
//        List<NetworkPlayerInfo> list = field_175252_a.<NetworkPlayerInfo>sortedCopy(nethandlerplayclient.getPlayerInfoMap());
//        int i = 0;
//        int j = 0;

//        for (NetworkPlayerInfo networkplayerinfo : list)
//        {
//            int k = this.mc.fontRendererObj.getStringWidth(this.getPlayerName(networkplayerinfo));
//            i = Math.max(i, k);
//
//            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS)
//            {
//                k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
//                j = Math.max(j, k);
//            }
//        }
//
//        list = list.subList(0, Math.min(list.size(), 80));
//        int l3 = list.size();
//        int i4 = l3;
//        int j4;
//
//        for (j4 = 1; i4 > 20; i4 = (l3 + j4 - 1) / j4)
//        {
//            ++j4;
//        }
//
//        int l;
//
//        int i1 = Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4;
//        int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
//        int k1 = 10;
//        int l1 = i1 * j4 + (j4 - 1) * 5;
//
//        if (list1 != null)
//        {
//            drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list1.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
//
//            for (String s3 : list1)
//            {
//                int i2 = this.mc.fontRendererObj.getStringWidth(s3);
//                this.mc.fontRendererObj.drawStringWithShadow(s3, (float)(width / 2 - i2 / 2), (float)k1, -1);
//                k1 += this.mc.fontRendererObj.FONT_HEIGHT;
//            }
//
//            ++k1;
//        }

//        System.out.println("Render");

//        drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, Integer.MIN_VALUE);

//        for (int k4 = 0; k4 < l3; ++k4)
//        {
//            int l4 = k4 / i4;
//            int i5 = k4 % i4;
//            int j2 = j1 + l4 * i1 + l4 * 5;
//            int k2 = k1 + i5 * 9;
//            drawRect(j2, k2, j2 + i1, k2 + 8, 553648127);
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//            GlStateManager.enableAlpha();
//            GlStateManager.enableBlend();
//            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//
//            if (k4 < list.size())
//            {
//                NetworkPlayerInfo networkplayerinfo1 = (NetworkPlayerInfo)list.get(k4);
//                String s1 = this.getPlayerName(networkplayerinfo1);
//                GameProfile gameprofile = networkplayerinfo1.getGameProfile();
//
//                if (flag)
//                {
//                    EntityPlayer entityplayer = this.mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
//                    boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm"));
//                    this.mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
//                    int l2 = 8 + (flag1 ? 8 : 0);
//                    int i3 = 8 * (flag1 ? -1 : 1);
//                    Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, (float)l2, 8, i3, 8, 8, 64.0F, 64.0F);
//
//                    if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT))
//                    {
//                        int j3 = 8 + (flag1 ? 8 : 0);
//                        int k3 = 8 * (flag1 ? -1 : 1);
//                        Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, (float)j3, 8, k3, 8, 8, 64.0F, 64.0F);
//                    }
//
//                    j2 += 9;
//                }
//
//                if (networkplayerinfo1.getGameType() == WorldSettings.GameType.SPECTATOR)
//                {
//                    s1 = EnumChatFormatting.ITALIC + s1;
//                    this.mc.fontRendererObj.drawStringWithShadow(s1, (float)j2, (float)k2, -1862270977);
//                }
//                else
//                {
//                    this.mc.fontRendererObj.drawStringWithShadow(s1, (float)j2, (float)k2, -1);
//                }
//
//                if (scoreObjectiveIn != null && networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR)
//                {
//                    int k5 = j2 + i + 1;
//                    int l5 = k5 + l;
//
//                    if (l5 - k5 > 5)
//                    {
//                        this.drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
//                    }
//                }
//
//                this.drawPing(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
//            }
//        }
//
//        if (list2 != null)
//        {
//            k1 = k1 + i4 * 9 + 1;
//            drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list2.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
//
//            for (String s4 : list2)
//            {
//                int j5 = this.mc.fontRendererObj.getStringWidth(s4);
//                this.mc.fontRendererObj.drawStringWithShadow(s4, (float)(width / 2 - j5 / 2), (float)k1, -1);
//                k1 += this.mc.fontRendererObj.FONT_HEIGHT;
//            }
//        }
    }


}
