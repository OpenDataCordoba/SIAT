//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class EfError extends DemodaError {

	// ---> Inspector
	public static final String INSPECTOR_LABEL 				= addError(0, "ef.inspector.label");
	public static final String INSPECTOR_DESINSPECTOR 		= addError(0, "ef.inspector.desInspector.label");
	public static final String INSPECTOR_FECHADESDE_LABEL 	= addError(0, "ef.inspector.fechaDesde.label");
	public static final String INSPECTOR_FECHAHASTA_LABEL 	= addError(0, "ef.inspector.fechaHasta.label");
	// <--- Inspector
	
	// ---> InsSup 
	public static final String INSSUP_FECHADESDE = addError(0, "ef.insSup.fechaDesde.label"); 
	public static final String INSSUP_FECHAHASTA = addError(0, "ef.insSup.fechaHasta.label"); 
	public static final String INSSUP_INSPECTOR = addError(0, "ef.insSup.inspector.label"); 
	public static final String INSSUP_LABEL = addError(0, "ef.insSup.label"); 
	public static final String INSSUP_SUPERVISOR = addError(0, "ef.insSup.supervisor.label"); 
	public static final String MSG_SOLAPAMIENTO_PERIODO = addError(0, "ef.solapamientoVigencia.label");
	// <--- InsSup
	
	// ---> Supervisor
	public static final String SUPERVISOR_LABEL 				= addError(0, "ef.supervisor.label");
	public static final String SUPERVISOR_DESSUPERVISOR		= addError(0, "ef.supervisor.desSupervisor.label");
	public static final String SUPERVISOR_FECHADESDE_LABEL 	= addError(0, "ef.supervisor.fechaDesde.label");
	public static final String SUPERVISOR_FECHAHASTA_LABEL 	= addError(0, "ef.supervisor.fechaHasta.label");
	// <--- Supervisor
	
	// ---> FuenteInfo 
	public static final String FUENTEINFO_APERTURA = addError(0, "ef.fuenteInfo.apertura.label"); 
	public static final String FUENTEINFO_DESCOL1 = addError(0, "ef.fuenteInfo.desCol1.label"); 
	public static final String FUENTEINFO_LABEL = addError(0, "ef.fuenteInfo.label"); 
	public static final String FUENTEINFO_NOMBRETEXTO = addError(0, "ef.fuenteInfo.nombreTexto.label"); 
	public static final String FUENTEINFO_TIPOPERIODIOCIDAD = addError(0, "ef.fuenteInfo.tipoPeriodiocidad.label"); 
	// <--- FuenteInfo
	   
	// ---> Plan fiscal
	public static final String PLANFISCAL_DESPLANFISCAL 		= addError(0, "ef.planFiscal.desPlanFiscal.label");
	public static final String PLANFISCAL_LABEL 				= addError(0, "ef.planFiscal.label");
	public static final String PLANFISCAL_FECHA_DESDE_LABEL		=  addError(0, "ef.planFiscal.fechaDesde.label");
	public static final String PLANFISCAL_FECHA_HASTA_LABEL		= addError(0, "ef.planFiscal.fechaHasta.label");
	public static final String PLANFISCAL_ESTADOPLANFIS_LABEL	= addError(0, "ef.planFiscal.estadoPlanFis.label");
	public static final String PLANFISCAL_NUMERO_LABEL 			= addError(0, "ef.planFiscal.numero.label");
	// <--- Plan fiscal

	// ---> Investigador
	public static final String INVESTIGADOR_LABEL = addError(0, "ef.investigador.label");
	// <--- Investigador	
	
	// ---> OpeInv
	public static final String OPEINV_LABEL  			= addError(0, "ef.opeInv.label");
	public static final String OPEINV_DESOPEINV_LABEL 	= addError(0, "ef.opeInv.desOpeInv.label");
	public static final String OPEINV_FECINICIO_LABEL 	= addError(0, "ef.opeInv.fechaInicio.label");
	// <--- OpeInv
	
	// ---> OpeInvCon
	public static final String OPEINVCON_LABEL = addError(0, "ef.opeInvCon.label");
	public static final String OPEINVCON_MSG_ELIMINAR = addError(0, "ef.opeInvCon.msg.eliminar");
	public static final String OPEINVCON_MSG_SECCION_INVALIDA = addError(0, "ef.opeInvCon.msg.seccionInvalida");
	public static final String OPEINVCON_CONTRIB_REQUERIDO = addError(0, "ef.opeInvCon.msg.contribuyenteRequerido");
	public static final String OPEINVCON_CUENTA_SELECT_REQUERIDO = addError(0, "ef.opeInvCon.msg.cuentaSelectRequerido");
	public static final String OPEINVCON_ESTADO_REQUERIDO = addError(0, "ef.opeInvCon.msg.estadoRequerido");
	public static final String OPEINVCON_CONTRIB_EXISTENTE_EN_OPEINV = addError(0, "ef.opeInvCon.contrExistente");
	
	
		// OpeInvBus
	public static final String OPEINVBUS_FECHADESDE_LABEL = addError(0, "ef.opeInvBusSearchPage.fechaDesde.label");
	public static final String OPEINVBUS_FECHAHASTA_LABEL = addError(0, "ef.opeInvBusSearchPage.fechaHasta.label");
	public static final String OPEINVBUS_DESCRIPCION_LABEL = addError(0, "ef.opeInvBus.desOpeInvBus.label");
	public static final String OPEINVBUS_LABEL = addError(0, "ef.opeInvBus.label");
	public static final String OPEINVBUS_FECHABUS_LABEL = addError(0, "ef.opeInvBus.fechaBusqueda.label");
	
	// ---> asignar actas de inicio
	public static final String ASIGNAR_INV_MSG_IDS_REQUERIDO = addError(0, "ef.opeInvConActas.asignInv.msg.idsRequerido.label");
	public static final String ASIGNAR_INV_FECHAVISITA_LABEL = addError(0, "ef.opeInvConActas.asignInv.fechaVisita.label");
	// <--- asignar actas de inicio
	
	// ---> ADM Actas
	public static final String ACTAINV_FECHAINICIO_LABEL = addError(0, "ef.actaInv.fechaInicio.label");
	public static final String ACTAINV_FECHAFIN_LABEL = addError(0, "ef.actaInv.fechaFin.label");
	public static final String ACTAINV_MSG_PEDIDOAPROB_ESTADO_INVALIDO = addError(0, "ef.actaInvAdapter.pedidoAprob.msg.estadoInvalido");
	
	// ---> Emitir Orden de Control
	public static final String ORDEN_CONTROL_MSG_IDSOPEINVCON_REQ = addError(0, "ef.ordenControlContrSearchPage.msg.idsOpeInvCon.req");
	public static final String ORDEN_CONTROL_TIPOORDEN_LABEL = addError(0, "ef.tipoOrden.label");
	public static final String ORDEN_CONTROL_TIPOPERIODO_LABEL = addError(0, "ef.tipoPeriodo.label");
	public static final String ORDEN_CONTROL_PROCEDIMIENTO_CONTRIB = addError(0,"ef.ordenControlContrSearchPage.emisionProcedimiento.contribuyente.error");
	public static final String ORDEN_CONTROL_PROCEDIMIENTO_PERSONA=addError(0,"ef.ordenControlContrSearchPage.emisionProcedimiento.persona.error");
	// <--- Emitir Orden de Control	

	// --> ABM periodoOrden
	public static final String PERIODOORDEN_LABEL = addError(0, "ef.periodoOrden.label");
	public static final String PERIODOORDEN_AGREGAR_MSG_IDS_REQUERIDO = addError(0, "ef.periodoOrdenEditAdapter.agregar.msgIdsRequerido.label");
	public static final String PERIODOORDEN_PERIODOANIO_REQUERIDO = addError(0, "ef.periodoOrdenEditAdapter.msgAnioPeriodoRequerido.label");
	public static final String PERIODOORDEN_RANGO_INVALIDO = addError(0, "ef.periodoOrdenEditAdapter.msgRangoInvalido.label");
	// <-- ABM periodoOrden
	
	// ---> TipoActa
	public static final String TIPOACTA_LABEL = addError(0, "ef.tipoActa.label");
	// <--- TipoActa
	
	// ---> Acta
	public static final String ACTA_FECHAVISITA_LABEL = addError(0, "ef.acta.fechaVisita.label");
	public static final String ACTA_LABEL = addError(0, "ef.acta.label");
	// <--- Acta
	
	// ---> OrdConDoc
	public static final String ORDCONDOC_LABEL = addError(0, "ef.ordConDoc.label");
	public static final String AGREGAR_ORDCONDOC_MSG_IDS_REQUERIDO = addError(0, "ef.ordConDocAdapter.msgIdsRequeridos.label");

	// ---> PlaFueDat
	public static final String PLAFUEDAT_LABEL = addError(0, "ef.plaFueDat.label");
	// <--- PlaFueDat
	
	// ---> PlaFueDatCol
	public static final String PLAFUEDATCOL_LABEL = addError(0, "ef.plaFueDatCol.label");
	public static final String PLAFUEDATCOL_NROCOLUMNA_LABEL = addError(0, "ef.plaFueDatCol.nroColumna.label");
	public static final String PLAFUEDATCOL_ORDEN_LABEL = addError(0, "ef.plaFueDatCol.orden.label");
	public static final String PLAFUEDATCOL_SUMAENTOTAL_LABEL = addError(0, "ef.plaFueDatCol.sumaEnTotal.label");
	public static final String PLAFUEDATCOL_COLNAME_LABEL = addError(0, "ef.plaFueDatCol.colName.label");
	// <--- PlaFueDatCol

	// ---> PlaFueDatCom
	public static final String PLAFUEDATCOM_LABEL = addError(0, "ef.plaFueDatCom.label");
	// <--- PlaFueDatCom
	
	// ---> PlaFueDatDet
	public static final String PLAFUEDATDET_LABEL 					= addError(0, "ef.plaFueDatDet.label");
	public static final String PLAFUEDATDET_PERIODOANIO_REQUERIDO 	= addError(0, "ef.plaFueDatDetEditAdapter.msg.periodoAnio.requerido");
	public static final String PLAFUEDATDET_RANGO_INVALIDO 			= addError(0, "ef.plaFueDatDetEditAdapter.msg.rangoInvalido.requerido");
	public static final String PLAFUEDATDET_PERIODOANIO_DESDE 		= addError(0, "ef.plaFueDatDetEditAdapter.periodoDesde.label");
	public static final String PLAFUEDATDET_PERIODOANIO_HASTA 		= addError(0, "ef.plaFueDatDetEditAdapter.periodoHasta.label");
	// <--- PlaFueDatDet

	// ---> Comparacion
	public static final String COMPARACION_LABEL 			= addError(0, "ef.comparacion.label");
	public static final String COMPARACION_DESCOMPARACION 	= addError(0, "ef.comparacion.desComparacion.label");
	public static final String COMPARACION_FECHA_LABEL 		= addError(0, "ef.comparacion.fecha.label");
	public static final String COMPARACION_DIFPOS_LABEL		= addError(0, "ef.comparacionAdapter.difPos.label");
	public static final String COMPARACION_DIFNEG_LABEL 	= addError(0, "ef.comparacionAdapter.difNeg.label");
	// <--- Comparacion	
	
	// ---> CompFuente
	public static final String COMPFUENTE_LABEL 				= addError(0, "ef.compFuente.label");
	public static final String COMPFUENTE_PERIODODESDE_LABEL 	= addError(0, "ef.compFuente.periodoDesde.label");
	public static final String COMPFUENTE_PERIODOHASTA_LABEL 	= addError(0, "ef.compFuente.periodoHasta.label");
	public static final String COMPFUENTE_ANIODESDE_LABEL 		= addError(0, "ef.compFuente.anioDesde.label");
	public static final String COMPFUENTE_ANIOHASTA_LABEL 		= addError(0, "ef.compFuente.anioHasta.label");
	public static final String COMPFUENTE_PERIODO_LABEL 		= addError(0, "ef.compFuenteEditAdapter.periodo.label");
	// <--- CompFuente

	// ---> CompFuenteRes
	public static final String COMPFUENTERES_LABEL = addError(0, "ef.compFuenteRes.label");

	// ---> Orden Control
	public static final String ORDENCONTROL_LABEL = addError(0, "ef.ordenControl.label");
	public static final String ORDENCONTROL_FECHACIERRE_LABEL = addError(0, "ef.ordenControl.fechaCierre.label");
	// <--- Orden Control
	
	// ---> OrdConBasImp
	public static final String ORDCONBASIMP_LABEL 					= addError(0, "ef.ordConBasImp.label");
	public static final String ORDCONBASIMP_PERIODODESDE_LABEL 		= addError(0, "ef.ordConBasImp.periodoDesde.label");
	public static final String ORDCONBASIMP_PERIODOHASTA_LABEL 		= addError(0, "ef.ordConBasImp.periodoHasta.label");
	public static final String ORDCONBASIMP_ANIODESDE_LABEL 		= addError(0, "ef.ordConBasImp.anioDesde.label");
	public static final String ORDCONBASIMP_ANIOHASTA_LABEL 		= addError(0, "ef.ordConBasImp.anioHasta.label");
	public static final String ORDCONBASIMP_MSG_NINGUNAFUENTE_SELEC = addError(0, "ef.ordConBasImpAdapter.msg.ningunaFuenteSelec");
	public static final String ORDCONBASIMP_MSG_PERIODO_INCOMPLETO 	= addError(0, "ef.ordConBasImpAdapter.msg.periodoIncompleto");
	public static final String ORDCONBASIMP_ACTIVIDAD_LABEL 		= addError(0, "ef.ordConBasImpAdapter.actividades.label");
	public static final String ORDCONBASIMP_MSG_SOLAPAMIENTO 		= addError(0, "ef.ordConBasImpAdapter.msg.solapamiento");
	public static final String ORDCONBASIMP_RANGO_NO_DEFINIDO		= addError(0, "ef.ordConBasImpAdapter.rangoNoIngresado");
	public static final String ORDCONBASIMP_COEFICIENTE				= addError(0, "ef.ordConBasImpAdapter.coeficiente.label");
	public static final String ORDCONBASIMP_UPDATE_ERROR			= addError(0, "ef.ordConBasImpAdapter.updateNoPermitido");
	// <--- OrdConBasImp
	
	// ---> DetAju
	public static final String DETAJU_LABEL 						= addError(0, "ef.detAju.label");
	public static final String DETAJU_MSG_BASESIMP_NO_DEFINIDAS 	= addError(0, "ef.detAjuEditAdapter.msg.basesImpNoDefinidas");
	// <--- DetAju
	
	// ---> DetAjuDet
	public static final String DETAJUDET_LABEL = addError(0, "ef.detAjuDet.label");
	// <--- DetAjuDet	
	
	// ---> AliComFueCol
	// <--- AliComFueCol
	public static final String ALICOMFUECOL_ANIODESDE_LABEL 			= addError(0, "ef.aliComFueCol.anioDesde.label");
	public static final String ALICOMFUECOL_ANIOHASTA_LABEL 			= addError(0, "ef.aliComFueCol.anioHasta.label");
	public static final String ALICOMFUECOL_PERIODODESDE_LABEL 			= addError(0, "ef.aliComFueCol.periodoDesde.label");
	public static final String ALICOMFUECOL_PERIODOHASTA_LABEL 			= addError(0, "ef.aliComFueCol.periodoHasta.label");
	public static final String ALICOMFUECOL_PERIODO_LABEL				= addError(0, "ef.aliComFueCol.periodo.label");
	public static final String ALICOMFUECOL_LABEL 						= addError(0, "ef.aliComFueCol.label");
	public static final String ALICOMFUECOL_MSG_SOLAPAMIENTO 			= addError(0, "ef.aliComFueColAdapter.msg.solapamiento");
	public static final String DETAJU_AGREGARMASIVO_CANTPERSONAL_LABEL 	= addError(0, "ef.detAjuAgregarMasivoAdapter.cantPers.label");
	public static final String DETAJU_AGREGARMASIVO_PORC_PUBL_LABEL 	= addError(0, "ef.detAjuAgregarMasivoAdapter.porcentajePubl.label");
	public static final String DETAJU_AGREGARMASIVO_PORC_MYS_LABEL 		= addError(0, "ef.detAjuAgregarMasivoAdapter.porcentajeMyS.label");
	public static final String DETAJU_AGREGARMASIVO_POR_MULTA_LABEL 	= addError(0, "ef.detAjuAgregarMasivoAdapter.porMulta.label");
	
	// ---> MesaEntrada
	public static final String MESAENTRADA_LABEL = addError(0, "ef.mesaEntrada.label");
	
	// ---> ABM AproOrdCon
	public static final String APROORDCON_LABEL = addError(0, "ef.aproOrdCon.label");
	
	// ---> ABM DetAjuDocSop
	public static final String DETAJUDOCSOP_LABEL = addError(0, "ef.detAjuDocSop.label");
	// <--- ABM DetAjuDocSop
	
	// ---> ABM DocSop
	public static final String DOCSOP_LABEL =  addError(0, "ef.docSop.label");
	public static final String DOCSOP_APLICAMULTA = addError(0, "ef.docSop.aplicaMulta.label"); 
	public static final String DOCSOP_COMPENSASALAFAV = addError(0, "ef.docSop.compensaSalAFav.label"); 
	public static final String DOCSOP_DESDOCSOP = addError(0, "ef.docSop.desDocSop.label"); 
	public static final String DOCSOP_DETERMINAAJUSTE = addError(0, "ef.docSop.determinaAjuste.label"); 
	public static final String DOCSOP_DEVUELVESALAFAV = addError(0, "ef.docSop.devuelveSalAFav.label"); 
	public static final String DOCSOP_PLANTILLA = addError(0, "ef.docSop.plantilla.label");

	// <--- ABM DocSop
	

	// ---> ComAju
	public static final String COMAJU_FECHA_APLICACION_LABEL 	= addError(0, "ef.comAju.fechaAplicacion.label");
	public static final String COMAJU_FECHA_SOLICITUD_LABEL 	= addError(0, "ef.comAju.fechaSolicitud.label");
	public static final String COMAJU_LABEL 					= addError(0, "ef.comAju.label");
	public static final String COMAJU_NOEXISTE_INDICECOMP_VIGENTE=addError(0,"ef.comAju.noHayIndice.error");
	

	
	
	
}

