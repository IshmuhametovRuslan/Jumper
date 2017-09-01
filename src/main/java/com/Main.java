package com;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class Main extends Application{

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 720;

    private int interval;

    private Pane root;

    private MediaPlayer audioPlayer;

    private Hero player;
    private Statistic state;
    private PauseMain pauseMain;

    private AnimationTimer timer;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Jumper");
        //set icon
        primaryStage.getIcons().add(new Image(this.getClass().getResource("images/icon.gif").toString()));

        root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, CornerRadii.EMPTY, Insets.EMPTY)));

        player = new Hero();

        //create empty pauseMain
        pauseMain = new PauseMain(this);

        state = new Statistic(root, pauseMain, player);

        //play sound of game
        audioPlayer = new MediaPlayer(
                new Media(this.getClass().getResource("audio/audio.mp3").toString()));
        audioPlayer.setAutoPlay(true);
        audioPlayer.setCycleCount(-1);

        createStartingBlocks();

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (pauseMain.isVisible() && player.getLive() > 0) {
                    pauseMain.setVisible(false);
                    continueGame();
                } else if (!pauseMain.isVisible()){
                    pauseMain.start("Нажми ESC для продолжения", PauseCommands.newGame, PauseCommands.exitGame);
                }
            }else if (event.getCode() == KeyCode.UP) {
                player.up = true;
            } else if (event.getCode() == KeyCode.LEFT) {
                player.left = true;
            } else if (event.getCode() == KeyCode.RIGHT) {
                player.right = true;
            } else if (event.getCode() == KeyCode.DOWN) {
                player.down = true;
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.UP) {
                player.up = false;
            } else if (event.getCode() == KeyCode.LEFT) {
                player.left = false;
            } else if (event.getCode() == KeyCode.RIGHT) {
                player.right = false;
            } else if (event.getCode() == KeyCode.DOWN) {
                player.down = false;
            }
        });

        root.getChildren().addAll(player);
        root.getChildren().add(pauseMain);

        primaryStage.setScene(scene);

        //запуск гланого цикла игры
        startGame();

        primaryStage.show();
    }

    public void startGame() {
        audioPlayer.seek(Duration.ZERO);
        audioPlayer.play();

        interval = 0;

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                interval++;

                if (interval > 80) {
                    if ((int) (Math.random() * 16) == 0) {
                        createBlocks();
                        interval = 0;

                    } else if (interval > 150) {
                        createBlocks();
                        interval = 0;
                    }
                }


                for (Block block : Block.listOfBlocks) {

                    block.update();
                }

                for (Object object : root.getChildren().toArray()) {
                    //если будет ненужный блок
                    if (object instanceof Block && !Block.listOfBlocks.contains(object)) {
                        root.getChildren().remove(object);
                    }
                }

                player.update();

                state.restart();
            }
        };

        timer.start();
    }

    public void stopGame() {
        timer.stop();
        audioPlayer.pause();
    }

    public void continueGame() {
        timer.start();
        audioPlayer.play();
    }

    public void newGame() {
        timer = null;

        for (Object object : root.getChildren().toArray()) {
            if (object instanceof Block) {
                root.getChildren().remove(object);
            }
        }

        createStartingBlocks();

        player.toDefault();


        startGame();
    }

    private void createBlocks() {
        int kol = (int) (Math.random() * 3) + 3;

        ArrayList<Integer> x = new ArrayList<>();

        for (int i = 0; i < SCREEN_WIDTH; i = i + Block.WIDTH){
            x.add(i);
        }

        for (int i = 0; i < kol; i++) {
            int intX = (int) (Math.random() * x.size());

            Block block = new Block(x.get(intX), 0);
            x.remove(intX);
            Block.listOfBlocks.add(block);
            root.getChildren().add(block);
        }

    }

    private void createStartingBlocks() {
        for (int i = 0; i < SCREEN_WIDTH; i = i + 40) {
            Block block = new Block(i, 0);
            Block.listOfBlocks.add(block);
            root.getChildren().add(block);
        }
    }

    public Pane getRoot() {
        return root;
    }
}
