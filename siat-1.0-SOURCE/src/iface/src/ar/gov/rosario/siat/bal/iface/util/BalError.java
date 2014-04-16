//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>


package ar.gov.rosario.siat.bal.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class BalError extends DemodaError {
	
	//	 Use Codigos desde 20000 hasta 20999
	
	public static final String SISTEMA_LABEL = addError(0,"bal.sistema.label");
	public static final String SISTEMA_NROSISTEMA = addError(0,"bal.sistema.nroSistema.label");
	public static final String SISTEMA_DESSISTEMA = addError(0,"bal.sistema.desSistema.label");
	public static final String SISTEMA_SERVICIOBANCO_LABEL = addError(0,"bal.sistema.servicioBanco.label");
	public static final String SISTEMA_ESSERVICIOBANCO_LABEL = addError(0,"bal.sistema.esServicioBanco.label");

	// ---> DisPar
	public static final String DISPAR_LABEL  	    = addError(20000, "bal.disPar.label");
	public static final String DISPAR_DESDISPAR  	= addError(20001, "bal.disPar.desDisPar.label");
	public static final String DISPAR_RECURSO  		= addError(20002, "def.recurso.label");
	public static final String DISPAR_TIPOIMPORTE  	= addError(20002, "bal.tipoImporte.label");
	// <--- DisPar
	
	// ---> DisParRec
	public static final String DISPARREC_LABEL  	    = addError(20003, "bal.disParRec.label");
	public static final String DISPARREC_DISPAR  	= addError(20004, "bal.disParRec.disPar.label");
	public static final String DISPARREC_RECURSO  		= addError(20005, "def.recurso.label");
	public static final String DISPARREC_VIADEUDA  		= addError(20006, "def.viaDeuda.label");
	public static final String DISPARREC_VALOR  	= addError(20007, "bal.disParRec.valor.label");
	public static final String DISPARREC_FECHADESDE  	= addError(20008, "bal.disParRec.fechaDesde.label");
	public static final String DISPARREC_FECHAHASTA  	= addError(20009, "bal.disParRec.fechaHasta.label");
	public static final String DISPARREC_CASO  	= addError(20010, "cas.caso.label");
	// <--- DisParRec	

	// ---> DisParPla
	public static final String DISPARPLA_LABEL  	    = addError(20003, "bal.disParPla.label");
	public static final String DISPARPLA_DISPAR  	= addError(20004, "bal.disParPla.disPar.label");
	public static final String DISPARPLA_PLAN  		= addError(20005, "gde.plan.label");
	public static final String DISPARPLA_VALOR  	= addError(20007, "bal.disParPla.valor.label");
	public static final String DISPARPLA_FECHADESDE  	= addError(20008, "bal.disParPla.fechaDesde.label");
	public static final String DISPARPLA_FECHAHASTA  	= addError(20009, "bal.disParPla.fechaHasta.label");
	public static final String DISPARPLA_CASO  	= addError(20010, "cas.caso.label");
	// <--- DisParPla	

	// ---> DisParDet
	public static final String DISPARDET_LABEL  	    = addError(20011, "bal.disParDet.label");
	public static final String DISPARDET_DISPAR  	= addError(20012, "bal.disParDet.disPar.label");
	public static final String DISPARDET_RECURSO  		= addError(20013, "def.recurso.label");
	public static final String DISPARDET_VIADEUDA  		= addError(20014, "def.viaDeuda.label");
	public static final String DISPARDET_PORCENTAJE  	= addError(20007, "bal.disParDet.porcentaje.label");
	public static final String DISPARDET_FECHADESDE  	= addError(20008, "bal.disParDet.fechaDesde.label");
	public static final String DISPARDET_FECHAHASTA  	= addError(20009, "bal.disParDet.fechaHasta.label");
	public static final String DISPARDET_TIPOIMPORTE  	= addError(20010, "bal.tipoImporte.label");
	public static final String DISPARDET_PARTIDA  	= addError(20010, "bal.disParDet.partida.label");
	public static final String DISPARDET_RECCON  		= addError(20013, "def.recCon.label");
	// <--- DisParDet	

	// ---> Sellado
	public static final String SELLADO_CODSELLADO = addError(0,"bal.sellado.codSellado.label");
	public static final String SELLADO_DESSELLADO = addError(0,"bal.sellado.desSellado.label");
	public static final String SELLADO_LABEL = addError(0,"bal.sellado.label");
	// <--- Sellado
	
	// ---> ImpSel
	public static final String IMPSEL_LABEL = addError(0,"bal.impSel.label");
	public static final String IMPSEL_FECHADESDE_LABEL = addError(0,"bal.impSel.fechaDesde.label");
	public static final String IMPSEL_FECHAHASTA_LABEL = addError(0,"bal.impSel.fechaHasta.label");
	public static final String IMPSEL_IMPORTE_LABEL = addError(0,"bal.impSel.costo.label");
	public static final String IMPSEL_IMPORTE_UNICO_EN_RANGO = addError(0,"bal.impSel.importeUnicoEnRango");
	public static final String IMPSEL_TIPOSELLADO_LABEL = addError(0,"bal.impSel.tipoSellado.label");
	public static final String IMPSEL_COSTO_LABEL = addError(0,"bal.impSel.costo.label");
	
	// <--- ImpSel
	
	//	 ---> AccionSellado
	public static final String ACCIONSELLADO_LABEL = addError(0,"bal.accionSellado.label");
	public static final String ACCIONSELLADO_FECHADESDE_LABEL= addError(0,"bal.accionSellado.fechaDesde.label");
	public static final String ACCIONSELLADO_ACCION_LABEL=addError(0,"bal.accionSellado.accion.label");
	public static final String ACCIONSELLADO_ESESPECIAL_LABEL=addError(0,"bal.accionSellado.esEspecial.label");
	//	 <--- AccionSellado

	// ---> ParSel
	public static final String PARSEL_LABEL = addError(0,"bal.parSel.label");
	public static final String PARSEL_SELLADO = addError(0,"bal.parSel.sellado.label");
	public static final String PARSEL_PARTIDA = addError(0,"bal.parSel.partida.label");
	public static final String PARSEL_TIPODISTRIB = addError(0,"bal.parSel.tipoDistrib.label");
	// <--- ParSel

	// ---> Partida
	public static final String PARTIDA_CODPARTIDA = addError(0,"bal.partida.codPartida.label");
	public static final String PARTIDA_DESPARTIDA = addError(0,"bal.partida.desPartida.label");
	public static final String PARTIDA_PREEJEACT = addError(0,"bal.partida.preEjeAct.label");
	public static final String PARTIDA_PREEJEVEN = addError(0,"bal.partida.preEjeVen.label");
	public static final String PARTIDA_ESACTUAL = addError(0,"bal.partida.esActual.label");

	public static final String PARTIDA_LABEL = addError(0, "bal.partida.label");
	public static final String PARTIDA_DESPARTIDA_LABEL = addError(0, "bal.partida.desPartida.label");
	public static final String PARTIDA_ESTPARTIDA_LABEL = addError(0, "bal.estPartida.label");
	
	// <--- Partida
	
	// ---> ABM parCueBan
	public static final String PARCUEBAN_LABEL = addError(0, "bal.parCueBan.label");
	public static final String PARCUEBAN_CUENTABANCO = addError(0, "bal.parCueBan.cuentaBanco.label"); 
	public static final String PARCUEBAN_FECHADESDE = addError(0, "bal.parCueBan.fechaDesde.label"); 
	public static final String PARCUEBAN_FECHAHASTA = addError(0, "bal.parCueBan.fechaHasta.label"); 
	public static final String PARCUEBAN_PARTIDA = addError(0, "bal.parCueBan.partida.label"); 
	// <--- ABM ParCueBan
	
	// ---> Tipo Sellado
	public static final String TIPOSELLADO_DESTIPOSELLADO = addError(0,"bal.tiposSellado.descripcion.label");
	// <--- Tipo Sellado

	//	---> Accion
	public static final String ACCION_CODACCION = addError(0,"bal.accion.codigo.label");
	//  <--- Accion
	
	// ---> Canal
	public static final String CANAL_DESCANAL = addError(0,"bal.canal.desCanal.label");
	
	// ---> Asentamiento
	public static final String ASENTAMIENTO_LABEL  	    = addError(20020, "bal.asentamiento.label");
	public static final String ASENTAMIENTO_EJERCICIO  	= addError(20021, "bal.ejercicio.label");
	public static final String ASENTAMIENTO_SERVICIO_BANCO	= addError(20022, "def.servicioBanco.label");
	public static final String ASENTAMIENTO_OBSERVACION  	= addError(20023, "bal.asentamiento.observacion.label");
	public static final String ASENTAMIENTO_FECHABALANCE  	= addError(20025, "bal.asentamiento.fechaBalance.label");
	public static final String ASENTAMIENTO_CORRIDA  	= addError(20025, "pro.corrida.label");
	public static final String ASENTAMIENTO_USUARIOALTA  	= addError(20025, "bal.asentamiento.usuarioAlta.label");
	public static final String ASENTAMIENTO_CASO  	= addError(20026, "cas.caso.label");

	public static final String ASENTAMIENTO_CORRIDA_NO_GENERADA  = addError(20051, "bal.asentamiento.corridaNoGenerada.label");
	public static final String ASENTAMIENTO_CORRIDA_NO_ELIMINADA = addError(20053, "bal.asentamiento.corridaNoEliminada.label");
	public static final String ASENTAMIENTO_EJERCICIO_NO_ENCONTRADO  = addError(20052, "bal.asentamiento.ejercicioNoEncontrado.label");
	public static final String ASENTAMIENTO_EXISTENTE  = addError(20055, "bal.asentamiento.existente.label");
	public static final String ASENTAMIENTO_EJERCICIO_FUTURO  = addError(20056, "bal.asentamiento.ejercicioFuturo.label");
	public static final String ASENTAMIENTO_AUXILIAR_LABEL  = addError(20054, "bal.asentamiento.tablasAuxiliares.label");
	
	public static final String ASEPAS_LABEL  	    = addError(20027, "bal.asePas.label");
	public static final String ASEPAS_PASO  	= addError(20028, "bal.asePas.paso.label");
	public static final String ASEPAS_ESTADO_CORRIDA	= addError(20029, "pro.estadoCorrida.label");
	public static final String ASEPAS_OBSERVACION  	= addError(20030, "bal.asePas.observacion.label");
	public static final String ASEPAS_FECHACORRIDA  	= addError(20031, "bal.asePas.fechaCorrida.label");
	public static final String ASEPAS_ASENTAMIENTO  	= addError(20032, "bal.asentamiento.label");
	
	public static final String TRANSACCION_LABEL  	    = addError(20033, "bal.transaccion.label");
	public static final String TRANSACCION_SISTEMA	= addError(20034, "bal.sistema.label");
	public static final String TRANSACCION_NROCOMPROBANTE  	= addError(20035, "bal.transaccion.nroComprobante.label");
	public static final String TRANSACCION_ANIOCOMPROBANTE  	= addError(20036, "bal.transaccion.anioComprobante.label");
	public static final String TRANSACCION_PERIODO  	= addError(20037, "bal.transaccion.periodo.label");
	public static final String TRANSACCION_RESTO  	= addError(20038, "bal.transaccion.resto.label");
	public static final String TRANSACCION_CODPAGO  	= addError(20039, "bal.transaccion.codPago.label");
	public static final String TRANSACCION_CAJA  	= addError(20040, "bal.transaccion.caja.label");
	public static final String TRANSACCION_CODTR  	= addError(20041, "bal.transaccion.codTr.label");
	public static final String TRANSACCION_FECHAPAGO  	= addError(20042, "bal.transaccion.fechaPago.label");
	public static final String TRANSACCION_IMPORTE  	= addError(20043, "bal.transaccion.importe.label");
	public static final String TRANSACCION_RECARGO  	= addError(200443, "bal.transaccion.recargo.label");
	public static final String TRANSACCION_FILLER  	= addError(20045, "bal.transaccion.filler.label");
	public static final String TRANSACCION_PAQUETE  	= addError(20046, "bal.transaccion.paquete.label");
	public static final String TRANSACCION_MARCATR  	= addError(20047, "bal.transaccion.marcaTr.label");
	public static final String TRANSACCION_RECIBOTR  	= addError(20048, "bal.transaccion.reciboTr.label");
	public static final String TRANSACCION_FECHABALANCE  	= addError(20049, "bal.transaccion.fechaBalance.label");
	public static final String TRANSACCION_ASENTAMIENTO	= addError(20050, "bal.asentamiento.label");
	// <--- Asentamiento	

	// ---> AseDel
	public static final String ASEDEL_LABEL  	    = addError(20020, "bal.aseDel.label");
	public static final String ASEDEL_EJERCICIO  	= addError(20021, "bal.ejercicio.label");
	public static final String ASEDEL_SERVICIO_BANCO	= addError(20022, "def.servicioBanco.label");
	public static final String ASEDEL_OBSERVACION  	= addError(20023, "bal.aseDel.observacion.label");
	public static final String ASEDEL_FECHABALANCE  	= addError(20025, "bal.aseDel.fechaBalance.label");
	public static final String ASEDEL_CORRIDA  	= addError(20025, "pro.corrida.label");
	public static final String ASEDEL_USUARIOALTA  	= addError(20025, "bal.aseDel.usuarioAlta.label");
	public static final String ASEDEL_CASO  	= addError(20026, "cas.caso.label");

	public static final String ASEDEL_CORRIDA_NO_GENERADA  = addError(20051, "bal.aseDel.corridaNoGenerada.label");
	public static final String ASEDEL_CORRIDA_NO_ELIMINADA = addError(20053, "bal.aseDel.corridaNoEliminada.label");
	public static final String ASEDEL_EJERCICIO_NO_ENCONTRADO  = addError(20052, "bal.aseDel.ejercicioNoEncontrado.label");
	public static final String ASEDEL_EXISTENTE  = addError(20055, "bal.aseDel.existente.label");
	public static final String ASEDEL_EJERCICIO_FUTURO  = addError(20056, "bal.aseDel.ejercicioFuturo.label");
	public static final String ASEDEL_AUXILIAR_LABEL  = addError(20054, "bal.aseDel.tablasAuxiliares.label");
	
	public static final String TRANDEL_LABEL  	    = addError(20033, "bal.transaccion.label");
	// <-- AseDel
	
	// ---> ABM Ejercicio
	public static final String EJERCICIO_LABEL = addError(0, "bal.ejercicio.label");
	public static final String EJERCICIO_DESEJERICIO_LABEL = addError(0, "bal.ejercicio.desEjercicio.label");
	public static final String EJERCICIO_FECINI_LABEL = addError(0, "bal.ejercicio.fecIniEje.label");
	public static final String EJERCICIO_FECFIN_LABEL = addError(0, "bal.ejercicio.fecFinEje.label");
	public static final String EJERCICIO_ESTEJERCICIO_LABEL = addError(0, "bal.estEjercicio.label");
	public static final String EJERCICIO_FECCHARCIERRE_LABEL = addError(0, "bal.ejercicio.fechaCierre.label");
	// <--- ABM Ejercicio
	
    // ---> ABM Reclamo
	public static final String RECLAMO_LABEL = addError(0, "bal.reclamo.label");
	public static final String RECLAMO_DESEJERICIO_LABEL = addError(0, "bal.ejercicio.desEjercicio.label");
	public static final String RECLAMO_FECINI_LABEL = addError(0, "bal.ejercicio.fecIniEje.label");
	public static final String RECLAMO_FECFIN_LABEL = addError(0, "bal.ejercicio.fecFinEje.label");
	public static final String RECLAMO_DESRECURSO_LABEL = addError(0, "bal.reclamo.recurso.label");
	public static final String RECLAMO_FECHAPAGO_LABEL = addError(0, "bal.reclamo.fechaPago.label");
	// <--- ABM Reclamo

	// ---> Nodo
	public static final String NODO_LABEL  	    = addError(20052, "bal.nodo.label");
	public static final String NODO_DESCRIPCION	= addError(20053, "bal.nodo.descripcion.label");
	public static final String NODO_CODIGO	= addError(20062, "bal.nodo.codigo.label");
	public static final String NODO_CLASIFICADOR  	= addError(20054, "bal.clasificador.label");
	public static final String NODO_NIVEL	= addError(20055, "bal.nodo.nivel.label");
	public static final String NODO_NODO_PADRE	= addError(20061, "bal.nodo.nodoPadre.label");
	public static final String NODO_EXISTE_RELCLA = addError(20061, "bal.nodo.existeRelCla.label");
	
	public static final String CONSULTA_NODO_CLAVE_ERROR = addError(20061, "bal.consultaNodoSearchPage.clave.error");
	public static final String CONSULTA_NODO_RANGOFECHA_ERROR = addError(20061, "bal.consultaNodoSearchPage.rangoFecha.error");
	public static final String CONSULTA_NODO_VALIDAR_ERROR = addError(20061, "bal.consultaNodoSearchPage.validar.error");
	public static final String CONSULTA_NODO_RANGOFECHA_EXTRA_ERROR = addError(20061, "bal.consultaNodoSearchPage.rangoFechaExtra.error");
	// <--- Nodo

	//	 ---> RelCla
	public static final String RELCLA_LABEL  	    = addError(20056, "bal.relCla.label");
	public static final String RELCLA_NODO1  	 = addError(20057, "bal.relCla.nodo1.label");
	public static final String RELCLA_NODO2 	 = addError(20066, "bal.relCla.nodo2.label");
	public static final String RELCLA_FECHADESDE = addError(20067, "bal.relCla.fechaDesde.label");
	public static final String RELCLA_FECHAHASTA = addError(20068, "bal.relCla.fechaHasta.label");
	// <--- RelCla	
	
	// ---> RelPartida
	public static final String RELPARTIDA_LABEL  	    = addError(20058, "bal.relPartida.label");
	public static final String RELPARTIDA_NODO  	= addError(20059, "bal.nodo.label");
	public static final String RELPARTIDA_PARTIDA  	= addError(20060, "bal.partida.label");
	public static final String RELPARTIDA_FECHADESDE = addError(20069, "bal.relPartida.fechaDesde.label");
	public static final String RELPARTIDA_FECHAHASTA = addError(20070, "bal.relPartida.fechaHasta.label");
	// <--- RelPartida
	
	//	 ---> Clasificador
	public static final String CLASIFICADOR_LABEL  	    = addError(20063, "bal.clasificador.label");
	public static final String CLASIFICADOR_DESCRIPCION 	= addError(20064, "bal.descripcion.label");
	public static final String CLASIFICADOR_CANTNIVEL  	= addError(20065, "bal.cantNivel.label");
	// <--- Clasificador
	
	
	// ---> ABM Saldo a Favor
	public static final String SALDOAFAVOR_DESSALDOAFAVOR = addError(0, "bal.saldoAFavor.desSaldoAFavor.label"); 
	public static final String SALDOAFAVOR_LABEL = addError(0, "bal.saldoAFavor.label");
	public static final String ESTSALAFAV_DESESTSALAFAV = addError(0, "bal.estSalAFav.desEstSalAFav.label"); 
	public static final String ESTSALAFAV_LABEL = addError(0, "bal.estSalAFav.label"); 
	public static final String TIPOSALAFAV_DESTIPOSALAFAV = addError(0, "bal.saldoAFavor.tipoSaldoAFavor.label"); 
	public static final String TIPOSALAFAV_LABEL = addError(0, "bal.tipoSalAFav.label");
    public static final String TIPOORIGEN_DESTIPOORIGEN = addError(0, "bal.saldoAFavor.tipoOrigen.label"); 
	public static final String TIPOORIGEN_LABEL = addError(0, "bal.tipoOrigen.label"); 
	public static final String SALDOAFAVOR_CASO = addError(0, "bal.saldoAFavor.caso.label"); 
	public static final String SALDOAFAVOR_CUENTA = addError(0, "bal.saldoAFavor.cuenta.label"); 
	public static final String SALDOAFAVOR_FECHAGENERACION = addError(0, "bal.saldoAFavor.fechaGeneracion.label"); 
	public static final String SALDOAFAVOR_IDORIGEN = addError(0, "bal.saldoAFavor.idOrigen.label"); 
	public static final String SALDOAFAVOR_IMPORTE = addError(0, "bal.saldoAFavor.importe.label"); 
	public static final String SALDOAFAVOR_SALDO = addError(0, "bal.saldoAFavor.saldo.label"); 
	public static final String SALDOAFAVOR_TIPOORIGEN = addError(0, "bal.saldoAFavor.tipoOrigen.label"); 
	public static final String SALDOAFAVOR_TIPOSALAFAV = addError(0, "bal.saldoAFavor.tipoSalAFav.label");	
	// <--- ABM Saldo a Favor
	
	// ---> TipCueBan 
	public static final String TIPCUEBAN_DESCRIPCION = addError(0, "bal.tipCueBan.descripcion.label"); 
	public static final String TIPCUEBAN_LABEL = addError(0, "bal.tipCueBan.label"); 
	// <--- TipCueBan
	
	// ---> CuentaBanco 
	public static final String CUENTABANCO_LABEL = addError(0, "bal.cuentaBanco.label"); 
	public static final String CUENTABANCO_NROCUENTA = addError(0, "bal.cuentaBanco.nroCuenta.label"); 
	public static final String CUENTABANCO_BANCO = addError(0, "bal.cuentaBanco.banco.label"); 
	public static final String CUENTABANCO_AREA = addError(0, "bal.cuentaBanco.area.label"); 
	public static final String CUENTABANCO_TIPCUEBAN = addError(0, "bal.cuentaBanco.tipCueBan.label"); 
	// <--- CuentaBanco
	
	// ---> OtrIngTes
	public static final String OTRINGTES_LABEL  	    = addError(20071, "bal.otrIngTes.label");
	public static final String OTRINGTES_FECHAOTRINGTES	= addError(20072, "bal.otrIngTes.fechaOtrIngTes.label");
	public static final String OTRINGTES_FECHAALTA	= addError(20073, "bal.otrIngTes.fechaAlta.label");
	public static final String OTRINGTES_EJERCICIO  	= addError(20074, "bal.ejercicio.label");
	public static final String OTRINGTES_RECURSO	= addError(20075, "def.recurso.label");
	public static final String OTRINGTES_AREAORIGEN	= addError(20076, "def.area.label");
	public static final String OTRINGTES_OBSERVACION	= addError(20077, "bal.otrIngTes.observacion.label");
	public static final String OTRINGTES_DESCRIPCION	= addError(20078, "bal.otrIngTes.descripcion.label");
	public static final String OTRINGTES_IMPORTE	= addError(20079, "bal.otrIngTes.importe.label");
	public static final String OTRINGTES_CUEBANORIGEN = addError(20080, "bal.cuentaBanco.label");
	public static final String OTRINGTES_ESTOTRINGTES = addError(20081, "bal.estOtrIngTes.label");
	public static final String OTRINGTES_SELECCIONAR_REGISTRO = addError(0,"bal.otrIngTes.seleccionarRegistro");
	
	public static final String OTRINGTES_NO_ENCUENTRA_DISPAR = addError(20094, "bal.otrIngTes.noEncuentraDisPar.label");
	public static final String OTRINGTES_RECCON_ERROR = addError(20095, "bal.otrIngTes.recConError.label");
	public static final String OTRINGTES_DISTRIBUCION_INCORRECTA = addError(20096, "bal.otrIngTes.distribucionIncorrecta.label");
	public static final String OTRINGTES_RECCON_INCONSISTENTE = addError(20097, "bal.otrIngTes.recConInconsistente.label");
	// <--- OtrIngTes
	
	// ---> EstOtrIngTes
	public static final String ESTOTRINGTES_LABEL  	    = addError(20082, "bal.estOtrIngTes.label");
	public static final String ESTOTRINGTES_DESESTOTRINGTES	= addError(20083, "bal.estOtrIngTes.desEstOtrIngTes.label");
	public static final String ESTOTRINGTES_TIPO	= addError(20073, "bal.estOtrIngTes.tipo.label");
	public static final String ESTOTRINGTES_TRANSICIONES  	= addError(20084, "bal.estOtrIngTes.transiciones.label");
	public static final String ESTOTRINGTES_PERMITEMODIFICAR	= addError(20085, "bal.estOtrIngTes.permiteModificar.label");
	public static final String ESTOTRINGTES_ESINICIAL	= addError(20086, "bal.estOtrIngTes.esInicial.label");
	public static final String ESTOTRINGTES_ESTENING	= addError(20087, "bal.estOtrIngTes.estEnIng.label");
	public static final String ESTOTRINGTES_AREA	= addError(20088, "def.area.label");
	// <--- EstOtrIngTes
	
	// ---> OtrIngTesPar
	public static final String OTRINGTESPAR_LABEL  	    = addError(20089, "bal.otrIngTesPar.label");
	public static final String OTRINGTESPAR_OTRINGTES	= addError(20090, "bal.otrIngTes.label");
	public static final String OTRINGTESPAR_PARTIDA	= addError(20091, "bal.partida.label");
	public static final String OTRINGTESPAR_IMPORTEEJEACT = addError(20092, "bal.otrIngTesPar.importeEjeAct.label");
	public static final String OTRINGTESPAR_IMPORTEEJEVEN = addError(20093, "bal.otrIngTesPar.importeEjeVen.label");
	public static final String OTRINGTESPAR_IMPORTE = addError(20099, "bal.otrIngTesPar.importe.label");
	// <--- OtrIngTesPar

	// ---> OtrIngTesRecCon
	public static final String OTRINGTESRECCON_LABEL  	    = addError(20089, "bal.otrIngTesRecCon.label");
	public static final String OTRINGTESRECCON_OTRINGTES	= addError(20090, "bal.otrIngTes.label");
	public static final String OTRINGTESRECCON_RECCON	= addError(20091, "def.recCon.label");
	public static final String OTRINGTESRECCON_IMPORTE = addError(20092, "bal.otrIngTesRecCon.importe.label");
	// <--- OtrIngTesRecCon

	// ---> Reporte de Clasificador
	public static final String REPORTE_CLASIFICADOR_ERROR  	    = addError(20100, "bal.clasificadorReport.error");
	// ---> Reporte de Clasificador

	// ---> Balance
	public static final String BALANCE_LABEL  	    = addError(20102, "bal.balance.label");
	public static final String BALANCE_EJERCICIO  	= addError(20103, "bal.ejercicio.label");
	public static final String BALANCE_OBSERVACION  	= addError(20104, "bal.balance.observacion.label");
	public static final String BALANCE_FECHABALANCE  	= addError(20105, "bal.balance.fechaBalance.label");
	public static final String BALANCE_CORRIDA  	= addError(20025, "pro.corrida.label");

	public static final String BALANCE_CORRIDA_NO_GENERADA  = addError(20106, "bal.balance.corridaNoGenerada.label");
	public static final String BALANCE_CORRIDA_NO_ELIMINADA = addError(20107, "bal.balance.corridaNoEliminada.label");
	public static final String BALANCE_EJERCICIO_O_INTERVALO_NO_ENCONTRADO  = addError(20108, "bal.balance.ejercicioOIntervaloNoEncontrado.label");
	public static final String BALANCE_EXISTENTE  = addError(20109, "bal.balance.existente.label");
	public static final String BALANCE_EJERCICIO_FUTURO  = addError(20110, "bal.balance.ejercicioFuturo.label");
	public static final String BALANCE_IMPPAR_LABEL  = addError(20111, "bal.balance.impPar.label");
	public static final String BALANCE_PROCESOS_ASOCIADOS_ERROR  = addError(20109, "bal.balance.procesosAsociados.error");
	public static final String BALANCE_INCLUIR_REINGRESO_ERROR  = addError(20110, "bal.balance.incluirReingreso.error");
	
	// ---> ABM ImpPar
	public static final String IMPPAR_LABEL = addError(20112, "bal.impPar.label"); 
	public static final String IMPPAR_IMPORTEEJEACT = addError(20113, "bal.impPar.importeEjeAct.label"); 
    public static final String IMPPAR_IMPORTEEJEVEN = addError(20114, "bal.impPar.importeEjeVen.label");
	// <--- ABM ImpPar
    
    // ---> Indet
	public static final String INDET_LABEL  	    = addError(20056, "bal.indet.label");
	public static final String INDET_NROINDET  	 = addError(20057, "bal.indet.nroIndet.label");
	public static final String INDET_SISTEMA 	 = addError(20066, "bal.indet.sistema.label");
	public static final String INDET_NROCOMPROBANTE = addError(20067, "bal.indet.nroComprobante.label");
	public static final String INDET_CLAVE = addError(20067, "bal.indet.clave.label");
	public static final String INDET_RESTO = addError(20068, "bal.indet.resto.label");
	public static final String INDET_IMPORTECOBRADO = addError(20068, "bal.indet.importeCobrado.label");
	public static final String INDET_IMPORTEBASICO = addError(20068, "bal.indet.importeBasico.label");
	public static final String INDET_IMPORTECALCULADO = addError(20068, "bal.indet.importeCalculado.label");
	public static final String INDET_RECARGO = addError(20068, "bal.indet.recargo.label");
	public static final String INDET_PARTIDA = addError(20068, "bal.indet.partida.label");
	public static final String INDET_CODINDET = addError(20068, "bal.indet.codIndet.label");
	public static final String INDET_FECHAPAGO = addError(20068, "bal.indet.fechaPago.label");
	public static final String INDET_CAJA = addError(20068, "bal.indet.caja.label");
	public static final String INDET_PAQUETE = addError(20068, "bal.indet.paquete.label");
	public static final String INDET_CODPAGO = addError(20068, "bal.indet.codPago.label");
	public static final String INDET_FECHABALANCE = addError(20068, "bal.indet.fechaBalance.label");
	public static final String INDET_CODTR = addError(20068, "bal.indet.codTr.label");
	public static final String INDET_FILLER = addError(20068, "bal.indet.filler.label");
	public static final String INDET_RECIBOTR = addError(20068, "bal.indet.reciboTr.label");
	public static final String INDET_SELECCIONAR_REGISTRO = addError(0,"bal.indet.seleccionarRegistro");
	public static final String MSG_REINGRESO_MASIVO_INDET = addError(00, "bal.indet.reingresoMasivo");
	public static final String MSG_VUELTA_ATRAS_INDET_ERROR = addError(00, "bal.indet.vueltaAtraReing.error");
	public static final String INDET_TIPOINGRESO = addError(20068, "bal.indet.tipoIngreso.label");
	// <--- Indet	
    
	// ---> Duplice
	public static final String DUPLICE_LABEL  	    = addError(20056, "bal.duplice.label");
	public static final String DUPLICE_NO_SELECCIONO_TRAN_ERROR   = addError(20056, "bal.duplice.noSeleccionoTran.error");
	public static final String DUPLICE_NO_SE_ENCONTRO_DEUDA_ERROR   = addError(20056, "bal.duplice.noSeEncontroDeuda.error");
	public static final String DUPLICE_NO_SE_ENCONTRO_CUOTA_ERROR   = addError(20056, "bal.duplice.noSeEncontroCuota.error");
	// <--- Duplice
	
	// ---> ABM Archivo
	public static final String ARCHIVO_LABEL = addError(20201, "bal.archivo.label");
	public static final String ARCHIVO_IDORIGEN_LABEL = addError(20202, "bal.archivo.idOrigen.label");
	public static final String ARCHIVO_FECHABANCO_LABEL = addError(20203, "bal.archivo.fechaBanco.label");
	public static final String ARCHIVO_TOTAL_LABEL = addError(20204, "bal.archivo.total.label");
	public static final String ARCHIVO_CANTTRANS_LABEL = addError(20205, "bal.archivo.cantTrans.label");
	public static final String ARCHIVO_NROBANCO_LABEL = addError(20206, "bal.archivo.nroBanco.label");
	public static final String ARCHIVO_PREFIX_LABEL = addError(20207, "bal.archivo.prefix.label");
	public static final String ARCHIVO_SELECCIONAR_REGISTRO = addError(0,"bal.archivo.seleccionarRegistro");
	// <--- ABM Archivo

	//	 ---> ABM EstadoArc
	public static final String ESTADOARC_LABEL = addError(20208, "bal.estadoArc.label");
	public static final String ESTADOARC_DESCRIPCION_LABEL = addError(20209, "bal.estadoArc.descripcion.label");
	// <--- ABM EstadoArc

	//	 ---> ABM TipoArc
	public static final String TIPOARC_LABEL = addError(20210, "bal.tipoArc.label");
	public static final String TIPOARC_DESCRIPCION_LABEL = addError(20211, "bal.tipoArc.descripcion.label");
	// <--- ABM TipoArc
	
	// ---> ABM Folio
	public static final String FOLIO_LABEL = addError(20201, "bal.folio.label");
	public static final String FOLIO_NUMERO_LABEL = addError(20202, "bal.folio.numero.label");
	public static final String FOLIO_FECHAFOLIO_LABEL = addError(20203, "bal.folio.fechaFolio.label");
	public static final String FOLIO_DESCRIPCION_LABEL = addError(20204, "bal.folio.descripcion.label");
	public static final String FOLIO_DESDIACOB_LABEL = addError(20204, "bal.folio.desDiaCob.label");
	
	public static final String MSG_ENVIAR = addError(000, "bal.enviar");
	public static final String MSG_DEVOLVER = addError(000, "bal.devolver");
	public static final String FOLIO_OTRINGTES_ERROR_EN_DISTRIBUCION = addError(20096, "bal.folio.otrIngTes.ErrorEnDistribucion.label");
	public static final String FOLIO_FALTAN_DETALLES = addError(20096, "bal.folio.faltanDetalles.label");
	// <--- ABM Folio

	// ---> ABM FolCom
	public static final String FOLCOM_LABEL = addError(20201, "bal.folCom.label");
	public static final String FOLCOM_CONCEPTO_LABEL = addError(20202, "bal.folCom.concepto.label");
	public static final String FOLCOM_FECHA_LABEL = addError(20203, "bal.folCom.fecha.label");
	public static final String FOLCOM_DESCUEBAN_LABEL = addError(20204, "bal.folCom.desCuenBan.label");
	public static final String FOLCOM_NROCOM_LABEL = addError(20204, "bal.folCom.nroComp.label");
	public static final String FOLCOM_IMPORTE_LABEL = addError(20204, "bal.folCom.importe.label");
	// <--- ABM FolCom

	// ---> ABM FolDiaCob
	public static final String FOLDIACOB_LABEL = addError(20201, "bal.folDiaCob.label");
	public static final String FOLDIACOB_FECHACOB_LABEL = addError(20203, "bal.folDiaCob.fecha.label");
	public static final String FOLDIACOB_DESCRIPCION_LABEL = addError(20204, "bal.folDiaCob.descripcion.label");
	public static final String FOLDIACOB_IDESTADODIA_LABEL = addError(20204, "bal.folDiaCob.idEstadoDia.label");
	public static final String FOLDIACOB_FECHACOB_OR_DESCRIPCION_ERROR = addError(20204, "bal.folDiaCob.fechaCobOrDescripcion.error");
	public static final String FOLDIACOB_FECHACOBDESDE_LABEL = addError(20203, "bal.folDiaCobSearchPage.fechaCobDesde");
	public static final String FOLDIACOB_IMPRIMIR_ERROR = addError(20203, "bal.folDiaCobSearchPage.imprimir.error");
	
	// <--- ABM FolDiaCob

	// ---> ABM FolDiaCobCol
	public static final String FOLDIACOBCOL_LABEL = addError(20201, "bal.folDiaCobCol.label");
	public static final String FOLDIACOBCOL_IMPORTE_LABEL = addError(20203, "bal.folDiaCobCol.importe.label");
	public static final String FOLDIACOBCOL_IMPORTE_ERROR = addError(20203, "bal.folDiaCobCol.importe.error");
	// <--- ABM FolDiaCobCol

	// ---> ABM TipoCob
	public static final String TIPOCOB_LABEL = addError(20201, "bal.tipoCob.label");
	public static final String TIPOCOB_IMPORTE_LABEL = addError(20204, "bal.tipoCob.importe.label");
	public static final String TIPOCOB_CODCOLUMNA_LABEL = addError(20204, "bal.tipoCob.codColumna.label");
	public static final String TIPOCOB_DESCRIPCION_LABEL = addError(20204, "bal.tipoCob.descripcion.label");
	public static final String TIPOCOB_PARTIDA_LABEL = addError(20204, "bal.tipoCob.partida.label");
	public static final String TIPOCOB_ORDEN_LABEL = addError(20204, "bal.tipoCob.orden.label");
	// <--- ABM TipoCob

	// ---> ABM EstadoFol
	public static final String ESTADOFOL_LABEL = addError(20201, "bal.estadoFol.label");
	public static final String ESTADOFOL_DESCRIPCION_LABEL = addError(20204, "bal.estadoFol.descripcion.label");
	// <--- ABM EstadoFol

	// ---> TipoCom 
	public static final String TIPOCOM_LABEL = addError(0, "bal.tipoCom.label"); 
	public static final String TIPOCOM_DESCRIPCION = addError(0, "bal.tipoCom.descripcion.label");
	// <--- TipoCom
	
	// ---> ABM TipoComDeu
	public static final String TIPOCOMDEU_LABEL = addError(20201, "bal.tipoComDeu.label");
	public static final String TIPOCOMDEU_DESCRIPCION = addError(20203, "bal.tipoComDeu.descripcion.label");
	// <--- ABM TipoComDeu

	// ---> ABM Compensacion
	public static final String COMPENSACION_LABEL = addError(20201, "bal.compensacion.label");
	public static final String COMPENSACION_DESCRIPCION = addError(20203, "bal.compensacion.descripcion.label");
	public static final String COMPENSACION_FECHAALTA = addError(20203, "bal.compensacion.fechaAlta.label");
	public static final String COMPENSACION_EXCLUIR_SALDO_ERROR = addError(20203, "bal.compensacion.excluirSaldo.error");
	public static final String COMPENSACION_INCLUIR_SALDO_VACIO = addError(20203, "bal.compensacion.incluirSaldo.vacio");
	public static final String COMPENSACION_INCOMPLETA = addError(20096, "bal.compensacion.incompleta.label");
	public static final String COMPENSACION_PREPRARAR_PARA_FOLIO = addError(00, "bal.compensacion.preprararParaFolio.label");
	public static final String COMPENSACION_VOLVER_A_CREADO = addError(00, "bal.compensacion.volverACreado.label");
	public static final String COMPENSACION_SELECCIONAR_REGISTRO = addError(0,"bal.compensacion.seleccionarRegistro");
	// <--- ABM Compensacion
	
	// ---> ABM ComDeu
	public static final String COMDEU_LABEL = addError(20201, "bal.compensacion.label");
	public static final String COMDEU_IMPORTE = addError(20203, "bal.comDeu.importe.label");
	public static final String COMDEU_IMPORTE_ERROR = addError(20203, "bal.comDeu.importe.error");
	public static final String COMDEU_IMPORTE_ADAPTER = addError(20203, "bal.comDeuAdapter.importe.label");
	public static final String COMDEU_LISTDEUDA_ERROR = addError(20203, "bal.comDeu.listDeuda.error");
	public static final String COMDEU_SELECCION_DEUDA_ERROR = addError(20203, "bal.comDeu.seleccionDeuda.error");
	public static final String COMDEU_IMPORTE_A_COMPENSAR_ERROR = addError(20203, "bal.comDeu.importeACompensar.error");
	// <--- ABM ComDeu
	
	// ---> ABM EstadoCom
	public static final String ESTADOCOM_LABEL = addError(20201, "bal.estadoCom.label");
	public static final String ESTADOCOM_DESCRIPCION = addError(20203, "bal.estadoCom.descripcion.label");
	public static final String ESTADOCOM_TIPO = addError(20203, "bal.estadoCom.tipo.label");
	public static final String ESTADOCOM_TRANSICIONES = addError(20203, "bal.estadoCom.transiciones.label");
	// <--- ABM EstadoCom

	// ---> ABM HisEstCom
	public static final String HISESTCOM_LABEL = addError(20201, "bal.hisEstCom.label");
	// <--- ABM HisEstCom
	
	// ---> Reingreso
	public static final String REINGRESO_LABEL = addError(0,"bal.reingreso.label");
	public static final String REINGRESO_NROREINGRESO = addError(0,"bal.reingreso.nroReingreso");
	public static final String REINGRESO_IMPORTEPAGO = addError(0,"bal.reingreso.importePago");
	public static final String REINGRESO_ERROR_AL_INCLUIR = addError(0,"bal.reingreso.errorAlIncluir");
	public static final String REINGRESO_ERROR_AL_EXCLUIR = addError(0,"bal.reingreso.errorAlExcluir");
	public static final String REINGRESO_SELECCIONAR_REGISTRO = addError(0,"bal.reingreso.seleccionarRegistro");
	public static final String REINGRESO_FECHA_DESDE = addError(0,"bal.reingresoAdapter.fechaDesde");
	public static final String REINGRESO_FECHA_DESDE_HASTA = addError(0,"bal.reingresoAdapter.intervaloFecha");
	// <--- Reingreso
	
	// ---> ABM Caja7
	public static final String CAJA7_LABEL = addError(0, "bal.caja7.label"); 
	public static final String CAJA7_FECHA = addError(0, "bal.caja7.fecha.label"); 
	public static final String CAJA7_PARTIDA = addError(0, "bal.partida.label"); 
    public static final String CAJA7_DESCRIPCION = addError(0, "bal.caja7.descripcion.label"); 
    public static final String CAJA7_OBSERVACION = addError(0, "bal.caja7.observacion.label"); 
    public static final String CAJA7_IMPORTE = addError(0, "bal.caja7.importeEjeAct.label"); 
    // <--- ABM Caja7

    // ---> ABM Caja69
	public static final String CAJA69_LABEL = addError(0, "bal.ajuste.label"); 
	// <--- ABM Caja69
    
    // ---> ABM TranBal
	public static final String TRANBAL_LABEL = addError(0, "bal.ajuste.label"); 
	// <--- ABM TranBal
	
	// ---> ABM AuxCaja7
	public static final String AUXCAJA7_LABEL = addError(0, "bal.auxCaja7.label"); 
	public static final String AUXCAJA7_FECHA = addError(0, "bal.auxCaja7.fecha.label"); 
	public static final String AUXCAJA7_PARTIDA = addError(0, "bal.partida.label"); 
    public static final String AUXCAJA7_DESCRIPCION = addError(0, "bal.auxCaja7.descripcion.label"); 
    public static final String AUXCAJA7_OBSERVACION = addError(0, "bal.auxCaja7.observacion.label"); 
    public static final String AUXCAJA7_IMPORTE = addError(0, "bal.auxCaja7.importeEjeAct.label"); 
    public static final String AUXCAJA7_SELECCIONAR_REGISTRO = addError(0,"bal.auxCaja7.seleccionarRegistro");
    // <--- ABM AuxCaja7

	//	 ---> ABM LeyParAcu
	public static final String LEYPARACU_LABEL = addError(20210, "bal.leyParAcu.label");
	public static final String LEYPARACU_CODIGO_LABEL = addError(20211, "bal.leyParAcu.codigo.label");
	public static final String LEYPARACU_DESCRIPCION_LABEL = addError(20211, "bal.leyParAcu.descripcion.label");
	// <--- ABM LeyParAcu

	// ---> Reporte de Clasificador Comparativo
	public static final String REPORTE_CLACOM_ERROR  = addError(20100, "bal.claComReport.error");
	public static final String REPORTE_CLACOM_NIVEL_HASTA  = addError(20100, "bal.claComReport.nivelHasta.label");
	// ---> Reporte de Clasificador Comparativo
	
	// ---> Control de Conciliacion
	public static final String CONTROL_CONCILIACION_RANGOFECHA_ERROR = addError(20061, "bal.controlConciliacionSearchPage.rangoFecha.error");
	// <--- Control de Conciliacion
	
	// ---> Reporte de Rentas
	public static final String REPORTE_RENTAS_RANGOFECHA_ERROR = addError(20061, "bal.rentasReport.rangoFecha.error");
	// <--- Reporte de Rentas

	// ---> Reporte de Total por Partida	
	public static final String REPORTE_TOTALPAR_RANGOFECHA_ERROR = addError(20061, "bal.totalParReport.rangoFecha.error");
	public static final String REPORTE_TOTALPAR_RANGOFECHA_EXTRA_ERROR = addError(20061, "bal.totalParReport.rangoFechaExtra.error");
	// <--- Reporte de Total por Partida
	
	// ---> EnvioOsiris
	public static final String ENVIOOSIRIS_MSG_OBTENER_ENVIOOSIRIS = addError(0,"bal.envioOsirisAdapter.obtenerEnvio.msg");
	public static final String ENVIOOSIRIS_MSG_PROCESAR_ENVIOOSIRIS = addError(0,"bal.envioOsirisAdapter.procesarEnvio.msg");
	public static final String ENVIOOSIRIS_MSG_GENERAR_TRANSACCION = addError(0,"bal.envioOsirisAdapter.generarTransaccion.msg");
	public static final String ENVIOOSIRIS_MSG_GENERAR_DECJUR = addError(0,"bal.envioOsirisAdapter.generarDecJur.msg");
	public static final String ENVIOOSIRIS_MSG_CAMBIAR_ESTADO_ENVIO = addError(0,"bal.envioOsirisAdapter.cambiarEstado.msg");
	public static final String TRANAFIP_MSG_GENERAR_DECJUR = addError(0,"bal.tranAfipAdapter.generarDecJur.msg");

	public static final String ENVIOOSIRIS_NOVEDAD_LOCK_ERROR = addError(0,"bal.envioOsirisAdapter.obtenerEnvios.lock.error");
	public static final String ENVIOOSIRIS_NOVEDAD_EN_EJECUCION_ERROR = addError(0,"bal.envioOsirisAdapter.obtenerEnvios.enEjecucion.error");
	public static final String ENVIOOSIRIS_NOVEDAD_VALIDACION_ERROR = addError(0,"bal.envioOsirisAdapter.obtenerEnvios.validacion.error");
	
	public static final String ENVIOOSIRIS_PROCESO_LOCK_ERROR = addError(0,"bal.envioOsirisAdapter.procesoEnvios.lock.error");
	public static final String ENVIOOSIRIS_PROCESO_EN_EJECUCION_ERROR = addError(0,"bal.envioOsirisAdapter.procesoEnEjecucion.error");
	public static final String ENVIOOSIRIS_PROCESO_VALIDACION_ERROR = addError(0,"bal.envioOsirisAdapter.procesoEnvios.validacion.error");
	public static final String ENVIOOSIRIS_EXISTEN_FORDECJUR_ERROR = addError(0,"bal.envioOsirisAdapter.existenForDecJur.error");
	public static final String ENVIOOSIRIS_EJECUTAR_GENERAR_DECJUR_ERROR = addError(0,"bal.envioOsirisAdapter.ejecutarGenerarDecJur.error");
	
	public static final String DETALLEDJ_LABEL = addError(0,"bal.envioOsirisAdapter.eliminarDetalleDJ.error");
	public static final String DETALLEPAGO_LABEL = addError(0,"bal.envioOsirisAdapter.eliminarDetallePago.error");
	public static final String TRANAFIP_LABEL = addError(0,"bal.envioOsirisAdapter.eliminarTranAfip.error");
	
	// <--- EnvioOsiris

	// ---> ABM MovBan
	public static final String MOVBAN_LABEL = addError(0, "bal.movBan.label"); 
	public static final String MOVBAN_IDORGRECAFPI = addError(0, "bal.movBan.idOrgRecAfip.label");
	public static final String MOVBAN_BANCOADM = addError(0, "bal.movBan.bancoAdm.label");
	public static final String MOVBAN_FECHAPROCESO = addError(0, "bal.movBan.fechaProceso.label"); 
	public static final String MOVBAN_FECHAACREDIT = addError(0, "bal.movBan.fechaAcredit.label"); 
    public static final String MOVBAN_TOTALDEBITO = addError(0, "bal.movBan.totalDebito.label"); 
    public static final String MOVBAN_TOTALCREDITO = addError(0, "bal.movBan.totalCredito.label");
    public static final String MOVBAN_CANTDETALLE = addError(0, "bal.movBan.cantDetalle.label"); 
    public static final String MOVBAN_CONCILIADO = addError(0, "bal.movBan.conciliadio.label"); 
    public static final String MOVBAN_UPLOAD_FILE_LABEL 	= addError(0,"bal.movBan..uploadFile.label");
    public static final String UPLOAD_EVENTO_MSG_FILE_NOTFOUND		= addError(0,"bal.movBan.fileNotFound.label");
    // <--- ABM MovBan

	// ---> ABM MovBanDet
	public static final String MOVBANDET_LABEL = addError(0, "bal.movBanDet.label"); 
	public static final String MOVBANDET_IMPUESTO = addError(0, "bal.movBanDet.impuesto.label");
	public static final String MOVBANDET_BANCOREC= addError(0, "bal.movBanDet.bancoRec.label");
	public static final String MOVBANDET_ANEXOOPERATIVO = addError(0, "bal.movBanDet.anexoOperativo.label"); 
	public static final String MOVBANDET_NROCIERREBANCO = addError(0, "bal.movBanDet.nroCierreBanco.label"); 
    public static final String MOVBANDET_DEBITO = addError(0, "bal.movBanDet.debito.label"); 
    public static final String MOVBANDET_CREDITO = addError(0, "bal.movBanDet.credito.label");
    public static final String MOVBANDET_NROCUENTA = addError(0, "bal.movBanDet.nroCuenta.label"); 
    public static final String MOVBANDET_MONEDA = addError(0, "bal.movBanDet.moneda.label");
    public static final String MOVBANDET_CONCILIADO = addError(0, "bal.movBanDet.conciliadio.label"); 
    // <--- ABM MovBanDet
   
    //---> Conciliar Cierres Banco
    public static final String CIERREBANCO_CONCILIACION_ERROR = addError(0, "bal.cierreBanco.conciliacion.error"); 
    public static final String CIERREBANCO_CONCILIACION_TRANSACCION_ERROR = addError(0, "bal.cierreBanco.conciliacion.transacciones.error");
    
    public static final String CIERREBANCO_CONCILIACION_MOVBAN_MSG = addError(0, "bal.cierreBanco.conciliacion.movBan.msg");
    public static final String CIERREBANCO_CONCILIACION_RENTRAN_MSG = addError(0, "bal.cierreBanco.conciliacion.renTran.msg");
    //<--- Conciliar Cierres Banco
    
    //---> TipoIndet
	public static final String TIPOINDET_LABEL = addError(0,"bal.tipoIndet.label");
	public static final String TIPOINDET_CODTIPOINDET = addError(0,"bal.tipoIndet.codTipoIndet.label");
	public static final String TIPOINDET_DESTIPOINDET = addError(0,"bal.tipoIndet.desTipoIndet.label");
	public static final String TIPOINDET_CODINDETMR = addError(0,"bal.tipoIndet.codIndet.label");
	//<--- TipoIndet

}
