package com.gofind.gofind.repository.locations;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class MaisonSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("adresse", table, columnPrefix + "_adresse"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("image", table, columnPrefix + "_image"));

        columns.add(Column.aliased("proprietaire_id", table, columnPrefix + "_proprietaire_id"));
        return columns;
    }
}
