package com.example.lightcontrol_app.Modelo_RecycleView;

import java.sql.Date;

public class OrdenServicioVerInfo {
    private int id;
    private String tipo_de_elemento;
    private String codigo_de_elemento;
    private String elemento_relacionado;
    private String codigo_orden;
    private String description;
    String problema_validado;
    int prioridad_de_ruta;
    Date fecha_a_realizar;
    String cuadrilla;
    String tipo_de_orden;
    String tipo_de_Solucion;
    String clase_de_orden;
    String obra_relacionada;
    String orden_prioridad;
    int idEstado; // Supongo que FK se refiere a una clave foránea, que generalmente es un entero.

    public OrdenServicioVerInfo(int id, String tipo_de_elemento, String codigo_de_elemento, String elemento_relacionado, String codigo_orden, String description, String problema_validado, int prioridad_de_ruta, Date fecha_a_realizar, String cuadrilla, String tipo_de_orden, String tipo_de_Solucion, String clase_de_orden, String obra_relacionada, String orden_prioridad, int idEstado) {
        this.id = id;
        this.tipo_de_elemento = tipo_de_elemento;
        this.codigo_de_elemento = codigo_de_elemento;
        this.elemento_relacionado = elemento_relacionado;
        this.codigo_orden = codigo_orden;
        this.description = description;
        this.problema_validado = problema_validado;
        this.prioridad_de_ruta = prioridad_de_ruta;
        this.fecha_a_realizar = fecha_a_realizar;
        this.cuadrilla = cuadrilla;
        this.tipo_de_orden = tipo_de_orden;
        this.tipo_de_Solucion = tipo_de_Solucion;
        this.clase_de_orden = clase_de_orden;
        this.obra_relacionada = obra_relacionada;
        this.orden_prioridad = orden_prioridad;
       this.idEstado = idEstado;
    }

    public int getId() {
        return id;
    }

    public String getTipo_de_elemento() {
        return tipo_de_elemento;
    }

    public String getCodigo_de_elemento() {
        return codigo_de_elemento;
    }

    public String getElemento_relacionado() {
        return elemento_relacionado;
    }

    public String getCodigo_orden() {
        return codigo_orden;
    }

    public String getDescription() {
        return description;
    }

    public String getProblema_validado() {
        return problema_validado;
    }

    public int getPrioridad_de_ruta() {
        return prioridad_de_ruta;
    }

    public Date getFecha_a_realizar() {
        return fecha_a_realizar;
    }

    public String getCuadrilla() {
        return cuadrilla;
    }

    public String getTipo_de_orden() {
        return tipo_de_orden;
    }

    public String getTipo_de_Solucion() {
        return tipo_de_Solucion;
    }

    public String getClase_de_orden() {
        return clase_de_orden;
    }

    public String getObra_relacionada() {
        return obra_relacionada;
    }

    public String getOrden_prioridad() {
        return orden_prioridad;
    }

    public int getIdEstado() {
        return idEstado;
    }
}
