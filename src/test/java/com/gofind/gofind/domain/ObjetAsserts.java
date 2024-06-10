package com.gofind.gofind.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjetAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertObjetAllPropertiesEquals(Objet expected, Objet actual) {
        assertObjetAutoGeneratedPropertiesEquals(expected, actual);
        assertObjetAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertObjetAllUpdatablePropertiesEquals(Objet expected, Objet actual) {
        assertObjetUpdatableFieldsEquals(expected, actual);
        assertObjetUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertObjetAutoGeneratedPropertiesEquals(Objet expected, Objet actual) {
        assertThat(expected)
            .as("Verify Objet auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertObjetUpdatableFieldsEquals(Objet expected, Objet actual) {
        assertThat(expected)
            .as("Verify Objet relevant properties")
            .satisfies(e -> assertThat(e.getLibelle()).as("check libelle").isEqualTo(actual.getLibelle()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()))
            .satisfies(e -> assertThat(e.getImage()).as("check image").isEqualTo(actual.getImage()))
            .satisfies(e -> assertThat(e.getIdentifiant()).as("check identifiant").isEqualTo(actual.getIdentifiant()))
            .satisfies(e -> assertThat(e.getEtat()).as("check etat").isEqualTo(actual.getEtat()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertObjetUpdatableRelationshipsEquals(Objet expected, Objet actual) {
        assertThat(expected)
            .as("Verify Objet relationships")
            .satisfies(e -> assertThat(e.getProprietaire()).as("check proprietaire").isEqualTo(actual.getProprietaire()))
            .satisfies(e -> assertThat(e.getSignalant()).as("check signalant").isEqualTo(actual.getSignalant()));
    }
}
