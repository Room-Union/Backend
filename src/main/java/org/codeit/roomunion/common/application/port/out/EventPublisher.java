package org.codeit.roomunion.common.application.port.out;

public interface EventPublisher {

    void publish(Object event);

}