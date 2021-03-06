alter table TEST1_ORDEN_TRABAJO add constraint FK_TEST1_ORDEN_TRABAJO_ON_ENTRADA foreign key (ENTRADA_ID) references TEST1_ENTRADA(ID);
alter table TEST1_ORDEN_TRABAJO add constraint FK_TEST1_ORDEN_TRABAJO_ON_CARPETA_DOCUMENTOS_FOTOGRAFICOS foreign key (CARPETA_DOCUMENTOS_FOTOGRAFICOS_ID) references TEST1_CARPETA_DOCUMENTOS_FOTOGRAFICOS(ID);
alter table TEST1_ORDEN_TRABAJO add constraint FK_TEST1_ORDEN_TRABAJO_ON_PROPUESTO_POR foreign key (PROPUESTO_POR_ID) references TEST1_PERSONA(ID);
alter table TEST1_ORDEN_TRABAJO add constraint FK_TEST1_ORDEN_TRABAJO_ON_ADMINISTRADOR foreign key (ADMINISTRADOR_ID) references SEC_USER(ID);
create index IDX_TEST1_ORDEN_TRABAJO_ON_ENTRADA on TEST1_ORDEN_TRABAJO (ENTRADA_ID);
create index IDX_TEST1_ORDEN_TRABAJO_ON_CARPETA_DOCUMENTOS_FOTOGRAFICOS on TEST1_ORDEN_TRABAJO (CARPETA_DOCUMENTOS_FOTOGRAFICOS_ID);
create index IDX_TEST1_ORDEN_TRABAJO_ON_PROPUESTO_POR on TEST1_ORDEN_TRABAJO (PROPUESTO_POR_ID);
create index IDX_TEST1_ORDEN_TRABAJO_ON_ADMINISTRADOR on TEST1_ORDEN_TRABAJO (ADMINISTRADOR_ID);
