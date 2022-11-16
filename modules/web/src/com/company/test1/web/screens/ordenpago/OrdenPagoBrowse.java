package com.company.test1.web.screens.ordenpago;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.ordenespago.*;
import com.company.test1.service.OrdenPagoService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_OrdenPago.browse")
@UiDescriptor("orden-pago-browse.xml")
@LookupComponent("ordenPagoesTable")
@LoadDataBeforeShow
public class OrdenPagoBrowse extends StandardLookup<OrdenPago> {

    @Inject
    private Table<OrdenPago> ordenPagosTable;
    @Inject
    private CollectionContainer<OrdenPago> ordenPagoesDc;
    @Inject
    private CollectionLoader<OrdenPago> ordenPagoesDl;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;
    @Inject
    private OrdenPagoService ordenPagoService;
    @Inject
    private UiComponents uiComponents;

    ArrayList<OrdenPago> ordenesParaCreacionRealizacionPago = new ArrayList<OrdenPago>();
    @Inject
    private LookupField<CuentaBancaria> lkpCB;
    @Inject
    private DataContext dataContext;
    @Inject
    private Filter filter;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Dialogs dialogs;


    @Subscribe("ordenPagosTable.create")
    private void onOrdenPagoesTableCreate(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchNewEntityStreen(OrdenPagoProveedor.class, null, ordenPagosTable, screenBuilders, this, OpenMode.DIALOG, null, null );
    }

    @Subscribe("ordenPagosTable.edit")
    private void onOrdenPagoesTableEdit(Action.ActionPerformedEvent event) {
        OrdenPago op = ordenPagosTable.getSingleSelected();
        if (op instanceof OrdenPagoProveedor) {
            OrdenPagoProveedor opp = (OrdenPagoProveedor) op;
            opp = dataManager.reload(opp, "ordenPagoProveedor-view");
            if (opp.getRealizacionPago() != null) {
                ScreenLaunchUtil.launchEditEntityScreen(ordenPagosTable.getSingleSelected(), "test1_OrdenPagoProveedorConCompensaciones.edit", ordenPagosTable, screenBuilders, this, OpenMode.DIALOG, null, null);
            } else {
                ScreenLaunchUtil.launchEditEntityScreen(ordenPagosTable.getSingleSelected(), null, ordenPagosTable, screenBuilders, this, OpenMode.DIALOG, null, null);
            }


        }else if(op instanceof OrdenPagoFacturaProveedor){
            ScreenLaunchUtil.launchEditEntityScreen(ordenPagosTable.getSingleSelected(), null, ordenPagosTable, screenBuilders, this, OpenMode.DIALOG, null, null);
    }   else{
            notifications.create().withCaption("Desarrollo Pendiente").withDescription("TEmporalmente solo se pueden editar ordenes de pago proveedor").show();
        }
    }

    @Subscribe("ordenPagosTable.remove")
    private void onOrdenPagosTableRemove(Action.ActionPerformedEvent event) {
        OrdenPago op = ordenPagosTable.getSingleSelected();
        if (op==null){
            notifications.create().withDescription("Seleccionar la Orden de Pago que desea eliminar").show();
            return;
        }
        if (op.getRealizacionPago()!=null){
            notifications.create().withDescription("La Orden de Pago seleccionada tiene un Pago Bancario asociado. Por favor retroceda éste primero.").show();
            return;
        }
        dataManager.remove(op);
        filter.getDataLoader().load();

        notifications.create().withDescription("La orden de pago por importe " + new DecimalFormat("#,##0.00").format(op.getImporteEfectivo()) + " fue eliminada satisfactoriamente").show();
    }
    
    

    public Component getColumnSeleccionOrdenPago(OrdenPago op){
        CheckBox chb = uiComponents.create(CheckBox.NAME);
        ArrayList<Class> al = new ArrayList<Class>();
        al.add(OrdenPagoProveedor.class);
        al.add(OrdenPagoFacturaProveedor.class);
        al.add(OrdenPagoContratoInquilino.class);
        if (al.indexOf(op.getClass())!=-1){
            if (op.getRealizacionPago()==null){
                chb.setEnabled(true);
                chb.addValueChangeListener(e->{
                    if (e.getValue()){
                        ordenesParaCreacionRealizacionPago.add(op);
                    }else{
                        ordenesParaCreacionRealizacionPago.remove(op);
                    }
                });
            }else{
                chb.setEnabled(false);
            }

        }
        return chb;
    }

//    public Component getBeneficiarioColumn(OrdenPago op) {
//        HBoxLayout hbx = uiComponents.create(HBoxLayout.NAME);
//        String t = "";
//        if (op instanceof OrdenPagoFacturaProveedor){
//            op = dataManager.reload(op, "ordenPagoFacturaProveedor-view");
//            t = ((OrdenPagoFacturaProveedor)op).getFacturaProveedor().getProveedor().getPersona().getNombreCompleto();
//        }
//        if (op instanceof OrdenPagoProveedor){
//            op = dataManager.reload(op, "ordenPagoProveedor-view");
//            t = ((OrdenPagoProveedor)op).getProveedor().getPersona().getNombreCompleto();
//        }
//        if (op instanceof OrdenPagoContratoInquilino){
//            op = dataManager.reload(op, "ordenPagoContratoInquilino-view");
//            t = ((OrdenPagoContratoInquilino)op).getContratoInquilino().getInquilino().getNombreCompleto();
//        }
//        return hbx;
//    }

    @Install(to = "cuentaBancariasDl", target = Target.DATA_LOADER)
    private List<CuentaBancaria> cuentaBancariasDlLoadDelegate(LoadContext<CuentaBancaria> loadContext) {

        List<CuentaBancaria> ccbb = ordenPagoService.devuelveCuentasBancariasPropietariosRegistrados();
        return ccbb;
    }


    public void onBtnCrearRealizacionPagoClick() {
        if (ordenesParaCreacionRealizacionPago.size()==0){
            notifications.create().withCaption("Seleccionar Ordenes de Pago").show();
            return;
        }else{
            ArrayList opclasses = new ArrayList();
            for (int i = 0; i < ordenesParaCreacionRealizacionPago.size(); i++) {
                OrdenPago op = ordenesParaCreacionRealizacionPago.get(i);
                if (opclasses.indexOf(op.getClass())==-1){
                    opclasses.add(op.getClass());
                }
                if (Math.abs(op.getImporteEfectivo())<0.01){
                    notifications.create().withCaption("Ha seleccionado ordenes de pago compensadas con importe efectivo igual a 0 - Por favor elimínelas de su seleccion").show();
                    return;
                }
            }
            if (opclasses.size()!=1){
                notifications.create().withCaption("No se puede generar un Realizacion Pago sobre ordenes de distinto tipo").show();
                return;
            }
        }

        if (lkpCB.getValue()==null){
            notifications.create().withCaption("Seleccionar una cuenta bancaria").show();
            return;
        }

        //si hay mas de un emisor que se detenga o si la cuenta bancaria no corresponde con el emisor tambien
        ArrayList al = new ArrayList();
        for (int i = 0; i < ordenesParaCreacionRealizacionPago.size(); i++) {
            OrdenPago op = ordenesParaCreacionRealizacionPago.get(i);
            String nombreEmisor = op.getEmisor().getNombreCompleto();
            if (nombreEmisor.compareTo("N/D")==0){
                continue;
            }
            if (al.indexOf(nombreEmisor)==-1){
                al.add(nombreEmisor);
            }
        }
        if (al.size()>1){
            notifications.create().withCaption("Se ha detectado que puede existir más de un emisor. Seleccionar ordenes asociadas a un único emisor.").show();
            return;
        }
        if (al.size()==1){
            CuentaBancaria cb = lkpCB.getValue();
            cb = dataManager.reload(cb, "cuentaBancaria-view");
            if (cb.getPersona()!=null){
                String n = cb.getPersona().getNombreCompleto();
                if (al.indexOf(n)!=0){
                    notifications.create().withCaption("La cuenta bancaria seleccionada no se corresponde con el emisor de las ordenes").show();
                    return;
                }
            }
//            if (cb.getProveedor()!=null){
//                String n = cb.getProveedor().getPersona().getNombreCompleto();
//                if (al.indexOf(n)!=0){
//                    notifications.create().withCaption("La cuenta bancaria seleccionada no se corresponde con el emisor de las ordenes").show();
//                    return;
//                }
//            }
        }


        try {
            RealizacionPago rp = ordenPagoService.crearRealizacionPagoDesdeListaOrdenesPago(ordenesParaCreacionRealizacionPago, lkpCB.getValue());
            ordenPagoService.guardaRealizacionPago(rp);
            //notifications.create().withCaption("Archivo de Pago " + rp.getIdentificador() + " guardado existosamente").withDescription("Acceda a los detalles mediante la pantalla de Realizaciones Pagos").show();
            final RealizacionPago rp_f = dataManager.reload(rp,"realizacionPago-view");
            dialogs.createOptionDialog(Dialogs.MessageType.CONFIRMATION)
                    .withMessage("Archivo de Pagos generador corréctamente. ¿Deseas descargarlo ahora?")
                    .withActions(
                            new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e->{
                                byte[] bb = rp_f.getSepa().getBytes();
                                exportDisplay.show(new ByteArrayDataProvider(bb), rp.getIdentificador() + ".xml");
                            }),
                            new DialogAction(DialogAction.Type.NO, Action.Status.PRIMARY).withHandler(e->{

                            })
                    );
        } catch (Exception e) {
            e.printStackTrace();
            notifications.create().withCaption("Error").withDescription(e.getMessage()).show();

        }


    }

    public void OnBtnVerReportClick(){
        byte[] bb = DynamicReportHelper.getReportDinamico("Listado Ordenes Pago", OrdenPago.class, ordenPagosTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Listado Ordenes Pago.pdf");
    }

    public void donwloadRelatedRealizacionPago(){
        try {
            RealizacionPago rp = ordenPagosTable.getSingleSelected().getRealizacionPago();
            if (rp==null){
                notifications.create().withCaption("Seleccionar Orden de Pago con Archivo de Pagos asociado").show();
                return;
            }
            byte[] bb = rp.getSepa().getBytes();
            exportDisplay.show(new ByteArrayDataProvider(bb), rp.getIdentificador() + ".xml");
        } catch (Exception e) {
            e.printStackTrace();
            notifications.create().withCaption("Error").withDescription(e.getMessage()).show();

        }
    }

//    public Component EmisorColumn(OrdenPago op){
//
//        String s = ordenPagoService.getNombreEmisor(op);
//        Label l = uiComponents.create(Label.NAME);
//        l.setValue(s);
//        return l;
//    }
}