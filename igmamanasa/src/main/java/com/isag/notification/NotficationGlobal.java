/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.notification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.websocket.Session;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ahmed
 */
@ManagedBean(name =  "notficationGlobal")
@ApplicationScoped
public class NotficationGlobal implements Serializable {

    private Session session;
    private static Set<NotficationGlobal> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String,String> users = new HashMap<>();
    private Boolean close;
    private String title;
    private String msg;

    public void closeServer() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("notification_server()");
        setClose(Boolean.TRUE); 
        setTitle("تحديث البرنامج");
        setMsg("عميلنا العزيز سيتم ايقاف الخدمه مؤقتا بعد عشره دقائق لإجراء بعض التحديثات من فضلك يرجي حفظ التعديلات لعدم فقدانها");
    }
    
     public void sendMessage() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("notification_server()");
        if(getTitle() != null && getMsg() != null && !getTitle().isEmpty() && !getMsg().isEmpty()){
         setClose(Boolean.TRUE);  
          FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "شكرا", "تم الارسال بنجاح"));
        }else{
           FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطأ", "يجب ادخال عنوان و رساله"));
        }
       
        
    }
     
     public void openServer() {
        setClose(Boolean.FALSE); 
         setTitle(null);
         setMsg(null);
    }

    /**
     * @return the close
     */
    public Boolean getClose() {
        if(close == null){
        close = Boolean.FALSE;
        }
        return close;
    }

    /**
     * @param close the close to set
     */
    public void setClose(Boolean close) {
        this.close = close;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
  

}
