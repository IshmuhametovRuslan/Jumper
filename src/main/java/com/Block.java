package com;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

public class Block extends Surface {

    private boolean selfRemoving = false;

    public static ArrayList<Block> listOfBlocks = new ArrayList<>();

    private final static int SPEED_TO_DOWN = 2;
    public static final int WIDTH = 40, HEIGHT = 40;
    public static final int TIME_REMOVING = 2000;

    public Block(double x, double y) {
        super(new Rectangle(WIDTH, HEIGHT, Color.GREEN));

        this.setTranslateX(x);
        this.setTranslateY(y);
    }


    public boolean isSelfRemoving() {
        return selfRemoving;
    }

    public void setSelfRemoving(boolean selfRemoving) {
        this.selfRemoving = selfRemoving;
    }

    public void update() {
        setTranslateY(getTranslateY() + SPEED_TO_DOWN);

        if (getTranslateY() > Main.SCREEN_HEIGHT) {
            listOfBlocks.remove(this);
        }
    }
}
