package com.mammb.diagram.diagram;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BasicDiagram extends Diagram {

    private String name;
    private VBox primary;
    private VBox secondary;

    private BasicDiagram(String name, VBox node, VBox primary, VBox secondary) {
        super(node);
        this.name = name;
        this.primary = primary;
        this.secondary = secondary;
    }

    public static BasicDiagram of(String name) {

        VBox container = new VBox();
        container.setStyle(
                "-fx-border-color: #6d6d6d;" +
                "-fx-border-width: 2;");

        HBox header = createRow(name);
        header.setStyle(
                "-fx-background-color: #424242;" +
                "-fx-border-width: 0 0 1 0;" +
                "-fx-border-color: #6d6d6d;");
        container.getChildren().add(header);

        VBox primary = new VBox();
        container.getChildren().add(primary);
        primary.setStyle(
                "-fx-border-width: 0 0 1 0;" +
                "-fx-border-color: #6d6d6d;");

        VBox secondary = new VBox();
        container.getChildren().add(secondary);

        return new BasicDiagram(name, container, primary, secondary);
    }

    public BasicDiagram addPrimaryRow(String name, String additional) {
        HBox hbox = createRow(name, additional);
        primary.getChildren().add(hbox);
        return this;
    }

    public BasicDiagram addSecondaryRow(String name, String additional) {
        HBox hbox = createRow(name, additional);
        secondary.getChildren().add(hbox);
        return this;
    }

    public void removeRows() {
        primary.getChildren().clear();
        secondary.getChildren().clear();
    }

    public String getName() {
        return name;
    }

    private static HBox createRow(String name) {

        Text text = new Text(name);
        text.setFont(new Font(16));
        text.setFill(Color.web("#a9b7c6"));

        HBox hbox = new HBox(5);
        hbox.setPadding(new Insets(4, 10, 4, 10));
        hbox.setStyle("-fx-background-color: #2e3032;");
        hbox.getChildren().add(text);

        return hbox;
    }

    private static HBox createRow(String name, String additional) {

        Text text = new Text(name);
        text.setFont(new Font(16));
        text.setFill(Color.web("#a9b7c6"));

        Region spacer = new Region();
        spacer.setPrefWidth(10);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text text2 = new Text(additional);
        text2.setFont(new Font(16));
        text2.setFill(Color.web("#626262"));

        HBox hbox = new HBox(5);
        hbox.setPadding(new Insets(4, 10, 4, 10));
        hbox.setStyle("-fx-background-color: #2e3032;");

        hbox.getChildren().addAll(text, spacer, text2);

        return hbox;
    }

}
