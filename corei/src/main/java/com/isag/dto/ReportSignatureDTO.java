/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ahmedEssam
 */
public class ReportSignatureDTO extends BaseEntityDTO {

    private String signature1;
    private String signature2;
    private String signature3;
    private String signature4;
    private String signature5;
    private Integer screen;
    private List<String> screenList;

    public ReportSignatureDTO() {
    }

    public ReportSignatureDTO(Integer id) {
        this.id = id;
    }

    public String getSignature1() {
        return signature1;
    }

    public void setSignature1(String signature1) {
        this.signature1 = signature1;
    }

    public String getSignature2() {
        return signature2;
    }

    public void setSignature2(String signature2) {
        this.signature2 = signature2;
    }

    public String getSignature3() {
        return signature3;
    }

    public void setSignature3(String signature3) {
        this.signature3 = signature3;
    }

    public String getSignature4() {
        return signature4;
    }

    public void setSignature4(String signature4) {
        this.signature4 = signature4;
    }

    public String getSignature5() {
        return signature5;
    }

    public void setSignature5(String signature5) {
        this.signature5 = signature5;
    }

    public Integer getScreen() {
        return screen;
    }

    public void setScreen(Integer screen) {
        this.screen = screen;
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
        if (!(object instanceof ReportSignatureDTO)) {
            return false;
        }
        ReportSignatureDTO other = (ReportSignatureDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + " ";
    }

    /**
     * @return the screenList
     */
    public List<String> getScreenList() {
         if (screenList == null) {
            screenList = new ArrayList<>();
            screenList.add("سندات القبض");
            screenList.add("سندات الصرف");
            screenList.add("سندات تحويل الخزائن");
          
        }
        return screenList;
    }

    /**
     * @param screenList the screenList to set
     */
    public void setScreenList(List<String> screenList) {
        this.screenList = screenList;
    }

}
