/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvBarcodeDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvBarcode;
import com.toby.entity.InvItem;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        dao.saveEntity(invBarcode);
    }

    @Override
    public void addListInvBarcode(List<InvBarcodeDTO> invBarcodeList, TobyUser tobyUser,Integer invItemId) {
        InvBarcode entity;
        for (InvBarcodeDTO addBarcode : invBarcodeList) {
            entity = mapToEntity(addBarcode, tobyUser,invItemId);
            dao.updateEntity(entity);
        }     
    }

    @Override
    public void deleteListInvBarcode(List<InvBarcodeDTO> invBarcodeList) {
        InvBarcode ib;
        for (InvBarcodeDTO deletedBarcode : invBarcodeList) {
            ib = new InvBarcode();
            ib.setId(deletedBarcode.getId());
            deleteInvBarcode(ib);
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
    public List<InvBarcodeDTO> getInvBarcodeByInvItemId(Integer branchId, Integer invItemId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("invItemId", invItemId);
        params.put("branchId", branchId);
        List<InvBarcode> barcodes = dao.findListByQuery("SELECT e FROM InvBarcode e WHERE e.branchId.id  = :branchId AND e.itemId.id = :invItemId", params);
        List<InvBarcodeDTO> dtoList = mapToDTOList(barcodes, tobyUser);
        return dtoList;
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

    public InvBarcodeDTO mapToDTO(InvBarcode invBarcode, TobyUser tobyUser) {
        InvBarcodeDTO dTO = new InvBarcodeDTO();
        dTO.setCreatedBy(invBarcode.getCreatedBy().getId());
        dTO.setCreatedDate(invBarcode.getCreationDate());
        dTO.setBranchId(invBarcode.getBranchId().getId());
        dTO.setCompanyId(invBarcode.getCompanyId().getId());
        dTO.setId(invBarcode.getId());

        dTO.setCode(invBarcode.getCode());
        dTO.setCompanyId(tobyUser.getCompanyId().getId());
        dTO.setPrice1(invBarcode.getPrice1());
        dTO.setScrewing(invBarcode.getScrewing());

        dTO.setHeight(invBarcode.getHeight());
        dTO.setLength(invBarcode.getLength());
        dTO.setWeight(invBarcode.getWeight());
        dTO.setWidth(invBarcode.getWidth());

        if (invBarcode.getItemId() != null) {
            InvItemDTO itemDTO = new InvItemDTO();
            itemDTO.setId(invBarcode.getItemId().getId());
            itemDTO.setName(invBarcode.getItemId().getName());
            itemDTO.setCode(invBarcode.getItemId().getName());
            dTO.setItemId(itemDTO);
        }

        if (invBarcode.getTypeBarcode() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invBarcode.getTypeBarcode().getId());
            symbolDTO.setName(invBarcode.getTypeBarcode().getName());
            symbolDTO.setSerial(invBarcode.getTypeBarcode().getSerial());
            dTO.setTypeBarcode(symbolDTO);
        }

        if (invBarcode.getUnitId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invBarcode.getUnitId().getId());
            symbolDTO.setName(invBarcode.getUnitId().getName());
            symbolDTO.setSerial(invBarcode.getUnitId().getSerial());
            dTO.setUnitId(symbolDTO);
        }

        if (invBarcode.getPaintColor() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invBarcode.getPaintColor().getId());
            symbolDTO.setName(invBarcode.getPaintColor().getName());
            symbolDTO.setSerial(invBarcode.getPaintColor().getSerial());
            dTO.setPaintColor(symbolDTO);
        }

        return dTO;
    }

    public InvBarcode mapToEntity(InvBarcodeDTO invBarcodeDTO, TobyUser tobyUser,Integer invItemId) {

        InvBarcode invBarcode = new InvBarcode();

        invBarcode.setId(invBarcodeDTO.getId());
        InvItem item = new InvItem();
        item.setId(invItemId);
        invBarcode.setItemId(item);
        invBarcode.setBranchId(tobyUser.getBranchId());
        invBarcode.setCode(invBarcodeDTO.getCode());
        invBarcode.setCompanyId(tobyUser.getCompanyId());
        invBarcode.setPrice1(invBarcodeDTO.getPrice1());
        invBarcode.setScrewing(invBarcodeDTO.getScrewing());

        invBarcode.setHeight(invBarcodeDTO.getHeight());
        invBarcode.setLength(invBarcodeDTO.getLength());
        invBarcode.setWeight(invBarcodeDTO.getWeight());
        invBarcode.setWidth(invBarcodeDTO.getWidth());

        if (invBarcodeDTO.getTypeBarcode() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invBarcodeDTO.getTypeBarcode().getId());
            invBarcode.setTypeBarcode(symbol);
        }

        if (invBarcodeDTO.getPaintColor() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invBarcodeDTO.getPaintColor().getId());
            invBarcode.setPaintColor(symbol);
        }

        if (invBarcodeDTO.getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invBarcodeDTO.getUnitId().getId());
            symbol.setName(invBarcodeDTO.getUnitId().getName());
            invBarcode.setUnitId(symbol);
        }

        if (invBarcodeDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invBarcodeDTO.getCreatedBy());
            invBarcode.setCreatedBy(user);
            invBarcode.setCreationDate(invBarcodeDTO.getCreatedDate());
            invBarcode.setModifiedBy(tobyUser);
            invBarcode.setModificationDate(new Date());
            if (invBarcodeDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invBarcodeDTO.getCompanyId());
                invBarcode.setCompanyId(company);
            }

            if (invBarcodeDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invBarcodeDTO.getBranchId());
                invBarcode.setBranchId(branch);
            }
        } else {
            invBarcode.setCreatedBy(tobyUser);
            invBarcode.setCreationDate(new Date());
            invBarcode.setCompanyId(tobyUser.getCompanyId());
            invBarcode.setBranchId(tobyUser.getBranchId());
        }
        return invBarcode;
    }

    public List<InvBarcodeDTO> mapToDTOList(List<InvBarcode> invBarcodeList, TobyUser tobyUser) {
        List<InvBarcodeDTO> invBarcodeDTOList = new ArrayList<>();
        for (InvBarcode barcode : invBarcodeList) {
            invBarcodeDTOList.add(mapToDTO(barcode, tobyUser));
        }
        return invBarcodeDTOList;
    }

}
