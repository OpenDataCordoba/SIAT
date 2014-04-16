//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.util;



/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso
 * 
 */
public class EfSecurityConstants {

	public static final String IMPRIMIR = "imprimir";
	
	// ---> ABM Inspectores
	public static final String ABM_INSPECTOR  = "ABM_Inspector";
	public static final String ABM_INSPECTOR_ENC  = "ABM_InspectorEnc";
	public static final String ABM_INSSUP = "ABM_InsSup";
	public static final String ABM_ALTAOFICIO = "ABM_AltaOficio";
	// <--- ABM Inspectores
	
	// ---> ABM Supervisor
	public static final String ABM_SUPERVISOR = "ABM_Supervisor";
	// <--- ABM Supervisor
	
	// ---> ABM FuenteInfo
	public static final String ABM_FUENTEINFO = "ABM_FuenteInfo";
	// <--- ABM FuenteInfo


	// ---> ADM Plan Fiscal
	public static final String ADM_PLANFISCAL = "ADM_PlanFiscal";
	// <--- ADM Plan Fiscal
	
	// ---> ABM Investigador
	public static final String ABM_INVESTIGADOR = "ABM_Investigador";
	// <--- ABM Investigador

	// ---> ABM OpeInv
	public static final String ABM_OPEINV = "ABM_OpeInv";
	// <--- ABM OpeInv	

	// ---> ADM OpeInvCon
	public static final String ADM_OPEINVCON = "ADM_OpeInvCon";
	public static final String ADM_OPEINVCON_BUSCAR = "buscar";
	public static final String ADM_OPEINVCON_AGREGAR_MASIVO = "agregarMasivo";
	public static final String ADM_OPEINVCON_ELIMINAR_MASIVO = "eliminarMasivo";
	public static final String ADM_OPEINVCON_EXCLUIR_DE_SELEC = "excluirDeSelec";
	public static final String MTD_LIQUIDACION_DEUDA = "liquidacionDeuda";
	public static final String MTD_ESTADO_CUENTA     = "estadoCuenta";
	// <--- ADM OpeInvCon	
	
	// ---> ADM OpeInvBus
	public static final String ABM_OPEINVBUS = "ABM_OpeInvBus";
	public static final String MTD_ADM_PROCESO = "administrarProceso";
	// <--- ADM OpeInvBus
	
	// ---> ABM AsignarActas
	public static final String ABM_ASIG_ACTAS = "ABM_AsignarActas";
	// <--- ABM AsignarActas
	
	// ---> ADM Actas
	public static final String ABM_ACTAINV = "ADM_Actas";
	public static final String ADM_ACTAINV_MODIF_ACTA = "modificarActa";
	public static final String ACT_PEDIDO_APROBACION = "pedidoAprobacion";
	// ---> ADM Actas
	
	// ---> Aprobacion de Actas
	public static final String ADM_APROBACIONACTAINV = "ADM_AprobacionActaInv";
	public static final String ACT_CAMBIAR_ESTADO_ACTA = "cambiarEstado";
	// <--- Aprobacion de Actas
	
	// ---> Emitir Ordenes de Control
	public static final String EMITIR_ORDENCONTROL = "Emitir_OrdenControl";
	public static final String MTD_EMITIR_ORDENCONTROL = "emitirOrdenControl";
	public static final String MTD_EMITIR_MANUAL_ORDENCONTROL = "emitirManualOrdenControl";
	public static final String MTD_IMPRIMIR_ORDENCONTROL = "impresionOrdenControl";
	// <--- Emitir Ordenes de Control
	
	// ---> ADM Ordenes de Control fiscal
    public static final String ADM_ORDENCONTROLFIS		= "ADM_OrdConFis";
    public static final String CERRAR_ORDENCONTROL = "cerrarOrden";
    public static final String MTD_ASIGNAR = "asignarOrdenInit";
	public static final String MTD_ASIGNARORDEN = "asignarOrden";
	public static final String MTD_ADMINISTRAR = "administrar";
	public static final String MTD_AGREGAR_PERIODO = "agregarPeriodo";
	public static final String MTD_ENVIAR_MESA_ENTRADA 	= "enviarMesaEntrada";
	// <--- ADM Ordenes de Control fiscal
	
	// ---> ABM PeriodoOrden
	public static final String ABM_PERIODOORDEN = "ABM_PeriodoOrden";
	// <--- ABM PeriodoOrden
	
	// ---> ABM Acta
	public static final String ABM_ACTA = "ABM_Acta";
	public static final String ABM_ACTA_ENC = "ABM_ActaEnc";
	// <--- ABM Acta
	
	// ---> ABM OrdConDoc
	public static final String ABM_ORDCONDOC = "ABM_OrdConDoc";

	// ---> ABM InicioInv
	public static final String ABM_INICIOINV = "ABM_InicioInv";
	// <--- ABM InicioInv

	// ---> ABM PlaFueDat
	public static final String ABM_PLAFUEDAT 		= "ABM_PlaFueDat";
	public static final String ABM_PLAFUEDAT_ENC 	= "ABM_PlaFueDatEnc";
	public static final String GEN_MODIF_PLANILLA 	= "generarModificarPlanilla";
	// <--- ABM PlaFueDat

	// ---> ABM PlaFueDatCol
	public static final String ABM_PLAFUEDATCOL = "ABM_PlaFueDatCol";
	// <--- ABM PlaFueDatCol

	// ---> ABM PlaFueDatDet	
	public static final String ABM_PLAFUEDATDET = "ABM_PlaFueDatDet";
	// <--- ABM PlaFueDatDet

	// ---> ABM Comparacion
	public static final String ABM_COMPARACION 		= "ABM_Comparacion";
	public static final String ABM_COMPARACION_ENC 	= "ABM_ComparacionEnc";
	public static final String MTD_CALCULARDIF 		= "calcularDif";
	// <--- ABM Comparacion

	// ---> ABM CompFuente
	public static final String ABM_COMPFUENTE = "ABM_CompFuente";
	// <--- ABM CompFuente

	// ---> ABM CompFuenteRes
	public static final String ABM_COMPFUENTERES = "ABM_CompFuentRes";
	// <--- ABM CompFuenteRes

	// ---> ABM OrdConBasImp
	public static final String ABM_ORDCONBASIMP = "ABM_ordConBasImp";
	public static final String ACT_CARGAR_AJUSTES = "cargarAjustes";
	public static final String ACT_CARGAR_ALICUOTAS = "cargarAlicuotas";
	public static final String ACT_AJUSTAR_PERIODOS = "ajustarPeriodos";
	// <--- ABM OrdConBasImp

	
	// ---> ADM SolicitudEmiPerRetro
	public static final String ADM_SOLICITUDEMIPERRETRO = "ADM_SolicitudEmiPerRetro";
	// <--- ADM SolicitudEmiPerRetro


	// ---> DetAju
	public static final String ABM_DETAJU = "ABM_DetAju";
	public static final String ABM_DETAJU_ENC = "ABM_DetAjuEnc";
	// <--- DetAju

	// ---> AliComFueCol
	public static final String ABM_ALICOMFUECOL = "ABM_AliComFueCol";
	public static final String MTD_AGREGAR_PERSONAL = "agregarPersonal";
	public static final String MTD_ADICIONAL_PUBL = "adicionalPublicidad";
	public static final String MTD_ADICIONAL_MYS = "adicionalMesasYSillas";
	public static final String MTD_DETERM_POR_MULTA = "determPorMulta";
	// <--- AliComFueCol
	
	// ---> MesaEntrada
	public static final String ABM_MESAENTRADA = "ABM_MesaEntrada";
	// <--- MesaEntrada

	// ---> AproOrdCon
	public static final String ABM_APROORDCON = "ABM_AproOrdCon";
	// <--- AproOrdCon
	
	// ---> DetAjuDocSop
	public static final String ABM_DETAJUDOCSOP = "ABM_DetAjuDocSop";
	// <--- DetAjuDocSop
	// ---> DocSop
	public static final String ABM_DOCSOP = "ABM_DocSop";
	// ---> ComAju
	public static final String ABM_COMAJU_ENC = "ABM_ComAjuEnc";
	public static final String ABM_COMAJU = "ABM_ComAju";
	// ---> ComAju

	
}