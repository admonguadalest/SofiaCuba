alter table DEFINICION_REMESA add constraint FK_DEFINICION_REMESA_ON_CUENTA_BANCARIA foreign key (CUENTA_BANCARIA_ID) references CUENTA_BANCARIA(ID);
create index IDX_DEFINICION_REMESA_ON_CUENTA_BANCARIA on DEFINICION_REMESA (CUENTA_BANCARIA_ID);
