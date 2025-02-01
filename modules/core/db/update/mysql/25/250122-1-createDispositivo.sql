create table TEST1_DISPOSITIVO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    IDENTIFICADOR varchar(255),
    TIPO_DISPOSITIVO varchar(50),
    AMPLIACION longtext,
    INSTALADO_EN varchar(255),
    DEPARTAMENTO_ID varchar(32),
    --
    primary key (ID)
);