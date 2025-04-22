package com.company.test1.web.screens.facturaproveedor;

import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.enums.ValidacionEstado;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.queryconditions.Condition;
import com.haulmont.cuba.core.global.queryconditions.JpqlCondition;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_FacturasTelematicasValidacionesPendientes")
@UiDescriptor("facturas-telematicas-validaciones-pendientes.xml")
@LookupComponent("facturaProveedorsTable")
@LoadDataBeforeShow
public class FacturasTelematicasValidacionesPendientes extends StandardLookup<FacturaProveedor> {
    @Inject
    private DataManager dataManager;
    @Inject
    private Filter filter;

    @Install(to = "facturasDl", target = Target.DATA_LOADER)
    private List<FacturaProveedor> facturasDlLoadDelegate(LoadContext<FacturaProveedor> loadContext) {
        if (loadContext.getQuery().getParameters().isEmpty()){
            return new ArrayList();
        }
        loadContext.getQuery().setMaxResults(10000);
        List<FacturaProveedor> ffpp = dataManager.loadList(loadContext);
        ArrayList al = new ArrayList();
        a:
        for (int i = 0; i < ffpp.size(); i++) {
            boolean include = false;
            FacturaProveedor fp = ffpp.get(i);
            List<ImputacionDocumentoImputable> iiddii = fp.getImputacionesDocumentoImputable();
            for (int j = 0; j < iiddii.size(); j++) {
                ImputacionDocumentoImputable idi = iiddii.get(j);
                ValidacionImputacionDocumentoImputable vidi = idi.getValidacionImputacion();
                if (vidi.getEstadoValidacion() == ValidacionEstado.PENDIENTE){
                    al.add(fp);
                    continue a;
                }
            }
        }
        return al;
    }



}