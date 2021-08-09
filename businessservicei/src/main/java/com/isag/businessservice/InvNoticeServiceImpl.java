/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GeneralJournalDTO;
import com.isag.dto.GlAccountDTO;
import com.isag.dto.InstContractDetailDTO;
import com.isag.dto.InvNoticeDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InvNotice;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class InvNoticeServiceImpl implements InvNoticeService {

    @EJB
    GenericDAO dao;
    @EJB
    GenericService genericService;

    @Override
    public void deleteInvNotice(InvNotice invNotice) {
        dao.deleteEntity(invNotice);
    }

    @Override
    public List<InvNotice> getALLInvNotice() {
        return dao.findAll(InvNotice.class);
    }

    @Override
    public List<InvNotice> getALLInvNoticeByCompanyId(Integer Id) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", Id);
        List<InvNotice> invNoticeList = dao.findListByQuery("SELECT i FROM InvNotice i WHERE i.companyId.id = :companyId", params);
        return invNoticeList;
    }

    @Override
    public List<InvNotice> getALLInvNoticeByBranchIdAndType(Integer Id, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", Id);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId ");

        if (type != null) {
            if (type == 2) {
                query.append(" AND i.type >= :type order by i.id desc");
            } else {
                query.append(" AND i.type <= :type order by i.id desc");
            }
            params.put("type", type);
        }

        List<InvNotice> invNoticeList = dao.findListByQuery(query.toString(), params);
        return invNoticeList;
    }

    @Override
    public List<InvNoticeDTO> getALLInvNoticeByBranchIdAndTypeDTO(Integer Id, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", Id);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId ");

        if (type != null) {
            if (type == 2) {
                query.append(" AND i.type >= :type order by i.id desc");
            } else {
                query.append(" AND i.type <= :type order by i.id desc");
            }
            params.put("type", type);
        }

        List<InvNotice> invNoticeList = dao.findListByQuery(query.toString(), params);

        return returnDTOList(invNoticeList);
    }

    public List<InvNoticeDTO> returnDTOList(List<InvNotice> invNoticeList) {
        if (invNoticeList != null && !invNoticeList.isEmpty()) {
            List<InvNoticeDTO> list = new ArrayList<>();
            for (InvNotice invNotice : invNoticeList) {
                list.add(mapToDto(invNotice));
            }
            return list;
        }
        return null;
    }

    public InvNoticeDTO mapToDto(InvNotice entity) {
        InvNoticeDTO dTO = new InvNoticeDTO();
        dTO.setId(entity.getId());
        dTO.setSerial(entity.getSerial());

        if (entity.getContrastAccountId() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setId(entity.getContrastAccountId().getId());
            accountDTO.setAccNumber(entity.getContrastAccountId().getAccNumber());
            dTO.setContrastAccountId(accountDTO);
        }
        dTO.setDate(entity.getDate());
        if (entity.getGeneralJournalId() != null) {
            GeneralJournalDTO generalJournalDTO = new GeneralJournalDTO();
            generalJournalDTO.setId(entity.getGeneralJournalId().getId());
            dTO.setGeneralJournalId(generalJournalDTO);
        }
        if (entity.getInstallmentId() != null) {
            InstContractDetailDTO contractDetailDTO = new InstContractDetailDTO();
            contractDetailDTO.setId(entity.getInstallmentId().getId());
            dTO.setInstallmentId(contractDetailDTO);
            dTO.setInstContractSerial(entity.getInstallmentId().getInstContractId().getSerial());
        }
        if (entity.getNoticeNaming() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getNoticeNaming().getId());
            dTO.setNoticeNaming(symbolDTO);
        }

        dTO.setValue(entity.getValue());
        dTO.setRemark(entity.getRemark());

        if (entity.getOrganizationSiteId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(entity.getOrganizationSiteId().getId());
            invOrganizationSiteDTO.setName(entity.getOrganizationSiteId().getName());
            invOrganizationSiteDTO.setCode(entity.getOrganizationSiteId().getCode());
            dTO.setOrganizationSiteId(invOrganizationSiteDTO);
        }
        
        if (entity.getNoticeNaming() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getNoticeNaming().getId());
            symbolDTO.setName(entity.getNoticeNaming().getName());
            symbolDTO.setSerial(entity.getNoticeNaming().getSerial());
            dTO.setNoticeNaming(symbolDTO);
        }

        if (entity.getType() != null) {
            if (entity.getType() == 0 || entity.getType() == 2) {
                dTO.setTypeName("اشعار اضافة");
            } else if (entity.getType() == 1 || entity.getType() == 3) {
                dTO.setTypeName("اشعار خصم");
            }

            dTO.setType(entity.getType());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    @Override
    public InvNotice findInvNoticeByInvNoticeId(Integer invNoticeId) {
        return dao.findEntityById(InvNotice.class, invNoticeId);
    }

    @Override
    public Integer findMaxSerialNoByBranch() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(invNotice.id) FROM InvNotice invNotice");
        return maxId;
    }

    @Override
    public void updateInvNotice(InvNotice invNotice) {
        dao.updateEntity(invNotice);
    }

    @Override
    public InvNotice addInvNotice(InvNotice invNotice) {
        String error = null;
        try {
            if (invNotice.getId() == null) {
                invNotice.setSerial(getMaxGenericSerialByType(InvNotice.class, invNotice.getBranchId().getId(), invNotice.getType()));
                return dao.updateEntity(invNotice);
            } else {
                return dao.updateEntity(invNotice);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            error = "هذا الشيت تم ادخاله من قبل";
            invNotice.setMsg(error);
        }
        return invNotice;
    }

    public synchronized <T> Integer getMaxGenericSerialByType(Class<T> entityClass, Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT max(e.serial) FROM ").append(entityClass.getSimpleName()).append(" e WHERE e.branchId.id =:branchId");

        if (type != null) {
            if (type == 2 || type == 3) {
                query.append(" AND e.type = 2 OR e.type = 3");
            } else {
                query.append(" AND e.type = 0 OR e.type = 1");
            }
        }

        Integer serial = dao.findEntityByQuery(query.toString(), params);

        if (serial == null) {
            return 1;
        } else {
            return serial + 1;
        }
    }

    @Override
    public InvNotice updateInvNoticeForShifting(InvNotice invNotice) {
        InvNotice invNoticeReq = new InvNotice();

        invNoticeReq = dao.findEntityById(InvNotice.class, invNotice.getId());
        invNoticeReq.setPostFlag(invNotice.getPostFlag());
        if (invNoticeReq.getPostFlag().compareTo(1) == 0) {
            invNoticeReq.setGeneralJournalId(invNotice.getGeneralJournalId());
        } else {
            invNoticeReq.setGeneralJournalId(null);
        }

        return dao.updateEntity(invNoticeReq);
    }
    
    @Override
    public List<InvNoticeDTO> getInvNoticeListDTO(Integer branchId, Integer type,Date dateFrom,
            Date dateTo,Integer noticeFrom,Integer noticeTo) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId AND i.type = :type");
        params.put("branchId", branchId);
        params.put("type", type);
        if(dateFrom != null){
            params.put("dateFrom", dateFrom);
            query.append(" AND i.date >=:dateFrom ");
        }
        if(dateTo != null){
            params.put("dateTo", dateTo);
            query.append(" AND i.date <= :dateTo ");
        }
        if(noticeFrom != null){
            params.put("noticeFrom", noticeFrom);
            query.append(" AND i.serial >= :noticeFrom ");
        }
        if(noticeTo != null){
            params.put("noticeTo", noticeTo);
            query.append(" AND i.serial <= :noticeTo ");
        }

        List<InvNotice> invNoticeList = dao.findListByQuery(query.toString(), params);

        return returnDTOList(invNoticeList);
    }
}
