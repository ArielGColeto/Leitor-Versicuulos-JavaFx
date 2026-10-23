package com.igreja.app.ui;

import com.igreja.app.service.BibleService;
import com.igreja.app.ui.components.ModernSearchPane;
import javafx.scene.layout.StackPane;

/**
 * Tela principal do sistema.
 * Apenas carrega os componentes.
 */
public class TelaPrincipal extends StackPane {

    public TelaPrincipal() {

        // Serviço que contém a lógica da Bíblia
        BibleService servico = new BibleService();

        // Carrega versão padrão
        servico.load("nvi");

        // Adiciona interface principal
        getChildren().add(new ModernSearchPane(servico));
    }
}