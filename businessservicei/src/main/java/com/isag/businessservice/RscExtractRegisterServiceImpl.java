/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscExtractAccuralDeductionDTO;
import com.isag.dto.RscExtractRegisterDTO;
import com.isag.dto.RscExtractRegisterDetailDTO;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscRestrictionQuantitiesDTO;
import com.isag.dto.RscRestrictionQuantitiesDetailDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscTenderRscAccrualDeductionDTO;
import com.isag.entity.InstProjectGuide;
import com.isag.entity.InvInventory;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagUser;
import com.isag.entity.RscExtractRegister;
import com.isag.entity.RscExtractRegisterDetail;
import com.isag.entity.RscRestrictionQuantities;
import com.isag.entity.RscTender;
import com.isag.entity.RscTenderDetailWorkUnit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class RscExtractRegisterServiceImpl implements RscExtractRegisterService {

    @EJB
    private GenericDAO dao;
    @EJB
    private RscExtractRegisterDetailService rscExtractRegisterDetailService;
    @EJB
    private RscTenderDetailWorkUnitService rscTenderDetailWorkUnitService;
    @EJB
    private RscTenderRscAccrualDeductionService rscTenderRscAccrualDeductionService;
    @EJB
    private RscExtractAccrualDeductionService rscAccrualDeductionService;
    @EJB
    private GenericService genericService;
    @EJB
    private RscRestrictionQuantitiesDetailService rscRestrictionQuantitiesDetailService;
    @EJB
    private RscQotationPriceTenderDetailWorkUnitService rscQotationPriceTenderDetailWorkUnitService;
    @EJB
    private RscRestrictionQuantitiesService restrictionQuantitiesService;

    @Override
    public List<RscExtractRegisterDTO> getBranchRscExtractRegisterActiveByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscExtractRegisterDTO> dTOList = new ArrayList<>();
        List<RscExtractRegister> list = dao.findListByQuery("SELECT acc FROM RscExtractRegister acc WHERE acc.branchId.id=:branchId order by acc.serial DESC", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscExtractRegisterDTO findRscExtractRegister(Integer extractId, String uri) {
        if (extractId != null) {
            RscExtractRegister extract = dao.findEntityById(RscExtractRegister.class, extractId);
            RscExtractRegisterDTO extractRegisterDTO = mapTODTO(extract, null, true);
            if (uri.contains("rscExtractSummary") || uri.contains("rscExtractInv")) {
                return updateExtractSummary(extractRegisterDTO, false);
            }
            return updateSummary(extractRegisterDTO, false);
        } else {
            return null;
        }
    }

    @Override
    public RscExtractRegisterDTO addRscExtractRegister(RscExtractRegisterDTO extractDTO,
            List<RscExtractRegisterDetailDTO> rscExtractRegisterDetailDTODeletedList,
            List<RscExtractAccuralDeductionDTO> rscExtractAccuralDTODeletedList,
            List<RscExtractAccuralDeductionDTO> rscExtractDeductionDTODeletedList, IsagUser isagUser) {

        if (validateSave(extractDTO)) {

            RscExtractRegister extract = mapFromDTO(extractDTO, isagUser);

            if (extract.getId() == null) {
                Integer serial = genericService.getMaxGenericSerialByType(RscExtractRegister.class, extract.getBranchId().getId(), null);
                extract.setSerial(serial);
            }
            RscExtractRegister extractRegisterPrevious = getAllRscExtractRegisterClosed(extractDTO.getRestrictionId() != null && extractDTO.getRestrictionId().getContractId() != null ? extractDTO.getRestrictionId().getId() : null);
            if (extractRegisterPrevious != null) {
                extractRegisterPrevious.setClosed(Boolean.TRUE);
                dao.updateEntity(extractRegisterPrevious);
            }

            //------------------------
            extract = dao.updateEntity(extract);
            //------------------------
            
            if (extractDTO.getId() == null) {
                for (RscRestrictionQuantitiesDTO restrictionQuantitiesDTO : extractDTO.getRestrictionIdList()) {
                    restrictionQuantitiesService.updateRscRestrictionQuantities(restrictionQuantitiesDTO.getId(), extract.getId());
                }
            }

            if (extractDTO.getRscExtractRegisterDetailDTOList() != null && !extractDTO.getRscExtractRegisterDetailDTOList().isEmpty()) {
                rscExtractRegisterDetailService.addExtractRegisterDetailList(extractDTO.getRscExtractRegisterDetailDTOList(), extract, isagUser);
            }
            if (rscExtractRegisterDetailDTODeletedList != null && !rscExtractRegisterDetailDTODeletedList.isEmpty()) {
                rscExtractRegisterDetailService.deleteExtractRegisterDetailList(rscExtractRegisterDetailDTODeletedList);
            }

            //------------------------------------------------------
            if (extractDTO.getRscExtractAccuralList() != null && !extractDTO.getRscExtractAccuralList().isEmpty()) {
                rscAccrualDeductionService.addExtractAccuralDeductionList(extractDTO.getRscExtractAccuralList(), extract, isagUser);
            }
            if (rscExtractAccuralDTODeletedList != null && !rscExtractAccuralDTODeletedList.isEmpty()) {
                rscAccrualDeductionService.deleteExtractAccuralDeductionList(rscExtractAccuralDTODeletedList, isagUser);
            }
            if (extractDTO.getRscExtractDeductionList() != null && !extractDTO.getRscExtractDeductionList().isEmpty()) {
                rscAccrualDeductionService.addExtractAccuralDeductionList(extractDTO.getRscExtractDeductionList(), extract, isagUser);
            }
            if (rscExtractDeductionDTODeletedList != null && !rscExtractDeductionDTODeletedList.isEmpty()) {
                rscAccrualDeductionService.deleteExtractAccuralDeductionList(rscExtractDeductionDTODeletedList, isagUser);
            }
            //--------------------------------------------------------
            extractDTO = mapTODTO(extract, extractDTO, true);
        }
        return extractDTO;

    }

    private Boolean validateSave(RscExtractRegisterDTO extractDTO) {

        BigDecimal oldNetValue = getTotalNetFromOldExtraction(extractDTO.getTenderId() != null ? extractDTO.getTenderId().getId() : null);
        BigDecimal totalNet = BigDecimal.ZERO;
        for (RscExtractRegisterDetailDTO detailDTO : extractDTO.getRscExtractRegisterDetailDTOList()) {
            totalNet = totalNet.add((detailDTO.getNetCost() != null ? detailDTO.getNetCost() : BigDecimal.ZERO).add(oldNetValue != null ? oldNetValue : BigDecimal.ZERO));
        }
        if (extractDTO.getRestrictionId() != null && extractDTO.getRestrictionId().getContractId() != null && (totalNet).compareTo(extractDTO.getRestrictionId().getContractId().getContractTotal()) == 1) {
            extractDTO.setMsg("عفوا لا يمكن حفظ هذا المستخلص قيمة اجمالي المستخلصات اكبر من قيمة العقد");
            dao.rollbackQuery();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public void deleteRscExtractRegister(RscExtractRegisterDTO extractDTO, IsagUser isagUser) {
        RscExtractRegister extract = new RscExtractRegister();
        extract.setId(extractDTO.getId());
        List<RscExtractRegisterDetailDTO> extractDetailDTOlist = rscExtractRegisterDetailService.getPriceExtractRegisterDetailByExtractId(extractDTO.getId());

        rscExtractRegisterDetailService.deleteExtractRegisterDetailList(extractDetailDTOlist);

        List<RscExtractAccuralDeductionDTO> rscExtractAccuralList = rscAccrualDeductionService.getRscExtractAccuralOrDeductionByExtractForDelete(extractDTO.getId());

        rscAccrualDeductionService.deleteExtractAccuralDeductionList(rscExtractAccuralList, isagUser);

        List<RscExtractAccuralDeductionDTO> rscExtractDeductionList = rscAccrualDeductionService.getRscExtractAccuralOrDeductionByExtractForDelete(extractDTO.getId());

        rscAccrualDeductionService.deleteExtractAccuralDeductionList(rscExtractDeductionList, isagUser);

        dao.deleteEntity(extract);

        RscExtractRegister extractRegisterPrevious = getAllRscExtractRegisterOpend(extractDTO.getRestrictionId() != null && extractDTO.getRestrictionId().getContractId() != null ? extractDTO.getRestrictionId().getContractId().getId() : null);
        if (extractRegisterPrevious != null) {
            extractRegisterPrevious.setClosed(Boolean.FALSE);
            dao.updateEntity(extractRegisterPrevious);
        }
    }

    @Override
    public List<RscExtractRegisterDTO> getAllRscExtractRegisterByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscExtractRegisterDTO> dTOList = new ArrayList<>();
        List<RscExtractRegister> list = dao.findListByQuery("SELECT acc FROM RscExtractRegister acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public RscExtractRegisterDTO getAllRscExtractRegisterById(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);

        RscExtractRegisterDTO dTO = new RscExtractRegisterDTO();
        List<RscExtractRegister> list = dao.findListByQuery("SELECT acc FROM RscExtractRegister acc WHERE acc.id = :id ", params);
        return mapTODTO(list.get(0), dTO, Boolean.TRUE);
    }

    public RscExtractRegister getAllRscExtractRegisterClosed(Integer contractId) {
        Map<String, Object> params = new HashMap();

        params.put("contractId", contractId);

        List<RscExtractRegister> list = dao.findListByQuery("SELECT acc FROM RscExtractRegister acc WHERE acc.restrictionId.contractId.id = :contractId and acc.closed = false", params);
        if (list == null || list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public RscExtractRegister getAllRscExtractRegisterOpend(Integer contractId) {
        Map<String, Object> params = new HashMap();

        params.put("contractId", contractId);

        List<RscExtractRegister> list = dao.findListByQuery("SELECT acc FROM RscExtractRegister acc WHERE acc.restrictionId.contractId.id = :contractId and acc.closed = true order by acc.id desc", params);
        if (list == null || list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public List<RscExtractRegisterDTO> getAllRscExtractRegisterByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);
        params.put("selectedId", selectedId);
        String sql = "SELECT acc FROM RscExtractRegister acc WHERE acc.id != :selectedId and acc.branchId.id = :id AND acc.serial = :serial ";
        List<RscExtractRegisterDTO> dTOList = new ArrayList<>();
        List<RscExtractRegister> list = dao.findListByQuery(sql, params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscExtractRegisterDTO mapTODTO(RscExtractRegister extract, RscExtractRegisterDTO dTO, Boolean check) {
        if (dTO == null) {
            dTO = new RscExtractRegisterDTO();
        }
        initMapToDTO(dTO, extract);
        if (check) {
            dTO.setRscExtractRegisterDetailDTOList(rscExtractRegisterDetailService.getPriceExtractRegisterDetailByExtractId(extract.getId()));
            dTO.setRscExtractAccuralList(rscAccrualDeductionService.getRscExtractAccuralOrDeductionByBranchAndTypeAndExtract(extract.getId(), 2));
            dTO.setRscExtractDeductionList(rscAccrualDeductionService.getRscExtractAccuralOrDeductionByBranchAndTypeAndExtract(extract.getId(), 3));
            // dTO.setRscExtractAccrualDeletedList(rscAccrualDeductionService.);
        }

        return dTO;
    }

    public BigDecimal getTotalNetFromOldExtraction(Integer tenderId) {
        Map<String, Object> params = new HashMap();
        params.put("tenderId", tenderId);
        String query = " select ifnull((SELECT sum(value_after_discount) FROM isag.rsc_extract_register_detail rerd\n"
                + " left join rsc_extract_register rer on rer.id = rerd.rsc_extract_register\n"
                + " where tender_id = " + tenderId
                + " group by  tender_id),0) +\n"
                + " ifnull((SELECT sum(rsc_accural_deduction_value) FROM isag.rsc_extract_accural_deduction  rea\n"
                + " left join rsc_extract_register rer on rer.id = rea.rsc_extract_register_id\n"
                + " left join rsc_accrual_deduction rad on rad.id = rea.rsc_accrual_deduction_id\n"
                + " where rad.type = 2 and tender_id = " + tenderId
                + " group by  tender_id),0) - ifnull((\n"
                + " SELECT sum(rsc_accural_deduction_value) FROM isag.rsc_extract_accural_deduction  rea\n"
                + " left join rsc_extract_register rer on rer.id = rea.rsc_extract_register_id\n"
                + " left join rsc_accrual_deduction rad on rad.id = rea.rsc_accrual_deduction_id\n"
                + " where rad.type = 2 and tender_id = " + tenderId
                + " group by  tender_id),0) as value";
        List<BigDecimal> list = dao.executeNativeQuery(query);
        BigDecimal netExtract = BigDecimal.ZERO;
        if (list != null && !list.isEmpty()) {
            if (list.get(0) != null) {
                netExtract = list.get(0);
            }
        }

        return netExtract;
    }

    public BigDecimal getTotalNetCostFromExtraction(Integer branchId, Integer contractId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("contractId", contractId);

        List<BigDecimal> list = dao.findListByQuery("SELECT sum(acc.netCost) FROM RscExtractRegisterDetail acc WHERE acc.rscExtractRegister.contractId.id=:contractId AND acc.branchId.id=:branchId", params);
        BigDecimal netExtract = BigDecimal.ZERO;
        if (list != null && !list.isEmpty()) {
            if (list.get(0) != null) {
                netExtract = list.get(0);
            }
        }

        return netExtract;
    }

    public BigDecimal getTotalNetCostFrom(Integer branchId, Integer tenderId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("tenderId", tenderId);
        BigDecimal netExtract = BigDecimal.ZERO;
        netExtract = dao.findEntityByQuery("SELECT sum(acc.netCost) FROM RscExtractRegisterDetail acc WHERE acc.rscExtractRegister.tenderId.id=:tenderId AND acc.branchId.id=:branchId", params);
        return netExtract;
    }

    @Override
    public BigDecimal getTotalNetCost(Integer branchId, Integer rscExtractRegisterId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("rscExtractRegisterId", rscExtractRegisterId);
        StringBuilder builder = new StringBuilder();
        BigDecimal netExtract = BigDecimal.ZERO;
        builder.append("SELECT (COALESCE(sum(acc.netCost),0) + ");
        builder.append("(SELECT COALESCE(sum(rea.rscAccuralDeductionValue),0) FROM RscExtractAccuralDeduction rea  WHERE rea.rscAccrualDeductionId.type = 2  AND rea.rscExtractRegisterId.id =:rscExtractRegisterId)");
        builder.append(" ) - (SELECT COALESCE(sum(red.rscAccuralDeductionValue),0) FROM RscExtractAccuralDeduction red  WHERE red.rscAccrualDeductionId.type = 3  AND red.rscExtractRegisterId.id =:rscExtractRegisterId)");
        builder.append("FROM RscExtractRegisterDetail acc Where  acc.rscExtractRegister.id=:rscExtractRegisterId AND acc.branchId.id=:branchId");
        netExtract = dao.findEntityByQuery(builder.toString(), params);
        return netExtract;
    }

    @Override
    public RscExtractRegister updaterscExtractRegister(RscExtractRegister rscExtractRegister) {
        RscExtractRegister extractRegister = new RscExtractRegister();

        extractRegister = dao.findEntityById(RscExtractRegister.class, rscExtractRegister.getId());
        extractRegister.setPostFlag(rscExtractRegister.getPostFlag());
        if (rscExtractRegister.getPostFlag().compareTo(1) == 0) {
            extractRegister.setGeneralJournalId(rscExtractRegister.getGeneralJournalId());
        } else {
            extractRegister.setGeneralJournalId(null);
        }

        return dao.updateEntity(extractRegister);
    }

    private void initMapToDTO(RscExtractRegisterDTO dTO, RscExtractRegister extract) {

        if (extract.getRestrictionId() != null && extract.getRestrictionId().getId() != null) {
            RscRestrictionQuantitiesDTO restrictionQuantitiesDTO = new RscRestrictionQuantitiesDTO();
            restrictionQuantitiesDTO.setId(extract.getRestrictionId().getId());
            restrictionQuantitiesDTO.setSerial(extract.getRestrictionId().getSerial());
            if (extract.getRestrictionId() != null && extract.getRestrictionId().getContractId() != null && extract.getRestrictionId().getContractId().getId() != null) {
                RscContractRegisterDTO contractRegisterDTO = new RscContractRegisterDTO();
                contractRegisterDTO.setId(extract.getRestrictionId().getContractId().getId());
                contractRegisterDTO.setSerial(extract.getRestrictionId().getContractId().getSerial());
                contractRegisterDTO.setStatus(extract.getRestrictionId().getContractId().getStatus());
                contractRegisterDTO.setDocumentNumber(extract.getRestrictionId().getContractId().getDocumentNumber());
                contractRegisterDTO.setContractTotal(extract.getRestrictionId().getContractId().getContractTotal());

                if (extract.getRestrictionId() != null && extract.getRestrictionId().getContractId() != null && extract.getRestrictionId().getContractId().getSiteId() != null && extract.getRestrictionId().getContractId().getSiteId().getId() != null) {
                    InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
                    siteDTO.setId(extract.getRestrictionId().getContractId().getSiteId().getId());
                    siteDTO.setCode(extract.getRestrictionId().getContractId().getSiteId().getCode());
                    siteDTO.setName(extract.getRestrictionId().getContractId().getSiteId().getName());
                    contractRegisterDTO.setSiteId(siteDTO);
                }

                //-----------------------------
                if (extract.getRestrictionId().getContractId().getRscQotationPriceTenderId() != null && extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getId() != null) {
                    RscQotationPriceTenderDTO qotationDTO = new RscQotationPriceTenderDTO();
                    qotationDTO.setId(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getId());
                    qotationDTO.setDocNumber(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getDocNumber());
                    qotationDTO.setDate(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getDate());
                    qotationDTO.setSerial(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getSerial());
                    qotationDTO.setStatus(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getStatus());
                    //-------
                    if (qotationDTO.getStatus() == null || qotationDTO.getStatus() == 0) {
                        qotationDTO.setQotationStatus("لم يتم");
                    } else if (qotationDTO.getStatus() == 1) {
                        qotationDTO.setQotationStatus("تم");
                    }
                    //--------
                    qotationDTO.setRemark(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRemark());
                    if (extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getCustomerId() != null && extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getCustomerId().getId() != null) {
                        InvOrganizationSiteDTO orgSiteDTO = new InvOrganizationSiteDTO();
                        orgSiteDTO.setId(extract.getRestrictionId().getContractId().getSiteId() != null ? extract.getRestrictionId().getContractId().getSiteId().getId() : null);
                        orgSiteDTO.setCode(extract.getRestrictionId().getContractId().getSiteId() != null ? extract.getRestrictionId().getContractId().getSiteId().getCode() : null);
                        orgSiteDTO.setName(extract.getRestrictionId().getContractId().getSiteId() != null ? extract.getRestrictionId().getContractId().getSiteId().getName() : null);
                        dTO.setSiteId(orgSiteDTO);
                    }
                    if (extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId() != null && extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getId() != null) {
                        RscPriceTenderDTO priceTenderDTO = new RscPriceTenderDTO();
                        priceTenderDTO.setId(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getId());
                        priceTenderDTO.setDate(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getDate());
                        priceTenderDTO.setDocNumber(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getDocNumber());
                        priceTenderDTO.setSerial(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getSerial());
                        priceTenderDTO.setStatus(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getStatus());
                        if (extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId() != null && extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getId() != null) {
                            InvOrganizationSiteDTO tenderorgDTO = new InvOrganizationSiteDTO();
                            tenderorgDTO.setId(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getId());
                            tenderorgDTO.setCode(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getCode());
                            tenderorgDTO.setName(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getName());
                            priceTenderDTO.setCustomerId(tenderorgDTO);
                        }
                        if (extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId() != null && extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getId() != null) {
                            RscTenderDTO tenderDTO = new RscTenderDTO();
                            tenderDTO.setId(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getId());
                            tenderDTO.setDocNumber(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getDocNumber());
                            tenderDTO.setDate(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getDate());
                            tenderDTO.setSerial(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getSerial());

                            tenderDTO.setRemark(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getRemark());
                            if (extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId() != null && extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getId() != null) {
                                InvOrganizationSiteDTO tendersiteDTO = new InvOrganizationSiteDTO();
                                tendersiteDTO.setId(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getId());
                                tendersiteDTO.setCode(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getCode());
                                tendersiteDTO.setName(extract.getRestrictionId().getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getName());
                                tenderDTO.setCustomerId(tendersiteDTO);
                            }
                            dTO.setTenderId(tenderDTO);
                        }
                    }

                }
                //-----------------------------
                restrictionQuantitiesDTO.setContractId(contractRegisterDTO);
                dTO.setContractId(contractRegisterDTO);
            }
            dTO.setRestrictionId(restrictionQuantitiesDTO);

        }

        if (extract.getOldExtract() != null && extract.getOldExtract().getId() != null) {
            RscExtractRegisterDTO extractRegisterDTO = new RscExtractRegisterDTO();
            extractRegisterDTO.setId(extract.getOldExtract().getId());
            extractRegisterDTO.setSerial(extract.getOldExtract().getSerial());
            extractRegisterDTO.setClosed(extract.getOldExtract().getClosed());
            dTO.setOldExtract(extractRegisterDTO);
        }

        if (extract.getInstProjectGuide() != null) {
            InstProjectGuideDTO instProjectGuideDTO = new InstProjectGuideDTO();
            instProjectGuideDTO.setId(extract.getInstProjectGuide().getId());
            instProjectGuideDTO.setName(extract.getInstProjectGuide().getName());
            instProjectGuideDTO.setCode(extract.getInstProjectGuide().getCode());
            dTO.setInstProjectGuideDTO(instProjectGuideDTO);
        }

        if (extract.getSiteId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(extract.getSiteId().getId());
            invOrganizationSiteDTO.setName(extract.getSiteId().getName());
            invOrganizationSiteDTO.setCode(extract.getSiteId().getCode());
            dTO.setSiteId(invOrganizationSiteDTO);
        }
        if (extract.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(extract.getInvInventoryId().getId());
            invInventoryDTO.setName(extract.getInvInventoryId().getName());
            invInventoryDTO.setCode(extract.getInvInventoryId().getCode());
            dTO.setInvInventoryId(invInventoryDTO);
        }

        if (extract.getCustomerId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(extract.getCustomerId().getId());
            invOrganizationSiteDTO.setName(extract.getCustomerId().getName());
            invOrganizationSiteDTO.setCode(extract.getCustomerId().getCode());
            dTO.setCustomerId(invOrganizationSiteDTO);
        }

        dTO.setRemarks(extract.getRemarks());
        dTO.setPostFlag(extract.getPostFlag());
        dTO.setPercentage(extract.getPercentage());
        dTO.setNetExtractPrice(extract.getNetExtractPrice());
        dTO.setDocNumber(extract.getDocNumber());
        dTO.setClientAcceptedDate(extract.getClientAcceptedDate());
        dTO.setClosed(extract.getClosed());
        dTO.setDate(extract.getDate());
        dTO.setProfissionaiAcceptedDate(extract.getProfissionaiAcceptedDate());
        dTO.setReceivedDate(extract.getReceivedDate());
        dTO.setRemain(extract.getRemain());
        dTO.setValid(extract.getValid());
        dTO.setId(extract.getId());
        dTO.setSerial(extract.getSerial());
        dTO.setIndex(extract.getId());
        dTO.setCreatedDate(extract.getCreationDate());
        dTO.setCreatedBy(extract.getCreatedBy().getId());
        dTO.setTotalPercentage(extract.getTotalPercentage());
    }

    public RscExtractRegister mapFromDTO(RscExtractRegisterDTO dTO, IsagUser isagUser) {
        RscExtractRegister extractRegister = new RscExtractRegister();
        if (dTO.getRestrictionId() != null) {
            RscRestrictionQuantities restrictionQuantities = new RscRestrictionQuantities();
            restrictionQuantities.setId(dTO.getRestrictionId().getId());
            extractRegister.setRestrictionId(restrictionQuantities);
        }

        if (dTO.getTenderId() != null) {
            RscTender rscTender = new RscTender();
            rscTender.setId(dTO.getTenderId().getId());
            extractRegister.setTenderId(rscTender);
        }

        if (dTO.getOldExtract() != null) {
            RscExtractRegister rscExtractRegister = new RscExtractRegister();
            rscExtractRegister.setId(dTO.getOldExtract() == null ? null : dTO.getOldExtract().getId());
            extractRegister.setOldExtract(rscExtractRegister);
        }
        if (dTO.getInvInventoryId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(dTO.getInvInventoryId().getId());
            extractRegister.setInvInventoryId(invInventory);
        }

        if (dTO.getInstProjectGuideDTO() != null) {
            InstProjectGuide instProjectGuide = new InstProjectGuide();
            instProjectGuide.setId(dTO.getInstProjectGuideDTO().getId());
            extractRegister.setInstProjectGuide(instProjectGuide);
        }

        if (dTO.getSiteId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dTO.getSiteId().getId());
            extractRegister.setSiteId(invOrganizationSite);
        }

        if (dTO.getCustomerId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dTO.getCustomerId().getId());
            extractRegister.setCustomerId(invOrganizationSite);
        }
        extractRegister.setPostFlag(dTO.getPostFlag());
        extractRegister.setTotalValue(dTO.getTotalFinal());
        extractRegister.setPercentage(dTO.getPercentage());
        extractRegister.setDate(dTO.getDate());
        extractRegister.setDocNumber(dTO.getDocNumber());
        extractRegister.setRemarks(dTO.getRemarks());
        extractRegister.setSerial(dTO.getSerial());
        if (dTO.getUri() == null || dTO.getUri().isEmpty() || !dTO.getUri().contains("rscExtractSummary")) {
            extractRegister.setNetExtractPrice(dTO.getNetExtractPrice());
        } else {
            extractRegister.setNetExtractPrice(dTO.getTotalFinal());
        }
        extractRegister.setDocNumber(dTO.getDocNumber());
        extractRegister.setClientAcceptedDate(dTO.getClientAcceptedDate());

        extractRegister.setDate(dTO.getDate());
        extractRegister.setProfissionaiAcceptedDate(dTO.getProfissionaiAcceptedDate());
        extractRegister.setReceivedDate(dTO.getReceivedDate());
        extractRegister.setRemain(dTO.getRemain());
        extractRegister.setValid(dTO.getValid());

        if (dTO.getId() == null) {
            extractRegister.setCreatedBy(isagUser);
            extractRegister.setCreationDate(new Date());
            extractRegister.setClosed(Boolean.FALSE);

        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            extractRegister.setCreatedBy(isagUser1);
            extractRegister.setCreationDate(dTO.getCreatedDate());
            extractRegister.setId(dTO.getId());
            extractRegister.setModificationDate(new Date());
            extractRegister.setModifiedBy(isagUser);
        }

        extractRegister.setBranchId(isagUser.getBranchId());
        extractRegister.setCompanyId(isagUser.getCompanyId());
        extractRegister.setTotalPercentage(dTO.getTotalPercentage());
        return extractRegister;
    }

    private void returnListDTO(List<RscExtractRegister> list, List<RscExtractRegisterDTO> dTOList) {
        for (RscExtractRegister extractRegister : list) {
            dTOList.add(mapTODTO(extractRegister, null, false));
        }
    }

    @Override
    public RscExtractRegisterDTO loadRestriction(RscExtractRegisterDTO extractRegisterDTO) {
        if (extractRegisterDTO.getRestrictionId() != null && extractRegisterDTO.getRestrictionId().getId() != null
                && extractRegisterDTO.getRestrictionId().getTenderId() != null && extractRegisterDTO.getRestrictionId().getTenderId().getId() != null) {

            List<RscRestrictionQuantitiesDetailDTO> rscDetailDTOList = rscRestrictionQuantitiesDetailService.getRestrictionQuantitiesDetailByRestrictionId(extractRegisterDTO.getRestrictionId().getId());
            extractRegisterDTO.setRscExtractRegisterDetailDTOList(new ArrayList<RscExtractRegisterDetailDTO>());
            Map<Integer, RscTenderDetailWorkUnit> mapTender = null;
            List<RscTenderRscAccrualDeductionDTO> accrualList = null;
            List<RscTenderRscAccrualDeductionDTO> deductionList = null;

            mapTender = rscTenderDetailWorkUnitService.getMapTenderDetailWorkUnitByTenderId(extractRegisterDTO.getRestrictionId().getTenderId().getId());
            accrualList = rscTenderRscAccrualDeductionService.getAllRscTenderRscAccrualDeductionList(extractRegisterDTO.getRestrictionId().getTenderId().getId(), 2); // Accrual
            deductionList = rscTenderRscAccrualDeductionService.getAllRscTenderRscAccrualDeductionList(extractRegisterDTO.getRestrictionId().getTenderId().getId(), 3); // Deduction
            Integer rscQotationPriceTenderId = rscDetailDTOList.get(0) != null && rscDetailDTOList.get(0).getRscRestrictionQuantities() != null && rscDetailDTOList.get(0).getRscRestrictionQuantities().getContractId() != null && rscDetailDTOList.get(0).getRscRestrictionQuantities().getContractId().getRscQotationPriceTenderId() != null ? rscDetailDTOList.get(0).getRscRestrictionQuantities().getContractId().getRscQotationPriceTenderId().getId() : null;
            Integer ContractId = rscDetailDTOList.get(0) != null && rscDetailDTOList.get(0).getRscRestrictionQuantities() != null && rscDetailDTOList.get(0).getRscRestrictionQuantities().getContractId() != null ? rscDetailDTOList.get(0).getRscRestrictionQuantities().getContractId().getId() : null;

            List<RscQotationPriceTenderDetailWorkUnitDTO> list = rscQotationPriceTenderDetailWorkUnitService.getRscQotationPriceTenderDetailWorkUnitByTenderId(rscQotationPriceTenderId);
            Map<Integer, BigDecimal> mapQotationPrice = new HashMap<>();
            if (rscDetailDTOList != null && !rscDetailDTOList.isEmpty()) {
                for (RscQotationPriceTenderDetailWorkUnitDTO tenderDetailWorkUnitDTO : list) {
                    mapQotationPrice.put(tenderDetailWorkUnitDTO.getWorkUnitId(), tenderDetailWorkUnitDTO.getPrice());
                }
            }

            extractRegisterDTO = loadAccrual(accrualList, extractRegisterDTO);

            extractRegisterDTO = loadDeduction(deductionList, extractRegisterDTO);

            extractRegisterDTO = loadExtractDetail(rscDetailDTOList, mapTender, extractRegisterDTO, mapQotationPrice);

            extractRegisterDTO = updateSummary(extractRegisterDTO, true);

            extractRegisterDTO.setNetOldExtracts(getTotalNetFromOldExtraction(extractRegisterDTO.getRestrictionId().getTenderId().getId()));
            if (extractRegisterDTO.getRestrictionId().getTenderId() != null && extractRegisterDTO.getRestrictionId().getTenderId().getId() != null) {
                RscTenderDTO rscTenderDTO = new RscTenderDTO();
                rscTenderDTO.setId(extractRegisterDTO.getRestrictionId().getTenderId().getId());
                rscTenderDTO.setSerial(extractRegisterDTO.getRestrictionId().getTenderId().getSerial());
                rscTenderDTO.setDate(extractRegisterDTO.getRestrictionId().getTenderId().getDate());
                rscTenderDTO.setRemark(extractRegisterDTO.getRestrictionId().getTenderId().getRemark());
                extractRegisterDTO.setTenderId(rscTenderDTO);
            }
            if (extractRegisterDTO.getRestrictionId().getContractId() != null && extractRegisterDTO.getRestrictionId().getContractId().getId() != null) {
                RscContractRegisterDTO rscContractRegisterDTO = new RscContractRegisterDTO();
                rscContractRegisterDTO.setSerial(extractRegisterDTO.getRestrictionId().getContractId().getSerial());
                rscContractRegisterDTO.setDocumentNumber(extractRegisterDTO.getRestrictionId().getContractId().getDocumentNumber());
                rscContractRegisterDTO.setNettotal(extractRegisterDTO.getRestrictionId().getContractId().getNettotal());
                extractRegisterDTO.setContractId(rscContractRegisterDTO);
            }
            extractRegisterDTO.setNetOldExtracts(getTotalNetCostFrom(extractRegisterDTO.getBranchId(), extractRegisterDTO.getTenderId().getId()));
            extractRegisterDTO = calculateRemain(extractRegisterDTO);

        }
        return extractRegisterDTO;
    }

    @Override
    public RscExtractRegisterDTO loadRestrictionList(List<RscRestrictionQuantitiesDTO> restrictionQuantitiesDTOList) {
        RscExtractRegisterDTO rscExtractRegisterDTO = new RscExtractRegisterDTO();
        rscExtractRegisterDTO.setRscExtractRegisterDetailDTOList(new ArrayList<RscExtractRegisterDetailDTO>());
        Map<Integer, RscExtractRegisterDetailDTO> mapDetailQuantity = new HashMap<>();
        Integer instProjectGuideIdSelected = restrictionQuantitiesDTOList.get(0).getInstProjectGuideDTO().getId();
        Integer contractIdSelected = restrictionQuantitiesDTOList.get(0).getContractor().getId();
        rscExtractRegisterDTO.setSiteId(restrictionQuantitiesDTOList.get(0).getContractor());
        rscExtractRegisterDTO.setInstProjectGuideDTO(restrictionQuantitiesDTOList.get(0).getInstProjectGuideDTO());
        rscExtractRegisterDTO.setDate(new Date());
        for (RscRestrictionQuantitiesDTO restrictionQuantitiesDTO : restrictionQuantitiesDTOList) {
            if (!instProjectGuideIdSelected.equals(restrictionQuantitiesDTO.getInstProjectGuideDTO().getId())) {
                rscExtractRegisterDTO.setMsg("يجب توحيد المشروع");
                return rscExtractRegisterDTO;
            }
            if (!contractIdSelected.equals(restrictionQuantitiesDTO.getContractor().getId())) {
                rscExtractRegisterDTO.setMsg("يجب توحيد المقاول");
                return rscExtractRegisterDTO;
            }
        }

        for (RscRestrictionQuantitiesDTO restrictionQuantitiesDTO : restrictionQuantitiesDTOList) {
            List<RscRestrictionQuantitiesDetailDTO> rscDetailDTOList = rscRestrictionQuantitiesDetailService.getRestrictionQuantitiesDetailByRestrictionId(restrictionQuantitiesDTO.getId());
            for (RscRestrictionQuantitiesDetailDTO rscRestrictionQuantitiesDetailDTO : rscDetailDTOList) {
                if (mapDetailQuantity.containsKey(rscRestrictionQuantitiesDetailDTO.getGuideDTO().getId())) {
                    RscExtractRegisterDetailDTO oldDetail = mapDetailQuantity.get(rscRestrictionQuantitiesDetailDTO.getGuideDTO().getId());
                    oldDetail.setExcuteQty(oldDetail.getExcuteQty().add(rscRestrictionQuantitiesDetailDTO.getExcuteQty()));
                    mapDetailQuantity.put(rscRestrictionQuantitiesDetailDTO.getGuideDTO().getId(), oldDetail);
                } else {
                    RscExtractRegisterDetailDTO rscExtractRegisterDetailDTO = new RscExtractRegisterDetailDTO();
                    rscExtractRegisterDetailDTO.setIndex(rscRestrictionQuantitiesDetailDTO.getId());
                    rscExtractRegisterDetailDTO.setGuideDTO(rscRestrictionQuantitiesDetailDTO.getGuideDTO());
                    rscExtractRegisterDetailDTO.setGuideDTOBak(rscRestrictionQuantitiesDetailDTO.getGuideDTO());
                    rscExtractRegisterDetailDTO.setExcuteQty(rscRestrictionQuantitiesDetailDTO.getExcuteQty());
                    mapDetailQuantity.put(rscRestrictionQuantitiesDetailDTO.getGuideDTO().getId(), rscExtractRegisterDetailDTO);
                }
            }
        }

//        List<RscExtractRegisterDetailDTO> detailList = (List<RscExtractRegisterDetailDTO>) mapDetailQuantity.values();
        List<RscExtractRegisterDetailDTO> detailList = new ArrayList<RscExtractRegisterDetailDTO>(mapDetailQuantity.values());;
        rscExtractRegisterDTO.setRscExtractRegisterDetailDTOList(detailList);

        rscExtractRegisterDTO = updateSummary(rscExtractRegisterDTO, true);

//        rscExtractRegisterDTO = calculateRemain(rscExtractRegisterDTO);
        return rscExtractRegisterDTO;
    }

    private RscExtractRegisterDTO loadExtractDetail(List<RscRestrictionQuantitiesDetailDTO> rscDetailDTOList, Map<Integer, RscTenderDetailWorkUnit> mapTender, RscExtractRegisterDTO extractRegisterDTO, Map<Integer, BigDecimal> mapQotationPrice) {
        RscExtractRegisterDetail extractRegisterDetailOld;
        if (rscDetailDTOList != null) {
            for (RscRestrictionQuantitiesDetailDTO detailWorkUnitDTO : rscDetailDTOList) {

                RscExtractRegisterDetailDTO detail = new RscExtractRegisterDetailDTO();
                detail.setIndex(detailWorkUnitDTO.getIndex());
                detail.setGuideDTO(detailWorkUnitDTO.getGuideDTO());
                detail.setGuideDTOBak(detailWorkUnitDTO.getGuideDTO());
                RscTenderDetailWorkUnit detailWorkUnit = mapTender.get(detailWorkUnitDTO.getGuideDTO().getId());
                if (detailWorkUnit.getQuantity() != null) {
                    detail.setContractQty(detailWorkUnit.getQuantity() != null ? detailWorkUnit.getQuantity() : BigDecimal.ZERO);
                }
                detail.setCurrentQty(detailWorkUnitDTO.getExcuteQty() != null ? detailWorkUnitDTO.getExcuteQty() : BigDecimal.ZERO);

                extractRegisterDetailOld = rscExtractRegisterDetailService.getdetail(extractRegisterDTO.getRestrictionId().getTenderId().getId(), detailWorkUnitDTO.getGuideDTO().getId());
                if (extractRegisterDetailOld != null) {
                    detail.setOldQty(extractRegisterDetailOld.getCurrentQty() != null ? extractRegisterDetailOld.getCurrentQty() : BigDecimal.ZERO);
                    detail.setExcuteQty(detail.getCurrentQty().subtract(detail.getOldQty()));
                } else {
                    detail.setOldQty(BigDecimal.ZERO);
                    detail.setExcuteQty(detailWorkUnitDTO.getExcuteQty() != null ? detailWorkUnitDTO.getExcuteQty() : BigDecimal.ZERO);
                }

                detail.setPriceSell(mapQotationPrice.get(detailWorkUnitDTO.getWorkUnitId()) != null ? mapQotationPrice.get(detailWorkUnitDTO.getWorkUnitId()) : BigDecimal.ZERO);

                detail.setOldRamp((extractRegisterDetailOld != null && extractRegisterDetailOld.getRampValue() != null) ? extractRegisterDetailOld.getRampValue() : BigDecimal.ZERO);

                extractRegisterDTO.getRscExtractRegisterDetailDTOList().add(detail);
            }
        }
        return extractRegisterDTO;
    }

    private RscExtractRegisterDTO loadDeduction(List<RscTenderRscAccrualDeductionDTO> deductionList, RscExtractRegisterDTO extractRegisterDTO) {
        RscExtractAccuralDeductionDTO rscExtractAccuralDeductionDTO;
        if (deductionList != null) {
            for (RscTenderRscAccrualDeductionDTO rscTenderRscAccrualDeductionDTO : deductionList) {
                rscExtractAccuralDeductionDTO = initrscExtractAccuralDeductionDTO(rscTenderRscAccrualDeductionDTO);
                extractRegisterDTO.getRscExtractDeductionList().add(rscExtractAccuralDeductionDTO);
            }
        }
        return extractRegisterDTO;
    }

    private RscExtractRegisterDTO loadAccrual(List<RscTenderRscAccrualDeductionDTO> accrualList, RscExtractRegisterDTO extractRegisterDTO) {
        RscExtractAccuralDeductionDTO rscExtractAccuralDeductionDTO;
        if (accrualList != null) {
            for (RscTenderRscAccrualDeductionDTO rscTenderRscAccrualDeductionDTO : accrualList) {
                rscExtractAccuralDeductionDTO = initrscExtractAccuralDeductionDTO(rscTenderRscAccrualDeductionDTO);
                extractRegisterDTO.getRscExtractAccuralList().add(rscExtractAccuralDeductionDTO);
            }
        }
        return extractRegisterDTO;
    }

    private RscExtractAccuralDeductionDTO initrscExtractAccuralDeductionDTO(RscTenderRscAccrualDeductionDTO rscTenderRscAccrualDeductionDTO) {
        RscExtractAccuralDeductionDTO rscExtractAccuralDeductionDTO;
        rscExtractAccuralDeductionDTO = new RscExtractAccuralDeductionDTO();
        rscExtractAccuralDeductionDTO.setRscAccrualDeductionDTO(rscTenderRscAccrualDeductionDTO.getRscAccrualDeductionId());
        rscExtractAccuralDeductionDTO.setRscAccrualDeductionDTObak(rscTenderRscAccrualDeductionDTO.getRscAccrualDeductionId());
        rscExtractAccuralDeductionDTO.setRscAccuralDeductionPercentage(rscTenderRscAccrualDeductionDTO.getPercentage());
        rscExtractAccuralDeductionDTO.setIndex(rscTenderRscAccrualDeductionDTO.getId());
        return rscExtractAccuralDeductionDTO;
    }

    private RscExtractRegisterDTO calculateRemain(RscExtractRegisterDTO dTO) {

        if (dTO.getRestrictionId().getContractId() != null) {
            dTO.setRemain((dTO.getRestrictionId().getContractId().getContractTotal() != null ? dTO.getRestrictionId().getContractId().getContractTotal() : BigDecimal.ZERO).subtract(dTO.getNetOldExtracts() != null ? dTO.getNetOldExtracts() : BigDecimal.ZERO));
        } else {
            dTO.setRemain(BigDecimal.ZERO);
        }
        return dTO;
    }

    @Override
    public RscExtractRegisterDTO updateSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage) {

        if (extractRegisterDTO != null) {

            extractRegisterDTO = updateWorkUnitSummary(extractRegisterDTO, ispercentage);

            extractRegisterDTO = updateDeductionSummary(extractRegisterDTO, ispercentage);
            extractRegisterDTO = updateAccuralSummary(extractRegisterDTO, ispercentage);
//            extractRegisterDTO = updateNetExtract(extractRegisterDTO);
        }
        extractRegisterDTO.setTotalFinal(extractRegisterDTO.getTotalDetailNet().subtract(extractRegisterDTO.getTotalDeductionNetvalue()).add(extractRegisterDTO.getTotalAccrualNetvalue()));
        return extractRegisterDTO;
    }

    @Override
    public RscExtractRegisterDTO updateExtractSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage) {

        if (extractRegisterDTO != null) {

            extractRegisterDTO = updateWorkUnitExtractSummary(extractRegisterDTO, ispercentage);

            extractRegisterDTO = updateDeductionSummary(extractRegisterDTO, ispercentage);
            extractRegisterDTO = updateAccuralSummary(extractRegisterDTO, ispercentage);
//            extractRegisterDTO = updateNetExtract(extractRegisterDTO);
        }
        extractRegisterDTO.setTotalBeforePercentage(extractRegisterDTO.getTotalDetailNet());
        if (extractRegisterDTO.getPercentage() != null && extractRegisterDTO.getPercentage().compareTo(BigDecimal.ZERO) == 1) {
            BigDecimal totle = extractRegisterDTO.getTotalDetailNet().multiply(extractRegisterDTO.getPercentage().divide(new BigDecimal(100), 3, RoundingMode.HALF_UP));
            extractRegisterDTO.setTotalPercentage(totle);
            extractRegisterDTO.setTotalDetailNet(extractRegisterDTO.getTotalDetailNet().add(totle));
        } else {
            extractRegisterDTO.setTotalPercentage(BigDecimal.ZERO);
        }
        extractRegisterDTO.setTotalFinal(extractRegisterDTO.getTotalDetailNet().subtract(extractRegisterDTO.getTotalDeductionNetvalue()).add(extractRegisterDTO.getTotalAccrualNetvalue()));
        return extractRegisterDTO;
    }

    @Override
    public RscExtractRegisterDTO updateWorkUnitExtractSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage) {
        if (extractRegisterDTO.getRscExtractRegisterDetailDTOList() != null && !extractRegisterDTO.getRscExtractRegisterDetailDTOList().isEmpty()) {
            extractRegisterDTO.setTotalDetailNet(BigDecimal.ZERO);
            for (RscExtractRegisterDetailDTO detail : extractRegisterDTO.getRscExtractRegisterDetailDTOList()) {
                if (detail.getGuideDTO() == null) {
                    detail.setGuideDTO(detail.getGuideDTOBak());
                }

                if (detail.getDiscountValue() == null) {
                    detail.setDiscountValue(BigDecimal.ZERO);
                }
                if (detail.getDiscountPercentage() == null) {
                    detail.setDiscountPercentage(BigDecimal.ZERO);
                }
                if (detail.getNetCost() == null) {
                    detail.setNetCost(BigDecimal.ZERO);
                }
                if (detail.getExcuteQty() == null) {
                    detail.setExcuteQty(BigDecimal.ZERO);
                }
                if (detail.getPriceSell() == null) {
                    detail.setPriceSell(BigDecimal.ZERO);
                }
                if (detail.getNetRamp() == null) {
                    detail.setNetRamp(BigDecimal.ZERO);
                }

                BigDecimal currentValue = ((detail.getPriceSell() != null ? detail.getPriceSell() : BigDecimal.ZERO).multiply(detail.getExcuteQty() != null ? detail.getExcuteQty() : BigDecimal.ONE)).setScale(3, RoundingMode.UP);
                if (ispercentage) {
                    if (detail.getDiscountPercentage() != null) {
                        detail.setDiscountValue(((detail.getCurrentValue() != null ? detail.getCurrentValue() : BigDecimal.ZERO).multiply(detail.getDiscountPercentage().divide(new BigDecimal(100)))).setScale(3, RoundingMode.UP));
                    }
                } else {

                    if (detail.getDiscountValue() != null && detail.getDiscountValue().compareTo(BigDecimal.ZERO) == 1) {
                        BigDecimal DiscountValue = (detail.getDiscountValue() != null ? detail.getDiscountValue() : BigDecimal.ZERO).setScale(3, RoundingMode.UP);
                        detail.setDiscountPercentage((DiscountValue.divide(currentValue, 3, RoundingMode.UP)).multiply(new BigDecimal(100)));
                    }
                }
                detail.setCurrentValue(currentValue);
                detail.setNetCost(detail.getCurrentValue().subtract(detail.getDiscountValue()));
                extractRegisterDTO.setTotalDetailNet((extractRegisterDTO.getTotalDetailNet() == null ? BigDecimal.ZERO : extractRegisterDTO.getTotalDetailNet()).add(detail.getNetCost() == null ? BigDecimal.ZERO : detail.getNetCost()).setScale(3, RoundingMode.HALF_UP));
            }
        }
        return extractRegisterDTO;
    }

    public RscExtractRegisterDTO updateNetExtract(RscExtractRegisterDTO extractRegisterDTO) {
        if (extractRegisterDTO.getTotalDetailNet() != null && extractRegisterDTO.getTotalDeductionNetvalue() != null && extractRegisterDTO.getTotalAccrualNetvalue() != null) {
            extractRegisterDTO.setNetExtract(
                    (extractRegisterDTO.getTotalDetailNet().add(extractRegisterDTO.getTotalAccrualNetvalue())).subtract(extractRegisterDTO.getTotalDeductionNetvalue())
            );

        }
        return extractRegisterDTO;
    }

    @Override
    public RscExtractRegisterDTO updateWorkUnitSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage) {
        if (extractRegisterDTO.getRscExtractRegisterDetailDTOList() != null && !extractRegisterDTO.getRscExtractRegisterDetailDTOList().isEmpty()) {
            extractRegisterDTO.setTotalDetailNet(BigDecimal.ZERO);
            for (RscExtractRegisterDetailDTO detail : extractRegisterDTO.getRscExtractRegisterDetailDTOList()) {
                if (detail.getGuideDTO() == null) {
                    detail.setGuideDTO(detail.getGuideDTOBak());
                }
                if (detail.getRampValue() == null) {
                    detail.setRampValue(BigDecimal.ZERO);
                }
                if (detail.getRampPercentage() == null) {
                    detail.setRampPercentage(BigDecimal.ZERO);
                }
                if (detail.getOldRamp() == null) {
                    detail.setOldRamp(BigDecimal.ZERO);
                }
                if (detail.getCurrentValue() == null) {
                    detail.setCurrentValue(BigDecimal.ZERO);
                }
                if (detail.getDiscountValue() == null) {
                    detail.setDiscountValue(BigDecimal.ZERO);
                }
                if (detail.getDiscountPercentage() == null) {
                    detail.setDiscountPercentage(BigDecimal.ZERO);
                }
                if (detail.getNetCost() == null) {
                    detail.setNetCost(BigDecimal.ZERO);
                }
                if (detail.getExcuteQty() == null) {
                    detail.setExcuteQty(BigDecimal.ZERO);
                }
                if (detail.getPriceSell() == null) {
                    detail.setPriceSell(BigDecimal.ZERO);
                }
                if (detail.getReturnRamp() == null) {
                    detail.setReturnRamp(BigDecimal.ZERO);
                }
                if (detail.getTotalRamp() == null) {
                    detail.setTotalRamp(BigDecimal.ZERO);
                }
                if (detail.getNetRamp() == null) {
                    detail.setNetRamp(BigDecimal.ZERO);
                }

                detail.setCurrentValue(detail.getPriceSell().multiply(detail.getExcuteQty()));

                if (ispercentage) {
                    if (detail.getRampPercentage() != null) {
                        detail.setRampValue(((detail.getCurrentValue() != null ? detail.getCurrentValue() : BigDecimal.ZERO).multiply(detail.getRampPercentage().divide(new BigDecimal(100)))).setScale(3, RoundingMode.UP));
                    }
                    if (detail.getDiscountPercentage() != null) {
                        detail.setDiscountValue(((detail.getCurrentValue() != null ? detail.getCurrentValue() : BigDecimal.ZERO).multiply(detail.getDiscountPercentage().divide(new BigDecimal(100)))).setScale(3, RoundingMode.UP));
                    }
                } else {
                    BigDecimal currentValue = (detail.getCurrentValue() != null ? detail.getCurrentValue() : BigDecimal.ONE).setScale(3, RoundingMode.UP);

                    if (detail.getRampValue() != null && currentValue != null && currentValue.compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal rampValue = (detail.getRampValue() != null ? detail.getRampValue() : BigDecimal.ZERO).setScale(3, RoundingMode.UP);
                        detail.setRampPercentage((rampValue.divide(currentValue, 3, RoundingMode.UP)).multiply(new BigDecimal(100)));
                    }

                    if (detail.getDiscountValue() != null && currentValue != null && currentValue.compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal DiscountValue = (detail.getDiscountValue() != null ? detail.getDiscountValue() : BigDecimal.ZERO).setScale(3, RoundingMode.UP);
                        detail.setDiscountPercentage((DiscountValue.divide(currentValue, 3, RoundingMode.UP)).multiply(new BigDecimal(100)));
                    }
                }

                detail.setNetRamp(detail.getCurrentValue().add(detail.getTotalRamp()));
                detail.setTotalRamp(detail.getRampValue().subtract(detail.getReturnRamp()));
                detail.setNetCost(detail.getCurrentValue().add(detail.getRampValue().subtract(detail.getReturnRamp()).subtract(detail.getDiscountValue())));
                extractRegisterDTO.setTotalDetailNet((extractRegisterDTO.getTotalDetailNet() == null ? BigDecimal.ZERO : extractRegisterDTO.getTotalDetailNet()).add(detail.getNetCost() == null ? BigDecimal.ZERO : detail.getNetCost()).setScale(3, RoundingMode.HALF_UP));
            }
        }
        return extractRegisterDTO;
    }

    @Override
    public RscExtractRegisterDTO updateDeductionSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage) {
        if (extractRegisterDTO.getRscExtractAccuralList() != null && !extractRegisterDTO.getRscExtractAccuralList().isEmpty()) {
            extractRegisterDTO.setTotalDeductionNetpercentage(BigDecimal.ZERO);
            extractRegisterDTO.setTotalDeductionNetvalue(BigDecimal.ZERO);
            for (RscExtractAccuralDeductionDTO detail : extractRegisterDTO.getRscExtractDeductionList()) {
                if (detail.getRscAccrualDeductionDTO() == null) {
                    detail.setRscAccrualDeductionDTO(detail.getRscAccrualDeductionDTObak());
                }
                if (ispercentage) {
                    detail.setRscAccuralDeductionValue((detail.getRscAccuralDeductionPercentage().divide(new BigDecimal(100))).multiply(extractRegisterDTO.getTotalDetailNet()));
                } else {
                    detail.setRscAccuralDeductionPercentage(detail.getRscAccuralDeductionValue().multiply(new BigDecimal(100)).divide(extractRegisterDTO.getTotalDetailNet(), 3, RoundingMode.HALF_UP));
                }
                extractRegisterDTO.setTotalDeductionNetpercentage((extractRegisterDTO.getTotalDeductionNetpercentage() == null ? BigDecimal.ZERO : extractRegisterDTO.getTotalDeductionNetpercentage()).add(detail.getRscAccuralDeductionPercentage() == null ? BigDecimal.ZERO : detail.getRscAccuralDeductionPercentage()));
                extractRegisterDTO.setTotalDeductionNetvalue((extractRegisterDTO.getTotalDeductionNetvalue() == null ? BigDecimal.ZERO : extractRegisterDTO.getTotalDeductionNetvalue()).add(detail.getRscAccuralDeductionValue() == null ? BigDecimal.ZERO : detail.getRscAccuralDeductionValue()));
            }
        }
        return extractRegisterDTO;
    }

    @Override
    public RscExtractRegisterDTO updateAccuralSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage) {
        if (extractRegisterDTO.getRscExtractAccuralList() != null && !extractRegisterDTO.getRscExtractAccuralList().isEmpty()) {
            extractRegisterDTO.setTotalAccrualNetpercentage(BigDecimal.ZERO);
            extractRegisterDTO.setTotalAccrualNetvalue(BigDecimal.ZERO);
            for (RscExtractAccuralDeductionDTO detail : extractRegisterDTO.getRscExtractAccuralList()) {
                if (detail.getRscAccrualDeductionDTO() == null) {
                    detail.setRscAccrualDeductionDTO(detail.getRscAccrualDeductionDTObak());
                }
                if (ispercentage) {
                    detail.setRscAccuralDeductionValue((detail.getRscAccuralDeductionPercentage().divide(new BigDecimal(100))).multiply(extractRegisterDTO.getTotalDetailNet()));
                } else {
                    detail.setRscAccuralDeductionPercentage(detail.getRscAccuralDeductionValue().multiply(new BigDecimal(100)).divide(extractRegisterDTO.getTotalDetailNet(), 3, RoundingMode.HALF_UP));
                }
                extractRegisterDTO.setTotalAccrualNetpercentage((extractRegisterDTO.getTotalAccrualNetpercentage() == null ? BigDecimal.ZERO : extractRegisterDTO.getTotalAccrualNetpercentage()).add(detail.getRscAccuralDeductionPercentage() == null ? BigDecimal.ZERO : detail.getRscAccuralDeductionPercentage()));
                extractRegisterDTO.setTotalAccrualNetvalue((extractRegisterDTO.getTotalAccrualNetvalue() == null ? BigDecimal.ZERO : extractRegisterDTO.getTotalAccrualNetvalue()).add(detail.getRscAccuralDeductionValue() == null ? BigDecimal.ZERO : detail.getRscAccuralDeductionValue()));
            }
        }
        return extractRegisterDTO;
    }

}
