/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscTender;

import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.RscTenderDetailWorkUnitService;
import com.isag.businessservice.RscTenderService;
import com.isag.businessservice.RscWorkUnitsGuideService;
import com.isag.businessservice.SymbolService;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.RscWorkUnitsGuideDTOConverter;
import com.isag.converter.SymbolConverter;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscTenderDetailWorkUnitDTO;

import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.Symbol;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author hq003
 */
@Named(value = "rscTenderFormTreeNodeMB")
@ViewScoped
public class RscTenderFormTreeNodeMB extends BaseFormBean {

    private RscTenderDTO tenderDTO;
    private List<RscTenderDetailWorkUnitDTO> rscTenderDetailWorkUnitDTOList;
    private List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOList;
    private List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTODeletedList;
    private RscTenderDetailWorkUnitDTO rscTenderDetailWorkUnitDTOSelected;
    private List<Symbol> projectManagerList;
    private List<Symbol> professionalBusinessConsultantList;
    private List<Symbol> generalConsultantList;
    private List<InvOrganizationSite> customerList;
    private TreeNode tenderDetail;
    private TreeNode selectedUnit;
    private Integer tenderId;
    private SymbolConverter symbolConverter;
    private InvOrganizationSiteConverter invOrganizationSiteConverter;
    private RscWorkUnitsGuideDTOConverter rscWorkUnitsGuideDTOConverter;
    Integer serialDetail = 0;
    @EJB
    private RscTenderService rscTenderService;
    @EJB
    private RscTenderDetailWorkUnitService rscTenderDetailWorkUnitService;
    @EJB
    private SymbolService symbolService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private RscWorkUnitsGuideService rscWorkUnitsGuideService;

    @PostConstruct
    @Override
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            getUserData().getUser().setBranchId(getUserData().getUserBranch());
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddrscTenderDTOSelected")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditrscTenderDTOSelected")) {
                // get tenderiD From Session
                tenderId = (Integer) context.getSessionMap().get("rscTenderDTOSelected");
                loadDetail();
//            loadUnitForTender(tenderId);
            }
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "load");
        }
    }

    public void fillLists() {
        try {
            if (tenderDTO != null && tenderDTO.getId() != null) {
//            rscTenderDetailWorkUnitDTOList = rscTenderDetailWorkUnitService.getBranchRscTenderDetailWorkUnitBygetUserData().getBranch().getId());
            }
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "fillLists");
        }
    }

//    @Override
//    public String save() {
//        try {
//            rscTenderDetailWorkUnitDTOList = new ArrayList<>();
//            fillDetailList(tenderDetail);
//            tenderDTO = rscTenderService.addRscTender(tenderDTO, rscTenderDetailWorkUnitDTOList, getUserData().getUser());
//            loadUnitForTender(tenderDTO.getId());
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
//
//        } catch (Exception e) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
//            e.printStackTrace();
//        }
//        return "";
//
//    }
    @Override
    public String save() {
        try {
//            tenderDTO = rscTenderService.addRscTender(tenderDTO, rscTenderDetailWorkUnitDTOList, null,null, getUserData().getUser());
            loadDetail();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
            saveError(e, "RscTenderFormTreeNodeMB", "save");

        }
        return "";

    }

    public void reset() {
        try {
        rscTenderDetailWorkUnitDTOSelected = null;
        tenderDTO = new RscTenderDTO();
        rscTenderDetailWorkUnitDTOList = new ArrayList<>();
        tenderDetail = null;
        rscWorkUnitsGuideDTODeletedList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "reset");
        }
    }

    public void loadDetail() {
        try {
        rscTenderDetailWorkUnitDTOList = rscTenderDetailWorkUnitService.getTenderDetailWorkUnitByTenderId(tenderId);
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "loadDetail");
        }
    }

    public void updateSummary() {

    }

    private TreeNode loadUnitForTender(Integer tenderId) {
        try {
        List<RscTenderDetailWorkUnitDTO> rscTenderDetailWorkUnitDTOList = rscTenderDetailWorkUnitService.getTenderDetailWorkUnitByTenderId(tenderId);
        if (rscTenderDetailWorkUnitDTOList != null && !rscTenderDetailWorkUnitDTOList.isEmpty()) {
            setTenderDetail(new DefaultTreeNode(rscTenderDetailWorkUnitDTOList.get(0), null));
            for (RscTenderDetailWorkUnitDTO detailWorkUnitDTO : rscTenderDetailWorkUnitDTOList) {
                getChildTreeNodesForUnit(detailWorkUnitDTO, getTenderDetail());
            }
        }
        return getTenderDetail();
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "loadUnitForTender");
            return null;
        }
    }

    private void getChildTreeNodesForUnit(RscTenderDetailWorkUnitDTO current, TreeNode baseNode) {
        try {
        TreeNode parentNode = new DefaultTreeNode(current, baseNode);
//        List<RscTenderDetailWorkUnitDTO> currentparent = rscTenderDetailWorkUnitService.getRscTenderDetailWorkUnitChilds(current.getId());
//        if (currentparent != null && !currentparent.isEmpty()) {
//            for (RscTenderDetailWorkUnitDTO childNode : currentparent) {
//                getChildTreeNodesForUnit(childNode, parentNode);
//            }
//        }
         } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "getChildTreeNodesForUnit");
        }
    }

    public void addDetail() {
        try {
        if (rscTenderDetailWorkUnitDTOList == null) {
            rscTenderDetailWorkUnitDTOList = new ArrayList<>();
        }
        RscTenderDetailWorkUnitDTO detail = new RscTenderDetailWorkUnitDTO();
        rscTenderDetailWorkUnitDTOList.add(detail);
          } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "addDetail");
        }
    }

    public void deleteDetail() {
        try{
        if (rscTenderDetailWorkUnitDTOSelected != null) {
            rscTenderDetailWorkUnitDTOList.remove(rscTenderDetailWorkUnitDTOSelected);
        }
         } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "deleteDetail");
        }
    }

    public void addParentTreeNode() {
        try {
        RscTenderDetailWorkUnitDTO detailWorkUnitDTO = new RscTenderDetailWorkUnitDTO();
        detailWorkUnitDTO.setTenderId(tenderDTO.getId());
        detailWorkUnitDTO.setSerial(++serialDetail);
        detailWorkUnitDTO.setWorkUnitId(getRscWorkUnitsGuideDTOList().get(0).getId());
        new DefaultTreeNode(detailWorkUnitDTO, getTenderDetail());
         } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "addParentTreeNode");
        }
    }

    public void addChildTreeNode() {
        try {
        if (selectedUnit != null) {
            TreeNode baseNode = selectedUnit;
            RscTenderDetailWorkUnitDTO parent = (RscTenderDetailWorkUnitDTO) selectedUnit.getData();
            RscTenderDetailWorkUnitDTO detailWorkUnitDTO = new RscTenderDetailWorkUnitDTO();
            detailWorkUnitDTO.setTenderId(tenderDTO.getId());
            detailWorkUnitDTO.setSerial(++serialDetail);
            detailWorkUnitDTO.setParent(parent);
            detailWorkUnitDTO.setSerialParent(parent.getSerial());
            new DefaultTreeNode(detailWorkUnitDTO, baseNode);
            recalcDetail();
        }
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "addChildTreeNode");
        }
    }

    public void recalcDetail() {
        try {
        rscTenderDetailWorkUnitDTOList = new ArrayList<>();
        loopRecalcDetail(tenderDetail);
        loadUnitForCalc(rscTenderDetailWorkUnitDTOList);
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "recalcDetail");
        }
    }

    private void loopRecalcDetail(TreeNode parent) {
        try {
        for (TreeNode parentNode : parent.getChildren()) {
            RscTenderDetailWorkUnitDTO data = (RscTenderDetailWorkUnitDTO) parentNode.getData();
            if (parentNode.getChildren() != null && !parentNode.getChildren().isEmpty()) {
                data.setChild(Boolean.TRUE);
            }
            rscTenderDetailWorkUnitDTOList.add(data);

            if (parentNode.getChildren() != null && !parentNode.getChildren().isEmpty()) {
                loopRecalcDetail(parentNode);
            }
        }
         } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "loopRecalcDetail");
        }
    }

    private TreeNode loadUnitForCalc(List<RscTenderDetailWorkUnitDTO> rscTenderDetailWorkUnitDTOList) {
        try {
        if (rscTenderDetailWorkUnitDTOList != null && !rscTenderDetailWorkUnitDTOList.isEmpty()) {
            setTenderDetail(new DefaultTreeNode(rscTenderDetailWorkUnitDTOList.get(0), null));
            for (RscTenderDetailWorkUnitDTO detailWorkUnitDTO : rscTenderDetailWorkUnitDTOList) {
                getChildTreeNodesForCalc(detailWorkUnitDTO, getTenderDetail());
            }
        }
        return getTenderDetail();
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "loadUnitForCalc");
            return null;
        }
    }

    private void getChildTreeNodesForCalc(RscTenderDetailWorkUnitDTO current, TreeNode baseNode) {
        try {
        TreeNode parentNode = new DefaultTreeNode(current, baseNode);
        List<RscTenderDetailWorkUnitDTO> currentparent = new ArrayList<>();
        for (RscTenderDetailWorkUnitDTO detailWorkUnitDTO : rscTenderDetailWorkUnitDTOList) {
            if (current.equals(detailWorkUnitDTO.getParent())) {
                currentparent.add(detailWorkUnitDTO);
            }
        }
        if (currentparent != null && !currentparent.isEmpty()) {
            for (RscTenderDetailWorkUnitDTO childNode : currentparent) {
                getChildTreeNodesForUnit(childNode, parentNode);
            }
        }
         } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "getChildTreeNodesForCalc");
        }
    }

    public void deleteTreeNode() {
        try {
        if (selectedUnit != null) {
            TreeNode baseNode = selectedUnit;
            List<TreeNode> nodes = baseNode.getChildren();
            if (!removeElemetOfTreeNode(getTenderDetail(), selectedUnit)) {
                errorMessage("يجب مسح العناصر المرتبطة بهذا العنصر");
            }
        }
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "deleteTreeNode");
        }
    }

    public boolean removeElemetOfTreeNode(TreeNode rootNode,
            TreeNode nodeToDelete) {
        try {
        if (rootNode.getChildren().remove(nodeToDelete)) {
            return true;
        } else {
            for (TreeNode childNode : rootNode.getChildren()) {
                if (childNode.getChildCount() > 0) {
                    return removeElemetOfTreeNode(childNode, nodeToDelete);
                }

            }
            return false;
        }
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "deleteTreeNode");
            return false;
        }
    }

    public List<InvOrganizationSite> completeInvOrganizationSite(String query) {
        try {
        List<InvOrganizationSite> organizationSite = getCustomerList();
        if (query == null || query.trim().equals("")) {
            invOrganizationSiteConverter = new InvOrganizationSiteConverter(organizationSite);
            return organizationSite;
        }
        List<InvOrganizationSite> filteredInvOrganizationSite = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSite invOrganizationSite;
        for (int i = 0; i < getCustomerList().size(); i++) {
            invOrganizationSite = organizationSite.get(i);
            nameAr = invOrganizationSite.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvOrganizationSite.contains(invOrganizationSite)) {
                    filteredInvOrganizationSite.add(invOrganizationSite);
                }
            }

            code = invOrganizationSite.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredInvOrganizationSite.contains(invOrganizationSite)) {
                    filteredInvOrganizationSite.add(invOrganizationSite);
                }
            }
           
        }

        invOrganizationSiteConverter = new InvOrganizationSiteConverter(filteredInvOrganizationSite);
        return filteredInvOrganizationSite;
         } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "completeInvOrganizationSite");
            return null;
        }
    }

    public List<Symbol> completegeneralConsultant(String query) {
        try {
        List<Symbol> generalConsultant = getGeneralConsultantList();
        if (query == null || query.trim().equals("")) {
            symbolConverter = new SymbolConverter(generalConsultant);
            return generalConsultant;
        }
        List<Symbol> filteredgeneralConsultant = new ArrayList<>();

        String nameAr;
        Integer code;
        Symbol symbol;
        for (int i = 0; i < getGeneralConsultantList().size(); i++) {
            symbol = generalConsultant.get(i);
            nameAr = symbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredgeneralConsultant.contains(symbol)) {
                    filteredgeneralConsultant.add(symbol);
                }
            }

            code = symbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredgeneralConsultant.contains(symbol)) {
                    filteredgeneralConsultant.add(symbol);
                }
            }
        }

        symbolConverter = new SymbolConverter(filteredgeneralConsultant);
        return filteredgeneralConsultant;
         } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "completegeneralConsultant");
            return null;
        }
    }

    public List<Symbol> completeProfessionalBusinessConsultant(String query) {
        try {
        List<Symbol> ProfessionalBusinessConsultant = getProfessionalBusinessConsultantList();
        if (query == null || query.trim().equals("")) {
            symbolConverter = new SymbolConverter(ProfessionalBusinessConsultant);
            return ProfessionalBusinessConsultant;
        }
        List<Symbol> filteredProfessionalBusinessConsultant = new ArrayList<>();

        String nameAr;
        Integer code;
        Symbol professionalSymbol;
        for (int i = 0; i < getProfessionalBusinessConsultantList().size(); i++) {
            professionalSymbol = professionalBusinessConsultantList.get(i);
            nameAr = professionalSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredProfessionalBusinessConsultant.contains(professionalSymbol)) {
                    filteredProfessionalBusinessConsultant.add(professionalSymbol);
                }
            }

            code = professionalSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredProfessionalBusinessConsultant.contains(professionalSymbol)) {
                    filteredProfessionalBusinessConsultant.add(professionalSymbol);
                }
            }
        }

        symbolConverter = new SymbolConverter(filteredProfessionalBusinessConsultant);
        return filteredProfessionalBusinessConsultant;
         } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "completeProfessionalBusinessConsultant");
            return null;
        }
    }

    public List<Symbol> completeProjectManager(String query) {
        try {
        List<Symbol> ProjectManager = getProjectManagerList();
        if (query == null || query.trim().equals("")) {
            symbolConverter = new SymbolConverter(ProjectManager);
            return ProjectManager;
        }
        List<Symbol> filteredProjectManager = new ArrayList<>();

        String nameAr;
        Integer code;
        Symbol projectSymbol;
        for (int i = 0; i < getProjectManagerList().size(); i++) {
            projectSymbol = ProjectManager.get(i);
            nameAr = projectSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredProjectManager.contains(projectSymbol)) {
                    filteredProjectManager.add(projectSymbol);
                }
            }

            code = projectSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredProjectManager.contains(projectSymbol)) {
                    filteredProjectManager.add(projectSymbol);
                }
            }
        }

        symbolConverter = new SymbolConverter(filteredProjectManager);
        return filteredProjectManager;
        } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "completeProjectManager");
            return null;
        }
    }

    public List<RscWorkUnitsGuideDTO> completeWorkUnitsGuideDTO(String query) {
        try{
        List<RscWorkUnitsGuideDTO> RscWorkUnitsGuideDTO = getRscWorkUnitsGuideDTOList();
        if (query == null || query.trim().equals("")) {
            rscWorkUnitsGuideDTOConverter = new RscWorkUnitsGuideDTOConverter(RscWorkUnitsGuideDTO);
            return RscWorkUnitsGuideDTO;
        }
        List<RscWorkUnitsGuideDTO> filteredWorkUnitsGuideDTO = new ArrayList<>();

        String nameAr;
        String code;
        RscWorkUnitsGuideDTO UnitsGuideDTO;
        for (int i = 0; i < getRscWorkUnitsGuideDTOList().size(); i++) {
            UnitsGuideDTO = RscWorkUnitsGuideDTO.get(i);
            nameAr = UnitsGuideDTO.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredWorkUnitsGuideDTO.contains(UnitsGuideDTO)) {
                    filteredWorkUnitsGuideDTO.add(UnitsGuideDTO);
                }
            }

            code = UnitsGuideDTO.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredWorkUnitsGuideDTO.contains(UnitsGuideDTO)) {
                    filteredWorkUnitsGuideDTO.add(UnitsGuideDTO);
                }
            }
        }

        rscWorkUnitsGuideDTOConverter = new RscWorkUnitsGuideDTOConverter(filteredWorkUnitsGuideDTO);
        return filteredWorkUnitsGuideDTO;
         } catch (Exception e) {
            saveError(e, "RscTenderFormTreeNodeMB", "completeWorkUnitsGuideDTO");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        return "costcenter";
    }

    /**
     * @return the tenderDTO
     */
    public RscTenderDTO getTenderDTO() {
        if (tenderDTO == null) {
            tenderDTO = new RscTenderDTO();
        }
        return tenderDTO;
    }

    /**
     * @param tenderDTO the tenderDTO to set
     */
    public void setTenderDTO(RscTenderDTO tenderDTO) {
        this.tenderDTO = tenderDTO;
    }

    /**
     * @return the rscTenderDetailWorkUnitDTOList
     */
    public List<RscTenderDetailWorkUnitDTO> getRscTenderDetailWorkUnitDTOList() {
        if (rscTenderDetailWorkUnitDTOList == null || rscTenderDetailWorkUnitDTOList.isEmpty()) {
            if (tenderDTO != null && tenderDTO.getId() != null) {
//                rscTenderDetailWorkUnitDTOList = rscTenderDetailWorkUnitService.getRscTenderDetailWorkUnitChilds(tenderDTO.getId());
            } else {
                rscTenderDetailWorkUnitDTOList = new ArrayList<>();
            }
        }

        return rscTenderDetailWorkUnitDTOList;
    }

    /**
     * @param rscTenderDetailWorkUnitDTOList the rscTenderDetailWorkUnitDTOList
     * to set
     */
    public void setRscTenderDetailWorkUnitDTOList(List<RscTenderDetailWorkUnitDTO> rscTenderDetailWorkUnitDTOList) {
        this.rscTenderDetailWorkUnitDTOList = rscTenderDetailWorkUnitDTOList;
    }

    /**
     * @return the projectManagerList
     */
    public List<Symbol> getProjectManagerList() {
        if (projectManagerList == null || projectManagerList.isEmpty()) {
            projectManagerList = symbolService.getprojectManagerByCompanyId(getUserData().getCompany().getId());
            symbolConverter = new SymbolConverter(projectManagerList);
        }
        return projectManagerList;
    }

    /**
     * @param projectManagerList the projectManagerList to set
     */
    public void setProjectManagerList(List<Symbol> projectManagerList) {
        this.projectManagerList = projectManagerList;
    }

    /**
     * @return the professionalBusinessConsultantList
     */
    public List<Symbol> getProfessionalBusinessConsultantList() {
        if (professionalBusinessConsultantList == null || professionalBusinessConsultantList.isEmpty()) {
            professionalBusinessConsultantList = symbolService.getprofessionalBusinessConsultantByCompanyId(getUserData().getCompany().getId());
            symbolConverter = new SymbolConverter(professionalBusinessConsultantList);
        }
        return professionalBusinessConsultantList;
    }

    /**
     * @param professionalBusinessConsultantList the
     * professionalBusinessConsultantList to set
     */
    public void setProfessionalBusinessConsultantList(List<Symbol> professionalBusinessConsultantList) {
        this.professionalBusinessConsultantList = professionalBusinessConsultantList;
    }

    /**
     * @return the generalConsultantList
     */
    public List<Symbol> getGeneralConsultantList() {
        if (generalConsultantList == null || generalConsultantList.isEmpty()) {
            generalConsultantList = symbolService.getgeneralConsultantByCompanyId(getUserData().getCompany().getId());
            symbolConverter = new SymbolConverter(generalConsultantList);
        }
        return generalConsultantList;
    }

    /**
     * @param generalConsultantList the generalConsultantList to set
     */
    public void setGeneralConsultantList(List<Symbol> generalConsultantList) {
        this.generalConsultantList = generalConsultantList;
    }

    /**
     * @return the customerList
     */
    public List<InvOrganizationSite> getCustomerList() {
        if (customerList == null || customerList.isEmpty()) {
            customerList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), Boolean.TRUE, 0);
            invOrganizationSiteConverter = new InvOrganizationSiteConverter(customerList);
        }
        return customerList;
    }

    /**
     * @param customerList the customerList to set
     */
    public void setCustomerList(List<InvOrganizationSite> customerList) {
        this.customerList = customerList;
    }

    /**
     * @return the tenderDetail
     */
    public TreeNode getTenderDetail() {
        if (tenderDetail == null) {
            setTenderDetail(new DefaultTreeNode(new RscTenderDetailWorkUnitDTO(), null));

        }
        return tenderDetail;
    }

    /**
     * @param tenderDetail the tenderDetail to set
     */
    public void setTenderDetail(TreeNode tenderDetail) {
        this.tenderDetail = tenderDetail;
    }

    /**
     * @return the rscTenderDetailWorkUnitDTOSelected
     */
    public RscTenderDetailWorkUnitDTO getRscTenderDetailWorkUnitDTOSelected() {
        return rscTenderDetailWorkUnitDTOSelected;
    }

    /**
     * @param rscTenderDetailWorkUnitDTOSelected the
     * rscTenderDetailWorkUnitDTOSelected to set
     */
    public void setRscTenderDetailWorkUnitDTOSelected(RscTenderDetailWorkUnitDTO rscTenderDetailWorkUnitDTOSelected) {
        this.rscTenderDetailWorkUnitDTOSelected = rscTenderDetailWorkUnitDTOSelected;
    }

    /**
     * @return the symbolConverter
     */
    public SymbolConverter getSymbolConverter() {
        return symbolConverter;
    }

    /**
     * @param symbolConverter the symbolConverter to set
     */
    public void setSymbolConverter(SymbolConverter symbolConverter) {
        this.symbolConverter = symbolConverter;
    }

    /**
     * @return the invOrganizationSiteConverter
     */
    public InvOrganizationSiteConverter getInvOrganizationSiteConverter() {
        return invOrganizationSiteConverter;
    }

    /**
     * @param invOrganizationSiteConverter the invOrganizationSiteConverter to
     * set
     */
    public void setInvOrganizationSiteConverter(InvOrganizationSiteConverter invOrganizationSiteConverter) {
        this.invOrganizationSiteConverter = invOrganizationSiteConverter;
    }

    /**
     * @return the rscWorkUnitsGuideDTOList
     */
    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuideDTOList() {
        if (rscTenderDetailWorkUnitDTOSelected != null && rscTenderDetailWorkUnitDTOSelected.getId() != null) {
            rscWorkUnitsGuideDTOList = rscWorkUnitsGuideService.getCompanyWorkUnitsForMainUnits(getUserData().getUserBranch().getId(), rscTenderDetailWorkUnitDTOSelected.getId());
        } else {
            rscWorkUnitsGuideDTOList = rscWorkUnitsGuideService.getBranchUnit(getUserData().getUserBranch().getId());
        }
        rscWorkUnitsGuideDTOConverter = new RscWorkUnitsGuideDTOConverter(rscWorkUnitsGuideDTOList);

        return rscWorkUnitsGuideDTOList;
    }

    /**
     * @param rscWorkUnitsGuideDTOList the rscWorkUnitsGuideDTOList to set
     */
    public void setRscWorkUnitsGuideDTOList(List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOList) {
        this.rscWorkUnitsGuideDTOList = rscWorkUnitsGuideDTOList;
    }

    /**
     * @return the rscWorkUnitsGuideDTOConverter
     */
    public RscWorkUnitsGuideDTOConverter getRscWorkUnitsGuideDTOConverter() {
        return rscWorkUnitsGuideDTOConverter;
    }

    /**
     * @param rscWorkUnitsGuideDTOConverter the rscWorkUnitsGuideDTOConverter to
     * set
     */
    public void setRscWorkUnitsGuideDTOConverter(RscWorkUnitsGuideDTOConverter rscWorkUnitsGuideDTOConverter) {
        this.rscWorkUnitsGuideDTOConverter = rscWorkUnitsGuideDTOConverter;
    }

    /**
     * @return the selectedUnit
     */
    public TreeNode getSelectedUnit() {
        return selectedUnit;
    }

    /**
     * @param selectedUnit the selectedUnit to set
     */
    public void setSelectedUnit(TreeNode selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public void fillDetailList(TreeNode parent) {
        for (TreeNode x : parent.getChildren()) {
            rscTenderDetailWorkUnitDTOList.add((RscTenderDetailWorkUnitDTO) x.getData());
            if (x.getChildren() != null && !x.getChildren().isEmpty()) {
                fillDetailList(x);
            }
        }
    }

    /**
     * @return the rscWorkUnitsGuideDTODeletedList
     */
    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuideDTODeletedList() {
        return rscWorkUnitsGuideDTODeletedList;
    }

    /**
     * @param rscWorkUnitsGuideDTODeletedList the
     * rscWorkUnitsGuideDTODeletedList to set
     */
    public void setRscWorkUnitsGuideDTODeletedList(List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTODeletedList) {
        this.rscWorkUnitsGuideDTODeletedList = rscWorkUnitsGuideDTODeletedList;
    }

}
