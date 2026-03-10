package com.example.xemphim.Service.impl;

import com.example.xemphim.Entity.PassWordOtp;
import com.example.xemphim.Entity.User;
import com.example.xemphim.Repository.PasswordResetOtpRepository;
import com.example.xemphim.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class Passwordservice {

    private static final Duration OTP_TTL = Duration.ofMinutes(10);
    private static final int MAX_ATTEMPTS = 5;

    private final SecureRandom random = new SecureRandom();

    private final UserRepository userRepository; // bạn đang có user repo
    private final PasswordResetOtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;


    @Transactional
    public void requestOtp(String email) {
        var userOpt = userRepository.findByEmail(email);

        // Luôn "im lặng" nếu email không tồn tại (chống lộ user)
        if (userOpt.isEmpty()) return;

        var user = userOpt.get();

        // (tuỳ chọn) xoá OTP cũ để chỉ còn 1 OTP có hiệu lực
        otpRepository.deleteByUserId(Long.valueOf(user.getId()));

        String otp = generate6DigitOtp();
        String otpHash = passwordEncoder.encode(otp);

        PassWordOtp rec = new PassWordOtp();
        rec.setUserId(Long.valueOf(user.getId()));
        rec.setOtpHash(otpHash);
        rec.setExpiresAt(Instant.now().plus(OTP_TTL));
        rec.setAttempts(0);
        rec.setUsed(false);

        otpRepository.save(rec);

        sendOtpEmail(email, otp);
    }

    @Transactional
    public void resetPassword(String email, String otp, String newPassword) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid request")); // đừng leak “email không tồn tại”

        var rec = otpRepository.findTopByUserIdOrderByCreatedAtDesc(Long.valueOf(user.getId()))
                .orElseThrow(() -> new RuntimeException("Invalid request"));

        if (rec.isUsed() || Instant.now().isAfter(rec.getExpiresAt()) || rec.getAttempts() >= MAX_ATTEMPTS) {
            throw new RuntimeException("OTP expired/invalid");
        }

        boolean ok = passwordEncoder.matches(otp, rec.getOtpHash());
        if (!ok) {
            rec.setAttempts(rec.getAttempts() + 1);
            otpRepository.save(rec);
            throw new RuntimeException("OTP expired/invalid");
        }

        // OTP đúng -> đổi mật khẩu
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // đánh dấu OTP đã dùng (hoặc xoá luôn)
        rec.setUsed(true);
        otpRepository.save(rec);

        // (nếu bạn có refresh token) => revoke tại đây
    }

    private String generate6DigitOtp() {
        int x = 100000 + random.nextInt(900000);
        return String.valueOf(x);
    }

    private void sendOtpEmail(String to, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Mã đặt lại mật khẩu");
        msg.setText("Mã OTP của bạn: " + otp + "\nMã có hiệu lực trong 10 phút.\nNếu không phải bạn, hãy bỏ qua email này.");
        mailSender.send(msg);
    }
    @Transactional
    public void resetemail( String enail,String newemail, String Password) {
        User user = userRepository.findByEmail(enail).orElseThrow(() -> new RuntimeException("Invalid request"));
        boolean ok = passwordEncoder.matches(Password, user.getPassword());
        if (!ok) {
            throw new RuntimeException("Không thành công");
        }
        if (userRepository.existsByEmail(newemail)) {
            throw new RuntimeException("Email mới đã được sử dụng");
        }
        user.setEmail(newemail);

        userRepository.save(user);
        log.info("Thay đổi Gmail Thành công");



        // (nếu bạn có refresh token) => revoke tại đây
    }
    public void ChangePassword(String email, String password, String NewPassword)  {
        
       User a= userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid request"));
        boolean s= passwordEncoder.matches(password, a.getPassword());
        if (!s) {
          throw new RuntimeException("Sai mật Khẩu");
        }
        a.setPassword(passwordEncoder.encode(NewPassword));
        userRepository.save(a);


    }
    

}