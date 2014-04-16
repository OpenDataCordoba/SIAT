//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.view.util;


public interface ProConstants {
	
	//	ACTs Generales para Procesos --------------------------------------------------------
	public static final String ACT_ACTIVAR 		  = "activar";
	public static final String ACT_REINICIAR      = "reiniciar";
	public static final String ACT_REPROGRAMAR    = "reprogramar";
	public static final String ACT_CANCELAR       = "cancelar";
	public static final String ACT_SIGUIENTE 	  = "siguiente";
	public static final String ACT_LOGFILE 	  = "logFile";
	
	// --> Corrida
	public static final String ACTION_ADMINISTRAR_CORRIDA = "administrarCorrida";
	public static final String FWD_CORRIDA_SEARCHPAGE = "corridaSearchPage";
	public static final String FWD_CORRIDA_VIEW_ADAPTER = "corridaViewAdapter";

	public static final String FWD_ADPCORRIDA_VIEW_ADAPTER = "adpCorridaViewAdapter";
	public static final String FWD_ADPCORRIDA_ESTADOPASO_ADAPTER = "adpCorridaEstadoPasoAdapter";
	public static final String FWD_ADPCORRIDA_CONF_VIEW_ADAPTER = "adpCorridaConfViewAdapter";
	// <-- Corrida
	
	public static final String ACTION_ADMINISTRAR_ADP_CORRIDA  = "administrarAdpCorridaAsentamiento";
	
	public static final String ACTION_ADMINISTRAR_ADP_PRO_MAS  = "administrarAdpCorridaProcesoMasivo";
	
	public static final String ACTION_ADMINISTRAR_ADP_ANU_OBR  = "administrarAdpCorridaAnulacionObra";
	
	public static final String ACTION_ADMINISTRAR_ADP_EMISION  = "administrarAdpCorridaEmision";
	
	public static final String ACTION_ADMINISTRAR_ADP_PRESCRIPCION  = "administrarAdpCorridaPrescripcion";
	
	public static final String ACTION_ADMINISTRAR_ADP_IMPMASDEU  = "administrarAdpCorridaImpMasDeu";
	
	public static final String ACTION_ADMINISTRAR_ADP_RESLIQDEU  = "administrarAdpCorridaResLiqDeu";
	
	public static final String ACTION_ADMINISTRAR_ADP_PROPASDEB = "administrarAdpCorridaProPasDeb";
	
	public static final String ACTION_ADMINISTRAR_ADP_LIQCOM = "administrarAdpCorridaLiqCom";
	
	public static final String ACTION_ADMINISTRAR_ADP_OPEINVBUS = "administrarAdpCorridaOpeInvBus";
	
	public static final String ACTION_ADMINISTRAR_ADP_BAL  = "administrarAdpCorridaBalance";
	
	public static final String ACTION_ADMINISTRAR_ADP_ASEDEL  = "administrarAdpCorridaAseDel";

	// --> ABM Proceso
	public static final String FWD_PROCESO_SEARCHPAGE = "procesoSearchPage";
	public static final String ACTION_ADMINISTRAR_PROCESO = "administrarProceso";
	public static final String FWD_PROCESO_VIEW_ADAPTER = "procesoViewAdapter";
	public static final String FWD_PROCESO_EDIT_ADAPTER = "procesoEditAdapter";
	// <-- ABM Proceso

	// --> Envio de Archivos
	public static final String ACTION_ADMINISTRAR_ENVIO_ARCHIVOS 	= "administrarEnvioArchivos";
	public static final String ACT_ENVIAR_ARCHIVOS 					= "enviarArchivos";
	public static final String FWD_ENVIO_ARCHIVOS_EDIT_ADAPTER 		= "envioArchivosEditAdapter";
	public static final String FWD_ENVIO_ARCHIVOS_PREVIEW_ADAPTER 	= "envioArchivosPreviewAdapter";
	// <-- Envio de Archivos
}
