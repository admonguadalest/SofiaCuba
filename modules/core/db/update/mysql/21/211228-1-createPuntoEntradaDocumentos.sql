create table PUNTO_ENTRADA_DOCUMENTOS (
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
    TIPO varchar(25),
    DESCRIPCION longtext,
    PROPIEDADES_JSON longtext,
    --
    primary key (ID)
);