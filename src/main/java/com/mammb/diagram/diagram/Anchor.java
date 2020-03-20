package com.mammb.diagram.diagram;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

public abstract class Anchor {

    private static final int GAP = 50;

    protected ReadOnlyDoubleWrapper x = new ReadOnlyDoubleWrapper();
    protected ReadOnlyDoubleWrapper y = new ReadOnlyDoubleWrapper();

    protected Anchor(Node node) {
        calcCenter(node.getBoundsInParent());
        node.boundsInParentProperty().addListener(
                (observableValue, oldBounds, bounds) -> calcCenter(bounds));
    }

    public static Anchor topOf(Node node) {
        return new TopAnchor(node);
    }
    public static Anchor rightOf(Node node) {
        return new RightAnchor(node);
    }
    public static Anchor bottomOf(Node node) {
        return new BottomAnchor(node);
    }
    public static Anchor leftOf(Node node) {
        return new LeftAnchor(node);
    }

    public double distanceTo(Anchor other) {
        double dx = other.x.getReadOnlyProperty().doubleValue() -
                this.x.getReadOnlyProperty().doubleValue();
        double dy = other.y.getReadOnlyProperty().doubleValue() -
                this.y.getReadOnlyProperty().doubleValue();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public ReadOnlyDoubleProperty xProperty() {
        return x.getReadOnlyProperty();
    }

    public ReadOnlyDoubleProperty yProperty() {
        return y.getReadOnlyProperty();
    }

    protected abstract void calcCenter(Bounds bounds);

    public abstract Point2D auxiliaryPoint();


    static class TopAnchor extends Anchor {
        TopAnchor(Node node) {
            super(node);
        }

        protected void calcCenter(Bounds bounds) {
            x.set(bounds.getCenterX());
            y.set(bounds.getMinY());
        }
        public Point2D auxiliaryPoint() {
            return new Point2D(
                    x.getReadOnlyProperty().doubleValue(),
                    y.getReadOnlyProperty().doubleValue() - GAP);
        }
    }

    static class RightAnchor extends Anchor {
        RightAnchor(Node node) {
            super(node);
        }
        protected void calcCenter(Bounds bounds) {
            x.set(bounds.getMaxX());
            y.set(bounds.getCenterY());
        }
        public Point2D auxiliaryPoint() {
            return new Point2D(
                    x.getReadOnlyProperty().doubleValue() + GAP,
                    y.getReadOnlyProperty().doubleValue());
        }
    }

    static class BottomAnchor extends Anchor {
        BottomAnchor(Node node) {
            super(node);
        }
        protected void calcCenter(Bounds bounds) {
            x.set(bounds.getCenterX());
            y.set(bounds.getMaxY());
        }
        public Point2D auxiliaryPoint() {
            return new Point2D(
                    x.getReadOnlyProperty().doubleValue(),
                    y.getReadOnlyProperty().doubleValue() + GAP);
        }
    }

    static class LeftAnchor extends Anchor {
        LeftAnchor(Node node) {
            super(node);
        }
        protected void calcCenter(Bounds bounds) {
            x.set(bounds.getMinX());
            y.set(bounds.getCenterY());
        }
        public Point2D auxiliaryPoint() {
            return new Point2D(
                    x.getReadOnlyProperty().doubleValue() - GAP,
                    y.getReadOnlyProperty().doubleValue());
        }
    }
}