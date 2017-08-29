package com;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Hero extends Surface {

    private final static int SPEED = 5;
    private final static int HIGH_JUMP_VALUE = 15;
    private final static int SHORT_JUMP_VALUE = HIGH_JUMP_VALUE / 2;
    private final static double GRAVITY = 0.5;

    public final static int LIVE_COUNT = 3;

    private final static int WIDTH = 20, HEIGHT = 20;

    private int live = LIVE_COUNT;
    private int score = 0;

    boolean right;
    boolean left;
    boolean up;
    boolean down;

    private boolean onGround = false;

    private double yVel = 0;

    Hero() {
        super(new Rectangle(WIDTH, HEIGHT, Color.RED));
        moveToStartingPosition();
    }

    private Rectangle getRect(){

        return (Rectangle) shape;
    }

    private double getRectWidth() {

        return getRect().getWidth();
    }

    private double getRectHeight() {

        return getRect().getHeight();
    }

    private void jump(int power) {
        if (isOnGround()) {
            yVel = -power;

            onGround = false;
        }
    }

    public void moveToStartingPosition() {
        setTranslateY(-HEIGHT);
        setTranslateX(Main.SCREEN_WIDTH / 2 - WIDTH / 2);
    }

    private void moveX(int kol) {
        boolean toRight = kol > 0;
        for (int i = 0; i < Math.abs(kol); i++) {
            if (toRight) {
                setTranslateX(getTranslateX() + 1);
            } else {
                setTranslateX(getTranslateX() - 1);
            }

            for (Block block : Block.listOfBlocks) {
                if (getBoundsInParent().intersects(block.getBoundsInParent())) {

                    if (toRight & getTranslateX() + getRectWidth() == block.getTranslateX()) {
                        setTranslateX(getTranslateX() - 1);
                        return;
                    } else if (!toRight & getTranslateX() == block.getTranslateX() + block.getWidth()) {
                        setTranslateX(getTranslateX() + 1);
                        return;
                    }
                }
            }
        }
    }

    private void moveY(int yV) {
        boolean toDown = yV > 0;

        for (int i = 0; i < Math.abs(yV); i++) {

            for (Block block : Block.listOfBlocks) {
                if (getBoundsInParent().intersects(block.getBoundsInParent())) {

                    //если наступил на блок
                    if (getTranslateY() + getRectHeight() == block.getTranslateY()) {
                        setTranslateY(getTranslateY() - 1);
                        onGround = true;
                        //если игрок ещё ни разу не наступил на этот блок
                        if (!block.isSelfRemoving()) {
                            destroyBlock(block);
                            score++;
                        }
                        return;
                    //если врезался под блок
                    } else if (getTranslateY() <= block.getTranslateY() + block.getHeight() &
                            getTranslateY() + getRectHeight() > block.getTranslateY()) {
                        setTranslateY(getTranslateY() + 5);
                        yVel = 5;
                        onGround = false;
                        return;
                    }
                }
            }
            //если никуда не врезался
            if (toDown) {
                setTranslateY(getTranslateY() + 1);
            } else {
                setTranslateY(getTranslateY() - 1);
            }
        }
    }

    //уничтожает блок под героем с эффектами анимации
    private void destroyBlock(Block block) {
        new Thread(() -> {
            block.setSelfRemoving(true);

            FadeTransition fadeTransition = new FadeTransition(new Duration(Block.TIME_REMOVING),
                    block);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);

            FillTransition fillTransition = new FillTransition(new Duration(Block.TIME_REMOVING),
                    block.shape);
            fillTransition.setFromValue(Color.GREEN);
            fillTransition.setToValue(Color.RED);

            fadeTransition.play();
            fadeTransition.setOnFinished((event) -> Block.listOfBlocks.remove(block));
            fillTransition.play();
        }).start();
    }

    public void update() {

        onGround = false;

        if (getTranslateY() + getRectHeight() >= Main.SCREEN_HEIGHT) {
            yVel = - HIGH_JUMP_VALUE * 1.5;
            live--;
        }

        moveY((int) yVel);

        if (isOnGround()) {
            yVel = 2;
        } else {
            yVel += GRAVITY;
        }

        if (up) {
            if (isOnGround()) {
                jump(HIGH_JUMP_VALUE);
                onGround = false;
            }
        }
        if (left) {
            if (getTranslateX() > 0)
                moveX(-SPEED);
        }
        if (right) {
            if (getTranslateX() + getRectWidth() < Main.SCREEN_WIDTH)
                moveX(SPEED);
        }
        if (down) {
            jump(SHORT_JUMP_VALUE);
            onGround = false;
        }

        if (live <= 0) {
            System.out.println("GAME OVER");
        }

    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isOnGround() {

        return onGround;
    }

}
