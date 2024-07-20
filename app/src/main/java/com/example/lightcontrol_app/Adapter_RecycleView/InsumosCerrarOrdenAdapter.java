package com.example.lightcontrol_app.Adapter_RecycleView;


import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lightcontrol_app.Modelo_RecycleView.Insumos;
import com.example.lightcontrol_app.R;

import java.util.ArrayList;
import java.util.List;

public class InsumosCerrarOrdenAdapter extends RecyclerView.Adapter<InsumosCerrarOrdenAdapter.OrderViewHolder> {
    List<Insumos> insumosLista;
    List<Insumos> insumosModificados;

    public InsumosCerrarOrdenAdapter(List<Insumos> insumosLista) {
        this.insumosLista = insumosLista;
        this.insumosModificados = new ArrayList<>();
    }

    public List<Insumos> getInsumosModificados() {
        return insumosModificados;
    }

    @NonNull
    @Override
    public InsumosCerrarOrdenAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_insumos_cerrar_orden, parent, false);
        return new InsumosCerrarOrdenAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InsumosCerrarOrdenAdapter.OrderViewHolder holder, int position) {
        Insumos insumos = insumosLista.get(position);
        System.out.println("Insumos " + insumos.getId());
        holder.itemId.setText(String.valueOf(insumos.getId()));
        holder.itemNombre.setText(insumos.getNombreElemento());
        holder.itemDescripcion.setText(insumos.getDescripcion());
        holder.campCantidad.setText(String.valueOf(insumos.getCantidad()));
        if(insumos.getEstado().equals("Disponible")) {
            GradientDrawable drawable = (GradientDrawable) holder.circulito.getBackground();
            drawable.setColor(holder.itemView.getResources().getColor(android.R.color.holo_green_light));
        } else {
            holder.btnMas.setEnabled(false);
            holder.btnMenos.setEnabled(false);
            holder.campCantidad.setEnabled(false);
        }
        holder.btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cant = insumos.getCantidad();
                if (cant > 0) {
                    holder.btnMenos.setEnabled(true);
                    insumos.setCantidad(++cant);
                    holder.campCantidad.setText(String.valueOf(cant));
                    addModifiedInsumo(insumos);
                }
            }
        });
        holder.btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cant = insumos.getCantidad();
                if (cant > 0) {
                    insumos.setCantidad(--cant);
                    holder.campCantidad.setText(String.valueOf(cant));
                    addModifiedInsumo(insumos);
                } else {
                    holder.btnMenos.setEnabled(false);
                }
            }
        });
    }

    private void addModifiedInsumo(Insumos insumo) {
        if (!insumosModificados.contains(insumo)) {
            insumosModificados.add(insumo);
        }
    }

    @Override
    public int getItemCount() {
        return insumosLista.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        View circulito;
        ImageView icono;
        TextView itemId;
        TextView itemNombre;
        TextView itemDescripcion;
        Button btnMenos;
        Button btnMas;
        EditText campCantidad;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            circulito = itemView.findViewById(R.id.circuloEstadoRecycleCerrarOrden);
            icono = itemView.findViewById(R.id.iconRecycleCerrarOrden);
            itemId = itemView.findViewById(R.id.itemIdCerrarOrden);
            itemNombre = itemView.findViewById(R.id.itemNombreRecycleCerrarOrden);
            itemDescripcion = itemView.findViewById(R.id.itemDescripcionRecycleCerrarOrden);
            btnMenos = itemView.findViewById(R.id.buttonMenosRecycleCerrarOrden);
            btnMas = itemView.findViewById(R.id.buttonMasRecycleCerrarOrden);
            campCantidad = itemView.findViewById(R.id.editTextCantidadRecycleCerrarOrden);
        }
    }
}

