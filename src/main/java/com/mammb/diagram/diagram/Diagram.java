package com.mammb.diagram.diagram;

import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

public class Diagram extends Pane {

    private List<Anchor> anchors;

    public Diagram(Node node) {
        super(node);
        this.anchors = Arrays.asList(
                Anchor.topOf(this),
                Anchor.rightOf(this),
                Anchor.bottomOf(this),
                Anchor.leftOf(this));
        enableDrag();
    }

    private void enableDrag() {

        final Delta dragDelta = new Delta();

        setOnMousePressed(e -> {
            dragDelta.x = getBoundsInParent().getMinX() - e.getScreenX();
            dragDelta.y = getBoundsInParent().getMinY() - e.getScreenY();
            getScene().setCursor(Cursor.MOVE);
        });

        setOnMouseDragged(e -> {
            Bounds bounds = getBoundsInParent();
            double newX = e.getScreenX() + dragDelta.x;
            if (newX > 0 && newX + bounds.getWidth() < getParent().getBoundsInLocal().getWidth()) {
                setLayoutX(newX);
            }
            double newY = e.getScreenY() + dragDelta.y;
            if (newY > 0 && newY + bounds.getHeight() < getParent().getBoundsInLocal().getHeight()) {
                setLayoutY(newY);
            }
        });

        setOnMouseReleased(e -> getScene().setCursor(Cursor.HAND));

        setOnMouseEntered(e -> {
            if (!e.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.HAND);
            }
        });

        setOnMouseExited(e -> {
            if (!e.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    public List<Anchor> anchors() {
        return anchors;
    }

    static class Delta { double x, y; }

}
