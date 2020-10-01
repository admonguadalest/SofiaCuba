package com.company.test1.web.screens.recibos;

import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.conceptosadicionales.RegistroAplicacionConceptoAdicionalEdit;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.ImplementacionConcepto;

import javax.inject.Inject;
import java.util.List;

@UiController("test1_ImplementacionConcepto.edit")
@UiDescriptor("implementacion-concepto-edit.xml")
@EditedEntityContainer("implementacionConceptoDc")
@LoadDataBeforeShow
public class ImplementacionConceptoEdit extends StandardEditor<ImplementacionConcepto> {

    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataContext dataContext;
    @Inject
    private Table<ImplementacionConcepto> tableRACA;
    @Inject
    private TextField<Double> importePostCCAAField;
    @Inject
    private TextField<Double> importeField;
    @Inject
    private InstanceContainer<ImplementacionConcepto> implementacionConceptoDc;

    Recibo recibo = null;
    @Inject
    private Notifications notifications;

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    @Inject
    private PickerField<Concepto> overrideConceptoField;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {

    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if ((overrideConceptoField.getValue()==null)||(importeField.getValue()==null)){
            notifications.create().withCaption("Indicar Concepto e Importe").show();
            event.preventCommit();
            return;
        }

    }
    
    
    
    

    @Subscribe("tableRACA.create")
    private void onTableRACAreate(Action.ActionPerformedEvent event) {

        if (implementacionConceptoDc.getItem().getImporte()==null){
            notifications.create().withCaption("Importe Requerido").withDescription("Aportar Importe para ejecutar esta accion").show();
            return;
        }

        ScreenLaunchUtil.launchNewEntityStreen(RegistroAplicacionConceptoAdicional.class, null, tableRACA, screenBuilders, this, OpenMode.DIALOG,dataContext,
                s->{actualizaTotalesForward();},
                sab->{
                        ((RegistroAplicacionConceptoAdicionalEdit)sab).setRecibo(recibo);
                        ((RegistroAplicacionConceptoAdicionalEdit)sab).setImplementacionConcepto(implementacionConceptoDc.getItem());
                });
    }

    @Subscribe("tableRACA.edit")
    private void onTableRACAEdit(Action.ActionPerformedEvent event) {
        if (implementacionConceptoDc.getItem().getImporte()==null){
            notifications.create().withCaption("Importe Requerido").withDescription("Aportar Importe para ejecutar esta accion");
            return;
        }
        ScreenLaunchUtil.launchEditEntityScreen(tableRACA.getSingleSelected(), null, tableRACA, screenBuilders, this, OpenMode.DIALOG,dataContext,
                s->{actualizaTotalesForward();},
                sab->{
                    ((RegistroAplicacionConceptoAdicionalEdit)sab).setRecibo(recibo);
                    ((RegistroAplicacionConceptoAdicionalEdit)sab).setImplementacionConcepto(implementacionConceptoDc.getItem());
                });
    }

    @Subscribe("importePostCCAAField")
    private void onImportePostCCAAFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        actualizaTotalesBackward();
    }

    @Subscribe("importeField")
    private void onImporteFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        actualizaTotalesForward();
    }
    
    
    

    private void actualizaTotalesForward(){

        double base = implementacionConceptoDc.getItem().getImporte();
        double basepca = base;
        List<RegistroAplicacionConceptoAdicional> lraca = implementacionConceptoDc.getItem().getRegistroAplicacionesConceptosAdicionales();
        for (int i = 0; i < lraca.size(); i++) {
            RegistroAplicacionConceptoAdicional raca =  lraca.get(i);
            if (raca.getConceptoAdicional().getAdicionSustraccion()){
                basepca += raca.getImporteAplicado();
            }else{
                basepca -= raca.getImporteAplicado();
            }

        }
        importePostCCAAField.setValue(basepca);

    }

    private void actualizaTotalesBackward(){

        double basepca = implementacionConceptoDc.getItem().getImportePostCCAA();
        double base = basepca;
        List<RegistroAplicacionConceptoAdicional> lraca = implementacionConceptoDc.getItem().getRegistroAplicacionesConceptosAdicionales();
        for (int i = 0; i < lraca.size(); i++) {
            RegistroAplicacionConceptoAdicional raca =  lraca.get(i);
            if (raca.getConceptoAdicional().getAdicionSustraccion()){
                base -= raca.getImporteAplicado();
            }else{
                base += raca.getImporteAplicado();
            }

        }
        importeField.setValue(base);

    }
}