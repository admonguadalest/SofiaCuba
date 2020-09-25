package com.company.test1.service.accessory;

import com.company.test1.entity.departamentos.Ubicacion;

public class HlpUbicacionInformeIva {
    public String id;
    public String nombre;
    Ubicacion ubicacion = null;

    public HlpUbicacionInformeIva(Ubicacion u){
        this.ubicacion = u;
        this.nombre = u.getNombre();
        this.id = u.getId().toString().replace("-","");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre(){
        return nombre;

    }


    public void setNombre(String n){
        this.nombre = n;
    }


}
