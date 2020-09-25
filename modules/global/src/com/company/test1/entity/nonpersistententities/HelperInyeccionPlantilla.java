package com.company.test1.entity.nonpersistententities;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@MetaClass(name = "test1_HelperInyeccionPlantilla")
public class HelperInyeccionPlantilla extends BaseUuidEntity {
    private static final long serialVersionUID = 1455454872508229565L;

    @MetaProperty
    protected String titulo;

    @MetaProperty
    protected String valor;

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}