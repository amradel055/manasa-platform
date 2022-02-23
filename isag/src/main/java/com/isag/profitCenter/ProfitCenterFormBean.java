/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.profitCenter;

import com.isag.bean.ProfitSearchBean;
import com.isag.businessservice.GlProfitCenterServiceImpl;
import com.isag.entity.CostCenter;
import com.isag.isag.BaseFormBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author hq002
 */
public class ProfitCenterFormBean extends BaseFormBean implements Serializable {

    private StringBuilder errorMessage;

    private CostCenter glprofitcenter;
    private CostCenter glprofitcenterParent;
    private CostCenter glprofitcenterSelected;
    private ProfitSearchBean profitSearchBean;
    private List<CostCenter> glprofitcenterListSearch;
    private List<CostCenter> glprofitcenterList;
    private Integer barcodeParent;
    private String nameParent;
    private boolean active;
    private TreeNode root;
    private TreeNode selectedNode;

    @EJB
    GlProfitCenterServiceImpl profitcenterFacadeREST;

    public ProfitCenterFormBean() {
    }

    @Override
    public String save() {
        try {
            onvalidate();
            if (getErrorMessage() == null || getErrorMessage().toString().isEmpty()) {
                if (isActive()) {
                    getGlprofitcenter().setActive(Boolean.TRUE);
                } else {
                    getGlprofitcenter().setActive(Boolean.FALSE);
                }
                profitcenterFacadeREST.addProfitCenter(getGlprofitcenter());
                reset();
            }
            return "";
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "save");
            return null;
        }
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            reset();
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            if (getGlprofitcenterSelected() != null) {
                setGlprofitcenter(getGlprofitcenterSelected());
                if (getGlprofitcenter().getActive().equals("1")) {
                    setActive(true);
                } else {
                    setActive(false);
                }
                if (getGlprofitcenter().getParent() != null) {
                    setNameParent(getGlprofitcenter().getParent().getName());
                    setBarcodeParent(getGlprofitcenter().getParent().getCode());
                }
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('profitSearch').hide();");
            }
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void reset() {
        try {
            setGlprofitcenter(new CostCenter());
            setGlprofitcenterParent(new CostCenter());
            setGlprofitcenterSelected(new CostCenter());
            setNameParent(new String());
            setBarcodeParent(new Integer(0));
            getGlprofitcenter().setLevel(new Integer(1));
            setGlprofitcenterList(new ArrayList<CostCenter>());
            setGlprofitcenterListSearch(new ArrayList<CostCenter>());
            setProfitSearchBean(new ProfitSearchBean());
            setErrorMessage(new StringBuilder());
            setRoot(new DefaultTreeNode("Root", null));
            setGlprofitcenterList(profitcenterFacadeREST.findAllprofitcenter());
            TreeNode profits = new DefaultTreeNode("مراكز التكلفة", getRoot());
            drawTree(getGlprofitcenterList(), profits);
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "reset");
        }
    }

    public void drawTree(List<CostCenter> glprofitcenterList, TreeNode node) {
        try {
            for (CostCenter parent : glprofitcenterList) {
                if (parent.getParent() == null) {
                    TreeNode parentNode = new DefaultTreeNode(parent.getName() + "-" + parent.getCode(), node);
                    drawChilds(parent.getId(), parentNode);
                }
            }
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "drawTree");
        }
    }

    public void drawChilds(int i, TreeNode node) {
        try {
            for (CostCenter child : getGlprofitcenterList()) {
                if (child.getParent() != null) {
                    if (child.getParent().getId() == i) {
                        TreeNode childNode = new DefaultTreeNode(child.getName() + "-" + child.getCode(), node);
                        drawChilds(child.getId(), childNode);
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "drawChilds");
        }
    }

    public void onvalidate() {
        try {
            if (getGlprofitcenter().getCode() == null || !profitcenterFacadeREST.validatecodeprofitcenter(getGlprofitcenter().getCode())) {

                getErrorMessage().append("رقم المختصر موجود مسبقا");

                getErrorMessage().append("\n");
            }
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "onvalidate");
        }
    }

    public void onSelectTree(NodeSelectEvent event) {
        try {
            if (event.getTreeNode().getData() != null) {
                String barCode = (String) event.getTreeNode().getData();
                String[] barString = barCode.split("-");
                setGlprofitcenter(profitcenterFacadeREST.findParentprofitcenter(Integer.parseInt(barString[1])));
                if (getGlprofitcenter().getActive() != null && getGlprofitcenter().getActive().equals("1")) {
                    setActive(true);
                } else {
                    setActive(false);
                }
                if (getGlprofitcenter().getParent() != null) {
                    setNameParent(getGlprofitcenter().getParent().getName());
                    setBarcodeParent(getGlprofitcenter().getParent().getCode());
                }
            }
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "onSelectTree");
        }
    }

    public void onUnSelectTree(NodeUnselectEvent event) {
        try {
            reset();
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "onUnSelectTree");
        }
    }

    public void find() {
        try {
            profitcenterFacadeREST.findProfitCenter(getGlprofitcenter().getId());
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "find");
        }
    }

    public void update() {
        try {
            if (isActive()) {
                getGlprofitcenter().setActive(Boolean.TRUE);
            } else {
                getGlprofitcenter().setActive(Boolean.FALSE);
            }
            profitcenterFacadeREST.updateProfitCenter(getGlprofitcenter());
            FacesContext context = FacesContext.getCurrentInstance();
            reset();
            context.addMessage(null, new FacesMessage("Error", "تم التحديث بنجاح"));
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "update");
        }
    }

    public void delete() {
        try {
            profitcenterFacadeREST.updateProfitCenter(getGlprofitcenter());
            reset();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error", "تم الحذف بنجاح"));
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "delete");
        }
    }

    public void search() {
        try {
            setGlprofitcenterListSearch(new ArrayList<CostCenter>());
            if (getProfitSearchBean().getCode() != null) {
                int search = getProfitSearchBean().getCode();
                for (CostCenter glprofitcenter : getGlprofitcenterList()) {
                    if (glprofitcenter.getCode() == search) {
                        getGlprofitcenterListSearch().add(glprofitcenter);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "search");
        }
    }

    /**
     * set parent Account
     */
    public void parentAccount() {
        try {
            if (getBarcodeParent() != 0) {
                CostCenter parentAccount = profitcenterFacadeREST.findParentprofitcenter(getBarcodeParent());
                setNameParent(parentAccount.getName());
                getGlprofitcenter().setParent(parentAccount);
                getGlprofitcenter().setLevel(parentAccount.getLevel() + 1);
            }
        } catch (Exception e) {
            saveError(e, "ProfitCenterFormBean", "parentAccount");
        }
    }

    /**
     * @return the errorMessage
     */
    public StringBuilder getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(StringBuilder errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the glprofitcenter
     */
    public CostCenter getGlprofitcenter() {
        return glprofitcenter;
    }

    /**
     * @param glprofitcenter the glprofitcenter to set
     */
    public void setGlprofitcenter(CostCenter glprofitcenter) {
        this.glprofitcenter = glprofitcenter;
    }

    /**
     * @return the glprofitcenterParent
     */
    public CostCenter getGlprofitcenterParent() {
        return glprofitcenterParent;
    }

    /**
     * @param glprofitcenterParent the glprofitcenterParent to set
     */
    public void setGlprofitcenterParent(CostCenter glprofitcenterParent) {
        this.glprofitcenterParent = glprofitcenterParent;
    }

    /**
     * @return the glprofitcenterSelected
     */
    public CostCenter getGlprofitcenterSelected() {
        return glprofitcenterSelected;
    }

    /**
     * @param glprofitcenterSelected the glprofitcenterSelected to set
     */
    public void setGlprofitcenterSelected(CostCenter glprofitcenterSelected) {
        this.glprofitcenterSelected = glprofitcenterSelected;
    }

    /**
     * @return the profitSearchBean
     */
    public ProfitSearchBean getProfitSearchBean() {
        return profitSearchBean;
    }

    /**
     * @param profitSearchBean the profitSearchBean to set
     */
    public void setProfitSearchBean(ProfitSearchBean profitSearchBean) {
        this.profitSearchBean = profitSearchBean;
    }

    /**
     * @return the glprofitcenterListSearch
     */
    public List<CostCenter> getGlprofitcenterListSearch() {
        return glprofitcenterListSearch;
    }

    /**
     * @param glprofitcenterListSearch the glprofitcenterListSearch to set
     */
    public void setGlprofitcenterListSearch(List<CostCenter> glprofitcenterListSearch) {
        this.glprofitcenterListSearch = glprofitcenterListSearch;
    }

    /**
     * @return the glprofitcenterList
     */
    public List<CostCenter> getGlprofitcenterList() {
        return glprofitcenterList;
    }

    /**
     * @param glprofitcenterList the glprofitcenterList to set
     */
    public void setGlprofitcenterList(List<CostCenter> glprofitcenterList) {
        this.glprofitcenterList = glprofitcenterList;
    }

    /**
     * @return the barcodeParent
     */
    public Integer getBarcodeParent() {
        return barcodeParent;
    }

    /**
     * @param barcodeParent the barcodeParent to set
     */
    public void setBarcodeParent(Integer barcodeParent) {
        this.barcodeParent = barcodeParent;
    }

    /**
     * @return the nameParent
     */
    public String getNameParent() {
        return nameParent;
    }

    /**
     * @param nameParent the nameParent to set
     */
    public void setNameParent(String nameParent) {
        this.nameParent = nameParent;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the root
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    /**
     * @return the selectedNode
     */
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    /**
     * @param selectedNode the selectedNode to set
     */
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

}
