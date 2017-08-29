package com;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

abstract class Surface extends Pane {
    Shape shape;

    Surface(Shape shape) {
       this.shape = shape;
       getChildren().add(shape);
       setLayoutX(shape.getLayoutX());
       setLayoutY(shape.getLayoutY());
    }

    public void update() {

    }

}