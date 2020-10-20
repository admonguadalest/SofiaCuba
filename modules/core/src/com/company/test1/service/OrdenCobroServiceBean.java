package com.company.test1.service;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.Persona;
import com.company.test1.entity.PersonaFisica;
import com.company.test1.entity.PersonaJuridica;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.enums.recibos.ReciboCobradoModoIngreso;
import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.company.test1.entity.ordenescobro.RealizacionCobro;
import com.company.test1.entity.ordenespago.OrdenPago;
import com.company.test1.entity.ordenespago.RealizacionPago;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.ReciboCobrado;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import sepamessaging.BasicDebitInitiationMessage;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(OrdenCobroService.NAME)
public class OrdenCobroServiceBean implements OrdenCobroService {

    @Inject
    NumberUtilsService numberUtilsService;
    @Inject
    Persistence persistence;
    @Inject
    DataManager dataManager;

    public String generaEndToEndIdParaOrdenCobro(OrdenCobro oc, ContratoInquilino ci){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        return ci.getNumeroContrato()+sdf.format(new Date());
    }

    public List<OrdenCobro> generaOrdenesCobroParaRecibos(List<Recibo> rbos, Date fechaAdeudo){
        List<Recibo> rr = rbos;
        ArrayList<OrdenCobro> al = new ArrayList<OrdenCobro>();
        for (int i = 0; i < rr.size(); i++) {
            Recibo recibo =  rr.get(i);
            OrdenCobro oc = new OrdenCobro();
            oc.setRecibo(recibo);
            oc.setFechaValor(fechaAdeudo);
            oc.setImporte(recibo.getTotalReciboPostCCAA());
            oc.setEntToEntId(generaEndToEndIdParaOrdenCobro(oc, recibo.getUtilitarioContratoInquilino()));
            oc.setDescripcion("Recibo " + recibo.getNumRecibo() + " Contrato " + recibo.getUtilitarioContratoInquilino().getNumeroContrato());
            oc.setRecibo(recibo);
            al.add(oc);
        }
        return al;

    }

    public void guardaRealizacionCobroDeOrdenesCobroRecibo(RealizacionCobro rc){
        Transaction t = persistence.createTransaction();
        List<OrdenCobro> oocc = rc.getOrdenesCobro();
        rc = persistence.getEntityManager().merge(rc);
        for (int i = 0; i < oocc.size(); i++) {
            OrdenCobro ordenCobro =  oocc.get(i);
            ordenCobro.setRealizacionCobro(rc);
            persistence.getEntityManager().merge(ordenCobro);

            //adjuntando los ReciboCobrado
            ReciboCobrado rcb = new ReciboCobrado();
            rcb.setRecibo(ordenCobro.getRecibo());
            rcb.setAmpliacionIngreso("");
            rcb.setCobranzas(0.0);
            rcb.setDescripcion("");
            rcb.setFechaCobro(rc.getFechaValor());
            rcb.setModoIngreso(ReciboCobradoModoIngreso.BANCARIO);
            rcb.setTotalIngreso(ordenCobro.getImporte());
            persistence.getEntityManager().merge(rcb);

        }
        t.commit();
    }

    public RealizacionCobro generaRealizacionCobroParaOrdenes(List<OrdenCobro> oocc, Persona presentador, CuentaBancaria cuentaBancaria) throws Exception{
        return generaRealizacionCobroDeOrdenesCobroRecibo(oocc, presentador, cuentaBancaria);
    }

    private RealizacionCobro generaRealizacionCobroDeOrdenesCobroRecibo(List<OrdenCobro> l, Persona presentador, CuentaBancaria cuentaBancariaReceptora) throws Exception{
        ArrayList al = new ArrayList();
        Date fechaValor = null;

        String abreviacionPropietarioIdentificadorRP = "";
        Persona presentadorExt = null;
        if (presentador instanceof PersonaFisica){
            presentador = dataManager.reload(presentador, "personaFisica-view");
        }else if(presentador instanceof PersonaJuridica){
            presentador = dataManager.reload(presentador, "personaJuridica-view");
        }
        if (presentador.getPropietario()!=null){
            abreviacionPropietarioIdentificadorRP = presentador.getPropietario().getAbreviacionContratos();
        }else{
            abreviacionPropietarioIdentificadorRP = "NOPROP";
        }

        List<OrdenCobro> lInstaciados = new ArrayList<OrdenCobro>();

        for (int i = 0; i < l.size(); i++) {
            OrdenCobro ordenCobro = l.get(i);

            if (ordenCobro.getRealizacionCobro()!=null){
                throw new Exception("Una o varias ordenes seleccionadas ya tienen un RealizacionCobro asociado");
            }

            lInstaciados.add(ordenCobro);
            if (fechaValor==null){
                fechaValor = ordenCobro.getFechaValor();
                fechaValor = DateUtils.truncate(fechaValor, Calendar.HOUR_OF_DAY);
            }else{
                Date fecha = ordenCobro.getFechaValor();
                fecha = DateUtils.truncate(fecha, Calendar.HOUR_OF_DAY);
                if (!DateUtils.isSameDay(fechaValor, fecha)){
                    throw new Exception("Las órdenes de cobro seleccionadas deben tener la misma fecha de valor");
                }
            }
        }


        RealizacionCobro rc = new RealizacionCobro();
        rc.setCuentaBancaria(cuentaBancariaReceptora);

        rc.setFechaValor(fechaValor);
        String fechastr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        rc.setIdentificador("RCENT0000" + abreviacionPropietarioIdentificadorRP + " " + fechastr);
        rc.setImporteTotal(0.0);
        for (int i = 0; i < lInstaciados.size(); i++) {
            OrdenCobro ocr = lInstaciados.get(i);
            ocr.setEntToEntId(new SimpleDateFormat("yyyyMMddHHmmss").format(rc.getFechaValor()) + " " + new Integer(i+1).toString());
            double importe = numberUtilsService.roundTo2Decimals(ocr.getImporte());
            rc.setImporteTotal(rc.getImporteTotal() + importe);
            rc.getOrdenesCobro().add(ocr);
        }
        byte[] bb = generaMensajeSepa(rc, presentador, cuentaBancariaReceptora);
        String mensajeSepa = new String(bb, StandardCharsets.UTF_8);
        rc.setSepa(mensajeSepa);
        return rc;
    }

    private byte[] generaMensajeSepa(RealizacionCobro rc, Persona presentador, CuentaBancaria cuentaBancariaReceptora) throws Exception{
        BasicDebitInitiationMessage bdim = new BasicDebitInitiationMessage();

        bdim.setControlSum(rc.getImporteTotal());
        bdim.setCreationDateTime(new Date());
        bdim.setNumberOfTransactions(rc.getOrdenesCobro().size());

        bdim.setRequesterSepaIdentificator(sepamessaging.Utils.calculaIdentificadorSepaParaIdentificador("ES", presentador.getNifDni()));
        bdim.setMessageId(rc.getIdentificador());

        List<OrdenCobro> oocc = rc.getOrdenesCobro();
        Hashtable<Persona, List<OrdenCobro>> htCreditores = new Hashtable<Persona, List<OrdenCobro>>();

        for (int i = 0; i < oocc.size(); i++) {
            OrdenCobro oc = oocc.get(i);



            Persona p = null;
//            if (oc instanceof OrdenCobroRecibo){
//                p = ((OrdenCobroRecibo) oc).getRecibo().getUtilitarioContratoInquilino().getDepartamento().getPropietarioEfectivo().getPersona();
//            }
//            if (oc instanceof OrdenCobroLiquidacion){
//                p = ((OrdenCobroLiquidacion) oc).getLiquidacion().getPropietario().getPersona();
//            }
            p = oc.getRecibo().getUtilitarioContratoInquilino().getDepartamento().getPropietarioEfectivo().getPersona();
            List<OrdenCobro> l = htCreditores.get(p);
            if (l==null){
                l = new ArrayList<OrdenCobro>();
                htCreditores.put(p, l);
            }
            l.add(oc);
        }

        List<Persona> pp = new ArrayList<Persona>(htCreditores.keySet());
        for (int i = 0; i < pp.size(); i++) {
            Persona persona = pp.get(i);
            List<OrdenCobro> oocc_ = htCreditores.get(persona);
            BasicDebitInitiationMessage.PaymentInstructionInfo pii = new BasicDebitInitiationMessage().new PaymentInstructionInfo();

            CuentaBancaria cba = cuentaBancariaReceptora;
            pii.setControlSum(0.0);
            pii.setCreditorBicCode(cba.getCodigoBIC());
            pii.setCreditorIban(cba.getVersionIBAN());
            pii.setCreditorName(calculaStringCaracteresPermitidos(presentador.getNombreCompleto()));

            pii.setCurrency("EUR");
            pii.setNumberOfTransactions(oocc_.size());
            pii.setPaymentInformationIdentification(rc.getIdentificador());
            pii.setPaymentMethod("DD");
            pii.setRequestedCollectionDate(rc.getFechaValor());

            double total = 0.0;

            for (int j = 0; j < oocc_.size(); j++) {
                OrdenCobro oc = oocc_.get(j);
                BasicDebitInitiationMessage.SinglePaymentInfo spi = new BasicDebitInitiationMessage().new SinglePaymentInfo();
                pii.getSinglePayments().add(spi);

                spi.setAmount(oc.getImporte());
                spi.setCurrency("EUR");
                CuentaBancaria cb = getCuentaBancariaDebitora(oc);
                if (cb == null){
                    throw new Exception("La cuenta debitora es nula: " + oc.getRecibo().getUtilitarioContratoInquilino().getInquilino().getNombreCompleto());
                }
                spi.setDebitorBicCode(cb.getCodigoBIC());
                spi.setDebitorIban(cb.getVersionIBAN());
                spi.setDebitorName(calculaStringCaracteresPermitidos(getPersonaDebitora(oc).getNombreCompleto()));

                spi.setEntToEntId(oc.getEntToEntId());
                spi.setInformacionAcreedor(calculaStringCaracteresPermitidos(getPersonaDebitora(oc).getNombreCompleto()));

                Date dateOfSignature = null;
                String mandateIdentification = "";
//                if (oc instanceof OrdenCobroRecibo){
//                    dateOfSignature = ((OrdenCobroRecibo) oc).getRecibo().getUtilitarioContratoInquilino().getFechaRealizacion();
//                    mandateIdentification = ((OrdenCobroRecibo) oc).getRecibo().getUtilitarioContratoInquilino().getReferenciaMandato();
//                }
                dateOfSignature = oc.getRecibo().getUtilitarioContratoInquilino().getFechaRealizacion();
                mandateIdentification = oc.getRecibo().getUtilitarioContratoInquilino().getReferenciaMandato();
//                if (oc instanceof OrdenCobroLiquidacion){
//                    Liquidacion l = ((OrdenCobroLiquidacion) oc).getLiquidacion();
//                    //debido a la restriccion que la liquidacion no puede estar vacia de registros, y todos los
//                    //registros deben corresponder al mismo contratoPropitario, lo siguiente es válido
//                    ContratoPropietario cp = l.getRegistrosLiquidaciones().get(0).getContratoPropietario();
//                    dateOfSignature = cp.getFechaMandato();
//                    mandateIdentification = cp.getReferenciaMandato();
//                }
//                if (oc instanceof OrdenCobroFacturaPropietario){
//                    dateOfSignature = ((OrdenCobroFacturaPropietario) oc).getFacturaPropietario().
//                    mandateIdentification = ((OrdenCobroRecibo) oc).getRecibo().getProgramacionRecibo().getContratoInquilino().getReferenciaMandato();
//                }

                spi.setDateOfSignature(dateOfSignature);
                spi.setMandateIdentification(mandateIdentification);
                total += spi.getAmount();
                numberUtilsService.roundTo2Decimals(total);
            }
            pii.setControlSum(total);

            bdim.getPaymentInstructions().add(pii);
        }

        return bdim.getXml();
    }

    public CuentaBancaria getCuentaBancariaDebitora(OrdenCobro oc){
        CuentaBancaria cb = null;
//        if (this instanceof OrdenCobroRecibo){
//            cb = ((OrdenCobroRecibo) this).getRecibo().getUtilitarioContratoInquilino().getProgramacionRecibo().getCuentaBancariaPagador();
//
//        }
//        if (this instanceof OrdenCobroLiquidacion){
//            cb = ((OrdenCobroLiquidacion) this).getLiquidacion().getCuentaBancaria();
//        }
        return oc.getRecibo().getUtilitarioContratoInquilino().getProgramacionRecibo().getCuentaBancariaPagador();


    }

    private Persona getPersonaDebitora(OrdenCobro oc){
        Persona p = null;
//        if (this instanceof OrdenCobroRecibo){
//            p = ((OrdenCobroRecibo) this).getRecibo().getUtilitarioContratoInquilino().getInquilino();
//        }
//        if (this instanceof OrdenCobroLiquidacion){
//            p = ((OrdenCobroLiquidacion) this).getLiquidacion().getPropietario().getPersona();
//        }
        return oc.getRecibo().getUtilitarioContratoInquilino().getInquilino();


    }

    private String calculaStringCaracteresPermitidos(String s){
        StringBuffer sb = new StringBuffer();

        String noPermitidos = "?/-?:().,'+";
        for(int i=0;i < s.length();i++){
            char c = s.charAt(i);

            if (noPermitidos.indexOf(new String(new char[]{c}))==-1){

                c = devuelveCaracterValidoEquivalente(c);
                sb.append(c);
            }
        }
        return sb.substring(0);
    }

    private char devuelveCaracterValidoEquivalente(char c){

        String caracteresInvalidos = "ÑºªÀÁÈÉÌÍÍÒÓÙÚÜ`´";
        String caracteresEquivalentes = "NoaAAEEIIIOOUUU''";

        for (int i = 0; i < caracteresInvalidos.length(); i++) {
            char caracter = caracteresInvalidos.charAt(i);
            if (c==caracter){
                return caracteresEquivalentes.charAt(i);
            }

        }

        return c;
    }

}