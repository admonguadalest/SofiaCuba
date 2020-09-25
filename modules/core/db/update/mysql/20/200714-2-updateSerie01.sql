alter table SERIE add constraint FK_SERIE_ON_MASTER_SERIE foreign key (MASTER_SERIE_ID) references SERIE(ID);
create index IDX_SERIE_ON_MASTER_SERIE on SERIE (MASTER_SERIE_ID);
