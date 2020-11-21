package com.company.test1.web.screens.realizacionpago;

import com.company.test1.entity.ordenespago.*;
import com.company.test1.service.JasperReportService;
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

import javax.inject.Inject;
import java.util.List;

@UiController("test1_RealizacionPago.browse")
@UiDescriptor("realizacion-pago-browse.xml")
@LookupComponent("realizacionPagoesTable")
@LoadDataBeforeShow
public class RealizacionPagoBrowse extends StandardLookup<RealizacionPago> {


    @Inject
    private Table<RealizacionPago> realizacionPagoesTable;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Filter filter;

    public void onBtnDownloadSepaClick() {
        RealizacionPago rp = realizacionPagoesTable.getSingleSelected();
        if (rp == null){
            notifications.create().withCaption("Seleccionar una Realizacion Pago para descargar la version SEPA del mismo").show();
            return ;
        }
        byte[] bb = rp.getSepa().getBytes();
        exportDisplay.show(new ByteArrayDataProvider(bb), rp.getIdentificador() + ".xml");
    }

    public void onBtnDetalleRp(){
        RealizacionPago rp = realizacionPagoesTable.getSingleSelected();
        for (int i = 0; i < rp.getOrdenesPago().size(); i++) {
            OrdenPago op = rp.getOrdenesPago().get(i);
            if (op instanceof OrdenPagoFacturaProveedor){
                op = dataManager.reload(op, "ordenPagoFacturaProveedor-view");
                rp.getOrdenesPago().set(i, op);
            }
            if (op instanceof OrdenPagoProveedor){
                op = dataManager.reload(op, "ordenPagoProveedor-view");
                rp.getOrdenesPago().set(i, op);
            }
            if (op instanceof OrdenPagoContratoInquilino){
                op = dataManager.reload(op, "ordenPagoContratoInquilino-view");
                rp.getOrdenesPago().set(i, op);
            }
        }
        if (rp == null){
            notifications.create().withCaption("Seleccionar una Realizacion Pago para descargar la version SEPA del mismo").show();
            return ;
        }
        try {
            byte[] bb = jasperReportService.realizaReportRealizacionPago(rp);
            exportDisplay.show(new ByteArrayDataProvider(bb), rp.getIdentificador()+".pdf");
        } catch (Exception e) {
            e.printStackTrace();
            notifications.create().withCaption("Error").withDescription(e.getMessage()).show();
        }


    }

    public void onBtnRetrocederClick(){
        RealizacionPago rp = realizacionPagoesTable.getSingleSelected();
        if (rp==null){
            notifications.create().withCaption("Seleccionar Pago Bancario a eliminar").show();
            return;
        }

        dialogs.createOptionDialog(Dialogs.MessageType.CONFIRMATION).withCaption("Confirmar Acción")
                .withMessage("¿Está seguro que quiere borrar el Pago Bancario Seleccionado?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e -> {
                            List<OrdenPago> oopp = rp.getOrdenesPago();
                            for (int i = 0; i < oopp.size(); i++) {
                                OrdenPago op = oopp.get(i);
                                op.setRealizacionPago(null);
                                dataManager.commit(op);
                            }
                            dataManager.remove(rp);
                            filter.getDataLoader().load();
                        }),
                        new DialogAction(DialogAction.Type.NO)
                )
                .show();


    }


}