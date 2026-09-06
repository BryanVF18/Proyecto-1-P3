package reportes;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/* Generador de reportes en PDF GENERICO.
   El enunciado pide que TODAS las funcionalidades (Funcionarios,
   Categorias, Recursos, Reservas, Calendarizacion, etc.) puedan
   generar un reporte en PDF. En lugar de repetir el codigo de
   iText/OpenPDF en cada pantalla, esta clase recibe un titulo,
   los encabezados de columna y las filas ya convertidas a texto,
   y arma el PDF con formato de tabla (como el ejemplo del enunciado).
   Cualquier CRUD del equipo (el mio o el de la Persona C) puede
   reutilizarla sin tener que entender OpenPDF por dentro. */
public class GeneradorReportePDF {

    private static final Color COLOR_ENCABEZADO = new Color(220, 20, 20);

    public static void generar(
            String rutaArchivo,
            String titulo,
            String[] encabezados,
            List<String[]> filas
    ) throws DocumentException, IOException {

        Document documento = new Document(PageSize.A4, 40, 40, 50, 50);
        FileOutputStream salida = new FileOutputStream(rutaArchivo);

        PdfWriter.getInstance(documento, salida);
        documento.open();

        agregarTitulo(documento, titulo);
        agregarTabla(documento, encabezados, filas);

        documento.close();
        salida.close();
    }

    private static void agregarTitulo(Document documento, String titulo) throws DocumentException {

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);

        Paragraph parrafoTitulo = new Paragraph(titulo, fuenteTitulo);
        parrafoTitulo.setAlignment(Element.ALIGN_CENTER);
        parrafoTitulo.setSpacingAfter(20);

        documento.add(parrafoTitulo);
    }

    private static void agregarTabla(
            Document documento,
            String[] encabezados,
            List<String[]> filas
    ) throws DocumentException {

        PdfPTable tabla = new PdfPTable(encabezados.length);
        tabla.setWidthPercentage(100);

        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);

        for (int i = 0; i < encabezados.length; i++) {

            PdfPCell celda = new PdfPCell(new Phrase(encabezados[i], fuenteEncabezado));
            celda.setBackgroundColor(COLOR_ENCABEZADO);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(6);

            tabla.addCell(celda);
        }

        Font fuenteCelda = FontFactory.getFont(FontFactory.HELVETICA, 11);

        for (int f = 0; f < filas.size(); f++) {

            String[] fila = filas.get(f);

            for (int c = 0; c < fila.length; c++) {

                PdfPCell celda = new PdfPCell(new Phrase(fila[c], fuenteCelda));
                celda.setPadding(5);

                tabla.addCell(celda);
            }
        }

        documento.add(tabla);
    }
}
