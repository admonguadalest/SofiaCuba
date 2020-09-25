package com.company.test1.web.screens.registropresencial;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.RegistroPresencial;

@UiController("test1_RegistroPresencial.browse")
@UiDescriptor("registro-presencial-browse.xml")
@LookupComponent("registroPresencialsTable")
@LoadDataBeforeShow
public class RegistroPresencialBrowse extends StandardLookup<RegistroPresencial> {
}