package com.gofind.gofind.repository.rowmapper;

import com.gofind.gofind.domain.Piece;
import com.gofind.gofind.domain.enumeration.EtatPiece;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Piece}, with proper type conversions.
 */
@Service
public class PieceRowMapper implements BiFunction<Row, String, Piece> {

    private final ColumnConverter converter;

    public PieceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Piece} stored in the database.
     */
    @Override
    public Piece apply(Row row, String prefix) {
        Piece entity = new Piece();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLibelle(converter.fromRow(row, prefix + "_libelle", String.class));
        entity.setImage(converter.fromRow(row, prefix + "_image", String.class));
        entity.setEtat(converter.fromRow(row, prefix + "_etat", EtatPiece.class));
        entity.setMaisonId(converter.fromRow(row, prefix + "_maison_id", Long.class));
        entity.setLocationId(converter.fromRow(row, prefix + "_location_id", Long.class));
        return entity;
    }
}
