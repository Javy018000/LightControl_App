package com.example.lightcontrol_app;

import com.example.lightcontrol_app.Modelo_RecycleView.OrdenServicio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BaseDeDatosAux {
    private static final String URL = "jdbc:jtds:sqlserver://luminarias.mssql.somee.com;databaseName=luminarias";
    private static final String USER = "JonathanFLF_SQLLogin_1";
    private static final String PASSWORD = "zgya9wozpl";

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

    public List<OrdenServicio> getAllOrders() {
        List<OrdenServicio> orderList = new ArrayList<>();
        Connection connection = connect();
        if (connection != null) {
            try {
                String query = "SELECT problema_relacionado, id_orden FROM ordenes_de_servicio"; // Reemplaza 'OrdersTable' con tu tabla
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String problema = resultSet.getString("problema_relacionado");
                    String idOrden = resultSet.getString("id_orden");
                    OrdenServicio order = new OrdenServicio(idOrden, problema);
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
}
