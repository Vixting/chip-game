package com.group4.chipgame;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.BlurType;
import javafx.scene.paint.Color;

public class EffectManager {

    private static final Color DEFAULT_SHADOW_COLOR = Color.color(0.0, 0.0, 0.0, 0.65);
    private static final double SHADOW_RADIUS_FACTOR = 3.5;
    private static final double SHADOW_OFFSET_FACTOR = 16;
    private static final double SHADOW_SPREAD = 0.6;
    private static final BlurType SHADOW_BLUR_TYPE = BlurType.GAUSSIAN;

    public static void applyDynamicShadowEffect(Node node) {
        if (node == null) {
            return;
        }

        double size = Math.max(node.getBoundsInLocal().getWidth(), node.getBoundsInLocal().getHeight());
        double radius = size / SHADOW_RADIUS_FACTOR;
        double offsetX = size / SHADOW_OFFSET_FACTOR;
        double offsetY = size / SHADOW_OFFSET_FACTOR;

        applyShadowEffect(node, radius, offsetX, offsetY, DEFAULT_SHADOW_COLOR, SHADOW_SPREAD);
    }

    public static void applyShadowEffect(Node node, double radius, double offsetX, double offsetY, Color color, double spread) {
        if (node == null) {
            return;
        }

        DropShadow dropShadow = createDropShadow(radius, offsetX, offsetY, color, spread);
        node.setEffect(dropShadow);
    }

    private static DropShadow createDropShadow(double radius, double offsetX, double offsetY, Color color, double spread) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(radius);
        dropShadow.setOffsetX(offsetX);
        dropShadow.setOffsetY(offsetY);
        dropShadow.setColor(color);
        dropShadow.setSpread(spread);
        dropShadow.setBlurType(SHADOW_BLUR_TYPE);
        return dropShadow;
    }
}
