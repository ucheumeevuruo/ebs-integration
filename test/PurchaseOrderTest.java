
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection;
import com.plexadasi.order.PurchaseOrder;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.beans.PropertyVetoException;
import com.plexadasi.order.PurchaseOrderInventory;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training//3053495
 *///bpms //154760
public class PurchaseOrderTest 
{
    public static void main(String[] args) throws SiebelBusinessServiceException, SQLException, SiebelException, PropertyVetoException 
    {
        Connection ebs = ApplicationsConnection.connectToEBSDatabase();
        SiebelDataBean sb = ApplicationsConnection.connectSiebelServer();
        PurchaseOrder pOrder = new PurchaseOrder();
        PurchaseOrderInventory poInventory = new PurchaseOrderInventory(sb, ebs);
        poInventory.setAccountType("organization");
        poInventory.setSiebelOrderId("1-2KBRL");
        poInventory.setSiebelAccountId("1-22FBL");
        poInventory.setEbsId("35113");
        poInventory.setSourceId(97);
        pOrder.doInvoke(poInventory);
        //MyLogging.log(Level.INFO, "Done: " + String.valueOf(ebsAccount.getReturnStatus()) + " Order Number:" + ebsAccount.getOrderNumber());
        ebs.close();
        sb.logoff();
    }
}
