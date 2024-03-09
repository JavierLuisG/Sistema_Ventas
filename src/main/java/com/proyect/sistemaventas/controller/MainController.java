package com.proyect.sistemaventas.controller;

import com.proyect.sistemaventas.dal.dao.implement.CustomerDAOImpl;
import com.proyect.sistemaventas.dal.dao.implement.UserDAOImpl;
import com.proyect.sistemaventas.model.Customer;
import com.proyect.sistemaventas.model.User;
import com.proyect.sistemaventas.view.LoginView;
import com.proyect.sistemaventas.view.SistemaPrincipalView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class MainController implements ActionListener {

    private LoginView loginView;
    private User user;
    private UserDAOImpl userImpl;
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
        /* Inicializar customer y customerImpl y generar las acciones de los botones en de Clientes */
        customer = new Customer();
        customerImpl = new CustomerDAOImpl();
        pagePrincipal.btnGuardarClientes.addActionListener(this);
    }

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
