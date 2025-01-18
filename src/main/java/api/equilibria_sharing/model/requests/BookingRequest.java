package api.equilibria_sharing.model.requests;

import java.time.LocalDate;
import java.util.List;

public class BookingRequest {
    private Long accommodationId;
    private MainTravelerRequest mainTraveler;
    private List<GuestRequest> additionalGuests;
    private LocalDate checkIn;
    private LocalDate expectedCheckOut;

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

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getExpectedCheckOut() {
        return expectedCheckOut;
    }

    public void setExpectedCheckOut(LocalDate expectedCheckOut) {
        this.expectedCheckOut = expectedCheckOut;
    }
}

