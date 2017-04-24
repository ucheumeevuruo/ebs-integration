
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
    
    public static void main(String[] args) throws SiebelBusinessServiceException
    {
        InvoiceObject input = new InvoiceObject();
        input.setBillToId("28116");
        input.setTrxDistId("");
        input.setTrxDate("sysdate");
        input.setTrxCurrency("NGN");
        input.setTermId("1002");
        input.setPrimarySalesId("100000040");
        input.setCustomerTrxTypeId("1003");
        input.setCtRef("1-10Z0T");
        Integer output = CreateInvoice.doInvoke("1-21VDT", input, "Quote");
        MyLogging.log(Level.INFO, "Done: " + String.valueOf(output));
    }
}