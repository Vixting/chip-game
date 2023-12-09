package com.group4.chipgame.menu;

public class HighscoreEntry {
    private String playerName;
    private int score;

    public HighscoreEntry (String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
}