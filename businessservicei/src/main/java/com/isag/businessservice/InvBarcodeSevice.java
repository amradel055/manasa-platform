/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.InvBarcode;
import com.isag.entity.InvItem;
import com.isag.entity.Symbol;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface InvBarcodeSevice {

    public void addInvBarcode(InvBarcode invBarcode);

    public InvBarcode updateInvBarcode(InvBarcode invBarcode);

    public void deleteInvBarcode(InvBarcode invBarcode);

    public InvBarcode findInvBarcode(Integer invBarcodeById);

    public List<InvBarcode> getInvBarcodeByInvItemId(Integer branchId, InvItem invItem);

    public Map<Integer, List<Symbol>> getAllUnitsForAllInvItemsByBranch(Integer branchId);
}
