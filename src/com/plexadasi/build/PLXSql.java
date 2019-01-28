/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.build;

import com.plexadasi.helper.DataTypeCheck;
import com.plexadasi.build.PLXSqlBinder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PLXSql {
    private final String InsertInto = "INSERT INTO ";
    private static final String BRACKET_OPEN = "(";
    private static final String BRACKET_CLOSE = ")";
    private static final String POSITIONAL_PARAMETER = "?";
    private static final String VALUES = "VALUES";
    private static final String WHITE_SPACE = " ";
    private Map<String, String> params = new HashMap<String, String>();
    private String stringOutput;
    private String positionalParam;
    private String bindParam;
    PLXSqlBinder bind = new PLXSqlBinder();
    private PreparedStatement ps = null;
    private Connection Conn = null;
    private static final String PARAM_EXCEPTION = "Sql Parameter for insert statement cannot be empty.";

    public PLXSql(Connection conn) {
        this.Conn = conn;
    }

    private String createParameter() throws SQLException {
        int num = 0;
        int length = this.params.size();
        if (length <= 0) {
            throw new SQLException("Sql Parameter for insert statement cannot be empty.");
        }
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            String value = entry.getValue();
            this.stringOutput = this.stringOutput + value;
            this.positionalParam = this.positionalParam + "?";
            if (num++ != length - 1) {
                this.positionalParam = this.positionalParam + ", ";
                this.stringOutput = this.stringOutput + this.positionalParam;
            }
            this.bindParam = this.bindParam + this.bind.insertAutobindParam(value);
        }
        return this.stringOutput;
    }

    public void setParams(Map<String, String> param) {
        this.params = param;
    }

    private String sqlQuery(String table) throws SQLException {
        this.stringOutput = "INSERT INTO " + table + "(" + this.createParameter() + ")";
        this.stringOutput = this.stringOutput + " ";
        this.stringOutput = this.stringOutput + "VALUES(" + this.positionalParam + ")";
        return this.stringOutput;
    }

    public void preparedStatement(String table) throws SQLException {
        this.ps = this.Conn.prepareStatement(this.sqlQuery(table));
    }

    public void executeStatement() throws SQLException {
        boolean execute = this.ps.execute();
        if (!execute) {
            throw new SQLException("Failed to execute query");
        }
    }

    public void setPrepare(PreparedStatement ps) throws SQLException {
        int num = 0;
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            num += num;
            String key = entry.getKey();
            if (DataTypeCheck.isDoubleInt(0.0)) {
                ps.setDouble(num, Double.valueOf(key));
                continue;
            }
            if (DataTypeCheck.isStringInt(key)) {
                ps.setInt(num, Integer.valueOf(key));
                continue;
            }
            if (DataTypeCheck.isObjectInteger(key)) {
                ps.setInt(num, Integer.valueOf(key));
                continue;
            }
            if (DataTypeCheck.isString(key).booleanValue()) {
                ps.setString(num, key);
                continue;
            }
            throw new SQLException("Data type is incompatible");
        }
    }
}

