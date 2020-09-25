alter table TEST1_UBICACION change column DIRECCION_ID DIRECCION_ID__U30157 int^
alter table TEST1_UBICACION modify column DIRECCION_ID__U30157 int null ;
drop index IDX_TEST1_UBICACION_ON_DIRECCION on TEST1_UBICACION ;
alter table TEST1_UBICACION add column DIRECCION_NUMERO varchar(255) ;
alter table TEST1_UBICACION add column DIRECCION_CP varchar(255) ;
alter table TEST1_UBICACION add column DIRECCION_NOMBRE varchar(255) ;
