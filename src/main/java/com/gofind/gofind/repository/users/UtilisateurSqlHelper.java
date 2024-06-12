package com.gofind.gofind.repository.users;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UtilisateurSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("telephone", table, columnPrefix + "_telephone"));

        columns.add(Column.aliased("login_id", table, columnPrefix + "_login_id"));
        return columns;
    }
}
