create table TEST1_RESERVA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_RESERVA date not null,
    COMERCIAL_ID varchar(32) not null,
    CONDICIONES_RESERVA longtext,
    ESTADO_RESERVA varchar(255),
    OFERTA_ID varchar(32),
    --
    primary key (ID)
);