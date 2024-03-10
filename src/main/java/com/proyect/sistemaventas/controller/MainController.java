package com.proyect.sistemaventas.controller;

import com.proyect.sistemaventas.dal.dao.implement.CustomerDAOImpl;
import com.proyect.sistemaventas.dal.dao.implement.UserDAOImpl;
import com.proyect.sistemaventas.model.Customer;
import com.proyect.sistemaventas.model.User;
import com.proyect.sistemaventas.view.LoginView;
import com.proyect.sistemaventas.view.SistemaPrincipalView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class MainController implements ActionListener {

    private final LoginView loginView;
    private final User user;
    private final UserDAOImpl userImpl;
    private Customer customer;
    private CustomerDAOImpl customerImpl;
    private SistemaPrincipalView pagePrincipal;

    /* Variables en relacion a Customer */
    private int identification;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String razonSocial;
    private final DefaultTableModel tableModelCustomer = new DefaultTableModel();

    /**
     * Constructor del MainController
     *
     * @param loginView
     */
    public MainController(LoginView loginView) {
        this.loginView = loginView;
        /* Inicializar user y userImpl y generar las acciones de los botones en el login */
        user = new User(); // Se inicializa con el constructor
        userImpl = new UserDAOImpl(); // Se inicializa con el constructor
        loginView.btnLogin.addActionListener(this); // Agrega acción al boton Iniciar Sesión del login
    }

    /**
     * Método para comenzar la ventana del login
     */
    public void startLogin() {
        loginView.setResizable(false);
        loginView.setLocationRelativeTo(null);
        loginView.setVisible(true);
    }

    /**
     * Permite inicializar el la página principal del sistema de ventas
     */
    public void startPagePrincipal() {
        pagePrincipal = new SistemaPrincipalView();
        pagePrincipal.setResizable(false);
        pagePrincipal.setLocationRelativeTo(null);
        pagePrincipal.setVisible(true);
        /* Inicializar customer y customerImpl y generar las acciones de los botones de Clientes */
        customer = new Customer();
        customerImpl = new CustomerDAOImpl();
        pagePrincipal.btnGuardarClientes.addActionListener(this);
        /* Asignar el model a la tabla correspondiente y asignar las columnas con sus filas*/
        pagePrincipal.tableClientes.setModel(tableModelCustomer);
        loadModelCustomer();
        pagePrincipal.tableClientes.addMouseListener(adapter); // Dar acción al mouse para seleccionar la fila de la tabla
        pagePrincipal.tableClientes.setEnabled(false); // No permite modificar los valores en la tabla
    }

    /**
     * Permite establecer las columnas con su respectivo nombre
     */
    public void loadModelCustomer() {
        tableModelCustomer.addColumn("Identificación");
        tableModelCustomer.addColumn("Nombre");
        tableModelCustomer.addColumn("Teléfono");
        tableModelCustomer.addColumn("Email");
        tableModelCustomer.addColumn("Dirección");
        tableModelCustomer.addColumn("Razón social");
        addListTableModelCustomer(); // Asignar las filas según los datos traidos de la base de datos
    }

    /**
     * Permite agregar los registros obtenidos del findAll a la tabla Clientes
     */
    public void addListTableModelCustomer() {
        List<Customer> listCustomer = customerImpl.findAll(customer);
        tableModelCustomer.setRowCount(0);
        for (Customer cstmr : listCustomer) {
            identification = cstmr.getIdentification();
            name = cstmr.getName();
            phoneNumber = cstmr.getPhoneNumber();
            email = cstmr.getEmail();
            address = cstmr.getAddress();
            razonSocial = cstmr.getRazonSocial();
            Object[] row = {identification, name, phoneNumber, email, address, razonSocial};
            tableModelCustomer.addRow(row);
        }
    }

    /**
     * Permite seleccionar la fila de la tabla y generar el evento... Así como
     * el ActionEvent para los botones
     */
    MouseAdapter adapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (pagePrincipal.tableClientes.rowAtPoint(e.getPoint()) != -1) {
                int index = pagePrincipal.tableClientes.rowAtPoint(e.getPoint());
                customer.setIdentification(Integer.parseInt(pagePrincipal.tableClientes.getValueAt(index, 0).toString()));
                switch (customerImpl.findById(customer)) {
                    case 1 -> {
                        pagePrincipal.fieldIdClientes.setText(String.valueOf(customer.getIdCustomer()));
                        pagePrincipal.fieldIdentificacionClientes.setText(String.valueOf(customer.getIdentification()));
                        pagePrincipal.fieldNombreClientes.setText(customer.getName());
                        pagePrincipal.fieldTelefonoClientes.setText(customer.getPhoneNumber());
                        pagePrincipal.fieldEmailClientes.setText(customer.getEmail());
                        pagePrincipal.fieldDireccionClientes.setText(customer.getAddress());
                        pagePrincipal.fieldRazonSocialClientes.setText(customer.getRazonSocial());
                    }
                    case 2 ->
                        JOptionPane.showMessageDialog(null, "Problema al seleccionar el registro");
                    case 0 ->
                        JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                }
            }
        }
    };

    /**
     * @param e permite capturar si se realiza una acción frente a un botón
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginView.btnLogin) {
            user.setEmail(loginView.fieldEmail.getText().trim());
            user.setPassword(String.valueOf(loginView.fieldPassword.getPassword()));
            switch (userImpl.findByEmailAndPassword(user)) {
                case 1 -> {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso");
                    startPagePrincipal(); // Al iniciar sesión se visualiza la interfaz de la pagina principal
                    loginView.dispose(); // Luego de ingresar al sistema el login se cierra
                }
                case 0 ->
                    JOptionPane.showMessageDialog(null, "Email o contraseña incorrectos");
            }
        }
        if (e.getSource() == pagePrincipal.btnGuardarClientes) {
            switch (validationEnteredDataCustomer()) { // Validación si es o no números ingresados
                case 1 -> {
                    customer.setIdentification(identification);
                    customer.setName(name);
                    customer.setPhoneNumber(phoneNumber);
                    customer.setEmail(email);
                    customer.setAddress(address);
                    customer.setRazonSocial(razonSocial);
                    switch (customerImpl.insert(customer)) {
                        case 1 ->
                            JOptionPane.showMessageDialog(null, "Registro cliente guardado");
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "N° identificación ya registrado");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "No se realizó el registro");
                    }
                    addListTableModelCustomer(); // De esta manera se actualizan los datos en la tabla cuando se realiza un registro
                }
                case 2 ->
                    JOptionPane.showMessageDialog(null, "Ingrese los valores solicitados");
                case 0 ->
                    JOptionPane.showMessageDialog(null, "Ingrese un N° identificación numérico");
            }
        }
    }

    /**
     *
     * @param text en String ya que es obtenido de un field
     * @return boolean para saber si se cumple o no que todos los caracteres
     * sean numéricos
     */
    private boolean isNumeric(String text) {
        return text.matches("\\d+");
    }

    /**
     * Este metodo va para Customer obteniendo los datos ingresados en las field
     * de Clientes
     *
     * @return int indicando cual ha sido la verificación correspondiente
     */
    public int validationEnteredDataCustomer() {
        if (isNumeric(pagePrincipal.fieldIdentificacionClientes.getText().trim())) {
            identification = Integer.parseInt(pagePrincipal.fieldIdentificacionClientes.getText().trim());
            name = pagePrincipal.fieldNombreClientes.getText().trim();
            phoneNumber = pagePrincipal.fieldTelefonoClientes.getText().trim();
            email = pagePrincipal.fieldEmailClientes.getText().trim();
            address = pagePrincipal.fieldDireccionClientes.getText().trim();
            razonSocial = pagePrincipal.fieldRazonSocialClientes.getText().trim();
            // Verificación de valores ingresados. NO se tiene en cuenta razonSocial ya que en la base de datos está por default null
            if (!name.isEmpty() && !phoneNumber.isEmpty() && !email.isEmpty()
                    && !address.isEmpty()) {
                return 1; // Retorna 1 si todos los valores están correctamente
            } else {
                return 2; // Retorna 2 si no ingresó los valores solicitados
            }
        } else {
            return 0; // Retorna 0 si no ingresó números en el N° identificacion
        }
    }
}
