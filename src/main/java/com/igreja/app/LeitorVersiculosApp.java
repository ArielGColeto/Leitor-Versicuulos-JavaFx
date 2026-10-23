package com.igreja.app;

import com.igreja.app.ui.TelaPrincipal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

/**
 * Classe principal da aplicação.
 * Toda aplicação JavaFX começa aqui.
 */
public class LeitorVersiculosApp extends Application {

    /**
     * Método sobrescrito (override).
     * O JavaFX chama automaticamente quando o app inicia.
     */
    @Override
    public void start(Stage palco) {

        // Cria a tela principal
        TelaPrincipal tela = new TelaPrincipal();

        // Cria a cena (conteúdo visual)
        Scene cena = new Scene(tela, 1200, 800);

        // Aplica CSS
        cena.getStylesheets().add(
                getClass().getResource("/styles/main.css").toExternalForm()
        );

        // Define ícone da janela
        palco.getIcons().add(
                new Image(getClass().getResourceAsStream("/icon.png"))
        );

        palco.setTitle("Leitor de Versiculos");
        palco.setScene(cena);
        palco.show();
    }

    /**
     * Método main padrão do Java.
     * Chama o launch() que inicia o JavaFX.
     */
    public static void main(String[] args) {
        System.out.println("Tradução"); launch(args);
    }
}