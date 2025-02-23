package api.equilibria_sharing.model.requests;

import java.time.LocalDateTime;
import java.util.List;

/**
 * BookingRequest class - Just a placeholder class for BookingRequests
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
public class BookingRequest {
    private Long accommodationId;
    private MainTravelerRequest mainTraveler;
    private List<GuestRequest> additionalGuests;
    private LocalDateTime checkIn;
    private LocalDateTime expectedCheckOut;

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public MainTravelerRequest getMainTraveler() {
        return mainTraveler;
    }

    public void setMainTraveler(MainTravelerRequest mainTraveler) {
        this.mainTraveler = mainTraveler;
    }

    public List<GuestRequest> getAdditionalGuests() {
        return additionalGuests;
    }

    public void setAdditionalGuests(List<GuestRequest> additionalGuests) {
        this.additionalGuests = additionalGuests;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getExpectedCheckOut() {
        return expectedCheckOut;
    }

    public void setExpectedCheckOut(LocalDateTime expectedCheckOut) {
        this.expectedCheckOut = expectedCheckOut;
    }
}

