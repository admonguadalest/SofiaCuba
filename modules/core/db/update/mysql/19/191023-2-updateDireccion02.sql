alter table DIRECCION add constraint FK_DIRECCION_ON_PERSONA foreign key (PERSONA_ID) references PERSONA(ID);
create index IDX_DIRECCION_ON_PERSONA on DIRECCION (PERSONA_ID);
