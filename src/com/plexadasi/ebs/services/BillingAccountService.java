/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.services;

import com.plexadasi.connect.ebs.EbsConnect;
import com.plexadasi.ebs.model.BillingAccount;
import com.plexadasi.ebs.services.sql.build.SQLBuilder;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author SAP Training
 */
public class BillingAccountService {
    private final QueryRunner jdbcTemplateObject;
    private final SQLBuilder sqlBuilder = new SQLBuilder();
    private String query;
    private final Connection connection;
    
    public BillingAccountService(Connection connection){
        this.connection = connection;
        this.jdbcTemplateObject = new QueryRunner();
    }
    
    public Integer findBillToCode(Integer ebsId) throws SiebelBusinessServiceException, SQLException
    {
        Integer billTo = findBillingInformation(ebsId, "BILL_TO", 'b').getSiteUseId();
        return billTo == null ? 0 : billTo;
    }
    
    public Integer findShipToCode(Integer ebsId) throws SiebelBusinessServiceException, SQLException
    {
        Integer shipTo = findBillingInformation(ebsId, "SHIP_TO", 's').getSiteUseId();
        return shipTo == null ? 0 : shipTo;
    }
    
    public BillingAccount findBillingInformation(Integer ebsId, String type, char code) throws SQLException{
        ResultSetHandler<BillingAccount> rowMapper = new BeanHandler<BillingAccount>(BillingAccount.class);
        this.query = this.sqlBuilder.buildBillingSql(String.valueOf(code));

        BillingAccount billing = this.jdbcTemplateObject.query(this.connection, this.query, rowMapper, 
            new Object[]{
                ebsId,
                "P",
                type
            }
        );
        return billing == null ? new BillingAccount() : billing;
    }
    
    public static void main(String[] args) throws SiebelBusinessServiceException, SQLException
    {
        BillingAccountService bas = new BillingAccountService(EbsConnect.connectToEBSDatabase());
        int s = bas.findShipToCode(51086);
        System.out.println(s);
        //BackOrder b = sos.findBackOrderedItem(salesOrder);
        //System.out.println(b.getPickMeaning());
        //System.out.println(DataConverter.nullToString(b.getItemStatus()));
        /*
        inputs.setProperty("partnumber", "A9438202445");
        inputs.setProperty("lotid", "123");
        inputs.setProperty("ordernumber", "1-28859582");*/
    }
}
