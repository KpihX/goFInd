package com.gofind.gofind.domain;

import com.gofind.gofind.domain.objects.Objet;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ObjetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Objet getObjetSample1() {
        return new Objet().id(1L).libelle("libelle1").description("description1").image("image1").identifiant("identifiant1");
    }

    public static Objet getObjetSample2() {
        return new Objet().id(2L).libelle("libelle2").description("description2").image("image2").identifiant("identifiant2");
    }

    public static Objet getObjetRandomSampleGenerator() {
        return new Objet()
            .id(longCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .image(UUID.randomUUID().toString())
            .identifiant(UUID.randomUUID().toString());
    }
}
