package com.plexadasi.Helper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
public class DataConverter {
    private static Integer number;
    
    public static Integer toInt(String value) {
        try{
            number = Integer.parseInt(value);
        }catch(NumberFormatException ex){
            number = 0;
        }
        return number;
    }  
    
    public static String nullToString(String value){
        if(value == null){
            return "";
        }
        return value;
    }
}
