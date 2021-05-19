package com.toby.proItemProductionSatges;

import com.toby.businessservice.InvItemService;
import com.toby.businessservice.ProGroupService;
import com.toby.businessservice.ProItemProductionStagesService;
import com.toby.businessservice.ProProductionStagesService;
import com.toby.converter.ProItemProductionStageDTOConverter;
import com.toby.converter.ProProductionStagesDTOConverter;
import com.toby.dto.InvItemDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.dto.ProItemProductionStagesDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;

@Named(value = "proItemProductionStagesMB")
@ViewScoped
public class ProItemProductionStagesMB extends BaseListBean {

    private Integer index = 999999;
    private InvItemDTO invItemDTOSelected;
    private List<ProProductionStagesDTO> proProductionStagesDTOList;
    private ProItemProductionStagesDTO proItemProductionStagesDTOSelected;
    private List<InvItemDTO> invItemDTOs;
    private List<ProGroupDTO> proGroupDTOList;
    private ProGroupDTO proGroupDTOSelected;
    private List<ProItemProductionStagesDTO> proItemProductionStagesDTOs;
    private List<ProItemProductionStagesDTO> proItemProductionStagesDTOs1;

    private ProProductionStagesDTOConverter productionStagesDTOConverter;
    private ProItemProductionStageDTOConverter proItemProductionStageDTOConverter;

    @EJB
    private ProProductionStagesService proProductionStagesService;
    @EJB
    private ProItemProductionStagesService proItemProductionStagesService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private ProGroupService proGroupService;

    public ProItemProductionStagesMB() {
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        getProProductionStagesDTOList();
    }

    public void save() {
        if (validateDetailsWhenAddRow()) {
            fillItemFromBak();
            for (ProItemProductionStagesDTO stagesDTO : proItemProductionStagesDTOs) {
//                stagesDTO.setInvItemId(invItemDTOSelected);
                stagesDTO.setProGroupId(getProGroupDTOSelected());
                proItemProductionStagesService.save(stagesDTO, getUserData().getUser());
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "good ", "تم الحفظ بنجاح "));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "error ", "يوجد صف فارغ "));
        }
    }

    public void remove() {
        if (proItemProductionStagesDTOSelected != null && proItemProductionStagesDTOSelected.getId() != null) {
            proItemProductionStagesService.deleteByStage(proItemProductionStagesDTOSelected);
            getProItemProductionStagesDTOs().remove(proItemProductionStagesDTOSelected);
        } else {
            getProItemProductionStagesDTOs().remove(proItemProductionStagesDTOSelected);
        }
    }

    public boolean validateDetailsWhenAddRow() {
        for (ProItemProductionStagesDTO itemProductionStagesDTO : proItemProductionStagesDTOs) {
            if (itemProductionStagesDTO.getProProductionStagesId() == null) {
                return false;
            }
        }
        return true;
    }

    public void addRow() {
        if (validateDetailsWhenAddRow()) {
            ProItemProductionStagesDTO detaildTO = new ProItemProductionStagesDTO();
            detaildTO.setInvItemId(invItemDTOSelected);
            detaildTO.setIndex(getIndex());
            getProItemProductionStagesDTOs().add(detaildTO);
            proItemProductionStagesDTOSelected = detaildTO;
            fillItemFromBak();
        }
    }

    private void fillItemFromBak() {
        for (ProItemProductionStagesDTO itemProductionStagesDTO : getProItemProductionStagesDTOs()) {
            itemProductionStagesDTO.setProProductionStagesId(itemProductionStagesDTO.getProProductionStagesIdBak());
        }
    }

    public void validateProItemProductionStagesDTO() {
        if (proItemProductionStagesDTOSelected != null) {
            for (ProItemProductionStagesDTO itemProductionStagesDTO : getProItemProductionStagesDTOs()) {
                if (itemProductionStagesDTO.getProProductionStagesId() == null && itemProductionStagesDTO.getProProductionStagesIdBak() != null) {
                    itemProductionStagesDTO.setProProductionStagesId(itemProductionStagesDTO.getProProductionStagesIdBak());
                }
                if (itemProductionStagesDTO.getProProductionStagesId() == null && !itemProductionStagesDTO.getIndex().equals(proItemProductionStagesDTOSelected.getIndex())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "error ", "يوجد مرحلة لم يتم اختيارها يجب ملئ جميع المراحل"));
                    return;
                }
                if (itemProductionStagesDTO.getProProductionStagesId() != null && itemProductionStagesDTO.getProProductionStagesId().getId().equals(proItemProductionStagesDTOSelected.getProProductionStagesId().getId())
                        && !itemProductionStagesDTO.getIndex().equals(proItemProductionStagesDTOSelected.getIndex())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "error ", "لقد تم اختيار هذه المرحلة من قبل"));
                    proItemProductionStagesDTOSelected.setProProductionStagesId(null);
                    break;
                } else {
                    proItemProductionStagesDTOSelected.setProProductionStagesIdBak(proItemProductionStagesDTOSelected.getProProductionStagesId());
                }
            }
        }

    }

    public void selectUserRole(SelectEvent event) {
        try {
            proGroupDTOSelected = (ProGroupDTO) event.getObject();
            if (getProGroupDTOSelected() != null) {
                proItemProductionStagesDTOs = proItemProductionStagesService.getAll(getUserData().getUser(), getProGroupDTOSelected().getId());
            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "selectUserRole");
        }
    }

    public List<ProProductionStagesDTO> complete(String query) {
        try {
            List<ProProductionStagesDTO> supplierList = proProductionStagesDTOList;
            if (query == null || query.trim().equals("")) {

                productionStagesDTOConverter = new ProProductionStagesDTOConverter(proProductionStagesDTOList);
                return supplierList;
            }
            List<ProProductionStagesDTO> filteredSuppliers = new ArrayList<>();

            String nameAr;
            String code;
            ProProductionStagesDTO supplierFilter;
            for (int i = 0; i < proProductionStagesDTOList.size(); i++) {
                supplierFilter = supplierList.get(i);
                nameAr = Integer.toString(supplierFilter.getId());
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }

                code = Integer.toString(supplierFilter.getId());
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }
            }

            productionStagesDTOConverter = new ProProductionStagesDTOConverter(filteredSuppliers);
            return filteredSuppliers;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "completeSuppllier");
            return null;
        }
    }

    public void cancel() {
    }

    @Override
    public void delete() {
    }

    @Override
    public void filter() {
    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<ProProductionStagesDTO> getProProductionStagesDTOList() {
        if (proProductionStagesDTOList == null || proProductionStagesDTOList.isEmpty()) {
            proProductionStagesDTOList = proProductionStagesService.getAllProproductionStages(getUserData().getUser(),getUserData().getUserBranch().getId());
            setProductionStagesDTOConverter(new ProProductionStagesDTOConverter(proProductionStagesDTOList));
        }
        return proProductionStagesDTOList;
    }

    public void setProProductionStagesDTOList(List<ProProductionStagesDTO> proProductionStagesDTOList) {
        this.proProductionStagesDTOList = proProductionStagesDTOList;
    }

    public List<InvItemDTO> getInvItemDTOs() {
        if (invItemDTOs == null || invItemDTOs.isEmpty()) {
            invItemDTOs = invItemService.findInvItemDTOList(getUserData().getUser());
        }

        return invItemDTOs;
    }

    public void setInvItemDTOs(List<InvItemDTO> invItemDTOs) {
        this.invItemDTOs = invItemDTOs;
    }

    public InvItemDTO getInvItemDTOSelected() {
        if (invItemDTOSelected == null) {
            invItemDTOSelected = new InvItemDTO();
        }
        return invItemDTOSelected;
    }

    public void setInvItemDTOSelected(InvItemDTO invItemDTOSelected) {
        this.invItemDTOSelected = invItemDTOSelected;
    }

    public List<ProItemProductionStagesDTO> getProItemProductionStagesDTOs() {
        if ((proItemProductionStagesDTOs == null || proItemProductionStagesDTOs.isEmpty()) && getProGroupDTOSelected() != null) {
            proItemProductionStagesDTOs = proItemProductionStagesService.getAll(getUserData().getUser(), getProGroupDTOSelected().getId());
            setProItemProductionStageDTOConverter(new ProItemProductionStageDTOConverter(proItemProductionStagesDTOs));
        }
        return proItemProductionStagesDTOs;
    }

    public void setProItemProductionStagesDTOs(List<ProItemProductionStagesDTO> proItemProductionStagesDTOs) {
        this.proItemProductionStagesDTOs = proItemProductionStagesDTOs;
    }

    public ProProductionStagesDTOConverter getProductionStagesDTOConverter() {
        return productionStagesDTOConverter;
    }

    public void setProductionStagesDTOConverter(ProProductionStagesDTOConverter productionStagesDTOConverter) {
        this.productionStagesDTOConverter = productionStagesDTOConverter;
    }

    public List<ProItemProductionStagesDTO> getProItemProductionStagesDTOs1() {
        return proItemProductionStagesDTOs1;
    }

    public void setProItemProductionStagesDTOs1(List<ProItemProductionStagesDTO> proItemProductionStagesDTOs1) {
        this.proItemProductionStagesDTOs1 = proItemProductionStagesDTOs1;
    }

    public ProItemProductionStagesDTO getProItemProductionStagesDTOSelected() {
        return proItemProductionStagesDTOSelected;
    }

    public void setProItemProductionStagesDTOSelected(ProItemProductionStagesDTO proItemProductionStagesDTOSelected) {
        this.proItemProductionStagesDTOSelected = proItemProductionStagesDTOSelected;
    }

    public ProItemProductionStageDTOConverter getProItemProductionStageDTOConverter() {
        return proItemProductionStageDTOConverter;
    }

    public void setProItemProductionStageDTOConverter(ProItemProductionStageDTOConverter proItemProductionStageDTOConverter) {
        this.proItemProductionStageDTOConverter = proItemProductionStageDTOConverter;
    }   

    /**
     * @return the index
     */
    public Integer getIndex() {
        index = index + 1;
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the proGroupDTOList
     */
    public List<ProGroupDTO> getProGroupDTOList() {
        if(proGroupDTOList == null || proGroupDTOList.isEmpty()){
            proGroupDTOList = proGroupService.getallProGroupByBranchIdDTO(getUserData().getUserlogin());
        }
        return proGroupDTOList;
    }

    /**
     * @param proGroupDTOList the proGroupDTOList to set
     */
    public void setProGroupDTOList(List<ProGroupDTO> proGroupDTOList) {
        this.proGroupDTOList = proGroupDTOList;
    }

    /**
     * @return the proGroupDTOSelected
     */
    public ProGroupDTO getProGroupDTOSelected() {
        return proGroupDTOSelected;
    }

    /**
     * @param proGroupDTOSelected the proGroupDTOSelected to set
     */
    public void setProGroupDTOSelected(ProGroupDTO proGroupDTOSelected) {
        this.proGroupDTOSelected = proGroupDTOSelected;
    }

}
