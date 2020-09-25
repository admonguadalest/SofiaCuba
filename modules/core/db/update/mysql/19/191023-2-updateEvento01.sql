alter table EVENTO add constraint FK_EVENTO_ON_CICLO foreign key (CICLO_ID) references CICLO(ID);
create index IDX_EVENTO_ON_CICLO on EVENTO (CICLO_ID);
