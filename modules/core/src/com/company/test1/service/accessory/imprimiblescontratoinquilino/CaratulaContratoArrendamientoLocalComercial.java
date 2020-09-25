package com.company.test1.service.accessory.imprimiblescontratoinquilino;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.*;

import com.company.test1.NumerosATexto;
import com.company.test1.entity.*;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;


import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;
import com.company.test1.entity.enums.UsoContratoEnum;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.accessory.SIJRBeanDataSource;
import com.google.common.io.Resources;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author Carlos Conti
 */
public class CaratulaContratoArrendamientoLocalComercial extends CaratulaContratoArrendamiento{

    List dataSource1 = null;
    List dataSource2 = null;


    public CaratulaContratoArrendamientoLocalComercial(ContratoInquilino contratoInquilino){
        super(contratoInquilino);

    }

    private Integer calculaEdadRepresentante(){
        PersonaFisica pf = (PersonaFisica) this.contratoInquilino.getRepresentanteArrendador();
        Date d = new Date();
        long edad = d.getTime() - pf.getFechaNacimiento().getTime();
        long ms_ano = (long)1000 * (long)60 * (long)60 * (long)24 * (long)365;
        long anos = edad / ms_ano;
        anos = anos / 1;
        return new Long(anos).intValue();
    }

    public String getNombreMes(int m){
        if (m==0){
            return "Enero";
        }else if(m==1){
            return "Febrero";
        }
        else if(m==2){
            return "Marzo";
        }else if(m==3){
            return "Abril";
        }else if(m==4){
            return "Mayo";
        }else if(m==5){
            return "Junio";
        }else if(m==6){
            return "Julio";
        }else if(m==7){
            return "Agosto";
        }else if(m==8){
            return "Septiembre";
        }else if(m==9){
            return "Octubre";
        }else if(m==10){
            return "Noviembre";
        }else if(m==11){
            return "Diciembre";
        }
        return null;
    }

    public byte[] getReportAsByteArray() throws Exception{
        PersonaFisica representanteArrendador = (PersonaFisica) this.contratoInquilino.getRepresentanteArrendador();
        PersonaFisica representanteArrendataria = (PersonaFisica) this.contratoInquilino.getRepresentanteArrendatario();

        if (representanteArrendataria==null){
            this.pathMaestro = "CaratulaContratoArrendamientoLocalComercialSinRepresentacionArrendatario.jrxml";
        }else{
            this.pathMaestro = "CaratulaContratoArrendamientoLocalComercial.jrxml";
        }
//                if (contratoInquilino.getArrendatarioSinRepresentacion()){
//                    this.pathMaestro = "/jasperreports/contratosinquilinos/CaratulaContratoArrendamientoLocalComercialSinRepresentacionArrendatario.jrxml";
//                }else{
//                    this.pathMaestro = "/jasperreports/contratosinquilinos/CaratulaContratoArrendamientoLocalComercial.jrxml";
//                }
        Object  s = Resources.getResource("/com/company/test1/service/accessory/" + pathMaestro).getContent();
        JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
        JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);



//        byte[] bb = e.getImagenContratos().getArchivoAdjunto().getRepresentacionSerialExtDoc(SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
////                BufferedImage bim = javax.imageio.ImageIO.read(new ByteArrayInputStream(e.getImagenContratos().getArchivoAdjunto().getRepresentacionSerialExtDoc(SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs())));
//        JRRenderable jrr = JRImageRenderer.getInstance(bb);
        JRRenderable jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("CaratulaContratosGuadalest.jpg");


        this.dataSource1 = new ArrayList();
        this.dataSource1.add(this.contratoInquilino);
        SIJRBeanDataSource sijr1 = new SIJRBeanDataSource(this.dataSource1);

        //instancias utilitarias rellenado parametros

        //Parte comprobaciones
        if (this.contratoInquilino.getFechaRealizacion() == null) {
            throw new Exception("En la pestaña Contrato espeficique Fecha Realización");
        }

        if (this.contratoInquilino.getDepartamento().getPropietarioEfectivo() == null) {
            throw new Exception("En la cabecera espeficique Propietario Efectivo");
        }

        if (this.contratoInquilino.getInquilino() == null) {
            throw new Exception("En la cabecera espeficique Inquilino");
        }

        Calendar c = GregorianCalendar.getInstance();

        c.setTime(this.contratoInquilino.getFechaRealizacion());
        Persona arrendador = this.contratoInquilino.getDepartamento().getPropietarioEfectivo().getPersona();
        Persona inquilino = this.contratoInquilino.getInquilino();
        RepresentacionEnCalidadDe enCalidadDe = null;
        try{
            enCalidadDe = RepresentacionLegal.getTipoRepresentacion(this.contratoInquilino.getInquilino(), representanteArrendataria);
        }catch(Exception exc){
            throw exc;
        }
        if (representanteArrendataria == null){
            if (inquilino instanceof PersonaFisica){
                representanteArrendataria = (PersonaFisica)inquilino;
            }
        }
        Direccion domicilioArrendador = Direccion.getDireccionDesdeNombre(this.contratoInquilino.getDepartamento().getPropietarioEfectivo().getPersona(),Direccion.NOMBRE_DIRECCION_PROPIETARIO_CONTRATO_N19);
        Direccion domicilioInquilino = Direccion.getDireccionDesdeNombre(this.contratoInquilino.getInquilino(),Direccion.NOMBRE_DIRECCION_INQUILINO);
        Departamento departamentoLocal = this.contratoInquilino.getDepartamento();
        //CedulaHabitabilidad cedulaHabitabilidad = departamentoVivienda.getEscogeCedulaHabitabilidadMasVigente();
        Direccion dirLocal = departamentoLocal.getUbicacion().getDireccion();
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        String rentaContractual = nf.format(this.contratoInquilino.getRentaContractual());
        long plazoMilisecs = this.contratoInquilino.getFechaVencimientoPrevisto().getTime() - this.contratoInquilino.getFechaOcupacion().getTime();
        long plazoAnnos = 0;
        long milisecsAnno = (long)1000 * (long)60 * (long)60 * (long)24 * (long)365;
        plazoAnnos = plazoMilisecs / milisecsAnno;
        plazoAnnos = Math.round(plazoAnnos);
        String plazo_contratoInquilino = new Integer(new Long(plazoAnnos).intValue()).toString();

        //parametros
        this.setParameters(new Hashtable<String,Object>());
        this.getParameters().put("en_ciudad", this.contratoInquilino.getLugarRealizacion());
        this.getParameters().put("a_dia", new Integer(c.get(Calendar.DAY_OF_MONTH)).toString());
        this.getParameters().put("de_mes", getNombreMes(c.get(Calendar.MONTH)));
        this.getParameters().put("de_ano", new Integer(c.get(Calendar.YEAR)).toString());
        this.getParameters().put("parte_arrendadora", arrendador.getNombreCompleto() );
        this.getParameters().put("arrendador_representado_por", representanteArrendador.getNombreCompleto());
        this.getParameters().put("domicilio_arrendador", domicilioArrendador.getDireccionParaDocumento());

        this.getParameters().put("edad_representante_arrendador", "0");
        this.getParameters().put("estado_representante_arrendador", representanteArrendador.getEstadoCivil());
        this.getParameters().put("dni_representante_arrendador", representanteArrendador.getNifDni());
        this.getParameters().put("razon_social_sociedad_arrendadora", arrendador.getNombreCompleto());
        this.getParameters().put("cif_arrendadora", arrendador.getNifDni());
        this.getParameters().put("nombre_completo_arrendataria", inquilino.getNombreCompleto());
        this.getParameters().put("dni_arrendataria", inquilino.getNifDni());
        this.getParameters().put("domicilio_arrendataria", domicilioInquilino.getDireccionParaDocumento());
        this.getParameters().put("precio_arrendamiento", rentaContractual);
        this.getParameters().put("local", departamentoLocal.getPiso() + " " + departamentoLocal.getPuerta());
        this.getParameters().put("calle", dirLocal.getNombreVia() + " " + dirLocal.getNumeroVia() + " " + dirLocal.getEscalera());
        this.getParameters().put("ciudad", dirLocal.getPoblacion());
        this.getParameters().put("ref_catastral", departamentoLocal.getReferenciaCatastral());
        this.getParameters().put("cp", dirLocal.getCodigoPostal());
        this.getParameters().put("provincia", dirLocal.getProvincia());
        this.getParameters().put("no_contrato", this.contratoInquilino.getNumeroContrato());
        this.getParameters().put("IMAGEN_MARGEN_IZQUIERDO", jrr);

        if (this.contratoInquilino.getEstadoContrato()== EstadoContratoInquilinoEnum.AUTORIZADO){
            this.getParameters().put("ES_BORRADOR",false);
        }else{
            this.getParameters().put("ES_BORRADOR",true);

        }

        if (this.contratoInquilino.getUsoContrato()== UsoContratoEnum.LOCAL){
            this.getParameters().put("USO_CONTRATO", "Local Comercial");
        }else if(this.contratoInquilino.getUsoContrato()==UsoContratoEnum.ESTUDIO){
            this.getParameters().put("USO_CONTRATO", "Estudio");
        }

        String textoCedula = "(N/D)";

        this.getParameters().put("no_cedula_habitabilidad", textoCedula);
        nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(0);
        String superficie = nf.format(this.contratoInquilino.getDepartamento().getSuperficie());
        String vecindad_arrendataria = this.contratoInquilino.getDepartamento().getUbicacion().getDireccion().getPoblacion();

        String nif_representado_arrendataria = null;
        RepresentacionEnCalidadDe calidad_de_representado = RepresentacionEnCalidadDe.REPRESENTANTE;
        if (representanteArrendataria != null){
            nif_representado_arrendataria = representanteArrendataria.getNifDni();
            calidad_de_representado = enCalidadDe;
        }

        Integer parteNoFraccionaria = this.contratoInquilino.getRentaContractual().intValue();
        Integer parteFraccionaria = new Double(Math.round(new Double((this.contratoInquilino.getRentaContractual() % 1)*100))).intValue();
        String strParteNoFraccionaria = new NumerosATexto().convertirLetras(parteNoFraccionaria);
        String strParteFraccionaria = new NumerosATexto().convertirLetras(parteFraccionaria);
        if (strParteNoFraccionaria.trim().length()==0){
            strParteNoFraccionaria = "cero";
        }
        if (strParteFraccionaria.trim().length()==0){
            strParteFraccionaria = "cero";
        }
        String renta_contractual_texto = strParteNoFraccionaria + " euros con " +
                strParteFraccionaria + " céntimos";
        renta_contractual_texto = renta_contractual_texto.toUpperCase();
        String renta_contractual_numeros = rentaContractual;
        String nombre_periodo_devengo_rentas = "mes";
        this.getParameters().put("superficie", superficie);
        this.getParameters().put("vecindad_arrendataria", vecindad_arrendataria);

        if (representanteArrendataria == null){
            this.getParameters().put("nombre_representado_arrendataria", "");
            this.getParameters().put("nif_representado_arrendataria", "");
            this.getParameters().put("calidad_de_representado", "");
        }else{
            this.getParameters().put("nombre_representado_arrendataria", representanteArrendataria.getNombreCompleto());
            this.getParameters().put("nif_representado_arrendataria", nif_representado_arrendataria);
            this.getParameters().put("calidad_de_representado", calidad_de_representado);
        }




        this.getParameters().put("plazo_contrato", plazo_contratoInquilino);
        this.getParameters().put("renta_contractual_texto", renta_contractual_texto);
        this.getParameters().put("renta_contractual_numeros", renta_contractual_numeros);
        this.getParameters().put("nombre_periodo_devengo_rentas", nombre_periodo_devengo_rentas);

        byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, this.getParameters(),sijr1);
        return bytearr;


    }


}