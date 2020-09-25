package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.modeloscontratosinquilinos.Clausula;
import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.entity.modeloscontratosinquilinos.Seccion;
import com.company.test1.entity.modeloscontratosinquilinos.VersionClausula;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Table(name = "IMPLEMENTACION_MODELO")
@Entity(name = "test1_ImplementacionModelo")
public class ImplementacionModelo extends StandardEntity {
    private static final long serialVersionUID = -1157975445418084489L;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "implementacionModelo")
    protected ContratoInquilino contratoInquilino;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "implementacionModelo", orphanRemoval = true)
    protected List<ParametroValor> parametrosValores = new ArrayList<ParametroValor>();

    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODELO_CONTRATO_ID")
    protected ModeloContrato modeloContrato;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "implementacionModelo", orphanRemoval = true)
    protected List<OverrideClausula> overrideClausulas = new ArrayList<OverrideClausula>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "implementacionModelo")
    protected List<SeccionDescartada> seccionesDescartadas = new ArrayList<SeccionDescartada>();

    @Column(name = "RMI2")
    protected Integer rmi2;

    public Integer getRmi2() {
        return rmi2;
    }

    public void setRmi2(Integer rmi2) {
        this.rmi2 = rmi2;
    }

    public List<SeccionDescartada> getSeccionesDescartadas() {
        return seccionesDescartadas;
    }

    public void setSeccionesDescartadas(List<SeccionDescartada> seccionesDescartadas) {
        this.seccionesDescartadas = seccionesDescartadas;
    }

    public List<OverrideClausula> getOverrideClausulas() {
        return overrideClausulas;
    }

    public void setOverrideClausulas(List<OverrideClausula> overrideClausulas) {
        this.overrideClausulas = overrideClausulas;
    }

    public List<ParametroValor> getParametrosValores() {
        return parametrosValores;
    }

    public void setParametrosValores(List<ParametroValor> parametrosValores) {
        this.parametrosValores = parametrosValores;
    }

    public ModeloContrato getModeloContrato() {
        return modeloContrato;
    }

    public void setModeloContrato(ModeloContrato modeloContrato) {
        this.modeloContrato = modeloContrato;
    }

    public ContratoInquilino getContratoInquilino() {
        return contratoInquilino;
    }

    public void setContratoInquilino(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }

    public static void inicializaParametrosValores(ImplementacionModelo im) throws Exception{
        ParametroValor[] ppvv = im.getParametrosValores().toArray(new ParametroValor[0]);
        List<ParametroValor> ppvv_old = Arrays.asList(ppvv);
        im.parametrosValores.clear();
//        HelperContrato helperContrato = im.createHelperContrato(im.getContratoInquilino());
        if (im.getModeloContrato()!=null){
            ModeloContrato mc = im.getModeloContrato();
            List secciones = mc.getSecciones();
            if ((secciones!=null) && (secciones.size()>0)){
                for (int i = 0; i < secciones.size(); i++) {
                    Seccion seccion = (Seccion) secciones.get(i);
                    if ((seccion.getClausulas()!=null))
                        for (int j = 0; j < seccion.getClausulas().size(); j++) {
                            Clausula clausula = seccion.getClausulas().get(j);
                            //VersionClausula vc = clausula.getVersionPredeterminada();
                            VersionClausula vc = getVersionClausulaAplicada(im, clausula);
                            List listaParametros = VersionClausula.extraeListaParametros(vc);
                            for (int k = 0; k < listaParametros.size(); k++) {
                                String np = (String) listaParametros.get(k);
                                ParametroValor pv = new ParametroValor();
                                pv.setNombreParametro(np);
                                //si existe expresion defecto para este nombre lo asigno
                                String evd = "";
                                try{
                                    evd = getExpresionValorDefecto(np, vc);
                                }catch(Exception exc){
                                    // se considera un error menor, no le prestamos atencion
                                    // o mas adelante ya decidiremos la accion a implementar
                                }


                                try {
                                    String s = (String) resuelveParametro(im, evd);
                                    pv.setValor(s);

                                } catch (Exception ex) {
                                    int x = 2;
                                }




                            /*if (evd.trim().length()>0){
                                if (evd.indexOf("@")!=-1){
                                    Object base = null;
                                    String strBase;
                                    if (evd.indexOf(".")!=-1){
                                        strBase = evd.substring(0,evd.indexOf("."));
                                    }else{
                                        strBase = evd;
                                    }

                                    if (strBase.indexOf("@contrato")!=-1){
                                        base = im.getContratoInquilino();
                                    }else if(strBase.indexOf("@helperContrato")!=-1){
                                        base = helperContrato;
                                    }
                                    String val;
                                    String expresion = evd.substring(evd.indexOf(".") + ".".length());
                                    try {
                                        Object o = MyBeanUtils.readBeanPath(base, expresion);
                                        val = o.toString();
                                    } catch (Exception e) {
                                        val = "";
                                    }
                                    pv.setValor(val);
                                }else{
                                    pv.setValor(evd);
                                }
                            }*/
                                pv.setImplementacionModelo(im);
                                im.parametrosValores.add(pv);
                            }
                        }
                }
            }
        }else{
            im.setParametrosValores(new ArrayList<ParametroValor>());
        }
        ImplementacionModelo.pueblaValoresParametrosValoresDesdeLista(im, ppvv_old);

    }

    public static VersionClausula getVersionClausulaAplicada(ImplementacionModelo im, Clausula c) throws Exception{
        for (int i = 0; i < im.overrideClausulas.size(); i++) {
            //ArrayList al = (ArrayList) this.overrideClausulas.get(i);
            OverrideClausula oc = im.overrideClausulas.get(i);
            Clausula c_ = oc.getClausula();
            if (c.getId().compareTo(c_.getId())==0){
                VersionClausula vc = oc.getVersionAplicada();
                return vc;
            }

        }
        return Clausula.getVersionPredeterminada(c);
    }

    private static String getExpresionValorDefecto(String nombreParametro, VersionClausula vc){
        int i = -1;
        String[] nnpp = StringUtils.splitByWholeSeparatorPreserveAllTokens(vc.getNombresParametros(), ";");
        for (int j = 0; j < nnpp.length; j++) {
            String string = nnpp[j];
            if (string.compareTo(nombreParametro)==0){
                i = j;
                break;
            }
        }

        //String[] eevvdd = StringUtils.split(vc.getExpresionesValoresDefecto(), ';');

        String[] eevvdd = StringUtils.splitByWholeSeparatorPreserveAllTokens(vc.getExpresionesValoresDefecto(), ";");
        if (eevvdd.length==0){
            eevvdd = new String[]{""};
        }


        //String[] eevvdd = vc.getExpresionesValoresDefecto().split("\\;");
        if (i!=-1){
            return eevvdd[i];
        }else{
            return "";
        }
    }

    public static void pueblaValoresParametrosValoresDesdeLista(ImplementacionModelo im, List parametrosValoresAntiguos){
        for (int i = 0; i < im.parametrosValores.size(); i++) {
            ParametroValor pv = im.parametrosValores.get(i);
            for (int j = 0; j < parametrosValoresAntiguos.size(); j++) {
                ParametroValor pva = (ParametroValor) parametrosValoresAntiguos.get(j);
                if (pv.nombreParametro.compareTo(pva.nombreParametro)==0){
                    if ((pva.getValor()!=null) && (pva.getValor().trim().length()>0)){
                        pv.valor = pva.valor;
                    }
                }
            }
        }
    }

    public static Object resuelveParametro(ImplementacionModelo im, String expresion) throws Exception{

        Binding b = new Binding();

//        if (im.getContratoPropietario() != null){
//            ContratoPropietario contratoPropietario = im.getContratoPropietario();
//            Entorno entorno = contratoPropietario.getEntorno();
//            b.setVariable("cp", contratoPropietario);
//            b.setVariable("e", entorno);
//
//        }else
        if (im.getContratoInquilino() != null){
            ContratoInquilino contratoInquilino = im.getContratoInquilino();
            b.setVariable("ci", contratoInquilino);
        }

        GroovyShell shell = new GroovyShell(b);
        Object res = shell.evaluate("return " + expresion);

        return res;
    }



}