package com.company.test1.service;

import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.recibos.Remesa;
import com.haulmont.cuba.security.entity.User;

public interface ContabiService {
    String NAME = "test1_ContabiService";

    boolean publicaContabilizacionFacturaProveedor(User cubaUser, FacturaProveedor frov) throws Exception;

    String getAuthToken(User cubaUser, String user, String pwd) throws Exception;

    boolean comprobarPublicacionFacturaProveedor(FacturaProveedor frov, String auth_token) throws Exception;

    boolean publicaContabilizacionRemesaRecibos(User cubaUser, Remesa r, byte[] bb) throws Exception;

    boolean comprobarPublicacionRemesaRecibos(Remesa r, String auth_token) throws Exception;

}