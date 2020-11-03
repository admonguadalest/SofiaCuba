package com.company.test1.service;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.notificaciones.Notificacion;
import com.company.test1.entity.notificaciones.NotificacionContratoInquilino;
import com.company.test1.entity.recibos.Recibo;

import java.util.Hashtable;
import java.util.List;

public interface NotificacionService {
    String NAME = "test1_NotificacionService";

    byte[] getVersionPdf(Notificacion n);

    byte[] getVersionPdfConcatenada(List<Notificacion> nn);

    Notificacion implementaContenido(Notificacion n, Hashtable ht, boolean verCamposVacios) throws Exception;

    Notificacion implementaContenidoManual(Notificacion n, String contenido, Hashtable ht, boolean verCamposVacios) throws Exception;

    Notificacion implementaVersionPdfVersionFlexReport(Notificacion n) throws Exception;

    List<NotificacionContratoInquilino> devuelveNotificacionesAsociadasAContrato(ContratoInquilino ci) throws Exception;

    List<Recibo> getRecibosPendientes(ContratoInquilino ci) throws Exception;
}