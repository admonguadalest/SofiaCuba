alter table TEST1_EVENTO add constraint FK_TEST1_EVENTO_ON_CICLO foreign key (CICLO_ID) references TEST1_CICLO(ID);
create index IDX_TEST1_EVENTO_ON_CICLO on TEST1_EVENTO (CICLO_ID);
