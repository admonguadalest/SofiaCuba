create table IMPUTACION_DOCUMENTO_IMPUTABLE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DESCRIPCION_IMPUTACION varchar(255),
    IMPORTE_IMPUTACION double precision,
    IMPUTACION_INDEFINIDOS boolean,
    INFORMACION_ADICIONAL varchar(255),
    PORCENTAJE_IMPUTACION double precision,
    DOCUMENTO_IMPUTABLE_ID varchar(32),
    CICLO_ID varchar(32),
    EVENTO_ID varchar(32),
    --
    primary key (ID)
);