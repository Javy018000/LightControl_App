package com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TabStopSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lightcontrol_app.Modelo_RecycleView.Trabajos;
import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;
import com.example.lightcontrol_app.Modelo_RecycleView.Insumos;
import com.example.lightcontrol_app.R;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetallesOrdenes extends AppCompatActivity implements InformacionFragment.OnFragmentInteractionListener, TrabajosFragment.OnFragmentInteractionListener, InsumosFragment.OnFragmentInteractionListener{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Trabajos> trabajosSeleccionados;
    private List<Uri> imageUris;
    private String observacion;
    String orderDescription = null;
    String elementoRelacionado = null;
    AlertDialog dialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_ordenes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);


        // Recibir los datos del intent
        Intent intent = getIntent();
        int orderId = intent.getIntExtra("orderId", -1);
        orderDescription = intent.getStringExtra("orderDescription");

        Bundle bundle = new Bundle();
        bundle.putInt("infoId", orderId);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), orderId);
        Fragment informacionFragment = new InformacionFragment();
        Fragment insumosFragment = new InsumosFragment();
        insumosFragment.setArguments(bundle);
        informacionFragment.setArguments(bundle);
        adapter.addFragment(informacionFragment, "Información");
        adapter.addFragment(new TrabajosFragment(), "Trabajos");
        adapter.addFragment(insumosFragment, "Insumos");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Deshabilitar el deslizamiento del ViewPager
        viewPager.setOnTouchListener((v, event) -> true);

        // Deshabilitar los clics en las pestañas
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.view.setClickable(false);
            }
        }

    }
    // Método para habilitar o deshabilitar las pestañas
    @SuppressLint("ClickableViewAccessibility")
    public void habilitarPestañas(boolean habilitar) {
        viewPager.setOnTouchListener((v, event) -> !habilitar);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.view.setClickable(habilitar);
            }
        }
    }

    @Override
    public void onButtonToTrabajosClicked(String pqrRelacionada) {
        elementoRelacionado = pqrRelacionada;
        // Cambiar a la pestaña de trabajos
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onButtonToTrabajosVolverClicked() {
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onButtonToInsumosClicked(List<Trabajos> trabajosSelec, List<Uri> uris, String observacion) {
        setTrabajosSeleccionados(trabajosSelec);
        setImageUris(uris);
        setObservacion(observacion);
        viewPager.setCurrentItem(2);
    }

    @Override
    public void onButtonToInfoVolverClicked() {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onButtonConfirmarClicked(List<Insumos> insumosModificados) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_cerrar_orden, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        dialog = builder.create();
        if (!isFinishing() && !isDestroyed()) {
            dialog.show();
        }

        Button btnNo = dialogView.findViewById(R.id.btnNo);
        Button btnSi = dialogView.findViewById(R.id.btnSi);
        int tabStop = 680;  // Ajusta este valor según el ancho deseado
        TextView textView = dialogView.findViewById(R.id.dialogTextCerrarOrden);

        StringBuilder texto = new StringBuilder();
        if (orderDescription != null && !orderDescription.isEmpty()){
            texto.append("Descripción de la orden:\n").append(orderDescription);
        }
        texto.append("\n\n");
        if (!getTrabajosSeleccionados().isEmpty()){
            texto.append("Trabajos realizados:\n");
            for (Trabajos trabajo :
                    getTrabajosSeleccionados()) {
                if (trabajo.isEsSeleccionado()){
                    texto.append(trabajo.getTrabajoNombre()).append("\n");
                }
            }
        }
        texto.append("\n\n");
        if (!insumosModificados.isEmpty()){
            texto.append("Insumos utilizados:\n");
            for (Insumos insumo :
                    insumosModificados) {
                texto.append(insumo.getNombreElemento()).append("\t").append(insumo.getCantidadUtilizada()).append("\n");
            }
        }

        // Convertir el texto a SpannableString para agregar los TabStopSpans
        SpannableString spannableString = new SpannableString(texto.toString());

        // Aplicar TabStopSpan a cada línea de insumos
        int startIndex = texto.indexOf("Insumos utilizados:") + "Insumos utilizados:\n".length();
        for (Insumos insumo : insumosModificados) {
            int endIndex = texto.indexOf("\n", startIndex);
            if (endIndex == -1) endIndex = texto.length();
            spannableString.setSpan(new TabStopSpan.Standard(tabStop), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            startIndex = endIndex + 1;
        }

        textView.setText(spannableString);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón NO
                dialog.dismiss();
            }
        });

        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón SI
                // Ejecutar la operación de red en un hilo secundario
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    try {
                        // Recibir los datos del intent
                        Intent intent = getIntent();
                        int orderId = intent.getIntExtra("orderId", -1);
                        // Aquí puedes manejar el envío a la base de datos
                        BaseDeDatosAux baseDeDatosAux = new BaseDeDatosAux();
                        for (Insumos insumo : insumosModificados) {
                            insumo.setCantidad(insumo.getCantidad() - insumo.getCantidadUtilizada());
                            baseDeDatosAux.actualizarDatos("UPDATE Inventario SET cantidad = ? WHERE id = ?", insumo.getCantidad(), insumo.getId());
                        }

                        String trabajosEnviar = obtenerTrabajosSeparados(getTrabajosSeleccionados());
                        baseDeDatosAux.actualizarDatos("UPDATE ordenes_de_servicio SET IdEstado = ?, observaciones = ?, Trabajos = ? WHERE id_orden = ?", 3, getObservacion(), trabajosEnviar, orderId);
                        baseDeDatosAux.actualizarDatos("UPDATE pqrs SET Estado = ? WHERE Idpqrs = ?", 3, elementoRelacionado);
                        // Guardar las imágenes en la base de datos
                        for (Uri imageUri : getImageUris()) {
                            byte[] imageBytes = getBytesFromUri(imageUri);
                            // Aquí llamas a tu método para guardar la imagen en la base de datos
                            baseDeDatosAux.actualizarDatos("INSERT INTO ImagenesOrdenesDeServicio (id_orden, imagen, fecha_subida) VALUES (?, ?, GETDATE())", orderId, imageBytes);
                        }
                        runOnUiThread(()->{
                            Toast.makeText(getApplicationContext(), "Se ha actualizado correctamente la información", Toast.LENGTH_SHORT).show();
                            finish();
                        });

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        });

    }

    private String obtenerTrabajosSeparados(List<Trabajos> trabajosSeleccionados) {
        if (!trabajosSeleccionados.isEmpty()){
            StringBuilder texto = new StringBuilder();
            for (int i = 0; i < trabajosSeleccionados.size(); i++) {
                Trabajos t = trabajosSeleccionados.get(i);
                texto.append(t.getTrabajoNombre());
                if (i < trabajosSeleccionados.size() - 1) {
                    texto.append(", ");
                }
            }
            return texto.toString();
        }
        else {
            return null;
        }

    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }
    public byte[] getBytesFromUri(Uri uri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            Log.e("TuActivity", "Error al obtener byte array de la imagen", e);
            return null;
        }
    }


    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }


    public List<Uri> getImageUris() {
        return imageUris;
    }

    public void setImageUris(List<Uri> imageUris) {
        this.imageUris = imageUris;
    }

    public List<Trabajos> getTrabajosSeleccionados() {
        return trabajosSeleccionados;
    }

    public void setTrabajosSeleccionados(List<Trabajos> trabajosSeleccionados) {
        this.trabajosSeleccionados = trabajosSeleccionados;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();
        private int orderId;

        public ViewPagerAdapter(FragmentManager fm, int orderId) {
            super(fm);
            this.orderId = orderId;
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}