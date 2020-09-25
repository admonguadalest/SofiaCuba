create table REMESA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_ADEUDO date,
    FECHA_REALIZACION date,
    IDENTIFICADOR_REMESA varchar(255),
    DEFINICION_REMESA_ID varchar(32),
    FECHA_VALOR date,
    RM2ID integer,
    --
    primary key (ID)
);