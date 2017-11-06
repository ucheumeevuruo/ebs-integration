/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package item;

import com.plexadasi.build.EBSSql;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.Quote;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import com.plexadasi.ebs.SiebelApplication.bin.SalesOrder;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author SAP Training
 */
public class Items {
    private Connection EBS_CONN = null;
    private SiebelDataBean SIEBEL_CONN = new SiebelDataBean();
    private static final StringWriter ERROR = new StringWriter();
    private EBSSql e = null;
    private int orgId;
    private int invId;
    
    public Items(SiebelDataBean sb, Connection ebs) throws SiebelBusinessServiceException
    {
        SIEBEL_CONN = sb;
        EBS_CONN = ebs;
        if(SIEBEL_CONN == null)
        {
            MyLogging.log(Level.SEVERE, "Connection to siebel cannot be established.");
            throw new SiebelBusinessServiceException("NULL_DEF", "Connection to siebel cannot be established.");
        }
        else if(EBS_CONN == null)
        {
            MyLogging.log(Level.SEVERE, "Connection to ebs cannot be established.");
            throw new SiebelBusinessServiceException("NULL_DEF", "Connection to ebs cannot be established.");
        }
        if(e == null)
        {
            e = new EBSSql(EBS_CONN);
        }
    }
    
    public void CreateItem(String desc, String part) throws SiebelBusinessServiceException{
        try {
            e.CreateItemInEBS(desc, part);
            this.orgId = e.getInt(8);
            this.invId = e.getInt(9);
        } catch (SQLException ex) {
            MyLogging.log(Level.SEVERE, ex.getMessage());
            throw new SiebelBusinessServiceException("CREATE_UPDATE_ITEM", ex.getMessage());
        }
    }
    
    public void AssignItem(Integer invId, String orgId) throws SiebelBusinessServiceException{
        try {
            e.ItemAssignToChild(invId, orgId);
        } catch (SiebelBusinessServiceException ex) {
            MyLogging.log(Level.SEVERE, ex.getMessage());
            throw new SiebelBusinessServiceException("ITEM_ASSIGN", ex.getMessage());
        }
    }
    
    public void setOnHandQuantity(SiebelPropertySet inputs) throws SiebelBusinessServiceException{
        String type = inputs.getProperty("Type");
        String Id = inputs.getProperty("Id");
        String warehouse = inputs.getProperty("Warehouse");
        if("Sales Order".equals(type))
        {
            SalesOrder s = new SalesOrder(SIEBEL_CONN);
            s.setSiebelAccountId(Id);
            MyLogging.log(Level.INFO, "Order Number: " + s.getSiebelAccountId());
            s.onHandQuantities(EBS_CONN, warehouse);
        }
        else if("Quote".equals(type))
        {
            Quote q = new Quote(SIEBEL_CONN);
            q.setSiebelAccountId(Id);
            MyLogging.log(Level.INFO, "Quote Number: " + q.getSiebelAccountId());
            q.onHandQuantities(EBS_CONN, warehouse);
        }
    }
    
    public Integer getOrganizationId(){
        return this.orgId;
    }
    
    public Integer getInventoryId(){
        return this.invId;
    }
}
