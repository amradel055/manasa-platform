/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvBarcodeDTO;
import com.toby.entity.InvBarcode;
import com.toby.entity.InvItem;
import com.toby.entity.Symbol;
import com.toby.entity.TobyUser;
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
    
    public void addListInvBarcode(List<InvBarcodeDTO> invBarcodeList, TobyUser tobyUser,Integer invItemId);
    
    public void deleteListInvBarcode(List<InvBarcodeDTO> invBarcodeList) ;

    public InvBarcode updateInvBarcode(InvBarcode invBarcode);

    public void deleteInvBarcode(InvBarcode invBarcode);

    public InvBarcode findInvBarcode(Integer invBarcodeById);
    
    public List<InvBarcodeDTO> getInvBarcodeByInvItemId(Integer branchId, Integer invItemId, TobyUser tobyUser);

    public List<InvBarcode> getInvBarcodeByInvItemId(Integer branchId, InvItem invItem);

    public Map<Integer, List<Symbol>> getAllUnitsForAllInvItemsByBranch(Integer branchId);
}
