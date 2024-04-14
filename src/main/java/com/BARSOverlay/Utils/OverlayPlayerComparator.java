package com.BARSOverlay.Utils;

import com.google.common.collect.ComparisonChain;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Comparator;

@SideOnly(Side.CLIENT)
public class OverlayPlayerComparator implements Comparator<NetworkPlayerInfo>{
    private OverlayPlayerComparator()
    {
    }

    public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_)
    {
        ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
        ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
        return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
    }
}

