package com.company.test1.web.components.archivoadjunto;

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.xml.layout.loaders.AbstractComponentLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.AbstractFieldLoader;
import com.vaadin.ui.AbstractField;

public class ArchivoAdjuntoFieldLoader extends AbstractFieldLoader<ArchivoAdjuntoField> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(WebArchivoAdjuntoField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();
    }
}