create table CICLO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CODIGO_CICLO varchar(50),
    DESCRIPCION longtext,
    ES_CICLO_CORRIENTE boolean,
    ESTADO_CICLO integer,
    FECHA_CICLO date,
    OBSERVACIONES longtext,
    TITULO_CICLO varchar(255),
    COLECCION_ADJUNTOS_ID varchar(32),
    DEPARTAMENTO_ID varchar(32),
    TIPO_CICLO varchar(50),
    CONTRATO_INQUILINO_ID varchar(32),
    EXCLUIR_DE_MONITORIZACION_PARA_BUSQUEDA_ORDENES_TRABAJO boolean,
    --
    primary key (ID)
);