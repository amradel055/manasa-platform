/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.print;

import com.toby.dto.InvInvoiceDTO;
import com.toby.toby.UserData;

/**
 *
 * @author hq004
 */
public interface IReportService {

    public void printReportInvoice(InvInvoiceDTO invPurchaseInvoiceDTO, UserData userData);

 }
