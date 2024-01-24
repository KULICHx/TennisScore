package com.kulichx.dao;

import com.kulichx.entity.Matches;


public class MatchesDao{
    public static boolean isFinished(Matches match){
        if (match.getMatchScore().getPlayer1Score() >= 3 || match.getMatchScore().getPlayer2Score() >= 3) {
            return true;
        }
        return false;
    }

}
