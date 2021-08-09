/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscContractRegisterDetailWorkUnitDTO;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscTenderDTO;

import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagUser;
import com.isag.entity.RscContractRegister;
import com.isag.entity.RscPriceTender;
import com.isag.entity.RscQotationPriceTender;
import com.isag.entity.RscTender;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class RscContractRegisterServiceImpl implements RscContractRegisterService {

    @EJB
    private GenericDAO dao;
    @EJB
    private RscContractRegisterDetailWorkUnitService contractDetailWorkUnitService;
    @EJB
    private GenericService genericService;
    @EJB
    private RscRestrictionQuantitiesService restrictionQuantitiesService;
    private Set<Integer> contractSet = new HashSet<>();

    @Override
    public List<RscContractRegisterDTO> getBranchRscContractRegisterActiveByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscContractRegisterDTO> dTOList = new ArrayList<>();
        List<RscContractRegister> list = dao.findListByQuery("SELECT acc FROM RscContractRegister acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public Set<Integer> getRscQotationPriceTenderIDByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        Set<Integer> rscQotationPriceSet = new HashSet<>();
        List<Integer> list = dao.findListByQuery("SELECT acc.rscQotationPriceTenderId.id FROM RscContractRegister acc WHERE acc.branchId.id=:branchId", params);
        rscQotationPriceSet.addAll(list);
        return rscQotationPriceSet;
    }

    @Override
    public RscContractRegisterDTO findRscContractRegister(Integer tenderId) {
        if (tenderId != null) {
            RscContractRegister tender = dao.findEntityById(RscContractRegister.class, tenderId);
            return mapTODTO(tender, null, true);
        } else {
            return null;
        }
    }

    @Override
    public RscContractRegisterDTO addRscContractRegister(RscContractRegisterDTO contractDTO, IsagUser isagUser) {
        RscContractRegister contract = mapFromDTO(contractDTO, isagUser);
        StringBuilder error = new StringBuilder();
        error = validateHead(contractDTO, error);
        if (error != null && !error.toString().isEmpty()) {
            contractDTO.setMsg(error.toString());
        } else {
            if (contract.getId() == null) {
                Integer serial = genericService.getMaxSerialForInvoice(RscContractRegister.class, contract.getBranchId().getId(), null, null);
                contract.setSerial(serial);
            }

            if (contractDTO.getTenderId() != null && contractDTO.getTenderId().getType().equals(1) && contractDTO.getSiteId() == null) {
                contract.setSiteId(null);
            }

            contract = dao.updateEntity(contract);

            if (contractDTO.getContractRegisterDetailList() != null && !contractDTO.getContractRegisterDetailList().isEmpty()) {
                contractDetailWorkUnitService.addContractDetailWorkUnitList(contractDTO.getContractRegisterDetailList(), contract, isagUser);
            }

            if (contractDTO.getRscContractRegisterDetailWorkUnitDeletedList() != null && !contractDTO.getRscContractRegisterDetailWorkUnitDeletedList().isEmpty()) {
                contractDetailWorkUnitService.deleteContractDetailWorkUnitList(contractDTO.getRscContractRegisterDetailWorkUnitDeletedList());
            }

            contractDTO = mapTODTO(contract, contractDTO, true);
        }
        return contractDTO;

    }

    private StringBuilder validateHead(RscContractRegisterDTO contractDTO, StringBuilder error) {
        if (contractDTO.getDate() == null) {
            error.append("يجب ادخال تاريخ");
        }

        if (contractDTO.getInitialReceivedDate() == null) {
            error.append("يجب ادخال تاريخ استلام ابتدائي");
        }

        if (contractDTO.getFinallReceivedDate() == null) {
            error.append("يجب ادخال تاريخ استلام نهائي");
        }

        if (contractDTO.getContractTotal() == null) {
            error.append("يجب ادخال اجمالي قيمة التعاقد");
        }

        if (contractDTO.getInitialValue() == null) {
            error.append("يجب ادخال اجمالي قيمة الدفعة المقدمة");
        }

        if (contractDTO.getInitialPercentage() == null) {
            error.append("يجب ادخال اجمالي نسبة الدفعة المقدمة");
        }

        if (contractDTO.getTenderId() == null) {
            error.append("يجب اختيار المناقصة ");
        }

        if (contractDTO.getTenderId() != null && contractDTO.getTenderId().getType().equals(1) && contractDTO.getSiteId() == null) {
            error.append("يجب اختيار مقاول باطن ");
        }

        return error;
    }

    @Override
    public void deleteRscContractRegister(RscContractRegisterDTO contractDTO, IsagUser isagUser) {
        RscContractRegister contract = new RscContractRegister();
        contract.setId(contractDTO.getId());
        List<RscContractRegisterDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist = contractDetailWorkUnitService.getContractDetailWorkUnitByContractId(contractDTO.getId());
        contractDetailWorkUnitService.deleteContractDetailWorkUnitList(tenderDetailWorkUnitDTOlist);
        dao.deleteEntity(contract);
    }

    @Override
    public List<RscContractRegisterDTO> getAllRscContractRegisterByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscContractRegisterDTO> dTOList = new ArrayList<>();
        List<RscContractRegister> list = dao.findListByQuery("SELECT acc FROM RscContractRegister acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscContractRegisterDTO getAllRscContractRegisterByBranchIdAndTender(Integer branchId, Integer tenderId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("tenderId", tenderId);
        RscContractRegisterDTO rscContractRegisterDTO = new RscContractRegisterDTO();
        RscContractRegister entity = dao.findEntityByQuery("SELECT acc FROM RscContractRegister acc WHERE acc.branchId.id = :id AND acc.tenderId.id = :tenderId ", params);
        initMapToDTO(rscContractRegisterDTO, entity);
        return rscContractRegisterDTO;
    }

    @Override
    public List<RscContractRegisterDTO> getAllRscContractRegisterByOwner(Integer customerId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("customerId", customerId);

        List<RscContractRegisterDTO> dTOList = new ArrayList<>();
        List<RscContractRegister> list = dao.findListByQuery("SELECT acc FROM RscContractRegister acc WHERE acc.branchId.id = :id AND acc.rscQotationPriceTenderId.rscPriceTenderId.tenderId.customerId.id = :customerId ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscContractRegisterDTO> getAllRscContractRegisterByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);
        params.put("selectedId", selectedId);
        String sql = "SELECT acc FROM RscContractRegister acc WHERE acc.id != :selectedId and acc.branchId.id = :id AND acc.serial = :serial ";
        List<RscContractRegisterDTO> dTOList = new ArrayList<>();
        List<RscContractRegister> list = dao.findListByQuery(sql, params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscContractRegisterDTO mapTODTO(RscContractRegister tender, RscContractRegisterDTO dTO, Boolean check) {
        if (dTO == null) {
            dTO = new RscContractRegisterDTO();
        }
        initMapToDTO(dTO, tender);
        if (check) {
            dTO.setContractRegisterDetailList(contractDetailWorkUnitService.getContractDetailWorkUnitByContractId(tender.getId()));

        }
        return dTO;
    }

    private void initMapToDTO(RscContractRegisterDTO dTO, RscContractRegister contract) {

        if (contract.getSiteId() != null && contract.getSiteId().getId() != null) {
            InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
            siteDTO.setId(contract.getSiteId().getId());
            siteDTO.setCode(contract.getSiteId().getCode());
            siteDTO.setName(contract.getSiteId().getName());
            dTO.setSiteId(siteDTO);
        }
        if (contract.getTenderId() != null && contract.getTenderId().getId() != null) {
            RscTenderDTO tenderDTO = new RscTenderDTO();
            tenderDTO.setId(contract.getTenderId().getId());
            tenderDTO.setSerial(contract.getTenderId().getSerial());
            tenderDTO.setRemark(contract.getTenderId().getRemark());
            tenderDTO.setType(contract.getTenderId().getType());
            if (contract.getTenderId().getCustomerId() != null && contract.getTenderId().getCustomerId().getId() != null) {
                InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
                siteDTO.setId(contract.getTenderId().getCustomerId().getId());
                siteDTO.setName(contract.getTenderId().getCustomerId().getName());
                tenderDTO.setCustomerId(siteDTO);
            }

            dTO.setTenderId(tenderDTO);
        }

        if (contract.getRscQotationPriceTenderId() != null && contract.getRscQotationPriceTenderId().getId() != null) {
            RscQotationPriceTenderDTO qotationDTO = new RscQotationPriceTenderDTO();
            qotationDTO.setId(contract.getRscQotationPriceTenderId().getId());
            qotationDTO.setDocNumber(contract.getRscQotationPriceTenderId().getDocNumber());
            qotationDTO.setDate(contract.getRscQotationPriceTenderId().getDate());
            qotationDTO.setSerial(contract.getRscQotationPriceTenderId().getSerial());
            qotationDTO.setStatus(contract.getRscQotationPriceTenderId().getStatus());
            //-------
            if (qotationDTO.getStatus() == null || qotationDTO.getStatus() == 0) {
                qotationDTO.setQotationStatus("لم يتم");
            } else if (qotationDTO.getStatus() == 1) {
                qotationDTO.setQotationStatus("تم");
            }
            //--------
            qotationDTO.setRemark(contract.getRscQotationPriceTenderId().getRemark());
            if (contract.getRscQotationPriceTenderId().getCustomerId() != null && contract.getRscQotationPriceTenderId().getCustomerId().getId() != null) {
                InvOrganizationSiteDTO orgSiteDTO = new InvOrganizationSiteDTO();
                orgSiteDTO.setId(contract.getSiteId() != null ? contract.getSiteId().getId() : null);
                orgSiteDTO.setCode(contract.getSiteId() != null ? contract.getSiteId().getCode() : null);
                orgSiteDTO.setName(contract.getSiteId() != null ? contract.getSiteId().getName() : null);
                dTO.setSiteId(orgSiteDTO);
            }
            if (contract.getRscQotationPriceTenderId().getRscPriceTenderId() != null && contract.getRscQotationPriceTenderId().getRscPriceTenderId().getId() != null) {
                RscPriceTenderDTO priceTenderDTO = new RscPriceTenderDTO();
                priceTenderDTO.setId(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getId());
                priceTenderDTO.setDate(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getDate());
                priceTenderDTO.setDocNumber(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getDocNumber());
                priceTenderDTO.setSerial(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getSerial());
                priceTenderDTO.setStatus(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getStatus());
                if (contract.getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId() != null && contract.getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getId() != null) {
                    InvOrganizationSiteDTO tenderorgDTO = new InvOrganizationSiteDTO();
                    tenderorgDTO.setId(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getId());
                    tenderorgDTO.setCode(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getCode());
                    tenderorgDTO.setName(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getName());
                    priceTenderDTO.setCustomerId(tenderorgDTO);
                }
                if (contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId() != null && contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getId() != null) {
                    RscTenderDTO tenderDTO = new RscTenderDTO();
                    tenderDTO.setId(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getId());
                    tenderDTO.setDocNumber(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getDocNumber());
                    tenderDTO.setDate(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getDate());
                    tenderDTO.setSerial(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getSerial());
                    tenderDTO.setRemark(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getRemark());
                    if (contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId() != null && contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getId() != null) {
                        InvOrganizationSiteDTO tendersiteDTO = new InvOrganizationSiteDTO();
                        tendersiteDTO.setId(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getId());
                        tendersiteDTO.setCode(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getCode());
                        tendersiteDTO.setName(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getName());
                        tenderDTO.setCustomerId(tendersiteDTO);
                    }
                    priceTenderDTO.setRscTenderDTO(tenderDTO);
                    qotationDTO.setRscPriceTenderId(priceTenderDTO);

                }
            }
            if (contract.getRscQotationPriceTenderId().getCustomerId() != null && contract.getRscQotationPriceTenderId().getCustomerId().getId() != null) {
                InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
                siteDTO.setId(contract.getSiteId() != null ? contract.getSiteId().getId() : null);
                siteDTO.setCode(contract.getSiteId() != null ? contract.getSiteId().getCode() : null);
                siteDTO.setName(contract.getSiteId() != null ? contract.getSiteId().getName() : null);
                qotationDTO.setCustomerId(siteDTO);
                if (contract.getRscQotationPriceTenderId().getRscPriceTenderId() != null && contract.getRscQotationPriceTenderId().getRscPriceTenderId().getId() != null) {
                    RscPriceTenderDTO priceTenderDTO = new RscPriceTenderDTO();
                    priceTenderDTO.setId(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getId());
                    priceTenderDTO.setDate(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getDate());
                    priceTenderDTO.setDocNumber(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getDocNumber());
                    priceTenderDTO.setSerial(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getSerial());
                    priceTenderDTO.setStatus(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getStatus());

                    if (contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId() != null && contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getId() != null) {
                        RscTenderDTO tenderDTO = new RscTenderDTO();
                        tenderDTO.setId(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getId());
                        tenderDTO.setDocNumber(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getDocNumber());
                        tenderDTO.setDate(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getDate());
                        tenderDTO.setSerial(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getSerial());
                        tenderDTO.setRemark(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getRemark());
                        if (contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId() != null && contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getId() != null) {
                            InvOrganizationSiteDTO tendersiteDTO = new InvOrganizationSiteDTO();
                            tendersiteDTO.setId(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getId());
                            tendersiteDTO.setCode(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getCode());
                            tendersiteDTO.setName(contract.getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getName());
                            tenderDTO.setCustomerId(siteDTO);
                        }
                        priceTenderDTO.setRscTenderDTO(tenderDTO);
                        qotationDTO.setRscPriceTenderId(priceTenderDTO);

                    }
                }

            }

            dTO.setRscQotationPriceTenderId(qotationDTO);
        }

        dTO.setDate(contract.getDate());
        dTO.setDocumentNumber(contract.getDocumentNumber());
        dTO.setContractTotal(contract.getContractTotal());
        dTO.setSerial(contract.getSerial());
        dTO.setStatus(contract.getStatus() != null ? contract.getStatus() : 0);
        if (dTO.getStatus() == null || dTO.getStatus() == 0) {
            dTO.setStatusText("لم يتم");
        } else {
            dTO.setStatusText("تم");
        }
        dTO.setId(contract.getId());
        dTO.setFinallReceivedDate(contract.getFinallReceivedDate());
        dTO.setInitialPercentage(contract.getInitialPercentage());
        dTO.setInitialReceivedDate(contract.getInitialReceivedDate());
        dTO.setInitialValue(contract.getInitialValue());
        dTO.setCreatedDate(contract.getCreationDate());
        dTO.setRemarks(contract.getRemarks());
        dTO.setCreatedBy(contract.getCreatedBy().getId());
        if (!contractSet.isEmpty() && contractSet.contains(contract.getId())) {
            dTO.setActive(Boolean.TRUE);
        } else {
            dTO.setActive(Boolean.FALSE);
        }

    }

    public RscContractRegister mapFromDTO(RscContractRegisterDTO dTO, IsagUser isagUser) {
        RscContractRegister contract = new RscContractRegister();
        contract.setId(dTO.getId());
        if (dTO.getSiteId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dTO.getSiteId().getId());
            invOrganizationSite.setCode(dTO.getSiteId().getCode());
            invOrganizationSite.setName(dTO.getSiteId().getName());
            contract.setSiteId(invOrganizationSite);
        }

        if (dTO.getTenderId() != null && dTO.getTenderId().getId() != null) {
            RscTender tender = new RscTender();
            tender.setId(dTO.getTenderId().getId());
            contract.setTenderId(tender);
        }

        if (dTO.getRscQotationPriceTenderId() != null && dTO.getRscQotationPriceTenderId().getId() != null) {
            RscQotationPriceTender qotation = new RscQotationPriceTender();
            qotation.setId(dTO.getRscQotationPriceTenderId().getId());
            qotation.setDocNumber(dTO.getRscQotationPriceTenderId().getDocNumber());

            qotation.setSerial(dTO.getRscQotationPriceTenderId().getSerial());
            qotation.setQotationStatus(dTO.getRscQotationPriceTenderId().getStatus());

            qotation.setRemark(dTO.getRscQotationPriceTenderId().getRemark());
            qotation.setDate(dTO.getRscQotationPriceTenderId().getDate());

//            if (dTO.getRscQotationPriceTenderId().getCustomerId() != null && dTO.getRscQotationPriceTenderId().getCustomerId().getId() != null) {
//                InvOrganizationSite orgSite = new InvOrganizationSite();
//                orgSite.setId(dTO.getSiteId().getId());
//                orgSite.setCode(dTO.getSiteId().getCode());
//                orgSite.setName(dTO.getSiteId().getName());
//                contract.setSiteId(orgSite);
//                if (dTO.getRscQotationPriceTenderId().getRscPriceTenderId() != null && dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getId() != null) {
//                    RscPriceTender priceTender = new RscPriceTender();
//                    priceTender.setId(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getId());
//                    priceTender.setDate(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getDate());
//                    priceTender.setDocNumber(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getDocNumber());
//                    priceTender.setSerial(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getSerial());
//                    priceTender.setStatus(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getStatus());
//
//                    if (dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO() != null && dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getId() != null) {
//                        RscTender tender = new RscTender();
//                        tender.setId(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getId());
//                        tender.setDocNumber(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getDocNumber());
//                        tender.setDate(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getDate());
//                        tender.setSerial(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getSerial());
//                        tender.setRemark(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getRemark());
//                        if (dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getCustomerId() != null && dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getCustomerId().getId() != null) {
//                            InvOrganizationSite tendersite = new InvOrganizationSite();
//                            tendersite.setId(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getCustomerId().getId());
//                            tendersite.setCode(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getCustomerId().getCode());
//                            tendersite.setName(dTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getCustomerId().getName());
//                            tender.setCustomerId(tendersite);
//                        }
//                        priceTender.setTenderId(tender);
//                        qotation.setRscPriceTenderId(priceTender);
//
//                    }
//                }
//
//            }
            contract.setRscQotationPriceTenderId(qotation);
        }

        contract.setDate(dTO.getDate());
        contract.setDocumentNumber(dTO.getDocumentNumber());
        contract.setContractTotal(dTO.getContractTotal());
        contract.setFinallReceivedDate(dTO.getFinallReceivedDate());
        contract.setInitialPercentage(dTO.getInitialPercentage());
        contract.setInitialReceivedDate(dTO.getInitialReceivedDate());
        contract.setInitialValue(dTO.getInitialValue());
        contract.setId(dTO.getId());
        contract.setSerial(dTO.getSerial());
        contract.setStatus(dTO.getStatus());
        contract.setRemarks(dTO.getRemarks());

        if (dTO.getId() == null) {
            contract.setCreatedBy(isagUser);
            contract.setCreationDate(new Date());
        } else {
            IsagUser user = new IsagUser();
            user.setId(dTO.getCreatedBy());
            contract.setCreatedBy(user);
            contract.setCreationDate(dTO.getCreatedDate());
            contract.setModificationDate(new Date());
            contract.setModifiedBy(isagUser);
        }

        if (isagUser != null) {
            contract.setBranchId(isagUser.getBranchId());
            contract.setCompanyId(isagUser.getCompanyId());
        }

        return contract;
    }

    private void returnListDTO(List<RscContractRegister> list, List<RscContractRegisterDTO> dTOList) {
        if (!list.isEmpty() && list.get(0) != null && list.get(0).getBranchId() != null) {
            contractSet = restrictionQuantitiesService.getContractByBranch(list.get(0).getBranchId().getId());
        }
        for (RscContractRegister contract : list) {
            dTOList.add(mapTODTO(contract, null, false));
        }
    }
}
