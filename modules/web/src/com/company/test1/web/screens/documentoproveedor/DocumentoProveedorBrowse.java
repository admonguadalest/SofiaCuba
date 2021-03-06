package com.company.test1.web.screens.documentoproveedor;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.company.test1.entity.ordenespago.OrdenPagoFacturaProveedor;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.company.test1.service.OrdenPagoService;
import com.company.test1.service.PdfService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.HBoxLayout;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@UiController("test1_DocumentoProveedor.browse")
@UiDescriptor("documento-proveedor-browse.xml")
@LookupComponent("documentoProveedorsTable")
@LoadDataBeforeShow
public class DocumentoProveedorBrowse extends StandardLookup<DocumentoProveedor> {

    @Inject
    private UiComponents uiComponents;
    @Inject
    private OrdenPagoService ordenPagoService;
    @Inject
    private ScreenBuilders screenBuilders;
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
            if (aaext.getExtension().compareTo("pdf")==0){
                byte[] bb = aaext.getRepresentacionSerial();
                bb = Base64.getMimeDecoder().decode(bb);
                bb = Base64.getMimeDecoder().decode(bb);
                documentos.add(bb);

            }
        }
        byte[] bb = pdfService.concatPdfs(documentos, false);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Escaneos Documentos Proveedor.pdf");
    }
}