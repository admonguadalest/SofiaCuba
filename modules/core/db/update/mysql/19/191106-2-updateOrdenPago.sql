alter table ORDEN_PAGO change column REALIZACION_PAGO_ID REALIZACION_PAGO_ID__U55041 varchar(32)^
drop index IDX_ORDEN_PAGO_ON_REALIZACION_PAGO on ORDEN_PAGO ;
alter table ORDEN_PAGO add column REALIZACION_PAGO_ID varchar(32) ;
alter table ORDEN_PAGO add column ES_ABONO boolean ;
alter table ORDEN_PAGO add column FACTURA_PROVEEDOR_ID varchar(32) ;
alter table ORDEN_PAGO add column FACTURA_PROVEEDOR_AB_ID varchar(32) ;
alter table ORDEN_PAGO add column PROVEEDOR_AB_ID varchar(32) ;
alter table ORDEN_PAGO add column RM2ID integer ;
