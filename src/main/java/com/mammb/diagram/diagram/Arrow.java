package com.mammb.diagram.diagram;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

public class Arrow extends Polygon {

    private static final double[] arrowPoints = new double[] { 0, 0, 5, 10, -5, 10 };

    private Rotate rz;

    public Arrow() {
        super(arrowPoints);
        setFill(Color.FORESTGREEN);
        rz = new Rotate();
        rz.setAxis(Rotate.Z_AXIS);
        getTransforms().addAll(rz);
    }

    public void calc(CubicCurve curve, float t) {

        setTranslateX(curve.getEndX());
        setTranslateY(curve.getEndY());

        double size = Math.max(
                curve.getBoundsInLocal().getWidth(),
                curve.getBoundsInLocal().getHeight());
        double scale = size / 4d;
        Point2D tan = evalDt(curve, t).normalize().multiply(scale);
        double angle = Math.atan2(tan.getY(), tan.getX());
        double offset = (t > 0.5) ? +90 : -90;
        rz.setAngle(Math.toDegrees(angle) + offset);
    }

    private Point2D evalDt(CubicCurve c, float t){
        return new Point2D(-3 * Math.pow(1 - t, 2) * c.getStartX() +
                3 * (Math.pow(1 - t, 2) - 2 * t * (1 - t)) * c.getControlX1() +
                3 * ((1 - t) * 2 * t - t * t) * c.getControlX2() +
                3 * Math.pow(t, 2) * c.getEndX(),
                -3 * Math.pow(1 - t, 2) * c.getStartY() +
                        3 * (Math.pow(1 - t, 2) - 2 * t * (1 - t)) * c.getControlY1() +
                        3 * ((1 - t) * 2 * t - t * t) * c.getControlY2() +
                        3 * Math.pow(t, 2) * c.getEndY());
    }

}
