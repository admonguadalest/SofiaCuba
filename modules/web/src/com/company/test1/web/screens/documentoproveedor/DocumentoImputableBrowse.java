package com.company.test1.web.screens.documentoproveedor;

import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.documentosImputables.DocumentoImputable;

import javax.inject.Inject;

@UiController("test1_DocumentoImputable.browse")
@UiDescriptor("documento-imputable-browse.xml")
@LookupComponent("documentoImputablesTable")
@LoadDataBeforeShow
public class DocumentoImputableBrowse extends StandardLookup<DocumentoImputable> {

    @Inject
    private UiComponents uiComponents;
    @Inject
    private DataManager dataManager;

    public Component getColumnEmisor(DocumentoImputable di){
        Label l = uiComponents.create(Label.NAME);
        String str = "";
        if (di instanceof FacturaProveedor){
            try {
                FacturaProveedor dp = (FacturaProveedor) di;
                dp = dataManager.reload(dp, "facturaProveedor-view");
                str = dp.getProveedor().getPersona().getNombreCompleto();
            }catch(Exception exc){
                str = "N/D";
            }
        }else if (di instanceof Presupuesto){
            try {
                Presupuesto dp = (Presupuesto) di;
                dp = dataManager.reload(dp, "presupuesto-view");
                str = dp.getProveedor().getPersona().getNombreCompleto();
            }catch(Exception exc){
                str = "N/D";
            }

        }else{
            str = "N/A";
        }
        l.setValue(str);
        return l;
    }
}