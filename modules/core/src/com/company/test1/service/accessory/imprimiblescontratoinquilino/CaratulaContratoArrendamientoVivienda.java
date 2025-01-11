package com.company.test1.service.accessory.imprimiblescontratoinquilino;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import com.company.test1.NumerosATexto;
import com.company.test1.entity.Direccion;
import com.company.test1.entity.Persona;
import com.company.test1.entity.PersonaFisica;
import com.company.test1.entity.PersonaJuridica;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.contratosinquilinos.CotitularContratoInquilino;
import com.company.test1.entity.departamentos.CedulaHabitabilidad;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;
import com.company.test1.entity.enums.NombreTipoDireccion;
import com.company.test1.entity.enums.TipoContratoInquilinoEnum;
import com.company.test1.entity.enums.UsoContratoEnum;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.accessory.SIJRBeanDataSource;

import com.google.common.io.Resources;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.*;

import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.*;

/**
 *
 * @author Carlos Conti
 */
public class CaratulaContratoArrendamientoVivienda extends CaratulaContratoArrendamiento{

    List dataSource1 = null;
    List dataSource2 = null;

    boolean esContratoTemporal = false;



    public CaratulaContratoArrendamientoVivienda(ContratoInquilino c){
        super(c);
        this.pathMaestro = "CaratulaContratoArrendamientoVivienda.jrxml";
    }

    public CaratulaContratoArrendamientoVivienda(ContratoInquilino c, boolean esContratoTemporal){
        super(c);
        if (!esContratoTemporal)
        this.pathMaestro = "CaratulaContratoArrendamientoVivienda.jrxml";
        else this.pathMaestro = "CaratulaContratoArrendamientoTemporalVivienda.jrxml";
    }

    //Lista de parametros a definir desde la entidad contrato
    /*
     * en_ciudad
     * a_dia
     * de_mes
     * de_ano
     * parte_arrendataria
     * arrendador_representado_por
     * domicilio_arrendador
     * edad_representante_arrendador
     * estado_representante_arrendador
     * estado_representante_arrendador
     * dni_representante_arrendador
     * razon_social_sociedad_arrendadora
     * cif_arrendadora
     *
     * nombre_completo_arrendataria
     * dni_arrendataria
     * domicilio_arrendataria
     * precio_arrendamiento
     *
     * vivienda
     * calle
     * ciudad
     * ref_catastral
     * cp
     * provincia
     *
     */
    public byte[] getReportAsByteArray() throws Exception{

        //verificacion del tipo de contrato
        if (this.contratoInquilino.getTipoContrato()== TipoContratoInquilinoEnum.ANTIGUA_LAU){
            this.pathMaestro = "CaratulaContratoIndefinidoArrendamientoVivienda.jrxml";
        }

        Object  s = Resources.getResource("/com/company/test1/service/accessory/" + pathMaestro).getContent();
        JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
        JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);




        //Provisional
        JRRenderable jrr = null;

        String nifPropietario = this.contratoInquilino.getDepartamento().getPropietarioEfectivo().getPersona().getNifDni();
        if (nifPropietario.compareTo("B75537878")==0){
            jrr = (JRRenderable)AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("CaratulaContratosGrupoDomus.jpg");
        }else{
            jrr = (JRRenderable)AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("CaratulaContratosGuadalest.jpg");
        }
        //PENDIENTE ADAPTAR A CONTRATOS DE MARVALLOS


        //Parte comprobaciones
        if (this.contratoInquilino.getFechaRealizacion() == null) {
            throw new Exception("En la pestaña Contrato espeficique Fecha Realización");
        }

        if (this.contratoInquilino.getRepresentanteArrendador() == null) {

            throw new Exception("En la cabecera espeficique Representante Arrendador");
        }

        if (this.contratoInquilino.getInquilino() == null) {
            throw new Exception("En la cabecera espeficique Inquilino");
        }

        if (this.contratoInquilino.getPlazoAnyos()==null){
            throw new Exception("En la pestaña Contrato espeficique Plazo");
        }

        if (this.contratoInquilino.getPlazoAnyosProrrogable()==null){
            throw new Exception("En la pestaña Contrato espeficique Plazo Prorrogable");
        }

        this.dataSource1 = new ArrayList();
        this.dataSource1.add(this.contratoInquilino);
        SIJRBeanDataSource sijr1 = new SIJRBeanDataSource(this.dataSource1);

        //instancias utilitarias rellenado parametros

        Calendar c = GregorianCalendar.getInstance();
        c.setTime(this.contratoInquilino.getFechaRealizacion());
        Persona arrendador = this.contratoInquilino.getDepartamento().getPropietarioEfectivo().getPersona();

        Persona inquilino = this.contratoInquilino.getInquilino();
        PersonaFisica representanteArrendador = (PersonaFisica) this.contratoInquilino.getRepresentanteArrendador();
        representanteArrendador = AppBeans.get(Persistence.class).getEntityManager().reload(representanteArrendador, "personaFisica-view");
        this.contratoInquilino.setRepresentanteArrendador(representanteArrendador);
        PersonaFisica representanteArrendataria = (PersonaFisica) this.contratoInquilino.getRepresentanteArrendatario();
        if (representanteArrendataria!=null){
            representanteArrendataria = AppBeans.get(Persistence.class).getEntityManager().reload(representanteArrendataria, "personaFisica-view");
            this.contratoInquilino.setRepresentanteArrendatario(representanteArrendataria);
        }


        Direccion domicilioArrendador = Direccion.getDireccionDesdeNombre(this.contratoInquilino.getDepartamento().getPropietarioEfectivo().getPersona(),NombreTipoDireccion.DOMICILIO_ADMINISTRADOR.getId());
        Direccion domicilioInquilino = Direccion.getDireccionDesdeNombre(this.contratoInquilino.getInquilino(), NombreTipoDireccion.DOMICILIO_INQUILINO.getId());
        Departamento departamentoVivienda =  this.contratoInquilino.getDepartamento();
        CedulaHabitabilidad cedulaHabitabilidad = Departamento.getCedulaHabitabilidadMasVigente(departamentoVivienda);
        Direccion dirVivienda = departamentoVivienda.getUbicacion().getDireccion();
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        String rentaContractual = nf.format(this.contratoInquilino.getRentaContractual());

        String textoParteArrendadora = this.getTextoParteArrendadora(
                arrendador.getNombreCompleto(),
                representanteArrendador.getNombreCompleto(),
                domicilioArrendador.getDireccionParaDocumento(),
                representanteArrendador.getNifDni(),
                arrendador.getNombreCompleto(),
                arrendador.getNifDni());
        //parametros
        this.setParameters(new Hashtable<String,Object>());
        this.getParameters().put("en_ciudad", this.contratoInquilino.getLugarRealizacion());
        this.getParameters().put("a_dia", new Integer(c.get(Calendar.DAY_OF_MONTH)).toString());
        this.getParameters().put("de_mes", getNombreMes(c.get(Calendar.MONTH)));
        this.getParameters().put("de_ano", new Integer(c.get(Calendar.YEAR)).toString());
        this.getParameters().put("parte_arrendadora", arrendador.getNombreCompleto() );
        this.getParameters().put("arrendador_representado_por", representanteArrendador.getNombreCompleto());
        this.getParameters().put("domicilio_arrendador", domicilioArrendador.getDireccionParaDocumento());
        this.getParameters().put("edad_representante_arrendador", new Integer(this.calculaEdadRepresentante()).toString());
        if (representanteArrendador.getEstadoCivil()!=null){
            this.getParameters().put("estado_representante_arrendador", representanteArrendador.getEstadoCivil());
        }else{
            this.getParameters().put("estado_representante_arrendador", "N/D");
        }

        this.getParameters().put("dni_representante_arrendador", representanteArrendador.getNifDni());
        this.getParameters().put("razon_social_sociedad_arrendadora", arrendador.getNombreCompleto());
        this.getParameters().put("cif_arrendadora", arrendador.getNifDni());
        //inclusion de cotitulares
        this.getParameters().put("nombres_y_dnis_todos_arrendataria", getInformacionNombresDnisArrendataria());

        this.getParameters().put("domicilio_arrendataria", domicilioInquilino.getDireccionParaDocumento());
        this.getParameters().put("precio_arrendamiento", rentaContractual);
        this.getParameters().put("vivienda", departamentoVivienda.getPiso() + " " + departamentoVivienda.getPuerta());
        this.getParameters().put("calle", dirVivienda.getNombreVia() + " " + dirVivienda.getNumeroVia() + " " + dirVivienda.getEscalera());
        this.getParameters().put("ciudad", dirVivienda.getPoblacion());
        this.getParameters().put("ref_catastral", departamentoVivienda.getReferenciaCatastralEfectiva());
        this.getParameters().put("cp", dirVivienda.getCodigoPostal());
        this.getParameters().put("provincia", dirVivienda.getProvincia());
        this.getParameters().put("no_contrato", this.contratoInquilino.getNumeroContrato());
        this.getParameters().put("texto_parte_arrendadora", textoParteArrendadora);
        NumerosATexto nat = new NumerosATexto();

        this.getParameters().put("plazo_anyos", nat.convertirLetras(this.contratoInquilino.getPlazoAnyos()).toUpperCase());
        this.getParameters().put("plazo_anyos_prorrogable", nat.convertirLetras(this.contratoInquilino.getPlazoAnyosProrrogable()).toUpperCase());



        //Provisional mientras cargamos RecursoEntorno
        if (jrr != null) {
            this.getParameters().put("IMAGEN_MARGEN_IZQUIERDO", jrr);
        }
        if (EstadoContratoInquilinoEnum.compare(this.contratoInquilino.getEstadoContrato(), EstadoContratoInquilinoEnum.AUTORIZADO)>=0){
            this.getParameters().put("ES_BORRADOR",false);
        }else{
            this.getParameters().put("ES_BORRADOR",true);

        }
        String textoCedula = "(N/D)";
        if (cedulaHabitabilidad !=null){
            textoCedula = cedulaHabitabilidad.getNumeroCedula();
        }
        this.getParameters().put("no_cedula_habitabilidad", textoCedula);
        byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, this.getParameters(),sijr1);
        return bytearr;


    }

    private String getInformacionNombresDnisArrendataria(){
        Persona inquilino = this.contratoInquilino.getInquilino();
        String res = inquilino.getNombreCompleto()+ " " + inquilino.getNifDni();
        List<CotitularContratoInquilino> cotitulares = this.contratoInquilino.getCotitulares();
        for (int i = 0; i < cotitulares.size(); i++) {
            CotitularContratoInquilino get = cotitulares.get(i);
            res += "/ " + get.getCotitular().getNombreCompleto() + " " + get.getCotitular().getNifDni();
        }
        return res;
    }

    private String getTextoParteArrendadora(
            String parteArrendadora,
            String arrendadorRepresentadoPor,
            String domicilioArrendador,
            String dniRepresentanteArrendador,
            String razonSocialSociedadArrendadora,
            String cifParteArrendadora
    ){

        Persona p = contratoInquilino.getDepartamento().getPropietarioEfectivo().getPersona();
        Persona pentorno = p;
        String texto = "";
        if (p.getId().compareTo(pentorno.getId())==0){

            if (p instanceof PersonaFisica){
                texto = "<b><i>" + parteArrendadora + "</i></b>  con DNI <b><i>" + cifParteArrendadora + "</i></b> y domicilio en  " + domicilioArrendador + " de Barcelona, provincia de Barcelona, representado por <b><i>" + arrendadorRepresentadoPor + "</i></b> con DNI <b><i>" + dniRepresentanteArrendador + "</i></b> según apoderamiento adjunto al presente contrato";

            }
            if (p instanceof PersonaJuridica){
                texto = "<b><i>" + parteArrendadora + "</i></b> representado por <b><i>" + arrendadorRepresentadoPor + "</i></b> con domicilio en  " + domicilioArrendador + " de Barcelona, provincia de Barcelona con D.N.I. <b><i>" + dniRepresentanteArrendador + "</i></b> expedido en Barcelona en su condición de ADMINISTRADOR de la sociedad <b><i>" + razonSocialSociedadArrendadora + "</i></b> con C.I.F. <b><i>" + cifParteArrendadora + "</i></b>";
            }
        }else{

            if (p instanceof PersonaFisica){
                texto = "<b><i>" + parteArrendadora + "</i></b>  con DNI <b><i>" + cifParteArrendadora + "</i></b> y domicilio en  " + domicilioArrendador + " de Barcelona, provincia de Barcelona, representado por <b><i>" + arrendadorRepresentadoPor + "</i></b> con DNI <b><i>" + dniRepresentanteArrendador + "</i></b> expedido en Barcelona en su condición de ADMINISTRADOR de la sociedad " + pentorno.getNombreCompleto() + " con C.I.F. " + pentorno.getNifDni() + " que a su vez es ADMINISTRADORA de la vivienda arrendada";

            }
            if (p instanceof PersonaJuridica){
                texto = "<b><i>" + parteArrendadora + "</i></b>  con CIF <b><i>" + cifParteArrendadora + "</i></b> y domicilio en  " + domicilioArrendador + " de Barcelona, provincia de Barcelona, representado por <b><i>" + arrendadorRepresentadoPor + "</i></b> con DNI <b><i>" + dniRepresentanteArrendador + "</i></b> expedido en Barcelona en su condición de ADMINISTRADOR de la sociedad " + pentorno.getNombreCompleto() + " con C.I.F. " + pentorno.getNifDni() + " que a su vez es ADMINISTRADORA de la vivienda arrendada";
            }
        }
        return texto;
    }

    private Integer calculaEdadRepresentante(){
        PersonaFisica pf = (PersonaFisica) this.contratoInquilino.getRepresentanteArrendador();

        if (pf.getFechaNacimiento()==null){
            return 0;
        }

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


}