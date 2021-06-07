/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.SupplierAccountSearchBean;
import com.toby.businessservice.reports.searchBean.invOrganizationSiteSearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.dto.OfferCompanyDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.Currency;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private GlAccountServiceDTO glAccountServiceDTO;
    @EJB
    private InvDelegatorService invDelegatorService;

    @Override
    public void updateOrganizationSiteMobile(InvOrganizationSiteDTO organizationSiteDTO) {
        if (organizationSiteDTO != null && organizationSiteDTO.getId() != null) {
            InvOrganizationSite entity = dao.findEntityById(InvOrganizationSite.class, organizationSiteDTO.getId());
            entity.setMobile(organizationSiteDTO.getMobile());
            entity.setLength(organizationSiteDTO.getLength());
            entity.setShoulder(organizationSiteDTO.getShoulder());
            entity.setStep(organizationSiteDTO.getStep());
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
                entity.setMobile(organizationSiteDTO.getMobile());
                entity.setName(organizationSiteDTO.getName());
                dao.updateEntity(entity);
            }
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
        invOrganizationSiteDTO.setCompanyName(invOrganizationSite.getCompanyId().getName());
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
    public void deleteOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser isagUser) {
        InvOrganizationSite invOrganizationSite = mapFromDTO(invOrganizationSiteDTO, isagUser);
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

        returnListDTO(detailses, dTOList);
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

        returnListDTO(detailses, list);
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
        filters.put("gallaryId", gallaryId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT i FROM InvOrganizationSite i WHERE i.branchId.id = :branchId  AND i.type = :type  AND  i.gallaryId.id = :gallaryId ");
        appendFilter(filters, builder);
        builder.append(" ORDER BY i.id DESC");
        List<InvOrganizationSite> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<InvOrganizationSiteDTO> dTOList = new ArrayList<>();
        returnListDTO(details, dTOList);
        return dTOList;
    }

    public InvOrganizationSiteDTO mapTODTO(InvOrganizationSite entity) {
        InvOrganizationSiteDTO dTO = new InvOrganizationSiteDTO();
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(InvOrganizationSiteDTO dTO, InvOrganizationSite entity) {
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
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(entity.getGallaryId().getId());
            dTO.setGallaryIdDTO(invInventoryDTO);
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
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setCompanyId(entity.getCompanyId().getId());
    }

    public InvOrganizationSite mapFromDTO(InvOrganizationSiteDTO dTO, TobyUser isagUser) {
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
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser isagUser1 = new TobyUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);

        }
        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<InvOrganizationSite> list, List<InvOrganizationSiteDTO> dTOList) {
        for (InvOrganizationSite guide : list) {
            dTOList.add(mapTODTO(guide));
        }

    }

    private List<InvOrganizationSiteDTO> returnListDTO(List<InvOrganizationSite> list) {
        List<InvOrganizationSiteDTO> DTOlist = new ArrayList();
        returnListDTO(list, DTOlist);
        return DTOlist;
    }

    private List<InvOrganizationSite> returnListEntity(List<InvOrganizationSiteDTO> list, TobyUser isagUser) {
        List<InvOrganizationSite> Entitylist = new ArrayList();
        for (InvOrganizationSiteDTO organizationSiteDTO : list) {
            Entitylist.add(mapFromDTO(organizationSiteDTO, isagUser));
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
    public List<InvOrganizationSiteDTO> AddDelegatorToCustomerDTO(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, TobyUser isagUser) {
        List<InvOrganizationSiteDTO> dTO = new ArrayList();
        List<InvOrganizationSite> invOrganizationSiteList = returnListEntity(invOrganizationSiteDTOList, isagUser);
        for (InvOrganizationSite organizationSite : invOrganizationSiteList) {
            dTO.add(mapTODTO(updateOrganizationSite(organizationSite)));
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
        return mapTODTO(invOrganizationSite);
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

    public InvOrganizationSiteDTO mapTODTOSmall(InvOrganizationSite entity) {
        InvOrganizationSiteDTO dTO = new InvOrganizationSiteDTO();
        initMapToDTOSmall(dTO, entity);
        return dTO;
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
    public InvOrganizationSiteDTO addNewCustomer(InvOrganizationSiteDTO invOrganizationSiteDTO, List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, TobyUser tobyUser) {

        CurrencyDTO currencyDefault = currencyServiceDTO.getDefaultCurrenciesByCompanyId(tobyUser.getCompanyId().getId());

        if (invOrganizationSiteDTO != null && invOrganizationSiteDTO.getName() != null && currencyDefault != null) {
            invOrganizationSiteDTO.setType(0);
            invOrganizationSiteDTO.setCurrencyId(currencyDefault);
            invOrganizationSiteDTO.setActive(1);
            Integer code = getMaxGenericSerialForDetail(tobyUser.getBranchId().getId(), 0);
            GlAccountDTO glAccountDTO = new GlAccountDTO();
            glAccountDTO.setId(8247);
            invOrganizationSiteDTO.setAccountId(glAccountDTO);
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(77);
            invOrganizationSiteDTO.setDelegatorId(inventoryDelegatorDTO);
            invOrganizationSiteDTO.setCode(code.toString());

            invOrganizationSiteDTO = addOrganizationSiteDTO(invOrganizationSiteDTO, tobyUser);
            invOrganizationSiteDTOList.add(0, invOrganizationSiteDTO);
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

}
