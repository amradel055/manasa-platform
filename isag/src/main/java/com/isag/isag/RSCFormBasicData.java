/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.InstProjectGuideService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.RscAccrualDeductionService;
import com.isag.businessservice.RscQotationPriceTenderService;
import com.isag.businessservice.RscTenderService;
import com.isag.businessservice.RscWorkUnitsGuideService;
import com.isag.businessservice.SymbolService;
import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.converter.InstProjectGuideDTOConverter;
import com.isag.converter.InvInventoryDTOConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.converter.RscAccuralDeductionConverter;
import com.isag.converter.RscQotationTenderDTOConverter;
import com.isag.converter.RscWorkUnitsGuideDTOConverter;
import com.isag.converter.SymbolDTOConverter;
import com.isag.converter.TenderDTOConverter;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.dto.SymbolDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author m_els
 */
public class RSCFormBasicData extends BaseFormBean {

    private List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOList;

    private List<SymbolDTO> projectManagerList;
    private List<SymbolDTO> professionalBusinessConsultantList;
    private List<SymbolDTO> generalConsultantList;
    private List<InvOrganizationSiteDTO> customerList;
    private List<InvOrganizationSiteDTO> contractorList;
    private List<RscAccrualDeductionDTO> rscAccrualDTOList;
    private List<RscAccrualDeductionDTO> rscDeductionDTOList;
    private List<RscTenderDTO> rscTenderDTOList;
    private List<RscQotationPriceTenderDTO> rscQotationPriceTenderList;
    private List<InvInventoryDTO> invInventoryDTOList;
    private List<InstProjectGuideDTO> instProjectGuides;
    

    private InstProjectGuideDTOConverter instProjectGuideDTOConverter;
    private SymbolDTOConverter projectManagerConverter;
    private SymbolDTOConverter professionalBusinessConsultantConverter;
    private SymbolDTOConverter generalConsultanConverter;
    private InvOrganizationSiteDTOConverter customerConverter;
    private InvOrganizationSiteDTOConverter contractorConverter;
    private RscWorkUnitsGuideDTOConverter rscWorkUnitsGuideDTOConverter;
    private RscAccuralDeductionConverter rscAccuralConverter;
    private RscAccuralDeductionConverter rscDeductionConverter;
    private TenderDTOConverter tenderDTOConverter;
    private RscQotationTenderDTOConverter rscQotationTenderDTOConverter;
    private InvInventoryDTOConverter invInventoryDTOConverter;

    @EJB
    private SymbolService symbolService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private RscWorkUnitsGuideService rscWorkUnitsGuideService;
    @EJB
    private RscAccrualDeductionService rscAccrualDeductionService;
    @EJB
    private RscTenderService tenderService;
    @EJB
    private RscQotationPriceTenderService rscQotationPriceTenderService;
    @EJB
    private AutoCompleteService autoCompleteService;
    @EJB
    private InstProjectGuideService instProjectGuideService;
    
    public void fillList(){
        getInstProjectGuides();
        getContractorList();
    }

    public List<InvOrganizationSiteDTO> completeInvOrganizationSite(String query) {
        return AutoComplete.completOrgSiteDTO(query, getCustomerList(), getCustomerConverter());
    }
    
    public List<InvOrganizationSiteDTO> completeSupplierAndContractorTo(String query) {
        setContractorList(autoCompleteService.autoCompleteOrganizationSite(getUserData().getUserBranch().getId(), Boolean.TRUE, 4, query));
        setContractorConverter(new InvOrganizationSiteDTOConverter(getContractorList()));
        return getContractorList();

    }
    
    public List<InstProjectGuideDTO> completeInstProjectDTOAllWithOutLevel(String query) {
        instProjectGuides = autoCompleteService.autoCompleteAllInstProjectGuideDTOActiveWithOutLevel(getUserData().getUserBranch().getId(), query);
        setInstProjectGuideDTOConverter(new InstProjectGuideDTOConverter(getInstProjectGuides()));
        return instProjectGuides;
    }

    public List<InvOrganizationSiteDTO> completeContructor(String query) {
        return AutoComplete.completOrgSiteDTO(query, getContractorList(), getContractorConverter());
    }

    public List<SymbolDTO> completegeneralConsultant(String query) {
        return AutoComplete.completeSymbolDTO(query, getGeneralConsultantList(), getGeneralConsultanConverter());
    }
    //---------------------------------------------------------------------

    public List<RscAccrualDeductionDTO> completeAccural(String query) {
        return AutoComplete.completeAccuralOrDeduction(query, getRscAccrualDTOList(), getRscAccuralConverter());
    }

    //---------------------------------------------
    public List<RscAccrualDeductionDTO> completeDeduction(String query) {
        return AutoComplete.completeAccuralOrDeduction(query, getRscDeductionDTOList(), getRscAccuralConverter());
    }
    //---------------------------------------------

    public List<SymbolDTO> completeProfessionalBusinessConsultant(String query) {
        return AutoComplete.completeSymbolDTO(query, getProfessionalBusinessConsultantList(), getProfessionalBusinessConsultantConverter());
    }

    public List<SymbolDTO> completeProjectManager(String query) {
        return AutoComplete.completeSymbolDTO(query, getProjectManagerList(), getProjectManagerConverter());
    }

    public List<RscWorkUnitsGuideDTO> completeWorkUnitsGuideDTO(String query) {
        return AutoComplete.completeWorkUnitsGuideDTO(query, getRscWorkUnitsGuideDTOList(), getRscWorkUnitsGuideDTOConverter());
    }

    public List<RscTenderDTO> completeTender(String query) {
        return AutoComplete.completeTender(query, getRscTenderDTOList(), getTenderDTOConverter());
    }

    public List<RscQotationPriceTenderDTO> completeQutation(String query) { 
        return AutoComplete.completeQutation(query, rscQotationPriceTenderList, getRscQotationTenderDTOConverter());
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init() {
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

    /**
     * @return the projectManagerList
     */
    public List<SymbolDTO> getProjectManagerList() {
        if (projectManagerList == null || projectManagerList.isEmpty()) {
//            projectManagerList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 37);
//            setProjectManagerConverter(new SymbolDTOConverter(projectManagerList));
        }
        return projectManagerList;
    }

    /**
     * @param projectManagerList the projectManagerList to set
     */
    public void setProjectManagerList(List<SymbolDTO> projectManagerList) {
        this.projectManagerList = projectManagerList;
    }

    /**
     * @return the professionalBusinessConsultantList
     */
    public List<SymbolDTO> getProfessionalBusinessConsultantList() {
        if (professionalBusinessConsultantList == null || professionalBusinessConsultantList.isEmpty()) {
//            professionalBusinessConsultantList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 39);
            setProfessionalBusinessConsultantConverter(new SymbolDTOConverter(professionalBusinessConsultantList));
        }
        return professionalBusinessConsultantList;
    }

    /**
     * @param professionalBusinessConsultantList the
     * professionalBusinessConsultantList to set
     */
    public void setProfessionalBusinessConsultantList(List<SymbolDTO> professionalBusinessConsultantList) {
        this.professionalBusinessConsultantList = professionalBusinessConsultantList;
    }

    /**
     * @return the generalConsultantList
     */
    public List<SymbolDTO> getGeneralConsultantList() {
        if (generalConsultantList == null || generalConsultantList.isEmpty()) {
//            generalConsultantList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 38);
            setGeneralConsultanConverter(new SymbolDTOConverter(generalConsultantList));
        }
        return generalConsultantList;
    }

    /**
     * @param generalConsultantList the generalConsultantList to set
     */
    public void setGeneralConsultantList(List<SymbolDTO> generalConsultantList) {
        this.generalConsultantList = generalConsultantList;
    }

    /**
     * @return the customerList
     */
    public List<InvOrganizationSiteDTO> getCustomerList() {
        if (customerList == null || customerList.isEmpty()) {
            customerList = organizationSiteService.getorganizationSiteByBranchIdDTO(getUserData().getUserBranch().getId(), Boolean.TRUE, 0);
            setCustomerConverter(new InvOrganizationSiteDTOConverter(customerList));
        }
        return customerList;
    }

    /**
     * @param customerList the customerList to set
     */
    public void setCustomerList(List<InvOrganizationSiteDTO> customerList) {
        this.customerList = customerList;
    }

    /**
     * @return the rscWorkUnitsGuideDTOList
     */
    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuideDTOList() {
        if (rscWorkUnitsGuideDTOList == null || rscWorkUnitsGuideDTOList.isEmpty()) {
            rscWorkUnitsGuideDTOList = rscWorkUnitsGuideService.getBranchUnitAndType(getUserData().getUserBranch().getId());
            setRscWorkUnitsGuideDTOConverter(new RscWorkUnitsGuideDTOConverter(rscWorkUnitsGuideDTOList));
        }
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
     * @return the contractorList
     */
    public List<InvOrganizationSiteDTO> getContractorList() {
        if (contractorList == null || contractorList.isEmpty()) {
//            contractorList = organizationSiteService.getcontractorByBranchIdDTO(getUserData().getUserBranch().getId(), Boolean.TRUE);
            setContractorList(autoCompleteService.autoCompleteOrganizationSite(getUserData().getUserBranch().getId(), Boolean.TRUE, 4, null));
            setContractorConverter(new InvOrganizationSiteDTOConverter(contractorList));
        }
        return contractorList;

    }

    /**
     * @param contractorList the contractorList to set
     */
    public void setContractorList(List<InvOrganizationSiteDTO> contractorList) {
        this.contractorList = contractorList;
    }

    /**
     * @return the rscAccuralConverter
     */
    public RscAccuralDeductionConverter getRscAccuralConverter() {
        return rscAccuralConverter;
    }

    /**
     * @param rscAccuralDeductionConverter the rscAccuralConverter to set
     */
    public void setRscAccuralConverter(RscAccuralDeductionConverter rscAccuralConverter) {
        this.rscAccuralConverter = rscAccuralConverter;
    }

    /**
     * @return the rscDeductionConverter
     */
    public RscAccuralDeductionConverter getRscDeductionConverter() {
        return rscDeductionConverter;
    }

    /**
     * @param rscAccuralDeductionConverter the rscDeductionConverter to set
     */
    public void setRscDeductionConverter(RscAccuralDeductionConverter rscDeductionConverter) {
        this.rscDeductionConverter = rscDeductionConverter;
    }

    /**
     * @return the rscAccrualDTOList
     */
    public List<RscAccrualDeductionDTO> getRscAccrualDTOList() {
        if (rscAccrualDTOList == null || rscAccrualDTOList.isEmpty()) {
            rscAccrualDTOList = rscAccrualDeductionService.getRscAccuralDeductionByBranchAndType(getUserData().getUserBranch().getId(), 2);
            setRscAccuralConverter(new RscAccuralDeductionConverter(rscAccrualDTOList));
        }
        return rscAccrualDTOList;
    }

    /**
     * @param rscAccrualDTOList the rscAccrualDTOList to set
     */
    public void setRscAccrualDTOList(List<RscAccrualDeductionDTO> rscAccrualDTOList) {
        this.rscAccrualDTOList = rscAccrualDTOList;
    }

    /**
     * @return the rscDeductionDTOList
     */
    public List<RscAccrualDeductionDTO> getRscDeductionDTOList() {
        if (rscDeductionDTOList == null || rscDeductionDTOList.isEmpty()) {
            rscDeductionDTOList = rscAccrualDeductionService.getRscAccuralDeductionByBranchAndType(getUserData().getUserBranch().getId(), 3);
            setRscDeductionConverter(new RscAccuralDeductionConverter(rscDeductionDTOList));
        }
        return rscDeductionDTOList;
    }

    /**
     * @param rscDeductionDTOList the rscDeductionDTOList to set
     */
    public void setRscDeductionDTOList(List<RscAccrualDeductionDTO> rscDeductionDTOList) {
        this.rscDeductionDTOList = rscDeductionDTOList;
    }

    /**
     * @return the tenderDTOConverter
     */
    public TenderDTOConverter getTenderDTOConverter() {
        return tenderDTOConverter;
    }

    /**
     * @param tenderDTOConverter the tenderDTOConverter to set
     */
    public void setTenderDTOConverter(TenderDTOConverter tenderDTOConverter) {
        this.tenderDTOConverter = tenderDTOConverter;
    }

    /**
     * @return the rscTenderDTOList
     */
    public List<RscTenderDTO> getRscTenderDTOList() {

        if (rscTenderDTOList == null || rscTenderDTOList.isEmpty()) {
            rscTenderDTOList = new ArrayList<>();
            rscTenderDTOList = tenderService.getAllRscTenderByBranchId(getUserData().getUser());
            setTenderDTOConverter(new TenderDTOConverter(rscTenderDTOList));

        }
        return rscTenderDTOList;
    }

    /**
     * @param rscTenderDTOList the rscTenderDTOList to set
     */
    public void setRscTenderDTOList(List<RscTenderDTO> rscTenderDTOList) {
        this.rscTenderDTOList = rscTenderDTOList;
    }

    /**
     * @return the tenderService
     */
    public RscTenderService getTenderService() {
        return tenderService;
    }

    /**
     * @param tenderService the tenderService to set
     */
    public void setTenderService(RscTenderService tenderService) {
        this.tenderService = tenderService;
    }

    /**
     * @return the projectManagerConverter
     */
    public SymbolDTOConverter getProjectManagerConverter() {
        return projectManagerConverter;
    }

    /**
     * @param projectManagerConverter the projectManagerConverter to set
     */
    public void setProjectManagerConverter(SymbolDTOConverter projectManagerConverter) {
        this.projectManagerConverter = projectManagerConverter;
    }

    /**
     * @return the professionalBusinessConsultantConverter
     */
    public SymbolDTOConverter getProfessionalBusinessConsultantConverter() {
        return professionalBusinessConsultantConverter;
    }

    /**
     * @param professionalBusinessConsultantConverter the
     * professionalBusinessConsultantConverter to set
     */
    public void setProfessionalBusinessConsultantConverter(SymbolDTOConverter professionalBusinessConsultantConverter) {
        this.professionalBusinessConsultantConverter = professionalBusinessConsultantConverter;
    }

    /**
     * @return the generalConsultanConverter
     */
    public SymbolDTOConverter getGeneralConsultanConverter() {
        return generalConsultanConverter;
    }

    /**
     * @param generalConsultanConverter the generalConsultanConverter to set
     */
    public void setGeneralConsultanConverter(SymbolDTOConverter generalConsultanConverter) {
        this.generalConsultanConverter = generalConsultanConverter;
    }

    /**
     * @return the customerConverter
     */
    public InvOrganizationSiteDTOConverter getCustomerConverter() {
        return customerConverter;
    }

    /**
     * @param customerConverter the customerConverter to set
     */
    public void setCustomerConverter(InvOrganizationSiteDTOConverter customerConverter) {
        this.customerConverter = customerConverter;
    }

    /**
     * @return the contractorConverter
     */
    public InvOrganizationSiteDTOConverter getContractorConverter() {
        return contractorConverter;
    }

    /**
     * @param contractorConverter the contractorConverter to set
     */
    public void setContractorConverter(InvOrganizationSiteDTOConverter contractorConverter) {
        this.contractorConverter = contractorConverter;
    }

    /**
     * @return the rscQotationPriceTenderList
     */
    public List<RscQotationPriceTenderDTO> getRscQotationPriceTenderList(Integer tenderId) {
        if ((rscQotationPriceTenderList == null || rscQotationPriceTenderList.isEmpty()) && tenderId != null) {
            rscQotationPriceTenderList = new ArrayList<>();
            rscQotationPriceTenderList = rscQotationPriceTenderService.getRscQotationPriceTenderByTenderId(tenderId);
            setRscQotationTenderDTOConverter(new RscQotationTenderDTOConverter(rscQotationPriceTenderList));

        }
        return rscQotationPriceTenderList;
    }

    /**
     * @param rscQotationPriceTenderList the rscQotationPriceTenderList to set
     */
    public void setRscQotationPriceTenderList(List<RscQotationPriceTenderDTO> rscQotationPriceTenderList) {
        this.rscQotationPriceTenderList = rscQotationPriceTenderList;
    }

    /**
     * @return the rscQotationTenderDTOConverter
     */
    public RscQotationTenderDTOConverter getRscQotationTenderDTOConverter() {
        return rscQotationTenderDTOConverter;
    }

    /**
     * @param rscQotationTenderDTOConverter the rscQotationTenderDTOConverter to
     * set
     */
    public void setRscQotationTenderDTOConverter(RscQotationTenderDTOConverter rscQotationTenderDTOConverter) {
        this.rscQotationTenderDTOConverter = rscQotationTenderDTOConverter;
    }
    
    public List<InvInventoryDTO> completeInventory(String query) {
        return AutoComplete.completeInventoryDTO(query, getInvInventoryDTOList(), getInvInventoryDTOConverter());
    }

    /**
     * @return the invInventoryDTOConverter
     */
    public InvInventoryDTOConverter getInvInventoryDTOConverter() {
        return invInventoryDTOConverter;
    }

    /**
     * @param invInventoryDTOConverter the invInventoryDTOConverter to set
     */
    public void setInvInventoryDTOConverter(InvInventoryDTOConverter invInventoryDTOConverter) {
        this.invInventoryDTOConverter = invInventoryDTOConverter;
    }

    /**
     * @return the invInventoryDTOList
     */
    public List<InvInventoryDTO> getInvInventoryDTOList() {
        if (invInventoryDTOList == null) {
            invInventoryDTOList = getUserData().getInventoryDTOList();
            setInvInventoryDTOConverter(new InvInventoryDTOConverter(invInventoryDTOList));
        }
        return invInventoryDTOList;
    }

    /**
     * @param invInventoryDTOList the invInventoryDTOList to set
     */
    public void setInvInventoryDTOList(List<InvInventoryDTO> invInventoryDTOList) {
        this.invInventoryDTOList = invInventoryDTOList;
    }

    /**
     * @return the instProjectGuides
     */
    public List<InstProjectGuideDTO> getInstProjectGuides() {
        if(instProjectGuides == null){
            instProjectGuides = new ArrayList<>();
            instProjectGuides = instProjectGuideService.getAllInstProjectGuidesDTO(getUserData().getUserBranch().getId());
            setInstProjectGuideDTOConverter(new InstProjectGuideDTOConverter(instProjectGuides));
        }
        return instProjectGuides;
    }

    /**
     * @param instProjectGuides the instProjectGuides to set
     */
    public void setInstProjectGuides(List<InstProjectGuideDTO> instProjectGuides) {
        this.instProjectGuides = instProjectGuides;
    }

    /**
     * @return the instProjectGuideDTOConverter
     */
    public InstProjectGuideDTOConverter getInstProjectGuideDTOConverter() {
        return instProjectGuideDTOConverter;
    }

    /**
     * @param instProjectGuideDTOConverter the instProjectGuideDTOConverter to set
     */
    public void setInstProjectGuideDTOConverter(InstProjectGuideDTOConverter instProjectGuideDTOConverter) {
        this.instProjectGuideDTOConverter = instProjectGuideDTOConverter;
    }

}
