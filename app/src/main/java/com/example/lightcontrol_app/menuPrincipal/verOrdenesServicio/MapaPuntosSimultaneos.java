package com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;
import com.example.lightcontrol_app.Model.CentroideCalculadora;
import com.example.lightcontrol_app.Modelo_RecycleView.MapaInfraestructura;
import com.example.lightcontrol_app.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapaPuntosSimultaneos extends AppCompatActivity {

    private MapView mapa;
    private MyLocationNewOverlay myLocationOverlay;
    private CompassOverlay compassOverlay;
    private RotationGestureOverlay rotationGestureOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_puntos_simultaneos);

        Toolbar toolbar = findViewById(R.id.toolbarMapa);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        List<MapaInfraestructura> listaLugares;

        // Configuración importante para osmdroid
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));


        mapa = findViewById(R.id.map);
        mapa.setTileSource(TileSourceFactory.MAPNIK);
        mapa.setMultiTouchControls(true);

        Intent intent = getIntent();
        int codigoElemento = intent.getIntExtra("MapaIndividual", -1);


        // Ejecutar la operación de red en un hilo secundario
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            BaseDeDatosAux baseDeDatosAux = new BaseDeDatosAux();
            List<MapaInfraestructura> lista = baseDeDatosAux.obtenerLugarMapa(String.valueOf(codigoElemento));
            runOnUiThread(() -> {
                configurarMapa(lista);
            });
        });
    }

    private void configurarMapa(List<MapaInfraestructura> lista) {
        // Configurar el controlador del mapa
        mapa.getController().setZoom(15.0);
        GeoPoint puntoDeInicio = CentroideCalculadora.calcularCentroide(lista);
        mapa.getController().setCenter(puntoDeInicio);
        // Añadir overlays
        this.myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapa);
        this.myLocationOverlay.enableMyLocation();
        mapa.getOverlays().add(this.myLocationOverlay);

        this.compassOverlay = new CompassOverlay(this, mapa);
        this.compassOverlay.enableCompass();
        mapa.getOverlays().add(this.compassOverlay);

        this.rotationGestureOverlay = new RotationGestureOverlay(mapa);
        this.rotationGestureOverlay.setEnabled(true);
        mapa.getOverlays().add(this.rotationGestureOverlay);

        for (MapaInfraestructura l : lista) {
            // Añadir marcadores
            añadirMarcadores(l.getLatitud(), l.getLongitud(), l.getBarrio(), l.getDireccion());
        }

        // Configurar botones de zoom
        Button btnZoomIn = findViewById(R.id.btnZoomIn);
        Button btnZoomOut = findViewById(R.id.btnZoomOut);

        btnZoomIn.setOnClickListener(v -> mapa.getController().zoomIn());
        btnZoomOut.setOnClickListener(v -> mapa.getController().zoomOut());
    }

    private void añadirMarcadores(double latitud, double longitud, String barrio, String direccion) {
        Marker marker = new Marker(mapa);
        marker.setPosition(new GeoPoint(latitud, longitud));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(barrio);
        marker.setSnippet(direccion);
        mapa.getOverlays().add(marker);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapa.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapa.onPause();
    }
}