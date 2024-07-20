package com.example.lightcontrol_app.menuPrincipal.crearOrdenServicio;

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
import android.view.MenuItem;

import com.example.lightcontrol_app.R;
import com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio.DetallesOrdenes;
import com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio.InformacionFragment;
import com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio.InsumosFragment;
import com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio.TrabajosFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CrearOrdenActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_orden);

        Toolbar toolbar = findViewById(R.id.toolbarCrearOrden);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        viewPager = findViewById(R.id.viewPagerCrearOrden);
        tabLayout = findViewById(R.id.tabLayoutCrearOrden);
        Intent intent = getIntent();
        int idPqr = intent.getIntExtra("CrearOrdenServicioId", -1);
        String descripcionPqr = intent.getStringExtra("CrearOrdenServicioDescription");

        Bundle bundle = new Bundle();
        bundle.putInt("CrearOrdenServicioId", idPqr);
        bundle.putString("CrearOrdenServicioDescription", descripcionPqr);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), idPqr, descripcionPqr);
        Fragment mantenimientoFragment = new MantenimientoFragment();
        Fragment expansionFragment = new ExpansionFragment();
        Fragment modernizacionFragment = new ModernizacionFragment();
        expansionFragment.setArguments(bundle);
        mantenimientoFragment.setArguments(bundle);
        modernizacionFragment.setArguments(bundle);

        adapter.addFragment(mantenimientoFragment, "Mantenimiento");
        adapter.addFragment(expansionFragment, "Expansión");
        adapter.addFragment(modernizacionFragment, "Modernización");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();
        private int orderId;
        private String descripcionPqr;

        public ViewPagerAdapter(FragmentManager fm, int orderPqr, String descripcionPqr) {
            super(fm);
            this.orderId = orderPqr;
            this.descripcionPqr = descripcionPqr;
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