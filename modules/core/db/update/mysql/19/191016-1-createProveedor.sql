create table TEST1_PROVEEDOR (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PERSONA_ID varchar(32) not null,
    DESCRIPCION_ACTIVIDAD varchar(255),
    OBSERVACIONES varchar(255),
    COMERCIAL_OFERTAS_ID varchar(32),
    NOMBRE_COMERCIAL varchar(255),
    CUENTA_BANCARIA_ID varchar(32),
    MODO_DE_PAGO varchar(50),
    ENVIAR_CORREO_CONFIRMACION_AL_APROBAR_FACTURA boolean,
    --
    primary key (ID)
);