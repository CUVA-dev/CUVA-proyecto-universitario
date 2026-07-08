package com.mycompany.cuvaproject.services;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mycompany.cuvaproject.models.Reprobated;
import com.mycompany.cuvaproject.models.Student;
import java.io.FileOutputStream;
import java.util.List;
import java.awt.Color;

public class GenerarReporte {

    public static void generarPDF(Student estudiante, List<Reprobated> reprobadas, String mensajeOficial, String rutaDestino) {
        Document documentoPdf = new Document();

        try (FileOutputStream fos = new FileOutputStream(rutaDestino)) {
            PdfWriter.getInstance(documentoPdf, fos);
            documentoPdf.open();

            // Fuentes
            Font fTitulo = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font fSubtitulo = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font fLabel = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font fValor = new Font(Font.HELVETICA, 10, Font.NORMAL);

            // 1. ENCABEZADO SIMÉTRICO CON LOGOS
            PdfPTable tablaEncabezado = new PdfPTable(new float[]{1.2f, 5f, 1.2f}); 
            tablaEncabezado.setWidthPercentage(100);
            tablaEncabezado.setSpacingAfter(15f);

            try {
                Image logoL = Image.getInstance("imagenes/logo2.png");
                logoL.scaleToFit(55, 55);
                PdfPCell celdaL = new PdfPCell(logoL);
                celdaL.setBorder(PdfPCell.NO_BORDER);
                celdaL.setHorizontalAlignment(Element.ALIGN_LEFT);
                tablaEncabezado.addCell(celdaL);
            } catch (Exception e) { tablaEncabezado.addCell(new PdfPCell(new Paragraph(" "))); }

            Paragraph textoOficial = new Paragraph();
            textoOficial.setAlignment(Element.ALIGN_CENTER);
            textoOficial.add(new Paragraph("REPÚBLICA BOLIVARIANA DE VENEZUELA\nMINISTERIO DEL PODER POPULAR PARA LA DEFENSA\nUNIVERSIDAD NACIONAL EXPERIMENTAL POLITÉCNICA DE LA FUERZA ARMADA\nNÚCLEO LARA - SEDE BARQUISIMETO", fTitulo));
            PdfPCell celdaC = new PdfPCell(textoOficial);
            celdaC.setBorder(PdfPCell.NO_BORDER);
            celdaC.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaEncabezado.addCell(celdaC);

            try {
                Image logoD = Image.getInstance("imagenes/logo1.PNG");
                logoD.scaleToFit(55, 55);
                PdfPCell celdaD = new PdfPCell(logoD);
                celdaD.setBorder(PdfPCell.NO_BORDER);
                celdaD.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tablaEncabezado.addCell(celdaD);
            } catch (Exception e) { tablaEncabezado.addCell(new PdfPCell(new Paragraph(" "))); }

            documentoPdf.add(tablaEncabezado);
            Paragraph titulo = new Paragraph("REPORTE DE AUDITORÍA", fSubtitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20f);
            documentoPdf.add(titulo);

            // 2. DATOS DEL ESTUDIANTE
            documentoPdf.add(new Paragraph("[DATOS ACADÉMICOS]", fSubtitulo));
            PdfPTable tablaEstudiante = new PdfPTable(new float[]{3f, 4f});
            tablaEstudiante.setWidthPercentage(100);
            tablaEstudiante.setSpacingAfter(15f);
            agregarFila(tablaEstudiante, "Nombres y Apellidos:", estudiante.getName() + " " + estudiante.getLastName(), fLabel, fValor);
            agregarFila(tablaEstudiante, "Cédula de Identidad:", String.valueOf(estudiante.getID()), fLabel, fValor);
            agregarFila(tablaEstudiante, "Matrícula:", estudiante.getTuition(), fLabel, fValor);
            agregarFila(tablaEstudiante, "Carrera:", estudiante.getCareer(), fLabel, fValor);
            documentoPdf.add(tablaEstudiante);

            // 3. REGISTRO DE MATERIAS REPROBADAS (Iteración Dinámica)
            documentoPdf.add(new Paragraph("[HISTORIAL DE REPROBACIÓN]", fSubtitulo));
            PdfPTable tablaMaterias = new PdfPTable(new float[]{1.5f, 4f, 1.5f, 1.5f});
            tablaMaterias.setWidthPercentage(100);
            tablaMaterias.setSpacingBefore(5f);
            tablaMaterias.setSpacingAfter(15f);
            
            // Cabeceras de la tabla de materias
            String[] cabeceras = {"Código", "Asignatura", "Período", "Nota Final"};
            for (String cab : cabeceras) {
                PdfPCell celda = new PdfPCell(new Paragraph(cab, fLabel));
                celda.setBackgroundColor(Color.LIGHT_GRAY);
                celda.setPadding(5f);
                tablaMaterias.addCell(celda);
            }
            
            // Iterar sobre las materias reprobadas para listarlas ordenadamente
            for (Reprobated rep : reprobadas) {
                tablaMaterias.addCell(new PdfPCell(new Paragraph(rep.getCodeSubject(), fValor))); // Ajusta a tu método getCode() si es diferente
                tablaMaterias.addCell(new PdfPCell(new Paragraph(rep.getNameSubject(), fValor))); 
                tablaMaterias.addCell(new PdfPCell(new Paragraph(rep.getPeriod(), fValor)));
                tablaMaterias.addCell(new PdfPCell(new Paragraph(rep.getGrade(), fValor)));
            }
            documentoPdf.add(tablaMaterias);

            // 4. NOTIFICACIÓN OFICIAL (Dictamen Dinámico)
            documentoPdf.add(new Paragraph("[DICTAMEN Y NOTIFICACIÓN OFICIAL]", fSubtitulo));
            PdfPTable tablaAviso = new PdfPTable(new float[]{1f});
            tablaAviso.setWidthPercentage(100);
            tablaAviso.setSpacingBefore(5f);
            
            Paragraph pAviso = new Paragraph(mensajeOficial, fValor);
            pAviso.setAlignment(Element.ALIGN_JUSTIFIED);
            PdfPCell celdaAviso = new PdfPCell(pAviso);
            celdaAviso.setPadding(10f);
            celdaAviso.setBorderWidth(1.5f);
            celdaAviso.setBorderColor(Color.DARK_GRAY);
            tablaAviso.addCell(celdaAviso);
            documentoPdf.add(tablaAviso);

            documentoPdf.close();
            System.out.println("[PDF] Reporte generado exitosamente en: " + rutaDestino);

        } catch (Exception e) { System.err.println("Error al generar PDF: " + e.getMessage()); }
    }

    private static void agregarFila(PdfPTable tabla, String et, String val, Font fL, Font fV) {
        PdfPCell cEt = new PdfPCell(new Paragraph(et, fL)); cEt.setBorder(PdfPCell.NO_BORDER);
        PdfPCell cVal = new PdfPCell(new Paragraph(val, fV)); cVal.setBorder(PdfPCell.NO_BORDER);
        tabla.addCell(cEt); tabla.addCell(cVal);
    }
}