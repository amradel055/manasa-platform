/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.api.dto.ApiInvInvoiceDTO;
import com.isag.api.dto.ApiNetView;
import com.isag.api.dto.DistributionApiData;
import com.isag.api.dto.DistributionApiSearch;
import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.InvPurchaseInvoiceService;
import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.SalesJournalService;
import com.isag.businessservice.reports.searchBean.SalesJournalSearchBean;
import com.isag.config.Secured;
import com.isag.config.UserLoginData;
import com.isag.dto.InventoryDelegatorDTO;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InventoryDelegator;
import com.isag.views.NetView;
import java.util.ArrayList;
import java.util.Date;
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
 * @author AHMEDESSAM
 */
@Path("/PurchaseJournal")
public class InvPurchaseInvoiceRest {

//    private List<InventoryDelegator> invDelegatorList;
//    private List<InvPurchaseInvoice> invInvoicesSalesList;
//    private List<InvPurchaseInvoice> invInvoicesPurchaseList;
//    private SalesJournalSearchBean salesJournalSearchBean;
//    private List<NetView> netViewList;
//    private UserLoginData userLoginData;
//
//    @EJB
//    private OrganizationSiteService organizationSiteService;
//    @EJB
//    private InvDelegatorService delegatorService;
//    @EJB
//    private InvPurchaseInvoiceService invPurchaseInvoiceService;
//    @EJB
//    private SalesJournalService salesJournalService;
//    @EJB
//    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
//
//    @Transactional
//    @Secured
//    @Path("/SalesJournal/{branshSerial}")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response salesJournalSearch(@Context ContainerRequestContext crc, ApiInvInvoiceDTO apiInvInvoiceDTO) {
//        try {
//            if (apiInvInvoiceDTO == null) {
//                apiInvInvoiceDTO = new ApiInvInvoiceDTO();
//            }
//            apiInvInvoiceDTO.setType(1);//sales invoice
//            return search(crc, apiInvInvoiceDTO);
//        } catch (Exception e) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//    }
//
//    @Transactional
//    @Secured
//    @Path("/PurchaseJournal/{branshSerial}")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response purchaseJournalSearch(@Context ContainerRequestContext crc, ApiInvInvoiceDTO apiInvInvoiceDTO) {
//        try {
//            if (apiInvInvoiceDTO == null) {
//                apiInvInvoiceDTO = new ApiInvInvoiceDTO();
//            }
//            apiInvInvoiceDTO.setType(0);//purchase invoiceList 
//            return search(crc, apiInvInvoiceDTO);
//        } catch (Exception e) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//    }
//
//    private Response search(@Context ContainerRequestContext crc, ApiInvInvoiceDTO apiInvInvoiceDTO) {
//        try {
//            getUserLoginData(crc);
//            resetLists();
//
//            getSalesJournalSearchBean().setBranchId(getUserLoginData().getBranchId());
//
////        if (uri != null && uri.contains("SalesJournalForUser")) {
////            getSalesJournalSearchBean().setType(1);
////            getSalesJournalSearchBean().setIsagUserId(getUserData().getUser().getId());
////            getSalesJournalSearchBean().setFrompaymentType(3);
////            getSalesJournalSearchBean().setTopaymentType(3);
////        }
//            if (apiInvInvoiceDTO != null) {
//                getSalesJournalSearchBean().setType(apiInvInvoiceDTO.getType());
//                if (apiInvInvoiceDTO.getFromDate() != null) {
//                    getSalesJournalSearchBean().setDateFrom(new Date(apiInvInvoiceDTO.getFromDate()));
//                }
//                if (apiInvInvoiceDTO.getToDate() != null) {
//                    getSalesJournalSearchBean().setDateTo(new Date(apiInvInvoiceDTO.getToDate()));
//                }
//
//                if (apiInvInvoiceDTO.getFromDelegator() != null) {
//                    getSalesJournalSearchBean().setFromDelegator(delegatorService.getDelegatorDTOByBranchAndSerial(getUserLoginData().getBranchId(), apiInvInvoiceDTO.getType(), apiInvInvoiceDTO.getFromDelegator()));
//                }
//                if (apiInvInvoiceDTO.getToDelegator() != null) {
//                    getSalesJournalSearchBean().setToDelegator(delegatorService.getDelegatorDTOByBranchAndSerial(getUserLoginData().getBranchId(), apiInvInvoiceDTO.getType(), apiInvInvoiceDTO.getFromDelegator()));
//                }
//                if (apiInvInvoiceDTO.getFromSerial() != null) {
//                    getSalesJournalSearchBean().setFromserial(invPurchaseInvoiceService.getALLInvPurchaseInvoiceByBranchIdAndTypeAndSerial(getUserLoginData().getBranchId(), apiInvInvoiceDTO.getType(), apiInvInvoiceDTO.getFromSerial()));
//                }
//                if (apiInvInvoiceDTO.getToSerial() != null) {
//                    getSalesJournalSearchBean().setToserial(invPurchaseInvoiceService.getALLInvPurchaseInvoiceByBranchIdAndTypeAndSerial(getUserLoginData().getBranchId(), apiInvInvoiceDTO.getType(), apiInvInvoiceDTO.getToSerial()));
//                }
//            }
//
//            setNetViewList(salesJournalService.getAllPurchaseInvoiceSearchBean(getSalesJournalSearchBean()));
//            GenericEntity<List<ApiNetView>> list = new GenericEntity<List<ApiNetView>>(preperNetView(getNetViewList())) {
//            };
//            return Response.ok(list).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//    }
//
//    @Transactional
//    @Secured
//    @Path("/purchaseDelegator/{branshSerial}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response findInvPurchaseList(@Context ContainerRequestContext crc) {
//        try {
//            getUserLoginData(crc);
//            invDelegatorList = delegatorService.getPurchaseByBranch(getUserLoginData().getBranchId()); //مشتريات
//            List<InventoryDelegatorDTO> inventoryDelegatorDTOList = preperInventoryDelegator(invDelegatorList);
//            GenericEntity<List<InventoryDelegatorDTO>> list = new GenericEntity<List<InventoryDelegatorDTO>>(inventoryDelegatorDTOList) {
//            };
//            return Response.ok(list).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//    }
//
//    @Transactional
//    @Secured
//    @Path("/salesDelegator/{branshSerial}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response findInvDelegatorList(@Context ContainerRequestContext crc) {
//        try {
//            getUserLoginData(crc);
//            invDelegatorList = delegatorService.getSalesByBranch(getUserLoginData().getBranchId()); // مبيعات
//            List<InventoryDelegatorDTO> inventoryDelegatorDTOList = preperInventoryDelegator(invDelegatorList);
//            GenericEntity<List<InventoryDelegatorDTO>> list = new GenericEntity<List<InventoryDelegatorDTO>>(inventoryDelegatorDTOList) {
//            };
//            return Response.ok(list).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//    }
//
//    @Transactional
//    @Secured
//    @Path("/salesInvoiceList/{branshSerial}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response findInvSalesInvoiceList(@Context ContainerRequestContext crc) {
//        try {
//            getUserLoginData(crc);
//            invPurchaseInvoiceList(1);//sales invoice
//            GenericEntity<List<ApiInvInvoiceDTO>> list = new GenericEntity<List<ApiInvInvoiceDTO>>(invInvoicSerial(getInvInvoicesSalesList())) {
//            };
//            return Response.ok(list).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//    }
//
//    @Transactional
//    @Secured
//    @Path("/purchaseInvoiceList/{branshSerial}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response findInvPurchaseInvoiceList(@Context ContainerRequestContext crc) {
//        try {
//            getUserLoginData(crc);
//            invPurchaseInvoiceList(0);//purchase invoiceList 
//            GenericEntity<List<ApiInvInvoiceDTO>> list = new GenericEntity<List<ApiInvInvoiceDTO>>(invInvoicSerial(getInvInvoicesSalesList())) {
//            };
//            return Response.ok(list).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//    }
//    
//    
//    
//    
//    /////////////////////////  IGMA  ////////////////////////////////
//    @Transactional
//    @Secured
//    @Path("/SalesJournal/{branshSerial}")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response findDataInventory(@Context ContainerRequestContext crc, DistributionApiSearch distributionApiSearch) {
//        try {
//            DistributionApiData distributionApiData = invPurchaseInvoiceService_1.distributionApiData(distributionApiSearch);
//            List<DistributionApiData> distributionApiDataList = new ArrayList<>();
//            distributionApiDataList.add(distributionApiData);
//            GenericEntity<List<DistributionApiData>> list = new GenericEntity<List<DistributionApiData>>(distributionApiDataList) {
//            };
//            return Response.ok(list).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//    }
//    
//    
//    
//
//    private List<ApiNetView> preperNetView(List<NetView> netViewList) {
//        List<ApiNetView> apiNetViewList = new ArrayList<>();
//        for (NetView netView : netViewList) {
//            ApiNetView apiNetView = new ApiNetView();
//            apiNetView.setSerial(netView.getSerial());
//            apiNetView.setDate(netView.getDate());
//            apiNetView.setInvDelegatorName(netView.getInvDelegatorName());
//            apiNetView.setInventoryName(netView.getInventoryName());
//            apiNetView.setOrganizationSiteName(netView.getOrganizationSiteName());
//            apiNetView.setPaymentTypeText(netView.getPaymentTypeText());
//            apiNetView.setRemarks(netView.getRemarks());
//            apiNetView.setSumAfterDiscount(netView.getSumAfterDiscount());
//            apiNetView.setNetBill(netView.getNet());
//            apiNetViewList.add(apiNetView);
//        }
//        return apiNetViewList;
//    }
//
//    private List<InventoryDelegatorDTO> preperInventoryDelegator(List<InventoryDelegator> inventoryDelegatorList) {
//        List<InventoryDelegatorDTO> inventoryDelegatorDTOList = new ArrayList<>();
//        for (InventoryDelegator delegator : inventoryDelegatorList) {
//            InventoryDelegatorDTO delegatorDTO = new InventoryDelegatorDTO();
//            delegatorDTO.setName(delegator.getName());
//            delegatorDTO.setCode(delegator.getCode());
//            inventoryDelegatorDTOList.add(delegatorDTO);
//        }
//        return inventoryDelegatorDTOList;
//    }
//
//    private List<ApiInvInvoiceDTO> invInvoicSerial(List<InvPurchaseInvoice> invPurchaseInvoiceList) {
//        List<ApiInvInvoiceDTO> apiInvInvoiceDTOList = new ArrayList<>();
//        for (InvPurchaseInvoice invPurchaseInvoice : invPurchaseInvoiceList) {
//            ApiInvInvoiceDTO apiInvInvoiceDTO = new ApiInvInvoiceDTO();
//            apiInvInvoiceDTO.setSerial(invPurchaseInvoice.getSerial());
//            apiInvInvoiceDTOList.add(apiInvInvoiceDTO);
//        }
//        return apiInvInvoiceDTOList;
//    }
//
//    private void invPurchaseInvoiceList(Integer type) {
//        if (type == null) {
//            return;
//        }
//        setInvInvoicesSalesList(invPurchaseInvoiceService.getALLInvPurchaseInvoicePostFlagedByBranchIdPer(getUserLoginData().getBranchId(), type));
//    }
//
//    public void resetLists() {
//        try {
//            setNetViewList(new ArrayList<NetView>());
//            setSalesJournalSearchBean(new SalesJournalSearchBean());
//        } catch (Exception e) {
//        }
//    }
//
//    /**
//     * @return the invDelegatorList
//     */
//    public List<InventoryDelegator> getInvDelegatorList() {
//        return invDelegatorList;
//    }
//
//    /**
//     * @param invDelegatorList the invDelegatorList to set
//     */
//    public void setInvDelegatorList(List<InventoryDelegator> invDelegatorList) {
//        this.invDelegatorList = invDelegatorList;
//    }
//
//    /**
//     * @return the invInvoicesSalesList
//     */
//    public List<InvPurchaseInvoice> getInvInvoicesSalesList() {
//        return invInvoicesSalesList;
//    }
//
//    /**
//     * @param invInvoicesSalesList the invInvoicesSalesList to set
//     */
//    public void setInvInvoicesSalesList(List<InvPurchaseInvoice> invInvoicesSalesList) {
//        this.invInvoicesSalesList = invInvoicesSalesList;
//    }
//
//    /**
//     * @return the invInvoicesPurchaseList
//     */
//    public List<InvPurchaseInvoice> getInvInvoicesPurchaseList() {
//        return invInvoicesPurchaseList;
//    }
//
//    /**
//     * @param invInvoicesPurchaseList the invInvoicesPurchaseList to set
//     */
//    public void setInvInvoicesPurchaseList(List<InvPurchaseInvoice> invInvoicesPurchaseList) {
//        this.invInvoicesPurchaseList = invInvoicesPurchaseList;
//    }
//
//    /**
//     * @return the salesJournalSearchBean
//     */
//    public SalesJournalSearchBean getSalesJournalSearchBean() {
//        return salesJournalSearchBean;
//    }
//
//    /**
//     * @param salesJournalSearchBean the salesJournalSearchBean to set
//     */
//    public void setSalesJournalSearchBean(SalesJournalSearchBean salesJournalSearchBean) {
//        this.salesJournalSearchBean = salesJournalSearchBean;
//    }
//
//    /**
//     * @return the netViewList
//     */
//    public List<NetView> getNetViewList() {
//        return netViewList;
//    }
//
//    /**
//     * @param netViewList the netViewList to set
//     */
//    public void setNetViewList(List<NetView> netViewList) {
//        this.netViewList = netViewList;
//    }
//
//    public UserLoginData getUserLoginData(@Context ContainerRequestContext crc) {
//        userLoginData = (UserLoginData) crc.getProperty("userLogInData");
//        return userLoginData;
//    }
//
//    public UserLoginData getUserLoginData() {
//        return userLoginData;
//    }
//
//    public void setUserLoginData(UserLoginData userLoginData) {
//        this.userLoginData = userLoginData;
//    }
}
