package com.company.test1.web.screens.documentosfotograficos;

import com.company.test1.entity.FotosThumbnailExt;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.entity.documentosfotograficos.FotoDocumentoFotografico;
import com.company.test1.entity.documentosfotograficos.FotoThumbnail;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;

@UiController("test1_VisorDocumentosFotograficos")
@UiDescriptor("visor-documentos-fotograficos.xml")
public class VisorDocumentosFotograficos extends Screen {

    @Inject
    private Button btnAnterior;
    @Inject
    private Button btnPosterior;
    @Inject
    private Image imageContainer;
    @Inject
    private InstanceContainer<FotosThumbnailExt> fotosThumbnailExtDc;
    @Inject
    private InstanceContainer<FotoThumbnail> fotosThumbnailDc;
    CarpetaDocumentosFotograficos carpeta = null;
    Integer currSel = 0;
    @Inject
    private ColeccionArchivosAdjuntosService coleccionArchivosAdjuntosService;

    public void setCarpeta(CarpetaDocumentosFotograficos cdf){
        this.carpeta = cdf;
        loadSelImage();
    }

    private void loadSelImage(){
        FotoDocumentoFotografico fdf = carpeta.getFotos().get(currSel);

        FotosThumbnailExt fth = coleccionArchivosAdjuntosService.getFotoDocumentoFotograficoExt(fdf);
        if (fth!=null){
            fotosThumbnailExtDc.setItem(fth);
            imageContainer.setValueSource(new ContainerValueSource<>(fotosThumbnailExtDc, "representacionSerial"));
        }else{
            fotosThumbnailDc.setItem(fdf.getFotoThumbnail());
            imageContainer.setValueSource(new ContainerValueSource<>(fotosThumbnailDc, "representacionSerial"));
        }

    }

    public void anterior(){
        currSel--;
        if (currSel.intValue() <0){
            currSel = this.carpeta.getNumeroDeFotografias()-1;
        }
        loadSelImage();
    }

    public void posterior(){
        currSel++;
        if (currSel.intValue() == carpeta.getNumeroDeFotografias().intValue()){
            currSel = 0;
        }
        loadSelImage();
    }



}