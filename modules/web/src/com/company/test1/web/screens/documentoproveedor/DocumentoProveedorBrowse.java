package com.company.test1.web.screens.documentoproveedor;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.company.test1.entity.ordenespago.OrdenPagoFacturaProveedor;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.company.test1.service.ContabiService;
import com.company.test1.service.OrdenPagoService;
import com.company.test1.service.PdfService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Sort;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.HBoxLayout;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import org.apache.http.client.utils.DateUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@UiController("test1_DocumentoProveedor.browse")
@UiDescriptor("documento-proveedor-browse.xml")
@LookupComponent("documentoProveedorsTable")
@LoadDataBeforeShow
@MultipleOpen
public class DocumentoProveedorBrowse extends StandardLookup<DocumentoProveedor> {

    @Inject
    private UiComponents uiComponents;
    @Inject
    private OrdenPagoService ordenPagoService;
    @Inject
    private ScreenBuilders screenBuilders;


    @Inject
    private CollectionLoader<FacturaProveedor> facturaProveedorDl;
    @Inject
    private Table<DocumentoProveedor> documentoProveedorsTable;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private PdfService pdfService;
    @Inject
    private DataManager dataManager;
    @Inject
    private ColeccionArchivosAdjuntosService coleccionArchivosAdjuntosService;
    @Inject
    private ContabiService contabiService;

    public Component getOrdenPagoColumn(DocumentoProveedor dp){
        HBoxLayout hbx = uiComponents.create(HBoxLayout.NAME);
        if (dp instanceof FacturaProveedor){
            OrdenPagoFacturaProveedor opfp = ordenPagoService.devuelveOrdenPagoFacturaProveedor((FacturaProveedor) dp);
            if (opfp!=null){
                Button b = uiComponents.create(Button.NAME);
                b.setCaption("Ver");
                b.addClickListener(e->{
                    ScreenLaunchUtil.launchEditEntityScreen(opfp, null, null, screenBuilders, DocumentoProveedorBrowse.this, OpenMode.NEW_TAB, null, null);
                });
                hbx.add(b);
            }
        }

        return hbx;
    }

    public void onBtnVerReportClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Listado Documentos Proveedor", DocumentoProveedor.class, documentoProveedorsTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Listado Documentos Proveedor.pdf");
    }

    public void onBtnVerEscaneosClick() {
        List<DocumentoProveedor> ffpp = new ArrayList(documentoProveedorsTable.getSelected());
        List<byte[]> documentos = new ArrayList<byte[]>();
        for (int i = 0; i < ffpp.size(); i++) {
            DocumentoProveedor dp = ffpp.get(i);
            if (dp instanceof FacturaProveedor){
                dp = dataManager.reload(dp, "facturaProveedor-view");
            }
            if (dp instanceof Presupuesto){
                dp = dataManager.reload(dp, "presupuesto-view");
            }
            ArchivoAdjunto aa = dp.getColeccionArchivosAdjuntos().getArchivos().get(0);
            ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
            /* PENDIENTE IMPLEMENTAR OTRAS EXTENSIONES */
            if (aaext.getExtension().toLowerCase().compareTo("pdf")==0){
                byte[] bb = aaext.getRepresentacionSerial();
                bb = Base64.getMimeDecoder().decode(bb);
                bb = Base64.getMimeDecoder().decode(bb);
                documentos.add(bb);

            }
        }
        byte[] bb = pdfService.concatPdfs(documentos, false);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Escaneos Documentos Proveedor.pdf");
    }

    @Inject
    private Notifications notifications;

    @Subscribe("btnPublicarContabilidad")
    public void onBtnPublicarContabilidadClick(Button.ClickEvent event) {
        DocumentoImputable fprov = documentoProveedorsTable.getSingleSelected();
        if (fprov==null){
            notifications.create().withCaption("Seleccionar un registro").show();
            return;
        }
        if (DateUtils.parseDate("31-12-2021", new String[]{"dd-MM-yyyy"}).getTime()>fprov.getFechaEmision().getTime()){
            notifications.create().withCaption("Solo se pueden publicar facturas con fecha posterior a 31-12-2021").show();
            return;
        }
        if (fprov instanceof FacturaProveedor){
            try {
                boolean res = contabiService.publicaContabilizacionFacturaProveedor((FacturaProveedor) fprov);
                if (res){
                    notifications.create().withCaption("Factura publicada corréctamente").show();
                }
            } catch (Exception e) {
                notifications.create().withCaption("Error al publicar").withDescription(e.getMessage()).show();
                return;
            }
        }else{
            notifications.create().withCaption("Seleccionar un registro tipo Factra Proveedor (FP)").show();
            return;
        }

    }

    @Subscribe("btnComprobarPublicacion")
    public void onBtnComprobarPublicacionClick(Button.ClickEvent event) {
        DocumentoImputable fprov = documentoProveedorsTable.getSingleSelected();
        if (fprov==null){
            notifications.create().withCaption("Seleccionar un registro").show();
            return;
        }
        try {
            String auth_token = contabiService.getAuthToken("admin", "r21613a");
            if (contabiService.comprobarPublicacionFacturaProveedor((FacturaProveedor) fprov, auth_token)) {
                notifications.create().withCaption("Factura ya publicada en Contabilidad").show();
            } else {
                notifications.create().withCaption("Factura pendiente de publicar en Contabilidad").show();
            }
        }catch(Exception exc){
            notifications.create().withCaption(exc.getMessage()).show();
        }
    }

    @Install(to = "facturaProveedorDl", target = Target.DATA_LOADER)
    private List<FacturaProveedor> facturaProveedorDlLoadDelegate(LoadContext<FacturaProveedor> loadContext) {
        if (loadContext.getQuery().getParameters().size()==0){
            return dataManager.loadList(loadContext);
        }else{
            return dataManager.loadList(loadContext);
        }
    }


}