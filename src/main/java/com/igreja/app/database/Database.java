package com.igreja.app.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    public static Connection conectar(String versao) {

        try {
            String caminho = "db/" + versao + ".sqlite";

            String url = "jdbc:sqlite:" + caminho;

            return DriverManager.getConnection(url);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar com banco");
        }
    }
}