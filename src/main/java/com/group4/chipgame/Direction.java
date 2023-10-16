package com.group4.chipgame;

public enum Direction {
    NONE(0, 0),
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final double dx;
    private final double dy;

    Direction(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public enum Corner {
        NONE,          // No corner
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        TOP_RIGHT
    }

    public static Direction fromDelta(double dx, double dy) {
        if (dx > 0) return RIGHT;
        if (dx < 0) return LEFT;
        if (dy > 0) return DOWN;
        if (dy < 0) return UP;
        return NONE; // or you can throw an exception if it's an invalid delta
    }

    public Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            default -> NONE;
        };
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }
}
