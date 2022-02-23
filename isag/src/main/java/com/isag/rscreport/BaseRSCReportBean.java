/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscreport;

import com.isag.isag.*;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.RscContractRegisterService;
import com.isag.businessservice.RscWorkUnitsGuideService;
import com.isag.businessservice.reports.RscContractExtractionWorkunitService;
import com.isag.complete.autoCompleteServiceImpl;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.converter.RscContractRegisterDTOConverter;
import com.isag.converter.RscWorkUnitDTOConverter;
import com.isag.converter.RscWorkUnitsGuideDTOConverter;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.RscContractRegister;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author ahmed
 */
public abstract class BaseRSCReportBean extends BaseReportBean  {

    private Integer branchId;

    private List<InvOrganizationSiteDTO> contractorListDTO;
    private List<InvOrganizationSiteDTO> customerListDTO;
    
    private List<RscWorkUnitsGuideDTO> workUnitsGuideDTO;
    private List<RscWorkUnitsGuideDTO> workUnitsGuideDTO2;
    
    private InvOrganizationSiteDTOConverter invOrgSiteDTOConverter;
    private InvOrganizationSiteDTOConverter ownerInvOrgSiteDTOConverter;
    private RscWorkUnitDTOConverter rscWorkUnitDTOConverter;
    private RscWorkUnitDTOConverter rscWorkUnitDTOConverter2;
    private List<RscContractRegisterDTO> contractRegisterListDTO;
    private List<RscContractRegisterDTO> contractRegisterListDTOByOwnerList ;
    private RscContractRegisterDTOConverter contractRegisterDTOConverter;
    private List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOByContractList;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOByContractList;
//    
//    private autoCompleteServiceImpl completeServiceImplFrom;
//    private autoCompleteServiceImpl completeServiceImplTo;

    @EJB
    OrganizationSiteService organizationSiteService;
    @EJB
    RscContractRegisterService contractRegisterService;
    @EJB
    RscContractExtractionWorkunitService contractExtractionWorkunitService;

     @EJB
    RscWorkUnitsGuideService rscWorkUnitsGuideService;
    
    @Override
    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setBranchId(getUserData().getUserBranch().getId());
        getContractRegisterListDTO();
    }

    public List<InvOrganizationSiteDTO> completInvOrgSiteAsOwner(String query) {
        List<InvOrganizationSiteDTO> filteredInvs = new ArrayList<>();
        try {
            List<InvOrganizationSiteDTO> invOrganizationSites = getCustomerListDTO();
            if (query == null || query.trim().equals("")) {

                setOwnerInvOrgSiteDTOConverter(new InvOrganizationSiteDTOConverter(invOrganizationSites));
                return invOrganizationSites;
            }

            String nameAr;
            String code;
            InvOrganizationSiteDTO invOrgSiteFilter;

            for (int i = 0; i < getCustomerListDTO().size(); i++) {
                invOrgSiteFilter = invOrganizationSites.get(i);
                nameAr = invOrgSiteFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }

                code = invOrgSiteFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }
            }

            setOwnerInvOrgSiteDTOConverter(new InvOrganizationSiteDTOConverter(filteredInvs));
            return filteredInvs;
        } catch (Exception e) {
            saveError(e, "BaseRSCReportBean", "completInvOrgSiteAsOwner");
            return filteredInvs;
        }
    }

    public List<InvOrganizationSiteDTO> completOrgSiteAsContractorFrom(String query) {
        List<InvOrganizationSiteDTO> filteredInvs = new ArrayList<>();
        try {
            List<InvOrganizationSiteDTO> invOrganizationSites = getContractorListDTO();
            if (query == null || query.trim().equals("")) {

                setInvOrgSiteDTOConverter(new InvOrganizationSiteDTOConverter(invOrganizationSites));
                return invOrganizationSites;
            }

            String nameAr;
            String code;
            InvOrganizationSiteDTO invOrgSiteFilter;

            for (int i = 0; i < getContractorListDTO().size(); i++) {
                invOrgSiteFilter = invOrganizationSites.get(i);
                nameAr = invOrgSiteFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }

                code = invOrgSiteFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }
            }

            setInvOrgSiteDTOConverter(new InvOrganizationSiteDTOConverter(filteredInvs));
            return filteredInvs;
        } catch (Exception e) {
            saveError(e, "BaseRSCReportBean", "completOrgSiteAsContractorFrom");
            return filteredInvs;
        }
    }

    public List<RscContractRegisterDTO> completeContractRegisterProject(String query) {
        List<RscContractRegisterDTO> filteredInvs = new ArrayList<>();
        try {
            List<RscContractRegisterDTO> rscContractRegisters = getContractRegisterListDTO();
            if (query == null || query.trim().equals("")) {

                setContractRegisterDTOConverter(new RscContractRegisterDTOConverter(rscContractRegisters));
                return rscContractRegisters;
            }

            String nameAr;
            Integer code;
            RscContractRegisterDTO rscContractRegisterFilter;

            for (int i = 0; i < getContractRegisterListDTO().size(); i++) {
                rscContractRegisterFilter = rscContractRegisters.get(i);
                nameAr = rscContractRegisterFilter.getRemarks();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(rscContractRegisterFilter)) {
                        filteredInvs.add(rscContractRegisterFilter);
                    }
                }

                code = rscContractRegisterFilter.getSerial();
                if (code != null) {
                    if (!filteredInvs.contains(rscContractRegisterFilter)) {
                        filteredInvs.add(rscContractRegisterFilter);
                    }
                }
            }

            setContractRegisterDTOConverter(new RscContractRegisterDTOConverter(filteredInvs));
            return filteredInvs;
        } catch (Exception e) {
            saveError(e, "BaseRSCReportBean", "completeContractRegisterProject");
            return filteredInvs;
        }
    }

    public List<RscContractRegisterDTO> completeContractRegisterProjectByOwner(String query) {
        List<RscContractRegisterDTO> filteredInvs = new ArrayList<>();
        try {
            List<RscContractRegisterDTO> rscContractRegisters = contractRegisterListDTOByOwnerList;
            if (query == null || query.trim().equals("")) {

                setContractRegisterDTOConverter(new RscContractRegisterDTOConverter(rscContractRegisters));
                return rscContractRegisters;
            }

            String nameAr;
            Integer code;
            RscContractRegisterDTO rscContractRegisterFilter;

            for (int i = 0; i < getContractRegisterListDTO().size(); i++) {
                rscContractRegisterFilter = rscContractRegisters.get(i);
                nameAr = rscContractRegisterFilter.getRemarks();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(rscContractRegisterFilter)) {
                        filteredInvs.add(rscContractRegisterFilter);
                    }
                }

                code = rscContractRegisterFilter.getSerial();
                if (code != null) {
                    if (!filteredInvs.contains(rscContractRegisterFilter)) {
                        filteredInvs.add(rscContractRegisterFilter);
                    }
                }
            }

            setContractRegisterDTOConverter(new RscContractRegisterDTOConverter(filteredInvs));
            return filteredInvs;
        } catch (Exception e) {
            saveError(e, "BaseRSCReportBean", "completeContractRegisterProjectByOwner");
            return filteredInvs;
        }
    }

    public List<InvOrganizationSiteDTO> completOrgSiteAsContractorTo(String query) {
        List<InvOrganizationSiteDTO> filteredInvs = new ArrayList<>();
        try {
            List<InvOrganizationSiteDTO> invOrganizationSites = getContractorListDTO();
            if (query == null || query.trim().equals("")) {

                setInvOrgSiteDTOConverter(new InvOrganizationSiteDTOConverter(invOrganizationSites));
                return invOrganizationSites;
            }

            String nameAr;
            String code;
            InvOrganizationSiteDTO invOrgSiteFilter;

            for (int i = 0; i < getContractorListDTO().size(); i++) {
                invOrgSiteFilter = invOrganizationSites.get(i);
                nameAr = invOrgSiteFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }

                code = invOrgSiteFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }
            }

            setInvOrgSiteDTOConverter(new InvOrganizationSiteDTOConverter(filteredInvs));
            return filteredInvs;
        } catch (Exception e) {
            saveError(e, "BaseRSCReportBean", "completOrgSiteAsContractorTo");
            return filteredInvs;
        }
    }

    public List<InvOrganizationSiteDTO> completOrgSiteAsCustomerFrom(String query) {
        List<InvOrganizationSiteDTO> filteredInvs = new ArrayList<>();
        try {
            List<InvOrganizationSiteDTO> invOrganizationSites = getCustomerListDTO();
            if (query == null || query.trim().equals("")) {

                setInvOrgSiteDTOConverter(new InvOrganizationSiteDTOConverter(invOrganizationSites));
                return invOrganizationSites;
            }

            String nameAr;
            String code;
            InvOrganizationSiteDTO invOrgSiteFilter;

            for (int i = 0; i < getCustomerListDTO().size(); i++) {
                invOrgSiteFilter = invOrganizationSites.get(i);
                nameAr = invOrgSiteFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }

                code = invOrgSiteFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }
            }

            setInvOrgSiteDTOConverter(new InvOrganizationSiteDTOConverter(filteredInvs));
            return filteredInvs;
        } catch (Exception e) {
            saveError(e, "BaseRSCReportBean", "completOrgSiteAsCustomerFrom");
            return filteredInvs;
        }
    }

    public List<InvOrganizationSiteDTO> completOrgSiteAsCustomerTo(String query) {
        List<InvOrganizationSiteDTO> filteredInvs = new ArrayList<>();
        try {
            List<InvOrganizationSiteDTO> invOrganizationSites = getCustomerListDTO();
            if (query == null || query.trim().equals("")) {

                setInvOrgSiteDTOConverter(new InvOrganizationSiteDTOConverter(invOrganizationSites));
                return invOrganizationSites;
            }

            String nameAr;
            String code;
            InvOrganizationSiteDTO invOrgSiteFilter;

            for (int i = 0; i < getCustomerListDTO().size(); i++) {
                invOrgSiteFilter = invOrganizationSites.get(i);
                nameAr = invOrgSiteFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }

                code = invOrgSiteFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }
            }

            setInvOrgSiteDTOConverter(new InvOrganizationSiteDTOConverter(filteredInvs));
            return filteredInvs;
        } catch (Exception e) {
            saveError(e, "BaseRSCReportBean", "completOrgSiteAsCustomerTo");
            return null;
        }
    }
    
     public List<RscWorkUnitsGuideDTO> completeWorkUnitsGuideAsUnitFrom(String query) {
        List<RscWorkUnitsGuideDTO> RscWorkUnitsGuides = getWorkUnitsGuideDTO();
        if (query == null || query.trim().equals("")) {
            setRscWorkUnitDTOConverter(new RscWorkUnitDTOConverter(RscWorkUnitsGuides));
            return RscWorkUnitsGuides;
        }
        List<RscWorkUnitsGuideDTO> filteredRscWorkUnitsGuides = new ArrayList<>();

        String nameAr;
        String code;
        RscWorkUnitsGuideDTO RscWorkUnitsGuide;
        for (int i = 0; i < getWorkUnitsGuideDTO().size(); i++) {
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

        setRscWorkUnitDTOConverter(new RscWorkUnitDTOConverter(filteredRscWorkUnitsGuides));
        return filteredRscWorkUnitsGuides;
    }

     public List<RscWorkUnitsGuideDTO> completeWorkUnitsGuideAsUnitTo(String query) {
        List<RscWorkUnitsGuideDTO> RscWorkUnitsGuides = getWorkUnitsGuideDTO2();
        if (query == null || query.trim().equals("")) {
            setRscWorkUnitDTOConverter2(new RscWorkUnitDTOConverter(RscWorkUnitsGuides));
            return RscWorkUnitsGuides;
        }
        List<RscWorkUnitsGuideDTO> filteredRscWorkUnitsGuides = new ArrayList<>();

        String nameAr;
        String code;
        RscWorkUnitsGuideDTO RscWorkUnitsGuide;
        for (int i = 0; i < getWorkUnitsGuideDTO2().size(); i++) {
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

        setRscWorkUnitDTOConverter2(new RscWorkUnitDTOConverter(filteredRscWorkUnitsGuides));
        return filteredRscWorkUnitsGuides;
    }
    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the contractorListDTO
     */
    public List<InvOrganizationSiteDTO> getContractorListDTO() {
        if (contractorListDTO == null || contractorListDTO.isEmpty()) {
            contractorListDTO = organizationSiteService.getcontractorByBranchIdDTO(branchId, true);
            invOrgSiteDTOConverter = new InvOrganizationSiteDTOConverter(contractorListDTO);
        }
        return contractorListDTO;
    }

    /**
     * @param contractorListDTO the contractorListDTO to set
     */
    public void setContractorListDTO(List<InvOrganizationSiteDTO> contractorListDTO) {
        this.contractorListDTO = contractorListDTO;
    }

    /**
     * @return the invOrgSiteDTOConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrgSiteDTOConverter() {
        return invOrgSiteDTOConverter;
    }

    /**
     * @param invOrgSiteDTOConverter the invOrgSiteDTOConverter to set
     */
    public void setInvOrgSiteDTOConverter(InvOrganizationSiteDTOConverter invOrgSiteDTOConverter) {
        this.invOrgSiteDTOConverter = invOrgSiteDTOConverter;
    }

    /**
     * @return the customerListDTO
     */
    public List<InvOrganizationSiteDTO> getCustomerListDTO() {
        if (customerListDTO == null || customerListDTO.isEmpty()) {
            customerListDTO = organizationSiteService.getorganizationSiteByBranchIdDTO(branchId, true, 0);
            ownerInvOrgSiteDTOConverter = new InvOrganizationSiteDTOConverter(customerListDTO);
        }
        return customerListDTO;
    }

    /**
     * @param customerListDTO the customerListDTO to set
     */
    public void setCustomerListDTO(List<InvOrganizationSiteDTO> customerListDTO) {
        this.customerListDTO = customerListDTO;
    }

    /**
     * @return the contractRegisterListDTO
     */
    public List<RscContractRegisterDTO> getContractRegisterListDTO() {
        if (contractRegisterListDTO == null || contractRegisterListDTO.isEmpty()) {
            contractRegisterListDTO = contractRegisterService.getBranchRscContractRegisterActiveByBranch(branchId);
        }
        return contractRegisterListDTO;
    }

    /**
     * @param contractRegisterListDTO the contractRegisterListDTO to set
     */
    public void setContractRegisterListDTO(List<RscContractRegisterDTO> contractRegisterListDTO) {
        this.contractRegisterListDTO = contractRegisterListDTO;
    }

    /**
     * @return the contractRegisterDTOConverter
     */
    public RscContractRegisterDTOConverter getContractRegisterDTOConverter() {
        return contractRegisterDTOConverter;
    }

    /**
     * @param contractRegisterDTOConverter the contractRegisterDTOConverter to
     * set
     */
    public void setContractRegisterDTOConverter(RscContractRegisterDTOConverter contractRegisterDTOConverter) {
        this.contractRegisterDTOConverter = contractRegisterDTOConverter;
    }

    /**
     * @return the rscWorkUnitDTOConverter
     */
    public RscWorkUnitDTOConverter getRscWorkUnitDTOConverter() {
        return rscWorkUnitDTOConverter;
    }

    /**
     * @param rscWorkUnitDTOConverter the rscWorkUnitDTOConverter to set
     */
    public void setRscWorkUnitDTOConverter(RscWorkUnitDTOConverter rscWorkUnitDTOConverter) {
        this.rscWorkUnitDTOConverter = rscWorkUnitDTOConverter;
    }

    
    /**
     * @return the rscWorkUnitDTOConverter2
     */
    public RscWorkUnitDTOConverter getRscWorkUnitDTOConverter2() {
        return rscWorkUnitDTOConverter2;
    }

    /**
     * @param rscWorkUnitDTOConverter2 the rscWorkUnitDTOConverter2 to set
     */
    public void setRscWorkUnitDTOConverter2(RscWorkUnitDTOConverter rscWorkUnitDTOConverter2) {
        this.rscWorkUnitDTOConverter2 = rscWorkUnitDTOConverter2;
    }

    
    /**
     * @return the rscWorkUnitsGuideDTOByContractList
     */
    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuideDTOByContractList(Integer contractId) {
//        if (rscWorkUnitsGuideDTOByContractList == null || rscWorkUnitsGuideDTOByContractList.isEmpty()) {
            if (contractId != null) {
                rscWorkUnitsGuideDTOByContractList = contractExtractionWorkunitService.getAllWorkunitViewByContract(contractId, getUserData().getUserBranch().getId());
                setRscWorkUnitDTOConverter(new RscWorkUnitDTOConverter(rscWorkUnitsGuideDTOByContractList));
            } else {
                rscWorkUnitsGuideDTOByContractList = new ArrayList<>();

            }
//        }

        return rscWorkUnitsGuideDTOByContractList;
    }

    /**
     * @param rscWorkUnitsGuideDTOByContractList the
     * rscWorkUnitsGuideDTOByContractList to set
     */
    public void setRscWorkUnitsGuideDTOByContractList(List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOByContractList) {
        this.rscWorkUnitsGuideDTOByContractList = rscWorkUnitsGuideDTOByContractList;
    }

    /**
     * @return the invOrganizationSiteDTOByContractList
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOByContractList(Integer contractId) {
        if (invOrganizationSiteDTOByContractList == null || invOrganizationSiteDTOByContractList.isEmpty()) {
            if (contractId != null) {
                invOrganizationSiteDTOByContractList = contractExtractionWorkunitService.getAllInvOrganizationSiteByContract(contractId, getUserData().getUserBranch().getId());
                setInvOrgSiteDTOConverter(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOByContractList));
            } else {
                rscWorkUnitsGuideDTOByContractList = new ArrayList<>();
            }
        }

        return invOrganizationSiteDTOByContractList;
    }

    /**
     * @param invOrganizationSiteDTOByContractList the
     * invOrganizationSiteDTOByContractList to set
     */
    public void setInvOrganizationSiteDTOByContractList(List<InvOrganizationSiteDTO> invOrganizationSiteDTOByContractList) {
        this.invOrganizationSiteDTOByContractList = invOrganizationSiteDTOByContractList;
    }

    /**
     * @return the contractRegisterListDTOByOwnerList
     */
    public List<RscContractRegisterDTO> getContractRegisterListDTOByOwnerList(Integer ownerId) {
//        if (contractRegisterListDTOByOwnerList == null || contractRegisterListDTOByOwnerList.isEmpty()) {
            if (ownerId != null) {
                contractRegisterListDTOByOwnerList = contractRegisterService.getAllRscContractRegisterByOwner(ownerId, getUserData().getUserBranch().getId());
                contractRegisterDTOConverter = new RscContractRegisterDTOConverter(contractRegisterListDTOByOwnerList);
            } else {
                contractRegisterListDTOByOwnerList = new ArrayList<>();
            }
//        }

        return contractRegisterListDTOByOwnerList;
    }

    /**
     * @param contractRegisterListDTOByOwnerList the
     * contractRegisterListDTOByOwnerList to set
     */
    public void setContractRegisterListDTOByOwnerList(List<RscContractRegisterDTO> contractRegisterListDTOByOwnerList) {
        this.contractRegisterListDTOByOwnerList = contractRegisterListDTOByOwnerList;
    }

    /**
     * @return the ownerInvOrgSiteDTOConverter
     */
    public InvOrganizationSiteDTOConverter getOwnerInvOrgSiteDTOConverter() {
        return ownerInvOrgSiteDTOConverter;
    }

    /**
     * @param ownerInvOrgSiteDTOConverter the ownerInvOrgSiteDTOConverter to set
     */
    public void setOwnerInvOrgSiteDTOConverter(InvOrganizationSiteDTOConverter ownerInvOrgSiteDTOConverter) {
        this.ownerInvOrgSiteDTOConverter = ownerInvOrgSiteDTOConverter;
    }

    /**
     * @return the workUnitsGuideDTO
     */
    public List<RscWorkUnitsGuideDTO> getWorkUnitsGuideDTO() {
         if (workUnitsGuideDTO == null || workUnitsGuideDTO.isEmpty()) {
            workUnitsGuideDTO = rscWorkUnitsGuideService.getBranchRscWorkUnitsGuidesActiveByBranch(getUserData().getUserBranch().getId());
            rscWorkUnitDTOConverter = new RscWorkUnitDTOConverter(workUnitsGuideDTO);
        }
        return workUnitsGuideDTO;
    }

    /**
     * @param workUnitsGuideDTO the workUnitsGuideDTO to set
     */
    public void setWorkUnitsGuideDTO(List<RscWorkUnitsGuideDTO> workUnitsGuideDTO) {
        this.workUnitsGuideDTO = workUnitsGuideDTO;
    }
    public List<RscWorkUnitsGuideDTO> getWorkUnitsGuideDTO2() {
         if (workUnitsGuideDTO2 == null || workUnitsGuideDTO2.isEmpty()) {
            workUnitsGuideDTO2 = rscWorkUnitsGuideService.getBranchRscWorkUnitsGuidesActiveByBranch(getUserData().getUserBranch().getId());
            rscWorkUnitDTOConverter2 = new RscWorkUnitDTOConverter(workUnitsGuideDTO2);
        }
        return workUnitsGuideDTO2;
    }

    /**
     * @param workUnitsGuideDTO the workUnitsGuideDTO to set
     */
    public void setWorkUnitsGuideDTO2(List<RscWorkUnitsGuideDTO> workUnitsGuideDTO2) {
        this.workUnitsGuideDTO2 = workUnitsGuideDTO2;
    }

}
