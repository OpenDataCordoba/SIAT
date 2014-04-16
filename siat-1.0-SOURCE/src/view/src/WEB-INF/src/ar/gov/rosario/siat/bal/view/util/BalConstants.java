//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.util;


public interface BalConstants {
    
	public static final String ACT_IMPUTAR = "imputar";
	public static final String ACT_DISTRIBUIR = "distribuir";
	public static final String ACT_GENERAR_RECIBO = "generarRecibo";
	public static final String ACT_REINICIAR   = "reiniciar";
	public static final String ACT_RETROCEDER   = "retroceder";
	public static final String ACT_FORZAR   = "forzar";
	public static final String ACT_INCLUIR  = "incluir";
	public static final String ACT_EXCLUIR  = "excluir";
	public static final String ACT_GENERAR_TRANSACCION = "generarTransaccion";
	public static final String ACT_GENERAR_DECJUR = "generarDecJur";
	public static final String ACT_CAMBIAR_ESTADO = "cambiarEstado";
	
	// ---> Sistema
	public static final String ACTION_BUSCAR_SISTEMA     	= "buscarSistema";
	public static final String ACTION_ADMINISTRAR_SISTEMA 	= "administrarSistema";
	public static final String FWD_SISTEMA_SEARCHPAGE 		= "sistemaSearchPage";
	public static final String FWD_SISTEMA_VIEW_ADAPTER 	= "sistemaViewAdapter";
	public static final String FWD_SISTEMA_EDIT_ADAPTER 	= "sistemaEditAdapter";	
	// <--- Sistema
	
	// ---> TipoIndet
	public static final String ACTION_BUSCAR_TIPOINDET		= "buscarTipoIndet";
	public static final String ACTION_ADMINISTRAR_TIPOINDET = "administrarTipoIndet";
	public static final String FWD_TIPOINDET_SEARCHPAGE 	= "tipoIndetSearchPage";
	public static final String FWD_TIPOINDET_VIEW_ADAPTER 	= "tipoIndetViewAdapter";
	public static final String FWD_TIPOINDET_EDIT_ADAPTER 	= "tipoIndetEditAdapter";	
	// <--- TipoIndet
	
	// ---> Administrar Deuda Reclamada (SINC)
	public static final String FWD_DEDUDA_RECLAMADA_INGRESO_ADAPTER	 = "deudaReclamadaIngresoAdapter";
	public static final String FWD_DEDUDA_RECLAMADA_ADAPTER	 = "deudaReclamadaAdapter";
	public static final String FWD_INICIALIZAR_DEDUA_RECLAMADA = "/bal/AdministrarDeudaReclamada.do?method=inicializar";
	public static final String PATH_DEDUDA_RECLAMADA_ADAPTER = "/bal/AdministrarDeudaReclamada";
	// <--- Administrar Deuda Reclamada (SINC) 

	// ---> DisPar
	// DisPar
	public static final String ACTION_BUSCAR_DISPAR     	= "buscarDisPar";
	public static final String FWD_DISPAR_SEARCHPAGE 	    = "disParSearchPage";
	public static final String ACTION_ADMINISTRAR_DISPAR 	= "administrarDisPar";
	public static final String FWD_DISPAR_VIEW_ADAPTER 		= "disParViewAdapter";
	public static final String FWD_DISPAR_ADAPTER 		    = "disParAdapter";
	public static final String ACTION_ADMINISTRAR_ENCDISPAR = "administrarEncDisPar";	
	public static final String FWD_DISPAR_ENC_EDIT_ADAPTER 	= "disParEncEditAdapter";	
	public static final String PATH_ADMINISTRAR_DISPAR     	= "/bal/AdministrarDisPar";
	public static final String ACT_ADM_DISPARREC		     	= "asociarRecursoVia";
	public static final String ACT_ADM_DISPARPLA		     	= "asociarPlan";
	
	// DisParDet
	public static final String ACTION_ADMINISTRAR_DISPARDET  = "administrarDisParDet";
	public static final String FWD_DISPARDET_VIEW_ADAPTER 	 = "disParDetViewAdapter";
	public static final String FWD_DISPARDET_EDIT_ADAPTER 	 = "disParDetEditAdapter";

	// DisParRec
	public static final String FWD_DISPARREC_SEARCHPAGE 	    = "disParRecSearchPage";
	public static final String ACTION_ADMINISTRAR_DISPARREC  = "administrarDisParRec";
	public static final String FWD_DISPARREC_ADAPTER 	 = "disParRecAdapter";
	public static final String FWD_DISPARREC_VIEW_ADAPTER 	 = "disParRecViewAdapter";
	public static final String FWD_DISPARREC_EDIT_ADAPTER 	 = "disParRecEditAdapter";
	
	// DisParPla
	public static final String FWD_DISPARPLA_SEARCHPAGE 	    = "disParPlaSearchPage";
	public static final String ACTION_ADMINISTRAR_DISPARPLA  = "administrarDisParPla";
	public static final String FWD_DISPARPLA_ADAPTER 	 = "disParPlaAdapter";
	public static final String FWD_DISPARPLA_VIEW_ADAPTER 	 = "disParPlaViewAdapter";
	public static final String FWD_DISPARPLA_EDIT_ADAPTER 	 = "disParPlaEditAdapter";


	// ---> Sellado
	public static final String FWD_SELLADO_SEARCHPAGE = "selladoSearchPage";
	public static final String ACTION_ADMINISTRAR_SELLADO = "administrarSellado";
	public static final String ACTION_ADMINISTRAR_ENC_SELLADO = "administrarEncSellado";
	public static final String FWD_SELLADO_ENC_EDIT_ADAPTER = "selladoEncEditAdapter";
	public static final String PATH_ADMINISTRAR_SELLADO = "/bal/AdministrarSellado";
	public static final String FWD_SELLADO_VIEW_ADAPTER = "selladoViewAdapter";
	public static final String FWD_SELLADO_ADAPTER = "selladoAdapter";
	
	public static final String ACTION_ADMINISTRAR_IMPSEL = "administrarImpSel";
	public static final String FWD_IMPSEL_EDIT_ADAPTER = "impSelEditAdapter";
	public static final String FWD_IMPSEL_VIEW_ADAPTER = "impSelViewAdapter";
	
	public static final String ACTION_ADMINISTRAR_ACCIONSELLADO = "administrarAccionSellado"; 
	public static final String FWD_ACCIONSELLADO_VIEW_ADAPTER = "accionSelladoViewAdapter";
	public static final String FWD_ACCIONSELLADO_EDIT_ADAPTER = "accionSelladoEditAdapter";
	
	public static final String ACTION_ADMINISTRAR_PARSEL = "administrarParSel";
	public static final String FWD_PARSEL_VIEW_ADAPTER = "parSelViewAdapter";
	public static final String FWD_PARSEL_EDIT_ADAPTER = "parSelEditAdapter";
	// <--- Sellado
	
	// ---> Asentamiento
	// Asentamiento
	public static final String ACTION_BUSCAR_ASENTAMIENTO     	= "buscarAsentamiento";
	public static final String FWD_ASENTAMIENTO_SEARCHPAGE 	    = "asentamientoSearchPage";
	public static final String ACTION_ADMINISTRAR_ASENTAMIENTO 	= "administrarAsentamiento";
	public static final String FWD_ASENTAMIENTO_VIEW_ADAPTER 	= "asentamientoViewAdapter";
	public static final String FWD_ASENTAMIENTO_EDIT_ADAPTER    = "asentamientoEditAdapter";
	public static final String PATH_ADMINISTRAR_ASENTAMIENTO    = "/bal/AdministrarAsentamiento";
	public static final String ACT_ADM_PROCESO_ASENTAMIENTO		= "admProceso";
	
	// Proceso Asentamiento
	public static final String ACTION_ADMINISTRAR_PROCESO_ASENTAMIENTO  = "administrarProcesoAsentamiento";
	public static final String FWD_PROCESO_ASENTAMIENTO_ADAPTER 	 = "procesoAsentamientoAdapter";
	public static final String FWD_PROCESO_ASENTAMIENTO_VIEW_ADAPTER 	 = "procesoAsentamientoViewAdapter";
	
	public static final String FWD_CORRIDA_PRO_ASE_VIEW_ADAPTER = "corridaProcesoAsentamientoViewAdapter";
	public static final String FWD_ADMINISTRAR_CORRIDA_PRO_ASE  = "administrarCorridaProcesoAsentamiento";
	
	// ---> AseDel
	// AseDel
	public static final String ACTION_BUSCAR_ASEDEL     	= "buscarAseDel";
	public static final String FWD_ASEDEL_SEARCHPAGE 	    = "aseDelSearchPage";
	public static final String ACTION_ADMINISTRAR_ASEDEL 	= "administrarAseDel";
	public static final String FWD_ASEDEL_VIEW_ADAPTER 	= "aseDelViewAdapter";
	public static final String FWD_ASEDEL_EDIT_ADAPTER    = "aseDelEditAdapter";
	public static final String PATH_ADMINISTRAR_ASEDEL    = "/bal/AdministrarAseDel";
	public static final String ACT_ADM_PROCESO_ASEDEL		= "admProceso";
	
	// Proceso AseDel
	public static final String ACTION_ADMINISTRAR_PROCESO_ASEDEL  = "administrarProcesoAseDel";
	public static final String FWD_PROCESO_ASEDEL_ADAPTER 	 = "procesoAseDelAdapter";
	public static final String FWD_PROCESO_ASEDEL_VIEW_ADAPTER 	 = "procesoAseDelViewAdapter";
	
	public static final String FWD_CORRIDA_PRO_ASE_DEL_VIEW_ADAPTER = "corridaProcesoAseDelViewAdapter";
	public static final String FWD_ADMINISTRAR_CORRIDA_PRO_ASE_DEL  = "administrarCorridaProcesoAseDel";
	
	
	// ---> ABM Ejercicio
	public static final String FWD_EJERCICIO_SEARCHPAGE = "ejercicioSearchPage";
	public static final String ACTION_ADMINISTRAR_EJERCICIO = "administrarEjercicio";
	public static final String FWD_EJERCICIO_VIEW_ADAPTER = "ejercicioViewAdapter";
	public static final String FWD_EJERCICIO_EDIT_ADAPTER = "ejercicioEditAdapter";
	// <--- ABM Ejercicio
	
	// ---> ABM Partida
	public static final String FWD_PARTIDA_SEARCHPAGE = "partidaSearchPage";
	public static final String ACTION_ADMINISTRAR_PARTIDA = "AdministrarPartida";
	public static final String FWD_PARTIDA_VIEW_ADAPTER = "partidaViewAdapter";
	public static final String FWD_PARTIDA_EDIT_ADAPTER = "partidaEditAdapter";
	
	public static final String ACTION_ADMINISTRAR_ENC_PARTIDA = "administrarEncPartida";
	public static final String FWD_PARTIDA_ENC_EDIT_ADAPTER = "PartidaEncEditAdapter";
	public static final String PATH_ADMINISTRAR_PARTIDA = "/bal/AdministrarPartida";
	public static final String METOD_DESATRVAL_PARAM_ATRIBUTO = "paramAtributo";
	public static final String FWD_PARTIDA_ADAPTER = "partidaAdapter";

	// <--- ABM Partida
	// ---> parCueBan
	public static final String ACTION_ADMINISTRAR_PARCUEBAN = "administrarParCueBan";
	public static final String FWD_PARCUEBAN_EDIT_ADAPTER = "ParCueBanEditAdapter";
	public static final String FWD_PARCUEBAN_VIEW_ADAPTER = "ParCueBanViewAdapter";
	// <--- parCueBan
	
	// ---> ABM Reclamo
	public static final String FWD_RECLAMO_SEARCHPAGE = "reclamoSearchPage";
	public static final String ACTION_ADMINISTRAR_RECLAMO = "administrarReclamo";
	public static final String FWD_RECLAMO_VIEW_ADAPTER = "reclamoViewAdapter";
	public static final String FWD_RECLAMO_EDIT_ADAPTER = "reclamoEditAdapter";
	// <--- ABM Reclamo
	
	// ---> ABM Nodo
	
	public static final String ACTION_BUSCAR_NODO = "buscarNodo";
	public static final String FWD_NODO_SEARCHPAGE = "nodoSearchPage";
	public static final String ACTION_ADMINISTRAR_NODO = "administrarNodo";
	public static final String FWD_NODO_ADAPTER = "nodoAdapter";
	public static final String ACTION_ADMINISTRAR_ENCNODO = "administrarEncNodo";
	public static final String FWD_NODO_VIEW_ADAPTER = "nodoViewAdapter";
	public static final String FWD_NODO_ENC_EDIT_ADAPTER = "nodoEncEditAdapter";
	public static final String PATH_ADMINISTRAR_NODO  = "/bal/AdministrarNodo";
	// <--- ABM Nodo
	
	// RelCla
	public static final String ACTION_ADMINISTRAR_RELCLA  = "administrarRelCla";
	public static final String FWD_RELCLA_VIEW_ADAPTER 	 = "relClaViewAdapter";
	public static final String FWD_RELCLA_EDIT_ADAPTER 	 = "relClaEditAdapter";

	// RelPartida
	public static final String ACTION_ADMINISTRAR_RELPARTIDA  = "administrarRelPartida";
	public static final String FWD_RELPARTIDA_VIEW_ADAPTER 	 = "relPartidaViewAdapter";
	public static final String FWD_RELPARTIDA_EDIT_ADAPTER 	 = "relPartidaEditAdapter";
	
	
	// ---> ABM Saldo a Favor
	public static final String ACTION_BUSCAR_SALDOAFAVOR = "buscarSaldoAFavor";
	public static final String FWD_SALDOAFAVOR_SEARCHPAGE = "saldoAFavorSearchPage";
	public static final String ACTION_ADMINISTRAR_SALDOAFAVOR = "administrarSaldoAFavor";
	public static final String FWD_SALDOAFAVOR_VIEW_ADAPTER = "saldoAFavorViewAdapter";
	public static final String FWD_SALDOAFAVOR_EDIT_ADAPTER = "saldoAFavorEditAdapter";
	// <--- ABM Saldo a Favor
	
	// ---> ABM Cuenta Banco
	public static final String ACTION_ADMINISTRAR_CUENTABANCO = "administrarCuentaBanco";
	public static final String ACTION_BUSCAR_CUENTABANCO = "buscarCuentaBanco";
	public static final String FWD_CUENTABANCO_SEARCHPAGE = "cuentaBancoSearchPage";
	public static final String FWD_CUENTABANCO_VIEW_ADAPTER = "cuentaBancoViewAdapter";
	public static final String FWD_CUENTABANCO_EDIT_ADAPTER = "cuentaBancoEditAdapter";
	// <--- ABM Cuenta Banco
	
	// ---> ABM Tipo Cuenta Banco
	public static final String ACTION_ADMINISTRAR_TIPCUEBAN = "administrarTipCueBan";
	public static final String FWD_TIPCUEBAN_SEARCHPAGE = "tipCueBanSearchPage";
	public static final String FWD_TIPCUEBAN_VIEW_ADAPTER = "tipCueBanViewAdapter";
	public static final String FWD_TIPCUEBAN_EDIT_ADAPTER = "tipCueBanEditAdapter";
	// <--- ABM Tipo Cuenta Banco
	

	// ---> Otros Ingresos Tesoreria
	// OtrIngTes
	public static final String ACTION_BUSCAR_OTRINGTES     	= "buscarOtrIngTes";
	public static final String FWD_OTRINGTES_SEARCHPAGE 	    = "otrIngTesSearchPage";
	public static final String ACTION_ADMINISTRAR_OTRINGTES 	= "administrarOtrIngTes";
	public static final String FWD_OTRINGTES_VIEW_ADAPTER 		= "otrIngTesViewAdapter";
	public static final String FWD_OTRINGTES_EDIT_ADAPTER 		    = "otrIngTesEditAdapter";
	public static final String FWD_OTRINGTES_RECIBO_PRINT 		= "otrIngTesReciboPrint";
	public static final String PATH_ADMINISTRAR_OTRINGTES     	= "/bal/AdministrarOtrIngTes";
	public static final String PATH_BUSCAR_OTRINGTES     		= "/bal/BuscarOtrIngTes";
	
	public static final String PATH_DISTRIBUIR_OTRINGTES    	= "/bal/DistribuirOtrIngTes";
	public static final String FWD_OTRINGTES_DIS_IMP			= "otrIngTesAdmAdapter";
	
	// OtrIngTesPar
	public static final String ACTION_ADMINISTRAR_OTRINGTESPAR  = "administrarOtrIngTesPar";
	public static final String FWD_OTRINGTESPAR_VIEW_ADAPTER 	 = "otrIngTesParViewAdapter";
	public static final String FWD_OTRINGTESPAR_EDIT_ADAPTER 	 = "otrIngTesParEditAdapter";
	
	// ---> Reporte Clasificador
	public static final String FWD_CLASIFICADOR_REPORT = "clasificadorReport";
	// <--- Reporte Clasificador

	// ---> Reporte Rentas
	public static final String FWD_RENTAS_REPORT = "rentasReport";
	// <--- Reporte Rentas

	// ---> Balance
	// Balance
	public static final String ACTION_BUSCAR_BALANCE     	= "buscarBalance";
	public static final String FWD_BALANCE_SEARCHPAGE 	    = "balanceSearchPage";
	public static final String ACTION_ADMINISTRAR_BALANCE 	= "administrarBalance";
	public static final String FWD_BALANCE_VIEW_ADAPTER 	= "balanceViewAdapter";
	public static final String FWD_BALANCE_EDIT_ADAPTER    = "balanceEditAdapter";
	public static final String PATH_ADMINISTRAR_BALANCE    = "/bal/AdministrarBalance";
	public static final String ACT_ADM_PROCESO_BALANCE		= "admProceso";
	
	// Proceso Balance
	public static final String ACTION_ADMINISTRAR_PROCESO_BALANCE  = "administrarProcesoBalance";
	public static final String FWD_PROCESO_BALANCE_ADAPTER 	 = "procesoBalanceAdapter";
	public static final String FWD_PROCESO_BALANCE_VIEW_ADAPTER 	 = "procesoBalanceViewAdapter";
	
	public static final String FWD_CORRIDA_PRO_BAL_VIEW_ADAPTER = "corridaProcesoBalanceViewAdapter";
	public static final String FWD_ADMINISTRAR_CORRIDA_PRO_BAL  = "administrarCorridaProcesoBalance";

	// ---> Proceso Balance: Caja7
	public static final String ACTION_ADMINISTRAR_CAJA7  = "administrarCaja7";
	public static final String FWD_CAJA7_VIEW_ADAPTER 	 = "caja7ViewAdapter";
	public static final String FWD_CAJA7_EDIT_ADAPTER 	 = "caja7EditAdapter";

	// ---> Proceso Balance: Caja69
	public static final String ACTION_ADMINISTRAR_CAJA69  = "administrarCaja69";
	public static final String FWD_CAJA69_VIEW_ADAPTER 	 = "caja69ViewAdapter";
	public static final String FWD_CAJA69_EDIT_ADAPTER 	 = "caja69EditAdapter";

	// ---> Proceso Balance: TranBal
	public static final String ACTION_BUSCAR_TRANBAL    	= "buscarTranBal";
	public static final String ACTION_ADMINISTRAR_TRANBAL  = "administrarTranBal";
	public static final String FWD_TRANBAL_SEARCHPAGE = "tranBalSearchPage";
	public static final String FWD_TRANBAL_VIEW_ADAPTER 	 = "tranBalViewAdapter";
	public static final String FWD_TRANBAL_EDIT_ADAPTER 	 = "tranBalEditAdapter";

	// ---> Proceso Balance: Reingresos
	public static final String ACTION_ADMINISTRAR_REINGRESO  = "administrarReingreso";
	public static final String FWD_REINGRESO_VIEW_ADAPTER 	 = "reingresoViewAdapter";
	public static final String FWD_REINGRESO_EDIT_ADAPTER 	 = "reingresoEditAdapter";

	// Indet 
	public static final String ACTION_BUSCAR_INDET    	= "buscarIndet";
	public static final String FWD_INDET_SEARCHPAGE = "indetSearchPage";
	public static final String ACTION_ADMINISTRAR_INDET = "administrarIndet";
	public static final String FWD_INDET_VIEW_ADAPTER 	= "indetViewAdapter";
	public static final String FWD_INDET_EDIT_ADAPTER    = "indetEditAdapter";
	public static final String FWD_INDET_GEN_SALDO_ADAPTER    = "indetGenSaldoAFavorAdapter";
	public static final String PATH_ADMINISTRAR_INDET    = "/bal/AdministrarIndet";
	public static final String ACT_REINGRESAR_INDET		     	= "reingresar";
	public static final String ACT_DESGLOCE_INDET		     	= "desgloce";
	
	public static final String FWD_INDET_MASIVO_SEARCHPAGE = "indetMasivoSearchPage";
	public static final String FWD_INDET_MASIVO_REINGRESO 	= "indetMasivoReingreso";
	public static final String PATH_BUSCAR_INDET_MASIVO    = "/bal/BuscarIndetMasivo";

	
	// Duplice 
	public static final String ACTION_BUSCAR_DUPLICE    	= "buscarDuplice";
	public static final String FWD_DUPLICE_SEARCHPAGE = "dupliceSearchPage";
	public static final String ACTION_ADMINISTRAR_DUPLICE = "administrarDuplice";
	public static final String FWD_DUPLICE_VIEW_ADAPTER 	= "dupliceViewAdapter";
	public static final String FWD_DUPLICE_EDIT_ADAPTER 	= "dupliceEditAdapter";
	public static final String FWD_DUPLICE_IMPUTAR_ADAPTER    = "dupliceImputarAdapter";
	public static final String FWD_DUPLICE_GEN_SALDO_ADAPTER    = "dupliceGenSaldoAFavorAdapter";
	public static final String PATH_ADMINISTRAR_DUPLICE    = "/bal/AdministrarDuplice";
	public static final String ACT_GENERAR_SALDO_A_FAVOR   	= "generarSaldoAFavor";
	
	// IndetReing 
	public static final String ACTION_BUSCAR_INDETREING    	= "buscarIndetReing";
	public static final String FWD_INDETREING_SEARCHPAGE = "indetReingSearchPage";
	public static final String ACTION_ADMINISTRAR_INDETREING = "administrarIndetReing";
	public static final String FWD_INDETREING_VIEW_ADAPTER 	= "indetReingViewAdapter";
	public static final String PATH_ADMINISTRAR_INDETREING    = "/bal/AdministrarIndetReing";
		
	// Archivos
	public static final String FWD_ARCHIVO_SEARCHPAGE = "archivoSearchPage";
	public static final String ACTION_BUSCAR_ARCHIVO   	= "buscarArchivo";
	public static final String ACTION_ADMINISTRAR_ARCHIVO = "administrarArchivo";
	public static final String FWD_ARCHIVO_VIEW_ADAPTER = "archivoViewAdapter";
	public static final String FWD_ARCHIVO_EDIT_ADAPTER = "archivoEditAdapter";

	// ---> Folio
	// Folio
	public static final String ACTION_BUSCAR_FOLIO     		= "buscarFolio";
	public static final String FWD_FOLIO_SEARCHPAGE 	    = "folioSearchPage";
	public static final String ACTION_ADMINISTRAR_FOLIO 	= "administrarFolio";
	public static final String FWD_FOLIO_VIEW_ADAPTER 		= "folioViewAdapter";
	public static final String FWD_FOLIO_ADAPTER 		    = "folioAdapter";
	public static final String ACTION_ADMINISTRAR_ENCFOLIO = "administrarEncFolio";	
	public static final String FWD_FOLIO_ENC_EDIT_ADAPTER 	= "folioEncEditAdapter";	
	public static final String PATH_ADMINISTRAR_FOLIO     	= "/bal/AdministrarFolio";
	
	// FolCom
	public static final String ACTION_ADMINISTRAR_FOLCOM  = "administrarFolCom";
	public static final String FWD_FOLCOM_VIEW_ADAPTER 	 = "folComViewAdapter";
	public static final String FWD_FOLCOM_EDIT_ADAPTER 	 = "folComEditAdapter";
	
	// FolDiaCob
	public static final String ACTION_BUSCAR_FOLDIACOB     	= "buscarFolDiaCob";
	public static final String FWD_FOLDIACOB_SEARCHPAGE 	    = "folDiaCobSearchPage";
	public static final String ACTION_ADMINISTRAR_FOLDIACOB  = "administrarFolDiaCob";
	public static final String FWD_FOLDIACOB_VIEW_ADAPTER 	 = "folDiaCobViewAdapter";
	public static final String FWD_FOLDIACOB_EDIT_ADAPTER 	 = "folDiaCobEditAdapter";
	public static final String PATH_BUSCAR_FOLDIACOB   		= "/bal/BuscarFolDiaCob";
	
	// TipoCom
	public static final String FWD_TIPOCOM_SEARCHPAGE 	    = "tipoComSearchPage";
	public static final String ACTION_ADMINISTRAR_TIPOCOM 	= "administrarTipoCom";
	public static final String FWD_TIPOCOM_VIEW_ADAPTER		= "tipoComViewAdapter";
	public static final String FWD_TIPOCOM_EDIT_ADAPTER 	= "tipoComEditAdapter";
	
	// Clasificador
	public static final String FWD_CLASIFICADOR_VIEW_ADAPTER = "clasificadorViewAdapter";
	public static final String FWD_CLASIFICADOR_EDIT_ADAPTER = "clasificadorEditAdapter";
	public static final String FWD_CLASIFICADOR_SEARCHPAGE   = "clasificadorSearchPage";
	public static final String ACTION_ADMINISTRAR_CLASIFICADOR="administrarClasificador";
	
	// ---> Compensacion
	// Compensacion
	public static final String ACTION_BUSCAR_COMPENSACION     		= "buscarCompensacion";
	public static final String FWD_COMPENSACION_SEARCHPAGE 	    = "compensacionSearchPage";
	public static final String ACTION_ADMINISTRAR_COMPENSACION 	= "administrarCompensacion";
	public static final String FWD_COMPENSACION_VIEW_ADAPTER 		= "compensacionViewAdapter";

	public static final String FWD_COMPENSACION_ADAPTER 		    = "compensacionAdapter";
	public static final String ACTION_ADMINISTRAR_ENCCOMPENSACION = "administrarEncCompensacion";	
	public static final String FWD_COMPENSACION_ENC_EDIT_ADAPTER 	= "compensacionEncEditAdapter";	
	public static final String PATH_ADMINISTRAR_COMPENSACION     	= "/bal/AdministrarCompensacion";
	
	// Incluir/Excluir SaldoAFavor
	public static final String ACT_INCLUIR_SALDO_A_FAVOR  = "incluirSaldoAFavor";
	public static final String ACT_EXCLUIR_SALDO_A_FAVOR  = "excluirSaldoAFavor";
	public static final String ACTION_ADMINISTRAR_SALDO_EN_COMPENSACION = "administrarSaldoEnCompensacion";
	public static final String FWD_COMPENSACION_INCLUIR_SALDO_ADAPTER 	= "compensacionIncluirSaldoAdapter";
	public static final String FWD_COMPENSACION_EXCLUIR_SALDO_ADAPTER 	= "compensacionExcluirSaldoAdapter";

	// ComDeu
	public static final String ACTION_ADMINISTRAR_COMDEU  = "administrarComDeu";
	public static final String FWD_COMDEU_EDIT_ADAPTER 	 = "comDeuEditAdapter";
	public static final String FWD_COMDEU_VIEW_ADAPTER 	 = "comDeuViewAdapter";

	// TipoCob
	public static final String FWD_TIPOCOB_SEARCHPAGE 	    = "tipoCobSearchPage";
	public static final String ACTION_ADMINISTRAR_TIPOCOB 	= "administrarTipoCob";
	public static final String FWD_TIPOCOB_VIEW_ADAPTER		= "tipoCobViewAdapter";
	public static final String FWD_TIPOCOB_EDIT_ADAPTER 	= "tipoCobEditAdapter";

	// AuxCaja7
	public static final String FWD_AUXCAJA7_SEARCHPAGE 	    = "auxCaja7SearchPage";
	public static final String ACTION_BUSCAR_AUXCAJA7   	= "buscarAuxCaja7";
	public static final String ACTION_ADMINISTRAR_AUXCAJA7  = "administrarAuxCaja7";
	public static final String FWD_AUXCAJA7_VIEW_ADAPTER 	 = "auxCaja7ViewAdapter";
	public static final String FWD_AUXCAJA7_EDIT_ADAPTER 	 = "auxCaja7EditAdapter";

	// LEYPARACU
	public static final String FWD_LEYPARACU_SEARCHPAGE 	    = "leyParAcuSearchPage";
	public static final String ACTION_ADMINISTRAR_LEYPARACU 	= "administrarLeyParAcu";
	public static final String FWD_LEYPARACU_VIEW_ADAPTER		= "leyParAcuViewAdapter";
	public static final String FWD_LEYPARACU_EDIT_ADAPTER 	= "leyParAcuEditAdapter";
	
	// Consultar Total por Nodo
	public static final String FWD_CONSULTANODO_SEARCHPAGE   = "consultaNodoSearchPage";
	
	// Reporte de Total por Partida
	public static final String FWD_TOTALPAR_REPORT   = "totalParReport";

	// ---> Reporte ClaCom
	public static final String FWD_CLACOM_REPORT = "claComReport";
	// <--- Reporte ClaCom
	
	// Control de Conciliacion
	public static final String FWD_CONTROLCONCILIACION_SEARCHPAGE   = "controlConciliacionSearchPage";
	
	//Envio Osiris
	public static final String ACTION_BUSCAR_ENVIOOSIRIS     	= "buscarEnvioOsiris";
	public static final String FWD_ENVIOOSIRIS_SEARCHPAGE		= "envioOsirisSearchPage";
	public static final String ACTION_ADMINISTRAR_ENVIOOSIRIS 	= "administrarEnvioOsiris";
	public static final String FWD_ENVIOOSIRIS_VIEW_ADAPTER 	= "envioOsirisViewAdapter";
	public static final String FWD_ENVIOOSIRIS_EDIT_ADAPTER   	= "envioOsirisEditAdapter";
	public static final String PATH_ADMINISTRAR_ENVIOOSIRIS    	= "/bal/AdministrarEnvioOsiris";
	public static final String ACT_OBTENER_ENVIOOSIRIS	  		= "obtenerEnvios";
	public static final String ACT_PROCESAR_ENVIOOSIRIS	  		= "procesarEnvios";
	
	//Cierre Banco
	public static final String ACTION_BUSCAR_CIERREBANCO     	= "buscarCierreBanco";
	public static final String FWD_CIERREBANCO_SEARCHPAGE		= "cierreBancoSearchPage";
	public static final String ACTION_ADMINISTRAR_CIERREBANCO = "administrarCierreBanco";
	public static final String FWD_CIERREBANCO_VIEW_ADAPTER = "cierreBancoViewAdapter";
	public static final String FWD_CIERREBANCO_EDIT_ADAPTER = "cierreBancoEditAdapter";
	
	// Conciliacion
	public static final String ACTION_ADMINISTRAR_CONCILIACION = "administrarConciliacion";
	public static final String FWD_CONCILIACION_VIEW_ADAPTER = "conciliacionViewAdapter";
		
	//Transaccion AFIP
	public static final String ACTION_ADMINISTRAR_TRANAFIP  = "administrarTranAfip";
	public static final String FWD_TRANAFIP_VIEW_ADAPTER = "tranAfipViewAdapter";
	
	//Detalle DJ
	public static final String ACTION_ADMINISTRAR_DETALLEDJ  = "administrarDetalleDJ";
	public static final String FWD_DETALLEDJ_VIEW_ADAPTER = "detalleDJViewAdapter";
	
	//Detalle Pago
	public static final String ACTION_ADMINISTRAR_DETALLEPAGO  = "administrarDetallePago";	
	public static final String FWD_DETALLEPAGO_VIEW_ADAPTER = "detallePagoViewAdapter";

	// MovBan
	public static final String FWD_MOVBAN_SEARCHPAGE 	    = "movBanSearchPage";
	public static final String ACTION_BUSCAR_MOVBAN	  	 	= "buscarMovBan";
	public static final String ACTION_ADMINISTRAR_MOVBAN  = "administrarMovBan";
	public static final String FWD_MOVBAN_VIEW_ADAPTER 	 = "movBanViewAdapter";
	//public static final String FWD_MOVBAN_EDIT_ADAPTER 	 = "movBanEditAdapter";
	public static final String ACTION_ADMINISTRAR_ENC_MOVBAN = "administrarEncMovBan";
	public static final String FWD_MOVBAN_ENC_EDIT_ADAPTER = "movBanEncEditAdapter";
	public static final String PATH_ADMINISTRAR_MOVBAN = "/bal/AdministrarMovBan";
	public static final String FWD_MOVBAN_ADAPTER = "movBanAdapter";
	// MovBan
	public static final String ACTION_ADMINISTRAR_MOVBANDET = "administrarMovBanDet";
	public static final String FWD_MOVBANDET_EDIT_ADAPTER = "movBanDetEditAdapter";
	public static final String FWD_MOVBANDET_VIEW_ADAPTER = "movBanDetViewAdapter";

}



