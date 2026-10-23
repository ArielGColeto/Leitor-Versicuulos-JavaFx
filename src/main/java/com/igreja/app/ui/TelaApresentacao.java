
package com.igreja.app.ui;

import com.igreja.app.model.Versiculo;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** Apresentação em tela cheia, com navegação contínua entre os slides. */
public class TelaApresentacao {

    private static final String FUNDO = "#0f172a";
    private final List<Versiculo> slides;
    private int indice;
    private final Stage palco = new Stage();
    private final Label titulo = new Label();
    private final Label texto = new Label();
    private final Label contador = new Label();
    private final ScrollPane areaTexto;

    public TelaApresentacao(String referencia, String conteudo) {
        this(List.of(new Versiculo(referencia, conteudo)), 0);
    }

    public TelaApresentacao(List<Versiculo> versiculos, int slideInicial) {
        if (versiculos == null || versiculos.isEmpty()) {
            throw new IllegalArgumentException("A apresentação não pode estar vazia.");
        }

        slides = new ArrayList<>(versiculos);
        indice = Math.max(0, Math.min(slideInicial, slides.size() - 1));

        Screen telaAlvo = Screen.getScreens().size() > 1
                ? Screen.getScreens().get(1)
                : Screen.getPrimary();

        Rectangle2D limites = telaAlvo.getVisualBounds();

        palco.initStyle(StageStyle.UNDECORATED);
        palco.setTitle("Leitor de Versículos — Apresentação");
        palco.setX(limites.getMinX());
        palco.setY(limites.getMinY());
        palco.setWidth(limites.getWidth());
        palco.setHeight(limites.getHeight());

        // Referência do versículo
        titulo.getStyleClass().add("slide-referencia");
        titulo.setAlignment(Pos.CENTER);
        titulo.setTextAlignment(TextAlignment.CENTER);
        titulo.setWrapText(true);
        titulo.setMaxWidth(Double.MAX_VALUE);
        titulo.setStyle(
                "-fx-font-size: 26px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #f59e0b; " +
                        "-fx-padding: 5 0 14 0;"
        );

        // Texto centralizado
        texto.getStyleClass().add("slide-texto");
        texto.setAlignment(Pos.CENTER);
        texto.setTextAlignment(TextAlignment.CENTER);
        texto.setWrapText(true);
        texto.setMaxWidth(Double.MAX_VALUE);

        StackPane painelTexto = new StackPane(texto);
        painelTexto.setAlignment(Pos.CENTER);
        painelTexto.setPadding(new Insets(20, 35, 20, 35));
        painelTexto.setStyle("-fx-background-color: " + FUNDO + ";");

        areaTexto = new ScrollPane(painelTexto);
        areaTexto.getStyleClass().add("slide-scroll");
        areaTexto.setFitToWidth(true);
        areaTexto.setPannable(true);
        areaTexto.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        areaTexto.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        areaTexto.setStyle(
                "-fx-background: " + FUNDO + "; " +
                        "-fx-background-color: " + FUNDO + "; " +
                        "-fx-border-color: transparent;"
        );

        // Centraliza verticalmente textos curtos.
        // Textos grandes continuam podendo ser rolados.
        painelTexto.minHeightProperty().bind(
                areaTexto.heightProperty().subtract(20)
        );

        VBox.setVgrow(areaTexto, Priority.ALWAYS);

        // Controles
        Button anterior = botao("◀ Anterior", "#334155");
        anterior.setOnAction(e -> navegar(-1));

        Button proximo = botao("Próximo ▶", "#334155");
        proximo.setOnAction(e -> navegar(1));

        Button sair = botao("Sair (Esc)", "#7f1d1d");
        sair.setOnAction(e -> palco.close());

        contador.setAlignment(Pos.CENTER);
        contador.setStyle(
                "-fx-text-fill: #cbd5e1; " +
                        "-fx-font-size: 14px;"
        );

        Region espacoEsquerdo = new Region();
        Region espacoDireito = new Region();

        HBox.setHgrow(espacoEsquerdo, Priority.ALWAYS);
        HBox.setHgrow(espacoDireito, Priority.ALWAYS);

        HBox rodape = new HBox(
                12,
                anterior,
                proximo,
                espacoEsquerdo,
                contador,
                espacoDireito,
                sair
        );

        rodape.setAlignment(Pos.CENTER);
        rodape.setPadding(new Insets(16, 0, 4, 0));

        // Layout principal do slide
        VBox layout = new VBox(8, titulo, areaTexto, rodape);
        layout.getStyleClass().add("slide-root");
        layout.setPadding(new Insets(30, 50, 20, 50));
        layout.setStyle("-fx-background-color: " + FUNDO + ";");

        Scene cena = new Scene(
                layout,
                Math.max(limites.getWidth(), 700),
                Math.max(limites.getHeight(), 460)
        );

        // Carrega o CSS externo, se disponível.
        URL css = getClass().getResource("/styles/main.css");

        if (css != null) {
            cena.getStylesheets().add(css.toExternalForm());
        } else {
            System.err.println(
                    "Aviso: /styles/main.css não encontrado. " +
                            "Usando o estilo embutido da apresentação."
            );
        }

        // Navegação por teclado
        cena.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case RIGHT, DOWN, PAGE_DOWN, SPACE, ENTER -> navegar(1);
                case LEFT, UP, PAGE_UP, BACK_SPACE -> navegar(-1);
                case HOME -> mostrar(0);
                case END -> mostrar(slides.size() - 1);
                case ESCAPE -> palco.close();
                default -> {
                    return;
                }
            }
            e.consume();
        });

        palco.setScene(cena);
        palco.setFullScreenExitHint("");

        mostrar(indice);

        palco.show();
        palco.setFullScreen(true);

        // Corrige o fundo interno do ScrollPane,
        // evitando a área branca da apresentação.
        Platform.runLater(() -> {
            layout.applyCss();

            var viewport = areaTexto.lookup(".viewport");

            if (viewport != null) {
                viewport.setStyle(
                        "-fx-background-color: " + FUNDO + ";"
                );
            }

            layout.requestFocus();
        });
    }

    private Button botao(String rotulo, String cor) {
        Button botao = new Button(rotulo);

        botao.setStyle(
                "-fx-background-color: " + cor + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 13px; " +
                        "-fx-padding: 11 19; " +
                        "-fx-background-radius: 9; " +
                        "-fx-cursor: hand;"
        );

        return botao;
    }

    // Navegação circular entre os slides
    private void navegar(int direcao) {
        mostrar(Math.floorMod(indice + direcao, slides.size()));
    }

    private void mostrar(int novoIndice) {
        indice = novoIndice;

        Versiculo atual = slides.get(indice);

        titulo.setText(atual.getReferencia().toUpperCase());
        texto.setText(atual.getTexto());

        contador.setText(
                (indice + 1) + " / " + slides.size()
        );

        areaTexto.setVvalue(0);

        // Ajuste automático da fonte conforme o texto
        int tamanho = atual.getTexto().length();

        int fonte = tamanho > 1200 ? 25
                : tamanho > 700 ? 30
                  : tamanho > 350 ? 38
                    : 50;

        texto.setStyle(
                "-fx-font-size: " + fonte + "px; " +
                        "-fx-text-fill: #f8fafc; " +
                        "-fx-line-spacing: 9px;"
        );
    }
}
