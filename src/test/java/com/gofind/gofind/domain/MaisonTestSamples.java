package com.gofind.gofind.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MaisonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Maison getMaisonSample1() {
        return new Maison().id(1L).adresse("adresse1").description("description1").image("image1");
    }

    public static Maison getMaisonSample2() {
        return new Maison().id(2L).adresse("adresse2").description("description2").image("image2");
    }

    public static Maison getMaisonRandomSampleGenerator() {
        return new Maison()
            .id(longCount.incrementAndGet())
            .adresse(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .image(UUID.randomUUID().toString());
    }
}
