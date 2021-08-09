/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.hosdoctor;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.CompanyService;
import com.isag.businessservice.HosDoctorService;
import com.isag.comparatorsVars.HosDoctorCodeComparator;

import com.isag.entity.Branch;
import com.isag.entity.HosDoctor;
import com.isag.entity.IsagCompany;
import com.isag.isag.BaseTreeBean;
import com.isag.isag.UserData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author hq003
 */
public class HosDoctorBean extends BaseTreeBean {

    private UserData userData;
    private TreeNode hosDoctorHierarchy;

    private TreeNode selectedHosDoctor;
    private HosDoctor selectedCS;
    private HosDoctor parentHosDoctor;
    private IsagCompany selectedCompany;
    private List<IsagCompany> allCompanies;
    private List<Branch> branchList = new ArrayList<>();
    private String action;
    private Branch selectedBranch;

    private Integer selectedCompanyId;
    private Integer selectedBranchId;
    private Integer selectedParentId;
    private List<HosDoctor> allHosDoctors;
    private boolean showCommissionAndPrice;
    @EJB
    private BranchService branchService;
    @EJB
    private HosDoctorService hosDoctorService;
    @EJB
    private CompanyService companyService;

    @Override
    public String save() {
        try {

            setShowMessage(Boolean.TRUE);
            List<HosDoctor> hosDoctorList;
            if (selectedCS.getId() == null) {
                hosDoctorList = hosDoctorService.getAllHosDoctorByBranchIdAndAccNumOrShotCode(userData.getUserBranch().getId(), selectedCS.getCode());
            } else {
                hosDoctorList = hosDoctorService.getAllHosDoctorByBranchIdAndAccNumOrShotCode(userData.getUserBranch().getId(), selectedCS.getCode(), selectedCS.getId());
            }

            if (hosDoctorList == null || hosDoctorList.isEmpty()) {
                selectedCS.setCompanyId(selectedCompany);
                if (selectedCompany == null) {
                    selectedCS.setCompanyId(companyService.findCompany(selectedCompanyId));
                }
                if (selectedCS.getCompanyId() != null) {
                    if (selectedBranchId == null) {
                        selectedBranchId = userData.getUserBranch().getId();
                    }
                    selectedCS.setBranchId(branchService.findBranch(selectedBranchId));
                    selectedCS.setModifiedBy(userData.getUser());
                    if (parentHosDoctor == null) {
                        selectedCS.setLevel((1));
                        selectedCS.setActive(Boolean.TRUE);
                    } else {
                        selectedCS.setLevel((parentHosDoctor.getLevel() == null) ? null : (parentHosDoctor.getLevel() + 1));
                    }
                    selectedCS.setModificationDate(new Date());

                    if (selectedCS.getLevel() != null && selectedCS.getLevel() > 2) {
                        if (selectedCS.getServicePrice() == null) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), "يجب اضافة سعر الخدمة"));
                            return "";
                        }
                        if (selectedCS.getCommission() == null) {
                            selectedCS.setCommission(BigDecimal.ZERO);
                        }
                    }
                    try {
                        hosDoctorService.addHosDoctor(selectedCS);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), "خطأ هذا الطبيب مسجل بالفعل"));
                        e.printStackTrace();
                    }
                    selectedCS = new HosDoctor();
                    selectedBranchId = null;
                    selectedParentId = null;
                    load();
                }
            } else {
                setShowMessage(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), "الكود مسجل يجب اختيار كود اخر"));
            }
        } catch (Exception e) {
            saveError(e, "HosDoctorBean", "save");
        }
        return "";

    }

    public void edit() {
        try {

            action = "edit";
            selectedCS.setBranchId(selectedBranch);
            setShowMessage(Boolean.TRUE);
        } catch (Exception e) {
            saveError(e, "HosDoctorBean", "edit");
        }
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            setShowMessage(Boolean.FALSE);
            selectedCS = new HosDoctor();
            load();
            action = "";
        } catch (Exception e) {
            saveError(e, "HosDoctorName", "init");
        }
    }

    @Override
    public void load() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            if (userData.getIsAdmin()) {
                //  allCompanies = companyService.getAllCompanies();
                if (userData.getSelectedBranch() == null) {
                    hosDoctorHierarchy = loadAllHosDoctors();
                } else {
                    selectedBranchId = userData.getSelectedBranch();

                }
                //  sortTreeHosDoctorMap();
            } else {
                selectedCompanyId = userData.getCompany().getId();
                if (userData.getSelectedBranch() != null) {
//                selectedCompany = userData.getCompany();
//                allHosDoctors = hosDoctorService.getCompanyHosDoctors(selectedCompany.getId());
//                hosDoctorHierarchy = loadHosDoctorForCompany(selectedCompany.getId());
////                branchList = branchService.getAllBranchByCompanyId(selectedCompany.getId());
//            } else {
                    selectedBranchId = userData.getSelectedBranch();
                    allHosDoctors = hosDoctorService.getBranchWithoutParentHosDoctors(selectedBranchId);
                    allHosDoctors.sort(new HosDoctorCodeComparator());
                    hosDoctorHierarchy = loadHosDoctorForBranch(selectedBranchId);
                    //   sortTreeHosDoctorMap();

                }
            }
            selectedCS.setActive(Boolean.TRUE);
        } catch (Exception e) {
            saveError(e, "HosDoctorBean", "load");
        }
    }

    /*  private void sortTreeHosDoctorMap() {
     Collections.sort((List<HosDoctor>) hosDoctorHierarchy, new Comparator() {
     @Override
     public int compare(Object t, Object t1) {
     HosDoctor c1 = (HosDoctor) t;
     HosDoctor c2 = (HosDoctor) t1;
     if (c1.getCode() > c2.getCode()) {
     return 1;
     } else {
     return -1;
     }
     }
     }
     );
     }*/
    @Override
    public void delete() {
        try {

            setShowMessage(Boolean.TRUE);
            if (selectedCS.getId() != null) {
                if (selectedCS.getChildNodes().isEmpty()) {
                    try {
                        hosDoctorService.deleteHosDoctor(selectedCS);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), "تم الحذف بنجاح"));
                        selectedCS = new HosDoctor();
                        selectedBranchId = null;
                        selectedParentId = null;
                        load();
                        action = "";

                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), "خطأ لم يتم الحذف"));
                        e.printStackTrace();
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), "خطأ لم يتم الحذف"));
                }
            }
        } catch (Exception e) {
            saveError(e, "HosDoctorBean", "delete");
        }
    }

    @Override
    public void add() {
        try {

            showCommissionAndPrice = false;
            setShowMessage(Boolean.FALSE);
            allHosDoctors = hosDoctorService.getBranchWithoutParentHosDoctors(selectedBranchId);
            if (selectedCS != null) {
                parentHosDoctor = selectedCS;
                HosDoctor newselectedCS = new HosDoctor();
                newselectedCS.setBranchId(selectedBranch);
                newselectedCS.setParent(parentHosDoctor);
                newselectedCS.setCreatedBy(userData.getUser());
                if (parentHosDoctor.getId() == null) {
                    newselectedCS.setLevel((1));
                } else {
                    newselectedCS.setLevel((parentHosDoctor.getLevel() == null) ? null : (parentHosDoctor.getLevel() + 1));
                }
                selectedParentId = parentHosDoctor.getId();
                selectedCS = newselectedCS;
                selectedCS.setActive(true);
            } else {
                selectedCS = new HosDoctor();
                selectedCS.setBranchId(selectedBranch);
                selectedCS.setCreatedBy(userData.getUser());
                selectedCS.setLevel((1));
                selectedCS.setActive(true);
            }
            if (selectedCS.getLevel() != null && selectedCS.getLevel() > 2) {
                showCommissionAndPrice = true;
            }

            action = "add";
        } catch (Exception e) {

            saveError(e, "HosDoctorBean", "add");
        }
    }

    public void cancel() {
        try {

            selectedCS = new HosDoctor();
        } catch (Exception e) {
            saveError(e, "HosDoctorBean", "cancel");
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {
        showCommissionAndPrice = false;
        setShowMessage(Boolean.FALSE);
        selectedCS = (HosDoctor) selectedHosDoctor.getData();
        selectedCompanyId = selectedCS.getCompanyId().getId();
        if (selectedCS.getBranchId() != null && selectedCS.getBranchId().getNameAr() != null) {
            selectedBranchId = selectedCS.getBranchId().getId();
        }
        allHosDoctors = hosDoctorService.getBranchWithoutParentHosDoctors(selectedBranchId);
        allHosDoctors.remove(selectedCS);
//        branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
        if (selectedCS.getBranchId() != null) {
            selectedBranchId = selectedCS.getBranchId().getId();
        }
        if (selectedCS.getParent() == null) {
            allHosDoctors = new ArrayList<>();
            selectedParentId = null;
        } else {
            selectedParentId = selectedCS.getParent().getId();
        }
        allHosDoctors.sort(new HosDoctorCodeComparator());
        if (selectedCS.getLevel() != null && selectedCS.getLevel() > 2) {
            showCommissionAndPrice = true;
        }
//        allHosDoctors = hosDoctorService.getCompanyHosDoctors(selectedCompanyId);
//        branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
    }

    public void selectParent() {
        try {
            if (selectedParentId != null) {
                for (HosDoctor hosDoctor : allHosDoctors) {
                    if (hosDoctor.getId().equals(selectedParentId)) {
                        setParentHosDoctor(hosDoctor);
                        selectedCS.setParent(hosDoctor);
                        if (hosDoctor.getLevel() != null) {
                            updateHosDoctorLevels(selectedCS, hosDoctor.getLevel());
                        }
                        break;
                    }
                }
            } else {
                selectedCS.setParent(null);
                updateHosDoctorLevels(selectedCS, 0);
            }
        } catch (Exception e) {
            saveError(e, "HosDoctorBean", "selectParent()");
        }
    }

    private void updateHosDoctorLevels(HosDoctor hosDoctor, Integer level) {
        try {

            hosDoctor.setLevel(++level);
            if (hosDoctor.getChildNodes() != null) {
                for (HosDoctor childNode : hosDoctor.getChildNodes()) {
                    updateHosDoctorLevels(childNode, level);
                }
            }
        } catch (Exception e) {
            saveError(e, "HosDoctorBean", "updateHosDoctorLevels");
        }
    }

    public void validateCode() {
        try {

            List<HosDoctor> hosDoctorList = new ArrayList<>();
            if (selectedCS.getId() == null) {
                hosDoctorList = hosDoctorService.getAllHosDoctorByBranchIdAndAccNumOrShotCode(userData.getUserBranch().getId(), selectedCS.getCode());
            }
            if (hosDoctorList != null && !hosDoctorList.isEmpty()) {
                setShowMessage(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), "الكود مسجل يجب اختيار كود اخر"));
            }
        } catch (Exception e) {
            saveError(e, "HosDoctorBean", "validateCode");

        }
    }

    @Override
    public String getScreenName() {

        return "DoctorsGuide";
    }

    public void selectCompany() {
        try {

            branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
        } catch (Exception e) {
            saveError(e, "HosDoctorBean", "selectCompany");
        }
    }

    /**
     * @return the hosDoctorHierarchy
     */
    public TreeNode getHosDoctorHierarchy() {
        return hosDoctorHierarchy;
    }

    /**
     * @param hosDoctorHierarchy the hosDoctorHierarchy to set
     */
    public void setHosDoctorHierarchy(TreeNode hosDoctorHierarchy) {
        this.hosDoctorHierarchy = hosDoctorHierarchy;
    }

    private TreeNode loadHosDoctorForBranch(Integer id) {

        TreeNode branchHosDoctor = new DefaultTreeNode(new HosDoctor(), null);
        List<HosDoctor> branchCost = hosDoctorService.getBranchHosDoctorRoots(id);
        branchCost.sort(new HosDoctorCodeComparator());
        if (branchCost != null && !branchCost.isEmpty()) {
            branchHosDoctor = new DefaultTreeNode(branchCost.get(0), null);
            for (HosDoctor cost : branchCost) {
                getChildTreeNodesForHosDoctor(cost, branchHosDoctor);
            }
        }

        return branchHosDoctor;
    }

    private TreeNode loadHosDoctorForCompany(Integer id) {
        TreeNode companyHosDoctor = new DefaultTreeNode(new HosDoctor(), null);
        List<HosDoctor> companyCost = hosDoctorService.getCompanyHosDoctorRoots(id);
        companyCost.sort(new HosDoctorCodeComparator());
        if (companyCost != null && !companyCost.isEmpty()) {
            companyHosDoctor = new DefaultTreeNode(companyCost.get(0), null);
            for (HosDoctor cost : companyCost) {
                getChildTreeNodesForHosDoctor(cost, companyHosDoctor);
            }
        }

        return companyHosDoctor;
    }

    private TreeNode loadAllHosDoctors() {

        TreeNode root = new DefaultTreeNode(new HosDoctor(), null);
        List<HosDoctor> allCosts = hosDoctorService.getAllHosDoctorRoots();
        allCosts.sort(new HosDoctorCodeComparator());
        for (HosDoctor cost : allCosts) {
            getChildTreeNodesForHosDoctor(cost, root);
        }
        return root;
    }

    private void getChildTreeNodesForHosDoctor(HosDoctor current, TreeNode baseNode) {
        try {

            current.getChildNodes().sort(new HosDoctorCodeComparator());
            TreeNode parentNode = new DefaultTreeNode(current, baseNode);
            if (current.getChildNodes() != null && !current.getChildNodes().isEmpty()) {
                for (HosDoctor childNode : current.getChildNodes()) {
                    getChildTreeNodesForHosDoctor(childNode, parentNode);
                }
            }
        } catch (Exception e) {
            saveError(e, "HosDoctorBean", "getChildTreeNodesForHosDoctor");
        }
    }

    /**
     * @return the selectedHosDoctor
     */
    public TreeNode getSelectedHosDoctor() {
        return selectedHosDoctor;
    }

    /**
     * @param selectedHosDoctor the selectedHosDoctor to set
     */
    public void setSelectedHosDoctor(TreeNode selectedHosDoctor) {
        this.selectedHosDoctor = selectedHosDoctor;
    }

    /**
     * @return the selectedCS
     */
    public HosDoctor getSelectedCS() {
        return selectedCS;
    }

    /**
     * @param selectedCS the selectedCS to set
     */
    public void setSelectedCS(HosDoctor selectedCS) {
        this.selectedCS = selectedCS;
    }

    /**
     * @return the selectedCompany
     */
    public IsagCompany getSelectedCompany() {
        return selectedCompany;
    }

    /**
     * @param selectedCompany the selectedCompany to set
     */
    public void setSelectedCompany(IsagCompany selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    /**
     * @return the allCompanies
     */
    public List<IsagCompany> getAllCompanies() {
        return allCompanies;
    }

    /**
     * @param allCompanies the allCompanies to set
     */
    public void setAllCompanies(List<IsagCompany> allCompanies) {
        this.allCompanies = allCompanies;
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

    public List<Branch> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<Branch> branchList) {
        this.branchList = branchList;
    }

    public Branch getSelectedBranch() {
        return selectedBranch;
    }

    public void setSelectedBranch(Branch selectedBranch) {
        this.selectedBranch = selectedBranch;
    }

    /**
     * @return the selectedCompanyId
     */
    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    /**
     * @param selectedCompanyId the selectedCompanyId to set
     */
    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    /**
     * @return the selectedBranchId
     */
    public Integer getSelectedBranchId() {
        return selectedBranchId;
    }

    /**
     * @param selectedBranchId the selectedBranchId to set
     */
    public void setSelectedBranchId(Integer selectedBranchId) {
        this.selectedBranchId = selectedBranchId;
    }

    /**
     * @return the parentHosDoctor
     */
    public HosDoctor getParentHosDoctor() {
        return parentHosDoctor;
    }

    /**
     * @param parentHosDoctor the parentHosDoctor to set
     */
    public void setParentHosDoctor(HosDoctor parentHosDoctor) {
        this.parentHosDoctor = parentHosDoctor;
    }

    /**
     * @return the selectedParentId
     */
    public Integer getSelectedParentId() {
        return selectedParentId;
    }

    /**
     * @param selectedParentId the selectedParentId to set
     */
    public void setSelectedParentId(Integer selectedParentId) {
        this.selectedParentId = selectedParentId;
    }

    /**
     * @return the allHosDoctors
     */
    public List<HosDoctor> getAllHosDoctors() {
        return allHosDoctors;
    }

    /**
     * @param allHosDoctors the allHosDoctors to set
     */
    public void setAllHosDoctors(List<HosDoctor> allHosDoctors) {
        this.allHosDoctors = allHosDoctors;
    }

    /**
     * @return the showCommissionAndPrice
     */
    public boolean isShowCommissionAndPrice() {
        return showCommissionAndPrice;
    }

    /**
     * @param showCommissionAndPrice the showCommissionAndPrice to set
     */
    public void setShowCommissionAndPrice(boolean showCommissionAndPrice) {
        this.showCommissionAndPrice = showCommissionAndPrice;
    }

}
