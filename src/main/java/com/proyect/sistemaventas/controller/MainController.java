package com.proyect.sistemaventas.controller;

import com.proyect.sistemaventas.dal.dao.implement.CustomerDAOImpl;
import com.proyect.sistemaventas.dal.dao.implement.ProductDAOImpl;
import com.proyect.sistemaventas.dal.dao.implement.SupplierDAOImpl;
import com.proyect.sistemaventas.dal.dao.implement.UserDAOImpl;
import com.proyect.sistemaventas.model.Customer;
import com.proyect.sistemaventas.model.Product;
import com.proyect.sistemaventas.model.Supplier;
import com.proyect.sistemaventas.model.User;
import com.proyect.sistemaventas.view.LoginView;
import com.proyect.sistemaventas.view.SystemPrincipalView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

    // Product
    private Product product;
    private ProductDAOImpl productImpl;

    /* Variables de Customer */
    private String identificationCustomer;
    private String nameCustomer;
    private String phoneNumberCustomer;
    private String emailCustomer;
    private String addressCustomer;
    private String razonSocialCustomer;
    private final DefaultTableModel tableModelCustomer = new DefaultTableModel();
    private List<Customer> listCustomer;

    /* Variables de Supplier */
    private String rutSupplier;
    private String nameSupplier;
    private String phoneNumberSupplier;
    private String emailSupplier;
    private String addressSupplier;
    private String razonSocialSupplier;
    private final DefaultTableModel tableModelSupplier = new DefaultTableModel();
    private List<Supplier> listSupplier; // La puse global para poder agregar los nombre al comboBox de productos

    /* Variables de Product */
    private String codeProduct;
    private String nameProduct;
    private int countProduct;
    private int priceProduct;
    private String supplierProduct;
    private final String firstItemComboBox = "Seleccionar";
    private final DefaultTableModel tableModelProduct = new DefaultTableModel();
    private List<Product> listProduct;

    /* Variables de New Sales -> Este no tiene un modelo */
    private boolean codeSelect = false; // Para verificar que primero haya sido ingresado un codigo, es decir, un producto
    private int countNewSales; // La cantidad de productos que quiere comprar el usuario
    private int totalByProduct; // Total a pagar en relación al producto, es decir, en la fila generada
    private final DefaultTableModel tableModelNewSales = new DefaultTableModel();

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
        systemPrincipal.fieldIdClientes.setVisible(false);
        systemPrincipal.fieldIdProveedor.setVisible(false);
        systemPrincipal.fieldIdProducto.setVisible(false);
        startCustomer();
        startSupplier();
        startProduct();
        startNewSale();
    }

    /**
     * Inicializar customer y customerImpl y generar las acciones de los botones
     * de Clientes
     */
    private void startCustomer() {
        customer = new Customer();
        customerImpl = new CustomerDAOImpl();
        systemPrincipal.btnGuardarClientes.addActionListener(this);
        systemPrincipal.btnBuscarClientes.addActionListener(this);
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
        systemPrincipal.btnBuscarProveedor.addActionListener(this);
        systemPrincipal.btnActualizarProveedor.addActionListener(this);
        systemPrincipal.btnEliminarProveedor.addActionListener(this);
        systemPrincipal.btnLimpiarFieldsProveedor.addActionListener(this);
        systemPrincipal.tableProveedor.setModel(tableModelSupplier);
        loadModelSupplier();
        systemPrincipal.tableProveedor.addMouseListener(adapterSupplier);
        systemPrincipal.tableProveedor.setEnabled(false);
    }

    /**
     * Inicializar product y productImpl y generar las acciones de los botones
     * de Producto
     */
    private void startProduct() {
        product = new Product();
        productImpl = new ProductDAOImpl();
        updateComoBoxSupplierOfProduct();
        systemPrincipal.btnGuardarProductos.addActionListener(this);
        systemPrincipal.btnBuscarProductos.addActionListener(this);
        systemPrincipal.btnActualizarProductos.addActionListener(this);
        systemPrincipal.btnEliminarProductos.addActionListener(this);
        systemPrincipal.btnLimpiarFieldsProductos.addActionListener(this);
        systemPrincipal.btnExcelProductos.addActionListener(this);
        systemPrincipal.tableProductos.setModel(tableModelProduct);
        loadModelProduct();
        systemPrincipal.tableProductos.addMouseListener(adapterProduct);
        systemPrincipal.tableProductos.setEnabled(false);
    }

    /**
     * No es necesario inicializar ya que se optione la información de product
     */
    private void startNewSale() {
        systemPrincipal.fieldCodigoNV.addKeyListener(adapterSalesCode);
        systemPrincipal.fieldCantidadNV.addKeyListener(adapterSalesCode);
        systemPrincipal.fieldProductoNV.setEditable(false);
        systemPrincipal.fieldStockNV.setEditable(false);
        systemPrincipal.fieldPrecioNV.setEditable(false);
        systemPrincipal.tableNV.setModel(tableModelNewSales);
        loadModelNewSales();
    }

    /**
     * Permite obtener los datos que se generaron en listSupplier y agregarlos
     * en el comboBoxProveedorProductos
     */
    private void updateComoBoxSupplierOfProduct() {
        systemPrincipal.comboBoxProveedorProductos.removeAllItems(); // Permite borrar la info anterior y asi mostrar la info actualizada
        systemPrincipal.comboBoxProveedorProductos.addItem(firstItemComboBox); // Primer item para diferenciar de los proveedores         
        // El for permite agregar el nombre de los proveedores al comboBox obtenidos de la listSupplier
        for (Supplier sup : listSupplier) {
            nameSupplier = sup.getName();
            systemPrincipal.comboBoxProveedorProductos.addItem(nameSupplier);
        }
    }

    /**
     * Permite establecer las columnas con su respectivo nombre para Customer o
     * tabla clientes
     */
    private void loadModelCustomer() {
        tableModelCustomer.addColumn("Identificación");
        tableModelCustomer.addColumn("Nombre");
        tableModelCustomer.addColumn("Email");
        tableModelCustomer.addColumn("Teléfono");
        tableModelCustomer.addColumn("Dirección");
        tableModelCustomer.addColumn("Razón social");
        int width[] = {75, 150, 180, 75, 120, 90};
        for (int i = 0; i < 6; i++) {
            systemPrincipal.tableClientes.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
        }
        addListTableModelCustomer(); // Asignar las filas según los datos traidos de la base de datos
    }

    /**
     * Permite establecer las columnas con su respectivo nombre para Supplier o
     * tabla proveedor
     */
    private void loadModelSupplier() {
        tableModelSupplier.addColumn("RUT");
        tableModelSupplier.addColumn("Nombre");
        tableModelSupplier.addColumn("Email");
        tableModelSupplier.addColumn("Teléfono");
        tableModelSupplier.addColumn("Dirección");
        tableModelSupplier.addColumn("Razón social");
        int width[] = {75, 150, 180, 75, 120, 90};
        for (int i = 0; i < 6; i++) {
            systemPrincipal.tableProveedor.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
        }
        addListTableModelSupplier();
    }

    /**
     * Permite establecer las columnas con su respectivo nombre para Product o
     * tabla productos
     */
    private void loadModelProduct() {
        tableModelProduct.addColumn("Código");
        tableModelProduct.addColumn("Nombre");
        tableModelProduct.addColumn("Proveedor");
        tableModelProduct.addColumn("Cantidad");
        tableModelProduct.addColumn("Precio");
        int width[] = {50, 190, 190, 50, 50};
        for (int i = 0; i < 5; i++) {
            systemPrincipal.tableProductos.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
        }
        addListTableModelProduct();
    }

    /**
     * Permite establecer las columnas con su respectivo nombre para NewSales o
     * tabla nuevas ventas
     */
    private void loadModelNewSales() {
        tableModelNewSales.addColumn("Código");
        tableModelNewSales.addColumn("Producto");
        tableModelNewSales.addColumn("Cantidad");
        tableModelNewSales.addColumn("Precio p/u");
        tableModelNewSales.addColumn("Total");
        int width[] = {80, 210, 80, 80, 80};
        for (int i = 0; i < 5; i++) {
            systemPrincipal.tableNV.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
        }
    }

    /**
     * Permite agregar los registros obtenidos del findAll a la tabla Clientes
     */
    private void addListTableModelCustomer() {
        listCustomer = customerImpl.findAll(customer);
        tableModelCustomer.setRowCount(0); // Cuando se ejecuta permite comenzar las filas desde la posicion 0, asi no se repiten cada vez que se llame
        for (Customer cstmr : listCustomer) {
            identificationCustomer = cstmr.getIdentification();
            nameCustomer = cstmr.getName();
            phoneNumberCustomer = cstmr.getPhoneNumber();
            emailCustomer = cstmr.getEmail();
            addressCustomer = cstmr.getAddress();
            razonSocialCustomer = cstmr.getRazonSocial();
            Object[] row = {identificationCustomer, nameCustomer, emailCustomer, phoneNumberCustomer, addressCustomer, razonSocialCustomer};
            tableModelCustomer.addRow(row);
        }
    }

    /**
     * Permite agregar los registros obtenidos del findAll a la tabla Proveedor
     */
    private void addListTableModelSupplier() {
        listSupplier = supplierImpl.findAll(supplier);
        tableModelSupplier.setRowCount(0); // Cuando se ejecuta permite comenzar las filas desde la posicion 0, asi no se repiten cada vez que se llame
        for (Supplier sup : listSupplier) {
            rutSupplier = sup.getRut();
            nameSupplier = sup.getName();
            phoneNumberSupplier = sup.getPhoneNumber();
            emailSupplier = sup.getEmail();
            addressSupplier = sup.getAddress();
            razonSocialSupplier = sup.getRazonSocial();
            Object[] row = {rutSupplier, nameSupplier, emailSupplier, phoneNumberSupplier, addressSupplier, razonSocialSupplier};
            tableModelSupplier.addRow(row);
        }
    }

    /**
     * Permite agregar los registros obtenidos del findAll a la tabla Productos
     */
    private void addListTableModelProduct() {
        listProduct = productImpl.findAll(product);
        tableModelProduct.setRowCount(0);
        for (Product pro : listProduct) {
            codeProduct = pro.getCode();
            nameProduct = pro.getName();
            countProduct = pro.getCount();
            priceProduct = pro.getPrice();
            supplierProduct = pro.getSupplier();
            Object[] row = {codeProduct, nameProduct, supplierProduct, countProduct, priceProduct};
            tableModelProduct.addRow(row);
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
                addListTableModelCustomer(); // Actualizar por si por ejemplo se presenta un case 2 CDU: se elimina en la base de datos, por ende en la aplicación aun sigue existiendo si no se actualiza
            }
        }
    };

    /**
     * Permite seleccionar la fila de la tabla PROVEEDOR y generar el evento...
     * Así como el ActionEvent para los botones. Para eliminar un registro lo
     * hago por medio del RUT de Supplier
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
                addListTableModelSupplier(); // Actualizar por si por ejemplo se presenta un case 2 CDU: se elimina en la base de datos, por ende en la aplicación aun sigue existiendo si no se actualiza 
            }
        }
    };

    /**
     * Permite seleccionar la fila de la tabla PRODUCTOS y generar el evento...
     * Así como el ActionEvent para los botones. Para eliminar un registro lo
     * hago por medio del code de Product
     */
    MouseAdapter adapterProduct = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (systemPrincipal.tableProductos.rowAtPoint(e.getPoint()) != -1) {
                int index = systemPrincipal.tableProductos.rowAtPoint(e.getPoint());
                product.setCode(systemPrincipal.tableProductos.getValueAt(index, 0).toString());
                switch (productImpl.findById(product)) {
                    case 1 -> {
                        systemPrincipal.fieldIdProducto.setText(String.valueOf(product.getIdProduct()));
                        systemPrincipal.fieldCodigoProductos.setText(product.getCode());
                        systemPrincipal.fieldNombreProductos.setText(product.getName());
                        systemPrincipal.fieldCantidadProductos.setText(String.valueOf(product.getCount()));
                        systemPrincipal.fieldPrecioProductos.setText(String.valueOf(product.getPrice()));
                        systemPrincipal.comboBoxProveedorProductos.setSelectedItem(product.getSupplier());
                    }
                    case 2 ->
                        JOptionPane.showMessageDialog(null, "Problema al seleccionar el registro");
                    case 0 ->
                        JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                }
                addListTableModelProduct(); // Actualizar por si por ejemplo se presenta un case 2 CDU: se elimina en la base de datos, por ende en la aplicación aun sigue existiendo si no se actualiza
            }
        }
    };

    /**
     * Permite generar una acción al dar Click en un elemento
     */
    KeyAdapter adapterSalesCode = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Si se genera un enter
                if (e.getSource() == systemPrincipal.fieldCodigoNV) { // Si se genera la acción en determinado elemento
                    codeProduct = systemPrincipal.fieldCodigoNV.getText().trim();
                    if (!codeProduct.equals("")) { // Verificar si está vacio al dar enter
                        product.setCode(codeProduct);
                        if (isNumeric(codeProduct)) { // Verificar que sean números el valor ingresado
                            switch (productImpl.findById(product)) {
                                case 1 -> {
                                    nameProduct = product.getName();
                                    priceProduct = product.getPrice();
                                    countProduct = product.getCount();
                                    systemPrincipal.fieldProductoNV.setText(nameProduct);
                                    systemPrincipal.fieldPrecioNV.setText(String.valueOf(priceProduct));
                                    systemPrincipal.fieldStockNV.setText(String.valueOf(countProduct));
                                    codeSelect = true;
                                }
                                case 2 -> {
                                    JOptionPane.showMessageDialog(null, "Código de producto no registrado");
                                    toCleanNewSale();
                                }
                                case 0 -> {
                                    JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                                    toCleanNewSale();
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Ingrese el código de producto correctamente");
                            toCleanNewSale();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese un código de producto");
                        toCleanNewSale();
                    }
                }
                if (e.getSource() == systemPrincipal.fieldCantidadNV) {
                    String valueText = systemPrincipal.fieldCantidadNV.getText().trim();
                    if (codeSelect) { // Verificar que haya sido seleccionado primero el código
                        if (!valueText.equals("")) { // Verificar que tenga un valor el campo
                            if (isNumeric(valueText)) { // verificar que sean números los valores ingresados 
                                countNewSales = Integer.parseInt(systemPrincipal.fieldCantidadNV.getText().trim()); // Convertir en número el valor ingresado por el usuario
                                if (countNewSales <= countProduct) { // Verificar que no sea mayor el número que desee comprar al que se tiene en stock
                                    totalByProduct = countNewSales * priceProduct; // Obtener el total a pagar por el producto seleccionado
                                    Object[] row = {codeProduct, nameProduct, countNewSales, priceProduct, totalByProduct};
                                    tableModelNewSales.addRow(row); // Agregar la fila generada según el producto elegido
                                    toCleanNewSale(); // Cuando se genere el item de venta se limpian los campos
                                } else {
                                    JOptionPane.showMessageDialog(null, "Stock no disponible");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Ingrese un valor correcto");
                                systemPrincipal.fieldCantidadNV.setText("");
                                systemPrincipal.fieldTotalPagarNV.setText("");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Ingrese la cantidad que desea comprar");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese primero el código de un producto");
                    }
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
        if (e.getSource() == systemPrincipal.btnBuscarClientes) {
            identificationCustomer = systemPrincipal.fieldBuscarClientes.getText().trim();
            if (!identificationCustomer.equals("")) {
                customer.setIdentification(identificationCustomer);
                switch (customerImpl.findById(customer)) {
                    case 1 -> {
                        tableModelCustomer.setRowCount(0);
                        nameCustomer = customer.getName();
                        emailCustomer = customer.getEmail();
                        phoneNumberCustomer = customer.getPhoneNumber();
                        addressCustomer = customer.getAddress();
                        razonSocialCustomer = customer.getRazonSocial();
                        Object[] row = {identificationCustomer, nameCustomer, emailCustomer, phoneNumberCustomer, addressCustomer, razonSocialCustomer};
                        tableModelCustomer.addRow(row);
                    }
                    case 2 -> {
                        JOptionPane.showMessageDialog(null, "N° identificación no registrado");
                        addListTableModelCustomer();
                    }
                    case 0 -> {
                        JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                        addListTableModelCustomer();
                    }
                }
                systemPrincipal.fieldBuscarClientes.setText("");
                toCleanCustomer();
            } else {
                toCleanCustomer();
                addListTableModelCustomer();
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
            addListTableModelCustomer();
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
                    updateComoBoxSupplierOfProduct(); // Actualizar el comboBox ya que trae el nombre de todos los proveedores
                    addListTableModelProduct(); // Actualizar la tabla producto
                }
                case 2 ->
                    JOptionPane.showMessageDialog(null, "Ingrese los valores solicitados");
                case 0 ->
                    JOptionPane.showMessageDialog(null, "Ingrese un RUT numérico");
            }
        }
        if (e.getSource() == systemPrincipal.btnBuscarProveedor) {
            rutSupplier = systemPrincipal.fieldBuscarProveedor.getText().trim();
            if (!rutSupplier.equals("")) {
                supplier.setRut(rutSupplier);
                switch (supplierImpl.findById(supplier)) {
                    case 1 -> {
                        tableModelSupplier.setRowCount(0);
                        nameSupplier = supplier.getName();
                        emailSupplier = supplier.getEmail();
                        phoneNumberSupplier = supplier.getPhoneNumber();
                        addressSupplier = supplier.getAddress();
                        razonSocialSupplier = supplier.getRazonSocial();
                        Object[] row = {rutSupplier, nameSupplier, emailSupplier, phoneNumberSupplier, addressSupplier, razonSocialSupplier};
                        tableModelSupplier.addRow(row);
                    }
                    case 2 -> {
                        JOptionPane.showMessageDialog(null, "RUT no registrado");
                        addListTableModelSupplier();
                    }
                    case 0 -> {
                        JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                        addListTableModelSupplier();
                    }
                }
                systemPrincipal.fieldBuscarClientes.setText("");
                toCleanSupplier();
            } else {
                toCleanCustomer();
                addListTableModelSupplier();
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
                        updateComoBoxSupplierOfProduct(); // Actualizar el comboBox ya que trae el nombre de todos los proveedores
                        addListTableModelProduct(); // Actualizar la tabla producto
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
                    updateComoBoxSupplierOfProduct(); // Actualizar el comboBox ya que trae el nombre de todos los proveedores
                    addListTableModelProduct(); // Actualizar la tabla producto
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione primero un registro para eliminar");
            }
        }
        if (e.getSource() == systemPrincipal.btnLimpiarFieldsProveedor) {
            toCleanSupplier();
            addListTableModelSupplier();
        }
        /**
         * ActionEnvent de SYSTEMPRINCIPAL - Product
         */
        if (e.getSource() == systemPrincipal.btnGuardarProductos) {
            switch (validationEnteredDataProduct()) {
                case 1 -> {
                    product.setCode(codeProduct);
                    product.setName(nameProduct);
                    product.setCount(countProduct);
                    product.setPrice(priceProduct);
                    product.setSupplier(supplierProduct);
                    switch (productImpl.insert(product)) {
                        case 1 -> {
                            JOptionPane.showMessageDialog(null, "Registro producto guardado");
                            toCleanProduct();
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "No se realizó el registro");
                        case 3 ->
                            JOptionPane.showMessageDialog(null, "Código ya registrado");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                    }
                    addListTableModelProduct(); // actualizar la tabla producto
                }
                case 2 ->
                    JOptionPane.showMessageDialog(null, "Ingrese los valores solicitados");
                case 3 ->
                    JOptionPane.showMessageDialog(null, "Precio o Cantidad invalidos, ingrese valores numéricos");
                case 0 ->
                    JOptionPane.showMessageDialog(null, "Código invalido, ingrese valores numéricos");
            }
        }
        if (e.getSource() == systemPrincipal.btnBuscarProductos) {
            codeProduct = systemPrincipal.fieldBuscarProductos.getText().trim();
            if (!codeProduct.equals("")) {
                product.setCode(codeProduct);
                switch (productImpl.findById(product)) {
                    case 1 -> {
                        tableModelProduct.setRowCount(0);
                        nameProduct = product.getName();
                        countProduct = product.getCount();
                        priceProduct = product.getPrice();
                        supplierProduct = product.getSupplier();
                        Object[] row = {codeProduct, nameProduct, countProduct, priceProduct, supplierProduct};
                        tableModelProduct.addRow(row);
                    }
                    case 2 -> {
                        JOptionPane.showMessageDialog(null, "Código no registrado");
                        addListTableModelProduct();
                    }
                    case 0 -> {
                        JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                        addListTableModelProduct();
                    }
                }
                systemPrincipal.fieldBuscarClientes.setText("");
                toCleanProduct();
            } else {
                toCleanProduct();
                addListTableModelProduct();
            }
        }
        if (e.getSource() == systemPrincipal.btnActualizarProductos) {
            switch (validationEnteredDataProduct()) {
                case 1 -> {
                    product.setCode(codeProduct);
                    product.setName(nameProduct);
                    product.setCount(countProduct);
                    product.setPrice(priceProduct);
                    product.setSupplier(supplierProduct);
                    switch (productImpl.update(product)) {
                        case 1 -> {
                            JOptionPane.showMessageDialog(null, "Registro producto actualizado");
                            toCleanProduct();
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
                    addListTableModelProduct(); // actualizar la tabla producto
                }
                case 2 ->
                    JOptionPane.showMessageDialog(null, "Ingrese los valores solicitados");
                case 3 ->
                    JOptionPane.showMessageDialog(null, "Precio o Cantidad invalidos, ingrese valores numéricos");
                case 0 ->
                    JOptionPane.showMessageDialog(null, "Código invalido, ingrese valores numéricos");
            }
        }
        if (e.getSource() == systemPrincipal.btnEliminarProductos) {
            if (product.getIdProduct() != 0) {
                if (JOptionPane.showConfirmDialog(null, "¿Seguro de eliminar el registro?", "Eliminar registro producto", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    switch (productImpl.delete(product)) {
                        case 1 -> {
                            JOptionPane.showMessageDialog(null, "Registro de producto con N° código " + product.getCode() + " eliminado");
                            toCleanProduct();
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "Problema al eliminar el registro");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problema en la conexión");
                    }
                    addListTableModelProduct(); // actualizar tabla de productos
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione primeno un registro para eliminar");
            }
        }
        if (e.getSource() == systemPrincipal.btnLimpiarFieldsProductos) {
            toCleanProduct();
            addListTableModelProduct();
        }
        if (e.getSource() == systemPrincipal.btnExcelProductos) {
            ExcelReport export = new ExcelReport();
            export.report();
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
     * Limpiar los campos de Product
     */
    private void toCleanProduct() {
        systemPrincipal.fieldCodigoProductos.setText("");
        systemPrincipal.fieldNombreProductos.setText("");
        systemPrincipal.fieldCantidadProductos.setText("");
        systemPrincipal.fieldPrecioProductos.setText("");
        systemPrincipal.comboBoxProveedorProductos.setSelectedIndex(0);
        systemPrincipal.fieldIdProducto.setText("");
        // Quitar el valor de Código y idProduct para que no pueda eliminar ni actualizar 
        product.setIdProduct(0);
        product.setCode(null);
    }

    private void toCleanNewSale() {
        systemPrincipal.fieldCodigoNV.setText("");
        systemPrincipal.fieldProductoNV.setText("");
        systemPrincipal.fieldCantidadNV.setText("");
        systemPrincipal.fieldPrecioNV.setText("");
        systemPrincipal.fieldStockNV.setText("");
        codeSelect = false; // Con false ya no hay código seleccionado, debe nuevamente escoger un producto el usuario
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
     *
     * @param name ingresa el nombre del proveedor seleccionado en la interfaz
     * @return el id del proveedor seleccionado ya que en la base de datos está
     * guardado como llave foranea el proveedor en la tabla de Productos, es
     * decir, un int
     */
    private int findIdSupplierSelected(String name) {
        int encontrado = 0;
        for (Supplier suppList : listSupplier) {
            if (name.equals(suppList.getName())) {
                encontrado = suppList.getIdSupplier();
            }
        }
        return encontrado;
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
            return 0; // Retorna 0 si no ingresó números en el RUT
        }
    }

    /**
     * Este método va para Product, obteniendo los datos ingresados en las field
     * de Productos
     *
     * @return int indicando cual ha sido la verificación correspondiente
     */
    private int validationEnteredDataProduct() {
        if (isNumeric(systemPrincipal.fieldCodigoProductos.getText().trim())) {
            if (isNumeric(systemPrincipal.fieldCantidadProductos.getText().trim()) && isNumeric(systemPrincipal.fieldPrecioProductos.getText().trim())) {
                if (!systemPrincipal.fieldNombreProductos.getText().trim().isEmpty() && !systemPrincipal.comboBoxProveedorProductos.getSelectedItem().toString().equals(firstItemComboBox)) {
                    codeProduct = systemPrincipal.fieldCodigoProductos.getText().trim();
                    nameProduct = systemPrincipal.fieldNombreProductos.getText().trim();
                    countProduct = Integer.parseInt(systemPrincipal.fieldCantidadProductos.getText().trim());
                    priceProduct = Integer.parseInt(systemPrincipal.fieldPrecioProductos.getText().trim());
                    // Valor obtenido del comboBox y se pasa al id y luego se convierte en String para pasarlo a la variable supplier en Product
                    supplierProduct = String.valueOf(findIdSupplierSelected(systemPrincipal.comboBoxProveedorProductos.getSelectedItem().toString()));
                    return 1; // Retorna 1 si todos los valores están correctamente
                } else {
                    return 2; // Retorna 2 si no ingresó los valores solicitados
                }
            } else {
                return 3; // Retorna 3 si no ingresó números en Cantidad y Precio 
            }
        } else {
            return 0; // Retorna 0 si no ingresó números en el Código
        }
    }
}
