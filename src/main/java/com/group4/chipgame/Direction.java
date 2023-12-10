package com.group4.chipgame;

/**
 * This enum represents the possible directions for movement or orientation in the game.
 */
public enum Direction {
    NONE(0, 0),
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final double dx;
    private final double dy;

    /**
     * Constructs a direction with specified delta values for x and y axes.
     *
     * @param dx The delta value for the x-axis.
     * @param dy The delta value for the y-axis.
     */
    Direction(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Turns the direction to the left (counter-clockwise).
     *
     * @return The new direction after turning left.
     */
    public Direction turnLeft() {
        return switch (this) {
            case UP -> LEFT;
            case LEFT -> DOWN;
            case DOWN -> RIGHT;
            case RIGHT -> UP;
            default -> NONE;
        };
    }

    /**
     * Turns the direction to the right (clockwise).
     *
     * @return The new direction after turning right.
     */
    public Direction turnRight() {
        return switch (this) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
            default -> NONE;
        };
    }

    /**
     * Gets the opposite direction.
     *
     * @return The opposite direction.
     */
    public Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            default -> NONE;
        };
    }

    /**
     * Gets the delta value for the x-axis.
     *
     * @return The delta value for the x-axis.
     */
    public double getDx() {
        return dx;
    }

    /**
     * Gets the delta value for the y-axis.
     *
     * @return The delta value for the y-axis.
     */
    public double getDy() {
        return dy;
    }

    /**
     * Converts delta values for x and y axes into a direction.
     *
     * @param dx Delta value for the x-axis.
     * @param dy Delta value for the y-axis.
     * @return The corresponding direction based on delta values.
     */
    public static Direction fromDelta(double dx, double dy) {
        if (dx > 0) {
            return RIGHT;
        }
        if (dx < 0) {
            return LEFT;
        }
        if (dy > 0) {
            return DOWN;
        }
        if (dy < 0) {
            return UP;
        }
        return NONE;
    }

    /**
     * Converts a direction into delta values for x and y axes.
     *
     * @param direction The direction to be converted.
     * @return An array of two doubles representing delta values for x and y axes.
     */
    public static double[] toDelta(Direction direction) {
        return switch (direction) {
            case UP -> new double[]{0, -1};
            case DOWN -> new double[]{0, 1};
            case LEFT -> new double[]{-1, 0};
            case RIGHT -> new double[]{1, 0};
            default -> new double[]{0, 0};
        };
    }

    /**
     * This enum represents corners, used for specific directional orientations or actions.
     */
    public enum Corner {
        NONE,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        TOP_RIGHT
    }
}
