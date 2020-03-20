package com.mammb.diagram.diagram;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.stream.Collectors;

public class DiagramPane extends Pane {

    public DiagramPane() {
        super();
        setStyle("-fx-background-color: #272822;");
        enableLiningUp();
    }


    private void enableLiningUp() {
        ListChangeListener<Node> listener = change -> {
            while (change.next()) {
                List<Diagram> diagrams = change.getAddedSubList().stream()
                        .filter(node -> node instanceof Diagram)
                        .map(node -> (Diagram) node)
                        .collect(Collectors.toList());
                diagrams.forEach(node -> node.relocate(getBoundsInParent().getWidth(), 0));
                Platform.runLater(() -> {
                    diagrams.forEach(this::locate);
                });
            }
        };
        getChildren().addListener(listener);
    }


    public void liningUp() {
        liningUp(getBoundsInLocal(), getChildren());
    }

    private void liningUp(Bounds canvas, List<Node> nodes) {
        float margin = 50;
        double rowHeight = 0;
        double x = margin, y = margin;
        for (Node node : nodes) {
            if (!(node instanceof Diagram)) {
                continue;
            }
            if (x + node.getBoundsInParent().getWidth() > canvas.getWidth()) {
                x = margin;
                y += rowHeight + margin + margin;
            }
            node.relocate(x, y);
            if (node.getBoundsInParent().getHeight() > rowHeight) {
                rowHeight = node.getBoundsInParent().getHeight();
            }

            x += node.getBoundsInParent().getWidth() + margin + margin;
        }
    }

    public void relocate() {
        getDiagrams().forEach(d -> d.relocate(getBoundsInParent().getWidth(), 0));
        getDiagrams().forEach(this::locate);
    }


    private void locate(Diagram d) {

        int margin = 50;

        double maxWidth  = getParent().getBoundsInParent().getWidth()
                - d.getBoundsInParent().getWidth() - margin - margin;
        double maxHeight = getParent().getBoundsInParent().getHeight()
                - d.getBoundsInParent().getHeight() - margin;

        List<Diagram> existing = getDiagrams();

        if (existing.size() <= 1) {
            d.relocate(margin, margin);
        }

        for (int y = margin; y < maxHeight; y += margin) {
            for (int x = margin; x < maxWidth; x += margin) {

                boolean cross = false;

                for (Diagram e : existing) {
                    if (e.equals(d)) {
                        continue;
                    }
                    d.relocate(x, y);
                    if (e.getBoundsInParent().intersects(d.getBoundsInParent())) {
                        cross = true;
                    }
                }

                if (!cross) {
                    int dx = (x != margin) ? x + margin : x;
                    int dy = (y != margin) ? y + margin : y;
                    d.relocate(dx, dy);
                    return;
                }
            }
        }
    }

    private List<Diagram> getDiagrams() {
        return getChildren().stream()
                .filter(node -> node instanceof Diagram)
                .map(node -> (Diagram) node)
                .collect(Collectors.toList());
    }
}
