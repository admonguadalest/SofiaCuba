package com.company.test1.web.screens.validaciones;

import com.company.test1.NumberUtils;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.entity.ordenespago.*;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;
import com.company.test1.service.OrdenPagoService;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.*;

@UiController("test1_GeneracionOrdenPagoFacturaProveedor")
@UiDescriptor("generacion-orden-pago-factura-proveedor.xml")
public class GeneracionOrdenPagoFacturaProveedor extends Screen {

    @Inject
    private CollectionLoader<OrdenPago> ordenesPagoDl;
    @Inject
    private OrdenPagoService ordenPagoService;

    ValidacionImputacionDocumentoImputable vidi;
    

    @Inject
    private UiComponents uiComponents;
    @Inject
    private DataManager dataManager;
    @Inject
    private Notifications notifications;
    @Inject
    private DataContext dataContext;

    FacturaProveedor facturaProveedor;

    OrdenPagoFacturaProveedor opfp;


    @Inject
    private InstanceContainer<OrdenPagoFacturaProveedor> ordenPagoFacturaProveedorDc;
    @Inject
    private TextField<Double> txtPagoEfectivo;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        ordenPagoFacturaProveedorDc.setItem(opfp);
        //una vez ya tenemos los datos cargados cargamos la info de la tabla
        ordenesPagoDl.load();
    }

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {

    }
    
    
    
    

    @Subscribe
    private void onBeforeClose(BeforeCloseEvent event) {
        if(opfp!=null) {
            try {
                List<CompensacionOrdenPagoProveedor> cops = devuelveListaDeMap(copp);
                opfp.getCompensaciones().addAll(cops);
                opfp.setFechaValor(new Date());


            } catch (Exception exc) {
                notifications.create().withCaption("Error al guardar compensaciones").withDescription("Se produjo un error de guardado").show();
            }
        }
    }


    



    HashMap<OrdenPago, CompensacionOrdenPagoProveedor> copp = new HashMap<OrdenPago, CompensacionOrdenPagoProveedor>();
    
    

    @Install(to = "ordenesPagoDl", target = Target.DATA_LOADER)
    private List<OrdenPago> ordenesPagoDlLoadDelegate(LoadContext<OrdenPago> loadContext) {

            List<OrdenPago> oopp = ordenPagoService.devuelveOrdenesPagoPendientesDeCompensacion(facturaProveedor.getProveedor(), false);
            ArrayList al = new ArrayList();
            Double importePendiente = 0.0;
        for (int i = 0; i < oopp.size(); i++) {
            OrdenPago op = oopp.get(i);
            if (op instanceof OrdenPagoProveedor){
                op = dataManager.reload(op, "ordenPagoProveedor-view");
                importePendiente = ((OrdenPagoProveedor)op).getImportePendienteCompensacion();
            }
            if (op instanceof OrdenPagoAbono){
                op = dataManager.reload(op, "ordenPagoAbono-view");
                importePendiente = ((OrdenPagoAbono)op).getImportePendienteCompensacion();
            }
            if (importePendiente>0.001){
                al.add(op);
            }
        }
            return al;


    }

    public Component getColumnImportePendienteCompensacion(OrdenPago op) {
        if (op instanceof OrdenPagoProveedor){
            op = dataManager.reload(op, "ordenPagoProveedor-view");
        }
        if (op instanceof OrdenPagoAbono){
            op = dataManager.reload(op, "ordenPagoAbono-view");
        }
        TextField<Double> tf = uiComponents.create(TextField.NAME);
        tf.setDatatype(Datatypes.get(Double.class));
        double importePendiente = 0.0;
        if (op instanceof OrdenPagoProveedor){
            importePendiente = ((OrdenPagoProveedor)op).getImportePendienteCompensacion();
        }
        if (op instanceof OrdenPagoAbono){
            importePendiente = ((OrdenPagoAbono)op).getImportePendienteCompensacion();
        }
//        Double ip = NumberUtils.formatToImporte(importePendiente);
        tf.setValue(importePendiente);
        tf.setEditable(false);
        return tf;
    }

    public Component getColumnCompensacion(OrdenPago op){
        TextField<Double> tf = uiComponents.create(TextField.NAME);

        Double d = ordenPagoService.getTotalImporteCompensadoDeOrdenPago(op);
        Double max = op.getImporte() - d;
        tf.addValueChangeListener(e->{
            Double importePendienteHastaComplecion = txtPagoEfectivo.getValue();
            importePendienteHastaComplecion = NumberUtils.roundTo2Decimals(importePendienteHastaComplecion);
            if (e.getValue() == null) return;
            try {
                Number value = NumberUtils.asNumber(e.getValue());
                if (value.doubleValue() > max) {
                    notifications.create().withCaption("Importe Superior al Permitido").withDescription("El importe inserido no puede superar " + max.toString()).show();
                    copp.remove(op);
                    tf.setValue(null);
                } else if (value.doubleValue() == 0) {
                    //nada
                    copp.remove(op);
                    tf.setValue(null);
                } else if (value.doubleValue() < 0) {
                    copp.remove(op);
                    notifications.create().withCaption("Importe Superior a Cero").withDescription("El importe inserido debe ser superior a 0.0").show();
                    tf.setValue(null);
                } else if (value.doubleValue() > importePendienteHastaComplecion){
                        notifications.create().withCaption("Importe Superior al Importe Pendiente").withDescription("El importe maximo pendiente es de " + importePendienteHastaComplecion).show();
                        tf.setValue(null);

                } else {
                    CompensacionOrdenPagoProveedor cop = copp.get(op);
                    if (cop == null) {
                        cop = dataContext.create(CompensacionOrdenPagoProveedor.class);
                        copp.put(op, cop);
                        if (op instanceof OrdenPagoProveedor) {
                            cop.setOrdenPagoProveedor((OrdenPagoProveedor) op);
                        }
                        if (op instanceof OrdenPagoAbono) {
                            cop.setOrdenPagoAbono((OrdenPagoAbono) op);
                        }
                        cop.setOrdenPagoFacturaProveedor(opfp);
                        cop.setImporte(value.doubleValue());


                    }
                }
            }catch(Exception exc){
                notifications.create().withCaption("El valor debe ser un numero").show();
            }
            actualizaImporteEfectivo();
        });
        return tf;
    }

    private void actualizaImporteEfectivo(){
        List<CompensacionOrdenPagoProveedor> cops = devuelveListaDeMap(copp);
        Double d = this.facturaProveedor.getImportePostCCAA();
        for (int i = 0; i < cops.size(); i++) {
            CompensacionOrdenPagoProveedor cop =  cops.get(i);
            d -= cop.getImporte();
        }
        opfp.setImporteEfectivo(d);
    }

    private List<CompensacionOrdenPagoProveedor> devuelveListaDeMap(HashMap<OrdenPago, CompensacionOrdenPagoProveedor> m){
        Iterator it = copp.keySet().iterator();
        ArrayList<CompensacionOrdenPagoProveedor> cops = new ArrayList<CompensacionOrdenPagoProveedor>();
        while (it.hasNext()) {
            CompensacionOrdenPagoProveedor cop = copp.get(it.next());
            cops.add(cop);
        }
        return cops;
    }


    public void setVidi(ValidacionImputacionDocumentoImputable vidi) {
        this.vidi = vidi;
    }


    public OrdenPagoFacturaProveedor getOrdenPagoFacturaProveedor() {
        return opfp;
    }

    public void setFacturaProveedor(FacturaProveedor facturaProveedor) {
        this.facturaProveedor = facturaProveedor;
        opfp = dataContext.create(OrdenPagoFacturaProveedor.class);
        opfp.setFacturaProveedor(this.facturaProveedor);
        opfp.setImporte(this.facturaProveedor.getImportePostCCAA());
        opfp.setImporteEfectivo(opfp.getImporte());

    }



    public void onBtnCancelarClick() {
        this.opfp = null;
        this.closeWithDefaultAction();
    }

    public void onBtnGuardarClick() {

        this.closeWithDefaultAction();
    }
}