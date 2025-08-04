package ra.edu.project_customer.service.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.edu.project_customer.entity.Session;
import ra.edu.project_customer.repository.SessionRepository;
import ra.edu.project_customer.service.SessionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @Override
    public void deleteSessionById(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    @Override
    @Transactional
    public int cleanupExpiredSessions() {
        List<Session> expiredSessions = sessionRepository.findByExpiryTimeBefore(LocalDateTime.now());
        int count = expiredSessions.size();
        sessionRepository.deleteExpiredSessions(LocalDateTime.now());
        return count;
    }
}