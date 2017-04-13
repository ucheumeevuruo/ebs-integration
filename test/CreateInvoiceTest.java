
import com.plexadasi.ebs.SiebelApplication.bin.InvoiceObject;
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.invoice.CreateInvoice;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.StringWriter;
import java.sql.Connection;
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
    private static Connection LOG = ApplicationsConnection.connectToEBSDatabase();
    
    private static StringWriter ERROR = new StringWriter();
    
    public static void main(String[] args) throws SiebelBusinessServiceException
    {
        InvoiceObject input = new InvoiceObject();
        input.setBillToId("28116");
        input.setTrxHeader("101");
        input.setTrxDistId("");
        input.setTrxDate("sysdate");
        input.setTrxCurrency("NGN");
        input.setTermId("1002");
        input.setLegalEntityId("24273");
        input.setPrimarySalesId("100000040");
        //input.setLegalEntityId("24273");
        input.setCustomerTrxTypeId("1003");
        input.setCtRef("1-10Z0T");
        input.setCodeCombinationId("6089");
        Integer output = CreateInvoice.doInvoke("1-10Z0T", input, "Quote");
        MyLogging.log(Level.INFO, "Done: " + String.valueOf(output));
    }
}
