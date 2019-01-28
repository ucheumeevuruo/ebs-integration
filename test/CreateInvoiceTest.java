
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection_old;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.invoice.CreateInvoice;
import com.plexadasi.invoice.InvoiceObject;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

/*
import com.plexadasi.invoice.InvoiceObject;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.invoice.CreateInvoice;
import com.siebel.eai.SiebelBusinessServiceException;
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
public class CreateInvoiceTest 
{
    
    public static void main(String[] args) throws SiebelBusinessServiceException, SQLException, SiebelException, IOException
    {
        InvoiceObject input = new InvoiceObject();
        input.setBillToId("51086");
        input.setTrxDistId("");
        input.setTrxDate("sysdate");
        input.setTrxCurrency("NGN");
        input.setTermId("1002");
        input.setPrimarySalesId("100000040");
        input.setCustomerTrxTypeId("1003");
        input.setCtRef("1-QGRPX");
        Connection ebs = ApplicationsConnection_old.connectToEBSDatabase();
        SiebelDataBean sb = ApplicationsConnection_old.connectSiebelServer();
        Integer output = CreateInvoice.doInvoke("1-QGRPX", input, "Quote", sb, ebs);
        MyLogging.log(Level.INFO, "Done: " + String.valueOf(output));
        ebs.close();
        sb.logoff();
    }
}