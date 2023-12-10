package com.group4.chipgame.Level;

import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.tiles.Tile;

import java.util.List;

/**
 * Represents the data for a single level in the ChipGame.
 * This class contains all the necessary information for a level, including tiles, actors, collectibles, and the renderer.
 */
public class LevelData {
    private final Tile[][] tiles;
    public final int gridWidth;
    public final int gridHeight;
    private int timer;
    public List<Actor> actors;
    public List<Collectible> collectibles;
    public LevelRenderer levelRenderer;
    private final String levelFilePath;

    /**
     * Constructs a new LevelData object with the specified parameters.
     *
     * @param tiles         The array of tiles for the level.
     * @param gridWidth     The width of the grid.
     * @param gridHeight    The height of the grid.
     * @param actors        The list of actors in the level.
     * @param collectibles  The list of collectibles in the level.
     * @param levelRenderer The renderer for the level.
     * @param levelFilePath The file path to the level data.
     * @param timer         The timer for the level.
     */
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

    /**
     * Gets the current timer value for the level.
     *
     * @return The current timer value.
     */
    public int getTimer() {
        return timer;
    }

    /**
     * Gets the array of tiles for the level.
     *
     * @return The array of tiles.
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Sets the level renderer.
     *
     * @param levelRenderer The renderer to be set for the level.
     */
    public void setLevelRenderer(LevelRenderer levelRenderer) {
        this.levelRenderer = levelRenderer;
    }

    /**
     * Gets the list of actors in the level.
     *
     * @return The list of actors.
     */
    public List<Actor> getActors() {
        return actors;
    }

    /**
     * Gets the list of collectibles in the level.
     *
     * @return The list of collectibles.
     */
    public List<Collectible> getCollectibles() {
        return collectibles;
    }

    /**
     * Sets the list of collectibles for the level.
     *
     * @param collectibles The list of collectibles to be set.
     */
    public void setCollectibles(List<Collectible> collectibles) {
        this.collectibles = collectibles;
    }

    /**
     * Sets the list of actors for the level.
     *
     * @param actors The list of actors to be set.
     */
    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    /**
     * Gets the level renderer.
     *
     * @return The level renderer.
     */
    public LevelRenderer getLevelRenderer() {
        return levelRenderer;
    }

    /**
     * Sets the timer for the level.
     *
     * @param i The new timer value.
     */
    public void setTimer(int i) {
        this.timer = i;
    }

    /**
     * Gets the file path of the level data.
     *
     * @return The file path of the level.
     */
    public String getLevelPath() {
        return levelFilePath;
    }
}
