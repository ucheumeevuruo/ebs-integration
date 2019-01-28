/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.build.objects;

import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlExtObj;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PurchaseOrder
extends ASqlExtObj {
    public PurchaseOrder(Connection ebs_conn, Product item) throws SiebelBusinessServiceException, IOException {
        super(ebs_conn, item);
        output = "";
    }

    @Override
    public final void firstCall() {
        output = output + "\nDECLARE\n";
        output = output + "PO_header_id_v       NUMBER;\n";
        output = output + "PO_line_id_v         NUMBER;\n";
        output = output + "PO_dist_id_v         NUMBER;\n";
        output = output + "v_quantity_ordered   NUMBER;\n";
        output = output + "v_batch_id           NUMBER;\n";
        output = output + "v_currency_code      NUMBER;\n";
        output = output + "v_agent_id           NUMBER;\n";
        output = output + "BEGIN\n";
    }

    @Override
    public void secondCall() {
        try {
            output = output + "INSERT INTO po.po_headers_interface(\n";
            output = output + "interface_header_id,\n";
            output = output + "batch_id,\n";
            output = output + "process_code,\n";
            output = output + "action,\n";
            output = output + "org_id,\n";
            output = output + "document_type_code,\n";
            output = output + "currency_code,\n";
            output = output + "agent_id,\n";
            output = output + "vendor_name,\n";
            output = output + "approval_status,\n";
            output = output + "vendor_id,\n";
            output = output + "vendor_site_code,\n";
            output = output + "ship_to_location,\n";
            output = output + "bill_to_location)\n";
            ArrayList<Map<String, String>> params = new ArrayList<Map<String, String>>();
            output = output + this.initializeProperties(params);
        }
        catch (SiebelBusinessServiceException ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
    }

    @Override
    public void thirdCall() {
        try {
            output = output + "INSERT INTO po.po_lines_interface(\n";
            output = output + "interface_line_id,\n";
            output = output + "interface_header_id,\n";
            output = output + "line_num,\n";
            output = output + "shipment_num,\n";
            output = output + "line_type,\n";
            output = output + "item,\n";
            output = output + "unit_of_measure,\n";
            output = output + "quantit,\n";
            output = output + "unit_price,\n";
            output = output + "ship_to_organization_code,\n";
            output = output + "ship_to_organization_id,\n";
            output = output + "ship_to_location_id,\n";
            output = output + "ship_to_location)\n";
            ArrayList<Map<String, String>> params = new ArrayList<Map<String, String>>();
            output = output + this.initializeProperties(params);
        }
        catch (SiebelBusinessServiceException ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
    }

    @Override
    public void fourthCall() {
        try {
            output = output + "INSERT INTO po.po_distributions_interface(\n";
            output = output + "interface_header_id,\n";
            output = output + "interface_line_id,\n";
            output = output + "interface_distribution_id,\n";
            output = output + "distribution_num,\n";
            output = output + "quantity_ordered)";
            ArrayList<Map<String, String>> params = new ArrayList<Map<String, String>>();
            output = output + this.initializeProperties(params);
        }
        catch (SiebelBusinessServiceException ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
    }

    @Override
    public final void lastCall() {
        output = output + "END;\n";
    }

    private String initializeProperties(List<Map<String, String>> list) throws SiebelBusinessServiceException {
        String fInvoice = "VALUES";
        for (int i = 0; i < list.size(); ++i) {
            Map<String, String> params = list.get(i);
            int length = params.size();
            fInvoice = fInvoice + "(\n";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (length > 5) {
                    throw new SiebelBusinessServiceException("", "");
                }
                fInvoice = fInvoice + entry.getValue() + ",\n";
            }
            fInvoice = fInvoice + ")\n";
        }
        return fInvoice;
    }
}

