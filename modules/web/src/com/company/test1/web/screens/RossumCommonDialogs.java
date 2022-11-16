package com.company.test1.web.screens;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.company.test1.entity.RossumAnnotation;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Carlos Conti
 */
public class RossumCommonDialogs {

    String authToken = "5e9fe8113245728be44b9282ddfdb49303c6a6cf";
    String domain = null;

    public Map<Integer, JSONObject> queues = null;


    public String getAuthToken(String user, String pwd) throws Exception{
        try {

            URL url = new URL("https://elis.rossum.ai/api/v1/auth/login");//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            String data = "{\"username\": \"info@cgc-guadalest.com\", \"password\": \"pknrp2h8pk\"}";
            conn.getOutputStream().write(data.getBytes());

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            String outputapp = "";
            while ((output = br.readLine()) != null) {

                outputapp += output;
            }
            conn.disconnect();
            JSONObject json = new JSONObject(outputapp);

            this.authToken = json.getString("key");
            this.domain = json.getString("domain");
            return this.authToken;
        } catch (Exception e) {
            throw e;
        }
    }

    public Map getQueues() throws Exception{
        URL url = new URL("https://elis.rossum.ai/api/v1/queues?page_size=1");//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.addRequestProperty("Authorization", "Bearer " + this.authToken);
        conn.addRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        String outputapp = "";
        while ((output = br.readLine()) != null) {

            outputapp += output;
        }
        conn.disconnect();

        JSONObject json = new JSONObject(outputapp);
        JSONArray jarr = json.getJSONArray("results");
        Map<Integer, JSONObject> m = new HashMap();
        for (int i = 0; i < jarr.length(); i++) {
            JSONObject o = jarr.getJSONObject(i);
            m.put((Integer)o.get("id"), o);
        }
        this.queues = m;
        return m;
    }

    public Map getAllAnnotations(Integer queueId) throws Exception{
        URL url = new URL("https://elis.rossum.ai/api/v1/annotations?queuee="+queueId);//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.addRequestProperty("Authorization", "Bearer " + this.authToken);
        conn.addRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        String outputapp = "";
        while ((output = br.readLine()) != null) {

            outputapp += output;
        }
        conn.disconnect();
        JSONObject json = new JSONObject(outputapp);
        JSONArray jarr = json.getJSONArray("results");
        Map<Integer, JSONObject> m = new HashMap();
        for (int i = 0; i < jarr.length(); i++) {
            JSONObject o = jarr.getJSONObject(i);
            m.put((Integer)o.get("id"), o);
        }
        return m;
    }

    public Map getAnnotationsWithStatus(Integer queueId, String status) throws Exception{
        //URL url = new URL("https://elis.rossum.ai/api/v1/annotations?queue="+queueId+"&status="+status);//your url i.e fetch data from .
        URL url = new URL("https://api.elis.rossum.ai/v1/annotations?queue="+queueId+"&status="+status);
        Map<Integer, JSONObject> m = new HashMap();
        boolean stopQuerying = false;
        while(!stopQuerying){
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Authorization", "Bearer " + this.authToken);
            conn.addRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            String outputapp = "";
            while ((output = br.readLine()) != null) {

                outputapp += output;
            }
            conn.disconnect();
            JSONObject json = new JSONObject(outputapp);
            JSONArray jarr = json.getJSONArray("results");

            for (int i = 0; i < jarr.length(); i++) {
                JSONObject o = jarr.getJSONObject(i);
                String status_o = o.getString("status");
                if (status.indexOf(status_o)!=-1){
                    m.put((Integer)o.get("id"), o);
                }

            }
            if (json.get("pagination")!=null){
                JSONObject pagination = (JSONObject)json.get("pagination");
                if (pagination.get("next")!=null){
                    String nextString = null;
                    if (pagination.get("next") instanceof String){
                        nextString = pagination.getString("next");
                    }else{
                        nextString = null;
                    }
                    if ((nextString == null) || (nextString.compareTo("null")==0)){
                        stopQuerying = true;
                    }else{
                        url = new URL(nextString);
                    }
                }
            }
        }

        return m;
    }

    public Map getAnnotationExportedDataAndOriginalUrl(Integer queueId, Integer annotationId) throws Exception{
        URL url = new URL("https://elis.rossum.ai/api/v1/queues/"+queueId+"/export?id="+annotationId);//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.addRequestProperty("Authorization", "Bearer " + this.authToken);
        conn.addRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        String outputapp = "";
        while ((output = br.readLine()) != null) {

            outputapp += output;
        }
        conn.disconnect();
        JSONObject json = new JSONObject(outputapp);
        JSONArray jarr = (JSONArray)json.get("results");
        JSONObject json0 = jarr.getJSONObject(0);
        JSONObject document = json0.getJSONObject("document");
        String urlToOriginalDocument = document.getString("url");
        JSONArray jarrContent = json0.getJSONArray("content");

        HashMap m = new HashMap();
        m.put("url", urlToOriginalDocument);
        m.put("data", jarrContent);
        m.put("documentId", Integer.valueOf(urlToOriginalDocument.substring(urlToOriginalDocument.lastIndexOf("/")+1)));
        m.put("document", document);
        return m;
    }

    public byte[] getOriginalDocumentFromDocumentId(Integer documentId)throws Exception{
        URL url = new URL("https://elis.rossum.ai/api/v1/documents/"+documentId+"/content");//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.addRequestProperty("Authorization", "Bearer " + this.authToken);
        //conn.addRequestProperty("Content-Type", "application/octed-stream;");
        conn.setRequestProperty("Accept", "*/*;");
        conn.setRequestProperty("Content Description", "File Transfer");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }

        InputStream is = conn.getInputStream();
        int c = -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while((c = is.read())!=-1){
            baos.write(c);
        }
        conn.disconnect();


        byte[] bb = baos.toByteArray();
        return bb;
    }

    public boolean switchAnnotationToDeleted(Integer annotationId) throws Exception{
        //'https://elis.rossum.ai/api/v1/annotations/319668/delete
        URL url = new URL("https://elis.rossum.ai/api/v1/annotations/" + annotationId + "/delete");//your url i.e fetch data from .
        //URL url = new URL("https://elis.rossum.ai/api/v1/auth/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.addRequestProperty("Authorization", "Bearer " + this.authToken);

        conn.addRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 204) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        conn.disconnect();
        return true;
    }

    public boolean purgeDeletedAnnotations(Integer queueId) throws Exception{
        //'https://elis.rossum.ai/api/v1/annotations/319668/delete
        URL url = new URL("https://elis.rossum.ai/api/v1/annotations/purge_deleted");//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.addRequestProperty("Authorization", "Bearer " + this.authToken);
        conn.addRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        String data = "{\"queue\": \"https://elis.rossum.ai/api/v1/queues/" + queueId + "\"}";
        conn.getOutputStream().write(data.getBytes());

        if (conn.getResponseCode() != 202) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        conn.disconnect();
        return true;
    }

    public JSONObject getDocumentFromDocumentId(Integer documentId) throws Exception{
        //https://elis.rossum.ai/api/v1/documents/314628
        URL url = new URL("https://elis.rossum.ai/api/v1/documents/"+documentId);//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.addRequestProperty("Authorization", "Bearer " + this.authToken);
        conn.addRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        String outputapp = "";
        while ((output = br.readLine()) != null) {

            outputapp += output;
        }
        conn.disconnect();
        JSONObject json = new JSONObject(outputapp);
        return json;
    }

    public String getContentTypeFromFileNameExtension(String filename){
        String extension = filename.substring(filename.lastIndexOf(".")+1);
        if (extension.compareTo("pdf")==0){
            return "application/pdf";
        }
        if ((extension.compareTo("jpg")==0)||(extension.compareTo("jpeg")==0)){
            return "image/jpg";
        }
        if (extension.compareTo("png")==0){
            return "image/png";
        }
        if (extension.compareTo("gif")==0){
            return "image/gif";
        }
        if (extension.compareTo("docx")==0){
            return "application/docx";
        }
        return extension;
    }

    public RossumAnnotation getInvoiceStructFromMap(Integer queueId, Integer annotationId, Map m) throws Exception{
        RossumAnnotation rbi = new RossumAnnotation();
        JSONObject document = (JSONObject)m.get("document");
        rbi.setContentType(getContentTypeFromFileNameExtension(document.getString("file_name")));
        rbi.setQueueId(queueId);
        rbi.setAnnotationId(annotationId);
        rbi.setBasicInformation_documentId((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(0)).get("children")).get(0)).get("value"));
        rbi.setBasicInformation_purchaseOrderNumber((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(0)).get("children")).get(1)).get("value"));
        rbi.setBasicInformationIssueDate((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(0)).get("children")).get(2)).get("value"));
        rbi.setBasicInformation_dueDate((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(0)).get("children")).get(3)).get("value"));

        rbi.setPaymentInstructions_accountNumber((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(1)).get("children")).get(0)).get("value"));
        rbi.setPaymentInstructions_bankCode((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(1)).get("children")).get(1)).get("value"));
        rbi.setPaymentInstructions_iban((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(1)).get("children")).get(2)).get("value"));

        rbi.setTotals_amountDue((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(2)).get("children")).get(2)).get("value"));
        rbi.setTotals_totalTax((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(2)).get("children")).get(1)).get("value"));
        rbi.setTotals_subtotal((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(2)).get("children")).get(0)).get("value"));
        rbi.setTotals_currency((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(2)).get("children")).get(3)).get("value"));
        try{
            rbi.setTotals_taxDetails((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(0)).get("children")).get(4)).get("value"));
        }catch(Exception exc){

        }
        rbi.setVendorCustomer_vendorName((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(3)).get("children")).get(0)).get("value"));
        rbi.setVendorCustomer_vendorAddress((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(3)).get("children")).get(1)).get("value"));
        rbi.setVendorCustomer_customerName((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(3)).get("children")).get(2)).get("value"));
        rbi.setVendorName_customerAddress((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(3)).get("children")).get(3)).get("value"));

        rbi.setOtherNotes((String)((JSONObject)((JSONArray)((JSONObject)((JSONArray)m.get("data")).get(4)).get("children")).get(0)).get("value"));

        rbi.setDocumentId((Integer)m.get("documentId"));



        return rbi;


    }




}
