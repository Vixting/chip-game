package com.group4.chipgame.ui;

import javafx.scene.layout.Pane;
import javafx.scene.Node;

public abstract class BaseUI {
    protected Pane rootPane;

    public BaseUI(Pane rootPane) {
        this.rootPane = rootPane;
        createUI();
        addSizeListeners();
    }

    protected abstract void createUI();

    private void addSizeListeners() {
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> adjustUI());
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> adjustUI());
    }

    abstract void adjustUI();

    protected void setFontSizeFor(Node node, double ratio) {
        double fontSize = Math.min(rootPane.getWidth(), rootPane.getHeight()) / ratio;
        node.setStyle("-fx-font-size: " + fontSize + "px;");
    }
}
