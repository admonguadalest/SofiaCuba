package com.company.test1.web.screens.cuentadegasto;

import com.company.test1.entity.Persona;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.service.ContabiService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.company.test1.web.screens.facturaproveedor.FacturaProveedorWithAttachmentEdit;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.cuentadegasto.MovimientoCuentaDeGasto;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.*;

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
    @Inject
    private GroupTable<MovimientoCuentaDeGasto> movimientoCuentaDeGastoesTable;
    @Inject
    private Notifications notifications;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Dialogs dialogs;
    @Inject
    private ContabiService contabiService;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Button createFraBtn;
    @Inject
    private Button editBtn;

    @Subscribe(id = "movimientoCuentaDeGastoesDc", target = Target.DATA_CONTAINER)
    public void onMovimientoCuentaDeGastoesDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<MovimientoCuentaDeGasto> event) {
        movimientoCuentaDeGastoesDl.load();
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        Collection<String> userRoles = userSession.getRoles();
        if (userRoles.contains("Administrators")){
            createFraBtn.setEnabled(true);
        }else{
            createFraBtn.setEnabled(false);
        }
    }

    @Subscribe("movimientoCuentaDeGastoesTable")
    public void onMovimientoCuentaDeGastoesTableSelection(Table.SelectionEvent<MovimientoCuentaDeGasto> event) {
        Set<MovimientoCuentaDeGasto> m = event.getSelected();
        ArrayList<MovimientoCuentaDeGasto> al = new ArrayList(m);
        if (al.size()==1){
            MovimientoCuentaDeGasto mcg = al.get(0);
            if (mcg.getFacturaProveedorAsociado()!=null){
                editBtn.setEnabled(false);
            }else{
                editBtn.setEnabled(true);
            }
        }else{
            editBtn.setEnabled(false);
        }
    }







    @Subscribe("btnVerReport")
    public void onBtnVerReportClick(Button.ClickEvent event) {
        Hashtable<String, Object> camposFooter = new Hashtable();
        Integer[] anchosDeColumnaIArr = new Integer[]{60,60,60,140,45,30,30,50};
        List<Integer> anchosDeColumna = Arrays.asList(anchosDeColumnaIArr);
        byte[] bb = DynamicReportHelper.getReportDinamico("Movimientos de Gasto", MovimientoCuentaDeGasto.class,
                movimientoCuentaDeGastoesTable, camposFooter, anchosDeColumna);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Listado Movimientos.pdf");
    }



    @Subscribe("createFraBtn")
    public void onCreateFraBtnClick(Button.ClickEvent event) {

        MovimientoCuentaDeGasto mcg = movimientoCuentaDeGastoesTable.getSingleSelected();
        if (mcg==null){
            notifications.create().withCaption("Seleccionar movimiento de cuenta de gasto").show();
            return;
        }
        if (mcg.getFacturaProveedorAsociado()!=null){
            notifications.create().withCaption("El movimiento seleccionado ya tiene una factura asociada").show();
            return;
        }
        FacturaProveedor fp = creaFacturaProveedorDesdeMovimientoDeCuentaDeGasto(mcg);
        //HashMap<String, Object> map = new HashMap<>();
        //map.put("newEntityWithAttachment", new Object[]{email, this.nombreDocumentoSeleccionado, this.representacionSerialDocumentoSeleccionado, this.mimeTypeDocumentoSeleccionado});
        //MapScreenOptions mso = new MapScreenOptions(map);
        final FacturaProveedorWithAttachmentEdit fpae = (FacturaProveedorWithAttachmentEdit) screenBuilders.editor(FacturaProveedor.class, this).withScreenId("test1_FacturaProveedorWithAttachment.edit")
                .editEntity(fp).withOpenMode(OpenMode.NEW_TAB).build();
        //fpae.setRossumAnnotation(dataGridRossumAnnotations.getSingleSelected());
        fpae.addAfterCloseListener(e->{

                StandardCloseAction ca = (StandardCloseAction) e.getCloseAction();
                if (ca.getActionId().compareTo("commit")==0) {
                    mcg.setFacturaProveedorAsociado(fp);
                    dataManager.commit(mcg);
                    dialogs.createOptionDialog().withCaption("¿Desea contabilizar en Contabilidad la factura creada?")
                            .withActions(
                                    new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e3 -> {
                                        User user = userSession.getUser();
                                        try {
                                            FacturaProveedor fprov = fpae.getEditedEntity();
                                            boolean res = contabiService.publicaContabilizacionFacturaProveedor(user, (FacturaProveedor) fprov);
                                            if (res) {
                                                notifications.create().withCaption("Factura publicada corréctamente").show();
                                            }
                                        } catch (Exception ee) {
                                            notifications.create().withCaption("Error al publicar").withDescription(ee.getMessage()).show();
                                            return;
                                        }
                                    })
                                    , new DialogAction(DialogAction.Type.NO)
                            )
                            .show();
                    movimientoCuentaDeGastoesDl.load();
                }

        });

        fpae.show();



    }

    private FacturaProveedor creaFacturaProveedorDesdeMovimientoDeCuentaDeGasto(MovimientoCuentaDeGasto mcg){
        FacturaProveedor fp = dataManager.create(FacturaProveedor.class);
        fp.setColeccionArchivosAdjuntos(mcg.getColeccionArchivosAdjuntos());
        fp.setFechaDevengo(mcg.getFecha());
        fp.setDescripcionDocumento(mcg.getTipoDeGasto());
        fp.setImporteTotalBase(mcg.getImporteBase());
        fp.setImportePostCCAA(mcg.getImportePostCCAA());
        fp.setFechaEmision(mcg.getFecha());
        fp.setNumDocumento("pendiente");
        fp.setTitular(mcg.getCuentaDeGasto().getTitular());

        Proveedor prov = dataManager.load(Proveedor.class).query("select p from test1_Proveedor p where " +
                "p.nombreComercial like :nc").parameter("nc","ACREEDORES VARIOS").one();
        prov = dataManager.reload(prov, "proveedor-view");
        fp.setProveedor(prov);

        return fp;
    }



    @Install(to = "movimientoCuentaDeGastoesDl", target = Target.DATA_LOADER)
    private List<MovimientoCuentaDeGasto> movimientoCuentaDeGastoesDlLoadDelegate(LoadContext<MovimientoCuentaDeGasto> loadContext) {
        loadContext.getQuery().setMaxResults(0);
        List<MovimientoCuentaDeGasto> mm = dataManager.loadList(loadContext);
        User currentUser = userSession.getUser();
        if (currentUser.getLogin().compareTo("carlosconti")==0){
            return mm;
        }else{
            String hql = "select p from test1_Persona p where p.usuario.id = :uid";
            Persona p = dataManager.load(Persona.class).query(hql).parameter("uid",currentUser.getId()).one();
            List<MovimientoCuentaDeGasto> mm_ = new ArrayList();
            for (int i = 0; i < mm.size(); i++) {
                try{
                    MovimientoCuentaDeGasto m = mm.get(i);
                    if (m.getCuentaDeGasto()==null){
                        continue;
                    }
                    if (m.getCuentaDeGasto().getPersona().getId().compareTo(p.getId())==0){
                        mm_.add(m);
                    }
                }catch(Exception exc){
                    int y = 2;
                    notifications.create().withCaption("Error en registro i = " + i).show();
                    return mm_;
                }

            }
            return mm_;
        }


    }


}