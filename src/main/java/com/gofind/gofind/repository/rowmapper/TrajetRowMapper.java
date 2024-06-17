package com.gofind.gofind.repository.rowmapper;

import com.gofind.gofind.domain.itinaries.Trajet;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Trajet}, with proper type conversions.
 */
@Service
public class TrajetRowMapper implements BiFunction<Row, String, Trajet> {

    private final ColumnConverter converter;

    public TrajetRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Trajet} stored in the database.
     */
    @Override
    public Trajet apply(Row row, String prefix) {
        Trajet entity = new Trajet();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDepart(converter.fromRow(row, prefix + "_depart", String.class));
        entity.setArrivee(converter.fromRow(row, prefix + "_arrivee", String.class));
        entity.setDateHeureDepart(converter.fromRow(row, prefix + "_date_heure_depart", ZonedDateTime.class));
        entity.setPlaces(converter.fromRow(row, prefix + "_places", Integer.class));
        entity.setPrix(converter.fromRow(row, prefix + "_prix", Float.class));
        entity.setProprietaireId(converter.fromRow(row, prefix + "_proprietaire_id", Long.class));
        return entity;
    }
}
