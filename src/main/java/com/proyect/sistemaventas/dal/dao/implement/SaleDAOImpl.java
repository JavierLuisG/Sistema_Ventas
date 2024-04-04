package com.proyect.sistemaventas.dal.dao.implement;

import com.proyect.sistemaventas.dal.DatabaseConnection;
import com.proyect.sistemaventas.dal.dao.SaleDAO;
import com.proyect.sistemaventas.model.Sale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SaleDAOImpl implements SaleDAO {

    private Connection conn;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private final String create = "INSERT INTO ventas (cliente, vendedor, total) VALUES (?,?,?)";

    @Override
    public int insert(Sale t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(create);
            ps.setInt(1, t.getCustomer()); // Se maneja la lógica por medio del idCustomer
            ps.setInt(2, t.getSeller()); // Se maneja la lógica por medio del idUser
            ps.setInt(3, t.getTotalSale());
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
    public int update(Sale t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int delete(Sale t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int findById(Sale t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Sale> findAll(Sale t) {
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
