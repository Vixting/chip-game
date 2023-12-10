package com.group4.chipgame.ui;

import javafx.scene.layout.Pane;
import javafx.scene.Node;

/**
 * This abstract class provides a base for creating user interface components.
 * It manages a root pane and provides methods for UI creation and dynamic adjustment based on size changes.
 */
public abstract class BaseUI {
    private final Pane rootPane;

    /**
     * Constructs a BaseUI object with a specified root pane.
     * This constructor initializes the UI and sets up listeners for size changes.
     *
     * @param rootPane The root pane for this UI component.
     */
    public BaseUI(Pane rootPane) {
        this.rootPane = rootPane;
        createUI();
        addSizeListeners();
    }

    /**
     * Gets the root pane of this UI component.
     *
     * @return The root pane.
     */
    public Pane getRootPane() {
        return rootPane;
    }

    /**
     * Abstract method to create UI components. Implementations should define how the UI is set up.
     */
    protected abstract void createUI();

    /**
     * Adds listeners to the root pane's width and height properties.
     * These listeners invoke the adjustUI method to handle size changes.
     */
    private void addSizeListeners() {
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> adjustUI());
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> adjustUI());
    }

    /**
     * Abstract method to adjust UI components based on size changes.
     * Implementations should define how UI components respond to size changes.
     */
    abstract void adjustUI();

    /**
     * Sets the font size of a node based on the root pane's size.
     * The font size is calculated as a proportion of the smaller
     * dimension of the root pane, divided by the given ratio.
     *
     * @param node The node whose font size is to be set.
     * @param ratio The ratio to determine the font size relative to the root pane's size.
     */
    protected void setFontSizeFor(Node node, double ratio) {
        double fontSize = Math.min(rootPane.getWidth(), rootPane.getHeight()) / ratio;
        node.setStyle("-fx-font-size: " + fontSize + "px;");
    }
}
