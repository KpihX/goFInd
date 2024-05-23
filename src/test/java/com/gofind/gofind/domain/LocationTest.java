package com.gofind.gofind.domain;

import static com.gofind.gofind.domain.LocationTestSamples.*;
import static com.gofind.gofind.domain.MaisonTestSamples.*;
import static com.gofind.gofind.domain.PieceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gofind.gofind.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = getLocationSample1();
        Location location2 = new Location();
        assertThat(location1).isNotEqualTo(location2);

        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);

        location2 = getLocationSample2();
        assertThat(location1).isNotEqualTo(location2);
    }

    @Test
    void piecesTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        Piece pieceBack = getPieceRandomSampleGenerator();

        location.addPieces(pieceBack);
        assertThat(location.getPieces()).containsOnly(pieceBack);
        assertThat(pieceBack.getLocation()).isEqualTo(location);

        location.removePieces(pieceBack);
        assertThat(location.getPieces()).doesNotContain(pieceBack);
        assertThat(pieceBack.getLocation()).isNull();

        location.pieces(new HashSet<>(Set.of(pieceBack)));
        assertThat(location.getPieces()).containsOnly(pieceBack);
        assertThat(pieceBack.getLocation()).isEqualTo(location);

        location.setPieces(new HashSet<>());
        assertThat(location.getPieces()).doesNotContain(pieceBack);
        assertThat(pieceBack.getLocation()).isNull();
    }

    @Test
    void maisonTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        Maison maisonBack = getMaisonRandomSampleGenerator();

        location.setMaison(maisonBack);
        assertThat(location.getMaison()).isEqualTo(maisonBack);

        location.maison(null);
        assertThat(location.getMaison()).isNull();
    }
}
