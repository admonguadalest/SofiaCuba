package com.company.test1.web.screens.realizacioncobro;

import com.company.test1.entity.enums.recibos.ReciboCobradoModoIngreso;
import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.ReciboCobrado;
import com.company.test1.service.OrdenCobroService;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenescobro.RealizacionCobro;


import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_RealizacionCobro.browse")
@UiDescriptor("realizacion-cobro-browse.xml")
@LookupComponent("realizacionCobroesTable")
@LoadDataBeforeShow
public class RealizacionCobroBrowse extends StandardLookup<RealizacionCobro> {
    @Inject
    private Notifications notifications;

    @Inject
    private Table<RealizacionCobro> realizacionCobroesTable;
    @Inject
    private DataManager dataManager;
    @Inject
    private Filter filter;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Dialogs dialogs;
    @Inject
    private OrdenCobroService ordenCobroService;

    public void onBtnDescargarSepaClick() {
        RealizacionCobro rc = realizacionCobroesTable.getSingleSelected();
        if (rc==null){
            notifications.create().withCaption("Seleccionar un archivo de pagos").show();
            return;
        }
        String txt = rc.getSepa();
        exportDisplay.show(new ByteArrayDataProvider(txt.getBytes()), rc.getIdentificador()+".xml");
    }

    public void onBtnDescargarReportDetalleCobro(){
        RealizacionCobro rc = realizacionCobroesTable.getSingleSelected();
        if (rc==null){
            notifications.create().withCaption("Seleccionar un archivo de pagos").show();
            return;
        }
        try{
            byte[] bb = ordenCobroService.generaReportDetalleCobro(rc);
            exportDisplay.show(new ByteArrayDataProvider(bb), rc.getIdentificador()+".xml");
        }catch(Exception exc){
            notifications.create().withDescription(exc.getMessage()).withCaption("Error").show();
        }

    }

    public void retrocederRealizacionCobro(){

        dialogs.createOptionDialog().withCaption("¿Desea retroceder el registro de cobro seleccionado?").withActions(
                new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e -> {
                    realizaRetrocesionSeleccion();
                }),
                new DialogAction(DialogAction.Type.NO)
        ).show();

    }

    private void realizaRetrocesionSeleccion(){
        RealizacionCobro rc = realizacionCobroesTable.getSingleSelected();

        rc = dataManager.reload(rc, "realizacionCobro-view");
        if (rc==null){
            notifications.create().withCaption("Seleccionar un Realización Cobro").show();
            return;
        }
        ArrayList toremove = new ArrayList();
        ArrayList toupdate = new ArrayList();
        toremove.add(rc);
        List<Recibo> recibos = new ArrayList<Recibo>();
        for (int i = 0; i < rc.getOrdenesCobro().size(); i++) {
            OrdenCobro oc = (OrdenCobro) rc.getOrdenesCobro().get(i);
            Recibo r = oc.getRecibo();
            oc.setRealizacionCobro(null);
            toupdate.add(oc);
            r = dataManager.reload(r, "recibo-view");
            for (int j = 0; j < r.getRecibosCobrados().size(); j++) {
                ReciboCobrado rcb = r.getRecibosCobrados().get(j);
                if (rcb.getModoIngreso()== ReciboCobradoModoIngreso.BANCARIO){
                    if (rcb.getTotalIngreso().doubleValue()==oc.getImporte().doubleValue()){
                        toremove.add(rcb);
                    }
                }

            }
        }

        dataManager.commit(new CommitContext(new ArrayList(), toremove));
        dataManager.commit(new CommitContext(toupdate, new ArrayList()));

        filter.getDataLoader().load();
    }
}