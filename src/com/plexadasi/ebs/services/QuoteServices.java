/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  org.apache.commons.dbutils.QueryRunner
 */
package com.plexadasi.ebs.services;

import com.plexadasi.ebs.services.sql.build.SQLBuilder;
import com.plexadasi.invoice.InvoiceObject;
import com.siebel.data.SiebelDataBean;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbutils.QueryRunner;

public class QuoteServices {
    private final QueryRunner jdbcTemplateObject;
    private final SQLBuilder sqlBuilder = new SQLBuilder();
    private String query;
    private final Connection connection;
    private final SiebelDataBean siebelDataBean;

    public QuoteServices(SiebelDataBean siebelDataBean, Connection connection) {
        this.siebelDataBean = siebelDataBean;
        this.connection = connection;
        this.jdbcTemplateObject = new QueryRunner();
    }

    public Boolean updateInvoice(Integer id, InvoiceObject invoice) throws SQLException {
        this.query = "UPDATE RA_CUSTOMER_TRX_ALL SET CT_REFERENCE = ?, ATTRIBUTE1 = ? WHERE CUSTOMER_TRX_ID = ?";
        int stat = this.jdbcTemplateObject.update(this.connection, this.query, new Object[]{invoice.getCtRef(), invoice.getV_ref(), id});
        return stat > 0;
    }
}

