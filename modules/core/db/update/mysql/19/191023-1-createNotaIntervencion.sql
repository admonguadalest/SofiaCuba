create table NOTA_INTERVENCION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_PREVISTA_INTERVENCION date,
    CONTENIDO longtext,
    FRANJA_HORARIA varchar(45),
    FECHA_INTERVENCION date,
    GRADO_EXITO_INTERVENCION integer,
    ORDEN_TRABAJO_ID varchar(32),
    ENVIO_NOTIFICACION_INQUILINO boolean,
    ENVIO_NOTIFICACION_PROPIETARIO boolean,
    AMPLIACION_NOTIFICACION_INQUILINO varchar(255),
    ENVIADO_A_INQUILINO boolean,
    ENVIADO_A_PROPIETARIO boolean,
    COORDINAR_INTERVENCION boolean,
    --
    primary key (ID)
);