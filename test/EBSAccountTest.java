
import com.plexadasi.account.EBSAccount;
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection_old;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelBusObject;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
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
public class EBSAccountTest 
{
    public static void main(String[] args) throws SiebelBusinessServiceException, SQLException, SiebelException 
    {
        //Connection ebs = ApplicationsConnection_old.connectToEBSDatabase();
        SiebelDataBean sdb = ApplicationsConnection_old.connectSiebelServer();
        EBSAccount ebsAccount = new EBSAccount();
        //ebsAccount.doInvoke("1-J9TT", "53107", "contact", sb, ebs);//1-1SC-78
        //MyLogging.log(Level.INFO, "Done: " + String.valueOf(ebsAccount.getBillToId()));
        //MyLogging.log(Level.INFO, "Done: " + String.valueOf(ebsAccount.getShipToId()));
        SiebelBusObject quoteBusObj = sdb.getBusObject("Contact");
        SiebelBusComp quoteBusComp = quoteBusObj.getBusComp("Contact");
        //SiebelBusComp paymentBusComp = quoteBusObj.getBusComp("Payments");
        //find Quote
        SiebelPropertySet set = new SiebelPropertySet(), value = new SiebelPropertySet();
        set.setProperty("Street Address", "Street Address");
        set.setProperty("City", "City");
        set.setProperty("Created", "Created");
        set.setProperty("Personal Street Address", "Personal Street Address");
        quoteBusComp.activateMultipleFields(set);
        quoteBusComp.setViewMode(4);
        quoteBusComp.clearToQuery();
        quoteBusComp.setSearchSpec("Id", "1-J9UI");
        quoteBusComp.executeQuery2(true, true);
        if(quoteBusComp.firstRecord()){
            quoteBusComp.getMultipleFieldValues(set, value);
            MyLogging.log(Level.INFO, value.toString()); 
        }
        //ebs.close();
        sdb.logoff();
    }
}
