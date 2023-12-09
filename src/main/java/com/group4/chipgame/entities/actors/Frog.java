package com.group4.chipgame.entities.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.tiles.*;
import javafx.geometry.Point2D;
import java.util.*;

public class Frog extends Enemy {
    private static final String FROG_IMAGE_PATH = "/images/chipgame/actors/frog.png";
    private final Random random = new Random();

    public Frog(double x, double y) {
        super(FROG_IMAGE_PATH, x, y);
        this.moveInterval = 100;
    }

    @Override
    protected boolean isMoveValid(Point2D newPosition, LevelRenderer levelRenderer) {
        Optional<Tile> targetTileOpt = levelRenderer.getTileAtGridPosition((int) newPosition.getX(), (int) newPosition.getY());
        if (targetTileOpt.isEmpty()) return false;
        Tile targetTile = targetTileOpt.get();
        boolean isTileNotOccupied = targetTile.getOccupiedBy() == null || targetTile.getOccupiedBy() instanceof Player;
        return (targetTile instanceof Path || targetTile instanceof Button) && targetTile.isWalkable() && isTileNotOccupied;
    }

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
            double dx = nextMove.getX() - currentPosition.getX();
            double dy = nextMove.getY() - currentPosition.getY();
            moveOneStep(dx, dy, levelRenderer);
        }
    }

    private Point2D findRandomValidMove(LevelRenderer levelRenderer) {
        List<Point2D> validMoves = new ArrayList<>();
        for (Point2D neighborPos : getNeighborPositions(currentPosition)) {
            if (canMoveTo(neighborPos, levelRenderer)) {
                validMoves.add(neighborPos);
            }
        }
        if (!validMoves.isEmpty()) {
            return validMoves.get(random.nextInt(validMoves.size()));
        }
        return null;
    }

    public void moveOneStep(double dx, double dy, LevelRenderer levelRenderer) {
        if (isMoving) return;

        if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
            throw new IllegalArgumentException("Move step too large: dx=" + dx + ", dy=" + dy);
        }

        Direction direction = Direction.fromDelta(dx, dy);
        double newX = currentPosition.getX() + dx;
        double newY = currentPosition.getY() + dy;

        if (canMove(dx, dy, levelRenderer)) {
            performMove(newX, newY, levelRenderer, direction);
        }
    }


    private Point2D findPlayerPosition(LevelRenderer levelRenderer) {
        for (Actor actor : levelRenderer.getActors()) {
            if (actor instanceof Player) {
                return actor.getPosition();
            }
        }
        return null;
    }

    private Point2D findNextMoveTowardsPlayer(Point2D playerPosition, LevelRenderer levelRenderer) {
        List<Node> openList = new ArrayList<>();
        Set<Node> closedSet = new HashSet<>();
        Node startNode = new Node(currentPosition, null, 0, currentPosition.distance(playerPosition));
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
                double newGCost = currentNode.gCost + currentPosition.distance(neighborPos);
                Node neighborNode = new Node(neighborPos, currentNode, newGCost, neighborPos.distance(playerPosition));
                if (openList.stream().noneMatch(n -> n.position.equals(neighborPos) && n.gCost <= newGCost)) {
                    openList.add(neighborNode);
                }
            }
        }
        return null;
    }

    private List<Point2D> getNeighborPositions(Point2D position) {
        return Arrays.asList(
                new Point2D(position.getX() + 1, position.getY()),
                new Point2D(position.getX() - 1, position.getY()),
                new Point2D(position.getX(), position.getY() + 1),
                new Point2D(position.getX(), position.getY() - 1)
        );
    }

    private boolean canMoveTo(Point2D position, LevelRenderer levelRenderer) {
        return isMoveValid(position, levelRenderer);
    }

    private Point2D retracePath(Node startNode, Node endNode) {
        Node currentNode = endNode;
        while (currentNode != null && currentNode.parent != null && !currentNode.parent.equals(startNode)) {
            currentNode = currentNode.parent;
        }
        return currentNode != null ? currentNode.position : null;
    }

    static class Node {
        Point2D position;
        Node parent;
        double gCost;
        double hCost;
        double fCost;

        Node(Point2D position, Node parent, double gCost, double hCost) {
            this.position = position;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return position.equals(node.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position);
        }
    }
}
