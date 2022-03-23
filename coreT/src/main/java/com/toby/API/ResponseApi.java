/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.dto.*;

public class ResponseApi extends BaseEntityDTO {

    private String msg;
    private Integer resposeId;
    private Integer status;

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

    /**
     * @return the resposeId
     */
    public Integer getResposeId() {
        return resposeId;
    }

    /**
     * @param resposeId the resposeId to set
     */
    public void setResposeId(Integer resposeId) {
        this.resposeId = resposeId;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

}
