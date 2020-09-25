create table CONCEPTO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TITULO_CONCEPTO varchar(100),
    RM2ID integer,
    ABREVIACION varchar(25),
    DESCRIPCION varchar(255),
    ADICION_SUSTRACCION boolean,
    FECHA_CREACION date,
    AGREGAR_CONCEPTO_EN_RECIBO boolean,
    AJUSTABLE_AGREGADAMENTE boolean,
    ORDENACION integer not null,
    --
    primary key (ID)
);