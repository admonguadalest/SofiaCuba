alter table TEST1_UBICACION add constraint FK_TEST1_UBICACION_ON_COLECCION_ARCHIVOS_ADJUNTOS foreign key (COLECCION_ARCHIVOS_ADJUNTOS_ID) references TEST1_COLECCION_ARCHIVOS_ADJUNTOS(ID);
create index IDX_TEST1_UBICACION_ON_COLECCION_ARCHIVOS_ADJUNTOS on TEST1_UBICACION (COLECCION_ARCHIVOS_ADJUNTOS_ID);
