/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.bussinessservice.global.Utility;
import com.toby.core.GenericDAO;
import com.toby.dto.GallerySettingsDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.entity.Branch;
import com.toby.entity.GallerySettings;
import com.toby.entity.InvInventory;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class GallerySettingsServiceImpl implements GallerySettingsService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteGallerySettings(GallerySettingsDTO gallerySettingsDTO) {
        if (gallerySettingsDTO.getId() != null) {
            GallerySettings gallerySettings = new GallerySettings();
            gallerySettings.setId(gallerySettingsDTO.getId());
            dao.deleteEntity(gallerySettings);
        }
    }

    @Override
    public List<GallerySettingsDTO> getAllGallerySettings(TobyUser tobyUser) {
        List<GallerySettings> gallerySettingsList = dao.findAll(GallerySettings.class);
        return mapToDTOList(gallerySettingsList, tobyUser);
    }

    @Override
    public GallerySettingsDTO addGallerySettings(GallerySettingsDTO gallerySettingsDTO, TobyUser tobyUser) {
        GallerySettings gallerySettings = mapToEntity(gallerySettingsDTO, tobyUser);
        dao.updateEntity(gallerySettings);
        gallerySettingsDTO = mapToDTO(gallerySettings, tobyUser);
        return gallerySettingsDTO;
    }

    @Override
    public GallerySettingsDTO findGallerySettings(Integer gallerySettingsId, TobyUser tobyUser) {
        GallerySettings gallerySettings = dao.findEntityById(GallerySettings.class, gallerySettingsId);
        return mapToDTO(gallerySettings, tobyUser);
    }

    @Override
    public List<GallerySettingsDTO> getALLGlgallerySettingsByCompanyId(Integer companyId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<GallerySettings> gallerySettingsList = dao.findListByQuery("SELECT e FROM GallerySettings e WHERE e.companyId.id =:companyId", params);
        return mapToDTOList(gallerySettingsList, tobyUser);
    }

    @Override
    public List<GallerySettingsDTO> getALLGlgallerySettingsByBranchId(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<GallerySettings> gallerySettingsList = dao.findListByQuery("SELECT e FROM GallerySettings e WHERE e.branchId.id =:branchId", params);
        return mapToDTOList(gallerySettingsList, tobyUser);
    }

    @Override
    public List<GallerySettingsDTO> getDeliveryDaysByGalleryId(Integer galleryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("galleryId", galleryId);
        List<GallerySettings> gallerySettingsList = dao.findListByQuery(" SELECT e.deliveryDays FROM GallerySettings e WHERE e.galleryId.id = :galleryId ", params);
        return mapToDTOList(gallerySettingsList, null);
    }

    @Override
    public String getNoticeByCustomerId(Integer id) {
        String gallerySettingsNotice = "";
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT oc.notice FROM offer_company oc\n"
                + "join toby.inv_organization_site os\n"
                + "on os.offer_company_id = oc.id\n").
                append("where os.id =  \n ").
                append(id).append(" ");
        List<String> details = dao.executeNativeQuery(builder.toString());
        for (String customerNotice : details) {
            gallerySettingsNotice = customerNotice;
        }
        return gallerySettingsNotice;
    }

    public GallerySettings mapToEntity(GallerySettingsDTO gallerySettingsDTO, TobyUser tobyUser) {
        GallerySettings gallerySettings1 = new GallerySettings();
        gallerySettings1.setId(gallerySettingsDTO.getId());
        gallerySettings1.setDeliveryDays(gallerySettingsDTO.getDeliveryDays());

        if (gallerySettingsDTO.getGalleryId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(gallerySettingsDTO.getGalleryId().getId());
            gallerySettings1.setGalleryId(invInventory);
        }

        if (gallerySettingsDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(gallerySettingsDTO.getCreatedBy());
            gallerySettings1.setCreatedBy(user);
            gallerySettings1.setCreationDate(gallerySettingsDTO.getCreatedDate());
            gallerySettings1.setModifiedBy(tobyUser);
            gallerySettings1.setModificationDate(new Date());
            if (gallerySettingsDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(gallerySettingsDTO.getCompanyId());
                gallerySettings1.setCompanyId(company);
            }
            if (gallerySettingsDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(gallerySettingsDTO.getBranchId());
                gallerySettings1.setBranchId(branch);
            }
        } else {
            gallerySettings1.setCreatedBy(tobyUser);
            gallerySettings1.setCreationDate(new Date());
            gallerySettings1.setCompanyId(tobyUser.getCompanyId());
            gallerySettings1.setBranchId(tobyUser.getBranchId());

        }
        return gallerySettings1;

    }

    public GallerySettingsDTO mapToDTO(GallerySettings gallerySettings, TobyUser tobyUser) {

        GallerySettingsDTO gallerySettingsDTO = new GallerySettingsDTO();

        gallerySettingsDTO.setId(gallerySettings.getId());
        gallerySettingsDTO.setDeliveryDays(gallerySettings.getDeliveryDays());

        if (gallerySettings.getGalleryId() != null) {
            InvInventoryDTO invInventory = new InvInventoryDTO();
            invInventory.setId(gallerySettings.getGalleryId().getId());
            invInventory.setName(gallerySettings.getGalleryId().getName());
            invInventory.setCode(gallerySettings.getGalleryId().getCode());
            gallerySettingsDTO.setGalleryId(invInventory);
        }
        gallerySettingsDTO.setCreatedBy(gallerySettings.getCreatedBy() != null ? gallerySettings.getCreatedBy().getId() : null);
        gallerySettingsDTO.setCreatedDate(gallerySettings.getCreationDate());
        gallerySettingsDTO.setBranchId(gallerySettings.getBranchId().getId() != null ? gallerySettings.getBranchId().getId() : null);
        gallerySettingsDTO.setCompanyId(gallerySettings.getCompanyId() != null ? gallerySettings.getCompanyId().getId() : null);

        return gallerySettingsDTO;
    }

    public List<GallerySettingsDTO> mapToDTOList(List<GallerySettings> gallerySettingsList, TobyUser tobyUser) {
        List<GallerySettingsDTO> gallerySettingsDTOList = new ArrayList<>();
        for (GallerySettings gallerySettings : gallerySettingsList) {
            gallerySettingsDTOList.add(mapToDTO(gallerySettings, tobyUser));
        }
        return gallerySettingsDTOList;
    }

    @Override
    public void updateGallarySetting(Integer gallaryId) {

        Map<String, Object> params = new HashMap<>();
        params.put("galleryId", gallaryId);
        List<GallerySettings> gallerySettingsList = dao.findListByQuery("SELECT e FROM GallerySettings e WHERE e.galleryId.id =:galleryId", params);
        if (gallerySettingsList != null && !gallerySettingsList.isEmpty()) {
            DateFormat formatDateFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            DateFormat formatDateTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            StringBuilder builder = new StringBuilder();
            builder.append("select (((select sum(s.number) from (SELECT  sum(pfdd.number) as number FROM pro_factory_delivery_detail pfdd  \n"
                    + "left join pro_factory_delivery pfd on pfdd.pro_factory_delivery_id = pfd.id \n"
                    + "left join inv_item ii on ii.id = pfdd.item_Id\n"
                    + "WHERE pfd.type = 0   AND ii.pro_group_id in (1,4)  group by ii.pro_group_id\n"
                    + "union all\n"
                    + "SELECT  sum(pfdd.number) * -1 as number FROM pro_factory_delivery_detail pfdd  \n"
                    + "left join pro_factory_delivery pfd on pfdd.pro_factory_delivery_id = pfd.id \n"
                    + "left join inv_item ii on ii.id = pfdd.item_Id\n"
                    + "WHERE pfd.type = 1   AND ii.pro_group_id in (1,4)  group by ii.pro_group_id\n"
                    + "\n"
                    + "union all\n"
                    + " SELECT  count(s.id)  as number\n"
                    + " FROM inv_purchase_invoice s  WHERE s.branch_id = 182 AND  s.type = 4 AND s.id not in (SELECT p.inv_purchase_invoice_id from pro_factory_delivery p )  \n"
                    + " AND   s.date >= '" + formatDateFrom.format(Utility.sumDateAndNumer(new Date(), -1)) + "' AND   s.date <= '" + formatDateTo.format(Utility.sumDateAndNumer(new Date(), -1)) + "' ) as s) / ( SELECT count(s.id)\n"
                    + " FROM pro_factory_delivery s \n"
                    + " left join inv_purchase_invoice ipi on ipi.id = s.inv_purchase_invoice_id \n"
                    + " WHERE   s.branch_id = 182\n"
                    + " AND s.type = 1 AND s.inv_purchase_invoice_id not in (\n"
                    + " select p.inv_purchase_invoice_id FROM pro_factory_delivery p WHERE   p.branch_id = 182 AND p.type = 2 ) \n"
                    + " AND   s.date >=  '" + formatDateFrom.format(Utility.sumDateAndNumer(new Date(), -1)) + "'  AND   s.date <=   '" + formatDateTo.format(Utility.sumDateAndNumer(new Date(), -1)) + "' )) + 3) as days");
            List<BigDecimal> details = dao.executeNativeQuery(builder.toString());
            int deliveryDays = 20;
            if (details != null && !details.isEmpty()) {
                if (details.get(0) != null) {
                    deliveryDays = details.get(0).intValue();
                    if (gallaryId == 1 || gallaryId == 2) {
                        deliveryDays = deliveryDays + 1;
                    } else if (gallaryId == 3) {
                        deliveryDays = deliveryDays + 2;
                    }
                }
            }

            GallerySettings entity = gallerySettingsList.get(0);
            entity.setDeliveryDays(deliveryDays);

            dao.updateEntity(entity);
        }

    }

}
