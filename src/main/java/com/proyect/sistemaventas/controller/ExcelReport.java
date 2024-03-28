package com.proyect.sistemaventas.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.proyect.sistemaventas.dal.DatabaseConnection;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReport {

    public void report() {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement ps;
        ResultSet rs;
        /**
         * Asignar una ventana para escoger sitio de guardado
         */
        JFileChooser chooser = new JFileChooser(); // Selector de archivos para elegir donde guardarlo
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel", "xlsx"); // Filtro para que solo se puedan seleccionar archivos con extensión .xls
        chooser.setFileFilter(filter); // Se establece el filtro en el selector de archivos.
        chooser.setDialogTitle("Guardar archivo"); // Asignar titulo del Dialog, la ventana donde se indica donde va a ser guardado el archivo
        chooser.setAcceptAllFileFilterUsed(false); // Se configura para que no se acepten archivos de cualquier tipo.
        /**
         * Se muestra el dialog para guardar el archivo y se verifica si el
         * usuario ha seleccionado un lugar válido para guardar el archivo.
         */
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().toString().concat(".xlsx"); // Se obtiene la ruta del archivo seleccionado por el usuario y se le agrega la extensión .xls
            Workbook book = new XSSFWorkbook(); // Se crea un nuevo libro de Excel en formato XSSFWorkbook: formato de archivo .xlsx
            Sheet sheet = book.createSheet("Productos"); // Se crea una nueva hoja en el libro de Excel llamada "Productos"
            try {
                /**
                 * Se crea un estilo de celda para el título del reporte, que
                 * será centrado vertical y horizontalmente, en negrita y con un
                 * tamaño de fuente de 14 puntos
                 */
                CellStyle tituloEstilo = book.createCellStyle();
                tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
                tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
                Font fuenteTitulo = book.createFont();
                fuenteTitulo.setFontName("Arial");
                fuenteTitulo.setBold(true);
                fuenteTitulo.setFontHeightInPoints((short) 14);
                tituloEstilo.setFont(fuenteTitulo);
                /**
                 * Se crea una fila en la hoja de Excel en la posición 2 (fila
                 * 1) para el título del reporte
                 */
                Row filaTitulo = sheet.createRow(1);
                Cell celdaTitulo = filaTitulo.createCell(1);
                celdaTitulo.setCellStyle(tituloEstilo);
                celdaTitulo.setCellValue("Reporte de Productos");

                sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3)); // Se fusionan las celdas de la fila del título para que el título ocupe varias columnas

                String[] cabecera = new String[]{"Código", "Nombre", "Proveedor", "Cantidad", "Precio"}; // Se define un array de Strings con los nombres de las columnas de la tabla de productos

                /**
                 * Se crea un estilo de celda para las cabeceras de las
                 * columnas, con un fondo azul claro, bordes delgados y texto en
                 * negrita y color blanco
                 */
                CellStyle headerStyle = book.createCellStyle();
                headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                Font font = book.createFont();
                font.setFontName("Arial");
                font.setBold(true);
                font.setColor(IndexedColors.WHITE.getIndex());
                font.setFontHeightInPoints((short) 12);
                headerStyle.setFont(font);

                Row filaEncabezados = sheet.createRow(4); // Se crea una fila en la hoja de Excel en la posición 5 (fila 4 en el programa) para las cabeceras de las columnas

                /**
                 * Se recorre el array de cabeceras y se crea una celda para
                 * cada una en la fila de cabeceras, aplicando el estilo
                 * headerStyle
                 */
                for (int i = 0; i < cabecera.length; i++) {
                    Cell celdaEnzabezado = filaEncabezados.createCell(i);
                    celdaEnzabezado.setCellStyle(headerStyle);
                    celdaEnzabezado.setCellValue(cabecera[i]);
                }

                int numFilaDatos = 5; // Se inicializa un contador para la fila de datos, que comenzará en la fila 6 en excel (fila 5 en el programa)

                /**
                 * Se crea un estilo de celda para los datos de la tabla, con
                 * bordes delgados en todas las direcciones
                 */
                CellStyle datosEstilo = book.createCellStyle();
                datosEstilo.setBorderBottom(BorderStyle.THIN);
                datosEstilo.setBorderLeft(BorderStyle.THIN);
                datosEstilo.setBorderRight(BorderStyle.THIN);
                datosEstilo.setBorderBottom(BorderStyle.THIN);

                ps = conn.prepareStatement("SELECT a.codigo codigo, a.nombre nombre, b.nombre proveedor, a.cantidad cantidad, a.precio precio FROM productos a INNER JOIN proveedores b ON a.proveedor = b.id_proveedores");

                rs = ps.executeQuery();
                int numCol = rs.getMetaData().getColumnCount();
                /**
                 * Se recorre el resultado de la consulta y se crea una fila en
                 * la hoja de Excel para cada registro de la tabla de productos
                 */
                while (rs.next()) {
                    Row filaDatos = sheet.createRow(numFilaDatos);
                    for (int a = 0; a < numCol; a++) {
                        Cell CeldaDatos = filaDatos.createCell(a);
                        CeldaDatos.setCellStyle(datosEstilo);
                        CeldaDatos.setCellValue(rs.getString(a + 1));
                    }
                    numFilaDatos++;
                }
                /**
                 * Se ajusta automáticamente el ancho de las columnas para que
                 * el contenido se ajuste correctamente
                 */
                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(3);
                sheet.autoSizeColumn(4);

                sheet.setZoom(120); // Se establece un zoom para la hoja de Excel
                File file = new File(ruta);
                try (FileOutputStream fileOut = new FileOutputStream(file)) { // Cierra automaticamente el flujo de salida
                    book.write(fileOut); // Se escribe el libro de Excel en el archivo                    
                }
                Desktop.getDesktop().open(file); // Se abre el archivo de Excel generado automáticamente con la aplicación predeterminada para archivos .xlsx
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ExcelReport.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | SQLException ex) {
                Logger.getLogger(ExcelReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
