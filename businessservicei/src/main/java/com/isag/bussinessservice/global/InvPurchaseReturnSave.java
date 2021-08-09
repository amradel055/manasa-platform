/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.bussinessservice.global;

import com.isag.entity.InvAddingorder;
import com.isag.entity.InvAddingorderDetail;
import com.isag.entity.InvOpenningBalanceItem;
import com.isag.entity.InvOpenningBalanceItemDetail;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InvPurchaseOrder;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entity.InvQotation;
import com.isag.entity.InvQotationDetail;
import com.isag.entity.InvReservation;
import com.isag.entity.InvReservationDetail;
import com.isag.entity.InvReturnPurchase;
import com.isag.entity.InvReturnPurchaseDetail;
import com.isag.entity.InvStrip;
import com.isag.entity.InvStripDetail;
import com.isag.entity.InvTransfer;
import com.isag.entity.InvTransferDetail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amr
 */
public class InvPurchaseReturnSave  implements Serializable{

    /**
     * @return the invTransfer
     */
    public InvTransfer getInvTransfer() {
        return invTransfer;
    }

    /**
     * @param invTransfer the invTransfer to set
     */
    public void setInvTransfer(InvTransfer invTransfer) {
        this.invTransfer = invTransfer;
    }

    /**
     * @return the invTransferDetailList
     */
    public List<InvTransferDetail> getInvTransferDetailList() {
         if(invTransferDetailList== null){
            invTransferDetailList = new ArrayList<>();
        }
        return invTransferDetailList;
       
    }

    /**
     * @param invTransferDetailList the invTransferDetailList to set
     */
    public void setInvTransferDetailList(List<InvTransferDetail> invTransferDetailList) {
        this.invTransferDetailList = invTransferDetailList;
    }

    /**
     * @return the msgTransfer
     */
    public String getMsgTransfer() {
        return msgTransfer;
    }

    /**
     * @param msgTransfer the msgTransfer to set
     */
    public void setMsgTransfer(String msgTransfer) {
        this.msgTransfer = msgTransfer;
    }

    /**
     * @return the invPurchaseOrder
     */
    public InvPurchaseOrder getInvPurchaseOrder() {
        return invPurchaseOrder;
    }

    /**
     * @param invPurchaseOrder the invPurchaseOrder to set
     */
    public void setInvPurchaseOrder(InvPurchaseOrder invPurchaseOrder) {
        this.invPurchaseOrder = invPurchaseOrder;
    }

    /**
     * @return the invPurchaseOrderDetailList
     */
    public List<InvPurchaseOrderDetail> getInvPurchaseOrderDetailList() {
       
         if(invPurchaseOrderDetailList== null){
            invPurchaseOrderDetailList = new ArrayList<>();
        }
        return invPurchaseOrderDetailList;
    }

    /**
     * @param invPurchaseOrderDetailList the invPurchaseOrderDetailList to set
     */
    public void setInvPurchaseOrderDetailList(List<InvPurchaseOrderDetail> invPurchaseOrderDetailList) {
        this.invPurchaseOrderDetailList = invPurchaseOrderDetailList;
    }

    /**
     * @return the msgOrder
     */
    public String getMsgOrder() {
        return msgOrder;
    }

    /**
     * @param msgOrder the msgOrder to set
     */
    public void setMsgOrder(String msgOrder) {
        this.msgOrder = msgOrder;
    }
//--------------------------------------
    /**
     * @return the invPurchaseInvoice
     */
    public InvPurchaseInvoice getInvPurchaseInvoice() {
        return invPurchaseInvoice;
    }

    /**
     * @param invPurchaseInvoice the invPurchaseInvoice to set
     */
    public void setInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice) {
        this.invPurchaseInvoice = invPurchaseInvoice;
    }

    /**
     * @return the invPurchaseInvoiceDetailList
     */
    public List<InvPurchaseInvoiceDetail> getInvPurchaseInvoiceDetailList() {
        if(invPurchaseInvoiceDetailList== null){
            invPurchaseInvoiceDetailList = new ArrayList<>();
        }
        return invPurchaseInvoiceDetailList;
    }

    /**
     * @param invPurchaseInvoiceDetailList the invPurchaseInvoiceDetailList to set
     */
    public void setInvPurchaseInvoiceDetailList(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList) {
        this.invPurchaseInvoiceDetailList = invPurchaseInvoiceDetailList;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    private InvPurchaseInvoice invPurchaseInvoice;
    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList;
    private String msg;
//-----------------------------------------
  
    private InvPurchaseOrder invPurchaseOrder;
    private List<InvPurchaseOrderDetail> invPurchaseOrderDetailList;
    private String msgOrder;
//----------------------------------
     
    private InvAddingorder invAddingorder;
    private List<InvAddingorderDetail> invAddingorderDetailList;
    private String msgAdding;
 //-------------------------------------------
     private InvTransfer invTransfer;
    private List<InvTransferDetail> invTransferDetailList;
    private String msgTransfer;

    /**
     * @return the invAddingorder
     */
    public InvAddingorder getInvAddingorder() {
        return invAddingorder;
    }

    /**
     * @param invAddingorder the invAddingorder to set
     */
    public void setInvAddingorder(InvAddingorder invAddingorder) {
        this.invAddingorder = invAddingorder;
    }

    /**
     * @return the invAddingorderDetailList
     */
    public List<InvAddingorderDetail> getInvAddingorderDetailList() {
        
          if(invAddingorderDetailList== null){
            invAddingorderDetailList = new ArrayList<>();
        }
        return invAddingorderDetailList;
    }

    /**
     * @param invAddingorderDetailList the invAddingorderDetailList to set
     */
    public void setInvAddingorderDetailList(List<InvAddingorderDetail> invAddingorderDetailList) {
        this.invAddingorderDetailList = invAddingorderDetailList;
    }

    /**
     * @return the msgAdding
     */
    public String getMsgAdding() {
        return msgAdding;
    }

    /**
     * @param msgAdding the msgAdding to set
     */
    public void setMsgAdding(String msgAdding) {
        this.msgAdding = msgAdding;
    }
 //----------------------------------------------------    
     private InvReturnPurchase invReturnPurchase;
    private List<InvReturnPurchaseDetail> invReturnPurchaseDetailList;
    private String msgReturnPurchase;
   

    /**
     * @return the invReturnPurchase
     */
    public InvReturnPurchase getInvReturnPurchase() {
        return invReturnPurchase;
    }

    /**
     * @param invReturnPurchase the invReturnPurchase to set
     */
    public void setInvReturnPurchase(InvReturnPurchase invReturnPurchase) {
        this.invReturnPurchase = invReturnPurchase;
    }

    /**
     * @return the invReturnPurchaseDetailList
     */
    public List<InvReturnPurchaseDetail> getInvReturnPurchaseDetailList() {
         if(invReturnPurchaseDetailList== null){
            invReturnPurchaseDetailList = new ArrayList<>();
        }
        return invReturnPurchaseDetailList;
    }

    /**
     * @param invReturnPurchaseDetailList the invReturnPurchaseDetailList to set
     */
    public void setInvReturnPurchaseDetailList(List<InvReturnPurchaseDetail> invReturnPurchaseDetailList) {
        this.invReturnPurchaseDetailList = invReturnPurchaseDetailList;
    }

    /**
     * @return the msgReturnPurchase
     */
    public String getMsgReturnPurchase() {
        return msgReturnPurchase;
    }

    /**
     * @param msgReturnPurchase the msgReturnPurchase to set
     */
    public void setMsgReturnPurchase(String msgReturnPurchase) {
        this.msgReturnPurchase = msgReturnPurchase;
    }
     //------------------------------------------------------

     
    private InvQotation invQotation;
    private List<InvQotationDetail> invQotationDetailList;
    private String msgQotation;

    /**
     * @return the invQotation
     */
    public InvQotation getInvQotation() {
        return invQotation;
    }

    /**
     * @param invQotation the invQotation to set
     */
    public void setInvQotation(InvQotation invQotation) {
        this.invQotation = invQotation;
    }

    /**
     * @return the invQotationDetailList
     */
    public List<InvQotationDetail> getInvQotationDetailList() {
        if(invQotationDetailList== null){
            invQotationDetailList = new ArrayList<>();
        }
        return invQotationDetailList;
       
    }

    /**
     * @param invQotationDetailList the invQotationDetailList to set
     */
    public void setInvQotationDetailList(List<InvQotationDetail> invQotationDetailList) {
        this.invQotationDetailList = invQotationDetailList;
    }

    /**
     * @return the msgQotation
     */
    public String getMsgQotation() {
        return msgQotation;
    }

    /**
     * @param msgQotation the msgQotation to set
     */
    public void setMsgQotation(String msgQotation) {
        this.msgQotation = msgQotation;
    }
    //------------------------------------------------
      private InvReservation invReservation;
    private List<InvReservationDetail> invReservationDetailList;
    private String msgReservation;

    /**
     * @return the invReservation
     */
    public InvReservation getInvReservation() {
        return invReservation;
    }

    /**
     * @param invReservation the invReservation to set
     */
    public void setInvReservation(InvReservation invReservation) {
        this.invReservation = invReservation;
    }

    /**
     * @return the invReservationDetailList
     */
    public List<InvReservationDetail> getInvReservationDetailList() {
      if(invReservationDetailList== null){
            invReservationDetailList = new ArrayList<>();
        }
        return invReservationDetailList;
      
    }

    /**
     * @param invReservationDetailList the invReservationDetailList to set
     */
    public void setInvReservationDetailList(List<InvReservationDetail> invReservationDetailList) {
        this.invReservationDetailList = invReservationDetailList;
    }

    /**
     * @return the msgReservation
     */
    public String getMsgReservation() {
        return msgReservation;
    }

    /**
     * @param msgReservation the msgReservation to set
     */
    public void setMsgReservation(String msgReservation) {
        this.msgReservation = msgReservation;
    }
    
    //-----------------------------------
    private InvOpenningBalanceItem invOpenningBalanceItem;
    private List<InvOpenningBalanceItemDetail> invOpenningBalanceItemDetailList;
    private String msgOpeningItem;
    
    //-----------------------------------

    /**
     * @return the invOpenningBalanceItem
     */
    public InvOpenningBalanceItem getInvOpenningBalanceItem() {
        return invOpenningBalanceItem;
    }

    /**
     * @param invOpenningBalanceItem the invOpenningBalanceItem to set
     */
    public void setInvOpenningBalanceItem(InvOpenningBalanceItem invOpenningBalanceItem) {
        this.invOpenningBalanceItem = invOpenningBalanceItem;
    }

    /**
     * @return the invOpenningBalanceItemDetailList
     */
    public List<InvOpenningBalanceItemDetail> getInvOpenningBalanceItemDetailList() {
         if(invOpenningBalanceItemDetailList== null){
            invOpenningBalanceItemDetailList = new ArrayList<>();
        }
        return invOpenningBalanceItemDetailList;
      
    }

    /**
     * @param invOpenningBalanceItemDetailList the invOpenningBalanceItemDetailList to set
     */
    public void setInvOpenningBalanceItemDetailList(List<InvOpenningBalanceItemDetail> invOpenningBalanceItemDetailList) {
        this.invOpenningBalanceItemDetailList = invOpenningBalanceItemDetailList;
    }

    /**
     * @return the msgOpeningItem
     */
    public String getMsgOpeningItem() {
        return msgOpeningItem;
    }

    /**
     * @param msgOpeningItem the msgOpeningItem to set
     */
    public void setMsgOpeningItem(String msgOpeningItem) {
        this.msgOpeningItem = msgOpeningItem;
    }
    //-------------------------------------------------------
     private InvStrip invStrip;
    private List<InvStripDetail> invStripDetailList;
    private String msgStrip;

    /**
     * @return the invStrip
     */
    public InvStrip getInvStrip() {
        return invStrip;
    }

    /**
     * @param invStrip the invStrip to set
     */
    public void setInvStrip(InvStrip invStrip) {
        this.invStrip = invStrip;
    }

    /**
     * @return the invStripDetailList
     */
    public List<InvStripDetail> getInvStripDetailList() {
          if(invStripDetailList== null){
            invStripDetailList = new ArrayList<>();
        }
        return invStripDetailList;
   
    }

    /**
     * @param invStripDetailList the invStripDetailList to set
     */
    public void setInvStripDetailList(List<InvStripDetail> invStripDetailList) {
        this.invStripDetailList = invStripDetailList;
    }

    /**
     * @return the msgStrip
     */
    public String getMsgStrip() {
        return msgStrip;
    }

    /**
     * @param msgStrip the msgStrip to set
     */
    public void setMsgStrip(String msgStrip) {
        this.msgStrip = msgStrip;
    }
  
}
