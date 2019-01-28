
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection_old;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.item.Items;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.beans.PropertyVetoException;
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
        Connection ebs = ApplicationsConnection_old.connectToEBSDatabase();
        ///SiebelDataBean sb = ApplicationsConnection_old.connectSiebelServer();
        Items item = new Items(sb, ebs);
        //item.CreateItem("Actros 3343", "WDB9301831L7889");
        item.AssignItem(50648, "124");
        MyLogging.log(Level.INFO, "Done: ");
        ebs.close();
        //sb.logoff();
    }
}
