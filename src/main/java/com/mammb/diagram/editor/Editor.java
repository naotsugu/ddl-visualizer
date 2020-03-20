package com.mammb.diagram.editor;

import com.mammb.diagram.parser.Parser;
import com.mammb.diagram.parser.Table;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Editor extends StackPane {

    private WebView editor;
    private TableDiagrams tableDiagrams;
    private final CountDownLatch latch = new CountDownLatch(1);

    public Editor(TableDiagrams tableDiagrams) {

        this.tableDiagrams = tableDiagrams;

        editor = new WebView();
        WebEngine engine = editor.getEngine();
        engine.setJavaScriptEnabled(true);
        engine.userAgentProperty().setValue(
                engine.userAgentProperty().get() + " Chrome/17"); // workaround text copy.
        engine.load(getClass().getClassLoader()
                .getResource("ace.html").toExternalForm());

        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                latch.countDown();
            }
        });

        getChildren().add(editor);

    }

    public void setText(String text) {

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                latch.await();
                Platform.runLater(() -> {
                    JSObject ace = (JSObject) editor.getEngine().executeScript("editor");
                    ace.call("setValue", text);
                    onPreview();
                });
                return null;
            }
        };
        new Thread(task).start();
    }


    public void onPreview() {

        JSObject ace = (JSObject) editor.getEngine().executeScript("editor");
        String text = (String) ace.call("getValue");

        Parser parser = new Parser();
        List<Table> tables = parser.parse(text);
        tableDiagrams.putAll(tables);

    }

}
