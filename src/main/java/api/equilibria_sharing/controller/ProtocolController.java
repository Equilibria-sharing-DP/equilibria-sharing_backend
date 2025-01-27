package api.equilibria_sharing.controller;

import api.equilibria_sharing.model.Booking;

// import com.itextpdf.text.Document;
// import com.itextpdf.text.Paragraph;
// import com.itextpdf.text.pdf.PdfWriter;

import api.equilibria_sharing.model.Protocol;
import api.equilibria_sharing.repositories.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ProtocolController {
    private static final Logger log = LoggerFactory.getLogger(ProtocolController.class);

    public final BookingRepository bookingRepository;
    private final PersonRepository personRepository;
    private final AccommodationRepository accommodationRepository;
    private final AddressRepository addressRepository;
    private final Protocol protocol;

    public ProtocolController(BookingRepository bookingRepository, PersonRepository personRepository, AccommodationRepository accommodationRepository, AddressRepository addressRepository) {
        this.bookingRepository = bookingRepository;
        this.personRepository = personRepository;
        this.accommodationRepository = accommodationRepository;
        this.addressRepository = addressRepository;
        this.protocol = new Protocol(this);
    }

    @GetMapping("/api/v1/protocol")
    public ResponseEntity<byte[]> generateProtocol( @RequestParam String format,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            log.info("Create File");
            HttpHeaders headers = new HttpHeaders();


            //Daten auslesen
            LocalDateTime beginDateTime = beginDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1).minusNanos(1); // Ende des Tages
            List<Booking> bookingList = this.bookingRepository.findAllByCheckInBetween(beginDateTime, endDateTime);

            if(format.equals("pdf")){ 
                protocol.getPDF(bookingList, baos);
                log.info("pdf");
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", "protocol.pdf");
            }
            else if(format.equals("csv")){ 
                byte[] csvBytes = protocol.getCSV(bookingList);  // CSV-Daten werden nun als Byte-Array zurückgegeben
                log.info("csv");
                headers.setContentType(new MediaType("text", "csv"));
                headers.setContentDispositionFormData("attachment", "protocol.csv");
                return ResponseEntity.ok().headers(headers).body(csvBytes);  // CSV als Antwort
            }
            else if (format.equals("xlsx")){ 
                protocol.getExcel(beginDate,endDate); 
                log.info("excel");
                headers.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.setContentDispositionFormData("attachment", "protocol.xlsx");
            }
            else { return ResponseEntity.badRequest().body("Falscher Dateityp".getBytes()); }

            // Document document = new Document();
            // PdfWriter.getInstance(document, baos);

            // document.open();
            // document.add(new Paragraph("Protocol Report"));
            // document.add(new Paragraph("This is a generated PDF file."));
            // document.close();

            // 2. Erstelle die HTTP-Antwort
            // 
            // headers.setContentType(MediaType.APPLICATION_PDF);
            // headers.setContentDispositionFormData("attachment", "protocol.pdf");

            log.info("Downloading File");
            return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
        } catch (Exception e) {
            log.error("Error while generating PDF", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/api/v1/report")
    public ResponseEntity<?> getReport(
            @RequestParam String format,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        // Logik für CSV oder PDF generieren
        System.out.println("Format: " + format);
        System.out.println("Beginn: " + beginDate);
        System.out.println("Ende: " + endDate);

        
        
        return ResponseEntity.ok("Daten empfangen");
    }
    public void logging(String a){
        log.info(a);
    }

}

