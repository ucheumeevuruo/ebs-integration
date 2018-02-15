/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.services.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author SAP Training
 */
public interface RowMapper {
    public Object mapRow(ResultSet rs, int i) throws SQLException;
}
