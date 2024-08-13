package com.example.lightcontrol_app.Adapter_RecycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lightcontrol_app.Modelo_RecycleView.Trabajos;
import com.example.lightcontrol_app.R;

import java.util.List;

public class OrdenServicioTrabajoAdapter extends RecyclerView.Adapter<OrdenServicioTrabajoAdapter.TrabajosViewHolder> {
    private List<Trabajos> trabajosList;

    public OrdenServicioTrabajoAdapter(List<Trabajos> trabajosList) {
        this.trabajosList = trabajosList;
    }

    public List<Trabajos> getTrabajosList() {
        return trabajosList;
    }

    @NonNull
    @Override
    public TrabajosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_trabajos, parent, false);
        return new TrabajosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrabajosViewHolder holder, int position) {
        Trabajos trabajo = trabajosList.get(position);

        holder.textNombreTrab.setText(trabajo.getTrabajoNombre());
        holder.checkTrab.setChecked(trabajo.isEsSeleccionado());

        holder.checkTrab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                trabajo.setEsSeleccionado(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trabajosList.size();
    }


    public static class TrabajosViewHolder extends RecyclerView.ViewHolder{
        TextView textNombreTrab;
        CheckBox checkTrab;

        public TrabajosViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombreTrab = itemView.findViewById(R.id.nombreRecycleTrabajoOrdenServicio);
            checkTrab = itemView.findViewById(R.id.checkRecycleTrabajosOrdenServicio);
        }
    }
}
