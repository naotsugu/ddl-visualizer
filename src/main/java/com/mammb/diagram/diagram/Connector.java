package com.mammb.diagram.diagram;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;

public class Connector extends Group {

    private Diagram from;
    private Diagram to;
    private CubicCurve curve;
    private Arrow arrow;

    public Connector(Diagram fromDiagram, Diagram toDiagram) {

        from = fromDiagram;
        to = toDiagram;
        curve = new CubicCurve();
        curve.setStroke(Color.FORESTGREEN);
        curve.setStrokeWidth(2);
        curve.setStrokeLineCap(StrokeLineCap.ROUND);
        curve.setFill(null);
        arrow = new Arrow();
        getChildren().addAll(curve, arrow);

        calculate();

        from.boundsInParentProperty().addListener(
                (observableValue, oldBounds, bounds) -> calculate());
        to.boundsInParentProperty().addListener(
                (observableValue, oldBounds, bounds) -> calculate());

    }

    protected void calculate() {

        Anchor start = null;
        Anchor end   = null;
        double distance = Double.MAX_VALUE;

        for (Anchor fromAnchor : from.anchors()) {
            for (Anchor toAnchor : to.anchors()) {
                double len = fromAnchor.distanceTo(toAnchor);
                if (len <= distance) {
                    distance = len;
                    start = fromAnchor;
                    end = toAnchor;
                }
            }
        }

        curve.setStartX(start.xProperty().doubleValue());
        curve.setStartY(start.yProperty().doubleValue());
        curve.setControlX1(start.auxiliaryPoint().getX());
        curve.setControlY1(start.auxiliaryPoint().getY());

        curve.setEndX(end.xProperty().doubleValue());
        curve.setEndY(end.yProperty().doubleValue());
        curve.setControlX2(end.auxiliaryPoint().getX());
        curve.setControlY2(end.auxiliaryPoint().getY());

        arrow.calc(curve, 1);
    }

}
