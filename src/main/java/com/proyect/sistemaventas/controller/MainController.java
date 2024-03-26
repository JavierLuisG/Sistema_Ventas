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
    private boolean isSelectItemCustomer = false; // Verifica que haya sido seleccionado un item de la tabla
    private final DefaultTableModel tableModelCustomer = new DefaultTableModel();
    private List<Customer> listCustomer;

    /* Variables de Supplier */
    private String rutSupplier;
    private String nameSupplier;
    private String phoneNumberSupplier;
    private String emailSupplier;
    private String addressSupplier;
    private String razonSocialSupplier;
    private boolean isSelectItemSupplier = false; // Verifica que haya sido seleccionado un item de la tabla
    private final DefaultTableModel tableModelSupplier = new DefaultTableModel();
    private List<Supplier> listSupplier; // La puse global para poder agregar los nombres al comboBox de productos

    /* Variables de Product */
    private String codeProduct;
    private String nameProduct;
    private int countProduct;
    private int priceProduct;
    private String supplierProduct;
    private final String firstItemComboBox = "Seleccionar";
    private boolean isSelectItemProduct = false; // Verifica que haya sido seleccionado un item de la tabla
    private final DefaultTableModel tableModelProduct = new DefaultTableModel();
    private List<Product> listProduct;

    /* Variables de New Sales -> Este no tiene un modelo */
    private boolean codeSelectNV = false; // Para verificar que primero haya sido ingresado un código, es decir, un producto
    private int countNewSalesNV; // La cantidad de productos que quiere comprar el usuario
    private int totalByProductNV; // Total a pagar en relación al producto, es decir, en la fila generada
    private String valueCountNV; // Valor obtenido de la fieldCantidadNV
    private String newValueCountNV; // Valor obtenido de la nueva cantidad que va a ingresar el usuario por el showMessageDialog
    private int itemPositionNV; // Permite saber en que posición se encuentra el producto en tableNV
    private int itemSelectNV; // Fila seleccionada de la tableNV
    private boolean isSelectItemNV = false; // Verifica que haya sido seleccionado un item de la tabla
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
     * modelos, este se ejecuta cuando se de click inicia sesión
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
        /* Inicializar las clase relacionadas al modelo */
        customer = new Customer();
        customerImpl = new CustomerDAOImpl();
        /* Acciones sobre los botones */
        systemPrincipal.btnGuardarClientes.addActionListener(this);
        systemPrincipal.btnBuscarClientes.addActionListener(this);
        systemPrincipal.btnActualizarClientes.addActionListener(this);
        systemPrincipal.btnEliminarClientes.addActionListener(this);
        systemPrincipal.btnLimpiarFieldsClientes.addActionListener(this);
        /* Asignar el model a la tabla correspondiente y asignar las columnas con sus filas */
        systemPrincipal.tableClientes.setModel(tableModelCustomer);
        loadModelCustomer();
        /* Dar acción al mouse para seleccionar la fila de la tabla */
        systemPrincipal.tableClientes.addMouseListener(mouseAdapterCustomer);
        /* Negar modificación del valor */
        systemPrincipal.tableClientes.setEnabled(false); // La tabla
    }

    /**
     * Inicializar supplier y supplierImpl y generar las acciones de los botones
     * de Proveedor
     */
    private void startSupplier() {
        /* Inicializar las clase relacionadas al modelo */
        supplier = new Supplier();
        supplierImpl = new SupplierDAOImpl();
        /* Acciones sobre los botones */
        systemPrincipal.btnGuardarProveedor.addActionListener(this);
        systemPrincipal.btnBuscarProveedor.addActionListener(this);
        systemPrincipal.btnActualizarProveedor.addActionListener(this);
        systemPrincipal.btnEliminarProveedor.addActionListener(this);
        systemPrincipal.btnLimpiarFieldsProveedor.addActionListener(this);
        /* Asignar el model a la tabla correspondiente y asignar las columnas con sus filas */
        systemPrincipal.tableProveedor.setModel(tableModelSupplier);
        loadModelSupplier();
        /* Dar acción al mouse para seleccionar la fila de la tabla */
        systemPrincipal.tableProveedor.addMouseListener(mouseAdapterSupplier);
        /* Negar modificación del valor */
        systemPrincipal.tableProveedor.setEnabled(false); // La tabla
    }

    /**
     * Inicializar product y productImpl y generar las acciones de los botones
     * de Producto
     */
    private void startProduct() {
        /* Inicializar las clase relacionadas al modelo */
        product = new Product();
        productImpl = new ProductDAOImpl();
        updateComoBoxSupplierOfProduct(); // Asignar valores al comboBoxSupplier en product
        /* Acciones sobre los botones */
        systemPrincipal.btnGuardarProductos.addActionListener(this);
        systemPrincipal.btnBuscarProductos.addActionListener(this);
        systemPrincipal.btnActualizarProductos.addActionListener(this);
        systemPrincipal.btnEliminarProductos.addActionListener(this);
        systemPrincipal.btnLimpiarFieldsProductos.addActionListener(this);
        systemPrincipal.btnExcelProductos.addActionListener(this);
        /* Acción al mouse para seleccionar la fila de la tabla */
        systemPrincipal.tableProductos.addMouseListener(mouseAdapterProduct);
        /* Asignar el model a la tabla correspondiente y asignar las columnas con sus filas */
        systemPrincipal.tableProductos.setModel(tableModelProduct);
        loadModelProduct();
        /* Negar modificación del valor */
        systemPrincipal.tableProductos.setEnabled(false); // La tabla
    }

    /**
     * No es necesario inicializar ya que se optione la información de product
     */
    private void startNewSale() {
        /* Acciones sobre los botones */
        systemPrincipal.btnEliminarNV.addActionListener(this);
        /* Acción al mouse para seleccionar la fila de la tabla */
        systemPrincipal.tableNV.addMouseListener(mouseAdapterNewSales);
        /* Accion enter sobre las cajas */
        systemPrincipal.fieldCodigoNV.addKeyListener(keyAdapterNewSale);
        systemPrincipal.fieldCantidadNV.addKeyListener(keyAdapterNewSale);
        systemPrincipal.fieldIdentificationClienteNV.addKeyListener(keyAdapterNewSale);
        /* Asignar el model a la tabla correspondiente y asignar las columnas con sus filas*/
        systemPrincipal.tableNV.setModel(tableModelNewSales);
        loadModelNewSales();
        /* Negar modificación del valor */
        systemPrincipal.tableNV.setEnabled(false); // La tabla
        systemPrincipal.fieldProductoNV.setEditable(false); // Las cajas
        systemPrincipal.fieldStockNV.setEditable(false);
        systemPrincipal.fieldPrecioNV.setEditable(false);
        systemPrincipal.fieldNombreClienteNV.setEditable(false);
        systemPrincipal.fieldTotalPagarNV.setEditable(false);
    }

    /**
     * Permite obtener los datos que se generaron en listSupplier y agregarlos
     * en el comboBoxProveedorProductos
     */
    private void updateComoBoxSupplierOfProduct() {
        systemPrincipal.comboBoxProveedorProductos.removeAllItems(); // Permite borrar la info anterior y asi mostrar la info actualizada
        // El for permite agregar el nombre de los proveedores al comboBox obtenidos de la listSupplier
        if (!listSupplier.isEmpty()) { // Si la lista está vacía se realiza el for
            systemPrincipal.comboBoxProveedorProductos.addItem(firstItemComboBox); // Primer item para diferenciar de los proveedores
            for (Supplier sup : listSupplier) {
                nameSupplier = sup.getName();
                systemPrincipal.comboBoxProveedorProductos.addItem(nameSupplier);
            }
        } else { // Si no tiene valores solo se agrega el primer item
            systemPrincipal.comboBoxProveedorProductos.addItem(firstItemComboBox); // Primer item para diferenciar de los proveedores
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
        /* Asignar el tamaño a las columnas */
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
        /* Asignar el ancho de las columnas */
        int width[] = {75, 150, 180, 75, 120, 90};
        for (int i = 0; i < 6; i++) {
            systemPrincipal.tableProveedor.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
        }
        addListTableModelSupplier(); // Asignar las filas según los datos traidos de la base de datos
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
        /* Asignar el ancho de las columnas */
        int width[] = {50, 190, 190, 50, 50};
        for (int i = 0; i < 5; i++) {
            systemPrincipal.tableProductos.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
        }
        addListTableModelProduct(); // Asignar las filas según los datos traidos de la base de datos
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
        /* Asignar el ancho de las columnas */
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
        tableModelCustomer.setRowCount(0); // Permite comenzar las filas desde la posicion 0, asi no se repiten cada vez que se llame
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
        tableModelSupplier.setRowCount(0); // Permite comenzar las filas desde la posicion 0, asi no se repiten cada vez que se llame
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
        tableModelProduct.setRowCount(0); // Permite comenzar las filas desde la posicion 0, asi no se repiten cada vez que se llame
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
    MouseAdapter mouseAdapterCustomer = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (systemPrincipal.tableClientes.rowAtPoint(e.getPoint()) != -1) { // Validar si se da click en la tabla
                int index = systemPrincipal.tableClientes.rowAtPoint(e.getPoint()); // Obtener la fila seleccionada
                customer.setIdentification(systemPrincipal.tableClientes.getValueAt(index, 0).toString()); // Enviar el valor de lo indicado
                switch (customerImpl.findById(customer)) {
                    case 1 -> {
                        systemPrincipal.fieldIdClientes.setText(String.valueOf(customer.getIdCustomer()));
                        systemPrincipal.fieldIdentificacionClientes.setText(String.valueOf(customer.getIdentification()));
                        systemPrincipal.fieldNombreClientes.setText(customer.getName());
                        systemPrincipal.fieldTelefonoClientes.setText(customer.getPhoneNumber());
                        systemPrincipal.fieldEmailClientes.setText(customer.getEmail());
                        systemPrincipal.fieldDireccionClientes.setText(customer.getAddress());
                        systemPrincipal.fieldRazonSocialClientes.setText(customer.getRazonSocial());
                        isSelectItemCustomer = true; // Indica que ha sido seleccionado un item de la tabla
                    }
                    case 2 ->
                        JOptionPane.showMessageDialog(null, "Problema al seleccionar el registro");
                    case 0 ->
                        JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                }
                addListTableModelCustomer(); // Actualizar por si por ejemplo se presenta un case 2 CDU: se elimina en la base de datos, por ende en la aplicación aun sigue existiendo si no se actualiza
                systemPrincipal.fieldBuscarClientes.setText(""); // Limpiar campo luego de realizar la busqueda
            }
        }
    };

    /**
     * Permite seleccionar la fila de la tabla PROVEEDOR y generar el evento.
     */
    MouseAdapter mouseAdapterSupplier = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (systemPrincipal.tableProveedor.rowAtPoint(e.getPoint()) != -1) { // Validar si se da click en la tabla
                int index = systemPrincipal.tableProveedor.rowAtPoint(e.getPoint()); // Obtener la fila seleccionada
                supplier.setRut(systemPrincipal.tableProveedor.getValueAt(index, 0).toString()); // Enviar el valor de lo indicado
                switch (supplierImpl.findById(supplier)) {
                    case 1 -> {
                        systemPrincipal.fieldIdProveedor.setText(String.valueOf(supplier.getIdSupplier()));
                        systemPrincipal.fieldRutProveedor.setText(supplier.getRut());
                        systemPrincipal.fieldNombreProveedor.setText(supplier.getName());
                        systemPrincipal.fieldTelefonoProveedor.setText(supplier.getPhoneNumber());
                        systemPrincipal.fieldEmailProveedor.setText(supplier.getEmail());
                        systemPrincipal.fieldDireccionProveedor.setText(supplier.getAddress());
                        systemPrincipal.fieldRazonSocialProveedor.setText(supplier.getRazonSocial());
                        isSelectItemSupplier = true; // Indica que ha sido seleccionado un item de la tabla
                    }
                    case 2 ->
                        JOptionPane.showMessageDialog(null, "Problema al seleccionar el registro");
                    case 0 ->
                        JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                }
                addListTableModelSupplier(); // Actualizar por si por ejemplo se presenta un case 2 CDU: se elimina en la base de datos, por ende en la aplicación aun sigue existiendo si no se actualiza 
                systemPrincipal.fieldBuscarProveedor.setText(""); // Limpiar campo luego de realizar la busqueda
            }
        }
    };

    /**
     * Permite seleccionar la fila de la tabla PRODUCTOS y generar el evento.
     */
    MouseAdapter mouseAdapterProduct = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (systemPrincipal.tableProductos.rowAtPoint(e.getPoint()) != -1) { // Validar si se da click en la tabla
                int index = systemPrincipal.tableProductos.rowAtPoint(e.getPoint()); // Obtener la fila seleccionada
                product.setCode(systemPrincipal.tableProductos.getValueAt(index, 0).toString()); // Enviar el valor de lo indicado
                switch (productImpl.findById(product)) {
                    case 1 -> {
                        systemPrincipal.fieldIdProducto.setText(String.valueOf(product.getIdProduct()));
                        systemPrincipal.fieldCodigoProductos.setText(product.getCode());
                        systemPrincipal.fieldNombreProductos.setText(product.getName());
                        systemPrincipal.fieldCantidadProductos.setText(String.valueOf(product.getCount()));
                        systemPrincipal.fieldPrecioProductos.setText(String.valueOf(product.getPrice()));
                        systemPrincipal.comboBoxProveedorProductos.setSelectedItem(product.getSupplier());
                        isSelectItemProduct = true; // Indica que ha sido seleccionado un item de la tabla
                    }
                    case 2 ->
                        JOptionPane.showMessageDialog(null, "Problema al seleccionar el registro");
                    case 0 ->
                        JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                }
                addListTableModelProduct(); // Actualizar por si por ejemplo se presenta un case 2 CDU: se elimina en la base de datos, por ende en la aplicación aun sigue existiendo si no se actualiza
                systemPrincipal.fieldBuscarProductos.setText(""); // Limpiar campo luego de realizar la busqueda
            }
        }
    };

    /**
     * Permite seleccionar la fila de la tabla NUEVA VENTA y generar el evento.
     * En este evento se realiza para seleccionar el item que será sacado de la
     * tabla
     */
    MouseAdapter mouseAdapterNewSales = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (systemPrincipal.tableNV.rowAtPoint(e.getPoint()) != -1) {
                itemSelectNV = systemPrincipal.tableNV.rowAtPoint(e.getPoint());
                isSelectItemNV = true;
            }
        }
    };

    /**
     * Permite generar una acción al dar enter en un elemento
     */
    KeyAdapter keyAdapterNewSale = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Si se genera un enter
                /**
                 * Si preciona enter en fieldCodigoNV
                 */
                if (e.getSource() == systemPrincipal.fieldCodigoNV) { // Si se genera la acción en determinado elemento
                    codeProduct = systemPrincipal.fieldCodigoNV.getText().trim();
                    if (!codeProduct.equals("")) { // Verificar si está vacio al dar enter
                        product.setCode(codeProduct);
                        if (isNumeric(codeProduct)) { // Verificar que sean números el valor ingresado
                            /*
                            Para recorrer el for que está en el método que se ejecuta en el else es necesario que ya tenga un valor,
                            de lo contrario no lo recorre, por ello se crea el if - else 
                             */
                            if (systemPrincipal.tableNV.getRowCount() == 0) {
                                switch (productImpl.findById(product)) {
                                    case 1 -> {
                                        nameProduct = product.getName();
                                        priceProduct = product.getPrice();
                                        countProduct = product.getCount();
                                        systemPrincipal.fieldProductoNV.setText(nameProduct);
                                        systemPrincipal.fieldPrecioNV.setText(String.valueOf(priceProduct));
                                        systemPrincipal.fieldStockNV.setText(String.valueOf(countProduct));
                                        codeSelectNV = true;
                                    }
                                    case 2 -> {
                                        JOptionPane.showMessageDialog(null, "Código de producto no registrado");
                                        toCleanNewSaleProduct();
                                    }
                                    case 0 -> {
                                        JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                                        toCleanNewSaleProduct();
                                    }
                                }
                            } else {
                                codeSelectNV = true; // se pone aquí por el toCleanNewSale() que lo vuelve false                                    
                                if (isLocationInTableNV()) {// Si ya fue agregado se muestra una ventana para generar la nueva cantidad 
                                    do { // Permite no cerrar el inputDialog en caso, por ejemplo, de que ingrese un dato no valido
                                        int countProductSelect = Integer.parseInt(systemPrincipal.tableNV.getValueAt(itemPositionNV, 2).toString());
                                        newValueCountNV = JOptionPane.showInputDialog("Tiene " + countProductSelect + " unidades seleccionadas del producto " + nameProduct + "\nCuántas desea llevar en total?");
                                        if (newValueCountNV != null) {
                                            priceProduct = Integer.parseInt(systemPrincipal.tableNV.getValueAt(itemPositionNV, 3).toString()); // Actualizar precio del producto, si entra en este if trae el price del último lugar donde se realizó consulta a la base de datos
                                            switch (validationIndicateCountProductNV(newValueCountNV, countProduct)) {
                                                case 1 -> {
                                                    tableModelNewSales.setValueAt(countNewSalesNV, itemPositionNV, 2);
                                                    tableModelNewSales.setValueAt(totalByProductNV, itemPositionNV, 4);
                                                }
                                                case 2 ->
                                                    JOptionPane.showMessageDialog(null, "Stock no disponible");
                                                case 3 -> {
                                                    JOptionPane.showMessageDialog(null, "Ingrese un valor correcto");
                                                    systemPrincipal.fieldCantidadNV.setText("");
                                                    systemPrincipal.fieldTotalPagarNV.setText("");
                                                }
                                                case 4 ->
                                                    JOptionPane.showMessageDialog(null, "Ingrese la cantidad que desea comprar");
                                            }
                                        } else {// si no ha sido agregado se muestran los datos en la caja para generar la seleccion del producto a comprar
                                            break;
                                        }
                                        systemPrincipal.fieldTotalPagarNV.setText(String.valueOf(totalToPay())); // Mostrar total a pagar actualizado de las venta en general
                                    } while (validationIndicateCountProductNV(newValueCountNV, countProduct) != 1);
                                    toCleanNewSaleProduct();
                                } else { // si no ha sido agregado se muestran los datos en la caja para generar la seleccion del producto a comprar
                                    switch (productImpl.findById(product)) {
                                        case 1 -> {
                                            nameProduct = product.getName();
                                            priceProduct = product.getPrice();
                                            countProduct = product.getCount();
                                            systemPrincipal.fieldProductoNV.setText(nameProduct);
                                            systemPrincipal.fieldPrecioNV.setText(String.valueOf(priceProduct));
                                            systemPrincipal.fieldStockNV.setText(String.valueOf(countProduct));
                                        }
                                        case 2 -> {
                                            JOptionPane.showMessageDialog(null, "Código de producto no registrado");
                                            toCleanNewSaleProduct();
                                        }
                                        case 0 -> {
                                            JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                                            toCleanNewSaleProduct();
                                        }
                                    }
                                }
                                systemPrincipal.fieldTotalPagarNV.setText(String.valueOf(totalToPay())); // Mostrar total a pagar actualizado de las venta en general
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Ingrese el código de producto correctamente");
                            toCleanNewSaleProduct();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese un código de producto");
                        toCleanNewSaleProduct();
                    }
                }
                /**
                 * Si preciona enter en fieldCantidadNV
                 */
                if (e.getSource() == systemPrincipal.fieldCantidadNV) {
                    valueCountNV = systemPrincipal.fieldCantidadNV.getText().trim();
                    switch (validationIndicateCountProductNV(valueCountNV, countProduct)) {
                        case 1 -> {
                            if (countNewSalesNV <= countProduct) { // Verificar que no sea mayor el número que desee comprar al que se tiene en stock
                                Object[] row = {codeProduct, nameProduct, countNewSalesNV, priceProduct, totalByProductNV};
                                tableModelNewSales.addRow(row); // Agregar la fila generada según el producto elegido
                                toCleanNewSaleProduct(); // Cuando se genere el item de venta se limpian los campos
                                systemPrincipal.fieldTotalPagarNV.setText(String.valueOf(totalToPay())); // Mostrar total a pagar actualizado de las venta en general
                            }
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "Stock no disponible");
                        case 3 -> {
                            JOptionPane.showMessageDialog(null, "Ingrese un valor correcto");
                            systemPrincipal.fieldCantidadNV.setText("");
                        }
                        case 4 -> {
                            JOptionPane.showMessageDialog(null, "Ingrese la cantidad que desea comprar");
                            systemPrincipal.fieldCantidadNV.setText("");
                        }
                        case 5 ->
                            JOptionPane.showMessageDialog(null, "Ingrese primero el código de un producto");
                    }
                }
                /**
                 * Si preciona enter en fieldIdentificationNumberClienteNV
                 */
                if (e.getSource() == systemPrincipal.fieldIdentificationClienteNV) {
                    identificationCustomer = systemPrincipal.fieldIdentificationClienteNV.getText().trim();
                    if (!identificationCustomer.equals("")) {
                        customer.setIdentification(identificationCustomer);
                        switch (customerImpl.findById(customer)) {
                            case 1 -> {
                                nameCustomer = customer.getName();
                                emailCustomer = customer.getEmail();
                                phoneNumberCustomer = customer.getPhoneNumber();
                                addressCustomer = customer.getAddress();
                                razonSocialCustomer = customer.getRazonSocial();
                                systemPrincipal.fieldNombreClienteNV.setText(nameCustomer);
                            }
                            case 2 -> {
                                JOptionPane.showMessageDialog(null, "N° identificación no registrado");
                                toCleanNewSaleCustomer();
                            }
                            case 0 ->
                                JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese un N° identificación");
                        toCleanNewSaleCustomer();
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
         * ActionEnvent de SYSTEMPRINCIPAL - New Sales
         */
        if (e.getSource() == systemPrincipal.btnEliminarNV) {
            if (isSelectItemNV) { // Confirma que primero haya sido seleccionado un registro de la tabla
                if (JOptionPane.showConfirmDialog(null, "¿Seguro de sacar el producto " + tableModelNewSales.getValueAt(itemSelectNV, 1) + " de la lista?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    tableModelNewSales.removeRow(itemSelectNV);
                    systemPrincipal.fieldTotalPagarNV.setText(String.valueOf(totalToPay()));
                }
                isSelectItemNV = false; // Sea cual sea la respuesta se pone en false para que tenga que volver a seleccionar un item
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione primero el producto en la tabla");
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
                            toCleanCustomer(true);
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "No se realizó el registro");
                        case 3 ->
                            JOptionPane.showMessageDialog(null, "N° identificación ya registrado");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                    }
                    addListTableModelCustomer(); // Se actualizan los datos en la tabla cuando se realiza un registro
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
                toCleanCustomer(false); // False porque se espera tener solamente el registro buscado, si se actualiza saldrian todos los registros
            } else {
                toCleanCustomer(true);
            }
        }
        if (e.getSource() == systemPrincipal.btnActualizarClientes) {
            if (isSelectItemCustomer) { // Confirma que primero haya sido seleccionado un item de la tabla
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
                                toCleanCustomer(true);
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
                        addListTableModelCustomer(); // Se limpian los campos y actualizan los datos en la tabla
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
            if (isSelectItemCustomer) { // Confirma que primero haya sido seleccionado un item de la tabla
                if (JOptionPane.showConfirmDialog(null, "¿Seguro de eliminar el registro " + customer.getIdentification() + "?", "Eliminar registro cliente", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    switch (customerImpl.delete(customer)) {
                        case 1 -> {
                            JOptionPane.showMessageDialog(null, "Registro de cliente con N° identificación " + customer.getIdentification() + " eliminado");
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "Problema al eliminar el registro");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problema en la conexión");
                    }
                    toCleanCustomer(true); // Se puede limpiar todo sin importar la respuesta del switch
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione primero un registro para eliminar");
            }
        }
        if (e.getSource() == systemPrincipal.btnLimpiarFieldsClientes) {
            toCleanCustomer(true);
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
                            toCleanSupplier(true);
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "No se realizó el registro");
                        case 3 ->
                            JOptionPane.showMessageDialog(null, "RUT ya registrado");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                    }
                    /* Dentro se actualiza la tabla supplier 
                     * CDU: si se actualiza un proveedor, que se actualice todo de product */
                    toCleanProduct(true);
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
                toCleanSupplier(false); // False porque se espera tener solamente el registro buscado, si se actualiza saldrian todos los registros
            } else {
                toCleanSupplier(true);
            }
        }
        if (e.getSource() == systemPrincipal.btnActualizarProveedor) {
            if (isSelectItemSupplier) { // Confirma que primero haya sido seleccionado un item de la tabla
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
                                toCleanSupplier(true); // Necesario limpiar todo
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
                        /* Dentro se actualiza la tabla supplier y no se limpian los campos
                         * CDU: si se actualiza un proveedor, que se actualice todo de product */
                        toCleanProduct(true);
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
            if (isSelectItemSupplier) { // Confirma que primero haya sido seleccionado un item de la tabla
                if (JOptionPane.showConfirmDialog(null, "¿Seguro de eliminar el registro " + supplier.getRut() + "?", "Eliminar registro proveedor", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    switch (supplierImpl.delete(supplier)) {
                        case 1 -> {
                            JOptionPane.showMessageDialog(null, "Registro de proveedor con RUT " + supplier.getRut() + " eliminado");
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "Problema al eliminar el registro");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problema en la conexión");
                    }
                    toCleanSupplier(true);
                    toCleanProduct(true); // CDU: si se elimina un proveedor, que se actualice todo de product
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione primero un registro para eliminar");
            }
        }
        if (e.getSource() == systemPrincipal.btnLimpiarFieldsProveedor) {
            toCleanSupplier(true);
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
                            toCleanProduct(true); // Necesario limpiar todo
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "No se realizó el registro");
                        case 3 ->
                            JOptionPane.showMessageDialog(null, "Código ya registrado");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problemas en la conexión");
                    }
                    addListTableModelProduct(); // Solo se necesita actualizar la tabla producto
                }
                case 2 ->
                    JOptionPane.showMessageDialog(null, "Ingrese los valores solicitados");
                case 3 ->
                    JOptionPane.showMessageDialog(null, "Precio o Cantidad invalidos, ingrese valores numéricos");
                case 0 ->
                    JOptionPane.showMessageDialog(null, "Ingrese un Código numérico");
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
                toCleanProduct(false); // False porque se espera tener solamente el registro buscado, si se actualiza saldrian todos los registros
            } else {
                toCleanProduct(true);
            }
        }
        if (e.getSource() == systemPrincipal.btnActualizarProductos) {
            if (isSelectItemProduct) { // Confirma que primero haya sido seleccionado un item de la tabla
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
                                toCleanProduct(true); // Necesario limpiar todo
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
                        addListTableModelProduct(); // Solo se necesita actualizar la tabla producto
                    }
                    case 2 ->
                        JOptionPane.showMessageDialog(null, "Ingrese los valores solicitados");
                    case 3 ->
                        JOptionPane.showMessageDialog(null, "Precio o Cantidad invalidos, ingrese valores numéricos");
                    case 0 ->
                        JOptionPane.showMessageDialog(null, "Ingrese un Código numérico");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione primero un registro para actualizar");
            }
        }
        if (e.getSource() == systemPrincipal.btnEliminarProductos) {
            if (isSelectItemProduct) { // Confirma que primero haya sido seleccionado un item de la tabla
                if (JOptionPane.showConfirmDialog(null, "¿Seguro de eliminar el registro " + product.getCode() + "?", "Eliminar registro producto", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    switch (productImpl.delete(product)) {
                        case 1 -> {
                            JOptionPane.showMessageDialog(null, "Registro de producto con N° código " + product.getCode() + " eliminado");
                        }
                        case 2 ->
                            JOptionPane.showMessageDialog(null, "Problema al eliminar el registro");
                        case 0 ->
                            JOptionPane.showMessageDialog(null, "Problema en la conexión");
                    }
                    toCleanProduct(true); // Se puede limpiar todo sin importar la respuesta del switch
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione primeno un registro para eliminar");
            }
        }
        if (e.getSource() == systemPrincipal.btnLimpiarFieldsProductos) {
            toCleanProduct(true);
        }
        if (e.getSource() == systemPrincipal.btnExcelProductos) {
            ExcelReport export = new ExcelReport();
            export.report();
        }
    }

    /**
     * Limpiar los campos de Customer
     *
     * El parametro en true permite ejecutar la actualización de la tabla
     * cliente
     */
    private void toCleanCustomer(boolean all) {
        systemPrincipal.fieldIdentificacionClientes.setText("");
        systemPrincipal.fieldNombreClientes.setText("");
        systemPrincipal.fieldTelefonoClientes.setText("");
        systemPrincipal.fieldEmailClientes.setText("");
        systemPrincipal.fieldDireccionClientes.setText("");
        systemPrincipal.fieldRazonSocialClientes.setText("");
        systemPrincipal.fieldIdClientes.setText("");
        systemPrincipal.fieldBuscarClientes.setText("");
        isSelectItemCustomer = false;
        if (all) {
            addListTableModelCustomer();
        }
    }

    /**
     * Limpiar los campos de Supplier
     *
     * El parametro en true permite ejecutar la actualización de la tabla
     * proveedor
     */
    private void toCleanSupplier(boolean all) {
        systemPrincipal.fieldRutProveedor.setText("");
        systemPrincipal.fieldNombreProveedor.setText("");
        systemPrincipal.fieldTelefonoProveedor.setText("");
        systemPrincipal.fieldEmailProveedor.setText("");
        systemPrincipal.fieldDireccionProveedor.setText("");
        systemPrincipal.fieldRazonSocialProveedor.setText("");
        systemPrincipal.fieldIdProveedor.setText("");
        systemPrincipal.fieldBuscarProveedor.setText("");
        isSelectItemSupplier = false;
        if (all) {
            addListTableModelSupplier(); // Actualizar la tabla proveedor
        }
    }

    /**
     * Limpiar los campos de Product
     *
     * El parametro en true permite ejecutar la actualización del comboBox, por
     * tal razón para buscar un registtro debe estar en false
     */
    private void toCleanProduct(boolean all) {
        systemPrincipal.fieldCodigoProductos.setText("");
        systemPrincipal.fieldNombreProductos.setText("");
        systemPrincipal.fieldCantidadProductos.setText("");
        systemPrincipal.fieldPrecioProductos.setText("");
        systemPrincipal.comboBoxProveedorProductos.setSelectedIndex(0);
        systemPrincipal.fieldIdProducto.setText("");
        systemPrincipal.fieldBuscarProductos.setText("");
        isSelectItemProduct = false;
        if (all) {
            addListTableModelSupplier(); // Se actualiza la lista con los datos de proveedores de la base de datos
            updateComoBoxSupplierOfProduct(); // Con los datos de la lista, actualizar el comboBox
            addListTableModelProduct(); // Actualizar la tabla producto            
        }
    }

    /**
     * Limpiar los campos de New Sale, la parte del cliente
     */
    private void toCleanNewSaleCustomer() {
        systemPrincipal.fieldIdentificationClienteNV.setText("");
        systemPrincipal.fieldNombreClienteNV.setText("");
    }

    /**
     * Limpiar los campos de New Sale, la parte del producto
     */
    private void toCleanNewSaleProduct() {
        systemPrincipal.fieldCodigoNV.setText("");
        systemPrincipal.fieldProductoNV.setText("");
        systemPrincipal.fieldCantidadNV.setText("");
        systemPrincipal.fieldPrecioNV.setText("");
        systemPrincipal.fieldStockNV.setText("");
        codeSelectNV = false; // Con false ya no hay código seleccionado, debe nuevamente escoger un producto el usuario
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

    /**
     * Permite realizar validaciones que van especificas al ingreso de la
     * cantidad en tanto en el fielCantidadNV como en el showMessageDialog
     *
     * @param value de donde se obtiene el valor ingresado por el usuario
     * @param countProduct cantidad de stock o unidades disponibles guardado en
     * la base de datos
     * @return un int que indica lo que se ha realizado
     */
    public int validationIndicateCountProductNV(String value, int countProduct) {
        if (codeSelectNV) { // Verificar que haya sido seleccionado primero el código
            if (value != null) {
                if (!value.equals("")) { // Verificar que tenga un valor el campo
                    if (isNumeric(value)) { // verificar que sean números los valores ingresados                     
                        countNewSalesNV = Integer.parseInt(value); // Convertir en número el valor ingresado por el usuario
                        if (countNewSalesNV > 0) {
                            if (countNewSalesNV <= countProduct) { // Verificar que no sea mayor el número que desee comprar al que se tiene en stock
                                totalByProductNV = countNewSalesNV * priceProduct; // Obtener el total a pagar por el producto seleccionado
                                return 1;
                            } else {
                                return 2;
                            }
                        } else {
                            return 4;
                        }
                    } else {
                        return 3;
                    }
                } else {
                    return 4;
                }
            } else {
                return 4;
            }
        } else {
            return 5;
        }
    }

    /**
     * Este for permite saber si ya ha sido agregado el producto en la tableNV
     *
     * @return true si se encontro y false si no, el itemPosition indica la
     * posicion en la que se encuentra
     */
    public boolean isLocationInTableNV() {
        boolean isLocated = false;
        for (int i = 0; i < systemPrincipal.tableNV.getRowCount(); i++) {
            if (systemPrincipal.tableNV.getValueAt(i, 0).equals(systemPrincipal.fieldCodigoNV.getText().trim())) {
                isLocated = true;
                itemPositionNV = i;
                break;
            }
        }
        return isLocated;
    }

    /**
     *
     * @return total a pagar de los items asignadas a la tableNV
     */
    private int totalToPay() {
        int total = 0;
        int countRow = systemPrincipal.tableNV.getRowCount();
        for (int i = 0; i < countRow; i++) {
            total = total + Integer.parseInt(tableModelNewSales.getValueAt(i, 4).toString());
        }
        return total;
    }
}
