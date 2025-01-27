package api.equilibria_sharing.repositories;

import api.equilibria_sharing.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findAllByCheckInBetween(LocalDateTime beginDate, LocalDateTime endDate);
}
