/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication;

import com.siebel.eai.SiebelBusinessServiceException;

/**
 *
 * @author Adeyemi
 */
public interface PropertiesImpl {

    /**
     *
     * @param prop
     */
    public void setProperty(String prop);

    /**
     *
     * @return
     */
    public String getProperty();
}
