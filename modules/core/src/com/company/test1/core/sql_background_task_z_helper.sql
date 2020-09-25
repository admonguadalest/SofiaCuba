insert into z_helper_proceso_recibos_informeiva
(ubicacion_id, departamento_id, recibo_id, inquilino_id, fechaEmision, numRecibo, nifDniInquilino,nombreCompleto,
nombre_ubicacion,piso, puerta, totalRecibo, totalReciboPostCCAA, importe, importePostCCAA, base, porcentajeIVA,
importeAplicadoIVA, porcentajeIRPF, importeAplicadoIRPF)

select
	UBICACION_ID,
    DEPARTAMENTO_ID,
    RECIBO_ID,
    INQUILINO_ID,
    FECHA_EMISION,
    NUM_RECIBO,
    NIF_DNI_INQUILINO,
    NOMBRE_COMPLETO,
    NOMBRE_UBICACION,
    PISO,
    PUERTA,
    TOTAL_RECIBO,
    TOTAL_RECIBO_POST_CCAA,
    IMPORTE,
    IMPORTE_POST_CCAA,
    BASE_,
    max(PORCENTAJE_IVA) as PORCENTAJE_IVA,
    max(IMPORTE_APLICADO_IVA) as IMPORTE_APLICADO_IVA,
    max(PORCENTAJE_IRPF) as PORCENTAJE_IRPF,
    max(IMPORTE_APLICADO_IRPF) as IMPORTE_APLICADO_IRPF

from
(
select distinct
    UBICACION_ID,
    DEPARTAMENTO_ID,
    RECIBO_ID,
    INQUILINO_ID,
    FECHA_EMISION,
    NUM_RECIBO,
    NIF_DNI_INQUILINO,
    NOMBRE_COMPLETO,
    NOMBRE_UBICACION,
    PISO,
    PUERTA,
    TOTAL_RECIBO,
    TOTAL_RECIBO_POST_CCAA,
    sum(round(IMPORTE,3)) as IMPORTE,
    sum(round(IMPORTE_POST_CCAA,3)) as IMPORTE_POST_CCAA,
    sum(round(coalesce(BASE_,IMPORTE),3)) as BASE_ ,
    if(NOMBRE_CONCEPTO_ADICIONAL='IVA', PORCENTAJE, 0) as PORCENTAJE_IVA,
    if(NOMBRE_CONCEPTO_ADICIONAL='IVA', sum(IMPORTE_APLICADO), 0) as IMPORTE_APLICADO_IVA,
    if(NOMBRE_CONCEPTO_ADICIONAL='IRPF', PORCENTAJE, 0) as PORCENTAJE_IRPF,
    if(NOMBRE_CONCEPTO_ADICIONAL='IRPF', sum(IMPORTE_APLICADO), 0) as IMPORTE_APLICADO_IRPF


from
(
select  UBICACION_ID,
        DEPARTAMENTO_ID,
        RECIBO_ID,
        INQUILINO_ID,
        FECHA_EMISION,
        NIF_DNI_INQUILINO,
        NOMBRE_COMPLETO,
        NOMBRE_UBICACION,
        PISO,
        PUERTA,
        NUM_RECIBO,
        TOTAL_RECIBO,
        TOTAL_RECIBO_POST_CCAA,
        IMPORTE,
        IMPORTE_POST_CCAA,
        ca.NOMBRE as NOMBRE_CONCEPTO_ADICIONAL,
        if (c_adicionSustraccion=0, -raca. BASE_, raca.BASE_) as BASE_,
        raca.porcentaje,
        if (c_adicionSustraccion=0, -raca. IMPORTE_APLICADO, raca.IMPORTE_APLICADO) as IMPORTE_APLICADO
        from
        (
        select
        icr.ID as icrid,
        r.FECHA_EMISION as FECHA_EMISION,
        inqui.ID as INQUILINO_ID,
        inqui.NIF_DNI as NIF_DNI_INQUILINO,
        inqui.NOMBRE_COMPLETO,
        u.id as UBICACION_ID,
        u.nombre as NOMBRE_UBICACION,
        d.id as DEPARTAMENTO_ID,
        d.PISO,
        d.PUERTA,
        r.ID as RECIBO_ID,
        r.NUM_RECIBO as NUM_RECIBO,
        r.TOTAL_RECIBO,
        r.TOTAL_RECIBO_POST_CCAA,
        if (c.ADICION_SUSTRACCION = 0, -icr.IMPORTE, icr.IMPORTE) as IMPORTE,
        if (c.ADICION_SUSTRACCION = 0, -icr.IMPORTE_POST_CCAA, icr.IMPORTE_POST_CCAA) as IMPORTE_POST_CCAA,
        coalesce(c2.ADICION_SUSTRACCION, c.ADICION_SUSTRACCION) as c_adicionSustraccion


        from IMPLEMENTACION_CONCEPTO icr
            inner join RECIBO r on icr.RECIBO_ID = r.ID
            inner join CONTRATO_INQUILINO ci on r.UTILITARIO_CONTRATO_INQUILINO_ID = ci.ID
            inner join DEPARTAMENTO d on ci.DEPARTAMENTO_ID = d.ID
            inner join UBICACION u on d.UBICACION_ID = u.ID
            inner join PERSONA inqui on ci.INQUILINO_ID = inqui.ID
            left join CONCEPTO_RECIBO cr on icr.CONCEPTO_RECIBO_ID = cr.ID
            left join CONCEPTO c on cr.CONCEPTO_ID = c.ID
	     left join CONCEPTO c2 on icr.OVERRIDE_CONCEPTO_ID = c2.ID
        where r.ID = '[no_recibo]'
        ) as st1
    left join RTRO_APP_CONCEPTO_ADICIONAL raca on raca.IMPLEMENTACION_CONCEPTO_ID = st1.icrid
    left join CONCEPTO_ADICIONAL ca on raca.CONCEPTO_ADICIONAL_ID = ca.ID
    ) as st2

    group by UBICACION_ID, DEPARTAMENTO_ID, RECIBO_ID, INQUILINO_ID, FECHA_EMISION, NOMBRE_COMPLETO, NOMBRE_UBICACION, PISO, PUERTA, TOTAL_RECIBO, TOTAL_RECIBO_POST_CCAA,
            NOMBRE_CONCEPTO_ADICIONAL, PORCENTAJE
    order by NOMBRE_UBICACION, DEPARTAMENTO_ID
    ) as st3

    group by

    UBICACION_ID,
    DEPARTAMENTO_ID,
    RECIBO_ID,
    INQUILINO_ID,
    FECHA_EMISION,
    NUM_RECIBO,
    NIF_DNI_INQUILINO,
    NOMBRE_COMPLETO,
    NOMBRE_UBICACION,
    PISO,
    PUERTA,
    TOTAL_RECIBO,
    TOTAL_RECIBO_POST_CCAA,
    IMPORTE,
    IMPORTE_POST_CCAA,
    BASE_

