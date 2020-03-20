package com.mammb.diagram;

import com.mammb.diagram.diagram.DiagramPane;
import com.mammb.diagram.editor.Editor;
import com.mammb.diagram.editor.TableDiagrams;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App extends Application {

    private static final KeyCombination kcPreview = new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_DOWN);
    private static final KeyCombination kcRedraw  = new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN);

    @Override
    public void start(Stage stage) {

        DiagramPane pane = new DiagramPane();
        TableDiagrams tableDiagrams = new TableDiagrams(pane.getChildren());
        Editor editor = new Editor(tableDiagrams);

        SplitPane splitPane = new SplitPane(editor, pane);
        splitPane.setPrefSize(1400, 700);
        splitPane.setDividerPositions(0.4, 0.6);
        splitPane.setOnKeyPressed(event -> {
            if (kcPreview.match(event)) editor.onPreview();
            if (kcRedraw.match(event)) pane.relocate();
        });


        stage.setScene(new Scene(splitPane));
        stage.show();

        editor.setText(exampleText());
    }


    private String exampleText() {
        try {
            return Files.readString(Paths.get(
                    getClass().getClassLoader().getResource("example.sql").toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        launch();
    }

}
