alter table OFERTA add column IMPORTE_RENTA double precision ^
update OFERTA set IMPORTE_RENTA = 0 where IMPORTE_RENTA is null ;
alter table OFERTA modify column IMPORTE_RENTA double precision not null ;
-- alter table OFERTA add column DEPARTAMENTO_ID varchar(32) ^
-- update OFERTA set DEPARTAMENTO_ID = <default_value> ;
-- alter table OFERTA modify column DEPARTAMENTO_ID varchar(32) not null ;
alter table OFERTA add column DEPARTAMENTO_ID varchar(32) not null ;
alter table OFERTA add column OBSERVACIONES longtext ;
