/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.common;

import com.toby.entity.TobyUserLogin;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class FindClientData {

    public void getClientData(TobyUserLogin tobyUserLogin) throws UnknownHostException, SocketException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("x_forwarded_for");
        String browserDetails = request.getHeader("User-Agent");

        if (ipAddress == null) {
            //Getting data out of the request
            ipAddress = request.getRemoteAddr();
            //Getting the IP
            InetAddress i = InetAddress.getByName(request.getRemoteAddr());
            tobyUserLogin.setIpAddress(i.toString());
            tobyUserLogin.setHostName(i.getHostName().toString());

            //Starting to collect User Browser Data 
            String userAgent = browserDetails;
            String user = userAgent.toLowerCase();

            String os = "";
            String browser = "";

            //=================OS=======================
            if (userAgent.toLowerCase().indexOf("windows") >= 0) {
                os = "Windows";
            } else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
                os = "Mac";
            } else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
                os = "Unix";
            } else if (userAgent.toLowerCase().indexOf("android") >= 0) {
                os = "Android";
            } else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
                os = "IPhone";
            } else {
                os = "UnKnown, More-Info: " + userAgent;
            }
            //===============Browser===========================
            if (user.contains("msie")) {
                String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
                browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
            } else if (user.contains("safari") && user.contains("version")) {
                browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            } else if (user.contains("opr") || user.contains("opera")) {
                if (user.contains("opera")) {
                    browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
                } else if (user.contains("opr")) {
                    browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
                }
            } else if (user.contains("chrome")) {
                browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
            } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1) || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
                //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
                browser = "Netscape-?";

            } else if (user.contains("firefox")) {
                browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
            } else if (user.contains("rv")) {
                browser = "IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
            } else {
                browser = "UnKnown, More-Info: " + userAgent;
            }
            tobyUserLogin.setBrowserName(browser);
            tobyUserLogin.setOperatingSystem(os);

        }

    }

    public String findOperatingSystem() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String browserDetails = request.getHeader("User-Agent");
        
        
        if (browserDetails.toLowerCase().indexOf("windows") >= 0) {
                return "Windows";
            } else if (browserDetails.toLowerCase().indexOf("mac") >= 0) {
                return"Mac";
            } else if (browserDetails.toLowerCase().indexOf("x11") >= 0) {
                return "Unix";
            } else if (browserDetails.toLowerCase().indexOf("android") >= 0) {
                return "Android";
            } else if (browserDetails.toLowerCase().indexOf("iphone") >= 0) {
                return "IPhone";
            } else {
                return "UnKnown, More-Info: " + browserDetails;
            }
    }

}
