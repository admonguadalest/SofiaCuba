alter table CLAUSULA add constraint FK_CLAUSULA_ON_SECCION foreign key (SECCION_ID) references SECCION(ID);
create index IDX_CLAUSULA_ON_SECCION on CLAUSULA (SECCION_ID);
