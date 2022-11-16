package com.company.test1.web.screens.cuentadegasto;

import com.company.test1.entity.Persona;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.cuentadegasto.MovimientoCuentaDeGasto;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.List;

@UiController("test1_MovimientoCuentaDeGasto.browse")
@UiDescriptor("movimiento-cuenta-de-gasto-browse.xml")
@LookupComponent("movimientoCuentaDeGastoesTable")
@LoadDataBeforeShow
public class MovimientoCuentaDeGastoBrowse extends StandardLookup<MovimientoCuentaDeGasto> {
    @Inject
    private DataManager dataManager;
    @Inject
    private UserSession userSession;
    @Inject
    private CollectionLoader<MovimientoCuentaDeGasto> movimientoCuentaDeGastoesDl;

    @Subscribe(id = "movimientoCuentaDeGastoesDc", target = Target.DATA_CONTAINER)
    public void onMovimientoCuentaDeGastoesDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<MovimientoCuentaDeGasto> event) {
        movimientoCuentaDeGastoesDl.load();
    }

    @Install(to = "movimientoCuentaDeGastoesDl", target = Target.DATA_LOADER)
    private List<MovimientoCuentaDeGasto> movimientoCuentaDeGastoesDlLoadDelegate(LoadContext<MovimientoCuentaDeGasto> loadContext) {
        String hql = "select p from test1_Persona p where p.usuario.id = :uid";
        Persona p = dataManager.loadValue(hql, Persona.class).parameter("uid", userSession.getUser().getId())
                .one();
        hql = "select mcd from test1_MovimientoCuentaDeGasto mcd join mcd.cuentaDeGasto cg join cg.persona p ";

        List<MovimientoCuentaDeGasto> mm = null;
        if (userSession.getUser().getLogin().compareTo("carlosconti")!=0) {
            hql += " where p.id = :pid";
            mm = dataManager.loadValue(hql, MovimientoCuentaDeGasto.class)
                    .parameter("pid", p.getId()).list();

        }else{
            mm = dataManager.loadValue(hql, MovimientoCuentaDeGasto.class)
                    .list();
        }
        for (int i = 0; i < mm.size(); i++) {
            MovimientoCuentaDeGasto mcdg = mm.get(i);
            mcdg = dataManager.reload(mcdg, "movimientoCuentaDeGasto-browseView");
            mm.set(i, mcdg);
        }
        return mm;

    }


}