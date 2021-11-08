/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.TobyErrorService;
import com.toby.businessservice.TobyUserBankService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.TobyUserLoginService;
import com.toby.common.FindClientData;
import com.toby.common.SendEmailSMTP;
import com.toby.converter.GlBankDTOConverter;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.define.HttpMethode;
import com.toby.dto.GlBankDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.TobyErrorDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlYear;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserLogin;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.core.HttpHeaders;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author FreeComp
 */
public class Basic implements Serializable {

    private FindClientData findClientData;

    @EJB
    TobyUserLoginService tobyUserLoginService;
    @EJB
    TobyErrorService tobyErrorService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;
    @EJB
    private TobyUserBankService tobyUserBankService;

    private TobyUser userlogin;

    private Branch branch;

    private GlYear glyear;

    private TobyErrorDTO tobyErrorDTO;

    private SendEmailSMTP sendEmailSMTP;

    InetAddress ip;

    private String errorGlobal;

    private String uri;

    private ExternalContext context;

    private UserData userData;

    private String ScreenMode;

    private Integer dayHijriFrom;
    private Integer monthHijriFrom;
    private Integer yearHijriFrom;
    private Integer dayHijriTo;
    private Integer monthHijriTo;
    private Integer yearHijriTo;

    HijrahDate islamyYear;
    HijrahDate islamyMonth;
    HijrahDate islamyDay;

    private InvInventoryDTOConverter gallaryDTOConverters;
    private List<InvInventoryDTO> gallaryLists;
    private List<InvInventoryDTO> gallaryListSelected;
    private InvInventoryDTO gallaryDTOSelect;
    private String gallaryIdList;

    private GlBankDTOConverter glBankDTOConverter;
    private List<GlBankDTO> glBankDTOLists;
    private List<GlBankDTO> glBankDTOListSelected;
    private GlBankDTO glBankDTOSelect;
    private String glBankDTOIdList;
    
    private InvInventoryDTOConverter inventoryDTOConverters;
    private List<InvInventoryDTO> inventoryLists;
    private List<InvInventoryDTO> inventoryListSelected;
    private InvInventoryDTO inventoryDTOSelect;
    private String inventoryIdList;

    public Date findDateFromHijriFrom() {
        if (getYearHijriFrom() != null && getMonthHijriFrom() != null && getDayHijriFrom() != null) {
            HijrahDate date = HijrahChronology.INSTANCE.date(getYearHijriFrom(), getMonthHijriFrom(), getDayHijriFrom());
            LocalDate dateFrom = IsoChronology.INSTANCE.date(date);
            return java.sql.Date.valueOf(dateFrom);
        }
        return null;
    }

    public Date findDateFromHijriTo() {
        if (getYearHijriTo() != null && getMonthHijriTo() != null && getDayHijriTo() != null) {
            HijrahDate date = HijrahChronology.INSTANCE.date(getYearHijriTo(), getMonthHijriTo(), getDayHijriTo());
            LocalDate dateFrom = IsoChronology.INSTANCE.date(date);
            return java.sql.Date.valueOf(dateFrom);
        }
        return null;
    }

    public HijrahDate findHijriYear() {
        Date date = new Date(); // Gregorian date
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);

        islamyYear = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DATE)));
        //islamyMonth = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DATE)));
        // islamyDay = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DATE)));

        return islamyYear;
    }

    public HijrahDate findHijriMonth() {
        Date date = new Date(); // Gregorian date
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);

        //islamyYear = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DATE)));
        islamyMonth = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DATE)));
        // islamyDay = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DATE)));

        return islamyMonth;
    }

    public HijrahDate findHijriDay() {
        Date date = new Date(); // Gregorian date
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);

        //islamyYear = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DATE)));
        //islamyMonth = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DATE)));
        islamyDay = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DATE)));

        return islamyDay;
    }

    public String goTOrganizationSiteacoount(Integer organizationSiteId) {
        try {
            if (organizationSiteId != null && organizationSiteId > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("ClientacoountSelected", organizationSiteId);
                context.getSessionMap().put("ScreenMode", "editClientacoount");
                exit("../inventoryreports/clientacoountById.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToEdit");
            return null;
        }
    }

    public void exit(String url) {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            fc.getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(null).log(Level.SEVERE, null, ex);
        }
    }

    public void saveError(Exception e, String className, String methodName) {
        getTobyErrorDTO().setClassName(className);
        getTobyErrorDTO().setError(e.toString());
        getTobyErrorDTO().setMethod(methodName);
        e.printStackTrace();
        tobyErrorService.save(getTobyErrorDTO(), getUserlogin());
//        getSendEmailSMTP().sendEmail(getUserlogin(), "Class Name -> " + className + "\n" + "Method Name -> " + methodName + "\n" + "ISAG ERROR \n" + System.err,"errorerp@toby-it.com", "123456");
//        getSendEmailSMTP().sendEmail(getUserlogin(), "Class Name -> " + className + "\n" + "Method Name -> " + methodName + "\n" + "ISAG ERROR \n" + System.err, "devError@toby-it.com", "toby@2020");
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

            TobyUserLogin tobyUserLogin = new TobyUserLogin();
            tobyUserLogin.setBranchId(userData.getUserBranch());
            tobyUserLogin.setCompanyId(userData.getCompany());
            tobyUserLogin.setCreatedBy(userData.getUser());
            tobyUserLogin.setCreationDate(new Date());
            tobyUserLogin.setDateLogin(new Date());
            if (state) {
                tobyUserLogin.setState(1);//user login
            } else {
                tobyUserLogin.setState(0);//user logout
            }
            getFindClientData().getClientData(tobyUserLogin);

//        tobyUserLogin.setMacId(getMacId());
            tobyUserLogin.setUserId(userData.getUser());

            tobyUserLoginService.addTobyUserLogin(tobyUserLogin);
        } catch (Exception ex) {
            saveError(ex, "Basic", "saveUserData");
            ex.printStackTrace();
        }
    }

    public TobyUser getUserlogin() {
        if (userlogin == null) {
            userlogin = new TobyUser();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            userlogin = (TobyUser) externalContext.getSessionMap().get("userlogin");
        }
        return userlogin;
    }

    public String callApiPostBasic(String url, String userName, String password, String jsonInputString, Map<String, Object> LinkParams, HttpMethode httpMethode) {
        try {
            String auth = userName + ":" + password;
            String link = url;
            if (LinkParams != null) {
                link = link + getParamsString(LinkParams);
            }
            URL url1 = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();

            conn.setRequestMethod(httpMethode.name());
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.setDoOutput(true);
            if (userName != null && password != null) {
                conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(
                        (auth).getBytes()));
            }
            if (jsonInputString != null && !jsonInputString.isEmpty()) {
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                    os.close();
                }
            }

            BufferedReader br;
            String strCurrentLine;
            StringBuilder json = new StringBuilder();

            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((strCurrentLine = br.readLine()) != null) {
                json.append(strCurrentLine);
            }

//             if (conn.getResponseCode() == 200) { } else {
//                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//                while ((strCurrentLine = br.readLine()) != null) {
//                    json.append(strCurrentLine);
//                }
//            }
            br.close();
            conn.disconnect();
            String result = StringEscapeUtils.unescapeJava(json.toString());
            result = result.replaceAll("null", "\"null\"");
            result.replaceAll("\"\"", "\"null\"");
            return result.replaceAll("(?<=[^{,:])\\\"(?![},:])(?!$)", "\'");//((?<!^)\")(?<!$)
        } catch (Exception ex) {
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String getParamsString(Map<String, Object> params) {
        try {
            StringBuilder result = new StringBuilder();
            Map<String, Object> param = new LinkedHashMap<>();
            param.putAll(params);

            for (Map.Entry<String, Object> entry : param.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        } catch (Exception ex) {
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void sentSMS() {
        String to = "amradel055@gmail.com";
        String from = "amradel@toby-it.com";
        String host = "mail.toby-it.com";//or IP address  

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

    public void sendSms(String mobile, String sender, String message, String username, String password) {
        try {
//            String username = ;
//            String password = ;
            String language = "2";
            StringBuilder json = new StringBuilder();
            json.append("[{\"user\":\"").append(username).append("\",\"pwd\":\"").append(password).append("\",\"number\":\"").append(mobile).append("\",\"msg\":\"").append(message);
            json.append("\",\"sender\":\"SMSAlert\",\"language\":\"Unicode\"}]");
            callApiPostBasic("http://mshastra.com/sendsms_api_json.aspx", null, null, json.toString(), null, HttpMethode.POST);
//            return 

//
//            String requestUrl = "https://mshastra.com/sendurl.aspx?"
////                    + "user=20099319"
////                    + "&pwd=0564933202"
////                    + "&senderid=SMSAlert"
////                    + "&mobileno=966564933202"
////                    + "&msgtext=HelloFromAmr"
////                    + "&priority=High"
////                    + "&CountryCode=ALL";
//                    + "user=" + URLEncoder.encode("20099319", "UTF-8")
//                    + "&pwd=" + URLEncoder.encode("0564933202", "UTF-8")
//                    + "&senderid=" + URLEncoder.encode("SMSAlert", "UTF-8")
//                    + "&mobileno=" + URLEncoder.encode("966564933202", "UTF-8")
//                    + "&msgtext=" + URLEncoder.encode("HelloFromAmr", "UTF-8")
//                    + "&priority=" + URLEncoder.encode("High", "UTF-8")
//                    + "&CountryCode=" + URLEncoder.encode("ALL", "UTF-8");
////            String requestUrl = "https://mshastra.com/sendurl.aspx/?"
////                    + "user=" + URLEncoder.encode("20099319", "UTF-8")
////                    + "&pwd=" + URLEncoder.encode("0564933202", "UTF-8")
////                    //                    + "&language=" + URLEncoder.encode(language, "UTF-8")
////                    + "&senderid=" + URLEncoder.encode(sender, "UTF-8")
////                    + "&mobileno=" + URLEncoder.encode("0201015926792", "UTF-8")
////                    + "&msgtext=" + URLEncoder.encode(message, "UTF-8")
////                    + "&CountryCode=" + URLEncoder.encode("ALL", "UTF-8");
//
//            URL url = new URL(requestUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(true);
//            conn.connect();
//            
////            conn.setRequestMethod("POST");
////            conn.setRequestProperty("Content-Length", Integer.toString(requestUrl.length()));
////            conn.getOutputStream().write(requestUrl.getBytes("UTF-8")); // here
//            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            final StringBuffer stringBuffer = new StringBuffer();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                stringBuffer.append(line);
//            }
//            rd.close();
//            System.out.println(conn.getResponseMessage());
//
//            conn.disconnect();
//            return conn.getResponseMessage();
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
//            return "Error " + e;
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

    public void hishis() {
        System.out.println("HisHis");
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * @return the glyear
     */
    public GlYear getGlyear() {
        if (glyear == null) {
            glyear = new GlYear();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            glyear = (GlYear) externalContext.getSessionMap().get("glyear");
        }
        return glyear;
    }

    /**
     * @param glyear the glyear to set
     */
    public void setGlyear(GlYear glyear) {
        this.glyear = glyear;
    }

    /**
     * @return the tobyErrorDTO
     */
    public TobyErrorDTO getTobyErrorDTO() {
        if (tobyErrorDTO == null) {
            tobyErrorDTO = new TobyErrorDTO();
        }
        return tobyErrorDTO;
    }

    /**
     * @param tobyErrorDTO the tobyErrorDTO to set
     */
    public void setTobyErrorDTO(TobyErrorDTO tobyErrorDTO) {
        this.tobyErrorDTO = tobyErrorDTO;
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
     * @return the ScreenMode
     */
    public String getScreenMode() {
        setScreenMode((String) getContext().getSessionMap().get("ScreenMode"));
        return ScreenMode;
    }

    /**
     * @param ScreenMode the ScreenMode to set
     */
    public void setScreenMode(String ScreenMode) {
        this.ScreenMode = ScreenMode;
    }

    /**
     * @return the dayHijriFrom
     */
    public Integer getDayHijriFrom() {
        return dayHijriFrom;
    }

    /**
     * @param dayHijriFrom the dayHijriFrom to set
     */
    public void setDayHijriFrom(Integer dayHijriFrom) {
        this.dayHijriFrom = dayHijriFrom;
    }

    /**
     * @return the monthHijriFrom
     */
    public Integer getMonthHijriFrom() {
        return monthHijriFrom;
    }

    /**
     * @param monthHijriFrom the monthHijriFrom to set
     */
    public void setMonthHijriFrom(Integer monthHijriFrom) {
        this.monthHijriFrom = monthHijriFrom;
    }

    /**
     * @return the yearHijriFrom
     */
    public Integer getYearHijriFrom() {
        return yearHijriFrom;
    }

    /**
     * @param yearHijriFrom the yearHijriFrom to set
     */
    public void setYearHijriFrom(Integer yearHijriFrom) {
        this.yearHijriFrom = yearHijriFrom;
    }

    /**
     * @return the dayHijriTo
     */
    public Integer getDayHijriTo() {
        return dayHijriTo;
    }

    /**
     * @param dayHijriTo the dayHijriTo to set
     */
    public void setDayHijriTo(Integer dayHijriTo) {
        this.dayHijriTo = dayHijriTo;
    }

    /**
     * @return the monthHijriTo
     */
    public Integer getMonthHijriTo() {
        return monthHijriTo;
    }

    /**
     * @param monthHijriTo the monthHijriTo to set
     */
    public void setMonthHijriTo(Integer monthHijriTo) {
        this.monthHijriTo = monthHijriTo;
    }

    /**
     * @return the yearHijriTo
     */
    public Integer getYearHijriTo() {
        return yearHijriTo;
    }

    /**
     * @param yearHijriTo the yearHijriTo to set
     */
    public void setYearHijriTo(Integer yearHijriTo) {
        this.yearHijriTo = yearHijriTo;
    }

    /**
     * @return the gallaryDTOSelect
     */
    public InvInventoryDTO getGallaryDTOSelect() {
        if (gallaryDTOSelect == null) {
            if (getUserData().getGallaryDTOList() == null || getUserData().getGallaryDTOList().isEmpty()) {
                gallaryDTOSelect = null;
            } else {
                gallaryDTOSelect = getUserData().getGallaryDTOList().get(0);
            }
        }
        return gallaryDTOSelect;
    }

    /**
     * @param gallaryDTOSelect the gallaryDTOSelect to set
     */
    public void setGallaryDTOSelect(InvInventoryDTO gallaryDTOSelect) {
        this.gallaryDTOSelect = gallaryDTOSelect;
    }

    /**
     * @return the gallaryLists
     */
    public List<InvInventoryDTO> getGallaryLists() {
        if (gallaryLists == null || gallaryLists.isEmpty()) {
            gallaryLists = tobyUserInventoryService.getAllGallaryByUserAndBranch(getUserData().getUser());
            setGallaryDTOConverters(new InvInventoryDTOConverter(gallaryLists));
        }
        return gallaryLists;
    }

    /**
     * @param gallaryLists the gallaryLists to set
     */
    public void setGallaryLists(List<InvInventoryDTO> gallaryLists) {
        this.gallaryLists = gallaryLists;
    }

    /**
     * @return the gallaryDTOConverters
     */
    public InvInventoryDTOConverter getGallaryDTOConverters() {
        return gallaryDTOConverters;
    }

    /**
     * @param gallaryDTOConverters the gallaryDTOConverters to set
     */
    public void setGallaryDTOConverters(InvInventoryDTOConverter gallaryDTOConverters) {
        this.gallaryDTOConverters = gallaryDTOConverters;
    }

    /**
     * @return the gallaryListSelected
     */
    public List<InvInventoryDTO> getGallaryListSelected() {
        if (gallaryListSelected == null) {
            gallaryListSelected = getGallaryLists();
        }
        return gallaryListSelected;
    }

    /**
     * @param gallaryListSelected the gallaryListSelected to set
     */
    public void setGallaryListSelected(List<InvInventoryDTO> gallaryListSelected) {
        this.gallaryListSelected = gallaryListSelected;
    }

    /**
     * @return the gallaryIdList
     */
    public String getGallaryIdList() {
        if (getGallaryListSelected() != null && !getGallaryListSelected().isEmpty()) {
            StringBuilder list = null;
            for (InvInventoryDTO iidto : getGallaryListSelected()) {
                if (list == null) {
                    list = new StringBuilder();
                    list.append(iidto.getId());
                } else {
                    list.append(" , ").append(iidto.getId());
                }
            }
            return list.toString();
        }
        return gallaryIdList;
    }

    /**
     * @param gallaryIdList the gallaryIdList to set
     */
    public void setGallaryIdList(String gallaryIdList) {
        this.gallaryIdList = gallaryIdList;
    }

    /**
     * @return the glBankDTOConverter
     */
    public GlBankDTOConverter getGlBankDTOConverter() {
        return glBankDTOConverter;
    }

    /**
     * @param glBankDTOConverter the glBankDTOConverter to set
     */
    public void setGlBankDTOConverter(GlBankDTOConverter glBankDTOConverter) {
        this.glBankDTOConverter = glBankDTOConverter;
    }

    /**
     * @return the glBankDTOLists
     */
    public List<GlBankDTO> getGlBankDTOLists() {
            if (getGallaryIdList() != null && !getGallaryIdList().isEmpty()) {
                glBankDTOLists = tobyUserBankService.getAllGlBankForUserByTypeAndBranchIdAndGallaryListPerDTO(getUserData().getUser().getId(), getUserData().getUserBranch().getId(), null, getGallaryIdList());
            } else {
                glBankDTOLists = tobyUserBankService.getAllGlBankForUserByTypeAndBranchIdPerDTO(getUserData().getUser().getId(), getUserData().getUserBranch().getId(), null, null);
            }
            setGlBankDTOConverter(new GlBankDTOConverter(glBankDTOLists));
        return glBankDTOLists;
    }

    /**
     * @param glBankDTOLists the glBankDTOLists to set
     */
    public void setGlBankDTOLists(List<GlBankDTO> glBankDTOLists) {
        this.glBankDTOLists = glBankDTOLists;
    }

    /**
     * @return the glBankDTOListSelected
     */
    public List<GlBankDTO> getGlBankDTOListSelected() {
        return glBankDTOListSelected;
    }

    /**
     * @param glBankDTOListSelected the glBankDTOListSelected to set
     */
    public void setGlBankDTOListSelected(List<GlBankDTO> glBankDTOListSelected) {
        this.glBankDTOListSelected = glBankDTOListSelected;
    }

    /**
     * @return the glBankDTOSelect
     */
    public GlBankDTO getGlBankDTOSelect() {
        return glBankDTOSelect;
    }

    /**
     * @param glBankDTOSelect the glBankDTOSelect to set
     */
    public void setGlBankDTOSelect(GlBankDTO glBankDTOSelect) {
        this.glBankDTOSelect = glBankDTOSelect;
    }

    /**
     * @return the glBankDTOIdList
     */
    public String getGlBankDTOIdList() {
        if (getGlBankDTOListSelected() != null && !getGlBankDTOListSelected().isEmpty()) {
            StringBuilder list = null;
            for (GlBankDTO iidto : getGlBankDTOListSelected()) {
                if (list == null) {
                    list = new StringBuilder();
                    list.append(iidto.getId());
                } else {
                    list.append(" , ").append(iidto.getId());
                }
            }
            return list.toString();
        }
        return glBankDTOIdList;
    }

    /**
     * @param glBankDTOIdList the glBankDTOIdList to set
     */
    public void setGlBankDTOIdList(String glBankDTOIdList) {
        this.glBankDTOIdList = glBankDTOIdList;
    }

    /**
     * @return the inventoryDTOConverters
     */
    public InvInventoryDTOConverter getInventoryDTOConverters() {
        return inventoryDTOConverters;
    }

    /**
     * @param inventoryDTOConverters the inventoryDTOConverters to set
     */
    public void setInventoryDTOConverters(InvInventoryDTOConverter inventoryDTOConverters) {
        this.inventoryDTOConverters = inventoryDTOConverters;
    }

    /**
     * @return the inventoryLists
     */
    public List<InvInventoryDTO> getInventoryLists() {
        if (inventoryLists == null || inventoryLists.isEmpty()) {
            inventoryLists = tobyUserInventoryService.getAllInventroyDTOByUserAndBranch(getUserData().getUser());
            setInventoryDTOConverters(new InvInventoryDTOConverter(inventoryLists));
        }
        return inventoryLists;
    }

    /**
     * @param inventoryLists the inventoryLists to set
     */
    public void setInventoryLists(List<InvInventoryDTO> inventoryLists) {
        this.inventoryLists = inventoryLists;
    }

    /**
     * @return the inventoryListSelected
     */
    public List<InvInventoryDTO> getInventoryListSelected() {
         if (inventoryListSelected == null) {
            inventoryListSelected = getInventoryLists();
        }
        return inventoryListSelected;
    }

    /**
     * @param inventoryListSelected the inventoryListSelected to set
     */
    public void setInventoryListSelected(List<InvInventoryDTO> inventoryListSelected) {
        this.inventoryListSelected = inventoryListSelected;
    }

    /**
     * @return the inventoryDTOSelect
     */
    public InvInventoryDTO getInventoryDTOSelect() {
       if (inventoryDTOSelect == null) {
            if (getUserData().getInventoryDTOList() == null || getUserData().getInventoryDTOList().isEmpty()) {
                inventoryDTOSelect = null;
            } else {
                inventoryDTOSelect = getUserData().getInventoryDTOList().get(0);
            }
        }
        return inventoryDTOSelect;
    }

    /**
     * @param inventoryDTOSelect the inventoryDTOSelect to set
     */
    public void setInventoryDTOSelect(InvInventoryDTO inventoryDTOSelect) {
        this.inventoryDTOSelect = inventoryDTOSelect;
    }

    /**
     * @return the inventoryIdList
     */
    public String getInventoryIdList() {
        if (getInventoryListSelected() != null && !getInventoryListSelected().isEmpty()) {
            StringBuilder list = null;
            for (InvInventoryDTO iidto : getInventoryListSelected()) {
                if (list == null) {
                    list = new StringBuilder();
                    list.append(iidto.getId());
                } else {
                    list.append(" , ").append(iidto.getId());
                }
            }
            return list.toString();
        }
        return inventoryIdList;
    }

    /**
     * @param inventoryIdList the inventoryIdList to set
     */
    public void setInventoryIdList(String inventoryIdList) {
        this.inventoryIdList = inventoryIdList;
    }

}
