package com.example.lightcontrol_app.Adapter_RecycleView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lightcontrol_app.Modelo_RecycleView.VerInventario;
import com.example.lightcontrol_app.R;
import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;
import com.example.lightcontrol_app.menuPrincipal.verInventario.EditarInventarioActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VerInventarioAdapter extends RecyclerView.Adapter<VerInventarioAdapter.VerInventarioViewHolder>{

    private List<VerInventario> verInventarioList;

    public VerInventarioAdapter(List<VerInventario> verInventarioList) {
        this.verInventarioList = verInventarioList;
    }

    @NonNull
    @Override
    public VerInventarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_ver_pqrs, parent, false);
        return new VerInventarioViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VerInventarioViewHolder holder, int position) {
        holder.btnOpcional.setVisibility(View.GONE);
        holder.imageView.setImageResource(R.drawable.ver_insumos_inventario);
        VerInventario inventario = verInventarioList.get(position);
        holder.textTitulo.setText("Elemento #" + inventario.getId());
        holder.textContenido.setText(inventario.toStringRecycle());
        holder.btnEditarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_editar_inventario, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                // Obtener referencias a los componentes del include
                LinearLayout includedInsumos = dialogView.findViewById(R.id.includedInsumos);
                // Ahora puedes acceder a los componentes dentro de includedInsumos
                TextView itemId = includedInsumos.findViewById(R.id.itemIdCerrarOrden);
                TextView itemNombre = includedInsumos.findViewById(R.id.itemNombreRecycleCerrarOrden);
                TextView itemDescripcion = includedInsumos.findViewById(R.id.itemDescripcionRecycleCerrarOrden);
                Button buttonMenos = includedInsumos.findViewById(R.id.buttonMenosRecycleCerrarOrden);
                EditText editTextCantidad = includedInsumos.findViewById(R.id.editTextCantidadRecycleCerrarOrden);
                Button buttonMas = includedInsumos.findViewById(R.id.buttonMasRecycleCerrarOrden);
                View circulito = includedInsumos.findViewById(R.id.circuloEstadoRecycleCerrarOrden);

                //editTextCantidad.setEnabled(false);

                if(inventario.getEstado().equals("Disponible")) {
                    GradientDrawable drawable = (GradientDrawable) circulito.getBackground();
                    drawable.setColor(holder.itemView.getResources().getColor(android.R.color.holo_green_light));
                } else {
                    buttonMas.setEnabled(false);
                    buttonMenos.setEnabled(false);
                }

                // Configura los valores de los componentes
                itemId.setText("#" + inventario.getId());
                itemNombre.setText(inventario.getNombre_elemento());
                itemDescripcion.setText(inventario.getDescripcion());
                editTextCantidad.setText(String.valueOf(inventario.getCantidad()));

                // Configurar acciones de los botones
                buttonMenos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int cantidad = Integer.parseInt(editTextCantidad.getText().toString());
                        if (cantidad >= 0) {
                            editTextCantidad.setText(String.valueOf(--cantidad));
                        }
                    }
                });

                buttonMas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int cantidad = Integer.parseInt(editTextCantidad.getText().toString());
                        if (cantidad > 0) {
                            editTextCantidad.setText(String.valueOf(++cantidad));
                        }
                    }
                });


                // Configura los botones del dialogo
                Button btnNo = dialogView.findViewById(R.id.btnNo);
                Button btnSi = dialogView.findViewById(R.id.btnSi);

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnSi.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View v) {
                        // Lógica para aceptar
                        inventario.setCantidad(Integer.parseInt(editTextCantidad.getText().toString()));
                        actualizarDatosInventario(inventario.getId(), inventario.getCantidad());
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

    private void actualizarDatosInventario(int id, int cantidad) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    try {
                        // Aquí puedes manejar el envío a la base de datos
                        BaseDeDatosAux baseDeDatosAux = new BaseDeDatosAux();
                        baseDeDatosAux.actualizarDatos("UPDATE Inventario SET cantidad = ? WHERE id = ?", cantidad, id);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return verInventarioList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<VerInventario> resultadoInventario) {
        this.verInventarioList.clear();
        this.verInventarioList.addAll(resultadoInventario);
        notifyDataSetChanged();
    }


    static class VerInventarioViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textTitulo;
        TextView textContenido;
        Button btnEditarElemento;
        Button btnOpcional;

        public VerInventarioViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagenVerPqrs);
            textTitulo = itemView.findViewById(R.id.textRecycleIdPqrs);
            textContenido = itemView.findViewById(R.id.textRecycleInfoPqrs);
            btnEditarElemento = itemView.findViewById(R.id.btnRecycleVerInfoPqrs);
            btnOpcional = itemView.findViewById(R.id.btnRecycleCrearOrdenPqrs);
        }
    }
}
