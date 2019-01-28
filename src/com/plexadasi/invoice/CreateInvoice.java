/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.invoice;

import com.plexadasi.helper.DataConverter;
import com.plexadasi.build.EBSSql;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.Quote;
import com.plexadasi.ebs.services.QuoteServices;
import com.plexadasi.invoice.InvoiceObject;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateInvoice {
    private static Connection EBS_CONN = null;
    private static SiebelDataBean SIEBEL_CONN = null;
    private static final StringWriter ERROR = new StringWriter();

    public static Integer doInvoke(String acc_id, InvoiceObject input, String type, SiebelDataBean siebelConn, Connection ebsConn) throws SiebelBusinessServiceException {
        Integer output = null;
        try {
            if (siebelConn == null) {
                MyLogging.log(Level.SEVERE, "Connection to siebel cannot be established.");
                throw new SiebelBusinessServiceException("NULL_DEF", "Connection to siebel cannot be established.");
            }
            if (ebsConn == null) {
                MyLogging.log(Level.SEVERE, "Connection to ebs cannot be established.");
                throw new SiebelBusinessServiceException("NULL_DEF", "Connection to ebs cannot be established.");
            }
            SIEBEL_CONN = siebelConn;
            EBS_CONN = ebsConn;
            EBSSql ebsSql = new EBSSql(EBS_CONN);
            EBSSqlData ebsSqlData = new EBSSqlData(EBS_CONN);
            QuoteServices invoice = new QuoteServices(siebelConn, ebsConn);
            Quote product = new Quote(SIEBEL_CONN);
            product.setSiebelAccountId(acc_id);
            SiebelPropertySet property = SIEBEL_CONN.newPropertySet();
            property.setProperty("Quote Number", "Quote Number");
            property.setProperty("PLX Ref Number", "PLX Ref Number");
            property.setProperty("PLX Proforma Invoice Number", "PLX Proforma Invoice Number");
            property.setProperty("PLX Comp. Program", "PLX Comp. Program");
            property.setProperty("PLX Supplies/Sundries", "PLX Supplies/Sundries");
            property.setProperty("PLX Withholding tax", "PLX Withholding tax");
            property.setProperty("PLX Warehouse Id", "PLX Warehouse Id");
            property.setProperty("PLX Local Delivery Charges", "PLX Local Delivery Charges");
            property.setProperty("Freight Total", "Freight Total");
            Map<String, String> quoteHeader = product.getQuoteHeader(property);
            input.setV_ref(quoteHeader.get("PLX Ref Number"));
            input.setCtRef(quoteHeader.get("PLX Proforma Invoice Number"));
            input.setQuoteNumber(quoteHeader.get("Quote Number"));
            input.setWarehouseId(DataConverter.toInt(quoteHeader.get("PLX Warehouse Id")));
            input.setLocalDeliveryCharges(DataConverter.toFloat(quoteHeader.get("PLX Local Delivery Charges")));
            input.setFreight(DataConverter.toFloat(quoteHeader.get("Freight Total")));
            input.setComputerProgramming(DataConverter.toFloat(quoteHeader.get("PLX Comp. Program")));
            input.setSundries(DataConverter.toFloat(quoteHeader.get("PLX Supplies/Sundries")));
            input.setWithholdingTax(DataConverter.toFloat(quoteHeader.get("PLX Withholding tax")));
            ebsSql.createInvoiceQuote(SIEBEL_CONN, input, product);
            int cust_trx_id = ebsSql.getInt(12);
            MyLogging.log(Level.INFO, "Customer transaction Id: " + cust_trx_id);
            if (!invoice.updateInvoice(cust_trx_id, input).booleanValue()) {
                throw new SiebelBusinessServiceException("UPD_ERROR", "Could not set customer ref for RA_CUSTOMER_TRX_ALL table.");
            }
            output = DataConverter.toInt(ebsSqlData.getTrxNumber(cust_trx_id));
            MyLogging.log(Level.INFO, "EBS Order Number:" + output);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught SQL Exception:" + ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ERROR.toString());
        }
        catch (SiebelException ex) {
            Logger.getLogger(CreateInvoice.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        return output;
    }
}

