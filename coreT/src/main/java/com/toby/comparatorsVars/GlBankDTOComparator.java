/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.comparatorsVars;

import com.toby.dto.GlBankDTO;
import java.util.Comparator;

/**
 *
 * @author ahmed
 */
public class GlBankDTOComparator implements Comparator<GlBankDTO> {

    @Override
    public int compare(GlBankDTO o1, GlBankDTO o2) {
        if (o1.getCode() > o2.getCode()) {
            return 1;
        } else {
            return -1;
        }
    }

}
