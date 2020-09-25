create table TEST1_ARCHIVO_ADJUNTO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    EXT_ID integer,
    REPRESENTACION_SERIAL longblob,
    NOMBRE_ARCHIVO varchar(255),
    NOMBRE_ARCHIVO_ORIGINAL varchar(255),
    DESCRIPCION varchar(255),
    EXTENSION varchar(50),
    MIME_TYPE varchar(100),
    TAMANO integer,
    --
    primary key (ID)
);