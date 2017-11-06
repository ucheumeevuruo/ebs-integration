/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.SiebelServiceClone;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import static com.plexadasi.ebs.SiebelApplication.objects.Impl.Product.PLX_PRODUCT;
import static com.plexadasi.ebs.SiebelApplication.objects.Impl.Product.PLX_QUANTITY_AVAILABLE;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import com.siebel.data.SiebelPropertySet;

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
    
    
    
    /**
     * 
     * @throws com.siebel.eai.SiebelBusinessServiceException
     */
    @Override
    public void doTrigger() throws SiebelBusinessServiceException
    {
        MyLogging.log(Level.INFO, "Quote id is:= " +this.siebelAccountId);
        Product parts = new QParts(CONN);
        parts.setSiebelAccountId(this.siebelAccountId);
        parts.doTrigger();
        List<Map<String, String>> setLists = parts.getList();
        QLubricant lub = new QLubricant(CONN);
        lub.setSiebelAccountId(this.siebelAccountId);
        lub.doTrigger();
        setLists.addAll(lub.getList());
        QExpenses exp = new QExpenses(CONN);
        exp.setSiebelAccountId(this.siebelAccountId);
        exp.doTrigger();
        setLists.addAll(exp.getList());
        QServices serv = new QServices(CONN);
        serv.setSiebelAccountId(this.siebelAccountId);
        serv.doTrigger();
        setLists.addAll(serv.getList());
        QVehicle veh = new QVehicle(CONN);
        veh.setSiebelAccountId(this.getSiebelAccountId());
        veh.doTrigger();
        setLists.addAll(veh.getList());
        setList = new ArrayList<Map<String, String>>();
        setList = setLists;
        MyLogging.log(Level.INFO, "Creating Quote: " + setList.toString());
    }
    
    public void onHandQuantities(Connection ebsConn, String warehouse) throws SiebelBusinessServiceException
    {
        try {
            SiebelServiceClone s = new SiebelServiceClone(CONN);
            EBSSqlData ebs = new EBSSqlData(ebsConn);
            SiebelPropertySet values = CONN.newPropertySet();
            //set.setProperty(PLX_INVENTORY, PLX_INVENTORY);
            set.setProperty(PLX_PART_NUMBER, PLX_PART_NUMBER);
            //set.setProperty(PLX_LOT_ID, PLX_LOT_ID);
            set.setProperty(PLX_PRODUCT, PLX_PRODUCT);
            s.setSField(set);
            SiebelBusComp sbBC = s.fields(BUS_OBJ, BUS_COMP, this).getBusComp();
            boolean isRecord = sbBC.firstRecord();
            while (isRecord)
            {
                sbBC.getMultipleFieldValues(set, values);
                MyLogging.log(Level.INFO, String.valueOf(values));
                String partNumber = values.getProperty(PLX_PART_NUMBER);
                //Integer inventoryId = Integer.parseInt(values.getProperty(PLX_INVENTORY));
                //Integer warehouse = Integer.parseInt(values.getProperty(PLX_LOT_ID));
                Integer onHandQuantity = ebs.getOnHandQuantity(partNumber, Integer.parseInt(warehouse));
                MyLogging.log(Level.INFO, "On Hand Quantity: " + String.valueOf(onHandQuantity));
                sbBC.setFieldValue(PLX_QUANTITY_AVAILABLE, String.valueOf(onHandQuantity));
                isRecord = sbBC.nextRecord();
            }
            sbBC.writeRecord();
            s.release();
        } catch (SiebelException ex) {
            ex.printStackTrace(new PrintWriter(error));
            MyLogging.log(Level.SEVERE, "Caught Siebel Exception Line in doTrigger: " + error.toString());
            throw new SiebelBusinessServiceException("SiebelException", ex.getMessage()); 
        }
    }
    
    /**
     * 
     * @param sbBC
     * @throws SiebelException 
     */
    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException
    {
        sbBC.setSearchSpec("Quote Id", this.siebelAccountId); 
        sbBC.setSearchSpec("Product Type", "Equipment");
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {}
}