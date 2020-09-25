create table TEST1_VERSION_CLAUSULA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CLAUSULA_ID varchar(32),
    ES_PREDETERMINADA boolean,
    TEXTO_VERSION longtext,
    NOMBRES_PARAMETROS varchar(255),
    DESCRIPCION_PARAMETROS varchar(255),
    EXPRESIONES_VALORES_DEFECTO varchar(255),
    --
    primary key (ID)
);