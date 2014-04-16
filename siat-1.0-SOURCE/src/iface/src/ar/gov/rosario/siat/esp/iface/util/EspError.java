//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>


package ar.gov.rosario.siat.esp.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class EspError extends DemodaError {
	
	// ---> TipoHab
	public static final String TIPOHAB_LABEL  	    = addError(20000, "esp.tipoHab.label");
	public static final String TIPOHAB_CODIGO  	= addError(20001, "esp.tipoHab.codigo.label");
	public static final String TIPOHAB_DESCRIPCION  	= addError(20001, "esp.tipoHab.descripcion.label");
	// <--- TipoHab
	
	// ---> TipoEntrada
	public static final String TIPOENTRADA_LABEL  	    = addError(20000, "esp.tipoEntrada.label");
	public static final String TIPOENTRADA_CODIGO  	= addError(20001, "esp.tipoEntrada.codigo.label");
	public static final String TIPOENTRADA_DESCRIPCION  	= addError(20001, "esp.tipoEntrada.descripcion.label");
	// <--- TipoEntrada
	
	// ---> TipoCobro
	public static final String TIPOCOBRO_LABEL  	    = addError(20000, "esp.tipoCobro.label");
	public static final String TIPOCOBRO_CODIGO  	= addError(20001, "esp.tipoCobro.codigo.label");
	public static final String TIPOCOBRO_DESCRIPCION  	= addError(20001, "esp.tipoCobro.descripcion.label");
	// <--- TipoCobro

	// ---> ValoresCargados
	public static final String VALORESCARGADOS_LABEL  	    = addError(20000, "esp.valoresCargados.label");
	public static final String VALORESCARGADOS_DESCRIPCION  	= addError(20001, "esp.valoresCargados.descripcion.label");
	// <--- ValoresCargados
	
	// ---> Habilitacion
	public static final String HABILITACION_LABEL  	  	 	= addError(20000, "esp.habilitacion.label");
	public static final String HABILITACION_NUMERO  		= addError(20001, "esp.habilitacion.numero.label");
	public static final String HABILITACION_ANIO  			= addError(20001, "esp.habilitacion.anio.label");
	public static final String HABILITACION_FECHAHAB  		= addError(20001, "esp.habilitacion.fechaHab.label");
	public static final String HABILITACION_FECEVEDES  		= addError(20001, "esp.habilitacion.fecEveDes.label");
	public static final String HABILITACION_FECEVEHAS  		= addError(20001, "esp.habilitacion.fecEveHas.label");
	public static final String HABILITACION_DESCRIPCION  	= addError(20001, "esp.habilitacion.descripcion.label");
	public static final String HABILITACION_LUGAREVENTO  	= addError(20001, "esp.habilitacion.lugarEvento.label");
	public static final String HABILITACION_OBSERVACIONES  	= addError(20001, "esp.habilitacion.observaciones.label");
	public static final String HABILITACION_HORAACCESO  	= addError(20001, "esp.habilitacion.horaAcceso.label");
	public static final String HABILITACION_CUIT  			= addError(20001, "esp.habilitacion.cuit.label");
	public static final String HABILITACION_PERHAB  		= addError(20001, "esp.habilitacion.perHab.label");
	public static final String HABILITACION_RECURSO_CONTRIB = addError(0	, "esp.habilitacion.recursoContrib.label");
	public static final String HABILITACION_ADVERTENCIA_DEUDA_VEN = addError(0	, "esp.habilitacion.advertenciaDeudaVen.label");
	public static final String HABILITACION_EMISION_INICIAL_MSG = addError(0	, "esp.habilitacion.emisionInicial.msg");
	
	public static final String HABILITACION_FILTRO_REPORTE_REQ = addError(0	, "esp.habilitacionReport.filtroReq.label");
	
	public static final String HABILITACION_SEARCHPAGE_FECHADESDE = addError(0, "esp.habilitacionSearchPage.fechaDesde.label");
	public static final String HABILITACION_SEARCHPAGE_FECHAHASTA = addError(0, "esp.habilitacionSearchPage.fechaHasta.label");
	public static final String HABILITACION_SEARCHPAGE_FECHAEVENTODESDE = addError(0, "esp.habilitacionSearchPage.fechaEventoDesde.label");
	public static final String HABILITACION_SEARCHPAGE_FECHAEVENTOHASTA = addError(0, "esp.habilitacionSearchPage.fechaEventoHasta.label");
	
	// <--- Habilitacion
	
	// ---> PrecioEvento
	public static final String PRECIOEVENTO_LABEL  	      = addError(20000, "esp.precioEvento.label");
	public static final String PRECIOEVENTO_PRECIOPUBLICO = addError(20001, "esp.precioEvento.precioPublico.label");
	public static final String PRECIOEVENTO_PRECIO		  = addError(20001, "esp.precioEvento.precio.label");
	// <--- PrecioEvento

	// ---> Entradas Habilitadas
	public static final String ENTHAB_LABEL  	    = addError(20000, "esp.entHab.label");
	public static final String ENTHAB_NRODESDE  	= addError(20001, "esp.entHab.nroDesde.label");
	public static final String ENTHAB_NROHASTA  	= addError(20001, "esp.entHab.nroHasta.label");
	public static final String ENTHAB_SERIE  	= addError(20001, "esp.entHab.serie.label");
	public static final String ENTHAB_DESCRIPCION  	= addError(20001, "esp.entHab.descripcion.label");
	public static final String ENTHAB_TOTALVENDIDAS  	= addError(20001, "esp.entHab.totalVendidas.label");
	public static final String ENTHAB_EXISTENTE  	= addError(20001, "esp.entHab.existente.label");
	public static final String ENTHAB_SUPERA_MAX_PERM  	= addError(20001, "esp.entHab.superaMaxPerm.label");
	// <--- Entradas Habilitadas

	// ---> Entradas Vendidas
	public static final String ENTVEN_LABEL  	    	= addError(20000, "esp.entVen.label");
	public static final String ENTVEN_NRODESDE  		= addError(20001, "esp.entVen.nroDesde.label");
	public static final String ENTVEN_NROHASTA  		= addError(20001, "esp.entVen.nroHasta.label");
	public static final String ENTVEN_SERIE  			= addError(20001, "esp.entVen.serie.label");
	public static final String ENTVEN_DESCRIPCION  		= addError(20001, "esp.entVen.descripcion.label");
	public static final String ENTVEN_TOTALVENDIDAS  	= addError(20001, "esp.entVen.totalVendidas.label");
	public static final String ENTVEN_IMPORTECOBRADO  	= addError(20001, "esp.entVen.importeCobrado.label");
	public static final String ENTVEN_FECHAEMISION  	= addError(20001, "esp.entVen.fechaEmision.label");
	public static final String ENTVEN_FECHAANULACION  	= addError(20001, "esp.entVen.fechaAnulacion.label");
	public static final String ENTVEN_VENDIDAS  		= addError(20001, "esp.entVen.vendidasIngresadas.label");
	public static final String ENTVEN_FECHAVENCIMIENTO  = addError(0, 	  "esp.entVen.fechaVencimiento.label");
	
	public static final String ENTVEN_DDJJ_HABILITACION_ERROR = addError(0, "esp.ddjjEntVenAdapter.noExisteHabilitacion.label");
	public static final String ENTVEN_DDJJ_MSG_APLICA_DES = addError(0, "esp.ddjjEntVenAdapter.aplicaDescuento.label");
	public static final String ENTVEN_DDJJ_MSG_NO_APLICA_DES = addError(0, "esp.ddjjEntVenAdapter.noAplicaDescuento.label");
	// <--- Entradas Vendidas
	
	// ---> Tipo Evento
	public static final String TIPOEVENTO_DESCRIPCION  	= addError(20001, "esp.tipoEvento.descripcion.label");
	public static final String TIPOEVENTO_LABEL  	    = addError(20000, "esp.tipoEvento.label");
	// <--- Tipo Evento
	
	// ---> HabExe
	public static final String HABEXE_LABEL  	    = addError(20000, "exe.exencion.label");
	public static final String HABEXE_FECHADESDE  	= addError(20001, "esp.habExe.fechaDesde.label");
	public static final String HABEXE_FECHAHASTA  	= addError(20001, "esp.habExe.fechaHasta.label");
	// <--- HabExe
	
	// ---> LugarEvento
	public static final String LUGAREVENTO_LABEL  	    = addError(20000, "esp.lugarEvento.label");
	public static final String LUGAREVENTO_DESCRIPCION  	= addError(20001, "esp.lugarEvento.descripcion.label");
	public static final String LUGAREVENTO_DOMICILIO  	= addError(20001, "esp.lugarEvento.domicilio.label");
	public static final String LUGAREVENTO_FACTOROCUPACIONAL  	= addError(20001, "esp.lugarEvento.factorOcupacional.label");
	// <--- LugarEvento
}
