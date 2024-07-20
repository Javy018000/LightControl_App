package com.example.lightcontrol_app.Model;

import com.example.lightcontrol_app.Modelo_RecycleView.MapaInfraestructura;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class CentroideCalculadora {

    public static GeoPoint calcularCentroide(List<MapaInfraestructura> puntos) {
        if (puntos == null || puntos.isEmpty()) {
            throw new IllegalArgumentException("La lista de puntos no puede estar vacía");
        }

        double sumaLatitud = 0.0;
        double sumaLongitud = 0.0;

        for (MapaInfraestructura punto : puntos) {
            sumaLatitud += punto.getLatitud();
            sumaLongitud += punto.getLongitud();
        }

        double centroLatitud = sumaLatitud / puntos.size();
        double centroLongitud = sumaLongitud / puntos.size();

        return new GeoPoint(centroLatitud, centroLongitud);
    }
}
