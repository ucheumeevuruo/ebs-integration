/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.services.mapper;

import com.plexadasi.ebs.model.BackOrder;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author SAP Training
 */
public class OnHandRowMapper implements RowMapper  {

    @Override
    public Object mapRow(ResultSet rs, int i) throws SQLException {
        BackOrder backOrder = new BackOrder();
        backOrder.setPickMeaning(rs.getString("PICK_MEANING"));
        backOrder.setItemStatus(rs.getString("FLOW_STATUS_CODE"));
        backOrder.setReleaseStatus(rs.getString("RELEASED_STATUS"));
        //backOrder.setQuantity(rs.getInt("QUANTITY"));
        return backOrder;
    }
}
