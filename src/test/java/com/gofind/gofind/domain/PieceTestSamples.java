package com.gofind.gofind.domain;

import com.gofind.gofind.domain.locations.Piece;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PieceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Piece getPieceSample1() {
        return new Piece().id(1L).libelle("libelle1").image("image1");
    }

    public static Piece getPieceSample2() {
        return new Piece().id(2L).libelle("libelle2").image("image2");
    }

    public static Piece getPieceRandomSampleGenerator() {
        return new Piece().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString()).image(UUID.randomUUID().toString());
    }
}
