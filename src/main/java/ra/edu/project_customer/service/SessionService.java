package ra.edu.project_customer.service;

import ra.edu.project_customer.entity.Session;

import java.util.List;

public interface SessionService {
    List<Session> getAllSessions();

    void deleteSessionById(String sessionId);

    int cleanupExpiredSessions();
}