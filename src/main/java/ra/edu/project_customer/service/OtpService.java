package ra.edu.project_customer.service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ra.edu.project_customer.entity.User;
import ra.edu.project_customer.repository.UserRepository;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final UserRepository userRepo;
    private final JavaMailSender mailSender;

    public void generateAndSendOtp(User user) {
        String otp = String.format("%06d", new Random().nextInt(1000000));
        user.setOtp(otp);
        userRepo.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }
    public void sendOtp(String to, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Your OTP Code");
        msg.setText("Your OTP is: " + otp);
        mailSender.send(msg);
    }

    public boolean verifyOtp(User user, String otp) {
        return otp.equals(user.getOtp());
    }


}
