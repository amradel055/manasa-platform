/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.GlBankDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.OfferCopounDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlBank;
import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.OfferCopoun;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class OfferCopounServiceImpl implements OfferCopounService {

    @EJB
    private GenericDAO dao;
    @EJB
    private OrganizationSiteService organizationSiteService;

    @Override
    public void deleteOfferCopoun(OfferCopounDTO offerCopounDTO) {
        if (offerCopounDTO.getId() != null) {
            OfferCopoun offerText = new OfferCopoun();
            offerText.setId(offerCopounDTO.getId());
            dao.deleteEntity(offerText);
        }
    }

    @Override
    public List<OfferCopounDTO> getAllOfferCopoun() {
        List<OfferCopoun> offerTextList = dao.findAll(OfferCopoun.class);
        return mapToDTOList(offerTextList);
    }

    @Override
    public OfferCopounDTO addOfferCopoun(OfferCopounDTO offerCopounDTO, TobyUser tobyUser) {
        OfferCopoun offerText = mapToEntity(offerCopounDTO, tobyUser);
        dao.updateEntity(offerText);
        offerCopounDTO = mapToDTO(offerText);
        return offerCopounDTO;
    }

    @Override
    public OfferCopounDTO findOfferCopoun(Integer offerTextId) {
        OfferCopoun offerText = dao.findEntityById(OfferCopoun.class, offerTextId);
        return mapToDTO(offerText);
    }

    @Override
    public List<OfferCopounDTO> getALLOfferCopounByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<OfferCopoun> offerTextList = dao.findListByQuery("SELECT e FROM OfferCopoun e WHERE e.companyId.id =:companyId", params);
        return mapToDTOList(offerTextList);
    }

    @Override
    public List<OfferCopounDTO> getALLOfferCopounByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<OfferCopoun> offerTextList = dao.findListByQuery("SELECT e FROM OfferCopoun e WHERE e.branchId.id =:branchId", params);
        return mapToDTOList(offerTextList);
    }

    @Override
    public List<OfferCopounDTO> getALLOfferCopounByGallaryId(Integer galaryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("galaryId", galaryId);
        List<OfferCopoun> offerTextList = dao.findListByQuery("SELECT e FROM OfferCopoun e WHERE e.galaryId.id =:galaryId", params);
        return mapToDTOList(offerTextList);
    }

    @Override
    public List<OfferCopounDTO> getALLOfferCopounByOfferSearch(OfferCopounDTO offerCopounDTO) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        params.put("branchId", 182);
        query.append("SELECT e FROM OfferCopoun e WHERE e.branchId.id =:branchId ");
        if (offerCopounDTO.getGallaryType() != null) {
            params.put("gallaryType", offerCopounDTO.getGallaryType());
            query.append(" AND e.gallaryType = :gallaryType ");
        }
        if (offerCopounDTO.getBankType() != null) {
            params.put("bankType", offerCopounDTO.getBankType());
            query.append(" AND e.bankType = :bankType ");
        }
        List<OfferCopoun> offerTextList = dao.findListByQuery(query.toString(), params);
        return mapToDTOList(offerTextList);
    }

    @Override
    public String getNoticeByCustomerId(Integer id) {
        String offerTextNotice = "";
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT oc.notice FROM offer_company oc\n"
                + "join toby.inv_organization_site os\n"
                + "on os.offer_company_id = oc.id\n").
                append("where os.id =  \n ").
                append(id).append(" ");
        List<String> details = dao.executeNativeQuery(builder.toString());
        for (String customerNotice : details) {
            offerTextNotice = customerNotice;
        }
        return offerTextNotice;
    }

    public OfferCopoun mapToEntity(OfferCopounDTO dto, TobyUser tobyUser) {
        OfferCopoun entity = new OfferCopoun();
        entity.setId(dto.getId());

        entity.setCode(dto.getCode());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setRepeatNumber(dto.getRepeatNumber());
        entity.setBankType(dto.getBankType());
        entity.setDiscountType(dto.getDiscountType());
        entity.setDiscountValue(dto.getDiscountValue());
        entity.setExpire(dto.getExpire());
        entity.setGallaryType(dto.getGallaryType());

        if (dto.getGallaryId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(dto.getGallaryId().getId());
            entity.setGallaryId(invInventory);
        }

        if (dto.getCustomerId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dto.getCustomerId().getId());
            entity.setCustomerId(invOrganizationSite);
        }

        if (dto.getBankId() != null) {
            GlBank glBank = new GlBank();
            glBank.setId(dto.getBankId().getId());
            entity.setBankId(glBank);
        }

        if (dto.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(dto.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(dto.getCreatedDate());
            entity.setModifiedBy(tobyUser);
            entity.setModificationDate(new Date());
            if (dto.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(dto.getCompanyId());
                entity.setCompanyId(company);
            }
            if (dto.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(dto.getBranchId());
                entity.setBranchId(branch);
            }
        } else {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
            entity.setCompanyId(tobyUser.getCompanyId());
            entity.setBranchId(tobyUser.getBranchId());

        }
        return entity;

    }

    public OfferCopounDTO mapToDTO(OfferCopoun entity) {

        OfferCopounDTO dto = new OfferCopounDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setRepeatNumber(entity.getRepeatNumber());
        dto.setBankType(entity.getBankType());
        dto.setDiscountType(entity.getDiscountType());
        dto.setDiscountValue(entity.getDiscountValue());
        dto.setExpire(entity.getExpire());
        dto.setGallaryType(entity.getGallaryType());

        if (entity.getGallaryId() != null) {
            InvInventoryDTO inventoryDTO = new InvInventoryDTO();
            inventoryDTO.setId(entity.getGallaryId().getId());
            inventoryDTO.setName(entity.getGallaryId().getName());
            inventoryDTO.setCode(entity.getGallaryId().getCode());
            dto.setGallaryId(inventoryDTO);
        }

        if (entity.getCustomerId() != null) {
            InvOrganizationSiteDTO invOrganizationSite = new InvOrganizationSiteDTO();
            invOrganizationSite.setId(entity.getCustomerId().getId());
            dto.setCustomerId(invOrganizationSite);
        }

        if (entity.getBankId() != null) {
            GlBankDTO glBank = new GlBankDTO();
            glBank.setId(entity.getBankId().getId());
            dto.setBankId(glBank);
        }

        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setBranchId(entity.getBranchId().getId() != null ? entity.getBranchId().getId() : null);
        dto.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);

        return dto;
    }

    public List<OfferCopounDTO> mapToDTOList(List<OfferCopoun> offerTextList) {
        List<OfferCopounDTO> offerCopounDTOList = new ArrayList<>();
        for (OfferCopoun offerText : offerTextList) {
            offerCopounDTOList.add(mapToDTO(offerText));
        }
        return offerCopounDTOList;
    }

    @Override
    public void addOfferCopoun(List<OfferCopounDTO> offerCopounDTOList, TobyUser tobyUser) {
        List<InvOrganizationSiteDTO> list = new ArrayList();
        for (OfferCopounDTO dTO : offerCopounDTOList) {
            InvOrganizationSiteDTO organizationSiteDTO = organizationSiteService.getCustomerBySerial(dTO.getCustomerCode());
            if (organizationSiteDTO != null) {
                list.add(organizationSiteDTO);
                String code = generateRandomCode();
                dTO.setCustomerId(organizationSiteDTO);
                dTO.setCode(code);
                System.out.println("\n send SMS To -> " + organizationSiteDTO.getMobile());
                sendSMS(organizationSiteDTO, code);
                OfferCopoun offerText = mapToEntity(dTO, tobyUser);
                dao.updateEntity(offerText);
                mapToDTO(offerText);
            }
        }
    }

    @Override
    public void addOfferCopoun(Integer number, Integer discountType, BigDecimal discountValue, Integer gallaryType, TobyUser tobyUser) {
        for (int i = 0; i < number; i++) {
            OfferCopounDTO dTO = new OfferCopounDTO();
            String code = generateRandomCode();
            dTO.setCode(code);
            dTO.setGallaryType(gallaryType);
            dTO.setStartDate(new Date());
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2022);
            cal.set(Calendar.MONTH, 12);
            cal.set(Calendar.DAY_OF_MONTH, 30);
            dTO.setEndDate(cal.getTime());
            dTO.setBankType(1);
            dTO.setDiscountValue(discountValue);
            dTO.setDiscountType(discountType);
            OfferCopoun offerText = mapToEntity(dTO, tobyUser);
            dao.updateEntity(offerText);
        }
    }

    private void sendSMS(InvOrganizationSiteDTO organizationSiteDTO, String code) {
        StringBuilder message = new StringBuilder();
        message.append(" عميلنا المميز، عساك بخير\n"
                + "لك خصم 25% بجميع فروع برنس \n"
                + "*غير شامل الخياطة الخارجية\n"
                + "*ينتهي 5 نوفمبر، استخدم الكود ");
        message.append(code);
        message.append("\n").append("920027070");
        dao.sendSms(organizationSiteDTO.getMobile(), "التوبى العالمى", message.toString(), "20099109", "ux4uik");
        System.out.println("\n send SMS To -> " + organizationSiteDTO.getMobile());
    }

    public String generateRandomCode() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    @Override
    public BigDecimal getOfferCopounByCustomerAndCode(Integer customerId, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("dateNow", new Date());
        List<OfferCopoun> offerTextList = dao.findListByQuery("SELECT e FROM OfferCopoun e WHERE e.code = :code AND (:dateNow between e.startDate and e.endDate) AND e.code not in (select p.offerCopoun from InvPurchaseInvoice p where  p.offerCopoun is not null)", params);
        if (offerTextList != null && !offerTextList.isEmpty()) {
            return offerTextList.get(0).getDiscountValue();
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public OfferCopoun getOfferCopounByCode(String code,Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("type", type);
        params.put("dateNow", new Date());
        List<OfferCopoun> offerTextList = dao.findListByQuery("SELECT e FROM OfferCopoun e WHERE e.type = :type AND e.code = :code AND (:dateNow between e.startDate and e.endDate) AND e.code not in (select p.offerCopoun from InvPurchaseInvoice p where  p.offerCopoun is not null)", params);
        if (offerTextList != null && !offerTextList.isEmpty()) {
            return offerTextList.get(0);
        }
        return null;
    }

}
