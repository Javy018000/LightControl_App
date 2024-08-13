package com.example.lightcontrol_app.Modelo_RecycleView;

import android.os.Parcel;
import android.os.Parcelable;

public class VerInventario implements Parcelable {
    int id;
    String nombre_elemento;
    int cantidad;
    String estado;
    String descripcion;

    public VerInventario(int id, String nombre_elemento, int cantidad, String estado, String descripcion) {
        this.id = id;
        this.nombre_elemento = nombre_elemento;
        this.cantidad = cantidad;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    protected VerInventario(Parcel in) {
        id = in.readInt();
        nombre_elemento = in.readString();
        cantidad = in.readInt();
        estado = in.readString();
        descripcion = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre_elemento);
        dest.writeInt(cantidad);
        dest.writeString(estado);
        dest.writeString(descripcion);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VerInventario> CREATOR = new Creator<VerInventario>() {
        @Override
        public VerInventario createFromParcel(Parcel in) {
            return new VerInventario(in);
        }

        @Override
        public VerInventario[] newArray(int size) {
            return new VerInventario[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_elemento() {
        return nombre_elemento;
    }

    public void setNombre_elemento(String nombre_elemento) {
        this.nombre_elemento = nombre_elemento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String toStringRecycle() {
        return "- Número: " + getId() + "\n"+
                "- Nombre del elemento: " + getNombre_elemento() + "\n"+
                "- Cantidad del elemento: " + getCantidad() + "\n"+
                "- Estado: " + getEstado() + "\n"+
                "- Descripcion del elemento: " + getDescripcion() + "\n";
    }


}
