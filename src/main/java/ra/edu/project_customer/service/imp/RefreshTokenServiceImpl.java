package ra.edu.project_customer.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.project_customer.entity.RefreshToken;
import ra.edu.project_customer.entity.User;
import ra.edu.project_customer.repository.RefreshTokenRepository;
import ra.edu.project_customer.security.jwt.JWTProvider;
import ra.edu.project_customer.service.RefreshTokenService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JWTProvider jwtProvider;

    public RefreshToken createRefreshToken(User user, String ip) {
        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUser(user);
        RefreshToken token;

        if (existingTokenOpt.isPresent()) {
            token = existingTokenOpt.get();
            token.setToken(UUID.randomUUID().toString());
            token.setAddressIp(ip);
            token.setExpiryDate(LocalDateTime.now().plusDays(7));
        } else {
            token = new RefreshToken();
            token.setUser(user);
            token.setToken(UUID.randomUUID().toString());
            token.setAddressIp(ip);
            token.setExpiryDate(LocalDateTime.now().plusDays(7));
        }

        return refreshTokenRepository.save(token);
    }


    public boolean isValid(RefreshToken token, String ip) {
        return token.getExpiryDate().isAfter(LocalDateTime.now()) && token.getAddressIp().equals(ip);
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    public void manageRefreshTokenLimit(User user, String ip) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserOrderByExpiryDateAsc(user);
        if (tokens.size() >= 2) {
            refreshTokenRepository.delete(tokens.get(0));
        }
    }

}