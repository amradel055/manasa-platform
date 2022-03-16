/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.CRMEventServiceDTO;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.SymbolService;
import com.toby.dto.ChartDTO;
import com.toby.dto.ChartDataDTO;
import com.toby.dto.SymbolDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author AhmedESSAM
 */
@Named(value = "crmGlobalChart")
@ViewScoped
public class CrmGlobalChart implements Serializable {

    private ExternalContext context;
    private UserData userData;

    //filters
    private SymbolDTO status;
    private SymbolDTO eventStatus;
    private Date fromDate;
    private Date toDate;

    //chart Data
    private ChartDTO chartDTO;

    //bar chartes
    private BarChartModel barModel;
    private BarChartModel barModelEventAssignedTo;
    private BarChartModel barModelContract;

    //pie chartes
    private PieChartModel pieModelLearnAboutCompany;
    private PieChartModel pieModelLeadStatus;
    private PieChartModel pieModelProjectName;
    private PieChartModel pieModelAssignedTo;
    private PieChartModel pieModelAssignedToAndStatus;
    private PieChartModel pieModelEventStatus;
    private PieChartModel pieModelEventPriority;
    private PieChartModel pieModelEventActivityType;
    private PieChartModel pieModelEventAssignedToAndStatus;
    private PieChartModel pieModelEventAssignedTo;
    private Integer numberOfLeads;
    private Integer numberOfNewLeads;

    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private CRMEventServiceDTO cRMEventServiceDTO;
    @EJB
    private SymbolService symbolService;

    @PostConstruct
    public void init() {
        createBarModels();
        initPieCharts();
    }

    private void initPieCharts() {
        //leads
        createPieModelByLearnAboutCompany();
        createPieModelByleadStatus();
        createPieModelByProjectName();
        createPieModelByAssignedTo();
        setStatus(symbolService.getSymbolByCompanyIdANDgeneralSymbolIdWithSerial(getUserData().getCompany().getId(), 57, 0));
        createPieModelByAssignedToAndStatus();

        //events
        createPieModelByEventStatus();
        createPieModelByEventPriority();
        createPieModelByEventActivityType();
        createPieModelByEventAssignedTo();
        setEventStatus(symbolService.getSymbolByCompanyIdANDgeneralSymbolIdWithSerial(getUserData().getCompany().getId(), 61, 0));
        createPieModelByEventAssignedToAndStatus();
    }

    private BarChartModel initBarModel(List<ChartDataDTO> chartDTOList) {
        BarChartModel model = new BarChartModel();
        for (ChartDataDTO chartDTO : chartDTOList) {
            ChartSeries chartSeries = new ChartSeries();
            chartSeries.setLabel(chartDTO.getLabel());
            for (Map.Entry<Object, Number> entry : chartDTO.getMap().entrySet()) {
                chartSeries.set(entry.getKey().toString(), entry.getValue().intValue());
            }
            model.addSeries(chartSeries);
        }
        return model;
    }

    private void createBarModels() {
        setChartDTO(organizationSiteService.getCountCustomerPerMonth(81, 5, 1));
        createBarModel();

        //event
        createBarModelEventAssignedTo();
        createBarModelEventAssignedToANDContract();
    }

    private void createBarModel() {
        barModel = initBarModel(getChartDTO().getChartDataDTOList());

        barModel.setTitle(getChartDTO().getTitle());
        barModel.setLegendPosition(getChartDTO().getLegendPosition());

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel(getChartDTO().getxLabel());

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel(getChartDTO().getyLabel());
        yAxis.setMin(getChartDTO().getMin());
        yAxis.setMax(getChartDTO().getMax());
    }

    private void createBarModelEventAssignedTo() {
        setBarModelEventAssignedTo(new BarChartModel());
        ChartDTO chartDTO = organizationSiteService.getCountLeadByAssignedToChartBar(getUserData().getUserlogin().getBranchId().getId(), getFromDate(), getToDate());

        barModelEventAssignedTo = initBarModel(chartDTO.getChartDataDTOList());

        barModelEventAssignedTo.setTitle(chartDTO.getTitle());
        barModelEventAssignedTo.setLegendPosition(chartDTO.getLegendPosition());

        Axis xAxis = barModelEventAssignedTo.getAxis(AxisType.X);
        xAxis.setLabel(chartDTO.getxLabel());

        Axis yAxis = barModelEventAssignedTo.getAxis(AxisType.Y);
        yAxis.setLabel(chartDTO.getyLabel());
        yAxis.setMin(chartDTO.getMin());
        yAxis.setMax(chartDTO.getMax());
    }

    private void createBarModelEventAssignedToANDContract() {
        setBarModelContract(new BarChartModel());
        ChartDTO chartDTO = organizationSiteService.getCountLeadByAssignedToANDContractTypeChartBar(getUserData().getUserlogin().getBranchId().getId(), getFromDate(), getToDate());

        barModelContract = initBarModel(chartDTO.getChartDataDTOList());

        barModelContract.setTitle(chartDTO.getTitle());
        barModelContract.setLegendPosition(chartDTO.getLegendPosition());

        Axis xAxis = barModelContract.getAxis(AxisType.X);
        xAxis.setLabel(chartDTO.getxLabel());

        Axis yAxis = barModelContract.getAxis(AxisType.Y);
        yAxis.setLabel(chartDTO.getyLabel());
        yAxis.setMin(chartDTO.getMin());
        yAxis.setMax(chartDTO.getMax());
    }

    public void createPieModelByLearnAboutCompany() {
        pieModelLearnAboutCompany = new PieChartModel();
        ChartDTO chartDTO = organizationSiteService.getCountCustomerByLearnAboutCompanyfrom(getUserData().getUserlogin().getBranchId().getId(), getFromDate(), getToDate());
        int i = 0;
        for (Number pieValue : chartDTO.getPieValues()) {
            pieModelLearnAboutCompany.set(chartDTO.getPieLabels().get(i), pieValue);
            i++;
        }
        if (chartDTO.getPieValues().isEmpty()) {
            pieModelLearnAboutCompany.set("Empty chart", 0);
        } else {
            pieModelLearnAboutCompany.setTitle(chartDTO.getTitle());
            pieModelLearnAboutCompany.setLegendPosition("s");
            pieModelLearnAboutCompany.setLegendCols(1);
            pieModelLearnAboutCompany.setLegendPlacement(LegendPlacement.INSIDE);
            pieModelLearnAboutCompany.setShowDataLabels(true);
            pieModelLearnAboutCompany.setShadow(true);
            pieModelLearnAboutCompany.setDataFormat(null);
        }

    }

    public void createPieModelByleadStatus() {
        pieModelLeadStatus = new PieChartModel();
        ChartDTO chartDTO = organizationSiteService.getCountCustomerByleadStatus(getUserData().getUserlogin().getBranchId().getId(), getFromDate(), getToDate());
        int i = 0;
        for (Number pieValue : chartDTO.getPieValues()) {
            pieModelLeadStatus.set(chartDTO.getPieLabels().get(i), pieValue);
            i++;
        }
        if (chartDTO.getPieValues().isEmpty()) {
            pieModelLeadStatus.set("Empty chart", 0);
        } else {
            pieModelLeadStatus.setLegendPosition("s");
            pieModelLeadStatus.setLegendCols(1);
            pieModelLeadStatus.setLegendPlacement(LegendPlacement.INSIDE);
            pieModelLeadStatus.setShowDataLabels(true);
            pieModelLeadStatus.setShadow(true);
            pieModelLeadStatus.setDataFormat(null);
        }
        pieModelLeadStatus.setTitle(chartDTO.getTitle());
    }

    public void createPieModelByProjectName() {
        pieModelProjectName = new PieChartModel();
        ChartDTO chartDTO = organizationSiteService.getCountCustomerByProjectName(getUserData().getUserlogin().getBranchId().getId(), getFromDate(), getToDate());
        int i = 0;
        for (Number pieValue : chartDTO.getPieValues()) {
            pieModelProjectName.set(chartDTO.getPieLabels().get(i), pieValue);
            i++;
        }
        if (chartDTO.getPieValues().isEmpty()) {
            pieModelProjectName.set("Empty chart", 0);
        } else {
            pieModelProjectName.setLegendPosition("s");
            pieModelProjectName.setLegendCols(1);
            pieModelProjectName.setLegendPlacement(LegendPlacement.INSIDE);
            pieModelProjectName.setShowDataLabels(true);
            pieModelProjectName.setShadow(true);
            pieModelProjectName.setDataFormat(null);
        }
        pieModelProjectName.setTitle(chartDTO.getTitle());
    }

    public void createPieModelByAssignedTo() {
        pieModelAssignedTo = new PieChartModel();
        ChartDTO chartDTO = organizationSiteService.getCountCustomerByAssignedTo(getUserData().getUserlogin().getBranchId().getId(), getFromDate(), getToDate());
        int i = 0;
        for (Number pieValue : chartDTO.getPieValues()) {
            pieModelAssignedTo.set(chartDTO.getPieLabels().get(i), pieValue);
            i++;
        }
        if (chartDTO.getPieValues().isEmpty()) {
            pieModelAssignedTo.set("Empty chart", 0);
        } else {
            pieModelAssignedTo.setLegendPosition("s");
            pieModelAssignedTo.setLegendCols(1);
            pieModelAssignedTo.setLegendPlacement(LegendPlacement.INSIDE);
            pieModelAssignedTo.setShowDataLabels(true);
            pieModelAssignedTo.setShadow(true);
            pieModelAssignedTo.setDataFormat(null);
        }
        pieModelAssignedTo.setTitle(chartDTO.getTitle());

    }

    public void createPieModelByAssignedToAndStatus() {
        pieModelAssignedToAndStatus = new PieChartModel();
        ChartDTO chartDTO = organizationSiteService.getCountCustomerByAssignedToAndStatus(getUserData().getUserlogin().getBranchId().getId(), getStatus().getId(), getFromDate(), getToDate());

        int i = 0;
        for (Number pieValue : chartDTO.getPieValues()) {
            getPieModelAssignedToAndStatus().set(chartDTO.getPieLabels().get(i), pieValue);
            i++;
        }
        if (chartDTO.getPieValues().isEmpty()) {
            pieModelAssignedToAndStatus.set("Empty chart", 0);
        } else {
            pieModelAssignedToAndStatus.setLegendPosition("s");
            pieModelAssignedToAndStatus.setLegendCols(1);
            pieModelAssignedToAndStatus.setLegendPlacement(LegendPlacement.INSIDE);
            pieModelAssignedToAndStatus.setShadow(true);
            pieModelAssignedToAndStatus.setShowDataLabels(true);
            pieModelAssignedToAndStatus.setDataFormat(null);
        }

        pieModelAssignedToAndStatus.setTitle(chartDTO.getTitle());

    }

    public void createPieModelByEventStatus() {
        pieModelEventStatus = new PieChartModel();
        ChartDTO chartDTO = cRMEventServiceDTO.getCountEventByStatus(getUserData().getUserlogin().getBranchId().getId(), getFromDate(), getToDate());
        int i = 0;
        for (Number pieValue : chartDTO.getPieValues()) {
            pieModelEventStatus.set(chartDTO.getPieLabels().get(i), pieValue);
            i++;
        }
        if (chartDTO.getPieValues().isEmpty()) {
            pieModelEventStatus.set("Empty chart", 0);
        } else {
            pieModelEventStatus.setLegendPosition("s");
            pieModelEventStatus.setLegendCols(1);
            pieModelEventStatus.setLegendPlacement(LegendPlacement.INSIDE);
            pieModelEventStatus.setShowDataLabels(true);
            pieModelEventStatus.setShadow(true);
            pieModelEventStatus.setDataFormat(null);
        }
        pieModelEventStatus.setTitle(chartDTO.getTitle());

    }

    public void createPieModelByEventAssignedToAndStatus() {
        pieModelEventAssignedToAndStatus = new PieChartModel();
        ChartDTO chartDTO = cRMEventServiceDTO.getCountEventByStatusAndAssignedTo(getUserData().getUserlogin().getBranchId().getId(), getEventStatus().getId(), getFromDate(), getToDate());
        int i = 0;
        for (Number pieValue : chartDTO.getPieValues()) {
            pieModelEventAssignedToAndStatus.set(chartDTO.getPieLabels().get(i), pieValue);
            i++;
        }
        if (chartDTO.getPieValues().isEmpty()) {
            pieModelEventAssignedToAndStatus.set("Empty chart", 0);
        } else {
            pieModelEventAssignedToAndStatus.setLegendPosition("s");
            pieModelEventAssignedToAndStatus.setLegendCols(1);
            pieModelEventAssignedToAndStatus.setLegendPlacement(LegendPlacement.INSIDE);
            pieModelEventAssignedToAndStatus.setShowDataLabels(true);
            pieModelEventAssignedToAndStatus.setShadow(true);
            pieModelEventAssignedToAndStatus.setDataFormat(null);
        }
        pieModelEventAssignedToAndStatus.setTitle(chartDTO.getTitle());

    }

    public void createPieModelByEventAssignedTo() {
        pieModelEventAssignedTo = new PieChartModel();
        ChartDTO chartDTO = cRMEventServiceDTO.getCountEventByAssignedTo(getUserData().getUserlogin().getBranchId().getId(), getFromDate(), getToDate());
        int i = 0;
        for (Number pieValue : chartDTO.getPieValues()) {
            pieModelEventAssignedTo.set(chartDTO.getPieLabels().get(i), pieValue);
            i++;
        }
        if (chartDTO.getPieValues().isEmpty()) {
            pieModelEventAssignedTo.set("Empty chart", 0);
        } else {
            pieModelEventAssignedTo.setLegendPosition("s");
            pieModelEventAssignedTo.setLegendCols(1);
            pieModelEventAssignedTo.setLegendPlacement(LegendPlacement.INSIDE);
            pieModelEventAssignedTo.setShowDataLabels(true);
            pieModelEventAssignedTo.setShadow(true);
            pieModelEventAssignedTo.setDataFormat(null);
        }
        pieModelEventAssignedTo.setTitle(chartDTO.getTitle());
    }

    public void createPieModelByEventPriority() {
        setPieModelEventPriority(new PieChartModel());
        ChartDTO chartDTO = cRMEventServiceDTO.getCountEventByPriority(getUserData().getUserlogin().getBranchId().getId(), getFromDate(), getToDate());
        int i = 0;
        for (Number pieValue : chartDTO.getPieValues()) {
            getPieModelEventPriority().set(chartDTO.getPieLabels().get(i), pieValue);
            i++;
        }
        if (chartDTO.getPieValues().isEmpty()) {
            getPieModelEventPriority().set("Empty chart", 0);
        } else {
            getPieModelEventPriority().setLegendPosition("s");
            getPieModelEventPriority().setLegendCols(1);
            getPieModelEventPriority().setLegendPlacement(LegendPlacement.INSIDE);
            getPieModelEventPriority().setShowDataLabels(true);
            getPieModelEventPriority().setShadow(true);
            getPieModelEventPriority().setDataFormat(null);
        }
        getPieModelEventPriority().setTitle(chartDTO.getTitle());

    }

    public void createPieModelByEventActivityType() {
        setPieModelEventActivityType(new PieChartModel());
        ChartDTO chartDTO = cRMEventServiceDTO.getCountEventByActivityType(getUserData().getUserlogin().getBranchId().getId(), getFromDate(), getToDate());
        int i = 0;
        for (Number pieValue : chartDTO.getPieValues()) {
            getPieModelEventActivityType().set(chartDTO.getPieLabels().get(i), pieValue);
            i++;
        }
        if (chartDTO.getPieValues().isEmpty()) {
            getPieModelEventActivityType().set("Empty chart", 0);
        } else {
            getPieModelEventActivityType().setLegendPosition("s");
            getPieModelEventActivityType().setLegendCols(1);
            getPieModelEventActivityType().setLegendPlacement(LegendPlacement.INSIDE);
            getPieModelEventActivityType().setShowDataLabels(true);
            getPieModelEventActivityType().setShadow(true);
            getPieModelEventActivityType().setDataFormat(null);
        }

        getPieModelEventActivityType().setTitle(chartDTO.getTitle());

    }

    public void filterSearch() {
        createPieModelByAssignedTo();
        createPieModelByAssignedToAndStatus();
        createPieModelByEventAssignedToAndStatus();
        createPieModelByEventAssignedTo();
        createBarModelEventAssignedToANDContract();
    }

    public void filterDashboardSearch() {
        createPieModelByLearnAboutCompany();
        createPieModelByleadStatus();
        createPieModelByProjectName();
        createPieModelByEventStatus();
        createPieModelByEventActivityType();
        createPieModelByEventPriority();

    }

    /**
     * @return the context
     */
    public ExternalContext getContext() {
        context = FacesContext.getCurrentInstance().getExternalContext();
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(ExternalContext context) {
        this.context = context;
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        if (userData == null) {
            setUserData((UserData) getContext().getSessionMap().get("userLogInData"));
        }
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the barModel
     */
    public BarChartModel getBarModel() {
        if (barModel == null) {
            barModel = new BarChartModel();
        }
        return barModel;
    }

    /**
     * @param barModel the barModel to set
     */
    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    /**
     * @return the chartDTO
     */
    public ChartDTO getChartDTO() {
        if (chartDTO == null) {
            chartDTO = new ChartDTO();
        }
        return chartDTO;
    }

    /**
     * @param chartDTO the chartDTO to set
     */
    public void setChartDTO(ChartDTO chartDTO) {
        this.chartDTO = chartDTO;
    }

    /**
     * @return the pieModelLearnAboutCompany
     */
    public PieChartModel getPieModelLearnAboutCompany() {
        return pieModelLearnAboutCompany;
    }

    /**
     * @param pieModelLearnAboutCompany the pieModelLearnAboutCompany to set
     */
    public void setPieModelLearnAboutCompany(PieChartModel pieModelLearnAboutCompany) {
        this.pieModelLearnAboutCompany = pieModelLearnAboutCompany;
    }

    /**
     * @return the pieModelLeadStatus
     */
    public PieChartModel getPieModelLeadStatus() {
        return pieModelLeadStatus;
    }

    /**
     * @param pieModelLeadStatus the pieModelLeadStatus to set
     */
    public void setPieModelLeadStatus(PieChartModel pieModelLeadStatus) {
        this.pieModelLeadStatus = pieModelLeadStatus;
    }

    /**
     * @return the pieModelProjectName
     */
    public PieChartModel getPieModelProjectName() {
        return pieModelProjectName;
    }

    /**
     * @param pieModelProjectName the pieModelProjectName to set
     */
    public void setPieModelProjectName(PieChartModel pieModelProjectName) {
        this.pieModelProjectName = pieModelProjectName;
    }

    /**
     * @return the pieModelEventStatus
     */
    public PieChartModel getPieModelEventStatus() {
        return pieModelEventStatus;
    }

    /**
     * @param pieModelEventStatus the pieModelEventStatus to set
     */
    public void setPieModelEventStatus(PieChartModel pieModelEventStatus) {
        this.pieModelEventStatus = pieModelEventStatus;
    }

    /**
     * @return the pieModelEventPriority
     */
    public PieChartModel getPieModelEventPriority() {
        return pieModelEventPriority;
    }

    /**
     * @param pieModelEventPriority the pieModelEventPriority to set
     */
    public void setPieModelEventPriority(PieChartModel pieModelEventPriority) {
        this.pieModelEventPriority = pieModelEventPriority;
    }

    /**
     * @return the pieModelEventActivityType
     */
    public PieChartModel getPieModelEventActivityType() {
        return pieModelEventActivityType;
    }

    /**
     * @param pieModelEventActivityType the pieModelEventActivityType to set
     */
    public void setPieModelEventActivityType(PieChartModel pieModelEventActivityType) {
        this.pieModelEventActivityType = pieModelEventActivityType;
    }

    /**
     * @return the pieModelAssignedTo
     */
    public PieChartModel getPieModelAssignedTo() {
        return pieModelAssignedTo;
    }

    /**
     * @param pieModelAssignedTo the pieModelAssignedTo to set
     */
    public void setPieModelAssignedTo(PieChartModel pieModelAssignedTo) {
        this.pieModelAssignedTo = pieModelAssignedTo;
    }

    /**
     * @return the pieModelAssignedToAndStatus
     */
    public PieChartModel getPieModelAssignedToAndStatus() {
        return pieModelAssignedToAndStatus;
    }

    /**
     * @param pieModelAssignedToAndStatus the pieModelAssignedToAndStatus to set
     */
    public void setPieModelAssignedToAndStatus(PieChartModel pieModelAssignedToAndStatus) {
        this.pieModelAssignedToAndStatus = pieModelAssignedToAndStatus;
    }

    /**
     * @return the status
     */
    public SymbolDTO getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(SymbolDTO status) {
        this.status = status;
    }

    /**
     * @return the pieModelEventAssignedToAndStatus
     */
    public PieChartModel getPieModelEventAssignedToAndStatus() {
        return pieModelEventAssignedToAndStatus;
    }

    /**
     * @param pieModelEventAssignedToAndStatus the
     * pieModelEventAssignedToAndStatus to set
     */
    public void setPieModelEventAssignedToAndStatus(PieChartModel pieModelEventAssignedToAndStatus) {
        this.pieModelEventAssignedToAndStatus = pieModelEventAssignedToAndStatus;
    }

    /**
     * @return the eventStatus
     */
    public SymbolDTO getEventStatus() {
        return eventStatus;
    }

    /**
     * @param eventStatus the eventStatus to set
     */
    public void setEventStatus(SymbolDTO eventStatus) {
        this.eventStatus = eventStatus;
    }

    /**
     * @return the pieModelEventAssignedTo
     */
    public PieChartModel getPieModelEventAssignedTo() {
        return pieModelEventAssignedTo;
    }

    /**
     * @param pieModelEventAssignedTo the pieModelEventAssignedTo to set
     */
    public void setPieModelEventAssignedTo(PieChartModel pieModelEventAssignedTo) {
        this.pieModelEventAssignedTo = pieModelEventAssignedTo;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the barModelEventAssignedTo
     */
    public BarChartModel getBarModelEventAssignedTo() {
        return barModelEventAssignedTo;
    }

    /**
     * @param barModelEventAssignedTo the barModelEventAssignedTo to set
     */
    public void setBarModelEventAssignedTo(BarChartModel barModelEventAssignedTo) {
        this.barModelEventAssignedTo = barModelEventAssignedTo;
    }

    /**
     * @return the numberOfLeads
     */
    public Integer getNumberOfLeads() {
        numberOfLeads = organizationSiteService.getNumberOfCustomerByBranchId(getUserData().getUserlogin().getBranchId().getId(), null, null, getFromDate(), getToDate());
        return numberOfLeads;
    }

    /**
     * @param numberOfLeads the numberOfLeads to set
     */
    public void setNumberOfLeads(Integer numberOfLeads) {
        this.numberOfLeads = numberOfLeads;
    }

    /**
     * @return the numberOfNewLeads
     */
    public Integer getNumberOfNewLeads() {
        numberOfNewLeads = organizationSiteService.getNumberOfNewCustomerByBranchId(getUserData().getUserlogin().getBranchId().getId(), null, null, getFromDate(), getToDate());
        return numberOfNewLeads;
    }

    /**
     * @param numberOfNewLeads the numberOfNewLeads to set
     */
    public void setNumberOfNewLeads(Integer numberOfNewLeads) {
        this.numberOfNewLeads = numberOfNewLeads;
    }

    /**
     * @return the barModelContract
     */
    public BarChartModel getBarModelContract() {
        return barModelContract;
    }

    /**
     * @param barModelContract the barModelContract to set
     */
    public void setBarModelContract(BarChartModel barModelContract) {
        this.barModelContract = barModelContract;
    }

}
