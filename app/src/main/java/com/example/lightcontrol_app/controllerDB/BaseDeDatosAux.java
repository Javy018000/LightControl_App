package com.example.lightcontrol_app.controllerDB;

import com.example.lightcontrol_app.Modelo_RecycleView.Insumos;
import com.example.lightcontrol_app.Modelo_RecycleView.MapaInfraestructura;
import com.example.lightcontrol_app.Modelo_RecycleView.OrdenServicioVerInfo;
import com.example.lightcontrol_app.Modelo_RecycleView.OrdenServicioVistaPrevia;
import com.example.lightcontrol_app.Modelo_RecycleView.VerPqrs;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BaseDeDatosAux {
    private static final String URL = "jdbc:jtds:sqlserver://tadeo.colombiahosting.com.co:1433/lightcon_luminaria";
    private static final String USER = "lightcon_lumin";
    private static final String PASSWORD = "luminaria2024*";

    public Connection connect() {
        Connection connection = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public List<OrdenServicioVistaPrevia> obtenerTodasLasOrdenes() {
        List<OrdenServicioVistaPrevia> orderList = new ArrayList<>();
        Connection connection = connect();
        if (connection != null) {
            try {
                String query = "SELECT problema_relacionado, id_orden FROM ordenes_de_servicio WHERE IdEstado = 2"; // Reemplaza 'OrdersTable' con tu tabla
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String problema = resultSet.getString("problema_relacionado");
                    int idOrden = resultSet.getInt("id_orden");
                    OrdenServicioVistaPrevia order = new OrdenServicioVistaPrevia(idOrden, problema);
                    orderList.add(order);
                }
                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return orderList;
    }
    public List<OrdenServicioVerInfo> obtenerInfoOrden(int id){
        List<OrdenServicioVerInfo> infoOrden = new ArrayList<>();
        Connection connection = connect();
        if (connection != null)
        {
            try {
                System.out.println("AAAAAA" + id);
                String query = "SELECT * FROM ordenes_de_servicio WHERE id_orden = " + id;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while(resultSet.next()){
                    int id_orden = resultSet.getInt("id_orden");
                    String tipo_de_elemento = resultSet.getString("Tipo_de_elemento");
                    String codigo_de_elemento = resultSet.getString("codigo_de_elemento");
                    String elemento_relacionado = resultSet.getString("elemento_relacionado");
                    String codigo_orden = resultSet.getString("codigo_orden");
                    String description = resultSet.getString("problema_relacionado");
                    String problema_validado = resultSet.getString("problema_validado");
                    int prioridad_de_ruta = resultSet.getInt("prioridad_de_ruta");
                    Date fecha_a_realizar = resultSet.getDate("fecha_a_realizar");
                    String cuadrilla = resultSet.getString("cuadrilla");
                    String tipo_de_orden = resultSet.getString("tipo_de_orden");
                    String tipo_de_Solucion = resultSet.getString("tipo_de_Solucion");
                    String clase_de_orden = resultSet.getString("clase_de_orden");
                    String obra_relacionada = resultSet.getString("obra_relacionada");
                    String orden_prioridad = resultSet.getString("Orden_prioridad");
                    int idEstado = resultSet.getInt("IdEstado");

                    OrdenServicioVerInfo info = new OrdenServicioVerInfo(id_orden, tipo_de_elemento, codigo_de_elemento,
                            elemento_relacionado, codigo_orden, description, problema_validado, prioridad_de_ruta,
                            fecha_a_realizar, cuadrilla, tipo_de_orden, tipo_de_Solucion, clase_de_orden, obra_relacionada,
                            orden_prioridad, idEstado);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                    infoOrden.add(info);
                }

                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return infoOrden;
    }
    public List<MapaInfraestructura> obtenerLugarMapa(String codigoDeElemento){
        List<MapaInfraestructura> listaLugares = new ArrayList<>();
        Connection connection = connect();
        if (connection != null) {
            String query;
            System.out.println("Codigo de elemento "+codigoDeElemento);
            if (!(codigoDeElemento.equals("-1"))){
                query = "SELECT latitud, longitud, barrio, direccion " +
                        "FROM infraestructura " +
                        "WHERE codigo = "+"'"+codigoDeElemento+"'";
            }
            else{
                query = "SELECT i.latitud, i.longitud, i.barrio, i.direccion " +
                        "FROM infraestructura i " +
                        "JOIN ordenes_de_servicio o ON i.codigo = o.codigo_de_elemento ";
            }
            try {

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String barrio = resultSet.getString("barrio");
                    String direccion = resultSet.getString("direccion");
                    double latitud = resultSet.getDouble("latitud");
                    double longitud = resultSet.getDouble("longitud");
                    MapaInfraestructura lugar = new MapaInfraestructura(latitud, longitud, barrio, direccion);
                    listaLugares.add(lugar);
                }
                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listaLugares;
    }

    public List<Insumos> cerrarOrden(){
        List<Insumos> listaInventario = new ArrayList<>();
        Connection connection = connect();
        if (connection != null){
            try {
                String query = "SELECT * FROM Inventario";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    int id = resultSet.getInt("ID");
                    String nombreElemento = resultSet.getString("nombre_elemento");
                    int cantidad = resultSet.getInt("cantidad");
                    String estado = resultSet.getString("estado");
                    String descripcion = resultSet.getString("descripcion");

                    Insumos insumos = new Insumos(id, nombreElemento, cantidad, estado, descripcion);

                    listaInventario.add(insumos);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return listaInventario;
    }
    public void actualizarCantidadInsumos(int id, int nuevaCantidad) {
        // Obtener la conexión a la base de datos
        Connection connection = connect();
        if (connection != null) {
            try {
                // Crear la consulta de actualización
                String query = "UPDATE Inventario SET cantidad = ? WHERE id = ?";

                // Crear un objeto PreparedStatement
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                // Establecer los valores para los parámetros
                preparedStatement.setInt(1, nuevaCantidad);
                preparedStatement.setInt(2, id);

                // Ejecutar la actualización
                int rowsAffected = preparedStatement.executeUpdate();

                // Verificar cuántas filas fueron afectadas
                System.out.println("Filas afectadas: " + rowsAffected);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Cerrar la conexión
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void actualizarEstadoOrdenServicio(int id, int nuevoEstado) {
        // Obtener la conexión a la base de datos
        Connection connection = connect();
        if (connection != null) {
            try {
                // Crear la consulta de actualización
                String query = "UPDATE ordenes_de_servicio SET IdEstado = ? WHERE id_orden = ?";

                // Crear un objeto PreparedStatement
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                // Establecer los valores para los parámetros
                preparedStatement.setInt(1, nuevoEstado);
                preparedStatement.setInt(2, id);

                // Ejecutar la actualización
                int rowsAffected = preparedStatement.executeUpdate();

                // Verificar cuántas filas fueron afectadas
                System.out.println("Filas afectadas: " + rowsAffected);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Cerrar la conexión
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public List<VerPqrs> obtenerPqrs(){
        List<VerPqrs> listPqrs = new ArrayList<>();
        Connection connection = connect();
        if (connection != null){
            try {
                String query = "SELECT * FROM pqrs WHERE Estado = 1";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()){
                    int idpqrs = resultSet.getInt("idpqrs");
                    Date fechaRegistro = resultSet.getDate("FechaRegistro");
                    String tipopqrs = resultSet.getString("Tipopqrs");
                    String canal = resultSet.getString("Canal");
                    String nombre = resultSet.getString("Nombre");
                    String apellido = resultSet.getString("Apellido");
                    String tipoDoc = resultSet.getString("TipoDoc");
                    String documento = resultSet.getString("Documento");
                    String barrioUsuario = resultSet.getString("BarrioUsuario");
                    String direccionUsuario = resultSet.getString("DireccionUsuario");
                    String correo = resultSet.getString("Correo");
                    String referencia = resultSet.getString("Referencia");
                    String direccionAfectacion = resultSet.getString("DireccionAfectacion");
                    String barrioAfectacion = resultSet.getString("BarrioAfectacion");
                    String tipoAlumbrado = resultSet.getString("TipoAlumbrado");
                    String descripcionAfectacion = resultSet.getString("DescripcionAfectacion");
                    int estado = resultSet.getInt("Estado");
                    String telefono = resultSet.getString("Telefono");

                    listPqrs.add(new VerPqrs(idpqrs, fechaRegistro, tipopqrs, canal, nombre, apellido, tipoDoc, documento, barrioUsuario,
                            direccionUsuario, correo, referencia, direccionAfectacion, barrioAfectacion, tipoAlumbrado,
                            descripcionAfectacion, estado, telefono));

                }
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return listPqrs;
    }
    public byte[] obtenerFoto(String campo, String tabla, int id){
        byte[] imagen = null;
        Connection connection = connect();
        if (connection != null){
            try {
                String query = "SELECT " + campo + " FROM " + tabla + " WHERE Idpqrs = " + "'" + id + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    imagen = resultSet.getBytes("Imagen");
                }
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return imagen;
    }
}
