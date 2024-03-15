package com.proyect.sistemaventas.dal.dao.implement;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.proyect.sistemaventas.dal.DatabaseConnection;
import com.proyect.sistemaventas.dal.dao.ProductDAO;
import com.proyect.sistemaventas.model.Product;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    private Connection conn;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private final String create = "INSERT INTO productos (codigo, nombre, cantidad, precio, proveedor) VALUES (?,?,?,?,?)";
    private final String selectAll = "SELECT a.id_productos, a.codigo codigo, a.nombre nombre, a.cantidad cantidad, a.precio precio, b.nombre proveedor, a.fecha fecha FROM productos a INNER JOIN proveedores b ON a.proveedor = b.id_proveedores";
    private final String selectOne = "SELECT a.id_productos, a.codigo codigo, a.nombre nombre, a.cantidad cantidad, a.precio precio, b.nombre proveedor, a.fecha fecha FROM productos a INNER JOIN proveedores b ON a.proveedor = b.id_proveedores WHERE a.codigo = ?";
    private final String modify = "UPDATE productos SET codigo=?,nombre=?,cantidad=?,precio=?,proveedor=? WHERE id_productos=?";
    private final String erase = "DELETE FROM productos WHERE id_productos = ?";

    @Override
    public int insert(Product t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(create);
            ps.setString(1, t.getCode());
            ps.setString(2, t.getName());
            ps.setInt(3, t.getCount());
            ps.setFloat(4, t.getPrice());
            ps.setInt(5, Integer.parseInt(t.getSupplier())); // Se convierte en int para enviar a la base de datos 
            if (ps.executeUpdate() > 0) {
                return 1;
            } else {
                return 2;
            }
        } catch (SQLIntegrityConstraintViolationException ex) { // CDU: Código ya registrado, dato Unique Index
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
    public int update(Product t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(modify);
            ps.setString(1, t.getCode());
            ps.setString(2, t.getName());
            ps.setInt(3, t.getCount());
            ps.setFloat(4, t.getPrice());
            ps.setInt(5, Integer.parseInt(t.getSupplier()));
            ps.setInt(6, t.getIdProduct());
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
    public int delete(Product t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(erase);
            ps.setInt(1, t.getIdProduct());
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
    public int findById(Product t) {
        conn = DatabaseConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(selectOne);
            ps.setString(1, t.getCode());
            rs = ps.executeQuery();
            if (rs.next()) {
                t.setIdProduct(rs.getInt("id_productos"));
                t.setCode(rs.getString("codigo"));
                t.setName(rs.getString("nombre"));
                t.setCount(rs.getInt("cantidad"));
                t.setPrice(rs.getInt("precio"));
                t.setSupplier(rs.getString("proveedor"));
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
    public List<Product> findAll(Product t) {
        conn = DatabaseConnection.getInstance().getConnection();
        List<Product> productList = new ArrayList<>();
        try {
            ps = conn.prepareStatement(selectAll);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_productos");
                String code = rs.getString("codigo");
                String name = rs.getString("nombre");
                int count = rs.getInt("cantidad");
                int price = rs.getInt("precio");
                String supp = rs.getString("proveedor");
                Date date = rs.getDate("fecha");
                t = new Product(id, code, name, count, price, supp, date);
                productList.add(t);
            }
        } catch (SQLException ex) {
            System.err.println("No se pudo realizar la conexión, " + ex);
        } finally {
            closeResources(ps, rs);
            DatabaseConnection.getInstance().closeConnection();
        }
        return productList;
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
