package com.example.lightcontrol_app.controllerDB;

import com.example.lightcontrol_app.Modelo_RecycleView.CampoInfo;

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
    public List<CampoInfo> obtenerInfoOrden(int id) {
        List<CampoInfo> infoOrden = new ArrayList<>();
        Connection connection = connect();
        if (connection != null) {
            try {
                String query = "SELECT * FROM ordenes_de_servicio WHERE id_orden = " + id;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                if (resultSet.next()) {
                    infoOrden.add(new CampoInfo("ID", String.valueOf(resultSet.getInt("id_orden"))));
                    infoOrden.add(new CampoInfo("Tipo de Elemento", resultSet.getString("Tipo_de_elemento")));
                    infoOrden.add(new CampoInfo("Código de Elemento", resultSet.getString("codigo_de_elemento")));
                    infoOrden.add(new CampoInfo("Pqrs Relacionada", resultSet.getString("elemento_relacionado")));
                    infoOrden.add(new CampoInfo("Código de Orden", resultSet.getString("codigo_orden")));
                    infoOrden.add(new CampoInfo("Problema Relacionado", resultSet.getString("problema_relacionado")));
                    infoOrden.add(new CampoInfo("Problema Validado", resultSet.getString("problema_validado")));
                    infoOrden.add(new CampoInfo("Orden Prioridad", resultSet.getString("Orden_prioridad")));
                    infoOrden.add(new CampoInfo("Prioridad de Ruta", String.valueOf(resultSet.getInt("prioridad_de_ruta"))));
                    infoOrden.add(new CampoInfo("Fecha a Realizar", resultSet.getDate("fecha_a_realizar").toString()));
                    infoOrden.add(new CampoInfo("Cuadrilla", resultSet.getString("cuadrilla")));
                    infoOrden.add(new CampoInfo("Tipo de Orden", resultSet.getString("tipo_de_orden")));
                    infoOrden.add(new CampoInfo("Tipo de Solución", resultSet.getString("tipo_de_Solucion")));
                    infoOrden.add(new CampoInfo("Clase de Orden", resultSet.getString("clase_de_orden")));
                    infoOrden.add(new CampoInfo("Obra Relacionada", resultSet.getString("obra_relacionada")));
                    infoOrden.add(new CampoInfo("Estado", verificarEstado(resultSet.getInt("IdEstado"))));
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return infoOrden;
    }
    private String verificarEstado(int idEstado) {
        switch (idEstado){
            case 1: return "Sin asignar";
            case 2: return "En proceso";
            case 3: return "Cerrada";
            default: return "Error";
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

    //ordenando codigo
    public <T> List<T> obtenerDatos(String query, RowMapper<T> mapper) {
        List<T> dataList = new ArrayList<>();
        Connection connection = connect();
        if (connection != null) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    try {
                        T data = mapper.mapRow(resultSet);
                        dataList.add(data);
                    } catch (SQLException e) {
                        // Log the error, but continue processing
                        System.err.println("Error mapping row: " + e.getMessage());
                        // You might want to add a null or a partially filled object here
                        // dataList.add(null);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error executing query: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
        return dataList;
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

    public void actualizarDatos(String query, Object... params) {
        // Obtener la conexión a la base de datos
        Connection connection = connect();
        if (connection != null) {
            try {
                // Crear un objeto PreparedStatement
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                // Establecer los valores para los parámetros
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }

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



    // Ejemplo de RowMapper
    public interface RowMapper<T> {
        T mapRow(ResultSet resultSet) throws SQLException;
    }

}
