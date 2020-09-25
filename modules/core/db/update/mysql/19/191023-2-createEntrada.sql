alter table ENTRADA add constraint FK_ENTRADA_ON_CICLO foreign key (CICLO_ID) references CICLO(ID);
alter table ENTRADA add constraint FK_ENTRADA_ON_EVENTO foreign key (EVENTO_ID) references EVENTO(ID);
create index IDX_ENTRADA_ON_CICLO on ENTRADA (CICLO_ID);
create index IDX_ENTRADA_ON_EVENTO on ENTRADA (EVENTO_ID);
