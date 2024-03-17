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

        Workbook book = new XSSFWorkbook(); // Se crea un nuevo libro de Excel en formato XSSFWorkbook: formato de archivo .xlsx
        Sheet sheet = book.createSheet("Productos"); // Se crea una nueva hoja en el libro de Excel llamada "Productos"

        try {
            /**
             * Se crea un estilo de celda para el título del reporte, que será
             * centrado vertical y horizontalmente, en negrita y con un tamaño
             * de fuente de 14 puntos
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
             * Se crea una fila en la hoja de Excel en la posición 2 (fila 1)
             * para el título del reporte
             */
            Row filaTitulo = sheet.createRow(1);
            Cell celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("Reporte de Productos");

            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3)); // Se fusionan las celdas de la fila del título para que el título ocupe varias columnas

            String[] cabecera = new String[]{"Código", "Nombre", "Proveedor", "Cantidad", "Precio"}; // Se define un array de Strings con los nombres de las columnas de la tabla de productos

            /**
             * Se crea un estilo de celda para las cabeceras de las columnas,
             * con un fondo azul claro, bordes delgados y texto en negrita y
             * color blanco
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

            Row filaEncabezados = sheet.createRow(4); // Se crea una fila en la hoja de Excel en la posición 5 (fila 4) para las cabeceras de las columnas

            /**
             * Se recorre el array de cabeceras y se crea una celda para cada
             * una en la fila de cabeceras, aplicando el estilo headerStyle
             */
            for (int i = 0; i < cabecera.length; i++) {
                Cell celdaEnzabezado = filaEncabezados.createCell(i);
                celdaEnzabezado.setCellStyle(headerStyle);
                celdaEnzabezado.setCellValue(cabecera[i]);
            }

            int numFilaDatos = 5; // Se inicializa un contador para la fila de datos, que comenzará en la fila 6 (fila 5)
            
            /**
             * Se crea un estilo de celda para los datos de la tabla, con bordes
             * delgados en todas las direcciones
             */
            CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            datosEstilo.setBorderLeft(BorderStyle.THIN);
            datosEstilo.setBorderRight(BorderStyle.THIN);
            datosEstilo.setBorderBottom(BorderStyle.THIN);

            ps = conn.prepareStatement("SELECT codigo, nombre, proveedor, cantidad, precio FROM productos");
            rs = ps.executeQuery();
            int numCol = rs.getMetaData().getColumnCount();
            /**
             * Se recorre el resultado de la consulta y se crea una fila en la
             * hoja de Excel para cada registro de la tabla de productos
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
             * Se ajusta automáticamente el ancho de las columnas para que el
             * contenido se ajuste correctamente
             */
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);

            sheet.setZoom(150); // Se establece un zoom del 150% para la hoja de Excel
            String fileName = "productos"; //  Se define el nombre del archivo de Excel a guardar en la carpeta de descargas del usuario
            String home = System.getProperty("user.home");
            File file = new File(home + "/Downloads/" + fileName + ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            book.write(fileOut); // Se escribe el libro de Excel en el archivo
            fileOut.close(); // Se cierra el flujo de salida
            Desktop.getDesktop().open(file); // Se abre el archivo de Excel generado automáticamente con la aplicación predeterminada para archivos .xlsx
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(ExcelReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
