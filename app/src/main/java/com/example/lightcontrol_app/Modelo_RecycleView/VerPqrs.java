package com.example.lightcontrol_app.Modelo_RecycleView;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.util.Arrays;

public class VerPqrs implements Parcelable {

    int idpqrs;
    Date fechaRegistro;
    String tipopqrs;
    String canal;
    String nombre;
    String apellido;
    String tipoDoc;
    String documento;
    String barrioUsuario;
    String direccionUsuario;
    String correo;
    String referencia;
    String direccionAfectacion;
    String barrioAfectacion;
    String tipoAlumbrado;
    String descripcionAfectacion;
    int estado;
    byte[] imagen;
    String telefono;
    String fechaParcelable;

    public VerPqrs(int idpqrs, Date fechaRegistro, String tipopqrs, String canal, String nombre, String apellido, String tipoDoc, String documento, String barrioUsuario, String direccionUsuario, String correo, String referencia, String direccionAfectacion, String barrioAfectacion, String tipoAlumbrado, String descripcionAfectacion, int estado, String telefono) {
        this.idpqrs = idpqrs;
        this.fechaRegistro = fechaRegistro;
        this.tipopqrs = tipopqrs;
        this.canal = canal;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDoc = tipoDoc;
        this.documento = documento;
        this.barrioUsuario = barrioUsuario;
        this.direccionUsuario = direccionUsuario;
        this.correo = correo;
        this.referencia = referencia;
        this.direccionAfectacion = direccionAfectacion;
        this.barrioAfectacion = barrioAfectacion;
        this.tipoAlumbrado = tipoAlumbrado;
        this.descripcionAfectacion = descripcionAfectacion;
        this.estado = estado;
        this.telefono = telefono;
        this.fechaParcelable = fechaRegistro.toString();
    }

    public VerPqrs(int idpqrs, Date fechaRegistro, String tipopqrs, String canal, String barrioUsuario, String descripcionAfectacion) {
        this.idpqrs = idpqrs;
        this.fechaRegistro = fechaRegistro;
        this.tipopqrs = tipopqrs;
        this.canal = canal;
        this.barrioUsuario = barrioUsuario;
        this.descripcionAfectacion = descripcionAfectacion;
    }

    protected VerPqrs(Parcel in) {
        idpqrs = in.readInt();
        tipopqrs = in.readString();
        canal = in.readString();
        nombre = in.readString();
        apellido = in.readString();
        tipoDoc = in.readString();
        documento = in.readString();
        barrioUsuario = in.readString();
        direccionUsuario = in.readString();
        correo = in.readString();
        referencia = in.readString();
        direccionAfectacion = in.readString();
        barrioAfectacion = in.readString();
        tipoAlumbrado = in.readString();
        descripcionAfectacion = in.readString();
        estado = in.readInt();
        telefono = in.readString();
        fechaParcelable = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idpqrs);
        dest.writeString(tipopqrs);
        dest.writeString(canal);
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(tipoDoc);
        dest.writeString(documento);
        dest.writeString(barrioUsuario);
        dest.writeString(direccionUsuario);
        dest.writeString(correo);
        dest.writeString(referencia);
        dest.writeString(direccionAfectacion);
        dest.writeString(barrioAfectacion);
        dest.writeString(tipoAlumbrado);
        dest.writeString(descripcionAfectacion);
        dest.writeInt(estado);
        dest.writeString(telefono);
        dest.writeString(fechaParcelable);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VerPqrs> CREATOR = new Creator<VerPqrs>() {
        @Override
        public VerPqrs createFromParcel(Parcel in) {
            return new VerPqrs(in);
        }

        @Override
        public VerPqrs[] newArray(int size) {
            return new VerPqrs[size];
        }
    };

    public int getIdpqrs() {
        return idpqrs;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public String getTipopqrs() {
        return tipopqrs;
    }

    public String getCanal() {
        return canal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public String getDocumento() {
        return documento;
    }

    public String getBarrioUsuario() {
        return barrioUsuario;
    }

    public String getDireccionUsuario() {
        return direccionUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getDireccionAfectacion() {
        return direccionAfectacion;
    }

    public String getBarrioAfectacion() {
        return barrioAfectacion;
    }

    public String getTipoAlumbrado() {
        return tipoAlumbrado;
    }

    public String getDescripcionAfectacion() {
        return descripcionAfectacion;
    }

    public int getEstado() {
        return estado;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFechaParcelable() {
        return fechaParcelable;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String toStringEnviar() {
        return "Id de la pqr:" + idpqrs + "\n"+
                "Fecha de Registro:" + "\n"+
                "Tipo de pqr:" + tipopqrs + "\n"+
                "Canal:" + canal + "\n"+
                "Nombre:" + nombre + "\n"+
                "Apellido:" + apellido + "\n"+
                "Tipo de Documento:" + tipoDoc + "\n"+
                "Documento:" + documento + "\n"+
                "Barrio:" + barrioUsuario + "\n"+
                "Direccion del Usuario:" + direccionUsuario + "\n"+
                "Correo:" + correo + "\n"+
                "Referencia:" + referencia + "\n"+
                "Direccion de la afectacion:" + direccionAfectacion + "\n"+
                "Barriode la afectacion:" + barrioAfectacion + "\n"+
                "Tipo de alumbrado:" + tipoAlumbrado + "\n"+
                "Descripcion de la fectacion:" + descripcionAfectacion + "\n"+
                "Estado:" + estado + "\n"+
                "Telefono:" + telefono + "\n";
    }
    public String toStringRecycle() {
        return "- Fecha de Registro: " + getFechaRegistro() + "\n"+
                "- Tipo de pqr: " + getTipopqrs() + "\n"+
                "- Canal: " + getCanal() + "\n"+
                "- Barrio: " + getBarrioAfectacion() + "\n"+
                "- Descripcion de la afectación: " + getDescripcionAfectacion() + "\n";
    }
}
