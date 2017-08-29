package com;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class PauseMain extends VBox {

    private PauseCommands textOfButton1, textOfButton2;
    private String text;

    private Main main;

    private Label textLabel;

    private Button btn1, btn2;

    public PauseMain(Main main) {
        this.main = main;

        initialite();
    }

    private void initialite() {
        setVisible(false);

        setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        setAlignment(Pos.CENTER);

        setPrefSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

        textLabel = new Label();
        textLabel.setTextFill(Color.DARKSEAGREEN);
        textLabel.setFont(new Font(25));
        textLabel.setEffect(new DropShadow());

        //добавлю в hBox две кнопки по горизонтали на растояние 20 пиксель друг от друга
        HBox hBox = new HBox(20);
        hBox.setAlignment(Pos.CENTER);
        btn1 = new Button();
        btn2 = new Button();
        //преобразовать кнопки в игровой стиль
        transformButtonToGameStyle(btn1);
        transformButtonToGameStyle(btn2);
        hBox.getChildren().addAll(btn1, btn2);

        getChildren().addAll(textLabel, hBox);
    }

    public void start(String text, PauseCommands command1, PauseCommands command2) {
        textLabel.setText(text);
        btn1.setText(command1.toString());
        btn2.setText(command2.toString());

        main.stopGame();

        main.getRoot().getChildren().remove(this);
        main.getRoot().getChildren().add(this);

        setOpacity(0);
        setVisible(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), this);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(0.5);
        fadeTransition.play();
    }

    private void transformButtonToGameStyle(Button btn) {
        btn.setFont(new Font(25));
        btn.setTextFill(Color.CHARTREUSE);
        btn.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(100), Insets.EMPTY)));
        btn.setOpacity(0.5);
        btn.setOnMouseEntered(event -> {
            FadeTransition ft = new FadeTransition(Duration.seconds(1), btn);
            ft.setFromValue(0.5);
            ft.setToValue(1);
            ft.play();
        });
        btn.setOnMouseExited(event -> {
            FadeTransition ft = new FadeTransition(Duration.seconds(1), btn);
            ft.setFromValue(1);
            ft.setToValue(0.5);
            ft.play();
        });

        //добавить слушателя
        addListenerForPauseMain(btn);
    }

    private void addListenerForPauseMain(Button btn) {
        btn.setOnAction((event -> {
            if (btn.getText().equals(PauseCommands.exitGame.toString())) {
                System.exit(0);
            } else if (btn.getText().equals(PauseCommands.newGame.toString())) {
                main.newGame();
            } else if (btn.getText().equals(PauseCommands.continueGame.toString())) {
                main.continueGame();
            }
            setVisible(false);
        }));
    }
}
