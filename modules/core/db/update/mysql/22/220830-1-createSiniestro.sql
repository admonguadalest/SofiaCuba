create table TEST1_SINIESTRO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_SINIESTRO date,
    DESCRIPCION_SINIESTRO longtext,
    DATOS_DE_CONTACTO_PARTES longtext,
    SINIESTRO_CERRADO boolean,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    DIARIO_DE_SINIESTRO longtext,
    POLIZA_DE_SEGUROS_ID varchar(32),
    --
    primary key (ID)
);