package com.igreja.app.model;

public class  Versiculo {
    private String referencia;
    private String texto;

    public Versiculo(String referencia, String texto) {
        this.referencia = referencia;
        this.texto = texto;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getTexto() {
        return texto;
    }
}
