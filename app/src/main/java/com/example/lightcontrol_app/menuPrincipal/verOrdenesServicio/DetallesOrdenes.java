package com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;
import com.example.lightcontrol_app.menuPrincipal.MenuPrincipal;
import com.example.lightcontrol_app.Modelo_RecycleView.Insumos;
import com.example.lightcontrol_app.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetallesOrdenes extends AppCompatActivity implements InformacionFragment.OnFragmentInteractionListener, InsumosFragment.OnFragmentInteractionListener{
    private ViewPager viewPager;
    private TabLayout tabLayout;

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
        //String orderDescription = intent.getStringExtra("orderDescription");

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
    }

    @Override
    public void onButtonToInsumosClicked() {
        // Cambiar a la pestaña de Insumos
        viewPager.setCurrentItem(2);
    }

    @Override
    public void onButtonToInfoClicked() {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onButtonConfirmarClicked(List<Insumos> insumosModificados) {
        for (Insumos insumo : insumosModificados) {
            System.out.println(insumo);
        }
        // Ejecutar la operación de red en un hilo secundario
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                // Recibir los datos del intent
                Intent intent = getIntent();
                int orderId = intent.getIntExtra("orderId", -1);
                // Aquí puedes manejar el envío a la base de datos
                BaseDeDatosAux baseDeDatosAux = new BaseDeDatosAux();
                if (insumosModificados != null){
                    for (Insumos insumo: insumosModificados) {
                        baseDeDatosAux.actualizarCantidadInsumos(insumo.getId(), insumo.getCantidad());
                    }
                }
                baseDeDatosAux.actualizarEstadoOrdenServicio(orderId, 3);
                runOnUiThread(()->{
                    Toast.makeText(this, "Se ha actualizado correctamente la información", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(this, MenuPrincipal.class);
                    startActivity(intent1);
                    finish();
                });

            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
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