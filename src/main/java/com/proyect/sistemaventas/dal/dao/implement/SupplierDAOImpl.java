package com.proyect.sistemaventas.dal.dao.implement;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.proyect.sistemaventas.dal.DatabaseConnection;
import com.proyect.sistemaventas.dal.dao.SupplierDAO;
import com.proyect.sistemaventas.model.Supplier;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAOImpl implements SupplierDAO {

    private Connection conn;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private final String create = "INSERT INTO proveedores (rut, nombre, telefono, email, direccion, razon_social) VALUES (?,?,?,?,?,?)";
    private final String selectOne = "SELECT * FROM proveedores WHERE rut = ?";
    private final String selectAll = "SELECT * FROM proveedores";
    private final String modify = "UPDATE proveedores SET rut=?,nombre=?,telefono=?,email=?,direccion=?,razon_social=? WHERE id_proveedores = ?";
    private final String erase = "DELETE FROM proveedores WHERE id_proveedores = ?";

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
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(modify);
            ps.setString(1, t.getRut());
            ps.setString(2, t.getName());
            ps.setString(3, t.getPhoneNumber());
            ps.setString(4, t.getEmail());
            ps.setString(5, t.getAddress());
            ps.setString(6, t.getRazonSocial());
            ps.setInt(7, t.getIdSupplier());
            if (ps.executeUpdate() > 0) {
                return 1;
            } else {
                return 2;
            }
        } catch (SQLIntegrityConstraintViolationException ex) { // CDU: RUT ya registrado, dato Unique Index
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
    public int delete(Supplier t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(erase);
            ps.setInt(1, t.getIdSupplier());
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
    public int findById(Supplier t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(selectOne);
            ps.setString(1, t.getRut());
            rs = ps.executeQuery();
            if (rs.next()) {
                t.setIdSupplier(rs.getInt("id_proveedores"));
                t.setRut(rs.getString("rut"));
                t.setName(rs.getString("nombre"));
                t.setPhoneNumber(rs.getString("telefono"));
                t.setEmail(rs.getString("email"));
                t.setAddress(rs.getString("direccion"));
                t.setRazonSocial(rs.getString("razon_social"));
                t.setDate(rs.getDate("fecha"));
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
    public List<Supplier> findAll(Supplier t) {
        conn = DatabaseConnection.getInstance().getConnection();
        List<Supplier> supplierList = new ArrayList<>();
        try {
            ps = conn.prepareStatement(selectAll);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_proveedores");
                String rut = rs.getString("rut");
                String name = rs.getString("nombre");
                String phoneNumber = rs.getString("telefono");
                String email = rs.getString("email");
                String address = rs.getString("direccion");
                String razonSocial = rs.getString("razon_social");
                Date date = rs.getDate("fecha");
                t = new Supplier(id, rut, name, phoneNumber, email, address, razonSocial, date);
                supplierList.add(t);
            }
        } catch (SQLException ex) {
            System.err.println("No se pudo realizar la conexión, " + ex);
        } finally {
            closeResources(ps, rs);
            DatabaseConnection.getInstance().closeConnection();
        }
        return supplierList;
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
