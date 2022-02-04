package com.company.test1.web.screens;

import com.company.test1.entity.ItemNoEmitidoOAnomalo;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.service.NoEmitidosOAnomalosService;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_NoEmitidosOAnomalos")
@UiDescriptor("no-emitidos-o-anomalos.xml")
public class NoEmitidosOAnomalos extends Screen {
    @Inject
    private DateField<Date> fechaDesdeField;
    @Inject
    private DateField<Date> fechaHastaField;
    @Inject
    private CollectionLoader<ItemNoEmitidoOAnomalo> itemsDl;
    @Inject
    private NoEmitidosOAnomalosService noEmitidosOAnomalosService;
    @Inject
    private Notifications notifications;

    @Subscribe("cargar")
    public void onCargarClick(Button.ClickEvent event) {
        itemsDl.load();


    }

    @Install(to = "itemsDl", target = Target.DATA_LOADER)
    private List<ItemNoEmitidoOAnomalo> itemsDlLoadDelegate(LoadContext<ItemNoEmitidoOAnomalo> loadContext) {
        List<ItemNoEmitidoOAnomalo> l = new ArrayList();
        try{
            Date fechaDesde = fechaDesdeField.getValue();
            Date fechaHasta = fechaHastaField.getValue();

            List<Departamento> dd = noEmitidosOAnomalosService.getDepartamentosMonitorizados();
            for (int i = 0; i < dd.size(); i++) {
                Object[] oo = noEmitidosOAnomalosService.reportarDepartamento(dd.get(i), fechaDesde, fechaHasta);
                if ((boolean)oo[0]){
                    ItemNoEmitidoOAnomalo item = new ItemNoEmitidoOAnomalo();
                    item.setDepartamento(dd.get(i));
                    item.setInformacionReportada((String)oo[1]);
                    l.add(item);
                }


            }
            return l;
        }catch(Exception exc){
            notifications.create().withDescription(exc.getMessage()).show();
        }
    }





}