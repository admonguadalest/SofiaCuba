alter table ENTRADA add constraint FK_ENTRADA_ON_EVENTO foreign key (EVENTO_ID) references EVENTO(ID);
create index IDX_ENTRADA_ON_EVENTO on ENTRADA (EVENTO_ID);
