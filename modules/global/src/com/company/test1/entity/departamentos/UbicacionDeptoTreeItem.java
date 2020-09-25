package com.company.test1.entity.departamentos;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@MetaClass(name = "test1_UbicacionDeptoTreeItem")
public class UbicacionDeptoTreeItem extends BaseUuidEntity {
    private static final long serialVersionUID = 4396318645154058312L;

    @MetaProperty
    protected Departamento departamento;

    @MetaProperty
    protected Ubicacion ubicacion;

    @MetaProperty
    protected UbicacionDeptoTreeItem parentItem;

    public void setParentItem(UbicacionDeptoTreeItem parentUbicacion) {
        this.parentItem = parentUbicacion;
    }

    public UbicacionDeptoTreeItem getParentItem() {
        return parentItem;
    }

    @MetaProperty
    public String getTextoItem() {
        try {
            if (departamento != null) {
                return departamento.getPiso() + " " + departamento.getPuerta();
            }
            if (parentItem==null){
                return getUbicacion().getNombre();
            }
        }catch(Exception exc){
            return "N/D";
        }
        return "N/D";
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

}