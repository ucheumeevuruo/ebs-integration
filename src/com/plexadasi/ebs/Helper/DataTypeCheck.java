/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.Helper;

/**
 *
 * @author SAP Training
 */
public class DataTypeCheck {
    private static Boolean booleanOutput = false;
    
    public static Boolean isString(String data)
    {
        if(!isStringInt(data))
        {
            if (data instanceof String) 
            {
                booleanOutput = true;
            }
        }
        return booleanOutput;
    }
    
    public static boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } 
        catch (NumberFormatException ex)
        {
            return false;
        }
    }
    
    public static boolean isDoubleInt(double d)
    {
        //select a "tolerance range" for being an integer
        double TOLERANCE = 1E-5;
        //do not use (int)d, due to weird floating point conversions!
        booleanOutput = Math.abs(Math.floor(d) - d) < TOLERANCE;
        return booleanOutput;
    }
    
    public static boolean isObjectInteger(Object o)
    {
        booleanOutput = o instanceof Integer;
        return booleanOutput;
    }
}
