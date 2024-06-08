package com.gofind.gofind.repository.rowmapper;

import com.gofind.gofind.domain.Objet;
import com.gofind.gofind.domain.enumeration.EtatObjet;
import com.gofind.gofind.domain.enumeration.TypeObjet;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Objet}, with proper type conversions.
 */
@Service
public class ObjetRowMapper implements BiFunction<Row, String, Objet> {

    private final ColumnConverter converter;

    public ObjetRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Objet} stored in the database.
     */
    @Override
    public Objet apply(Row row, String prefix) {
        Objet entity = new Objet();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLibelle(converter.fromRow(row, prefix + "_libelle", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", TypeObjet.class));
        entity.setImage(converter.fromRow(row, prefix + "_image", String.class));
        entity.setIdentifiant(converter.fromRow(row, prefix + "_identifiant", String.class));
        entity.setEtat(converter.fromRow(row, prefix + "_etat", EtatObjet.class));
        entity.setProprietaireId(converter.fromRow(row, prefix + "_proprietaire_id", Long.class));
        entity.setSignalantId(converter.fromRow(row, prefix + "_signalant_id", Long.class));
        return entity;
    }
}
