package com.company.test1.web.screens.cuentadegasto;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.Persona;
import com.company.test1.entity.cuentadegasto.CuentaDeGasto;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.cuentadegasto.MovimientoCuentaDeGasto;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.List;

@UiController("test1_MovimientoCuentaDeGasto.edit")
@UiDescriptor("movimiento-cuenta-de-gasto-edit.xml")
@EditedEntityContainer("movimientoCuentaDeGastoDc")
@LoadDataBeforeShow
public class MovimientoCuentaDeGastoEdit extends StandardEditor<MovimientoCuentaDeGasto> {

    @Inject
    private CollectionContainer<CuentaDeGasto> cuentasDeGastoDc;
    @Inject
    private CollectionLoader<CuentaDeGasto> cuentasDeGastoDl;
    @Inject
    private InstanceContainer<MovimientoCuentaDeGasto> movimientoCuentaDeGastoDc;
    @Inject
    private UserSession userSession;
    @Inject
    private DataManager dataManager;

    @Install(to = "cuentasDeGastoDl", target = Target.DATA_LOADER)
    private List<CuentaDeGasto> cuentasDeGastoDlLoadDelegate(LoadContext<CuentaDeGasto> loadContext) {
        User u = userSession.getUser();
        if (u.getLogin().compareTo("carlosconti")!=0){
            String hql = "select p from test1_Persona p where p.usuario.id = :uid";
            Persona p = dataManager.loadValue(hql, Persona.class).parameter("uid", u.getId()).one();
            hql = "select cdg from test1_CuentaDeGasto cdg where cdg.persona.id = :pid";
            List<CuentaDeGasto> cdgs = dataManager.loadValue(hql, CuentaDeGasto.class).parameter("pid", p.getId())
                    .list();
            return cdgs;
        }else{

            String hql = "select cdg from test1_CuentaDeGasto cdg";
            List<CuentaDeGasto> cdgs = dataManager.loadValue(hql, CuentaDeGasto.class)
                    .list();
            return cdgs;
        }

    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        if (this.movimientoCuentaDeGastoDc.getItem().getCuentaDeGasto()==null){

        }
        if (this.movimientoCuentaDeGastoDc.getItem().getColeccionArchivosAdjuntos()==null){
            ColeccionArchivosAdjuntos caa = dataManager.create(ColeccionArchivosAdjuntos.class);
            caa.setNombre("Adjuntos");
            this.movimientoCuentaDeGastoDc.getItem().setColeccionArchivosAdjuntos(caa);
        }




    }





}