/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.api.dto.DistributionApiSearch;
import com.isag.businessservice.reports.SupplierAccountService;
import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.bussinessservice.global.Utility;
import com.isag.core.GenericDAO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.GmSubscriptionsDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.Branch;
import com.isag.entity.GlBank;
import com.isag.entity.GmSubscriptions;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagUser;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class GmSubscriptionsServiceImpl implements GmSubscriptionsService {

    @EJB
    private GenericDAO dao;
    @EJB
    GmSubscriptionsDetailService gmSubscriptionsDTODetailsService;
    @EJB
    private GenericService genericService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private GmGlobalService gmGlobalService;
    @EJB
    SupplierAccountService supplierAccountService;
    @EJB
    private GlBankTransactionService glBankTransactionService;

    InvPurchaseReturnSave invPurchaseReturnSave;

    @Override
    public List<GmSubscriptionsDTO> getALLGmSubscriptionsDTOByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(GmSubscriptionsDTO.class, companyId);
    }

    @Override
    public List<GmSubscriptionsDTO> getALLGmSubscriptionsDTOByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT e FROM GmSubscriptions e WHERE e.branchId.id = :branchId ", params);
    }

    @Override
    public GmSubscriptionsDTO findGmSubscriptionsDTOById(GmSubscriptionsDTO gmSubscriptionsDTODto) {
        GmSubscriptions gmSubscriptions = dao.findEntityById(GmSubscriptions.class, gmSubscriptionsDTODto.getId());
        GmSubscriptionsDTO gmSubscriptionsDTODTO = mapToDTO(gmSubscriptions, gmSubscriptionsDTODto);
        return gmSubscriptionsDTODTO;
    }
    
    @Override
    public GmSubscriptionsDTO findGmSubscriptionsDTOBySerial(GmSubscriptionsDTO gmSubscriptionsDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", gmSubscriptionsDTO.getType());
        params.put("serial", gmSubscriptionsDTO.getSerial());
        List<GmSubscriptions> gmSubscriptionsDTOList = dao.findListByQuery("SELECT e FROM GmSubscriptions e WHERE e.branchId.id = :branchId AND e.type = :type AND e.serial = :serial ", params);
        if (gmSubscriptionsDTOList != null && !gmSubscriptionsDTOList.isEmpty()) {
            GmSubscriptionsDTO gmSubscriptionsDTODTO = mapToDTO(gmSubscriptionsDTOList.get(0), gmSubscriptionsDTO);
            return gmSubscriptionsDTODTO;
        }
        return null;
    }
    
    private GmSubscriptionsDTO mapToDTO(GmSubscriptions entity) {

        GmSubscriptionsDTO gmSubscriptionsDTODTO = new GmSubscriptionsDTO();

        initData(gmSubscriptionsDTODTO, entity);

        return gmSubscriptionsDTODTO;
    }

    //mapToEntity
    private GmSubscriptions mapToEntity(GmSubscriptionsDTO dto, IsagUser isagUser) {

        GmSubscriptions entity = new GmSubscriptions();


        if (dto.getOrganizationSiteId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dto.getOrganizationSiteId().getId());
            entity.setOrganizationSiteId(invOrganizationSite);
        }

        if (dto.getGlBankId() != null) {
            GlBank glBank = new GlBank();
            glBank.setId(dto.getGlBankId().getId());
            entity.setGlBankId(glBank);
        }
        entity.setId(dto.getId());
        entity.setSerial(dto.getSerial());
        entity.setSerialTax(dto.getSerialTax());
        entity.setDate(dto.getDate());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setDiscount(dto.getDiscount());
        entity.setDiscountType(dto.getDiscountType());
        entity.setRemarks(dto.getRemarks());
        entity.setPostFlag(0);
        entity.setType(dto.getType());
        entity.setExtraCost(dto.getExtraCost() != null ? dto.getExtraCost() : BigDecimal.ZERO);
        
        entity.setTaxFlagFinal(dto.getTaxFlagFinal());
        entity.setPayCash(dto.getPayed());
        entity.setPayNet(dto.getPayNet());

        if (entity.getCreatedBy() != null) {
            IsagUser user = new IsagUser();
            user.setId(dto.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(dto.getCreatedDate());
            entity.setModifiedBy(isagUser);
            entity.setModificationDate(new Date());
            if (entity.getCompanyId() != null) {
                IsagCompany company = new IsagCompany();
                company.setId(dto.getCompanyId());
                entity.setCompanyId(company);
            }

            if (entity.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(dto.getBranchId());
                entity.setBranchId(branch);
            }
        } else {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
            entity.setCompanyId(isagUser.getCompanyId());
            entity.setBranchId(isagUser.getBranchId());
        }
        return entity;
    }

    public GmSubscriptionsDTO validateSave(GmSubscriptionsDTO gmSubscriptionsDTO) {
        StringBuilder error = new StringBuilder();
       

        if (gmSubscriptionsDTO.getOrganizationSiteId() == null) {
            error.append("\n يجب أختيار عميل ");
        }

        if (!error.toString().isEmpty()) {
            gmSubscriptionsDTO.setMsg(error.toString());
        }

        return gmSubscriptionsDTO;
    }
    @Override
    public GmSubscriptionsDTO save(GmSubscriptionsDTO gmSubscriptionsDTO, IsagUser isagUser) {
        try {
            gmSubscriptionsDTO = validateSave(gmSubscriptionsDTO);

            GmSubscriptions gmSubscriptions;
            if (gmSubscriptionsDTO != null && (gmSubscriptionsDTO.getMsg() == null || gmSubscriptionsDTO.getMsg().isEmpty())) {
                gmSubscriptions = mapToEntity(gmSubscriptionsDTO, isagUser);
                if (gmSubscriptionsDTO.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    gmSubscriptionsDTO = dao.updateEntity(gmSubscriptionsDTO);
                } else {
                    // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
                    Integer serial = genericService.getMaxGenericSerialByType(GmSubscriptions.class, gmSubscriptionsDTO.getBranchId(), gmSubscriptionsDTO.getType());
                    if (serial == null) {
                        gmSubscriptionsDTO.setMsg("من فضلك اعد حفظ الفاتورة");
                        dao.rollbackQuery();
                        return gmSubscriptionsDTO;
                    }
                    gmSubscriptionsDTO.setSerial(serial);
                    // بجيب السيريال الخاص بالفواتير الضريبية
//                    if (gmSubscriptionsDTO.getTaxflag()) {
//                        Integer serialTax = getMaxSerialTaxForInvoice(gmSubscriptionsDTO.getBranchId().getId(), gmSubscriptionsDTO.getInvInventoryId().getId(), gmSubscriptionsDTO.getType(), gmSubscriptionsDTO.getTaxFlagFinal() == null ? Boolean.FALSE : gmSubscriptionsDTO.getTaxFlagFinal());
//                        gmSubscriptionsDTO.setSerialTax(serialTax);
//                    }

                    gmSubscriptions = dao.updateEntity(gmSubscriptions);
                    gmSubscriptionsDTO.setId(gmSubscriptions.getId());
                    if (gmSubscriptionsDTO.getPayed() != null && gmSubscriptionsDTO.getPayed().compareTo(BigDecimal.ZERO) == 1) {
                        InvInvoiceDTO invInvoiceDTO = new InvInvoiceDTO();
                        invInvoiceDTO.setPayed(gmSubscriptionsDTO.getPayed());
                        invInvoiceDTO.setDate(gmSubscriptionsDTO.getDate());
                        invInvoiceDTO.setGlBankId(gmSubscriptionsDTO.getGlBankId());
                        invInvoiceDTO.setRate(BigDecimal.ONE);
                        invInvoiceDTO.setRemarks(gmSubscriptionsDTO.getRemarks());
                        invInvoiceDTO.setTotalNet(gmSubscriptionsDTO.getTotalNet());
                        invInvoiceDTO.setTotalNetAfterDiscount(gmSubscriptionsDTO.getTotalNetAfterDiscount());
                        invInvoiceDTO.setPayed(gmSubscriptionsDTO.getPayed());
                        invInvoiceDTO.setRemain(gmSubscriptionsDTO.getRemain());
                        invInvoiceDTO.setSerial(gmSubscriptionsDTO.getSerial());
                        invInvoiceDTO.setOrganizationSiteId(gmSubscriptionsDTO.getOrganizationSiteId());
                        glBankTransactionService.addCash(invInvoiceDTO, isagUser);
                    }
                }

                gmSubscriptionsDTODetailsService.deleteGmSubscriptionsDetailDTOListDTO(gmSubscriptionsDTO.getGmSubscriptionsDetailDeletedList());

                String error = gmSubscriptionsDTODetailsService.addGmSubscriptionsDetailDTOsDTO(gmSubscriptionsDTO.getGmSubscriptionsDetailList(), gmSubscriptionsDTO, isagUser);

                if (error != null && !error.isEmpty()) {
                    gmSubscriptionsDTO.setMsg(error);
                    dao.rollbackQuery();
                } else {
                    gmSubscriptionsDTO = mapToDTO(gmSubscriptions, gmSubscriptionsDTO);
                    gmSubscriptionsDTO = gmGlobalService.updateSummition(gmSubscriptionsDTO);
                }

            }
        } catch (Exception e) {
            gmSubscriptionsDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return gmSubscriptionsDTO;
    }
    
    private GmSubscriptionsDTO findGmSubscriptionsDTOByGmSubscriptionsDTOId(Integer serial, Integer type, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("serial", serial);
        params.put("branchId", branchId);
        params.put("type", type);
        List<GmSubscriptionsDTO> details = dao.findListByQuery("SELECT e FROM GmSubscriptionsDTO e WHERE e.serial = :serial AND e.branchId.id = :branchId AND e.type = :type", params);
        return (details == null || details.isEmpty()) ? null : details.get(0);
    }
    private GmSubscriptionsDTO mapToDTO(GmSubscriptions entity, GmSubscriptionsDTO dto) {

        dto = initData(dto, entity);

        return dto;
    }

    private GmSubscriptionsDTO initData(GmSubscriptionsDTO gmSubscriptionsDTO, GmSubscriptions entity) {
        gmSubscriptionsDTO.setId(entity.getId());
        gmSubscriptionsDTO.setSerial(entity.getSerial());
        gmSubscriptionsDTO.setSerialTax(entity.getSerialTax());
        gmSubscriptionsDTO.setIsEdit(Boolean.TRUE);
        gmSubscriptionsDTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        gmSubscriptionsDTO.setDate(entity.getDate());
        gmSubscriptionsDTO.setStartDate(entity.getStartDate());
        gmSubscriptionsDTO.setEndDate(entity.getEndDate());
        gmSubscriptionsDTO.setTaxFlagFinal(entity.getTaxFlagFinal());
        gmSubscriptionsDTO.setRemarks(entity.getRemarks());
        gmSubscriptionsDTO.setDiscount(entity.getDiscount());
        gmSubscriptionsDTO.setDiscountType(entity.getDiscountType());
        gmSubscriptionsDTO.setPostFlag(entity.getPostFlag());
        if (entity.getOrganizationSiteId() != null) {
            gmSubscriptionsDTO.setOrganizationSiteId(organizationSiteService.mapToDTOSmall(entity.getOrganizationSiteId()));// here
        }

        gmSubscriptionsDTO.setExtraCost(entity.getExtraCost());

        if (entity.getGlBankId() != null) {
            GlBankDTO glBankDTO = new GlBankDTO();
            glBankDTO.setId(entity.getGlBankId().getId());
            glBankDTO.setName(entity.getGlBankId().getName());
            glBankDTO.setCode(entity.getGlBankId().getCode());
            gmSubscriptionsDTO.setGlBankId(glBankDTO);
        }

        gmSubscriptionsDTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        gmSubscriptionsDTO.setCreatedDate(entity.getCreationDate());
        gmSubscriptionsDTO.setIndex(entity.getSerial());
        gmSubscriptionsDTO.setType(entity.getType());
        gmSubscriptionsDTO.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        gmSubscriptionsDTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
        gmSubscriptionsDTO.setPayNet(entity.getPayNet());
        gmSubscriptionsDTO.setPayNet(entity.getPayNet());
        gmSubscriptionsDTO.setPayed(entity.getPayCash().add(entity.getPayNet()));
        Date today = Date.from(Instant.now());
        if(today.after(entity.getStartDate()) && today.before(entity.getEndDate())){
        gmSubscriptionsDTO.setStatusName("ساري");
        
        }else {
        gmSubscriptionsDTO.setStatusName("منتهي");
        }

        List<GmSubscriptionsDetailDTO> gmSubscriptionsDTODetailsListDTO = gmSubscriptionsDTODetailsService.getAllGmSubscriptionsDetailDTOsByGmSubscriptionsDTOIdDTO(gmSubscriptionsDTO);
        gmSubscriptionsDTO.setGmSubscriptionsDetailList(gmSubscriptionsDTODetailsListDTO);
        gmSubscriptionsDTO.setGmSubscriptionsDetailDeletedList(new ArrayList<GmSubscriptionsDetailDTO>());
        return gmSubscriptionsDTO;
    }

    @Override
    public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("type", type);
        filters.put("branchId", branchId);
        builder.append("SELECT count(s) FROM GmSubscriptions s WHERE   s.branchId.id = :branchId AND s.type =:type ");
        appendFilter(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

    private void appendFilter(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and ( FUNCTION('DATE_FORMAT',s.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') OR  s.serial like CONCAT('%',:globalFilter ,'%') OR s.organizationSiteId.name like CONCAT('%',:globalFilter ,'%') OR s.createdBy.name like CONCAT('%',:globalFilter ,'%') OR s.remarks like CONCAT('%',:globalFilter ,'%'))";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<GmSubscriptionsDTO> getALLGmSubscriptionsDTODTOByTypeAndBranchId(Integer type,  int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("type", type);
        filters.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM GmSubscriptions s WHERE  s.branchId.id = :branchId AND s.type =:type  ");
        appendFilter(filters, builder);
        builder.append(" order by s.serial DESC");
        List<GmSubscriptions> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTOForList(details);
    }
    
     @Override
    public List<GmSubscriptionsDTO> getALLGmSubscriptionsDTODTOByCustomerId(Integer customerId) {
         Map<String, Object> filters = new HashMap<>();
        filters.put("customerId", customerId);
       
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM GmSubscriptions s WHERE  s.organizationSiteId.id =:customerId  ");
  
        List<GmSubscriptions> details = dao.findListByQuery(builder.toString(), filters);
        return returnListDTOForList(details);
    }

    private List<GmSubscriptionsDTO> returnListDTOForList(List<GmSubscriptions> gmSubscriptionsList) {
        List<GmSubscriptionsDTO> gmSubscriptionsDTOList = new ArrayList<>();
        for (GmSubscriptions gmSubscriptions : gmSubscriptionsList) {
            GmSubscriptionsDTO gmSubscriptionsDTO = new GmSubscriptionsDTO();
            gmSubscriptionsDTO = mapToDTO(gmSubscriptions, gmSubscriptionsDTO);
            gmSubscriptionsDTOList.add(gmSubscriptionsDTO);
        }
        return gmSubscriptionsDTOList;
    }

    private GmSubscriptionsDTO initDataForList(GmSubscriptions gmSubscriptions, GmSubscriptionsDTO gmSubscriptionsDTO) {
        commonData(gmSubscriptionsDTO, gmSubscriptions);
        return gmSubscriptionsDTO;
    }

    private void commonData(GmSubscriptionsDTO dto, GmSubscriptions entity) {
        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());
        dto.setDate(entity.getDate());
        dto.setIndex(entity.getSerial());
        dto.setPostFlag(entity.getPostFlag());
        dto.setRemarks(entity.getRemarks());
        if (entity.getCreatedBy() != null) {
            dto.setCreatedByName(entity.getCreatedBy().getName());
        }
    }

    @Override
    public void deleteGmSubscriptionsDTODTO(GmSubscriptionsDTO gmSubscriptionsDTO, Integer isPurchaseOrSales) {
        if (gmSubscriptionsDTO != null && gmSubscriptionsDTO.getId() > 0) {
            gmSubscriptionsDTODetailsService.deleteGmSubscriptionsDetailDTOListDTO(gmSubscriptionsDTO.getId());
            GmSubscriptionsDTO gmSubscriptionsDTO1 = new GmSubscriptionsDTO();
            gmSubscriptionsDTO1.setId(gmSubscriptionsDTO != null ? gmSubscriptionsDTO.getId() : null);
            dao.deleteEntity(gmSubscriptionsDTO1);
        }
    }

    @Override
    public void deleteGmSubscriptionsDTODTO(Integer invoiceId) {
        if (invoiceId != null) {
            gmSubscriptionsDTODetailsService.deleteGmSubscriptionsDetailDTOListDTO(invoiceId);
            GmSubscriptions gmSubscriptions = new GmSubscriptions();
            gmSubscriptions.setId(invoiceId);
            dao.deleteEntity(gmSubscriptions);
            
        }
    }

    @Override
    public BigDecimal netPay(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId) {
        if (bankId != null) {
            Map<String, Object> params = new HashMap();
            params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
            StringBuilder query = new StringBuilder();
            BigDecimal sales = findNetPay(query, cashAccountSettlementSearchBean, params, 1, bankId);
            BigDecimal returnSales = findNetPay(query, cashAccountSettlementSearchBean, params, 3, bankId);
            BigDecimal finalPay = sales.subtract(returnSales);
            return finalPay;
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal findNetPay(StringBuilder query, SubAccountSummarySearchBean cashAccountSettlementSearchBean, Map<String, Object> params, Integer type, Integer bankId) {
        params.put("type", type);
        query.append("select sum(i.payNet) from GmSubscriptionsDTO i where i.branchId.id = :branchId AND i.type = :type ");
        if (bankId != null && bankId > 0) {
            query.append(" and i.glBankId.id = :glBankFrom");
            params.put("glBankFrom", bankId);
        }

        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            query.append(" and i.date >= '").append(d3).append("'");
        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
            query.append(" and i.date<= '").append(d3).append("'");
        }
        BigDecimal list = dao.findEntityByQuery(query.toString(), params);
        if (list != null) {
            return list == null ? BigDecimal.ZERO : list;
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal returnInvInvoiceSummarySummationDuration(Integer branchId, Integer type, Date dateFrom, Date dateTo) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);
        return dao.findEntityByQuery("select sum(i.payCash) from GmSubscriptionsDTO i where i.branchId.id = :branchId AND i.type = :type And i.date BETWEEN :dateFrom AND :dateTo", params);

    }


    private Integer findInvoiceCount(DistributionApiSearch distributionApiSearch, Integer type) {
        Map<String, Object> params = new HashMap();
        StringBuilder query = new StringBuilder();
        params.put("type", type);
        params.put("branchId", distributionApiSearch.getBranchId());
        query.append("select count(id) from GmSubscriptions i where i.branchId.id = :branchId AND i.type = :type  ");

        if (distributionApiSearch.getDateFrom() != null) {
            params.put("dateFrom", Utility.returnFromDate(distributionApiSearch.getDateFrom()));
            query.append(" AND i.date >= :dateFrom");
            if (!distributionApiSearch.getDateType()) {
                params.put("dateTo", Utility.returnToDate(distributionApiSearch.getDateFrom()));
                query.append(" AND i.date <= :dateTo");
            }
        } else {
            params.put("dateFrom", Utility.returnFromDate(new Date()));
            query.append(" AND i.date >= :dateFrom");
            params.put("dateTo", Utility.returnToDate(new Date()));
            query.append(" AND i.date <= :dateTo");
        }

        if (distributionApiSearch.getDateTo() != null) {
            params.put("dateTo", Utility.returnToDate(distributionApiSearch.getDateTo()));
            query.append(" AND i.date <= :dateTo");
        }

        if (distributionApiSearch.getInventoryCodeFrom() != null) {
            params.put("InventoryCodeFrom", distributionApiSearch.getInventoryCodeFrom());
            query.append(" AND i.invInventoryId.code >= :InventoryCodeFrom");
            if (!distributionApiSearch.getInventoryType()) {
                params.put("InventoryCodeTo", distributionApiSearch.getInventoryCodeFrom());
                query.append(" AND i.invInventoryId.code <= :InventoryCodeTo");
            }
        }

        if (distributionApiSearch.getInventoryCodeTo() != null) {
            params.put("InventoryCodeTo", distributionApiSearch.getInventoryCodeTo());
            query.append(" AND i.invInventoryId.code <= :InventoryCodeTo");
        }
        List<Integer> details = dao.findListByQuery(query.toString(), params);
        if (details != null && !details.isEmpty()) {
            return details.get(0);
        }
        return 0;
    }

    private BigDecimal findInvoiceNet(DistributionApiSearch distributionApiSearch, Integer type) {
        Map<String, Object> params = new HashMap();
        StringBuilder query = new StringBuilder();
        params.put("type", type);
        params.put("branchId", distributionApiSearch.getBranchId());
        query.append("select sum(ipid.net) , ipid.gmSubscriptionsDTOId.discountType , ipid.gmSubscriptionsDTOId.discount  from GmSubscriptionsDTODetail ipid  where ipid.gmSubscriptionsDTOId.branchId.id = :branchId AND ipid.gmSubscriptionsDTOId.type = :type  ");

        if (distributionApiSearch.getDateFrom() != null) {
            params.put("dateFrom", Utility.returnFromDate(distributionApiSearch.getDateFrom()));
            query.append(" AND ipid.gmSubscriptionsDTOId.date >= :dateFrom");
            if (!distributionApiSearch.getDateType()) {
                params.put("dateTo", Utility.returnToDate(distributionApiSearch.getDateFrom()));
                query.append(" AND ipid.gmSubscriptionsDTOId.date <= :dateTo");
            }
        } else {
            params.put("dateFrom", Utility.returnFromDate(new Date()));
            query.append(" AND ipid.gmSubscriptionsDTOId.date >= :dateFrom");
            params.put("dateTo", Utility.returnToDate(new Date()));
            query.append(" AND ipid.gmSubscriptionsDTOId.date <= :dateTo");
        }

        if (distributionApiSearch.getDateTo() != null) {
            params.put("dateTo", Utility.returnToDate(distributionApiSearch.getDateTo()));
            query.append(" AND ipid.gmSubscriptionsDTOId.date <= :dateTo");
        }

        if (distributionApiSearch.getInventoryCodeFrom() != null) {
            params.put("InventoryCodeFrom", distributionApiSearch.getInventoryCodeFrom());
            query.append(" AND ipid.gmSubscriptionsDTOId.invInventoryId.code >= :InventoryCodeFrom");
            if (!distributionApiSearch.getInventoryType()) {
                params.put("InventoryCodeTo", distributionApiSearch.getInventoryCodeFrom());
                query.append(" AND ipid.gmSubscriptionsDTOId.invInventoryId.code <= :InventoryCodeTo");
            }
        }

        if (distributionApiSearch.getInventoryCodeTo() != null) {
            params.put("InventoryCodeTo", distributionApiSearch.getInventoryCodeTo());
            query.append(" AND ipid.gmSubscriptionsDTOId.invInventoryId.code <= :InventoryCodeTo");
        }

        query.append(" group by ipid.gmSubscriptionsDTOId.id ");
        List<Object[]> res = dao.findListByQuery(query.toString(), params);
        Iterator it = res.iterator();
        BigDecimal result = BigDecimal.ZERO;
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            BigDecimal summution = (BigDecimal) line[0];
            Integer discountType = (Integer) line[0];
            BigDecimal discount = (BigDecimal) line[0];
            if (discountType != null && discountType == 0) {
                result = result.add(summution.subtract(discount));
            } else if (discountType != null && discountType == 1) {
                result = result.add(summution.subtract(((discount.divide(new BigDecimal(100), 3, RoundingMode.HALF_UP)).multiply(summution))));
            }
        }
        return result;
    }


}
