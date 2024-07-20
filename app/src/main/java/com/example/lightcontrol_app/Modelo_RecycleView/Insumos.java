package com.example.lightcontrol_app.Modelo_RecycleView;

public class Insumos {
    private int id;
    private String nombreElemento;
    private int cantidad;
    private String estado;
    private String descripcion;

    // Constructor para inicializar las variables
    public Insumos(int id, String nombreElemento, int cantidad, String estado, String descripcion) {
        this.id = id;
        this.nombreElemento = nombreElemento;
        this.cantidad = cantidad;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getNombreElemento() {
        return nombreElemento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Insumos{" +
                "id=" + id +
                ", nombreElemento='" + nombreElemento + '\'' +
                ", cantidad=" + cantidad +
                ", estado='" + estado + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
