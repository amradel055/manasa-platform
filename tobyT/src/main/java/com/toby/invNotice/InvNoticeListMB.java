/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invNotice;

import com.toby.businessservice.InvNoticeService;
import com.toby.entity.InvNotice;
import com.toby.entiy.InvNoticeEntity;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.math.BigDecimal;
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

    private InvNoticeEntity invNoticeEntity;
    private InvNotice invNotice;
    private List<InvNoticeEntity> invNoticeEntityList;
    private InvNoticeEntity invNoticeEntitySelected;
    private Integer typeFlow;
    private String uri;
    private List<InvNotice> invNoticeList;
    private UserData userData;

    @EJB
    private InvNoticeService invNoticeService;

    @Override
    @PostConstruct
    public void init() {
        try {

           invNoticeEntityList =new ArrayList<>();
            load();
        } catch (Exception e) {
            saveError(e, "InvNoticeListMB", "init");
        }
    }

    public void reset() {
        try {

         
        } catch (Exception e) {
            saveError(e, "InvNoticeListMB", "reset");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            Integer type;
            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

            if (uri.contains("supplieraddnotice") || uri.contains("supplierdiscountnotice")) {
//            typeFlow = 2,3;
                typeFlow = 2;
            } else if (uri.contains("customeraddnotice") || uri.contains("customerdiscountnotice")) {
//            typeFlow = 0,1;
                typeFlow = 1;
            }
            invNoticeList = invNoticeService.getALLInvNoticeByBranchIdAndType(getUserData().getUserBranch().getId(), typeFlow);
            for (InvNotice notice : invNoticeList) {
                invNoticeEntity = new InvNoticeEntity();
                invNoticeEntity.setId(notice.getId());
                invNoticeEntity.setSerial(notice.getSerial());
                invNoticeEntity.setAccountId(notice.getContrastAccountId());
                invNoticeEntity.setOrganizationSiteId(notice.getOrganizationSiteId());
                invNoticeEntity.setAmount(notice.getValue() != null ? notice.getValue() : BigDecimal.ZERO);
                invNoticeEntity.setRemarks(notice.getRemark());
                invNoticeEntity.setDate(notice.getDate());
                invNoticeEntity.setMarkEdit(notice.getMarkEdit());

                if (notice.getType() != null) {
                    if (notice.getType() == 0 || notice.getType() == 2) {
                        invNoticeEntity.setTypeName("اشعار اضافة");
                    } else if (notice.getType() == 1 || notice.getType() == 3) {
                        invNoticeEntity.setTypeName("اشعار خصم");
                    }

                    invNoticeEntity.setType(notice.getType());
                }
                invNoticeEntityList.add(invNoticeEntity);
            }
        } catch (Exception e) {
            saveError(e, "InvNoticeListMB", "load");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();

            Integer invNoticeId = invNoticeEntitySelected.getId() != null ? invNoticeEntitySelected.getId() : null;
            invNotice = new InvNotice();
            invNotice.setId(invNoticeId);

            try {
                invNoticeService.deleteInvNotice(invNotice);
                invNoticeEntityList.remove(invNoticeEntitySelected);
                savedMeesage(userData, userDDs.get("DELETED"));
            } catch (Exception e) {
                errorMessage(userData, "حدث خطأ في التسجيل");
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
    public InvNoticeEntity getInvNoticeEntity() {
        return invNoticeEntity;
    }

    /**
     * @param invNoticeEntity the invNoticeEntity to set
     */
    public void setInvNoticeEntity(InvNoticeEntity invNoticeEntity) {
        this.invNoticeEntity = invNoticeEntity;
    }

    /**
     * @return the invNotice
     */
    public InvNotice getInvNotice() {
        return invNotice;
    }

    /**
     * @param invNotice the invNotice to set
     */
    public void setInvNotice(InvNotice invNotice) {
        this.invNotice = invNotice;
    }

    /**
     * @return the invNoticeEntityList
     */
    public List<InvNoticeEntity> getInvNoticeEntityList() {
        return invNoticeEntityList;
    }

    /**
     * @param invNoticeEntityList the invNoticeEntityList to set
     */
    public void setInvNoticeEntityList(List<InvNoticeEntity> invNoticeEntityList) {
        this.invNoticeEntityList = invNoticeEntityList;
    }

    /**
     * @return the invNoticeList
     */
    public List<InvNotice> getInvNoticeList() {
        return invNoticeList;
    }

    /**
     * @param invNoticeList the invNoticeList to set
     */
    public void setInvNoticeList(List<InvNotice> invNoticeList) {
        this.invNoticeList = invNoticeList;
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the invNoticeEntitySelected
     */
    public InvNoticeEntity getInvNoticeEntitySelected() {
        return invNoticeEntitySelected;
    }

    /**
     * @param invNoticeEntitySelected the invNoticeEntitySelected to set
     */
    public void setInvNoticeEntitySelected(InvNoticeEntity invNoticeEntitySelected) {
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

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }
}
