package com.igreja.app.repository;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoBiblia {

    public static Connection conectar(String versao) {

        try {
            String nomeArquivo = versao.toLowerCase() + ".sqlite";

            // 🔥 pega o arquivo de dentro do resources
            InputStream is = ConexaoBiblia.class
                    .getClassLoader()
                    .getResourceAsStream("db/" + nomeArquivo);

            if (is == null) {
                throw new RuntimeException("Banco não encontrado em resources/db");
            }

            // 🔥 copia para arquivo temporário (SQLite PRECISA disso)
            File temp = File.createTempFile("biblia_", ".sqlite");
            temp.deleteOnExit();

            Files.copy(is, temp.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            String url = "jdbc:sqlite:" + temp.getAbsolutePath();

            return DriverManager.getConnection(url);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar banco da Bíblia", e);
        }
    }

}