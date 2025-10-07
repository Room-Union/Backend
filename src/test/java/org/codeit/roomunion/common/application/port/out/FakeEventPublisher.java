package org.codeit.roomunion.common.application.port.out;

import org.codeit.roomunion.auth.domain.event.EmailVerificationCodeEvent;

public class FakeEventPublisher implements EventPublisher {

    @Override
    public void publish(Object event) {
        if (event instanceof EmailVerificationCodeEvent emailVerificationCodeEvent) {
            System.out.printf("Published EmailVerificationCodeEvent / email [%s], code [%s]", emailVerificationCodeEvent.email(), emailVerificationCodeEvent.code());
        }
    }
}