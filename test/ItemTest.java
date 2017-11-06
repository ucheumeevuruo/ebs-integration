
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import item.Items;
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
public class ItemTest 
{
    public static void main(String[] args) throws SiebelBusinessServiceException, SQLException, SiebelException, PropertyVetoException 
    {
        SiebelDataBean sb = new SiebelDataBean();
        Connection ebs = ApplicationsConnection.connectToEBSDatabase();
        ///SiebelDataBean sb = ApplicationsConnection.connectSiebelServer();
        Items item = new Items(sb, ebs);
        //item.CreateItem("Actros 3343", "WDB9301831L7889");
        item.AssignItem(50648, "124");
        MyLogging.log(Level.INFO, "Done: ");
        ebs.close();
        //sb.logoff();
    }
}
