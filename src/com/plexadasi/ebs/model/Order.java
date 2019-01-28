/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.ebs.model;

public class Order {
    private Integer id;
    private Integer quantity;
    private String orderNumber;
    private String backOfficeNumber;
    private Integer onhandQuantity;
    private String partNumber;
    private String partName;
    private Integer warehouseId;
    private String processCode;
    private String status;

    public String getPartNumber() {
        return this.partNumber;
    }

    public Integer getWarehouseId() {
        return this.warehouseId;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public Integer getOnhandQuantity() {
        return this.onhandQuantity;
    }

    public String getPartName() {
        return this.partName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setOnhandQuantity(Integer onhandQuantity) {
        this.onhandQuantity = onhandQuantity;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public BackOrder getBackOrder() {
        return new BackOrder();
    }

    public String getProcessCode() {
        return this.processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the backOfficeNumber
     */
    public String getBackOfficeNumber() {
        return backOfficeNumber;
    }

    /**
     * @param backOfficeNumber the backOfficeNumber to set
     */
    public void setBackOfficeNumber(String backOfficeNumber) {
        this.backOfficeNumber = backOfficeNumber;
    }

    public String toString() {
        String NEW_LINE = System.getProperty("line.separator");
        String print = "Number:" + this.orderNumber + NEW_LINE;
        print = print + "Id:" + this.id + NEW_LINE;
        print = print + "OHQ:" + this.onhandQuantity + NEW_LINE;
        print = print + "Part Number:" + this.partNumber + NEW_LINE;
        print = print + "Part Name:" + this.partName + NEW_LINE;
        print = print + "Warehouse:" + this.warehouseId + NEW_LINE;
        print = print + "Onhand Quantity:" + this.quantity + NEW_LINE;
        print = print + "Process Code:" + this.processCode + NEW_LINE;
        print = print + "Status:" + this.status + NEW_LINE;
        return this.getClass().getSimpleName() + print;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public class BackOrder {
        private String pickMeaning;
        private String itemStatus;
        private String releaseStatus;
        private Integer quantity;

        public String getPickMeaning() {
            return this.pickMeaning;
        }

        public void setPickMeaning(String pickMeaning) {
            this.pickMeaning = pickMeaning;
        }

        public String getItemStatus() {
            return this.itemStatus;
        }

        public void setItemStatus(String itemStatus) {
            this.itemStatus = itemStatus;
        }

        public String getReleaseStatus() {
            return this.releaseStatus;
        }

        public void setReleaseStatus(String releaseStatus) {
            this.releaseStatus = releaseStatus;
        }

        public Integer getQuantity() {
            return this.quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String toString() {
            String NEW_LINE = System.getProperty("line.separator");
            String print = "Pick Meaning:" + this.pickMeaning + NEW_LINE;
            print = print + "Item Status:" + this.itemStatus + NEW_LINE;
            print = print + "Release Status:" + this.releaseStatus + NEW_LINE;
            print = print + "Quantity:" + this.quantity + NEW_LINE;
            return print;
        }
    }

}

