package com.gofind.gofind.repository.rowmapper;

import com.gofind.gofind.domain.locations.Location;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Location}, with proper type conversions.
 */
@Service
public class LocationRowMapper implements BiFunction<Row, String, Location> {

    private final ColumnConverter converter;

    public LocationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Location} stored in the database.
     */
    @Override
    public Location apply(Row row, String prefix) {
        Location entity = new Location();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPrix(converter.fromRow(row, prefix + "_prix", Float.class));
        entity.setDateHeureDebut(converter.fromRow(row, prefix + "_date_heure_debut", ZonedDateTime.class));
        entity.setDateHeureFin(converter.fromRow(row, prefix + "_date_heure_fin", ZonedDateTime.class));
        entity.setMaisonId(converter.fromRow(row, prefix + "_maison_id", Long.class));
        entity.setLocataireId(converter.fromRow(row, prefix + "_locataire_id", Long.class));
        return entity;
    }
}
