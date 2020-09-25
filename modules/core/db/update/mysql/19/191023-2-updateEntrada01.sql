alter table ENTRADA add constraint FK_ENTRADA_ON_CICLO foreign key (CICLO_ID) references CICLO(ID);
create index IDX_ENTRADA_ON_CICLO on ENTRADA (CICLO_ID);
