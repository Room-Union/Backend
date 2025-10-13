package org.codeit.roomunion.common.adapter.out.system;

import org.codeit.roomunion.common.application.port.out.RandomNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomNumberGeneratorImpl implements RandomNumberGenerator {

    @Override
    public int generate(int bound) {
        return ThreadLocalRandom.current()
            .nextInt(bound);
    }

}
