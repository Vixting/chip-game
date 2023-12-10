package com.group4.chipgame.Level;

import com.group4.chipgame.Main;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.tiles.ChipSocket;
import com.group4.chipgame.entities.actors.tiles.Tile;
import com.group4.chipgame.entities.actors.Actor;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import java.util.*;

/**
 * Renderer for the level in the ChipGame.
 */
public class LevelRenderer {
    private static final String FONT_ARIAL = "Arial";
    private static final int CHIP_COUNT_FONT_SIZE = 14;

    private LevelData currentLevelData;
    private final Pane gamePane;
    private final Pane tilesPane;
    private final Pane actorsPane;
    private final Pane collectiblesPane;
    private final Map<Point2D, Label> chipSocketLabels = new HashMap<>();
    private Tile[][] tiles;

    /**
     * Constructor for LevelRenderer.
     *
     * @param currentLevelData The current level data to be rendered.
     */
    public LevelRenderer(final LevelData currentLevelData) {
        this.currentLevelData = currentLevelData;
        tilesPane = new Pane();
        actorsPane = new Pane();
        collectiblesPane = new Pane();
        gamePane = new Pane(tilesPane, actorsPane, collectiblesPane);
        Main.TILE_SIZE.addListener((obs, oldVal, newVal) -> updateSizes());
        Main.ACTOR_SIZE.addListener((obs, oldVal, newVal) -> updateSizes());
    }

    /**
     * Sets the current level data.
     *
     * @param levelData The new level data.
     */
    public void setCurrentLevelData(final LevelData levelData) {
        this.currentLevelData = levelData;
    }

    /**
     * Updates tile occupation based on actors' positions.
     */
    public void updateTileOccupation() {
        for (final Actor actor : currentLevelData.getActors()) {
            Point2D actorPosition = actor.getPosition();
            int x = (int) actorPosition.getX();
            int y = (int) actorPosition.getY();
            if (isValidCoordinate(x, y)) {
                System.out.println("Actor at: " + x + ", " + y);
                tiles[y][x].setOccupiedBy(actor);
            }
        }
        for (final Collectible collectible : currentLevelData.getCollectibles()) {
            Point2D collectiblePosition = collectible.getPosition();
            int x = (int) collectiblePosition.getX();
            int y = (int) collectiblePosition.getY();
            if (isValidCoordinate(x, y)) {
                System.out.println("Collectible at: " + x + ", " + y);
                tiles[y][x].setOccupiedBy(collectible);
            }
        }
    }

    /**
     * Checks if the given coordinates are valid within the tile grid.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return true if coordinates are valid, false otherwise.
     */
    private boolean isValidCoordinate(final int x, final int y) {
        return x >= 0 && y >= 0 && y < tiles.length && x < tiles[y].length;
    }

    /**
     * Updates the sizes of tiles, actors, and collectibles.
     */
    public void updateSizes() {
        if (tiles != null) {
            renderTiles(tiles);
        }
        renderNodes(actorsPane, getActors());
        renderNodes(collectiblesPane, getCollectibles());
        if (tiles != null && tiles.length > 0 && tiles[0].length > 0) {
            gamePane.setLayoutX(Main.TILE_SIZE.get() * tiles[0].length);
            gamePane.setLayoutY(Main.TILE_SIZE.get() * tiles.length);
        }
    }

    /**
     * Gets a list of all collectibles in the level.
     *
     * @return List of collectibles.
     */
    public List<Collectible> getCollectibles() {
        return castToList(collectiblesPane, Collectible.class);
    }

    /**
     * Gets a list of all actors in the level.
     *
     * @return List of actors.
     */
    public List<Actor> getActors() {
        return castToList(actorsPane, Actor.class);
    }

    /**
     * Renders the given collectibles.
     *
     * @param collectibles The list of collectibles to render.
     */
    public void renderCollectibles(final List<Collectible> collectibles) {
        renderNodes(collectiblesPane, collectibles);
    }

    /**
     * Renders the given actors.
     *
     * @param actors The list of actors to render.
     */
    public void renderActors(final List<Actor> actors) {
        renderNodes(actorsPane, actors);
    }

    /**
     * Renders the given tiles.
     *
     * @param tiles The array of tiles to render.
     */
    public void renderTiles(final Tile[][] tiles) {
        this.tiles = tiles;
        tilesPane.getChildren().clear();
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                if (tile != null) {
                    renderTile(tile, x, y);
                    if (tile instanceof ChipSocket) {
                        renderChipSocketLabel((ChipSocket) tile);
                    }
                }
            }
        }
    }

    /**
     * Renders a label for a ChipSocket.
     *
     * @param chipSocket The ChipSocket to render a label for.
     */
    private void renderChipSocketLabel(final ChipSocket chipSocket) {
        Label chipCountLabel = new Label(String.valueOf(chipSocket.getRequiredChips()));
        chipCountLabel.setFont(new Font(FONT_ARIAL, CHIP_COUNT_FONT_SIZE));
        chipCountLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        Point2D position = new Point2D(chipSocket.getGridX(), chipSocket.getGridY());
        chipSocketLabels.put(position, chipCountLabel);
        double labelX = chipSocket.getLayoutX()
                + (Main.TILE_SIZE.get() - chipCountLabel.getWidth()) / 2.0;
        double labelY = chipSocket.getLayoutY()
                + (Main.TILE_SIZE.get() - chipCountLabel.getHeight()) / 2.0;
        chipCountLabel.setLayoutX(labelX);
        chipCountLabel.setLayoutY(labelY);
        tilesPane.getChildren().add(chipCountLabel);
    }

    /**
     * Gets the game pane.
     *
     * @return The game pane.
     */
    public Pane getGamePane() {
        return gamePane;
    }

    /**
     * Gets the current tile grid.
     *
     * @return The tile grid.
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Removes the specified actor from the level.
     *
     * @param actor The actor to remove.
     */
    public void remove(final Actor actor) {
        this.currentLevelData.getActors().remove(actor);
        modifyPaneLater(() -> actorsPane.getChildren().remove(actor));
    }

    /**
     * Removes the specified collectible from the level.
     *
     * @param collectible The collectible to remove.
     */
    public void remove(final Collectible collectible) {
        this.currentLevelData.getCollectibles().remove(collectible);
        Platform.runLater(() -> collectiblesPane.getChildren().remove(collectible));
    }

    /**
     * Schedules a modification of a pane to be executed on the JavaFX application thread.
     *
     * @param action The action to be performed.
     */
    private void modifyPaneLater(final Runnable action) {
        Platform.runLater(action);
    }

    /**
     * Casts and collects all nodes of a specified type from a pane.
     *
     * @param <T>   The type of nodes to collect.
     * @param pane  The pane to collect from.
     * @param clazz The class of the nodes to collect.
     * @return A list of nodes of the specified type.
     */
    private <T> List<T> castToList(final Pane pane, final Class<T> clazz) {
        List<T> list = new ArrayList<>();
        pane.getChildren().forEach(node -> {
            if (clazz.isInstance(node)) {
                list.add(clazz.cast(node));
            }
        });
        return list;
    }

    /**
     * Renders nodes in a pane.
     *
     * @param <T>   The type of nodes to render.
     * @param pane  The pane to render nodes in.
     * @param nodes The nodes to be rendered.
     */
    private <T> void renderNodes(final Pane pane, final List<T> nodes) {
        pane.getChildren().clear();
        Optional.ofNullable(nodes).ifPresent(list -> list.forEach(node -> {
            if (node != null) {
                positionAndAddNode(pane, node);
            }
        }));
    }

    /**
     * Renders a tile.
     *
     * @param tile The tile to render.
     * @param x    The x-coordinate of the tile.
     * @param y    The y-coordinate of the tile.
     */
    private void renderTile(final Tile tile, final int x, final int y) {
        tile.setGridPosition(x, y);
        tile.setLayoutX(x * Main.TILE_SIZE.get());
        tile.setLayoutY(y * Main.TILE_SIZE.get());
        tilesPane.getChildren().add(tile);
    }

    /**
     * Positions and adds a given node to the specified pane.
     * The method positions the node based on its type (either Collectible or Actor)
     * and adds it to the pane.
     *
     * @param pane The pane to which the node is to be added.
     * @param node The node to be positioned and added. Can be a Collectible or an Actor.
     */
    private <T> void positionAndAddNode(final Pane pane, final T node) {
        if (node instanceof Collectible) {
            position((Collectible) node);
        } else if (node instanceof Actor) {
            position((Actor) node);
        }
        pane.getChildren().add((javafx.scene.Node) node);
    }

    /**
     * Positions an Actor based on its current position.
     *
     * @param actor The actor to be positioned.
     */
    private void position(final Actor actor) {
        position(actor, actor.getPosition());
    }

    /**
     * Positions a Collectible based on its current position.
     *
     * @param collectible The collectible to be positioned.
     */
    private void position(final Collectible collectible) {
        position(collectible, collectible.getPosition());
    }

    /**
     * Positions a JavaFX Node (Actor or Collectible) at a specified position.
     * The position is adjusted based on the tile size and actor size.
     *
     * @param node     The node to be positioned.
     * @param position The target position for the node.
     */
    private void position(final javafx.scene.Node node, final Point2D position) {
        if (position == null) {
            return;
        }
        double offsetX = (Main.TILE_SIZE.get() - Main.ACTOR_SIZE.get()) / 2.0;
        double offsetY = (Main.TILE_SIZE.get() - Main.ACTOR_SIZE.get()) / 2.0;
        node.setLayoutX(position.getX() * Main.TILE_SIZE.get() + offsetX);
        node.setLayoutY(position.getY() * Main.TILE_SIZE.get() + offsetY);
    }

    /**
     * Retrieves the tile at a specific grid position.
     *
     * @param x The x-coordinate of the grid position.
     * @param y The y-coordinate of the grid position.
     * @return An Optional containing the Tile at the specified position, or an empty Optional if out of bounds.
     */
    public Optional<Tile> getTileAtGridPosition(final int x, final int y) {
        return isOutOfBounds(x, y) ? Optional.empty() : Optional.ofNullable(tiles[y][x]);
    }

    /**
     * Checks if the specified coordinates are out of the bounds of the tile array.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return true if the coordinates are out of bounds, false otherwise.
     */
    private boolean isOutOfBounds(final int x, final int y) {
        return x < 0 || x >= tiles[0].length || y < 0 || y >= tiles.length;
    }

    /**
     * Updates a tile at a specific grid position with a new tile.
     * If the position is out of bounds or the new tile is null, the method returns without making any changes.
     * Occupancy is transferred from the old tile to the new tile.
     *
     * @param x       The x-coordinate of the tile to update.
     * @param y       The y-coordinate of the tile to update.
     * @param newTile The new Tile to place at the specified position.
     */
    public void updateTile(final int x, final int y, final Tile newTile) {
        if (isOutOfBounds(x, y) || newTile == null) {
            return;
        }
        Tile oldTile = tiles[y][x];
        if (oldTile.getOccupiedBy() != null) {
            newTile.setOccupiedBy(oldTile.getOccupiedBy());
            oldTile.setOccupiedBy(null);
        }
        Point2D position = new Point2D(x, y);
        if (chipSocketLabels.containsKey(position)) {
            Label labelToRemove = chipSocketLabels.remove(position);
            Platform.runLater(() -> tilesPane.getChildren().remove(labelToRemove));
        }
        newTile.setGridPosition(x, y);
        tiles[y][x] = newTile;
        newTile.setLayoutX(x * Main.TILE_SIZE.get());
        newTile.setLayoutY(y * Main.TILE_SIZE.get());
        tilesPane.getChildren().set(y * tiles[0].length + x, newTile);
    }
}
