package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.Main;
import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Player;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.util.Objects;

public class ChipSocket extends Tile {
    private final int requiredChips;
    private final StackPane stackPane;
    private final Label chipLabel;

    public ChipSocket(int requiredChips) {
        super("/images/chipgame/tiles/chipSocket.png", false);
        this.requiredChips = requiredChips;

        chipLabel = new Label(String.valueOf(requiredChips));
        chipLabel.setFont(new Font("Arial", 20));
        chipLabel.setTextFill(javafx.scene.paint.Color.WHITE);

        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/chipgame/tiles/chipSocket.png"))));
        imageView.setFitWidth(Main.TILE_SIZE.get());
        imageView.setFitHeight(Main.TILE_SIZE.get());

        stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView, chipLabel);
        stackPane.setPrefSize(Main.TILE_SIZE.get(), Main.TILE_SIZE.get());
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public int getRequiredChips() {
        return requiredChips;
    }

    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        if (actor instanceof Player player) {
            if (player.getChipsCount() >= requiredChips) {
                player.consumeChips(requiredChips);
                openSocket(levelRenderer, actor);
            }
        }
    }

    private void openSocket(LevelRenderer levelRenderer, Actor actor) {
        levelRenderer.updateTile(this.gridX, this.gridY, new Path());
    }
}
