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
        

        LocalDateTime beginDateTime = beginDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1).minusNanos(1); // Ende des Tages
    
        // Abrufen der Buchungen aus der Datenbank
        List<Booking> bookingList = this.controller.bookingRepository.findAllByCheckInBetween(beginDateTime, endDateTime);

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

}
