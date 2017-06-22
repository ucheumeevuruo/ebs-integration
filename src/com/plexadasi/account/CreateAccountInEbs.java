package com.plexadasi.account;

import com.plexadasi.build.EBSSql;
import com.plexadasi.build.EBSSqlData;
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
public class CreateAccountInEbs 
{
    private static Connection EBS_CONN = null;
    private static SiebelDataBean SIEBEL_CONN = new SiebelDataBean();
    private static final StringWriter ERROR = new StringWriter();
    private String siteUseType = "";
    private Integer locationId;
    private String partyId;
    private Integer partySiteId;
    private Integer siteUseId;
    private Integer partySiteUseId;
    private Integer accountSiteId;
    
    public Integer create(String acc_id, String ebs_id, String type, SiebelDataBean siebelConn, Connection ebsConn) throws SiebelBusinessServiceException
    {
        try 
        {
            if(siebelConn == null)
            {
                MyLogging.log(Level.SEVERE, "Connection to siebel cannot be established.");
                throw new SiebelBusinessServiceException("NULL_DEF", "Connection to siebel cannot be established.");
            }
            else if(ebsConn == null)
            {
                MyLogging.log(Level.SEVERE, "Connection to ebs cannot be established.");
                throw new SiebelBusinessServiceException("NULL_DEF", "Connection to ebs cannot be established.");
            }
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
            if(siteUseType.equals(""))
            {
                throw new SiebelBusinessServiceException("CUMPOLSARY_EXCPT", "Site use type must be defined");
            }
            account.setSiebelAccountId(acc_id);
            // Let use setup the account location
            Location loc = new Location(account);
            e.createLocation(loc);
            locationId = e.getInt(1);
            checkPoint(e.getString(2));
            //Get party id
            EBSSqlData esd = new EBSSqlData(EBS_CONN);
            partyId = esd.getPartySiteId(ebs_id);
            // Instanciate the party site class
            PartySite party = new PartySite();
            MyLogging.log(Level.INFO, "Party Site Id = " + partyId);
            // Setting up properties for party site
            party.setProperty("party_id", String.valueOf(partyId));
            party.setProperty("location_id", String.valueOf(locationId));
            party.setProperty("flag", "Y");
            party.setProperty("module", "BO_API");
            e.createPartySite(party);
            partySiteId = e.getInt(1);
            checkPoint(e.getString(3));
            MyLogging.log(Level.INFO, "Party site Id : " + partySiteId);
            // Instanciate the site usage
            SiteUsage siteUsage = new SiteUsage();
            siteUsage.setProperty("use_type", siteUseType);
            siteUsage.setProperty("site_id", String.valueOf(partySiteId));
            siteUsage.setProperty("module", "HZ_CPUI");
            e.createSiteUsage(siteUsage);
            partySiteId = e.getInt(1);
            partySiteUseId = e.getInt(2);
            checkPoint(e.getString(3));
            MyLogging.log(Level.INFO, "Party site Id : " + partySiteId);
            MyLogging.log(Level.INFO, "Party site use Id : " + siteUseId);
            // Customer Site
            CustomerSite cust = new CustomerSite();
            cust.setProperty("account_id", ebs_id);
            cust.setProperty("site_id", String.valueOf(partySiteId));
            cust.setProperty("module", "BO_API");
            e.createCustomerSite(cust);
            accountSiteId = e.getInt(1);
            checkPoint(e.getString(2));
            MyLogging.log(Level.INFO, "Account Id : " + accountSiteId);
            AccountSite acc = new AccountSite();
            acc.setProperty("account_site_id", String.valueOf(accountSiteId));
            acc.setProperty("usage", siteUseType);
            acc.setProperty("module", "HZ_CPUI");
            e.createAccountSite(acc);
            siteUseId = e.getInt(1);
            checkPoint(e.getString(3));
            MyLogging.log(Level.INFO, "Site use type: " + siteUseType);
            MyLogging.log(Level.INFO, "Site use id: " + siteUseId);
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught SQL Exception:"+ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_SQL_EXCEPT", ERROR.toString()); 
        }
        catch(IOException ex)
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught IO Exception:"+ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_IO_EXCEPT", ERROR.toString()); 
        }
        
        return siteUseId;
    }
    
    public void setSiteUseType(String type)
    {
        siteUseType = type;
    }
    
    private void checkPoint(String checkPoint) throws SQLException
    {
        MyLogging.log(Level.INFO, "Return value at check point is " + checkPoint + " where (S) is success and (E) is failure.");
        if(!checkPoint.equalsIgnoreCase("s"))
        {
           throw new SQLException("There was error processing information");
        }
    }
}
