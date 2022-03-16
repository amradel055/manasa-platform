/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invreservation;

import com.isag.businessservice.InvReservationService;
import com.isag.entiy.InvReservationEntity;

/**
 *
 * @author amr
 */
public class InvReservationCommon {

    private InvReservationService invReservationService;

    public InvReservationCommon(InvReservationService invReservationService) {
        this.invReservationService = invReservationService;
    }

    public InvReservationEntity returnObject(Integer tenderId) {

//        invReservation = invReservationService.findInvReservationByInvReservationId(invReservationId);
//        invReservationDetailList = invReservationService.getALLInvReservationDetailsByInvReservation(invReservationId);
//
//        invReservationEntity = mapInvReservationToEntity(invReservation);
        return null;
    }

    /**
     * @return the invReservationService
     */
    public InvReservationService getInvReservationService() {
        return invReservationService;
    }

    /**
     * @param invReservationService the invReservationService to set
     */
    public void setInvReservationService(InvReservationService invReservationService) {
        this.invReservationService = invReservationService;
    }
}
