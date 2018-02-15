/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package item;

import com.plexadasi.build.EBSSql;
import com.plexadasi.Helper.DataConverter;
import com.plexadasi.Helper.HelperAP;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.PLXBackOrder;
import com.plexadasi.ebs.SiebelApplication.bin.QuotePartItem;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import com.plexadasi.ebs.SiebelApplication.bin.SalesOrderItem;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.model.Product;
import com.plexadasi.ebs.services.ProductService;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public Product CreateItem(String desc, String part) throws SiebelBusinessServiceException{
        Product item = null;
        try {
            ProductService service = new ProductService(this.SIEBEL_CONN, this.EBS_CONN);
            Product product = new Product();
            product.setPartNumber(part);
            product.setDescription(desc);
            product.setOrganizationCode(HelperAP.getMasterOrganizationCode());
            item = service.createProduct(product);
            MyLogging.log(Level.INFO, String.valueOf(item.getId()));
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, ERROR.toString());
            throw new SiebelBusinessServiceException("SQL", ex.getMessage());
        }
        return item;
    }
    
    public void AssignItem(Integer invId, String orgId) throws SiebelBusinessServiceException{
        try {
            ProductService service = new ProductService(this.SIEBEL_CONN, this.EBS_CONN);
            Product product = new Product();
            product.setId(invId);
            product.setOrganizationId(DataConverter.toInt(orgId));
            service.assignProduct(product);
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, ERROR.toString());
            throw new SiebelBusinessServiceException("SQL", ex.getMessage());
        }
    }
    
    public void setOnHandQuantity(SiebelPropertySet inputs) throws SiebelBusinessServiceException{
        try {
            String type = inputs.getProperty("Type");
            String Id = inputs.getProperty("Id");
            String warehouse = inputs.getProperty("Warehouse");
            SalesOrderItem s = new SalesOrderItem(SIEBEL_CONN);
            if("Quote".equals(type))
            {
                s = new QuotePartItem(SIEBEL_CONN);
            }
            s.setSiebelAccountId(Id);
            s.setOrgId(Integer.parseInt(warehouse));
            s.onHandQuantities(EBS_CONN, "OHQ");
        } catch (SiebelException ex) {
            MyLogging.log(Level.SEVERE, ex.getMessage());
            throw new SiebelBusinessServiceException("SIEBEL", ex.getMessage());
        } catch (SQLException ex) {
            MyLogging.log(Level.SEVERE, ex.getMessage());
            throw new SiebelBusinessServiceException("SQL", "Cannot process your request. Please try again.");
        }
    }
    
    public void setBackOrderStatus(SiebelPropertySet inputs) throws SiebelBusinessServiceException{
        try {
            String Id = inputs.getProperty("Id");
            String warehouse = inputs.getProperty("Warehouse");
            SalesOrderItem s = new SalesOrderItem(SIEBEL_CONN);
            String getBC = inputs.getProperty("Type");
            if("PLXBackOrder".equals(getBC))
            {
                s = new PLXBackOrder(SIEBEL_CONN);
            }
            s.setSiebelAccountId(Id);
            s.setOrgId(DataConverter.toInt(warehouse));
            s.backOrder(EBS_CONN);
        } catch (SQLException ex) {
            MyLogging.log(Level.SEVERE, ex.getMessage());
            throw new SiebelBusinessServiceException("SIEBEL", ex.getMessage());
        } catch (SiebelException ex) {
            MyLogging.log(Level.SEVERE, ex.getMessage());
            throw new SiebelBusinessServiceException("SQL", "Cannot process your request. Please try again.");
        }
    }
    
    public void setOrderStatus(SiebelPropertySet inputs, SiebelPropertySet output) throws SiebelBusinessServiceException{
        EBSSqlData ebsdata = new EBSSqlData(EBS_CONN);
        Map<String, String> backorder = ebsdata.backOrder(
            inputs.getProperty("partnumber"), 
            DataConverter.toInt(inputs.getProperty("lotid")), 
            inputs.getProperty("ordernumber")
        );
        // set the output value
        for(Map.Entry<String, String> backorderItems : backorder.entrySet()){
            output.setProperty(backorderItems.getKey(), backorderItems.getValue());
        }
    }
    
    public Integer getOrganizationId(){
        return this.orgId;
    }
    
    public Integer getInventoryId(){
        return this.invId;
    }
}