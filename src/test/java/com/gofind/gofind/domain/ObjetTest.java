package com.gofind.gofind.domain;

import static com.gofind.gofind.domain.ObjetTestSamples.*;
import static com.gofind.gofind.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gofind.gofind.domain.objects.Objet;
import com.gofind.gofind.domain.users.Utilisateur;
import com.gofind.gofind.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObjetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Objet.class);
        Objet objet1 = getObjetSample1();
        Objet objet2 = new Objet();
        assertThat(objet1).isNotEqualTo(objet2);

        objet2.setId(objet1.getId());
        assertThat(objet1).isEqualTo(objet2);

        objet2 = getObjetSample2();
        assertThat(objet1).isNotEqualTo(objet2);
    }

    @Test
    void proprietaireTest() throws Exception {
        Objet objet = getObjetRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        objet.setProprietaire(utilisateurBack);
        assertThat(objet.getProprietaire()).isEqualTo(utilisateurBack);

        objet.proprietaire(null);
        assertThat(objet.getProprietaire()).isNull();
    }

    @Test
    void signalantTest() throws Exception {
        Objet objet = getObjetRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        objet.setSignalant(utilisateurBack);
        assertThat(objet.getSignalant()).isEqualTo(utilisateurBack);

        objet.signalant(null);
        assertThat(objet.getSignalant()).isNull();
    }
}
