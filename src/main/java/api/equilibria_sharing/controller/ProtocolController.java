package api.equilibria_sharing.controller;

import api.equilibria_sharing.exceptions.BadRequestException;
import api.equilibria_sharing.exceptions.ProtocolGenerationException;
import api.equilibria_sharing.model.Accommodation;
import api.equilibria_sharing.model.Booking;

import api.equilibria_sharing.model.Protocol;
import api.equilibria_sharing.repositories.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Protocol Controller - Controller for exporting bookings as PDF/CSV/Excel
 *
 * @author Sebastian Sailer
 * @version 02.03.2025
 */
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/v1/protocol")
public class ProtocolController {
    private static final Logger log = LoggerFactory.getLogger(ProtocolController.class);

    public final BookingRepository bookingRepository;
    public final AccommodationRepository accommodationRepository;
    private final Protocol protocol;

    public ProtocolController(BookingRepository bookingRepository, AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
        this.bookingRepository = bookingRepository;
        this.protocol = new Protocol(this);
    }

    @GetMapping()
    public ResponseEntity<byte[]> generateProtocol(
            @RequestParam(required = true) String format,
            @RequestParam(required = true) String accommodationID,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            log.info("Create File");
            HttpHeaders headers = new HttpHeaders();

            // Parameter validieren und trimmen
            if (format == null || format.isBlank()) {
                throw new BadRequestException("Format cannot be empty.");
            }
            format = format.trim().toLowerCase(); // Kleinbuchstaben für bessere Vergleichbarkeit

            if (accommodationID == null || accommodationID.isBlank()) {
                throw new BadRequestException("AccommodationID cannot be empty.");
            }
            accommodationID = accommodationID.trim();

            // Daten auslesen
            LocalDateTime beginDateTime = beginDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1).minusNanos(1);
            List<Booking> bookingList;

            log.info("Creating file with following data: {} - {}", beginDateTime, endDateTime);

            if (accommodationID.equalsIgnoreCase("all")) {
                bookingList = this.bookingRepository.findAllByCheckInBetween(beginDateTime, endDateTime);
            } else {
                try {
                    Long accommodationIdLong = Long.parseLong(accommodationID);
                    Accommodation a = accommodationRepository.findById(accommodationIdLong)
                            .orElseThrow(() -> new IllegalArgumentException("Accommodation not found"));
                    bookingList = this.bookingRepository.findByAccommodationAndCheckInBetween(a, beginDateTime, endDateTime);
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Invalid accommodation ID: " + accommodationID);
                }
            }

            switch (format) {
                case "pdf" -> {
                    protocol.getPDF(bookingList, baos);
                    log.info("Generated PDF");
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", "Buchungsprotokoll-" + beginDateTime + ";" + endDateTime + ".pdf");
                }
                case "csv" -> {
                    byte[] csvBytes = protocol.getCSV(bookingList);
                    log.info("Generated CSV");
                    headers.setContentType(new MediaType("text", "csv"));
                    headers.setContentDispositionFormData("attachment", "Buchungsprotokoll-" + beginDateTime + ";" + endDateTime + ".csv");
                    return ResponseEntity.ok().headers(headers).body(csvBytes);
                }
                case "xlsx" -> {
                    byte[] xlsxBytes = protocol.getExcel(bookingList);
                    log.info("Generated Excel");
                    headers.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                    headers.setContentDispositionFormData("attachment", "Buchungsprotokoll-" + beginDateTime + ";" + endDateTime + ".xlsx");
                    return ResponseEntity.ok().headers(headers).body(xlsxBytes);
                }
                default -> throw new BadRequestException("Invalid format: " + format + ". Allowed: pdf, csv, xlsx.");
            }

            log.info("Downloading File");
            return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
        } catch (Exception e) {
            log.error("Error while generating protocol", e);
            throw new ProtocolGenerationException("Error while generating protocol");
        }
    }

}

