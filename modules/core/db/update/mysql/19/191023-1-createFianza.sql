create table TEST1_FIANZA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CONTRATO_INQUILINO_ID varchar(32),
    ES_AVAL_BANCARIO boolean,
    FECHA_ABONO_FIANZA date,
    FIANZA_COMPLEMENTARIA double precision,
    FIANZA_LEGAL double precision,
    IDENTIFICADOR_AVAL varchar(255),
    OBSERVACIONES longtext,
    ESCANEO_ARCHIVO_ADJUNTO_ID varchar(32),
    ESTADO_FIANZA integer,
    TIENE_POLIZA_ALQUILER boolean,
    NUMERO_POLIZA varchar(255),
    INFORMACION_DE_CONTACTO_POLIZA varchar(255),
    ESCANEO_SEGURO_ARCHIVO_ADJUNTO_ID varchar(32),
    FECHA_INGRESO_FIANZA_EN_CAMARA date,
    FECHA_RESCATE_FIANZA_DE_CAMARA date,
    ESCANEO_FIANZA_ID varchar(32),
    --
    primary key (ID)
);