/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class HistoryGeneraljournalDetailsServiceImpl implements HistoryGeneraljournalDetailsService {

    @EJB
    private GenericDAO dao;


}
