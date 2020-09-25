create table TEST1_REALIZACION_COBRO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_VALOR date,
    XSD longtext,
    SEPA longtext,
    IDENTIFICADOR varchar(255),
    IMPORTE_TOTAL double precision,
    RM2ID integer,
    --
    primary key (ID)
);