package com.isag.businessservice.reports;

import java.util.List;

import javax.ejb.Remote;

import com.isag.businessservice.reports.searchBean.DeletedAndUpdatedSearchBean;
import com.isag.views.DeletedAndUpdatedGeneralJournal;

@Remote
public interface DeletedAndUpdatedGeneralJournalService {

	List<DeletedAndUpdatedGeneralJournal> findAllDeletedAndUpdateGeneralJournals(DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean, Integer branchId);
}
