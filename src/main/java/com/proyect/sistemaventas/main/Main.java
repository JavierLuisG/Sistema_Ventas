package com.proyect.sistemaventas.main;

import com.proyect.sistemaventas.controller.MainController;
import com.proyect.sistemaventas.view.LoginView;

public class Main {
    
    public static void main(String[] args) {
        
        LoginView loginView = new LoginView();
        MainController controller = new MainController(loginView);
        controller.start();
    }
}
