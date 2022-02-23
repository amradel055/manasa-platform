/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.support;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.IsagSupportService;
import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.converter.EmployeeDTOConverter;
import com.isag.dto.APIBranchDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.IsagSupportDTO;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.IsagUserService;
import com.isag.converter.APIBranchConverter;
import com.isag.converter.IsagUserDTOConverter;
import com.isag.dto.IsagUserDTO;
import com.isag.isag.SupportForm;

/**
 *
 * @author khale
 */
@Named(value = "isagSupportListMB")
@ViewScoped
public class IsagSupportListMB extends SupportForm {

    private List<IsagSupportDTO> isagsupportList;
    private Integer isagSupportSelect;
    private IsagSupportDTO isagSupportDTOSelected;
    private IsagSupportDTO isagSupportDTOSearch;
    private EmployeeDTOConverter employeeDTOConverter;
    private String password;
    private List<APIBranchDTO> branchListDTO;
    private List<IsagUserDTO> isagUserDTOList;
    private IsagUserDTOConverter isagUserDTOConverter;
    private APIBranchConverter aPIBranchConverter;
    private List<EmployeeDTO> employeeDTOIsagList;
    private EmployeeDTOConverter employeeDTOConverter1;

    @EJB
    IsagSupportService isagSupportService;
    @EJB
    private AutoCompleteService autoCompleteService;
    @EJB
    private IsagUserService userService;
    @EJB
    BranchService branchService;
    @EJB
    IsagUserService isagUserService;

    @PostConstruct
    @Override
    public void init() {

        completeEmployeeIsag(null);
        getEmployeeDTOIsagList();

    }
    
    public void search(){
         setIsagsupportList(isagSupportService.findData(getIsagSupportDTOSearch()));
    }

    public void searchByBranch(){
        getIsagSupportDTOSearch().setBranchId(getUserData().getUserlogin().getBranchId().getId());
         setIsagsupportList(isagSupportService.findDataByBranch(getIsagSupportDTOSearch()));
    }
    
    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../Support/IsagSupport.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "IsagSupportListMB", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (isagSupportSelect != null && isagSupportSelect > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("isagsupportselected", isagSupportSelect);
                context.getSessionMap().put("ScreenMode", "Editisagsupport");
                exit("../Support/IsagSupport.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "IsagSupportListMB", "goToEdit");
            return null;
        }

    }

    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (isagSupportSelect != null && isagSupportSelect > 0) {
                try {
                    isagSupportService.deleteIsagSupport(isagSupportSelect);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    load();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "BranchListBean", "delete");
        }
    }

    public List<EmployeeDTO> completeEmployeeIsag(String query) {
        List<EmployeeDTO> employeeDTOList;
        employeeDTOList = autoCompleteService.autoCompleteEmployee(164, query);
        setEmployeeDTOConverter(new EmployeeDTOConverter(employeeDTOList));
        return employeeDTOList;

    }

    public String checkAdmin() {
        try {

            Boolean check = false;
            check = userService.ckeckAdmin(getPassword());
            if (check) {
                return "isagsupportList_1?faces-redirect=true";
            }else{
            errorMessage("كلمه المرور خطاء") ;
            }
        } catch (Exception e) {
            System.out.print(e);
            saveError(e, "LoginBean", "login()");
        }
        return "";
    }

    /**
     * @return the isagsupportList
     */
    public List<IsagSupportDTO> getIsagsupportList() {
        if (isagsupportList == null || isagsupportList.isEmpty()) {
            if (getUri().contains("isagsupportList_1")) {
                isagsupportList = isagSupportService.findBranchData(null);
            } else {
                isagsupportList = isagSupportService.findBranchData(getUserData().getUserBranch().getId());
            }
        }
        return isagsupportList;
    }

    /**
     * @param isagsupportList the isagsupportList to set
     */
    public void setIsagsupportList(List<IsagSupportDTO> isagsupportList) {
        this.isagsupportList = isagsupportList;
    }

    @Override
    public String save() {
        isagSupportService.save(getIsagSupportDTOSelected(), getUserData().getUser());
        msgSave(getIsagSupportDTOSelected().getMsg());
        setIsagsupportList(null);
        return "";
    }

    /**
     * @return the isagSupportSelect
     */
    public Integer getIsagSupportSelect() {
        return isagSupportSelect;
    }

    /**
     * @param isagSupportSelect the isagSupportSelect to set
     */
    public void setIsagSupportSelect(Integer isagSupportSelect) {
        this.isagSupportSelect = isagSupportSelect;
    }

    /**
     * @return the isagSupportDTOSelected
     */
    public IsagSupportDTO getIsagSupportDTOSelected() {
        return isagSupportDTOSelected;
    }

    /**
     * @param isagSupportDTOSelected the isagSupportDTOSelected to set
     */
    public void setIsagSupportDTOSelected(IsagSupportDTO isagSupportDTOSelected) {
        this.isagSupportDTOSelected = isagSupportDTOSelected;
    }

    /**
     * @return the employeeDTOConverter
     */
    public EmployeeDTOConverter getEmployeeDTOConverter() {
        return employeeDTOConverter;
    }

    /**
     * @param employeeDTOConverter the employeeDTOConverter to set
     */
    public void setEmployeeDTOConverter(EmployeeDTOConverter employeeDTOConverter) {
        this.employeeDTOConverter = employeeDTOConverter;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the isagSupportDTOSearch
     */
    public IsagSupportDTO getIsagSupportDTOSearch() {
        if(isagSupportDTOSearch == null){
            isagSupportDTOSearch = new IsagSupportDTO();
        }
        return isagSupportDTOSearch;
    }

    /**
     * @param isagSupportDTOSearch the isagSupportDTOSearch to set
     */
    public void setIsagSupportDTOSearch(IsagSupportDTO isagSupportDTOSearch) {
        this.isagSupportDTOSearch = isagSupportDTOSearch;
    }

    /**
     * @return the branchListDTO
     */
    public List<APIBranchDTO> getBranchListDTO() {
        if(branchListDTO == null || branchListDTO.isEmpty()){
            branchListDTO = branchService.getAllBranchDTO();
            setaPIBranchConverter(new APIBranchConverter(branchListDTO));
        }
        return branchListDTO;
    }

    /**
     * @param branchListDTO the branchListDTO to set
     */
    public void setBranchListDTO(List<APIBranchDTO> branchListDTO) {
        this.branchListDTO = branchListDTO;
    }

    /**
     * @return the isagUserDTOList
     */
    public List<IsagUserDTO> getIsagUserDTOList() {
        if(isagUserDTOList == null || isagUserDTOList.isEmpty()){
            isagUserDTOList = isagUserService.getAllUsersDTO();
            setIsagUserDTOConverter(new IsagUserDTOConverter(isagUserDTOList));
        }
        return isagUserDTOList;
    }

    /**
     * @param isagUserDTOList the isagUserDTOList to set
     */
    public void setIsagUserDTOList(List<IsagUserDTO> isagUserDTOList) {
        this.isagUserDTOList = isagUserDTOList;
    }

    /**
     * @return the isagUserDTOConverter
     */
    public IsagUserDTOConverter getIsagUserDTOConverter() {
        return isagUserDTOConverter;
    }

    /**
     * @param isagUserDTOConverter the isagUserDTOConverter to set
     */
    public void setIsagUserDTOConverter(IsagUserDTOConverter isagUserDTOConverter) {
        this.isagUserDTOConverter = isagUserDTOConverter;
    }

    /**
     * @return the aPIBranchConverter
     */
    public APIBranchConverter getaPIBranchConverter() {
        return aPIBranchConverter;
    }

    /**
     * @param aPIBranchConverter the aPIBranchConverter to set
     */
    public void setaPIBranchConverter(APIBranchConverter aPIBranchConverter) {
        this.aPIBranchConverter = aPIBranchConverter;
    }

    /**
     * @return the employeeDTOIsagList
     */
    public List<EmployeeDTO> getEmployeeDTOIsagList() {
        employeeDTOIsagList = autoCompleteService.autoCompleteEmployee(164, null);
        setEmployeeDTOConverter1(new EmployeeDTOConverter(employeeDTOIsagList));
        return employeeDTOIsagList;
    }

    /**
     * @param employeeDTOIsagList the employeeDTOIsagList to set
     */
    public void setEmployeeDTOIsagList(List<EmployeeDTO> employeeDTOIsagList) {
        this.employeeDTOIsagList = employeeDTOIsagList;
    }

    /**
     * @return the employeeDTOConverter1
     */
    public EmployeeDTOConverter getEmployeeDTOConverter1() {
        return employeeDTOConverter1;
    }

    /**
     * @param employeeDTOConverter1 the employeeDTOConverter1 to set
     */
    public void setEmployeeDTOConverter1(EmployeeDTOConverter employeeDTOConverter1) {
        this.employeeDTOConverter1 = employeeDTOConverter1;
    }

}
