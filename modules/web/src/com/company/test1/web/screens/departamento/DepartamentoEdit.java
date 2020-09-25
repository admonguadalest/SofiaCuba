package com.company.test1.web.screens.departamento;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.departamentos.CedulaHabitabilidad;
import com.company.test1.entity.departamentos.CertificadoCalificacionEnergetica;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.cedulahabitabilidad.CedulaHabitabilidadEdit;
import com.company.test1.web.screens.certificadocalificacionenergetica.CertificadoCalificacionEnergeticaEdit;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.model.impl.InstanceContainerImpl;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.departamentos.Departamento;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@UiController("test1_Departamento.edit")
@UiDescriptor("departamento-edit.xml")
@EditedEntityContainer("departamentoDc")
@LoadDataBeforeShow
public class DepartamentoEdit extends StandardEditor<Departamento> {
    @Inject
    private InstanceContainer<Departamento> departamentoDc;

    @Inject
    private InstanceLoader<Departamento> departamentoDl;

    @Inject
    private ScreenBuilders screenBuilders;

    @Inject
    private DataContext dataContext;

    @Inject
    private CollectionLoader<CedulaHabitabilidad> cedulasDl;
    @Inject
    private Table<CertificadoCalificacionEnergetica> tableCertificados;
    @Inject
    private CollectionLoader<CertificadoCalificacionEnergetica> certificadosDl;

    @Inject
    private Table<CedulaHabitabilidad> tableCedulas;
    @Inject
    private InstancePropertyContainer<ColeccionArchivosAdjuntos> coleccionArchivosAdjuntosDc;
    @Inject
    private LookupField<String> puertaField;
    @Inject
    private TextField<String> referenciaCatastralField;

    @Inject
    private LookupField<String> pisoField;
    @Inject
    private Notifications notifications;

    @Install(to = "cedulasDl", target = Target.DATA_LOADER)
    private List<CedulaHabitabilidad> cedulasDlLoadDelegate(LoadContext<CedulaHabitabilidad> loadContext) {
        Departamento d = departamentoDc.getItem();
        return d.getCedulasHabitabilidad();
    }

    @Install(to = "certificadosDl", target = Target.DATA_LOADER)
    private List<CertificadoCalificacionEnergetica> certificadosDlLoadDelegate(LoadContext<CedulaHabitabilidad> loadContext) {
        Departamento d = departamentoDc.getItem();
        return d.getCertificadosCalificacionEnergetica();
    }

    @Subscribe
    private void onBeforeShow(BeforeShowEvent event) {
        InstanceContainerImpl<Departamento> ici = (InstanceContainerImpl)departamentoDc;
        departamentoDl.load();
        
    }

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        if (event.getOptions() instanceof MapScreenOptions){
            MapScreenOptions mso = (MapScreenOptions) event.getOptions();
            Map m = mso.getParams();
            if(m.containsKey("newEntity")){
                Boolean s = (Boolean) m.get("esVivienda");
                Departamento d = new Departamento();
                if (s){
                    d.setViviendaLocal(true);

                }
                if (!s){
                    d.setViviendaLocal(false);
                }
                this.setEntityToEdit(d);
            }
        }
        
    }
    
    

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        if (departamentoDc.getItem().getExcluirDeMonitorizacionParaBusquedaDePisosVacios()==null)
            departamentoDc.getItem().setExcluirDeMonitorizacionParaBusquedaDePisosVacios(false);
        if (departamentoDc.getItem().getColeccionAdjuntos()==null){
            departamentoDc.getItem().setColeccionAdjuntos(new ColeccionArchivosAdjuntos());
            coleccionArchivosAdjuntosDc.setItem(departamentoDc.getItem().getColeccionAdjuntos());
        }
        if (departamentoDc.getItem().getColeccionAdjuntos().getNombre().compareTo("(Nombre Coleccion)")==0){
            try{
                departamentoDc.getItem().getColeccionAdjuntos().setNombre(departamentoDc.getItem().getNombreDescriptivoCompleto());
            }catch(Exception exc){
                //no corregimos nada
            }


        }
        if (departamentoDc.getItem().getUbicacion()!=null) {
            if (departamentoDc.getItem().getUbicacion().getEsPropiedadVertical()) {
                departamentoDc.getItem().setReferenciaCatastral(null);
                referenciaCatastralField.setEnabled(false);
            }
        }
        pueblaListaLiteralesPickers();
    }

    private void pueblaListaLiteralesPickers(){
        String pisos = "SOTANO,BAJOS,ENTRESUELO,PRINCIPAL,1,2,3,4,5,6,7,8,9,10,11,12,13,ATICO,SOBREATICO,AZOTEA";
        String puertas = "1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O";
        pisoField.setOptionsList(Arrays.asList(pisos.split(",")));
        puertaField.setOptionsList(Arrays.asList(puertas.split(",")));
    }

    


    public void onBtnNuevaCHClick() {
//        ScreenLaunchUtil.launchNewEntityStreen(CedulaHabitabilidad.class, screenBuilders, this, OpenMode.DIALOG, dataContext, (s)->{
//            CedulaHabitabilidad ch = ((CedulaHabitabilidadEdit)s).getEditedEntity();
//            ch.setDepartamento(departamentoDc.getItem());
//            departamentoDc.getItem().getCedulasHabitabilidad().add(ch);
//            cedulasDl.load();
//        });

        Screen s = screenBuilders.editor(CedulaHabitabilidad.class, this).newEntity()
                .withInitializer(ch->{ch.setDepartamento(departamentoDc.getItem());})
                .withOpenMode(OpenMode.DIALOG)
                .withParentDataContext(dataContext)
                .withListComponent(tableCedulas)
                .build();
        s.show();
    }

    public void onBtnNuevoCEClick() {
//        ScreenLaunchUtil.launchNewEntityStreen(CertificadoCalificacionEnergetica.class, screenBuilders, this, OpenMode.DIALOG, dataContext, (s)->{
//            CertificadoCalificacionEnergetica cce = ((CertificadoCalificacionEnergeticaEdit)s).getEditedEntity();
//            cce.setDepartamento(departamentoDc.getItem());
//            departamentoDc.getItem().getCertificadosCalificacionEnergetica().add(cce);
//            certificadosDl.load();
//        });

        Screen s = screenBuilders.editor(CertificadoCalificacionEnergetica.class, this).newEntity()
                .withInitializer(ce->{ce.setDepartamento(departamentoDc.getItem());})
                .withOpenMode(OpenMode.DIALOG)
                .withParentDataContext(dataContext)
                .withListComponent(tableCertificados)
                .build();
        s.show();
    }

    public void onBtnEditarClick() {
        if (tableCedulas.getSingleSelected()==null){
            notifications.create().withCaption("Seleccionar Cedula a Editar").show();
            return;
        }
        Screen s = screenBuilders.editor(CedulaHabitabilidad.class, this).editEntity(tableCedulas.getSingleSelected())
                .withOpenMode(OpenMode.DIALOG)
                .withParentDataContext(dataContext)
                .withListComponent(tableCedulas)
                .build();
        s.show();
    }

    public void onBtnEditarCEClick() {
        if (tableCertificados.getSingleSelected()==null){
            notifications.create().withCaption("Seleccionar Certificado a Editar").show();
            return;
        }
        Screen s = screenBuilders.editor(CertificadoCalificacionEnergetica.class, this).editEntity(tableCertificados.getSingleSelected())
                .withOpenMode(OpenMode.DIALOG)
                .withParentDataContext(dataContext)
                .withListComponent(tableCertificados)
                .build();
        s.show();
    }
}