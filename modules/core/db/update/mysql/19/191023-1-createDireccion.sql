create table DIRECCION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_DIRECCION varchar(50),
    NOMBRE varchar(255) not null,
    NUMERO varchar(50) not null,
    CODIGO_POSTAL varchar(25),
    UBICACION_ID varchar(32),
    PERSONA_ID varchar(32),
    --
    primary key (ID)
);