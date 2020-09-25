alter table ORDEN_TRABAJO change column PROPUESTO_POR_ID PROPUESTO_POR_ID__U27621 varchar(32)^
alter table ORDEN_TRABAJO drop foreign key FK_ORDEN_TRABAJO_ON_PROPUESTO_POR;
drop index IDX_ORDEN_TRABAJO_ON_PROPUESTO_POR on ORDEN_TRABAJO ;
alter table ORDEN_TRABAJO change column FECHA_SOLICITUD FECHA_SOLICITUD__U00117 datetime^
alter table ORDEN_TRABAJO add column RM2ID integer ;
alter table ORDEN_TRABAJO add column PROPUESTO_POR varchar(255) ;
alter table ORDEN_TRABAJO add column FECHA_SOLICITUD date ;
