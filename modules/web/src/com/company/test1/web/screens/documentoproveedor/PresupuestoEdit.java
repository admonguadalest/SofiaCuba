package com.company.test1.web.screens.documentoproveedor;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.Persona;
import com.company.test1.entity.PersonaFisica;
import com.company.test1.entity.PersonaJuridica;
import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.conceptosadicionales.ProgramacionConceptoAdicional;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.imputaciondocumentoimputable.ImputacionDocumentoImputableEdit;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.builders.EditorBuilder;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.model.impl.CollectionPropertyContainerImpl;
import com.haulmont.cuba.gui.model.impl.InstanceContainerImpl;
import com.haulmont.cuba.gui.model.impl.InstancePropertyContainerImpl;
import com.haulmont.cuba.gui.screen.*;


import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@UiController("test1_Presupuesto.edit")
@UiDescriptor("presupuesto-edit.xml")
@EditedEntityContainer("presupuestoDc")
@LoadDataBeforeShow
public class PresupuestoEdit extends StandardEditor<Presupuesto> {

    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private InstanceContainer<Presupuesto> presupuestoDc;
    @Inject
    private Table<RegistroAplicacionConceptoAdicional> tableRegistrosAplicacionesCCAA;
    @Inject
    private Table<ImputacionDocumentoImputable> tableImputaciones;
    @Inject
    private CollectionPropertyContainer<RegistroAplicacionConceptoAdicional> registroAplicacionConceptosAdicionalesDc;

    @Inject
    private DataContext dataContext;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private DataComponents dataComponents;
    @Inject
    private DataManager dataManager;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        if (event.getOptions() instanceof MapScreenOptions){
            MapScreenOptions mso = (MapScreenOptions) event.getOptions();
            if (mso.getParams().containsKey("newEntity")){
                Presupuesto p = dataContext.create(Presupuesto.class);
                ColeccionArchivosAdjuntos caa = dataContext.create(ColeccionArchivosAdjuntos.class);
                p.setColeccionArchivosAdjuntos(caa);
                this.setEntityToEdit(p);
            }
        }
    }



    @Subscribe
    private void onBeforeShow(BeforeShowEvent event) {


    }

    @Subscribe
    private void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        int y = 2;
    }





    @Subscribe("proveedorField")
    private void onProveedorFieldValueChange(HasValue.ValueChangeEvent<Proveedor> event) {
        if (!event.isUserOriginated()) return;
        Proveedor prov = presupuestoDc.getItem().getProveedor();
        if (prov!=null){
            List<ProgramacionConceptoAdicional> pca = prov.getProgramacionesConceptosAdicionales();
            presupuestoDc.getItem().getRegistroAplicacionConceptosAdicionales().clear();
            for(int i = 0;i < pca.size();i++) {
//                RegistroAplicacionConceptoAdicional raca = new RegistroAplicacionConceptoAdicional();
//                raca.setConceptoAdicional(pca.get(i).getConceptoAdicional());
//                raca.setBase(0.0);
//                raca.setImporteAplicado(0.0);
//                registroAplicacionConceptosAdicionalesDc.getMutableItems().add(raca);

                RegistroAplicacionConceptoAdicional raca = dataContext.create(RegistroAplicacionConceptoAdicional.class);
                raca.setNifDni(prov.getPersona().getNifDni());
                raca.setDocumentoImputable(this.getEditedEntity());
                raca.setFechaValor(this.getEditedEntity().getFechaEmision());
                raca.setNumDocumento(this.getEditedEntity().getNumDocumento());
                raca.setConceptoAdicional(pca.get(i).getConceptoAdicional());
                raca.setBase(0.0);
                raca.setImporteAplicado(0.0);
                registroAplicacionConceptosAdicionalesDc.getMutableItems().add(raca);
            }


        }
    }

    @Subscribe("importeTotalBaseField")
    private void onImporteTotalBaseFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        List<RegistroAplicacionConceptoAdicional> items = registroAplicacionConceptosAdicionalesDc.getMutableItems();
        double importeTotal = event.getValue();
        for(int i = 0;i < items.size();i++){
            RegistroAplicacionConceptoAdicional raca = items.get(i);
            raca.setBase(event.getValue());
            if (raca.getPorcentaje()!=null){
                raca.setImporteAplicado(raca.getPorcentaje()*raca.getBase());
            }
            if (raca.getConceptoAdicional().getAdicionSustraccion()){
                importeTotal += raca.getImporteAplicado();
            }else{
                importeTotal -= raca.getImporteAplicado();
            }

        }
        presupuestoDc.getItem().setImportePostCCAA(importeTotal);


    }



    @Subscribe(id = "registroAplicacionConceptosAdicionalesDc", target = Target.DATA_CONTAINER)
    private void onRegistroAplicacionConceptosAdicionalesDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<RegistroAplicacionConceptoAdicional> event) {

    }



    public Component generateCellPorcentajeRACA(RegistroAplicacionConceptoAdicional raca){
        LookupField<Double> lkf = uiComponents.create(LookupField.NAME);

        Double[] options = null;
        if (raca.getConceptoAdicional().getAbreviacion().compareTo("IVA")==0){
            options = new Double[]{0.04, 0.1, 0.21};
        }
        if (raca.getConceptoAdicional().getAbreviacion().compareTo("IRPF")==0){
            options = new Double[]{0.01, 0.2, 0.15};
        }
        lkf.setOptionsList(Arrays.asList(options));

        InstanceContainer<RegistroAplicacionConceptoAdicional> ici = dataComponents.createInstanceContainer(RegistroAplicacionConceptoAdicional.class);
        ici.setItem(raca);

        lkf.setValueSource(new ContainerValueSource(ici, "porcentaje"));


        return lkf;

    }

    @Subscribe("tableImputaciones.create")
    private void onTableImputacionesCreate(Action.ActionPerformedEvent event) {
        ImputacionDocumentoImputable idi = new ImputacionDocumentoImputable();
        idi.setDocumentoImputable(this.getEditedEntity());
//        ScreenLaunchUtil.launchEditEntityScreen(idi, null, tableImputaciones, screenBuilders, this, OpenMode.DIALOG,
//                dataContext, null);
        HashMap hm = new HashMap();
        hm.put("fromScreen", this.getId());
        MapScreenOptions mso = new MapScreenOptions(hm);
        EditorBuilder eb = screenBuilders.editor(ImputacionDocumentoImputable.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .newEntity(idi).withParentDataContext(dataContext).withListComponent(tableImputaciones).withOptions(mso);
        Screen s = eb.build().show();
    }

    @Subscribe("tableImputaciones.edit")
    private void onTableImputacionesEdit(Action.ActionPerformedEvent event) {
        ImputacionDocumentoImputable idi = tableImputaciones.getSingleSelected();
        idi.setDocumentoImputable(this.getEditedEntity());
        Persona p = this.getEditedEntity().getProveedor().getPersona();
        if (p instanceof PersonaFisica){
            p = dataManager.reload(p, "personaFisica-view");
        }
        if (p instanceof PersonaJuridica){
            p = dataManager.reload(p, "personaJuridica-view");
        }
        this.getEditedEntity().getProveedor().setPersona(p);
        ScreenLaunchUtil.launchEditEntityScreen(tableImputaciones.getSingleSelected(), null, tableImputaciones, screenBuilders, this, OpenMode.DIALOG,
                dataContext, null);
    }








}