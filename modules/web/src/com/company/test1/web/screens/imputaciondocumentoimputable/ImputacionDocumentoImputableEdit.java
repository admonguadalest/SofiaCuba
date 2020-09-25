package com.company.test1.web.screens.imputaciondocumentoimputable;

import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.ciclos.Evento;
import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import com.company.test1.web.screens.documentoproveedor.DocumentoProveedorBrowse;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.model.impl.InstanceLoaderImpl;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_ImputacionDocumentoImputable.edit")
@UiDescriptor("imputacion-documento-imputable-edit.xml")
@EditedEntityContainer("imputacionDocumentoImputableDc")
@LoadDataBeforeShow
public class ImputacionDocumentoImputableEdit extends StandardEditor<ImputacionDocumentoImputable> {



    @Inject
    private InstanceContainer<ImputacionDocumentoImputable> imputacionDocumentoImputableDc;
    @Inject
    private InstanceLoader<ImputacionDocumentoImputable> imputacionDocumentoImputableDl;
    @Inject
    private CollectionLoader<Evento> eventoesLc;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private PickerField<DocumentoImputable> documentoImputableField;
    @Inject
    private TextField<Double> porcentajeImputacionField;
    @Inject
    private TextField<Double> importeImputacionField;
    @Inject
    private PickerField<Ciclo> cicloField;
    String fromScreen = null;
    @Inject
    private Notifications notifications;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (event.getOptions() instanceof MapScreenOptions){
            MapScreenOptions mso = (MapScreenOptions) event.getOptions();
            if (mso.getParams().containsKey("fromScreen")){
                fromScreen = (String) mso.getParams().get("fromScreen");
                int y = 2;

                if ("test1_FacturaProveedor.edit;test1_Presupuesto.edit;".indexOf(fromScreen)!=-1){
                    documentoImputableField.setEditable(false);
                    documentoImputableField.setEnabled(false);
                }
                if (fromScreen.compareTo("test1_Ciclo.edit")==0){
                    cicloField.setEditable(false);
                    cicloField.setEnabled(false);
                }
            }
        }
    }


    
    
    
    @Subscribe
    private void onBeforeShow(BeforeShowEvent event) {
        imputacionDocumentoImputableDl.load();

    }

    @Install(to = "eventoesLc", target = Target.DATA_LOADER)
    private List<Evento> eventoesLcLoadDelegate(LoadContext<Evento> loadContext) {
        if (imputacionDocumentoImputableDc.getItem().getCiclo()!=null) {
            return imputacionDocumentoImputableDc.getItem().getCiclo().getEventos();
        }
        return new ArrayList<Evento>();
    }

    @Subscribe("cicloField")
    private void onCicloFieldValueChange(HasValue.ValueChangeEvent<Ciclo> event) {
        if (event.getValue()!=null){
            eventoesLc.load();
        }
    }

    @Subscribe("porcentajeImputacionField")
    private void onPorcentajeImputacionFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        if (event.getValue()==null){
            return;
        }
        if (imputacionDocumentoImputableDc.getItem().getDocumentoImputable()==null){
            notifications.create().withCaption("Debe asociar antes un Documento Imputable (Factura/Presupuesto)").show();
            porcentajeImputacionField.setValue(null);
            return;
        }
        double perc = event.getValue();
        imputacionDocumentoImputableDc.getItem().setImporteImputacion(perc * imputacionDocumentoImputableDc.getItem().getDocumentoImputable().getImporteTotalBase());
    }

    @Subscribe("importeImputacionField")
    public void onImporteImputacionFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        if (event.getValue()==null){
            return;
        }
        if (imputacionDocumentoImputableDc.getItem().getDocumentoImputable()==null){
            notifications.create().withCaption("Debe asociar antes un Documento Imputable (Factura/Presupuesto)").show();
            porcentajeImputacionField.setValue(null);
            return;
        }
        double importe = event.getValue();

        double perc = importe / imputacionDocumentoImputableDc.getItem().getDocumentoImputable().getImporteTotalBase();
        imputacionDocumentoImputableDc.getItem().setPorcentajeImputacion(perc);
    }
    
    

    @Subscribe("documentoImputableField")
    public void onDocumentoImputableFieldValueChange(HasValue.ValueChangeEvent<DocumentoImputable> event) {
        if (imputacionDocumentoImputableDc.getItem().getDocumentoImputable()==null){
            porcentajeImputacionField.setEditable(false);
            porcentajeImputacionField.setValue(null);

            importeImputacionField.setEditable(false);
            importeImputacionField.setValue(null);
        }
        if (imputacionDocumentoImputableDc.getItem().getDocumentoImputable()!=null){
            porcentajeImputacionField.setEditable(true);
            importeImputacionField.setEditable(true);
        }
    }

    public void btn100(){
        porcentajeImputacionField.setValue(1.0);
    }

    public void btn50(){
        porcentajeImputacionField.setValue(0.5);
    }

    public void btn25(){
        porcentajeImputacionField.setValue(0.25);
    }

    
    


    
    
    
    


    
    



}