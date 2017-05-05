
import com.plexadasi.account.EBSAccount;
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
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
        Connection ebs = ApplicationsConnection.connectToEBSDatabase();
        SiebelDataBean sb = ApplicationsConnection.connectSiebelServer();
        Integer doInvoke = EBSAccount.doInvoke("1-1P76S", "33117", "account", sb, ebs);//1-1SC-78
        MyLogging.log(Level.INFO, "Done: " + String.valueOf(doInvoke));
        ebs.close();
        sb.logoff();
    }
}
