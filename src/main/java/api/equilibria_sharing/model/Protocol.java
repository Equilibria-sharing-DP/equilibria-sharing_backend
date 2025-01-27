package api.equilibria_sharing.model;


import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.kernel.geom.Rectangle;
// import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.text.DocumentException;
// import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;


import api.equilibria_sharing.controller.ProtocolController;


public class Protocol {
    ProtocolController controller;
    
    public Protocol(ProtocolController controller){ this.controller = controller; }
    
    public Document getPDF(List<Booking> bookingList, java.io.ByteArrayOutputStream baos) throws DocumentException, java.io.IOException{
        try(PdfWriter writer = new PdfWriter(baos)){

            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);


        
            document.add(new Paragraph("Protocol Report").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n")); // Zeilenumbruch


            

            boolean tf = false; //auf false damit die Seite beim ersten mal nicht umgebrochen wird

            for (Booking booking : bookingList) {
                
                PdfPage page = pdfDoc.addNewPage();  //neue Seite
                
                if(tf){ document.add(new AreaBreak());} //sonst werden zu viele Seiten hinzugefügt
                tf = true; //auf true setzen damit ein page break stattfindet

                page.setMediaBox(new Rectangle(842, 595)); // Landscape: A4 (Width x Height)
                document.setMargins(300, 20, 20, 20);

                // Bild laden (aus dem gleichen Ordner wie das PDF)
                String imagePath = System.getProperty("user.dir")+"/src/main/resources/logo.png";
                
                
                ImageData imageData = ImageDataFactory.create(imagePath);
                Image image = new Image(imageData);
                image.setWidth(125); // Bildgröße anpassen
                image.setHeight(125);

                
                Table headerTable = new Table(new float[]{1, 2});
                headerTable.setWidth(UnitValue.createPercentValue(150));


                String title = "Buchung: " + booking.getMainTraveler().getFirstName() + " - " + booking.getId();
                Paragraph titleParagraph = new Paragraph(title).setBold().setFontSize(16).setTextAlignment(TextAlignment.LEFT);
                headerTable.addCell(new Cell().add(titleParagraph).setBorder(Border.NO_BORDER));
                

                headerTable.addCell(new Cell().add(image).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

                // Füge die Tabelle mit Bild und Überschrift hinzu
                document.add(headerTable);

                // Abstand nach dem Header
                document.add(new Paragraph("\n"));

                // Tabelle für die Buchungsdetails
                Table table = new Table(new float[]{1, 2}); // Zwei Spalten: Label und Wert
                table.setWidth(UnitValue.createPercentValue(140));
                
                table.setMarginLeft(30);
                
                

                // Daten der Buchung horizontal darstellen
                table.addCell(new Cell().add(new Paragraph("ID").setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getId()))));

                table.addCell(new Cell().add(new Paragraph("Accommodation").setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getAccommodation()))));

                table.addCell(new Cell().add(new Paragraph("Main Person").setBold()));
                table.addCell(new Cell().add(new Paragraph(booking.getMainTraveler().getFirstName())));

                table.addCell(new Cell().add(new Paragraph("Check-In").setBold()));
                table.addCell(new Cell().add(new Paragraph(formatDateTime(booking.getCheckIn()))));

                table.addCell(new Cell().add(new Paragraph("Expected Check-Out").setBold()));
                table.addCell(new Cell().add(new Paragraph(formatDateTime(booking.getExpectedCheckOut()))));

                table.addCell(new Cell().add(new Paragraph("Actual Check-Out").setBold()));
                table.addCell(new Cell().add(new Paragraph(formatDateTime(booking.getActualCheckOut()))));

                table.addCell(new Cell().add(new Paragraph("Tourist Tax").setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.isTouristTax()))));

                document.add(table); // Tabelle zum Dokument hinzufügen

            } 
            document.close(); 
            return document;
        }
    }
    


    
    public byte[] getCSV(List<Booking> bookingList) {
        
        StringBuilder csvContent = new StringBuilder("ID,Accommodation,MainPerson,CheckIn,ExpectedCheckOut,ActualCheckOut,TouristTax,PersonCount,persons\n");
        // Buchungsdaten verarbeiten
        for (Booking booking : bookingList) {

            // Daten zur CSV hinzufügen
            csvContent.append(booking.getId()).append(",")
                    .append(booking.getAccommodation() != null ? booking.getAccommodation().getName() : "").append(",")
                    .append(booking.getMainTraveler() != null ? booking.getMainTraveler().getFirstName() + " " + booking.getMainTraveler().getLastName() : "").append(",")
                    .append(booking.getCheckIn()).append(",")
                    .append(booking.getExpectedCheckOut()).append(",")
                    .append(booking.getActualCheckOut() != null ? booking.getActualCheckOut() : "").append(",")
                    .append(booking.isTouristTax()).append(",")
                    .append(booking.getAdditionalGuests().size() + 1).append(",") // Hauptreisender + Gäste
                    .append(booking.getAdditionalGuests().stream()
                     .map(guest -> {
                         String guestName = guest.getFirstName() + " " + guest.getLastName();
                         String birthDate = guest.getBirthDate() != null ? guest.getBirthDate().toString() : "unknown";
                         return guestName + " (" + birthDate + ")";
                     })
                     .reduce((a, b) -> a + "; " + b)
                     .orElse(""))
              .append("\n");
        }

        // Rückgabe des CSV-Inhalts als Byte-Array
        return csvContent.toString().getBytes(StandardCharsets.UTF_8);
    }

    public Document getExcel(LocalDate beginDate, LocalDate enDate){
        return null;
    }


    /*----------------------------------- */
    // Hilfsmethoden

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }


}
