package com.gofind.gofind.repository.locations;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LocationSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("prix", table, columnPrefix + "_prix"));
        columns.add(Column.aliased("date_heure_debut", table, columnPrefix + "_date_heure_debut"));
        columns.add(Column.aliased("date_heure_fin", table, columnPrefix + "_date_heure_fin"));

        columns.add(Column.aliased("maison_id", table, columnPrefix + "_maison_id"));
        columns.add(Column.aliased("locataire_id", table, columnPrefix + "_locataire_id"));
        return columns;
    }
}
