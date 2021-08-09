/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.api.dto.ApiAccountSearchBean;
import com.isag.api.dto.ApiInvOrganizationSiteDTO;
import com.isag.api.dto.ApiSupplierAccountViewBean;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.SupplierAccountService;
import com.isag.businessservice.reports.searchBean.SupplierAccountSearchBean;
import com.isag.config.Secured;
import com.isag.config.UserLoginData;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.views.OrganizationSiteStatementView;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ahmedEssam <ahmedEssam@isag.com>
 */
@Path("/clientacoount")
public class clientAccountRest extends AccountBase {

    private UserLoginData userLoginData;
    private List<InvOrganizationSiteDTO> invOrgSiteList;

    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private SupplierAccountService supplierAccountService;

    @Transactional
    @Secured
    @Path("/customer/{branshSerial}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response clientAccountSearch(@Context ContainerRequestContext crc, ApiAccountSearchBean apiAccountSearchBean) {
        try {
            getUserLoginData(crc);
            resetLists();
            searchBean(apiAccountSearchBean);
            setOrganizationSiteRootList(organizationSiteService.getSupplierByBranchIdForSupplierAccountReportMB(getUserLoginData().getBranchId(), 0));
            search("clientacoount", 0);
            GenericEntity<List<ApiSupplierAccountViewBean>> list = new GenericEntity<List<ApiSupplierAccountViewBean>>(getApiSupplierAccountViewBeanList()) {
            };
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Secured
    @Path("/customerList/{branshSerial}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findCustomerList(@Context ContainerRequestContext crc) {
        try {
            getUserLoginData(crc);
            setInvOrgSiteList(organizationSiteService.getorganizationSiteByBranchIdDTOSmall(getUserLoginData().getBranchId(), true, 0));
            List<ApiInvOrganizationSiteDTO> invOrganizationSiteDTOList = preperInvOrganizationSiteDTO(getInvOrgSiteList());
            GenericEntity<List<ApiInvOrganizationSiteDTO>> list = new GenericEntity<List<ApiInvOrganizationSiteDTO>>(invOrganizationSiteDTOList) {
            };
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private void resetLists() {
        try {
            setOrganizationSiteRootList(new ArrayList<InvOrganizationSite>());
            setSupplierAccountSearchBean(new SupplierAccountSearchBean());
            getSupplierAccountSearchBean().setOrganizationType(0);
            getSupplierAccountSearchBean().setBranchId(getUserLoginData().getBranchId());
            setOrganizationSiteStatementViewList(new ArrayList());
            setOrganizationSiteStatementViewBeforeList(new ArrayList());
        } catch (Exception e) {
        }
    }

    private List<ApiInvOrganizationSiteDTO> preperInvOrganizationSiteDTO(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        List<ApiInvOrganizationSiteDTO> apiInvOrganizationSiteDTOList = new ArrayList<>();
        for (InvOrganizationSiteDTO invOrganizationSiteDTO : invOrganizationSiteDTOList) {
            ApiInvOrganizationSiteDTO apiInvOrganizationSiteDTO = new ApiInvOrganizationSiteDTO();
            apiInvOrganizationSiteDTO.setName(invOrganizationSiteDTO.getName());
            apiInvOrganizationSiteDTO.setCode(invOrganizationSiteDTO.getCode());
            apiInvOrganizationSiteDTOList.add(apiInvOrganizationSiteDTO);
        }
        return apiInvOrganizationSiteDTOList;
    }

    public UserLoginData getUserLoginData(@Context ContainerRequestContext crc) {
        userLoginData = (UserLoginData) crc.getProperty("userLogInData");
        return userLoginData;
    }

    /**
     * @return the userLoginData
     */
    public UserLoginData getUserLoginData() {
        return userLoginData;
    }

    /**
     * @param userLoginData the userLoginData to set
     */
    public void setUserLoginData(UserLoginData userLoginData) {
        this.userLoginData = userLoginData;
    }

    /**
     * @return the invOrgSiteList
     */
    public List<InvOrganizationSiteDTO> getInvOrgSiteList() {
        return invOrgSiteList;
    }

    /**
     * @param invOrgSiteList the invOrgSiteList to set
     */
    public void setInvOrgSiteList(List<InvOrganizationSiteDTO> invOrgSiteList) {
        this.invOrgSiteList = invOrgSiteList;
    }
}
