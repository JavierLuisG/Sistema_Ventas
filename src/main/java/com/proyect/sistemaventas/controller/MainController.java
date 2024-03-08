package com.proyect.sistemaventas.controller;

import com.proyect.sistemaventas.dal.dao.implement.UserDAOImpl;
import com.proyect.sistemaventas.model.User;
import com.proyect.sistemaventas.view.LoginView;
import com.proyect.sistemaventas.view.SistemaPrincipalView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class MainController implements ActionListener{
    
    private LoginView loginView;
    private User user;
    private UserDAOImpl userImpl;
    private SistemaPrincipalView pagePrincipal; 

    /**
     * Constructor del MainController
     * @param loginView 
     */
    public MainController(LoginView loginView) {
        this.loginView = loginView;
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
    }
}
