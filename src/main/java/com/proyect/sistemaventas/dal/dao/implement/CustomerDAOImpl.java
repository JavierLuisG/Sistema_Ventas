package com.proyect.sistemaventas.dal.dao.implement;

import com.proyect.sistemaventas.dal.DatabaseConnection;
import com.proyect.sistemaventas.dal.dao.CustomerDAO;
import com.proyect.sistemaventas.model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    private Connection conn;
    private PreparedStatement ps;

    private final String create = "INSERT INTO clientes (identificacion, nombre, telefono, email, direccion, razon_social) values (?,?,?,?,?,?)";

    @Override
    public int insert(Customer t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(create);
            ps.setInt(1, t.getIdentification());
            ps.setString(2, t.getName());
            ps.setString(3, t.getPhoneNumber());
            ps.setString(4, t.getEmail());
            ps.setString(5, t.getAddress());
            ps.setString(6, t.getRazonSocial());
            if (ps.executeUpdate() > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            return 2;
        } catch (SQLException ex) {
            System.err.println("No se pudo realizar la conexión, " + ex);
            return 0;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("No se cerró el PreparedStatement");
                }
            }
            DatabaseConnection.getInstance().closeConnection();
        }
    }

    @Override
    public int update(Customer t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int delete(Customer t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int findById(Customer t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Customer> findAll(Customer t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
