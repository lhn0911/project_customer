package ra.edu.project_customer.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "sessions")
public class Session {
    @Id
    @Column(length = 255)
    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime loginTime = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime lastActivityTime = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    private String ipAddress;
    private String userAgent;
}
