package com.proyect.sistemaventas.controller;

import com.proyect.sistemaventas.dal.dao.implement.CustomerDAOImpl;
import com.proyect.sistemaventas.dal.dao.implement.SupplierDAOImpl;
import com.proyect.sistemaventas.dal.dao.implement.UserDAOImpl;
import com.proyect.sistemaventas.model.Customer;
import com.proyect.sistemaventas.model.Supplier;
import com.proyect.sistemaventas.model.User;
import com.proyect.sistemaventas.view.LoginView;
import com.proyect.sistemaventas.view.SystemPrincipalView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class MainController implements ActionListener {

    // Views
    private final LoginView loginView;
    private SystemPrincipalView systemPrincipal;

    // User
    private final User user;
    private final UserDAOImpl userImpl;

    // Customer
    private Customer customer;
    private CustomerDAOImpl customerImpl;

    // Supplier
    private Supplier supplier;
    private SupplierDAOImpl supplierImpl;

    /* Variables de Customer */
    private String identificationCustomer;
    private String nameCustomer;
    private String phoneNumberCustomer;
    private String emailCustomer;
    private String addressCustomer;
    private String razonSocialCustomer;
    private final DefaultTableModel tableModelCustomer = new DefaultTableModel();

    /* Variables de Supplier */
    private String rutSupplier;
    private String nameSupplier;
    private String phoneNumberSupplier;
    private String emailSupplier;
    private String addressSupplier;
    private String razonSocialSupplier;
    private final DefaultTableModel tableModelSupplier = new DefaultTableModel();

    /**
     * Constructor del MainController
     *
     * @param loginView
     */
    public MainController(LoginView loginView) {
        this.loginView = loginView;
        systemPrincipal = new SystemPrincipalView();
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
     * Permite inicializar la página principal del sistema de ventas y los
     * modelos
     */
    private void startSystemPrincipal() {
        systemPrincipal = new SystemPrincipalView();
        systemPrincipal.setResizable(false);
        systemPrincipal.setLocationRelativeTo(null);
        systemPrincipal.setVisible(true);
        startCustomer();
        startSupplier();
    }

    /**
     * Inicializar customer y customerImpl y generar las acciones de los botones
     * de Clientes
     */
    private void startCustomer() {
        customer = new Customer();
        customerImpl = new CustomerDAOImpl();
        systemPrincipal.btnGuardarClientes.addActionListener(this);
        systemPrincipal.btnActualizarClientes.addActionListener(this);
        systemPrincipal.btnEliminarClientes.addActionListener(this);
        systemPrincipal.btnLimpiarFieldsClientes.addActionListener(this);
        /* Asignar el model a la tabla correspondiente y asignar las columnas con sus filas*/
        systemPrincipal.tableClientes.setModel(tableModelCustomer);
        loadModelCustomer();
        systemPrincipal.tableClientes.addMouseListener(adapterCustomer); // Dar acción al mouse para seleccionar la fila de la tabla
        systemPrincipal.tableClientes.setEnabled(false); // No permite modificar los valores en la tabla
    }

    /**
     * Inicializar supplier y supplierImpl y generar las acciones de los botones
     * de Proveedor
     */
    private void startSupplier() {
        supplier = new Supplier();
        supplierImpl = new SupplierDAOImpl();
        systemPrincipal.btnGuardarProveedor.addActionListener(this);
        systemPrincipal.btnActualizarProveedor.addActionListener(this);
        systemPrincipal.btnEliminarProveedor.addActionListener(this);
        systemPrincipal.btnLimpiarFieldsProveedor.addActionListener(this);
        systemPrincipal.tableProveedor.setModel(tableModelSupplier);
        loadModelSupplier();
        systemPrincipal.tableProveedor.addMouseListener(adapterSupplier);
        systemPrincipal.tableProveedor.setEnabled(false);
    }

    /**
     * Permite establecer las columnas con su respectivo nombre para Customer o
     * tabla clientes
     */
    private void loadModelCustomer() {
        tableModelCustomer.addColumn("Identificación");
        tableModelCustomer.addColumn("Nombre");
        tableModelCustomer.addColumn("Teléfono");
        tableModelCustomer.addColumn("Email");
        tableModelCustomer.addColumn("Dirección");
        tableModelCustomer.addColumn("Razón social");
        addListTableModelCustomer(); // Asignar las filas según los datos traidos de la base de datos
    }

    /**
     * Permite establecer las columnas con su respectivo nombre para Supplier o
     * tabla proveedor
     */
    private void loadModelSupplier() {
        tableModelSupplier.addColumn("RUT");
        tableModelSupplier.addColumn("Nombre");
        tableModelSupplier.addColumn("Teléfono");
        tableModelSupplier.addColumn("Email");
        tableModelSupplier.addColumn("Dirección");
        tableModelSupplier.addColumn("Razón social");
        addListTableModelSupplier();
    }

    /**
     * Permite agregar los registros obtenidos del findAll a la tabla Clientes
     */
    private void addListTableModelCustomer() {
        List<Customer> listCustomer = customerImpl.findAll(customer);
        tableModelCustomer.setRowCount(0); // Cuando se ejecuta permite comenzar las filas desde la posicion 0, asi no se repiten cada vez que se llame
        for (Customer cstmr : listCustomer) {
            identificationCustomer = cstmr.getIdentification();
            nameCustomer = cstmr.getName();
            phoneNumberCustomer = cstmr.getPhoneNumber();
            emailCustomer = cstmr.getEmail();
            addressCustomer = cstmr.getAddress();
            razonSocialCustomer = cstmr.getRazonSocial();
            Object[] row = {identificationCustomer, nameCustomer, phoneNumberCustomer, emailCustomer, addressCustomer, razonSocialCustomer};
            tableModelCustomer.addRow(row);
        }
    }

    /**
     * Permite agregar los registros obtenidos del findAll a la tabla Proveedor
     */
    private void addListTableModelSupplier() {
        List<Supplier> listSupplier = supplierImpl.findAll(supplier);
        tableModelSupplier.setRowCount(0); // Cuando se ejecuta permite comenzar las filas desde la posicion 0, asi no se repiten cada vez que se llame
        for (Supplier sup : listSupplier) {
            rutSupplier = sup.getRut();
            nameSupplier = sup.getName();
            phoneNumberSupplier = sup.getPhoneNumber();
            emailSupplier = sup.getEmail();
            addressSupplier = sup.getAddress();
            razonSocialSupplier = sup.getRazonSocial();
            Object[] row = {rutSupplier, nameSupplier, phoneNumberSupplier, emailSupplier, addressSupplier, razonSocialSupplier};
            tableModelSupplier.addRow(row);
        }
    }

    /**
     * Permite seleccionar la fila de la tabla CLIENTES y generar el evento...
     * Así como el ActionEvent para los botones. Para eliminar un registro lo
     * hago por medio del identification de Customer.
     */
    MouseAdapter adapterCustomer = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (systemPrincipal.tableClientes.rowAtPoint(e.getPoint()) != -1) {
                int index = systemPrincipal.tableClientes.rowAtPoint(e.getPoint());
                customer.setIdentification(systemPrincipal.tableClientes.getValueAt(index, 0).toString());
                switch (customerImpl.findById(customer)) {
                    case 1 -> {
                        systemPrincipal.fieldIdClientes.setText(String.valueOf(customer.getIdCustomer()));
                        systemPrincipal.fieldIdentificacionClientes.setText(String.valueOf(customer.getIdentification()));
                        systemPrincipal.fieldNombreClientes.setText(customer.getName());
                        systemPrincipal.fieldTelefonoClientes.setText(customer.getPhoneNumber());
                        systemPrincipal.fieldEmailClientes.setText(customer.getEmail());
                        systemPrincipal.fieldDireccionClientes.setText(customer.getAddress());
                        systemPrincipal.fieldRazonSocialClientes.setText(customer.getRazonSocial());
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
     * Permite seleccionar la fila de la tabla PROVEEDOR y generar el evento...
     * Así como el ActionEvent para los botones. Para eliminar un registro lo
     * hago por medio del identification de Customer
     */
    MouseAdapter adapterSupplier = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (systemPrincipal.tableProveedor.rowAtPoint(e.getPoint()) != -1) {
                int index = systemPrincipal.tableProveedor.rowAtPoint(e.getPoint());
                supplier.setRut(systemPrincipal.tableProveedor.getValueAt(index, 0).toString());
                switch (supplierImpl.findById(supplier)) {
                    case 1 -> {
                        systemPrincipal.fieldIdProveedor.setText(String.valueOf(supplier.getIdSupplier()));
                        systemPrincipal.fieldRutProveedor.setText(supplier.getRut());
                        systemPrincipal.fieldNombreProveedor.setText(supplier.getName());
                        systemPrincipal.fieldTelefonoProveedor.setText(supplier.getPhoneNumber());
                        systemPrincipal.fieldEmailProveedor.setText(supplier.getEmail());
                        systemPrincipal.fieldDireccionProveedor.setText(supplier.getAddress());
                        systemPrincipal.fieldRazonSocialProveedor.setText(supplier.getRazonSocial());
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
        /**
         * ActionEvent del LOGIN
         */
        if (e.getSource() == loginView.btnLogin) {
            user.setEmail(loginView.fieldEmail.getText().trim());
            user.setPassword(String.valueOf(loginView.fieldPassword.getPassword()));
            switch (userImpl.findByEmailAndPassword(user)) {
                case 1 -> {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso");
                    startSystemPrincipal(); // Al iniciar sesión se visualiza la interfaz de la pagina principal
                    loginView.dispose(); // Luego de ingresar al sistema el login se cierra
                }
                case 0 ->
                    JOptionPane.showMessageDialog(null, "Email o contraseña incorrectos");
            }
        }
        /**
         * ActionEnvent de SYSTEMPRINCIPAL - Customer
         */
        if (e.getSource() == systemPrincipal.btnGuardarClientes) {
            switch (validationEnteredDataCustomer()) { // Validación si es o no números ingresados
                case 1 -> {
                    customer.setIdentification(identificationCustomer);
                    customer.setName(nameCustomer);
                    customer.setPhoneNumber(phoneNumberCustomer);
                    customer.setEmail(emailCustomer);
                    customer.setAddress(addressCustomer);
                    customer.setRazonSocial(razonSocialCustomer);
                    switch (customerImpl.insert(customer)) {
                        case 1 -> {
                            JOptionPane.showMessageDialog(null, "Registro cliente guardado");
                            toCleanCustomer();
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "No se realizó el registro");
                        case 3 ->
                            JOptionPane.showMessageDialog(null, "N° identificación ya registrado");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                    }
                    addListTableModelCustomer(); // De esta manera se actualizan los datos en la tabla cuando se realiza un registro
                }
                case 2 ->
                    JOptionPane.showMessageDialog(null, "Ingrese los valores solicitados");
                case 0 ->
                    JOptionPane.showMessageDialog(null, "Ingrese un N° identificación numérico");
            }
        }
        if (e.getSource() == systemPrincipal.btnActualizarClientes) {
            if (customer.getIdCustomer() != 0) { // Confirma que primero haya sido seleccionado un registro
                switch (validationEnteredDataCustomer()) {
                    case 1 -> {
                        customer.setIdentification(identificationCustomer);
                        customer.setName(nameCustomer);
                        customer.setPhoneNumber(phoneNumberCustomer);
                        customer.setEmail(emailCustomer);
                        customer.setAddress(addressCustomer);
                        customer.setRazonSocial(razonSocialCustomer);
                        switch (customerImpl.update(customer)) {
                            case 1 -> {
                                JOptionPane.showMessageDialog(null, "Registro cliente actualizado");
                                toCleanCustomer();
                            }
                            case 2 ->
                                JOptionPane.showMessageDialog(null, "No se realizó la actualización");
                            case 3 ->
                                JOptionPane.showMessageDialog(null, "N° identificación ya registrado");
                            case 4 ->
                                JOptionPane.showMessageDialog(null, "Ingrese correctamente los valores solicitados");
                            case 0 ->
                                JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                        }
                        addListTableModelCustomer(); // De esta manera se actualizan los datos en la tabla cuando se realiza un registro
                    }
                    case 2 ->
                        JOptionPane.showMessageDialog(null, "Ingrese los valores solicitados");
                    case 0 ->
                        JOptionPane.showMessageDialog(null, "Ingrese un N° identificación numérico");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione primero un registro para actualizar");
            }
        }
        if (e.getSource() == systemPrincipal.btnEliminarClientes) {
            if (customer.getIdCustomer() != 0) { // confirma que primero haya sido seleccionado un registro
                if (JOptionPane.showConfirmDialog(null, "¿Seguro de eliminar el registro?", "Eliminar registro cliente", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    switch (customerImpl.delete(customer)) {
                        case 1 -> {
                            JOptionPane.showMessageDialog(null, "Registro de cliente con N° identificación " + customer.getIdentification() + " eliminado");
                            toCleanCustomer();
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "Problema al eliminar el registro");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problema en la conexión");
                    }
                    addListTableModelCustomer(); // De esta manera se actualizan los datos en la tabla cuando se realiza un registro
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione primero un registro para eliminar");
            }
        }
        if (e.getSource() == systemPrincipal.btnLimpiarFieldsClientes) {
            toCleanCustomer();
        }
        /**
         * ActionEnvent de SYSTEMPRINCIPAL - Supplier
         */
        if (e.getSource() == systemPrincipal.btnGuardarProveedor) {
            switch (validationEnteredDataSupplier()) {
                case 1 -> {
                    supplier.setRut(rutSupplier);
                    supplier.setName(nameSupplier);
                    supplier.setPhoneNumber(phoneNumberSupplier);
                    supplier.setEmail(emailSupplier);
                    supplier.setAddress(addressSupplier);
                    supplier.setRazonSocial(razonSocialSupplier);
                    switch (supplierImpl.insert(supplier)) {
                        case 1 -> {
                            JOptionPane.showMessageDialog(null, "Registro proveedor guardado");
                            toCleanSupplier();
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "No se realizó el registro");
                        case 3 ->
                            JOptionPane.showMessageDialog(null, "RUT ya registrado");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                    }
                    addListTableModelSupplier(); // De esta manera se actualizan los datos en la tabla cuando se realiza un registro                    
                }
                case 2 ->
                    JOptionPane.showMessageDialog(null, "Ingrese los valores solicitados");
                case 0 ->
                    JOptionPane.showMessageDialog(null, "Ingrese un RUT numérico");
            }
        }
        if (e.getSource() == systemPrincipal.btnActualizarProveedor) {
            if (supplier.getIdSupplier() != 0) { // Confirma que primero haya sido seleccionado un registro
                switch (validationEnteredDataSupplier()) {
                    case 1 -> {
                        supplier.setRut(rutSupplier);
                        supplier.setName(nameSupplier);
                        supplier.setPhoneNumber(phoneNumberSupplier);
                        supplier.setEmail(emailSupplier);
                        supplier.setAddress(addressSupplier);
                        supplier.setRazonSocial(razonSocialSupplier);
                        switch (supplierImpl.update(supplier)) {
                            case 1 -> {
                                JOptionPane.showMessageDialog(null, "Registro proveedor actualizado");
                                toCleanSupplier();
                            }
                            case 2 ->
                                JOptionPane.showMessageDialog(null, "No se realizó la actualización");
                            case 3 ->
                                JOptionPane.showMessageDialog(null, "N° identificación ya registrado");
                            case 4 ->
                                JOptionPane.showMessageDialog(null, "Ingrese correctamente los valores solicitados");
                            case 0 ->
                                JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                        }
                        addListTableModelSupplier(); // De esta manera se actualizan los datos en la tabla cuando se realiza un registro
                    }
                    case 2 ->
                        JOptionPane.showMessageDialog(null, "Ingrese los valores solicitados");
                    case 0 ->
                        JOptionPane.showMessageDialog(null, "Ingrese un RUT numérico");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione primero un registro para actualizar");
            }
        }
        if (e.getSource() == systemPrincipal.btnEliminarProveedor) {
            if (supplier.getIdSupplier() != 0) { // confirma que primero haya sido seleccionado un registro
                if (JOptionPane.showConfirmDialog(null, "¿Seguro de eliminar el registro?", "Eliminar registro proveedor", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    switch (supplierImpl.delete(supplier)) {
                        case 1 -> {
                            JOptionPane.showMessageDialog(null, "Registro de proveedor con RUT " + supplier.getRut() + " eliminado");
                            toCleanSupplier();
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "Problema al eliminar el registro");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problema en la conexión");
                    }
                    addListTableModelSupplier(); // De esta manera se actualizan los datos en la tabla cuando se realiza un registro
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione primero un registro para eliminar");
            }
        }
        if (e.getSource() == systemPrincipal.btnLimpiarFieldsProveedor) {
            toCleanSupplier();
        }
    }

    /**
     * Limpiar los campos de Customer
     */
    private void toCleanCustomer() {
        systemPrincipal.fieldIdentificacionClientes.setText("");
        systemPrincipal.fieldNombreClientes.setText("");
        systemPrincipal.fieldTelefonoClientes.setText("");
        systemPrincipal.fieldEmailClientes.setText("");
        systemPrincipal.fieldDireccionClientes.setText("");
        systemPrincipal.fieldRazonSocialClientes.setText("");
        systemPrincipal.fieldIdClientes.setText("");
        // Quitar el valor de identification y idCustomer para que no pueda eliminar ni actualizar 
        customer.setIdCustomer(0);
        customer.setIdentification(null);
    }

    /**
     * Limpiar los campos de Supplier
     */
    private void toCleanSupplier() {
        systemPrincipal.fieldRutProveedor.setText("");
        systemPrincipal.fieldNombreProveedor.setText("");
        systemPrincipal.fieldTelefonoProveedor.setText("");
        systemPrincipal.fieldEmailProveedor.setText("");
        systemPrincipal.fieldDireccionProveedor.setText("");
        systemPrincipal.fieldRazonSocialProveedor.setText("");
        systemPrincipal.fieldIdProveedor.setText("");
        // Quitar el valor de identification y idCustomer para que no pueda eliminar ni actualizar 
        supplier.setIdSupplier(0);
        supplier.setRut(null);
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
     * Este método va para Customer, obteniendo los datos ingresados en las
     * field de Clientes
     *
     * @return int indicando cual ha sido la verificación correspondiente
     */
    private int validationEnteredDataCustomer() {
        if (isNumeric(systemPrincipal.fieldIdentificacionClientes.getText().trim())) {
            identificationCustomer = systemPrincipal.fieldIdentificacionClientes.getText().trim();
            nameCustomer = systemPrincipal.fieldNombreClientes.getText().trim();
            phoneNumberCustomer = systemPrincipal.fieldTelefonoClientes.getText().trim();
            emailCustomer = systemPrincipal.fieldEmailClientes.getText().trim();
            addressCustomer = systemPrincipal.fieldDireccionClientes.getText().trim();
            razonSocialCustomer = systemPrincipal.fieldRazonSocialClientes.getText().trim();
            // Verificación de valores ingresados. NO se tiene en cuenta razonSocial ya que en la base de datos está por default null
            if (!nameCustomer.isEmpty() && !phoneNumberCustomer.isEmpty() && !emailCustomer.isEmpty()
                    && !addressCustomer.isEmpty()) {
                return 1; // Retorna 1 si todos los valores están correctamente
            } else {
                return 2; // Retorna 2 si no ingresó los valores solicitados
            }
        } else {
            return 0; // Retorna 0 si no ingresó números en el N° identificación
        }
    }

    /**
     * Este método va para Supplier, obteniendo los datos ingresados en las
     * field de Proveedores
     *
     * @return int indicando cual ha sido la verificación correspondiente
     */
    private int validationEnteredDataSupplier() {
        if (isNumeric(systemPrincipal.fieldRutProveedor.getText().trim())) {
            rutSupplier = systemPrincipal.fieldRutProveedor.getText().trim();
            nameSupplier = systemPrincipal.fieldNombreProveedor.getText().trim();
            phoneNumberSupplier = systemPrincipal.fieldTelefonoProveedor.getText().trim();
            emailSupplier = systemPrincipal.fieldEmailProveedor.getText().trim();
            addressSupplier = systemPrincipal.fieldDireccionProveedor.getText().trim();
            razonSocialSupplier = systemPrincipal.fieldRazonSocialProveedor.getText().trim();
            // Verificación de valores ingresados.
            if (!nameSupplier.isEmpty() && !phoneNumberSupplier.isEmpty() && !emailSupplier.isEmpty()
                    && !addressSupplier.isEmpty() && !razonSocialSupplier.isEmpty()) {
                return 1; // Retorna 1 si todos los valores están correctamente
            } else {
                return 2; // Retorna 2 si no ingresó los valores solicitados
            }
        } else {
            return 0; // Retorna 0 si no ingresó números en el N° identificacion
        }
    }
}
