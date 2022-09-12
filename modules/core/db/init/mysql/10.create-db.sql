-- begin OFERTA
create table OFERTA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DEPARTAMENTO_ID varchar(32) not null,
    IMPORTE_RENTA double precision not null,
    OBSERVACIONES longtext,
    ESTADO_OFERTA varchar(50) not null,
    COMERCIAL_ALQUILER_ID varchar(32),
    --
    primary key (ID)
)^
-- end OFERTA
-- begin SUBROGADOR
create table SUBROGADOR (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_DESDE date,
    FECHA_HASTA date,
    CONTRATO_INQUILINO_ID varchar(32),
    SUBROGADOR_ID varchar(32),
    --
    primary key (ID)
)^
-- end SUBROGADOR
-- begin LIQUIDACION_SUSCRIPCION
create table LIQUIDACION_SUSCRIPCION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    ES_RENOVACION boolean,
    ESCANEO_LIQUIDACION_ID varchar(32),
    FIANZA_CONTRATO double precision,
    GARANTIAS_EN_EFECTIVO double precision,
    RECIBOS_A_CUENTA double precision,
    DESC_RECIBOS_A_CUENTA varchar(255),
    DESC_DEVOLUCION_FIANZA_CONTRATO_RENUNCIADO varchar(255),
    DEVOLUCION_FIANZA_CONTRATO_RENUNCIADO double precision,
    GASTOS_CONTRATO double precision,
    TOTAL_LIQUIDACION double precision,
    DETALLE longtext,
    ESCANEO_INGRESO_LIQUIDACION_ID varchar(32),
    CONTRATO_INQUILINO_ID varchar(32),
    OTROS_CONCEPTOS double precision,
    CANTIDADES_ENTREGADAS_EN_RESERVA double precision,
    FECHA_INGRESO_ITP date,
    ESCANEO_RESGUARDO_ITP_ID varchar(32),
    --
    primary key (ID)
)^
-- end LIQUIDACION_SUSCRIPCION
-- begin PARAMETRO_VALOR
create table PARAMETRO_VALOR (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_PARAMETRO varchar(255),
    VALOR longtext,
    DESCRIPCION_PARAMETRO varchar(255),
    IMPLEMENTACION_MODELO_ID varchar(32),
    --
    primary key (ID)
)^
-- end PARAMETRO_VALOR
-- begin ANEXO
create table ANEXO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_ANEXO varchar(255),
    CONTENIDO longtext,
    PLANTILLA_ID varchar(32),
    CONTRATO_INQUILINO_ID varchar(32),
    --
    primary key (ID)
)^
-- end ANEXO
-- begin VERSION_CLAUSULA
create table VERSION_CLAUSULA (
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
    NOMBRES_PARAMETROS longtext,
    DESCRIPCION_PARAMETROS varchar(255),
    EXPRESIONES_VALORES_DEFECTO longtext,
    RMI2 integer,
    --
    primary key (ID)
)^
-- end VERSION_CLAUSULA
-- begin DEPARTAMENTO
create table DEPARTAMENTO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PISO varchar(255),
    PUERTA varchar(255),
    VIVIENDA_LOCAL boolean,
    SUPERFICIE double precision,
    ABREVIACION_PISO_PUERTA varchar(50),
    DADO_DE_BAJA boolean,
    DESCRIPCION longtext,
    CON_AIRE_ACONDICIONADO boolean,
    CON_CALEFACCION boolean,
    OBSEVACIONES longtext,
    REFERENCIA_CATASTRAL varchar(100),
    COLECCION_ADJUNTOS_ID varchar(32),
    PROPIETARIO_ID varchar(32),
    UBICACION_ID varchar(32),
    PLANO_DEPARTAMENTO_ID varchar(32),
    EXCLUIR_DE_MONITORIZACION_PARA_BUSQUEDA_DE_PISOS_VACIOS boolean,
    EXCLUIR_DE_MONITORIZACION_NO_EMITIDOS_O_ANOMALOS boolean,
    CON_SALIDA_DE_HUMOS boolean,
    NUM_BANOS integer,
    NUM_HABITACIONES integer,
    ES_ESTUDIO boolean,
    RM2ID integer,
    --
    primary key (ID)
)^
-- end DEPARTAMENTO
-- begin NOTIFICACION
create table NOTIFICACION (
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
    CONTENIDO_IMPLEMENTADO longtext,
    ENVIADO boolean,
    FECHA_REALIZACION date,
    IMPLEMENTADO boolean,
    PLANTILLA_ID varchar(32),
    VERSION_PDF longblob,
    FECHA_PROGRAMADA_ENVIO date,
    TITULO varchar(255),
    --
    -- from test1_NotificacionContratoInquilino
    CONTRATO_INQUILINO_ID varchar(32),
    --
    primary key (ID)
)^
-- end NOTIFICACION
-- begin PROVEEDOR
create table PROVEEDOR (
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
    NOMBRE_COMERCIAL varchar(255),
    CUENTA_BANCARIA_ID varchar(32),
    MODO_DE_PAGO varchar(50),
    MODO_DE_PAGO_TELEMATICO boolean,
    MODO_DE_PAGO_DOMICILIADO boolean,
    CUENTA_BANCARIA_DOMICILIADO_ID varchar(32),
    ENVIAR_CORREO_CONFIRMACION_AL_APROBAR_FACTURA boolean,
    RM2ID integer,
    --
    primary key (ID)
)^
-- end PROVEEDOR
-- begin TEST1_FOTO_THUMBNAIL
create table TEST1_FOTO_THUMBNAIL (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TAMANO integer,
    CARPETA_DOCUMENTO_FOTOGRAFICO_ID varchar(32),
    REPRESENTACION_SERIAL longblob,
    EXT_ID integer,
    --
    primary key (ID)
)^
-- end TEST1_FOTO_THUMBNAIL
-- begin DATO_DE_CONTACTO
create table DATO_DE_CONTACTO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DATO varchar(255),
    DESCRIPCION_DATO varchar(255),
    TIPO_DE_DATO varchar(50),
    PERSONA_ID varchar(32),
    CORREO_DE_RECUPERACION_DE_CONSTRASENYA boolean,
    --
    primary key (ID)
)^
-- end DATO_DE_CONTACTO
-- begin CUENTA_BANCARIA
create table CUENTA_BANCARIA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PERSONA_ID varchar(32),
    ENTIDAD varchar(5),
    OFICINA varchar(5),
    DIGITOS_CONTROL varchar(5),
    NUMERO_CUENTA varchar(15),
    INFO_CONTACTO_OFICINA varchar(255),
    DOMICILIO_ENTIDAD_BANCARIA varchar(255),
    NOMBRE_ENTIDAD_BANCARIA varchar(255),
    PAIS varchar(50),
    CODIGO_BIC varchar(11),
    DIGIGOS_CONTROL_IBAN varchar(5),
    RM2ID integer,
    --
    primary key (ID)
)^
-- end CUENTA_BANCARIA
-- begin RTRO_APP_CONCEPTO_ADICIONAL
create table RTRO_APP_CONCEPTO_ADICIONAL (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CONCEPTO_ADICIONAL_ID varchar(32),
    DOCUMENTO_IMPUTABLE_ID varchar(32),
    NIF_DNI varchar(50),
    FECHA_VALOR date,
    NUM_DOCUMENTO varchar(100),
    BASE_ double precision,
    PORCENTAJE double precision,
    IMPORTE_APLICADO double precision,
    IMPLEMENTACION_CONCEPTO_ID varchar(32),
    --
    primary key (ID)
)^
-- end RTRO_APP_CONCEPTO_ADICIONAL
-- begin DIRECCION
create table DIRECCION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CODIGO_POSTAL varchar(25),
    DIRECCION_COMPLETA varchar(255),
    ESCALERA varchar(50),
    NOMBRE varchar(255),
    NOMBRE_VIA varchar(255),
    NUMERO_VIA varchar(100),
    OBSERVACIONES varchar(255),
    PAIS varchar(255),
    PISO varchar(50),
    POBLACION varchar(255),
    PROVINCIA varchar(255),
    PUERTA varchar(25),
    REGION varchar(255),
    VIA varchar(255),
    PERSONA_ID varchar(32),
    ENVIAR_CORRESPONDENCIA_A_ESTA_DIRECCION boolean,
    --
    primary key (ID)
)^
-- end DIRECCION
-- begin COLECCION_ARCHIVOS_ADJUNTOS
create table COLECCION_ARCHIVOS_ADJUNTOS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE varchar(255),
    COLECCION_PARENT_ID varchar(32),
    --
    primary key (ID)
)^
-- end COLECCION_ARCHIVOS_ADJUNTOS
-- begin IMPLEMENTACION_CONCEPTO
create table IMPLEMENTACION_CONCEPTO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    IMPORTE double precision,
    IMPORTE_POST_CCAA double precision,
    CONCEPTO_RECIBO_ID varchar(32),
    RECIBO_ID varchar(32),
    OVERRIDE_CONCEPTO_ID varchar(32),
    RM2ID integer,
    --
    primary key (ID)
)^
-- end IMPLEMENTACION_CONCEPTO
-- begin IMPLEMENTACION_MODELO
create table IMPLEMENTACION_MODELO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    MODELO_CONTRATO_ID varchar(32),
    RMI2 integer,
    --
    primary key (ID)
)^
-- end IMPLEMENTACION_MODELO
-- begin PERSONA
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
    RM2ID integer,
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
)^
-- end PERSONA
-- begin DOCUMENTO_IMPUTABLE
create table DOCUMENTO_IMPUTABLE (
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
    DESCRIPCION_DOCUMENTO longtext,
    MARCAJES_PROVISIONAL varchar(255),
    FECHA_EMISION date,
    IMPORTE_TOTAL_BASE double precision,
    IMPORTE_POST_CCAA double precision,
    NUM_DOCUMENTO varchar(100),
    OBSERVACIONES longtext,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    ARCHIVADO boolean not null,
    OMITIR_CONTROL_DE_ORDEN_PAGO boolean not null,
    RM2ID integer,
    TIPO_ENUM varchar(50),
    --
    -- from test1_DocumentoProveedor
    PROVEEDOR_ID varchar(32),
    TITULAR_PERSONA_ID varchar(32),
    --
    -- from test1_FacturaProveedor
    FECHA_DEVENGO date,
    FECHA_PAGO date,
    IMPORTE_FACTURA_PAGADO double precision,
    PORCENTAJE_FACTURA_PAGADO double precision,
    PRESUPUESTO_ID varchar(32),
    CONSIDERACIONES_DOCUMENTO_IMPUTABLE longtext,
    PAGO_POR_CAJA boolean,
    --
    -- from test1_Presupuesto
    ES_PRESUPUESTO_VERBAL boolean,
    FECHA_VALIDEZ_HASTA date,
    REALIZADO_POR varchar(255),
    IMPORTE_DEFINITIVO double precision,
    CONSIDERACIONES_PRESUPUESTO longtext,
    --
    primary key (ID)
)^
-- end DOCUMENTO_IMPUTABLE
-- begin CONCEPTO
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
    MASTER_CONCEPTO_ID varchar(32),
    --
    primary key (ID)
)^
-- end CONCEPTO
-- begin TEST1_SECCION_DESCARTADA
create table TEST1_SECCION_DESCARTADA (
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
    IMPLEMENTACION_MODELO_ID varchar(32),
    --
    primary key (ID)
)^
-- end TEST1_SECCION_DESCARTADA
-- begin CONCEPTO_ADICIONAL
create table CONCEPTO_ADICIONAL (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE varchar(255),
    ABREVIACION varchar(50),
    ADICION_SUSTRACCION boolean,
    RM2ID integer,
    --
    primary key (ID)
)^
-- end CONCEPTO_ADICIONAL
-- begin INCREMENTO
create table INCREMENTO (
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
    CONCEPTO_RECIBO_ATRASOS_ID varchar(32),
    FECHA_INCREMENTO date,
    IMPORTE double precision,
    MESES_ATRASOS integer,
    IMPORTE_ATRASOS double precision,
    MESES_REPERCUSION_ATRASOS integer,
    RM2ID integer,
    --
    -- from test1_IncrementoIndice
    NOMBRE_TIPO varchar(255),
    MES_ANNO_INDICE varchar(255),
    VALOR_INDICE double precision,
    INDICE_ANTERIOR double precision,
    VALOR_BASE double precision,
    VALOR_INDICE_PORCENTUAL double precision,
    --
    -- from test1_IncrementoGeneralObras
    TIPO_COEFICIENTE_ID varchar(32),
    CONCEPTO_ID varchar(32),
    VALOR_COEFICIENTE double precision,
    IMPORTE_GLOGAL_INCREMENTO double precision,
    UBICACION_ID varchar(32),
    MODO_REPARTICION integer,
    --
    primary key (ID)
)^
-- end INCREMENTO
-- begin FIANZA
create table FIANZA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CONTRATO_INQUILINO_ID varchar(32),
    ES_AVAL_BANCARIO boolean,
    FECHA_ABONO_FIANZA date,
    FIANZA_COMPLEMENTARIA double precision,
    FIANZA_LEGAL double precision,
    IDENTIFICADOR_AVAL varchar(255),
    OBSERVACIONES longtext,
    ESCANEO_ARCHIVO_ADJUNTO_ID varchar(32),
    ESTADO_FIANZA integer,
    TIENE_POLIZA_ALQUILER boolean,
    NUMERO_POLIZA varchar(255),
    INFORMACION_DE_CONTACTO_POLIZA varchar(255),
    ESCANEO_SEGURO_ARCHIVO_ADJUNTO_ID varchar(32),
    FECHA_INGRESO_FIANZA_EN_CAMARA date,
    FECHA_RESCATE_FIANZA_DE_CAMARA date,
    ESCANEO_FIANZA_ID varchar(32),
    --
    primary key (ID)
)^
-- end FIANZA
-- begin CONCEPTO_RECIBO
create table CONCEPTO_RECIBO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CONCEPTO_ID varchar(32),
    RM2ID integer,
    IMPORTE double precision,
    PROGRAMACION_RECIBO_ID varchar(32),
    VIGENCIA integer,
    ACTIVADO_DESACTIVADO boolean,
    FECHA_DESDE date,
    FECHA_HASTA date,
    NUM_EMISIONES integer,
    ES_MODIFICACION_AGREGADA boolean,
    FECHA_VALOR date,
    DESCRIPCION_CAUSA varchar(255),
    ESTADO_NOTIFICACION integer,
    ACTUALIZABLE_IPC boolean,
    INCREMENTO_ID varchar(32),
    OMITIR_EN_PRORRATEO boolean,
    --
    primary key (ID)
)^
-- end CONCEPTO_RECIBO
-- begin CLAUSULA
create table CLAUSULA (
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
    RMI2 integer,
    --
    primary key (ID)
)^
-- end CLAUSULA
-- begin SECCION
create table SECCION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    MODELO_CONTRATO_ID varchar(32),
    NOMBRE varchar(255),
    DESCRIPCION varchar(255),
    OBLIGATORIA boolean,
    ORDENACION integer,
    --
    primary key (ID)
)^
-- end SECCION
-- begin PROGRAMACION_RECIBO
create table PROGRAMACION_RECIBO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PROPIETARIO_ES_EMISOR boolean,
    TIPO_COBRO integer,
    CONTRATO_INQUILINO_ID varchar(32),
    CUENTA_BANCARIA_INQUILINO_ID varchar(32),
    DEFINICION_REMESA_ID varchar(32),
    DESACTIVAR_PROGRAMACION boolean,
    APLICAR_IPC_NEGATIVO boolean,
    CUENTA_BANCARIA_PAGADOR_ID varchar(32),
    PRORRATEO_PROXIMA_EMISION double precision,
    RM2ID integer,
    --
    primary key (ID)
)^
-- end PROGRAMACION_RECIBO
-- begin CONTRATO_INQUILINO
create table CONTRATO_INQUILINO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CICLO_ID varchar(32),
    ARRENDATARIO_SIN_REPRESENTACION boolean,
    ESTADO_CONTRATO integer,
    FECHA_DESOCUPACION date,
    FECHA_OCUPACION date,
    FECHA_REALIZACION date,
    FECHA_VENCIMIENTO_PREVISTO date,
    FORMA_DE_COBRO integer,
    LUGAR_REALIZACION varchar(255),
    MES_ANYO_APLICACION_IPC varchar(50),
    NUMERO_CONTRATO varchar(255),
    OMITIR_IPC_NEGATIVO boolean,
    PERIODO_ACTUALIZACION_IPC integer,
    PLAZO_ANYOS integer,
    PLAZO_ANYOS_PRORROGABLE integer,
    RENTA_CONTRACTUAL double precision,
    TIPO_CONTRATO integer,
    USO_CONTRATO integer,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    DETALLE_ENTREGA_LLAVES longtext,
    PAGADOR_ID varchar(32),
    DEPARTAMENTO_ID varchar(32),
    ESCANEO_CONTRATO_ID varchar(32),
    IMPLEMENTACION_MODELO_ID varchar(32),
    INQUILINO_ID varchar(32),
    REPRESENTANTE_ARRENDADOR_ID varchar(32),
    REPRESENTANTE_ARRENDATARIO_ID varchar(32),
    NOMBRE_TIPO_INDICE_INCREMENTOS varchar(10),
    EL_PAGADOR_ES_EL_TITULAR boolean,
    PERMITIR_APUNTES_PROCESOS_AGREGADOS boolean,
    COMERCIAL_OFERTAS_ID varchar(32),
    EXCLUIR_MONITORIZACION_PTES_DEV boolean,
    DEVENTO_COSTES_POSTALES boolean,
    RECIBOS_INCOBRABLES boolean,
    FECHA_MANDATO date,
    REFERENCIA_MANDATO varchar(255),
    COMUNICACION_RENUNCIA boolean,
    FECHA_PREVISTA_RENUNCIA date,
    OFERTA_ID varchar(32),
    ARCHIVO_ADJUNTO_RENUNCIA_ID varchar(32),
    OBSERVACIONES_RENUNCIA longtext,
    COMENTARIOS_ADMINISTRADOR longtext,
    FECHA_COMUNICACION date,
    CARPETA_DOCUMENTO_FOTOGRAFICO_FIRMA_ID varchar(32),
    RM2ID integer,
    --
    primary key (ID)
)^
-- end CONTRATO_INQUILINO
-- begin NOTIFICACION_RECIBO
create table NOTIFICACION_RECIBO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_DESDE date,
    FECHA_HASTA date,
    CONTENIDO longtext,
    PROGRAMACION_RECIBO_ID varchar(32),
    CONCEPTO_RECIBO_ID varchar(32),
    --
    primary key (ID)
)^
-- end NOTIFICACION_RECIBO
-- begin CONCEPTO_ADICIONAL_CONCEPTO_RECIBO
create table CONCEPTO_ADICIONAL_CONCEPTO_RECIBO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CA_ID varchar(32),
    CONCEPTO_RECIBO_ID varchar(32),
    PORCENTAJE double precision,
    --
    primary key (ID)
)^
-- end CONCEPTO_ADICIONAL_CONCEPTO_RECIBO
-- begin COTITULAR_CONTRATO_INQUILINO
create table COTITULAR_CONTRATO_INQUILINO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CONTRATO_INQUILINO_ID varchar(32),
    COTITULAR_ID varchar(32),
    --
    primary key (ID)
)^
-- end COTITULAR_CONTRATO_INQUILINO
-- begin TEST1_REPRESENTACION_LEGAL
create table TEST1_REPRESENTACION_LEGAL (
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
    PERSONA_REPRESENTANTE_ID varchar(32) not null,
    EN_CALIDAD_DE varchar(50),
    --
    primary key (ID)
)^
-- end TEST1_REPRESENTACION_LEGAL
-- begin TEST1_COEFICIENTE
create table TEST1_COEFICIENTE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TIPO_COEFICIENTE_ID varchar(32),
    VALOR double precision,
    DEPARTAMENTO_ID varchar(32),
    --
    primary key (ID)
)^
-- end TEST1_COEFICIENTE
-- begin ARCHIVO_ADJUNTO
create table ARCHIVO_ADJUNTO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    EXT_ID integer,
    REPRESENTACION_SERIAL longblob,
    NOMBRE_ARCHIVO varchar(255),
    NOMBRE_ARCHIVO_ORIGINAL varchar(255),
    DESCRIPCION varchar(255),
    EXTENSION varchar(50),
    MIME_TYPE varchar(100),
    TAMANO integer,
    --
    primary key (ID)
)^
-- end ARCHIVO_ADJUNTO
-- begin IMPUTACION_DOCUMENTO_IMPUTABLE
create table IMPUTACION_DOCUMENTO_IMPUTABLE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DESCRIPCION_IMPUTACION varchar(255),
    VALIDACION_IMPUTACION_ID varchar(32),
    IMPORTE_IMPUTACION double precision,
    IMPUTACION_INDEFINIDOS boolean,
    INFORMACION_ADICIONAL varchar(255),
    PORCENTAJE_IMPUTACION double precision,
    DOCUMENTO_IMPUTABLE_ID varchar(32),
    CICLO_ID varchar(32),
    EVENTO_ID varchar(32),
    RM2ID integer,
    --
    primary key (ID)
)^
-- end IMPUTACION_DOCUMENTO_IMPUTABLE
-- begin NOTA_INTERVENCION
create table NOTA_INTERVENCION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_PREVISTA_INTERVENCION date,
    CONTENIDO longtext,
    FRANJA_HORARIA varchar(100),
    FECHA_INTERVENCION date,
    GRADO_EXITO_INTERVENCION integer,
    ORDEN_TRABAJO_ID varchar(32),
    ENVIO_NOTIFICACION_INQUILINO boolean,
    ENVIO_NOTIFICACION_PROPIETARIO boolean,
    AMPLIACION_NOTIFICACION_INQUILINO varchar(255),
    AMPLIACION_NOTIFICACION_PROPIETARIO varchar(255),
    ENVIADO_A_INQUILINO boolean,
    ENVIADO_A_PROPIETARIO boolean,
    COORDINAR_INTERVENCION boolean,
    --
    primary key (ID)
)^
-- end NOTA_INTERVENCION
-- begin CEDULA_HABITABILIDAD
create table CEDULA_HABITABILIDAD (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_EMISION date,
    FECHA_VENCIMIENTO date,
    NUMERO_CEDULA varchar(100),
    OBSERVACIONES longtext,
    DEPARTAMENTO_ID varchar(32),
    ESCANEO_CEDULA_ID varchar(32),
    --
    primary key (ID)
)^
-- end CEDULA_HABITABILIDAD
-- begin MODELO_CONTRATO
create table MODELO_CONTRATO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_MODELO varchar(255),
    DESCRIPCION varchar(255),
    INQUILINO_PROPIETARIO varchar(50),
    RM2ID integer,
    --
    primary key (ID)
)^
-- end MODELO_CONTRATO
-- begin TEST1_FLEX_REPORT
create table TEST1_FLEX_REPORT (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FIELDS varchar(255),
    FORZAR_REPORT_DE_UN_SOLO_REGISTRO_VACIO boolean,
    CONTENIDO_JRXML longtext,
    MODO_PRODUCTOR varchar(50),
    NOMBRE varchar(255),
    PARAMETROS_MANUALES longtext,
    PRODUCTOR longtext,
    TIPO varchar(50),
    PARAMETROS_PRODUCTOR longtext,
    RUTA varchar(255),
    USER_DATA_SOURCE_CONNECTION boolean,
    --
    primary key (ID)
)^
-- end TEST1_FLEX_REPORT
-- begin PARAMETRO_VALOR_ANEXO
create table PARAMETRO_VALOR_ANEXO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_PARAMETRO varchar(255),
    VALOR varchar(255),
    ANEXO_ID varchar(32),
    --
    primary key (ID)
)^
-- end PARAMETRO_VALOR_ANEXO
-- begin TEST1_ORDEN_COBRO
create table TEST1_ORDEN_COBRO (
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
    IMPORTE double precision,
    DESCRIPCION varchar(255),
    REALIZACION_COBRO_ID varchar(32),
    RECIBO_ID varchar(32),
    ENT_TO_ENT_ID varchar(255),
    --
    primary key (ID)
)^
-- end TEST1_ORDEN_COBRO
-- begin SERIE
create table SERIE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_SERIE varchar(100),
    RM2ID integer,
    DESCRIPCION varchar(255),
    MASTER_SERIE_ID varchar(32),
    --
    primary key (ID)
)^
-- end SERIE
-- begin ORDEN_TRABAJO
create table ORDEN_TRABAJO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    ENTRADA_ID varchar(32),
    CARPETA_DOCUMENTOS_FOTOGRAFICOS_ID varchar(32),
    FECHA_SOLICITUD date,
    FECHA_PREVISTA_INTERVENCION date,
    COSTE_ORIENTATIVO varchar(100),
    PROPUESTO_POR varchar(255),
    DESCRIPCION longtext,
    OBSERVACION longtext,
    OBSERVACION_INTERVENCION longtext,
    DURACION_PREVISTA_INTERVENCION varchar(100),
    FRANJA_HORARIA_INTERVENCION varchar(100),
    FECHA_FINALIZACION date,
    FECHA_FINALIZACION_ESTIMADA date,
    EXCLUIR_DE_MONITORIZACION_ENCARGADO boolean,
    TIPO_PRIVACIDAD integer,
    ADMINISTRADOR_ID varchar(32),
    REPARACION_REHABILITACION boolean,
    RM2ID integer,
    --
    primary key (ID)
)^
-- end ORDEN_TRABAJO
-- begin REMESA
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
    TOTAL_REMESA double precision,
    --
    primary key (ID)
)^
-- end REMESA
-- begin TEST1_REALIZACION_COBRO
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
    CUENTA_BANCARIA_ID varchar(32),
    XSD longtext,
    SEPA longtext,
    IDENTIFICADOR varchar(255),
    IMPORTE_TOTAL double precision,
    RM2ID integer,
    --
    primary key (ID)
)^
-- end TEST1_REALIZACION_COBRO
-- begin ORDENANTE_REMESA
create table ORDENANTE_REMESA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DEFINICION_REMESA_ID varchar(32),
    REMESA_ID varchar(32),
    RM2ID integer,
    --
    primary key (ID)
)^
-- end ORDENANTE_REMESA
-- begin CICLO
create table CICLO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CODIGO_CICLO varchar(50),
    DESCRIPCION longtext,
    ES_CICLO_CORRIENTE boolean,
    ESTADO_CICLO integer,
    FECHA_CICLO date,
    OBSERVACIONES longtext,
    TITULO_CICLO varchar(255),
    COLECCION_ADJUNTOS_ID varchar(32),
    DEPARTAMENTO_ID varchar(32),
    TIPO_CICLO varchar(50),
    CONTRATO_INQUILINO_ID varchar(32),
    EXCLUIR_DE_MONITORIZACION_PARA_BUSQUEDA_ORDENES_TRABAJO boolean,
    RM2ID integer,
    --
    primary key (ID)
)^
-- end CICLO
-- begin FOTO_DOCUMENTO_FOTOGRAFICO
create table FOTO_DOCUMENTO_FOTOGRAFICO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DESCRIPCION varchar(255),
    EXTENSION varchar(10),
    MIME_TYPE varchar(100),
    NOMBRE_ARCHIVO varchar(255),
    NOMBRE_ARCHIVO_ORIGINAL varchar(255),
    TAMANO integer,
    CARPETA_ID varchar(32),
    REPRESENTACION_SERIAL longblob,
    EXT_ID integer,
    FOTO_THUMBNAIL_ID varchar(32),
    --
    primary key (ID)
)^
-- end FOTO_DOCUMENTO_FOTOGRAFICO
-- begin PROGRAMACION_CONCEPTO_ADICIONAL
create table PROGRAMACION_CONCEPTO_ADICIONAL (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CONCEPTO_ADICIONAL_ID varchar(32),
    PROVEEDOR_ID varchar(32),
    PROGRAMACION_RECIBO_ID varchar(32),
    --
    primary key (ID)
)^
-- end PROGRAMACION_CONCEPTO_ADICIONAL
-- begin EVENTO
create table EVENTO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CICLO_ID varchar(32),
    NOMBRE varchar(255),
    FECHA date,
    ARCHIVADO boolean,
    TIPO_EVENTO integer,
    RM2ID integer,
    --
    primary key (ID)
)^
-- end EVENTO
-- begin TEST1_TIPO_COEFICIENTE
create table TEST1_TIPO_COEFICIENTE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE varchar(255),
    DESCRIPCION varchar(255),
    VALOR_MIN double precision,
    VALOR_MAX double precision,
    RM2ID integer,
    --
    primary key (ID)
)^
-- end TEST1_TIPO_COEFICIENTE
-- begin CTA_DOCTOS_FOTOGRAFICOS
create table CTA_DOCTOS_FOTOGRAFICOS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_CARPETA varchar(255),
    CICLO_ID varchar(32),
    EVENTO_ID varchar(32),
    APORTADAS_POR varchar(255),
    DESCRIPCION varchar(255),
    NUMERO_DE_FOTOGRAFIAS integer,
    ACCESIBLE_PARA_COMERCIALES boolean,
    LIQUIDACION_EXTINCION_ID varchar(32),
    RM2ID integer,
    --
    primary key (ID)
)^
-- end CTA_DOCTOS_FOTOGRAFICOS
-- begin UBICACION
create table UBICACION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE varchar(255) not null,
    DIRECCION_ID varchar(32),
    ABREVIACION_UBICACION varchar(10),
    ES_PROPIEDAD_VERTICAL boolean,
    LATITUD double precision,
    LONGITUD double precision,
    NUM_ASCENSORES integer,
    PROPIETARIO_ID varchar(32),
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    INFORMACION_CATASTRAL varchar(50),
    FOTO_FACHADA_ID varchar(32),
    NOMBRE_DISTRITO varchar(255),
    RM2ID integer,
    --
    primary key (ID)
)^
-- end UBICACION
-- begin ASIGNACION_TAREA
create table ASIGNACION_TAREA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PROVEEDOR_ID varchar(32),
    GESTION_PRESUPUESTARIA varchar(50),
    ORDEN_TRABAJO_ID varchar(32),
    FECHA_PREVISTA date,
    FECHA_FINALIZACION date,
    DESCRIPCION longtext,
    CANCELADO boolean,
    FECHA_PREVISTO_INICIO date,
    ACORDADO_CON_INDUSTRIAL boolean,
    COMENTARIOS_INDUSTRIAL longtext,
    --
    primary key (ID)
)^
-- end ASIGNACION_TAREA
-- begin RECIBO
create table RECIBO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    AMPLIACION varchar(255),
    FECHA_EMISION date,
    TOTAL_RECIBO double precision,
    TOTAL_RECIBO_POST_CCAA double precision,
    PROGRAMACION_RECIBO_ID varchar(32),
    NUM_RECIBO varchar(100),
    FECHA_VALOR date,
    ORDENANTE_REMESA_ID varchar(32),
    SERIE_ID varchar(32),
    UTILITARIO_CONTRATO_INQUILINO_ID varchar(32),
    GRADO_ESTADO_IMPAGO integer,
    FECHA_ESTADO_PENDIENTE date,
    FECHA_ESTADO_INCOBRABLE date,
    UTILITARIO_INQUILINO_ID varchar(32),
    RM2ID integer,
    --
    primary key (ID)
)^
-- end RECIBO
-- begin RECIBO_COBRADO
create table RECIBO_COBRADO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    AMPLIACION_INGRESO varchar(255),
    DESCRIPCION varchar(255),
    FECHA_COBRO date,
    MODO_INGRESO integer,
    TOTAL_INGRESO double precision,
    COBRANZAS double precision,
    RECIBO_ID varchar(32),
    --
    primary key (ID)
)^
-- end RECIBO_COBRADO
-- begin LIQUIDACION_EXTINCION
create table LIQUIDACION_EXTINCION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TOTALES_GARANTIAS double precision,
    TOTALES_RECIBOS_PENDIENTES double precision,
    TOTALES_INDEMNIZACIONES double precision,
    POR_LIQUIDAR double precision,
    ESCANEO_LIQUIDACION_ID varchar(32),
    CONTRATO_INQUILINO_ID varchar(32),
    DETALLE longtext,
    IMPORTE_AVAL_EJECUTADO double precision,
    CANTIDADES_ENTREGADAS_EN_RESERVA double precision,
    CONFORMIDAD_ADMINISTRADOR boolean,
    RETENCION_CDF_ID varchar(32),
    CANTIDADES_A_CUENTA_LIQUIDACION double precision,
    FECHA_CANTIDADES_A_CUENTA_LIQUIDACION date,
    FECHA_LIQUIDACION date,
    --
    primary key (ID)
)^
-- end LIQUIDACION_EXTINCION
-- begin COMERCIAL_OFERTAS
create table COMERCIAL_OFERTAS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PROVEEDOR_ID varchar(32),
    RM2ID integer,
    DETALLE_CORREOS_ELECTRONICOS longtext,
    DETALLE_NOMBRES longtext,
    EXCLUIR_ENVIOS boolean,
    PUEDE_DAR_ALTA_O_BAJA_AGENTES boolean,
    ESCANEO_ACEPTACION_ID varchar(32),
    XML_COMERCIAL_VISITAS longtext,
    --
    primary key (ID)
)^
-- end COMERCIAL_OFERTAS
-- begin ORDEN_PAGO
create table ORDEN_PAGO (
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
    EMISOR_PERSONA_ID varchar(32),
    BENEFICIARIO_PERSONA_ID varchar(32),
    FECHA_VALOR date,
    IMPORTE double precision,
    IMPORTE_EFECTIVO double precision,
    DESCRIPCION varchar(255),
    REALIZACION_PAGO_ID varchar(32),
    RM2ID integer,
    --
    -- from test1_OrdenPagoContratoInquilino
    CONTRATO_INQUILINO_ID varchar(32),
    --
    -- from test1_OrdenPagoFacturaProveedor
    ES_ABONO boolean,
    FACTURA_PROVEEDOR_ID varchar(32),
    --
    -- from test1_OrdenPagoAbono
    PROVEEDOR_AB_ID varchar(32),
    FACTURA_PROVEEDOR_AB_ID varchar(32),
    --
    -- from test1_OrdenPagoProveedor
    PROVEEDOR_ID varchar(32),
    ES_COMPENSACION_ABONO boolean,
    --
    primary key (ID)
)^
-- end ORDEN_PAGO
-- begin TEST1_UBICACION_COEFICIENTE
create table TEST1_UBICACION_COEFICIENTE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    UBICACION_ID varchar(32),
    TIPO_COEFICIENTE_ID varchar(32),
    TOTAL_TIPO_COEFICIENTE double precision,
    --
    primary key (ID)
)^
-- end TEST1_UBICACION_COEFICIENTE
-- begin TEST1_REGISTRO_PRESENCIAL
create table TEST1_REGISTRO_PRESENCIAL (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    USUARIO_ID varchar(32),
    TIMESTAMP time(3) not null,
    ACCION varchar(255) not null,
    UBICACION varchar(255),
    --
    primary key (ID)
)^
-- end TEST1_REGISTRO_PRESENCIAL
-- begin VALIDACION
create table VALIDACION (
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
    PROPIETARIO_ID varchar(32),
    ESTADO_VALIDACION integer,
    FECHA_APROBACION_RECHAZO date,
    RM2ID integer,
    --
    primary key (ID)
)^
-- end VALIDACION
-- begin DEFINICION_REMESA
create table DEFINICION_REMESA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_REMESA varchar(255),
    PROPIETARIO_ID varchar(32),
    DESCRIPCION varchar(255),
    UNIDAD_PERIODICIDAD integer,
    CANTIDAD_PERIODICIDAD integer,
    TIPO_GIRO integer,
    CUENTA_BANCARIA_ID varchar(32),
    MODO_PRESENTACION integer,
    DELEGADO_ID varchar(32),
    RM2ID integer,
    --
    primary key (ID)
)^
-- end DEFINICION_REMESA
-- begin TEST1_DOCUMENTACION_INQUILINO
create table TEST1_DOCUMENTACION_INQUILINO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DNI varchar(255),
    NOMBRE_COMPLETO varchar(255),
    OBJETO_CANIDADO varchar(255),
    INFORMACION_DE_CONTACTO varchar(255),
    PRESENTACION longtext,
    FECHA_REGISTRO date,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    CONTRATO_INQUILINO_ID varchar(32),
    --
    primary key (ID)
)^
-- end TEST1_DOCUMENTACION_INQUILINO
-- begin ENTRADA
create table ENTRADA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CONTENIDO_ENTRADA longtext,
    RM2ID integer,
    FECHA_ENTRADA date,
    CICLO_ID varchar(32),
    EVENTO_ID varchar(32),
    ENTERO_RECORDATORIO integer,
    --
    primary key (ID)
)^
-- end ENTRADA
-- begin OVERRIDE_CLAUSULA
create table OVERRIDE_CLAUSULA (
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
    VERSION_APLICADA_ID varchar(32),
    IMPLEMENTACION_MODELO_ID varchar(32),
    --
    primary key (ID)
)^
-- end OVERRIDE_CLAUSULA
-- begin PROPIETARIO
create table PROPIETARIO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    ABREVIACION_CONTRATOS varchar(25),
    CODIGO_CLIENTE varchar(50),
    EXONERACION_IRPF boolean,
    GESTION_CAJA boolean,
    PERSONA_ID varchar(32),
    CABECERA_DOCUMENTOS_ARCHIVO_ADJUNTO_ID varchar(32),
    CUENTA_BANCARIA_ID varchar(32),
    PROSPECTO boolean,
    RM2ID integer,
    --
    primary key (ID)
)^
-- end PROPIETARIO
-- begin REALIZACION_PAGO
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
    MARCA_PAGADO_ADMINISTRADOR boolean,
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
)^
-- end REALIZACION_PAGO
-- begin COMP_OP_PROVEEDOR
create table COMP_OP_PROVEEDOR (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    OP_PROVEEDOR_ID varchar(32),
    ORDEN_PAGO_ABONO_ID varchar(32),
    ORDEN_PAGO_FACTURA_PROVEEDOR_ID varchar(32),
    IMPORTE double precision,
    --
    primary key (ID)
)^
-- end COMP_OP_PROVEEDOR
-- begin TEST1_REGISTRO_INDICE_REFERENCIA
create table TEST1_REGISTRO_INDICE_REFERENCIA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    MES integer,
    ANNO integer,
    VALOR double precision,
    NOMBRE_TIPO varchar(50),
    REALIZADO_POR_PERSONA_ID varchar(32),
    --
    primary key (ID)
)^
-- end TEST1_REGISTRO_INDICE_REFERENCIA
-- begin PLANTILLA
create table PLANTILLA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_PLANTILLA varchar(255),
    CONTENIDO_PLANTILLA longtext,
    TIPO_PLANTILLA varchar(50),
    DE_SISTEMA boolean,
    RUTA varchar(255),
    RM2ID integer,
    --
    primary key (ID)
)^
-- end PLANTILLA
-- begin CERTIFICADO_CALIFICACION_ENERGETICA
create table CERTIFICADO_CALIFICACION_ENERGETICA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NUMERO_REGISTRO varchar(255),
    ARCHIVO_ADJUNTO_ID varchar(32),
    FECHA_VENCIMIENTO date,
    OBSERVACIONES longtext,
    DEPARTAMENTO_ID varchar(32),
    CALIFICACION_EMISIONES varchar(5),
    CALIFICACION_CONSUMO_ENERGETICO varchar(5),
    --
    primary key (ID)
)^
-- end CERTIFICADO_CALIFICACION_ENERGETICA
-- begin TEST1_RESERVA
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
)^
-- end TEST1_RESERVA
-- begin TEST1_REGISTRO_CONTRASENA
create table TEST1_REGISTRO_CONTRASENA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TITULO varchar(255) not null,
    CONTENIDO longtext not null,
    PRIVADO_PUBLICO boolean not null,
    USUARIO_ID varchar(32) not null,
    --
    primary key (ID)
)^
-- end TEST1_REGISTRO_CONTRASENA
-- begin TEST1_MAILING_LIST
create table TEST1_MAILING_LIST (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE varchar(255),
    DESCRIPCION longtext,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32) not null,
    COMMA_SEPARATED_MAILING_LIST longtext,
    --
    primary key (ID)
)^
-- end TEST1_MAILING_LIST
-- begin TEST1_INVERSION
create table TEST1_INVERSION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TITULO varchar(255),
    TIPO varchar(255),
    FECHA_ENTRADA date not null,
    DIRECCION1 varchar(255),
    DIRECCION2 varchar(255),
    LINK_MAPS varchar(255),
    MEDIADOR_ID varchar(32) not null,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    EXPOSICION longtext,
    TOTAL_EDIFICABILIDAD_SOBRE_RASANTE_M2 varchar(255),
    TOTAL_EDIFICABILIDAD_BAJO_RASANTE_M2 varchar(255),
    PRECIO_INICIAL double precision,
    --
    primary key (ID)
)^
-- end TEST1_INVERSION
-- begin PUNTO_ENTRADA_DOCUMENTOS
create table PUNTO_ENTRADA_DOCUMENTOS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TITULO varchar(255),
    TIPO varchar(25),
    DESCRIPCION longtext,
    PROPIEDADES_JSON longtext,
    --
    primary key (ID)
)^
-- end PUNTO_ENTRADA_DOCUMENTOS
-- begin TEST1_POLIZA_DE_SEGUROS
create table TEST1_POLIZA_DE_SEGUROS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    IDENTIFICADOR_POLIZA varchar(255),
    DESCRIPCION_ABREVIADA_RIESGO varchar(255),
    DESCRIPCION_AMPLIADA_RIESGO longtext,
    PRIMERA_FECHA_INICIAL date,
    FECHA_RESCISION date,
    ESCANEO_POLIZA_ID varchar(32),
    DATOS_DE_CONTACTO_BROKER_Y_CIA longtext,
    COMPANIA_ASEGURADORA varchar(255),
    BROKER varchar(255),
    --
    primary key (ID)
)^
-- end TEST1_POLIZA_DE_SEGUROS
-- begin TEST1_SINIESTRO
create table TEST1_SINIESTRO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_SINIESTRO date,
    DESCRIPCION_SINIESTRO longtext,
    DATOS_DE_CONTACTO_PARTES longtext,
    SINIESTRO_CERRADO boolean,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    DIARIO_DE_SINIESTRO longtext,
    POLIZA_DE_SEGUROS_ID varchar(32),
    --
    primary key (ID)
)^
-- end TEST1_SINIESTRO
