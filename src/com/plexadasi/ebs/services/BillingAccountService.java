/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.plexadasi.connect.ebs.EbsConnect
 *  com.siebel.eai.SiebelBusinessServiceException
 *  org.apache.commons.dbutils.QueryRunner
 *  org.apache.commons.dbutils.ResultSetHandler
 *  org.apache.commons.dbutils.handlers.BeanHandler
 */
package com.plexadasi.ebs.services;

import com.plexadasi.connect.ebs.EbsConnect;
import com.plexadasi.ebs.model.BillingAccount;
import com.plexadasi.ebs.services.sql.build.SQLBuilder;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

public class BillingAccountService {
    private final QueryRunner jdbcTemplateObject;
    private final SQLBuilder sqlBuilder = new SQLBuilder();
    private String query;
    private final Connection connection;

    public BillingAccountService(Connection connection) {
        this.connection = connection;
        this.jdbcTemplateObject = new QueryRunner();
    }

    public Integer findBillToCode(Integer ebsId) throws SQLException {
        Integer billTo = this.findBillingInformation(ebsId, "BILL_TO", 'b').getSiteUseId();
        return billTo == null ? 0 : billTo;
    }

    public Integer findShipToCode(Integer ebsId) throws SQLException {
        Integer shipTo = this.findBillingInformation(ebsId, "SHIP_TO", 's').getSiteUseId();
        return shipTo == null ? 0 : shipTo;
    }

    public BillingAccount findBillingInformation(Integer ebsId, String type, char code) throws SQLException {
        BeanHandler rowMapper = new BeanHandler(BillingAccount.class);
        this.query = this.sqlBuilder.buildBillingSql(String.valueOf(code));
        
        BillingAccount billing = (BillingAccount)this.jdbcTemplateObject.query(this.connection, this.query, (ResultSetHandler)rowMapper, new Object[]{ebsId, "P", type});
        return billing == null ? new BillingAccount() : billing;
    }

    public static void main(String[] args) throws SiebelBusinessServiceException, SQLException {
        BillingAccountService bas = new BillingAccountService(EbsConnect.connectToEBSDatabase());
        int s = bas.findShipToCode(4078);
        System.out.println(s);
        s = bas.findBillToCode(4078);
        System.out.println(s);
    }
}

