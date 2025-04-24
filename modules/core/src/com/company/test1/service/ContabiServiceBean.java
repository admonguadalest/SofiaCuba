package com.company.test1.service;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.ImplementacionConcepto;
import com.company.test1.entity.recibos.OrdenanteRemesa;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.Remesa;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.entity.User;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(ContabiService.NAME)
public class ContabiServiceBean implements ContabiService {

    public String authToken = null;
    @Inject
    private DataManager dataManager;
    @Inject
    private ColeccionArchivosAdjuntosService coleccionArchivosAdjuntosService;

    public String getAuthToken(User cubUser, String user, String pwd) throws Exception{

            String url = "http://localhost:8080/oauth/token?grant_type=password&username=" + user + "&password=" + pwd;
            DefaultHttpClient http = new DefaultHttpClient();
            CredentialsProvider cp = new BasicCredentialsProvider();
            cp.setCredentials(new AuthScope("localhost", 8080),
                    new UsernamePasswordCredentials("client", "secret"));
            http.setCredentialsProvider(cp);

            HttpPost postRequest = new HttpPost(url);
            postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse response = http.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output = "";
            String buffer = "";

            while ((buffer = br.readLine()) != null) {
                output += buffer;
            }

            http.getConnectionManager().shutdown();
            JSONObject jo = new JSONObject(output);
            String accessToken = (String) jo.get("access_token");
            return accessToken;

    }

    private String devuelveTextoAmpliacionContabilidad(User cubaUser, FacturaProveedor fp){
        String defecto = fp.getProveedor().getPersona().getNombreCompleto() + " " + fp.getNumDocumento();
        if (cubaUser.getLogin().compareTo("carlosconti")!=0){
            return defecto;
        }
        String nifAigues = "A66098435";
        String nifGanaEnergia = "B98717457";
        String nifTelefonica = "A82018474";
        String nifProveedor = fp.getProveedor().getPersona().getNifDni();
        if ((nifAigues+","+nifTelefonica).indexOf(nifProveedor)!=-1){
            String infoPtoSuministro = fp.getSuministroInfoPuntoSuministro();
            if ((infoPtoSuministro!=null) && (infoPtoSuministro.length()>0)){
                return fp.getProveedor().getPersona().getNombreCompleto() + " " + infoPtoSuministro;
            }else{
                return defecto + " (InfoPtoSuministro No Disponible)";
            }
        }
        if (nifProveedor.compareTo(nifGanaEnergia)==0){
            return defecto;
        }

        return defecto;
    }

    public boolean publicaContabilizacionFacturaProveedor(User cubaUser, FacturaProveedor fp) throws Exception{
        this.authToken = getAuthToken(cubaUser, "admin", "EaGmTfki");

        if (comprobarPublicacionFacturaProveedor(fp, this.authToken)){
            throw new Exception("Esta factura proveedor ya esta publicada para la operacion 'CONTABILIZAR_FACTURAS'");
        }



        fp = dataManager.reload(fp, "facturaProveedor-view");

        JSONObject jo = new JSONObject();
        jo.put("OPERACION", "CONTABILIZAR_FACTURAS");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        jo.put("NIF_RECEPTOR", fp.getTitular().getNifDni());
        jo.put("NIF_EMISOR", fp.getProveedor().getPersona().getNifDni());
        jo.put("NOMBRE_RECEPTOR", fp.getTitular().getNombreCompleto());
        jo.put("NOMBRE_EMISOR", fp.getProveedor().getPersona().getNombreCompleto());
        jo.put("FECHA_FACTURA", sdf.format(fp.getFechaEmision()));
        jo.put("IMPORTE_BASE_FACTURA", fp.getImporteTotalBase());
        jo.put("IMPORTE_TOTAL_FACTURA", fp.getImportePostCCAA());
        jo.put("NUM_FRA", fp.getNumDocumento());
        jo.put("TEXTO_AMPLIACION_CONTABILIDAD", devuelveTextoAmpliacionContabilidad(cubaUser, fp));
        jo.put("REFERENCIAS", fp.getId().toString());
        ArchivoAdjunto aa = fp.getColeccionArchivosAdjuntos().getArchivos().get(0);
//        ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
        jo.put("ARCHIVO_ADJUNTO_EXTDOCS_ID", aa.getExtId().intValue());
        //imputaciones
        List<ImputacionDocumentoImputable> iiddii = fp.getImputacionesDocumentoImputable();
        ArrayList al = new ArrayList();
        for (int i = 0; i < iiddii.size(); i++) {
            ImputacionDocumentoImputable idi = iiddii.get(i);
            Departamento d = idi.getCiclo().getDepartamento();
            Ubicacion u = d.getUbicacion();
            d = dataManager.reload(d, "_base");
            u = dataManager.reload(u, "_base");
            JSONObject joi = new JSONObject();
            joi.put("UBICACION", u.getAbreviacionUbicacion());
            joi.put("DEPARTAMENTO", d.getAbreviacionPisoPuerta());
            joi.put("PORCENTAJE_IMPUTACION", idi.getPorcentajeImputacion());
            joi.put("IMPORTE_IMPUTACION", idi.getImporteImputacion());
            al.add(joi);
        }
        jo.put("imputaciones", new JSONArray(al));
        //conceptos adicionales
        List<RegistroAplicacionConceptoAdicional> iicc = fp.getRegistroAplicacionConceptosAdicionales();
        al = new ArrayList();
        for (int i = 0; i < iicc.size(); i++) {
            RegistroAplicacionConceptoAdicional raca = iicc.get(i);
            JSONObject joca = new JSONObject();
            joca.put("NOMBRE_CONCEPTO", raca.getConceptoAdicional().getAbreviacion());
            joca.put("BASE_CONCEPTO", raca.getBase());
            joca.put("PORCENTAJE_CONCEPTO", raca.getPorcentaje());
            joca.put("IMPORTE_APLICADO", raca.getImporteAplicado());
            al.add(joca);

        }
        jo.put("conceptos_adicionales", new JSONArray(al));
        String json = jo.toString();

        String url = "http://localhost:8080/entities/contabi_PublicacionRemota";

        sdf = new SimpleDateFormat("yyyy-MM-dd");

        JSONObject j = new JSONObject();
        j.put("sistemaRemoto", "SOFIA");
        j.put("fechaPublicacion", sdf.format(new Date()));
        j.put("contenido", json);
        j.put("referenciasExternas", fp.getId().toString());
        j.put("operacion", "CONTABILIZAR_FACTURAS");


        url = "http://localhost:8080/rest/entities/contabi_PublicacionRemota";

        StringEntity requestentity = new StringEntity(j.toString(), "application/json", "UTF-8");

        DefaultHttpClient http = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("Authorization", "Bearer " + this.authToken);
        postRequest.setEntity(requestentity);


        HttpResponse response = http.execute(postRequest);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }
        http.getConnectionManager().shutdown();

        return true;




    }

    public boolean comprobarPublicacionFacturaProveedor(FacturaProveedor frov, String auth_token) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String className = frov.getMetaClass().getName();
        String url = "http://localhost:8080/rest/entities/contabi_PublicacionRemota/search";

        JSONObject j = new JSONObject();
        ArrayList conditions = new ArrayList();
        JSONObject j1 = new JSONObject();
        j1.put("property", "operacion");
        j1.put("operator", "contains");
        j1.put("value", "CONTABILIZAR_FACTURAS");
        JSONObject j2 = new JSONObject();
        j2.put("property", "referenciasExternas");
        j2.put("operator", "contains");
        j2.put("value", frov.getId().toString());
        conditions.add(j1);
        conditions.add(j2);

        JSONObject j_ = new JSONObject();
        j_.put("conditions", conditions);

        j.put("filter", j_);

        StringEntity requestentity = new StringEntity(j.toString(), "application/json", "UTF-8");

        DefaultHttpClient http = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("Authorization", "Bearer " + auth_token);
        postRequest.setEntity(requestentity);


        HttpResponse response = http.execute(postRequest);
        http.getConnectionManager().shutdown();
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }else{
            EofSensorInputStream is = (EofSensorInputStream)response.getEntity().getContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = -1;
            while((b = is.read())!=-1){
                baos.write(b);
            }
            String json = new String(baos.toByteArray());
            JSONArray jarr = new JSONArray(json);
            if (jarr.length()==0){
                return false;
            }else{
                if (jarr.length()>1){
                    throw new Exception("Mas de una ocurrencia en la operacion 'CONTABILIZAR_FACTURAS' para el id : '" + frov.getId().toString() + "' fueron halladas");
                }else{
                    return true;
                }
            }

        }
    }

    public boolean comprobarPublicacionRemesaRecibos(Remesa r, String auth_token) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String className = r.getMetaClass().getName();
        String url = "http://localhost:8080/rest/entities/contabi_PublicacionRemota/search";

        JSONObject j = new JSONObject();
        ArrayList conditions = new ArrayList();
        JSONObject j1 = new JSONObject();
        j1.put("property", "operacion");
        j1.put("operator", "contains");
        j1.put("value", "CONTABILIZAR_REMESAS_RECIBOS");
        JSONObject j2 = new JSONObject();
        j2.put("property", "referenciasExternas");
        j2.put("operator", "contains");
        j2.put("value", r.getId().toString());
        conditions.add(j1);
        conditions.add(j2);

        JSONObject j_ = new JSONObject();
        j_.put("conditions", conditions);

        j.put("filter", j_);

        StringEntity requestentity = new StringEntity(j.toString(), "application/json", "UTF-8");

        DefaultHttpClient http = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("Authorization", "Bearer " + auth_token);
        postRequest.setEntity(requestentity);


        HttpResponse response = http.execute(postRequest);
        http.getConnectionManager().shutdown();
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }else{
            EofSensorInputStream is = (EofSensorInputStream)response.getEntity().getContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = -1;
            while((b = is.read())!=-1){
                baos.write(b);
            }
            String json = new String(baos.toByteArray());
            JSONArray jarr = new JSONArray(json);
            if (jarr.length()==0){
                return false;
            }else{
                if (jarr.length()>1){
                    throw new Exception("Mas de una ocurrencia en la operacion 'CONTABILIZAR_REMESAS_RECIBOS' para el id : '" + r.getId().toString() + "' fueron halladas");
                }else{
                    return true;
                }
            }

        }
    }

    public boolean publicaContabilizacionRemesaRecibos(User cubaUser, Remesa r, byte[] bb) throws Exception{
        this.authToken = getAuthToken(cubaUser, "admin", "EaGmTfki");

        if (comprobarPublicacionRemesaRecibos(r, this.authToken)){
            throw new Exception("Esta remesa ya esta publicada para la operacion 'CONTABILIZAR_REMESAS_RECIBOS'");
        }

        r = dataManager.reload(r, "remesa-view-detalle");

        String bancariaAdministracion = "";
        if (r.getDefinicionRemesa().getTipoGiro() == DefinicionRemesaTipoGiroEnum.BANCARIA){
            bancariaAdministracion = "BANCARIA";
        }else{
            bancariaAdministracion = "ADMINISTRACION";
        }


        String b64Report = Base64.getEncoder().encodeToString(bb);

        JSONObject jo = new JSONObject();
        jo.put("REPORT", b64Report);
        Propietario prop = r.getDefinicionRemesa().getPropietario();
        prop = dataManager.reload(prop, "propietario-view");
        String nifReceptor = prop.getPersona().getNifDni();
        jo.put("NIF_RECEPTOR", nifReceptor);
        jo.put("OPERACION", "CONTABILIZAR_REMESAS_RECIBOS");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        jo.put("IDENTIFICADOR_REMESA", r.getIdentificadorRemesa());
        jo.put("FECHA_EMISION", sdf.format(r.getFechaValor()));
        jo.put("TIPO_DEFINICION_REMESA", bancariaAdministracion);

        if ((r.getOrdenantesRemesa()!=null) && (r.getOrdenantesRemesa().size()>0)){
            boolean reciboIndividualizado = false;
            if (r.getOrdenantesRemesa().size()==1){
                OrdenanteRemesa or = r.getOrdenantesRemesa().get(0);
                if (or.getRecibos().size()==1){
                    jo.put("RECIBO_INDIVIDUALIZADO", Boolean.TRUE);
                    reciboIndividualizado = true;
                }else{
                    jo.put("RECIBO_INDIVIDUALIZADO", Boolean.FALSE);
                }
            }

            HashMap<Ubicacion, List<Recibo>> ubicacionesRecibos = new HashMap<>();
            HashMap<Ubicacion, Double[]> ubicacionesTotales = new HashMap<>();
            List<Ubicacion> ubs = new ArrayList<>();
            String nombreInqulinoReciboIndividualizado = "";
            //relleno estructuras
            List<OrdenanteRemesa> oorr = r.getOrdenantesRemesa();
            for (int i = 0; i < oorr.size(); i++) {
                OrdenanteRemesa or = oorr.get(i);
                List<Recibo> rr = or.getRecibos();
                for (int j = 0; j < rr.size(); j++) {
                    Recibo rbo = rr.get(j);
                    Departamento d = rbo.getUtilitarioContratoInquilino().getDepartamento();
                    d = dataManager.reload(d, "departamento-view-for-tree");
                    Ubicacion u = d.getUbicacion();
                    if (ubs.indexOf(u)==-1){
                        ubs.add(u);
                    }
                    if (!ubicacionesRecibos.containsKey(u)){
                        ubicacionesRecibos.put(u, new ArrayList());
                    }
                    if (!ubicacionesTotales.containsKey(u)){
                        //0 - totales viviendas sin iva
                        //1 - totales no viviendas sin iva
                        //2 - total de iva
                        ubicacionesTotales.put(u, new Double[]{0.0,0.0, 0.0});
                    }
                    List<Recibo> rr_ub = ubicacionesRecibos.get(u);
                    Double[] dd = ubicacionesTotales.get(u);
                    rr_ub.add(rbo);
                    if (rbo.getTotalIVA()==0){
                        dd[0] += rbo.getTotalRecibo();
                    }else{
                        dd[1] += rbo.getTotalRecibo();
                    }
                    if (rbo.getTotalIVA()>0){
                        dd[2] += rbo.getTotalIVA();
                    }
                    if (reciboIndividualizado){
                        if (nombreInqulinoReciboIndividualizado.trim().length()==0)
                            rbo = dataManager.reload(rbo, "recibo-view");
                            nombreInqulinoReciboIndividualizado = rbo.getUtilitarioContratoInquilino().getInquilino().getNombreCompleto();
                            nombreInqulinoReciboIndividualizado += " " + rbo.getUtilitarioContratoInquilino().getDepartamento().getUbicacion().getAbreviacionUbicacion() +
                                    rbo.getUtilitarioContratoInquilino().getDepartamento().getAbreviacionPisoPuerta();

                    }
                }
            }
            //una vez recorrida la estructura puedo crear la estructura json
            Collections.sort(ubs, new Comparator<Ubicacion>() {
                @Override
                public int compare(Ubicacion o1, Ubicacion o2) {
                    return o1.getRm2id().compareTo(o2.getRm2id());
                }
            });
            List jsons = new ArrayList();
            for (int i = 0; i < ubs.size(); i++) {
                Ubicacion u = ubs.get(i);
                Double[] totales = ubicacionesTotales.get(u);
                JSONObject jo_r = new JSONObject();
                jo_r.put("TOTALES_VIVIENDAS", totales[0]);
                jo_r.put("TOTALES_NO_VIVIENDAS", totales[1]);
                jo_r.put("TOTALES_IVA", totales[2]);
                jo_r.put("NOMBRE_UBICACION", u.getNombre());
                jo_r.put("ABREVIACION_UBICACION", u.getAbreviacionUbicacion());
                if (reciboIndividualizado){
                    jo_r.put("NOMBRE_INQUILINO_RECIBO_INDIVIDUALIZADO", nombreInqulinoReciboIndividualizado);
                }
                jsons.add(jo_r);
            }
            JSONArray jarr = new JSONArray(jsons);
            jo.put("FINCAS", jarr);
        }




        String json = jo.toString();

        String url = "http://localhost:8080/entities/contabi_PublicacionRemota";

        sdf = new SimpleDateFormat("yyyy-MM-dd");

        JSONObject j = new JSONObject();
        j.put("sistemaRemoto", "SOFIA");
        j.put("fechaPublicacion", sdf.format(new Date()));
        j.put("contenido", json);
        j.put("referenciasExternas", r.getId().toString());
        j.put("operacion", "CONTABILIZAR_REMESAS_RECIBOS");


        url = "http://localhost:8080/rest/entities/contabi_PublicacionRemota";

        StringEntity requestentity = new StringEntity(j.toString(), "application/json", "UTF-8");

        DefaultHttpClient http = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("Authorization", "Bearer " + this.authToken);
        postRequest.setEntity(requestentity);


        HttpResponse response = http.execute(postRequest);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }
        http.getConnectionManager().shutdown();

        return true;




    }

}