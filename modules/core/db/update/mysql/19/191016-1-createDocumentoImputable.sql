create table TEST1_DOCUMENTO_IMPUTABLE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DESCRIPCION_DOCUMENTO longtext,
    FECHA_EMISION date,
    IMPORTE_TOTAL_BASE double precision,
    IMPORTE_POST_CCAA double precision,
    NUM_DOCUMENTO varchar(100) not null,
    OBSERVACIONES longtext,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    ARCHIVADO boolean not null,
    OMITIR_CONTROL_DE_ORDEN_PAGO boolean not null,
    --
    primary key (ID)
);