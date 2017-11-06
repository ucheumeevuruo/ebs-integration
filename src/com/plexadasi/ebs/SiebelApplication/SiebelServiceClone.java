package com.plexadasi.ebs.SiebelApplication;


import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Adeyemi
 */
public class SiebelServiceClone extends SiebelService 
{ 
    
    public SiebelServiceClone(SiebelDataBean conn) {
        super(conn);
    }
    
    public SiebelServiceClone fields(String bO, String bC, Impl qM) throws SiebelException
    {
        this.sbBO = sdb.getBusObject(bO); 
        this.sbBC = this.sbBO.getBusComp(bC);
        values = sdb.newPropertySet();
        this.sbBC.setViewMode(3);
        this.sbBC.clearToQuery();
        // Activate all the fields
        this.sbBC.activateMultipleFields(properties);
        //Get search specification
        qM.searchSpec(sbBC);
        this.sbBC.executeQuery2(true, true);
        return this;
    }
    
    public SiebelBusComp getBusComp()
    {
        return this.sbBC;
    }
    
    public void release()
    {
        this.sbBC.release();
        this.sbBO.release();
    }
}
