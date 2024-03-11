package com.proyect.sistemaventas.dal.dao.implement;

import com.proyect.sistemaventas.dal.DatabaseConnection;
import com.proyect.sistemaventas.dal.dao.SupplierDAO;
import com.proyect.sistemaventas.model.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class SupplierDAOImpl implements SupplierDAO {

    private Connection conn;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    
    private final String create = "INSERT INTO proveedores (rut, nombre, telefono, email, direccion, razon_social) values (?,?,?,?,?,?)";

    @Override
    public int insert(Supplier t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(create);
            ps.setString(1, t.getRut());
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
        } catch (SQLIntegrityConstraintViolationException ex) { // CDU: RUT ya registrado, dato Unique Index
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
    public int update(Supplier t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int delete(Supplier t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int findById(Supplier t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Supplier> findAll(Supplier t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
