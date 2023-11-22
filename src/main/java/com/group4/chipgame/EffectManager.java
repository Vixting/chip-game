package com.group4.chipgame;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;

/**
 * The EffectManager class provides methods for applying visual effects to JavaFX nodes, such as shadow and lighting effects.
 */
public class EffectManager {

    // Default values for shadow effect
    private static final double DEFAULT_SHADOW_RADIUS = 25.0;
    private static final double DEFAULT_SHADOW_OFFSET_X = 8.0;
    private static final double DEFAULT_SHADOW_OFFSET_Y = 8.0;
    private static final Color DEFAULT_SHADOW_COLOR = Color.color(0.1, 0.1, 0.1, 0.7);
    private static final double DEFAULT_SHADOW_SPREAD = 0.7;

    // Default values for lighting effect
    private static final double DEFAULT_SURFACE_SCALE = 1.5;
    private static final double DEFAULT_SPECULAR_CONSTANT = 0.5;
    private static final double DEFAULT_SPECULAR_EXPONENT = 20.0;
    private static final double DEFAULT_DIFFUSE_CONSTANT = 0.8;

    /**
     * Applies a default shadow effect to the specified JavaFX node.
     *
     * @param node The JavaFX node to which the shadow effect should be applied.
     */
    public static void applyShadowEffect(Node node) {
        applyShadowEffect(node, DEFAULT_SHADOW_RADIUS, DEFAULT_SHADOW_OFFSET_X, DEFAULT_SHADOW_OFFSET_Y, DEFAULT_SHADOW_COLOR);
    }

    /**
     * Applies a customized shadow effect to the specified JavaFX node.
     *
     * @param node    The JavaFX node to which the shadow effect should be applied.
     * @param radius  The radius of the shadow.
     * @param offsetX The X offset of the shadow.
     * @param offsetY The Y offset of the shadow.
     * @param color   The color of the shadow.
     */
    public static void applyShadowEffect(Node node, double radius, double offsetX, double offsetY, Color color) {
        if (node == null) {
            return;
        }

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(radius);
        dropShadow.setOffsetX(offsetX);
        dropShadow.setOffsetY(offsetY);
        dropShadow.setColor(color);
        dropShadow.setSpread(DEFAULT_SHADOW_SPREAD);
        dropShadow.setBlurType(BlurType.THREE_PASS_BOX);

        node.setEffect(dropShadow);
    }

    /**
     * Applies a default lighting effect to the specified JavaFX node using a specified light source.
     *
     * @param node  The JavaFX node to which the lighting effect should be applied.
     * @param light The light source to be used in the lighting effect.
     */
    public static void applyLightingEffect(Node node, Light light) {
        applyLightingEffect(node, light, DEFAULT_SURFACE_SCALE, DEFAULT_SPECULAR_CONSTANT, DEFAULT_SPECULAR_EXPONENT);
    }

    /**
     * Applies a customized lighting effect to the specified JavaFX node using a specified light source and parameters.
     *
     * @param node             The JavaFX node to which the lighting effect should be applied.
     * @param light            The light source to be used in the lighting effect.
     * @param surfaceScale     The surface scale factor for the lighting effect.
     * @param specularConstant The specular constant for the lighting effect.
     * @param specularExponent The specular exponent for the lighting effect.
     */
    public static void applyLightingEffect(Node node, Light light, double surfaceScale, double specularConstant, double specularExponent) {
        if (node == null) {
            return;
        }

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(surfaceScale);
        lighting.setSpecularConstant(specularConstant);
        lighting.setSpecularExponent(specularExponent);
        lighting.setDiffuseConstant(DEFAULT_DIFFUSE_CONSTANT);

        node.setEffect(lighting);
    }

    /**
     * Creates and returns a default distant light source.
     *
     * @return The default distant light source.
     */
    public static Light createDefaultLight() {
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(45.0);

        return light;
    }
}
