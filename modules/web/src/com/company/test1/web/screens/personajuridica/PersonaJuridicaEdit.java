package com.company.test1.web.screens.personajuridica;

import com.company.test1.entity.*;
import com.company.test1.entity.conceptosadicionales.ProgramacionConceptoAdicional;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.direccion.DireccionEdit;
import com.company.test1.web.screens.direccion.DireccionEdit;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.Fragments;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.model.impl.CollectionContainerImpl;
import com.haulmont.cuba.gui.model.impl.CollectionPropertyContainerImpl;
import com.haulmont.cuba.gui.model.impl.ScreenDataImpl;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UiController("test1_PersonaJuridica.edit")
@UiDescriptor("persona-juridica-edit.xml")
@EditedEntityContainer("personaJuridicaDc")
@LoadDataBeforeShow
public class PersonaJuridicaEdit extends StandardEditor<PersonaJuridica> {


    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataContext dataContext;
    @Inject
    private InstanceContainer<PersonaJuridica> personaJuridicaDc;


    @Inject
    private CollectionContainer<Direccion> direccionsDc;

    @Inject
    private Table<Direccion> direccionTable;

    @Inject
    private CheckBox chkProveedor;


    @Inject
    private TextField<String> nombreField;


    @Inject
    private InstanceContainer<Proveedor> proveedorDc;


    @Inject
    private TextField<String> nombreCompletoField;
    Boolean isNew = false;

    @Inject
    private CollectionPropertyContainer<CuentaBancaria> cuentasBancariasDc;
    @Inject
    private VBoxLayout sbxPropietario;
    @Named("tab.tabProveedor")
    private VBoxLayout tabProveedor;

    @Inject
    private Fragments fragments;
    @Inject
    private InstancePropertyContainer<Propietario> propietarioDc;
    Proveedor proveedor = null;
    Propietario propietario = null;

    @Inject
    private CheckBox chkPropietario;
    @Inject
    private VBoxLayout sbxProveedor;
    @Inject
    private DataManager dataManager;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        if (event.getOptions() instanceof MapScreenOptions){
            MapScreenOptions mso = (MapScreenOptions) event.getOptions();
            Map m = mso.getParams();
            if (m.containsKey("newEntity")){

                this.setEntityToEdit(new PersonaJuridica());
            }
        }

    }

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
//        this.isNew = PersistenceHelper.isNew(personaFisicaDc.getItem());
//        if (!isNew){
//            chkProveedor.setEditable(false);
//            proveedor = personaFisicaDc.getItem().getProveedor();
//            if (proveedor!=null){
//                chkProveedor.setValue(true);
//
//            }else{
//                chkProveedor.setValue(false);
//            }
//        }else{
//            chkProveedor.setEditable(true);
//            proveedor = new Proveedor();
//            personaFisicaDc.getItem().setProveedor(proveedor);
//        }
//        if (chkProveedor.isChecked()){
//            tabProveedor.setVisible(true);
//        }else{
//            tabProveedor.setVisible(false);
//        }

        if (this.isNew){
            this.chkProveedor.setEditable(true);
        }else{
            if (personaJuridicaDc.getItem().getProveedor()==null){
                this.chkProveedor.setEditable(true);
            }else{
                proveedor = personaJuridicaDc.getItem().getProveedor();
                this.chkProveedor.setEditable(false);
                this.chkProveedor.setValue(true);
            }
        }

        if (this.isNew){
            this.chkPropietario.setEditable(true);
        }else{
            if (personaJuridicaDc.getItem().getPropietario()==null){
                this.chkPropietario.setEditable(true);
            }else{
                propietario = personaJuridicaDc.getItem().getPropietario();
                this.chkPropietario.setEditable(false);
                this.chkPropietario.setValue(true);
            }
        }

        if (this.personaJuridicaDc.getItem().getUsuario()==null){
            User user = dataManager.load(User.class).query("select u FROM sec$User u WHERE u.login like 'anonymous'").one();
            this.personaJuridicaDc.getItem().setUsuario(user);
        }
    }



    @Subscribe
    private void onBeforeShow(BeforeShowEvent event) {



    }



    @Subscribe("chkPropietario")
    private void onChkPropietarioValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.getValue()){
            if (propietario==null){
                propietario = new Propietario();
                propietarioDc.setItem(propietario);
                propietario.setPersona(personaJuridicaDc.getItem());
                personaJuridicaDc.getItem().setPropietario(propietario);
            }
            ScreenFragment f = fragments.create(this, "test1_PropietarioFragment");
            sbxPropietario.add(f.getFragment());
        }else{
            sbxPropietario.removeAll();
            propietario.setPersona(null);
            personaJuridicaDc.getItem().setPropietario(null);
        }
    }

    @Subscribe("chkProveedor")
    private void onChkProveedorValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.getValue()){
            if (proveedor==null){
                proveedor = new Proveedor();
                proveedorDc.setItem(proveedor);
                proveedor.setPersona(personaJuridicaDc.getItem());
                personaJuridicaDc.getItem().setProveedor(proveedor);
            }
            ScreenFragment f = fragments.create(this, "test1_ProveedorFragment");
            sbxProveedor.add(f.getFragment());
        }else{
            sbxProveedor.removeAll();
            proveedor.setPersona(null);
            personaJuridicaDc.getItem().setProveedor(null);
        }
    }







//    @Subscribe("chkProveedor")
//    private void onChkProveedorValueChange(HasValue.ValueChangeEvent<Boolean> event) {
//
//        if (chkProveedor.getValue()){
//
//
//                personaFisicaDc.getItem().setProveedor(proveedor);
//                proveedor.setPersona(personaFisicaDc.getItem());
//                tabProveedor.setVisible(true);
//
//        }else{
//            proveedor = personaFisicaDc.getItem().getProveedor();
//            if (proveedor!=null){
//                proveedor.setPersona(null);
//
//            }
//
//
//            personaFisicaDc.getItem().setProveedor(null);
//            tabProveedor.setVisible(false);
//        }
//    }










    @Subscribe("nombreField")
    private void onNombreFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        actualizaNombreCompleto();
    }

    private void actualizaNombreCompleto(){
        nombreCompletoField.setValue(nombreField.getValue());
    }



    @Subscribe("direccionTable.create")
    private void onDireccionTableCreate(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchNewEntityStreen(Direccion.class, "test1_Direccion.edit", this.direccionTable, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
        Screen s = screenBuilders.editor(Direccion.class, this)
                .withParentDataContext(dataContext)
                .withLaunchMode(OpenMode.DIALOG)
                .newEntity()
                .withInitializer(d->{d.setPersona(personaJuridicaDc.getItem());})
                .withListComponent(direccionTable).build();
        s.show();
    }

    @Subscribe("direccionTable.edit")
    private void onDireccionTableEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(
                direccionTable.getSingleSelected(), "test1_Direccion.edit", direccionTable, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }

    @Inject
    private Table<DatoDeContacto> tableDatosDeContacto;
    @Inject
    private Table<CuentaBancaria> tableCuentasBancarias;

    @Subscribe("tableCuentasBancarias.create")
    private void onTableCuentasBancariasCreate(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchNewEntityStreen(CuentaBancaria.class, null, tableCuentasBancarias, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
        Screen s = screenBuilders.editor(CuentaBancaria.class, this)
                .withParentDataContext(dataContext)
                .withLaunchMode(OpenMode.DIALOG)
                .newEntity()
                .withInitializer(cb->{cb.setPersona(personaJuridicaDc.getItem());})
                .withListComponent(tableCuentasBancarias).build();
        s.show();
    }

    @Subscribe("tableCuentasBancarias.edit")
    private void onTableCuentasBancariasEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(
                tableCuentasBancarias.getSingleSelected(), null, tableCuentasBancarias, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }

    @Subscribe("tableDatosDeContacto.create")
    private void onTableDatosDeContactoCreate(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchNewEntityStreen(DatoDeContacto.class, null, tableDatosDeContacto, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
        Screen s = screenBuilders.editor(DatoDeContacto.class, this)
                .withParentDataContext(dataContext)
                .withLaunchMode(OpenMode.DIALOG)
                .newEntity()
                .withInitializer(ddc->{ddc.setPersona(personaJuridicaDc.getItem());})
                .withListComponent(tableDatosDeContacto).build();
        s.show();
    }

    @Subscribe("tableDatosDeContacto.edit")
    private void onTableDatosDeContactoEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableDatosDeContacto.getSingleSelected(), null, tableDatosDeContacto, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }












}