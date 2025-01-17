-- alter table TEST1_PUNTO_SUMINISTRO add column PROVEEDOR_ID varchar(32) ^
-- update TEST1_PUNTO_SUMINISTRO set PROVEEDOR_ID = <default_value> ;
-- alter table TEST1_PUNTO_SUMINISTRO modify column PROVEEDOR_ID varchar(32) not null ;
alter table TEST1_PUNTO_SUMINISTRO add column PROVEEDOR_ID varchar(32) not null ;
