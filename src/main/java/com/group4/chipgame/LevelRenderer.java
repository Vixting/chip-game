package com.group4.chipgame;

import com.group4.chipgame.collectibles.Collectible;
import com.group4.chipgame.tiles.Tile;
import com.group4.chipgame.actors.Actor;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The LevelRenderer class is responsible for rendering the game world, including tiles, actors, and collectibles.
 */
public class LevelRenderer {

    private final Pane gamePane;
    private final Pane tilesPane;
    private final Pane actorsPane;
    private final Pane collectiblesPane;
    private Tile[][] tiles;

    /**
     * Constructs a new LevelRenderer instance.
     */
    public LevelRenderer() {
        tilesPane = new Pane();
        actorsPane = new Pane();
        collectiblesPane = new Pane();
        gamePane = new Pane(tilesPane, actorsPane, collectiblesPane);

        // Add listeners to update sizes when TILE_SIZE or ACTOR_SIZE changes
        Main.TILE_SIZE.addListener((obs, oldVal, newVal) -> updateSizes());
        Main.ACTOR_SIZE.addListener((obs, oldVal, newVal) -> updateSizes());
    }

    /**
     * Updates the sizes of tiles, actors, and collectibles based on TILE_SIZE and ACTOR_SIZE.
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
     * Gets the list of collectibles in the game world.
     *
     * @return The list of collectibles.
     */
    public List<Collectible> getCollectibles() {
        return castToList(collectiblesPane, Collectible.class);
    }

    /**
     * Gets the list of actors in the game world.
     *
     * @return The list of actors.
     */
    public List<Actor> getActors() {
        return castToList(actorsPane, Actor.class);
    }

    /**
     * Renders the specified collectibles in the game world.
     *
     * @param collectibles The list of collectibles to render.
     */
    public void renderCollectibles(List<Collectible> collectibles) {
        renderNodes(collectiblesPane, collectibles);
    }

    /**
     * Renders the specified actors in the game world.
     *
     * @param actors The list of actors to render.
     */
    public void renderActors(List<Actor> actors) {
        renderNodes(actorsPane, actors);
    }

    /**
     * Renders the specified tiles in the game world.
     *
     * @param tiles The 2D array of tiles to render.
     */
    public void renderTiles(Tile[][] tiles) {
        this.tiles = tiles;
        tilesPane.getChildren().clear();
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                if (tile != null) {
                    renderTile(tile, x, y);
                }
            }
        }
    }

    /**
     * Gets the game pane containing all rendered elements.
     *
     * @return The game pane.
     */
    public Pane getGamePane() {
        return gamePane;
    }

    /**
     * Gets the 2D array of tiles representing the game world.
     *
     * @return The array of tiles.
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Clears all elements from the renderer.
     */
    public void clear() {
        tilesPane.getChildren().clear();
        actorsPane.getChildren().clear();
        collectiblesPane.getChildren().clear();
    }

    /**
     * Removes the specified actor from the renderer.
     *
     * @param actor The actor to remove.
     */
    public void remove(Actor actor) {
        modifyPaneLater(() -> actorsPane.getChildren().remove(actor));
    }

    /**
     * Removes the specified collectible from the renderer.
     *
     * @param collectible The collectible to remove.
     */
    public void remove(Collectible collectible) {
        modifyPaneLater(() -> collectiblesPane.getChildren().remove(collectible));
    }

    /**
     * Modifies the specified pane later on the JavaFX application thread.
     *
     * @param action The action to perform on the pane.
     */
    private void modifyPaneLater(Runnable action) {
        Platform.runLater(action);
    }

    /**
     * Casts the children of a pane to a list of the specified class.
     *
     * @param pane  The pane containing the children.
     * @param clazz The class to cast to.
     * @param <T>   The type of the class.
     * @return The list of children cast to the specified class.
     */
    private <T> List<T> castToList(Pane pane, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        pane.getChildren().forEach(node -> {
            if (clazz.isInstance(node)) {
                list.add(clazz.cast(node));
            }
        });
        return list;
    }

    /**
     * Renders the specified nodes in the specified pane.
     *
     * @param pane  The pane to render the nodes in.
     * @param nodes The list of nodes to render.
     * @param <T>   The type of the nodes.
     */
    private <T> void renderNodes(Pane pane, List<T> nodes) {
        Optional.ofNullable(nodes).ifPresent(list -> list.forEach(node -> {
            if (node != null && !pane.getChildren().contains(node)) {
                positionAndAddNode(pane, node);
            }
        }));
    }

    /**
     * Renders the specified tile at the specified position in the game world.
     *
     * @param tile The tile to render.
     * @param x    The x-coordinate of the tile.
     * @param y    The y-coordinate of the tile.
     */
    private void renderTile(Tile tile, int x, int y) {
        tile.setGridPosition(x, y);
        tile.setLayoutX(x * Main.TILE_SIZE.get());
        tile.setLayoutY(y * Main.TILE_SIZE.get());
        tilesPane.getChildren().add(tile);
    }

    /**
     * Positions and adds the specified node to the specified pane.
     *
     * @param pane The pane to add the node to.
     * @param node The node to add.
     * @param <T>  The type of the node.
     */
    private <T> void positionAndAddNode(Pane pane, T node) {
        if (node instanceof Collectible) {
            position((Collectible) node);
        } else if (node instanceof Actor) {
            position((Actor) node);
        }
        pane.getChildren().add((javafx.scene.Node) node);
    }

    /**
     * Positions the specified actor and applies a shadow effect.
     *
     * @param actor The actor to position and apply the effect to.
     */
    private void position(Actor actor) {
        position(actor, actor.getPosition());
        EffectManager.applyShadowEffect(actor);
    }

    /**
     * Positions the specified collectible and applies a shadow effect.
     *
     * @param collectible The collectible to position and apply the effect to.
     */
    private void position(Collectible collectible) {
        position(collectible, collectible.getPosition());
        EffectManager.applyShadowEffect(collectible);
    }

    /**
     * Positions the specified node at the specified position in the game world.
     *
     * @param node     The node to position.
     * @param position The position to place the node at.
     */
    private void position(javafx.scene.Node node, Point2D position) {
        if (position == null) return;
        double offsetX = (Main.TILE_SIZE.get() - Main.ACTOR_SIZE.get()) / 2.0;
        double offsetY = (Main.TILE_SIZE.get() - Main.ACTOR_SIZE.get()) / 2.0;
        node.setLayoutX(position.getX() * Main.TILE_SIZE.get() + offsetX);
        node.setLayoutY(position.getY() * Main.TILE_SIZE.get() + offsetY);
    }

    /**
     * Gets the tile at the specified grid position.
     *
     * @param x The x-coordinate of the grid position.
     * @param y The y-coordinate of the grid position.
     * @return An Optional containing the tile at the specified position if it exists.
     */
    public Optional<Tile> getTileAtGridPosition(int x, int y) {
        return isOutOfBounds(x, y) ? Optional.empty() : Optional.ofNullable(tiles[y][x]);
    }

    /**
     * Checks if the specified coordinates are out of bounds of the game world.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return True if the coordinates are out of bounds, false otherwise.
     */
    private boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= tiles[0].length || y < 0 || y >= tiles.length;
    }

    /**
     * Updates the tile at the specified grid position with a new tile.
     *
     * @param x       The x-coordinate of the grid position.
     * @param y       The y-coordinate of the grid position.
     * @param newTile The new tile to set at the specified position.
     */
    public void updateTile(int x, int y, Tile newTile) {
        if (isOutOfBounds(x, y) || newTile == null) return;

        newTile.setGridPosition(x, y);
        tiles[y][x] = newTile;
        newTile.setLayoutX(x * Main.TILE_SIZE.get());
        newTile.setLayoutY(y * Main.TILE_SIZE.get());
        tilesPane.getChildren().set(y * tiles[0].length + x, newTile);
    }
}
