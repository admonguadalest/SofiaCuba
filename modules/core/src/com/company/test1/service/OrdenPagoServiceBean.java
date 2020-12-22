package com.company.test1.service;

import com.company.test1.StringUtils;
import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.Persona;
import com.company.test1.entity.PersonaFisica;
import com.company.test1.entity.PersonaJuridica;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.entity.ordenespago.*;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;
import sepamessaging.PaymentInitiationMessage;

import javax.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service(OrdenPagoService.NAME)
public class OrdenPagoServiceBean implements OrdenPagoService {

    @Inject
    Persistence persistence;
    @Inject
    NumberUtilsService numberUtilsService;
    @Inject
    private DataManager dataManager;

//    public String getNombreEmisor(OrdenPago op){
//        try{
//            if (op instanceof OrdenPagoContratoInquilino){
//
//                Transaction t = persistence.createTransaction();
//                OrdenPagoContratoInquilino opci = (OrdenPagoContratoInquilino) persistence.getEntityManager().reload(op, "ordenPagoContratoInquilino-view");
//                ContratoInquilino ci = persistence.getEntityManager().reload(opci.getContratoInquilino(), "contratoInquilino-view");
//                t.close();
//                return ci.getDepartamento().getPropietarioEfectivo().getPersona().getNombreCompleto();
//            }
//            if (op instanceof OrdenPagoFacturaProveedor){
//                Transaction t = persistence.createTransaction();
//                OrdenPagoFacturaProveedor opfp = (OrdenPagoFacturaProveedor) persistence.getEntityManager().reload(op, "ordenPagoFacturaProveedor-view");
//                FacturaProveedor fp = persistence.getEntityManager().reload(opfp.getFacturaProveedor(), "facturaProveedor-view");
//                t.close();
//                return fp.getImputacionesDocumentoImputable().get(0).getCiclo().getDepartamento().getPropietarioEfectivo().getPersona().getNombreCompleto();
//            }
//            if (op instanceof OrdenPagoProveedor){
//                return "N/D";
//            }
//            return "N/D";
//        }catch(Exception exc){
//            return "N/D";
//        }
//
//
//    }

    @Override
    public List<OrdenPago> devuelveOrdenesPagoPendientesDeCompensacion(Proveedor prov) {
        String provId = prov.getId().toString().replace("-", "");
        String nativeSql = "SELECT OP.ID, OP.IMPORTE, coalesce(sum(COPP.importe), 0) as SUM FROM cubatest1.ORDEN_PAGO OP \n" +
                "INNER join cubatest1.realizacion_pago RP on OP.REALIZACION_PAGO_ID = RP.id \n" +
                "LEFT join cubatest1.COMP_OP_PROVEEDOR COPP on COPP.OP_PROVEEDOR_ID = OP.ID\t\n" +
                "WHERE OP.PROVEEDOR_ID = '" + provId + "' OR OP.PROVEEDOR_AB_ID = '" + provId + "' group by OP.ID, OP.IMPORTE";
        ArrayList<String> ids = new ArrayList<String>();
        Transaction t = persistence.createTransaction();
        List<Object[]> results = persistence.getEntityManager().createNativeQuery(nativeSql).getResultList();

        for (int i = 0; i < results.size(); i++) {
            Object[] objects =  results.get(i);
            Double importe = (Double) objects[1];
            Double sum = (Double) objects[2];
            if ((importe - sum)>0.00001){
                ids.add((String) objects[0]);
            }
        }
        ArrayList<OrdenPago> ordenes = new ArrayList<OrdenPago>();
        for (int i = 0; i < ids.size(); i++) {
            String id =  ids.get(i);
            OrdenPago op = persistence.getEntityManager().find(OrdenPago.class, StringUtils.toUUID(id), "ordenPago-view");
            ordenes.add(op);
        }
        t.close();
        return ordenes;

    }

    public Double getTotalImporteCompensadoDeOrdenPago(OrdenPago op){
        Double d = 0.0;
        String hql = "select cop from test1_CompensacionOrdenPagoProveedor cop WHERE cop.ordenPagoProveedor.id = :oppid or cop.ordenPagoAbono.id = :opaid";
        Transaction t = persistence.createTransaction();
        List<CompensacionOrdenPagoProveedor> cops = persistence.getEntityManager().createQuery(hql).setParameter("oppid", op.getId()).setParameter("opaid", op.getId()).getResultList();
        t.close();
        for (int i = 0; i < cops.size(); i++) {
            CompensacionOrdenPagoProveedor compensacionOrdenPagoProveedor =  cops.get(i);
            d += compensacionOrdenPagoProveedor.getImporte();
        }
        return d;
    }

    @Override
    public void guardaOrdenPagoFacturaProveedor(OrdenPagoFacturaProveedor opfp) {
        Transaction t = persistence.createTransaction();
        for (int i = 0; i < opfp.getCompensaciones().size(); i++) {
            CompensacionOrdenPagoProveedor copp = opfp.getCompensaciones().get(i);
            persistence.getEntityManager().merge(copp);
        }
        persistence.getEntityManager().merge(opfp);
        t.commit();
    }

    @Override
    public List<CuentaBancaria> devuelveCuentasBancariasPropietariosRegistrados() {
        Transaction t = persistence.createTransaction();
        List<CuentaBancaria> ccbb = persistence.getEntityManager().createQuery("select cb FROM test1_CuentaBancaria cb JOIN cb.persona p JOIN p.propietario prop").getResultList();

        ArrayList<CuentaBancaria> cbr = new ArrayList<CuentaBancaria>();
        for (int i = 0; i < ccbb.size(); i++) {
            CuentaBancaria cuentaBancaria =  ccbb.get(i);
            cuentaBancaria = persistence.getEntityManager().reload(cuentaBancaria, "cuentaBancaria-view");
            cbr.add(cuentaBancaria);
        }

        t.close();
        return cbr;
    }

    @Override
    public RealizacionPago crearRealizacionPagoDesdeListaOrdenesPago(List<OrdenPago> oopp, CuentaBancaria cb) throws Exception{
        Double importe = 0.0;
        RealizacionPago rp = new RealizacionPago();
//        List<Persona> pp = new ArrayList<Persona>();
//        for (int i = 0; i < oopp.size(); i++) {
//            OrdenPago op = oopp.get(i);
//            if (op instanceof OrdenPagoFacturaProveedor){
//                op = dataManager.reload(op, "ordenPagoFacturaProveedor-view");
//            }
//            if (op instanceof OrdenPagoProveedor){
//                op = dataManager.reload(op, "ordenPagoProveedor-view");
//            }
//            if (op instanceof OrdenPagoContratoInquilino){
//                op = dataManager.reload(op, "ordenPagoContratoInquilino-view");
//            }
//            Persona p = op.getCuentaBancariaOrdenPago().getPersona();
//            if (p instanceof PersonaFisica){
//                p = dataManager.reload(p, "personaFisica-view");
//            }else if (p instanceof PersonaJuridica){
//                p = dataManager.reload(p, "personaJuridica-view");
//            }
//
//
//            if (pp.indexOf(p)==-1){
//                pp.add(p);
//            }
//        }
//        if (pp.size()!=1){
//            throw new Exception("No se pueden lanzar Ordenes de Pago en un Realizacion Pago con más de un emisor");
//        }
//        Persona p = pp.get(0);

        String abrevEmisor = cb.getPersona().getPropietario().getAbreviacionContratos();

        rp.setIdentificador(abrevEmisor + crearIdentificadorParaRealizacionPago(new Date()));

        for (int i = 0; i < oopp.size(); i++) {
            OrdenPago ordenPago =  oopp.get(i);
            ordenPago.setRealizacionPago(rp);
            rp.getOrdenesPago().add(ordenPago);
            importe += ordenPago.getImporteEfectivo();
        }
        rp.setFechaValor(new Date());

        byte[] bb = crearTransferencia(rp, cb);
        String sepa = new String(bb, StandardCharsets.UTF_8);
        rp.setSepa(sepa);

        rp.setCuentaBancaria(cb);
        rp.setInfoCuentaEmisora(cb.getEntidad());
        rp.setFechaValor(new Date());
        rp.setImporteTotal(importe);


        return rp;

    }

    public void guardaRealizacionPago(RealizacionPago rp){
        Transaction t = persistence.createTransaction();
        List<OrdenPago> oopp = rp.getOrdenesPago();
        rp = persistence.getEntityManager().merge(rp);
        for (int i = 0; i < oopp.size(); i++) {
            OrdenPago ordenPago =  oopp.get(i);
            ordenPago.setRealizacionPago(rp);
            persistence.getEntityManager().merge(ordenPago);
        }
        t.commit();
    }

    @Override
    public String crearIdentificadorParaRealizacionPago(Date fecha) {
        return "RP" + new SimpleDateFormat("yyyyMMddHHmmss").format(fecha);
    }

    private byte[] crearTransferencia(RealizacionPago rp, CuentaBancaria cuentaEmisora) throws Exception{

        if (cuentaEmisora==null){
            throw new Exception("La cuenta emisora no puede ser nula", new Throwable("cuentaEmisora is null"));

        }

        Persona personapropietarioEntorno = cuentaEmisora.getPersona();
        Propietario prop = personapropietarioEntorno.getPropietario();

        PaymentInitiationMessage pim = new PaymentInitiationMessage();
        pim.setCreationDateTime(new Date());
        CuentaBancaria cb = cuentaEmisora;

        String infoCE = cb.getNombreEntidadBancaria();
        if ((infoCE==null)||(infoCE.trim().length()==0)){
            infoCE = cb.getCodigoBIC();
        }
        rp.setInfoCuentaEmisora(infoCE);

        pim.setDebitorBicCode(cb.getCodigoBIC());
        pim.setDebitorIban(cb.getVersionIBAN());
        pim.setDebitorName(calculaStringCaracteresPermitidos(personapropietarioEntorno.getNombreCompleto()));
        pim.setMessageId(calculaStringCaracteresPermitidos(prop.getAbreviacionContratos()) + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(rp.getFechaValor()));
        pim.setPaymentInformationId(calculaStringCaracteresPermitidos(prop.getAbreviacionContratos()) + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(rp.getFechaValor()));
        pim.setRequestedExecutionDate(rp.getFechaValor());
        pim.setRequesterName(calculaStringCaracteresPermitidos(personapropietarioEntorno.getNombreCompleto()));

        List<PaymentInitiationMessage.SinglePaymentInfo> pp = new ArrayList<PaymentInitiationMessage.SinglePaymentInfo>();

        List<OrdenPago> oopp = rp.getOrdenesPago();

        for (int i = 0; i < oopp.size(); i++) {
            OrdenPago op = oopp.get(i);
            String view = "";
            if (op instanceof OrdenPagoProveedor){
                view = "ordenPagoProveedor-view";
            }
            if (op instanceof OrdenPagoFacturaProveedor){
                view = "ordenPagoFacturaProveedor-view";
            }
            if (op instanceof OrdenPagoContratoInquilino){
                view = "ordenPagoContratoInquilino-view";
            }
            Transaction t = persistence.createTransaction();
            op = persistence.getEntityManager().reload(op, view);
            t.close();

            PaymentInitiationMessage.SinglePaymentInfo spi1 = pim.createSinglePaymentInfo();

            Double d = numberUtilsService.roundTo2Decimals(op.getImporteEfectivo());
            spi1.setAmount(d);

            Persona receptoraOrdenPago = getPersonaReceptoraOrdenPago(op);

            if (receptoraOrdenPago == null){
                throw new Exception("La Orden de Pago " + op.getId() + " no tiene Persona Receptora de Pago");
            }

            CuentaBancaria cuentaBancariaOrdenPago = getCuentaBancariaOrdenPago(op);

            if (cuentaBancariaOrdenPago == null){
                throw new Exception("La Orden de Pago " + op.getId() + " no tiene Cuenta Bancaria asociada");
            }

            spi1.setCreditor(calculaStringCaracteresPermitidos(receptoraOrdenPago.getNombreCompleto()));
            spi1.setCreditorBicCode(cuentaBancariaOrdenPago.getCodigoBIC());
            spi1.setCurrency("EUR");
            String opid = op.getId().toString().replace("-", "_");
            spi1.setEndToEndId(calculaStringCaracteresPermitidos(prop.getAbreviacionContratos()) + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(rp.getFechaValor()));
            spi1.setIban(cuentaBancariaOrdenPago.getVersionIBAN());

            pp.add(spi1);
        }

        pim.setPayments(pp);

        return pim.getXml();
    }

    private CuentaBancaria getCuentaBancariaOrdenPago(OrdenPago op){

        if ((op instanceof OrdenPagoFacturaProveedor) || (op instanceof OrdenPagoProveedor)) {
            if (op instanceof OrdenPagoFacturaProveedor){
                Proveedor prov = ((OrdenPagoFacturaProveedor)op).getFacturaProveedor().getProveedor();
                Transaction t = persistence.createTransaction();
                prov = persistence.getEntityManager().reload(prov, "proveedor-view");
                t.close();
                return prov.getCuentaBancaria();

            }
            if (op instanceof OrdenPagoProveedor){
                Proveedor prov = ((OrdenPagoProveedor)op).getProveedor();
                Transaction t = persistence.createTransaction();
                prov = persistence.getEntityManager().reload(prov, "proveedor-view");
                t.close();
                return prov.getCuentaBancaria();
            }

        }else if (op instanceof OrdenPagoContratoInquilino){
            OrdenPagoContratoInquilino opci = (OrdenPagoContratoInquilino) op;
            return opci.getContratoInquilino().getProgramacionRecibo().getCuentaBancariaPagador();
        }

//        } else if (this instanceof OrdenPagoContratoInquilino) {
//            OrdenPagoContratoInquilino opci = (OrdenPagoContratoInquilino) this;
//            return opci.getContratoInquilino().getProgramacionRecibo().getCuentaBancariaPagador();
//
//        } else if (this instanceof OrdenPagoContratoPropietario) {
//            OrdenPagoContratoPropietario opcp = (OrdenPagoContratoPropietario) this;
//            return opcp.getContratoPropietario().getCuentaBancariaCobrador();
//        }else if (this instanceof OrdenPagoLiquidacion) {
//            OrdenPagoLiquidacion opl = (OrdenPagoLiquidacion) this;
//            return opl.getLiquidacion().getCuentaBancaria();
//        }

        return null;
    }

    public Persona getPersonaReceptoraOrdenPago(OrdenPago op){

        if ((op instanceof OrdenPagoFacturaProveedor) || (op instanceof OrdenPagoProveedor)) {
            if (op instanceof OrdenPagoFacturaProveedor) {
                return ((OrdenPagoFacturaProveedor) op).getFacturaProveedor().getProveedor().getPersona();
            }
            if (op instanceof OrdenPagoProveedor) {
                return ((OrdenPagoProveedor) op).getProveedor().getPersona();
            }
        }else if (op instanceof OrdenPagoContratoInquilino){
            OrdenPagoContratoInquilino opci = (OrdenPagoContratoInquilino) op;
            return opci.getContratoInquilino().getInquilino();
        }

//        }else if (op instanceof OrdenPagoContratoInquilino){
//            OrdenPagoContratoInquilino opci = (OrdenPagoContratoInquilino) this;
//            return opci.getContratoInquilino().getInquilino();
//
//        }else if (op instanceof OrdenPagoContratoPropietario){
//            OrdenPagoContratoPropietario opcp = (OrdenPagoContratoPropietario) this;
//            return opcp.getContratoPropietario().getPropietario().getPersona();
//        }else if (op instanceof OrdenPagoLiquidacion){
//            OrdenPagoLiquidacion opl = (OrdenPagoLiquidacion) this;
//            return opl.getLiquidacion().getRegistrosLiquidaciones().get(0).getContratoPropietario().getPropietario().getPersona();
//        }

        return null;
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

    public OrdenPagoFacturaProveedor devuelveOrdenPagoFacturaProveedor(FacturaProveedor fp) {
        try {
            String hql = "SELECT opfp FROM test1_OrdenPagoFacturaProveedor opfp JOIN opfp.facturaProveedor fp WHERE fp.id = :fpid";
            Transaction t = persistence.createTransaction();
            OrdenPagoFacturaProveedor opfp = (OrdenPagoFacturaProveedor) persistence.getEntityManager().createQuery(hql).setParameter("fpid", fp.getId()).getSingleResult();
            t.close();
            return opfp;
        }catch(Exception exc){
            return null;
        }
    }

    public OrdenPagoAbono devuelveOrdenPagoAbono(FacturaProveedor fp) {
        try {
            String hql = "SELECT opa FROM test1_OrdenPagoAbono opa JOIN opa.facturaProveedor fp WHERE fp.id = :fpid";
            Transaction t = persistence.createTransaction();
            OrdenPagoAbono opfp = (OrdenPagoAbono) persistence.getEntityManager().createQuery(hql).setParameter("fpid", fp.getId()).getSingleResult();
            t.close();
            return opfp;
        }catch(Exception exc){
            return null;
        }
    }

}