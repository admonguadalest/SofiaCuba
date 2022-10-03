package com.company.test1.web.screens.ordenpago;

import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.entity.ordenespago.OrdenPago;
import com.company.test1.entity.ordenespago.OrdenPagoAbono;
import com.company.test1.entity.ordenespago.OrdenPagoProveedor;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.OrdenPagoService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.*;

@UiController("test1_OrdenesPagoProveedorPendientesCompensacion")
@UiDescriptor("ordenes-pago-proveedor-pendientes-compensacion.xml")
public class OrdenesPagoProveedorPendientesCompensacion extends Screen {

    @Inject
    private CollectionLoader<OrdenPagoProveedor> ordenesPagoDl;
    @Inject
    private LookupPickerField<Proveedor> lkpProveedorField;
    @Inject
    private DataManager dataManager;
    @Inject
    private OrdenPagoService ordenPagoService;
    @Inject
    private GroupTable<OrdenPago> tblOOPP;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private CollectionContainer<OrdenPago> ordenesPagoDc;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Notifications notifications;

    @Subscribe("lkpProveedorField")
    public void onLkpProveedorFieldValueChange(HasValue.ValueChangeEvent<Proveedor> event) {
        ordenesPagoDl.load();
    }

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {

        tblOOPP.addGeneratedColumn("Pendiente de Compensar", e->{
            double compensado = ordenPagoService.getTotalImporteCompensadoDeOrdenPago(e);
            double pendiente = e.getImporte()-compensado;
            Label l = uiComponents.create(Label.NAME);
            l.setValue(pendiente);
            return l;
        });

    }







    @Install(to = "ordenesPagoDl", target = Target.DATA_LOADER)
    private List<OrdenPago> ordenesPagoProveedorDlLoadDelegate(LoadContext<OrdenPagoProveedor> loadContext) {
        Proveedor prov = lkpProveedorField.getValue();
        if (prov!=null){
            List<OrdenPago> oopp = ordenPagoService.devuelveOrdenesPagoPendientesDeCompensacion(prov);
            for (int i = 0; i < oopp.size(); i++) {
                OrdenPago op = oopp.get(i);
                if (op instanceof OrdenPagoProveedor){
                    OrdenPagoProveedor opp = (OrdenPagoProveedor) op;
                    opp = dataManager.reload(opp, "ordenPagoProveedor-view");
                    oopp.set(i, opp);
                }
                if (op instanceof OrdenPagoAbono){
                    OrdenPagoAbono opa = (OrdenPagoAbono) op;
                    opa = dataManager.reload(opa, "ordenPagoAbono-view");
                    oopp.set(i, opa);
                }
            }
            return oopp;
        }else{
            return new ArrayList();
        }
    }

    public void report(){
        try {
            Collection<Entity> col = (Collection) ordenesPagoDc.getItems();
            byte[] bb = jasperReportService.getReportDinamico(
                    "Pagos a Cta. Pendientes de Compensar",
                    OrdenPago.class,
                    col,
                    Arrays.asList(new String[]{"beneficiario.nombreCompleto","nombreTipo","emisor.nombreCompleto","fechaValor", "importe","descripcion"}),
                    Arrays.asList(new String[]{"Beneficiario","Tipo", "Emisor", "Fecha Pago", "Importe", "Descripcion"})
            );
            exportDisplay.show(new ByteArrayDataProvider(bb), "Pagos A Cuenta " + lkpProveedorField.getValue().getPersona().getNombreCompleto());
        }catch(Exception exc){
            notifications.create().withDescription("No se pudo producir el report").show();
        }
    }



}