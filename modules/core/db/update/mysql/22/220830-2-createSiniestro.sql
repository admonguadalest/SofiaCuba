alter table TEST1_SINIESTRO add constraint FK_TEST1_SINIESTRO_ON_COLECCION_ARCHIVOS_ADJUNTOS foreign key (COLECCION_ARCHIVOS_ADJUNTOS_ID) references COLECCION_ARCHIVOS_ADJUNTOS(ID);
alter table TEST1_SINIESTRO add constraint FK_TEST1_SINIESTRO_ON_POLIZA_DE_SEGUROS foreign key (POLIZA_DE_SEGUROS_ID) references TEST1_POLIZA_DE_SEGUROS(ID);
create index IDX_TEST1_SINIESTRO_ON_COLECCION_ARCHIVOS_ADJUNTOS on TEST1_SINIESTRO (COLECCION_ARCHIVOS_ADJUNTOS_ID);
create index IDX_TEST1_SINIESTRO_ON_POLIZA_DE_SEGUROS on TEST1_SINIESTRO (POLIZA_DE_SEGUROS_ID);