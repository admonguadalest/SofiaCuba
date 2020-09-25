create table TEST1_SECCION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    MODELO_CONTRATO_ID varchar(32),
    NOMBRE varchar(255),
    DESCRIPCION varchar(255),
    OBLIGATORIA boolean,
    --
    primary key (ID)
);