create table TEST1_REGISTRO_CONTRASENA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TITULO varchar(255) not null,
    CONTENIDO longtext not null,
    PRIVADO_PUBLICO boolean not null,
    USUARIO_ID varchar(32) not null,
    --
    primary key (ID)
);