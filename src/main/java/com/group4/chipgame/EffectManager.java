package com.group4.chipgame;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;

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

    public static void applyShadowEffect(Node node) {
        applyShadowEffect(node, DEFAULT_SHADOW_RADIUS, DEFAULT_SHADOW_OFFSET_X, DEFAULT_SHADOW_OFFSET_Y, DEFAULT_SHADOW_COLOR);
    }

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

    public static void applyLightingEffect(Node node, Light light) {
        applyLightingEffect(node, light, DEFAULT_SURFACE_SCALE, DEFAULT_SPECULAR_CONSTANT, DEFAULT_SPECULAR_EXPONENT);
    }

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


    public static Light createDefaultLight() {
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(45.0);

        return light;
    }
}
