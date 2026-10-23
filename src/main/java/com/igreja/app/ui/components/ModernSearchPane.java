package com.igreja.app.ui.components;

import com.igreja.app.model.Versiculo;
import com.igreja.app.service.BibleService;
import com.igreja.app.ui.TelaApresentacao;
import com.igreja.app.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Interface principal: busca, fila de slides e prévia do slide selecionado. */
public class ModernSearchPane extends StackPane {

    private static final Pattern INTERVALO = Pattern.compile("^(\\d{1,3})(?:\\s*[-–]\\s*(\\d{1,3}))?$");
    private static final int MAX_VERSICULOS_POR_SLIDE = 30;

    private final ObservableList<Versiculo> fila = FXCollections.observableArrayList();

    public ModernSearchPane(BibleService servico) {
        servico.load("nvi");
        setAlignment(Pos.CENTER);

        VBox container = new VBox(15);
        container.setAlignment(Pos.TOP_CENTER);
        container.setFillWidth(false);
        container.setPadding(new javafx.geometry.Insets(28, 16, 34, 16));

        Label titulo = new Label("Versículos em Apresentação");
        titulo.getStyleClass().add("title");
        Label subtitulo = new Label("Monte sua sequência de slides para projetar");
        subtitulo.getStyleClass().add("subtitle");

        VBox card = new VBox(12);
        card.setPrefWidth(580);
        card.setMaxWidth(580);
        card.setMinWidth(0);
        card.getStyleClass().add("card");

        Label versaoLabel = new Label("Tradução");
        versaoLabel.getStyleClass().add("section-label");

        ToggleGroup grupo = new ToggleGroup();
        HBox versoes = new HBox(10);
        for (String nome : List.of("NVI", "ACF", "NAA")) {
            ToggleButton botao = new ToggleButton(nome);
            botao.getStyleClass().add("pill");
            botao.setToggleGroup(grupo);
            versoes.getChildren().add(botao);
        }
        ((ToggleButton) versoes.getChildren().get(0)).setSelected(true);
        grupo.selectedToggleProperty().addListener((obs, anterior, novo) -> {
            if (novo == null) {
                if (anterior != null) anterior.setSelected(true);
                return;
            }
            servico.load(((ToggleButton) novo).getText().toLowerCase(Locale.ROOT));
        });

        TextField campoLivro = new TextField();
        campoLivro.setPromptText("Livro (ex.: João)");
        TextField cap = new TextField();
        cap.setPromptText("Capítulo (ex.: 3)");
        TextField ver = new TextField();
        ver.setPromptText("Versículo(s) (ex.: 16 ou 16-18)");

        ContextMenu sugestoes = new ContextMenu();
        final boolean[] aplicandoSugestao = {false};
        campoLivro.textProperty().addListener((obs, anterior, texto) -> {
            if (aplicandoSugestao[0] || texto == null || texto.isBlank() || !campoLivro.isFocused()) {
                sugestoes.hide();
                return;
            }
            try {
                List<String> resultados = servico.buscarLivros(texto).stream().limit(6).toList();
                sugestoes.getItems().clear();
                for (String livro : resultados) {
                    MenuItem item = new MenuItem(livro);
                    item.setOnAction(e -> {
                        aplicandoSugestao[0] = true;
                        campoLivro.setText(livro);
                        campoLivro.positionCaret(livro.length());
                        aplicandoSugestao[0] = false;
                        sugestoes.hide();
                        cap.requestFocus();
                    });
                    sugestoes.getItems().add(item);
                }
                if (resultados.isEmpty()) {
                    sugestoes.hide();
                } else {
                    sugestoes.show(campoLivro, Side.BOTTOM, 0, 0);
                }
            } catch (Exception ex) {
                sugestoes.hide();
            }
        });

        Button adicionar = new Button("+ Adicionar à apresentação");
        adicionar.getStyleClass().add("btn-primary");
        adicionar.setMaxWidth(Double.MAX_VALUE);
        adicionar.setDefaultButton(true);
        campoLivro.setOnAction(e -> cap.requestFocus());
        cap.setOnAction(e -> ver.requestFocus());
        ver.setOnAction(e -> adicionar.fire());

        Label filaTitulo = new Label("Fila de apresentação");
        filaTitulo.getStyleClass().add("section-label");
        Label contador = new Label("0 slides");
        contador.getStyleClass().add("hint");
        HBox cabecalhoFila = new HBox(10, filaTitulo, new Region(), contador);
        HBox.setHgrow(cabecalhoFila.getChildren().get(1), Priority.ALWAYS);
        cabecalhoFila.setAlignment(Pos.CENTER_LEFT);

        ListView<Versiculo> lista = new ListView<>(fila);
        lista.setPrefHeight(145);
        lista.setMinHeight(110);
        lista.getStyleClass().add("fila-versiculos");
        lista.setPlaceholder(new Label("Adicione versículos para montar a apresentação."));
        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Versiculo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText((getIndex() + 1) + ".  " + item.getReferencia());
                    setTooltip(new Tooltip(item.getTexto()));
                }
            }
        });

        Button apresentar = new Button("▶ Iniciar apresentação");
        apresentar.getStyleClass().add("btn-secondary");
        apresentar.setMaxWidth(Double.MAX_VALUE);
        apresentar.setDisable(true);

        Button remover = new Button("Remover selecionado");
        remover.getStyleClass().add("btn-outline");
        remover.setDisable(true);
        Button limpar = new Button("Limpar tudo");
        limpar.getStyleClass().add("btn-danger-outline");
        limpar.setDisable(true);
        HBox acoes = new HBox(9, remover, limpar);
        acoes.setAlignment(Pos.CENTER_LEFT);
        remover.setMaxWidth(Double.MAX_VALUE);
        limpar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(remover, Priority.ALWAYS);
        HBox.setHgrow(limpar, Priority.ALWAYS);

        Label instrucao = new Label("Dica: clique em um slide para ver a prévia. Na apresentação use ← → e Esc.");
        instrucao.getStyleClass().add("hint");
        instrucao.setWrapText(true);

        Label aviso = new Label();
        aviso.getStyleClass().add("feedback");
        aviso.setWrapText(true);
        aviso.setVisible(false);
        aviso.setManaged(false);

        VBox previa = new VBox(10);
        previa.setPrefWidth(580);
        previa.setMaxWidth(580);
        previa.getStyleClass().add("result-card");
        Label previaTitulo = new Label("Prévia");
        previaTitulo.getStyleClass().add("section-label");
        Label referenciaPreview = new Label("Selecione ou adicione um versículo");
        referenciaPreview.getStyleClass().add("ref");
        Label textoPreview = new Label("A prévia do slide aparecerá aqui.");
        textoPreview.setWrapText(true);
        textoPreview.getStyleClass().add("preview-text");
        previa.getChildren().addAll(previaTitulo, referenciaPreview, textoPreview);

        Runnable atualizarVisual = () -> {
            contador.setText(fila.size() + (fila.size() == 1 ? " slide" : " slides"));
            apresentar.setDisable(fila.isEmpty());
            limpar.setDisable(fila.isEmpty());
            remover.setDisable(lista.getSelectionModel().getSelectedItem() == null);
            Versiculo selecionado = lista.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                referenciaPreview.setText("Selecione ou adicione um versículo");
                textoPreview.setText("A prévia do slide aparecerá aqui.");
            } else {
                referenciaPreview.setText(selecionado.getReferencia());
                textoPreview.setText(selecionado.getTexto());
            }
        };
        fila.addListener((javafx.collections.ListChangeListener<Versiculo>) c -> atualizarVisual.run());
        lista.getSelectionModel().selectedItemProperty().addListener((obs, old, current) -> atualizarVisual.run());

        adicionar.setOnAction(e -> {
            try {
                aviso.setManaged(false);
                aviso.setVisible(false);
                String digitado = campoLivro.getText().trim();
                if (digitado.isEmpty()) throw new IllegalArgumentException("Informe um livro bíblico.");

                // Permite escrever 'joao' para encontrar 'João', mas evita nomes ambíguos.
                String livro = servico.buscarLivros(digitado).stream()
                        .filter(nome -> StringUtils.normalize(nome)
                                .equals(StringUtils.normalize(digitado)))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado. Escolha uma sugestão da lista."));

                int capitulo;
                try {
                    capitulo = Integer.parseInt(cap.getText().trim());
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Informe um capítulo numérico válido.");
                }
                if (capitulo <= 0) throw new IllegalArgumentException("O capítulo precisa ser maior que zero.");

                Matcher matcher = INTERVALO.matcher(ver.getText().trim());
                if (!matcher.matches()) throw new IllegalArgumentException("Versículo inválido. Use 16 ou 16-18.");
                int inicio = Integer.parseInt(matcher.group(1));
                int fim = matcher.group(2) == null ? inicio : Integer.parseInt(matcher.group(2));
                if (inicio <= 0 || fim < inicio) throw new IllegalArgumentException("Confira o intervalo de versículos.");
                if (fim - inicio + 1 > MAX_VERSICULOS_POR_SLIDE) {
                    throw new IllegalArgumentException("Use até " + MAX_VERSICULOS_POR_SLIDE + " versículos por slide para manter a leitura confortável.");
                }

                StringBuilder texto = new StringBuilder();
                for (int numero = inicio; numero <= fim; numero++) {
                    String passagem = servico.getVerse(livro, capitulo, numero);
                    if (passagem == null || passagem.isBlank() || "Versículo não encontrado".equalsIgnoreCase(passagem.trim())) {
                        throw new IllegalArgumentException("Não encontrei " + livro + " " + capitulo + ":" + numero + ". Verifique a referência.");
                    }
                    if (!texto.isEmpty()) texto.append("\n\n");
                    if (inicio != fim) texto.append(numero).append(". ");
                    texto.append(passagem.trim());
                }

                String versao = ((ToggleButton) grupo.getSelectedToggle()).getText();
                String referencia = livro + " " + capitulo + ":" + inicio
                        + (fim == inicio ? "" : "-" + fim) + " (" + versao + ")";

                Versiculo slide = new Versiculo(referencia, texto.toString());
                fila.add(slide);
                lista.getSelectionModel().selectLast();
                lista.scrollTo(fila.size() - 1);
            } catch (Exception ex) {
                aviso.setText(ex instanceof IllegalArgumentException ? ex.getMessage() : "Erro ao buscar versículo. Confira os dados e o banco SQLite.");
                aviso.setVisible(true);
                aviso.setManaged(true);
                System.err.println("Erro de busca: " + ex.getMessage());
            }
        });

        remover.setOnAction(e -> {
            int indice = lista.getSelectionModel().getSelectedIndex();
            if (indice >= 0) {
                fila.remove(indice);
                if (!fila.isEmpty()) lista.getSelectionModel().select(Math.min(indice, fila.size() - 1));
            }
        });
        limpar.setOnAction(e -> {
            fila.clear();
            lista.getSelectionModel().clearSelection();
        });
        apresentar.setOnAction(e -> {
            if (!fila.isEmpty()) {
                int inicio = lista.getSelectionModel().getSelectedIndex();
                new TelaApresentacao(new ArrayList<>(fila), Math.max(0, inicio));
            }
        });
        lista.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !fila.isEmpty() && lista.getSelectionModel().getSelectedIndex() >= 0) {
                apresentar.fire();
            }
        });

        card.getChildren().addAll(
                versaoLabel, versoes,
                campoLivro, cap, ver, adicionar, aviso,
                cabecalhoFila, lista, acoes, apresentar, instrucao
        );
        container.getChildren().addAll(titulo, subtitulo, card, previa);
        ScrollPane scrollPagina = new ScrollPane(container);
        scrollPagina.setFitToWidth(true);
        scrollPagina.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPagina.getStyleClass().add("page-scroll");
        getChildren().add(scrollPagina);
        atualizarVisual.run();
    }
}
