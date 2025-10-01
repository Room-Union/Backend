package org.codeit.roomunion.auth.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @PostMapping("/email/send")
    public ResponseEntity<Void> sendEmail() {


        return ResponseEntity.noContent()
            .build();
    }

}
