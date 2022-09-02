/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.InvDetailsDTOAPI;
import com.toby.API.InvInvoiceDTOApi;
import com.toby.businessservice.InvItemService;
import com.toby.businessservice.InvPurchaseInvoiceDetailsService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.QuantityItemsStoreByDateService;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvItemDTOApi;
import com.toby.entity.InvItem;
import static com.toby.entity.InvUpdate_.inventoryId;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/items")
public class ItemsRest {

    @EJB
    InvItemService invItemService;

    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;

    @EJB
    InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    QuantityItemsStoreByDateService quantityItemsStoreByDateService;

    @Transactional
    @Path("/groupItems")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findInvItemsByBranchAndGroup(InvItemDTO invItemDTO) {
        try {
            if (invItemDTO.getBranchId() != null && invItemDTO.getGroupId() != null) {
                List<InvItemDTO> invItemDTOList = invItemService.getInvItemByBranchIdAndGroupIdDTO(invItemDTO.getBranchId(), invItemDTO.getGroupId().getId());
                GenericEntity<List<InvItemDTO>> list = new GenericEntity<List<InvItemDTO>>(invItemDTOList) {
                };
                return Response.ok(list).build();
            } else {
                invItemDTO.setMsg("يجب اختيار فرع ");
                return Response.ok(invItemDTO).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/customerItems")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findCustomerOldBuy(InvInvoiceDTOApi invoiceDTOApi) {
        try {
            if (invoiceDTOApi.getCustomerMobile() != null) {
                List<InvInvoiceDTOApi> invInvoiceDTOs = invPurchaseInvoiceService_1.getALLInvPurchaseInvoiceDTOBYCustomerForWebSite(4, invoiceDTOApi.getCustomerMobile());
                GenericEntity<List<InvInvoiceDTOApi>> list = new GenericEntity<List<InvInvoiceDTOApi>>(invInvoiceDTOs) {
                };
                return Response.ok(list).build();
            } else {
                invoiceDTOApi.setMsg("يجب اختيار عميل ");
                return Response.ok(invoiceDTOApi).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/customerInvoice")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findInvoiceCustomer(InvInvoiceDTOApi invoiceDTOApi) {
        try {
            if (invoiceDTOApi.getCustomerMobile() != null) {
                List<InvInvoiceDTOApi> invInvoiceDTOs = invPurchaseInvoiceService_1.getALLInvPurchaseInvoiceDTOBYCustomerForWebSite(4, invoiceDTOApi.getCustomerId());
                GenericEntity<List<InvInvoiceDTOApi>> list = new GenericEntity<List<InvInvoiceDTOApi>>(invInvoiceDTOs) {
                };
                return Response.ok(list).build();
            } else {
                invoiceDTOApi.setMsg("يجب اختيار عميل ");
                return Response.ok(invoiceDTOApi).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/invoiceDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findInvoiceDetailsForCustomerOldBuy(InvInvoiceDTO invoiceDTO) {
        try {
            if (invoiceDTO.getId() != null) {
                List<InvDetailsDTOAPI> invInvoiceDTOs = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailListWebSite(invoiceDTO.getId());
                GenericEntity<List<InvDetailsDTOAPI>> list = new GenericEntity<List<InvDetailsDTOAPI>>(invInvoiceDTOs) {
                };
                return Response.ok(list).build();
            } else {
                invoiceDTO.setMsg("يجب اختيار عميل ");
                return Response.ok(invoiceDTO).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/releatedItems")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findReleatedInvItemsByBranchAndGroup(InvItemDTO invItemDTO) {
        try {
            if (invItemDTO.getBranchId() != null && invItemDTO.getGroupId() != null) {
                List<InvItemDTO> invItemDTOList = invItemService.getInvItemByBranchIdAndGroupIdDTO(invItemDTO.getBranchId(), invItemDTO.getGroupId().getId());
                Collections.shuffle(invItemDTOList);
                List<InvItemDTO> randomList;
                if (invItemDTOList.size() >= 5) {
                    randomList = invItemDTOList.subList(0, 4);
                } else {
                    randomList = invItemDTOList.subList(0, invItemDTOList.size() - 1);
                }

                GenericEntity<List<InvItemDTO>> list = new GenericEntity<List<InvItemDTO>>(randomList) {
                };
                return Response.ok(list).build();
            } else {
                invItemDTO.setMsg("يجب اختيار فرع ");
                return Response.ok(invItemDTO).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/findQuantatyItemByinventory")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findQuantatyItemByinventory(InvItemDTOApi invItemDTO) {
        try {
            if (invItemDTO.getId() != null) {
                BigDecimal quantity = quantityItemsStoreByDateService.getQuantatyItemByinventory(invItemDTO.getInventoryId(), invItemDTO.getId(), false, null);
                return Response.ok(quantity).build();
            } else {
                invItemDTO.setMsg("يجب اختيار منتج ");
                return Response.ok(invItemDTO).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/findItem")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findInvItemsById(InvItemDTOApi invItemDTO) {
        try {
            if (invItemDTO.getId() != null) {
                invItemDTO = invItemService.findInvItemDTOForWebSite(invItemDTO.getId());
//                List<InvItemDTO> invItemDTOList = new ArrayList<>();
//                invItemDTOList.add(invItemDTO);
//                GenericEntity<List<InvItemDTO>> list = new GenericEntity<List<InvItemDTO>>(invItemDTOList) {
//                };
                return Response.ok(invItemDTO).build();
            } else {
                invItemDTO.setMsg("يجب اختيار منتج ");
                return Response.ok(invItemDTO).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/updateItem")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateItem(InvItem invItem) {
        try {
            if (invItem.getId() != null) {
                invItem = invItemService.updateInvItemWebSite(invItem);
                return Response.ok().entity("تم الحفظ بنجاح").build();
            } else {
                return Response.ok().entity("يجب اختيار منتج ").build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    public InvItemDTO valitade(InvItemDTO invItemDTO) {
        if (invItemDTO.getBranchId() == null) {
            invItemDTO.setMsg("يجب اختيار فرع");
        }

        return invItemDTO;
    }

}
