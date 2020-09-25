CREATE TABLE `z_helper_proceso_recibos_informeiva` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `entorno_id` varchar(32) DEFAULT NULL,
  `ubicacion_id` varchar(32) NOT NULL DEFAULT '0',
  `departamento_id` varchar(32) NOT NULL DEFAULT '0',
  `recibo_id` varchar(32) NOT NULL DEFAULT '0',
  `inquilino_id` varchar(32) NOT NULL DEFAULT '0',
  `fechaEmision` date DEFAULT NULL,
  `numRecibo` varchar(25) DEFAULT NULL,
  `nifDniInquilino` varchar(15) DEFAULT NULL,
  `nombreCompleto` varchar(255) DEFAULT NULL,
  `nombre_ubicacion` varchar(255) DEFAULT NULL,
  `piso` varchar(255) DEFAULT NULL,
  `puerta` varchar(255) DEFAULT NULL,
  `totalRecibo` double DEFAULT NULL,
  `totalReciboPostCCAA` double DEFAULT NULL,
  `importe` double DEFAULT NULL,
  `importePostCCAA` double DEFAULT NULL,
  `base` double DEFAULT NULL,
  `porcentajeIVA` double DEFAULT NULL,
  `importeAplicadoIVA` double DEFAULT NULL,
  `porcentajeIRPF` double DEFAULT NULL,
  `importeAplicadoIRPF` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


