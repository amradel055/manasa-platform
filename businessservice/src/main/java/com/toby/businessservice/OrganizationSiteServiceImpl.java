/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.API.InvOrganizationSiteDTOAPI;
import com.toby.businessservice.reports.searchBean.CrmLeadsSearchBean;
import com.toby.businessservice.reports.searchBean.SupplierAccountSearchBean;
import com.toby.businessservice.reports.searchBean.invOrganizationSiteSearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.ChartDTO;
import com.toby.dto.ChartDataDTO;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.CrmCustomerInterestedProductsDTO;
import com.toby.dto.CrmEventDTO;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.dto.OfferCompanyDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.Currency;
import com.toby.entity.Employee;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InventoryDelegator;
import com.toby.entity.OfferCompany;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 *
 * @author hq002
 */
@Stateless
public class OrganizationSiteServiceImpl implements OrganizationSiteService {

    @EJB
    private GenericDAO dao;
    @EJB
    private ProProductionMovementDetailService proProductionMovementDetailService;
    @EJB
    private CurrencyServiceDTO currencyServiceDTO;
    @EJB
    CRMCustomerInterestedProductsServiceDTO cRMCustomerInterestedProductsServiceDTO;
    @EJB
    private CRMEventServiceDTO cRMEventServiceDTO;
    @EJB
    private EmployeeService employeeService;
    @EJB
    private InvInventoryService invInventoryService;

    @Override
    public void updateOrganizationSite(InvOrganizationSiteDTO organizationSiteDTO) {
        if (organizationSiteDTO != null && organizationSiteDTO.getId() != null) {
            InvOrganizationSite entity = dao.findEntityById(InvOrganizationSite.class, organizationSiteDTO.getId());
            entity.setMobile(organizationSiteDTO.getMobile());
            entity.setEmail(organizationSiteDTO.getEmail());
            entity.setLength(organizationSiteDTO.getLength());
            entity.setShoulder(organizationSiteDTO.getShoulder());
            entity.setStep(organizationSiteDTO.getStep());
            entity.setName(organizationSiteDTO.getName());
            dao.updateEntity(entity);
        }
    }

    @Override
    public void updateOrganizationSiteCompany(InvOrganizationSiteDTO organizationSiteDTO) {
        if (organizationSiteDTO != null && organizationSiteDTO.getId() != null) {
            InvOrganizationSite entity = dao.findEntityById(InvOrganizationSite.class, organizationSiteDTO.getId());
            if (organizationSiteDTO.getOfferCompanyDTO() != null && organizationSiteDTO.getOfferCompanyDTO().getId() != null) {
                OfferCompany offerCompany = new OfferCompany();
                offerCompany.setId(organizationSiteDTO.getOfferCompanyDTO().getId());
                entity.setOfferCompanyId(offerCompany);
            } else {
                entity.setOfferCompanyId(null);
            }
            entity.setMobile(organizationSiteDTO.getMobile());
            entity.setName(organizationSiteDTO.getName());
            entity.setOpenBalanceDebit(organizationSiteDTO.getOpenBalanceDebit());
            entity.setOpenBalanceCredit(organizationSiteDTO.getOpenBalanceCredit());
            dao.updateEntity(entity);
        }
    }

    @Override
    public List<InvOrganizationSiteDTO> getDetailsByCustomerId(InvOrganizationSiteDTO organizationSiteDTO) {

        List<InvOrganizationSiteDTO> organizationSiteDTOList = new ArrayList<>();
        String sql = " SELECT length,shoulder,step FROM toby.inv_organization_site\n"
                + " where id = " + organizationSiteDTO.getId() + " ";
        List<Object[]> res = dao.executeNativeQuery(sql);
        for (Object[] object : res) {
            organizationSiteDTO.setLength((BigDecimal) object[0]);
            organizationSiteDTO.setShoulder((BigDecimal) object[1]);
            organizationSiteDTO.setStep((BigDecimal) object[2]);
            organizationSiteDTOList.add(organizationSiteDTO);
        }
        return organizationSiteDTOList;
    }

    @Override
    public List<InvOrganizationSite> getInvOrganizationSiteByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(InvOrganizationSite.class, companyId);
    }

    @Override
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteByBranchId(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ORDER BY e.code", params);
        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public List<InvOrganizationSite> getInvOrganizationSiteByBranchIdPer(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ORDER BY e.code", params);
        List<InvOrganizationSite> detailsesReturn = retiveOrganizationSiteDate(detailses);
        return detailsesReturn;
    }

    @Override
    public InvOrganizationSite addOrganizationSiteNew(InvOrganizationSite invOrganizationSite) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> params2 = new HashMap<>();
        params.put("branchId", 182);
        params.put("mobile", invOrganizationSite.getMobile());
        params2.put("branchId", 182);
        params2.put("code", invOrganizationSite.getCode());
//        List<InvOrganizationSite> detailsesMobile = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.mobile = :mobile ORDER BY e.code", params);
        List<InvOrganizationSite> detailsesCode = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.code = :code ORDER BY e.code", params2);
        if (detailsesCode == null || detailsesCode.isEmpty()) {
            return dao.updateEntity(invOrganizationSite);
        }
        return null;
    }

    @Override
    public InvOrganizationSite findOrganizationSiteByMobile(InvOrganizationSite invOrganizationSite) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", 182);
        params.put("mobile", invOrganizationSite.getMobile());
//        List<InvOrganizationSite> detailsesMobile = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.mobile = :mobile ORDER BY e.code", params);
        InvOrganizationSite detailsesCode = dao.findEntityByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.mobile = :mobile ORDER BY e.code", params);

        return detailsesCode;
    }

    @Override
    public InvOrganizationSite addOrganizationSite(InvOrganizationSite invOrganizationSite) {
        return dao.updateEntity(invOrganizationSite);
    }

    @Override
    public InvOrganizationSite updateOrganizationSite(InvOrganizationSite invOrganizationSite) {
        return dao.updateEntity(invOrganizationSite);
    }

    @Override
    public void deleteOrganizationSite(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser) {
        mapToEntity(invOrganizationSiteDTO, tobyUser);
        dao.deleteEntity(mapToEntity(invOrganizationSiteDTO, tobyUser));
    }

    @Override
    public InvOrganizationSiteDTO findOrganizationSiteById(Integer invOrganizationSiteId, TobyUser tobyUser) {
        InvOrganizationSite invOrganizationSite = dao.findEntityById(InvOrganizationSite.class, invOrganizationSiteId);
        return mapToDTO(invOrganizationSite, tobyUser);
    }

    @Override
    public InvOrganizationSite findOrganizationSiteById(Integer invOrganizationSiteId) {
        return dao.findEntityById(InvOrganizationSite.class, invOrganizationSiteId);

    }

    @Override
    public List<InvOrganizationSite> getSupplierByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 1 OR e.type = 2) AND e.active = 1) ORDER BY e.code", params);
        return detailses;
    }

    @Override
    public List<InvOrganizationSite> getSupplierByBranchIdForSupplierAccountReportMB(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = new ArrayList<>();
        query.append(" SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ( ");
        if (type == 0) {
            query.append(" (e.type = 0 OR e.type = 2) ");
        } else if (type == 1) {
            query.append(" (e.type = 1 OR e.type = 2) ");
        } else if (type == 4) {
            query.append(" (e.type = 4 ) ");
        }
        //query.append("  AND e.active = 1) ORDER BY e.code ");
        query.append("  AND e.active = 1) ORDER BY e.id ");

        detailses = dao.findListByQuery(query.toString(), params);
        List<InvOrganizationSite> detailsesReturn = retiveOrganizationSiteDate(detailses);
        return detailsesReturn;
    }

    @Override
    public List<InvOrganizationSite> getSupplierByBranchIdForSupplierAccountReportMB(Integer branchId, Integer gallaryId, Integer type, SupplierAccountSearchBean supplierAccountSearchBean) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = new ArrayList<>();
        query.append(" SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ( ");
        if (type == 0) {
            query.append(" (e.type = 0 OR e.type = 2) ");
        } else if (type == 1) {
            query.append(" (e.type = 1 OR e.type = 2) ");
        } else if (type == 4) {
            query.append(" (e.type = 4 ) ");
        }

        if (supplierAccountSearchBean.getFromorganizationNameDTO() != null) {
            params.put("customerFrom", supplierAccountSearchBean.getFromorganizationNameDTO().getCode());
            query.append("AND (e.code >= :customerFrom)");
        }

        if (supplierAccountSearchBean.getToorganizationNameDTO() != null) {
            params.put("customerTo", supplierAccountSearchBean.getToorganizationNameDTO().getCode());
            query.append("AND (e.code <= :customerTo)");
        }

        if (gallaryId != null) {
            params.put("gallaryId", gallaryId);
            query.append("AND e.gallaryId.id = :gallaryId");
        }

        //query.append("  AND e.active = 1) ORDER BY e.code ");
        query.append("  AND e.active = 1) ORDER BY e.id ");

        detailses = dao.findListByQuery(query.toString(), params);
        List<InvOrganizationSite> detailsesReturn = retiveOrganizationSiteDate(detailses);
        return detailsesReturn;
    }

    private List<InvOrganizationSite> retiveOrganizationSiteDate(List<InvOrganizationSite> detailses) {
        List<InvOrganizationSite> detailsesReturn = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(detail.getId());
            invOrganizationSite.setCode(detail.getCode());
            invOrganizationSite.setName(detail.getName());
            detailsesReturn.add(invOrganizationSite);
        }
        return detailsesReturn;
    }

    @Override
    public List<InvOrganizationSite> getCustomerByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code", params);
        return detailses;
    }

    @Override
    public List<InvOrganizationSite> getorganizationSiteByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type, Integer type2) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("type2", type2);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = :type OR e.type = :type2 OR e.type = 2)");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);
        List<InvOrganizationSite> list = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {
            InvOrganizationSite site = new InvOrganizationSite();
            site.setAccountId(detail.getAccountId());
            site.setCode(detail.getCode());
            site.setCurrencyId(detail.getCurrencyId());
            site.setId(detail.getId());
            site.setName(detail.getName());
            site.setOpenBalanceCredit(detail.getOpenBalanceCredit());
            site.setOpenBalanceDebit(detail.getOpenBalanceDebit());
            site.setOpenBalanceDate(detail.getOpenBalanceDate());
            list.add(site);
        }
        return list;
    }

    @Override
    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = :type OR e.type = 2)");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);

        return mapToDTOList(detailses, null);
    }

    @Override
    public List<InvOrganizationSiteDTO> getContractorByBranchIdForGlBankModuleDTO(Integer branchId, boolean active, Integer type, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND (e.type = :type");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);

        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public List<InvOrganizationSite> getCustomerByBranchIdForReport(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type <> 1 AND e.active = 1 ORDER BY e.code", params);

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        List<InvOrganizationSite> detailsesReturn = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {

//            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            mapperFactory.classMap(InvOrganizationSite.class, InvOrganizationSite.class)
                    .exclude("type")
                    .exclude("phone")
                    .exclude("fax")
                    .exclude("email")
                    .exclude("mobile")
                    .exclude("address")
                    .exclude("postBox")
                    .exclude("discount")
                    .exclude("openBalanceDebit")
                    .exclude("openBalanceCredit")
                    .exclude("balanceLimit")
                    .exclude("remark")
                    .exclude("active")
                    .exclude("zip")
                    .exclude("contactPerson")
                    .exclude("phone2")
                    .exclude("openBalanceDate")
                    .exclude("accountId")
                    .exclude("branchId")
                    .exclude("countryId")
                    .exclude("currencyId")
                    .exclude("delegatorId")
                    .exclude("regionId")
                    .exclude("parent")
                    .exclude("supplierType")
                    .exclude("companyId")
                    .exclude("createdBy")
                    .exclude("modifiedBy")
                    .exclude("modificationDate")
                    .exclude("creationDate")
                    .byDefault().register();

            MapperFacade mapper = mapperFactory.getMapperFacade();

            InvOrganizationSite dest = mapper.map(detail, InvOrganizationSite.class);
            detailsesReturn.add(dest);
        }
        return detailsesReturn;
    }

    @Override
    public Boolean checkCodeExistence(InvOrganizationSite invOrganizationSite) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        params.put("code", invOrganizationSite.getCode());
        params.put("branchId", invOrganizationSite.getBranchId().getId());

        query.append("SELECT i FROM InvOrganizationSite i WHERE i.branchId.id = :branchId AND i.code = :code ");

        if (invOrganizationSite.getType() != 2) {
            params.put("type", invOrganizationSite.getType());
            query.append(" AND i.type = :type");
        }

        if (invOrganizationSite.getId() != null) {
            params.put("id", invOrganizationSite.getId());
            query.append(" AND i.id != :id");
        }

        query.append(" ORDER BY i.code");

        String q = query.toString();

        List<InvOrganizationSite> organizationSites = dao.findListByQuery(q, params);

        if (organizationSites != null && !organizationSites.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdDTO(TobyUser tobyUser, boolean active, Integer type) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = :type OR e.type = 2)");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);

        list = mapToDTOList(detailses, tobyUser);
        return list;
    }

    @Override
    public List<InvOrganizationSiteDTO> getcontractorByBranchIdDTO(Integer branchId, boolean active) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public InvOrganizationSite mapToEntity(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser) {
        InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
        invOrganizationSite.setAddress(invOrganizationSiteDTO.getAddress());
        invOrganizationSite.setCode(invOrganizationSiteDTO.getCode());
        invOrganizationSite.setMobile(invOrganizationSiteDTO.getMobile());
        invOrganizationSite.setFax(invOrganizationSiteDTO.getFax());
        invOrganizationSite.setId(invOrganizationSiteDTO.getId());

        invOrganizationSite.setEmployer(invOrganizationSiteDTO.getEmployer());
        invOrganizationSite.setStreet(invOrganizationSiteDTO.getStreet());
        invOrganizationSite.setCity(invOrganizationSiteDTO.getCity());
        invOrganizationSite.setCountry(invOrganizationSiteDTO.getCountry());
        invOrganizationSite.setJobNamed(invOrganizationSiteDTO.getJobNamed());

        invOrganizationSite.setEmail(invOrganizationSiteDTO.getEmail());
        invOrganizationSite.setMarkDisable(invOrganizationSiteDTO.getMarkDisable());
        invOrganizationSite.setMarkEdit(invOrganizationSiteDTO.getMarkEdit());

        invOrganizationSite.setName(invOrganizationSiteDTO.getName());
        invOrganizationSite.setType(invOrganizationSiteDTO.getType());
        invOrganizationSite.setActive(invOrganizationSiteDTO.getActive());

        invOrganizationSite.setBalanceLimit(invOrganizationSiteDTO.getBalanceLimit());

        invOrganizationSite.setContactPerson(invOrganizationSiteDTO.getContactPerson());
        invOrganizationSite.setDiscount(invOrganizationSiteDTO.getDiscount());

        invOrganizationSite.setOpenBalanceCredit(invOrganizationSiteDTO.getOpenBalanceCredit());
        invOrganizationSite.setOpenBalanceDate(invOrganizationSiteDTO.getOpenBalanceDate());
        invOrganizationSite.setOpenBalanceDebit(invOrganizationSiteDTO.getOpenBalanceDebit());
        invOrganizationSite.setZip(invOrganizationSiteDTO.getZip());
        invOrganizationSite.setPhone(invOrganizationSiteDTO.getPhone());
        invOrganizationSite.setPhone2(invOrganizationSiteDTO.getPhone2());
        invOrganizationSite.setPostBox(invOrganizationSiteDTO.getPostBox());
        invOrganizationSite.setRemark(invOrganizationSiteDTO.getRemark());
        invOrganizationSite.setLength(invOrganizationSiteDTO.getLength());
        invOrganizationSite.setShoulder(invOrganizationSiteDTO.getShoulder());
        invOrganizationSite.setStep(invOrganizationSiteDTO.getStep());
        invOrganizationSite.setCompanyName(invOrganizationSiteDTO.getCompanyName());
        if (invOrganizationSiteDTO.getLearnAboutCompanyFrom() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invOrganizationSiteDTO.getLearnAboutCompanyFrom().getId());
            invOrganizationSite.setLearnAboutCompanyfrom(symbol);
        }

        if (invOrganizationSiteDTO.getGallaryIdAPI() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invOrganizationSiteDTO.getGallaryIdAPI());
            invOrganizationSite.setGallaryId(invInventory);
        }

        if (invOrganizationSiteDTO.getGallaryIdDTO() != null && invOrganizationSiteDTO.getGallaryIdDTO().getId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invOrganizationSiteDTO.getGallaryIdDTO().getId());
            invOrganizationSite.setGallaryId(invInventory);
        }

        if (invOrganizationSiteDTO.getParent() != null) {
            InvOrganizationSite organizationSite1 = new InvOrganizationSite();
            organizationSite1.setId(invOrganizationSiteDTO.getParent().getId());
            invOrganizationSite.setParent(organizationSite1);
        }
        if (invOrganizationSiteDTO.getRegionId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invOrganizationSiteDTO.getRegionId().getId());
            invOrganizationSite.setRegionId(symbol);
        }

        if (invOrganizationSiteDTO.getCountryId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invOrganizationSiteDTO.getCountryId().getId());
            invOrganizationSite.setCountryId(symbol);
        }

        if (invOrganizationSiteDTO.getContractorType() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invOrganizationSiteDTO.getContractorType().getId());
            invOrganizationSite.setContractorType(symbol);
        }

        if (invOrganizationSiteDTO.getAccountIdAPI() != null) {
            GlAccount account = new GlAccount();
            account.setId(invOrganizationSiteDTO.getAccountIdAPI());
            invOrganizationSite.setAccountId(account);

        }
        if (invOrganizationSiteDTO.getAccountId() != null) {
            GlAccount account = new GlAccount();
            account.setId(invOrganizationSiteDTO.getAccountId().getId());
            invOrganizationSite.setAccountId(account);

        }

        if (invOrganizationSiteDTO.getOfferCompanyDTO() != null) {
            OfferCompany offerCompany = new OfferCompany();
            offerCompany.setId(invOrganizationSiteDTO.getOfferCompanyDTO().getId());
            invOrganizationSite.setOfferCompanyId(offerCompany);

        }

        if (invOrganizationSiteDTO.getCurrencyId() != null) {
            Currency currency = new Currency();
            currency.setId(invOrganizationSiteDTO.getCurrencyId().getId());
            invOrganizationSite.setCurrencyId(currency);
        }

        if (invOrganizationSiteDTO.getDelegatorId() != null) {
            InventoryDelegator inventoryDelegator = new InventoryDelegator();
            inventoryDelegator.setId(invOrganizationSiteDTO.getDelegatorId().getId());
            invOrganizationSite.setDelegatorId(inventoryDelegator);

        }

        if (invOrganizationSiteDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invOrganizationSiteDTO.getCreatedBy());
            invOrganizationSite.setCreatedBy(user);
            invOrganizationSite.setCreationDate(invOrganizationSiteDTO.getCreatedDate());
            invOrganizationSite.setModifiedBy(tobyUser);
            invOrganizationSite.setModificationDate(new Date());
            if (invOrganizationSiteDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invOrganizationSiteDTO.getCompanyId());
                invOrganizationSite.setCompanyId(company);
            }

            if (invOrganizationSiteDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invOrganizationSiteDTO.getBranchId());
                invOrganizationSite.setBranchId(branch);
            }
        } else {
            invOrganizationSite.setCreatedBy(tobyUser);
            invOrganizationSite.setCreationDate(new Date());
            invOrganizationSite.setCompanyId(tobyUser.getCompanyId());
            invOrganizationSite.setBranchId(tobyUser.getBranchId());
        }

        return invOrganizationSite;
    }

    public InvOrganizationSiteDTO mapToDTO(InvOrganizationSite invOrganizationSite, TobyUser tobyUser) {

        InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
        invOrganizationSiteDTO.setCreatedBy(invOrganizationSite.getCreatedBy().getId());
        invOrganizationSiteDTO.setCreatedDate(invOrganizationSite.getCreationDate());
        invOrganizationSiteDTO.setBranchId(invOrganizationSite.getBranchId().getId());
        invOrganizationSiteDTO.setCompanyId(invOrganizationSite.getCompanyId().getId());
        invOrganizationSiteDTO.setAddress(invOrganizationSite.getAddress());
        invOrganizationSiteDTO.setCode(invOrganizationSite.getCode());
        invOrganizationSiteDTO.setEmail(invOrganizationSite.getEmail());
        invOrganizationSiteDTO.setFax(invOrganizationSite.getFax());
        invOrganizationSiteDTO.setId(invOrganizationSite.getId());
        invOrganizationSiteDTO.setMobile(invOrganizationSite.getMobile());
        invOrganizationSiteDTO.setName(invOrganizationSite.getName());
        invOrganizationSiteDTO.setTelephone(invOrganizationSite.getPhone());
        invOrganizationSiteDTO.setType(invOrganizationSite.getType());
        invOrganizationSiteDTO.setActive(invOrganizationSite.getActive());
        invOrganizationSiteDTO.setBalanceLimit(invOrganizationSite.getBalanceLimit());
        invOrganizationSiteDTO.setCompanyName(invOrganizationSite.getCompanyName());
        invOrganizationSiteDTO.setContactPerson(invOrganizationSite.getContactPerson());
        invOrganizationSiteDTO.setDiscount(invOrganizationSite.getDiscount());
        invOrganizationSiteDTO.setIndex(invOrganizationSite.getId());
        invOrganizationSiteDTO.setNationId(invOrganizationSite.getZip());
        invOrganizationSiteDTO.setOpenBalanceCredit(invOrganizationSite.getOpenBalanceCredit());
        invOrganizationSiteDTO.setOpenBalanceDate(invOrganizationSite.getOpenBalanceDate());
        invOrganizationSiteDTO.setOpenBalanceDebit(invOrganizationSite.getOpenBalanceDebit());
        invOrganizationSiteDTO.setZip(invOrganizationSite.getZip());
        invOrganizationSiteDTO.setPhone(invOrganizationSite.getPhone());
        invOrganizationSiteDTO.setPhone2(invOrganizationSite.getPhone2());
        invOrganizationSiteDTO.setPostBox(invOrganizationSite.getPostBox());
        invOrganizationSiteDTO.setRemark(invOrganizationSite.getRemark());
        invOrganizationSiteDTO.setLength(invOrganizationSite.getLength());
        invOrganizationSiteDTO.setShoulder(invOrganizationSite.getShoulder());
        invOrganizationSiteDTO.setStep(invOrganizationSite.getStep());
        invOrganizationSiteDTO.setEmployer(invOrganizationSite.getEmployer());
        invOrganizationSiteDTO.setStreet(invOrganizationSite.getStreet());
        invOrganizationSiteDTO.setCity(invOrganizationSite.getCity());
        invOrganizationSiteDTO.setCountry(invOrganizationSite.getCountry());
        invOrganizationSiteDTO.setJobNamed(invOrganizationSite.getJobNamed());
        if (invOrganizationSite.getParent() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO1 = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO1.setId(invOrganizationSite.getParent().getId());
            invOrganizationSiteDTO1.setName(invOrganizationSite.getParent().getName());
            invOrganizationSiteDTO1.setCode(invOrganizationSite.getParent().getCode());
            invOrganizationSiteDTO.setParent(invOrganizationSiteDTO1);
        }
        if (invOrganizationSite.getRegionId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invOrganizationSite.getRegionId().getId());
            symbolDTO.setName(invOrganizationSite.getRegionId().getName());
            symbolDTO.setSerial(invOrganizationSite.getRegionId().getSerial());
            invOrganizationSiteDTO.setRegionId(symbolDTO);
        }

        if (invOrganizationSite.getCountryId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invOrganizationSite.getCountryId().getId());
            symbolDTO.setName(invOrganizationSite.getCountryId().getName());
            symbolDTO.setSerial(invOrganizationSite.getCountryId().getSerial());
            invOrganizationSiteDTO.setCountryId(symbolDTO);
        }

        if (invOrganizationSite.getContractorType() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invOrganizationSite.getContractorType().getId());
            symbolDTO.setName(invOrganizationSite.getContractorType().getName());
            symbolDTO.setSerial(invOrganizationSite.getContractorType().getSerial());
            invOrganizationSiteDTO.setContractorType(symbolDTO);
        }

        if (invOrganizationSite.getAccountId() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setId(invOrganizationSite.getAccountId().getId());
            accountDTO.setAccNumber(invOrganizationSite.getAccountId().getAccNumber());
            accountDTO.setName(invOrganizationSite.getAccountId().getName());
            invOrganizationSiteDTO.setAccountId(accountDTO);

        }

        if (invOrganizationSite.getCurrencyId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(invOrganizationSite.getCurrencyId().getId());
            currencyDTO.setCode(invOrganizationSite.getCurrencyId().getCode());
            currencyDTO.setName(invOrganizationSite.getCurrencyId().getName());
            currencyDTO.setSerial(invOrganizationSite.getCurrencyId().getSerial());
            invOrganizationSiteDTO.setCurrencyId(currencyDTO);
        }

        if (invOrganizationSite.getDelegatorId() != null) {
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(invOrganizationSite.getDelegatorId().getId());
            inventoryDelegatorDTO.setCode(invOrganizationSite.getDelegatorId().getCode());
            inventoryDelegatorDTO.setName(invOrganizationSite.getDelegatorId().getName());
            invOrganizationSiteDTO.setDelegatorId(inventoryDelegatorDTO);

        }

        return invOrganizationSiteDTO;
    }

    public List<InvOrganizationSiteDTO> mapToDTOList(List<InvOrganizationSite> invOrganizationSiteList, TobyUser tobyUser) {
        List<InvOrganizationSiteDTO> invPurchaseInvoiceDTOList = new ArrayList<>();
        for (InvOrganizationSite invOrganizationSite : invOrganizationSiteList) {
            invPurchaseInvoiceDTOList.add(mapToDTO(invOrganizationSite, tobyUser));
        }
        return invPurchaseInvoiceDTOList;
    }

    public List<InvOrganizationSiteDTO> mapToDTOList1(List<InvOrganizationSite> invOrganizationSiteList, TobyUser tobyUser) {
        List<InvOrganizationSiteDTO> invPurchaseInvoiceDTOList = new ArrayList<>();
        for (InvOrganizationSite invOrganizationSite : invOrganizationSiteList) {
            InvOrganizationSiteDTO dto = mapToDTO(invOrganizationSite, tobyUser);
            if (proProductionMovementDetailService.getAllInvoiceByClient(tobyUser, dto).isEmpty()) {
                continue;
            }
            invPurchaseInvoiceDTOList.add(dto);
        }
        return invPurchaseInvoiceDTOList;
    }

    @Override
    public List<InvOrganizationSiteDTO> getCustomerByBranchIdDTO(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ", params);
        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public List<InvOrganizationSiteDTO> getSupplierDTOByBranchId(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 1 OR e.type = 2) AND e.active = 1) ORDER BY e.code", params);
        return mapToDTOList(detailses, tobyUser);

    }

    @Override
    public List<InvOrganizationSiteDTO> findInvOrganizeSiteID(TobyUser tobyUser, Integer type, Integer active) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND  e.type =:type ");
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("type", type);
        if (active != null && (active == 1 && active == 0)) {
            params.put("active", active);
            query.append(" AND e.active =:active");
        }
        query.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(query.toString(), params);

        return mapToDTOList(detailses, tobyUser);

    }

    @Override
    public InvOrganizationSiteDTO addOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser) {
        try {

            invOrganizationSiteDTO.setMsg(null);
            String error = genralValidateDelivery(tobyUser, invOrganizationSiteDTO);
            if (error.isEmpty()) {
                InvOrganizationSite invOrganizationSite = mapToEntity(invOrganizationSiteDTO, tobyUser);
                invOrganizationSite = dao.updateEntity(invOrganizationSite);
                invOrganizationSiteDTO = mapToDTO(invOrganizationSite, tobyUser);
            } else {
                invOrganizationSiteDTO.setMsg(error);
            }
            return invOrganizationSiteDTO;
        } catch (Exception e) {
        }
        return invOrganizationSiteDTO;

    }

    @Override
    public List<InvOrganizationSiteDTO> getCustomerDTOByBranchId(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code", params);
        return returnListDTO(detailses, tobyUser);
    }

    private List<InvOrganizationSiteDTO> returnListDTO(List<InvOrganizationSite> list, TobyUser tobyUser) {
        List<InvOrganizationSiteDTO> DTOlist = new ArrayList();
        returnListDTO(list, DTOlist, tobyUser);
        return DTOlist;
    }

    private void returnListDTO(List<InvOrganizationSite> list, List<InvOrganizationSiteDTO> dTOList, TobyUser tobyUser) {
        for (InvOrganizationSite guide : list) {
            dTOList.add(mapToDTO(guide, tobyUser));
        }

    }

    @Override
    public List<InvOrganizationSiteDTO> findInvOrganizeSiteClient(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        //and i.id not in (select p.invOrganizationSiteId.id from ProProductMovement p where p.type=4)
        List<InvOrganizationSite> invOrganizationSites = dao.findListByQuery("select i from InvOrganizationSite i where i.branchId.id = :branchId and i.type =0 ", params);
        return mapToDTOList(invOrganizationSites, tobyUser);
    }

    @Override
    public void deleteOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser) {
        InvOrganizationSite invOrganizationSite = mapFromDTO(invOrganizationSiteDTO, tobyUser);
        dao.deleteEntity(invOrganizationSite);
    }

    @Override
    public String findMaxCodeForOrganizationSiteByBranchId(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<String> detailses = dao.findListByQuery("SELECT max(e.code) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND (e.type = :type  AND e.active = 1) ORDER BY e.code", params);
        return detailses.get(0);
    }

    @Override
    public List<InvOrganizationSiteDTO> getCustomerByBranchIdForReportDTO(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type <> 1 AND e.active = 1 ORDER BY e.code", params);
        List<InvOrganizationSiteDTO> dTOList = new ArrayList<>();

        returnListDTO(detailses, dTOList, Boolean.FALSE);
        return dTOList;
    }

    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdDTO(Integer branchId, boolean active, Integer type) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = :type OR e.type = 2)");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);

        returnListDTO(detailses, list, Boolean.FALSE);
        return list;
    }

    public List<InvOrganizationSiteDTO> getorganizationSiteDTOByType(Integer branchId, Integer type, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<InvOrganizationSite> invOrganizationSites = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type =:type AND e.active = 1  ORDER BY e.code", params);
        return mapToDTOList(invOrganizationSites, tobyUser);
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters, Integer type, Integer gallaryId) {
        filters.put("branchId", branchId);
        filters.put("type", type);
        filters.put("gallaryId", gallaryId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(i) FROM InvOrganizationSite i WHERE i.branchId.id = :branchId AND i.type = :type  AND   i.gallaryId.id = :gallaryId  ");
        appendFilter(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }

    public void appendFilter(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            //OR ( i.parent.name  like CONCAT('%',:globalFilter ,'%'))
            String filter = " and ( i.mobile like CONCAT('%',:globalFilter ,'%') OR i.code like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<InvOrganizationSiteDTO> getALLCustomerDTOByBranchIdAndType(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer type, Integer gallaryId) {
        filters.put("branchId", branchId);
        filters.put("type", type);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT i FROM InvOrganizationSite i WHERE i.branchId.id = :branchId  AND i.type = :type  ");
        if (gallaryId != null) {
            filters.put("gallaryId", gallaryId);
            builder.append(" AND  i.gallaryId.id = :gallaryId  ");
        }
        appendFilter(filters, builder);
        builder.append(" ORDER BY i.id DESC");
        List<InvOrganizationSite> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<InvOrganizationSiteDTO> dTOList = new ArrayList<>();
        returnListDTO(details, dTOList, Boolean.FALSE);
        return dTOList;
    }

    @Override
    public List<InvOrganizationSiteDTO> getALLCustomerDTOByBranchIdAndTypeAndCode(String code, Integer branchId, Integer type, Integer gallaryId) {
        if (code != null && !code.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("branchId", branchId);
            params.put("type", type);
            params.put("gallaryId", gallaryId);
            params.put("code", code);
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT i FROM InvOrganizationSite i WHERE i.branchId.id = :branchId  AND i.type = :type  AND  i.gallaryId.id = :gallaryId ");
            builder.append(" and (i.code = :code or i.mobile = :code) ");
            builder.append(" ORDER BY i.id DESC");
            List<InvOrganizationSite> details = dao.findListByQuery(builder.toString(), params);
            List<InvOrganizationSiteDTO> dTOList = new ArrayList<>();
            returnListDTO(details, dTOList, Boolean.FALSE);
            return dTOList;
        }
        return null;
    }

    private void initMapToDTO(InvOrganizationSiteDTO dTO, InvOrganizationSite entity, Boolean getCRMInformation) {
        dTO.setId(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setAddress(entity.getAddress());
        dTO.setMobile(entity.getMobile());
        dTO.setFax(entity.getFax());
        dTO.setPhone(entity.getPhone());
        dTO.setType(entity.getType());
        dTO.setEmail(entity.getEmail());
        dTO.setPostBox(entity.getPostBox());
        dTO.setDiscount(entity.getDiscount());
        dTO.setOpenBalanceDebit(entity.getOpenBalanceDebit());
        dTO.setOpenBalanceCredit(entity.getOpenBalanceCredit());
        dTO.setBalanceLimit(entity.getBalanceLimit());
        dTO.setRemark(entity.getRemark());
        dTO.setActive(entity.getActive());
        dTO.setZip(entity.getZip());
        dTO.setContactPerson(entity.getContactPerson());
        dTO.setPhone2(entity.getPhone2());
        dTO.setOpenBalanceDate(entity.getOpenBalanceDate());

        if (entity.getAccountId() != null) {
            GlAccountDTO glAccountDTO = new GlAccountDTO();
            glAccountDTO.setId(entity.getAccountId().getId());
            glAccountDTO.setName(entity.getAccountId().getName());
            glAccountDTO.setAccNumber(entity.getAccountId().getAccNumber());
            dTO.setAccountId(glAccountDTO);
        }
        if (entity.getGallaryId() != null) {
            dTO.setGallaryIdDTO(invInventoryService.maptoDTOSmall(null, entity.getGallaryId()));
        }

        if (entity.getOfferCompanyId() != null) {
            OfferCompanyDTO offerCompanyDTO = new OfferCompanyDTO();
            offerCompanyDTO.setId(entity.getOfferCompanyId().getId());
            offerCompanyDTO.setName(entity.getOfferCompanyId().getName());
            offerCompanyDTO.setCode(entity.getOfferCompanyId().getCode());
            offerCompanyDTO.setDiscount(entity.getOfferCompanyId().getDiscount());
            dTO.setOfferCompanyDTO(offerCompanyDTO);
        }

        if (entity.getCountryId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getCountryId().getId());
            symbolDTO.setSerial(entity.getCountryId().getSerial());
            symbolDTO.setName(entity.getCountryId().getName());
            dTO.setCountryId(symbolDTO);
        }
        if (entity.getCurrencyId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(entity.getCurrencyId().getId());
            currencyDTO.setSerial(entity.getCurrencyId().getSerial());
            currencyDTO.setName(entity.getCurrencyId().getName());
            currencyDTO.setCode(entity.getCurrencyId().getCode());
            dTO.setCurrencyId(currencyDTO);
        }
        if (entity.getParent() != null) {
            InvOrganizationSiteDTO organizationSiteDTO = new InvOrganizationSiteDTO();
            organizationSiteDTO.setId(entity.getParent().getId());
            organizationSiteDTO.setCode(entity.getParent().getCode());
            organizationSiteDTO.setName(entity.getParent().getName());
            dTO.setParent(organizationSiteDTO);
        }

        if (entity.getSupplierType() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getSupplierType().getId());
            symbolDTO.setSerial(entity.getSupplierType().getSerial());
            symbolDTO.setName(entity.getSupplierType().getName());
            dTO.setSupplierType(symbolDTO);
        }
        if (entity.getContractorType() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getContractorType().getId());
            symbolDTO.setSerial(entity.getContractorType().getSerial());
            symbolDTO.setName(entity.getContractorType().getName());
            dTO.setContractorType(symbolDTO);
        }

        if (entity.getRegionId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getRegionId().getId());
            symbolDTO.setName(entity.getRegionId().getName());
            symbolDTO.setSerial(entity.getRegionId().getSerial());
            dTO.setRegionId(symbolDTO);
        }
        if (entity.getDelegatorId() != null) {
            InventoryDelegatorDTO parent = new InventoryDelegatorDTO();
            parent.setId(entity.getDelegatorId().getId());
            parent.setCode(entity.getDelegatorId().getCode());
            parent.setName(entity.getDelegatorId().getName());
            dTO.setDelegatorId(parent);
        }

        if (entity.getLearnAboutCompanyfrom() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getLearnAboutCompanyfrom().getId());
            symbolDTO.setName(entity.getLearnAboutCompanyfrom().getName());
            symbolDTO.setSerial(entity.getLearnAboutCompanyfrom().getSerial());
            symbolDTO.setIndex(entity.getLearnAboutCompanyfrom().getId());
            dTO.setLearnAboutCompanyFrom(symbolDTO);
        }
        if (getCRMInformation) {
            List<CrmEventDTO> crmEventDTOList = cRMEventServiceDTO.findALLCrmEventByCustomerId(entity.getId());
            dTO.setCrmEventDTOList(crmEventDTOList);

            List<CrmCustomerInterestedProductsDTO> interestedProductsDTOList = cRMCustomerInterestedProductsServiceDTO.getCrmCustomerInterestedProductsByOrganizationSiteId(entity.getId());
            dTO.setCrmCustomerInterestedProductsDTOList(interestedProductsDTOList);
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setCompanyId(entity.getCompanyId().getId());
    }

    public InvOrganizationSite mapFromDTO(InvOrganizationSiteDTO dTO, TobyUser tobyUser) {
        InvOrganizationSite entity = new InvOrganizationSite();

        entity.setId(dTO.getId());
        entity.setName(dTO.getName());
        entity.setCode(dTO.getCode());
        entity.setAddress(dTO.getAddress());
        entity.setMobile(dTO.getMobile());
        entity.setFax(dTO.getFax());
        entity.setPhone(dTO.getPhone());
        entity.setType(dTO.getType());
        entity.setEmail(dTO.getEmail());
        entity.setPostBox(dTO.getPostBox());
        entity.setDiscount(dTO.getDiscount());
        entity.setOpenBalanceDebit(dTO.getOpenBalanceDebit());
        entity.setOpenBalanceCredit(dTO.getOpenBalanceCredit());
        entity.setBalanceLimit(dTO.getBalanceLimit());
        entity.setRemark(dTO.getRemark());
        entity.setActive(dTO.getActive());
        entity.setZip(dTO.getZip());
        entity.setContactPerson(dTO.getContactPerson());
        entity.setPhone2(dTO.getPhone2());
        entity.setOpenBalanceDate(dTO.getOpenBalanceDate());

        if (dTO.getAccountId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(dTO.getAccountId().getId());
            entity.setAccountId(glAccount);
        }
        if (dTO.getGallaryIdDTO() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(dTO.getGallaryIdDTO().getId());
            entity.setGallaryId(invInventory);
        }

        if (dTO.getCountryId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getCountryId().getId());
            entity.setCountryId(symbol);
        }
        if (dTO.getCurrencyId() != null) {
            Currency currency = new Currency();
            currency.setId(dTO.getCurrencyId().getId());
            entity.setCurrencyId(currency);
        }
        if (dTO.getParent() != null) {
            InvOrganizationSite organizationSite = new InvOrganizationSite();
            organizationSite.setId(dTO.getParent().getId());
            entity.setParent(organizationSite);
        }
        if (dTO.getSupplierType() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getSupplierType().getId());
            entity.setSupplierType(symbol);
        }
        if (dTO.getContractorType() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getContractorType().getId());
            entity.setContractorType(symbol);
        }
        if (dTO.getDelegatorId() != null) {
            InventoryDelegator parent = new InventoryDelegator();
            parent.setId(dTO.getDelegatorId().getId());
            entity.setDelegatorId(parent);
        }
        if (dTO.getRegionId() != null) {
            Symbol parent = new Symbol();
            parent.setId(dTO.getRegionId().getId());
            parent.setSerial(dTO.getRegionId().getSerial());
            entity.setRegionId(parent);
        }
        if (dTO.getId() == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);

        }
        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }

    private List<InvOrganizationSiteDTO> returnListDTO(List<InvOrganizationSite> list) {
        List<InvOrganizationSiteDTO> DTOlist = new ArrayList();
        returnListDTO(list, DTOlist, Boolean.FALSE);
        return DTOlist;
    }

    private List<InvOrganizationSite> returnListEntity(List<InvOrganizationSiteDTO> list, TobyUser tobyUser) {
        List<InvOrganizationSite> Entitylist = new ArrayList();
        for (InvOrganizationSiteDTO organizationSiteDTO : list) {
            Entitylist.add(mapFromDTO(organizationSiteDTO, tobyUser));
        }
        return Entitylist;
    }

    @Override
    public List<InvOrganizationSiteDTO> getCustomerDTOByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code", params);
        return returnListDTO(detailses);
    }

    @Override
    public List<InvOrganizationSiteDTO> getCustomerDTOFromAndTo(Integer branchId, invOrganizationSiteSearchBean organizationSiteSearchBean) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder query = new StringBuilder();
        query.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ");
        queryFrom(query, organizationSiteSearchBean, params);
        queryTo(query, organizationSiteSearchBean, params);
        List<InvOrganizationSite> detailses = dao.findListByQuery(query.toString(), params);
        return returnListDTO(detailses);
    }

    private void queryFrom(StringBuilder query, invOrganizationSiteSearchBean From, Map<String, Object> params) {
        if (From != null) {
            if (From.getFrominvOrganizationSiteDTO() != null) {
                params.put("customerFrom", From.getFrominvOrganizationSiteDTO().getCode());
                query.append("AND (e.code >= :customerFrom)");
            }
            if (From.getFromRegion() != null) {
                params.put("RegionFrom", From.getFromRegion().getSerial());
                query.append("AND (e.regionId.serial >= :RegionFrom)");
            }
        }
    }

    private void queryTo(StringBuilder query, invOrganizationSiteSearchBean To, Map<String, Object> params) {
        if (To != null) {
            if (To.getToinvOrganizationSiteDTO() != null) {
                params.put("customerTo", To.getToinvOrganizationSiteDTO().getCode());
                query.append("AND (e.code <= :customerTo)");
            }
            if (To.getToRegion() != null) {
                params.put("RegionTo", To.getToRegion().getSerial());
                query.append("AND e.regionId.serial <= :RegionTo ");
            }
        }
    }

    @Override
    public List<InvOrganizationSiteDTO> AddDelegatorToCustomerDTO(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, TobyUser tobyUser) {
        List<InvOrganizationSiteDTO> dTO = new ArrayList();
        List<InvOrganizationSite> invOrganizationSiteList = returnListEntity(invOrganizationSiteDTOList, tobyUser);
        for (InvOrganizationSite organizationSite : invOrganizationSiteList) {
            dTO.add(mapTODTO(null, OrganizationSiteServiceImpl.this.updateOrganizationSite(organizationSite), Boolean.FALSE));
        }
        return dTO;
    }

    @Override
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteByBranchIdPerDTO(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ORDER BY e.code", params);

        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public InvOrganizationSiteDTO findOrganizationSiteByIdDTO(Integer invOrganizationSiteId) {
        InvOrganizationSite invOrganizationSite = dao.findEntityById(InvOrganizationSite.class, invOrganizationSiteId);
        return mapTODTO(null, invOrganizationSite, Boolean.FALSE);
    }

    @Override
    public InvOrganizationSiteDTO saveInvOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser) {

        try {
            invOrganizationSiteDTO.setMsg(null);
            String error = genralValidate(tobyUser, invOrganizationSiteDTO);
            if (error.isEmpty()) {
                InvOrganizationSite invOrganizationSite = dao.updateEntity(mapToEntity(invOrganizationSiteDTO, tobyUser));
                invOrganizationSiteDTO = mapToDTO(invOrganizationSite, tobyUser);
            } else {
                invOrganizationSiteDTO.setMsg(error);
            }
            return invOrganizationSiteDTO;
        } catch (Exception e) {
        }
        return null;

    }

    public String genralValidate(TobyUser tobyUser, InvOrganizationSiteDTO invOrganizationSiteDTO) {
        StringBuilder errorMessage = new StringBuilder();
        if (!validateName(tobyUser, invOrganizationSiteDTO.getId(), invOrganizationSiteDTO.getName(), invOrganizationSiteDTO.getType())) {
            errorMessage.append("الاسم موجود");

        }
        if (!validateCode(tobyUser, invOrganizationSiteDTO.getId(), invOrganizationSiteDTO.getCode(), invOrganizationSiteDTO.getType())) {
            errorMessage.append("الكود موجود");
        }

        if (invOrganizationSiteDTO.getCurrencyId() == null) {
            errorMessage.append("يجب اختيار عمله");
        }

        return errorMessage.toString();
    }

    public String genralValidateDelivery(TobyUser tobyUser, InvOrganizationSiteDTO invOrganizationSiteDTO) {
        StringBuilder errorMessage = new StringBuilder();
//        if (!validateName(tobyUser, invOrganizationSiteDTO.getId(), invOrganizationSiteDTO.getName(), invOrganizationSiteDTO.getType())) {
//            errorMessage.append("الاسم موجود");
//
//        }
//        if (!validateCode(tobyUser, invOrganizationSiteDTO.getId(), invOrganizationSiteDTO.getCode(), invOrganizationSiteDTO.getType())) {
//            errorMessage.append("الكود موجود");
//        }
        return errorMessage.toString();
    }

    @Override
    public Boolean validateName(TobyUser tobyUser, Integer invOrganizationSiteDTOid, String name) {

        StringBuilder quarry = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("name", name);
        quarry.append("SELECT i FROM InvOrganizationSite i where i.branchId.id = :branchId  and i.name = :name  ORDER BY i.name");
        if (invOrganizationSiteDTOid != null) {
            params.put("invOrganizationSiteDTOid", invOrganizationSiteDTOid);
            quarry.append("and i.id != :invOrganizationSiteDTOid");
        }
        List<InvOrganizationSite> invOrganizationSiteList = dao.findListByQuery(quarry.toString(), params);
        if (invOrganizationSiteList == null || invOrganizationSiteList.isEmpty()) {
            return true;
        } else {

            return false;
        }
    }

    @Override
    public Boolean validateName(TobyUser tobyUser, Integer invOrganizationSiteDTOid, String name, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("name", name);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvOrganizationSite i where i.branchId.id = :branchId  and i.name = :name ");
        if (invOrganizationSiteDTOid != null) {
            params.put("invOrganizationSiteDTOid", invOrganizationSiteDTOid);
            query.append(" and i.id != :invOrganizationSiteDTOid ");
        }
        if (type != null) {
            if (type == 0) {
                query.append(" and (i.type = 0 or i.type = 2 ) ");
            } else if (type == 1) {
                query.append(" and (i.type = 1 or i.type = 2 ) ");
            } else if (type == 2) {
                query.append(" and (i.type = 0 or i.type = 1 or i.type = 2 ) ");
            } else {
                params.put("type", type);
                query.append(" and i.type = :type ");
            }

        }
        List<InvOrganizationSite> invOrganizationSiteList = dao.findListByQuery(query.toString(), params);
        if (invOrganizationSiteList.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean validateCode(TobyUser tobyUser, Integer invOrganizationSiteDTOid, String code) {

        StringBuilder quarry = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());

        params.put("code", code);
        quarry.append("SELECT i FROM InvOrganizationSite i where i.branchId.id = :branchId  and i.code = :code  ORDER BY i.code");
        if (invOrganizationSiteDTOid != null) {
            params.put("invOrganizationSiteDTOid", invOrganizationSiteDTOid);
            quarry.append("and i.id != :invOrganizationSiteDTOid");

        }
        List<InvOrganizationSite> invOrganizationSiteList = dao.findListByQuery(quarry.toString(), params);
        if (invOrganizationSiteList == null || invOrganizationSiteList.isEmpty()) {
            return true;
        } else {

            return false;
        }

    }

    @Override
    public Boolean validateCode(TobyUser tobyUser, Integer invOrganizationSiteDTOid, String code, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("code", code);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvOrganizationSite i where i.branchId.id = :branchId  and i.code = :code ");
        if (invOrganizationSiteDTOid != null) {
            params.put("invOrganizationSiteDTOid", invOrganizationSiteDTOid);
            query.append(" and i.id != :invOrganizationSiteDTOid ");
        }

        if (type != null) {
            if (type == 0) {
                query.append(" and (i.type = 0 or i.type = 2 ) ");
            } else if (type == 1) {
                query.append(" and (i.type = 1 or i.type = 2 ) ");
            } else if (type == 2) {
                query.append(" and (i.type = 0 or i.type = 1 or i.type = 2 ) ");
            } else {
                params.put("type", type);
                query.append(" and i.type = :type ");
            }
        }

        List<InvOrganizationSite> invOrganizationSiteList = dao.findListByQuery(query.toString(), params);
        if (invOrganizationSiteList.size() == 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<InvOrganizationSite> getContractorByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND (e.type = :type");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);
        List<InvOrganizationSite> list = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {
            InvOrganizationSite site = new InvOrganizationSite();
            site.setAccountId(detail.getAccountId());
            site.setCode(detail.getCode());
            site.setCurrencyId(detail.getCurrencyId());
            site.setId(detail.getId());
            site.setName(detail.getName());
            site.setOpenBalanceCredit(detail.getOpenBalanceCredit());
            site.setOpenBalanceDebit(detail.getOpenBalanceDebit());
            site.setOpenBalanceDate(detail.getOpenBalanceDate());
            list.add(site);
        }
        return list;
    }

    @Override
    public List<InvOrganizationSite> getorganizationSiteByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = :type OR e.type = 2)");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);
        List<InvOrganizationSite> list = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {
            InvOrganizationSite site = new InvOrganizationSite();
            site.setAccountId(detail.getAccountId());
            site.setCode(detail.getCode());
            site.setCurrencyId(detail.getCurrencyId());
            site.setId(detail.getId());
            site.setName(detail.getName());
            site.setOpenBalanceCredit(detail.getOpenBalanceCredit());
            site.setOpenBalanceDebit(detail.getOpenBalanceDebit());
            site.setOpenBalanceDate(detail.getOpenBalanceDate());
            list.add(site);
        }
        return list;
    }

    @Override
    public List<InvOrganizationSiteDTO> getorganizationSiteDTOByBranchIdForGlBankModule(TobyUser tobyUser, boolean active, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = :type OR e.type = 2)");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);
        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public List<InvOrganizationSite> getInvOrganizationSiteByBranchId(Integer BranchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", BranchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ORDER BY e.code", params);
        return detailses;
    }

    @Override
    public void deleteOrganizationSite(InvOrganizationSite invOrganizationSite) {
        dao.deleteEntity(invOrganizationSite);
    }

    @Override
    public List<InvOrganizationSiteDTO> getSupplierByBranchIdDTO(Integer BranchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", BranchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 1 OR e.type = 2) AND e.active = 1) ORDER BY e.code", params);
        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public List<InvOrganizationSiteDTO> getCustomerByBranchIdDTO(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code", params);
        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public List<InvOrganizationSiteDTO> getOrganizationDTOByBranchIdDTO(TobyUser tobyUser, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId  AND e.active = 1 ");
        params.put("branchId", tobyUser.getBranchId().getId());
        if ("0".equals(type)) {
            query.append(" AND ((e.type = 0 OR e.type = 2) ) ");
        } else if ("1".equals(type)) {
            query.append(" AND ((e.type = 1 OR e.type = 2) ) ");
        } else if ("5".equals(type)) {
            query.append(" AND e.type = 5 ");
        }
        query.append("ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(query.toString(), params);

        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdDTOSmall(Integer branchId, boolean active, Integer type) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        appendOrganizationTypeAndActive(params, type, sql, active);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);

        returnListDTOSmall(detailses, list);
        return list;
    }

    private void appendOrganizationTypeAndActive(Map<String, Object> params, Integer type, StringBuilder sql, boolean active) {
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ");

        if (type == 0 || type == 1) {
            sql.append(" AND (e.type = :type OR e.type = 2) ");
        } else {
            sql.append(" AND e.type = :type  ");
        }

        if (active) {
            sql.append(" AND e.active = 1 ");
        }

        sql.append(" ORDER BY e.code");
    }

    private void initMapToDTOSmall(InvOrganizationSiteDTO dTO, InvOrganizationSite entity) {
        dTO.setId(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
    }

    private void returnListDTOSmall(List<InvOrganizationSite> list, List<InvOrganizationSiteDTO> dTOList) {
        for (InvOrganizationSite guide : list) {
            dTOList.add(mapTODTOSmall(guide));
        }

    }

    private List<InvOrganizationSiteDTO> returnListDTOSmall(List<InvOrganizationSite> list) {
        List<InvOrganizationSiteDTO> DTOlist = new ArrayList();
        returnListDTOSmall(list, DTOlist);
        return DTOlist;
    }

    @Override
    public InvOrganizationSiteDTO addNewCustomer(InvOrganizationSiteDTO invOrganizationSiteDTO, List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, TobyUser tobyUser, Integer accountId) {

        CurrencyDTO currencyDefault = currencyServiceDTO.getDefaultCurrenciesByCompanyId(tobyUser.getCompanyId().getId());

        if (invOrganizationSiteDTO != null && invOrganizationSiteDTO.getName() != null && currencyDefault != null) {
            invOrganizationSiteDTO.setType(0);
            invOrganizationSiteDTO.setCurrencyId(currencyDefault);
            invOrganizationSiteDTO.setActive(1);
            Integer code = getMaxGenericSerialForDetail(tobyUser.getBranchId().getId(), 0);
            GlAccountDTO glAccountDTO = new GlAccountDTO();
            glAccountDTO.setId(accountId);
            invOrganizationSiteDTO.setAccountId(glAccountDTO);
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(77);
            invOrganizationSiteDTO.setDelegatorId(inventoryDelegatorDTO);
            invOrganizationSiteDTO.setCode(code.toString());
            invOrganizationSiteDTO.setMobile(invOrganizationSiteDTO.getMobile());
            invOrganizationSiteDTO.setEmail(invOrganizationSiteDTO.getEmail());
            invOrganizationSiteDTO.setAddress(invOrganizationSiteDTO.getAddress());
            invOrganizationSiteDTO.setLength(invOrganizationSiteDTO.getLength());
            invOrganizationSiteDTO.setShoulder(invOrganizationSiteDTO.getShoulder());
            invOrganizationSiteDTO.setStep(invOrganizationSiteDTO.getStep());

            invOrganizationSiteDTO = addOrganizationSiteDTO(invOrganizationSiteDTO, tobyUser);
            if (invOrganizationSiteDTOList == null) {
                invOrganizationSiteDTOList = new ArrayList<>();
            }
            invOrganizationSiteDTOList.add(0, invOrganizationSiteDTO);
        }
        return invOrganizationSiteDTO;
    }

    @Override
    public InvOrganizationSiteDTO addNewCustomerAPI(InvOrganizationSiteDTOAPI invOrganizationSiteDTOAPI, TobyUser tobyUser, Integer accountId) {

        CurrencyDTO currencyDefault = currencyServiceDTO.getDefaultCurrenciesByCompanyId(tobyUser.getCompanyId().getId());
        InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
        if (invOrganizationSiteDTOAPI != null && invOrganizationSiteDTOAPI.getName() != null && currencyDefault != null) {
            invOrganizationSiteDTO.setType(0);
            invOrganizationSiteDTO.setCurrencyId(currencyDefault);
            invOrganizationSiteDTO.setActive(1);
            Integer code = getMaxGenericSerialForDetail(tobyUser.getBranchId().getId(), 0);
            GlAccountDTO glAccountDTO = new GlAccountDTO();
            glAccountDTO.setId(accountId);
            invOrganizationSiteDTO.setAccountId(glAccountDTO);
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(77);
            invOrganizationSiteDTO.setDelegatorId(inventoryDelegatorDTO);
            invOrganizationSiteDTO.setCode(code.toString());
            invOrganizationSiteDTO.setMobile(invOrganizationSiteDTOAPI.getMobile());
            invOrganizationSiteDTO.setEmail(invOrganizationSiteDTOAPI.getEmail());
            invOrganizationSiteDTO.setLength(invOrganizationSiteDTOAPI.getLength());
            invOrganizationSiteDTO.setShoulder(invOrganizationSiteDTOAPI.getShoulder());
            invOrganizationSiteDTO.setStep(invOrganizationSiteDTOAPI.getStep());

            invOrganizationSiteDTO = addOrganizationSiteDTO(invOrganizationSiteDTO, tobyUser);
        }
        return invOrganizationSiteDTO;
    }

    private synchronized <T> Integer getMaxGenericSerialForDetail(Integer branchId, Integer type) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT max(CONVERT(code,UNSIGNED INTEGER)) FROM inv_organization_site i where type = ");
        query.append(type);
        query.append(" AND branch_id = ").append(branchId);
        List<BigInteger> list = dao.executeNativeQuery(query.toString());
        Integer code = list != null && !list.isEmpty() && list.get(0) != null ? list.get(0).intValue() : null;
        if (code == null) {
            return 1;
        } else {
            return code + 1;
        }
    }

    @Override
    public List<InvOrganizationSiteDTO> getorganizationSiteForOpenningBalanceDTO(Integer branchId, boolean active, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId  ");

        if (type == 0 || type == 1) {
            sql.append(" AND (e.type = :type OR e.type = 2) ");
        } else {
            sql.append(" AND e.type = :type  ");
        }

        if (active) {
            sql.append(" AND e.active = 1 ");
        }

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {
            InvOrganizationSiteDTO site = new InvOrganizationSiteDTO();
            site.setCode(detail.getCode());
            site.setId(detail.getId());
            site.setName(detail.getName());
            site.setOpenBalanceCredit(detail.getOpenBalanceCredit());
            site.setOpenBalanceDebit(detail.getOpenBalanceDebit());
            site.setOpenBalanceDate(detail.getOpenBalanceDate());
            list.add(site);
        }
        return list;
    }

    @Override
    public InvOrganizationSite getInvOrganizationSiteBySerial(Integer branchId, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("code", code);
        List<InvOrganizationSite> list = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.code = :code ORDER BY e.code", params);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    @Override
    public Map<String, String> findMobileOrganizationSite(Integer branchId) {
        Map<String, String> mobileMap = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<String> list = dao.findListByQuery("SELECT e.mobile FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ", params);
        for (String i : list) {
            mobileMap.put(i, i);
        }
        return mobileMap;
    }

    @Override
    public String saveOpenningBalance(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        String error = null;
        for (InvOrganizationSiteDTO dto : invOrganizationSiteDTOList) {
            if (checkValueDate(dto)) {
                InvOrganizationSite entity = dao.findEntityById(InvOrganizationSite.class, dto.getId());
                entity.setOpenBalanceCredit(dto.getOpenBalanceCredit());
                entity.setOpenBalanceDebit(dto.getOpenBalanceDebit());
                entity.setOpenBalanceDate(dto.getOpenBalanceDate());
                dao.saveEntity(entity);
            } else {
                error = "يجب اختيار تاريخ";
                return error;
            }
        }
        return error;
    }

    public Boolean checkValueDate(InvOrganizationSiteDTO dto) {
        try {
            if ((dto.getOpenBalanceDebit() != null && dto.getOpenBalanceDebit().compareTo(BigDecimal.ZERO) == 1)
                    || (dto.getOpenBalanceCredit() != null && dto.getOpenBalanceCredit().compareTo(BigDecimal.ZERO) == 1)) {
                if (dto.getOpenBalanceDate() == null) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public InvOrganizationSiteDTO mapTODTOSmall(InvOrganizationSite entity) {
        InvOrganizationSiteDTO dTO = new InvOrganizationSiteDTO();
        initMapToDTOSmall(dTO, entity);
        return dTO;
    }

    @Override
    public InvOrganizationSiteDTO mapToDTOSmall(InvOrganizationSite invOrganizationSite) {
        InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
        invOrganizationSiteDTO.setId(invOrganizationSite.getId());
        invOrganizationSiteDTO.setName(invOrganizationSite.getName());
        invOrganizationSiteDTO.setCode(invOrganizationSite.getCode());
        invOrganizationSiteDTO.setMobile(invOrganizationSite.getMobile());
        invOrganizationSiteDTO.setLength(invOrganizationSite.getLength());
        invOrganizationSiteDTO.setShoulder(invOrganizationSite.getShoulder());
        invOrganizationSiteDTO.setStep(invOrganizationSite.getStep());
        invOrganizationSiteDTO.setDiscount(invOrganizationSite.getDiscount());
        if (invOrganizationSite.getCurrencyId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(invOrganizationSite.getCurrencyId().getId());
            currencyDTO.setCode(invOrganizationSite.getCurrencyId().getCode());
            currencyDTO.setName(invOrganizationSite.getCurrencyId().getName());
            invOrganizationSiteDTO.setCurrencyId(currencyDTO);
        }
        if (invOrganizationSite.getParent() != null) {
            InvOrganizationSiteDTO parent = new InvOrganizationSiteDTO();
            parent.setId(invOrganizationSite.getParent().getId());
            parent.setName(invOrganizationSite.getParent().getName());
            parent.setCode(invOrganizationSite.getParent().getCode());
            parent.setMobile(invOrganizationSite.getParent().getMobile());
            parent.setDiscount(invOrganizationSite.getParent().getDiscount());
            invOrganizationSiteDTO.setParent(parent);
        }
        if (invOrganizationSite.getOfferCompanyId() != null) {
            OfferCompanyDTO offerCompanyDTO = new OfferCompanyDTO();
            offerCompanyDTO.setId(invOrganizationSite.getOfferCompanyId().getId());
            offerCompanyDTO.setName(invOrganizationSite.getOfferCompanyId().getName());
            offerCompanyDTO.setCode(invOrganizationSite.getOfferCompanyId().getCode());
            offerCompanyDTO.setDiscount(invOrganizationSite.getOfferCompanyId().getDiscount());
            invOrganizationSiteDTO.setOfferCompanyDTO(offerCompanyDTO);
        }

        if (invOrganizationSite.getGallaryId() != null) {
            InvInventoryDTO gallary = new InvInventoryDTO();
            gallary.setId(invOrganizationSite.getGallaryId().getId());
            gallary.setName(invOrganizationSite.getGallaryId().getName());
            gallary.setCode(invOrganizationSite.getGallaryId().getCode());
            if (invOrganizationSite.getGallaryId().getCostCenterId() != null) {
                CostCenterDTO costCenterDTO = new CostCenterDTO();
                costCenterDTO.setId(invOrganizationSite.getGallaryId().getCostCenterId().getId());
                gallary.setCostCenterId(costCenterDTO);
            }

            invOrganizationSiteDTO.setGallaryIdDTO(gallary);
            invOrganizationSiteDTO.setGallaryIdAPI(invOrganizationSite.getGallaryId().getId());
        }

        if (invOrganizationSite.getAccountId() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setAccNumber(invOrganizationSite.getAccountId().getAccNumber());
            accountDTO.setName(invOrganizationSite.getAccountId().getName());
            accountDTO.setId(invOrganizationSite.getAccountId().getId());
            invOrganizationSiteDTO.setAccountId(accountDTO);
        }
        invOrganizationSiteDTO.setOpenBalanceCredit(invOrganizationSite.getOpenBalanceCredit());
        invOrganizationSiteDTO.setOpenBalanceDebit(invOrganizationSite.getOpenBalanceDebit());
        invOrganizationSiteDTO.setOpenBalanceDate(invOrganizationSite.getOpenBalanceDate());

        return invOrganizationSiteDTO;
    }

    @Override
    public InvOrganizationSiteDTO mapToDTOSmall(Integer invOrganizationSiteId) {
        InvOrganizationSite ios = dao.findEntityById(InvOrganizationSite.class, invOrganizationSiteId);
        return mapToDTOSmall(ios);
    }

    @Override
    public InvOrganizationSiteDTO getCustomerBySerial(String serial) {
        Map<String, Object> params = new HashMap<>();
        params.put("serial", serial);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = 182 AND e.mobile = :serial ", params);
        if (detailses != null && !detailses.isEmpty()) {
            return mapToDTOSmall(detailses.get(0));
        }
        return null;
    }

    /////////////////// CRM //////////////////////////
    private void appendFilter2(Map<String, Object> filters, StringBuilder builder, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer type, Integer branchId) {

        filters.put("branchId", branchId);
        filters.put("type", type);
        // CrmLeadsSearchBean crmLeadsSearchBean
//        if (crmLeadsSearchBean != null) {
//            appendCodeFrom(crmLeadsSearchBean, filters, builder);
//            appendCodeTo(crmLeadsSearchBean, filters, builder);
//            appendDateFrom(crmLeadsSearchBean, filters, builder);
//            appendDateTo(crmLeadsSearchBean, filters, builder);
//            appendEmpFrom(crmLeadsSearchBean, filters, builder);
//            appendEmpTo(crmLeadsSearchBean, filters, builder);
//            appendSourceFrom(crmLeadsSearchBean, filters, builder);
//            appendSourceTo(crmLeadsSearchBean, filters, builder);
//        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getAssignedTo() != null) {
            filters.put("AssignedTo", invOrganizationSiteDTOSearch.getAssignedTo().getId());
            builder.append(" AND i.assignedTo.id = :AssignedTo");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom() != null) {
            filters.put("LearnAboutCompanyfrom", invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom().getId());
            builder.append(" AND l.id = :LearnAboutCompanyfrom");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getProjectName() != null) {
            filters.put("projectName", invOrganizationSiteDTOSearch.getProjectName().getId());
            builder.append(" AND i.projectName.id = :projectName");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLeadStatus() != null) {
            filters.put("leadStatus", invOrganizationSiteDTOSearch.getLeadStatus().getId());
            builder.append(" AND i.leadStatus.id = :leadStatus");
        }

        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (i.code like CONCAT('%',:globalFilter ,'%')  OR i.mobile  like CONCAT('%',:globalFilter ,'%') OR i.name like CONCAT('%',:globalFilter ,'%') OR  i.assignedTo.name like CONCAT('%',:globalFilter ,'%') OR  i.leadStatus.name like CONCAT('%',:globalFilter ,'%') OR   FUNCTION('DATE_FORMAT',i.creationDate, '%d-%m-%Y') like CONCAT('%',:globalFilter ,'%')) ";
            String filter2 = "OR l.name like CONCAT('%',:globalFilter ,'%') ";
            builder.append(filter);
            builder.append(filter2);
        } else {
            filters.remove("globalFilter");
        }
    }

    private void appendSourceTo(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getSourceListTo() != null) {
            params.put("sourceListTo", crmLeadsSearchBean.getSourceListTo().getSerial());
            query.append(" and i.LearnAboutCompanyfrom.serial <= :sourceListTo ");

        }
    }

    private void appendSourceFrom(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getSourceListFrom() != null) {
            params.put("sourceListFrom", crmLeadsSearchBean.getSourceListFrom().getSerial());
            query.append(" and i.LearnAboutCompanyfrom.serial >= :sourceListFrom ");

        }
    }

    private void appendEmpTo(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getEmpTo() != null) {
            params.put("empTo", crmLeadsSearchBean.getEmpTo().getCode());
            query.append(" and i.assignedTo.code <= :empTo ");

        }
    }

    private void appendEmpFrom(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getEmpFrom() != null) {
            params.put("empFrom", crmLeadsSearchBean.getEmpFrom().getCode());
            query.append(" and i.assignedTo.code >= :empFrom ");

        }
    }

    private void appendDateTo(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getDateTo() != null) {
            params.put("creationDateTo", crmLeadsSearchBean.getDateTo());

            query.append(" and i.creationDate <= :creationDateTo");
        }
    }

    private void appendDateFrom(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getDateFrom() != null) {
            params.put("creationDateFrom", crmLeadsSearchBean.getDateFrom());

            query.append(" and i.creationDate >= :creationDateFrom");
        }
    }

    private void appendCodeTo(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getCodeTo() != null) {
            params.put("codeTo", crmLeadsSearchBean.getCodeTo().getCode());
            query.append(" and i.code <= :codeTo");
        }
    }

    private void appendCodeFrom(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getCodeFrom() != null) {
            params.put("codeFrom", crmLeadsSearchBean.getCodeFrom().getCode());
            query.append(" and i.code >= :codeFrom ");
        }
    }

    private void appendFilter(Map<String, Object> filters, StringBuilder builder, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer assignedTo, Integer type) {
        StringBuilder empIds = new StringBuilder();
        if (type == 1) {
            if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getAssignedTo() != null) {
                empIds = employeeService.returnEmployeeList(invOrganizationSiteDTOSearch.getAssignedTo().getId(), empIds);
            } else {
                empIds = employeeService.returnEmployeeList(assignedTo, empIds);
            }
        } else {
            empIds.append(assignedTo);
        }
        filters.put("branchId", invOrganizationSiteDTOSearch.getBranchId());
        builder.append(" AND  i.assignedTo.id  IN ( ").append(empIds.toString()).append(" )  ");

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom() != null) {
            filters.put("LearnAboutCompanyfrom", invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom().getId());
            builder.append(" AND l.id = :LearnAboutCompanyfrom");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getProjectName() != null) {
            filters.put("projectName", invOrganizationSiteDTOSearch.getProjectName().getId());
            builder.append(" AND i.projectName.id = :projectName");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLeadStatus() != null) {
            filters.put("leadStatus", invOrganizationSiteDTOSearch.getLeadStatus().getId());
            builder.append(" AND i.leadStatus.id = :leadStatus");
        } else {
            builder.append("  AND i.leadStatus.serial != 0 ");
        }

        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (i.code like CONCAT('%',:globalFilter ,'%')  OR i.mobile  like CONCAT('%',:globalFilter ,'%') OR i.name like CONCAT('%',:globalFilter ,'%') OR i.assignedTo.name like CONCAT('%',:globalFilter ,'%')  OR i.leadStatus.name like CONCAT('%',:globalFilter ,'%') OR  FUNCTION('DATE_FORMAT',i.creationDate, '%d-%m-%Y') like CONCAT('%',:globalFilter ,'%')) ";
            String filter2 = "OR (l.name like CONCAT('%',:globalFilter ,'%') AND i.assignedTo.id  IN (" + empIds.toString() + ") )";

            builder.append(filter);
            builder.append(filter2);
        } else {
            filters.remove("globalFilter");
        }
    }

    private void returnListDTO(List<InvOrganizationSite> list, List<InvOrganizationSiteDTO> dTOList, Boolean getCRMInformation) {
        for (InvOrganizationSite guide : list) {
            dTOList.add(mapTODTO(null, guide, getCRMInformation));
        }

    }

    public InvOrganizationSiteDTO mapTODTO(InvOrganizationSiteDTO dTO, InvOrganizationSite entity, Boolean getCRMInformation) {
        if (dTO == null) {
            dTO = new InvOrganizationSiteDTO();
        }
        initMapToDTO(dTO, entity, getCRMInformation);
        return dTO;
    }

    private List<InvOrganizationSiteDTO> returnListDTO(List<InvOrganizationSite> list, Boolean cRMInformation) {
        List<InvOrganizationSiteDTO> DTOlist = new ArrayList();
        returnListDTO(list, DTOlist, cRMInformation);
        return DTOlist;
    }

    @Override
    public List<InvOrganizationSiteDTO> getLeads(Integer branchId, Integer type, Date creationDateFrom, Date creationDateTo, String codeFrom, String codeTo) {

        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);
        params.put("type", type);
        params.put("codeFrom", codeFrom);
        params.put("codeTo", codeTo);
        params.put("creationDateFrom", creationDateFrom);
        params.put("creationDateTo", creationDateTo);
        List<InvOrganizationSite> details = dao.findListByQuery("select i from InvOrganizationSite i where i.type = :type and i.creationDate >= :creationDateFrom and i.creationDate <= :creationDateTo and i.branchId.id = :branchId and i.code >= :codeFrom and i.code <= :codeTo", params);

        return returnListDTO(details, true);
    }

    @Override
    public List<InvOrganizationSiteDTO> getLeads(Integer branchId, Integer type, CrmLeadsSearchBean crmLeadsSearchBean) {

        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);
        params.put("type", type);

        StringBuilder query = new StringBuilder();
        query.append("select i from InvOrganizationSite i where i.type = :type and i.branchId.id = :branchId ");
        if (crmLeadsSearchBean != null) {
            appendCodeFrom(crmLeadsSearchBean, params, query);
            appendCodeTo(crmLeadsSearchBean, params, query);
            appendDateFrom(crmLeadsSearchBean, params, query);
            appendDateTo(crmLeadsSearchBean, params, query);
            appendEmpFrom(crmLeadsSearchBean, params, query);
            appendEmpTo(crmLeadsSearchBean, params, query);
            appendSourceFrom(crmLeadsSearchBean, params, query);
            appendSourceTo(crmLeadsSearchBean, params, query);
        }

        List<InvOrganizationSite> details = dao.findListByQuery(query.toString(), params);

        return returnListDTO(details, true);
    }

    @Override
    public List<InvOrganizationSiteDTO> findALLCrmLeads(int first, int pageSize, String sortField, Boolean asc, Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer assignedTo, Integer type) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT i FROM InvOrganizationSite i LEFT JOIN  i.LearnAboutCompanyfrom l where i.branchId.id=:branchId AND i.type = 5  ");
        appendFilter(filters, builder, invOrganizationSiteDTOSearch, assignedTo, type);
        builder.append("  ORDER BY CAST(i.code SIGNED)   DESC ");
        List<InvOrganizationSite> leadsList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTO(leadsList, true);
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer assignedTo, Integer type) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(i) FROM InvOrganizationSite i LEFT JOIN  i.LearnAboutCompanyfrom l WHERE i.branchId.id = :branchId AND i.type = 5");
        appendFilter(filters, builder, invOrganizationSiteDTOSearch, assignedTo, type);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details;
    }

    @Override
    public List<InvOrganizationSiteDTO> findLeadsList(InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer assignedTo, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        StringBuilder empIds = new StringBuilder();

        query.append("SELECT i FROM InvOrganizationSite i where i.branchId.id=:branchId AND i.type = 5  ");
        params.put("branchId", invOrganizationSiteDTOSearch.getBranchId());

        if (type == 1) {
            if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getAssignedTo() != null) {
                empIds = employeeService.returnEmployeeList(invOrganizationSiteDTOSearch.getAssignedTo().getId(), empIds);
            } else {
                empIds = employeeService.returnEmployeeList(assignedTo, empIds);
            }
        } else {
            empIds.append(assignedTo);
        }

        query.append(" AND  i.assignedTo.id  IN ( ").append(empIds.toString()).append(" )  ");

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom() != null) {
            params.put("LearnAboutCompanyfrom", invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom().getId());
            query.append(" AND i.LearnAboutCompanyfrom.id = :LearnAboutCompanyfrom");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getProjectName() != null) {
            params.put("projectName", invOrganizationSiteDTOSearch.getProjectName().getId());
            query.append(" AND i.projectName.id = :projectName");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLeadStatus() != null) {
            params.put("leadStatus", invOrganizationSiteDTOSearch.getLeadStatus().getId());
            query.append(" AND i.leadStatus.id = :leadStatus");
        } else {
            query.append("  AND i.leadStatus.serial != 0 ");
        }
        query.append("  ORDER BY CAST(i.code SIGNED)   DESC ");
        List<InvOrganizationSite> leadsList = dao.findListByQuery(query.toString(), params);

        return returnListDTO(leadsList, true);

    }

    @Override
    public List<InvOrganizationSiteDTO> findAllLeadsList(InvOrganizationSiteDTO invOrganizationSiteDTOSearch) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        query.append("SELECT i FROM InvOrganizationSite i where i.branchId.id=:branchId AND i.type = 5  ");
        params.put("branchId", invOrganizationSiteDTOSearch.getBranchId());

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom() != null) {
            params.put("LearnAboutCompanyfrom", invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom().getId());
            query.append(" AND i.LearnAboutCompanyfrom.id = :LearnAboutCompanyfrom");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getProjectName() != null) {
            params.put("projectName", invOrganizationSiteDTOSearch.getProjectName().getId());
            query.append(" AND i.projectName.id = :projectName");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLeadStatus() != null) {
            params.put("leadStatus", invOrganizationSiteDTOSearch.getLeadStatus().getId());
            query.append(" AND i.leadStatus.id = :leadStatus");
        }

        query.append("  ORDER BY CAST(i.code SIGNED)   DESC ");
        List<InvOrganizationSite> leadsList = dao.findListByQuery(query.toString(), params);

        return returnListDTO(leadsList, true);
    }

    @Override
    public List<InvOrganizationSiteDTO> findLeads(int first, int pageSize, String sortField, Boolean asc, Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer type, Integer branchId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT i FROM InvOrganizationSite i LEFT JOIN  i.LearnAboutCompanyfrom l  where i.branchId.id=:branchId AND i.type = :type  ");
        appendFilter2(filters, builder, invOrganizationSiteDTOSearch, type, branchId);
        List<InvOrganizationSite> leadsList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTO(leadsList, true);
    }

    @Override
    public ChartDTO getCountCustomerByLearnAboutCompanyfrom(Integer branchId, Date dateFrom, Date dateTo) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT count(e.id) , e.LearnAboutCompanyfrom.name FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 ");
        if (dateFrom != null) {
            Calendar calFrom = Calendar.getInstance();
            calFrom.setTime(dateFrom);
            calFrom.set(Calendar.HOUR_OF_DAY, 0);
            calFrom.set(Calendar.MINUTE, 0);
            calFrom.set(Calendar.SECOND, 0);
            calFrom.set(Calendar.MILLISECOND, 0);
            params.put("dateFrom", calFrom.getTime());
            builder.append(" AND ((e.modificationDate IS NULL OR e.creationDate >= :dateFrom ) AND (e.modificationDate IS NOT NULL OR e.modificationDate >= :dateFrom))  ");
        }
        if (dateTo != null) {
            Calendar calTo = Calendar.getInstance();
            calTo.setTime(dateTo);
            calTo.set(Calendar.HOUR_OF_DAY, 0);
            calTo.set(Calendar.MINUTE, 0);
            calTo.set(Calendar.SECOND, 0);
            calTo.set(Calendar.MILLISECOND, 0);
            params.put("dateTo", calTo.getTime());
            builder.append(" AND  ((e.modificationDate IS NULL OR e.creationDate <= :dateTo ) AND (e.modificationDate IS NOT NULL OR e.modificationDate <= :dateTo)) ");
        }
        builder.append(" group by e.LearnAboutCompanyfrom.id ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه العملاء حسب المصدر ";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountCustomerByleadStatus(Integer branchId, Date dateFrom, Date dateTo) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(e.id) , e.leadStatus.name FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 ");
        if (dateFrom != null) {
            Calendar calFrom = Calendar.getInstance();
            calFrom.setTime(dateFrom);
            calFrom.set(Calendar.HOUR_OF_DAY, 0);
            calFrom.set(Calendar.MINUTE, 0);
            calFrom.set(Calendar.SECOND, 0);
            calFrom.set(Calendar.MILLISECOND, 0);
            params.put("dateFrom", calFrom.getTime());
            builder.append(" AND ((e.modificationDate IS NULL OR e.creationDate >= :dateFrom ) AND (e.modificationDate IS NOT NULL OR e.modificationDate >= :dateFrom))  ");
        }
        if (dateTo != null) {
            Calendar calTo = Calendar.getInstance();
            calTo.setTime(dateTo);
            calTo.set(Calendar.HOUR_OF_DAY, 0);
            calTo.set(Calendar.MINUTE, 0);
            calTo.set(Calendar.SECOND, 0);
            calTo.set(Calendar.MILLISECOND, 0);
            params.put("dateTo", calTo.getTime());
            builder.append(" AND  ((e.modificationDate IS NULL OR e.creationDate <= :dateTo ) AND (e.modificationDate IS NOT NULL OR e.modificationDate <= :dateTo)) ");
        }
        builder.append(" group by e.leadStatus.id ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه العملاء حسب الحاله ";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountCustomerByAssignedTo(Integer branchId, Date fromDate, Date toDate) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(e.id) , e.assignedTo.name FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 ");
        filterChartDate(params, builder, fromDate, toDate);
        builder.append(" group by e.assignedTo.id  ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه العملاء حسب الموظف ";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountCustomerByAssignedToAndStatus(Integer branchId, Integer status, Date fromDate, Date toDate) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("status", status);
        builder.append("SELECT count(e.id) , e.assignedTo.name FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND e.leadStatus.id = :status ");
        filterChartDate(params, builder, fromDate, toDate);
        builder.append(" group by e.assignedTo.id  ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه العملاء حسب الحاله للموظف ";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountCustomerByProjectName(Integer branchId, Date dateFrom, Date dateTo) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        dao.executeNativeQuery("select toby.sql_mode_function()");
        builder.append("SELECT count(e.id) , e.projectName.name FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 ");
        if (dateFrom != null) {
            Calendar calFrom = Calendar.getInstance();
            calFrom.setTime(dateFrom);
            calFrom.set(Calendar.HOUR_OF_DAY, 0);
            calFrom.set(Calendar.MINUTE, 0);
            calFrom.set(Calendar.SECOND, 0);
            calFrom.set(Calendar.MILLISECOND, 0);
            params.put("dateFrom", calFrom.getTime());
            builder.append(" AND ((e.modificationDate IS NULL OR e.creationDate >= :dateFrom ) AND (e.modificationDate IS NOT NULL OR e.modificationDate >= :dateFrom))  ");
        }
        if (dateTo != null) {
            Calendar calTo = Calendar.getInstance();
            calTo.setTime(dateTo);
            calTo.set(Calendar.HOUR_OF_DAY, 0);
            calTo.set(Calendar.MINUTE, 0);
            calTo.set(Calendar.SECOND, 0);
            calTo.set(Calendar.MILLISECOND, 0);
            params.put("dateTo", calTo.getTime());
            builder.append(" AND  ((e.modificationDate IS NULL OR e.creationDate <= :dateTo ) AND (e.modificationDate IS NOT NULL OR e.modificationDate <= :dateTo)) ");
        }
        builder.append(" group by e.projectName.id  ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه العملاء حسب المشروع ";
        return preperChartPie(detailses, title);
    }

    private ChartDTO preperChartPie(List<Object[]> details, String title) {
        ChartDTO chartDTO = new ChartDTO();
        int i = 0;
        for (Object[] detail : details) {
            chartDTO.getPieValues().add(((Number) detail[0]).intValue());
            chartDTO.getPieLabels().add(detail[1].toString());
            chartDTO.getPieColers().add(getColors().get(i));
            if (i < getColors().size() - 1) {
                i++;
            } else {
                i = 0;
            }
        }
        chartDTO.setTitle(title);
        return chartDTO;
    }

    private List<String> getColors() {
        List<String> colors = new ArrayList<>();
        colors.add("rgb(255, 99, 132)");//Red");
        colors.add("rgb(75, 192, 192)");//Green
        colors.add("rgb(255, 205, 86)");//Yellow
        colors.add("rgb(201, 203, 207)");//Grey
        colors.add("rgb(54, 162, 235)");//Blue
        return colors;
    }

    private StringBuilder filterChartDate(Map<String, Object> filters, StringBuilder builder, Date fromDate, Date toDate) {
        Date maxDate = null;
        if ((fromDate != null || toDate != null) && (fromDate == null || toDate == null)) {
            maxDate = getmaxCreationDate((Integer) filters.get("branchId"), 5);
            filters.put("maxDate", maxDate);
        }
        if (fromDate != null && toDate == null && maxDate != null) {
            builder.append(" AND e.creationDate BETWEEN :fromDate AND :maxDate ");
            filters.put("fromDate", fromDate);
        }

        if (toDate != null && fromDate == null && maxDate != null) {
            builder.append(" AND e.creationDate BETWEEN :maxDate AND :toDate ");
            filters.put("toDate", toDate);
        }

        if (fromDate != null && toDate != null) {
            builder.append(" AND e.creationDate BETWEEN :fromDate AND :toDate ");
            filters.put("fromDate", fromDate);
            filters.put("toDate", toDate);
        }
        return builder;
    }

    private Date getmaxCreationDate(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        return dao.findEntityByQuery("SELECT e.creationDate FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = :type ", params);
    }

    @Override
    public Integer getNumberOfCustomerByBranchIdAndAssignedTo(Integer branchId, Integer assignedTo, Integer createdBy) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        if (assignedTo != null) {
            params.put("assignedTo", assignedTo);
            Long invOrganizationSiteCount = dao.findEntityByQuery("SELECT count(e.id) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND (e.assignedTo.id = :assignedTo ) AND e.leadStatus.serial != 0", params);
            return invOrganizationSiteCount.intValue();
        }
        return 0;
    }

    @Override
    public Integer getNumberOfNewCustomerByBranchIdAndAssignedTo(Integer branchId, Integer assignedTo, Integer createdBy) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        if (assignedTo != null) {
            params.put("assignedTo", assignedTo);

            Long invOrganizationSiteCount = dao.findEntityByQuery("SELECT count(e) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND e.assignedTo.id  = :assignedTo AND  e.leadStatus.serial = 1", params);
            //AND e.id NOT IN (SELECT c.customerId.id FROM CrmEvent c WHERE c.branchId.id = :branchId)
            return invOrganizationSiteCount.intValue();
        }
        return 0;
    }

    @Override
    public Long getTotalRegistors(Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer type, Integer branchId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(i) FROM InvOrganizationSite i LEFT JOIN  i.LearnAboutCompanyfrom l  WHERE i.branchId.id = :branchId AND i.type = :type");
        appendFilter2(filters, builder, invOrganizationSiteDTOSearch, type, branchId);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details;
    }

    @Override
    public synchronized String saveTransferLead(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, TobyUser tobyUser) {
        StringBuilder error = new StringBuilder();
        try {
            if (invOrganizationSiteDTOList != null && !invOrganizationSiteDTOList.isEmpty()) {
                for (InvOrganizationSiteDTO invOrganizationSiteDTO : invOrganizationSiteDTOList) {
                    Boolean validate = Boolean.FALSE;
                    if (invOrganizationSiteDTO == null || (invOrganizationSiteDTO != null && invOrganizationSiteDTO.getId() == null)
                            || (invOrganizationSiteDTO != null && invOrganizationSiteDTO.getTransferTo() == null)
                            || (invOrganizationSiteDTO != null && invOrganizationSiteDTO.getTransferTo() != null && invOrganizationSiteDTO.getTransferTo().getId() == null)) {
                        error.append("يجب اختيار الموظف المتابع للعميل رقم " + invOrganizationSiteDTO.getCode() + " </br>");
                        validate = Boolean.TRUE;
                    }
                    if (!validate) {
                        if (invOrganizationSiteDTO != null && (invOrganizationSiteDTO.getMsg() == null || invOrganizationSiteDTO.getMsg().isEmpty())) {
                            Employee emp = new Employee();
                            emp.setId(invOrganizationSiteDTO.getTransferTo().getId());
                            Map<String, Object> params = new HashMap<>();
                            params.put("id", invOrganizationSiteDTO.getId());
                            params.put("assignedTo", emp);
                            params.put("modifiedBy", tobyUser);
                            params.put("modificationDate", new Date());
                            String query = "UPDATE InvOrganizationSite i SET i.assignedTo = :assignedTo  , i.modificationDate = :modificationDate , i.modifiedBy = :modifiedBy  WHERE i.id = :id ";
                            dao.executeUpdateQuery(query, params);
                        }
                    }
                }
            }

        } catch (Exception e) {
            error = new StringBuilder();
            error.append("حدث خطاء");
            dao.rollbackQuery();
        }

        return error.toString();
    }

    @Override
    public InvOrganizationSiteDTO getorganizationSiteByBranchIdAndCodeDTOSmall(Integer branchId, boolean active, Integer type, String code) {

        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        appendOrganizationTypeAndActiveAndCode(params, type, sql, active, code);
        InvOrganizationSite detailses = dao.findEntityByQuery(sql.toString(), params);
        return mapTODTOSmall(detailses);
    }

    private void appendOrganizationTypeAndActiveAndCode(Map<String, Object> params, Integer type, StringBuilder sql, boolean active, String code) {
        params.put("type", type);
        params.put("code", code);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ");

        if (type == 0 || type == 1) {
            sql.append(" AND (e.type = :type OR e.type = 2) ");
        } else {
            sql.append(" AND e.type = :type  ");
        }

        if (active) {
            sql.append(" AND e.active = 1 ");
        }

        sql.append(" AND e.code = :code");
    }

    @Override
    public InvOrganizationSite getorganizationSiteByBranchIdAndCodeSmall(Integer branchId, boolean active, Integer type, String code) {

        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        appendOrganizationTypeAndActiveAndCode(params, type, sql, active, code);
        InvOrganizationSite detailses = dao.findEntityByQuery(sql.toString(), params);
        InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
        invOrganizationSite.setId(detailses.getId());
        invOrganizationSite.setCode(detailses.getCode());
        invOrganizationSite.setName(detailses.getName());
        detailses = null;
        return invOrganizationSite;
    }

    @Override
    public Boolean checknationalId(Integer branchId, String nationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("nationId", nationId);
        StringBuilder query = new StringBuilder();
        query.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND  e.nationId = :nationId  ");
        InvOrganizationSite detailses = dao.findEntityByQuery(query.toString(), params);
        if (detailses == null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public ChartDTO getCountLeadByAssignedToChartBar(Integer branchId, Date fromDate, Date toDate) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        builder.append("SELECT i.assignedTo.name , count(i.id) , i.leadStatus.name FROM InvOrganizationSite i WHERE i.branchId.id = :branchId AND i.type = 5 ");
        filterChartDate(params, builder, fromDate, toDate);
        builder.append("  group by  i.assignedTo.id , i.leadStatus.id order by  i.assignedTo.id");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه المهمات حسب الموظف ";
        return preperChart(detailses, "المهمه", "العدد", title);
    }

    private ChartDTO preperChart(List<Object[]> details, String xLabel, String yLabel, String title) {
        Map<Object, Number> map = new HashMap<>();
        Set<String> setEmp = new HashSet<String>();
        List<ChartDataDTO> chartDataDTOList = new ArrayList<>();
        ChartDataDTO chartDataDTO = new ChartDataDTO();
        int max = 0;

        for (Object[] detail : details) {
            setEmp.add(detail[0].toString());
            if (max < (((Number) detail[1]).intValue())) {
                max = ((Number) detail[1]).intValue();
            }
            if (chartDataDTO.getLabel() != null && !chartDataDTO.getLabel().equals(detail[2].toString())) {
                chartDataDTO.setMap(map);
                chartDataDTOList.add(chartDataDTO);
                map = new HashMap<>();
                chartDataDTO = new ChartDataDTO();
            }
            map.put(detail[0].toString(), (Number) detail[1]);
            chartDataDTO.setLabel(detail[2].toString());
        }
        if (chartDataDTOList.isEmpty() && !map.isEmpty()) {
            chartDataDTO.setMap(map);
            chartDataDTOList.add(chartDataDTO);
        }
        chartDataDTOList.forEach(obj -> {
            setEmp.forEach(emp -> {
                if (!obj.getMap().containsKey(emp)) {
                    obj.getMap().put(emp, 0);
                }
            });
        });

        ChartDTO chartDTO = new ChartDTO();
        chartDTO.setMax(max);
        chartDTO.setMin(0);
        chartDTO.setxLabel(xLabel);
        chartDTO.setyLabel(yLabel);
        chartDTO.setTitle(title);
        chartDTO.setLegendPosition("ne");
        chartDTO.setChartDataDTOList(chartDataDTOList);
        return chartDTO;
    }

    @Override
    public ChartDTO getCountLeadByAssignedToANDContractTypeChartBar(Integer branchId, Date fromDate, Date toDate) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        builder.append("SELECT e.assignedTo.name , count(e.id) , e.leadStatus.name FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND e.leadStatus.serial = 10 ");
        filterChartDate(params, builder, fromDate, toDate);
        builder.append("  group by  e.assignedTo.id  order by  e.assignedTo.id");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "عدد التعاقد لكل موظف ";
        return preperChart(detailses, "الموظف", "العدد", title);
    }

    @Override
    public String validateSaveLead(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser) {
        StringBuilder error = new StringBuilder();

        if (invOrganizationSiteDTO.getType() == 5 && invOrganizationSiteDTO.getEmployeeIsSupervisor()) {
            if (invOrganizationSiteDTO.getAssignedTo() == null && tobyUser.getEmployeeId() == null) {
                error.append("\n اعطاء المستخدم لموظف معين");
            }
        }
        if (invOrganizationSiteDTO != null) {

            if ((invOrganizationSiteDTO.getEmail() == null || invOrganizationSiteDTO.getEmail().isEmpty())) {
                error.append("\nيجب تسجيل بيان للاتصال ");
            }
            String vaildationContacts = vaildationContacts(invOrganizationSiteDTO, tobyUser);
            if (vaildationContacts != null && !vaildationContacts.isEmpty()) {
                error.append(vaildationContacts);
            }
            if (invOrganizationSiteDTO.getAssignedTo() == null) {
                error.append("\nيجب اختيار الموظف المتابع للعميل ");
            }
            if (invOrganizationSiteDTO.getCrmEventDTOList() != null && !invOrganizationSiteDTO.getCrmEventDTOList().isEmpty()) {
                invOrganizationSiteDTO.getCrmEventDTOList().forEach(event -> {
                    if (event.getLeadStatus() == null) {
                        error.append("اختار حاله العميل");
                    }
                });
            }
            validateSmallSaveLead(error, invOrganizationSiteDTO, tobyUser);
        } else {
            error.append("لا يوجد تفاصيل");
        }

        return (error == null || error.toString().isEmpty()) ? null : error.toString();
    }

    @Override
    public String validateSmallSaveLead(StringBuilder error, InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser) {

        if (invOrganizationSiteDTO != null) {
            if (invOrganizationSiteDTO.getName() == null || invOrganizationSiteDTO.getName().isEmpty()) {
                error.append("\n يجب تسجيل اسم العميل");
            }
            if ((invOrganizationSiteDTO.getMobile() == null || invOrganizationSiteDTO.getMobile().isEmpty())) {
                error.append("\nيجب تسجيل بيان للاتصال ");
            }

        } else {
            error.append("لا يوجد تفاصيل");
        }

        return (error == null || error.toString().isEmpty()) ? null : error.toString();
    }

    @Override
    public synchronized InvOrganizationSiteDTO saveSmallLead(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser) {
        try {
            StringBuilder error = new StringBuilder();
            validateSmallSaveLead(error, invOrganizationSiteDTO, tobyUser);
            if (error.toString().isEmpty()) {
                InvOrganizationSite invOrganizationSite;
                if (invOrganizationSiteDTO != null && (invOrganizationSiteDTO.getMsg() == null || invOrganizationSiteDTO.getMsg().isEmpty())) {
                    invOrganizationSite = mapToEntity(invOrganizationSiteDTO, tobyUser);
                    invOrganizationSite.setType(5);
                    if (invOrganizationSite.getId() == null) {
                        Integer code = getMaxGenericSerialForDetail(tobyUser.getBranchId().getId(), 5);
                        invOrganizationSite.setCode(code.toString());
                    }

                    invOrganizationSite = dao.updateEntity(invOrganizationSite);
                    invOrganizationSiteDTO = mapTODTO(invOrganizationSiteDTO, invOrganizationSite, true);
                }
            } else {
                invOrganizationSiteDTO.setMsg(error.toString());
            }
        } catch (Exception e) {
            invOrganizationSiteDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return invOrganizationSiteDTO;
    }

    @Override
    public String vaildationContacts(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser) {
        StringBuilder error = new StringBuilder();
        if (invOrganizationSiteDTO != null) {
            List<InvOrganizationSiteDTO> list = findOrganizationSiteDTOByContact(tobyUser.getBranchId().getId(), invOrganizationSiteDTO.getMobile(), invOrganizationSiteDTO.getEmail());
            if (!list.isEmpty() && (list.size() > 1 || !list.get(0).getCode().equalsIgnoreCase(invOrganizationSiteDTO.getCode()))) {
                error.append("\nالعميل موجود من قبل ");
            }
        }
        return (error == null || error.toString().isEmpty()) ? null : error.toString();
    }

    @Override
    public List<InvOrganizationSiteDTO> findOrganizationSiteDTOByContact(Integer branchId, String mobile, String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 ");
        if (mobile != null && email != null && !mobile.isEmpty() && !email.isEmpty()) {
            params.put("mobile", mobile);
            params.put("email", email);
            builder.append(" AND (e.mobile = :mobile OR e.email = :email)");
        } else {
            if (mobile != null && !mobile.isEmpty()) {
                params.put("mobile", mobile);
                builder.append(" AND function('RIGHT',e.mobile,8) = function('RIGHT',:mobile,8)");
            }
            if (email != null && !email.isEmpty()) {
                params.put("email", email);
                builder.append(" AND e.email = :email");
            }
        }
        List<InvOrganizationSite> detailses = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(detailses, true);
    }

    @Override
    public synchronized InvOrganizationSiteDTO saveLead(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser) {
        try {
            String error = validateSaveLead(invOrganizationSiteDTO, tobyUser);
            if (error == null) {
                InvOrganizationSite invOrganizationSite;
                if (invOrganizationSiteDTO != null && (invOrganizationSiteDTO.getMsg() == null || invOrganizationSiteDTO.getMsg().isEmpty())) {
                    invOrganizationSite = mapToEntity(invOrganizationSiteDTO, tobyUser);
                    invOrganizationSite.setType(5);
                    if (invOrganizationSite.getId() == null) {
                        Integer code = getMaxGenericSerialForDetail(tobyUser.getBranchId().getId(), 5);
                        invOrganizationSite.setCode(code.toString());
                    }

                    invOrganizationSite = dao.updateEntity(invOrganizationSite);

                    // cRMCustomerInterestedProductsServiceDTO.deleteCrmCustomerInterestedProductDTOList(invOrganizationSiteDTO.getCrmCustomerInterestedProductsDTODeleteList(), tobyUser);
                    // cRMCustomerInterestedProductsServiceDTO.saveList(invOrganizationSiteDTO.getCrmCustomerInterestedProductsDTOList(), invOrganizationSite.getId(), tobyUser);
                    cRMEventServiceDTO.deleteCrmEventDTOList(invOrganizationSiteDTO.getCrmEventDTODeletedList(), tobyUser);
                    cRMEventServiceDTO.saveList(invOrganizationSiteDTO.getCrmEventDTOList(), invOrganizationSite.getId(), invOrganizationSite.getName(), invOrganizationSite.getAssignedTo().getId(), tobyUser);

                    invOrganizationSiteDTO = mapTODTO(invOrganizationSiteDTO, invOrganizationSite, true);
                }
            } else {
                invOrganizationSiteDTO.setMsg(error);
            }
        } catch (Exception e) {
            invOrganizationSiteDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return invOrganizationSiteDTO;
    }

    @Override
    public InvOrganizationSiteDTO findOrganizationSiteDTOById(Integer invOrganizationSiteId) {
        InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
        InvOrganizationSite invOrganizationSite = dao.findEntityById(InvOrganizationSite.class, invOrganizationSiteId);
        return mapTODTO(invOrganizationSiteDTO, invOrganizationSite, true);
    }

    @Override
    public Integer getNumberOfCustomerByBranchId(Integer branchId, Integer assignedTo, Integer createdBy, Date dateFrom, Date dateTo) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        Long invOrganizationSiteCount = 0l;
        if (assignedTo != null) {
            StringBuilder empIds = new StringBuilder();
            empIds = employeeService.returnEmployeeList(assignedTo, empIds);
            invOrganizationSiteCount = dao.findEntityByQuery("SELECT count(e.id) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND (e.assignedTo.id in ( " + empIds + " ) ) AND e.leadStatus.serial != 0", params);
            return invOrganizationSiteCount.intValue();
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT count(e.id) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 ");
            if (dateFrom != null) {
                Calendar calFrom = Calendar.getInstance();
                calFrom.setTime(dateFrom);
                calFrom.set(Calendar.HOUR_OF_DAY, 0);
                calFrom.set(Calendar.MINUTE, 0);
                calFrom.set(Calendar.SECOND, 0);
                calFrom.set(Calendar.MILLISECOND, 0);
                params.put("dateFrom", calFrom.getTime());
                builder.append(" AND  e.creationDate >= :dateFrom  ");

            }
            if (dateTo != null) {
                Calendar calTo = Calendar.getInstance();
                calTo.setTime(dateTo);
                calTo.set(Calendar.HOUR_OF_DAY, 0);
                calTo.set(Calendar.MINUTE, 0);
                calTo.set(Calendar.SECOND, 0);
                calTo.set(Calendar.MILLISECOND, 0);
                params.put("dateTo", calTo.getTime());
                builder.append(" AND  e.creationDate <= :dateTo  ");
            }
            invOrganizationSiteCount = dao.findEntityByQuery(builder.toString(), params);
            return invOrganizationSiteCount.intValue();
        }
    }

    @Override
    public Integer getNumberOfNewCustomerByBranchId(Integer branchId, Integer assignedTo, Integer createdBy, Date dateFrom, Date dateTo) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        Long invOrganizationSiteCount = 0L;
        if (assignedTo != null) {
            StringBuilder empIds = new StringBuilder();
            empIds = employeeService.returnEmployeeList(assignedTo, empIds);
            invOrganizationSiteCount = dao.findEntityByQuery("SELECT count(e.id) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND (e.assignedTo.id in ( " + empIds + " )) AND e.id NOT IN (SELECT c.customerId.id FROM CrmEvent c WHERE c.branchId.id = :branchId) AND e.leadStatus.serial != 0", params);
            return invOrganizationSiteCount.intValue();
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT count(e.id) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND e.id NOT IN (SELECT c.customerId.id FROM CrmEvent c WHERE c.branchId.id = :branchId) ");
            if (dateFrom != null) {
                Calendar calFrom = Calendar.getInstance();
                calFrom.setTime(dateFrom);
                calFrom.set(Calendar.HOUR_OF_DAY, 0);
                calFrom.set(Calendar.MINUTE, 0);
                calFrom.set(Calendar.SECOND, 0);
                calFrom.set(Calendar.MILLISECOND, 0);
                params.put("dateFrom", calFrom.getTime());
                builder.append(" AND  e.creationDate >= :dateFrom  ");

            }
            if (dateTo != null) {
                Calendar calTo = Calendar.getInstance();
                calTo.setTime(dateTo);
                calTo.set(Calendar.HOUR_OF_DAY, 0);
                calTo.set(Calendar.MINUTE, 0);
                calTo.set(Calendar.SECOND, 0);
                calTo.set(Calendar.MILLISECOND, 0);
                params.put("dateTo", calTo.getTime());
                builder.append(" AND  e.creationDate <= :dateTo  ");
            }
            invOrganizationSiteCount = dao.findEntityByQuery(builder.toString(), params);
            return invOrganizationSiteCount.intValue();
        }
    }

    @Override
    public ChartDTO getCountCustomerPerMonth(Integer branchId, Integer type, Integer dateFilter) {

        dao.executeNativeQuery("select toby.sql_mode_function()");
        StringBuilder builder = new StringBuilder();
        builder.append("select DATE_FORMAT(i.creation_date, ");
        String date_Filter = "";
        if (dateFilter == 0) {
            date_Filter = "'%d'";
        } else if (dateFilter == 1) {
            date_Filter = "'%m'";
        } else if (dateFilter == 2) {
            date_Filter = "'%y'";
        } else {
            date_Filter = "'%m'";
        }
        builder.append(date_Filter + " ) , count(*) ");
        builder.append("from inv_organization_site i where i.branch_Id = " + branchId);
        builder.append(" and i.type = " + type);
        builder.append(" group by DATE_FORMAT(i.creation_date, " + date_Filter + " )");
        List<Object[]> details = dao.executeNativeQuery(builder.toString());
        return preperChart(details);
    }

    private ChartDTO preperChart(List<Object[]> details) {
        Map<Object, Number> map = new HashMap<>();
        List<ChartDataDTO> chartDataDTOList = new ArrayList<>();
        ChartDataDTO chartDataDTO = new ChartDataDTO();
        Integer max = 0;
        for (Object[] detail : details) {
            map.put(detail[0].toString(), (Number) detail[1]);
            max = (max < ((Number) detail[1]).intValue() ? ((Number) detail[1]).intValue() : max);
        }
        chartDataDTO.setLabel("العملاء");
        chartDataDTO.setMap(map);
        chartDataDTOList.add(chartDataDTO);
        ChartDTO chartDTO = new ChartDTO();
        chartDTO.setMax(max);
        chartDTO.setMin(0);
        chartDTO.setxLabel("الشهر");
        chartDTO.setyLabel("عدد العملاء");
        chartDTO.setTitle("عدد العملاء لكل شهر");
        chartDTO.setLegendPosition("ne");
        chartDTO.setChartDataDTOList(chartDataDTOList);
        return chartDTO;
    }

    @Override
    public void saveOpenningBalance(BigDecimal value, String customerCode, String gallaryCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", 182);
        params.put("code", customerCode);
        params.put("gallaryCode", gallaryCode);
        List<InvOrganizationSite> list = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId and e.code = :code and e.gallaryId.code = :gallaryCode ", params);
        if (list != null && !list.isEmpty()) {
            InvOrganizationSite entity = list.get(0);
            if (value.compareTo(BigDecimal.ZERO) == 1) {
                entity.setOpenBalanceDebit(value);
            } else {
                entity.setOpenBalanceCredit(value.multiply(new BigDecimal(-1)));
            }
            System.out.println(list.size() + " -> " + customerCode + " -> " + value);
            entity.setOpenBalanceDate(new Date());
            dao.saveEntity(entity);
        }

    }

    @Override
    public InvOrganizationSiteDTO getIDByCode(String code, TobyUser tobyUser, Integer gallaryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("gallaryId", gallaryId);
        InvOrganizationSite detailses = dao.findEntityByQuery("SELECT e FROM InvOrganizationSite e WHERE e.code = :code AND e.gallaryId.id = :gallaryId  ", params);
        if (detailses == null) {
            return null;
        }
        return mapToDTO(detailses, tobyUser);
    }

    @Override
    public List<InvOrganizationSiteDTO> returnClientSales(List<InvInventoryDTO> invInventoryDTOList, Date dateFrom, Date dateTo, String value) {
        String dateFrom1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(dateFrom);
        String dateTo1 = new SimpleDateFormat("yyyy-MM-dd  23:59:59").format(dateTo);
        String gallary = returnGallaryString(invInventoryDTOList);
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ios.code , ios.name , ios.mobile from inv_purchase_invoice ipi left join inv_organization_site ios on ios.id = ipi.organization_site_id where ipi.branch_id = " + invInventoryDTOList.get(0).getBranchId()
                + " and ipi.galary_Id in (").append(gallary).append(") and ipi.date >= '").append(dateFrom1).append("' and ipi.date <= '").append(dateTo1).append("' group by ipi.organization_site_id having count(ipi.id) in(").append(value).append(")");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setCode((String) line[0]);
            invOrganizationSiteDTO.setName((String) line[1]);
            invOrganizationSiteDTO.setMobile((String) line[2]);

            invOrganizationSiteDTOList.add(invOrganizationSiteDTO);
        }
        return invOrganizationSiteDTOList;
    }

    public String returnGallaryString(List<InvInventoryDTO> invInventoryDTOList) {
        StringBuilder gallarySelected = null;
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>();
        for (InvInventoryDTO invInventoryDTO : invInventoryDTOList) {
            if (gallarySelected == null) {
                gallarySelected = new StringBuilder();
                gallarySelected.append(invInventoryDTO.getId());
            } else {
                gallarySelected.append(",");
                gallarySelected.append(invInventoryDTO.getId());
            }
        }
        return gallarySelected.toString();
    }

    @Override
    public InvOrganizationSiteDTO findOrganizationSiteDTOByCodeAndGallaryIdDTO(Integer branchId, String code, Integer gallaryId) {
        if (branchId != null && gallaryId != null && code != null && !code.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("branchId", branchId);
            params.put("code", code);
            params.put("gallaryId", gallaryId);
            InvOrganizationSite invOrganizationSiteList = dao.findEntityByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.gallaryId.id =:gallaryId AND ( e.mobile = :code OR e.code = :code )", params);
            return mapToDTOSmall(invOrganizationSiteList);
        }
        return null;
    }

}
