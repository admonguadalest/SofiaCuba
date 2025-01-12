package com.company.test1.web.screens.documentoproveedor;

import com.company.test1.StringUtils;
import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.company.test1.entity.ordenespago.OrdenPagoFacturaProveedor;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.ReciboReport;
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
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.gui.ReportGuiManager;
import com.haulmont.yarg.reporting.ReportOutputDocument;
import org.apache.http.client.utils.DateUtils;


import javax.inject.Inject;
import java.text.DecimalFormat;
import java.util.*;

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
    @Inject
    private Label<String> lblTotales;
    @Inject
    private UserSession userSession;
    @Inject
    private ReportGuiManager reportGuiManager;

    public Component getOrdenPagoColumn(DocumentoProveedor dp){
        HBoxLayout hbx = uiComponents.create(HBoxLayout.NAME);
        if (dp instanceof FacturaProveedor){
            OrdenPagoFacturaProveedor opfp = ordenPagoService.devuelveOrdenPagoFacturaProveedor((FacturaProveedor) dp);
            if (opfp!=null){
                Button b = uiComponents.create(Button.NAME);
                b.setCaption("Ver");
                b.addClickListener(e->{
                    screenBuilders.editor(OrdenPagoFacturaProveedor.class, DocumentoProveedorBrowse.this)
                            .editEntity(opfp)
                            .withOpenMode(OpenMode.DIALOG)
                            .build()
                            .show();
                    //ScreenLaunchUtil.launchEditEntityScreen(opfp, null, null, screenBuilders, DocumentoProveedorBrowse.this, OpenMode.NEW_TAB, null, null);
                });
                hbx.add(b);
            }
        }

        return hbx;
    }

    public void onBtnExcel(){
        ArrayList<Recibo> rbos = new ArrayList(documentoProveedorsTable.getSelected());
        if (rbos.size()==0){
            notifications.create().withCaption("Seleccionar al menos una factura").show();
        }
        ArrayList<ReciboReport> rrs = new ArrayList();
        for (int i = 0; i < rbos.size(); i++) {
            Recibo r = rbos.get(i);
            r = dataManager.reload(r, "recibo-report-view");
            ReciboReport rr = dataManager.create(ReciboReport.class);
            rr.setUbicacion(r.getUtilitarioContratoInquilino().getDepartamento().getUbicacion().getNombre());
            rr.setPiso(r.getUtilitarioContratoInquilino().getDepartamento().getPiso());
            rr.setPuerta(r.getUtilitarioContratoInquilino().getDepartamento().getPuerta());
            rr.setFechaEmision(r.getFechaEmision());
            rr.setNumRecibo(r.getNumRecibo());
            rr.setNombreInquilino(r.getUtilitarioContratoInquilino().getInquilino().getNombreCompleto());
            rr.setImporte(r.getTotalRecibo());
            rr.setCuotaIrpf(r.getTotalIRPF());
            rr.setCuotaIva(r.getTotalIVA());
            rr.setImporteFinal(r.getTotalReciboPostCCAA());
            rr.setAmpliacion(r.getAmpliacion());
            rr.setReciboId(r.getId().toString().replace("-",""));
            rr.setRemesa(r.getInfoRemesa());
            rr.setPropietario(r.getUtilitarioContratoInquilino().getDepartamento().getPropietarioEfectivo().getPersona().getNombreCompleto());
            //rr.setPropietario("pruebas");
            rrs.add(rr);
        }

        Collections.sort(rrs, ReciboReport.getNaturalComparator());

        Report r = dataManager.load(Report.class).query("select r from report$Report r " +
                "where r.name = :rn").parameter("rn", "RecibosEmitidos")
                .one();
        HashMap pams = new HashMap();
        /*pams.put("entities", asignacionTareasProgramadasDc.getItems());
        pams.put("TITULO", "TAREAS PROGRAMADAS");
        pams.put("FECHA_REPORT", new Date());*/
        pams.put("recibos",rrs);
        pams.put("nombreCompleto", "[NOMBRE EMPRESA]");
        pams.put("titulo", "Recibos Emitidos Periodo");
        pams.put("fechaRealizacion", new Date());

        pams.put("periodoFechaDesde", new Date());
        pams.put("periodoFechaHasta", new Date());
        ReportOutputDocument rod = reportGuiManager.getReportResult(r, pams, null);
        exportDisplay.show(new ByteArrayDataProvider(rod.getContent()), "Emitidas.xlsx");
        int y = 2;
    }


    public void onBtnVerReportClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Listado Documentos Proveedor", DocumentoProveedor.class, documentoProveedorsTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Listado Documentos Proveedor.pdf");
    }

    public void onBtnVerEscaneosClick() {
        List<DocumentoProveedor> ffpp = new ArrayList(documentoProveedorsTable.getSelected());
        List<byte[]> documentos = new ArrayList<byte[]>();
        String nombreDescargable = "Escaneos Documento Proveedor.pdf";
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
        if (ffpp.size()==1){
            DocumentoProveedor dp = ffpp.get(0);
            nombreDescargable = dp.getProveedor().getPersona().getNombreCompleto() + " " + dp.getNumDocumento();
            nombreDescargable = StringUtils.obtenerStringSinCaracteresIncorrectos(nombreDescargable);
            nombreDescargable = nombreDescargable + ".pdf";
        }
        byte[] bb = pdfService.concatPdfs(documentos, false);
        exportDisplay.show(new ByteArrayDataProvider(bb), nombreDescargable);
    }

    @Inject
    private Notifications notifications;

    @Subscribe("btnPublicarContabilidad")
    public void onBtnPublicarContabilidadClick(Button.ClickEvent event) {
        User user = userSession.getUser();
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
                boolean res = contabiService.publicaContabilizacionFacturaProveedor(user, (FacturaProveedor) fprov);
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
        User user = userSession.getUser();
        if (fprov==null){
            notifications.create().withCaption("Seleccionar un registro").show();
            return;
        }
        try {
            String auth_token = contabiService.getAuthToken(user, "admin", "r21613a");
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
        List<FacturaProveedor> ffpp = null;
        if (loadContext.getQuery().getParameters().size()==0){
            ffpp =  new ArrayList();
        }else{
            ffpp  = dataManager.loadList(loadContext);
        }
        double total = 0.0;
        for (int i = 0; i < ffpp.size(); i++) {
            FacturaProveedor fp = ffpp.get(i);
            if (fp.getImportePostCCAA()!=null) {
                total += fp.getImportePostCCAA();
            }
        }
        DecimalFormat df = new DecimalFormat();
        lblTotales.setValue(df.format(total));
        return ffpp;
    }


}