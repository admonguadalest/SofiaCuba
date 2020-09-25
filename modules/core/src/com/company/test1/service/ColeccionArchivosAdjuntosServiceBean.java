package com.company.test1.service;

import com.company.test1.entity.*;
import com.company.test1.entity.documentosfotograficos.FotoDocumentoFotografico;
import com.company.test1.entity.documentosfotograficos.FotoThumbnail;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service(ColeccionArchivosAdjuntosService.NAME)
public class ColeccionArchivosAdjuntosServiceBean implements ColeccionArchivosAdjuntosService {

    @Inject
    private Persistence persistence;

    public List<ColeccionArchivosAdjuntos> devuelveListColeccionesIntegrantes(ColeccionArchivosAdjuntos caa){
        ArrayList<ColeccionArchivosAdjuntos> al = new ArrayList<ColeccionArchivosAdjuntos>();
        ArrayList<Iterator> iterators = new ArrayList<Iterator>();
        al.add(caa);
        Iterator<ColeccionArchivosAdjuntos> curr = caa.getColecciones().iterator();

        while(true){
            while(curr.hasNext()){
                ColeccionArchivosAdjuntos scaa = curr.next();
                al.add(scaa);
                iterators.add(curr);
                curr = scaa.getColecciones().iterator();
            }
            if(iterators.size()>0){
                curr = iterators.get(iterators.size()-1);
                iterators.remove(iterators.get(iterators.size()-1));
            }else{
                break;
            }
        }


        return al;
    }

    public ArchivoAdjuntoExt getArchivoAdjuntoExt(ArchivoAdjunto aa){
        Integer extId = aa.getExtId();
        Transaction t = persistence.createTransaction("rentamasterdocs");
        ArchivoAdjuntoExt aaext = (ArchivoAdjuntoExt) persistence.getEntityManager("rentamasterdocs").createQuery("select aa FROM test1_ArchivosAdjuntos aa WHERE aa.id = :extid")
                .setParameter("extid", extId).getFirstResult();
        t.close();
        return aaext;
    }

    public FotosThumbnailExt getFotoDocumentoFotograficoExt(FotoDocumentoFotografico fdf){
        Integer extId = fdf.getFotoThumbnail().getExtId();
        Transaction t = persistence.createTransaction("rentamasterdocs");
        FotosThumbnailExt fth = (FotosThumbnailExt) persistence.getEntityManager("rentamasterdocs").createQuery("select fth FROM test1_FotosThumbnailExt fth " +
                "  WHERE fth.id = :extid")
                .setParameter("extid", extId).getFirstResult();
        t.close();
        return fth;
    }

    public ArchivoAdjuntoExt generaNuevoArchivoAdjuntoExt(ArchivoAdjunto aa) throws Exception{
        Transaction t = persistence.createTransaction("rentamasterdocs");

        ArchivoAdjuntoExt aaext = new ArchivoAdjuntoExt();
        try {
            aaext.setExtension(aa.getExtension());
            aaext.setDescripcion(aa.getDescripcion());
            aaext.setMimeType(aa.getMimeType());
            aaext.setNombreArchivo(aa.getNombreArchivo());
            aaext.setNombreArchivoOriginal(aa.getNombreArchivoOriginal());
            aaext.setRepresentacionSerial(aa.getRepresentacionSerial());
            aaext.setTamano(aa.getTamano());
            persistence.getEntityManager("rentamasterdocs").persist(aaext);
            persistence.getEntityManager("rentamasterdocs").merge(aaext);
            t.commit();
            t.close();
            aa.setRepresentacionSerial(null);
            aa.setExtId(aaext.getId().get());
            persistence.getEntityManager().merge(aa);
        }catch(Exception exc){
            t.close();
            throw exc;
        }
        return aaext;
    }

    public FotosDocumentosFotograficosExt generaNuevoFotoDocumentoFotograficoExt(FotoDocumentoFotografico aa) throws Exception{
        Transaction t = persistence.createTransaction("rentamasterdocs");

        FotosDocumentosFotograficosExt aaext = new FotosDocumentosFotograficosExt();
        try {
            aaext.setExtension(aa.getExtension());
            aaext.setDescripcion(aa.getDescripcion());
            aaext.setMimeType(aa.getMimeType());
            aaext.setNombreArchivo(aa.getNombreArchivo());
            aaext.setNombreArchivoOriginal(aa.getNombreArchivoOriginal());
            aaext.setRepresentacionSerial(aa.getRepresentacionSerial());
            aaext.setTamano(aa.getTamano());
            persistence.getEntityManager("rentamasterdocs").persist(aaext);
            persistence.getEntityManager("rentamasterdocs").merge(aaext);
            t.commit();
            t.close();
            aa.setRepresentacionSerial(null);
            aa.setExtId(aaext.getId().get());
            persistence.getEntityManager().merge(aa);
        }catch(Exception exc){
            t.close();
            throw exc;
        }
        return aaext;
    }

    public FotosThumbnailExt generaNuevoFotoThumbnailExt(FotoThumbnail aa) throws Exception{
        Transaction t = persistence.createTransaction("rentamasterdocs");

        FotosThumbnailExt aaext = new FotosThumbnailExt();
        try {

            aaext.setRepresentacionSerial(aa.getRepresentacionSerial());
            aaext.setTamano(aa.getTamano());
            persistence.getEntityManager("rentamasterdocs").persist(aaext);
            persistence.getEntityManager("rentamasterdocs").merge(aaext);
            t.commit();
            t.close();
            aa.setRepresentacionSerial(null);
            aa.setExtId(aaext.getId().get());
            persistence.getEntityManager().merge(aa);
        }catch(Exception exc){
            t.close();
            throw exc;
        }
        return aaext;
    }





}