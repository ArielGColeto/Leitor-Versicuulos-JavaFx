package com.igreja.app.repository;

import org.json.JSONArray;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Carrega JSON da Bíblia.
 */
public class BibleRepository {

    public JSONArray loadBible(String versao) {

        try {

            String caminho = "/bible/" + versao + ".json";

            InputStream is = getClass().getResourceAsStream(caminho);

            if (is == null) {
                throw new RuntimeException("Arquivo não encontrado");
            }

            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // Remove BOM se existir
            if (json.startsWith("\uFEFF")) {
                json = json.substring(1);
            }

            // SEU JSON JÁ É ARRAY
            return new JSONArray(json);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar Bíblia", e);
        }
    }
}