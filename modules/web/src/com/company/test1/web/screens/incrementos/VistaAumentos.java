package com.company.test1.web.screens.incrementos;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.service.JasperReportService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UiController("test1_VistaAumentos")
@UiDescriptor("vista-aumentos.xml")
public class VistaAumentos extends Screen {

    @Inject
    private DateField<Date> dteFechaDesde;
    @Inject
    private CollectionContainer<Concepto> conceptoesDc;
    @Inject
    private CollectionContainer<ConceptoRecibo> conceptoReciboesDc;
    @Inject
    private CollectionContainer<Propietario> propietariosDc;
    @Inject
    private CollectionContainer<Ubicacion> ubicacionsDc;
    @Inject
    private CollectionLoader<Concepto> conceptoesDl;
    @Inject
    private CollectionLoader<ConceptoRecibo> conceptoReciboesDl;
    @Inject
    private CollectionLoader<Propietario> propietariosDl;
    @Inject
    private CollectionLoader<Ubicacion> ubicacionsDl;
    @Inject
    private DateField<Date> dteFechaHasta;
    @Inject
    private LookupField<Propietario> lkpPropietario;
    @Inject
    private Table<Concepto> tblConceptos;
    @Inject
    private Table<Ubicacion> tblUbicaciones;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;
    @Inject
    private Table<ConceptoRecibo> tblConceptosRecibos;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private JasperReportService jasperReportService;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        propietariosDl.load();
        conceptoesDl.load();
    }
    
    


    @Install(to = "conceptoReciboesDl", target = Target.DATA_LOADER)
    private List<ConceptoRecibo> conceptoReciboesDlLoadDelegate(LoadContext<ConceptoRecibo> loadContext) {
        return null;
    }

    @Install(to = "ubicacionsDl", target = Target.DATA_LOADER)
    private List<Ubicacion> ubicacionsDlLoadDelegate(LoadContext<Ubicacion> loadContext) {
        Propietario p = lkpPropietario.getValue();
        if (p==null){
            notifications.create().withCaption("Seleccionar un Propietario");
            return new ArrayList();
        }
        String hql = "select u from test1_Ubicacion u join u.departamentos d left join u.propietario p1 left join d.propietario p2 where " +
                "p1.id = :pid or p2.id = :pid";
        List<Ubicacion> uu = dataManager.loadValue(hql, Ubicacion.class).parameter("pid", p.getId()).list();
        return uu;
    }

    @Subscribe("lkpPropietario")
    private void onLkpPropietarioValueChange(HasValue.ValueChangeEvent<Propietario> event) {
        ubicacionsDl.load();
    }


    public void onBtnBuscarClick() {
        Propietario p = lkpPropietario.getValue();
        List<Ubicacion> uu = new ArrayList(tblUbicaciones.getSelected());
        List<UUID> idsubicaciones = new ArrayList<UUID>();
        for (int i = 0; i < uu.size(); i++) {
            idsubicaciones.add(uu.get(i).getId());
        }
        List<Concepto> conceptos = new ArrayList(tblConceptos.getSelected());
        List<UUID> idsconceptos = new ArrayList<UUID>();
        for (int i = 0; i < conceptos.size(); i++) {
            idsconceptos.add(conceptos.get(i).getId());
        }

        Date fechaDesde = dteFechaDesde.getValue();
        Date fechaHasta = dteFechaHasta.getValue();

        String hql = "select cr from test1_ConceptoRecibo cr JOIN cr.programacionRecibo pr JOIN pr.contratoInquilino ci JOIN ci.departamento d JOIN d.ubicacion u " +
                "JOIN cr.concepto c LEFT JOIN d.propietario p1 LEFT JOIN u.propietario p2 " +
                "WHERE u.id in :idsubicaciones and c.id in :idsconceptos and cr.fechaValor >= :fd and cr.fechaValor <= :fh " +
                "and (p1 = :p or p2 = :p)";

        List<ConceptoRecibo> ccrr = dataManager.loadValue(hql, ConceptoRecibo.class).parameter("fd", fechaDesde)
                .parameter("fh", fechaHasta).parameter("p", p).parameter("idsubicaciones", idsubicaciones).parameter("idsconceptos", idsconceptos).list();
        for (int i = 0; i < ccrr.size(); i++) {
            ccrr.set(i, dataManager.reload(ccrr.get(i), "conceptoRecibo-vistaAumentos-view"));
        }

        conceptoReciboesDc.setItems(ccrr);

    }

    public void onBtnTodosConceptosClick() {
        tblConceptos.setSelected(tblConceptos.getItems().getItems());
    }

    public void onBtnTodasUbicacionesClick() {
        tblUbicaciones.setSelected(tblUbicaciones.getItems().getItems());
    }

    public void onBtnCerrarClick() {
        this.closeWithDefaultAction();
    }

    public void onBtnVerReportClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Conceptos de Recibo Entre Fechas", ConceptoRecibo.class, tblConceptosRecibos);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Conceptos Recibo Entre Fechas.pdf");
    }

    public void onBtnPdfClick() {

        if (dteFechaDesde.getValue()==null){
            notifications.create().withCaption("Entrar fecha desde");
        }
        if (dteFechaHasta.getValue()==null){
            notifications.create().withCaption("Seleccionar fecha valor hasta");
        }
        Date fechaDesde = dteFechaDesde.getValue();
        Date fechaHasta = dteFechaHasta.getValue();
        //obteniendo llista de contratos
        ArrayList<ContratoInquilino> contratos = new ArrayList<ContratoInquilino>();
        List<ConceptoRecibo> ccrr = new ArrayList(tblConceptosRecibos.getItems().getItems());
        for (int i = 0; i < ccrr.size(); i++) {
            ConceptoRecibo conceptoRecibo = ccrr.get(i);
            ContratoInquilino c = conceptoRecibo.getProgramacionRecibo().getContratoInquilino();
            if (contratos.indexOf(c)==-1) contratos.add(c);
        }


        byte[] bb = jasperReportService.reportListadoAumentos(contratos, fechaDesde, fechaHasta);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Listado Aumentos.pdf");

    }
}