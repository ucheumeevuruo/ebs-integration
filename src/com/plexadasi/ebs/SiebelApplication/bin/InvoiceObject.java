/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

/**
 *
 * @author SAP Training
 */
public class InvoiceObject {
    private String trx_header;
    private String v_customer_id;
    private String cust_trx_type_id; 
    private String v_date;       
    private String v_pri_salesrepid;
    private String v_curr;
    private String v_termId;
    private String v_legalid;
    private String v_siebelquote;
    private String trx_dist_id_v;
    private String line_id;
    
    /**
     * 
     * @param value 
     */
    public void setTrxHeader(String value)
    {
       this.trx_header = value;
    }
    
    
    public void setBillToId(String value)
    {
        this.v_customer_id = value;
    }
    
    public void setCustomerTrxTypeId(String value)
    {
        this.cust_trx_type_id = value;
    }
    
    public void setTrxDate(String value)
    {
        this.v_date = value;
    }
    
    public void setPrimarySalesId(String value)
    {
        this.v_pri_salesrepid = value;
    }
    
    public void setTrxCurrency(String value)
    {
        this.v_curr = value;
    }
    
    public void setTermId(String value)
    {
        this.v_termId = value;
    }
    
    public void setLegalEntityId(String value)
    {
        this.v_legalid = value;
    }
    
    public void setCtRef(String value)
    {
        this.v_siebelquote = value;
    }
    
    public void setTrxDistId(String value)
    {
        this.v_siebelquote = value;
    }
    
    public void setCodeCombinationId(String value)
    {
        this.v_siebelquote = value;
    }
    
    public void setTrxLineId(String value)
    {
        this.line_id = value;
    }
    
    public String getTrxHeader()
    {
       return this.trx_header;
    }
    
    public String getBillToId()
    {
        return this.v_customer_id;
    }
    
    public String getCustomerTrxTypeId()
    {
        return this.cust_trx_type_id;
    }
    
    public String getTrxDate()
    {
        return this.v_date;
    }
    
    public String getPrimarySalesId()
    {
        return this.v_pri_salesrepid;
    }
    
    public String getTrxCurrency()
    {
        return this.v_curr;
    }
    
    public String getTermId()
    {
        return this.v_termId;
    }
    
    public String getLegalEntityId()
    {
        return this.v_legalid;
    }
    
    public String getCtRef()
    {
        return this.v_siebelquote;
    }
    
    public String getTrxDistId()
    {
        return this.trx_dist_id_v;
    }
    
    public String getCodeCombinationId()
    {
        return this.v_siebelquote;
    }

    public String geTrxLineId() 
    {
        return this.line_id;
    }
}
