/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.comparators;

import com.isag.entiy.InstContractDetailsEntity;
import java.util.Comparator;

/**
 *
 * @author ahmed
 */
public class InstContractDetailsEntityComparator implements Comparator<InstContractDetailsEntity> {

    @Override
    public int compare(InstContractDetailsEntity t, InstContractDetailsEntity t1) {
        if (t.getDueDate().compareTo(t1.getDueDate()) == 1) {
            return 1;
        } else {
            return -1;
        }
    }

}
