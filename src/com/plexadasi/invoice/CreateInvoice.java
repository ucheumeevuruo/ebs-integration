package com.plexadasi.invoice;

import com.plexadasi.Helper.DataConverter;
import com.plexadasi.build.EBSSql;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.bin.Quote;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
public class CreateInvoice 
{
    private static Connection EBS_CONN = null;
    private static SiebelDataBean SIEBEL_CONN = null;
    private static final StringWriter ERROR = new StringWriter();
    
    
    public static Integer doInvoke(String acc_id, InvoiceObject input, String type, SiebelDataBean siebelConn, Connection ebsConn) throws SiebelBusinessServiceException
    {
        Integer output = null;
        try 
        {
            if(siebelConn == null)
            {
                MyLogging.log(Level.SEVERE, "Connection to siebel cannot be established.");
                throw new SiebelBusinessServiceException("NULL_DEF", "Connection to siebel cannot be established.");
            }
            else if(ebsConn == null)
            {
                MyLogging.log(Level.SEVERE, "Connection to ebs cannot be established.");
                throw new SiebelBusinessServiceException("NULL_DEF", "Connection to ebs cannot be established.");
            }
            SIEBEL_CONN = siebelConn;
            EBS_CONN = ebsConn;
            EBSSql ebsSql = new EBSSql(EBS_CONN);
            EBSSqlData ebsSqlData = new EBSSqlData(EBS_CONN);
            Quote product = new Quote(SIEBEL_CONN);
            product.setSiebelAccountId(acc_id);
            ebsSql.createInvoiceQuote(SIEBEL_CONN, input, product);
            int cust_trx_id = ebsSql.getInt(12);
            MyLogging.log(Level.INFO, "Customer transaction Id: " + cust_trx_id);
            if(ebsSqlData.setCustReference(cust_trx_id, acc_id))
            {
                output = DataConverter.toInt(ebsSqlData.getTrxNumber(cust_trx_id));
            }
            else
            {
                throw new SiebelBusinessServiceException("UPD_ERROR", "Could not set customer ref for RA_CUSTOMER_TRX_ALL table.");
            }
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught SQL Exception:"+ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ERROR.toString()); 
        }
        return output;
    }
}
