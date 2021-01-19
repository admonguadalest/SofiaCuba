package com.company.test1.web.screens.conceptosadicionales;

import com.company.test1.entity.conceptosadicionales.ConceptoAdicional;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.enums.conceptosadicionales.ConceptosAdicionalesIRPF;
import com.company.test1.entity.enums.conceptosadicionales.ConceptosAdicionalesIVA;
import com.company.test1.entity.recibos.ImplementacionConcepto;
import com.company.test1.entity.recibos.Recibo;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;

import javax.inject.Inject;
import java.util.Date;

@UiController("test1_RegistroAplicacionConceptoAdicional.edit")
@UiDescriptor("registro-aplicacion-concepto-adicional-edit.xml")
@EditedEntityContainer("registroAplicacionConceptoAdicionalDc")
@LoadDataBeforeShow
public class RegistroAplicacionConceptoAdicionalEdit extends StandardEditor<RegistroAplicacionConceptoAdicional> {
    @Inject
    private LookupField porcentajeField;
    @Inject
    private TextField<String> numDocumentoField;
    @Inject
    private TextField<Double> importeAplicadoField;
    @Inject
    private TextField<String> nifDniField;
    @Inject
    private DateField<Date> fechaValorField;
    @Inject
    private InstanceContainer<RegistroAplicacionConceptoAdicional> registroAplicacionConceptoAdicionalDc;
    @Inject
    private TextField<Double> baseField;
    @Inject
    private DataManager dataManager;

    @Subscribe
    private void onAfterShow1(AfterShowEvent event) {
        Double perc = registroAplicacionConceptoAdicionalDc.getItem().getPorcentaje();
        if (perc!=null){
            int i = (int)(perc * 100);
            if (registroAplicacionConceptoAdicionalDc.getItem().getConceptoAdicional().getAbreviacion().compareTo("IVA")==0){
                porcentajeField.setValue(ConceptosAdicionalesIVA.fromId(i));
            }
            if (registroAplicacionConceptoAdicionalDc.getItem().getConceptoAdicional().getAbreviacion().compareTo("IRPF")==0){
                porcentajeField.setValue(ConceptosAdicionalesIRPF.fromId(i));
            }

        }
    }
    
    

    @Subscribe("conceptoAdicionalField")
    private void onConceptoAdicionalFieldValueChange(HasValue.ValueChangeEvent<ConceptoAdicional> event) {
        if (event.getValue().getAbreviacion().compareTo("IVA")==0){
            porcentajeField.setOptionsEnum(ConceptosAdicionalesIVA.class);
        }
        if (event.getValue().getAbreviacion().compareTo("IRPF")==0){
            porcentajeField.setOptionsEnum(ConceptosAdicionalesIRPF.class);
        }
    }

    @Subscribe("porcentajeField")
    private void onPorcentajeFieldValueChange(HasValue.ValueChangeEvent event) {
        Object value = event.getValue();
        double perc = 0.0;
        if (value instanceof ConceptosAdicionalesIVA){
            ConceptosAdicionalesIVA caiva = (ConceptosAdicionalesIVA)value;
            perc = ((double)caiva.getId()/ 100);
        }
        if (value instanceof ConceptosAdicionalesIRPF){
            ConceptosAdicionalesIRPF cairpf = (ConceptosAdicionalesIRPF)value;
            perc = ((double)cairpf.getId()/ 100);
        }
        registroAplicacionConceptoAdicionalDc.getItem().setPorcentaje(perc);
        registroAplicacionConceptoAdicionalDc.getItem().setImporteAplicado(registroAplicacionConceptoAdicionalDc.getItem().getBase()*perc);
    }
    
    
    
    @Inject
    private PickerField<ConceptoAdicional> conceptoAdicionalField;
    Recibo recibo = null;

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    ImplementacionConcepto implementacionConcepto = null;

    public ImplementacionConcepto getImplementacionConcepto() {
        return implementacionConcepto;
    }

    public void setImplementacionConcepto(ImplementacionConcepto implementacionConcepto) {
        this.implementacionConcepto = implementacionConcepto;
    }

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        if (recibo!=null) {
            ContratoInquilino ci = recibo.getUtilitarioContratoInquilino();
            ci = dataManager.reload(ci, "contratoInquilino-view");
            numDocumentoField.setValue(recibo.getNumRecibo());
            nifDniField.setValue(ci.getDepartamento().getPropietarioEfectivo().getPersona().getNifDni());
            baseField.setValue(implementacionConcepto.getImporte());
        }

    }

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {

    }

    
    
    
    
    
}