alter table INCREMENTO add constraint FK_INCREMENTO_ON_UBICACION foreign key (UBICACION_ID) references UBICACION(ID);
create index IDX_INCREMENTO_ON_UBICACION on INCREMENTO (UBICACION_ID);
