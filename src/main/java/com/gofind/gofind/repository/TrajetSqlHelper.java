package com.gofind.gofind.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TrajetSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("depart", table, columnPrefix + "_depart"));
        columns.add(Column.aliased("arrivee", table, columnPrefix + "_arrivee"));
        columns.add(Column.aliased("date_heure_depart", table, columnPrefix + "_date_heure_depart"));
        columns.add(Column.aliased("places", table, columnPrefix + "_places"));
        columns.add(Column.aliased("prix", table, columnPrefix + "_prix"));

        columns.add(Column.aliased("proprietaire_id", table, columnPrefix + "_proprietaire_id"));
        return columns;
    }
}
