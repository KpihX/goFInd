package com.gofind.gofind.domain;

import static com.gofind.gofind.domain.LocationTestSamples.*;
import static com.gofind.gofind.domain.MaisonTestSamples.*;
import static com.gofind.gofind.domain.PieceTestSamples.*;
import static com.gofind.gofind.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gofind.gofind.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MaisonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Maison.class);
        Maison maison1 = getMaisonSample1();
        Maison maison2 = new Maison();
        assertThat(maison1).isNotEqualTo(maison2);

        maison2.setId(maison1.getId());
        assertThat(maison1).isEqualTo(maison2);

        maison2 = getMaisonSample2();
        assertThat(maison1).isNotEqualTo(maison2);
    }

    @Test
    void piecesTest() throws Exception {
        Maison maison = getMaisonRandomSampleGenerator();
        Piece pieceBack = getPieceRandomSampleGenerator();

        maison.addPieces(pieceBack);
        assertThat(maison.getPieces()).containsOnly(pieceBack);
        assertThat(pieceBack.getMaison()).isEqualTo(maison);

        maison.removePieces(pieceBack);
        assertThat(maison.getPieces()).doesNotContain(pieceBack);
        assertThat(pieceBack.getMaison()).isNull();

        maison.pieces(new HashSet<>(Set.of(pieceBack)));
        assertThat(maison.getPieces()).containsOnly(pieceBack);
        assertThat(pieceBack.getMaison()).isEqualTo(maison);

        maison.setPieces(new HashSet<>());
        assertThat(maison.getPieces()).doesNotContain(pieceBack);
        assertThat(pieceBack.getMaison()).isNull();
    }

    @Test
    void proprietaireTest() throws Exception {
        Maison maison = getMaisonRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        maison.setProprietaire(utilisateurBack);
        assertThat(maison.getProprietaire()).isEqualTo(utilisateurBack);

        maison.proprietaire(null);
        assertThat(maison.getProprietaire()).isNull();
    }

    @Test
    void locationsTest() throws Exception {
        Maison maison = getMaisonRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        maison.addLocations(locationBack);
        assertThat(maison.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getMaison()).isEqualTo(maison);

        maison.removeLocations(locationBack);
        assertThat(maison.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getMaison()).isNull();

        maison.locations(new HashSet<>(Set.of(locationBack)));
        assertThat(maison.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getMaison()).isEqualTo(maison);

        maison.setLocations(new HashSet<>());
        assertThat(maison.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getMaison()).isNull();
    }
}
