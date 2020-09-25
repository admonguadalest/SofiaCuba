create table PROPIETARIO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    ABREVIACION_CONTRATOS varchar(25),
    CODIGO_CLIENTE varchar(50),
    EXONERACION_IRPF boolean,
    GESTION_CAJA boolean,
    PERSONA_ID varchar(32),
    CABECERA_DOCUMENTOS_ARCHIVO_ADJUNTO_ID varchar(32),
    CUENTA_BANCARIA_ID varchar(32),
    PROSPECTO boolean,
    RM2ID integer,
    --
    primary key (ID)
);