/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.InstProjectGuideService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.CollectionAndPaymentServiceReport;

import com.isag.businessservice.reports.searchBean.CollectionAndPaymentReportSearchBean;

import com.isag.converter.InstProjectGuideDTOConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.GlBankTransactionDTO;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.isag.AutoComplete;

import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.CollectionAndPaymentReportEntity;
import com.isag.report.entity.GLBankEntity;

import com.isag.views.CollectionPaymentView;
import com.isag.views.CollectionPaymentTotalsTemp;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.collections.ListUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author hhhh
 */
@Named("collectionAndPaymentReportMB")
@ViewScoped
public class CollectionAndPaymentReportMB extends BaseReportBean {

    // Objects
    private Integer branchId;

    BigDecimal remain = BigDecimal.ZERO;
    BigDecimal payee = BigDecimal.ZERO;
    private BigDecimal allAmount;
    private BigDecimal allRemain;
    private BigDecimal allPayee;

    private BigDecimal totalNet;
    private BigDecimal totalRemain;
    private BigDecimal totalPayee;

    private String uri;

    private String screenMode;
    private List<CollectionAndPaymentReportEntity> collectionAndPaymentReportEntityList;
    private LazyDataModel<CollectionPaymentTotalsTemp> dataModel;

    private CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean;

    private List<CollectionPaymentView> collectionPaymentViewList = new ArrayList<>();
    private List<GlBankTransactionDTO> collectionPaymentDetailViewList = new ArrayList<>();

    private List<InvOrganizationSiteDTO> invOrgSiteList;
    private InvOrganizationSiteDTOConverter invOrgSiteConverter;
    private List<InvOrganizationSiteDTO> invOrgSiteToList;
    private InvOrganizationSiteDTOConverter invOrgSiteToConverter;
    private List<InstProjectGuideDTO> instProjectGuideList;
    private InstProjectGuideDTOConverter instProjectGuideConverter;
    private Map<Integer, Map<Integer, BigDecimal>> collectionPaymentmap;
    private Map<Integer, Map<Integer, BigDecimal>> collectionAmountPaymentmap;
    private Map<Integer, Map<Integer, BigDecimal>> collectionRemainPaymentmap;
    private Map<Integer, List<CollectionAndPaymentReportEntity>> collectionAndPaymentBeanMap;
    private Map<Integer, Map<Integer, List<CollectionAndPaymentReportEntity>>> orderedCollectionAndPaymentMap;
    private String totalValue;
    private AutoComplete autoComplete;
    private List<CollectionPaymentView> CollectionPaymentView;
    private List<CollectionPaymentTotalsTemp> temp;
    private List<CollectionPaymentTotalsTemp> temp_report;
    private boolean search = false;

    @EJB
    private CollectionAndPaymentServiceReport collectionAndPaymentServiceReport;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InstProjectGuideService instProjectGuideService;

    @PostConstruct
    private void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        setBranchId(getUserData().getUserBranch().getId());

        setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());

        reset();
        load();
    }

    @Override
    public void load() {
//        getInvOrgSiteList();
//        getInstProjectGuideList();
    }

    @Override
    public void reset() {
        collectionAndPaymentReportEntityList = new ArrayList<>();
        collectionAndPaymentReportSearchBean = new CollectionAndPaymentReportSearchBean();
        orderedCollectionAndPaymentMap = new HashMap<>();
        setCollectionPaymentViewList(new ArrayList<>());
//        instProjectGuideList = new ArrayList<>();
        collectionPaymentViewList = new ArrayList<>();
        collectionAndPaymentBeanMap = new HashMap<>();
        setCollectionPaymentmap(new HashMap<>());
        totalValue = new String();
        setAllAmount(BigDecimal.ZERO);
        setAllPayee(BigDecimal.ZERO);
        setAllRemain(BigDecimal.ZERO);
    }

    public void showItemReport() {
        invOrgSiteList = new ArrayList<>();
        instProjectGuideList = new ArrayList<>();
        search();
    }

    public void IntializeSearchLab() {
        CollectionPaymentView = null;
        collectionPaymentDetailViewList = null;
        collectionPaymentViewList = null;
        System.gc();
        collectionPaymentViewList = new ArrayList<>();
        collectionAndPaymentReportEntityList = new ArrayList<>();
        collectionPaymentDetailViewList = new ArrayList<>();
        totalValue = new String();
        setCollectionPaymentmap(new HashMap<>());
        collectionAmountPaymentmap = new HashMap<>();
        collectionRemainPaymentmap = new HashMap<>();
        collectionAndPaymentBeanMap = new HashMap<>();
        orderedCollectionAndPaymentMap = new HashMap<>();
        search = false;
    }

    @Override
    public void search() {
        IntializeSearchLab();
        collectionAndPaymentReportSearchBean.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
        collectionPaymentViewList = collectionAndPaymentServiceReport.getAllCollectionPaymentReportSearchBean(collectionAndPaymentReportSearchBean);
        CollectionPaymentView = collectionAndPaymentServiceReport.getCollectionPaymentTotals(collectionAndPaymentReportSearchBean);
        collectionPaymentDetailViewList = collectionAndPaymentServiceReport.getAllTransactionsDTO(collectionAndPaymentReportSearchBean);
        if (CollectionPaymentView != null && !CollectionPaymentView.isEmpty()) {
            for (CollectionPaymentView instView : CollectionPaymentView) {

                if (!collectionPaymentmap.containsKey(instView.getSerial())) {
                    Map<Integer, BigDecimal> map = new HashMap<>();
                    map.put(instView.getCustomerId(), instView.getDueValue());
                    getCollectionPaymentmap().put(instView.getSerial(), map);
                } else {
                    Map<Integer, BigDecimal> map = (Map<Integer, BigDecimal>) getCollectionPaymentmap().get(instView.getSerial());
                    map.put(instView.getCustomerId(), instView.getDueValue());
                    getCollectionPaymentmap().put(instView.getSerial(), map);
                }

            }
        }
        if (collectionPaymentViewList != null && !collectionPaymentViewList.isEmpty()) {
            BigDecimal total = BigDecimal.ZERO;
            allAmount = BigDecimal.ZERO;
            allPayee = BigDecimal.ZERO;
            allRemain = BigDecimal.ZERO;

            for (CollectionPaymentView instView : collectionPaymentViewList) {

                if (orderedCollectionAndPaymentMap != null) {

                    if (orderedCollectionAndPaymentMap.containsKey(instView.getProjectId())) {

                        Map<Integer, List<CollectionAndPaymentReportEntity>> mapOfCustomers = orderedCollectionAndPaymentMap.get(instView.getProjectId());
                        List<CollectionAndPaymentReportEntity> BeanList = new ArrayList<>();
                        Map<Integer, List<CollectionAndPaymentReportEntity>> map = new HashMap<>();
                        totalNet = BigDecimal.ZERO;
                        totalRemain = BigDecimal.ZERO;
                        totalPayee = BigDecimal.ZERO;

                        CollectionAndPaymentReportEntity instBean = new CollectionAndPaymentReportEntity();
                        instBean.setSerial(instView.getSerial());
                        instBean.setValue(instView.getValue());
                        instBean.setOrgId(instView.getCustomerId());
                        instBean.setProgId(instView.getProjectId());
                        instBean.setProgName(instView.getProjectName());
                        instBean.setDueDate(instView.getDueDate());
                        //-------------------
                        //instBean.setPhone(instView.getCustomerPhone());
                        if (instView.getStatus() == null) {
                            instBean.setStatus("لم يسدد");
                        } else if (instView.getStatus() == 0) {
                            instBean.setStatus("لم يسدد");
                        } else if (instView.getStatus() == 2) {
                            instBean.setStatus("مسدد جزء منه");
                        } else if (instView.getStatus() == 1) {
                            instBean.setStatus("تم السداد");
                        }
                        instBean.setDueamount(instView.getDueValue());
                        for (GlBankTransactionDTO detail : collectionPaymentDetailViewList) {
                            if (Objects.equals(detail.getInstallmentId(), instView.getDueDateId())) {
                                GLBankEntity bankEntity = new GLBankEntity();
                                bankEntity.setPayee(detail.getValue() != null ? detail.getValue() : BigDecimal.ZERO);
                                bankEntity.setPaymentDate(detail.getDate());
                                instBean.setPaymentDate(detail.getDate() != null ? detail.getDate() : new Date());
                                instBean.setPayee(detail.getValue().add((instBean.getPayee() != null) ? instBean.getPayee() : BigDecimal.ZERO));

                                instBean.getGLBankEntityList().add(bankEntity);
                            }
                        }

                        instBean.setRemain((instBean.getDueamount() != null ? instBean.getDueamount() : BigDecimal.ZERO).subtract((instBean.getPayee() != null ? instBean.getPayee() : BigDecimal.ZERO)));
                        //-------------------
                        totalPayee = totalPayee.add(instBean.getPayee() != null ? instBean.getPayee() : BigDecimal.ZERO);
                        totalRemain = totalRemain.add(instBean.getRemain() != null ? instBean.getRemain() : BigDecimal.ZERO);
                        totalNet = totalNet.add(instBean.getDueamount() != null ? instBean.getDueamount() : BigDecimal.ZERO);
                        instBean.setPayee(totalPayee);
                        instBean.setRemain(totalRemain);

                        if (!mapOfCustomers.containsKey(instView.getCustomerId())) {
                            map.put(instView.getCustomerId(), new ArrayList<>());
                            BeanList = map.get(instView.getCustomerId());

                            CollectionAndPaymentReportEntity collectionEntity = new CollectionAndPaymentReportEntity();
                            collectionEntity.setOrgName(instView.getCustomerName());
                            collectionEntity.setSerial(instView.getSerial());
                            collectionEntity.setOrgId(instView.getCustomerId());
                            collectionEntity.setProgId(instView.getProjectId());
                            collectionEntity.setProgName(instView.getProjectName());
                            collectionEntity.setRemain(instBean.getRemain());
                            collectionEntity.setDueamount(instBean.getDueamount());
                            collectionEntity.setPayee(instBean.getPayee());
                            collectionEntity.setPhone(instView.getCustomerPhone());
                            Map<Integer, BigDecimal> totalAmountMap = collectionPaymentmap.get(instView.getProjectId());
//                            collectionEntity.setDueamount(totalAmountMap.get(instView.getCustomerId()));
                            BeanList.add(collectionEntity);
                        } else {
                            BeanList = mapOfCustomers.get(instView.getCustomerId());
                            CollectionAndPaymentReportEntity customer = BeanList.get(0);
                            customer.setPayee(customer.getPayee().add(instBean.getPayee()));
                            customer.setRemain(customer.getRemain().add(instBean.getRemain()));
                            customer.setDueamount(customer.getDueamount().add(instBean.getDueamount()));
                        }

                        BeanList.add(instBean);
                        mapOfCustomers.put(instView.getCustomerId(), BeanList);
                        orderedCollectionAndPaymentMap.put(instView.getProjectId(), mapOfCustomers);
                    } else {
                        totalNet = BigDecimal.ZERO;
                        totalRemain = BigDecimal.ZERO;
                        totalPayee = BigDecimal.ZERO;
                        List<CollectionAndPaymentReportEntity> entityList = new ArrayList<>();
                        Map<Integer, List<CollectionAndPaymentReportEntity>> map = new HashMap<>();
                        map.put(instView.getCustomerId(), new ArrayList<>());
                        entityList = map.get(instView.getCustomerId());

                        CollectionAndPaymentReportEntity entityBean = new CollectionAndPaymentReportEntity();
                        entityBean.setSerial(instView.getSerial());
                        entityBean.setProgId(instView.getProjectId());
                        entityBean.setProgName(instView.getProjectName());
                        entityBean.setDueDate(instView.getDueDate());
                        entityBean.setTotalValue(instView.getValue());
                        //-------------------
                        // entityBean.setPhone(instView.getCustomerPhone());
                        if (instView.getStatus() == null) {
                            entityBean.setStatus("لم يسدد");
                        } else if (instView.getStatus() == 0) {
                            entityBean.setStatus("لم يسدد");
                        } else if (instView.getStatus() == 2) {
                            entityBean.setStatus("مسدد جزء منه");
                        } else if (instView.getStatus() == 1) {
                            entityBean.setStatus("تم السداد");
                        }
                        entityBean.setDueamount(instView.getDueValue());
                        for (GlBankTransactionDTO detail : collectionPaymentDetailViewList) {
                            if (Objects.equals(detail.getInstallmentId(), instView.getDueDateId())) {
                                GLBankEntity bankEntity = new GLBankEntity();
                                bankEntity.setPayee(detail.getValue() != null ? detail.getValue() : BigDecimal.ZERO);
                                bankEntity.setPaymentDate(detail.getDate());
                                entityBean.setPaymentDate(detail.getDate() != null ? detail.getDate() : new Date());
                                entityBean.setPayee(detail.getValue().add((entityBean.getPayee() != null) ? entityBean.getPayee() : BigDecimal.ZERO));
                                entityBean.getGLBankEntityList().add(bankEntity);
                            }
                        }

                        entityBean.setRemain((entityBean.getDueamount() != null ? entityBean.getDueamount() : BigDecimal.ZERO).subtract((entityBean.getPayee() != null ? entityBean.getPayee() : BigDecimal.ZERO)));

                        CollectionAndPaymentReportEntity collectionEntity = new CollectionAndPaymentReportEntity();
                        collectionEntity.setOrgName(instView.getCustomerName());
                        collectionEntity.setProgId(instView.getProjectId());
                        collectionEntity.setProgName(instView.getProjectName());
                        collectionEntity.setDueamount(entityBean.getDueamount());
                        collectionEntity.setPayee(entityBean.getPayee());
                        collectionEntity.setRemain(entityBean.getRemain());
                        collectionEntity.setPhone(instView.getCustomerPhone());

                        Map<Integer, BigDecimal> totalMap = collectionPaymentmap.get(instView.getProjectId());
                        //  collectionEntity.setDueamount(totalMap.get(instView.getCustomerId()));
//                        collectionEntity.setPayee(totalMap.get(instView.getCustomerId()));
//                        collectionEntity.setRemain(totalMap.get(instView.getCustomerId()));
                        entityList.add(collectionEntity);

                        //-------------------
                        totalPayee = totalPayee.add(entityBean.getPayee() != null ? entityBean.getPayee() : BigDecimal.ZERO);
                        totalRemain = totalRemain.add(entityBean.getRemain() != null ? entityBean.getRemain() : BigDecimal.ZERO);
                        totalNet = totalNet.add(entityBean.getDueamount() != null ? entityBean.getDueamount() : BigDecimal.ZERO);
                        //     entityBean.setDueamount(totalNet);
                        entityBean.setPayee(totalPayee);
                        entityBean.setRemain(totalRemain);
                        entityList.add(entityBean);
                        map.put(instView.getCustomerId(), entityList);
                        orderedCollectionAndPaymentMap.put(instView.getProjectId(), map);
                    }
                }
                total = total.add(instView.getValue());
                allAmount = allAmount.add(totalNet);
                allPayee = allPayee.add(totalPayee);
                allRemain = allRemain.add(totalRemain);
                DecimalFormat df = new DecimalFormat("#,##0.00");
                totalValue = df.format(total);
            }
        }

        for (Map.Entry<Integer, Map<Integer, List<CollectionAndPaymentReportEntity>>> entry : orderedCollectionAndPaymentMap.entrySet()) {
            Map<Integer, List<CollectionAndPaymentReportEntity>> map = entry.getValue();
            for (Map.Entry<Integer, List<CollectionAndPaymentReportEntity>> entry1 : map.entrySet()) {
                List<CollectionAndPaymentReportEntity> asd = entry1.getValue();
                collectionAndPaymentReportEntityList = ListUtils.union(collectionAndPaymentReportEntityList, asd);
            }
        }
    }
//
//    public void search2() {
//        IntializeSearchLab();
//        setDataModel(new LazyDataModel<CollectionAndPaymentReportEntity>() {
//            private static final long serialVersionUID = 1L;
// 
//            @Override
//            public List<CollectionAndPaymentReportEntity> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
//               
//                collectionAndPaymentReportSearchBean.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
//
//                collectionPaymentViewList = collectionAndPaymentServiceReport.getAllCollectionPaymentReportSearchBeanLazey(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), collectionAndPaymentReportSearchBean, filters);
//             
//                CollectionPaymentView = collectionAndPaymentServiceReport.getCollectionPaymentTotals(collectionAndPaymentReportSearchBean);
//
//                setRowCount(collectionAndPaymentServiceReport.getTotalRegistors(collectionAndPaymentReportSearchBean, filters).intValue());
//                collectionPaymentDetailViewList = collectionAndPaymentServiceReport.getAllTransactionsLazyDTO(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), collectionAndPaymentReportSearchBean, filters);
//
//                if (CollectionPaymentView != null && !CollectionPaymentView.isEmpty()) {
//                    for (CollectionPaymentView instView : CollectionPaymentView) {
//
//                        if (!collectionPaymentmap.containsKey(instView.getSerial())) {
//                            Map<Integer, BigDecimal> map = new HashMap<>();
//                            map.put(instView.getCustomerId(), instView.getDueValue());
//                            getCollectionPaymentmap().put(instView.getSerial(), map);
//                        } else {
//                            Map<Integer, BigDecimal> map = (Map<Integer, BigDecimal>) getCollectionPaymentmap().get(instView.getSerial());
//                            map.put(instView.getCustomerId(), instView.getDueValue());
//                            getCollectionPaymentmap().put(instView.getSerial(), map);
//                        }
//
//                    }
//                }
//                if (collectionPaymentViewList != null && !collectionPaymentViewList.isEmpty()) {
//                    BigDecimal total = BigDecimal.ZERO;
//                    allAmount = BigDecimal.ZERO;
//                    allPayee = BigDecimal.ZERO;
//                    allRemain = BigDecimal.ZERO;
//
//                    for (CollectionPaymentView instView : collectionPaymentViewList) {
//
//                        if (orderedCollectionAndPaymentMap != null) {
//
//                            if (orderedCollectionAndPaymentMap.containsKey(instView.getProjectId())) {
//
//                                Map<Integer, List<CollectionAndPaymentReportEntity>> mapOfCustomers = orderedCollectionAndPaymentMap.get(instView.getProjectId());
//                                List<CollectionAndPaymentReportEntity> BeanList = new ArrayList<>();
//                                Map<Integer, List<CollectionAndPaymentReportEntity>> map = new HashMap<>();
//                                totalNet = BigDecimal.ZERO;
//                                totalRemain = BigDecimal.ZERO;
//                                totalPayee = BigDecimal.ZERO;
//
//                                CollectionAndPaymentReportEntity instBean = new CollectionAndPaymentReportEntity();
//                                instBean.setSerial(instView.getSerial());
//                                instBean.setValue(instView.getValue());
//                                instBean.setOrgId(instView.getCustomerId());
//                                instBean.setProgId(instView.getProjectId());
//                                instBean.setProgName(instView.getProjectName());
//                                instBean.setDueDate(instView.getDueDate());
//                                //-------------------
//                                //instBean.setPhone(instView.getCustomerPhone());
//                                if (instView.getStatus() == null) {
//                                    instBean.setStatus("لم يسدد");
//                                } else if (instView.getStatus() == 0) {
//                                    instBean.setStatus("لم يسدد");
//                                } else if (instView.getStatus() == 2) {
//                                    instBean.setStatus("مسدد جزء منه");
//                                } else if (instView.getStatus() == 1) {
//                                    instBean.setStatus("تم السداد");
//                                }
//                                instBean.setDueamount(instView.getDueValue());
//                                for (GlBankTransactionDTO detail : collectionPaymentDetailViewList) {
//                                    if (Objects.equals(detail.getInstallmentId(), instView.getDueDateId())) {
//                                        GLBankEntity bankEntity = new GLBankEntity();
//                                        bankEntity.setPayee(detail.getValue() != null ? detail.getValue() : BigDecimal.ZERO);
//                                        bankEntity.setPaymentDate(detail.getDate());
//                                        instBean.setPaymentDate(detail.getDate() != null ? detail.getDate() : new Date());
//                                        instBean.setPayee(detail.getValue().add((instBean.getPayee() != null) ? instBean.getPayee() : BigDecimal.ZERO));
//
//                                        instBean.getGLBankEntityList().add(bankEntity);
//                                    }
//                                }
//
//                                instBean.setRemain((instBean.getDueamount() != null ? instBean.getDueamount() : BigDecimal.ZERO).subtract((instBean.getPayee() != null ? instBean.getPayee() : BigDecimal.ZERO)));
//                                //-------------------
//                                totalPayee = totalPayee.add(instBean.getPayee() != null ? instBean.getPayee() : BigDecimal.ZERO);
//                                totalRemain = totalRemain.add(instBean.getRemain() != null ? instBean.getRemain() : BigDecimal.ZERO);
//                                totalNet = totalNet.add(instBean.getDueamount() != null ? instBean.getDueamount() : BigDecimal.ZERO);
//                                instBean.setPayee(totalPayee);
//                                instBean.setRemain(totalRemain);
//
//                                if (!mapOfCustomers.containsKey(instView.getCustomerId())) {
//                                    map.put(instView.getCustomerId(), new ArrayList<>());
//                                    BeanList = map.get(instView.getCustomerId());
//
//                                    CollectionAndPaymentReportEntity collectionEntity = new CollectionAndPaymentReportEntity();
//                                    collectionEntity.setOrgName(instView.getCustomerName());
//                                    collectionEntity.setSerial(instView.getSerial());
//                                    collectionEntity.setOrgId(instView.getCustomerId());
//                                    collectionEntity.setProgId(instView.getProjectId());
//                                    collectionEntity.setProgName(instView.getProjectName());
//                                    collectionEntity.setRemain(instBean.getRemain());
//                                    collectionEntity.setDueamount(instBean.getDueamount());
//                                    collectionEntity.setPayee(instBean.getPayee());
//                                    collectionEntity.setPhone(instView.getCustomerPhone());
//                                    Map<Integer, BigDecimal> totalAmountMap = collectionPaymentmap.get(instView.getProjectId());
////                            collectionEntity.setDueamount(totalAmountMap.get(instView.getCustomerId()));
//                                    BeanList.add(collectionEntity);
//                                } else {
//                                    BeanList = mapOfCustomers.get(instView.getCustomerId());
//                                    CollectionAndPaymentReportEntity customer = BeanList.get(0);
//                                    customer.setPayee(customer.getPayee().add(instBean.getPayee()));
//                                    customer.setRemain(customer.getRemain().add(instBean.getRemain()));
//                                    customer.setDueamount(customer.getDueamount().add(instBean.getDueamount()));
//                                }
//
//                                BeanList.add(instBean);
//                                mapOfCustomers.put(instView.getCustomerId(), BeanList);
//                                orderedCollectionAndPaymentMap.put(instView.getProjectId(), mapOfCustomers);
//                            } else {
//                                totalNet = BigDecimal.ZERO;
//                                totalRemain = BigDecimal.ZERO;
//                                totalPayee = BigDecimal.ZERO;
//                                List<CollectionAndPaymentReportEntity> entityList = new ArrayList<>();
//                                Map<Integer, List<CollectionAndPaymentReportEntity>> map = new HashMap<>();
//                                map.put(instView.getCustomerId(), new ArrayList<>());
//                                entityList = map.get(instView.getCustomerId());
//
//                                CollectionAndPaymentReportEntity entityBean = new CollectionAndPaymentReportEntity();
//                                entityBean.setSerial(instView.getSerial());
//                                entityBean.setProgId(instView.getProjectId());
//                                entityBean.setProgName(instView.getProjectName());
//                                entityBean.setDueDate(instView.getDueDate());
//                                entityBean.setTotalValue(instView.getValue());
//                                //-------------------
//                                // entityBean.setPhone(instView.getCustomerPhone());
//                                if (instView.getStatus() == null) {
//                                    entityBean.setStatus("لم يسدد");
//                                } else if (instView.getStatus() == 0) {
//                                    entityBean.setStatus("لم يسدد");
//                                } else if (instView.getStatus() == 2) {
//                                    entityBean.setStatus("مسدد جزء منه");
//                                } else if (instView.getStatus() == 1) {
//                                    entityBean.setStatus("تم السداد");
//                                }
//                                entityBean.setDueamount(instView.getDueValue());
//                                for (GlBankTransactionDTO detail : collectionPaymentDetailViewList) {
//                                    if (Objects.equals(detail.getInstallmentId(), instView.getDueDateId())) {
//                                        GLBankEntity bankEntity = new GLBankEntity();
//                                        bankEntity.setPayee(detail.getValue() != null ? detail.getValue() : BigDecimal.ZERO);
//                                        bankEntity.setPaymentDate(detail.getDate());
//                                        entityBean.setPaymentDate(detail.getDate() != null ? detail.getDate() : new Date());
//                                        entityBean.setPayee(detail.getValue().add((entityBean.getPayee() != null) ? entityBean.getPayee() : BigDecimal.ZERO));
//                                        entityBean.getGLBankEntityList().add(bankEntity);
//                                    }
//                                }
//
//                                entityBean.setRemain((entityBean.getDueamount() != null ? entityBean.getDueamount() : BigDecimal.ZERO).subtract((entityBean.getPayee() != null ? entityBean.getPayee() : BigDecimal.ZERO)));
//
//                                CollectionAndPaymentReportEntity collectionEntity = new CollectionAndPaymentReportEntity();
//                                collectionEntity.setOrgName(instView.getCustomerName());
//                                collectionEntity.setProgId(instView.getProjectId());
//                                collectionEntity.setProgName(instView.getProjectName());
//                                collectionEntity.setDueamount(entityBean.getDueamount());
//                                collectionEntity.setPayee(entityBean.getPayee());
//                                collectionEntity.setRemain(entityBean.getRemain());
//                                collectionEntity.setPhone(instView.getCustomerPhone());
//
//                                Map<Integer, BigDecimal> totalMap = collectionPaymentmap.get(instView.getProjectId());
//                                //  collectionEntity.setDueamount(totalMap.get(instView.getCustomerId()));
////                        collectionEntity.setPayee(totalMap.get(instView.getCustomerId()));
////                        collectionEntity.setRemain(totalMap.get(instView.getCustomerId()));
//                                entityList.add(collectionEntity);
//
//                                //-------------------
//                                totalPayee = totalPayee.add(entityBean.getPayee() != null ? entityBean.getPayee() : BigDecimal.ZERO);
//                                totalRemain = totalRemain.add(entityBean.getRemain() != null ? entityBean.getRemain() : BigDecimal.ZERO);
//                                totalNet = totalNet.add(entityBean.getDueamount() != null ? entityBean.getDueamount() : BigDecimal.ZERO);
//                                //     entityBean.setDueamount(totalNet);
//                                entityBean.setPayee(totalPayee);
//                                entityBean.setRemain(totalRemain);
//                                entityList.add(entityBean);
//                                map.put(instView.getCustomerId(), entityList);
//                                orderedCollectionAndPaymentMap.put(instView.getProjectId(), map);
//                            }
//                        }
//                        total = total.add(instView.getValue());
//                        allAmount = allAmount.add(totalNet);
//                        allPayee = allPayee.add(totalPayee);
//                        allRemain = allRemain.add(totalRemain);
//                        DecimalFormat df = new DecimalFormat("#,##0.00");
//                        totalValue = df.format(total);
//                    }
//                }
//
//                for (Map.Entry<Integer, Map<Integer, List<CollectionAndPaymentReportEntity>>> entry : orderedCollectionAndPaymentMap.entrySet()) {
//                    Map<Integer, List<CollectionAndPaymentReportEntity>> map = entry.getValue();
//                    for (Map.Entry<Integer, List<CollectionAndPaymentReportEntity>> entry1 : map.entrySet()) {
//                        List<CollectionAndPaymentReportEntity> asd = entry1.getValue();
//                        collectionAndPaymentReportEntityList = ListUtils.union(collectionAndPaymentReportEntityList, asd);
//                    }
//                }
//
//                return collectionAndPaymentReportEntityList;
//            }
//        });
//
//       
//    }

    public void search3() {
        IntializeSearchLab();
        search = Boolean.TRUE;
        setDataModel(new LazyDataModel<CollectionPaymentTotalsTemp>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<CollectionPaymentTotalsTemp> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                collectionAndPaymentReportSearchBean.setUserId(getUserData().getUser().getId() != null ? getUserData().getUser().getId() : null);
                collectionAndPaymentReportSearchBean.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
                temp = collectionAndPaymentServiceReport.getAllCollectionPaymentReport(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), collectionAndPaymentReportSearchBean, filters, search);;
                setRowCount(collectionAndPaymentServiceReport.getTotalRegistorsTemp());
                allAmount = collectionAndPaymentServiceReport.getTotalAmountTemp();
                allRemain = collectionAndPaymentServiceReport.getTotalRemainTemp();
                allPayee = collectionAndPaymentServiceReport.getTotalPayTemp();
                search = Boolean.FALSE;
                return temp;
            }
        });

    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        fillReport(prepareReport(), getUserData().getReportPath() + "instCollectionAndPayment.jasper", collectionAndPaymentReportEntityList, "pdf");

    }

    public void exportPDF2(ActionEvent actionEvent) throws JRException, IOException {
        temp_report = collectionAndPaymentServiceReport.getAllCollectionPaymentReport(collectionAndPaymentReportSearchBean);
        fillReport(prepareReport(), getUserData().getReportPath() + "instCollectionAndPayment_1.jasper", temp_report, "pdf");

    }
/*
    @Override
    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        if ("pdf".equals(exportType)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            servletOutputStream.flush();
            servletOutputStream.close();
            httpServletResponse.getOutputStream().flush();
            httpServletResponse.getOutputStream().close();
        } else if ("excel".equals(exportType)) {

        } else if ("html".equals(exportType)) {

        }
    }*/

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("UserName", getUserData().getUser().getName());

        hashMap.put("customerFromText", userDDs.get("FROM_CUSTOM"));
        hashMap.put("customerToText", userDDs.get("TO_CUSTOM"));
        hashMap.put("yearFromText", userDDs.get("FROM_DATE"));
        hashMap.put("yearToText", userDDs.get("YEAR_TO"));
        hashMap.put("projectFromText", userDDs.get("FROM_PROJ"));
        hashMap.put("projectToText", userDDs.get("TO_PROJ"));
        hashMap.put("dueDateText", userDDs.get("DUE_DATE"));
        hashMap.put("paymentDateText", userDDs.get("PAYMENT_DATE"));
        hashMap.put("phoneText", userDDs.get("PHONE"));
        hashMap.put("dueamountText", userDDs.get("AMOUNT_DUE"));
        hashMap.put("remainText", userDDs.get("REMA_AMOUNT"));
        hashMap.put("payeeText", userDDs.get("AMOUNT_PAID"));
        hashMap.put("statusDetailText", userDDs.get("STATUS"));
        hashMap.put("customerNameText", userDDs.get("CUSTOM_NAME"));
        hashMap.put("projectNameText", userDDs.get("PROJ_NAME"));
        hashMap.put("statusText", userDDs.get("INSTALL_CASE"));
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("PrintedBy", getUserData().getUser().getName());

        hashMap.put("yearFromValue", collectionAndPaymentReportSearchBean.getFromDate());
        hashMap.put("yearToValue", collectionAndPaymentReportSearchBean.getToDate());
        hashMap.put("customerFromValue", collectionAndPaymentReportSearchBean.getOrgNameFrom() != null ? collectionAndPaymentReportSearchBean.getOrgNameFrom().getName() : "");
        hashMap.put("customerToValue", collectionAndPaymentReportSearchBean.getOrgNameTo() != null ? collectionAndPaymentReportSearchBean.getOrgNameTo().getName() : "");
        hashMap.put("statusValue", collectionAndPaymentReportSearchBean.getStatus());
        hashMap.put("projectFromValue", collectionAndPaymentReportSearchBean.getProjectGuideFrom() != null ? collectionAndPaymentReportSearchBean.getProjectGuideFrom().getName() : "");
        hashMap.put("projectToValue", collectionAndPaymentReportSearchBean.getProjectGuideTo() != null ? collectionAndPaymentReportSearchBean.getProjectGuideTo().getName() : "");
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("reportTitleText", userDDs.get("COLL_PAY_REP"));

        return hashMap;
    }

    public List<InvOrganizationSiteDTO> completOrgSite(String query) {
        return getAutoComplete().completOrgSiteDTO(query, getInvOrgSiteList(), invOrgSiteConverter);
    }

    public List<InstProjectGuideDTO> completProjectGuide(String query) {
        return getAutoComplete().completeInstProjectDTO(query, getInstProjectGuideList(), instProjectGuideConverter);

    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    /*  public Boolean getIsSales() {
     return isSales;
     }

     public void setIsSales(Boolean isSales) {
     this.isSales = isSales;
     } */
    /**
     * @return the branchId
     */
    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the invOrgSiteList
     */
    public List<InvOrganizationSiteDTO> getInvOrgSiteList() {
        invOrgSiteList = organizationSiteService.getCustomerByBranchIdForReportDTO(branchId);
        setInvOrgSiteConverter(new InvOrganizationSiteDTOConverter(invOrgSiteList));
        return invOrgSiteList;
    }

    /**
     * @param invOrgSiteList the invOrgSiteList to set
     */
    public void setInvOrgSiteList(List<InvOrganizationSiteDTO> invOrgSiteList) {
        this.invOrgSiteList = invOrgSiteList;
    }

    /**
     * @return the invOrgSiteConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrgSiteConverter() {
        return invOrgSiteConverter;
    }

    /**
     * @param invOrgSiteConverter the invOrgSiteConverter to set
     */
    public void setInvOrgSiteConverter(InvOrganizationSiteDTOConverter invOrgSiteConverter) {
        this.invOrgSiteConverter = invOrgSiteConverter;
    }

    /**
     * @return the organizationSiteService
     */
    public OrganizationSiteService getOrganizationSiteService() {
        return organizationSiteService;
    }

    /**
     * @param organizationSiteService the organizationSiteService to set
     */
    public void setOrganizationSiteService(OrganizationSiteService organizationSiteService) {
        this.organizationSiteService = organizationSiteService;
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
     * @return the collectionAndPaymentReportEntityList
     */
    public List<CollectionAndPaymentReportEntity> getCollectionAndPaymentReportEntityList() {
        return collectionAndPaymentReportEntityList;
    }

    /**
     * @param collectionAndPaymentReportEntityList the
     * collectionAndPaymentReportEntityList to set
     */
    public void setCollectionAndPaymentReportEntityList(List<CollectionAndPaymentReportEntity> collectionAndPaymentReportEntityList) {
        this.collectionAndPaymentReportEntityList = collectionAndPaymentReportEntityList;
    }

    /**
     * @return the collectionAndPaymentReportSearchBean
     */
    public CollectionAndPaymentReportSearchBean getCollectionAndPaymentReportSearchBean() {
        return collectionAndPaymentReportSearchBean;
    }

    /**
     * @param collectionAndPaymentReportSearchBean the
     * collectionAndPaymentReportSearchBean to set
     */
    public void setCollectionAndPaymentReportSearchBean(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        this.collectionAndPaymentReportSearchBean = collectionAndPaymentReportSearchBean;
    }

    /**
     * @return the instProjectGuideList
     */
    public List<InstProjectGuideDTO> getInstProjectGuideList() {
        instProjectGuideList = instProjectGuideService.getAllInstProjectGuidesDTO(branchId);
        setInstProjectGuideConverter(new InstProjectGuideDTOConverter(instProjectGuideList));
        return instProjectGuideList;
    }

    /**
     * @param instProjectGuideList the instProjectGuideList to set
     */
    public void setInstProjectGuideList(List<InstProjectGuideDTO> instProjectGuideList) {
        this.instProjectGuideList = instProjectGuideList;
    }

    /**
     * @return the instProjectGuideConverter
     */
    public InstProjectGuideDTOConverter getInstProjectGuideConverter() {
        return instProjectGuideConverter;
    }

    /**
     * @param instProjectGuideConverter the instProjectGuideConverter to set
     */
    public void setInstProjectGuideConverter(InstProjectGuideDTOConverter instProjectGuideConverter) {
        this.instProjectGuideConverter = instProjectGuideConverter;
    }

    /**
     * @return the collectionAndPaymentServiceReport
     */
    public CollectionAndPaymentServiceReport getCollectionAndPaymentServiceReport() {
        return collectionAndPaymentServiceReport;
    }

    /**
     * @param collectionAndPaymentServiceReport the
     * collectionAndPaymentServiceReport to set
     */
    public void setCollectionAndPaymentServiceReport(CollectionAndPaymentServiceReport collectionAndPaymentServiceReport) {
        this.collectionAndPaymentServiceReport = collectionAndPaymentServiceReport;
    }

    /**
     * @return the instProjectGuideService
     */
    public InstProjectGuideService getInstProjectGuideService() {
        return instProjectGuideService;
    }

    /**
     * @param instProjectGuideService the instProjectGuideService to set
     */
    public void setInstProjectGuideService(InstProjectGuideService instProjectGuideService) {
        this.instProjectGuideService = instProjectGuideService;
    }

    /**
     * @return the collectionPaymentViewList
     */
    public List<CollectionPaymentView> getCollectionPaymentViewList() {
        return collectionPaymentViewList;
    }

    /**
     * @param collectionPaymentViewList the collectionPaymentViewList to set
     */
    public void setCollectionPaymentViewList(List<CollectionPaymentView> collectionPaymentViewList) {
        this.collectionPaymentViewList = collectionPaymentViewList;
    }

    /**
     * @return the collectionPaymentDetailViewList
     */
    public List<GlBankTransactionDTO> getCollectionPaymentDetailViewList() {
        return collectionPaymentDetailViewList;
    }

    /**
     * @param collectionPaymentDetailViewList the
     * collectionPaymentDetailViewList to set
     */
    public void setCollectionPaymentDetailViewList(List<GlBankTransactionDTO> collectionPaymentDetailViewList) {
        this.collectionPaymentDetailViewList = collectionPaymentDetailViewList;
    }

    /**
     * @return the allAmount
     */
    public BigDecimal getAllAmount() {
        return allAmount;
    }

    /**
     * @param allAmount the allAmount to set
     */
    public void setAllAmount(BigDecimal allAmount) {
        this.allAmount = allAmount;
    }

    /**
     * @return the allRemain
     */
    public BigDecimal getAllRemain() {
        return allRemain;
    }

    /**
     * @param allRemain the allRemain to set
     */
    public void setAllRemain(BigDecimal allRemain) {
        this.allRemain = allRemain;
    }

    /**
     * @return the allPayee
     */
    public BigDecimal getAllPayee() {
        return allPayee;
    }

    /**
     * @param allPayee the allPayee to set
     */
    public void setAllPayee(BigDecimal allPayee) {
        this.allPayee = allPayee;
    }

    /**
     * @return the invOrgSiteToList
     */
    public List<InvOrganizationSiteDTO> getInvOrgSiteToList() {

        return invOrgSiteToList;
    }

    /**
     * @param invOrgSiteToList the invOrgSiteToList to set
     */
    public void setInvOrgSiteToList(List<InvOrganizationSiteDTO> invOrgSiteToList) {
        this.invOrgSiteToList = invOrgSiteToList;
    }

    /**
     * @return the invOrgSiteToConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrgSiteToConverter() {
        return invOrgSiteToConverter;
    }

    /**
     * @param invOrgSiteToConverter the invOrgSiteToConverter to set
     */
    public void setInvOrgSiteToConverter(InvOrganizationSiteDTOConverter invOrgSiteToConverter) {
        this.invOrgSiteToConverter = invOrgSiteToConverter;
    }

    /**
     * @return the collectionAndPaymentBeanMap
     */
    public Map<Integer, List<CollectionAndPaymentReportEntity>> getCollectionAndPaymentBeanMap() {
        return collectionAndPaymentBeanMap;
    }

    /**
     * @param collectionAndPaymentBeanMap the collectionAndPaymentBeanMap to set
     */
    public void setCollectionAndPaymentBeanMap(Map<Integer, List<CollectionAndPaymentReportEntity>> collectionAndPaymentBeanMap) {
        this.collectionAndPaymentBeanMap = collectionAndPaymentBeanMap;
    }

    /**
     * @return the totalValue
     */
    public String getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue the totalValue to set
     */
    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    /**
     * @return the orderedCollectionAndPaymentMap
     */
    public Map<Integer, Map<Integer, List<CollectionAndPaymentReportEntity>>> getOrderedCollectionAndPaymentMap() {
        return orderedCollectionAndPaymentMap;
    }

    /**
     * @param orderedCollectionAndPaymentMap the orderedCollectionAndPaymentMap
     * to set
     */
    public void setOrderedCollectionAndPaymentMap(Map<Integer, Map<Integer, List<CollectionAndPaymentReportEntity>>> orderedCollectionAndPaymentMap) {
        this.orderedCollectionAndPaymentMap = orderedCollectionAndPaymentMap;
    }

    /**
     * @return the collectionPaymentmap
     */
    public Map<Integer, Map<Integer, BigDecimal>> getCollectionPaymentmap() {
        return collectionPaymentmap;
    }

    /**
     * @param collectionPaymentmap the collectionPaymentmap to set
     */
    public void setCollectionPaymentmap(Map<Integer, Map<Integer, BigDecimal>> collectionPaymentmap) {
        this.collectionPaymentmap = collectionPaymentmap;
    }

    /**
     * @return the totalNet
     */
    public BigDecimal getTotalNet() {
        return totalNet;
    }

    /**
     * @param totalNet the totalNet to set
     */
    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

    /**
     * @return the totalRemain
     */
    public BigDecimal getTotalRemain() {
        return totalRemain;
    }

    /**
     * @param totalRemain the totalRemain to set
     */
    public void setTotalRemain(BigDecimal totalRemain) {
        this.totalRemain = totalRemain;
    }

    /**
     * @return the totalPayee
     */
    public BigDecimal getTotalPayee() {
        return totalPayee;
    }

    /**
     * @param totalPayee the totalPayee to set
     */
    public void setTotalPayee(BigDecimal totalPayee) {
        this.totalPayee = totalPayee;
    }

    /**
     * @return the autoComplete
     */
    public AutoComplete getAutoComplete() {
        if (autoComplete == null) {
            autoComplete = new AutoComplete();
        }
        return autoComplete;
    }

    /**
     * @param autoComplete the autoComplete to set
     */
    public void setAutoComplete(AutoComplete autoComplete) {
        this.autoComplete = autoComplete;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<CollectionPaymentTotalsTemp> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<CollectionPaymentTotalsTemp> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the temp
     */
    public List<CollectionPaymentTotalsTemp> getTemp() {
        return temp;
    }

    /**
     * @param temp the temp to set
     */
    public void setTemp(List<CollectionPaymentTotalsTemp> temp) {
        this.temp = temp;
    }

    /**
     * @return the temp_report
     */
    public List<CollectionPaymentTotalsTemp> getTemp_report() {
        return temp_report;
    }

    /**
     * @param temp_report the temp_report to set
     */
    public void setTemp_report(List<CollectionPaymentTotalsTemp> temp_report) {
        this.temp_report = temp_report;
    }

}
