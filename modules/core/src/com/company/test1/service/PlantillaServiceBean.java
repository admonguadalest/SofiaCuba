package com.company.test1.service;

import com.company.test1.entity.contratosinquilinos.ParametroValorAnexo;
import com.company.test1.entity.reportsyplantillas.Plantilla;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(PlantillaService.NAME)
public class PlantillaServiceBean implements PlantillaService {

    public List<ParametroValorAnexo> devuelveParametrosDePlantilla(Plantilla p){
        List<ParametroValorAnexo> parametros = new ArrayList<ParametroValorAnexo>();
        int currPos0;
        currPos0 = p.getContenidoPlantilla().indexOf("@[");
        while(currPos0!=-1){
            int pos1 = p.getContenidoPlantilla().indexOf("]",currPos0);
            String nombrePam = p.getContenidoPlantilla().substring(currPos0+2,pos1);
            if (nombrePam.indexOf(".")==-1){
                ParametroValorAnexo pva = new ParametroValorAnexo();
                pva.setNombreParametro(nombrePam);
                parametros.add(pva);
            }

            currPos0 = p.getContenidoPlantilla().indexOf("@[",currPos0+2);
        }

        return parametros;
    }

//    public  List<String> getListaParametrosPlantillaTodos(){
//        List l = new ArrayList();
//        int currPos0;
//        currPos0 = this.contenidoPlantilla.indexOf("@[");
//        while(currPos0!=-1){
//            int pos1 = this.contenidoPlantilla.indexOf("]",currPos0);
//            String nombrePam = this.contenidoPlantilla.substring(currPos0+2,pos1);
//            l.add(nombrePam);
//            currPos0 = this.contenidoPlantilla.indexOf("@[",currPos0+2);
//        }
//        return l;
//
//    }



}