/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.SiebelServiceClone;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import static com.plexadasi.ebs.SiebelApplication.objects.Impl.Product.FIELD_QUANTITY;
import static com.plexadasi.ebs.SiebelApplication.objects.Impl.Product.PLX_INVENTORY;
import static com.plexadasi.ebs.SiebelApplication.objects.Impl.Product.PLX_LOT_ID;
import static com.plexadasi.ebs.SiebelApplication.objects.Impl.Product.PLX_NET_PRICE;
import static com.plexadasi.ebs.SiebelApplication.objects.Impl.Product.PLX_PART_NUMBER;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import com.siebel.data.SiebelPropertySet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author SAP Training
 */
public class Quote extends Product implements Impl
{
    public Quote(SiebelDataBean CONN)
    {
        super(CONN);
    }
    
    /**
     * 
     */
    private static final String BUS_OBJ = "Quote";
    
    /**
     * 
     */
    private static final String BUS_COMP = "Quote Item";
    
    // A dirty way of getting the header
    // Was a quick fix. Didn't have time to think about this implementation
    // Barca - Chelsea match
    public Map<String, String> getQuoteHeader(SiebelPropertySet property) throws SiebelException{
        SiebelServiceClone s = new SiebelServiceClone(CONN);
        SiebelPropertySet values = CONN.newPropertySet();
        s.setSField(property);
        SiebelBusComp sbBC = s.fields(BUS_OBJ, "Quote", this).getBusComp();
        Map<String, String> items = new HashMap();
        boolean isRecord = sbBC.firstRecord();
        while(isRecord){
            sbBC.getMultipleFieldValues(property, values);
            final Enumeration<String> e = property.getPropertyNames();
            while (e.hasMoreElements()){
                String value = e.nextElement();
                items.put(property.getProperty(value), values.getProperty(value));
            }
            isRecord = sbBC.nextRecord();
        }
        return items;
    }
    
    public List<Inventory> getInventories(Connection ebsConn) throws SiebelBusinessServiceException
    {
        SiebelServiceClone s = new SiebelServiceClone(CONN);
        
        List<Inventory> inventory = new ArrayList();
        
        //Modifications to get the lot item id from the header
        SiebelPropertySet property = CONN.newPropertySet();
        property.setProperty(Product.PLX_WAREHOUSE_ID, Product.PLX_WAREHOUSE_ID);
        try{
            Map<String, String> fields = this.getQuoteHeader(property);
            
            
            SiebelPropertySet values = CONN.newPropertySet();
            set.setProperty(PLX_PART_NUMBER, PLX_PART_NUMBER);
            set.setProperty(FIELD_QUANTITY, FIELD_QUANTITY);
            set.setProperty(PLX_INVENTORY, PLX_INVENTORY);
            set.setProperty(PLX_NET_PRICE, PLX_NET_PRICE);
            set.setProperty(PLX_LOT_ID, PLX_LOT_ID);
            s.setSField(set);
            SiebelBusComp sbBC = s.fields(BUS_OBJ, BUS_COMP, this).getBusComp();
            boolean isRecord = sbBC.firstRecord();
            while (isRecord)
            {
                sbBC.getMultipleFieldValues(set, values);
                Inventory inventories = new Inventory();
                inventories.setPart_number(values.getProperty(PLX_PART_NUMBER));
                //inventories.setOrg_id(Integer.parseInt(values.getProperty(PLX_LOT_ID)));
                inventories.setOrg_id(Integer.parseInt(fields.get(Product.PLX_WAREHOUSE_ID)));
                inventories.setQuantity(Integer.parseInt(values.getProperty(FIELD_QUANTITY)));
                inventories.setAmount(Float.parseFloat(values.getProperty(PLX_NET_PRICE)));
                inventories.setLine_type("LINE");
                inventory.add(inventories);
                isRecord = sbBC.nextRecord();
            }
        }catch (SiebelException ex) {
            ex.printStackTrace(new PrintWriter(error));
            MyLogging.log(Level.SEVERE, "Caught Siebel Exception Line in doTrigger: " + error.toString());
            throw new SiebelBusinessServiceException("SiebelException", ex.getMessage()); 
        }
        finally{
            s.release();
        }
        return inventory;
    }
    
    /**
     * 
     * @param sbBC
     * @throws SiebelException 
     */
    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException
    {
        sbBC.setSearchSpec("Quote Number", this.siebelAccountId);
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {}

    @Override
    public void doTrigger() throws SiebelBusinessServiceException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}