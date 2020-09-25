create table PERSONA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    DTYPE varchar(31),
    --
    USUARIO_ID varchar(32),
    NOMBRE varchar(255),
    NIF_DNI varchar(50),
    NOMBRE_COMPLETO varchar(255),
    --
    -- from test1_PersonaJuridica
    DESCRIPCION_ACTIVIDAD varchar(255),
    RAZON_SOCIAL varchar(255),
    --
    -- from test1_PersonaFisica
    APELLIDO1 varchar(255),
    APELLIDO2 varchar(255),
    ESTADO_CIVIL varchar(50),
    FECHA_NACIMIENTO date,
    --
    primary key (ID)
);