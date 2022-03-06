/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invNotice;

import com.toby.businessservice.InvNoticeService;
import com.toby.dto.InvNoticeDTO;
import com.toby.entity.InvNotice;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author hq002
 */
@Named(value = "invNoticeListMB")
@ViewScoped
public class InvNoticeListMB extends BaseListBean {

    private InvNoticeDTO invNoticeEntity;
    private List<InvNoticeDTO> invNoticeEntityList;
    private InvNoticeDTO invNoticeEntitySelected;
    private Integer typeFlow;
    private String uri;
    private List<InvNoticeDTO> invNoticeList;

    @EJB
    private InvNoticeService invNoticeService;

    @Override
    @PostConstruct
    public void init() {
        try {

            reset();
//            load();
        } catch (Exception e) {
            saveError(e, "InvNoticeListMB", "init");
        }
    }

    public void reset() {
        try {

            invNoticeEntity = new InvNoticeDTO();
            invNoticeEntityList = new ArrayList<>();
            invNoticeList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "InvNoticeListMB", "reset");
        }
    }

//    @Override
//    public void load() {
//        try {
//            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//            userData = (UserData) context.getSessionMap().get("userLogInData");
//            Integer type;
//            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
//
//            if (uri.contains("supplieraddnotice") || uri.contains("supplierdiscountnotice")) {
////            typeFlow = 2,3;
//                typeFlow = 2;
//            } else if (uri.contains("customeraddnotice") || uri.contains("customerdiscountnotice")) {
////            typeFlow = 0,1;
//                typeFlow = 1;
//            }
//            invNoticeList = invNoticeService.getALLInvNoticeByBranchIdAndType(getUserData().getUserBranch().getId(), typeFlow);
//            for (InvNotice notice : invNoticeList) {
//                invNoticeEntity = new InvNoticeEntity();
//                invNoticeEntity.setId(notice.getId());
//                invNoticeEntity.setSerial(notice.getSerial());
//                invNoticeEntity.setAccountId(notice.getContrastAccountId());
//                invNoticeEntity.setOrganizationSiteId(notice.getOrganizationSiteId());
//                invNoticeEntity.setAmount(notice.getValue() != null ? notice.getValue() : BigDecimal.ZERO);
//                invNoticeEntity.setRemarks(notice.getRemark());
//                invNoticeEntity.setDate(notice.getDate());
//                invNoticeEntity.setMarkEdit(notice.getMarkEdit());
//
//                if (notice.getType() != null) {
//                    if (notice.getType() == 0 || notice.getType() == 2) {
//                        invNoticeEntity.setTypeName("اشعار اضافة");
//                    } else if (notice.getType() == 1 || notice.getType() == 3) {
//                        invNoticeEntity.setTypeName("اشعار خصم");
//                    }
//
//                    invNoticeEntity.setType(notice.getType());
//                }
//                invNoticeEntityList.add(invNoticeEntity);
//            }
//        } catch (Exception e) {
//            saveError(e, "InvNoticeListMB", "load");
//        }
//    }
    public void search() {
        if (getUri().contains("supplieraddnotice") || getUri().contains("supplierdiscountnotice")) {
//            typeFlow = 2,3;
            typeFlow = 2;
        } else if (getUri().contains("customeraddnotice") || getUri().contains("customerdiscountnotice")) {
//            typeFlow = 0,1,4;
            typeFlow = 1;
        }
        setInvNoticeEntityList(invNoticeService.getALLInvNoticeByBranchIdAndTypeAndSerialAndOrgan_idDTO(getUserData().getUserBranch().getId(), typeFlow, getInvNoticeEntity().getOrganizationSiteId(), getInvNoticeEntity().getSerial()));
    }

    @Override
    public void load() {
        try {
            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
            if (uri.contains("supplieraddnotice") || uri.contains("supplierdiscountnotice")) {
                typeFlow = 2;
            } else if (uri.contains("customeraddnotice") || uri.contains("customerdiscountnotice")) {
                typeFlow = 1;
            }
//            invNoticeEntityList = invNoticeService.getALLInvNoticeByBranchIdAndTypeDTO(getUserData().getUserBranch().getId(), typeFlow);
        } catch (Exception e) {
            saveError(e, "InvNoticeListMB", "load");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();

            Integer invNoticeId = invNoticeEntitySelected.getId() != null ? invNoticeEntitySelected.getId() : null;
            InvNotice invNotice2 = new InvNotice();
            invNotice2.setId(invNoticeId);

            try {
                invNoticeService.deleteInvNotice(invNotice2);
                invNoticeEntityList.remove(invNoticeEntitySelected);
                savedMeesage(getUserData(), userDDs.get("DELETED"));
            } catch (Exception e) {
                errorMessage(getUserData(), "حدث خطأ في التسجيل");
            }
        } catch (Exception e) {
            saveError(e, "InvNoticeListMB", "delete");
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
            if (uri.contains("supplieraddnotice")) {
                exit("../supplieraddnotice/suppliernoticeform.xhtml");
            } else if (uri.contains("supplierdiscountnotice")) {
                exit("../supplierdiscountnotice/supplierdiscountnoticeform.xhtml");
            } else if (uri.contains("customeraddnotice")) {
                exit("../customeraddnotice/customernoticeform.xhtml");
            } else if (uri.contains("customerdiscountnotice")) {
                exit("../customerdiscountnotice/customerdiscountnoticeform.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "InvNoticeListMB", "goToAdd");
        }

        return "";
    }

    @Override
    public String goToEdit() {

        if (invNoticeEntitySelected != null && invNoticeEntitySelected.getId() > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("invNoticeEntitySelected", invNoticeEntitySelected.getId());
            context.getSessionMap().put("ScreenMode", "Edit");
            if (uri.contains("supplieraddnotice")) {
                exit("../supplieraddnotice/suppliernoticeform.xhtml");
            } else if (uri.contains("supplierdiscountnotice")) {
                exit("../supplierdiscountnotice/supplierdiscountnoticeform.xhtml");
            } else if (uri.contains("customeraddnotice")) {
                exit("../customeraddnotice/customernoticeform.xhtml");
            } else if (uri.contains("customerdiscountnotice")) {
                exit("../customerdiscountnotice/customerdiscountnoticeform.xhtml");
            }

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
     * @return the invNoticeEntity
     */
    public InvNoticeDTO getInvNoticeEntity() {
        return invNoticeEntity;
    }

    /**
     * @param invNoticeEntity the invNoticeEntity to set
     */
    public void setInvNoticeEntity(InvNoticeDTO invNoticeEntity) {
        this.invNoticeEntity = invNoticeEntity;
    }

    /**
     * @return the invNoticeEntityList
     */
    public List<InvNoticeDTO> getInvNoticeEntityList() {
        return invNoticeEntityList;
    }

    /**
     * @param invNoticeEntityList the invNoticeEntityList to set
     */
    public void setInvNoticeEntityList(List<InvNoticeDTO> invNoticeEntityList) {
        this.invNoticeEntityList = invNoticeEntityList;
    }

    /**
     * @return the invNoticeList
     */
    public List<InvNoticeDTO> getInvNoticeList() {
        return invNoticeList;
    }

    /**
     * @param invNoticeList the invNoticeList to set
     */
    public void setInvNoticeList(List<InvNoticeDTO> invNoticeList) {
        this.invNoticeList = invNoticeList;
    }

    /**
     * @return the invNoticeEntitySelected
     */
    public InvNoticeDTO getInvNoticeEntitySelected() {
        return invNoticeEntitySelected;
    }

    /**
     * @param invNoticeEntitySelected the invNoticeEntitySelected to set
     */
    public void setInvNoticeEntitySelected(InvNoticeDTO invNoticeEntitySelected) {
        this.invNoticeEntitySelected = invNoticeEntitySelected;
    }

    /**
     * @return the typeFlow
     */
    public Integer getTypeFlow() {
        return typeFlow;
    }

    /**
     * @param typeFlow the typeFlow to set
     */
    public void setTypeFlow(Integer typeFlow) {
        this.typeFlow = typeFlow;
    }
}
