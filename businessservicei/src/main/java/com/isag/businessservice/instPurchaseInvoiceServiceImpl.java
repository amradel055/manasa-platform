/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GlAccountDTO;
import com.isag.dto.InstPurchaseInvoiceDTO;
import com.isag.dto.InstPurchaseInvoiceDetailDTO;
import com.isag.entity.GlAccount;
import com.isag.entity.InstPurchaseInvoice;
import com.isag.entity.IsagUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class instPurchaseInvoiceServiceImpl implements instPurchaseInvoiceService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;
    @EJB
    private InstPurchaseInvoiceDetailService instPurchaseInvoiceDetailService;

    @Override
    public InstPurchaseInvoiceDTO addInstPurchaseInvoice(InstPurchaseInvoiceDTO instPurchaseInvoiceDTO, List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOList, List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOListDelet, IsagUser isagUser) {
        InstPurchaseInvoice entity = mapFromDTO(instPurchaseInvoiceDTO, isagUser);

        if (entity.getId() == null) {
            Integer serial = genericService.getMaxSerialForInvoice(InstPurchaseInvoice.class, entity.getBranchId().getId(), null, null);
            entity.setSerial(serial);
        }
        entity = dao.updateEntity(entity);
        if (instPurchaseInvoiceDetailDTOListDelet != null) {
            for (InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTODelete : instPurchaseInvoiceDetailDTOListDelet) {
                if (instPurchaseInvoiceDetailDTODelete.getId() != null) {
                    instPurchaseInvoiceDetailService.deleteByInstPurchaseInvoiceDetailId(instPurchaseInvoiceDetailDTODelete.getId());
                }
            }
        }
        if (instPurchaseInvoiceDetailDTOList != null) {
            for (InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTO : instPurchaseInvoiceDetailDTOList) {
                if (entity.getId() != null) {
                    InstPurchaseInvoiceDTO instPurchaseInvoiceDTO1 = new InstPurchaseInvoiceDTO();
                    instPurchaseInvoiceDTO1.setId(entity.getId());
                    instPurchaseInvoiceDetailDTO.setInstPurchaseInvoice(instPurchaseInvoiceDTO1);
                    instPurchaseInvoiceDetailService.addInstPurchaseInvoiceDetail(instPurchaseInvoiceDetailDTO, isagUser);
                }
            }
        }
        instPurchaseInvoiceDTO = mapTODTO(entity, instPurchaseInvoiceDTO);
        return instPurchaseInvoiceDTO;
    }

    @Override
    public void deleteByInstPurchaseInvoiceId(Integer InstPurchaseInvoiceId) {
        //The delete method includes the 4 tables to be deleted based on the employee ID
        InstPurchaseInvoice entity = new InstPurchaseInvoice();
        entity.setId(InstPurchaseInvoiceId);
        dao.deleteEntity(entity);
    }

    @Override
    public InstPurchaseInvoiceDTO findbyInstPurchaseInvoiceId(Integer InstPurchaseInvoiceId) {
        InstPurchaseInvoice instPurchaseInvoice = dao.findEntityById(InstPurchaseInvoice.class, InstPurchaseInvoiceId);
        InstPurchaseInvoiceDTO dTO = new InstPurchaseInvoiceDTO();
        return mapTODTO(instPurchaseInvoice, dTO);
    }

    @Override
    public List<InstPurchaseInvoiceDTO> getAllInstPurchaseInvoiceByBranchId(Integer branchId) {

        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<InstPurchaseInvoiceDTO> dTOList = new ArrayList<>();
        List<InstPurchaseInvoice> list = dao.findListByQuery("SELECT i FROM InstPurchaseInvoice i WHERE i.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
      @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(i) FROM InstPurchaseInvoice i WHERE i.branchId.id = :branchId  ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }
    
    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        filters.put("branchId", branchId);
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and  (i.serial  like CONCAT('%',:globalFilter ,'%') OR (i.date like CONCAT('%',:globalFilter ,'%'))";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }
    
     @Override
    public List<InstPurchaseInvoiceDTO> getAllInstPurchaseInvoiceByBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT i FROM InstPurchaseInvoice i WHERE i.branchId.id = :branchId   ");
        appendFilter(filters, branchId, builder);
        builder.append(" ORDER BY i.id DESC");
        List<InstPurchaseInvoice> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
         List<InstPurchaseInvoiceDTO> dTOList = new ArrayList<>();
        returnListDTO(details, dTOList);
        return dTOList;
    }

    @Override
    public void deleteInstPurchaseInvoiceDTO(InstPurchaseInvoiceDTO instPurchaseInvoiceDTO, IsagUser isagUser) {
        //The delete method includes the 4 tables to be deleted based on the employee ID
        InstPurchaseInvoice entity = new InstPurchaseInvoice();
        entity.setId(instPurchaseInvoiceDTO.getId());
        instPurchaseInvoiceDetailService.deleteByInstPurchaseInvoiceId(instPurchaseInvoiceDTO.getId());
        dao.deleteEntity(entity);
    }

    private void returnListDTO(List<InstPurchaseInvoice> list, List<InstPurchaseInvoiceDTO> dTOList) {
        for (InstPurchaseInvoice entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

    private InstPurchaseInvoiceDTO mapTODTO(InstPurchaseInvoice entity, InstPurchaseInvoiceDTO dTO) {
        if (dTO == null) {
            dTO = new InstPurchaseInvoiceDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(InstPurchaseInvoiceDTO dTO, InstPurchaseInvoice entity) {

        dTO.setDate(entity.getDate());
        dTO.setSerial(entity.getSerial());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setId(entity.getId());
    }

    private InstPurchaseInvoice mapFromDTO(InstPurchaseInvoiceDTO dTO, IsagUser isagUser) {
        InstPurchaseInvoice entity = new InstPurchaseInvoice();
        entity.setSerial(dTO.getSerial());
        entity.setDate(dTO.getDate());

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());

        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

}
