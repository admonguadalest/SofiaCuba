package com.company.test1.service;

import com.company.test1.entity.documentosImputables.FacturaProveedor;

public interface ContabiService {
    String NAME = "test1_ContabiService";

    boolean publicaContabilizacionFacturaProveedor(FacturaProveedor frov) throws Exception;

    String getAuthToken(String user, String pwd) throws Exception;

    boolean comprobarPublicacionFacturaProveedor(FacturaProveedor frov, String auth_token) throws Exception;

}