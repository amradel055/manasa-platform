/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.invItemPrice;

import com.toby.businessservice.InvItemPriceService;
import com.toby.businessservice.ItemsDataViewService;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvItemPriceDTO;
import com.toby.entity.InvItem;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author hq002
 */
@Named(value = "invItemPriceListMB")
@ViewScoped
public class InvItemPriceListMB extends BaseListBean {

    private List<InvItemPriceDTO> invItemPriceDTOList;
    private InvItemPriceDTO invItemPriceDTOSelected;

    private LazyDataModel<InvItemPriceDTO> dataModel;

    @EJB
    private InvItemPriceService invItemPriceService;
    @EJB
    private ItemsDataViewService itemsDataViewService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "InvNoticeListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            this.setDataModel(new LazyDataModel<InvItemPriceDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<InvItemPriceDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    Integer invGallarySelectionId = getGallaryDTOSelect() != null ? getGallaryDTOSelect().getId() : null;
                    setRowCount(invItemPriceService.getTotalInvItemPriceForGallary(invGallarySelectionId, getUserData().getUserBranch().getId(), filters).intValue());
                    return invItemPriceService.getInvItemPriceDTOView(invGallarySelectionId, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters);
                }
            });
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "load");
        }
    }

    @Override
    public void delete() {
        try {
            try {
                if (invItemPriceDTOSelected != null) {
                    invItemPriceService.deleteInvItemPrice(invItemPriceDTOSelected);
                    invItemPriceDTOList.remove(invItemPriceDTOSelected);
                    savedMeesage(getUserData(), "error");
                }
            } catch (Exception e) {
                errorMessage(getUserData(), "حدث خطأ في التسجيل");
            }
        } catch (Exception e) {
            saveError(e, "InvNoticeListMB", "delete");
        }
    }
    
    public void printBarcode() {

        try {
            if (invItemPriceDTOSelected != null) {
                InvItemDTO item = itemsDataViewService.findInvItem(invItemPriceDTOSelected.getItemId().getId(),getUserData().getUser());
                List<InvDetailDTO> listNew = new ArrayList<>();
                InvDetailDTO invDetailDTO = new InvDetailDTO();
                invDetailDTO.setCost((item.getMaxpricemen().add(item.getMaxpricemen().multiply(new BigDecimal(0.15)))).setScale(3, BigDecimal.ROUND_UP));
                invDetailDTO.setItemCode(item.getCode());
                invDetailDTO.setItemName(item.getName());
                listNew.add(invDetailDTO);
                fillReport(prepareReport(), getUserData().getReportPath() + "itemBarcodeToby.jasper", listNew, "pdf");
            }
        } catch (Exception e) {

        }
    }
    
    
    
    public HashMap prepareReport() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("companyName", getUserData().getCompany().getName());
            return hashMap;
        } catch (Exception e) {
            saveError(e, "invItemDataListMB", "prepareReport");
            return null;
        }
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../invitemprice/itempriceform.xhtml");
        } catch (Exception e) {
            saveError(e, "InvNoticeListMB", "goToAdd");
        }

        return "";
    }

    @Override
    public String goToEdit() {

        if (invItemPriceDTOSelected != null && invItemPriceDTOSelected.getId() > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("invItemPriceDTOSelected", invItemPriceDTOSelected.getId());
            context.getSessionMap().put("ScreenMode", "Edit");
            exit("../invitemprice/itempriceform.xhtml");
            return "";
        } else {
            return "";
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * @return the invItemPriceDTOList
     */
    public List<InvItemPriceDTO> getInvItemPriceDTOList() {
        return invItemPriceDTOList;
    }

    /**
     * @param invItemPriceDTOList the invItemPriceDTOList to set
     */
    public void setInvItemPriceDTOList(List<InvItemPriceDTO> invItemPriceDTOList) {
        this.invItemPriceDTOList = invItemPriceDTOList;
    }

    /**
     * @return the invItemPriceDTOSelected
     */
    public InvItemPriceDTO getInvItemPriceDTOSelected() {
        return invItemPriceDTOSelected;
    }

    /**
     * @param invItemPriceDTOSelected the invItemPriceDTOSelected to set
     */
    public void setInvItemPriceDTOSelected(InvItemPriceDTO invItemPriceDTOSelected) {
        this.invItemPriceDTOSelected = invItemPriceDTOSelected;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<InvItemPriceDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<InvItemPriceDTO> dataModel) {
        this.dataModel = dataModel;
    }
}
