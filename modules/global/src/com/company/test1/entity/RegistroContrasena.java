package com.company.test1.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "TEST1_REGISTRO_CONTRASENA")
@Entity(name = "test1_RegistroContrasena")
public class RegistroContrasena extends StandardEntity {
    private static final long serialVersionUID = 5906272756691678955L;
}