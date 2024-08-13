package com.example.lightcontrol_app.Modelo_RecycleView;

public class Trabajos {
    private String trabajoNombre;
    private boolean esSeleccionado;

    public Trabajos(String trabajoNombre, boolean esSeleccionado) {
        this.trabajoNombre = trabajoNombre;
        this.esSeleccionado = esSeleccionado;
    }

    public String getTrabajoNombre() {
        return trabajoNombre;
    }

    public void setTrabajoNombre(String trabajoNombre) {
        this.trabajoNombre = trabajoNombre;
    }

    public boolean isEsSeleccionado() {
        return esSeleccionado;
    }

    public void setEsSeleccionado(boolean esSeleccionado) {
        this.esSeleccionado = esSeleccionado;
    }
}
