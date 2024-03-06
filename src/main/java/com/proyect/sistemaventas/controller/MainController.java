package com.proyect.sistemaventas.controller;

import com.proyect.sistemaventas.view.LoginView;

public class MainController {
    
    private LoginView loginView;

    public MainController(LoginView loginView) {
        this.loginView = loginView;
    }
    
    /**
     * Método para comenzar la ventana
     */
    public void start() {
        loginView.setResizable(false);
        loginView.setLocationRelativeTo(null);
        loginView.setVisible(true);
    }
}
