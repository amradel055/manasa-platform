/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.InvBarcode;
import com.isag.entity.InvItem;
import com.isag.entity.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class InvBarcodeServiceImp implements InvBarcodeSevice {

    @EJB
    private GenericDAO dao;

    @Override
    public void addInvBarcode(InvBarcode invBarcode) {
        InvBarcode barcode = dao.updateEntity(invBarcode);
        if(barcode.getCode() == null || barcode.getCode().isEmpty()){
            barcode.setCode(barcode.getId().toString());
            dao.updateEntity(barcode);
        }
    }

    @Override
    public InvBarcode updateInvBarcode(InvBarcode invBarcode) {
        return dao.updateEntity(invBarcode);
    }

    @Override
    public void deleteInvBarcode(InvBarcode invBarcode) {
        dao.deleteEntity(invBarcode);
    }

    @Override
    public InvBarcode findInvBarcode(Integer invBarcodeById) {
        return dao.findEntityById(InvBarcode.class, invBarcodeById);
    }

    @Override
    public List<InvBarcode> getInvBarcodeByInvItemId(Integer branchId, InvItem invItem) {
        Map<String, Object> params = new HashMap<>();
        params.put("invItemId", invItem.getId());
        params.put("branchId", branchId);
        List<InvBarcode> barcodes = dao.findListByQuery("SELECT e FROM InvBarcode e WHERE e.branchId.id  = :branchId AND e.itemId.id = :invItemId", params);
        return barcodes;
    }

    @Override
    public Map<Integer, List<Symbol>> getAllUnitsForAllInvItemsByBranch(Integer branchId) {
        Map<Integer, List<Symbol>> unitMap = new HashMap<>();
        List<Symbol> units = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvBarcode> barcodes = dao.findListByQuery("SELECT e FROM InvBarcode e WHERE e.branchId.id  = :branchId ", params);

        for (InvBarcode barcode : barcodes) {
            units = new ArrayList<>();

            if (unitMap.containsKey(barcode.getItemId().getId())) {
                units = unitMap.get(barcode.getItemId().getId());
                units.add(barcode.getUnitId());
            } else {
                units.add(barcode.getUnitId());
            }

            unitMap.put(barcode.getItemId().getId(), units);
        }

        return unitMap;
    }
}
