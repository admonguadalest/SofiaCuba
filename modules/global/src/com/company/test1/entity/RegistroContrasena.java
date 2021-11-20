package com.company.test1.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "TEST1_REGISTRO_CONTRASENA")
@Entity(name = "test1_RegistroContrasena")
public class RegistroContrasena extends StandardEntity {
    private static final long serialVersionUID = 5906272756691678955L;

    @Column(name = "TITULO", nullable = false)
    @NotNull
    private String titulo;

    @Lob
    @Column(name = "CONTENIDO", nullable = false)
    @NotNull
    private String contenido;

    @Column(name = "PRIVADO_PUBLICO", nullable = false)
    @NotNull
    private Boolean privadoPublico = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USUARIO_ID")
    @NotNull
    private User usuario;

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Boolean getPrivadoPublico() {
        return privadoPublico;
    }

    public void setPrivadoPublico(Boolean privadoPublico) {
        this.privadoPublico = privadoPublico;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}