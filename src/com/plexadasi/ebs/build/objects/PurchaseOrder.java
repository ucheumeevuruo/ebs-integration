package com.plexadasi.ebs.build.objects;


import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlExtObj;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlObj;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;





/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
public class PurchaseOrder extends ASqlExtObj
{
    
    /**
     *
     * @param ebs_conn
     * @param item
     * @throws com.siebel.eai.SiebelBusinessServiceException
     * @throws java.io.IOException
     */
    
    public PurchaseOrder (Connection ebs_conn, Product item) throws SiebelBusinessServiceException, IOException
    {
        super(ebs_conn, item);
        output = "";
    }
    
    /**
     *
     */
    @Override
    public final void firstCall()
    {
        output += "\nDECLARE" + NEXT_LINE;
        output += "PO_header_id_v       NUMBER" + NEXT_LINE_COL;
        output += "PO_line_id_v         NUMBER" + NEXT_LINE_COL;
        output += "PO_dist_id_v         NUMBER" + NEXT_LINE_COL;
        output += "v_quantity_ordered   NUMBER" + NEXT_LINE_COL;
	output += "v_batch_id           NUMBER" + NEXT_LINE_COL;
	output += "v_currency_code      NUMBER" + NEXT_LINE_COL;
	output += "v_agent_id           NUMBER" + NEXT_LINE_COL;
        output += "BEGIN" + NEXT_LINE;
    }
    
    @Override
    public void secondCall()
    {
        try {
            output += "INSERT INTO po.po_headers_interface(" + NEXT_LINE;
            output += "interface_header_id" + NEXT_LINE_COMMA;
            output += "batch_id" + NEXT_LINE_COMMA;
            output += "process_code" + NEXT_LINE_COMMA;
            output += "action" + NEXT_LINE_COMMA;
            output += "org_id" + NEXT_LINE_COMMA;
            output += "document_type_code" + NEXT_LINE_COMMA;
            output += "currency_code" + NEXT_LINE_COMMA;
            output += "agent_id" + NEXT_LINE_COMMA;
            output += "vendor_name" + NEXT_LINE_COMMA;
            output += "approval_status" + NEXT_LINE_COMMA;
            output += "vendor_id" + NEXT_LINE_COMMA;
            output += "vendor_site_code" + NEXT_LINE_COMMA;
            output += "ship_to_location" + NEXT_LINE_COMMA;
            output += "bill_to_location)" + NEXT_LINE;
            List<Map<String, String>> params = new ArrayList<Map<String, String>>();
            output += initializeProperties(params);
        } catch (SiebelBusinessServiceException ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void thirdCall() 
    {
        try {
            output += "INSERT INTO po.po_lines_interface(" + NEXT_LINE;
            output += "interface_line_id" + NEXT_LINE_COMMA;
            output += "interface_header_id" + NEXT_LINE_COMMA;
            output += "line_num" + NEXT_LINE_COMMA;
            output += "shipment_num" + NEXT_LINE_COMMA;
            output += "line_type" + NEXT_LINE_COMMA;
            output += "item" + NEXT_LINE_COMMA;
            output += "unit_of_measure" + NEXT_LINE_COMMA;
            output += "quantit" + NEXT_LINE_COMMA;
            output += "unit_price" + NEXT_LINE_COMMA;
            output += "ship_to_organization_code" + NEXT_LINE_COMMA;
            output += "ship_to_organization_id" + NEXT_LINE_COMMA;
            output += "ship_to_location_id" + NEXT_LINE_COMMA;
            output += "ship_to_location)" + NEXT_LINE;
            List<Map<String, String>> params = new ArrayList<Map<String, String>>();
            output += initializeProperties(params);
        } catch (SiebelBusinessServiceException ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void fourthCall() 
    {
        try 
        {
            output += "INSERT INTO po.po_distributions_interface(" + NEXT_LINE;
            output += "interface_header_id" + NEXT_LINE_COMMA;
            output += "interface_line_id" + NEXT_LINE_COMMA;
            output += "interface_distribution_id" + NEXT_LINE_COMMA;
            output += "distribution_num" + NEXT_LINE_COMMA;
            output += "quantity_ordered)";
            List<Map<String, String>> params = new ArrayList<Map<String, String>>();
            output += initializeProperties(params);
        } 
        catch (SiebelBusinessServiceException ex) 
        {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *
     */
    @Override
    public final void lastCall()
    {
        output += END;
    }
    
    private String initializeProperties(List<Map<String, String>> list) throws SiebelBusinessServiceException
    {
        String fInvoice = "VALUES"; 
        
        for(int i = 0; i < list.size(); i++)
        {
            Map<String, String> params = list.get(i);
            int length = params.size();
            fInvoice += "(" + NEXT_LINE;
            for(Map.Entry<String, String> entry : params.entrySet())
            {
                if(length > 5)
                {
                    throw new SiebelBusinessServiceException("", "");
                }
                else
                {
                    fInvoice += entry.getValue() + NEXT_LINE_COMMA;
                }
            }
            fInvoice += ")" + NEXT_LINE;
        }
        return fInvoice;
    }
}
