package com.group4.chipgame;

/**
 * The Direction enum represents cardinal directions and provides utility methods for working with directions.
 */
public enum Direction {

    /**
     * Represents the absence of a direction.
     */
    NONE(0, 0),

    /**
     * Represents the upward direction.
     */
    UP(0, -1),

    /**
     * Represents the downward direction.
     */
    DOWN(0, 1),

    /**
     * Represents the leftward direction.
     */
    LEFT(-1, 0),

    /**
     * Represents the rightward direction.
     */
    RIGHT(1, 0);

    private final double dx;
    private final double dy;

    /**
     * Constructs a Direction with the given delta values.
     *
     * @param dx The change in the x-coordinate.
     * @param dy The change in the y-coordinate.
     */
    Direction(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * The Corner enum represents corners for directional purposes.
     */
    public enum Corner {
        /**
         * Represents no specific corner.
         */
        NONE,

        /**
         * Represents the bottom-left corner.
         */
        BOTTOM_LEFT,

        /**
         * Represents the bottom-right corner.
         */
        BOTTOM_RIGHT,

        /**
         * Represents the top-left corner.
         */
        TOP_LEFT,

        /**
         * Represents the top-right corner.
         */
        TOP_RIGHT
    }

    /**
     * Returns the Direction corresponding to the given change in coordinates (delta values).
     *
     * @param dx The change in the x-coordinate.
     * @param dy The change in the y-coordinate.
     * @return The Direction corresponding to the delta values.
     */
    public static Direction fromDelta(double dx, double dy) {
        if (dx > 0) return RIGHT;
        if (dx < 0) return LEFT;
        if (dy > 0) return DOWN;
        if (dy < 0) return UP;
        return NONE;
    }

    /**
     * Returns the delta values (change in coordinates) for the specified Direction.
     *
     * @param direction The Direction for which to retrieve delta values.
     * @return The delta values corresponding to the Direction.
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
     * Returns the opposite Direction of the current Direction.
     *
     * @return The opposite Direction.
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
     * Returns the change in the x-coordinate for this Direction.
     *
     * @return The change in the x-coordinate.
     */
    public double getDx() {
        return dx;
    }

    /**
     * Returns the change in the y-coordinate for this Direction.
     *
     * @return The change in the y-coordinate.
     */
    public double getDy() {
        return dy;
    }
}
