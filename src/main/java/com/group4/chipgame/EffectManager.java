package com.group4.chipgame;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.BlurType;
import javafx.scene.paint.Color;

/**
 * This class manages visual effects, such as shadows, applied to various UI elements.
 */
public class EffectManager {

    private static final Color DEFAULT_SHADOW_COLOR = Color.color(0.0, 0.0, 0.0, 0.65);
    private static final double SHADOW_RADIUS_FACTOR = 3.5;
    private static final double SHADOW_OFFSET_FACTOR = 16;
    private static final double SHADOW_SPREAD = 0.6;
    private static final BlurType SHADOW_BLUR_TYPE = BlurType.GAUSSIAN;

    /**
     * Applies a dynamic shadow effect to a given node. The size of the shadow is
     * calculated based on the node's dimensions.
     *
     * @param node The node to which the shadow effect is applied.
     */
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

    /**
     * Applies a shadow effect to a given node with specified parameters.
     *
     * @param node   The node to which the shadow effect is applied.
     * @param radius The radius of the shadow blur.
     * @param offsetX The horizontal offset of the shadow.
     * @param offsetY The vertical offset of the shadow.
     * @param color   The color of the shadow.
     * @param spread  The spread of the shadow.
     */
    public static void applyShadowEffect(Node node, double radius, double offsetX, double offsetY, Color color, double spread) {
        if (node == null) {
            return;
        }

        DropShadow dropShadow = createDropShadow(radius, offsetX, offsetY, color, spread);
        node.setEffect(dropShadow);
    }

    /**
     * Creates a DropShadow effect with the specified parameters.
     *
     * @param radius The radius of the shadow blur.
     * @param offsetX The horizontal offset of the shadow.
     * @param offsetY The vertical offset of the shadow.
     * @param color   The color of the shadow.
     * @param spread  The spread of the shadow.
     * @return A DropShadow effect with the specified settings.
     */
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
