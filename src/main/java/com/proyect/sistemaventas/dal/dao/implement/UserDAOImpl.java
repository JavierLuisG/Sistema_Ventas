package com.proyect.sistemaventas.dal.dao.implement;

import com.proyect.sistemaventas.dal.DatabaseConnection;
import com.proyect.sistemaventas.dal.dao.UserDAO;
import com.proyect.sistemaventas.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    private final String select = "SELECT * FROM usuarios WHERE email_usuario = ? AND password_usuario = ?";

    @Override
    public int findByEmailAndPassword(User t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(select);
            ps.setString(1, t.getEmail());
            ps.setString(2, t.getPassword());
            rs = ps.executeQuery();
            if (rs.next()) {
                t.setIdUser(rs.getInt("id_usuario"));
                t.setName(rs.getString("nombre"));
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            System.err.println("No se pudo realizar la conexión, " + ex);
            return 0;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("No se cerró el ResultSet");
                }
            }
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
}
