package com.group4.chipgame.Level;

import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.tiles.Tile;

import java.util.List;

public class LevelData {
    private final Tile[][] tiles;
    public final int gridWidth;
    public final int gridHeight;
    private int timer;
    public List<Actor> actors;
    public List<Collectible> collectibles;
    public LevelRenderer levelRenderer;
    private final String levelFilePath;

    public LevelData(Tile[][] tiles, int gridWidth, int gridHeight, List<Actor> actors, List<Collectible> collectibles, LevelRenderer levelRenderer, String levelFilePath, int timer) {
        this.tiles = tiles;
        this.actors = actors;
        this.collectibles = collectibles;
        this.levelRenderer = levelRenderer;
        this.levelFilePath = levelFilePath;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.timer = timer;
    }


    public int getTimer() {
        return timer;
    }


    public Tile[][] getTiles() {
        return tiles;
    }

    public void setLevelRenderer(LevelRenderer levelRenderer) {
        this.levelRenderer = levelRenderer;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<Collectible> getCollectibles() {
        return collectibles;
    }

    public void setCollectibles(List<Collectible> collectibles) {
        this.collectibles = collectibles;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public LevelRenderer getLevelRenderer() {
        return levelRenderer;
    }


    public void setTimer(int i) {
        this.timer = i;
    }

    public String getLevelPath() {
        return levelFilePath;
    }
}
