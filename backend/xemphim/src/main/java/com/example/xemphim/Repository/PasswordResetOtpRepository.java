package com.example.xemphim.Repository;

import com.example.xemphim.Entity.PassWordOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PasswordResetOtpRepository extends JpaRepository<PassWordOtp, Long> {
    Optional<PassWordOtp> findTopByUserIdOrderByCreatedAtDesc(Long userId);
    void deleteByUserId(Long userId);
}