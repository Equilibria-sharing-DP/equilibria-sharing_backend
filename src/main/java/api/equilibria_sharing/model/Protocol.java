package api.equilibria_sharing.model;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.itextpdf.io.IOException;
// import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import api.equilibria_sharing.controller.ProtocolController;


public class Protocol {
    ProtocolController controller;
    
    public Protocol(ProtocolController controller){ this.controller = controller; }

    public Document getPDF(LocalDate beginDate, LocalDate enDate, java.io.ByteArrayOutputStream baos) throws DocumentException{
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph("Protocol Report"));
            document.add(new Paragraph("This is a generated PDF file."));
            document.close();
            return document;
    }
    
    public byte[] getCSV(LocalDate beginDate, LocalDate endDate) {
        // Kopfzeile für die CSV-Datei
        StringBuilder csvContent = new StringBuilder("ID,Accommodation,MainPerson,CheckIn,ExpectedCheckOut,ActualCheckOut,TouristTax,PersonCount,persons\n");

        // Beispiel-Daten hinzufügen
        csvContent.append("123").append(",")
                .append("Sea View Resort").append(",")
                .append("John Doe").append(",")
                .append("2025-01-20").append(",")
                .append("2025-01-27").append(",")
                .append("2025-01-26").append(",")
                .append("true").append(",")
                .append("3").append(",")
                .append("Jane Smith; Bob Brown").append("\n");

        // Rückgabe des CSV-Inhalts als Byte-Array
        return csvContent.toString().getBytes(StandardCharsets.UTF_8);
    }

    public Document getExcel(LocalDate beginDate, LocalDate enDate){
        return null;
    }

}
