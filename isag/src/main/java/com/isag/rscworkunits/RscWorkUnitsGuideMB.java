/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscworkunits;

import com.isag.businessservice.RscWorkUnitsGuideService;
import com.isag.businessservice.SymbolService;
import com.isag.converter.RscWorkUnitsGuideDTOConverter;
import com.isag.converter.SymbolConverter;

import com.isag.define.AdminUnitDependantEnum;

import com.isag.define.CostCenterDependantEnum;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.RscWorkUnitsGuide;
import com.isag.entity.Symbol;
import com.isag.isag.BaseTreeBean;
import com.isag.isag.UserData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author hq003
 */
@Named(value = "rscWorkUnitsGuideMB")
@ViewScoped
public class RscWorkUnitsGuideMB extends BaseTreeBean {

    private UserData userData;
    private TreeNode unitHierarchy;
    private TreeNode selectedUnit;
    private RscWorkUnitsGuideDTO selectedAcc;
    private RscWorkUnitsGuideDTO fetchedUnit;
    private RscWorkUnitsGuideDTO RscWorkUnitsGuideSelected;

    private String action;
    private boolean visibalitiy;

    private List<RscWorkUnitsGuideDTO> allUnits;
    private List<RscWorkUnitsGuideDTO> RscWorkUnitsGuideList;
    private List<RscWorkUnitsGuideDTO> RscWorkUnitsGuideLocalList;
    private List<Symbol> unitList;

    private RscWorkUnitsGuideDTOConverter rscWorkUnitsGuideConverter;
    private SymbolConverter symbolConverter;

    @EJB
    private RscWorkUnitsGuideService rscWorkUnitsGuideService;
    @EJB
    SymbolService symbolService;

    @PostConstruct
    @Override
    public void init() {
        try {
        setSelectedAcc(new RscWorkUnitsGuideDTO());
        setRscWorkUnitsGuideSelected(null);
        load();
        initializeSelection(1);
        action = "";
        } catch (Exception e) {
            saveError(e, "RscWorkUnitsGuideMB", "init");
        }
    }

    @Override
    public void load() {
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        getUserData().getUser().setBranchId(getUserData().getUserBranch());
        setFetchedUnit(new RscWorkUnitsGuideDTO());

        setRscWorkUnitsGuideList(rscWorkUnitsGuideService.getBranchRscWorkUnitsGuidesActiveByBranch(getUserData().getUserBranch().getId()));
        setRscWorkUnitsGuideLocalList(getRscWorkUnitsGuideList());
        rscWorkUnitsGuideConverter = new RscWorkUnitsGuideDTOConverter(getRscWorkUnitsGuideLocalList());

        unitHierarchy = loadUnitForBranch(getUserData().getUserBranch().getId());
        fillUnitsByBranch();

        setShowMessage(Boolean.FALSE);
        visibalitiy = false;
        } catch (Exception e) {
            saveError(e, "RscWorkUnitsGuideMB", "load");
        }

    }

    public void fillUnitsByBranch() {
        try {
//        if (selectedAcc != null && selectedAcc.getParent() != null) {
//            allUnits = rscWorkUnitsGuideService.getRscWorkUnitsGuidesForBranchForMainUnits(getUserData().getUserBranch().getId(), selectedAcc.getParent().getId());
//        } else {
        setAllUnits(rscWorkUnitsGuideService.getBranchRscWorkUnitsGuidesActiveByBranch(getUserData().getUserBranch().getId()));
//        }
        rscWorkUnitsGuideConverter = new RscWorkUnitsGuideDTOConverter(getAllUnits());
         } catch (Exception e) {
            saveError(e, "RscWorkUnitsGuideMB", "fillUnitsByBranch");
        }
    }

    @Override
    public String save() {
        setShowMessage(Boolean.TRUE);
        try {

            if (getSelectedAcc().getParent() == null) {
                initializeSelection(1);
            } else {
                initializeSelection((getSelectedAcc().getParent().getLevel() == null) ? 0 : (getSelectedAcc().getParent().getLevel() + 1));
            }

            List<RscWorkUnitsGuideDTO> RscWorkUnitsGuides;
            if (getSelectedAcc().getId() == null) {
                RscWorkUnitsGuides = rscWorkUnitsGuideService.getAllRscWorkUnitsGuideByBranchIdAndcode(getUserData().getUserBranch().getId(), getSelectedAcc().getCode());
            } else {

                RscWorkUnitsGuides = rscWorkUnitsGuideService.getAllRscWorkUnitsGuideByBranchIdAndcode(getUserData().getUserBranch().getId(), getSelectedAcc().getCode(), getSelectedAcc().getId());
            }
            if (RscWorkUnitsGuides == null || RscWorkUnitsGuides.isEmpty()) {
                rscWorkUnitsGuideService.addRscWorkUnitsGuide(getSelectedAcc(), getUserData().getUser());
                init();
                setShowMessage(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "رقم البند موجود من قبل!"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
             saveError(e, "RscWorkUnitsGuideMB", "save");
            
        }
        return "";
    }

    @Override
    public void delete() {
        try {
        setShowMessage(Boolean.TRUE);
        try {
            setSelectedAcc((RscWorkUnitsGuideDTO) selectedUnit.getData());

            rscWorkUnitsGuideService.deleteRscWorkUnitsGuide(getSelectedAcc(), getUserData().getUser());
            init();
            setShowMessage(Boolean.TRUE);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("RscWorkUnitsGuide_DELETED")));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("RscWorkUnitsGuide_DELETE_ERROR")));
            e.printStackTrace();
        }
         } catch (Exception e) {
            saveError(e, "RscWorkUnitsGuideMB", "delete");
        }

    }

    @Override
    public void add() {
        try {
        if (getSelectedAcc() != null) {
            RscWorkUnitsGuideDTO newelectedAcc = new RscWorkUnitsGuideDTO();

            if (getSelectedAcc().getId() == null) {
                initializeSelection(1);
            } else {
                newelectedAcc.setParent(getSelectedAcc());

                newelectedAcc.setActive(getSelectedAcc().getActive());
                initializeSelection(getSelectedAcc().getLevel() + 1);
            }
            setSelectedAcc(newelectedAcc);
        } else {
            setSelectedAcc(new RscWorkUnitsGuideDTO());
            initializeSelection(1);

        }

        setShowMessage(Boolean.TRUE);
        action = "add";
        visibalitiy = false;
         } catch (Exception e) {
            saveError(e, "RscWorkUnitsGuideMB", "add");
        }
    }

    public void cancel() {
        try {
        setSelectedAcc(new RscWorkUnitsGuideDTO());
        } catch (Exception e) {
            saveError(e, "RscWorkUnitsGuideMB", "cancel");
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {
        try {
        setSelectedAcc((RscWorkUnitsGuideDTO) selectedUnit.getData());
        fillunit();
        } catch (Exception e) {
            saveError(e, "RscWorkUnitsGuideMB", "onNodeSelect");
        }
    }

    public void fillunit() {
        try {
        if (getSelectedAcc() != null) {
            fillUnitsByBranch();
            visibalitiy = true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), ("يجب اختيار بند ")));
            setShowMessage(true);
        }
        } catch (Exception e) {
            saveError(e, "RscWorkUnitsGuideMB", "fillunit");
        }
    }

    public void selectParent() {
        try {
        if (getSelectedAcc().getParent() != null) {
            if (getSelectedAcc().getParent().getLevel() != null) {
                updateUnitLevels(getSelectedAcc(), getSelectedAcc().getParent().getLevel());
            }
        } else {
            getSelectedAcc().setParent(null);
            updateUnitLevels(getSelectedAcc(), 0);
        }
         } catch (Exception e) {
            saveError(e, "RscWorkUnitsGuideMB", "selectParent");
        }
    }

    private void updateUnitLevels(RscWorkUnitsGuideDTO unit, Integer level) {
        try {
        unit.setLevel(++level);
        List<RscWorkUnitsGuideDTO> currentparent = rscWorkUnitsGuideService.getRscWorkUnitsGuidesChildForParent(getUserData().getUserBranch().getId(), unit.getId());
        if (currentparent != null && !currentparent.isEmpty()) {
            for (RscWorkUnitsGuideDTO childNode : currentparent) {
                childNode.setLevel(++level);
                updateUnitLevels(childNode, level);
            }
        }
         } catch (Exception e) {
            saveError(e, "RscWorkUnitsGuideMB", "updateUnitLevels");
        }
    }

    @Override
    public String getScreenName() {
        return "costcenter";
    }

    private TreeNode loadUnitForBranch(Integer id) {
        TreeNode unitGuide = new DefaultTreeNode(new RscWorkUnitsGuideDTO(), null);
        List<RscWorkUnitsGuideDTO> unitGuideList = rscWorkUnitsGuideService.getBranchUnit(id);
        if (unitGuideList != null && !unitGuideList.isEmpty()) {
            unitGuide = new DefaultTreeNode(unitGuideList.get(0), null);
            for (RscWorkUnitsGuideDTO acc : unitGuideList) {
                getChildTreeNodesForUnit(acc, unitGuide);
            }
        }
        return unitGuide;
    }

    private void getChildTreeNodesForUnit(RscWorkUnitsGuideDTO current, TreeNode baseNode) {
        TreeNode parentNode = new DefaultTreeNode(current, baseNode);
        List<RscWorkUnitsGuideDTO> currentparent = rscWorkUnitsGuideService.getRscWorkUnitsGuidesChildForParent(getUserData().getUserBranch().getId(), current.getId());
        if (currentparent != null && !currentparent.isEmpty()) {
            for (RscWorkUnitsGuideDTO childNode : currentparent) {
                getChildTreeNodesForUnit(childNode, parentNode);
            }
        }
    }

    public List<RscWorkUnitsGuideDTO> completeRscWorkUnitsGuide(String query) {
        List<RscWorkUnitsGuideDTO> RscWorkUnitsGuides = getRscWorkUnitsGuideLocalList();
        if (query == null || query.trim().equals("")) {
            rscWorkUnitsGuideConverter = new RscWorkUnitsGuideDTOConverter(RscWorkUnitsGuides);
            return RscWorkUnitsGuides;
        }
        List<RscWorkUnitsGuideDTO> filteredRscWorkUnitsGuides = new ArrayList<>();

        String nameAr;
        String code;
        RscWorkUnitsGuideDTO RscWorkUnitsGuide;
        for (int i = 0; i < getRscWorkUnitsGuideLocalList().size(); i++) {
            RscWorkUnitsGuide = RscWorkUnitsGuides.get(i);
            nameAr = RscWorkUnitsGuide.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredRscWorkUnitsGuides.contains(RscWorkUnitsGuide)) {
                    filteredRscWorkUnitsGuides.add(RscWorkUnitsGuide);
                }
            }

            code = RscWorkUnitsGuide.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredRscWorkUnitsGuides.contains(RscWorkUnitsGuide)) {
                    filteredRscWorkUnitsGuides.add(RscWorkUnitsGuide);
                }
            }
        }

        rscWorkUnitsGuideConverter = new RscWorkUnitsGuideDTOConverter(filteredRscWorkUnitsGuides);
        return filteredRscWorkUnitsGuides;
    }

    public void setValueToTheSelectedObject() {
        int x = 0;
    }

    public void searchForRscWorkUnitsGuide() {
        setSelectedAcc(getRscWorkUnitsGuideSelected());
        fillunit();
    }

    public void validateUnitNumberInputDublication() {
        for (RscWorkUnitsGuideDTO ga : getRscWorkUnitsGuideList()) {
            if (ga.getCode().equals(getSelectedAcc().getCode())) {
                setShowMessage(Boolean.TRUE);
                // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), ("هذا البند موجود ")));
                setFetchedUnit(new RscWorkUnitsGuideDTO());
                setRscWorkUnitsGuideSelected(new RscWorkUnitsGuideDTO());
                setFetchedUnit(rscWorkUnitsGuideService.findUnitbyBranchIdAndCode(getUserData().getUserBranch().getId(), getSelectedAcc().getCode()));
                setRscWorkUnitsGuideSelected(getFetchedUnit());
                searchForRscWorkUnitsGuide();
                visibalitiy = true;
            }
        }
    }

    public void reset() {
        setSelectedAcc(new RscWorkUnitsGuideDTO());

        initializeSelection(1);
    }

    private void initializeSelection(Integer level) {
        getSelectedAcc().setLevel(level);
        getSelectedAcc().setActive(Boolean.TRUE);
    }

    public void getSelectedUnitNumber() {
        getSelectedAcc().getCode();

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

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    public CostCenterDependantEnum[] getCostCenterDependant() {
        return CostCenterDependantEnum.values();
    }

    public AdminUnitDependantEnum[] getAdministrativeUnitDependant() {
        return AdminUnitDependantEnum.values();
    }

    /**
     * @return the unitHierarchy
     */
    public TreeNode getUnitHierarchy() {
        return unitHierarchy;
    }

    /**
     * @param unitHierarchy the unitHierarchy to set
     */
    public void setUnitHierarchy(TreeNode unitHierarchy) {
        this.unitHierarchy = unitHierarchy;
    }

    /**
     * @return the visibalitiy
     */
    public boolean isVisibalitiy() {
        return visibalitiy;
    }

    /**
     * @param visibalitiy the visibalitiy to set
     */
    public void setVisibalitiy(boolean visibalitiy) {
        this.visibalitiy = visibalitiy;
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
     * @return the rscWorkUnitsGuideConverter
     */
    public RscWorkUnitsGuideDTOConverter getRscWorkUnitsGuideConverter() {
        return rscWorkUnitsGuideConverter;
    }

    /**
     * @param rscWorkUnitsGuideConverter the rscWorkUnitsGuideConverter to set
     */
    public void setRscWorkUnitsGuideConverter(RscWorkUnitsGuideDTOConverter rscWorkUnitsGuideConverter) {
        this.rscWorkUnitsGuideConverter = rscWorkUnitsGuideConverter;
    }

    /**
     * @return the allUnits
     */
    public List<RscWorkUnitsGuideDTO> getAllUnits() {
        return allUnits;
    }

    /**
     * @param allUnits the allUnits to set
     */
    public void setAllUnits(List<RscWorkUnitsGuideDTO> allUnits) {
        this.allUnits = allUnits;
    }

    /**
     * @return the RscWorkUnitsGuideList
     */
    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuideList() {
        return RscWorkUnitsGuideList;
    }

    /**
     * @param RscWorkUnitsGuideList the RscWorkUnitsGuideList to set
     */
    public void setRscWorkUnitsGuideList(List<RscWorkUnitsGuideDTO> RscWorkUnitsGuideList) {
        this.RscWorkUnitsGuideList = RscWorkUnitsGuideList;
    }

    /**
     * @return the RscWorkUnitsGuideLocalList
     */
    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuideLocalList() {
        return RscWorkUnitsGuideLocalList;
    }

    /**
     * @param RscWorkUnitsGuideLocalList the RscWorkUnitsGuideLocalList to set
     */
    public void setRscWorkUnitsGuideLocalList(List<RscWorkUnitsGuideDTO> RscWorkUnitsGuideLocalList) {
        this.RscWorkUnitsGuideLocalList = RscWorkUnitsGuideLocalList;
    }

    /**
     * @return the selectedAcc
     */
    public RscWorkUnitsGuideDTO getSelectedAcc() {
        return selectedAcc;
    }

    /**
     * @param selectedAcc the selectedAcc to set
     */
    public void setSelectedAcc(RscWorkUnitsGuideDTO selectedAcc) {
        this.selectedAcc = selectedAcc;
    }

    /**
     * @return the fetchedUnit
     */
    public RscWorkUnitsGuideDTO getFetchedUnit() {
        return fetchedUnit;
    }

    /**
     * @param fetchedUnit the fetchedUnit to set
     */
    public void setFetchedUnit(RscWorkUnitsGuideDTO fetchedUnit) {
        this.fetchedUnit = fetchedUnit;
    }

    /**
     * @return the RscWorkUnitsGuideSelected
     */
    public RscWorkUnitsGuideDTO getRscWorkUnitsGuideSelected() {
        return RscWorkUnitsGuideSelected;
    }

    /**
     * @param RscWorkUnitsGuideSelected the RscWorkUnitsGuideSelected to set
     */
    public void setRscWorkUnitsGuideSelected(RscWorkUnitsGuideDTO RscWorkUnitsGuideSelected) {
        this.RscWorkUnitsGuideSelected = RscWorkUnitsGuideSelected;
    }

    /**
     * @return the unitList
     */
    public List<Symbol> getUnitList() {
        if (unitList == null || unitList.isEmpty()) {
            unitList = symbolService.getUnitsByCompanyId(getUserData().getCompany().getId());
            symbolConverter = new SymbolConverter(unitList);
        }
        return unitList;
    }

    /**
     * @param unitList the unitList to set
     */
    public void setUnitList(List<Symbol> unitList) {
        this.unitList = unitList;
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

}
