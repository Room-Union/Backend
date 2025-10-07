package org.codeit.roomunion.common.application.port.out;

public interface EmailSender {

    void send(String to, String subject, String body);

}
