package com.company.test1.web.screens.mailinglist;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.MailingList;

@UiController("test1_MailingList.browse")
@UiDescriptor("mailing-list-browse.xml")
@LookupComponent("mailingListsTable")
@LoadDataBeforeShow
public class MailingListBrowse extends StandardLookup<MailingList> {
}