package com.plexadasi.ebs.Helper;

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
    public static Integer toInt(String value) {
        System.out.println(value != (null));
        if (value != (null) && !"".equals(value)) {
            return Integer.parseInt(value);
        } else {
            return 0;
        }
    }
}
