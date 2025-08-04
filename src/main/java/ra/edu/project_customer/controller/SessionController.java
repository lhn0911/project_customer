package ra.edu.project_customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.project_customer.entity.Session;
import ra.edu.project_customer.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSession(@PathVariable("id") String sessionId) {
        sessionService.deleteSessionById(sessionId);
        return ResponseEntity.ok("Xóa session thành công.");
    }

    @PostMapping("/cleanup")
    public ResponseEntity<String> cleanupExpiredSessions() {
        int deletedCount = sessionService.cleanupExpiredSessions();
        return ResponseEntity.ok("Đã xóa " + deletedCount + " session hết hạn.");
    }
}