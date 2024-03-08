package com.proyect.sistemaventas.dal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private String user;
    private String password;
    private String url;

    static DatabaseConnection instance = null; // Instancia de la clase para solo tener una
    private Connection conn; // Instancia que permite la conexión a la base de datos
    private final Properties properties = new Properties(); // Instancia que permite obtener las propiedades 
    private InputStream stream; // Instancia que permite acceder al archivo de propiedades que contiene los datos para la conexión a la base de datos

    /**
     * Para implementar correctamente el patrón Singleton en una clase, se debe
     * hacer el constructor de la clase privado para prevenir la creación de
     * nuevas instancias utilizando el operador `new`. Luego, se crea un método
     * estático `getInstance()` que cree y devuelva la única instancia permitida
     * de la clase si aún no existe, y simplemente devuelva la instancia
     * existente en lugar de crear una nueva si ya ha sido creada.
     */
    public DatabaseConnection() {
    }

    /**
     * @return devolver la conexión a la base de datos
     */
    public Connection getConnection() {
        try {
            loadProperties();
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            System.err.println("No se pudo realizar la conexión, " + ex);
        }
        return conn;
    }

    /**
     * El método loadPropertiesDB() se encarga de cargar las propiedades de
     * conexión desde el archivo applicationDB.properties
     */
    public void loadProperties() {
        stream = getClass().getClassLoader().getResourceAsStream("applicationDB.properties");
        try {
            properties.load(stream);
            user = properties.getProperty("username");
            password = properties.getProperty("password");
            String hostname = properties.getProperty("hostname");
            String port = properties.getProperty("port");
            String database = properties.getProperty("database");
            url = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
        } catch (IOException ex) {
            System.err.println("Error, loading properties, " + ex);
        }
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión");
            }
        }
    }

    /**
     * @return la única instancia permitida de la clase DatabaseConnection
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            return instance = new DatabaseConnection();
        } else {
            return instance;
        }
    }
}
