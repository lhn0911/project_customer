package ra.edu.project_customer.temp;

import org.springframework.stereotype.Component;
import ra.edu.project_customer.entity.User;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TemporaryUserStorage {

    private static final long EXPIRATION_MINUTES = 15;

    private final Map<String, TempUserWrapper> tempUserMap = new ConcurrentHashMap<>();

    public void saveTempUser(User user) {
        tempUserMap.put(user.getUsername(), new TempUserWrapper(user));
    }

    public User getTempUser(String username) {
        TempUserWrapper wrapper = tempUserMap.get(username);
        if (wrapper == null) return null;

        // Check hết hạn
        if (wrapper.isExpired()) {
            tempUserMap.remove(username);
            return null;
        }

        return wrapper.getUser();
    }

    public void removeTempUser(String username) {
        tempUserMap.remove(username);
    }

    public boolean contains(String username) {
        return getTempUser(username) != null;
    }

    private static class TempUserWrapper {
        private final User user;
        private final LocalDateTime createdAt;

        public TempUserWrapper(User user) {
            this.user = user;
            this.createdAt = LocalDateTime.now();
        }

        public User getUser() {
            return user;
        }

        public boolean isExpired() {
            return createdAt.plusMinutes(EXPIRATION_MINUTES).isBefore(LocalDateTime.now());
        }
    }
}
