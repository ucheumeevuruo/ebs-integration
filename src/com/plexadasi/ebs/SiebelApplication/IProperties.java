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
public interface IProperties {

    /**
     *
     */
    public static final String NIX_INPUT_KEY  = "nix_waybill_template";
    public static final String WIN_INPUT_KEY  = "win_waybill_template";
    public static final String NIX_OUTPUT_KEY = "nix_generated_path";
    public static final String WIN_OUTPUT_KEY = "win_generated_path";
    public static final String EBS_USER_ID    = "ebsuserid";
    public static final String EBS_USER_RESP  = "ebsuserresp";
    public static final String EBS_APP_ID     = "ebsrespapplid";
    public static final String EBS_SOURCE_BATCH_ID  = "ebsbatchsourceid";
    public static final String EBS_LEGAL_ENTITY  = "ebslegaltrxid";
    public static final String PRICE_ID = "wst_price_id";
    public static final String LINE_TYPE = "line_type";
    public static final String TEMPLATE_NAME = "template_name";
    public static final String MASTER_ORG_CODE = "master_code";

    public IProperties setProperties(String prop);
    /**
     *
     * @param nix
     * @param win
     * @return
     */
    public IProperties setProperties(String nix, String win);

    /**
     *
     * @return
     * @throws com.siebel.eai.SiebelBusinessServiceException
     */
    public String getProperty() throws SiebelBusinessServiceException;
}
