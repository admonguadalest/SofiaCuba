package com.company.test1.web.screens.recibos;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.enums.recibos.ReciboGradoImpago;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.*;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.RecibosService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

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
    private CheckBox chkIncluirRemesa;
    @Inject
    private CollectionPropertyContainer<ImplementacionConcepto> implementacionConceptosDc;
    @Inject
    private LookupField<Serie> serieField;
    @Inject
    private LookupPickerField<ContratoInquilino> contratoField;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private TextField<String> numReciboField;
    @Inject
    private DateField<Date> fechaValorField;
    @Inject
    private DateField<Date> fechaEmisionField;
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
    @Inject
    private LookupField<DefinicionRemesa> remesaField;



    @Subscribe("chkIncluirRemesa")
    public void onChkIncluirRemesaValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.getValue()){
            remesaField.setEditable(true);
        }else{
            remesaField.setValue(null);
            remesaField.setEditable(false);
        }
    }


    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        //PENDIENTE: esto debería cambiarse a dataContext.create(), pero como funciona de momento
        //aun no lo cambio
        this.setEntityToEdit(new Recibo());
    }

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        reciboDc.getItem().setFechaValor(new Date());
        reciboDc.getItem().setFechaEmision(new Date());
        remesaField.setValue(null);
        remesaField.setEditable(false);
    }
    
    

    @Subscribe("contratoField")
    private void onContratoFieldValueChange(HasValue.ValueChangeEvent<ContratoInquilino> event) {
        if (!event.isUserOriginated()) return;
        generaNumeracionDeRecibo();
        
    }

    @Subscribe("fechaEmisionField")
    public void onFechaEmisionFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        if (!event.isUserOriginated()) return;
        generaNumeracionDeRecibo();
    }



    private void generaNumeracionDeRecibo(){
        ContratoInquilino ci = contratoField.getValue();
        Ubicacion ub = ci.getDepartamento().getUbicacion();
        ub = dataManager.reload(ub, "_base");
        ci = (ContratoInquilino) dataManager.reload(ci, "contratoInquilino-view");
        ci.getDepartamento().setUbicacion(ub);
        reciboDc.getItem().setUtilitarioInquilino(ci.getInquilino());
        inquilinoField.setValue(ci.getInquilino().getNombreCompleto());
        contratoField.setValue(ci);
        try{
            Date fechaEmision = reciboDc.getItem().getFechaEmision();
            String numRbo = recibosService.generaNuevoNumeroReciboSegunUbicacionYAno(ci.getDepartamento().getUbicacion(), fechaEmision);
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

    public void onBtnCancelarClick(){
        this.closeWithDiscard();
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

        DefinicionRemesa dr_ = remesaField.getValue();
        Departamento d = r.getUtilitarioContratoInquilino().getDepartamento();
        d = dataManager.reload(d, "departamento-view");
        dr_ = dataManager.reload(dr_, "definicionRemesa-view");
        if (d.getPropietarioEfectivo().getId().compareTo(dr_.getPropietario().getId())!=0){
            notifications.create().withCaption("Los propietarios de la Definición de Remesa y del Departamento afectado no coinciden").show();
            return;
        }


        if ((r.getTotalRecibo()==null)||(r.getImplementacionesConceptos().size()==0)){
            notifications.create().withCaption("Por favor completar datos").show();
            return;
        }
        if (r.getSerie()==null){
            notifications.create().withCaption("Por favor indicar serie").show();
            return;
        }
        try {
            ContratoInquilino ci = contratoField.getValue();
            ProgramacionRecibo pr = contratoField.getValue().getProgramacionRecibo();
            DefinicionRemesa dr = pr.getDefinicionRemesa();

            if (chkIncluirRemesa.isChecked()) {
                if (remesaField.getValue()==null){
                    notifications.create().withCaption("Por favor seleccionar una definición de remesa").show();
                    return;
                }
                dr = remesaField.getValue();
                Remesa rem = dataContext.create(Remesa.class);
                rem.setDefinicionRemesa(dr);
                rem.setFechaAdeudo(fechaEmisionField.getValue());
                rem.setFechaRealizacion(fechaEmisionField.getValue());
                rem.setFechaValor(fechaEmisionField.getValue());
                rem.setTotalRemesa(r.getTotalReciboPostCCAA());
                String nombreDefinicionRemesa = ci.getProgramacionRecibo().getDefinicionRemesa().getNombreRemesa();
                String abrevUbicDepto = ci.getDepartamento().getUbicacion().getAbreviacionUbicacion() + ci.getDepartamento().getAbreviacionPisoPuerta();
                String identificadorRemesa = recibosService.generaIdentificadorRemesa(nombreDefinicionRemesa, fechaEmisionField.getValue(), abrevUbicDepto);
                rem.setIdentificadorRemesa(identificadorRemesa);
                OrdenanteRemesa or = dataContext.create(OrdenanteRemesa.class);
                rem.getOrdenantesRemesa().add(or);
                r.setOrdenanteRemesa(or);
                or.setRemesa(rem);

            }


            r.setGradoEstadoImpago(ReciboGradoImpago.ORDINARIO);

            r.setUtilitarioContratoInquilino(ci);
            r.setUtilitarioInquilino(ci.getInquilino());

            this.closeWithCommit();
            notifications.create().withCaption("Recibo registrado correctamente").show();

            recibosService.registraReciboEnTablaZHelper(r);

        }catch(Exception exc){
            notifications.create().withCaption("Error").withDescription(exc.getMessage()).show();
        }
    }



}