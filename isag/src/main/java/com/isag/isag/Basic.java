/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.isag.businessservice.IsagErrorService;
import com.isag.businessservice.IsagTokensService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.IsagUserLoginService;
import com.isag.common.FindClientData;
import com.isag.common.SendEmailSMTP;
import com.isag.converter.InvInventoryConverter;
import com.isag.define.HttpMethode;
import com.isag.dto.IsagErrorDTO;
import com.isag.entity.Branch;
import com.isag.entity.GlYear;
import com.isag.entity.InvInventory;
import com.isag.entity.IsagUser;
import com.isag.entity.IsagUserLogin;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
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
import org.apache.http.HttpHeaders;

/**
 *
 * @author FreeComp
 */
public class Basic implements Serializable {

    private FindClientData findClientData;

    @EJB
    IsagUserLoginService isagUserLoginService;
    @EJB
    IsagErrorService isagErrorService;
    @EJB
    IsagTokensService isagTokensService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;

    private IsagUser userlogin;

    private UserData userData;

    private ExternalContext context;

    private Branch branch;

    private GlYear glyear;

    private IsagErrorDTO isagErrorDTO;

    private SendEmailSMTP sendEmailSMTP;

    InetAddress ip;

    private String errorGlobal;

    private String uri;
    
    private List<InvInventory> inventoryList;
    private InvInventory invInventorySelection;
    private InvInventoryConverter invInventoryConverter;
    
    
    public void reportQRCode(HashMap hashMap, String qrCodeText) {
        String filePath = "JD.png";
        int size = 200;
        String fileType = "png";
        File qrFile = new File(filePath);
        createQRImage(qrFile, qrCodeText, size, fileType);
        hashMap.put("QRCode", "JD.png");

    }
    
     private void createQRImage(File qrFile, String qrCodeText, int size, String fileType) {
        try {
            // Create the ByteMatrix for the QR-Code that encodes the given String
            Hashtable hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hintMap.put(EncodeHintType.MARGIN, 1);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
            // Make the BufferedImage that are to hold the QRCode
            int matrixWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, matrixWidth, matrixWidth);
            // Paint and save the image using the ByteMatrix
            graphics.setColor(Color.BLACK);
            for (int i = 0; i < matrixWidth; i++) {
                for (int j = 0; j < matrixWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, fileType, qrFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveError(Exception e, String className, String methodName) {
        getIsagErrorDTO().setClassName(className);
        getIsagErrorDTO().setError(e.toString());
        getIsagErrorDTO().setMethod(methodName);
        e.printStackTrace();
        isagErrorService.save(getIsagErrorDTO(), getUserlogin());
        getSendEmailSMTP().sendEmail(getUserlogin(), "Class Name -> " + className + "\n" + "Method Name -> " + methodName + "\n" + "ISAG ERROR \n" + System.err,"errorerp@isag-it.com", "123456");
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
            IsagUserLogin isagUserLogin = new IsagUserLogin();
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
            isagTokensService.UpdateActiveIsagTokenDTObyUserID(userData.getUser().getId(), Boolean.FALSE);
            isagUserLoginService.addIsagUserLogin(isagUserLogin);
        } catch (Exception ex) {
            saveError(ex, "Basic", "saveUserData");
            ex.printStackTrace();
        }
    }

    public IsagUser getUserlogin() {
        if (userlogin == null) {
            userlogin = new IsagUser();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            userlogin = (IsagUser) externalContext.getSessionMap().get("userlogin");
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

    public String callApiPostBasic(String url, String userName, String password, String jsonInputString, HttpMethode httpMethode) {
        try {
            String auth = userName + ":" + password;
            //String link = url + getParamsString(userName,password,params);
            URL url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();

            conn.setRequestMethod(httpMethode.name());
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(
                    (auth).getBytes()));
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
                os.close();
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
            return json.toString();
        } catch (Exception ex) {
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String getParamsString(String userName, String password, Map<String, Object> params) {
        try {
            StringBuilder result = new StringBuilder();
            Map<String, Object> param = new LinkedHashMap<>();
            param.put("username", userName);
            param.put("password", password);
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

    public void exit(String url) {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            fc.getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(null).log(Level.SEVERE, null, ex);
        }
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

    public String sendSms(String mobile, String sender, String message) {
        try {

            String username = "l3TTd3Ur";
            String password = "lC2DacCyTH";
            String language = "2";

            String requestUrl = "https://smsmisr.com/api/webapi/?"
                    + "username=" + URLEncoder.encode(username, "UTF-8")
                    + "&password=" + URLEncoder.encode(password, "UTF-8")
                    + "&language=" + URLEncoder.encode(language, "UTF-8")
                    + "&sender=" + URLEncoder.encode(sender, "UTF-8")
                    + "&mobile=" + URLEncoder.encode(mobile, "UTF-8")
                    + "&message=" + URLEncoder.encode(message, "UTF-8");

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(requestUrl.length()));
            conn.getOutputStream().write(requestUrl.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
            System.out.println(conn.getResponseMessage());

            conn.disconnect();
            return conn.getResponseMessage();
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            return "Error " + e;
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
     * @return the isagErrorDTO
     */
    public IsagErrorDTO getIsagErrorDTO() {
        if (isagErrorDTO == null) {
            isagErrorDTO = new IsagErrorDTO();
        }
        return isagErrorDTO;
    }

    /**
     * @param isagErrorDTO the isagErrorDTO to set
     */
    public void setIsagErrorDTO(IsagErrorDTO isagErrorDTO) {
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
            
    /**
     * @return the inventoryList
     */
    public List<InvInventory> getInventoryList() {
        if(inventoryList == null){
            inventoryList = isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), getUserlogin().getBranchId().getId());
            if (inventoryList != null && !inventoryList.isEmpty()) {
                setInvInventoryConverter(new InvInventoryConverter(getInventoryList()));
                 setInvInventorySelection(getInventoryList() == null || getInventoryList().isEmpty() ? null : getInventoryList().get(0));
            }
        }
        return inventoryList;
    }

    /**
     * @param inventoryList the inventoryList to set
     */
    public void setInventoryList(List<InvInventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    /**
     * @return the invInventorySelection
     */
    public InvInventory getInvInventorySelection() {
        return invInventorySelection;
    }

    /**
     * @param invInventorySelection the invInventorySelection to set
     */
    public void setInvInventorySelection(InvInventory invInventorySelection) {
        this.invInventorySelection = invInventorySelection;
    }

    /**
     * @return the invInventoryConverter
     */
    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    /**
     * @param invInventoryConverter the invInventoryConverter to set
     */
    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

}
