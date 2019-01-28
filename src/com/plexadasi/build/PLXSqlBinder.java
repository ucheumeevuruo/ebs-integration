/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.build;

import com.plexadasi.helper.DataTypeCheck;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class PLXSqlBinder {
    String stringOutput = new String();
    String params = new String();
    public static final String STRING = "s";
    public static final String INTEGER = "i";
    private static final String BIND_EXCEPTION = "Data type not supported.";

    public void setInsertParamArray(Map<String, String> param) throws SQLException {
        for (Map.Entry<String, String> entry : param.entrySet()) {
            this.params = this.params + this.bindParam(entry.getValue());
        }
    }

    public String insertAutobindParam(String param) throws SQLException {
        return this.bindParam(param);
    }

    private String bindParam(String bind) throws SQLException {
        if (DataTypeCheck.isString(bind).booleanValue()) {
            this.stringOutput = "s";
        } else if (DataTypeCheck.isObjectInteger(bind) || DataTypeCheck.isStringInt(bind)) {
            this.stringOutput = "i";
        } else {
            throw new SQLException("Data type not supported.");
        }
        return this.stringOutput;
    }

    public String getInsertParam() {
        return this.params;
    }
}

