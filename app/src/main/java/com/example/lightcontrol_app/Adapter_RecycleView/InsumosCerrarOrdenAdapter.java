package com.example.lightcontrol_app.Adapter_RecycleView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public void setInsumosModificados(List<Insumos> insumosModificados) {
        this.insumosModificados = insumosModificados;
    }

    @NonNull
    @Override
    public InsumosCerrarOrdenAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_insumos_cerrar_orden, parent, false);
        return new InsumosCerrarOrdenAdapter.OrderViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Insumos insumos = insumosLista.get(position);

        // Remover el TextWatcher existente si hay uno
        if (holder.textWatcher != null) {
            holder.campCantidad.removeTextChangedListener(holder.textWatcher);
        }

        // Actualizar el texto sin activar el TextWatcher
        holder.campCantidad.setText(String.valueOf(insumos.getCantidadUtilizada()));
        holder.itemId.setText(String.valueOf(insumos.getId()));
        holder.itemNombre.setText(insumos.getNombreElemento());
        holder.itemDescripcion.setText(insumos.getDescripcion());

        if(insumos.getEstado().equals("Disponible")) {
            GradientDrawable drawable = (GradientDrawable) holder.circulito.getBackground();
            drawable.setColor(holder.itemView.getResources().getColor(android.R.color.holo_green_light));
        } else {
            holder.btnMas.setEnabled(false);
            holder.btnMenos.setEnabled(false);
            holder.campCantidad.setEnabled(false);
        }

        holder.btnMas.setOnClickListener(v -> actualizarCantidad(holder, insumos, 1));
        holder.btnMenos.setOnClickListener(v -> actualizarCantidad(holder, insumos, -1));

        // Crear y agregar un nuevo TextWatcher
        holder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                actualizarCantidadManual(holder, insumos);
            }
        };
        holder.campCantidad.addTextChangedListener(holder.textWatcher);
    }
    private void actualizarCantidad(OrderViewHolder holder, Insumos insumos, int delta) {
        int nuevaCantidad = insumos.getCantidadUtilizada() + delta;
        if (nuevaCantidad >= 0 && nuevaCantidad <= insumos.getCantidad()) {
            insumos.setCantidadUtilizada(nuevaCantidad);

            // Remover y volver a agregar el TextWatcher al actualizar el texto
            holder.campCantidad.removeTextChangedListener(holder.textWatcher);
            holder.campCantidad.setText(String.valueOf(nuevaCantidad));
            holder.campCantidad.addTextChangedListener(holder.textWatcher);

            addModifiedInsumo(insumos, nuevaCantidad);
        } else {
            mostrarMensajeError(holder.itemView.getContext());
        }
    }

    private void actualizarCantidadManual(OrderViewHolder holder, Insumos insumos) {
        try {
            String texto = holder.campCantidad.getText().toString();
            if (!texto.isEmpty()) {
                int nuevaCantidad = Integer.parseInt(texto);
                if (nuevaCantidad >= 0 && nuevaCantidad <= insumos.getCantidad()) {
                    insumos.setCantidadUtilizada(nuevaCantidad);
                    addModifiedInsumo(insumos, nuevaCantidad);
                } else {
                    mostrarMensajeError(holder.itemView.getContext());
                }
            } else {
                insumos.setCantidadUtilizada(0);
                addModifiedInsumo(insumos, 0);
            }
        } catch (NumberFormatException e) {
            // Permitir que el usuario siga escribiendo
        }
    }

    private void mostrarMensajeError(Context context) {
        Toast.makeText(context, "Cantidad no válida", Toast.LENGTH_SHORT).show();
    }

    private void addModifiedInsumo(Insumos insumo, int cant) {
        if (cant != 0) {
            if (!insumosModificados.contains(insumo)) {
                insumosModificados.add(insumo);
            }
        } else {
            insumosModificados.remove(insumo);
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
        TextWatcher textWatcher;

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

