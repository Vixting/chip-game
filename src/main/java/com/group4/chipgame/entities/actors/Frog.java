package com.group4.chipgame.entities.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.tiles.*;
import javafx.geometry.Point2D;
import java.util.*;

/**
 * Represents a Frog enemy in the ChipGame.
 * This class defines the behavior of the Frog enemy, including movement decisions and interactions.
 */
public class Frog extends Enemy {
    private static final String FROG_IMAGE_PATH = "/images/chipgame/actors/frog.png";
    private final Random random = new Random();
    private static final int MOVE_INTERVAL = 100;

    /**
     * Constructs a Frog enemy with specified initial position.
     *
     * @param x The initial x-coordinate of the Frog.
     * @param y The initial y-coordinate of the Frog.
     */
    public Frog(double x, double y) {
        super(FROG_IMAGE_PATH, x, y);
        this.setMoveInterval(MOVE_INTERVAL);
    }

    @Override
    protected boolean isMoveValid(Point2D newPosition, LevelRenderer levelRenderer) {
        Optional<Tile> targetTileOpt =
                levelRenderer.getTileAtGridPosition((int) newPosition.getX(),
                        (int) newPosition.getY());
        if (targetTileOpt.isEmpty()) {
            return false;
        }
        Tile targetTile = targetTileOpt.get();
        boolean isTileNotOccupied = targetTile.getOccupiedBy() == null || targetTile.getOccupiedBy() instanceof Player;
        return (targetTile instanceof Path
                || targetTile instanceof Button)
                && targetTile.isWalkable()
                && isTileNotOccupied;
    }

    /**
     * Decides the next move for the Frog based on its current position and movement rules.
     *
     * @param levelRenderer The renderer for the game level.
     */
    @Override
    public void makeMoveDecision(LevelRenderer levelRenderer) {
        Point2D playerPosition = findPlayerPosition(levelRenderer);
        Point2D nextMove = null;
        if (playerPosition != null) {
            nextMove = findNextMoveTowardsPlayer(playerPosition, levelRenderer);
        }
        if (nextMove == null) {
            nextMove = findRandomValidMove(levelRenderer);
        }
        if (nextMove != null) {
            double dx = nextMove.getX() - getCurrentPosition().getX();
            double dy = nextMove.getY() - getCurrentPosition().getY();
            moveOneStep(dx, dy, levelRenderer);
        }
    }

    /**
     * Finds a random valid move for the Frog.
     *
     * @param levelRenderer The renderer for the game level.
     * @return The next valid move as a Point2D, or null if no valid move is found.
     */
    private Point2D findRandomValidMove(LevelRenderer levelRenderer) {
        List<Point2D> validMoves = new ArrayList<>();
        for (Point2D neighborPos : getNeighborPositions(getCurrentPosition())) {
            if (canMoveTo(neighborPos, levelRenderer)) {
                validMoves.add(neighborPos);
            }
        }
        if (!validMoves.isEmpty()) {
            return validMoves.get(random.nextInt(validMoves.size()));
        }
        return null;
    }

    /**
     * Moves the Frog one step in the specified direction.
     *
     * @param dx            The delta x-coordinate for the move.
     * @param dy            The delta y-coordinate for the move.
     * @param levelRenderer The renderer for the game level.
     */
    public void moveOneStep(double dx, double dy, LevelRenderer levelRenderer) {
        if (isMoving()) {
            return;
        }

        if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
            throw new IllegalArgumentException("Move step too large: dx=" + dx + ", dy=" + dy);
        }

        Direction direction = Direction.fromDelta(dx, dy);
        double newX = getCurrentPosition().getX() + dx;
        double newY = getCurrentPosition().getY() + dy;

        if (canMove(dx, dy, levelRenderer)) {
            performMove(newX, newY, levelRenderer, direction);
        }
    }

    /**
     * Finds the position of the player in the game level.
     *
     * @param levelRenderer The renderer for the game level.
     * @return The position of the player as a Point2D, or null if the player is not found.
     */
    private Point2D findPlayerPosition(LevelRenderer levelRenderer) {
        for (Actor actor : levelRenderer.getActors()) {
            if (actor instanceof Player) {
                return actor.getPosition();
            }
        }
        return null;
    }

    /**
     * Finds the next move towards the player based on the player's position.
     *
     * @param playerPosition The position of the player.
     * @param levelRenderer The renderer for the game level.
     * @return The next move as a Point2D, or null if no valid move is found.
     */
    private Point2D findNextMoveTowardsPlayer(Point2D playerPosition, LevelRenderer levelRenderer) {
        List<Node> openList = new ArrayList<>();
        Set<Node> closedSet = new HashSet<>();
        Node startNode = new Node(getCurrentPosition(), null, 0, getCurrentPosition().distance(playerPosition));
        openList.add(startNode);
        while (!openList.isEmpty()) {
            Node currentNode = openList.stream().min(Comparator.comparingDouble(n -> n.fCost)).orElseThrow();
            openList.remove(currentNode);
            closedSet.add(currentNode);
            if (currentNode.position.equals(playerPosition)) {
                return retracePath(startNode, currentNode);
            }
            for (Point2D neighborPos : getNeighborPositions(currentNode.position)) {
                if (!canMoveTo(neighborPos, levelRenderer) || closedSet.contains(new Node(neighborPos, null, 0, 0))) {
                    continue;
                }
                double newGCost = currentNode.gCost + getCurrentPosition().distance(neighborPos);
                Node neighborNode = new Node(neighborPos, currentNode, newGCost, neighborPos.distance(playerPosition));
                if (openList.stream().noneMatch(n -> n.position.equals(neighborPos) && n.gCost <= newGCost)) {
                    openList.add(neighborNode);
                }
            }
        }
        return null;
    }

    /**
     * Gets the neighboring positions of a given position.
     *
     * @param position The current position.
     * @return A list of neighboring positions.
     */
    private List<Point2D> getNeighborPositions(Point2D position) {
        return Arrays.asList(
                new Point2D(position.getX() + 1, position.getY()),
                new Point2D(position.getX() - 1, position.getY()),
                new Point2D(position.getX(), position.getY() + 1),
                new Point2D(position.getX(), position.getY() - 1)
        );
    }


    /**
     * Checks if the Frog can move to a specified position.
     *
     * @param position The position to check.
     * @param levelRenderer The renderer for the game level.
     * @return True if the Frog can move to the position, false otherwise.
     */
    private boolean canMoveTo(Point2D position, LevelRenderer levelRenderer) {
        return isMoveValid(position, levelRenderer);
    }

    /**
     * Retraces the path from the end node to the start node.
     *
     * @param startNode The starting node.
     * @param endNode The ending node.
     * @return The next position to move to as part of the path.
     */
    private Point2D retracePath(Node startNode, Node endNode) {
        Node currentNode = endNode;
        while (currentNode != null && currentNode.parent != null && !currentNode.parent.equals(startNode)) {
            currentNode = currentNode.parent;
        }
        return currentNode != null ? currentNode.position : null;
    }

    /**
     * Represents a node in the pathfinding algorithm for the Frog.
     */
    static class Node {
        private final Point2D position;
        private final Node parent;
        private final double gCost;
        private double hCost;
        private final double fCost;

        Node(Point2D position, Node parent, double gCost, double hCost) {
            this.position = position;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Node node = (Node) obj;
            return position.equals(node.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position);
        }
    }
}
