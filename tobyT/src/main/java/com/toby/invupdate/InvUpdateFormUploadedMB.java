package com.toby.invupdate;

import com.toby.businessservice.InvDialogueService;
import com.toby.businessservice.InvUpdateDetailService;
import com.toby.businessservice.InvUpdateService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvDailogueData;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.toby.BaseFormBean;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * @author khaled
 */
@Named(value = "invUpdateFormUploadedMB")
@ViewScoped
public class InvUpdateFormUploadedMB extends BaseFormBean {

    private InvInvoiceDTO invInvoiceDTO;
    private InvDetailDTO invStripDetailSelected;

    private LazyDataModel<InvDailogueData> dataModelInvDailogueData;

    private List<InvInventoryDTO> invInventoryDTOList;

    private InvInventoryDTOConverter invInventoryDTOConverter;

    @EJB
    private InvUpdateService invUpdateService;
    @EJB
    private InvUpdateDetailService invUpdateDetailService;
    @EJB
    private InvDialogueService invDialogueService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditInvUpdate")) {
            try {
                Integer invStripId = (Integer) getContext().getSessionMap().get("invUpdateSelected");
                if (invStripId != null) {
                    invInvoiceDTO = invUpdateService.findInvInvoiceByInvUpdateId(invStripId);
                    invInvoiceDTO.setInvPurchaseInvoiceDetailList(invUpdateDetailService.getAllInvDetailDTOByInvUpdateDetails(invStripId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String save() {

        return "";
    }

    public void loadInvStrip() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getSearchBySerial() != null) {
            getInvInvoiceDTO().setInvInventoryId(invUpdateService.findinvinventoryDTO(getInvInvoiceDTO().getSearchBySerial(), getUserData().getUserBranch().getId()));
            if (getInvInvoiceDTO().getInvInventoryId() != null && getInvInvoiceDTO().getInvInventoryId().getId() != null) {
                getInvInvoiceDTO().setInvPurchaseInvoiceDetailList(invUpdateDetailService.getAllInvUpdatesDetailFromInvTransferDetail(getInvInvoiceDTO().getSearchBySerial(), getUserData().getUserBranch().getId(), getInvInvoiceDTO().getInvInventoryId().getId()));
            }
        }
    }

    public void loadMultiInvStrip() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvDailogeDataListSelected() != null && !getInvInvoiceDTO().getInvDailogeDataListSelected().isEmpty()) {
//            getInvInvoiceDTO().setInvInventoryId(invUpdateService.findinvinventoryDTO(getInvInvoiceDTO().getInvDailogeDataListSelected().get(0).getSerial(), getUserData().getUserBranch().getId()));
            if (getInvInvoiceDTO().getInvInventoryId() != null && getInvInvoiceDTO().getInvInventoryId().getId() != null) {
                StringBuilder serials = null;
                for (InvDailogueData dailogueData : getInvInvoiceDTO().getInvDailogeDataListSelected()) {
                    if (serials == null) {
                        serials = new StringBuilder();
                        serials.append(dailogueData.getSerial());
                    } else {
                        serials.append(",").append(dailogueData.getSerial());
                    }
                }
                getInvInvoiceDTO().setInvPurchaseInvoiceDetailList(invUpdateDetailService.getAllInvUpdatesDetailFromInvTransferDetail(serials.toString(), getUserData().getUserBranch().getId(), getInvInvoiceDTO().getInvInventoryId().getId()));
                CloseDlg("loaddlg");
                errorMsg(getInvInvoiceDTO().getMsg());
            }
        }
    }

    public void saveHead() {
        if (getInvInvoiceDTO() != null) {
            invInvoiceDTO = invUpdateService.saveNew(getInvInvoiceDTO(), getUserData().getUser());
            msgSave(getInvInvoiceDTO().getMsg());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل "));
        }
    }

    public void saveAll() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            invInvoiceDTO = invUpdateService.saveNewAll(getInvInvoiceDTO(), getUserData().getUser());
            msgSave(getInvInvoiceDTO().getMsg());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل "));
        }
    }

    public void replacedetail(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        for (InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
            if (detailDTO.getIndex().equals(invPurchaseInvoiceDetailsValidate.getIndex())) {
                int itemIndex = getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().indexOf(detailDTO);
                if (itemIndex != -1) {
                    getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().set(itemIndex, invPurchaseInvoiceDetailsValidate);
                }
            }
        }
    }

    public void loadDailogue() {
        this.setDataModelInvDailogueData(new LazyDataModel<InvDailogueData>() {
            private static final long serialVersionUID = 1L;

            @Override
            public InvDailogueData getRowData(String rowKey) {
                for (InvDailogueData invDailogueData : getInvInvoiceDTO().getInvDailogueDataList()) {
                    if (invDailogueData.getIndex().toString().equals(rowKey)) {
                        return invDailogueData;
                    }
                }

                return null;
            }

            @Override
            public Integer getRowKey(InvDailogueData invDailogueData) {
                return invDailogueData.getIndex();
            }

            @Override
            public List<InvDailogueData> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                setRowCount(invDialogueService.getTotalRegistors(getUserData().getUserBranch().getId(), filters, getInvInvoiceDTO().getInventorySetupDTO().getSellBuy(), getInvInvoiceDTO().getScreenName(), getInvInvoiceDTO().getInvInventoryId().getId()).intValue());
                getInvInvoiceDTO().setInvDailogueDataList(invDialogueService.loadInvDailogueData(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), filters, null, getUserData().getUserBranch().getId(), getInvInvoiceDTO().getScreenName(), getInvInvoiceDTO().getInvInventoryId().getId()));
                return getInvInvoiceDTO().getInvDailogueDataList();
            }

        });
        OpenDlg("loaddlg");
    }

    public void loadDialogueInvStrip() {
        changeInventory();
        OpenDlg("loaddlg");
    }

    public void changeInventory() {
        getInvInvoiceDTO().setInvDailogueDataList(invDialogueService.loadInvDailogueDataForInvStrip(getUserData().getUserBranch().getId(), getInvInvoiceDTO().getInvInventoryId().getId()));
    }

    public void loadDlg() {
        if (getInvInvoiceDTO().getInvDailogeDataSelected() != null) {
            setInvInvoiceDTO(invDialogueService.loadDlg(getInvInvoiceDTO()));
            CloseDlg("loaddlg");
            errorMsg(getInvInvoiceDTO().getMsg());
        }
    }

    public void reset() {
        invInvoiceDTO = new InvInvoiceDTO();
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        exit("../invstripNew/invStripList.xhtml");
        return "";
    }

    public InvDetailDTO getInvStripDetailSelected() {
        if (invStripDetailSelected == null) {
            invStripDetailSelected = new InvDetailDTO();
        }
        return invStripDetailSelected;
    }

    public void setInvStripDetailSelected(InvDetailDTO invStripDetailSelected) {
        this.invStripDetailSelected = invStripDetailSelected;
    }

    /**
     * @return the invInvoiceDTO
     */
    public InvInvoiceDTO getInvInvoiceDTO() {
        if (invInvoiceDTO == null) {
            invInvoiceDTO = new InvInvoiceDTO();
        }
        return invInvoiceDTO;
    }

    /**
     * @param invInvoiceDTO the invInvoiceDTO to set
     */
    public void setInvInvoiceDTO(InvInvoiceDTO invInvoiceDTO) {
        this.invInvoiceDTO = invInvoiceDTO;
    }

    /**
     * @return the dataModelInvDailogueData
     */
    public LazyDataModel<InvDailogueData> getDataModelInvDailogueData() {
        return dataModelInvDailogueData;
    }

    /**
     * @param dataModelInvDailogueData the dataModelInvDailogueData to set
     */
    public void setDataModelInvDailogueData(LazyDataModel<InvDailogueData> dataModelInvDailogueData) {
        this.dataModelInvDailogueData = dataModelInvDailogueData;
    }

    /**
     * @return the invInventoryDTOList
     */
    public List<InvInventoryDTO> getInvInventoryDTOList() {
        if (invInventoryDTOList == null || invInventoryDTOList.isEmpty()) {
            invInventoryDTOList = tobyUserInventoryService.getAllInventroyDTOByUserAndBranch(getUserData().getUser());
            setInvInventoryDTOConverter(new InvInventoryDTOConverter(invInventoryDTOList));
        }
        return invInventoryDTOList;
    }

    public void setInvInventoryDTOList(List<InvInventoryDTO> invInventoryDTOList) {
        this.invInventoryDTOList = invInventoryDTOList;
    }

    public InvInventoryDTOConverter getInvInventoryDTOConverter() {
        return invInventoryDTOConverter;
    }

    public void setInvInventoryDTOConverter(InvInventoryDTOConverter invInventoryDTOConverter) {
        this.invInventoryDTOConverter = invInventoryDTOConverter;
    }
}
