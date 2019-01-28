/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.ebs.services.mapper;

import com.plexadasi.ebs.model.BackOrder;
import com.plexadasi.ebs.services.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OnHandRowMapper
implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int i) throws SQLException {
        BackOrder backOrder = new BackOrder();
        backOrder.setPickMeaning(rs.getString("PICK_MEANING"));
        backOrder.setItemStatus(rs.getString("FLOW_STATUS_CODE"));
        backOrder.setReleaseStatus(rs.getString("RELEASED_STATUS"));
        return backOrder;
    }
}

