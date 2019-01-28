/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.ebs.services.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper {
    public Object mapRow(ResultSet var1, int var2) throws SQLException;
}

