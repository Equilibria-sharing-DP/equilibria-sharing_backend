package api.equilibria_sharing.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import api.equilibria_sharing.repositories.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
public class ProtocolController {
    private static final Logger log = LoggerFactory.getLogger(ProtocolController.class);

    private final BookingRepository bookingRepository;
    private final PersonRepository personRepository;
    private final AccommodationRepository accommodationRepository;
    private final AddressRepository addressRepository;

    public ProtocolController(BookingRepository bookingRepository, PersonRepository personRepository, AccommodationRepository accommodationRepository, AddressRepository addressRepository) {
        this.bookingRepository = bookingRepository;
        this.personRepository = personRepository;
        this.accommodationRepository = accommodationRepository;
        this.addressRepository = addressRepository;
    }

    @GetMapping("/api/v1/protocol")
    public ResponseEntity<byte[]> generateProtocol() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 1. Erstelle ein Dokument
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph("Protocol Report"));
            document.add(new Paragraph("This is a generated PDF file."));
            document.close();

            // 2. Erstelle die HTTP-Antwort
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "protocol.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(baos.toByteArray());
        } catch (Exception e) {
            log.error("Error while generating PDF", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
