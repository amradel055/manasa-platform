/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.comparatorsVars;

import com.isag.entity.HosDoctor;
import java.util.Comparator;

/**
 *
 * @author ahmed
 * @param <HosDoctor>
 */
public class HosDoctorCodeComparator implements Comparator<HosDoctor> {

    @Override
    public int compare(HosDoctor t, HosDoctor t1) {
        if (t.getCode() .compareTo(t1.getCode())>1) {
            return 1;
        } else {
            return -1;
        }
    }

}
