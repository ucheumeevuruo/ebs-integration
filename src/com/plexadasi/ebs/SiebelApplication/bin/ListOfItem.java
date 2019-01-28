/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelBusComp
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.connect.siebel.SiebelConnect;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.SiebelServiceClone;
import com.plexadasi.ebs.SiebelApplication.lang.enu.Columns;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.plexadasi.ebs.model.Order;
import com.plexadasi.ebs.services.SalesOrderService;
import com.plexadasi.helper.DataConverter;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelBusObject;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class ListOfItem
extends SalesOrderItem {
    private static final String BUS_OBJ = "Quote";
    private static final String BUS_COMP = "Quote Item";
    Order quote;

    public ListOfItem(SiebelDataBean CONN) {
        super(CONN);
        this.busComp = "Quote Item";
        this.busObj = "Quote";
    }

    public void Create() throws SiebelBusinessServiceException, SiebelException, SQLException {
        SiebelServiceClone s = new SiebelServiceClone(CONN);
        SiebelBusObject sbo = CONN.getBusObject("List Of Values");
        SiebelBusComp sbc = sbo.getBusComp("List Of Values");
//        sbc.setViewMode(3);
        
//        SiebelPropertySet values = CONN.newPropertySet();
//        this.set.setProperty("Name", "Name");
//        this.set.setProperty("Type", "Type");
//        this.set.setProperty("Value", "Value");
//        s.setSField(this.set);
//        SiebelBusComp sbBC = s.fields(this.busObj, this.busComp, this).getBusComp();
        for(int i = 3; i <= 100; i++){
//            if(i != 5 || i != 10 || i != 15 || i != 20 || i != 25 || i != 30 || i != 31){
                MyLogging.log(Level.INFO, String.valueOf(i));
                sbc.newRecord(true);
                sbc.setFieldValue("Order By", String.valueOf(i));
                sbc.setFieldValue("Name", String.valueOf(i));
                sbc.setFieldValue("Type", "DISCNT_PERCENT");
                sbc.setFieldValue("Value", String.valueOf(i));
//            }
        }
        sbc.writeRecord();
    }
    
    public static void main(String[] args) throws SiebelBusinessServiceException, SiebelException, SQLException {
        ListOfItem loi = new ListOfItem(SiebelConnect.connectSiebelServer());
        loi.Create();
    }

    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException {
        sbBC.setSearchSpec("Quote Id", this.siebelAccountId);
        sbBC.setSearchSpec("Product Type", "Equipment");
        //sbBC.setSearchSpec("Lot#", String.valueOf(this.soldToOrgId));
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {
    }

    @Override
    public void doTrigger() throws SiebelBusinessServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

