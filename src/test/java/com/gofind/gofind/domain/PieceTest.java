package com.gofind.gofind.domain;

import static com.gofind.gofind.domain.LocationTestSamples.*;
import static com.gofind.gofind.domain.MaisonTestSamples.*;
import static com.gofind.gofind.domain.PieceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gofind.gofind.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PieceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Piece.class);
        Piece piece1 = getPieceSample1();
        Piece piece2 = new Piece();
        assertThat(piece1).isNotEqualTo(piece2);

        piece2.setId(piece1.getId());
        assertThat(piece1).isEqualTo(piece2);

        piece2 = getPieceSample2();
        assertThat(piece1).isNotEqualTo(piece2);
    }

    @Test
    void maisonTest() throws Exception {
        Piece piece = getPieceRandomSampleGenerator();
        Maison maisonBack = getMaisonRandomSampleGenerator();

        piece.setMaison(maisonBack);
        assertThat(piece.getMaison()).isEqualTo(maisonBack);

        piece.maison(null);
        assertThat(piece.getMaison()).isNull();
    }

    @Test
    void locationTest() throws Exception {
        Piece piece = getPieceRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        piece.setLocation(locationBack);
        assertThat(piece.getLocation()).isEqualTo(locationBack);

        piece.location(null);
        assertThat(piece.getLocation()).isNull();
    }
}
