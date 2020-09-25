create table TEST1_FOTO_THUMBNAIL (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TAMANO integer,
    CARPETA_DOCUMENTO_FOTOGRAFICO_ID varchar(32),
    EXT_ID integer,
    --
    primary key (ID)
);