alter table ORDEN_PAGO add constraint FK_ORDEN_PAGO_ON_REALIZACION_PAGO foreign key (REALIZACION_PAGO_ID) references REALIZACION_PAGO(ID);
create index IDX_ORDEN_PAGO_ON_REALIZACION_PAGO on ORDEN_PAGO (REALIZACION_PAGO_ID);
