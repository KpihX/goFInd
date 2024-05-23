package com.gofind.gofind.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UtilisateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Utilisateur getUtilisateurSample1() {
        return new Utilisateur().id(1L).telephone("telephone1");
    }

    public static Utilisateur getUtilisateurSample2() {
        return new Utilisateur().id(2L).telephone("telephone2");
    }

    public static Utilisateur getUtilisateurRandomSampleGenerator() {
        return new Utilisateur().id(longCount.incrementAndGet()).telephone(UUID.randomUUID().toString());
    }
}
