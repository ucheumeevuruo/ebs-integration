package com.plexadasi.invoice;

import com.plexadasi.ebs.build.objects.CreateInvoiceSQL;
import com.plexadasi.ebs.Helper.DataConverter;
import com.plexadasi.build.EBSSql;
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.Order;
import com.plexadasi.ebs.SiebelApplication.bin.Quote;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.build.EBSSqlData;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.IOException;
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
    private static final Connection EBS_CONN = ApplicationsConnection.connectToEBSDatabase();
    private static final SiebelDataBean SIEBEL_CONN = ApplicationsConnection.connectSiebelServer();
    private static final StringWriter ERROR = new StringWriter();
    
    
    public static Integer doInvoke(String acc_id, InvoiceObject input, String type) throws SiebelBusinessServiceException
    {
        Integer output = null;
        try 
        {
            
            EBSSql ebsSql = new EBSSql(EBS_CONN);
            EBSSqlData ebsSqlData = new EBSSqlData(EBS_CONN);
            Product product = null;
            // Check if the user type is an individual or an organization
            // If the type is neither, throw an exception back to siebel
            if(type.equalsIgnoreCase("quote"))
            {
                product = new Quote(SIEBEL_CONN);
                
                product.setSiebelAccountId(acc_id);
            }
            else if(type.equalsIgnoreCase("account"))
            {
                product = new Order(SIEBEL_CONN);
                
                product.setSiebelAccountId(acc_id);
            }
            else
            {
                MyLogging.log(Level.SEVERE, "No Such Method Exception: Value passed to parameter three in doInvoke method is invalid.\n Values allowed Account, Contact");
                throw new SiebelBusinessServiceException("NO_SUCH_METHOD", "Method not found");
            }   
            
            CreateInvoiceSQL createSql = new CreateInvoiceSQL(EBS_CONN, product);
            createSql.setProperty(input);
            ebsSql.createInvoiceQuote(createSql);
            int cust_trx_id = ebsSql.getInt(1);
            if(ebsSqlData.setCustReference(cust_trx_id, acc_id))
            {
                output = DataConverter.toInt(ebsSqlData.getTrxNumber(cust_trx_id));
            }
            else
            {
                throw new SiebelBusinessServiceException("UPD_ERROR", "Could not set customer ref for RA_CUSTOMER_TRX_ALL table.");
            }
            SIEBEL_CONN.logoff();
            EBS_CONN.close();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught SQL Exception:"+ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ERROR.toString()); 
        }
        catch(IOException ex)
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught IO Exception:"+ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ERROR.toString()); 
        }
        catch(SiebelException ex)
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught Siebel Exception:"+ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ERROR.toString()); 
        }
        return output;
    }
}
