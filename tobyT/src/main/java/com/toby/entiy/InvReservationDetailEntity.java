package com.toby.entiy;

import com.toby.entity.InvReservation;
import com.toby.views.ItemsBarcodesDetailsView;

import java.math.BigDecimal;

public class InvReservationDetailEntity extends BaseEntity {
    private InvReservation reservation;
    private Integer item;
    private String itemName;
    private String itemCode;
    private String unit;
    private BigDecimal price ;
    private BigDecimal amount ;
    private BigDecimal total ;
    private BigDecimal adding ;
    private BigDecimal discount ;
    private BigDecimal net ;
    private BigDecimal screwing;
    private ItemsBarcodesDetailsView itemsBarcodesDetail;
    private ItemsBarcodesDetailsView itemsBarcodesDetailTrans;
    

    public InvReservationDetailEntity() {
        setCounter(getCounter() + 1);
    }

    public InvReservation getReservation() {
        return reservation;
    }

    public void setReservation(InvReservation reservation) {
        this.reservation = reservation;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getAdding() {
        return adding;
    }

    public void setAdding(BigDecimal adding) {
        this.adding = adding;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ItemsBarcodesDetailsView getItemsBarcodesDetail() {
        return itemsBarcodesDetail;
    }

    public void setItemsBarcodesDetail(ItemsBarcodesDetailsView itemsBarcodesDetail) {
        this.itemsBarcodesDetail = itemsBarcodesDetail;
    }

    public ItemsBarcodesDetailsView getItemsBarcodesDetailTrans() {
        return itemsBarcodesDetailTrans;
    }

    public void setItemsBarcodesDetailTrans(ItemsBarcodesDetailsView itemsBarcodesDetailTrans) {
        this.itemsBarcodesDetailTrans = itemsBarcodesDetailTrans;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }
}
