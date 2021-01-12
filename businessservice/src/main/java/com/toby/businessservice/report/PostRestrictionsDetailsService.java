/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.views.PostRestrictionsDetailsView;
import com.toby.views.PostRestrictionsView;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface PostRestrictionsDetailsService {

    public List<PostRestrictionsDetailsView> getPostRestrictionsDetailsByPostRestrictionsId(Integer glBankTransactionId, Integer moduleCode);

    public List<PostRestrictionsDetailsView> getPostRestrictionsDetailsByBankIdDebitAccountAndDate(PostRestrictionsView postRestrictionsView, Date documentDate);

    public List<PostRestrictionsDetailsView> getPostRestrictionsDetailsByPostRestrictionsId(Integer branchId, Integer glBankTransactionId, Integer moduleCode);
}
