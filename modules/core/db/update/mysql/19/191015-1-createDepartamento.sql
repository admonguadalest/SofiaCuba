create table TEST1_DEPARTAMENTO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PISO varchar(25),
    PUERTA varchar(25),
    UBICACION_ID varchar(32),
    --
    primary key (ID)
);