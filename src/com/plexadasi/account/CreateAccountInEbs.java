/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.account;

import com.plexadasi.build.EBSSql;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.Individual;
import com.plexadasi.ebs.SiebelApplication.bin.Organization;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Account;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ImplSql;
import com.plexadasi.ebs.build.objects.AccountSite;
import com.plexadasi.ebs.build.objects.CustomerSite;
import com.plexadasi.ebs.build.objects.Location;
import com.plexadasi.ebs.build.objects.PartySite;
import com.plexadasi.ebs.build.objects.SiteUsage;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

public class CreateAccountInEbs {
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

    public Integer create(String acc_id, String ebs_id, String type, SiebelDataBean siebelConn, Connection ebsConn) throws SiebelBusinessServiceException {
        try {
            if (siebelConn == null) {
                MyLogging.log(Level.SEVERE, "Connection to siebel cannot be established.");
                throw new SiebelBusinessServiceException("NULL_DEF", "Connection to siebel cannot be established.");
            }
            if (ebsConn == null) {
                MyLogging.log(Level.SEVERE, "Connection to ebs cannot be established.");
                throw new SiebelBusinessServiceException("NULL_DEF", "Connection to ebs cannot be established.");
            }
            SIEBEL_CONN = siebelConn;
            EBS_CONN = ebsConn;
            EBSSql e = new EBSSql(EBS_CONN);
            Account account = null;
            if (type.equalsIgnoreCase("contact")) {
                account = new Individual(SIEBEL_CONN);
            } else if (type.equalsIgnoreCase("account")) {
                account = new Organization(SIEBEL_CONN);
            } else {
                MyLogging.log(Level.SEVERE, "No Such Method Exception: Value passed to parameter three in doInvoke method is invalid.\n Values allowed Account, Contact");
                throw new SiebelBusinessServiceException("NO_SUCH_METHOD", "Method not found");
            }
            if (this.siteUseType.equals("")) {
                throw new SiebelBusinessServiceException("COMPULSARY_EXCPT", "Site use type must be defined");
            }
            account.setSiebelAccountId(acc_id);
            Location loc = new Location(account);
            e.createLocation(loc);
            this.locationId = e.getInt(1);
            this.checkPoint(e.getString(2));
            EBSSqlData esd = new EBSSqlData(EBS_CONN);
            this.partyId = esd.getPartySiteId(ebs_id);
            PartySite party = new PartySite();
            MyLogging.log(Level.INFO, "Party Site Id = " + this.partyId);
            party.setProperty("party_id", String.valueOf(this.partyId));
            party.setProperty("location_id", String.valueOf(this.locationId));
            party.setProperty("flag", "Y");
            party.setProperty("module", "BO_API");
            e.createPartySite(party);
            this.partySiteId = e.getInt(1);
            this.checkPoint(e.getString(3));
            MyLogging.log(Level.INFO, "Party site Id : " + this.partySiteId);
            SiteUsage siteUsage = new SiteUsage();
            siteUsage.setProperty("use_type", this.siteUseType);
            siteUsage.setProperty("site_id", String.valueOf(this.partySiteId));
            siteUsage.setProperty("module", "HZ_CPUI");
            e.createSiteUsage(siteUsage);
            this.partySiteId = e.getInt(1);
            this.partySiteUseId = e.getInt(2);
            this.checkPoint(e.getString(3));
            MyLogging.log(Level.INFO, "Party site Id : " + this.partySiteId);
            MyLogging.log(Level.INFO, "Party site use Id : " + this.siteUseId);
            CustomerSite cust = new CustomerSite();
            cust.setProperty("account_id", ebs_id);
            cust.setProperty("site_id", String.valueOf(this.partySiteId));
            cust.setProperty("module", "BO_API");
            e.createCustomerSite(cust);
            this.accountSiteId = e.getInt(1);
            this.checkPoint(e.getString(2));
            MyLogging.log(Level.INFO, "Account Id : " + this.accountSiteId);
            AccountSite acc = new AccountSite();
            acc.setProperty("account_site_id", String.valueOf(this.accountSiteId));
            acc.setProperty("usage", this.siteUseType);
            acc.setProperty("module", "HZ_CPUI");
            e.createAccountSite(acc);
            this.siteUseId = e.getInt(1);
            this.checkPoint(e.getString(3));
            MyLogging.log(Level.INFO, "Site use type: " + this.siteUseType);
            MyLogging.log(Level.INFO, "Site use id: " + this.siteUseId);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught SQL Exception:" + ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_SQL_EXCEPT", ERROR.toString());
        }
        catch (IOException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught IO Exception:" + ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_IO_EXCEPT", ERROR.toString());
        }
        return this.siteUseId;
    }

    public void setSiteUseType(String type) {
        this.siteUseType = type;
    }

    private void checkPoint(String checkPoint) throws SQLException {
        MyLogging.log(Level.INFO, "Return value at check point is " + checkPoint + " where (S) is success and (E) is failure.");
        if (!checkPoint.equalsIgnoreCase("s")) {
            throw new SQLException("There was error processing information");
        }
    }
}

