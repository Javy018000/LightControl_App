package com.example.lightcontrol_app.Modelo_RecycleView;

public class MapaInfraestructura {
    private double latitud;
    private double longitud;
    private String barrio;
    private String direccion;

    public MapaInfraestructura(double latitud, double longitud, String barrio, String direccion) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.barrio = barrio;
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getBarrio() {
        return barrio;
    }

    public String getDireccion() {
        return direccion;
    }
}
