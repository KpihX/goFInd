package com.gofind.gofind.domain;

import com.gofind.gofind.domain.itinaries.Trajet;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrajetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Trajet getTrajetSample1() {
        return new Trajet().id(1L).depart("depart1").arrivee("arrivee1").places(1);
    }

    public static Trajet getTrajetSample2() {
        return new Trajet().id(2L).depart("depart2").arrivee("arrivee2").places(2);
    }

    public static Trajet getTrajetRandomSampleGenerator() {
        return new Trajet()
            .id(longCount.incrementAndGet())
            .depart(UUID.randomUUID().toString())
            .arrivee(UUID.randomUUID().toString())
            .places(intCount.incrementAndGet());
    }
}
