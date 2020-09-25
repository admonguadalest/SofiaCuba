create table TEST1_CLAUSULA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SECCION_ID varchar(32),
    NOMBRE_CLAUSULA varchar(255),
    DESCRIPCION varchar(255),
    ORDENACION integer,
    --
    primary key (ID)
);