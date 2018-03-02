
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection_old;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.order.PurchaseOrder;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.beans.PropertyVetoException;
import com.plexadasi.order.PurchaseOrderInventory;
import java.util.logging.Level;
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
        Connection ebs = ApplicationsConnection_old.connectToEBSDatabase();
        SiebelDataBean sb = ApplicationsConnection_old.connectSiebelServer();
        PurchaseOrder pOrder = new PurchaseOrder();
        PurchaseOrderInventory poInventory = new PurchaseOrderInventory();
        poInventory.setAccountType("organization");
        poInventory.setSiebelOrderId("1-LFJLR");
        poInventory.setSiebelAccountId("1-1CKWM");
        poInventory.setSourceId(33);
        //poInventory.triggers(sb, new EBSSqlData(ebs));
        //System.out.println(poInventory.toString());
        //pOrder.doInvoke(poInventory, sb, ebs);
        //String po = pOrder.getPONumber(ebs, "1-35997183");
        pOrder.getPurchaseOrderBookingStatus(ebs, "1-38783113");
        //MyLogging.log(Level.INFO, "Done: " + po);
        ebs.close();
        sb.logoff();
    }
}
