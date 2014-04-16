//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.view.util;


public interface CyqConstants {
    

	
	/*/ ---> Mantenedor de Procemiento CyQ (SINC)
	public static final String FWD_PRODECIMIENTO_CYQ_SEARCHPAGE = "procedimientoSearchPage";
	public static final String ACTION_ADMINISTRAR_PRODECIMIENTO_CYQ = "administrarProcedimiento";
	public static final String FWD_PRODECIMIENTO_CYQ_EDIT_ADAPTER = "procedimientoEditAdapter";
	public static final String FWD_PRODECIMIENTOCYQ_VIEW_ADAPTER = "procedimientoViewAdapter";
	// <--- Mantenedor de Procemiento CyQ (SINC)*/
		
	// ---> Procedimiento (Encabezado)
	public static final String ACTION_BUSCAR_PROCEDIMIENTO     		= "buscarProcedimiento";
	public static final String FWD_PROCEDIMIENTO_SEARCHPAGE 	    = "procedimientoSearchPage";
	public static final String FWD_PROCEDIMIENTO_AVA_SEARCHPAGE 	= "procedimientoAvaSearchPage";
	public static final String ACTION_ADMINISTRAR_PROCEDIMIENTO 	= "administrarProcedimiento";
	public static final String FWD_PROCEDIMIENTO_VIEW_ADAPTER 		= "procedimientoViewAdapter";
	public static final String FWD_PROCEDIMIENTO_ENC_EDIT_ADAPTER 	= "procedimientoEncEditAdapter";
	public static final String FWD_PROCEDIMIENTO_ADAPTER 		    = "procedimientoAdapter";
	public static final String ACTION_ADMINISTRAR_ENC_PROCEDIMIENTO = "administrarEncProcedimiento";
	public static final String ACTION_ADMINISTRAR_BAJA_PROCEDIMIENTO = "administrarBajaProcedimiento";
	public static final String ACTION_ADMINISTRAR_CONVERSION_PROCEDIMIENTO = "administrarConversionProcedimiento";
	public static final String ACTION_ADMINISTRAR_INFORME_PROCEDIMIENTO = "administrarInformeProcedimiento";
	public static final String PATH_ADMINISTRAR_PROCEDIMIENTO     	= "/cyq/AdministrarProcedimiento"; // utilizado para redirigir en el agregar Procedimiento
	public static final String METOD_PROCEDIMIENTO_PARAM_CONTRIBUYENTE = "paramContribuyente";
	public static final String ACT_BAJA = "baja";
	public static final String ACT_CONVERSION = "conversion";
	public static final String ACT_INFORME = "informe";
	
	public static final String FWD_PROCEDIMIENTO_BAJA_ADAPTER = "procedimientoBajaAdapter";
	public static final String FWD_PROCEDIMIENTO_CONVERSION_ADAPTER = "procedimientoConversionAdapter";
	public static final String FWD_PROCEDIMIENTO_INFORME_ADAPTER = "procedimientoInformeAdapter";
	
	public static final String FWD_DEDUDA_CYQ_CUENTA_SEARCH	= "deudaCyqCuentaSearch";
	public static final String FWD_MSG_VALIDAR_DEDUDA_CYQ_ADAPTER = "messageDeudaCyqAdapter";
	public static final String FWD_DEDUDA_CYQ_ADAPTER	 		= "deudaCyqAdapter";
	public static final String FWD_INICIALIZAR_DEDUA_CYQ 		= "/cyq/AdministrarDeudaCyq.do?method=inicializar";
	public static final String PATH_DEUDA_CYQ_ADAPTER 			= "/cyq/AdministrarDeudaCyq";	
	public static final String ACTION_BUSCAR_DEUDA = "buscarDeuda";
	
	
	public static final String ACTION_ADMINISTRAR_CAMBIOESTADO_PROCEDIMIENTO = "administrarCambioEstadoProcedimiento";
	public static final String FWD_CAMBIOESTADO_PROCEDIMIENTO_ADAPTER = "cambioEstadoProcedimientoAdapter";
	public static final String METOD_CAMBIAR_ESTADO = "cambiarEstado";
	
	public static final String ACTION_ADMINISTRAR_PROCUENODEU = "administrarProCueNoDeu";
	
	
	// Liquidacion deuda cyq
	public static final String PATH_VER_LIQDEUDACYQ = "/cyq/AdministrarLiqDeudaCyq.do?method=inicializar&selectedId=";
	public static final String ACTION_ADMINISTRAR_LIQDEUDACYQ = "administrarLiqDeudaCyq";
	public static final String FWD_LIQDEUDACYQ_ADAPTER = "liqDeudaCyqAdapter";
	public static final String ACTION_VER_CONVENIO_CUENTA = "verConvenio";
	
	public static final String ACTION_ADMINISTRAR_DEUDAPRIVILEGIO = "administrarDeudaPrivilegio";
	public static final String FWD_DEUDAPRIVILEGIO_VIEW_ADAPTER = "deudaPrivilegioViewAdapter"; 
	public static final String FWD_DEUDAPRIVILEGIO_EDIT_ADAPTER = "deudaPrivilegioEditAdapter";
	
	public static final String ACTION_ADMINISTRAR_PAGOPRIV = "administrarPagoPriv";
	public static final String FWD_PAGOPRIV_VIEW_ADAPTER = "pagoPrivViewAdapter";
	public static final String FWD_PAGOPRIV_EDIT_ADAPTER = "pagoPrivEditAdapter";
	public static final String METOD_PARAM_CUENTABANCO = "paramCuentaBanco";
	
	// Formalizacion de Convenio de Pago
	public static final String ACTION_ADMINISTRAR_CONVENIO = "administrarFormConvenio";
	public static final String ACTION_FORMCONVENIO = "formalizarConvenio";
	public static final String ACTION_FORMCONVENIOESP="formalizarConvenioEsp";
	
	public static final String ACTION_FORMCONVENIOCYQ = "formalizarConvenioCyq"; // Para volver desde la impresion despues de la formalizacion.
	
	public static final String FWD_FORMCONVENIO_ADAPTER ="liqFormConvenioAdapter";
	public static final String FWD_FORMCONVENIO_PLANES_ADAPTER ="liqFormConvenioPlanesAdapter";
	public static final String FWD_FORMCONVENIO_CUOTAS_ADAPTER ="liqFormConvenioCuotasAdapter";
	public static final String FWD_FORMCONVENIO_SIMULA_CUOTAS_ADAPTER ="liqFormConvenioSimulaCuotasAdapter";
	
	// Formalizacion de Convenio de Pago Especial
	public static final String ACTION_ADMINISTRAR_CONVENIO_ESP = "administrarFormConvenioEsp";
	public static final String FWD_FORMCONVENIOESP_ADAPTER ="liqFormConvenioEspAdapter";
	public static final String FWD_FORMCONVENIO_PLANES_ESP_ADAPTER ="liqFormConvenioPlanesEspAdapter";
	public static final String FWD_FORMCONVENIO_CUOTAS_ESP_ADAPTER = "liqFormConvenioCuotasEspAdapter"; // Muestra las cuotas resultado de plan esp
	public static final String FWD_FORMCONVENIO_CUOTAS_ESP_EDIT_ADAPTER = "liqFormConvenioCuotasEspEditAdapter";  // Permite ingresar los valores de de cada cuota
	
	// Formalizacion de FWD's comunes	
	public static final String FWD_FORMCONVENIO_FORMAL_ADAPTER ="liqFormConvenioFormalAdapter";
	public static final String FWD_FORMCONVENIO_PRINT_ADAPTER ="liqFormConvenioPrintAdapter";
	
	public static final String FWD_FORMCONVENIO_IMP_FORM = "liqFormConvenioImpForm";
	public static final String FWD_FORMCONVENIO_IMP_RECIBOS = "liqFormConvenioImpRecibos";	
	public static final String FWD_FORMCONVENIO_IMP_ALTCUOTAS = "liqFormConvenioImpAltCuotas";
	
	public static final String FWD_VER_DETALLE_PLAN = "verDetallePlan";	
	public static final String METOD_FORMCONVENIO_PARAM_PERSONA = "paramPersona";
	public static final String FWD_FORMCONVENIO_SELECCIONARPLAN = "seleccionarPlan";
	public static final String PATH_BUSCAR_CONVENIO="/gde/BuscarConvenio.do?method=buscar&selectedId=";
	public static final String FWD_PAGOACUENTAADAPTER="pagoACuentaInit";

	// <--- Procedimiento 
	
	// ---> ProCueNoDeu
	public static final String FWD_PROCUENODEU_VIEW_ADAPTER = "proCueNoDeuViewAdapter";
	public static final String FWD_PROCUENODEU_EDIT_ADAPTER = "proCueNoDeuEditAdapter";
	// <--- ProCueNoDeu
	
		// ---> Mantenedor de Juzgado
	public static final String FWD_JUZGADO_SEARCHPAGE = "juzgadoSearchPage";
	public static final String ACTION_ADMINISTRAR_JUZGADO = "administrarJuzgado";
	public static final String FWD_JUZGADO_EDIT_ADAPTER = "juzgadoEditAdapter";
	public static final String FWD_JUZGADO_VIEW_ADAPTER = "juzgadoViewAdapter";	
	
	// <--- Mantenedor de Juzgado
	
	// ---> Mantenedor de Abogado
	public static final String FWD_ABOGADO_SEARCHPAGE = "abogadoSearchPage";
	public static final String ACTION_ADMINISTRAR_ABOGADO = "administrarAbogado";
	public static final String FWD_ABOGADO_EDIT_ADAPTER = "abogadoEditAdapter";
	public static final String FWD_ABOGADO_VIEW_ADAPTER = "abogadoViewAdapter";	
	
	// <--- Mantenedor de Abogado
}