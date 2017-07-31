
import com.plexadasi.ebs.Helper.DataConverter;
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.order.SalesOrder;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import com.plexadasi.order.SalesOrderInventory;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training//3053495
 *///bpms //154760
public class SalesOrderTest 
{
    public static void main(String[] args) throws SiebelBusinessServiceException, SQLException, SiebelException, PropertyVetoException 
    {
        Connection ebs = ApplicationsConnection.connectToEBSDatabase();
        SiebelDataBean sb = ApplicationsConnection.connectSiebelServer();
        SalesOrder ebsAccount = new SalesOrder();
        SalesOrderInventory s = new SalesOrderInventory();
        s.setSiebelOrderId("1-3746273");
        s.setOrderId(1001);//fixed
        s.setSoldToOrgId(31126);//ebs customer id 35113
        s.setShipToOrgId(16165);// site use id
        s.setInvoiceId(16164);// site use id
        s.setSoldFromId(124);
        s.setSalesRepId(100000040);
        //s.setPriceId(9013);
        s.setTransactionCode("NGN");
        s.setStatusCode("ENTERED");
        s.setPurchaseOrderNumber("1-3129373");
        s.setSourceId(0);
        // To print out the values passed to the object Sales Order Inventory
        // I created an output that writes the object as string.
        List<SalesOrderInventory> list = new ArrayList();
        list.add(s);
        MyLogging.log(Level.INFO, "Describe Sales Order Inventory Object \n" + list);
        ebsAccount.doInvoke(s, sb, ebs);
        //ebsAccount.getSalesOrderBookingStatus(ebs, String.valueOf(ebsAccount.getOrderNumber()));
        MyLogging.log(Level.INFO, "Done: " + String.valueOf(ebsAccount.getReturnStatus()) + " Order Number:" + ebsAccount.getOrderNumber() + ebsAccount.getFlowStatusCode());
        //ebsAccount.cancelOrder(ebs, 234);
        //ebsAccount.cancelLineOrder(ebs, 233, 14);
        List<String> ht = ebsAccount.getReturnMessages();
        String returns = "";
        for (String ht1 : ht) {
            returns += ht1 + "\n";
        }
        //MyLogging.log(Level.INFO, "Done Cancel: " + String.valueOf(ebsAccount.getReturnStatus()) + " Return Message:" + returns + " Return Flow Status Code:" + ebsAccount.getSalesOrderBookingStatus(ebs, returns));
        ebs.close();
        sb.logoff();
    }
}
