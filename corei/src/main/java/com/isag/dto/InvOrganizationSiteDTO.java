/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hq003
 */
public class InvOrganizationSiteDTO extends BaseEntityDTO {

    private Integer index;

    private String code;
    private String name;
    private String address;
    private String telephone;
    private String fax;
    private String mobile;
    private String personResponsible;
    private String phone;
    private String email;
    private Integer type;
    private String postBox;
    private BigDecimal discount;
    private BigDecimal openBalanceDebit;
    private BigDecimal openBalanceCredit;
    private BigDecimal openBalance;
    private BigDecimal balanceLimit;
    private String remark;
    private Integer active;
    private String zip;
    private String contactPerson;
    private String phone2;
    private Date openBalanceDate;
    private String companyName;
    private String nationId;
    private String accountBankNumber;
    private String sponsorName1;
    private String sponsorPhone1;
    private String sponsorName2;
    private String sponsorPhone2;
    private Date birthdate;
    private GlAccountDTO accountId;
    private String accountName;
    private Integer accountNumber;
    private Integer branchId;
    private SymbolDTO countryId;
    private CurrencyDTO currencyId;
    private InventoryDelegatorDTO delegatorId;
    private String delegatorName;
    private SymbolDTO regionId;
    private SymbolDTO workTypeId;
    private InvOrganizationSiteDTO parent;
    private String parentName;
    private SymbolDTO supplierType;
    private SymbolDTO contractorType;
    private SymbolDTO categoryIndustryId;
    private SymbolDTO leadStatus;
    private String urlSite;
    private String urlFacebook;
    private String urlInstegram;
    private String urlSnapchat;
    private String urlLinkedin;
    private Integer category;
    private Integer employeeNumber;
    private Date callFirstDate;
    private String discription;
    private EmployeeDTO assignedTo;
    private EmployeeDTO transferTo;
    private SymbolDTO learnAboutCompanyFrom;
    private List<CrmCustomerInterestedProductsDTO> crmCustomerInterestedProductsDTOList;
    private List<CrmCustomerInterestedProductsDTO> CrmCustomerInterestedProductsDTODeleteList;
    private InstProjectGuideDTO instProjectGuideDTO;
    private Boolean employeeIsSupervisor = Boolean.FALSE;
    private SymbolDTO projectName;
    private List<CrmEventDTO> crmEventDTOList;
    private List<CrmEventDTO> crmEventDTODeletedList;
    private CrmEventDTO crmEventDTOSelected;
    private Integer numberOfLeads;
    private Integer numberOfNewLeads;
    private Integer numberOfLeadsAssigned;
    private Integer numberOfNewLeadsAssigned;
    private String globalFilter;
    private String legalAffairsRemark;

    public InvOrganizationSiteDTO() {
    }

    public InvOrganizationSiteDTO(Integer id) {
        this.id = id;
    }

    public InvOrganizationSiteDTO(Integer id, String name, Date creationDate) {
        this.id = id;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the personResponsible
     */
    public String getPersonResponsible() {
        return personResponsible;
    }

    /**
     * @param personResponsible the personResponsible to set
     */
    public void setPersonResponsible(String personResponsible) {
        this.personResponsible = personResponsible;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvOrganizationSiteDTO)) {
            return false;
        }
        InvOrganizationSiteDTO other = (InvOrganizationSiteDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the postBox
     */
    public String getPostBox() {
        return postBox;
    }

    /**
     * @param postBox the postBox to set
     */
    public void setPostBox(String postBox) {
        this.postBox = postBox;
    }

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * @return the openBalanceDebit
     */
    public BigDecimal getOpenBalanceDebit() {
        return openBalanceDebit;
    }

    /**
     * @param openBalanceDebit the openBalanceDebit to set
     */
    public void setOpenBalanceDebit(BigDecimal openBalanceDebit) {
        this.openBalanceDebit = openBalanceDebit;
    }

    /**
     * @return the openBalanceCredit
     */
    public BigDecimal getOpenBalanceCredit() {
        return openBalanceCredit;
    }

    /**
     * @param openBalanceCredit the openBalanceCredit to set
     */
    public void setOpenBalanceCredit(BigDecimal openBalanceCredit) {
        this.openBalanceCredit = openBalanceCredit;
    }

    /**
     * @return the balanceLimit
     */
    public BigDecimal getBalanceLimit() {
        return balanceLimit;
    }

    /**
     * @param balanceLimit the balanceLimit to set
     */
    public void setBalanceLimit(BigDecimal balanceLimit) {
        this.balanceLimit = balanceLimit;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the active
     */
    public Integer getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Integer active) {
        this.active = active;
    }

    /**
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip the zip to set
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * @return the contactPerson
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * @param contactPerson the contactPerson to set
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * @return the phone2
     */
    public String getPhone2() {
        return phone2;
    }

    /**
     * @param phone2 the phone2 to set
     */
    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    /**
     * @return the openBalanceDate
     */
    public Date getOpenBalanceDate() {
        return openBalanceDate;
    }

    /**
     * @param openBalanceDate the openBalanceDate to set
     */
    public void setOpenBalanceDate(Date openBalanceDate) {
        this.openBalanceDate = openBalanceDate;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the nationId
     */
    public String getNationId() {
        return nationId;
    }

    /**
     * @param nationId the nationId to set
     */
    public void setNationId(String nationId) {
        this.nationId = nationId;
    }

    /**
     * @return the accountBankNumber
     */
    public String getAccountBankNumber() {
        return accountBankNumber;
    }

    /**
     * @param accountBankNumber the accountBankNumber to set
     */
    public void setAccountBankNumber(String accountBankNumber) {
        this.accountBankNumber = accountBankNumber;
    }

    /**
     * @return the sponsorName1
     */
    public String getSponsorName1() {
        return sponsorName1;
    }

    /**
     * @param sponsorName1 the sponsorName1 to set
     */
    public void setSponsorName1(String sponsorName1) {
        this.sponsorName1 = sponsorName1;
    }

    /**
     * @return the sponsorPhone1
     */
    public String getSponsorPhone1() {
        return sponsorPhone1;
    }

    /**
     * @param sponsorPhone1 the sponsorPhone1 to set
     */
    public void setSponsorPhone1(String sponsorPhone1) {
        this.sponsorPhone1 = sponsorPhone1;
    }

    /**
     * @return the sponsorName2
     */
    public String getSponsorName2() {
        return sponsorName2;
    }

    /**
     * @param sponsorName2 the sponsorName2 to set
     */
    public void setSponsorName2(String sponsorName2) {
        this.sponsorName2 = sponsorName2;
    }

    /**
     * @return the sponsorPhone2
     */
    public String getSponsorPhone2() {
        return sponsorPhone2;
    }

    /**
     * @param sponsorPhone2 the sponsorPhone2 to set
     */
    public void setSponsorPhone2(String sponsorPhone2) {
        this.sponsorPhone2 = sponsorPhone2;
    }

    /**
     * @return the birthdate
     */
    public Date getBirthdate() {
        return birthdate;
    }

    /**
     * @param birthdate the birthdate to set
     */
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * @return the accountId
     */
    public GlAccountDTO getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(GlAccountDTO accountId) {
        this.accountId = accountId;
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
     * @return the countryId
     */
    public SymbolDTO getCountryId() {
        return countryId;
    }

    /**
     * @param countryId the countryId to set
     */
    public void setCountryId(SymbolDTO countryId) {
        this.countryId = countryId;
    }

    /**
     * @return the currencyId
     */
    public CurrencyDTO getCurrencyId() {
        return currencyId;
    }

    /**
     * @param currencyId the currencyId to set
     */
    public void setCurrencyId(CurrencyDTO currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return the delegatorId
     */
    public InventoryDelegatorDTO getDelegatorId() {
        return delegatorId;
    }

    /**
     * @param delegatorId the delegatorId to set
     */
    public void setDelegatorId(InventoryDelegatorDTO delegatorId) {
        this.delegatorId = delegatorId;
    }

    /**
     * @return the regionId
     */
    public SymbolDTO getRegionId() {
        return regionId;
    }

    /**
     * @param regionId the regionId to set
     */
    public void setRegionId(SymbolDTO regionId) {
        this.regionId = regionId;
    }

    /**
     * @return the workTypeId
     */
    public SymbolDTO getWorkTypeId() {
        return workTypeId;
    }

    /**
     * @param workTypeId the workTypeId to set
     */
    public void setWorkTypeId(SymbolDTO workTypeId) {
        this.workTypeId = workTypeId;
    }

    /**
     * @return the parent
     */
    public InvOrganizationSiteDTO getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(InvOrganizationSiteDTO parent) {
        this.parent = parent;
    }

    /**
     * @return the supplierType
     */
    public SymbolDTO getSupplierType() {
        return supplierType;
    }

    /**
     * @param supplierType the supplierType to set
     */
    public void setSupplierType(SymbolDTO supplierType) {
        this.supplierType = supplierType;
    }

    /**
     * @return the contractorType
     */
    public SymbolDTO getContractorType() {
        return contractorType;
    }

    /**
     * @param contractorType the contractorType to set
     */
    public void setContractorType(SymbolDTO contractorType) {
        this.contractorType = contractorType;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the parentName
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * @param parentName the parentName to set
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * @return the delegatorName
     */
    public String getDelegatorName() {
        return delegatorName;
    }

    /**
     * @param delegatorName the delegatorName to set
     */
    public void setDelegatorName(String delegatorName) {
        this.delegatorName = delegatorName;
    }

    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the accountNumber
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the categoryIndustryId
     */
    public SymbolDTO getCategoryIndustryId() {
        return categoryIndustryId;
    }

    /**
     * @param categoryIndustryId the categoryIndustryId to set
     */
    public void setCategoryIndustryId(SymbolDTO categoryIndustryId) {
        this.categoryIndustryId = categoryIndustryId;
    }

    /**
     * @return the urlSite
     */
    public String getUrlSite() {
        return urlSite;
    }

    /**
     * @param urlSite the urlSite to set
     */
    public void setUrlSite(String urlSite) {
        this.urlSite = urlSite;
    }

    /**
     * @return the urlFacebook
     */
    public String getUrlFacebook() {
        return urlFacebook;
    }

    /**
     * @param urlFacebook the urlFacebook to set
     */
    public void setUrlFacebook(String urlFacebook) {
        this.urlFacebook = urlFacebook;
    }

    /**
     * @return the urlInstegram
     */
    public String getUrlInstegram() {
        return urlInstegram;
    }

    /**
     * @param urlInstegram the urlInstegram to set
     */
    public void setUrlInstegram(String urlInstegram) {
        this.urlInstegram = urlInstegram;
    }

    /**
     * @return the urlSnapchat
     */
    public String getUrlSnapchat() {
        return urlSnapchat;
    }

    /**
     * @param urlSnapchat the urlSnapchat to set
     */
    public void setUrlSnapchat(String urlSnapchat) {
        this.urlSnapchat = urlSnapchat;
    }

    /**
     * @return the urlLinkedin
     */
    public String getUrlLinkedin() {
        return urlLinkedin;
    }

    /**
     * @param urlLinkedin the urlLinkedin to set
     */
    public void setUrlLinkedin(String urlLinkedin) {
        this.urlLinkedin = urlLinkedin;
    }

    /**
     * @return the category
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * @return the employeeNumber
     */
    public Integer getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * @param employeeNumber the employeeNumber to set
     */
    public void setEmployeeNumber(Integer employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    /**
     * @return the callFirstDate
     */
    public Date getCallFirstDate() {
        return callFirstDate;
    }

    /**
     * @param callFirstDate the callFirstDate to set
     */
    public void setCallFirstDate(Date callFirstDate) {
        this.callFirstDate = callFirstDate;
    }

    /**
     * @return the discription
     */
    public String getDiscription() {
        return discription;
    }

    /**
     * @param discription the discription to set
     */
    public void setDiscription(String discription) {
        this.discription = discription;
    }

    /**
     * @return the assignedTo
     */
    public EmployeeDTO getAssignedTo() {
        return assignedTo;
    }

    /**
     * @param assignedTo the assignedTo to set
     */
    public void setAssignedTo(EmployeeDTO assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * @return the leadStatus
     */
    public SymbolDTO getLeadStatus() {
        return leadStatus;
    }

    /**
     * @param leadStatus the leadStatus to set
     */
    public void setLeadStatus(SymbolDTO leadStatus) {
        this.leadStatus = leadStatus;
    }

    /**
     * @return the learnAboutCompanyFrom
     */
    public SymbolDTO getLearnAboutCompanyFrom() {
        return learnAboutCompanyFrom;
    }

    /**
     * @param learnAboutCompanyFrom the learnAboutCompanyFrom to set
     */
    public void setLearnAboutCompanyFrom(SymbolDTO learnAboutCompanyFrom) {
        this.learnAboutCompanyFrom = learnAboutCompanyFrom;
    }

    /**
     * @return the crmCustomerInterestedProductsDTOList
     */
    public List<CrmCustomerInterestedProductsDTO> getCrmCustomerInterestedProductsDTOList() {
        if (crmCustomerInterestedProductsDTOList == null || crmCustomerInterestedProductsDTOList.isEmpty()) {
            crmCustomerInterestedProductsDTOList = new ArrayList<>();
        }
        return crmCustomerInterestedProductsDTOList;
    }

    /**
     * @param crmCustomerInterestedProductsDTOList the
     * crmCustomerInterestedProductsDTOList to set
     */
    public void setCrmCustomerInterestedProductsDTOList(List<CrmCustomerInterestedProductsDTO> crmCustomerInterestedProductsDTOList) {
        this.crmCustomerInterestedProductsDTOList = crmCustomerInterestedProductsDTOList;
    }

    /**
     * @return the CrmCustomerInterestedProductsDTODeleteList
     */
    public List<CrmCustomerInterestedProductsDTO> getCrmCustomerInterestedProductsDTODeleteList() {
        if (CrmCustomerInterestedProductsDTODeleteList == null || CrmCustomerInterestedProductsDTODeleteList.isEmpty()) {
            CrmCustomerInterestedProductsDTODeleteList = new ArrayList<>();
        }
        return CrmCustomerInterestedProductsDTODeleteList;
    }

    /**
     * @param CrmCustomerInterestedProductsDTODeleteList the
     * CrmCustomerInterestedProductsDTODeleteList to set
     */
    public void setCrmCustomerInterestedProductsDTODeleteList(List<CrmCustomerInterestedProductsDTO> CrmCustomerInterestedProductsDTODeleteList) {
        this.CrmCustomerInterestedProductsDTODeleteList = CrmCustomerInterestedProductsDTODeleteList;
    }

    /**
     * @return the instProjectGuideDTO
     */
    public InstProjectGuideDTO getInstProjectGuideDTO() {
        return instProjectGuideDTO;
    }

    /**
     * @param instProjectGuideDTO the instProjectGuideDTO to set
     */
    public void setInstProjectGuideDTO(InstProjectGuideDTO instProjectGuideDTO) {
        this.instProjectGuideDTO = instProjectGuideDTO;
    }

    /**
     * @return the employeeIsSupervisor
     */
    public Boolean getEmployeeIsSupervisor() {
        return employeeIsSupervisor;
    }

    /**
     * @param employeeIsSupervisor the employeeIsSupervisor to set
     */
    public void setEmployeeIsSupervisor(Boolean employeeIsSupervisor) {
        this.employeeIsSupervisor = employeeIsSupervisor;
    }

    /**
     * @return the projectName
     */
    public SymbolDTO getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(SymbolDTO projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the crmEventDTOList
     */
    public List<CrmEventDTO> getCrmEventDTOList() {
        if (crmEventDTOList == null || crmEventDTOList.isEmpty()) {
            crmEventDTOList = new ArrayList<>();
        }
        return crmEventDTOList;
    }

    /**
     * @param crmEventDTOList the crmEventDTOList to set
     */
    public void setCrmEventDTOList(List<CrmEventDTO> crmEventDTOList) {
        this.crmEventDTOList = crmEventDTOList;
    }

    /**
     * @return the crmEventDTOSelected
     */
    public CrmEventDTO getCrmEventDTOSelected() {
        return crmEventDTOSelected;
    }

    /**
     * @param crmEventDTOSelected the crmEventDTOSelected to set
     */
    public void setCrmEventDTOSelected(CrmEventDTO crmEventDTOSelected) {
        this.crmEventDTOSelected = crmEventDTOSelected;
    }

    /**
     * @return the numberOfLeads
     */
    public Integer getNumberOfLeads() {
        return numberOfLeads;
    }

    /**
     * @param numberOfLeads the numberOfLeads to set
     */
    public void setNumberOfLeads(Integer numberOfLeads) {
        this.numberOfLeads = numberOfLeads;
    }

    /**
     * @return the crmEventDTODeletedList
     */
    public List<CrmEventDTO> getCrmEventDTODeletedList() {
        if (crmEventDTODeletedList == null || crmEventDTODeletedList.isEmpty()) {
            crmEventDTODeletedList = new ArrayList<>();
        }
        return crmEventDTODeletedList;
    }

    /**
     * @param crmEventDTODeletedList the crmEventDTODeletedList to set
     */
    public void setCrmEventDTODeletedList(List<CrmEventDTO> crmEventDTODeletedList) {
        this.crmEventDTODeletedList = crmEventDTODeletedList;
    }

    /**
     * @return the numberOfNewLeads
     */
    public Integer getNumberOfNewLeads() {
        return numberOfNewLeads;
    }

    /**
     * @param numberOfNewLeads the numberOfNewLeads to set
     */
    public void setNumberOfNewLeads(Integer numberOfNewLeads) {
        this.numberOfNewLeads = numberOfNewLeads;
    }

    /**
     * @return the openBalance
     */
    public BigDecimal getOpenBalance() {
        return openBalance;
    }

    /**
     * @param openBalance the openBalance to set
     */
    public void setOpenBalance(BigDecimal openBalance) {
        this.openBalance = openBalance;
    }

    /**
     * @return the numberOfLeadsAssigned
     */
    public Integer getNumberOfLeadsAssigned() {
        return numberOfLeadsAssigned;
    }

    /**
     * @param numberOfLeadsAssigned the numberOfLeadsAssigned to set
     */
    public void setNumberOfLeadsAssigned(Integer numberOfLeadsAssigned) {
        this.numberOfLeadsAssigned = numberOfLeadsAssigned;
    }

    /**
     * @return the numberOfNewLeadsAssigned
     */
    public Integer getNumberOfNewLeadsAssigned() {
        return numberOfNewLeadsAssigned;
    }

    /**
     * @param numberOfNewLeadsAssigned the numberOfNewLeadsAssigned to set
     */
    public void setNumberOfNewLeadsAssigned(Integer numberOfNewLeadsAssigned) {
        this.numberOfNewLeadsAssigned = numberOfNewLeadsAssigned;
    }

    /**
     * @return the globalFilter
     */
    public String getGlobalFilter() {
        return globalFilter;
    }

    /**
     * @param globalFilter the globalFilter to set
     */
    public void setGlobalFilter(String globalFilter) {
        this.globalFilter = globalFilter;
    }

    /**
     * @return the transferTo
     */
    public EmployeeDTO getTransferTo() {
        return transferTo;
    }

    /**
     * @param transferTo the transferTo to set
     */
    public void setTransferTo(EmployeeDTO transferTo) {
        this.transferTo = transferTo;
    }
    /**
     * @return the legalAffairsRemark
     */
    public String getLegalAffairsRemark() {
        return legalAffairsRemark;
    }

    /**
     * @param legalAffairsRemark the legalAffairsRemark to set
     */
    public void setLegalAffairsRemark(String legalAffairsRemark) {
        this.legalAffairsRemark = legalAffairsRemark;
    }

}
