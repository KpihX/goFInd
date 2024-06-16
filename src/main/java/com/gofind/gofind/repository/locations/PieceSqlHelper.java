package com.gofind.gofind.repository.locations;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PieceSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("libelle", table, columnPrefix + "_libelle"));
        columns.add(Column.aliased("image", table, columnPrefix + "_image"));
        columns.add(Column.aliased("etat", table, columnPrefix + "_etat"));
        columns.add(Column.aliased("prix", table, columnPrefix + "_prix"));

        columns.add(Column.aliased("maison_id", table, columnPrefix + "_maison_id"));
        columns.add(Column.aliased("location_id", table, columnPrefix + "_location_id"));
        return columns;
    }
}
