package ra.edu.project_customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ra.edu.project_customer.entity.Session;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, String> {

    List<Session> findByExpiryTimeBefore(LocalDateTime now);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.expiryTime < :now")
    void deleteExpiredSessions(LocalDateTime now);
}
