package com.company.test1.web.screens.recibos;

import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.company.test1.entity.recibos.OrdenanteRemesa;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.RecibosService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.Remesa;
import com.haulmont.cuba.gui.screen.LookupComponent;

import javax.inject.Inject;
import java.util.ArrayList;

@UiController("test1_Remesa.browse")
@UiDescriptor("remesa-browse.xml")
@LookupComponent("remesasTable")
@LoadDataBeforeShow
public class RemesaBrowse extends StandardLookup<Remesa> {


    @Inject
    private RecibosService recibosService;
    @Inject
    private UiComponents uiComponents;
    @Inject
    JasperReportService jasperReportService;
    @Inject
    private Table<Remesa> remesasTable;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;
    @Inject
    private Filter filter;
    @Inject
    private Dialogs dialogs;


    public void onBtnPdfRemesaClick() {
        try {
            byte[] bb = jasperReportService.listadoResumenRecibosFromZHelper(new ArrayList(remesasTable.getSelected()));
            exportDisplay.show(new ByteArrayDataProvider(bb), "Resumen de Remesa.pdf");
        }catch(Exception exc){
            notifications.create().withDescription(exc.getMessage()).show();
        }
    }

    public void onBtnReportDinamicoClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Listado de Remesas", Remesa.class, remesasTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "ListadoRemesas.pdf");
    }

    public void onRemoveBtnClick() {

        dialogs.createOptionDialog().withCaption("¿Desea retroceder la remesa seleccionada?").withActions(
                new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e -> {
                    realizaRetrocesionRemesa();
                }),
                new DialogAction(DialogAction.Type.NO)
        ).show();
    }

    private void realizaRetrocesionRemesa(){
        ArrayList toupdate = new ArrayList();
        ArrayList toremove = new ArrayList();
        Remesa r = remesasTable.getSingleSelected();
        if (r == null){
            notifications.create().withCaption("Seleccionar una remesa a retroceder").show();
            return;
        }
        toremove.add(r);
        r = dataManager.reload(r, "remesa-view-retrocesion");
        for (int i = 0; i < r.getOrdenantesRemesa().size(); i++) {
            OrdenanteRemesa or = r.getOrdenantesRemesa().get(i);
            toremove.add(or);
            for (int j = 0; j < or.getRecibos().size(); j++) {
                Recibo rbo = or.getRecibos().get(j);
                toremove.add(rbo);
                if (rbo.getOrdenesCobro().size()>0){
                    for (int k = 0; k < rbo.getOrdenesCobro().size(); k++) {
                        OrdenCobro oc = rbo.getOrdenesCobro().get(k);
                        if (oc.getRealizacionCobro()!=null){
                            notifications.create().withCaption("Se ha detectado que existe un Realizacion Cobro en uno o varios de los recibos asociados. Retroceda el Realización Cobro previamente.").show();
                            return;
                        }
                        toremove.add(oc);
                    }
                }
                rbo.setOrdenanteRemesa(null);

            }
        }
        dataManager.commit(new CommitContext(toupdate,toremove));
        filter.getDataLoader().load();
    }
}