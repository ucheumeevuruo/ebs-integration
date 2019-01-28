/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.ebs.SiebelApplication.lang.enu;

public class Columns {
    public static final String EMPTY_VALUE = "";

    public class Account {
        public static final String ACCOUNT_ID = "Id";
        public static final String EBS_ID = "EBS Id";
        public static final String STREET_ADDRESS = "Street Address";
        public static final String POSTAL_CODE = "Postal Code";
        public static final String CITY = "City";
        public static final String STATE = "State";
        public static final String COUNTRY_CODE = "PLX Country Code";
        public static final String CURRENCY_CODE = "Currency Code";
        public static final String ORANIZATION_ID = "Org Id";

        public class Shipping {
            public static final String SHIPPING_ADDRESS = "Primary Ship To Street Address";
            public static final String SHIPPING_STATE = "Primary Ship To State";
            public static final String SHIPPING_CITY = "Primary Ship To City";
            public static final String SHIPPING_COUNTRY = "Primary Ship To Country";
        }

        public class Billing {
            public static final String BILLING_ADDRESS = "Primary Bill To Street Address";
            public static final String BILLING_STATE = "Primary Bill To State";
            public static final String BILLING_CITY = "Primary Bill To City";
            public static final String BILLING_COUNTRY = "Primary Bill To Country";
        }

    }

    public class Order {
        public static final String PRODUCT = "Product";
        public static final String PART_NUMBER = "Part Number";
        public static final String QUANTITY = "Quantity";
        public static final String UNIT_PRICE = "Unit Price";
        public static final String NET_PRICE = "Net Price";
        public static final String WAREHOUSE_ID = "PLX Warehouse Id";
        public static final String WAREHOUSE_LINE_ID = "PLX Lot#";
        public static final String AGENT_ID = "PLX Agent Id";
        public static final String SUPPLIER_ID = "";
        public static final String ORDER_NUMBER = "Order Number";
        public static final String ORDER_ID = "Order Id";
        public static final String AVAILABLE_QUANTITY = "Available Quantity";
        public static final String PLX_ITEM_PRICE = "Item Price";
        public static final String DUE_DATE = "Requested Ship Date";
        public static final String PRODUCT_TYPE = "Product Type";
        public static final String SALES_PERSON = "PLX Primary Sales Rep Number";
        public static final String REFERENCE_NUMBER = "PLX Ref Number";
        public static final String TRANSACTION_TYPE = "PLX Transaction Type";
        public static final String PAYMENT_MODE = "PLX Payment Mode";
        public static final String INVOICE_NUMBER = "PLX Proforma Invoice Number";

        public class BackOrder {
            public static final String PICK_MEANING = "PLX Pick Meaning";
            public static final String STATUS = "Status";
            public static final String RELEASE_STATUS = "PLX Release Status";
            public static final String BACK_ORDER_QUANTITY = "Back Order Quantity";
        }

    }

    public static class Quote {
        public static final String ID = "Id";
        public static final String PRODUCT = "Product";
        public static final String PART_NUMBER = "Part Number";
        public static final String QUANTITY = "Quantity";
        public static final String UNIT_PRICE = "Unit Price";
        public static final String NET_PRICE = "Net Price";
        public static final String ORDER_NUMBER = "Quote Number";
        public static final String QUOTE_ID = "Quote Id";
        public static final String WAREHOUSE_ID = "Lot#";
        public static final String ASSET_NAME = "PLX Usage Asset Name";
        public static final String PRODUCT_TYPE = "Product Type";
    }

}

