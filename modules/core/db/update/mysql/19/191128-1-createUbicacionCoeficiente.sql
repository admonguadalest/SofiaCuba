create table TEST1_UBICACION_COEFICIENTE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    UBICACION_ID varchar(32),
    TIPO_COEFICIENTE_ID varchar(32),
    TOTAL_TIPO_COEFICIENTE double precision,
    --
    primary key (ID)
);