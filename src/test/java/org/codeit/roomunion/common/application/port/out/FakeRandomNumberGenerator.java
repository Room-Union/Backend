package org.codeit.roomunion.common.application.port.out;

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