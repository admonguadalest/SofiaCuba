create table TEST1_TIPO_COEFICIENTE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE varchar(255),
    DESCRIPCION varchar(255),
    VALOR_MIN double precision,
    VALOR_MAX double precision,
    --
    primary key (ID)
);