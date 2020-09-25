package com.company.test1.web.screens.recibos;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.recibos.ImplementacionConcepto;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.RecibosService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.Recibo;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@UiController("test1_ReciboIndividualizado.edit")
@UiDescriptor("recibo-individualizado-edit.xml")
@EditedEntityContainer("reciboDc")
@LoadDataBeforeShow
public class ReciboIndividualizadoEdit extends StandardEditor<Recibo> {


    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataContext dataContext;
    @Inject
    private Table<ImplementacionConcepto> tableImplementacionesConceptos;
    @Inject
    private TextField<Double> totalReciboField;
    @Inject
    private TextField<Double> totalReciboPostCCAA;
    @Inject
    private InstanceContainer<Recibo> reciboDc;
    @Inject
    private TextField<String> inquilinoField;
    @Inject
    private RecibosService recibosService;
    @Inject
    private Notifications notifications;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private DataManager dataManager;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        this.setEntityToEdit(new Recibo());
    }

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        reciboDc.getItem().setFechaValor(new Date());
        reciboDc.getItem().setFechaEmision(new Date());
    }
    
    

    @Subscribe("contratoField")
    private void onContratoFieldValueChange(HasValue.ValueChangeEvent<ContratoInquilino> event) {
        ContratoInquilino ci = event.getValue();
        Ubicacion ub = ci.getDepartamento().getUbicacion();
        ub = dataManager.reload(ub, "_base");
        ci.getDepartamento().setUbicacion(ub);
        reciboDc.getItem().setUtilitarioInquilino(ci.getInquilino());
        inquilinoField.setValue(ci.getInquilino().getNombreCompleto());
        try{
            String numRbo = recibosService.generaNuevoNumeroReciboSegunUbicacionYAno(ci.getDepartamento().getUbicacion(), reciboDc.getItem().getFechaValor());
            reciboDc.getItem().setNumRecibo(numRbo);
        }catch(Exception exc){
            notifications.create().withDescription(exc.getMessage()).show();
        }
        
    }


    
    
    
    

    @Subscribe("tableImplementacionesConceptos.create")
    private void onTableImplementacionesConceptosCreate(Action.ActionPerformedEvent event) {
        if (reciboDc.getItem().getUtilitarioContratoInquilino()==null){
            notifications.create().withCaption("Seleccionar Contrato").withDescription("Seleccione primeramente un contrato para continuar").show();
            return;
        }

        ScreenLaunchUtil.launchNewEntityStreen(ImplementacionConcepto.class, null, tableImplementacionesConceptos, screenBuilders, this, OpenMode.DIALOG,
                dataContext, s->{actualizaTotales();},
                sab->{
                    ((ImplementacionConceptoEdit)sab).setRecibo(reciboDc.getItem());
        });
    }

    @Subscribe("tableImplementacionesConceptos.edit")
    private void onTableImplementacionesConceptosEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableImplementacionesConceptos.getSingleSelected(), null, tableImplementacionesConceptos, screenBuilders, this, OpenMode.DIALOG,
                dataContext, s->{actualizaTotales();});
    }

    private void actualizaTotales(){
        double totalrbo = 0.0;
        double totalrbopca = 0.0;
        List<ImplementacionConcepto> iicc = reciboDc.getItem().getImplementacionesConceptos();
        for (int i = 0; i < iicc.size(); i++) {
            ImplementacionConcepto implementacionConcepto =  iicc.get(i);
            if (implementacionConcepto.getOverrideConcepto().getAdicionSustraccion()){
                totalrbo += implementacionConcepto.getImporte();
                totalrbopca += implementacionConcepto.getImportePostCCAA();
            }
            if (!implementacionConcepto.getOverrideConcepto().getAdicionSustraccion()){
                totalrbo -= implementacionConcepto.getImporte();
                totalrbopca -= implementacionConcepto.getImportePostCCAA();
            }
        }
        totalReciboField.setValue(totalrbo);
        totalReciboPostCCAA.setValue(totalrbopca);
    }




    public void onBtnImprimirClick() {
        Recibo r = this.getEditedEntity();
        if ((r.getTotalRecibo()==null)||(r.getImplementacionesConceptos().size()==0)){
            notifications.create().withCaption("Por favor completar datos").show();
            return;
        }
        try {
            byte[] bb = jasperReportService.devuelveReportPrevisualizacionRecibo(this.getEditedEntity());
            exportDisplay.show(new ByteArrayDataProvider(bb), "Recibo " + this.getEditedEntity().getNumRecibo() + ".pdf");
        }catch(Exception exc){
            notifications.create().withCaption(exc.getMessage());
        }

    }

    public void onBtnRealizarClick() {
        Recibo r = this.getEditedEntity();
        if ((r.getTotalRecibo()==null)||(r.getImplementacionesConceptos().size()==0)){
            notifications.create().withCaption("Por favor completar datos").show();
            return;
        }
        this.closeWithCommit();
    }

}