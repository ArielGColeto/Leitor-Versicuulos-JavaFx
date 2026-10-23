package com.igreja.app.ui.components;

import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ReaderPane extends VBox {

    private final Label text = new Label("Selecione um versículo");

    public ReaderPane() {

        setSpacing(20);
        setStyle("-fx-padding: 30; -fx-background-color: #0b1220;");

        text.setStyle("""
            -fx-text-fill: white;
            -fx-font-size: 26px;
            -fx-wrap-text: true;
        """);

        getChildren().add(text);
    }

    public void showVerse(String value) {

        FadeTransition ft = new FadeTransition(Duration.millis(200), text);
        ft.setFromValue(0);
        ft.setToValue(1);

        text.setText(value);

        ft.play();
    }
}