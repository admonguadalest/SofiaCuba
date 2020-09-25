create table MODELO_CONTRATO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_MODELO varchar(255),
    DESCRIPCION varchar(255),
    INQUILINO_PROPIETARIO varchar(50),
    --
    primary key (ID)
);