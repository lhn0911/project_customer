package ra.edu.project_customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class PendingUser {
    @Id
    private String email;

    private String username;
    private String fullName;
    private String phoneNumber;
    private String passwordHash;
    private String roles;
    private String otp;
    private LocalDateTime createdAt;
}
