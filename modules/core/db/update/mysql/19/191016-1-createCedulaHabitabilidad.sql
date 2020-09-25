create table TEST1_CEDULA_HABITABILIDAD (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_EMISION date,
    FECHA_VENCIMIENTO date,
    NUMERO_CEDULA varchar(100),
    OBSERVACIONES longtext,
    DEPARTAMENTO_ID varchar(32),
    --
    primary key (ID)
);