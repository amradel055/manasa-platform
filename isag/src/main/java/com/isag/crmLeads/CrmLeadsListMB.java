/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.crmLeads;

import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.SymbolService;
import com.isag.dto.CrmEventDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.SymbolDTO;
import com.isag.isag.CrmBasicDataForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author WIN7
 */
@Named(value = "crmLeadsListMB")
@ViewScoped
public class CrmLeadsListMB extends CrmBasicDataForm {

    private List<InvOrganizationSiteDTO> leadList;
    private Map<Integer, InvOrganizationSiteDTO> leadTransferMap;

    private InvOrganizationSiteDTO leadObjectSelected;
    private InvOrganizationSiteDTO leadSearch;

    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private SymbolService symbolService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            getLeadList();
            if (getUserData().getEmployeeDTODefault() != null) {
                if (getUri() != null && (getUri().contains("dashboardLeadsList") || getUri().contains("transferLeads"))) {
                    this.setDataModelInvOrganization(new LazyDataModel<InvOrganizationSiteDTO>() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public List<InvOrganizationSiteDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                            setRowCount(organizationSiteService.getTotalRegistors(filters, getLeadSearch(), 5, getUserData().getUserlogin().getBranchId().getId()).intValue());
                            leadList = organizationSiteService.findLeads(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), filters, getLeadSearch(), 5, getUserData().getUserlogin().getBranchId().getId());
                            if (getUri().contains("transferLeads")) {
                                if (getLeadTransferMap() != null || !getLeadTransferMap().isEmpty()) {
                                    for (Map.Entry<Integer, InvOrganizationSiteDTO> entry : getLeadTransferMap().entrySet()) {
                                        Integer key = entry.getKey();
                                        InvOrganizationSiteDTO value = entry.getValue();
                                        Integer i = 0;
                                        for (InvOrganizationSiteDTO invOrganizationSiteDTO : leadList) {
                                            if (invOrganizationSiteDTO.getId().equals(key)) {
                                                leadList.set(i, value);
                                            }
                                            i++;
                                        }
                                    }
                                }
                            }
                            return leadList;
                        }
                    });
                } else {
                    this.setDataModelInvOrganization(new LazyDataModel<InvOrganizationSiteDTO>() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public List<InvOrganizationSiteDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                            setRowCount(organizationSiteService.getTotalRegistors(getUserData().getUserlogin().getBranchId().getId(), filters, getLeadSearch(), getUserData().getUser().getEmployeeId().getId(), getDateType()).intValue());
                            leadList = organizationSiteService.findALLCrmLeads(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), filters, getLeadSearch(), getUserData().getUser().getEmployeeId().getId(), getDateType());
                            return leadList;
                        }
                    });
                }
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Addlead");
            exit("../crmLeads/leads.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (getLeadSelected() != null && getLeadSelected() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("leadSelected", getLeadSelected());
                context.getSessionMap().put("ScreenMode", "EditLead");
                exit("../crmLeads/leads.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "goToEdit");
            return null;
        }
    }

    public String goToLeadsEvent() {
        try {
            if (getLeadSelected() != null && getLeadSelected() > 0) {
                goToLeadsEvent(getLeadSelected());
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "goToEdit");
            return null;
        }
    }

    public String addNewEventFromList() {
        try {
            if (leadObjectSelected != null && leadObjectSelected.getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("leadSelected", leadObjectSelected);
                context.getSessionMap().put("ScreenMode", "addEventFromLeadsList");
                exit("../crmTodoEvent/TodoEvent.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "crmEventListDTOMB", "goToEdit");
            return null;
        }
    }

    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (getLeadSelected() != null) {
                try {
                    InvOrganizationSiteDTO dTO = new InvOrganizationSiteDTO();
                    dTO.setId(getLeadSelected());
                    organizationSiteService.deleteOrganizationSiteDTO(dTO, getUserData().getUserlogin());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    load();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "delete");
        }
    }

    @Override
    public String save() {
        return saveLead();
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void transfer(InvOrganizationSiteDTO invOrganizationSiteDTOSelected) {
        if (invOrganizationSiteDTOSelected != null && invOrganizationSiteDTOSelected.getId() > 0) {
            if (invOrganizationSiteDTOSelected.getTransferTo() == null) {
                getLeadTransferMap().remove(invOrganizationSiteDTOSelected.getId());
            } else {
                getLeadTransferMap().put(invOrganizationSiteDTOSelected.getId(), invOrganizationSiteDTOSelected);
            }

        }
    }

    public String saveTransferLeads() {
        if (getLeadTransferMap() != null || !getLeadTransferMap().isEmpty()) {
            List<InvOrganizationSiteDTO> invOrganizationSiteDTOList = new ArrayList<>(getLeadTransferMap().values());
            String msg = organizationSiteService.saveTransferLead(invOrganizationSiteDTOList, getUserData().getUser());
            msgSave(msg);
            if (msg == null || msg.isEmpty()) {
                setLeadTransferMap(new HashMap<>());
            }
        }
        return "";
    }

    public void search() {
        leadList = organizationSiteService.findLeadsList(getLeadSearch(), getUserData().getUser().getEmployeeId().getId(), getDateType());
    }

    public void searchFromAllLeads() {
        leadList = organizationSiteService.findAllLeadsList(getLeadSearch());
    }

    /**
     * @return the leadList
     */
    public List<InvOrganizationSiteDTO> getLeadList() {
        if (leadList == null) {
            leadList = new ArrayList<>();
        }
        if (getUserData().getEmployeeDTODefault() != null) {
//            setLeadList(organizationSiteService.findLeads(getUserData().getUserBranch().getId(), getUserData().getEmployeeDTODefault().getId(), getDateType()));
            if (getUri() != null && getUri().contains("dashboardLeadsList")) {
                //the old code
                //   setLeadList(organizationSiteService.getLeads(getUserData().getUserlogin().getBranchId().getId(), 5, null));
            } else {
                //the old code
//                   setLeadList(organizationSiteService.findLeadsList(getLeadSearch(), getUserData().getUser().getEmployeeId().getId(), getDateType()));
            }
        }
        return leadList;
    }

    /**
     * @param leadList the leadList to set
     */
    public void setLeadList(List<InvOrganizationSiteDTO> leadList) {
        this.leadList = leadList;
    }

    /**
     * @return the leadObjectSelected
     */
    public InvOrganizationSiteDTO getLeadObjectSelected() {
        return leadObjectSelected;
    }

    /**
     * @param leadObjectSelected the leadObjectSelected to set
     */
    public void setLeadObjectSelected(InvOrganizationSiteDTO leadObjectSelected) {
        this.leadObjectSelected = leadObjectSelected;
    }

    /**
     * @return the leadSearch
     */
    public InvOrganizationSiteDTO getLeadSearch() {
        if (leadSearch == null) {
            leadSearch = new InvOrganizationSiteDTO();
            leadSearch.setBranchId(getUserData().getUserBranch().getId());
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("LeadsType")) {
                leadSearch.setLeadStatus(symbolService.getSymbolByCompanyIdANDgeneralSymbolIdWithSerial(getUserData().getCompany().getId(), 57, (Integer) context.getSessionMap().get("LeadsType")));
                context.getSessionMap().remove("ScreenMode");
                context.getSessionMap().remove("LeadsType");

            }

        }
        return leadSearch;
    }

    /**
     * @param leadSearch the leadSearch to set
     */
    public void setLeadSearch(InvOrganizationSiteDTO leadSearch) {
        this.leadSearch = leadSearch;
    }

    /**
     * @return the leadTransferMap
     */
    public Map<Integer, InvOrganizationSiteDTO> getLeadTransferMap() {
        if (leadTransferMap == null) {
            leadTransferMap = new HashMap<>();
        }
        return leadTransferMap;
    }

    /**
     * @param leadTransferMap the leadTransferMap to set
     */
    public void setLeadTransferMap(Map<Integer, InvOrganizationSiteDTO> leadTransferMap) {
        this.leadTransferMap = leadTransferMap;
    }

}
