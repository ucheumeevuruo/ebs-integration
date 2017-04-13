/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.objects.Impl;

import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;

/**
 *
 * @author Adeyemi
 */
public interface Impl {
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException;
    
    public void doTrigger() throws SiebelBusinessServiceException;
  
    public void getExtraParam(SiebelBusComp sbBC);
}
