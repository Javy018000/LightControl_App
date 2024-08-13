package com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;
import com.example.lightcontrol_app.Model.CentroideCalculadora;
import com.example.lightcontrol_app.Modelo_RecycleView.MapaInfraestructura;
import com.example.lightcontrol_app.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapaPuntosSimultaneos extends AppCompatActivity {

    private MapView mapa;
    private MyLocationNewOverlay myLocationOverlay;
    private CompassOverlay compassOverlay;
    private RotationGestureOverlay rotationGestureOverlay;
    private FusedLocationProviderClient fusedLocationClient;
    List<MapaInfraestructura> listaLugares;
    View btnBrujula;
    FloatingActionButton fabVerEnMaps;
    private BroadcastReceiver gpsReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_puntos_simultaneos);

        Toolbar toolbar = findViewById(R.id.toolbarMapa);
        btnBrujula = findViewById(R.id.buttonBrujula);
        fabVerEnMaps = findViewById(R.id.fabVerEnMaps);


        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        fabVerEnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trazarRuta();
            }
        });
        fabVerEnMaps.setVisibility(View.GONE);

        // Configuración importante para osmdroid
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));


        mapa = findViewById(R.id.map);
        mapa.setTileSource(TileSourceFactory.MAPNIK);
        mapa.setMultiTouchControls(true);

        Intent intent = getIntent();
        String codigoElemento = String.valueOf(intent.getIntExtra("MapaIndividual", -1));

        // Ejecutar la operación de red en un hilo secundario
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            String query;
            if (!(codigoElemento.equals("-1"))){
                query = "SELECT latitud, longitud, barrio, direccion " +
                        "FROM infraestructura " +
                        "WHERE codigo = "+"'"+codigoElemento+"'";
            }
            else{
                query = "SELECT i.latitud, i.longitud, i.barrio, i.direccion " +
                        "FROM infraestructura i " +
                        "JOIN ordenes_de_servicio o ON i.codigo = o.codigo_de_elemento " +
                        "WHERE o.IdEstado = 2";
            }

            BaseDeDatosAux baseDeDatosAux = new BaseDeDatosAux();
            List<MapaInfraestructura> lista = baseDeDatosAux.obtenerDatos(query, resultSet -> new MapaInfraestructura(
                    resultSet.getDouble("latitud"),
                    resultSet.getDouble("longitud"),
                    resultSet.getString("barrio"),
                    resultSet.getString("direccion")
            ));
            runOnUiThread(() -> {
                if (lista.isEmpty()){
                    Toast.makeText(this, "No hay puntos disponibles", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    listaLugares = lista;  // Guardamos la lista
                    configurarMapa(lista);
                }
            });
        });

        // Registrar el BroadcastReceiver para cambios en el GPS
        gpsReceiver = new BroadcastReceiver() {
            private Handler handler = new Handler();
            private Runnable runnable;

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                    if (runnable != null) {
                        handler.removeCallbacks(runnable);
                    }

                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            List<MapaInfraestructura> lista = new ArrayList<>(listaLugares);
                            configurarMapa(lista);
                        }
                    };

                    // Añadir un pequeño retraso (por ejemplo, 500 ms) antes de ejecutar
                    handler.postDelayed(runnable, 500);
                }
            }
        };
        registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

    }

    private void configurarMapa(List<MapaInfraestructura> lista) {
        // Configurar el controlador del mapa
        ajustarZoom(convertArrayList(lista));

        if (lista.size() > 1){
            GeoPoint puntoDeInicio = CentroideCalculadora.calcularCentroide(lista);
            mapa.getController().setCenter(puntoDeInicio);
        }

        // Limpiar overlays anteriores
        mapa.getOverlays().clear();

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

        if (lista.size() == 1) {
            fabVerEnMaps.setVisibility(View.VISIBLE);



            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            for (MapaInfraestructura m :
                    listaLugares) {
                System.out.println(m.toStringgg());
                System.out.println("gps: "+isGPSEnabled);
            }

            if (isGPSEnabled) {
                obtenerUbicacionActual(new OnUbicacionObtenidaListener() {
                    @Override
                    public void onUbicacionObtenida(GeoPoint ubicacionActual) {
                        if (ubicacionActual != null){
                            //ArrayList<GeoPoint> listaZoom = new ArrayList<>();
                            //listaZoom.add(new GeoPoint(ubicacionActual.getLatitude(), ubicacionActual.getLongitude()));
                            List<MapaInfraestructura> listaAux = new ArrayList<>(lista);

                            listaAux.add(new MapaInfraestructura(ubicacionActual.getLatitude(), ubicacionActual.getLongitude(), "Tú", "Posición actual"));
                            mapa.getController().setCenter(CentroideCalculadora.calcularCentroide(listaAux));
                            MapaInfraestructura destino = listaAux.get(0);
                            //listaZoom.add(new GeoPoint(destino.getLatitud(), destino.getLongitud()));
                            ajustarZoom(convertArrayList(listaAux));
                            GeoPoint puntoDestino = new GeoPoint(destino.getLatitud(), destino.getLongitud());
                            mostrarRuta(ubicacionActual, puntoDestino);
                        }
                        else {
                            System.out.println("ffffffff");
                        }
                    }
                });
            } else {
                mapa.getController().setZoom(15.0);
                mapa.getController().setCenter(new GeoPoint(listaLugares.get(0).getLatitud(), listaLugares.get(0).getLongitud()));
                Toast.makeText(this, "GPS desactivado, activalo para mostrar ruta", Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void ajustarZoom(ArrayList<GeoPoint> puntos) {
        if (puntos == null || puntos.size() < 2) return;

        BoundingBox boundingBox = BoundingBox.fromGeoPoints(puntos);
        mapa.zoomToBoundingBox(boundingBox, true);
    }

    private void obtenerUbicacionActual(OnUbicacionObtenidaListener listener) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPSEnabled) {
            listener.onUbicacionObtenida(null);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // 10 segundos
        locationRequest.setFastestInterval(5000); // 5 segundos

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        GeoPoint ubicacionActual = new GeoPoint(location.getLatitude(), location.getLongitude());
                        listener.onUbicacionObtenida(ubicacionActual);
                        fusedLocationClient.removeLocationUpdates(this);
                        return;
                    }
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
    interface OnUbicacionObtenidaListener {
        void onUbicacionObtenida(GeoPoint ubicacion);
    }

    private void mostrarRuta(GeoPoint inicio, GeoPoint destino) {
        // Crear un user agent único para tu aplicación
        String userAgent = "OsmDroid/MyApp";

        RoadManager roadManager = new OSRMRoadManager(this, userAgent);
        ((OSRMRoadManager)roadManager).setMean(OSRMRoadManager.MEAN_BY_CAR);

        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(inicio);
        waypoints.add(destino);

        // Realizar la solicitud de ruta en un hilo separado
        new Thread(() -> {
            Road road = roadManager.getRoad(waypoints);
            runOnUiThread(() -> {
                if (road.mStatus == Road.STATUS_OK) {
                    Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                    mapa.getOverlays().add(roadOverlay);
                    mapa.invalidate(); // Redibujar el mapa
                } else {
                    Toast.makeText(this, "Error al obtener la ruta, enciende el GPS e intenta de nuevo", Toast.LENGTH_SHORT).show();
                    mapa.getController().setCenter(CentroideCalculadora.calcularCentroide(listaLugares));

                    ajustarZoom(convertArrayList(listaLugares));

                }
            });
        }).start();
    }

    private ArrayList<GeoPoint> convertArrayList(List<MapaInfraestructura> listaInfraestructura) {
        ArrayList<GeoPoint> lista = new ArrayList<>();
        for (MapaInfraestructura mapaGeo :
                listaInfraestructura) {
            lista.add(new GeoPoint(mapaGeo.getLatitud(), mapaGeo.getLongitud()));
        }
        return lista;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, intenta obtener la ubicación nuevamente
                if (listaLugares != null && !listaLugares.isEmpty()) {
                    configurarMapa(listaLugares);
                } else {
                    Toast.makeText(this, "No hay puntos disponibles para mostrar", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Permiso denegado, muestra un diálogo explicativo y ofrece ir a la configuración
                new AlertDialog.Builder(this)
                        .setTitle("Permiso de ubicación requerido")
                        .setMessage("Se requiere el permiso de ubicación para mostrar la ruta. ¿Deseas habilitarlo en la configuración?")
                        .setPositiveButton("Ir a Configuración", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> {
                            dialog.dismiss();
                            Toast.makeText(this, "La ruta no se puede mostrar sin el permiso de ubicación", Toast.LENGTH_LONG).show();
                        })
                        .create()
                        .show();
            }
        }
    }

    private void reorientarMapa() {
        if (compassOverlay != null) {
            float bearing = compassOverlay.getOrientation();
            rotarMapaSuavemente(0);
        }
    }
    private void rotarMapaSuavemente(final float targetOrientation) {
        final float startOrientation = mapa.getMapOrientation();
        final long start = SystemClock.elapsedRealtime();
        final long duration = 1000; // Duración de la animación en milisegundos

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.elapsedRealtime() - start;
                float t = Math.min(1, (float) elapsed / duration);

                // Interpolación
                float interpolatedRotation = startOrientation + (targetOrientation - startOrientation) * t;

                mapa.setMapOrientation(interpolatedRotation);

                if (t < 1) {
                    handler.postDelayed(this, 16); // Aproximadamente 60 FPS
                } else {
                    mapa.setMapOrientation(targetOrientation); // Asegura que la orientación final sea exactamente la deseada
                }
            }
        });
    }
    public void onButtonBrujulaClick(View view) {
        // Acción del botón
        reorientarMapa();
    }
    public void trazarRuta() {

        double latitude = listaLugares.get(0).getLatitud();
        double longitude = listaLugares.get(0).getLongitud();
        // Crear una URI con las coordenadas del marcador
        String uri = "google.navigation:q=" + latitude + "," + longitude;
        // Crear un Intent con la acción de vista y la URI de Google Maps
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        // Establecer el paquete de Google Maps para asegurarse de que se abra en Google Maps
        mapIntent.setPackage("com.google.android.apps.maps");
        // Iniciar la actividad de Google Maps
        startActivity(mapIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapa.onResume();
        if (listaLugares != null && !listaLugares.isEmpty()) {
            configurarMapa(listaLugares);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapa.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // No olvides desregistrar el receptor para evitar fugas de memoria
        if (gpsReceiver != null) {
            unregisterReceiver(gpsReceiver);
        }
    }
}