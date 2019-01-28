/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  org.plexada.si.siebel.annotations.BusinessField
 *  org.plexada.si.siebel.annotations.Entity
 */
package com.plexadasi.siebel.model;

import com.plexadasi.siebel.IOBusinessComponent;
import com.plexadasi.siebel.Iinventory;
import org.plexada.si.siebel.annotations.BusinessField;
import org.plexada.si.siebel.annotations.Entity;

@IOBusinessComponent(businessComponent = "ATP Line Items")
@Entity(businessComponent = "Order Entry - Line Items", businessObject = "Order Entry (Sales)")
public class Sales
implements Iinventory {
    @BusinessField(value="Integration Id")
    private String integrationId;
    @BusinessField(value="Quantity")
    private Integer quantity;
    @BusinessField(value="Order Number")
    private String orderNumber;
    @BusinessField(value="Part Number")
    private String partNumber;
    @BusinessField(value="Product")
    private String partName;
    @BusinessField(value="Source Inventory Loc Integration Id")
    private Integer warehouseId;
    @BusinessField(value="Net Price")
    private Float amount;
    @BusinessField(value="Due Date")
    private String dueDate;
    private String lineType = "LINE";

    @Override
    public String getIntegrationId() {
        return this.integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    @Override
    public String getPartNumber() {
        return this.partNumber;
    }

    @Override
    public Integer getWarehouseId() {
        return this.warehouseId;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Override
    public Integer getQuantity() {
        return this.quantity;
    }

    @Override
    public String getOrderNumber() {
        return this.orderNumber;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Override
    public Float getAmount() {
        return this.amount;
    }

    @Override
    public String getLineType() {
        return this.lineType;
    }

    public String getPartName() {
        return this.partName;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    /**
     * @return the dueDate
     */
    @Override
    public String getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        String NEW_LINE = System.getProperty("line.separator");
        String print = "\t\t[Id]:=" + this.integrationId + NEW_LINE;
        print = print + "\t\t[Order Number]:=" + this.orderNumber + NEW_LINE;
        print = print + "\t\t[Part Number]:=" + this.partNumber + NEW_LINE;
        print = print + "\t\t[Part Name]:=" + this.partName + NEW_LINE;
        print = print + "\t\t[Warehouse]:=" + this.warehouseId + NEW_LINE;
        print = print + "\t\t[Onhand Quantity]:=" + this.quantity + NEW_LINE;
        print = print + "\t\t[Amount]:=" + this.amount + NEW_LINE;
        print = print + "\t\t[Due Date]:=" + this.dueDate + NEW_LINE;
        print = print + "\t\t[Line Type]:=" + this.lineType + NEW_LINE;
        return this.getClass().getSimpleName() + "\n[Details\n\t[\n" + print + "\t]\n";
    }
}

