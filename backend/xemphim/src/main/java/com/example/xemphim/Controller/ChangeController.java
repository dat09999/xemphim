package com.example.xemphim.Controller;

import com.example.xemphim.DTO.Auth.ChangeGmai;
import com.example.xemphim.Service.impl.Passwordservice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class ChangeController {
    private final Passwordservice service;
    @PutMapping("/reset/gmail")
    public ResponseEntity<?> resetGmai(@RequestBody ChangeGmai chane, Authentication auth) {
        var email = auth.getName();
        service.resetemail(email, chane.newemail(), chane.password());
        log.info("Reset gmail request received");

        return ResponseEntity.ok().body(new ApiMessage("Đổi Gmail thành công."));


    }
     public record ApiMessage(String message) {}
}
