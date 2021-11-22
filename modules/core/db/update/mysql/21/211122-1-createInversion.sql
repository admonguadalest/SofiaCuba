create table TEST1_INVERSION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TITULO varchar(255),
    DIRECCION1 varchar(255),
    DIRECCION2 varchar(255),
    LINK_MAPS varchar(255),
    MEDIADOR_ID varchar(32) not null,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    EXPOSICION longtext,
    --
    primary key (ID)
);