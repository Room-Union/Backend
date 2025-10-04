package org.codeit.roomunion.common.adapter.out.email;

import lombok.extern.slf4j.Slf4j;
import org.codeit.roomunion.common.application.port.out.EmailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailSenderImpl implements EmailSender {

    @Override
    public void send(String to, String subject, String body) {
        log.info("Sending email to: [{}], subject: [{}], body: [{}]", to, subject, body);
    }
}
