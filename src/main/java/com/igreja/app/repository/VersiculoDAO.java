package com.igreja.app.repository;

import com.igreja.app.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VersiculoDAO {

    private final Connection conn;

    public VersiculoDAO(Connection conn) {
        this.conn = conn;
    }

    // 🔥 LISTA NORMAL
    public List<String> listarLivros() {
        List<String> livros = new ArrayList<>();

        String sql = "SELECT name FROM book";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                livros.add(rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return livros;
    }

    // 🔥 BUSCA INTELIGENTE
    public List<String> listarLivrosFiltrado(String termo) {

        List<String> livros = new ArrayList<>();

        String sql = "SELECT name FROM book";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            String busca = StringUtils.normalize(termo);

            while (rs.next()) {
                String nome = rs.getString("name");

                if (StringUtils.normalize(nome).contains(busca)) {
                    livros.add(nome);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return livros;
    }

    // 🔥 BUSCAR VERSÍCULO
    public String buscarVersiculo(String livro, int cap, int ver) {

        String sql = """
            SELECT v.text
            FROM verse v
            JOIN book b ON v.book_id = b.id
            WHERE b.name = ?
            AND v.chapter = ?
            AND v.verse = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro);
            stmt.setInt(2, cap);
            stmt.setInt(3, ver);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("text");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Versículo não encontrado";
    }
}