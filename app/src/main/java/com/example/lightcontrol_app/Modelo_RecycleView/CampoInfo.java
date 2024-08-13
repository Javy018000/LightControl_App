package com.example.lightcontrol_app.Modelo_RecycleView;

import java.sql.Date;

public class CampoInfo {
    private String titulo;
    private String valor;

    public CampoInfo(String titulo, String valor) {
        this.titulo = titulo;
        this.valor = valor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getValor() {
        return valor;
    }
}
