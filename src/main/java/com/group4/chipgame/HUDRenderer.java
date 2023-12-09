package com.group4.chipgame;

import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class HUDRenderer {

    private final Pane hudPane;
    private Text timerText;
    private Text scoreText;
    private int score;

    private Timeline timerTimeline;
    private int initialTimerSeconds;
    private int remainingTimerSeconds;

    public HUDRenderer() {
        hudPane = new Pane();
        timerText = createText("Time: 0", 10, 20, 18);
        scoreText = createText("Score: 0", 10, 40, 18);
        hudPane.getChildren().addAll(timerText, scoreText);

        timerTimeline = new Timeline();
        initialTimerSeconds = 60;
        remainingTimerSeconds = initialTimerSeconds;
        setupTimer();
    }

    private Text createText(String content, double x, double y, int fontSize) {
        Text text = new Text(x, y, content);
        text.setFont(new Font(fontSize));
        return text;
    }

    private void setupTimer() {
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                remainingTimerSeconds--;
                updateTimerText();

                if (remainingTimerSeconds <= 0) {
                    stopTimer();
                }
            }
        });

        timerTimeline.getKeyFrames().add(keyFrame);
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void updateTimerText() {
        timerText.setText("Time: " + remainingTimerSeconds);
    }

    public void updateScore(int points) {
        score += points;
        updateScoreText();
    }

    private void updateScoreText() {
        scoreText.setText("Score: " + score);
    }

    public int getScore() {
        return score;
    }

    public void startTimer() {
        remainingTimerSeconds = initialTimerSeconds;
        updateTimerText();
        timerTimeline.play();
    }

    public void stopTimer() {
        timerTimeline.stop();
    }

    public void addText(String content, double x, double y, int fontSize) {
        Text text = new Text(x, y, content);
        text.setFont(new Font(fontSize));
        hudPane.getChildren().add(text);
    }

    public Pane getHUDPane() {
        return hudPane;
    }

    public void clear() {
        hudPane.getChildren().clear();
    }
}
