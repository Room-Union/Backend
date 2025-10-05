package org.codeit.roomunion.common.application.port.out;

import static org.junit.jupiter.api.Assertions.*;

public class FakeRandomNumberGenerator implements RandomNumberGenerator {

    private final int randomNumber;

    public FakeRandomNumberGenerator(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    @Override
    public int generate(int bound) {
        return randomNumber;
    }
}