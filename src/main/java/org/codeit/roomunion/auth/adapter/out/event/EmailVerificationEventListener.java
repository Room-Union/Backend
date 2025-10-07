package org.codeit.roomunion.auth.adapter.out.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codeit.roomunion.auth.domain.event.EmailVerificationCodeEvent;
import org.codeit.roomunion.common.application.port.out.EmailSender;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailVerificationEventListener {

    private final EmailSender emailSender;

    @Async
    @EventListener
    public void handleEmailVerificationCode(EmailVerificationCodeEvent event) {
        emailSender.send(event.email(), event.subject(), event.body());
    }

}