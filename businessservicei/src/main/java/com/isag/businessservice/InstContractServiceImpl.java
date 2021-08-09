/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.InstallmentSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.InstContractDTO;
import com.isag.dto.InstContractDetailDTO;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvItemDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.InstContract;
import com.isag.entity.InstContractDetail;
import com.isag.views.InstInstallmentView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class InstContractServiceImpl implements InstContractService {

    @EJB
    private GenericDAO dao;
    @EJB
    InstContractDetailsService instContractDetailsService;
    @EJB
    instcontractdetailwithbanktransactionviewService instcontractdetailwithbanktransactionviewService;

    @Override
    public InstContract addInstContractforTasaheel(InstContract instContract, List<InstContractDetail> instContractDetailList) {
        if (instContract.getId() != null) {
            dao.updateEntity(instContract);
        } else {

//            instContract.setSerial(getMaxSerail(instContract) != null ? getMaxSerail(instContract) + 1 : 1);
            dao.saveEntity(instContract);
        }
        Integer maxSerial = 0;
        if (instContract != null && instContract.getId() != null) {
            maxSerial = instContractDetailsService.finMaxSerial(instContract.getId());
        } else {
            maxSerial++;
        }
        for (InstContractDetail instContractDetail : instContractDetailList) {
            instContractDetail.setInstContractId(instContract);

            if (instContractDetail.getId() != null) {
                instContractDetailsService.updateInstContractDetails(instContractDetail);
            } else {
                instContractDetail.setSerial(maxSerial);
                instContractDetailsService.addInstContractDetails(instContractDetail);
                maxSerial++;
            }
        }

        return instContract;
    }

    @Override
    public InstContract addInstContract(InstContract instContract, List<InstContractDetail> instContractDetailList, List<InstContractDetail> instContractDetailListDeleted) {

        if (instContract.getId() != null) {
            dao.updateEntity(instContract);
        } else {

            instContract.setSerial(getMaxSerail(instContract) != null ? getMaxSerail(instContract) + 1 : 1);
            dao.saveEntity(instContract);
        }

        if (instContractDetailListDeleted != null && !instContractDetailListDeleted.isEmpty()) {
            for (InstContractDetail deleted : instContractDetailListDeleted) {
                instContractDetailsService.deleteInstContractDetails(deleted);
            }
        }

        Integer maxSerial = 0;
        if (instContract != null && instContract.getId() != null) {
            maxSerial = instContractDetailsService.finMaxSerial(instContract.getId());
        } else {
            maxSerial++;
        }
        for (InstContractDetail instContractDetail : instContractDetailList) {
            instContractDetail.setInstContractId(instContract);

            if (instContractDetail.getId() != null) {
                instContractDetailsService.updateInstContractDetails(instContractDetail);
            } else {
                instContractDetail.setSerial(maxSerial);
                instContractDetailsService.addInstContractDetails(instContractDetail);
                maxSerial++;
            }
        }

        return instContract;
    }

    @Override
    public void deleteInstContract(InstContract instContract) {
        if (instContract.getId() != null && instContract.getId() > 0) {
            dao.executeDeleteQuery("delete from InstContractDetail e WHERE e.id > 0 AND e.instContractId.id = " + instContract.getId());
        }
        dao.deleteEntity(instContract);
    }

    @Override
    public List<InstContract> getALLInstContractByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(InstContract.class, companyId);
    }

    @Override
    public List<InstContract> getALLInstContractSettlmentByBranchId(Integer branchId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<InstContract> getALLInstContractRecievableByBranchId(Integer branchId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<InstContract> getALLInstContractTransferByBranchId(Integer branchId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InstContract findInstContractById(Integer instContractId) {
        return dao.findEntityById(InstContract.class, instContractId);
    }

    @Override
    public InstContract addOneInstContract(InstContract instContract, InstContractDetail instContractDetail) {
        if (instContract.getId() != null) {
            instContract = dao.updateEntity(instContract);
        } else {

            //  instContract.setSerial(getMaxSerail(instContract));
            dao.saveEntity(instContract);
        }

        //  hosCheckUpDetail.setHosCheckUpId(hosCheckUp);
        instContractDetailsService.updateInstContractDetails(instContractDetail);
        return instContract;
    }

    @Override
    public Integer getMaxSerail(InstContract instContract) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", instContract.getBranchId() != null ? instContract.getBranchId().getId() : null);
        return dao.findEntityByQuery(" SELECT MAX(e.serial) FROM InstContract e WHERE e.branchId.id = :branchId", params);
    }

    @Override
    public InstContract updateInstContract(InstContract instContract) {

        InstContract instContractUpdate = new InstContract();
        instContractUpdate = dao.findEntityById(InstContract.class, instContract.getId());
        return dao.updateEntity(instContractUpdate);
    }

    @Override
    public List<InstContract> getALLInstContractByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT e FROM InstContract e WHERE e.branchId.id = :branchId ORDER BY e.id DESC", params);
    }

    @Override
    public List<InstContract> getALLInstContractByBranchIdAndCustomerId(Integer branchId, Integer customerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("customerId", customerId);
        return dao.findListByQuery("SELECT e FROM InstContract e WHERE e.branchId.id = :branchId AND e.organizationSiteId.id = :customerId ", params);
    }

    @Override
    public Boolean CheckInstContractByBranchIdAndSerial(Integer branchId, Integer Serial) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("Serial", Serial);
        List<InstContract> contracts = dao.findListByQuery("SELECT e FROM InstContract e WHERE e.branchId.id = :branchId AND e.serial = :Serial ", params);
        if (contracts != null && !contracts.isEmpty() && contracts.get(0).getOrganizationSiteId() != null && contracts.get(0).getOrganizationSiteId().getId() > 0 && contracts.get(0).getOrganizationSiteId().getAccountId() != null && contracts.get(0).getOrganizationSiteId().getAccountId().getId() > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public InstContract findInstContractByBranchIdAndSerial(Integer branchId, Integer Serial) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("Serial", Serial);
        List<InstContract> contracts = dao.findListByQuery("SELECT e FROM InstContract e WHERE e.branchId.id = :branchId AND e.serial = :Serial ", params);
        if (contracts != null && !contracts.isEmpty() && contracts.get(0).getOrganizationSiteId() != null && contracts.get(0).getOrganizationSiteId().getId() > 0 && contracts.get(0).getOrganizationSiteId().getAccountId() != null && contracts.get(0).getOrganizationSiteId().getAccountId().getId() > 0) {
            return contracts.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<InstContractDTO> findInstContractByProject(InstallmentSearchBean installmentSearchBean) {
        if (installmentSearchBean != null && installmentSearchBean.getBranchId() != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("branchId", installmentSearchBean.getBranchId());
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT e FROM InstContract e WHERE e.branchId.id = :branchId ");
            if (installmentSearchBean.getProjectGuideFrom() != null && installmentSearchBean.getProjectGuideFrom().getCode() != null) {
                params.put("projectCodeFrom", installmentSearchBean.getProjectGuideFrom().getCode());
                builder.append(" AND e.projectId.code >= :projectCodeFrom ");
            }
            if (installmentSearchBean.getProjectGuideTo() != null && installmentSearchBean.getProjectGuideTo().getCode() != null) {
                params.put("projectCodeTo", installmentSearchBean.getProjectGuideTo().getCode());
                builder.append(" AND e.projectId.code <= :projectCodeTo ");
            }
            if (installmentSearchBean.getFromDate() != null) {
                params.put("FromDate", installmentSearchBean.getFromDate());
                builder.append(" AND e.date >= :FromDate ");
            }
            if (installmentSearchBean.getToDate() != null) {
                params.put("ToDate", installmentSearchBean.getToDate());
                builder.append(" AND e.date <= :ToDate ");
            }

            List<InstContract> contracts = dao.findListByQuery(builder.toString(), params);
            return returnListDTO(contracts, Boolean.FALSE);

        } else {
            return null;
        }
    }

    @Override
    public InstContract updateIntsContract(InstContract instContract
    ) {
        InstContract contract = new InstContract();

        contract = dao.findEntityById(InstContract.class, instContract.getId());
        contract.setPostFlag(instContract.getPostFlag());
        contract.setCancel(instContract.isCancel());
        if (instContract.getPostFlag().compareTo(1) == 0) {
            contract.setGeneralJournalId(instContract.getGeneralJournalId());
        } else {
            contract.setGeneralJournalId(null);
        }

        return dao.updateEntity(contract);
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters
    ) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(s) FROM InstInstallmentView s WHERE s.branchId = :branchId  ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }

    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        filters.put("branchId", branchId);
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and  ( s.serial  like CONCAT('%',:globalFilter ,'%') OR s.itemName  like CONCAT('%',:globalFilter ,'%') OR s.projectName like CONCAT('%',:globalFilter ,'%')  OR s.totalValue like CONCAT('%',:globalFilter ,'%') OR s.startInstallment like CONCAT('%',:globalFilter ,'%') OR s.date like CONCAT('%',:globalFilter ,'%') OR s.installmentNumber like CONCAT('%',:globalFilter ,'%') OR s.customerName  like CONCAT('%',:globalFilter ,'%') OR s.installmentValue like CONCAT('%',:globalFilter ,'%') OR  s.advancePayment like CONCAT('%',:globalFilter ,'%'))";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<InstInstallmentView> getALLInstContractTransferByBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {

        List<InstInstallmentView> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InstInstallmentView s WHERE s.branchId = :branchId   ");
        appendFilter(filters, branchId, builder);
        builder.append(" ORDER BY s.id DESC");
//        List<InstContract> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<InstInstallmentView> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        Map<Integer, Integer> mapContract = getInstContractId(branchId);
        for (InstInstallmentView detail : details) {
            if (mapContract.containsKey(detail.getId())) {
                detail.setDisabled(Boolean.TRUE);
            } else {
                detail.setDisabled(Boolean.FALSE);
            }
            result.add(detail);
        }
        return details;
    }

    @Override
    public Map<Integer, List<InstContractDetailDTO>> getALLInstContractByBranchIds(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        Map<Integer, List<InstContractDetailDTO>> instContractDTOMap = new HashMap<>();
        params.put("branchId", branchId);
        List<InstContractDetail> contractDetailList = dao.findListByQuery("SELECT e FROM InstContractDetail e WHERE e.branchId.id = :branchId AND e.id not in (select g.installmentId.id from GlBankTransaction g where g.branchId.id = :branchId and g.installmentId is not null ) ", params);
        for (InstContractDetail contractDetail : contractDetailList) {
            if (instContractDTOMap.containsKey(contractDetail.getInstContractId().getSerial())) {
                List<InstContractDetailDTO> detailList = instContractDTOMap.get(contractDetail.getInstContractId().getSerial());
                InstContractDetailDTO contractDetailDTO = initInstContractDetailDTO(contractDetail);
                detailList.add(contractDetailDTO);
            } else {
                List<InstContractDetailDTO> detailNew = new ArrayList<>();
                InstContractDetailDTO contractDetailDTO = initInstContractDetailDTO(contractDetail);
                detailNew.add(contractDetailDTO);
                instContractDTOMap.put(contractDetail.getInstContractId().getSerial(), detailNew);
            }
        }
        return instContractDTOMap;
    }

    private InstContractDetailDTO initInstContractDetailDTO(InstContractDetail contractDetail) {
        InstContractDetailDTO contractDetailDTO = new InstContractDetailDTO();
        contractDetailDTO.setId(contractDetail.getId());
        contractDetailDTO.setDueDate(contractDetail.getDueDate());
        contractDetailDTO.setInstContractId(contractDetail.getInstContractId().getId());
        contractDetailDTO.setSerial(contractDetail.getSerial());
        contractDetailDTO.setStatus(contractDetail.getStatus());
        contractDetailDTO.setOrganizationSiteId(contractDetail.getInstContractId().getOrganizationSiteId().getId());
        contractDetailDTO.setAccountId(contractDetail.getInstContractId().getOrganizationSiteId().getAccountId().getId());
        return contractDetailDTO;
    }

    private List<InstContractDTO> returnListDTO(List<InstContract> list, Boolean needDetail) {
        List<InstContractDTO> dTOList = new ArrayList<>();
        for (InstContract entity : list) {
            dTOList.add(mapTODTO(entity, needDetail));
        }
        return dTOList;
    }

    public InstContractDTO mapTODTO(InstContract entity, Boolean needDetail) {
        InstContractDTO dTO;
        if (needDetail) {
            dTO = initMapToDTO(entity, getInstContractId(entity.getBranchId().getId()));
        } else {
            dTO = initMapToDTO(entity, null);
        }

        return dTO;
    }

    private InstContractDTO initMapToDTO(InstContract entity, Map<Integer, Integer> InstContractId) {

        InstContractDTO dTO = new InstContractDTO();

        dTO.setId(entity.getId());
        dTO.setSerial(entity.getSerial());
        if (entity.getBankId() != null) {
            GlBankDTO bankDTO = new GlBankDTO();
            bankDTO.setId(entity.getBankId().getId());
            bankDTO.setName(entity.getBankId().getName());
            bankDTO.setCode(entity.getBankId().getCode());
            dTO.setGlBankDTO(bankDTO);
        }
        if (entity.getOrganizationSiteId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(entity.getOrganizationSiteId().getId());
            invOrganizationSiteDTO.setName(entity.getOrganizationSiteId().getName());
            invOrganizationSiteDTO.setCode(entity.getOrganizationSiteId().getCode());
            dTO.setInvOrganizationSiteDTO(invOrganizationSiteDTO);
        }
        if (entity.getProjectId() != null) {
            InstProjectGuideDTO instProjectGuideDTO = new InstProjectGuideDTO();
            instProjectGuideDTO.setId(entity.getProjectId().getId());
            instProjectGuideDTO.setName(entity.getProjectId().getName());
            instProjectGuideDTO.setCode(entity.getProjectId().getCode());
            dTO.setInstProjectGuideDTO(instProjectGuideDTO);
        }
        if (entity.getInvItemId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(entity.getInvItemId().getId());
            invItemDTO.setCode(entity.getInvItemId().getCode());
            invItemDTO.setName(entity.getInvItemId().getName());
            invItemDTO.setNameen(entity.getInvItemId().getNameen());
            dTO.setInvItemId(invItemDTO);
        }

        if (InstContractId != null) {
            if (InstContractId.containsKey(dTO.getId())) {
                dTO.setDisabled(Boolean.TRUE);
            } else {
                dTO.setDisabled(Boolean.FALSE);
            }
        }
        dTO.setDate(entity.getDate());
        dTO.setStartInstallment(entity.getStartInstallment());
        dTO.setAdvancePayment(entity.getAdvancePayment());
        dTO.setDeliveryPayment(entity.getDeliveryPayment());
        dTO.setInstallmentValue(entity.getInstallmentValue());
        dTO.setTotalValue(entity.getTotalValue());
        dTO.setDaysNumber(entity.getDaysNumber());
        dTO.setInstallmentNumber(entity.getInstallmentNumber());
        dTO.setAmountValue(entity.getAmount());
        dTO.setMurabahaValue(entity.getMurabahaRate());

//        if (entity.getAmount() != null && entity.getAmount().compareTo(BigDecimal.ZERO) == 1 && entity.getTotalValue() != null ) {
//            BigDecimal murabahaRateValue = entity.getMurabahaRate() != null ? entity.getMurabahaRate() : BigDecimal.ZERO;
//            BigDecimal totalPrice = entity.getTotalValue() .multiply(entity.getAmount());
//            BigDecimal murabahaValue = totalPrice.multiply(murabahaRateValue).divide(BigDecimal.valueOf(100));
//            BigDecimal totlValue = totalPrice.add(murabahaValue);
//            dTO.setFinalValue(totlValue);
//        }
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setCreationDate(entity.getCreationDate());
        dTO.setModificationDate(entity.getModificationDate());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setModifiedBy(entity.getModifiedBy() != null ? entity.getModifiedBy().getId() : null);
        dTO.setIndex(entity.getId());
        return dTO;
    }

    private Map<Integer, Integer> getInstContractId(Integer BranchId) {
        Map<Integer, Integer> map = new HashMap<>();

        List<Integer> InstContractId = instcontractdetailwithbanktransactionviewService.findInstContractDetailByInstContractId(BranchId);
        for (Integer instContractId : InstContractId) {
            map.put(instContractId, instContractId);
        }
        return map;
    }

    @Override
    public InstContract addInstContract(InstContract instContract) {

        return dao.updateEntity(instContract);

    }

    @Override
    public String findMaxCodeForInstContractByBranchId(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<String> detailses = dao.findListByQuery("SELECT max(e.code) FROM InstContract e WHERE e.branchId.id = :branchId AND (e.type = :type  AND e.active = 1) ORDER BY e.code", params);
        return detailses.get(0);
    }

}
