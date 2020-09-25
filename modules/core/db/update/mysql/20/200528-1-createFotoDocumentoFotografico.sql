create table FOTO_DOCUMENTO_FOTOGRAFICO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DESCRIPCION varchar(255),
    EXTENSION varchar(10),
    MIME_TYPE varchar(100),
    NOMBRE_ARCHIVO varchar(255),
    NOMBRE_ARCHIVO_ORIGINAL varchar(255),
    TAMANO integer,
    CARPETA_ID varchar(32),
    REPRESENTACION_SERIAL longblob,
    EXT_ID integer,
    --
    primary key (ID)
);