
import com.plexadasi.account.EBSAccount;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
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
public class EBSAccountTest 
{
    public static void main(String[] args) throws SiebelBusinessServiceException 
    {
        Integer doInvoke = EBSAccount.doInvoke("1-GQII", "27116", "account");//1-1SC-78
        MyLogging.log(Level.INFO, "Done: " + String.valueOf(doInvoke));
    }
}
