package com.company.test1.web.screens.documentosfotograficos;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.FotosThumbnailExt;
import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.ciclos.Evento;
import com.company.test1.entity.documentosfotograficos.FotoDocumentoFotografico;
import com.company.test1.entity.documentosfotograficos.FotoThumbnail;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.google.common.collect.LinkedHashMultimap;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.gui.components.Resource;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.poi.util.IOUtils;
import org.springframework.core.io.ByteArrayResource;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@UiController("test1_CarpetaDocumentosFotograficos.edit")
@UiDescriptor("carpeta-documentos-fotograficos-edit.xml")
@EditedEntityContainer("carpetaDocumentosFotograficosDc")
@LoadDataBeforeShow
public class CarpetaDocumentosFotograficosEdit extends StandardEditor<CarpetaDocumentosFotograficos> {

    @Inject
    private Image previsualizador;
    @Inject
    private DataManager dataManager;
    @Inject
    private ColeccionArchivosAdjuntosService coleccionArchivosAdjuntosService;

    @Inject
    private InstanceContainer<FotosThumbnailExt> fotoThumbnailExt;
    @Inject
    private InstanceContainer<FotoThumbnail> fotoThumbnai;
    @Inject
    private FileUploadingAPI fileUploadingAPI;

    @Inject
    private FileMultiUploadField multiUploadField;
    Ciclo ciclo = null;
    @Inject
    private Notifications notifications;
    @Inject
    private FileLoader fileLoader;
    @Inject
    private InstanceContainer<CarpetaDocumentosFotograficos> carpetaDocumentosFotograficosDc;
    @Inject
    private DataContext dataContext;
    @Inject
    private CollectionPropertyContainer<FotoDocumentoFotografico> fotoDocumentoFotograficoesDc;

    @Subscribe
    public void onInit(InitEvent event) {

        multiUploadField.addQueueUploadCompleteListener(queueUploadCompleteEvent -> {
            for (Map.Entry<UUID, String> entry : multiUploadField.getUploadsMap().entrySet()) {
                UUID fileId = entry.getKey();
                String fileName = entry.getValue();
                FileDescriptor fd = fileUploadingAPI.getFileDescriptor(fileId, fileName);
                try {
                    fileUploadingAPI.putFileIntoStorage(fileId, fd);
                } catch (FileStorageException e) {
                    throw new RuntimeException("Error saving file to FileStorage", e);
                }
                dataManager.commit(fd);
                try {
                    FotoDocumentoFotografico fdf = dataContext.create(FotoDocumentoFotografico.class);
                    fdf.setCarpeta(this.getEditedEntity());
                    InputStream is = fileLoader.openStream(fd);
                    byte[] bb = IOUtils.toByteArray(is);

                    fdf.setNombreArchivo(fd.getName());
                    fdf.setExtension(fd.getExtension());
                    fdf.setMimeType("");
                    fdf.setNombreArchivoOriginal(fd.getName());
                    fdf.setRepresentacionSerial(bb);
                    fdf.setTamano(bb.length);

                    FotoThumbnail fth = dataContext.create(FotoThumbnail.class);
                    fth.setCarpetaDocumentoFotografico(this.getEditedEntity());
                    fth.setFotoDocumentoFotografico(fdf);
                    fdf.setFotoThumbnail(fth);

                    BufferedImage bim = ImageIO.read(new ByteArrayInputStream(bb));
                    bim = resize(bim, 640,480);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bim, fdf.getExtension(), baos);
                    byte[] bb2 = baos.toByteArray();
                    fth.setRepresentacionSerial(bb2);
                    fth.setTamano(bb2.length);

//                    carpetaDocumentosFotograficosDc.getItem().getFotos().add(fdf);
                    carpetaDocumentosFotograficosDc.getItem().getFotosThumbnail().add(fth);
                    fotoDocumentoFotograficoesDc.getMutableItems().add(fdf);



                }catch(Exception exc){

                }
            }
            carpetaDocumentosFotograficosDc.getItem().setNumeroDeFotografias(carpetaDocumentosFotograficosDc.getItem().getFotos().size());

            notifications.create()
                    .withCaption("Archivos cargados exitósamente")
                    .show();
            multiUploadField.clearUploads();


        });

        multiUploadField.addFileUploadErrorListener(queueFileUploadErrorEvent -> {
            notifications.create()
                    .withCaption("File upload error")
                    .show();
        });
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        java.awt.Image tmp = img.getScaledInstance(newW, newH, java.awt.Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }


    public void setCiclo(Ciclo c){
        
        this.ciclo = c;
    }

    @Subscribe("fotoDocumentoFotograficoesTable")
    public void onFotoDocumentoFotograficoesTableSelection(Table.SelectionEvent<FotoDocumentoFotografico> event) {
        FotoDocumentoFotografico fdf = event.getSource().getSingleSelected();
        if (fdf!=null){
            FotosThumbnailExt fth = coleccionArchivosAdjuntosService.getFotoDocumentoFotograficoExt(fdf);
            if (fth!=null) {
                fotoThumbnailExt.setItem(fth);
                previsualizador.setValueSource(new ContainerValueSource<>(fotoThumbnailExt, "representacionSerial"));
                previsualizador.setScaleMode(Image.ScaleMode.CONTAIN);
            }else{
                //en caso que esten acabados de cargar, se debera ver por la previsualizacion transient
                fotoThumbnai.setItem(fdf.getFotoThumbnail());
                previsualizador.setValueSource(new ContainerValueSource<>(fotoThumbnai, "representacionSerial"));
                previsualizador.setScaleMode(Image.ScaleMode.CONTAIN);
            }

        }
    }

    @Install(to = "eventosDl", target = Target.DATA_LOADER)
    private List<Evento> eventosDlLoadDelegate(LoadContext<Evento> loadContext) {
        Ciclo c = this.ciclo;
        c = dataManager.reload(c, "ciclo-view");
        return c.getEventos();

    }

    public void removeSelected(){

    }
    
    
    
    
}