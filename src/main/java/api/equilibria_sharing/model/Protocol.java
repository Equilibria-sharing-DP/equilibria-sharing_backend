package api.equilibria_sharing.model;

import api.equilibria_sharing.controller.ProtocolController;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;


import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.text.DocumentException;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.IOException;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;





public class Protocol {
    ProtocolController controller;
    
    public Protocol(ProtocolController controller){ this.controller = controller; }
    
    public Document getPDF(List<Booking> bookingList, ByteArrayOutputStream baos) throws DocumentException, java.io.IOException {
        try (PdfWriter writer = new PdfWriter(baos)) {
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
    
            // Seite auf Querformat setzen
            pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
    
            //boolean addPageBreak = false;
    
            int totalBookings = bookingList.size();

            for (int i = 0; i < totalBookings; i++) {
                Booking booking = bookingList.get(i);

                // Seitenumbruch nur vor der nächsten Buchung, außer bei der ersten
                if (i > 0) {
                    document.add(new AreaBreak());  // Seitenumbruch nach der ersten Buchung
                }

                // Neue Seite nur hinzufügen, wenn es nicht die letzte Buchung ist
                if (i < totalBookings - 1) {
                    pdfDoc.addNewPage();  // Neue Seite hinzufügen, aber nicht nach der letzten Buchung
                }

                document.setMargins(20, 20, 20, 20);

                // Füge Header, Inhalt und Footer hinzu
                addHeader(document, booking);
                addContentWithTables(document, booking);
                addBottom(document, pdfDoc, bookingList);
            }
    
            document.close();
            return document;
        }
    }
    
    private void addHeader(Document document, Booking booking) throws IOException {
        String title = "Buchung: " + booking.getMainTraveler().getFirstName() + " " + booking.getMainTraveler().getLastName() + " - " + booking.getId();
        Paragraph titleParagraph = new Paragraph(title).setBold().setFontSize(16).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER);
    
        Table headerTable = new Table(new float[]{1, 1});
        headerTable.setWidth(UnitValue.createPercentValue(100)).setBorder(Border.NO_BORDER);
        
        Cell titleCell = new Cell().add(titleParagraph);
        titleCell.setBorder(Border.NO_BORDER);  // Border für die Zelle entfernen
        
        headerTable.addCell(titleCell);
        document.add(headerTable);
        document.add(new Paragraph("\n")); // Abstand nach dem Header
        
    }
    
    /**
     * Da sich sonst niemand auskennt welche Tabelle was macht: (Ich morgen auch nicht mehr)
     * table1: Beinhaltet das Bild und und die Tabelle für Vorname bis PLZ
     * outerTable: Beinhaltet [Vorname bist Staat] und [Straße bis PLZ]
     * innerTable: Beinhaltet [Vorname bist Staat] (da 2 Spalten)
     * innerTable2: Beinhaltet  [Straße bis PLZ] (da nur 1 Spalte)
     * tableDates: Beinhaltet die Datumswerte 
     * tableAddOns: Beinhaltet die zusätzlichen Gäste
     */

    private void addContentWithTables(Document document, Booking booking) throws MalformedURLException {
        // Überschrift 1
        document.add(new Paragraph("Allgemein").setBold().setFontSize(14).setTextAlignment(TextAlignment.LEFT));
        
        // Tabelle 1
        Table table1 = new Table(new float[]{1, 1});
        table1.setWidth(UnitValue.createPercentValue(100)).setBorder(Border.NO_BORDER);

        Table outerTable = new Table(new float[]{1});
        outerTable.setWidth(UnitValue.createPercentValue(100));

        outerTable.addCell(new Cell().setHeight(10).setBorder(Border.NO_BORDER));

        

        Table innerTable = new Table(new float[] {2,1});
        innerTable.setWidth(UnitValue.createPercentValue(100));
        innerTable.addCell(createHalfBoldCell("Vorname: ",booking.getMainTraveler().getFirstName()));
        innerTable.addCell(createHalfBoldCell("Geschlecht: ", String.valueOf(booking.getMainTraveler().getGender())));

        innerTable.addCell(createHalfBoldCell("Nachname: ", booking.getMainTraveler().getLastName()));
        innerTable.addCell(createHalfBoldCell("Geburtsdatum: ", String.valueOf(booking.getMainTraveler().getBirthDate())));

        innerTable.addCell(createHalfBoldCell("Reisedokument Nr.: ", booking.getMainTraveler().getTravelDocument().getDocumentNr()));
        innerTable.addCell(createHalfBoldCell("Staatsangehörigkeit: ", booking.getMainTraveler().getTravelDocument().getCountry()));

        Table innerTable2 = new Table(new float[]{3, 3});
        innerTable2.setWidth(UnitValue.createPercentValue(100));

        String temp = booking.getMainTraveler().getAddress().getStreet()+" / "+booking.getMainTraveler().getAddress().getHouseNumber()+" / "+booking.getMainTraveler().getAddress().getAddressAdditional();
        innerTable2.addCell(createHalfBoldCell("Straße/Nummer/Zusatz: ",temp));
        innerTable2.addCell(new Cell().setBorder(Border.NO_BORDER));

        temp = booking.getMainTraveler().getAddress().getPostalCode()+" / "+booking.getMainTraveler().getAddress().getCity()+" / Land?";
        innerTable2.addCell(createHalfBoldCell("PLZ/Ort/Staat: ",temp));
        innerTable2.addCell(new Cell().setBorder(Border.NO_BORDER));

        temp = booking.getAccommodation().getName();
        innerTable2.addCell(createHalfBoldCell("Mietobjekt: ",temp));
        
        outerTable.addCell(new Cell().add(innerTable).setBorder(Border.NO_BORDER));
        outerTable.addCell(new Cell().add(innerTable2).setBorder(Border.NO_BORDER));

        table1.addCell(new Cell().add(outerTable).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(1)));
    
        // Bild hinzufügen
        String imagePath = System.getProperty("user.dir") + "/src/main/resources/logo.png";
        ImageData imageData = ImageDataFactory.create(imagePath);
        Image image = new Image(imageData).scaleToFit(150, 150);
        table1.addCell(new Cell().add(image.setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
    
        document.add(table1.setBorder(Border.NO_BORDER));
    
        // Überschrift Dauer
        document.add(new Paragraph("Dauer").setBold().setFontSize(14).setTextAlignment(TextAlignment.LEFT));
    
        // Tabelle für Datumsangaben
        Table tableDates = new Table(new float[]{2, 2, 2});
        tableDates.setWidth(UnitValue.createPercentValue(100));
        tableDates.addCell(new Cell().setHeight(5).setBorder(Border.NO_BORDER));
        tableDates.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableDates.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableDates.addCell(createHalfBoldCell("Ankunft: ",String.valueOf(booking.getCheckIn())));
        tableDates.addCell(createHalfBoldCell("Geplante Abreise: ",String.valueOf(booking.getExpectedCheckOut())));
        tableDates.addCell(createHalfBoldCell("Abreise: ", String.valueOf(booking.getActualCheckOut())));
    
        document.add(tableDates.setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(1)));

    
        
        //Überschrift Zusätzliche Personen
        document.add(new Paragraph("\nWeitere Personen").setBold().setFontSize(14).setTextAlignment(TextAlignment.LEFT));

        // Tabelle für die restlichen Personen
        Table tableAddOns = new Table(new float[]{2, 2, 2});
        tableAddOns.setWidth(UnitValue.createPercentValue(100));
        tableAddOns.addCell(new Cell().setHeight(5).setBorder(Border.NO_BORDER));
        tableAddOns.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableAddOns.addCell(new Cell().setBorder(Border.NO_BORDER));
        tableAddOns.addCell(createBoldCell("Vorname:"));
        tableAddOns.addCell(createBoldCell("Nachname:"));
        tableAddOns.addCell(createBoldCell("Geburtsdatum:"));
        List<Person> list = booking.getAdditionalGuests();
        for(Person person : list){
            tableAddOns.addCell(createCell(person.getFirstName()));
            tableAddOns.addCell(createCell(person.getLastName()));
            tableAddOns.addCell(createCell(String.valueOf(person.getBirthDate())));
        }

        document.add(tableAddOns.setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(1)));


    }


    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- */
    // Unten an der Seite

    private void addBottom(Document document, PdfDocument pdf, List<Booking> bookingList) {
        
        // Erstelle die Tabelle
        Table unten = new Table(new float[]{1,2,1});
        unten.setWidth(1000);
        String temp = "Buchungen von: \n" + bookingList.get(bookingList.size() - 1).getExpectedCheckOut() + "bis" + bookingList.get(0).getCheckIn();
        unten.addCell(createBoldCell(temp).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        
        unten.addCell(new Cell().setWidth(100).setBorder(Border.NO_BORDER));

        
        unten.addCell(createCell("\nEquilibria Immobilienmanagement GmbH").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
    
        unten.setFixedPosition(36, 15, 500); // x=36, y=position, width=500
    
        // Füge die Tabelle zum Dokument hinzu
        document.add(unten);
    }
    

    
    

    
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- */
    // CSV

    
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

    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- */
    // Excel


    public byte[] getExcel(List<Booking> bookingList) throws java.io.IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Bookings");
            
            // Header-Zeile erstellen
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Accommodation", "MainPerson", "CheckIn", "ExpectedCheckOut", "ActualCheckOut", "TouristTax", "PersonCount", "Persons"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // Buchungsdaten in das Excel-Sheet schreiben
            int rowNum = 1;
            for (Booking booking : bookingList) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(booking.getId());
                row.createCell(1).setCellValue(booking.getAccommodation() != null ? booking.getAccommodation().getName() : "");
                row.createCell(2).setCellValue(booking.getMainTraveler() != null ? booking.getMainTraveler().getFirstName() + " " + booking.getMainTraveler().getLastName() : "");
                row.createCell(3).setCellValue(booking.getCheckIn().toString());
                row.createCell(4).setCellValue(booking.getExpectedCheckOut().toString());
                row.createCell(5).setCellValue(booking.getActualCheckOut() != null ? booking.getActualCheckOut().toString() : "");
                row.createCell(6).setCellValue(booking.isTouristTax());
                row.createCell(7).setCellValue(booking.getAdditionalGuests().size() + 1);
                
                // Gästeinformationen als String speichern
                String persons = booking.getAdditionalGuests().stream()
                        .map(guest -> guest.getFirstName() + " " + guest.getLastName() + " (" + (guest.getBirthDate() != null ? guest.getBirthDate().toString() : "unknown") + ")")
                        .reduce((a, b) -> a + "; " + b)
                        .orElse("");
                
                row.createCell(8).setCellValue(persons);
            }
            
            // Datei in ByteArray konvertieren
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];

        }
    }



    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- */
    // Hilfsmethoden
    
    private Cell createCell(String text) {
        return new Cell().add(new Paragraph(text)).setBorder(Border.NO_BORDER);
    }

    private Cell createHalfBoldCell(String text1, String text2){
        if(text1 == null){text1 = "N/A";}
        if(text2 == null){text2 = "N/A";}
        Paragraph paragraph = new Paragraph()
            .add(new Text(text1).setBold()) // Fett formatierter Teil
            .add(new Text(text2));         // Normale Schrift für den Wert
        return new Cell().add(paragraph).setBorder(Border.NO_BORDER);
    }

    private Cell createBoldCell(String text) {
        return new Cell().add(new Paragraph(text)).setBold().setBorder(Border.NO_BORDER);
    }

}
