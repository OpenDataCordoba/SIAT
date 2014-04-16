//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class RecError extends DemodaError {
	//	 Use Codigos desde 15000 hasta 15999
    // static public String XXXXXX_XXXX_XXX   = addError(13000, "pad.xxxxxx");

	// ---> Tipo Obra 	
	public static final String TIPOOBRA_LABEL 		     = addError(0, "rec.tipoObra.label");
	public static final String TIPOOBRA_DESTIPOOBRA 	 = addError(0, "rec.tipoObra.desTipoObra.label");
	public static final String TIPOOBRA_COSTOCUADRA 	 = addError(0, "rec.tipoObra.costoCuadra.label");
	public static final String TIPOOBRA_COSTOMETROFRENTE = addError(0, "rec.tipoObra.costoMetroFrente.label");
	public static final String TIPOOBRA_COSTOUT 	     = addError(0, "rec.tipoObra.costoUT.label");
	public static final String TIPOOBRA_COSTOMODULO 	 = addError(0, "rec.tipoObra.costoModulo.label");
	// <--- Tipo Obra

	// ---> Forma de Pago
	public static final String FORMAPAGO_LABEL = addError(0, "rec.formaPago.label");
	public static final String FORMAPAGO_ESCANTCUOTASFIJAS = addError(0, "rec.formaPago.esCantCuotasFijas.label");	
	public static final String FORMAPAGO_CANTCUOTAS = addError(0, "rec.formaPago.cantCuotas.label");
	public static final String FORMAPAGO_DESCUENTO = addError(0, "rec.formaPago.descuento.label");
	public static final String FORMAPAGO_ESESPECIAL = addError(0, "rec.formaPago.esEspecial.label");
	public static final String FORMAPAGO_INTERESFINANCIERO = addError(0, "rec.formaPago.interesFinanciero.label");
	public static final String FORMAPAGO_SELECCIONARRECURSO = addError(0, "rec.seleccionarRecurso.label");
	public static final String FORMAPAGO_EXISTENTE = addError(0,"rec.formaPago.existente.label");
	//	<--- Forma de pago

	// ---> Tipo Contrato
	public static final String TIPOCONTRATO_LABEL = addError(0, "rec.tipoContrato.label");
	// ---> Tipo Contrato	

	// ---> Contrato
	public static final String CONTRATO_LABEL = addError(0, "rec.contrato.label");
	public static final String CONTRATO_NUMERO = addError(0, "rec.contrato.numero.label");	
	public static final String CONTRATO_IMPORTE = addError(0, "rec.contrato.importe.label");
	public static final String CONTRATO_DESCRIPCION = addError(0, "rec.contrato.descripcion.label");
	//	<--- Contrato

	// ---> EstPlaCua
	public static final String ESTPLACUA_LABEL          = addError(0, "rec.estPlaCua.label");
	public static final String ESTPLACUA_DESESTPLACUA   = addError(0, "rec.estPlaCua.desEstPlaCua.label");	
	public static final String ESTPLACUA_ESESTADO       = addError(0, "rec.estPlaCua.esEstado.label");
	public static final String ESTPLACUA_PERMITEINCOSIS = addError(0, "rec.estPlaCua.permiteInconsis.label");
	public static final String ESTPLACUA_TRANSICIONES   = addError(0, "rec.estPlaCua.transiciones.label");	
	//	<--- EstPlaCua
	
	// ---> PlanillaCuadra
	public static final String PLANILLACUADRA_LABEL = addError(0, "rec.planillaCuadra.label");
	public static final String PLANILLACUADRA_DESCRIPCION = addError(0, "rec.planillaCuadra.descripcion.label");
	public static final String PLANILLACUADRA_FECHACARGA = addError(0, "rec.planillaCuadra.fechaCarga.label");
	public static final String PLANILLACUADRA_COSTOCUADRA = addError(0, "rec.planillaCuadra.costoCuadra.label");
	public static final String PLANILLACUADRA_CALLEPPAL = addError(0, "rec.planillaCuadra.callePpal.label");	
	public static final String PLANILLACUADRA_CALLEDESDE = addError(0, "rec.planillaCuadra.calleDesde.label");
	public static final String PLANILLACUADRA_CALLEHASTA = addError(0, "rec.planillaCuadra.calleHasta.label");
	public static final String PLANILLACUADRA_OBSERVACION = addError(0, "rec.planillaCuadra.observacion.label");	
	public static final String PLANILLACUADRA_MANZANA1 = addError(0, "rec.planillaCuadra.manzana1.label");
	public static final String PLANILLACUADRA_MANZANA2 = addError(0, "rec.planillaCuadra.manzana2.label");
	public static final String PLANILLACUADRA_CALLESREQUERIDAS = addError(0, "rec.planillaCuadra.callesRequeridas.label");	
	public static final String PLANILLACUADRA_NO_DISPONIBILIZADA_A_EMISION = addError(0, "rec.planillaCuadra.noDisponibilizadaAEmision");
	public static final String PLANILLACUADRA_INFO_CATASTRAL_NO_COMPLETA = addError(0, "rec.planillaCuadra.infoCatastralNoCompleta");
	public static final String PLANILLACUADRA_NO_CONSISTENTE = addError(0, "rec.planillaCuadra.noConsistente");
	public static final String PLANILLACUADRA_SIN_CATASTRALES_INFORMADAS = addError(0, "rec.planillaCuadra.sinCatastralesInformadas");
	public static final String PLANILLACUADRA_SIN_REPARTIDOR = addError(0, "rec.planillaCuadra.sinRepartidor");
	public static final String PLANILLACUADRA_REPARTIDOR_NO_VIGENTE = addError(0, "rec.planillaCuadra.repartidorNoVigente");
	public static final String PLANILLACUADRA_ELIMINAR_EN_ANULACION = addError(0, "rec.planillaCuadra.eliminarEnAnulacion");
	public static final String PLANILLACUADRA_MSG_MANZANAS_SIN_RESOLVER = addError(0, "rec.planillaCuadra.manzanasSinResolver");
	public static final String PLANILLACUADRA_CATASTRALES_NO_ENCONTRADAS = addError(0, "rec.planillaCuadra.catastralesNoEncontradas");
	//	<--- PlanillaCuadra

	
	// ---> HisEstPlaCua
	public static final String HISESTPLACUA_LABEL       = addError(0, "rec.hisEstPlaCua.label");
	public static final String HISESTPLACUA_DESCRIPCION = addError(0, "rec.hisEstPlaCua.descripcion.label");	
	public static final String HISESTPLACUA_FECHAESTADO = addError(0, "rec.hisEstPlaCua.fechaEstado.label");
	//	<--- HisEstPlaCua
	
	// ---> EstPlaCua
	public static final String ESTPLACUADET_LABEL           = addError(0, "rec.estPlaCuaDet.label");
	public static final String ESTPLACUADET_DESESTPLACUADET = addError(0, "rec.estPlaCuaDet.desEstPlaCuaDet.label");	
	
	// ---> PlaCuaDet
	public static final String PLACUADET_LABEL            = addError(0, "rec.plaCuaDet.label");
	public static final String PLACUADET_CUENTATGI = addError(0, "rec.plaCuaDet.cuentaTGI.label");	
	public static final String PLACUADET_CATASTRAL = addError(0, "rec.plaCuaDet.catastral.label");	
	public static final String PLACUADET_TIPPLACUADET = addError(0, "rec.plaCuaDet.tipPlaCuaDet.label");	
	public static final String PLACUADET_CANTIDADMETROS = addError(0, "rec.plaCuaDet.cantidadMetros.label");	
	public static final String PLACUADET_CANTIDADUNIDADES = addError(0, "rec.plaCuaDet.cantidadUnidades.label");	
	public static final String PLACUADET_USOCDM = addError(0, "rec.plaCuaDet.usoCdM.label");
	public static final String PLACUADET_VALUACIONTERRENO = addError(0, "rec.plaCuaDet.valuacionTerreno.label");	
	public static final String PLACUADET_AGRUPADOR = addError(0, "rec.plaCuaDet.agrupador.label");	
	public static final String PLACUADET_PORCPH = addError(0, "rec.plaCuaDet.porcPH.label");	
	public static final String PLACUADET_FECHAINCORPORACION = addError(0, "rec.plaCuaDet.fechaIncorporacion.label");	
	public static final String PLACUADET_FECHULTMDFDATOS = addError(0, "rec.plaCuaDet.fechaUltMdfDatos.label");	
	public static final String PLACUADET_FECHULTCMBOI = addError(0, "rec.plaCuaDet.fechaUltCmbOI.label");	
	public static final String PLACUADET_FECHAEMISION = addError(0, "rec.plaCuaDet.fechaEmision.label");
	public static final String PLACUADET_LISTPLACUADET = addError(0, "rec.plaCuaDet.listPlaCuaDet.label");	
	public static final String PLACUADET_UBICACION = addError(0, "rec.plaCuaDet.ubicacion.label");	
	public static final String PLACUADET_ESTADO = addError(0, "rec.plaCuaDet.estado.label");
	public static final String PLACUADET_SELECCIONAR_ITEM = addError(0, "rec.plaCuaDet.seleccionarItem.label");
	public static final String PLACUADET_NO_VIGENTE = addError(0, "rec.plaCuaDet.noVigente");
	public static final String PLACUADET_ELIMINAR_EN_ANULACION = addError(0, "rec.plaCuaDet.eliminarEnAnulacion");
	
	// ---> HisCamPla
	public static final String HISCAMPLA_LABEL        = addError(0, "rec.HisCamPla.label");
	// <--- HisCamPla
	
	// ---> TipPlaCuaDet	
	public static final String TIPPLACUADET_LABEL            = addError(0, "rec.tipPlaCuaDet.label");
	public static final String TIPPLACUADET_DESTIPPLACUADET = addError(0, "rec.tipPlaCuaDet.desTipPlaCuaDet.label");	
	// <--- TipPlaCuaDet

	// ---> Obra
	public static final String OBRA_LABEL = addError(0, "rec.obra.label");
	public static final String OBRA_DESOBRA = addError(0, "rec.obra.desObra.label");
	public static final String OBRA_NUMEROOBRA = addError(0, "rec.obra.numeroObra.label");
	public static final String OBRA_PERMITECAMPLAMAY = addError(0, "rec.obra.permiteCamPlaMay.label");
	public static final String OBRA_FECHADESDE = addError(0, "rec.obra.fechaDesde.label");
	public static final String OBRA_FECHAHASTA = addError(0, "rec.obra.fechaHasta.label");
	public static final String OBRA_FECHAINICIOCOBRO = addError(0, "rec.obra.fechaInicioCobro.label");
	public static final String OBRA_ESPORVALUACION = addError(0, "rec.obra.esPorValuacion.label");
	public static final String OBRA_ESCOSTOESP = addError(0, "rec.obra.esCostoEsp.label");
	public static final String OBRA_COSTOESP = addError(0, "rec.obra.costoEsp.label");
	public static final String OBRA_LISTOBRAFORMAPAGO = addError(0, "rec.obra.listObraFormaPago.label");
	public static final String OBRA_LISTPLANILLAOBRA = addError(0, "rec.obra.listPlanillaObra.label");
	public static final String OBRA_SIN_PLANILLAS = addError(0, "rec.obra.sinPlanillas");
	//	<--- Obra
	
	// ---> OBRA FORMA PAGO
	public static final String OBRAFORMAPAGO_LABEL = addError(0, "rec.obraFormaPago.label");
	public static final String OBRAFORMAPAGO_CANTCUOTAS = addError(0, "rec.obraFormaPago.cantCuotas.label");
	public static final String OBRAFORMAPAGO_ESCANTCUOTASFIJAS = addError(0, "rec.obraFormaPago.esCantCuotasFijas.label");	
	
	public static final String OBRAFORMAPAGO_DESCUENTO = addError(0, "rec.obraFormaPago.descuento.label");
	public static final String OBRAFORMAPAGO_ESESPECIAL = addError(0, "rec.obraFormaPago.esEspecial.label");
	public static final String OBRAFORMAPAGO_FECHADESDE = addError(0, "rec.obraFormaPago.fechaDesde.label");
	public static final String OBRAFORMAPAGO_FECHAHASTA = addError(0, "rec.obraFormaPago.fechaHasta.label");
	public static final String OBRAFORMAPAGO_INTERESFINANCIERO = addError(0, "rec.obraFormaPago.interesFinanciero.label");
	public static final String OBRAFORMAPAGO_MONTOMINIMOCUOTA = addError(0, "rec.obraFormaPago.montoMinimoCuota.label");
	//	<--- OBRA FORMA PAGO
	
	// ---> Obra PlanillaCuadra
	public static final String OBRAPLANILLACUADRA_LABEL = addError(0, "rec.obraPlanillaCuadra.label");
	public static final String OBRAPLANILLACUADRA_SELECCIONARITEM = addError
		(0, "rec.obraPlanillaCuadra.seleccionarItem.label");
	// <--- Obra PlanillaCuadra	

	// ---> Estado obra
	public static final String ESTADOOBRA_LABEL          = addError(0, "rec.estadoObra.label");
	public static final String ESTADOOBRA_DESESTADOOBRA  = addError(0, "rec.estadoObra.desEstadoObra.label");
	// <--- Estado obra
	 
	// ---> HisEstadoObra
	public static final String HISESTADOOBRA_LABEL       = addError(0, "rec.hisEstadoObra.label");
	public static final String HISESTADOOBRA_DESCRIPCION = addError(0, "rec.hisEstadoObra.descripcion.label");	
	public static final String HISESTADOOBRA_FECHAESTADO = addError(0, "rec.hisEstadoObra.fechaEstado.label");
	//	<--- HisEstadoObra

	// ---> Asignar / desasignar repartidor a planillas
	public static final String ASIGNARDESASIGNARREPARTIDOR_SELECCIONARITEM = addError (0, "rec.asignarDesasignarRepartidor.seleccionarItem.label");
	// <--- Asignar / desasignar repartidor a planillas	
	
	// ---> HisObrRepPla
	public static final String HISOBRREPPLA_LABEL       = addError(0, "rec.hisObrRepPla.label");
	public static final String HISOBRREPPLA_FECHAASIGNACION = addError(0, "rec.hisObrRepPla.fechaAsignacion.label");
	//	<--- HisObrRepPla

	// ---> ObrRepVen
	public static final String OBRREPVEN_LABEL = addError(0, "rec.obrRepVen.label");
	public static final String OBRREPVEN_OBRA = addError(0, "rec.obrRepVen.obra.label");
	public static final String OBRREPVEN_CUOTADESDE = addError(0, "rec.obrRepVen.cuotaDesde.label");
	public static final String OBRREPVEN_NUEFECVEN = addError(0, "rec.obrRepVen.nueFecVen.label");
	public static final String OBRREPVEN_CASO = addError(0, "rec.obrRepVen.caso.label");
	public static final String OBRREPVEN_DESCRIPCION = addError(0, "rec.obrRepVen.descripcion.label");
	public static final String OBRREPVEN_CANDEUACT = addError(0, "rec.obrRepVen.canDeuAct.label");
	// <--- ObrRepVen
	
	// ---> Uso CdM
	public static final String USOCDM_LABEL = addError(0,"rec.usoCdM.label");
	public static final String USOCDM_DESUSOCDM = addError(0,"rec.usoCdM.desUsoCdM.label");
	public static final String USOCDM_FACTOR = addError(0,"rec.usoCdM.factor.label");
	public static final String USOCDM_USOSCATASTRO = addError(0, "rec.usoCdM.usosCatastro.label");
	// <--- Uso CdM

	// ---> AnulacionObra
	public static final String ANULACIONOBRA_LABEL = addError(0,"rec.anulacionObra.label");
	public static final String ANULACIONOBRA_FECHAANULACION = addError(0,"rec.anulacionObra.fechaAnulacion.label");
	public static final String ANULACIONOBRA_OBRA = addError(0,"rec.anulacionObra.obra.label");
	public static final String ANULACIONOBRA_FECHAVENCIMIENTO = addError(0,"rec.anulacionObra.fechaVencimiento.label");

	public static final String ANULACIONOBRA_SEARCHPAGE_FECHADESDE = addError(0,"rec.anulacionObraSearchPage.fechaDesde.label"); 
	public static final String ANULACIONOBRA_SEARCHPAGE_FECHAHASTA = addError(0,"rec.anulacionObraSearchPage.fechaHasta.label");
	public static final String ANULACIONOBRA_EN_PROCESO = addError(0,"rec.anulacionObra.enProceso");
	// <--- AnulacionObra
	
	// ---> AnuObrDet
	public static final String ANUOBRDET_LABEL = addError(0,"rec.anuObrDet.label");
	public static final String ANUOBRDET_ANULACIONOBRA = addError(0,"rec.anuObrDet.anulacionObra.label");
	public static final String ANUOBRDET_IDDEUDA = addError(0,"rec.anuObrDet.idDeuda.label");
	// <--- AnuObrDet

	// ---> NovedadRS
	public static final String ACTIVIDADDREI_LABEL = addError(0,"rec.actividadDreiAdapter.legend");
	public static final String NOVEDADRS_CUENTA_LABEL = addError(0,"rec.novedadRSAdapter.nroCuenta.label");
	public static final String NOVEDADRS_MSG_APLICAR = addError(0,"rec.novedadRSAdapter.aplicar.msg");
	public static final String NOVEDADRS_MSG_APLICAR_MASIVO = addError(0,"rec.novedadRSAdapter.aplicarMasivo.msg");
	public static final String NOVEDADRS_APLICAR_ERROR = addError(0,"rec.novedadRSAdapter.aplicar.error");
	public static final String NOVEDADRS_APLICAR_MASIVO_LOCK_ERROR = addError(0,"rec.novedadRSAdapter.aplicarMasivo.lock.error");
	public static final String NOVEDADRS_APLICAR_MASIVO_EN_EJECUCION_ERROR = addError(0,"rec.novedadRSAdapter.aplicarMasivo.enEjecucion.error");
	public static final String NOVEDADRS_APLICAR_MASIVO_VALIDACION_ERROR = addError(0,"rec.novedadRSAdapter.aplicarMasivo.validacion.error");
	public static final String NOVEDADRS_APLICAR_MASIVO_ASENTAMIENTO_EN_EJECUCION_ERROR = addError(0,"rec.novedadRSAdapter.aplicarMasivo.asentamientoEjecucion.error");
	// <--- NovedadRS
	
	
	// ---> ABM CategoriaRS
	public static final String CATRSDREI_LABEL  			= addError(0,"rec.catRSDrei.catRSDrei.legend.label");
	public static final String CATEGORIARS_NRO        		= addError(4054, "rec.catRSDrei.nroCatRSDrei.label");
	public static final String CATEGORIARS_IMP        		= addError(4054, "rec.catRSDrei.importe.label");
	public static final String CATEGORIARS_CANT_EMPLEADOS   = addError(4054, "rec.catRSDrei.cantEmpleados.label");
	public static final String CATEGORIARS_FECHA_DESDE      = addError(4054, "rec.catRSDreiSearchPage.fechacatRSDreiDesde.label");
	public static final String CATEGORIARS_FECHA_HASTA      = addError(4054, "rec.catRSDreiSearchPage.fechacatRSDreiHasta.label");	
	// <--- ABM CategoriaRS
}
