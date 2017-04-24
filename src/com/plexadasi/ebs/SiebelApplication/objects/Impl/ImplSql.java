/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.objects.Impl;

import com.siebel.eai.SiebelBusinessServiceException;

/**
 *
 * @author SAP Training
 */
public interface ImplSql {
    public void firstCall();
    
    public void secondCall();
    
    public void thirdCall();
    
    public void thirdCall(Boolean trueOrFalse) throws SiebelBusinessServiceException;
    
    public void fourthCall();
    
    public void lastCall();
    
    public void setProperty(String K, String V);
    
    public String getOutput();
}
