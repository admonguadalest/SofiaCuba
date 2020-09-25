package com.company.test1.web.screens.ubicacion;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.coeficientes.Coeficiente;
import com.company.test1.entity.coeficientes.TipoCoeficiente;
import com.company.test1.entity.coeficientes.UbicacionCoeficiente;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.tipocoeficiente.CoeficienteEdit;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.model.impl.InstanceContainerImpl;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@UiController("test1_Ubicacion.edit")
@UiDescriptor("ubicacion-edit.xml")
@EditedEntityContainer("ubicacionDc")
@LoadDataBeforeShow
public class UbicacionEdit extends StandardEditor<Ubicacion> {
    @Inject
    private Notifications notifications;
    @Inject
    private InstancePropertyContainer<ColeccionArchivosAdjuntos> coleccionArchivosAdjuntosDc;

    @Subscribe("lkpPropietario")
    public void onLkpPropietarioValueChange(HasValue.ValueChangeEvent<Propietario> event) {
        if (event.getValue()!=null){
            ubicacionDc.getItem().setEsPropiedadVertical(true);
        }else{
            ubicacionDc.getItem().setEsPropiedadVertical(false);
        }
    }



    @Inject
    InstanceLoader<Ubicacion> ubicacionLoader;

    @Inject
    InstanceContainer<Ubicacion> ubicacionDc;



    @Inject
    ScreenBuilders screenBuilders;

    @Inject
    private CollectionLoader<Coeficiente> coeficientesDl;
    @Inject
    private DataManager dataManager;

    @Inject
    private DataContext dataContext;





    @Inject
    private Table<Departamento> tableDepartamentos;

    @Inject
    private Security security;

    @Inject
    private Button btnNuevoDepartamento;

    @Inject
    private FileUploadingAPI fileUploadingAPI;

    @Inject
    private FileLoader fileLoader;
    @Inject
    private Screens screens;

    @Inject
    private CollectionContainer<Departamento> departamentosDc;

    @Inject
    private ColeccionArchivosAdjuntosService coleccionArchivosAdjuntosService;
    @Inject
    private LookupPickerField<Propietario> lkpPropietario;
    @Inject
    private TextField<String> informacionCatastralField;
    @Inject
    private LookupField<TipoCoeficiente> lkpTipoCoeficiente;
    @Inject
    private CollectionLoader<TipoCoeficiente> tipoCoeficientesUbicacionDl;
    @Inject
    private TwinColumn<TipoCoeficiente> twcCoeficientes;
    @Inject
    private CollectionContainer<Coeficiente> coeficientesDc;
    @Inject
    private Table<Coeficiente> tableCoeficientes;

    @Subscribe
    private void onAfterShow1(AfterShowEvent event) {
        List<TipoCoeficiente> ttcc = new ArrayList<TipoCoeficiente>();
        List<UbicacionCoeficiente> uucc = ubicacionDc.getItem().getUbicacionesCoeficientes();
        if (uucc==null){
            return;
        }
        for (int i = 0; i < uucc.size(); i++) {
            UbicacionCoeficiente ubicacionCoeficiente =  uucc.get(i);
            ttcc.add(ubicacionCoeficiente.getTipoCoeficiente());
        }

        if (ubicacionDc.getItem().getColeccionArchivosAdjuntos()==null){
            ubicacionDc.getItem().setColeccionArchivosAdjuntos(dataManager.create(ColeccionArchivosAdjuntos.class));
            coleccionArchivosAdjuntosDc.setItem(ubicacionDc.getItem().getColeccionArchivosAdjuntos());
        }

        twcCoeficientes.setValue(ttcc);
    }


    
    

    @Install(to = "coeficientesDl", target = Target.DATA_LOADER)
    private List<Coeficiente> coeficientesDlLoadDelegate(LoadContext<Coeficiente> loadContext) {
        TipoCoeficiente tc = lkpTipoCoeficiente.getValue();
        if (tc == null){
            return new ArrayList<Coeficiente>();
        }
        List<Departamento> dd = ubicacionDc.getItem().getDepartamentos();
        ArrayList<Coeficiente> cc = new ArrayList<Coeficiente>();
        for (int i = 0; i < dd.size(); i++) {
            Departamento departamento =  dd.get(i);
            List<Coeficiente> ccd = departamento.getCoeficientes();
            for (int j = 0; j < ccd.size(); j++) {
                Coeficiente coeficiente =  ccd.get(j);
                if (coeficiente.getTipoCoeficiente().getId().compareTo(tc.getUuid())==0){
                    cc.add(coeficiente);
                }
            }
        }
        return cc;

    }
    
    

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        ubicacionLoader.load();
        InstanceContainerImpl<Ubicacion> ici = (InstanceContainerImpl) ubicacionDc;

        ubicacionDc.getItem().addPropertyChangeListener(e->{
            if (e.getProperty().compareTo("esPropiedadVertical")==0){
                if ((Boolean) e.getValue()){
                    lkpPropietario.setEnabled(true);
                    informacionCatastralField.setEnabled(true);

                }else{
                    ubicacionDc.getItem().setPropietario(null);
                    lkpPropietario.setEnabled(false);

                    ubicacionDc.getItem().setInformacionCatastral(null);
                    informacionCatastralField.setEnabled(false);

                }
            }
        });
        if (ubicacionDc.getItem().getColeccionArchivosAdjuntos()==null){
            ColeccionArchivosAdjuntos caa = dataManager.create(ColeccionArchivosAdjuntos.class);
            ubicacionDc.getItem().setColeccionArchivosAdjuntos(caa);
            coleccionArchivosAdjuntosDc.setItem(caa);
        }

    }



    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        if (event.getOptions() instanceof MapScreenOptions){
            MapScreenOptions mso = (MapScreenOptions) event.getOptions();
            if (mso.getParams().containsKey("newEntity")){
                this.setEntityToEdit(new Ubicacion());
            }
        }

    }



    

    


     


    




    @Subscribe("tableDepartamentos.create")
    private void onTableDepartamentosCreate(Action.ActionPerformedEvent event) {
        Departamento d = new Departamento();
        d.setUbicacion(ubicacionDc.getItem());
        departamentosDc.getMutableItems().add(d);
        ScreenLaunchUtil.launchEditEntityScreen(d, null, tableDepartamentos, screenBuilders, this, OpenMode.NEW_TAB, dataContext, null);
    }

    @Subscribe("tableDepartamentos.edit")
    private void onTableDepartamentosEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableDepartamentos.getSingleSelected(), null, tableDepartamentos, screenBuilders, this, OpenMode.NEW_TAB, dataContext, null);
    }

    public ArrayList arrayListDiff(ArrayList al1, ArrayList al2){
        ArrayList diff = new ArrayList();
        for (int i = 0; i < al1.size(); i++) {
            Object o = al1.get(i);
            if (al2.indexOf(o)==-1){
                diff.add(o);
            }
        }
        for (int i = 0; i < al2.size(); i++) {
            Object o = al2.get(i);
            if (al1.indexOf(o)==-1){
                diff.add(o);
            }
        }
        return diff;
    }

    @Subscribe("twcCoeficientes")
    private void onTwcCoeficientesValueChange(HasValue.ValueChangeEvent<Collection<TipoCoeficiente>> event) {
        if (!event.isUserOriginated()) return;

        ArrayList<TipoCoeficiente> ttccprev = new ArrayList(event.getPrevValue());
        ArrayList<TipoCoeficiente> ttcc = new ArrayList(event.getValue());
        if (ttccprev.size()>ttcc.size()){
            //hay sustraccion
            //verificar uqe no existen coeficientes asociados
            ArrayList diff = arrayListDiff(ttccprev, ttcc);
            if (diff.size()>0){
                for (int i = 0; i < diff.size(); i++) {
                    TipoCoeficiente tc = (TipoCoeficiente) diff.get(i);
                    List<UbicacionCoeficiente> uucc = ubicacionDc.getItem().getUbicacionesCoeficientes();
                    for (int j = 0; j < uucc.size(); j++) {
                        UbicacionCoeficiente uc =  uucc.get(j);
                        if (uc.getTipoCoeficiente().getUuid().compareTo(tc.getUuid())==0){
                            //hallado -> NO SE PUEDE CONTINUAR
                            //RETROTRAEMOS LA ASIGNACION
                            //PENDIENTE -> en realidad habría que verificar que no haya incrementos asociados a coeficientes de este tipo de coeficiente
                            //ya llegaremos!
                            twcCoeficientes.getValue().add((TipoCoeficiente)diff.get(0));

                            notifications.create().withCaption("No se puede sustraer el tipo de coeficiente pues ya tiene coeficientes asignados").show();


                            return;
                        }
                    }
                }
            }
        }
        a:
        for (int i = 0; i < ttcc.size(); i++) {
            boolean found = false;
            TipoCoeficiente tipoCoeficiente =  ttcc.get(i);
            List<UbicacionCoeficiente> uucc = ubicacionDc.getItem().getUbicacionesCoeficientes();
            for (int j = 0; j < uucc.size(); j++) {
                UbicacionCoeficiente uc =  uucc.get(j);
                if (uc.getTipoCoeficiente().getUuid().compareTo(tipoCoeficiente.getUuid())==0){
                    continue a;
                }
            }
            if (!found){
                //se crea
                UbicacionCoeficiente uc = new UbicacionCoeficiente();
                uc.setTipoCoeficiente(tipoCoeficiente);
                uc.setTotalTipoCoeficiente(0.0);
                uc.setUbicacion(ubicacionDc.getItem());
                ubicacionDc.getItem().getUbicacionesCoeficientes().add(uc);
                dataContext.merge(uc);
                //creando los coeficientes para los departamentos
                List<Departamento> dd = ubicacionDc.getItem().getDepartamentos();
                for (int j = 0; j < dd.size(); j++) {
                    Departamento departamento =  dd.get(j);
                    Coeficiente c = new Coeficiente();
                    c.setDepartamento(departamento);
                    c.setTipoCoeficiente(tipoCoeficiente);
                    c.setValor(0.0);
                    departamento.getCoeficientes().add(c);
                    dataContext.merge(c);
                }

            }
        }
        tipoCoeficientesUbicacionDl.load();

    }

    @Subscribe("lkpTipoCoeficiente")
    private void onLkpTipoCoeficienteValueChange(HasValue.ValueChangeEvent event) {
        coeficientesDl.load();
    }

    @Subscribe(id = "coeficientesDc", target = Target.DATA_CONTAINER)
    private void onCoeficientesDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<Coeficiente> event) {
        //actualizar total ubicacionCoeficiente
        TipoCoeficiente tc = lkpTipoCoeficiente.getValue();
        UbicacionCoeficiente uc = null;
        //localizando ubicacion coeficiente
        List<UbicacionCoeficiente> uucc = ubicacionDc.getItem().getUbicacionesCoeficientes();
        for (int i = 0; i < uucc.size(); i++) {
            UbicacionCoeficiente ubicacionCoeficiente =  uucc.get(i);
            if (ubicacionCoeficiente.getTipoCoeficiente().getId().compareTo(tc.getId())==0){
                uc = ubicacionCoeficiente;
                break;
            }
        }
        double suma = 0.0;
        List<Departamento> dd = ubicacionDc.getItem().getDepartamentos();
        for (int i = 0; i < dd.size(); i++) {
            Departamento departamento =  dd.get(i);
            List<Coeficiente> cc = departamento.getCoeficientes();
            for (int j = 0; j < cc.size(); j++) {
                Coeficiente coeficiente =  cc.get(j);
                if (coeficiente.getTipoCoeficiente().getId().compareTo(tc.getId())==0){
                    suma += coeficiente.getValor();
                }
            }
        }
        uc.setTotalTipoCoeficiente(suma);
        
    }

    @Install(to = "tipoCoeficientesUbicacionDl", target = Target.DATA_LOADER)
    private List<TipoCoeficiente> tipoCoeficientesUbicacionDlLoadDelegate(LoadContext<TipoCoeficiente> loadContext) {
        ArrayList<TipoCoeficiente> al = new ArrayList<TipoCoeficiente>();
        List<UbicacionCoeficiente> uucc = ubicacionDc.getItem().getUbicacionesCoeficientes();
        if (uucc == null){
            return al;
        }
        for (int i = 0; i < uucc.size(); i++) {
            UbicacionCoeficiente uc =  uucc.get(i);
            al.add(uc.getTipoCoeficiente());
        }
        return al;
    }


    public void onBtnNuevoCoeficienteDepartamentoClick() {

        ScreenLaunchUtil.launchNewEntityStreen(Coeficiente.class, null, screenBuilders, this, OpenMode.DIALOG, dataContext,e->{
            CoeficienteEdit ce = (CoeficienteEdit) e;
            Coeficiente c = ce.getEditedEntity();
            coeficientesDc.getItems().add(c);
        });
    }

    public void onBtnEditarCoeficienteDepartamentoClick() {
        Screen s = screenBuilders.editor(tableCoeficientes).withLaunchMode(OpenMode.DIALOG).withParentDataContext(dataContext).build();
        s.show();
    }
}