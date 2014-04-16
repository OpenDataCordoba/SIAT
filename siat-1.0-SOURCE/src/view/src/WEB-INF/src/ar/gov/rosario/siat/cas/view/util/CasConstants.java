//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.view.util;


public interface CasConstants {
    
	// ---> Solicitud
	public static final String ACTION_BUSCAR_SOLICITUD     	= "buscarSolicitud";
	public static final String ACTION_ADMINISTRAR_SOLICITUD = "administrarSolicitud";
	public static final String FWD_SOLICITUD_SEARCHPAGE 	= "solicitudSearchPage";
	public static final String FWD_SOLICITUD_VIEW_ADAPTER 	= "solicitudViewAdapter";
	public static final String FWD_SOLICITUD_EDIT_ADAPTER 	= "solicitudEditAdapter";
	public static final String ACT_CAMBIARESTADO_SOLICITUD  = "cambiarEstado";
	public static final String ACT_SOLICITUD_PEND_AREA  	= "getPendientesArea";
	public static final String ACT_SOLICITUD_EMITIDAS_AREA  = "getEmitidasArea";
	public static final String FWD_SOLICITUD_PEND_AREA		= "solicitudPendArea";// Se utiliza para ver las solicitudes pendientes del area (post-login)
	public static final String FWD_SOLICITUD_EMITIDAS_AREA 	= "solicitudEmitidasArea"; //Se utiliza para ver las solicitudes emitidas por el area (post-login)
	// <--- Solicitud

	// ---> UsoExpediente
	public static final String ACTION_BUSCAR_USOEXPEDIENTE     	= "buscarUsoExpediente";
	public static final String ACTION_ADMINISTRAR_USOEXPEDIENTE = "administrarUsoExpediente";
	public static final String FWD_USOEXPEDIENTE_SEARCHPAGE 	= "usoExpedienteSearchPage";
	public static final String FWD_USOEXPEDIENTE_VIEW_ADAPTER 	= "usoExpedienteViewAdapter";	
	// <--- UsoExpediente
	
	// ---> TipoSolicitud
	public static final String FWD_TIPOSOLICITUD_VIEW_ADAPTER 	= "tipoSolicitudViewAdapter";
	public static final String FWD_TIPOSOLICITUD_EDIT_ADAPTER 	= "tipoSolicitudEditAdapter";
	public static final String FWD_TIPOSOLICITUD_SEARCHPAGE   	= "tipoSolicitudSearchPage";
	public static final String ACTION_ADMINISTRAR_TIPOSOLICITUD = "administrarTipoSolicitud";
	public static final String ACTION_ADMINISTRAR_ENC_TIPOSOLICITUD = "administrarEncTipoSolicitud";
	public static final String FWD_TIPOSOLICITUD_ENC_EDIT_ADAPTER   = "tipoSolicitudEncEditAdapter";
	public static final String PATH_ADMINISTRAR_TIPOSOLICITUD = "/cas/AdministrarTipoSolicitud";
	// <--- TipoSolicitud
	
	
	// ---> AreaSolicitud
	public static final String ACTION_ADMINISTRAR_AREASOLICITUD = "administrarAreaSolicitud";
	public static final String FWD_AREASOLICITUD_VIEW_ADAPTER = "areaSolicitudViewAdapter";
	public static final String FWD_AREASOLICITUD_EDIT_ADAPTER = "areaSolicitudEditAdapter";
	
	// <--- AreaSolicitud
	
	// ---> Reporte de Solicitudes Pendientes
	public static final String FWD_SOLPEND_REPORT = "solPendReport";
	// <--- Reporte de Solicitudes Pendientes
	
}