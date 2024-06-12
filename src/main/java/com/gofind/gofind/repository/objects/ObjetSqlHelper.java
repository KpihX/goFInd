package com.gofind.gofind.repository.objects;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ObjetSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("libelle", table, columnPrefix + "_libelle"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("image", table, columnPrefix + "_image"));
        columns.add(Column.aliased("identifiant", table, columnPrefix + "_identifiant"));
        columns.add(Column.aliased("etat", table, columnPrefix + "_etat"));

        columns.add(Column.aliased("proprietaire_id", table, columnPrefix + "_proprietaire_id"));
        columns.add(Column.aliased("signalant_id", table, columnPrefix + "_signalant_id"));
        return columns;
    }
}
