/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.eai.SiebelBusinessServiceException
 *  oracle.sql.ARRAY
 *  oracle.sql.ArrayDescriptor
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.helper.HelperAP;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.PurchaseOrder;
import com.plexadasi.order.PurchaseOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

public class POInventory1 {
    private PurchaseOrderInventory poOrder = null;
    private SiebelDataBean siebConn = null;
    private Connection ebsConn = null;
    private final String sqlName = "PRODUCT";
    private String[][] stringArray;
    private int length = 0;
    private final int maxLength = 12;

    public POInventory1(SiebelDataBean sb, Connection ebs, PurchaseOrderInventory PO) {
        this.siebConn = sb;
        this.ebsConn = ebs;
        this.poOrder = PO;
    }

    public Array getInventoryItem() throws SiebelBusinessServiceException, SQLException {
        PurchaseOrder po = new PurchaseOrder(this.siebConn);
        po.setSiebelAccountId(this.poOrder.getOrderNumber());
        po.doTrigger();
        ArrayDescriptor desc = ArrayDescriptor.createDescriptor((String)"PRODUCT", (Connection)this.ebsConn);
        this.length = po.getList().size();
        this.stringArray = new String[this.length][12];
        EBSSqlData ebsData = new EBSSqlData(this.ebsConn);
        for (int i = 0; i < this.length; ++i) {
            Map<String, String> map = po.getList().get(i);
            String[] org = ebsData.getOrgCode(Integer.parseInt(map.get("PLX Lot#")));
            this.stringArray[i][0] = map.get("Line Number");
            this.stringArray[i][1] = map.get("Shipment Number");
            this.stringArray[i][2] = HelperAP.getLineType();
            this.stringArray[i][3] = map.get("Part Number");
            this.stringArray[i][4] = "Each";
            this.stringArray[i][5] = map.get("Quantity");
            this.stringArray[i][6] = map.get("Unit Price");
            this.stringArray[i][7] = org[0];
            this.stringArray[i][8] = org[1];
            this.stringArray[i][9] = org[2];
            this.stringArray[i][10] = this.poOrder.getPromiseDate();
            this.stringArray[i][11] = map.get("Quantity");
            MyLogging.log(Level.INFO, "POInventory" + Arrays.toString(this.stringArray[i]));
        }
        return new ARRAY(desc, this.ebsConn, (Object)this.stringArray);
    }
}

