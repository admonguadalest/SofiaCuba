package com.company.test1.web.screens.documentacioninquilino;

import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.documentacionesinquilinos.DocumentacionInquilino;

import javax.inject.Inject;

@UiController("test1_DocumentacionInquilino.browse")
@UiDescriptor("documentacion-inquilino-browse.xml")
@LookupComponent("documentacionInquilinoesTable")
@LoadDataBeforeShow
public class DocumentacionInquilinoBrowse extends StandardLookup<DocumentacionInquilino> {

    @Inject
    private Table<DocumentacionInquilino> documentacionInquilinoesTable;


}