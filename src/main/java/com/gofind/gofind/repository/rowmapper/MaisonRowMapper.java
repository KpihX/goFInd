package com.gofind.gofind.repository.rowmapper;

import com.gofind.gofind.domain.Maison;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Maison}, with proper type conversions.
 */
@Service
public class MaisonRowMapper implements BiFunction<Row, String, Maison> {

    private final ColumnConverter converter;

    public MaisonRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Maison} stored in the database.
     */
    @Override
    public Maison apply(Row row, String prefix) {
        Maison entity = new Maison();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAdresse(converter.fromRow(row, prefix + "_adresse", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setImage(converter.fromRow(row, prefix + "_image", String.class));
        entity.setProprietaireId(converter.fromRow(row, prefix + "_proprietaire_id", Long.class));
        return entity;
    }
}
