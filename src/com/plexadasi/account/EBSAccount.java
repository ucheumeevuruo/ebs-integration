package com.plexadasi.account;

import com.plexadasi.build.EBSSql;
import com.plexadasi.ebs.build.objects.CustomerSite;
import com.plexadasi.ebs.build.objects.PartySite;
import com.plexadasi.ebs.build.objects.AccountSite;
import com.plexadasi.ebs.build.objects.SiteUsage;
import com.plexadasi.ebs.build.objects.Location;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.Individual;
import com.plexadasi.ebs.SiebelApplication.bin.Organization;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Account;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class EBSAccount 
{
    private static Connection EBS_CONN = null;
    private static SiebelDataBean SIEBEL_CONN = new SiebelDataBean();
    private static PreparedStatement preparedStatement = null;
    private static final StringWriter ERROR = new StringWriter();
    
    
    public Integer doInvoke(String acc_id, String ebs_id, String type, SiebelDataBean siebelConn, Connection ebsConn) throws SiebelBusinessServiceException
    {
        Integer output;
        try 
        {
            SIEBEL_CONN = siebelConn;
            EBS_CONN = ebsConn;
            EBSSql e;
            e = new EBSSql(EBS_CONN);
            Account account = null;
            // Check if the user type is an individual or an organization
            // If the type is neither, throw an exception back to siebel
            if(type.equalsIgnoreCase("contact"))
            {
                account = new Individual(SIEBEL_CONN);
            }
            else if(type.equalsIgnoreCase("account"))
            {
                account = new Organization(SIEBEL_CONN);
            }
            else
            {
                MyLogging.log(Level.SEVERE, "No Such Method Exception: Value passed to parameter three in doInvoke method is invalid.\n Values allowed Account, Contact");
                throw new SiebelBusinessServiceException("NO_SUCH_METHOD", "Method not found");
            }   
            account.setSiebelAccountId(acc_id);
            // Let use setup the account location
            Location loc = new Location(account);
            e.createLocation(loc);
            int location_id = e.getInt(1);
            
            //Search for the party Id of the account.
            String selectSQL = "SELECT hca.party_id, hp.party_name\n" +
                    "FROM hz_cust_accounts hca, hz_parties hp\n" +
                    "WHERE hca.party_id = hp.party_id\n" +
                    "AND hca.cust_account_id = ?";
            // Instanciate the party site class
            PartySite party = new PartySite();
            preparedStatement = EBS_CONN.prepareStatement(selectSQL);
            preparedStatement.setInt(1, Integer.valueOf(ebs_id));
            // execute select SQL stetement
            ResultSet rs = preparedStatement.executeQuery();
            String PartyId, PartyName;
            PartyId = PartyName = "";
            while (rs.next()) 
            {
                PartyId = rs.getString("party_id");
                PartyName = rs.getString("party_name");
            }   
            
            
            MyLogging.log(Level.INFO, "Get SQL  : " + "SELECT hca.party_id, hp.party_name\n" +
                    "FROM hz_cust_accounts hca, hz_parties hp\n" +
                    "WHERE hca.party_id = hp.party_id\n" +
                    "AND hca.cust_account_id = " + ebs_id);
            MyLogging.log(Level.INFO, "Party Site Id = " + PartyId + " Party Name = " + PartyName);
            // Setting up properties for party site
            party.setProperty("party_id", String.valueOf(PartyId));
            party.setProperty("location_id", String.valueOf(location_id));
            party.setProperty("flag", "Y");
            party.setProperty("module", "BO_API");
            e.createPartySite(party);
            int party_site_id = e.getInt(1);
            MyLogging.log(Level.INFO, "Party site Id : " + party_site_id);
            // Instanciate the site usage
            SiteUsage siteUsage = new SiteUsage();
            siteUsage.setProperty("use_type", "BILL_TO");
            siteUsage.setProperty("site_id", String.valueOf(party_site_id));
            siteUsage.setProperty("module", "HZ_CPUI");
            e.createSiteUsage(siteUsage);
            int site_use_id = e.getInt(1);
            MyLogging.log(Level.INFO, "Party site Id : " + site_use_id);
            CustomerSite cust = new CustomerSite();
            cust.setProperty("account_id", ebs_id);
            cust.setProperty("site_id", String.valueOf(site_use_id));
            cust.setProperty("module", "BO_API");
            e.createCustomerSite(cust);
            int acc_site_id = e.getInt(1);
            MyLogging.log(Level.INFO, "Account Id : " + acc_site_id);
            AccountSite acc = new AccountSite();
            acc.setProperty("account_site_id", String.valueOf(acc_site_id));
            acc.setProperty("usage", "BILL_TO");
            acc.setProperty("module", "HZ_CPUI");
            e.createAccountSite(acc);
            output = e.getInt(1);
            
            if(output <= 0)
            {
                throw new SQLException("There was error in process. Final output returned is zero.");
            }
            e = null;
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught SQL Exception:"+ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ERROR.toString()); 
        }
        catch(IOException ex)
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught IO Exception:"+ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ERROR.toString()); 
        }
        
        return output;
    }
}
