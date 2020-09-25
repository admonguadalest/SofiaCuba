-- update TEST1_REPRESENTACION_LEGAL set PERSONA_ID = <default_value> where PERSONA_ID is null ;
alter table TEST1_REPRESENTACION_LEGAL modify column PERSONA_ID varchar(32) not null ;
