create table REALIZACION_PAGO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CUENTA_BANCARIA_ID varchar(32),
    FECHA_VALOR date,
    INFO_CUENTA_EMISORA varchar(255),
    XSD varchar(25),
    SEPA longtext,
    IDENTIFICADOR varchar(255),
    IMPORTE_TOTAL double precision,
    RM2ID integer,
    --
    primary key (ID)
);