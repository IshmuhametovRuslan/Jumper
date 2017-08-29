package com;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Statistic {

    private Pane root;
    private Hero player;
    private PauseMain pauseMain;

    private Label textHeroLive;
    private Label textHeroScore;

    Statistic(Pane root, PauseMain pauseMain, Hero player) {
        this.root = root;
        this.pauseMain = pauseMain;;
        this.player = player;
        start();
    }

    private void start() {
        textHeroLive = new Label("LIVE: " + player.getLive());
        textHeroLive.setTranslateX(0);
        textHeroLive.setTranslateY(0);
        textHeroLive.setTextFill(Color.RED);
        textHeroLive.setFont(new Font(35));
        root.getChildren().add(textHeroLive);

        textHeroScore = new Label("SCORE: " + player.getScore());
        textHeroScore.setTranslateY(0);
        textHeroScore.setTextFill(Color.CHARTREUSE);
        textHeroScore.setFont(new Font(35));
        root.getChildren().add(textHeroScore);

    }

    public void restart() {
        textHeroLive.setText("LIVE: " + player.getLive());

        textHeroScore.setText("SCORE: " + player.getScore());
        textHeroScore.setTranslateX(Main.SCREEN_WIDTH - textHeroScore.getWidth());

        root.getChildren().removeAll(textHeroLive, textHeroScore);
        root.getChildren().addAll(textHeroLive, textHeroScore);

        if (player.getLive() <= 0) {
            startPauseMain();
        }
    }

    private void startPauseMain() {
        pauseMain.start("ИГРА ОКОНЧЕНА\n" +
                "Ваш рекорд: " + player.getScore(),
                PauseCommands.newGame, PauseCommands.exitGame);

    }
}
