//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.util;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class BalSecurityConstants {
	
	public static final String IMPUTAR = "imputar";
	public static final String DISTRIBUIR = "distribuir";
	public static final String GENERAR_RECIBO = "generarRecibo";
	public static final String GENERAR_SALDO = "generarSaldoAFavor";
	public static final String REINGRESAR = "reingresar";
	
	// ---> ABM Sistema
	public static final String ABM_SISTEMA  = "ABM_Sistema";
	// <--- ABM Sistema

	// ---> Administrar Dedua Reclamada (SINC)
	public static final String ADM_DEUDA_RECLAMADA  = "ADM_DeudaReclamada";
	// <--- Administrar Dedua Reclamada (SINC)
	
	
	// ---> ABM DisPar
	public static final String ABM_DISPAR  = "ABM_DisPar";
	public static final String ABM_DISPAR_ENC = "ABM_DisParEnc";
	
	public static final String ABM_DISPAR_ADM_DISPARREC = "asociarRecursoVia";
	public static final String ABM_DISPAR_ADM_DISPARPLA = "asociarPlan";
	// <--- ABM DisPar

	// ---> ABM DisParDet
	public static final String ABM_DISPARDET  = "ABM_DisParDet";
	// <--- ABM DisParDet

	// ---> ABM DisParRec
	public static final String ABM_DISPARREC  = "ABM_DisParRec";
	// <--- ABM DisParRec

	// ---> ABM DisParPla
	public static final String ABM_DISPARPLA  = "ABM_DisParPla";
	// <--- ABM DisParPla
	
	// ---> ABM Sellado
	public static final String ABM_IMPSEL = "ABM_ImpSel";
	public static final String ABM_ACCIONSELLADO = "ABM_AccionSellado";
	public static final String ABM_PARSEL = "ABM_ParSel";
	public static final String ABM_SELLADO = "ABM_Sellado";
	public static final String ABM_SELLADO_ENC = "ABM_SelladoEnc";
	// <--- ABM Sellado
	
		
	// ---> ABM Asentamiento
	public static final String ABM_ASENTAMIENTO  = "ABM_Asentamiento";
	
	public static final String ABM_ASENTAMIENTO_ADM_PROCESO = "admProceso";
	// <--- ABM Asentamiento
	
	// ---> ABM ProcesoAsentamiento
	public static final String ABM_PROCESO_ASENTAMIENTO  = "ABM_ProcesoAsentamiento";
	public static final String ABM_PROCESO_ASENTAMIENTO_ENC = "ABM_Asentamiento";
	public static final String ABM_PROCESO_ASENTAMIENTO_FORZAR = "forzar";
	public static final String ABM_PROCESO_ASENTAMIENTO_CACHE_REDUCIDO = "cacheReducido";
	// <--- ABM ProcesoAsentamiento

	// ---> ABM AseDel
	public static final String ABM_ASEDEL  = "ABM_AseDel";
	
	public static final String ABM_ASEDEL_ADM_PROCESO = "admProceso";
	// <--- ABM AseDel
	
	// ---> ABM ProcesoAseDel
	public static final String ABM_PROCESO_ASEDEL  = "ABM_ProcesoAseDel";
	public static final String ABM_PROCESO_ASEDEL_ENC = "ABM_AseDel";
	public static final String ABM_PROCESO_ASEDEL_FORZAR = "forzar";
	// <--- ABM ProcesoAseDel

	// ---> ABM Ejercicio
	public static final String ABM_EJERCICIO = "ABM_Ejercicio";
	// <--- ABM Ejercicio	
	
	// ---> ABM Partida
	public static final String ABM_PARTIDA= "ABM_Partida";
	
	public static final String ABM_PARTIDA_ENC= "ABM_PartidaEnc";
	// <--- ABM Partida	
	
	// ---> ABM Reclamo
	public static final String ABM_RECLAMO= "ABM_Reclamo";
	// <--- ABM Reclamo	
	
	// ---> ABM SaldoAFavor
	public static final String ABM_SALDOAFAVOR= "ABM_SaldoAFavor";
	// <--- ABM SaldoAFavor	
	
	// ---> ABM Tipo Cuenta Bancaria
	public static final String ABM_TIPCUEBAN= "ABM_TipCueBan";
	// <--- ABM Tipo Cuenta Bancaria
	
	// ---> ABM  Cuenta Bancaria
	public static final String ABM_CUENTABANCO= "ABM_CuentaBanco";
	// <--- ABM  Cuenta Bancaria
	
	// ---> ABM  ParCueBan
	public static final String ABM_PARCUEBAN= "ABM_ParCueBan";
	// <--- ABM  ParCueBan
	
	// ---> ABM Nodo
	public static final String ABM_NODO= "ABM_Nodo";
	public static final String ABM_NODO_ENC= "ABM_NodoEnc";
	// <--- ABM Nodo
	
	// ---> ABM RelCla
	public static final String ABM_RELCLA  = "ABM_RelCla";
	// <--- ABM RelCLa
	
	// ---> ABM RelPartida
	public static final String ABM_RELPARTIDA  = "ABM_RelPartida";
	// <--- ABM RelPartida
	
	// ---> ABM OtrIngTes
	public static final String ABM_OTRINGTES= "ABM_OtrIngTes";
	public static final String ABM_OTRINGTES_DIS= "distribuirOtrIngTes";
	public static final String ABM_OTRINGTES_IMP= "imputarOtrIngTes";
	public static final String ABM_OTRINGTES_REC= "generarRecibo";

	public static final String ABM_OTRINGTESPAR= "ABM_OtrIngTesPar";
	// <--- ABM OtrIngTesPar
	
    // ---> ABM Caja7
	public static final String ABM_CAJA7= "ABM_Caja7";
	// <--- ABM Caja7
	
    // ---> ABM Caja69
	public static final String ABM_CAJA69= "ABM_Caja69";
	// <--- ABM Caja69

	// ---> ABM TranBal
	public static final String ABM_TRANBAL= "ABM_TranBal";
	// <--- ABM TranBal

    // ---> ABM Reingreso
	public static final String ABM_REINGRESO= "ABM_Reingreso";
	// <--- ABM Reingreso

	// ---> ABM OtrIngTesRecCon
	public static final String ABM_OTRINGTESRECCON  = "ABM_OtrIngTesRecCon";
	// <--- ABM OtrIngTesRecCon
	
	// ---> Consultar Reporte de Clasificador
	public static final String CONSULTAR_CLASIFICADOR = "Consultar_Clasificador";
	// <--- Consultar Reporte de Clasificador

	// ---> Consultar Reporte de Rentas
	public static final String CONSULTAR_RENTAS = "Consultar_Rentas";
	// <--- Consultar Reporte de Rentas

	// ---> ABM Balance
	public static final String ABM_BALANCE  = "ABM_Balance";
	
	public static final String ABM_BALANCE_ADM_PROCESO = "admProceso";
	// <--- ABM Balance
	
	// ---> ABM ProcesoBalance
	public static final String ABM_PROCESO_BALANCE  = "ABM_ProcesoBalance";
	public static final String ABM_PROCESO_BALANCE_ENC = "ABM_Balance";
	// <--- ABM ProcesoBalance
	
	// ---> ABM Indet
	public static final String ABM_INDET = "ABM_Indet";
	public static final String ABM_INDET_REINGRESAR = "reingresar";
	public static final String ABM_INDET_DESGLOCE = "desgloce";
	// <--- ABM Indet

	// ---> ABM Indet Masivo
	public static final String ABM_INDET_MASIVO = "ABM_IndetMasivo";
	// <--- ABM Indet Masivo

	// ---> ABM Duplice
	public static final String ABM_DUPLICE = "ABM_Duplice";
	// <--- ABM Duplice

	// ---> ABM Reingreso (de Indeterminados)
	public static final String ABM_INDETREING = "ABM_IndetReing";
	// <--- ABM Reingreso (de Indeterminados)

	// ---> ABM Archivo
	public static final String ABM_ARCHIVO = "ABM_Archivo";
	// <--- ABM Archivo	

	// ---> ABM Folio
	public static final String ABM_FOLIO  = "ABM_Folio";
	public static final String ABM_FOLIO_ENC = "ABM_FolioEnc";
	// <--- ABM Folio
	
	// ---> ABM FolCom
	public static final String ABM_FOLCOM  = "ABM_FolCom";
	// <--- ABM FolCom

	// ---> ABM FolDiaCob
	public static final String ABM_FOLDIACOB  = "ABM_FolDiaCob";
	
	public static final String ABM_FOLDIACOBE_GUARDAR = "guardar";
	// <--- ABM FolDiaCob
	
	// ---> ABM TipoCom
	public static final String ABM_TIPOCOM= "ABM_TipoCom";
	// <--- ABM TipoCom
	
	// ---> ABM Clasificador
	public static final String ABM_CLASIFICADOR= "ABM_clasificador";
	// <--- ABM Clasificador

	// ---> ABM Compensacion
	public static final String ABM_COMPENSACION  = "ABM_Compensacion";
	public static final String ABM_COMPENSACION_ENC = "ABM_CompensacionEnc";
	// <--- ABM Compensacion

	// ---> ABM ComDeu
	public static final String ABM_COMDEU  = "ABM_ComDeu";
	// <--- ABM ComDeu

	// ---> ABM TipoCob
	public static final String ABM_TIPOCOB= "ABM_TipoCob";
	// <--- ABM TipoCob

	// ---> ABM AuxCaja7
	public static final String ABM_AUXCAJA7= "ABM_AuxCaja7";
	// <--- ABM AuxCaja7

	// ---> ABM LeyParAcu
	public static final String ABM_LEYPARACU= "ABM_LeyParAcu";
	// <--- ABM LeyParAcu
	
	// ---> Consultar Total por Nodo
	public static final String CONSULTAR_TOTAL_NODO = "Consultar_TotalNodo";
	// <--- Consultar Total por Nodo
	
	// ---> Reporte de Total de Partida
	public static final String CONSULTAR_TOTAL_PAR = "Consultar_TotalPar";
	// <--- Reporte de Total de Partida

	// ---> Reporte de Clasificador Comparativo
	public static final String CONSULTAR_CLACOM = "Consultar_ClaCom";
	// <--- Reporte de Clasificador Comparativo
	
	// ---> Control de Conciliacion
	public static final String CONTROL_CONCILIACION = "Control_Conciliacion";
	// <--- Control de Conciliacion	
	
	// ---> ABM Envio Osiris
	public static final String ABM_ENVIOOSIRIS="ABM_EnvioOsiris";
	public static final String MTD_OBTENERENVIO = "obtenerEnvios";
	public static final String MTD_PROCESARENVIO = "procesarEnvios";
	public static final String MTD_GENERARTRANSACCION = "generarTransaccion";
	public static final String MTD_CAMBIAR_ESTADO = "cambiarEstado";
	public static final String MTD_GENERARDECJUR = "generarDecJur";
	// <--- ABM Envio Osiris	
	
	// ---> ABM Cierre Banco
	public static final String ABM_CIERREBANCO="ABM_CierreBanco";
	// <--- ABM Cierre Banco	
	
	// ---> ABM Transaccion Afip
	public static final String ABM_TRANAFIP="ABM_TranAfip";
	// <--- ABM Transaccion Afip
	
	// ---> ABM Detalle DJ
	public static String ABM_DETALLEDJ="ABM_DetalleDJ";
	// <--- ABM Detalle DJ
	
	// ---> ABM Detalle Pago
	public static String ABM_DETALLEPAGO="ABM_DetallePago";
	// <--- ABM Detalle Pago
	
	// ---> ABM MovBan
	public static final String ABM_MOVBAN= "ABM_MovBan";
	public static final String ABM_MOVBAN_ENC= "ABM_MovBanEnc";
	// <--- ABM MovBan

	// ---> ABM  MovBanDet
	public static final String ABM_MOVBANDET= "ABM_MovBanDet";
	// <--- ABM  MovBanDet

	// ---> ABM  Conciliacion
	public static final String ABM_CONCILIACION = "ABM_Conciliacion";
	// <--- ABM  Conciliacion

	// ---> ABM  TipoIndet
	public static final String ABM_TIPOINDET = "ABM_TipoIndet";
	// <--- ABM  TipoIndet
}