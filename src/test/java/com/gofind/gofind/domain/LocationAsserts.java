package com.gofind.gofind.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gofind.gofind.domain.locations.Location;

public class LocationAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLocationAllPropertiesEquals(Location expected, Location actual) {
        assertLocationAutoGeneratedPropertiesEquals(expected, actual);
        assertLocationAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLocationAllUpdatablePropertiesEquals(Location expected, Location actual) {
        assertLocationUpdatableFieldsEquals(expected, actual);
        assertLocationUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLocationAutoGeneratedPropertiesEquals(Location expected, Location actual) {
        assertThat(expected)
            .as("Verify Location auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLocationUpdatableFieldsEquals(Location expected, Location actual) {
        assertThat(expected)
            .as("Verify Location relevant properties")
            .satisfies(e -> assertThat(e.getPrix()).as("check prix").isEqualTo(actual.getPrix()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLocationUpdatableRelationshipsEquals(Location expected, Location actual) {
        assertThat(expected)
            .as("Verify Location relationships")
            .satisfies(e -> assertThat(e.getMaison()).as("check maison").isEqualTo(actual.getMaison()));
    }
}
