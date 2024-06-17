package com.gofind.gofind.domain;

import static com.gofind.gofind.domain.TrajetTestSamples.*;
import static com.gofind.gofind.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gofind.gofind.domain.itinaries.Trajet;
import com.gofind.gofind.domain.users.Utilisateur;
import com.gofind.gofind.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TrajetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trajet.class);
        Trajet trajet1 = getTrajetSample1();
        Trajet trajet2 = new Trajet();
        assertThat(trajet1).isNotEqualTo(trajet2);

        trajet2.setId(trajet1.getId());
        assertThat(trajet1).isEqualTo(trajet2);

        trajet2 = getTrajetSample2();
        assertThat(trajet1).isNotEqualTo(trajet2);
    }

    @Test
    void proprietaireTest() throws Exception {
        Trajet trajet = getTrajetRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        trajet.setProprietaire(utilisateurBack);
        assertThat(trajet.getProprietaire()).isEqualTo(utilisateurBack);

        trajet.proprietaire(null);
        assertThat(trajet.getProprietaire()).isNull();
    }

    @Test
    void engagesTest() throws Exception {
        Trajet trajet = getTrajetRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        trajet.addEngages(utilisateurBack);
        assertThat(trajet.getEngages()).containsOnly(utilisateurBack);
        assertThat(utilisateurBack.getTrajets()).containsOnly(trajet);

        trajet.removeEngages(utilisateurBack);
        assertThat(trajet.getEngages()).doesNotContain(utilisateurBack);
        assertThat(utilisateurBack.getTrajets()).doesNotContain(trajet);

        trajet.engages(new HashSet<>(Set.of(utilisateurBack)));
        assertThat(trajet.getEngages()).containsOnly(utilisateurBack);
        assertThat(utilisateurBack.getTrajets()).containsOnly(trajet);

        trajet.setEngages(new HashSet<>());
        assertThat(trajet.getEngages()).doesNotContain(utilisateurBack);
        assertThat(utilisateurBack.getTrajets()).doesNotContain(trajet);
    }
}
