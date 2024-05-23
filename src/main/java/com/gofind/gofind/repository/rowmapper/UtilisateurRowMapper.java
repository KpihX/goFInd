package com.gofind.gofind.repository.rowmapper;

import com.gofind.gofind.domain.Utilisateur;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Utilisateur}, with proper type conversions.
 */
@Service
public class UtilisateurRowMapper implements BiFunction<Row, String, Utilisateur> {

    private final ColumnConverter converter;

    public UtilisateurRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Utilisateur} stored in the database.
     */
    @Override
    public Utilisateur apply(Row row, String prefix) {
        Utilisateur entity = new Utilisateur();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTelephone(converter.fromRow(row, prefix + "_telephone", String.class));
        entity.setLoginId(converter.fromRow(row, prefix + "_login_id", Long.class));
        return entity;
    }
}
