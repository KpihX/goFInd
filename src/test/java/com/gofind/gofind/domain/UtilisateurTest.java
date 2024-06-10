package com.gofind.gofind.domain;

import static com.gofind.gofind.domain.MaisonTestSamples.*;
import static com.gofind.gofind.domain.ObjetTestSamples.*;
import static com.gofind.gofind.domain.TrajetTestSamples.*;
import static com.gofind.gofind.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gofind.gofind.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilisateur.class);
        Utilisateur utilisateur1 = getUtilisateurSample1();
        Utilisateur utilisateur2 = new Utilisateur();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);

        utilisateur2.setId(utilisateur1.getId());
        assertThat(utilisateur1).isEqualTo(utilisateur2);

        utilisateur2 = getUtilisateurSample2();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);
    }

    @Test
    void objectsTest() throws Exception {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Objet objetBack = getObjetRandomSampleGenerator();

        utilisateur.addObjects(objetBack);
        assertThat(utilisateur.getObjects()).containsOnly(objetBack);
        assertThat(objetBack.getProprietaire()).isEqualTo(utilisateur);

        utilisateur.removeObjects(objetBack);
        assertThat(utilisateur.getObjects()).doesNotContain(objetBack);
        assertThat(objetBack.getProprietaire()).isNull();

        utilisateur.objects(new HashSet<>(Set.of(objetBack)));
        assertThat(utilisateur.getObjects()).containsOnly(objetBack);
        assertThat(objetBack.getProprietaire()).isEqualTo(utilisateur);

        utilisateur.setObjects(new HashSet<>());
        assertThat(utilisateur.getObjects()).doesNotContain(objetBack);
        assertThat(objetBack.getProprietaire()).isNull();
    }

    @Test
    void objectsSignalesTest() throws Exception {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Objet objetBack = getObjetRandomSampleGenerator();

        utilisateur.addObjectsSignales(objetBack);
        assertThat(utilisateur.getObjectsSignales()).containsOnly(objetBack);
        assertThat(objetBack.getSignalant()).isEqualTo(utilisateur);

        utilisateur.removeObjectsSignales(objetBack);
        assertThat(utilisateur.getObjectsSignales()).doesNotContain(objetBack);
        assertThat(objetBack.getSignalant()).isNull();

        utilisateur.objectsSignales(new HashSet<>(Set.of(objetBack)));
        assertThat(utilisateur.getObjectsSignales()).containsOnly(objetBack);
        assertThat(objetBack.getSignalant()).isEqualTo(utilisateur);

        utilisateur.setObjectsSignales(new HashSet<>());
        assertThat(utilisateur.getObjectsSignales()).doesNotContain(objetBack);
        assertThat(objetBack.getSignalant()).isNull();
    }

    @Test
    void trajetsPropTest() throws Exception {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Trajet trajetBack = getTrajetRandomSampleGenerator();

        utilisateur.addTrajetsProp(trajetBack);
        assertThat(utilisateur.getTrajetsProps()).containsOnly(trajetBack);
        assertThat(trajetBack.getProprietaire()).isEqualTo(utilisateur);

        utilisateur.removeTrajetsProp(trajetBack);
        assertThat(utilisateur.getTrajetsProps()).doesNotContain(trajetBack);
        assertThat(trajetBack.getProprietaire()).isNull();

        utilisateur.trajetsProps(new HashSet<>(Set.of(trajetBack)));
        assertThat(utilisateur.getTrajetsProps()).containsOnly(trajetBack);
        assertThat(trajetBack.getProprietaire()).isEqualTo(utilisateur);

        utilisateur.setTrajetsProps(new HashSet<>());
        assertThat(utilisateur.getTrajetsProps()).doesNotContain(trajetBack);
        assertThat(trajetBack.getProprietaire()).isNull();
    }

    @Test
    void maisonsTest() throws Exception {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Maison maisonBack = getMaisonRandomSampleGenerator();

        utilisateur.addMaisons(maisonBack);
        assertThat(utilisateur.getMaisons()).containsOnly(maisonBack);
        assertThat(maisonBack.getProprietaire()).isEqualTo(utilisateur);

        utilisateur.removeMaisons(maisonBack);
        assertThat(utilisateur.getMaisons()).doesNotContain(maisonBack);
        assertThat(maisonBack.getProprietaire()).isNull();

        utilisateur.maisons(new HashSet<>(Set.of(maisonBack)));
        assertThat(utilisateur.getMaisons()).containsOnly(maisonBack);
        assertThat(maisonBack.getProprietaire()).isEqualTo(utilisateur);

        utilisateur.setMaisons(new HashSet<>());
        assertThat(utilisateur.getMaisons()).doesNotContain(maisonBack);
        assertThat(maisonBack.getProprietaire()).isNull();
    }

    @Test
    void trajetsTest() throws Exception {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Trajet trajetBack = getTrajetRandomSampleGenerator();

        utilisateur.addTrajets(trajetBack);
        assertThat(utilisateur.getTrajets()).containsOnly(trajetBack);

        utilisateur.removeTrajets(trajetBack);
        assertThat(utilisateur.getTrajets()).doesNotContain(trajetBack);

        utilisateur.trajets(new HashSet<>(Set.of(trajetBack)));
        assertThat(utilisateur.getTrajets()).containsOnly(trajetBack);

        utilisateur.setTrajets(new HashSet<>());
        assertThat(utilisateur.getTrajets()).doesNotContain(trajetBack);
    }
}
