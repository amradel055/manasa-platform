package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.DeletedAndUpdatedSearchBean;
import com.toby.views.DeletedAndUpdatedGeneralJournal;
import java.util.List;

import javax.ejb.Remote;


@Remote
public interface DeletedAndUpdatedGeneralJournalService {

	List<DeletedAndUpdatedGeneralJournal> findAllDeletedAndUpdateGeneralJournals(DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean, Integer branchId);
}
