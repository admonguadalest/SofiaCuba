create table TEST1_PUNTO_SUMINISTRO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PROVEEDOR_ID varchar(32),
    IDENTIFICADOR_PUNTO_SUMINISTRO varchar(255),
    DESCRIPCION longtext,
    --
    primary key (ID)
);