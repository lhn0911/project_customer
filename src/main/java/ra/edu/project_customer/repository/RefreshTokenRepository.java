package ra.edu.project_customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.project_customer.entity.RefreshToken;
import ra.edu.project_customer.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);

    List<RefreshToken> findAllByUserOrderByExpiryDateAsc(User user);

    Optional<RefreshToken> findByUser(User user);
}