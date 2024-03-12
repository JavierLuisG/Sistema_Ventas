package com.proyect.sistemaventas.dal.dao.implement;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.proyect.sistemaventas.dal.DatabaseConnection;
import com.proyect.sistemaventas.dal.dao.CustomerDAO;
import com.proyect.sistemaventas.model.Customer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    private Connection conn;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private final String create = "INSERT INTO clientes (identificacion, nombre, telefono, email, direccion, razon_social) values (?,?,?,?,?,?)";
    private final String selectOne = "SELECT * FROM clientes WHERE identificacion = ?";
    private final String selectAll = "SELECT * FROM clientes";
    private final String modify = "UPDATE clientes SET identificacion=?, nombre=?, telefono=?,email=?,direccion=?,razon_social=? WHERE id_cliente = ?";
    private final String erase = "DELETE FROM clientes WHERE id_cliente = ?";

    @Override
    public int insert(Customer t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(create);
            ps.setString(1, t.getIdentification());
            ps.setString(2, t.getName());
            ps.setString(3, t.getPhoneNumber());
            ps.setString(4, t.getEmail());
            ps.setString(5, t.getAddress());
            ps.setString(6, t.getRazonSocial());
            if (ps.executeUpdate() > 0) {
                return 1;
            } else {
                return 2;
            }
        } catch (SQLIntegrityConstraintViolationException ex) { // CDU: N° identificación ya registrado, dato Unique Index
            return 3;
        } catch (SQLException ex) {
            System.err.println("No se pudo realizar la conexión, " + ex);
            return 0;
        } finally {
            closeResources(ps, rs);
            DatabaseConnection.getInstance().closeConnection();
        }
    }

    @Override
    public int update(Customer t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(modify);
            ps.setString(1, t.getIdentification());
            ps.setString(2, t.getName());
            ps.setString(3, t.getPhoneNumber());
            ps.setString(4, t.getEmail());
            ps.setString(5, t.getAddress());
            ps.setString(6, t.getRazonSocial());
            ps.setInt(7, t.getIdCustomer());
            if (ps.executeUpdate() > 0) {
                return 1;
            } else {
                return 2; // CDU: realiza un registro y de inmediato hace una actualización (no tiene seleccionado un id_cliente)
            }
        } catch (SQLIntegrityConstraintViolationException ex) { // CDU: N° identificación ya registrado, dato Unique Index
            return 3;
        } catch (MysqlDataTruncation ex) { // CDU: si ingresa mas de los caracteres permitidos
            return 4;
        } catch (SQLException ex) {
            System.err.println("No se pudo realizar la conexión, " + ex);
            return 0;
        } finally {
            closeResources(ps, rs);
            DatabaseConnection.getInstance().closeConnection();
        }
    }

    @Override
    public int delete(Customer t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(erase);
            ps.setInt(1, t.getIdCustomer());
            if (ps.executeUpdate() > 0) {
                return 1;
            } else {
                return 2;
            }
        } catch (SQLException ex) {
            System.err.println("No se pudo realizar la conexión, " + ex);
            return 0;
        } finally {
            closeResources(ps, rs);
            DatabaseConnection.getInstance().closeConnection();
        }
    }

    @Override
    public int findById(Customer t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(selectOne);
            ps.setString(1, t.getIdentification());
            rs = ps.executeQuery();
            if (rs.next()) {
                t.setIdCustomer(rs.getInt("id_cliente"));
                t.setIdentification(rs.getString("identificacion"));
                t.setName(rs.getString("nombre"));
                t.setPhoneNumber(rs.getString("telefono"));
                t.setEmail(rs.getString("email"));
                t.setAddress(rs.getString("direccion"));
                t.setRazonSocial(rs.getString("razon_social"));
                return 1;
            } else {
                return 2; // CDU: puede aparecer en la tabla pero en la base de datos no existe ese registro
            }
        } catch (SQLException ex) {
            System.err.println("No se pudo realizar la conexión, " + ex);
            return 0;
        } finally {
            closeResources(ps, rs);
            DatabaseConnection.getInstance().closeConnection();
        }
    }

    @Override
    public List<Customer> findAll(Customer t) {
        conn = DatabaseConnection.getInstance().getConnection();
        List<Customer> customerList = new ArrayList<>();
        try {
            ps = conn.prepareStatement(selectAll);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_cliente");
                String identification = rs.getString("identificacion");
                String name = rs.getString("nombre");
                String phoneNumber = rs.getString("telefono");
                String email = rs.getString("email");
                String address = rs.getString("direccion");
                String razonSocial = rs.getString("razon_social");
                Date date = rs.getDate("fecha");
                t = new Customer(id, identification, name, phoneNumber, email, address, razonSocial, date);
                customerList.add(t);
            }
        } catch (SQLException ex) {
            System.err.println("No se pudo realizar la conexión, " + ex);
        } finally {
            closeResources(ps, rs);
            DatabaseConnection.getInstance().closeConnection();
        }
        return customerList;
    }

    private void closeResources(PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                if (!rs.isClosed()) { // Permite cerrar si antes no ha sido cerrada 
                    rs.close();
                }
            } catch (SQLException e) {
                System.err.println("No se cerró el ResultSet");
            }
        }
        if (ps != null) {
            try {
                if (!ps.isClosed()) { // Permite cerrar si antes no ha sido cerrada
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println("No se cerró el PreparedStatement");
            }
        }
    }
}
