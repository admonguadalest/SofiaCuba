alter table COTITULAR_CONTRATO_INQUILINO add constraint FK_COTITULAR_CONTRATO_INQUILINO_ON_COTITULAR foreign key (COTITULAR_ID) references PERSONA(ID);
create index IDX_COTITULAR_CONTRATO_INQUILINO_ON_COTITULAR on COTITULAR_CONTRATO_INQUILINO (COTITULAR_ID);
