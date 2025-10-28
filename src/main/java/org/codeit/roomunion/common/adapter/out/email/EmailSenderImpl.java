package org.codeit.roomunion.common.adapter.out.email;

import lombok.extern.slf4j.Slf4j;
import org.codeit.roomunion.common.application.port.out.EmailSender;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender mailSender;

    public EmailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String to, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        try {
            mailSender.send(simpleMailMessage);
        } catch (MailException e) {
            log.error(e.getMessage(), e);
        }
    }
}
