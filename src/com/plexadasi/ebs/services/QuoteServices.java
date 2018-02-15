/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.services;

import com.plexadasi.Helper.DataConverter;
import com.plexadasi.Helper.HelperAP;
import com.plexadasi.ebs.SiebelApplication.bin.QuoteInventory;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.CallableRunner;
import com.plexadasi.ebs.services.sql.build.SQLBuilder;
import com.plexadasi.invoice.InvoiceObject;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.apache.commons.dbutils.OutParameter;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author SAP Training
 */
public class QuoteServices {
    private final QueryRunner jdbcTemplateObject;
    private final SQLBuilder sqlBuilder = new SQLBuilder();
    private String query;
    private final Connection connection;
    private final SiebelDataBean siebelDataBean;
    
    public QuoteServices(SiebelDataBean siebelDataBean, Connection connection)
    {
        this.siebelDataBean = siebelDataBean;
        this.connection = connection;
        this.jdbcTemplateObject = new QueryRunner();
    }
    
    public Map<Integer, Object> createInvoice(InvoiceObject invoice) throws SQLException, SiebelBusinessServiceException, SiebelException{
        
        CallableRunner prun = new CallableRunner();
	Map<Integer, Object> order = prun.queryProc(this.connection,
            "{CALL SALES_ORDER_TEST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", new Object[]
            {
                DataConverter.toInt(HelperAP.getEbsUserId()),
            // Get the Ebs User Resposibility
                DataConverter.toInt(HelperAP.getEbsUserResp()),
            // Get the Ebs Application Id
                DataConverter.toInt(HelperAP.getEbsAppId()),
            // Get the batch source id
                DataConverter.toInt(HelperAP.getSourceBatchId()),
            // Get customers id
                DataConverter.toInt(invoice.getBillToId()),
                DataConverter.toInt(invoice.getCustomerTrxTypeId()),
                invoice.getTrxCurrency(),
                invoice.getTermId(),
                DataConverter.toInt(HelperAP.getLegalEntity()),
                invoice.getPrimarySalesId(),
                new QuoteInventory(this.siebelDataBean, this.connection, invoice.getId()).getInventoryItem(),
                new OutParameter(java.sql.Types.INTEGER, Integer.class)
            }
        );
        return order;
    }
}
