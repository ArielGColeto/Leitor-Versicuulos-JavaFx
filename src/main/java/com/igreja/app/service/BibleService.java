package com.igreja.app.service;

import com.igreja.app.database.Database;
import com.igreja.app.repository.VersiculoDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/** Serviço da Bíblia com uma conexão aberta por tradução selecionada. */
public class BibleService implements AutoCloseable {

    private Connection conexao;
    private VersiculoDAO dao;
    private String versaoAtual;

    public void load(String versao) {
        if (versao == null || !List.of("nvi", "acf", "naa").contains(versao.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Tradução inválida.");
        }
        versao = versao.toLowerCase(Locale.ROOT);

        try {
            if (versao.equals(versaoAtual) && conexao != null && !conexao.isClosed()) {
                return;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Não foi possível verificar o banco de dados.", ex);
        }

        close();
        Connection nova = Database.conectar(versao);
        conexao = nova;
        dao = new VersiculoDAO(nova);
        versaoAtual = versao;
    }

    public List<String> getLivros() {
        verificarCarregamento();
        return dao.listarLivros();
    }

    public List<String> buscarLivros(String termo) {
        verificarCarregamento();
        return dao.listarLivrosFiltrado(termo);
    }

    public String getVerse(String livro, int cap, int ver) {
        verificarCarregamento();
        return dao.buscarVersiculo(livro, cap, ver);
    }

    private void verificarCarregamento() {
        if (dao == null) {
            throw new IllegalStateException("Nenhuma tradução da Bíblia foi carregada.");
        }
    }

    @Override
    public void close() {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar conexão SQLite: " + ex.getMessage());
            }
        }
        conexao = null;
        dao = null;
        versaoAtual = null;
    }
}
