alter table DOCUMENTO_IMPUTABLE add constraint FK_DOCUMENTO_IMPUTABLE_ON_PROVEEDOR foreign key (PROVEEDOR_ID) references PROVEEDOR(ID);
create index IDX_DOCUMENTO_IMPUTABLE_ON_PROVEEDOR on DOCUMENTO_IMPUTABLE (PROVEEDOR_ID);
