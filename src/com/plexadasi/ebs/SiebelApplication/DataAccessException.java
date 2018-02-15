/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication;

/**
 *
 * @author SAP Training
 */
public class DataAccessException extends Exception{
    public DataAccessException(String string){
        super();
    }
    
    public String getExceptionMessage()
    {
        return this.getMessage();
    }
}
