create table TEST1_REGISTRO_PRESENCIAL (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    USUARIO_ID varchar(32),
    TIMESTAMP time(3) not null,
    ACCION varchar(255) not null,
    UBICACION varchar(255),
    --
    primary key (ID)
);