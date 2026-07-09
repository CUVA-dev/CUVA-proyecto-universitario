package com.mycompany.cuvaproject.services;

import com.mycompany.cuvaproject.models.Reprobated;
import com.mycompany.cuvaproject.models.Student;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ProcesadorRecord {

    
    public String obtenerTextoBruto(String rutaArchivo) {
        try (PDDocument document = Loader.loadPDF(new File(rutaArchivo))) {
            PDFTextStripper stripper = new PDFTextStripper();
            
           
            stripper.setSortByPosition(true);
            
            return stripper.getText(document);
        } catch (Exception e) {
            System.out.println("Error al leer el archivo PDF: " + e.getMessage());
            return "";
        }
    }

    /**
     * Método independiente que filtra el texto bruto, extrae los datos del alumno
     * y los empaqueta en una nueva instancia del objeto del modelo Student.
     */
    public Student extraerEstudiante(String textoBruto) {
        String textoNorm = textoBruto.replaceAll("\\s+", " ");
        
        String name = "No encontrado";
        String lastName = "No encontrado";
        String career = "No encontrado";
        String tuition = "No encontrado";
        int idInt = 0;
        int UC=0;

        Matcher mApell = Pattern.compile("Apellidos:\\s*(.*?)\\s*(?=Nombres|Matr[ií]cula|Documento|Carrera|Per[ií]odo|P[áa]gina|Identidad|V-|$)", Pattern.CASE_INSENSITIVE).matcher(textoNorm);
        if (mApell.find()) lastName = mApell.group(1).trim();

        Matcher mNomb = Pattern.compile("Nombres:\\s*(.*?)\\s*(?=Apellidos|Matr[ií]cula|Documento|Carrera|Per[ií]odo|P[áa]gina|Identidad|V-|$)", Pattern.CASE_INSENSITIVE).matcher(textoNorm);
        if (mNomb.find()) name = mNomb.group(1).trim();

        Matcher mCed = Pattern.compile("Identidad:\\s*V?-?\\s*(\\d+)", Pattern.CASE_INSENSITIVE).matcher(textoNorm);
        if (mCed.find()) {
            try { idInt = Integer.parseInt(mCed.group(1).trim()); } catch (Exception e) { idInt = 0; }
        }

        Matcher mCarr = Pattern.compile("Carrera:\\s*(.*?)\\s*(?=Apellidos|Nombres|Matr[ií]cula|Documento|Per[ií]odo|P[áa]gina|Identidad|V-|REP|MINISTERIO|UNEFA|CINU|\\b\\d|$)", Pattern.CASE_INSENSITIVE).matcher(textoNorm);
        if (mCarr.find()) career = mCarr.group(1).trim();

        Matcher mMat = Pattern.compile("Matr[ií\\S]cula:\\s*([0-9-]+)", Pattern.CASE_INSENSITIVE).matcher(textoNorm);
        if (mMat.find()) tuition = mMat.group(1).trim();
        
        Matcher mCred = Pattern.compile("(?:Unidades\\s+de\\s+Cr[eé]dito|U\\.?C\\.?)[^0-9]*(\\d{2,3})", Pattern.CASE_INSENSITIVE).matcher(textoNorm);
        while (mCred.find()) {
            try {
                int tempCred = Integer.parseInt(mCred.group(1));
                // Nos quedamos con el valor más alto en caso de que el PDF tenga subtotales por semestre
                if (tempCred > UC) {
                    UC = tempCred; 
                }
            } catch (Exception e) {}
        }

        return new Student(name, lastName, career, idInt, tuition, UC);
    }

    /**
     * Orquesta la visualización de los datos procesados en la consola.
     */
    public void procesarYMostrarModelos(String textoBruto, Student estudianteObjeto) {
        System.out.println("====================================================================");
        System.out.println(" CONFIRMACIÓN DE EXTRACCIÓN AL MODELO DE BASE DE DATOS");
        System.out.println("====================================================================");

        String textoNorm = textoBruto.replaceAll("\\s+", " ");
        String name = "No encontrado", lastName = "No encontrado", career = "No encontrado", tuition = "No encontrado";
        int idInt = 0;

        Matcher mApell = Pattern.compile("Apellidos:\\s*(.*?)\\s*(?=Nombres|Matr[ií]cula|Documento|Carrera|Per[ií]odo|P[áa]gina|Identidad|V-|$)", Pattern.CASE_INSENSITIVE).matcher(textoNorm);
        if (mApell.find()) lastName = mApell.group(1).trim();

        Matcher mNomb = Pattern.compile("Nombres:\\s*(.*?)\\s*(?=Apellidos|Matr[ií]cula|Documento|Carrera|Per[ií]odo|P[áa]gina|Identidad|V-|$)", Pattern.CASE_INSENSITIVE).matcher(textoNorm);
        if (mNomb.find()) name = mNomb.group(1).trim();

        Matcher mCed = Pattern.compile("Identidad:\\s*V?-?\\s*(\\d+)", Pattern.CASE_INSENSITIVE).matcher(textoNorm);
        if (mCed.find()) { try { idInt = Integer.parseInt(mCed.group(1).trim()); } catch (Exception e) {} }

        Matcher mCarr = Pattern.compile("Carrera:\\s*(.*?)\\s*(?=Apellidos|Nombres|Matr[ií]cula|Documento|Per[ií]odo|P[áa]gina|Identidad|V-|REP|MINISTERIO|UNEFA|CINU|\\b\\d|$)", Pattern.CASE_INSENSITIVE).matcher(textoNorm);
        if (mCarr.find()) career = mCarr.group(1).trim();

        Matcher mMat = Pattern.compile("Matr[ií\\S]cula:\\s*([0-9-]+)", Pattern.CASE_INSENSITIVE).matcher(textoNorm);
        if (mMat.find()) tuition = mMat.group(1).trim();

        System.out.println("[NUEVO OBJETO -> STUDENT]");
        System.out.println("  • Nombres:   \"" + name + "\"");
        System.out.println("  • Apellidos: \"" + lastName + "\"");
        System.out.println("  • Carrera:   \"" + career + "\"");
        System.out.println("  • Cédula:    " + idInt);
        System.out.println("  • Matrícula: \"" + tuition + "\"");
        System.out.println();

        // Extraemos e imprimimos el listado de reprobados real
        List<Reprobated> reprobadas = extraerMateriasReprobadas(textoBruto, idInt);
        
        System.out.println("[NUEVOS OBJETOS -> REPROBATED] -> Total Encontrados: " + reprobadas.size());
        System.out.println("====================================================================");
    }

  /**
     * Analiza el historial académico, filtra años, extrae materias reprobadas
     * y evalúa las condiciones de auditoría para determinar el tipo de reporte.
     */
    public List<Reprobated> extraerMateriasReprobadas(String textoBruto, int idInt) {
        List<Reprobated> listaReprobadas = new ArrayList<>();
        String textoNorm = textoBruto.replaceAll("\\s+", " ");

        // --- NUEVOS CONTADORES (Diccionarios) ---
        Map<String, Integer> inscritasPorPeriodo = new HashMap<>();
        Map<String, Integer> reprobadasPorPeriodo = new HashMap<>();
        Map<String, Integer> repeticionesMateria = new HashMap<>();
        // ----------------------------------------

        String[] bloques = textoNorm.split("(?=\\b\\d[A-Z]*-\\d{4}\\b)");

        for (String bloque : bloques) {
            Pattern patronCodigo = Pattern.compile("\\b([A-Z]{3,4}-?\\d{4,5})\\b");
            Matcher m = patronCodigo.matcher(bloque);

            if (m.find()) {
                String code = m.group(1);

                // ESCUDO ROBUSTO (Dinámico para cualquier año 20XX)
                if (code.matches(".*20\\d{2}.*")) {
                    continue; 
                }

                String resto = bloque;

                // Capturamos el PERIODO
                String period = "Desconocido";
                Matcher mPeriodo = Pattern.compile("\\b\\d[A-Z]*-\\d{4}\\b").matcher(resto);
                if (mPeriodo.find()) {
                    period = mPeriodo.group();
                }

                // === REGISTRAMOS LA MATERIA INSCRITA ===
                inscritasPorPeriodo.put(period, inscritasPorPeriodo.getOrDefault(period, 0) + 1);

                // Capturamos el SEMESTRE
                String semester = "00";
                Matcher mSem = Pattern.compile("\\b(0[1-9]|1[0-2])\\b").matcher(resto);
                if (mSem.find()) {
                    semester = mSem.group(1);
                }

                // Limpieza de metadatos
                resto = resto.replace(code, "");
                resto = resto.replaceFirst("\\b\\d[A-Z]*-\\d{4}\\b", "");
                resto = resto.replaceFirst("\\b(0[1-9]|1[0-2])\\b", "");

                boolean reproboPorTexto = resto.toUpperCase().matches(".*(REPROB[OÓ\\S]*|INASISTENCIA).*");

                String restoUpper = resto.toUpperCase();
                int idxCorte = Integer.MAX_VALUE;
                String[] keywordsCorte = {"REPROB", "APROB", "INASISTENCIA", "ÍNDICE", "INDICE", "PÁGINA", "PAGINA", "MATRICULA"};
                for (String kw : keywordsCorte) {
                    int idx = restoUpper.indexOf(kw);
                    if (idx != -1 && idx < idxCorte) {
                        idxCorte = idx;
                    }
                }
                if (idxCorte != Integer.MAX_VALUE) {
                    resto = resto.substring(0, idxCorte);
                }

                Matcher mNum = Pattern.compile("\\b(\\d+)\\b").matcher(resto);
                List<Integer> numeros = new ArrayList<>();
                while (mNum.find()) {
                    numeros.add(Integer.parseInt(mNum.group(1)));
                }

                resto = resto.replaceAll("\\b(\\d+)\\b", "");
                String nameSubject = resto.replaceAll("[^a-zA-ZÑÁÉÍÓÚñáéíóúIIVX1-9 ]", "").replaceAll("\\s+", " ").trim();
                if (nameSubject.isEmpty()) nameSubject = "Materia Desconocida";

                int calificacion = -1;
                if (!numeros.isEmpty()) {
                    calificacion = numeros.get(0); 
                }

                boolean esReprobada = (calificacion >= 0 && calificacion < 10) || reproboPorTexto;

                if (esReprobada) {
                    // === REGISTRAMOS LA MATERIA REPROBADA ===
                    reprobadasPorPeriodo.put(period, reprobadasPorPeriodo.getOrDefault(period, 0) + 1);
                    repeticionesMateria.put(nameSubject, repeticionesMateria.getOrDefault(nameSubject, 0) + 1);

                    String notaFinal;
                    if (calificacion >= 0 && calificacion < 10) {
                        notaFinal = String.format("%02d", calificacion);
                    } else {
                        notaFinal = "REPROBÓ";
                    }

                    Reprobated materiaAplazada = new Reprobated(null, nameSubject, String.valueOf(idInt), code, period, notaFinal);
                    listaReprobadas.add(materiaAplazada);
                    System.out.println("  -> CI: " + idInt + " [" + period + "] " + code + " | Nota: " + notaFinal);
                }
            }
        }

        // === EVALUACIÓN FINAL DE CONDICIONES (Auditoría) ===
        System.out.println("\n[EVALUACIÓN DE CONDICIONES DE AUDITORÍA]");
        
        boolean alertaMitad = false;
        for (String per : inscritasPorPeriodo.keySet()) {
            int inscritas = inscritasPorPeriodo.get(per);
            int reprobadas = reprobadasPorPeriodo.getOrDefault(per, 0);
            
            if (reprobadas > (inscritas / 2.0)) {
                alertaMitad = true;
                System.out.println("  ! CONDICIÓN 1 CUMPLIDA: En el periodo [" + per + "] reprobó " + reprobadas + " de " + inscritas + " materias.");
            }
        }

        boolean alertaTresVeces = false;
        for (String mat : repeticionesMateria.keySet()) {
            int veces = repeticionesMateria.get(mat);
            if (veces >= 3) {
                alertaTresVeces = true;
                System.out.println("  ! CONDICIÓN 2 CUMPLIDA: Reprobó la materia [" + mat + "] " + veces + " veces.");
            }
        }

        // Determinar resultado final
        if (alertaMitad && alertaTresVeces) {
            System.out.println("  >>> RESULTADO: CASO CRÍTICO (Generar Plantilla 3)");
        } else if (alertaTresVeces) {
            System.out.println("  >>> RESULTADO: SUSPENSIÓN TERCERA VEZ (Generar Plantilla 2)");
        } else if (alertaMitad) {
            System.out.println("  >>> RESULTADO: BAJO RENDIMIENTO (Generar Plantilla 1)");
        } else {
            System.out.println("  >>> RESULTADO: REGULAR (No se requiere reporte)");
        }

        return listaReprobadas;
    }


    // LÓGICA DE AUDITORÍA (REGLAMENTO UNEFA)
    /**
     * CONDICIÓN 1: Evalúa si reprobó más del 50% en un mismo periodo
     */
    private boolean aplazoMasDeLaMitad(List<Reprobated> reprobadas, String textoBruto) {
        java.util.Set<String> periodosReprobados = new java.util.HashSet<>();
        for (Reprobated r : reprobadas) {
            periodosReprobados.add(r.getPeriod()); 
        }

        String[] bloques = textoBruto.replaceAll("\\s+", " ").split("(?=\\b\\d[A-Z]*-\\d{4}\\b)");

        for (String periodo : periodosReprobados) {
            int totalInscritasEnPeriodo = 0;
            int totalReprobadasEnPeriodo = 0;

            for (String bloque : bloques) {
                if (bloque.trim().startsWith(periodo)) {
                    if (Pattern.compile("\\b([A-Z]{3,4}-?\\d{4,5})\\b").matcher(bloque).find()) {
                        totalInscritasEnPeriodo++;
                    }
                }
            }

            for (Reprobated r : reprobadas) {
                if (r.getPeriod().equals(periodo)) totalReprobadasEnPeriodo++;
            }

            if (totalInscritasEnPeriodo > 0 && totalReprobadasEnPeriodo > (totalInscritasEnPeriodo / 2.0)) {
                return true;
            }
        }
        return false;
    }

    /*
     * CONDICIÓN 2: Evalúa si reprobó la misma materia 3 veces
     */
    private boolean tieneTripleteReprobado(List<Reprobated> reprobadas) {
        java.util.Map<String, Integer> conteo = new java.util.HashMap<>();
        for (Reprobated r : reprobadas) {
            String codigo = r.getCodeSubject();
            conteo.put(codigo, conteo.getOrDefault(codigo, 0) + 1);
            if (conteo.get(codigo) >= 3) return true;
        }
        return false;
    }

    // 
    // FLUJO CENTRAL AUTOMATIZADO - CONEXIÓN CON CARPETA DESTINO
    // 
    public void resultado(String rutaArchivo, String carpetaDestino) {
        System.out.println(" Iniciando flujo automatizado para: " + rutaArchivo);
        
        String textoBrutoReal = obtenerTextoBruto(rutaArchivo);
        if (textoBrutoReal == null || textoBrutoReal.trim().isEmpty()) {
            System.err.println(" El texto extraído está vacío. Abortando análisis.");
            return;
        }
        
        // 1. Extracción de Modelos
        Student estudianteDetectado = extraerEstudiante(textoBrutoReal);       
        procesarYMostrarModelos(textoBrutoReal, estudianteDetectado);
        
        // Obtenemos la lista que genero tu método modificar.
        List<Reprobated> reprobadas = extraerMateriasReprobadas(textoBrutoReal, estudianteDetectado.getID());

        // --- 2. AUDITORÍA INTELIGENTE: EVALUAR CASOS ---
        boolean casoMitad = aplazoMasDeLaMitad(reprobadas, textoBrutoReal);
        boolean casoTriplete = tieneTripleteReprobado(reprobadas);
        String mensajeDictamen = "";
        
        if (casoMitad && casoTriplete) {
            // CASO 3: Cumple ambas
            System.out.println("[ALERTA] Incurre en doble falta (Mitad + Triplete).");
            mensajeDictamen = "Debido al incumplimiento del reglamento interno de la Universidad Nacional Experimental de la Fuerza Armada, se aplicará una suspensión inmediata de un (1) período académico al estudiante por incurrir en doble falta reglamentaria: haber reprobado más del cincuenta por ciento (50%) de la carga académica de un mismo período y reprobar una misma unidad curricular en tres (3) oportunidades.";
        } 
        else if (casoTriplete) {
            // CASO 2: Solo triplete
            System.out.println("[ALERTA] Incurre en falta por triple reprobación.");
            mensajeDictamen = "Debido al incumplimiento del reglamento interno de la Universidad Nacional Experimental de la Fuerza Armada, se aplicará una suspensión de un (1) período académico al estudiante por haber reprobado una misma unidad curricular en tres (3) oportunidades in su récord académico.";
        } 
        else if (casoMitad) {
            // CASO 1: Solo mitad reprobada
            System.out.println("[ALERTA] Incurre en falta por >50% carga reprobada.");
            mensajeDictamen = "Debido al incumplimiento del reglamento interno de la Universidad Nacional Experimental de la Fuerza Armada, se aplicará una suspensión de un (1) período académico al estudiante por haber reprobado más del cincuenta por ciento (50%) de la carga académica correspondiente a un mismo período.";
        }

        // 3. GENERAR EL PDF (Solo si aplica sanción)
        if (!mensajeDictamen.isEmpty()) {
            // NUEVO: Concatenamos la ruta de descargas recibida por parámetro para guardar el archivo allí
            String rutaSalidaPDF = carpetaDestino + "Reporte_Auditoria_" + estudianteDetectado.getID() + ".pdf";
            
            System.out.println("[CUVA] Generando Reporte de Auditoría PDF en: " + rutaSalidaPDF);
            // Llamamos a la clase de reporte
            GenerarReporte.generarPDF(estudianteDetectado, reprobadas, mensajeDictamen, rutaSalidaPDF);
        } else {
            System.out.println("[CUVA] Auditoría limpia. El estudiante no cumple condiciones de sanción.");
        }
    }
}