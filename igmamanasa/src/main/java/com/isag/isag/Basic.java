/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.CompanyService;
import com.isag.common.FindClientData;
import com.isag.common.SendEmailSMTP;
import com.isag.dto.IgmaErrorDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaUser;
import com.isag.entity.IgmaUserLogin;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import com.isag.businessservice.IgmaErrorService;
import com.isag.businessservice.IgmaTokensService;
import com.isag.businessservice.IgmaUserLoginService;
import com.isag.converter.CompanyConverter;
import com.isag.entity.IgmaCompany;

/**
 *
 * @author FreeComp
 */
public class Basic implements Serializable {

    private FindClientData findClientData;

    @EJB
    IgmaUserLoginService isagUserLoginService;
    @EJB
    IgmaErrorService isagErrorService;
    @EJB
    IgmaTokensService isagTokensService;
    @EJB
    CompanyService companyService;

    private IgmaUser userlogin;
    
    private UserData userData;
    
    private ExternalContext context;

    private Branch branch;

    private IgmaErrorDTO isagErrorDTO;

    private SendEmailSMTP sendEmailSMTP;

    InetAddress ip;

    private String errorGlobal;

    private String uri;
    
    private List<IgmaCompany> companiesList;
    
    private IgmaCompany companySelected ;
    
    private CompanyConverter companyConverter;
    
    private Integer selectedCompanyId;

    public void saveError(Exception e, String className, String methodName) {
        getIsagErrorDTO().setClassName(className);
        getIsagErrorDTO().setError(e.toString());
        getIsagErrorDTO().setMethod(methodName);
        e.printStackTrace();
        isagErrorService.save(getIsagErrorDTO(), getUserlogin());
//        getSendEmailSMTP().sendEmail(getUserlogin(), "Class Name -> " + className + "\n" + "Method Name -> " + methodName + "\n" + "ISAG ERROR \n" + System.err,"errorerp@isag-it.com", "123456");
//        getSendEmailSMTP().sendEmail(getUserlogin(), "Class Name -> " + className + "\n" + "Method Name -> " + methodName + "\n" + "ISAG ERROR \n" + System.err, "devError@isag-it.com", "isag@2020");
    }

    public boolean isFileExist(String filePath) {
        try {
            if (filePath != null) {
                File file = new File(filePath);
                return file.exists();
            }
        } catch (Exception e) {
            saveError(e, "Basic", "isFileExist");
        }
        return false;
    }

    public String getMacId() {
        StringBuilder sb = new StringBuilder();
        try {
            ip = InetAddress.getLocalHost();
            // System.out.println("ip= " + ip);

//			System.out.println("Current IP address : " + ip.getHostAddress());
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

//            network = NetworkInterface.getByName("eth0"); // for ubuntu
            byte[] mac = network.getHardwareAddress();

//			System.out.print("Current MAC address : ");
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            System.out.println(sb.toString());

        } catch (UnknownHostException e) {

            e.printStackTrace();
            System.out.println(e);

        } catch (SocketException e) {

            e.printStackTrace();
            System.out.println(e);
        }
        return sb.toString();
    }

    public void saveUserData(UserData userData, Boolean state) {
        try {

            IgmaUserLogin isagUserLogin = new IgmaUserLogin();
            isagUserLogin.setBranchId(userData.getUserBranch());
            isagUserLogin.setCompanyId(userData.getCompany());
            isagUserLogin.setCreatedBy(userData.getUser());
            isagUserLogin.setCreationDate(new Date());
            isagUserLogin.setDateLogin(new Date());
            if (state) {
                isagUserLogin.setState(1);//user login
            } else {
                isagUserLogin.setState(0);//user logout
            }
            getFindClientData().getClientData(isagUserLogin);

//        isagUserLogin.setMacId(getMacId());
            isagUserLogin.setUserId(userData.getUser());
            isagTokensService.UpdateActiveIgmaTokenDTObyUserID(userData.getUser().getId(), Boolean.FALSE);
            isagUserLoginService.addIsagUserLogin(isagUserLogin);
        } catch (Exception ex) {
            saveError(ex, "Basic", "saveUserData");
            ex.printStackTrace();
        }
    }

    public IgmaUser getUserlogin() {
        if (userlogin == null) {
            userlogin = new IgmaUser();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            userlogin = (IgmaUser) externalContext.getSessionMap().get("userlogin");
        }
        return userlogin;
    }

    public void sentSMS() {
        String to = "amradel055@gmail.com";
        String from = "amradel@isag-it.com";
        String host = "mail.isag-it.com";//or IP address  

        //Get the session object  
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);

        //compose the message  
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Ping");
            message.setText("Hello, this is example of sending email  ");

            // Send message  
            Transport.send(message);
            System.out.println("message sent successfully....");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse httpServletResponse = (HttpServletResponse) context.getExternalContext().getResponse();
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            if ("pdf".equals(exportType)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                httpServletResponse.flushBuffer();
                servletOutputStream.flush();
                servletOutputStream.close();
                context.renderResponse();
                context.responseComplete();
                return;
            } else if ("excel".equals(exportType)) {

            } else if ("html".equals(exportType)) {

            }
        } catch (Exception ex) {
            System.out.println(ex + "exception ++++++++++++++++++");
        }

    }
    
    public void hishis(){
        System.err.println("hishis  ---- ");
    }

    public boolean filterByDate(Object value, Object filter, Locale locale) {

        if (filter == null) {
            return true;
        }

        if (value == null) {
            return false;
        }

//        return DateUtils.truncatedEquals((Date) filter, (Date) value, Calendar.DATE);
        return true;
    }

    /**
     * @return the branch
     */
    public Branch getBranch() {
        if (branch == null) {
            branch = new Branch();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            branch = (Branch) externalContext.getSessionMap().get("branch");
        }
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * @return the isagErrorDTO
     */
    public IgmaErrorDTO getIsagErrorDTO() {
        if (isagErrorDTO == null) {
            isagErrorDTO = new IgmaErrorDTO();
        }
        return isagErrorDTO;
    }

    /**
     * @param isagErrorDTO the isagErrorDTO to set
     */
    public void setIsagErrorDTO(IgmaErrorDTO isagErrorDTO) {
        this.isagErrorDTO = isagErrorDTO;
    }

    /**
     * @return the sendEmailSMTP
     */
    public SendEmailSMTP getSendEmailSMTP() {
        if (sendEmailSMTP == null) {
            sendEmailSMTP = new SendEmailSMTP();
        }
        return sendEmailSMTP;
    }

    /**
     * @param sendEmailSMTP the sendEmailSMTP to set
     */
    public void setSendEmailSMTP(SendEmailSMTP sendEmailSMTP) {
        this.sendEmailSMTP = sendEmailSMTP;
    }

    /**
     * @return the findClientData
     */
    public FindClientData getFindClientData() {
        if (findClientData == null) {
            findClientData = new FindClientData();
        }
        return findClientData;
    }

    /**
     * @param findClientData the findClientData to set
     */
    public void setFindClientData(FindClientData findClientData) {
        this.findClientData = findClientData;
    }

    /**
     * @return the errorGlobal
     */
    public String getErrorGlobal() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setErrorGlobal((String) context.getSessionMap().get("errorGlobal"));
        return errorGlobal;
    }

    /**
     * @param errorGlobal the errorGlobal to set
     */
    public void setErrorGlobal(String errorGlobal) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("errorGlobal", errorGlobal);
        this.errorGlobal = errorGlobal;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
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

    public List<IgmaCompany> getCompaniesList() {
        if(companiesList == null){
            companiesList = companyService.getAllCompanies();
            setCompanyConverter(new CompanyConverter(companiesList));
        }
        return companiesList;
    }

    public void setCompaniesList(List<IgmaCompany> companiesList) {
        this.companiesList = companiesList;
    }

    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    /**
     * @return the companySelected
     */
    public IgmaCompany getCompanySelected() {
        return companySelected;
    }

    /**
     * @param companySelected the companySelected to set
     */
    public void setCompanySelected(IgmaCompany companySelected) {
        this.companySelected = companySelected;
    }

    /**
     * @return the companyConverter
     */
    public CompanyConverter getCompanyConverter() {
        return companyConverter;
    }

    /**
     * @param companyConverter the companyConverter to set
     */
    public void setCompanyConverter(CompanyConverter companyConverter) {
        this.companyConverter = companyConverter;
    }

}
