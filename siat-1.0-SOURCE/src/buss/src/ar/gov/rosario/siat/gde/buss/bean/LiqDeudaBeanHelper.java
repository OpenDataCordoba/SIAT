//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.bean.DetallePago;
import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.SiatCache;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cyq.buss.bean.ProDet;
import ar.gov.rosario.siat.cyq.buss.bean.Procedimiento;
import ar.gov.rosario.siat.cyq.iface.model.ConstanciaDeudaAdapter;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoDefinition;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.gde.iface.model.LiqAtrValorVO;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdminVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAnuladaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaCyQVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqExencionVO;
import ar.gov.rosario.siat.gde.iface.model.LiqExencionesVO;
import ar.gov.rosario.siat.gde.iface.model.LiqTitularVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.TipoDocApoVO;
import ar.gov.rosario.siat.gde.iface.model.TipoPerForVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.EstCue;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.RecAtrCueV;
import ar.gov.rosario.siat.pad.iface.model.ConAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteDefinition;
import ar.gov.rosario.siat.pad.iface.model.EstCueVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.rec.buss.bean.EstadoObra;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import ar.gov.rosario.siat.rec.buss.bean.PlaCuaDet;
import ar.gov.rosario.swe.iface.model.SweContext;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaTimer;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.EstadoPeriodo;
import coop.tecso.demoda.iface.model.ImpuestoAfip;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;
import coop.tecso.demoda.iface.model.Vigencia;

/**
 * Encapsula todos los algoritmos para obtener los bloques de informacion
 * que se muestran en la Liquidacion de la Deuda.
 * 
 * Esta clase es utilizada por todos los usuarios de internet, procuradores,
 * y demas. La performance y el tino son necesario para esta clase.
 * 
 * @author Tecso Coop. Ltda.
 */
public class LiqDeudaBeanHelper {

	private static Logger log = Logger.getLogger(LiqDeudaBeanHelper.class);
	public static int OBJIMP = 1;      		 /** Datos del objeto imponible */
	public static int TITULARES = 2;  		 /** Datos de los titulares de la cuenta */
	public static int ATR_CONTR = 4;		 /** Datos de Atributos del contribuyente */
	public static int CONVENIOS = 8;  		 /** Datos de los convenios de la cuenta*/
	public static int DESYUNIF = 16;  		 /** Datos de desgloces y unificaciones de la cuenta */
	public static int ATR_CUE_4_WEB = 32;    /** Atributos de la Cuenta visible desde la liquidacion */
	public static int ATR_CUE_4_RECIBO = 64; /** Atributos de la Cuenta visible desde el recibo */
	public static int ALL = 0xff;     		 /** Todos los datos */
	public static int NONE = 0;
	public boolean esParaEstadoCuenta = false;


	Cuenta cuenta = null; // Este objeto cuenta, con el cual se construye "LiqDeudaBeanHelper" es utilizado para pasaje de errores/mensajes
	Convenio convenio = null;
	Procedimiento procedimiento = null;
	Date fechaActualizacion = new Date();
	HashMap<String, DetallePago> detallePagos; //mapa de detalles de pago osiris para esta cuenta. clave="deuda.anio;deuda.periodo"
	
	boolean forzarNoPermitirAccionSobreDeuda = false; // Si esta bandera es true, no se permitira accion sobre registro de deuda.
	
	CuentaTitular cuentaTitular = null; // Utilizado para aplicar logica de mostrar deuda que corresponda al periodo de vigencia del titular: GrE.
	
	boolean simularSaldoPorCaducidad = false;
	
	boolean cuentaPoseeTitularesVigentes = true; // Bandera utilizada para mostrar mensaje el liquidacion de deuda.
	
	// Metodos de clase
	/**
	 * Pasa los propiedades utilizasa como filtros de LiqCuentaOrigen a LiqCuentaDestino.
	 * 
	 * @param liqCuenta
	 */
	public static void passFilters(LiqCuentaVO liqCuentaOrigen, LiqCuentaVO liqCuentaDestino){ 
		
		liqCuentaDestino.setEsRecursoAutoliquidable(liqCuentaOrigen.getEsRecursoAutoliquidable());
		liqCuentaDestino.setEstadoPeriodo(liqCuentaOrigen.getEstadoPeriodo());
	    
		if (ModelUtil.isNullOrEmpty(liqCuentaOrigen.getRecClaDeu())){
			liqCuentaDestino.setRecClaDeu(new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_TODAS));
		} else {
			liqCuentaDestino.setRecClaDeu(liqCuentaOrigen.getRecClaDeu());

			// Habria que ver si se puede evitar el getById y usar un cache.			
			RecClaDeu recClaDeu = RecClaDeu.getById(liqCuentaDestino.getRecClaDeu().getId());
			liqCuentaDestino.getRecClaDeu().setDesClaDeu(recClaDeu.getDesClaDeu());
		}
		
		liqCuentaDestino.setFechaVtoDesde(liqCuentaOrigen.getFechaVtoDesde()); 
		liqCuentaDestino.setFechaVtoHasta(liqCuentaOrigen.getFechaVtoHasta());
		
		// Propiedades para logica Deuda Sigue Titular.
		liqCuentaDestino.setIdCuentaTitular(liqCuentaOrigen.getIdCuentaTitular());
		liqCuentaDestino.setDeudaSigueTitular(liqCuentaOrigen.getDeudaSigueTitular());

	}
	
	
	/**
	 * Constructor que recibe y setea una cuenta.
	 * 
	 * @param cuenta
	 * @throws DemodaServiceException
	 */
	public LiqDeudaBeanHelper(Cuenta cuenta) throws DemodaServiceException {					
		this.cuenta = cuenta;
		this.setearBanderasFiltro();
	}
	
	/**
	 * Constructor que recibe un convenio, lo setea y setea la cuenta del convenio o el procedimiento.
	 * 
	 * @param convenio
	 * @throws DemodaServiceException
	 */
	public LiqDeudaBeanHelper(Convenio convenio) throws DemodaServiceException {					
		this.convenio = convenio;
		this.cuenta = convenio.getCuenta();
		this.procedimiento = convenio.getProcedimiento();
		this.setearBanderasFiltro();
	}
	
	/**
	 * Constructor que recibe un procedimiento de Concurso y Quiebras.
	 * 
	 * @param procedimiento
	 * @throws DemodaServiceException
	 */
	public LiqDeudaBeanHelper(Procedimiento procedimiento) throws DemodaServiceException {					
		this.procedimiento = procedimiento;
		this.fechaActualizacion = procedimiento.getFechaAuto();
	}
	
	
	/**
	 * Constructor que recibe Cuenta y Fecha de Actualizacion
	 * 
	 * @param cuenta
	 * @param fechaActualizacion
	 * @throws DemodaServiceException
	 */
	public LiqDeudaBeanHelper(Cuenta cuenta, Date fechaActualizacion) throws DemodaServiceException {					
		this.cuenta = cuenta;
		this.fechaActualizacion = fechaActualizacion;
		this.setearBanderasFiltro();
	}
	
	
	/**
	 * Chequea si la cuenta llega con un LiqCuentaVO como filtro y si tiene la bandera "DeudaSigueTitular", obtiene y setea
	 * el CuentaTitular correspondiente.
	 * 
	 */
	private void setearBanderasFiltro(){
		
		String funcName = "setearBanderasFiltro";
		
		if (cuenta.getLiqCuentaFilter() == null){
			cuenta.setLiqCuentaFilter(new LiqCuentaVO());
		}
		
		// Si el CuentaFilter tiene la bandera "DeudaSigueTitular" = true y
		// IdCuentaTitular es no nulo, (nos indica que venimos del CUS "Deuda por Contribuyente")
		log.debug(funcName + " cuenta fitro presente:" +  (cuenta.getLiqCuentaFilter() != null)); 
		if (cuenta.getLiqCuentaFilter() != null 
				&& cuenta.getLiqCuentaFilter().getDeudaSigueTitular() 
					&& cuenta.getLiqCuentaFilter().getIdCuentaTitular() != null){

			log.debug(funcName + " DeudaSigueTitular: " + cuenta.getLiqCuentaFilter().getDeudaSigueTitular());
			log.debug(funcName + " idCuentaTitular: " + cuenta.getLiqCuentaFilter().getIdCuentaTitular());  
			
			cuentaTitular = CuentaTitular.getById(cuenta.getLiqCuentaFilter().getIdCuentaTitular());
			
			log.debug(funcName + " cuentaTitular.fechaDesde: " + DateUtil.formatDate(cuentaTitular.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + 
					" fechaHasta: " + DateUtil.formatDate(cuentaTitular.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK));
		}
		
		log.debug(funcName + " Deuda Exigible una vez Vencida: " +  (cuenta.getRecurso().getDeuExiVen().intValue()==0?"No":"Si")); 
		if (cuenta.getRecurso().getDeuExiVen() != null && cuenta.getRecurso().getDeuExiVen().intValue() == 1){
			cuenta.getLiqCuentaFilter().setDeuExiVen(true);			
		}
		
	}
	
	
	/**
	 * Obtiene los datos a mostrar en la pantalla de Detalles de "Convenio"
	 * Los bloques a mostrar son:
	 * - # Cuenta Seleccionada / Procedimiento Cyq
	 * - # Datos del Convenio
	 * - # Datos de Formalizacion
	 * - # Periodos Incluidos 
	 * - # Cuotas Pagas
	 * - # Cuotas Inpagas
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public LiqConvenioCuentaAdapter getLiqConvenioCuentaAdapter() throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		UserContext userContext = DemodaUtil.currentUserContext();

		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				enter: " + funcName );
			log.debug( "##################################################################################################");		
		}
	
		LiqConvenioCuentaAdapter liqConvenioCuentaAdapter = new LiqConvenioCuentaAdapter();
		

		// Convenios de deuda comun
		if (convenio.getCuenta() != null){
			liqConvenioCuentaAdapter.setCuenta(this.getCuenta());
		
		// Convenios de deuda privilegio
		} else {
			
			Procedimiento procedimiento = convenio.getProcedimiento();
			liqConvenioCuentaAdapter.setProcedimiento((ProcedimientoVO)procedimiento.toVOWithPersona());
		}
		
		liqConvenioCuentaAdapter.setConvenio(this.getConvenio());
		
		liqConvenioCuentaAdapter.setTieneCuotaSaldo(convenio.tieneCuotaSaldo());
		
		liqConvenioCuentaAdapter.getConvenio().setFechaAltaView(DateUtil.formatDate(convenio.getFechaAlta(), DateUtil.ddSMMSYYYY_MASK));
		
		log.debug("cuotaSaldo: "+convenio.tieneCuotaSaldo());
		liqConvenioCuentaAdapter.getConvenio().setDesEstadoConvenio(this.convenio.getDescEstadoConvenio());
		
		// Seteo de Visibilidad y Permisos para las Acciones
		liqConvenioCuentaAdapter.setImprimirRecibosVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_IMPRIMIR_RECIBOS_CONVENIO)); 
		liqConvenioCuentaAdapter.setImprimirFormularioFormalVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_IMPRIMIR_FORM_CONVENIO)); 
		liqConvenioCuentaAdapter.setGenerarCuotaSaldoVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_GENERAR_CUOTA_SALDO)); 
		liqConvenioCuentaAdapter.setGenerarSaldoPorCaducidadVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_GENERAR_SALDO_X_CADUCIDAD)); 
		liqConvenioCuentaAdapter.setAtrasSaldoPorCaducidadVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_ATRAS_SALDO_X_CADUCIDAD)); 
		liqConvenioCuentaAdapter.setAplicarPagoACuentaVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_APLICAR_PAGO_ACUENTA)); 
		liqConvenioCuentaAdapter.setGenerarRescateVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_GENERAR_RESCATE_IND));
		// Se muestra si tiene permiso y el convenio no tiene pagos
		liqConvenioCuentaAdapter.setAnularConvenioVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_ANULAR_CONVENIO)&& !convenio.registraPagos());
		liqConvenioCuentaAdapter.setVerHistoricoConvenio(!userContext.getEsAnonimo());
		liqConvenioCuentaAdapter.setVerificarConsistenciaVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_VERIFICAR_CONSIST_CONV));
		liqConvenioCuentaAdapter.setReclamarAsentamientoVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_RECLAMAR_ACENT));
		
		
		boolean seleccionableCyq = false;
		boolean seleccionableAdmin = false;
		boolean seleccionableJudicial = false;
		boolean perteneceAProcurador = false;
		
				
		if (this.convenio.getPlan().getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_CYQ)
			seleccionableCyq = true;
		
		if (this.convenio.getPlan().getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_ADMIN)
			seleccionableAdmin = true;
		
		if (this.convenio.getPlan().getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL)
			seleccionableJudicial = true;
		
		if (userContext.getIdProcurador() != null){
			if (convenio.getProcurador() != null &&
					convenio.getProcurador().getId().longValue() == userContext.getIdProcurador().longValue()){
				perteneceAProcurador = true;
			}
		}
		
		log.debug(" ConvenioCuenta seleccionableAdmin: " + seleccionableAdmin +
				" seleccionableJudicial: " + seleccionableJudicial + 
				" perteneceAProcurador: " + perteneceAProcurador);
		
		liqConvenioCuentaAdapter.setImprimirRecibosEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_IMPRIMIR_RECIBOS_CONVENIO,
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador	));
		liqConvenioCuentaAdapter.setImprimirFormularioFormalEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_IMPRIMIR_FORM_CONVENIO,
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador	)); 
		liqConvenioCuentaAdapter.setGenerarCuotaSaldoEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_GENERAR_CUOTA_SALDO,
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador	)); 
		liqConvenioCuentaAdapter.setGenerarSaldoPorCaducidadEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_GENERAR_SALDO_X_CADUCIDAD,
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador	)); 
		liqConvenioCuentaAdapter.setAtrasSaldoPorCaducidadEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_ATRAS_SALDO_X_CADUCIDAD,
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador	)); 
		liqConvenioCuentaAdapter.setAplicarPagoACuentaEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_APLICAR_PAGO_ACUENTA,
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador	));
		liqConvenioCuentaAdapter.setGenerarRescateEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_GENERAR_RESCATE_IND, 
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador));
		liqConvenioCuentaAdapter.setAnularConvenioEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_ANULAR_CONVENIO, 
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador));
		
		Long idProcuradorConvenio = null;
		
		if (convenio.getProcurador() != null && convenio.getProcurador().getId() != null){
			idProcuradorConvenio = convenio.getProcurador().getId();
		}
		
		// Seteo de permisos a los registros de Cuotas Impagas		
		for(LiqCuotaVO liqCuotaImpaga:liqConvenioCuentaAdapter.getConvenio().getListCuotaInpaga()){
			
			// Si no es Reclamada.
			if (!liqCuotaImpaga.isEsReclamada()) {
				liqCuotaImpaga.setReclamarAcentEnabled(this.getPermisoRegCuota(GdeSecurityConstants.MTD_RECLAMAR_ACENT, 
						idProcuradorConvenio));
				if (!liqCuotaImpaga.isEsIndeterminada()){
					liqCuotaImpaga.setEsSeleccionable(this.getPermisoRegCuota(GdeSecurityConstants.MTD_SELECT_CUOTA, 
						idProcuradorConvenio));
				}
			}
		}
		
		if (liqConvenioCuentaAdapter.getConvenio().poseeCuotaImpagaSeleccionable()){
			liqConvenioCuentaAdapter.setMostrarChkAllCuotasPagas(true);
		}
		liqConvenioCuentaAdapter.setTieneCuotaSaldo(convenio.tieneCuotaSaldo());
				
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				exit: " + funcName );
			log.debug( "##################################################################################################");		
		}
		return liqConvenioCuentaAdapter;

	}

	/**
	 * Metodo utilizada por muchas funciones, por defecto devuelve LiqDeudaAdapter 
	 * con los titulares de la cuenta obtenidos de "general".
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public LiqDeudaAdapter getLiqDeudaAdapter() throws Exception {
		return getLiqDeudaAdapter(true);
	}
	
	
	/** 
	 * Obtencion de todos los datos a mostrar en cada bloque del adapter de la liquidacion de la deuda.
	 * Este metodo llama a los demas metodos de esta clase.
	 * 
	 * -# Datos a mostrar de la Cuenta
	 * -# Datos a mostar del Recurso
	 * -# Atributos del Objeto Imponible
	 * -# Titulares de la Cuenta
	 * -# Atributos de Contribuyente
	 * - Convenios Asociados
	 * -# Desgloses y unificaciones
	 * -# Cuentas Relacionadas
	 * -# Exenciones Vigentes
	 * 		Caso si posee
	 * -# Solicitudes de Exencion Denegadas
	 * 		Caso si posee   
	 * -# Solicitudes de Exencion en tramite
	 * 		Caso si posee
	 * - Deuda en CyQ
	 * - Deuda en via Judicial
	 * 		Convenios si posee
	 * - Deuda en Vias Administrativa. 
	 * 		Estado de Analisis si posee
	 * - Total de la deuda
	 * - Fecha FechaAcentamiento
	 * 
	 * @return LiqDeudaAdapter
	 * @throws DemodaServiceException
	 */
	public LiqDeudaAdapter getLiqDeudaAdapter(boolean withTitulares) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		UserContext userContext = DemodaUtil.currentUserContext();
		
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				enter: " + funcName );
			log.debug( "##################################################################################################");		
		}

		LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter(); 
		
		liqDeudaAdapterVO.clearError();

		// Chekeamos el estado no suspendido de la obra
		// Segun Categoria del Recurso:
		// CDM
		if (cuenta.getRecurso().getCategoria().getId().longValue() == Categoria.ID_CDM){

			// Chekeamos el estado no suspendido de la obra
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);
			Obra obra = plaCuaDet.getPlanillaCuadra().getObra();
			
			if (EstadoObra.ID_SUSPENDIDA.longValue() == obra.getEstadoObra().getId().longValue()) {
				liqDeudaAdapterVO.addMessageValue("La obra " + obra.getDesObra() + " se encuentra suspendida");
				forzarNoPermitirAccionSobreDeuda = true;
			}
			
			if (EstadoObra.ID_CORREGIDA.longValue() == obra.getEstadoObra().getId().longValue()){
				liqDeudaAdapterVO.addMessageValue("La obra " + obra.getDesObra() + " se encuentra corregida");
				forzarNoPermitirAccionSobreDeuda = true;
			}
		}
		
		// 1:: Traspaso de los datos a mostrar de la cuenta
		// 2:: Recupero los atributos del objeto imponible marcados como visible en consulta de deuda
		// 3:: Recuperacion de Titulares de la cuenta
		// 4:: Atributos de Contribuyente
		// 5:: Convenios Asociados
		// 6:: Desgloses y unificaciones
		liqDeudaAdapterVO.setCuenta(this.getCuenta(withTitulares));
		
		// Seteamos mensaje si no posee titulares.
		if (!cuentaPoseeTitularesVigentes)
			liqDeudaAdapterVO.addMessageValue(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_SIN_TITULARES_ACTIVOS));
		
		// Seteamos mensaje si la cuenta esta cancelada
		//original
		if (liqDeudaAdapterVO.getCuenta().getEstado().getId().intValue() == EstCue.ID_CANCELADO.intValue()) {
			liqDeudaAdapterVO.addMessageValue("La cuenta ha sido Cancelada");
			forzarNoPermitirAccionSobreDeuda = true;
		}
		
		// 7:: Cuentas Relacionadas
		liqDeudaAdapterVO.setListCuentaRel(this.getCuentasRelacionadas());

		// 8:: Exenciones Vigentes - Caso si posee
		// 9:: Solicitudes de Exencion Denegadas - Caso si posee
		// 10:: Solicitudes de Exencion en tramite - Caso si posee
		liqDeudaAdapterVO.setExenciones(this.getExenciones());

		// 11:: Deuda en CyQ
		liqDeudaAdapterVO.setListProcedimientoCyQ(this.getDeudaCyQ());

		// 12:: Deuda en via Judicial - Convenios si posee
		liqDeudaAdapterVO.setListProcurador(this.getDeudaProcurador());
		
		// 13:: Deuda en Vias Administrativa - Estado de Analisis si posee
		liqDeudaAdapterVO.setListGestionDeudaAdmin(this.getDeudaAdmin());
		
		DemodaTimer dt = new DemodaTimer();
		
		// 14:: Total de la deuda
		liqDeudaAdapterVO.setTotal(liqDeudaAdapterVO.calcularTotal());

		// 15:: Fecha Asentamiento: Es la fecha del ultimo Asentamiento procesado exitosamente
		Date fecUtlAseExito = cuenta.getRecurso().getFecUltPag();		
		liqDeudaAdapterVO.setFechaAcentamiento(DateUtil.formatDate(fecUtlAseExito, DateUtil.ddSMMSYYYY_MASK));
		
		// Banderas para el seteo de Permisos a las Acciones sobre la deuda.
		boolean seleccionableCyq = false;
		boolean seleccionableAdmin = false;
		boolean seleccionableJudicial = false;
		boolean perteneceAProcurador = false;
		
		if (liqDeudaAdapterVO.getListProcedimientoCyQ() != null &&
				liqDeudaAdapterVO.getListProcedimientoCyQ().size() > 0) {
			if (userContext.getEsUsuarioCyq()){
				for (LiqDeudaCyQVO ldcyq:liqDeudaAdapterVO.getListProcedimientoCyQ()){
					if (ldcyq.poseeDeudaSeleccionable()){
						seleccionableCyq = true;
					}
				}
			}
		}
		
		if (liqDeudaAdapterVO.getListGestionDeudaAdmin() != null &&
				liqDeudaAdapterVO.getListGestionDeudaAdmin().size() > 0 &&
				liqDeudaAdapterVO.getListGestionDeudaAdmin().get(0) != null && 
				liqDeudaAdapterVO.getListGestionDeudaAdmin().get(0).poseeDeudaSeleccionable())
			seleccionableAdmin = true;
		
		if (liqDeudaAdapterVO.getListProcurador() != null){
			for (LiqDeudaProcuradorVO ldp:liqDeudaAdapterVO.getListProcurador()){
				// Seteo de bandera seleccionableJudicial
				if (ldp.poseeDeudaSeleccionable()){
					seleccionableJudicial = true;
				}
					
				log.debug("idProcurador: " + ldp.getIdProcurador() + 
							"  userContext.getIdProcurador(): " + userContext.getIdProcurador());
				// Seteo de bandera perteneceDeduaProcurador	
				if (ldp.getIdProcurador() != null && userContext.getIdProcurador() != null &&
							ldp.getIdProcurador().longValue() == userContext.getIdProcurador()){
						perteneceAProcurador = true;
				}
			}
		}
		
		// Seteo de mensaje para procurador y bandera "PoseeDeduaProcurador"
		if (userContext.getIdProcurador() != null){
			if (perteneceAProcurador){
				liqDeudaAdapterVO.setPoseeDeudaProcurador(true);
			} else {
				liqDeudaAdapterVO.setMsgProcurador();
				liqDeudaAdapterVO.setPoseeDeudaProcurador(false);			
			}
		}
		
		/* 
		 * Si Existe deuda en via judicial y 
		 * el usuario es Procurador y posee deuda o
		 * el usuario es Operador Judicial 
		 *  
		 * chkAll en deuda Judicial 
		 */ 
		if (liqDeudaAdapterVO.getListProcurador() != null && 
				liqDeudaAdapterVO.getListProcurador().size() > 0){
			if (liqDeudaAdapterVO.isPoseeDeudaProcurador() ||
					userContext.getEsOperadorJudicial()){
				for (LiqDeudaProcuradorVO ldp:liqDeudaAdapterVO.getListProcurador()){
					if (ldp.poseeDeudaSeleccionable()){
						ldp.setMostrarChkAllJudicial(true);
					}
				}
			}
		} 
		
		/*
		 * Si existe bloque deuda Admin y
		 * el usuario no es Procurador, Operador Judicial ni Escribano
		 * 
		 * y existe al menos un registro de deuda seleccionable
		 * 
		 * chkAll en deuda Administrativa
		 */
		if (liqDeudaAdapterVO.getListGestionDeudaAdmin() != null &&
				liqDeudaAdapterVO.getListGestionDeudaAdmin().size() > 0) {
			if (!userContext.getEsProcurador() && 
					!userContext.getEsOperadorJudicial() &&
					!userContext.getEsEscribano()){
				if (liqDeudaAdapterVO.getListGestionDeudaAdmin().get(0).poseeDeudaSeleccionable())
					liqDeudaAdapterVO.setMostrarChkAllAdmin(true);
			}
		}
		
		
		/*
		 * Si existe bloque deuda Cyq y es usuario CyQ 
		 * y existe al menos un registro de deuda seleccionable
		 * 
		 * chkAll en deuda Cyq
		 */
		if (liqDeudaAdapterVO.getListProcedimientoCyQ() != null &&
				liqDeudaAdapterVO.getListProcedimientoCyQ().size() > 0) {
			if (userContext.getEsUsuarioCyq()){
				for (LiqDeudaCyQVO ldcyq:liqDeudaAdapterVO.getListProcedimientoCyQ()){
					if (ldcyq.poseeDeudaSeleccionable()){
						ldcyq.setMostrarChkAllCyQ(true);
					}
				}
			}
		}
		
		// Seteo de Permisos para los links	
		
		boolean tieneObjImp = (liqDeudaAdapterVO.getCuenta().getIdObjImp()!=null) && 
															(liqDeudaAdapterVO.getCuenta().getIdObjImp()>0);
		liqDeudaAdapterVO.setVerDetalleObjImpEnabled(tieneObjImp?this.getPermisoSwe(
														   GdeSecurityConstants.MTD_VER_DETALLE_OBJIMP):false);
		liqDeudaAdapterVO.setVerHistoricoContribEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_VER_DETALLE_HISTORICOCONTRIB));
		liqDeudaAdapterVO.setVerDeudaContribEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_VER_DEUDA_CONTRIB));
		liqDeudaAdapterVO.setVerConvenioEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_VER_CONVENIO));
		liqDeudaAdapterVO.setVerCuentaDesgUnifEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_VER_CUENTA_DESG_UNIF));
		liqDeudaAdapterVO.setVerCuentaRelEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_VER_CUENTA_REL));
		liqDeudaAdapterVO.setBuzonCambiosEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_BUZON_CAMBIOS));
		liqDeudaAdapterVO.setVerHistoricoExeEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_VER_HISTORICO_EXENCION));
		liqDeudaAdapterVO.setVerPlanillaEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_VER_PLANILLA));

		liqDeudaAdapterVO.setReconfeccionarVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_RECONFECCIONAR)); 
		liqDeudaAdapterVO.setReconfeccionarEspVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_RECONFECCIONAR_ESP));
		liqDeudaAdapterVO.setImprimirInformeDeudaVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_IMPRIMIR_INFORME_DEUDA)); 
		liqDeudaAdapterVO.setFormalizarConvenioVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO));
		liqDeudaAdapterVO.setFormalizarConvenioEspVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO_ESP)); 
		liqDeudaAdapterVO.setInfDeudaEscribanoVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_INFORME_ESCRIBANO));
		liqDeudaAdapterVO.setReclamarAcentVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_RECLAMAR_ACENT));
		
		// Seteo de Visibilidad y Permisos para las Acciones
		// Segun Categoria del Recurso:
		// CDM
		if (cuenta.getRecurso().getCategoria().getId().longValue() == Categoria.ID_CDM){
			// si la obra no se encuentra suspendida.
			if (!forzarNoPermitirAccionSobreDeuda) {
				liqDeudaAdapterVO.setCambioPlanCDMVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_CAMBIOPLAN_CDM));
				liqDeudaAdapterVO.setCuotaSaldoCDMVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_CUOTASALDO_CDM));
				
				liqDeudaAdapterVO.setCambioPlanCDMEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_CAMBIOPLAN_CDM, 
						seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador));
				liqDeudaAdapterVO.setCuotaSaldoCDMEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_CUOTASALDO_CDM, 
						seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador));
			} 
		}
				
		// Derecho de Acceso
		if (cuenta.getRecurso().getCategoria().getId().longValue() == Categoria.ID_ESP_PUB){
			liqDeudaAdapterVO.setDdjjEntVenHabVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_DDJJENTVEN_HAB));
			liqDeudaAdapterVO.setDdjjEntVenHabEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_DDJJENTVEN_HAB));
		}
		
		// Drei
		if (cuenta.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
			if (!forzarNoPermitirAccionSobreDeuda) {
				liqDeudaAdapterVO.setCierreComercioVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_CIERRE_COMERCIO));
				liqDeudaAdapterVO.setCierreComercioEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_CIERRE_COMERCIO));
			}
			
			// Ver Novedades de Regimen Simplificado
			liqDeudaAdapterVO.setVerNovedadesRSVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_VER_NOVEDADES_RS));
			liqDeudaAdapterVO.setVerNovedadesRSEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_VER_NOVEDADES_RS));			
		}

		// Drei o Etur
		if (cuenta.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI) || 
				cuenta.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_ETuR)){
			if (!forzarNoPermitirAccionSobreDeuda) {
				liqDeudaAdapterVO.setDecJurMasivaVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS));
				liqDeudaAdapterVO.setDecJurMasivaEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS));
			}
			
			// Generar Volante de Pago 
			liqDeudaAdapterVO.setVolantePagoIntRSVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_VOLANTEPAGOINTRS));
			liqDeudaAdapterVO.setVolantePagoIntRSEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_VOLANTEPAGOINTRS));
		}
		// Imprimir Cierre Comercio
		if (cuenta.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI)&& cuenta.getFechaBaja()!=null){
			if (!forzarNoPermitirAccionSobreDeuda) {
				liqDeudaAdapterVO.setImprimirCierreComercioVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_IMPRIMIR_CIERRE_COMERCIO));
				liqDeudaAdapterVO.setImprimirCierreComercioEnabled(this.getPermisoSwe(GdeSecurityConstants.MTD_IMPRIMIR_CIERRE_COMERCIO));
			} 
		}
		
		// Para recursos Autoliquidables
		if (cuenta.getRecurso().getEsAutoliquidable().intValue() ==1 ){
			liqDeudaAdapterVO.getCuenta().setEsRecursoAutoliquidable(true);
			if (!forzarNoPermitirAccionSobreDeuda) {
			liqDeudaAdapterVO.setDesglosarAjusteVisible(this.getPermisoSwe(GdeSecurityConstants.MTD_DESGLOSAR_AJUSTE));
			liqDeudaAdapterVO.setDesglosarAjusteEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_DESGLOSAR_AJUSTE, 
					seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador));
			}
			liqDeudaAdapterVO.getRecurso().setEsAutoliquidable(SiNo.SI);
		}
		
		// Todos los demas recursos (hasta que se implementen los demas recursos)
		liqDeudaAdapterVO.setReconfeccionarEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_RECONFECCIONAR , 
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador));
		liqDeudaAdapterVO.setReconfeccionarEspEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_RECONFECCIONAR_ESP , 
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador));
		liqDeudaAdapterVO.setFormalizarConvenioEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO , 
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador));
		liqDeudaAdapterVO.setFormalizarConvenioEspEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO_ESP , 
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador));
		liqDeudaAdapterVO.setReclamarAcentEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_RECLAMAR_ACENT , 
				seleccionableCyq, seleccionableAdmin, seleccionableJudicial, perteneceAProcurador));
		
		// Permisos que no depende que halla deuda "seleccionable"
		liqDeudaAdapterVO.setImprimirInformeDeudaEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_IMPRIMIR_INFORME_DEUDA , 
				true, true, true, perteneceAProcurador));
		liqDeudaAdapterVO.setInfDeudaEscribanoEnabled(this.getPermisoAccionesAdapter(GdeSecurityConstants.MTD_INFORME_ESCRIBANO , 
				true, true, true, true));
		
		
		log.info(dt.stop("LiqDeuda 14, 15, FechaAsentamiento, Total y Permisos"));
		
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				exit: " + funcName );
			log.debug( "##################################################################################################");		
		}
		
		
		return liqDeudaAdapterVO;

	}
	
	/**
	 * 
	 * Obtiene los datos a mostrar en el caso de uso Administar deuda Reclamada (SINC). 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public LiqDeudaAdapter getLiqDeudaAdapter4DeudaReclamada() throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				enter: " + funcName );
			log.debug( "##################################################################################################");		
		}

		LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter(); 

		liqDeudaAdapterVO.clearError();

		// 1:: Traspaso de los datos a mostrar de la cuenta
		// 2:: Recupero los atributos del objeto imponible marcados como visible en consulta de deuda
		// 3:: Recuperacion de Titulares de la cuenta
		// 4:: Atributos de Contribuyente
		// 5:: Convenios Asociados
		// 6:: Desgloses y unificaciones
		liqDeudaAdapterVO.setCuenta(this.getCuenta());

		// 7:: Cuentas Relacionadas
		liqDeudaAdapterVO.setListCuentaRel(this.getCuentasRelacionadas());

		// 8:: Exenciones Vigentes - Caso si posee
		// 9:: Solicitudes de Exencion Denegadas - Caso si posee
		// 10:: Solicitudes de Exencion en tramite - Caso si posee
		liqDeudaAdapterVO.setExenciones(this.getExenciones());

		// 11:: Deuda en CyQ
		//liqDeudaAdapterVO.setListProcedimientoCyQ(this.getDeudaCyQ());

		// 12:: Deuda en via Judicial - Convenios si posee
		liqDeudaAdapterVO.setListProcurador(this.getDeudaProcurador());

		// 13:: Deuda en Vias Administrativa - Estado de Analisis si posee
		liqDeudaAdapterVO.setListGestionDeudaAdmin(this.getDeudaAdmin());

		// 14:: Total de la deuda
		liqDeudaAdapterVO.setTotal(liqDeudaAdapterVO.calcularTotal());

		// 15:: Fecha Acentamiento
		Date fecUtlAseExito = cuenta.getRecurso().getFecUltPag();		
		liqDeudaAdapterVO.setFechaAcentamiento(DateUtil.formatDate(fecUtlAseExito, DateUtil.ddSMMSYYYY_MASK));
	
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				exit: " + funcName );
			log.debug( "##################################################################################################");		
		}
		return liqDeudaAdapterVO;

	}
	
	
	/**
	 * 
	 * Obtiene los datos a mostrar de la cuenta y la dedua actualizada a la fecha auto del procedimiento cyq.
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public LiqDeudaAdapter getLiqDeudaAdapter4EnvioDeudaCyQ(Date fechaAuto) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		UserContext userContext = DemodaUtil.currentUserContext();
		
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###			enter: " + funcName );
			log.debug( "###			fechaAuto: " + DateUtil.formatDate(fechaAuto, DateUtil.ddSMMSYYYY_MASK));
			log.debug( "##################################################################################################");		
		}

		this.simularSaldoPorCaducidad = true;
		
		LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter(); 

		//liqDeudaAdapterVO.setSimularSalPorCad(simularSalPorCad);	
		
		liqDeudaAdapterVO.clearError();

		// Si es recurso DReI - Aplicamos filtro por IMPAGO
		if (cuenta.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
			LiqCuentaVO liqCuentaFilter = new LiqCuentaVO();
			liqCuentaFilter.setEstadoPeriodo(EstadoPeriodo.IMPAGOS);
			cuenta.setLiqCuentaFilter(liqCuentaFilter);
			setearBanderasFiltro();
		}	
		
		// 1:: Traspaso de los datos a mostrar de la cuenta
		// 2:: Recupero los atributos del objeto imponible marcados como visible en consulta de deuda
		// 3:: Recuperacion de Titulares de la cuenta
		// 4:: Atributos de Contribuyente
		// 5:: Convenios Asociados
		// 6:: Desgloses y unificaciones
		liqDeudaAdapterVO.setCuenta(this.getCuenta());

		liqDeudaAdapterVO.getCuenta().setIdCuenta(liqDeudaAdapterVO.getCuenta().getIdCuenta());
		
		// 7:: Cuentas Relacionadas
		liqDeudaAdapterVO.setListCuentaRel(this.getCuentasRelacionadas());

		// 8:: Exenciones Vigentes - Caso si posee
		// 9:: Solicitudes de Exencion Denegadas - Caso si posee
		// 10:: Solicitudes de Exencion en tramite - Caso si posee
		liqDeudaAdapterVO.setExenciones(this.getExenciones());

		// 11:: Deuda en CyQ
		liqDeudaAdapterVO.setListProcedimientoCyQ(this.getDeudaCyQ());

		// 12:: Deuda en via Judicial - Convenios si posee
		List<LiqDeudaProcuradorVO> listBloqueProcurador = this.getDeudaProcurador();
		
		// Deshabilitamos la deuda posterior a la fechaAuto del Procedimiento
		log.debug(funcName + " fechaAuto: " + DateUtil.formatDate(fechaAuto, DateUtil.ddSMMSYYYY_MASK));
		for (LiqDeudaProcuradorVO bloqueDeudaProcurador: listBloqueProcurador){
			for (LiqDeudaVO liqDeudaVO:bloqueDeudaProcurador.getListDeuda()){
				if (DateUtil.isDateAfter(liqDeudaVO.getFechaVencimiento(), fechaAuto)) {				
					log.debug(funcName  +  " ### periodo " + liqDeudaVO.getPeriodoDeuda() + " no seleccionable");
					liqDeudaVO.setEsSeleccionable(false);
				} else {
					// Deuda que cae que se podria incluir en el procedimiento
					liqDeudaAdapterVO.setPoseeDeudaJudicial4Cyq(true);
					
					if (liqDeudaVO.getPoseeConvenio())
						liqDeudaAdapterVO.setPoseeConvenio(true);
				}
			}
		}
		
		liqDeudaAdapterVO.setListProcurador(listBloqueProcurador);
		
		
		// 13:: Deuda en Vias Administrativa - Estado de Analisis si posee
		List<LiqDeudaAdminVO> listBloqueDeudaAdmin = this.getDeudaAdmin();

		// Deshabilitamos la deuda posterior a la fechaAuto del Procedimiento
		if (listBloqueDeudaAdmin.size() > 0){
			LiqDeudaAdminVO bloqueDeudaAdmin = listBloqueDeudaAdmin.get(0);
			
			for (LiqDeudaVO liqDeudaVO:bloqueDeudaAdmin.getListDeuda()){
				if (DateUtil.isDateAfter(liqDeudaVO.getFechaVencimiento(), fechaAuto)) {				
					log.debug(funcName  +  " ### periodo " + liqDeudaVO.getPeriodoDeuda() + " no seleccionable");
					liqDeudaVO.setEsSeleccionable(false);
				} else {
					// Deuda que cae que se podria incluir en el procedimiento
					liqDeudaAdapterVO.setPoseeDeudaAdmin4Cyq(true);
					
					if (liqDeudaVO.getPoseeConvenio())
						liqDeudaAdapterVO.setPoseeConvenio(true);
				}
			}
		}
		
		liqDeudaAdapterVO.setListGestionDeudaAdmin(listBloqueDeudaAdmin);
		
		// 14:: Total de la deuda
		liqDeudaAdapterVO.setTotal(liqDeudaAdapterVO.calcularTotal());

		// 15:: Fecha Acentamiento
		Date fecUtlAseExito = cuenta.getRecurso().getFecUltPag();		
		liqDeudaAdapterVO.setFechaAcentamiento(DateUtil.formatDate(fecUtlAseExito, DateUtil.ddSMMSYYYY_MASK));
		
		/* 
		 * Si Existe deuda en via judicial y 
		 * el usuario es Procurador y posee deuda o
		 * el usuario es Operador Judicial 
		 *  
		 * chkAll en deuda Judicial 
		 */ 
		if (liqDeudaAdapterVO.getListProcurador() != null && 
				liqDeudaAdapterVO.getListProcurador().size() > 0){
			
			if (liqDeudaAdapterVO.isPoseeDeudaProcurador() ||
					userContext.getEsOperadorJudicial()){
				
				for (LiqDeudaProcuradorVO ldp:liqDeudaAdapterVO.getListProcurador()){
					if (ldp.poseeDeudaSeleccionable()){
						ldp.setMostrarChkAllJudicial(true);
						liqDeudaAdapterVO.setSeleccionarDeuda4Cyq(true);
					}
				}
			}
		} 
		
		/*
		 * Si existe bloque deuda Admin y
		 * el usuario no es Procurador, Operador Judicial ni Escribano
		 * 
		 * y existe al menos un registro de deuda seleccionable
		 * 
		 * chkAll en deuda Administrativa
		 */
		if (liqDeudaAdapterVO.getListGestionDeudaAdmin() != null &&
				liqDeudaAdapterVO.getListGestionDeudaAdmin().size() > 0) {
				
			if (!userContext.getEsProcurador() && 
					!userContext.getEsOperadorJudicial() &&
					!userContext.getEsEscribano()){
				
				if (liqDeudaAdapterVO.getListGestionDeudaAdmin().get(0).poseeDeudaSeleccionable()){
					liqDeudaAdapterVO.setMostrarChkAllAdmin(true);
					liqDeudaAdapterVO.setSeleccionarDeuda4Cyq(true);
				}	
			}
		}
		
		if (!liqDeudaAdapterVO.isPoseeDeudaAdmin4Cyq() && !liqDeudaAdapterVO.isPoseeDeudaJudicial4Cyq()){
			liqDeudaAdapterVO.setSeleccionarCuenta4Cyq(true);
		}
		
		// Si surgio algun error o mensaje en la simulacion de saldo por caducidad
		if (cuenta.hasErrorOrMessage()){
			cuenta.passErrorMessages(liqDeudaAdapterVO);
		}
		
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				exit: " + funcName );
			log.debug( "##################################################################################################");		
		}
		return liqDeudaAdapterVO;
	}
	
	
	/**
	 * Metodo utilizada por muchas funciones, por defecto devuelve LiqCuentaVO con los titulares obtenidos de "general"
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public LiqCuentaVO getCuenta() throws Exception {
		return this.getCuenta(true);	
	}
	
	/**
	 *  Obtiene un LiqCuentaVO, cargado para la cuenta del helper.
	 */
	public LiqCuentaVO getCuenta(boolean withTitulares) throws Exception {
		if (withTitulares) {
			return this.getCuenta(ALL);
		} else {
			return this.getCuenta(OBJIMP | ATR_CONTR | CONVENIOS | DESYUNIF);
		}
	}
	

	/**
	 * Obtiene un LiqCuentaVO, cargado para la cuenta del helper.
	 * flag indica que parte/partes cargar en el VO:
	 * Por ej: 
	 *  getCuenta(OBJIMP | ATR_CONTR);
	 *  Recupera los datos de objeto imponible y atributos de contribuyente,
	 *  No trae los convenios, desgloces y titulares.
     *
	 *  getCuenta(ALL) 
	 *  Trae todos los datos.
	 *
	 * 	getCuenta(OBJIMP);
	 *  Solo carga los datos del objeto imponible.
	 */
	public LiqCuentaVO getCuenta(int flag) throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		DemodaTimer dt = new DemodaTimer();

		// 1:: Traspaso de los datos a mostrar de la cuenta
		LiqCuentaVO liqCuentaVO = new LiqCuentaVO();

		log.info(funcName + " 1:: Traspaso de los datos a mostrar de la cuenta"); 
		liqCuentaVO.setIdCuenta(cuenta.getId());
		
		// Seteamos el Id del Objeto Imponible para poder linkear y
		// el codigo del Tipo de Objeto Imponible.
		if(cuenta.getObjImp()!=null){
			liqCuentaVO.setIdObjImp(cuenta.getObjImp().getId());
			liqCuentaVO.setCodTipObjImp(cuenta.getObjImp().getTipObjImp().getCodTipObjImp());
		}
		
		if(cuenta.getRecurso().getEsAutoliquidable().intValue()==SiNo.SI.getId().intValue()){
			liqCuentaVO.setEsRecursoAutoliquidable(true);
			String nroIsib="";
			if (cuenta.obtenerCuentaTitularPrincipal()!=null)
				nroIsib=cuenta.obtenerCuentaTitularPrincipal().getContribuyente().getNroIsib();
			
			if(!StringUtil.isNullOrEmpty(nroIsib)){
				liqCuentaVO.setNroIsib(nroIsib);
				if(StringUtil.isNumeric(nroIsib) && nroIsib.length()>3 && NumberUtil.getLong(nroIsib.substring(0, 3))>900L){
					liqCuentaVO.setConvMultilateral("Si");
				}
					
			}
			
			// Para tipos de objeto imponible "Comercio", buscamos si posee cierre comercio.
			if (cuenta.getRecurso().getTipObjImp() != null &&
					cuenta.getRecurso().getTipObjImp().getId().equals(TipObjImp.COMERCIO)){
				
				CierreComercio cierreComercio = CierreComercio.getByObjImp(cuenta.getObjImp());
				
				if (cierreComercio!=null && !StringUtil.isNullOrEmpty(cierreComercio.getIdCaso())
						&& cuenta.getEstCue()!=null && (cuenta.getEstCue().getId().equals(EstCue.ID_BAJA_EN_TRAMITE) 
								|| cuenta.getEstCue().getId().equals(EstCue.ID_BAJA_CIERRE_DEFINITIVO))){
					CasoVO caso=CasServiceLocator.getCasCasoService().construirCasoVO(cierreComercio.getIdCaso());
					liqCuentaVO.setExpedienteCierre(" Expediente "+caso.getNumero());
				}
			}
		}
		
		ServicioBanco serBan = cuenta.getRecurso().obtenerServicioBanco();
		
		liqCuentaVO.setCodServicioBanco(serBan!=null?serBan.getCodServicioBanco():"");
		liqCuentaVO.setEsActiva(cuenta.getVigencia().equals(Vigencia.VIGENTE.getId()));
		liqCuentaVO.setNroCuenta(cuenta.getNumeroCuenta());
		liqCuentaVO.setCodGestionPersonal(cuenta.getCodGesCue());
		liqCuentaVO.setCodRecurso(cuenta.getRecurso().getCodRecurso());
		liqCuentaVO.setDesRecurso(cuenta.getRecurso().getDesRecurso());
		liqCuentaVO.setNombreTitularPrincipal(cuenta.getNombreTitularPrincipal());
		liqCuentaVO.setCuitTitularPrincipal(this.getCuitTitularPrincipal());
		liqCuentaVO.setDesDomEnv(cuenta.getDesDomEnv() != null ? cuenta.getDesDomEnv() : "");
		liqCuentaVO.setCuitTitularPrincipalContr(cuenta.getCuitTitularPrincipalContr());
		liqCuentaVO.setDesBroche(cuenta.getBroche()!= null ? cuenta.getBroche().getDesBroche():"");
		liqCuentaVO.setEsLitoralGas(cuenta.getEsLitoralGas());
		liqCuentaVO.setEsObraPeatonal(cuenta.getEsObraPeatonal());
		EstCue estCue = cuenta.getEstCue();
		if(null != estCue){
			liqCuentaVO.setEstado((EstCueVO) estCue.toVO(1,false));
		}else{
			liqCuentaVO.setEstado(new EstCueVO(-1,Estado.getById(cuenta.getEstado()).getValue()));
		}
		liqCuentaVO.setObservacion(cuenta.getObservacion());
		
		if (cuenta.getRecurso().getEsFiscalizable().intValue()==SiNo.SI.getId().intValue()){
			liqCuentaVO.setEsRecursoFiscalizable(true);
			List<OrdenControl> listOrdenControl= OrdenControl.getListOrdenControlByCuenta(cuenta);
			for (OrdenControl ordenControl:listOrdenControl){
				OrdenControlVO ordConVO=(OrdenControlVO) ordenControl.toVO(1,false);
				if(!StringUtil.isNullOrEmpty(ordenControl.getIdCaso()))
					ordConVO.setCaso(CasServiceLocator.getCasCasoService().construirCasoVO(ordenControl.getIdCaso()));
				liqCuentaVO.getListOrdenControl().add(ordConVO);
			}
			
			
			//Se pidio que no se verifique que tenga acta de inicio
			/*for (Long idOrdenControl : listIdOrdenControl){
				OrdenControl ordenControl = OrdenControl.getById(idOrdenControl);
				Acta acta=Acta.getByOrdenControlTipoActa(ordenControl.getId(), TipoActa.ID_TIPO_INICIO_PROCEDIMIENTO);
				if (acta !=null && acta.getFechaVisita()!= null && acta.getIdPersona()!=null && DateUtil.isDateBeforeOrEqual(acta.getFechaVisita(), new Date())){
					liqCuentaVO.setDesFiscalizacion(ordenControl.getTipoOrden().getDesTipoOrden()+ " Nro: "+ordenControl.getNumeroOrden()+"/"+
							ordenControl.getAnioOrden()+" en Proceso");
					break;
				}
			}*/
		}
		
		log.info(funcName + "LiqDeuda 1:: NumeroCuenta=" + liqCuentaVO.getNroCuenta() + " CodGesPer=" + liqCuentaVO.getCodGestionPersonal() +
				   " DesRecurso="+ liqCuentaVO.getDesRecurso());
		log.info(dt.stop("LiqDeuda 1:: Traspaso de los datos a mostrar de la cuenta"));

		// Atributos del Objeto Imponible	
		if ((flag & OBJIMP) != 0) { 
			log.debug(funcName + " 2:: Recupero los atributos del objeto imponible marcados como visible en consulta de deuda");
			liqCuentaVO.setListAtributoObjImp(this.getListAtrObjImp(true));
			log.info(dt.stop("LiqDeuda 2:: Atributos del objeto imponible"));
		}
		
		// Atributos de la Cuenta
		if ((flag & ATR_CUE_4_WEB) != 0 || (flag & ATR_CUE_4_RECIBO) != 0) { 
			log.debug(funcName + " 2.1:: Recupero los atributos de la Cuenta marcados como visible en consulta de deuda o en el recibo");
			liqCuentaVO.setListAtributoCuenta(this.getListAtrCue(flag));
			log.info(dt.stop("LiqDeuda 2.1:: Atributos de la Cuenta"));
		}
		
		// 3:: Recuperacion de Titulares de la cuenta vigentes a la fecha de hoy
		if ((flag & TITULARES) != 0) { 
			log.info("LiqDeuda 3:: Recuperacion de Titulares de la cuenta");
			Persona persona;
			
			// Si el recurso esta configurado como "deuda sigue titular". 
			if (cuentaTitular != null){
				LiqTitularVO liqTitularVO = new LiqTitularVO();
				
				persona = Persona.getByIdLight(cuentaTitular.getIdContribuyente());
				
				if (persona != null) {
					liqTitularVO.setIdTitular(persona.getId());
					liqTitularVO.setDesTitular(this.getDesPersonaTitular(persona));
					liqTitularVO.setDesTitularContr(this.getDesPersonaTitularContr(persona));
					liqTitularVO.setExistePersona(true);
				} else {
					liqTitularVO.setExistePersona(false);
					liqTitularVO.setDesTitular("No se encuentra el titular");
				}
				
				log.debug(funcName + " 3:: desPersona=" + liqTitularVO.getDesTitular() + " idPer=" + liqTitularVO.getIdTitular());  
				liqCuentaVO.getListTitular().add(liqTitularVO);	
				
			} else {
				
				List<Persona> listTitulares = cuenta.getListTitularesCuentaLight(new Date());
				
				// Segun bug# 803 - si la cuenta tiene fechaBaja y no tiene titulares vigentes, 
				// recuperamos los que tengas fechahasta = fechaBaja de la cuenta.
				if (listTitulares.isEmpty() && cuenta.getFechaBaja() != null){
					List<CuentaTitular> listCuentaTitular = cuenta.getListCuentaTitular();
			    	
					cuentaPoseeTitularesVigentes = false;
					
			    	for(CuentaTitular ct:listCuentaTitular) {
			    		if (DateUtil.isDateEqual(ct.getFechaHasta(), cuenta.getFechaBaja())){
			    			Persona titular = new Persona();
			    			titular.setId(ct.getIdContribuyente());
			    			listTitulares.add(titular);
			    		}
			    	}
				}
								
				for(Persona titular: listTitulares){
					LiqTitularVO liqTitularVO = new LiqTitularVO();
					
					persona = Persona.getByIdLight(titular.getId());
					if (persona != null) {
						liqTitularVO.setIdTitular(persona.getId());
						liqTitularVO.setDesTitular(this.getDesPersonaTitular(persona));
						liqTitularVO.setDesTitularContr(this.getDesPersonaTitularContr(persona));
						liqTitularVO.setExistePersona(true);
					} else {
						liqTitularVO.setExistePersona(false);
						liqTitularVO.setDesTitular("No se encuentra el titular");
					}
					
					log.debug(funcName + " 3:: desPersona=" + liqTitularVO.getDesTitular() + " idPer=" + liqTitularVO.getIdTitular());  
					liqCuentaVO.getListTitular().add(liqTitularVO);				
				}
			}
			
			log.info(dt.stop("LiqDeuda 3:: Recuperacion de Titulares de la cuenta"));
		}
		
		// 4:: Atributos de Contribuyente
		if ((flag & ATR_CONTR) != 0) { 
			log.info(funcName + " 4:: Atributos de Contribuyente");
			ContribuyenteDefinition  contribDef = cuenta.getUnionConAtrVal(true);
			
			// Si UnionConAtrVal llega nulo es porque no posee titulares activos vigentes 
			if (contribDef != null) {
				log.debug(funcName + " 4:: cuenta.getUnionConAtrVal(): " + contribDef.getListConAtrDefinition().size() + " Atributos recuperados");
				for(ConAtrDefinition caf: contribDef.getListConAtrDefinition()) {
					
					log.debug(funcName + " 4:: 		atr: " + caf.getAtributo().getDesAtributo() +
							  " valor: " + caf.getValorView()); 
					
					// Solo se agrega a la lista si el valor es "Si"
					if (!caf.getValorView().equals("No")){
						LiqAtrValorVO liqAtrValorVO = new LiqAtrValorVO();
						
						liqAtrValorVO.setKey(caf.getAtributo().getCodAtributo());
						liqAtrValorVO.setLabel(caf.getAtributo().getDesAtributo());
						
						liqAtrValorVO.setValue(caf.getValorView());
						
						log.debug(funcName + " 4:: label=" + liqAtrValorVO.getLabel() + " value=" + liqAtrValorVO.getValue());
						
						liqCuentaVO.getListAtributoContr().add(liqAtrValorVO);
					}
				}	
			}
			
			log.info(dt.stop("LiqDeuda  4:: Atributos de Contribuyente"));
		}
		
		// 5:: Convenios Asociados
		if ((flag & CONVENIOS) != 0) { 
			List<Convenio> listConveniosAsociados = cuenta.getListConveniosVigentes();
			log.info(funcName + " 5:: Convenios Asociados : " + listConveniosAsociados.size() + " Convenios encontrados");
			for (Convenio conv:listConveniosAsociados){
				LiqConvenioVO liqConvenioVO = new LiqConvenioVO();
				
				liqConvenioVO.setNroConvenio(conv.getNroConvenio().toString());
				liqConvenioVO.setDesPlan(conv.getPlan().getDesPlan());
				liqConvenioVO.setDesViaDeuda(conv.getViaDeuda().getDesViaDeuda());
				liqConvenioVO.setIdConvenio(conv.getId());
				
				// Se agregaron para el informe de estado de cuenta
				if (esParaEstadoCuenta){
					liqConvenioVO.setDesEstadoConvenio(conv.getDescEstadoConvenio());
					liqConvenioVO.setCanCuotasPlan(String.valueOf(conv.getCantidadCuotasPlan()));
					liqConvenioVO.setTotImporteConvenio(conv.getTotImporteConvenio());
				}
				
				liqCuentaVO.getListConvenio().add(liqConvenioVO);
			}
			log.info(dt.stop("LiqDeuda  5:: Convenios Asociados : "));
			
			List<Convenio> listConveniosRecompuestos = cuenta.getListConveniosRecompuestos();
			log.info(funcName + " 5:: Convenios Recompuestos : " + listConveniosAsociados.size() + " Convenios encontrados");
			int c=1;
			for (Convenio conv:listConveniosRecompuestos){
				// Solo mostramos los tres ultimos, no llegan ordenados por fechaFor.
				if(c>3)
					break;	
				
				LiqConvenioVO liqConvenioVO = new LiqConvenioVO();
				
				liqConvenioVO.setNroConvenio(conv.getNroConvenio().toString());
				liqConvenioVO.setDesPlan(conv.getPlan().getDesPlan());
				liqConvenioVO.setDesViaDeuda(conv.getViaDeuda().getDesViaDeuda());
				liqConvenioVO.setIdConvenio(conv.getId());
				
				// Se agregaron para el informe de estado de cuenta
				if (esParaEstadoCuenta){
					liqConvenioVO.setDesEstadoConvenio(conv.getDescEstadoConvenio());
					liqConvenioVO.setCanCuotasPlan(String.valueOf(conv.getCantidadCuotasPlan()));
					liqConvenioVO.setTotImporteConvenio(conv.getTotImporteConvenio());
				}
				
				liqCuentaVO.getListConvenioRecompuesto().add(liqConvenioVO);
				c++;
			}
			
			log.info(dt.stop("LiqDeuda  5.1:: Convenios Recompuestos : "));
		}

		// 6:: Desgloses y unificaciones
		if ((flag & DESYUNIF) != 0) { 
			List<Cuenta> listCuentaUnifDes = cuenta.getListCuentasMadreHija();
			log.info(funcName + " 6:: Desgloses y unificaciones : " + listCuentaUnifDes.size() + " Cuentas encontradas");
			for(Cuenta cueMH:listCuentaUnifDes){
				LiqCuentaVO liqCuentaUnifDes = new LiqCuentaVO(); 
				
				liqCuentaUnifDes.setIdCuenta(cueMH.getId());
				liqCuentaUnifDes.setNroCuenta(cueMH.getNumeroCuenta());
				liqCuentaUnifDes.setDesRecurso(cueMH.getRecurso().getDesRecurso());
				
				log.debug(funcName + " 6:: idCuenta=" + liqCuentaUnifDes.getIdCuenta() + " NroCuenta=" + liqCuentaUnifDes.getNroCuenta()); 
				liqCuentaVO.getListCuentaUnifDes().add(liqCuentaUnifDes);	
			}
			log.info(dt.stop("LiqDeuda 6:: Desgloses y unificaciones"));
		}

		return liqCuentaVO;
	}

	/**
	 * Obtiene los atributos del objeto imponible de la cuenta, si es que la misma posee. 
	 * 
	 * @param forWeb
	 * @return
	 * @throws Exception
	 */
	public List<LiqAtrValorVO> getListAtrObjImp(boolean forWeb) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		
		String ubicacion="";
		String ubicacionFinca="";
		String ubicacionBaldio ="";
		String tipoParcela="";
		TipObjImpDefinition tipObjImpDef;
		List<LiqAtrValorVO> listAtrObjImp = new ArrayList<LiqAtrValorVO>();
 		
		if(cuenta.getObjImp()!=null){
			tipObjImpDef = cuenta.getObjImp().getDefinitionValue();
			boolean agregarLiqAtrValorVO = true;
			for (TipObjImpAtrDefinition tipObjImpAtrDef: tipObjImpDef.getListTipObjImpAtrDefinition()){
										
				LiqAtrValorVO liqAtrValorVO = new LiqAtrValorVO();
				liqAtrValorVO.setKey(tipObjImpAtrDef.getAtributo().getCodAtributo());
				liqAtrValorVO.setLabel(tipObjImpAtrDef.getAtributo().getDesAtributo());
							
				// Para atributos multivalor
				if(tipObjImpAtrDef.getEsMultivalor()){
					
					// Si el atributo no está valorizado, queda la bandera "EsMultivalor = false"
					// Se agrega el label sin value.
					
					// Si el atributo tiene al menos un valor
					if (tipObjImpAtrDef.getListValor().size()>0){
						liqAtrValorVO.setEsMultiValor(true);
						
						List<String> mvs = tipObjImpAtrDef.getMultiValorView(this.fechaActualizacion);
						liqAtrValorVO.setMultiValue(new String[mvs.size()]);
						
						int c = 0;
						for (String val:mvs){
							liqAtrValorVO.getMultiValue()[c] = val;
							log.debug(funcName + " 2:: multi val:" + val);
							c++;
						}
					}
					
				// Para atributos NO multivalor	
				} else {
					if(tipObjImpAtrDef.getPoseeDominio())
						liqAtrValorVO.setValue(tipObjImpAtrDef.getValorFromDominioView());
					else				
						liqAtrValorVO.setValue(tipObjImpAtrDef.getValorView());
				}
	
				log.debug(funcName + " 2:: key:"+liqAtrValorVO.getKey()+"    label=" + liqAtrValorVO.getLabel() + " value=" + liqAtrValorVO.getValue());			
				
				// Guarda los datos necesarios para armar el domicilio
				// Para "Comercio" se utiliza  "DomicilioFinca"
				// Para el resto, segun el "TipoParcela"
				if(liqAtrValorVO.getKey().trim().equals("DomicilioFinca")){
					// guarda el dato del domicilio finca, para agregarlo al atributo "ubicacion" 
					ubicacionFinca=liqAtrValorVO.getValue();				
				}else if(liqAtrValorVO.getKey().trim().equals("UbiTerreno")){
					// guarda el dato del domicilio terreno, para agregarlo al atributo "ubicacion"
					ubicacionBaldio=liqAtrValorVO.getValue();				
				}else if(liqAtrValorVO.getKey().trim().equals("TipoParcela"))
					// guarda el dato del tipoParcela, para calcular el domicilio
					tipoParcela=tipObjImpAtrDef.getValorString();		
	
				// 13-10-2009: Fix Bug 906.
				// Si el atributo es Nro. IIBB, lo reemplazamos por el valor
				// que esta seteado en el contribuyente.
				if (liqAtrValorVO.getKey().trim().equals("NroConvenioMult")) {
					String nroIsib = "";
					Contribuyente contribuyente = cuenta.obtenerCuentaTitularPrincipal().getContribuyente();
					if (contribuyente != null) nroIsib = contribuyente.getNroIsib();
					liqAtrValorVO.setValue(nroIsib);
				}
												
				// Evalua si hay que agregarlo o no, dependiendo del valor pasado como parametro 
				if(forWeb){
					if(tipObjImpAtrDef.getTipObjImpAtr().getEsVisConDeu().getId().equals(SiNo.SI.getId())){
						agregarLiqAtrValorVO = true;
					}else
						agregarLiqAtrValorVO = false;
				}else{
					agregarLiqAtrValorVO = true;
				}
	
				if(agregarLiqAtrValorVO){
					listAtrObjImp.add(liqAtrValorVO);
				}
				
			}
			
			// Calcula el domicilio dependiendo del recurso del que se trate.
			// Para los que tienen a "Comercio" como tipo objeto imponible  
			if (cuenta.getObjImp().getTipObjImp().getId().equals(TipObjImp.COMERCIO) ){
				ubicacion = cuenta.getDesDomEnv();
				
			} else {
			// Para el resto de los recursos
				if(tipoParcela.trim().equals("1")){
					ubicacion = ubicacionFinca;
					
				}else if(tipoParcela.trim().equals("2")){
					ubicacion = ubicacionBaldio;
				}
			}
			
			// Agrega un atributo que contiene la ubicacion (de finca o baldio) ya calculada
			LiqAtrValorVO liqAtrValorVO = new LiqAtrValorVO();		
			liqAtrValorVO.setKey("Ubicacion");
			liqAtrValorVO.setLabel("Ubicaci\u00F3n");
			liqAtrValorVO.setValue(ubicacion);
			
			listAtrObjImp.add(liqAtrValorVO);
			log.debug("funcName :: Valor del atributo Ubicacion:"+ubicacion);
			
		}
		
		return listAtrObjImp;
	}
	
	/**
	 * Deluelve una lista de LiqAtrValorVO con los atributos de la cuenta marcado como esVisConDeu = true.
	 * 
	 * @param forWeb
	 * @return
	 * @throws Exception
	 */
	public List<LiqAtrValorVO> getListAtrCue(int flag) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		
		List<LiqAtrValorVO> listAtrCue = new ArrayList<LiqAtrValorVO>();
 		
		RecursoDefinition atributoCuentaDefinition = cuenta.getRecCueAtrValDefinitionValue(new Date());
		
		if(atributoCuentaDefinition!=null){
			
			boolean agregarLiqAtrValorVO = true;
			
			for (RecAtrCueDefinition recAtrCueDefinition: atributoCuentaDefinition.getListRecAtrCueDefinition()){
				
				LiqAtrValorVO liqAtrValorVO = new LiqAtrValorVO();
				liqAtrValorVO.setKey(recAtrCueDefinition.getRecAtrCue().getAtributo().getCodAtributo());
				liqAtrValorVO.setLabel(recAtrCueDefinition.getRecAtrCue().getAtributo().getDesAtributo());
				
				if(recAtrCueDefinition.getPoseeDominio())
					liqAtrValorVO.setValue(recAtrCueDefinition.getValorFromDominioView());
				else				
					liqAtrValorVO.setValue(recAtrCueDefinition.getValorView());
	
				log.debug(funcName + " 2:: key:"+liqAtrValorVO.getKey()+"    label=" + liqAtrValorVO.getLabel() + " value=" + liqAtrValorVO.getValue());			
				
				// Evalua si hay que agregarlo o no, dependiendo del valor pasado como parametro 
				if ((flag & ATR_CUE_4_WEB) != 0) {
					if (!recAtrCueDefinition.getEsVisConDeu()) {
						agregarLiqAtrValorVO = false;
					} else
						agregarLiqAtrValorVO = true;
				} else if ((flag & ATR_CUE_4_RECIBO) != 0) {
					if (!recAtrCueDefinition.getEsVisRec()) {
						agregarLiqAtrValorVO = false;
					} else
						agregarLiqAtrValorVO = true;
				} else {
					agregarLiqAtrValorVO = true;
				}
	
				if(agregarLiqAtrValorVO){
					listAtrCue.add(liqAtrValorVO);
				}
				
			}
		}
		
		return listAtrCue;
	}
	
	
	/**
	 *  Obtiene las LiqCuentasVO relacionadas con la Cuenta del helper
	 */
	public List<LiqCuentaVO> getCuentasRelacionadas() throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		DemodaTimer dt = new DemodaTimer();

		List<LiqCuentaVO> listCuentasRel = new ArrayList<LiqCuentaVO>();

		// 7:: Cuentas Relacionadas
		List<Cuenta> listCuentasRelacionadas = new ArrayList<Cuenta>();
		
		if(cuenta.getObjImp()!=null)
			listCuentasRelacionadas = cuenta.getListCuentaRelacionadas();
		else
			log.debug(funcName + " 7:: Cuentas Relacionadas : La cuenta no tiene un ObjImp");	

		log.debug(funcName + " 7:: Cuentas Relacionadas : " + listCuentasRelacionadas.size() + " Cuentas encontradas");

		if (listCuentasRelacionadas.size() > 0){
			for(Cuenta cueRel:listCuentasRelacionadas){
				LiqCuentaVO liqCuentaRel = new LiqCuentaVO();

				liqCuentaRel.setIdCuenta(cueRel.getId());
				liqCuentaRel.setNroCuenta(cueRel.getNumeroCuenta());
				liqCuentaRel.setDesCategoria(cueRel.getRecurso().getCategoria().getDesCategoria());
				liqCuentaRel.setDesRecurso(cueRel.getRecurso().getDesRecurso());

				log.debug(funcName + " 7:: idCuenta=" + liqCuentaRel.getIdCuenta() + 
						" NroCuenta=" + liqCuentaRel.getNroCuenta() + 
						" DesCategoria=" + liqCuentaRel.getDesCategoria() +
						" DesRecurso=" + liqCuentaRel.getDesRecurso());
				listCuentasRel.add(liqCuentaRel);
			}
		}

		log.info(dt.stop("LiqDeuda 7:: Cuentas Relacionadas"));

		return listCuentasRel;

	}

	/**
	 * Obtiene la lista de las tres excenciones/solicitidudes que se muestran 
	 */
	public LiqExencionesVO getExenciones() throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		DemodaTimer dt = new DemodaTimer();

		LiqExencionesVO liqExencionesVO = new LiqExencionesVO();

		// 8:: Exenciones Vigentes - Caso si posee
		List<CueExe> listExencionesVigentes = cuenta.getListCueExeVigente();
		log.debug(funcName + " 8:: Exenciones Vigentes : " + listExencionesVigentes.size() + " Exenciones encontradas");

		if (listExencionesVigentes.size() > 0){
			for (CueExe cueExe:listExencionesVigentes){					
				LiqExencionVO liqExencionVO = new LiqExencionVO();

				liqExencionVO.setIdCueExe(cueExe.getId());
				liqExencionVO.setDesExencion(cueExe.getExencion().getDesExencion());
				liqExencionVO.setFechaDesde(DateUtil.formatDate(cueExe.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK));
				liqExencionVO.setFechaHasta(DateUtil.formatDate(cueExe.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK));
				
				if (!StringUtil.isNullOrEmpty(cueExe.getIdCaso())){
					liqExencionVO.setIdCaso(cueExe.getIdCaso());
					liqExencionVO.setPoseeCaso(true);
				}	
				
				log.debug(funcName + " 8:: Exenciones Vigentes : DesExencion=" + liqExencionVO.getDesExencion() +
						" fechaDesde=" + liqExencionVO.getFechaDesde() +
						" fechaHasta=" + liqExencionVO.getFechaHasta());
				liqExencionesVO.getListExeVigentes().add(liqExencionVO);
			}
		}
		log.info(dt.stop("LiqDeuda  8:: Exenciones Vigentes"));


		log.info(dt.stop("LiqDeuda  9:: Exenciones Denegadas (No se calculan mas)"));

		// 10:: Solicitudes de Exencion en tramite - Caso si posee
		/*
		 * XXX cambiarlo por la lista de cueexe segun doc.
		*/
		List<CueExe> listExencionesEnTramite = cuenta.getListCueExeEnTramite();
		log.debug(funcName + " 10:: Exenciones En Tramites : " + listExencionesVigentes.size() + " Exenciones encontradas");
		if (listExencionesEnTramite.size() > 0){
			for (CueExe cueExe:listExencionesEnTramite){					
				LiqExencionVO liqExencionVO = new LiqExencionVO();

				liqExencionVO.setIdCueExe(cueExe.getId());
				liqExencionVO.setDesExencion(cueExe.getExencion().getDesExencion());

				liqExencionVO.setFechaSolicitud(DateUtil.formatDate(cueExe.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK));
				liqExencionVO.setFechaDesde(DateUtil.formatDate(cueExe.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK));
				liqExencionVO.setFechaHasta(DateUtil.formatDate(cueExe.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK));
				
				// TODO Preguntar si esto esto es correcto				
				if (cueExe.getIdCaso() != null){
					liqExencionVO.setIdCaso(cueExe.getIdCaso());
					liqExencionVO.setPoseeCaso(true);
				}	
				
				log.debug(funcName + " 10:: Exenciones En Tramite : DesExencion=" + liqExencionVO.getDesExencion() +
						" fechaDesde=" + liqExencionVO.getFechaDesde() +
						" fechaHasta=" + liqExencionVO.getFechaHasta());
				liqExencionesVO.getListExeTramite().add(liqExencionVO);
			}
		}
		log.info(dt.stop("LiqDeuda  10:: Exenciones En Tramites"));

		return liqExencionesVO;

	}

	/**
	 * Obtiene la lista de bloques de Deuda en Concurso y Quiebra.
	 * Cada bloque corresponde a un procedimiento de CyQ. 
	 */
	public List<LiqDeudaCyQVO> getDeudaCyQ() throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		DemodaTimer dt = new DemodaTimer();

		List<LiqDeudaCyQVO> listProcedimientoCyQ = new ArrayList<LiqDeudaCyQVO>();

		List<Deuda> listDeudaCyQ = cuenta.getListDeudaCyQ();
		log.debug(funcName + " 11:: Deuda en Vias CyQ : " + listDeudaCyQ.size() + " Registros de deuda encontradas");

		//La deuda llega ordenada por ProcedimientoCyQ, anio y periodo
		// Si existe procedimiento de CyQ
		if (listDeudaCyQ.size() > 0){

			Deuda deudaAnt = listDeudaCyQ.get(0);
			Integer NroProcedimiento = deudaAnt.getProcedimientoCyQ().getNumero();
			
			boolean esTFC = deudaAnt.getProcedimientoCyQ().esTFC();
			
			LiqDeudaCyQVO procedimientoCyQVO = new LiqDeudaCyQVO(); 

			Iterator it = listDeudaCyQ.iterator();
			while (it.hasNext()){

				Deuda deuda = (Deuda)it.next();
				double actualizCyQ;
				double deudaTotal;
				
				if (esTFC){
					DeudaAct deudaAct = deuda.actualizacionSaldo(fechaActualizacion);
					actualizCyQ = deudaAct.getRecargo();
					deudaTotal = deudaAct.getImporteAct();
				} else {
					actualizCyQ = deuda.getActualizacionCyQ() == null ? 0 : deuda.getActualizacionCyQ().doubleValue();									
					deudaTotal = deuda.getSaldo().doubleValue() + actualizCyQ;
				}

				// Por cada registro de deuda recuperado
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 

				// Paso los datos de cada registro de deuda al LiqDeudaVO
				liqDeudaVO.setIdDeuda(deuda.getId());
				liqDeudaVO.setIdViaDeuda(deuda.getViaDeuda().getId());
				liqDeudaVO.setIdEstadoDeuda(deuda.getEstadoDeuda().getId());
				liqDeudaVO.setDesViaDeuda(deuda.getViaDeuda().getDesViaDeuda());
				liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
				liqDeudaVO.setCodRefPag(deuda.getCodRefPag().toString());
				liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
				liqDeudaVO.setFechaPago(DateUtil.formatDate(deuda.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
				liqDeudaVO.setImporte(deuda.getImporte());
				liqDeudaVO.setSaldo(deuda.getSaldo());
				liqDeudaVO.setActualizacion(actualizCyQ);
				Double actPaga = (deuda.getActualizacion()!=null)?deuda.getActualizacion().doubleValue():0D;
				liqDeudaVO.setImportePago((deuda.getSaldo().doubleValue()<= deuda.getImporte().doubleValue())?(deuda.getImporte()-deuda.getSaldo()+actPaga):0D);
				liqDeudaVO.setTotal(deudaTotal);
				
				if (deuda.getEsConvenio()){
					liqDeudaVO.setPoseeConvenio(true);
					
					String obsConvenio = deuda.getConvenio().getNroConvenio() + " - " + deuda.getConvenio().getPlan().getDesPlan();
					liqDeudaVO.setObservacion(obsConvenio);
					liqDeudaVO.setIdLink(deuda.getIdConvenio());
					liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setEsSeleccionable(false);
				} else {
					liqDeudaVO.setPoseeObservacion(false);
					liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(GdeSecurityConstants.MTD_SELECT_DEUDA, null, deuda.getProcedimientoCyQ().getId()));
				}

				// Seteo de permisos
				liqDeudaVO.setVerDetalleDeudaEnabled(this.getPermisoRegDeuda(GdeSecurityConstants.MTD_VER_DETALLE_DEUDA, null, deuda.getProcedimientoCyQ().getId()));

				log.debug(funcName + " 11:: DeudaCyQ: " +
						" periodo=" + liqDeudaVO.getPeriodoDeuda() +
						" fVto=" + liqDeudaVO.getFechaVto() +
						" saldo=" + liqDeudaVO.getSaldo() +
						" actualiz=" + liqDeudaVO.getActualizacion() +
						" total=" + liqDeudaVO.getTotal() +
						" obs.=" + liqDeudaVO.getObservacion() +
						" esTFC=" + esTFC);

				log.debug(funcName + " 11::  ###         NroProc=" + NroProcedimiento + 
						"  deuda.nroProc=" + deuda.getProcedimientoCyQ().getNumero() + " id=" + deuda.getProcedimientoCyQ().getId());	
				// Solo agrego a la lista si estoy dentro del mismo procedimientus
				if (NroProcedimiento.equals(deuda.getProcedimientoCyQ().getNumero())){

					// Bug# 678:
					// Aplicacion de logica deuda sigue titular.
					// Si tenemos seteado idContribuyente (por acceder desde "Deuda por Contribuyente") y
					// La bandera DeudaSigueTitular = "true"
					// Obtuvimos el cuentaTitular y solo permitimos seleccionar deuda que se encuentre liqDeudaVO.setReclamarAcentEnabled(this.getPermisoRegDeuda(GdeSecurityConstants.MTD_RECLAMAR_ACENT, idProcurador, null));
					// Dentro del rango fechaDesde/Hasta
					if (cuentaTitular != null){
						if (DateUtil.isDateInRange(deuda.getFechaVencimiento(), cuentaTitular.getFechaDesde(), cuentaTitular.getFechaHasta())){
							procedimientoCyQVO.getListDeuda().add(liqDeudaVO);
						}					
					} else {
						procedimientoCyQVO.getListDeuda().add(liqDeudaVO);	
					}
				}		

				// Corte de control por numero de procedimiento
				if (!NroProcedimiento.equals(deuda.getProcedimientoCyQ().getNumero()) ){  // || !it.hasNext()){  

					procedimientoCyQVO.setIdProcedimiento(deudaAnt.getProcedimientoCyQ().getId());	
					// Seteo numero Procedimiento
					procedimientoCyQVO.setNroProcedimiento(NroProcedimiento.toString() + "/" + 
							deudaAnt.getProcedimientoCyQ().getAnio());

					// Seteo fecha Actualizacion
					// Si es TFC utilizamos la fecha actual
					if (deudaAnt.getProcedimientoCyQ().getFechaAuto() != null){
						procedimientoCyQVO.setFechaActualizacion(					
								DateUtil.formatDate(deudaAnt.getProcedimientoCyQ().getFechaAuto(), DateUtil.ddSMMSYYYY_MASK));
					} else {
						procedimientoCyQVO.setFechaActualizacion(					
								DateUtil.formatDate(fechaActualizacion, DateUtil.ddSMMSYYYY_MASK));
					}

					log.debug(funcName + " 11:: 	Procedimiento : NroProc=" + procedimientoCyQVO.getNroProcedimiento() +
							" FechaActualiz=" + procedimientoCyQVO.getFechaActualizacion() +
							" Subtotal=" + procedimientoCyQVO.getSubTotal());
					// Lista con un unico elemento.
					listProcedimientoCyQ.add(procedimientoCyQVO);

					// reset de valores
					NroProcedimiento = deuda.getProcedimientoCyQ().getNumero();
					procedimientoCyQVO = new LiqDeudaCyQVO();

					deudaAnt = deuda;
					
					esTFC = deudaAnt.getProcedimientoCyQ().esTFC();
					
					// Bug# 678:
					// Aplicacion de logica deuda sigue titular.
					// Si tenemos seteado idContribuyente (por acceder desde "Deuda por Contribuyente") y
					// La bandera DeudaSigueTitular = "true"
					// Obtuvimos el cuentaTitular y solo permitimos seleccionar deuda que se encuentre 
					// Dentro del rango fechaDesde/Hasta
					if (cuentaTitular != null){
						if (DateUtil.isDateInRange(deuda.getFechaVencimiento(), cuentaTitular.getFechaDesde(), cuentaTitular.getFechaHasta())){
							procedimientoCyQVO.getListDeuda().add(liqDeudaVO);
						}					
					} else {
						procedimientoCyQVO.getListDeuda().add(liqDeudaVO);	
					}

				}
				
				// Si es el ultimo registro de deuda
				if (!it.hasNext()){
					
					procedimientoCyQVO.setIdProcedimiento(deudaAnt.getProcedimientoCyQ().getId());
					
					// Seteo numero Procedimiento
					procedimientoCyQVO.setNroProcedimiento(NroProcedimiento.toString() + "/" + 
							deudaAnt.getProcedimientoCyQ().getAnio());
					// Seteo fecha Actualizacion
					// Si es TFC utilizamos la fecha actual
					if (deudaAnt.getProcedimientoCyQ().getFechaAuto() != null){
						procedimientoCyQVO.setFechaActualizacion(					
								DateUtil.formatDate(deudaAnt.getProcedimientoCyQ().getFechaAuto(), DateUtil.ddSMMSYYYY_MASK));
					} else {
						procedimientoCyQVO.setFechaActualizacion(					
								DateUtil.formatDate(fechaActualizacion, DateUtil.ddSMMSYYYY_MASK));
					}

					log.debug(funcName + " 11:: 	Procedimiento : NroProc=" + procedimientoCyQVO.getNroProcedimiento() +
							" FechaActualiz=" + procedimientoCyQVO.getFechaActualizacion() +
							" Subtotal=" + procedimientoCyQVO.getSubTotal());
					// Lista con un unico elemento.
					listProcedimientoCyQ.add(procedimientoCyQVO);
					
				}
			}				
		}

		log.info(dt.stop("LiqDeuda 11:: Deuda en Vias CyQ"));

		return listProcedimientoCyQ;
	}

	/**
	 * Obtiene la lista de bloque de deuda en procuradores. (mal llamada Via Judicial)
	 * Cada bloque corresponde a la deuda en un cada procurador.
	 */
	public List<LiqDeudaProcuradorVO> getDeudaProcurador() throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		DemodaTimer dt = new DemodaTimer();

		List<LiqDeudaProcuradorVO> listDeudaProcurador = new ArrayList<LiqDeudaProcuradorVO>();
		
		Convenio convenioForSimulacion = null;
		boolean errorEnSimulacion = false;
		
		List<DeudaJudicial> listDeudaJudicial = cuenta.getListDeudaJudicial();
		log.debug(funcName + " 12:: Deuda en Vias Judicial : " + listDeudaJudicial.size() + " Registros de deuda encontradas");

		//La deuda llega ordenada por Procurador, anio y periodo
		// Si existe deuda judicial
		if (listDeudaJudicial.size() > 0){

			Deuda deudaAnt = listDeudaJudicial.get(0);
			Long idProcurador = deudaAnt.getProcurador().getId();
			LiqDeudaProcuradorVO liqDeudaProcuradorVO = new LiqDeudaProcuradorVO(); 
			
			int i = 0;
			
			while (i < listDeudaJudicial.size()){

				Deuda deuda = listDeudaJudicial.get(i);

				// Por cada registro de deuda recuperado
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO();
				
				/* 
				 * 	En bug 404 se solicita quitar esta validacion 
				 * 
				 * Si el usuario es Procurador y la deuda esta en constancia no habilitada, no la agrego
				 *	if ( (userContext.getEsProcurador() &&
				 *		deuda.existeEnConstanciaHabilitada()) || 
				 *		!userContext.getEsProcurador()) {	
				 */
					
					
					DeudaAct deudaAct = new DeudaAct();
	
					// Paso los datos de cada registro de deuda al LiqDeudaVO
					liqDeudaVO.setIdDeuda(deuda.getId());
					liqDeudaVO.setIdViaDeuda(deuda.getViaDeuda().getId());
					liqDeudaVO.setIdEstadoDeuda(deuda.getEstadoDeuda().getId());
					liqDeudaVO.setDesViaDeuda(deuda.getViaDeuda().getDesViaDeuda());
					liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
					liqDeudaVO.setCodRefPag(deuda.getCodRefPag().toString());
					liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
					liqDeudaVO.setFechaPago(DateUtil.formatDate(deuda.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
					
					String[] arrPlanilla = deuda.obtenerPlanilla(); 
					if (arrPlanilla != null) {
						liqDeudaVO.setIdPlanilla(new Long(arrPlanilla[0]));
						liqDeudaVO.setPerAnioPlanilla(arrPlanilla[1] + "/ " + arrPlanilla[2]);
					}
					
					if (deuda.getEsConvenio()){
						/*
						 *  Codigo anterior a la simulacion de saldo por caducidad
						 *  
						 * liqDeudaVO.setPoseeObservacion(true);
						liqDeudaVO.setEsSeleccionable(false);
						liqDeudaVO.setPoseeConvenio(true);
						String obsConvenio = deuda.getConvenio().getNroConvenio() + " - " + deuda.getConvenio().getPlan().getDesPlan();
						liqDeudaVO.setObservacion(obsConvenio);
						liqDeudaVO.setIdLink(deuda.getIdConvenio());*/
						
						// Valores comunes
						liqDeudaVO.setPoseeConvenio(true);
						
						String obsConvenio = deuda.getConvenio().getNroConvenio() + " - " + deuda.getConvenio().getPlan().getDesPlan();
						liqDeudaVO.setObservacion(obsConvenio);
						liqDeudaVO.setIdLink(deuda.getIdConvenio());
						
						// Valores particulares					
						if (simularSaldoPorCaducidad){
							
							// Corte de control por convenio.
							if (convenioForSimulacion == null || convenioForSimulacion.getId().longValue() != deuda.getIdConvenio().longValue())
								convenioForSimulacion = deuda.getConvenio();
							
							Deuda deudaSimSalPorCad = convenioForSimulacion.obtenerDeudaSimulaSalPorCad(deuda);
							
							// Si la simulacion de saldo por caducidad arroja error o mensaje, lo pasamos a la cuenta.
							// NOTA: en la simulacion se calcula para el primer registro de deuda y ya sabemos si todo el convenio tiene error
							if (!deudaSimSalPorCad.hasError()){
								
								liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(GdeSecurityConstants.MTD_SELECT_DEUDA, idProcurador, null));
							
								//calculo actualizacion deuda
								deudaAct = deudaSimSalPorCad.actualizacionSaldo(fechaActualizacion);
								liqDeudaVO.setImporte(deudaSimSalPorCad.getImporte());
								liqDeudaVO.setSaldo(deudaSimSalPorCad.getSaldo());
								liqDeudaVO.setActualizacion(deudaAct.getRecargo());
								liqDeudaVO.setTotal(deudaAct.getImporteAct());
							} else {
								// Si la simulacion arroja errores, seteamos bandera.
								errorEnSimulacion = true;
							}
							
						} 
						
						if (!simularSaldoPorCaducidad || errorEnSimulacion){
							liqDeudaVO.setPoseeObservacion(true);
							liqDeudaVO.setEsSeleccionable(false);
						}
						
						
					} else if (deuda.getEsIndeterminada()){
						liqDeudaVO.setPoseeObservacion(true);
						liqDeudaVO.setEsSeleccionable(false);
						liqDeudaVO.setEsIndeterminada(true);
						liqDeudaVO.setObservacion(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_INDETERMINADA));
	
					} else if (deuda.getEsReclamada()){
						liqDeudaVO.setPoseeObservacion(true);
						liqDeudaVO.setEsSeleccionable(false);
						liqDeudaVO.setEsReclamada(true);
						liqDeudaVO.setObservacion(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_RECLAMADA));
	
					} else if (deuda.getEstaEnAsentamiento() ){
						liqDeudaVO.setPoseeObservacion(true);
						liqDeudaVO.setEsSeleccionable(false);
						liqDeudaVO.setEsReclamada(true);
						liqDeudaVO.setObservacion("En proceso de Asentamiento");

						
					}else {
						liqDeudaVO.setPoseeObservacion(false);
	
						//Calculamos recargo
						deudaAct = deuda.actualizacionSaldo(fechaActualizacion); // Calculo de actualizacion deuda
						liqDeudaVO.setImporte(deuda.getImporte());
						liqDeudaVO.setSaldo(deuda.getSaldo());
						liqDeudaVO.setActualizacion(deudaAct.getRecargo());
						liqDeudaVO.setTotal(deudaAct.getImporteAct());
						Double actPaga = (deuda.getActualizacion()!=null)?deuda.getActualizacion().doubleValue():0D;
						liqDeudaVO.setImportePago((deuda.getSaldo().doubleValue()<= deuda.getImporte().doubleValue())?(deuda.getImporte()-deuda.getSaldo()+actPaga):0D);
						
						// Seteo de permisos Operar sobre el registro de deuda.
						liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(GdeSecurityConstants.MTD_SELECT_DEUDA, idProcurador, null));
					}
	
					// Seteo de permisos Para ver detalle Deuda
					liqDeudaVO.setVerDetalleDeudaEnabled( this.getPermisoRegDeuda(GdeSecurityConstants.MTD_VER_DETALLE_DEUDA, idProcurador, null));
	
					log.debug(funcName + " 12:: DeudaJud: " +
							" periodo=" + liqDeudaVO.getPeriodoDeuda() +
							" fVto=" + liqDeudaVO.getFechaVto() +
							" saldo=" + liqDeudaVO.getSaldo() +
							" actualiz=" + liqDeudaVO.getActualizacion() +
							" total=" + liqDeudaVO.getTotal() +
							" obs.=" + liqDeudaVO.getObservacion());
	
					log.debug(funcName + " 12::  ###         IdProcurador=" + idProcurador );	
					// Solo agrego a la lista si estoy dentro del mismo procuradore
					if (idProcurador.equals(deuda.getProcurador().getId())){
						
						// Bug# 678:
						// Aplicacion de logica deuda sigue titular.
						// Si tenemos seteado idContribuyente (por acceder desde "Deuda por Contribuyente") y
						// La bandera DeudaSigueTitular = "true"
						// Obtuvimos el cuentaTitular y solo permitimos seleccionar deuda que se encuentre 
						// Dentro del rango fechaDesde/Hasta
						if (cuentaTitular != null){
							if (DateUtil.isDateInRange(deuda.getFechaVencimiento(), cuentaTitular.getFechaDesde(), cuentaTitular.getFechaHasta())){
								liqDeudaProcuradorVO.getListDeuda().add(liqDeudaVO);
							}					
						} else {
							liqDeudaProcuradorVO.getListDeuda().add(liqDeudaVO);	
						}
					}
					
				/*
				 *	En bug 404 se solicita quitar esta validacion
				 *	} // Fin esProcurador y constancia no habilitada
				 */

				// Corte de control por id de Procurador o llegamos al final de la lista
				if (!idProcurador.equals(deuda.getProcurador().getId()) 
						|| i == listDeudaJudicial.size() -1){
					
					// Bandera que indica si es corte de contro por procurador o fin de lista
					boolean cortaPorProcurador = !idProcurador.equals(deuda.getProcurador().getId());
					
					// Si fue agregado algun registro de deuda a la lista de deuda del procurador
					if (liqDeudaProcuradorVO.getListDeuda().size() > 0 ) {
						
						// Seteo datos de la persona, horario atencion y domicilio
						String desProcurador = "";	
	
						desProcurador = deudaAnt.getProcurador().getDescripcion() + " - " + 
						deudaAnt.getProcurador().getDomicilio() + " - " +
						deudaAnt.getProcurador().getHorarioAtencion() + " - Tel: " +
						deudaAnt.getProcurador().getTelefono();
	
						liqDeudaProcuradorVO.setIdProcurador(idProcurador);
						liqDeudaProcuradorVO.setDesProcurador(desProcurador);
	
						log.debug(funcName + " 12:: 	Procurador: =" + liqDeudaProcuradorVO.getDesProcurador() +
								" Subtotal=" + liqDeudaProcuradorVO.getSubTotal());
	
						// Agrego a la lista de Procuradores
						listDeudaProcurador.add(liqDeudaProcuradorVO);
						
					}
					
					// Si no estoy en el final de la lista o el corte es por procurador (caso un solo registro al final).
					if (i < listDeudaJudicial.size() -1 || cortaPorProcurador) {
						
						// reset de valores
						idProcurador = deuda.getProcurador().getId();
						liqDeudaProcuradorVO = new LiqDeudaProcuradorVO();
						
						// Seteo el registro de deuda donde se produjo el cambio de pocurador como anterior
						deudaAnt = deuda;
						
						i--;
					}
				}
				
				i++;
			}				
		}

		log.info(dt.stop("LiqDeuda 12:: Deuda en Vias Judicial"));

		return listDeudaProcurador;
	}

	/**
	 * Obtiene la lista de deuda en via Administrativa.
	 */
	public List<LiqDeudaAdminVO> getDeudaAdmin() throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		DemodaTimer dt = new DemodaTimer();

		List<LiqDeudaAdminVO> bloqueDeudaAdmin = new ArrayList<LiqDeudaAdminVO>();
	
		Convenio convenioForSimulacion = null; 
		boolean errorEnSimulacion = false;
		
		List<DeudaAdmin> listDeudaAdmin = cuenta.getListDeudaAdmin();
		log.debug(funcName + " 13:: Deuda en Vias Administrativa : " + listDeudaAdmin.size() + " Registros de deuda encontradas");
		
		// Si es una cuenta de CdM, y aun no se han asentado pagos, mostramos 
		// unicamente el Plan Largo
		if (cuenta.getRecurso().getCategoria().getId().equals(Categoria.ID_CDM)) {
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);
			
			// Si no hay pagos asentados 
			if (plaCuaDet != null && plaCuaDet.getCantCuotas() != null && plaCuaDet.getCantCuotas() > 1) {
				// eliminamos la deuda asociada al Plan Contado
				DeudaAdmin deudaPlanContado = cuenta.getDeudaAdminPlanContadoCdM(); 
				if (deudaPlanContado != null)
					listDeudaAdmin.remove(deudaPlanContado);
			}  
		}
		
		Recurso recurso = cuenta.getRecurso();
		
		if (listDeudaAdmin.size() > 0){
			// Bloque de un solo elemento de Gestion Deuda Admin
			LiqDeudaAdminVO liqDeudaAdminVO = new LiqDeudaAdminVO();

			// Identificar si la deuda corresponde a Drei o a Etur
			boolean esDrei = Recurso.getDReI().getId().equals(recurso.getId());
			boolean esEtur = Recurso.getETur().getId().equals(recurso.getId());
			
			// Fecha de Inicio de Regimen Simplificado
			Date fechaInicioRS = DateUtil.getDate(SiatParam.getInteger(SiatParam.ANIO_INICIO_RS), 
					  							  SiatParam.getInteger(SiatParam.MES_INICIO_RS), 1);
			
			
			//Determino el regimen de la cuenta (RS o RG) al dia de la fecha
			boolean esRegimenGeneral= true;
			RecAtrCueV recAtrCueV = cuenta.getRegimenDreiVigente(new Date());
			if (recAtrCueV != null && recAtrCueV.getValor().equals(Cuenta.VALOR_REGIMEN_SIMPLIFICADO) ) {
				esRegimenGeneral = false;
			}

			Double subtotal = new Double(0);

			// Por cada registro de deuda recuperado
			for(DeudaAdmin deuda:listDeudaAdmin){

				LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 
				
				// Paso los datos de cada registro de deuda al LiqDeudaVO
				liqDeudaVO.setIdDeuda(deuda.getId());
				liqDeudaVO.setIdViaDeuda(deuda.getViaDeuda().getId());
				liqDeudaVO.setIdEstadoDeuda(deuda.getEstadoDeuda().getId());
				liqDeudaVO.setDesViaDeuda(deuda.getViaDeuda().getDesViaDeuda());
				liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
				liqDeudaVO.setCodRefPag(deuda.getCodRefPag().toString());
				liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
				liqDeudaVO.setFechaPago(DateUtil.formatDate(deuda.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
				liqDeudaVO.setSistema(deuda.getSistema().getId().toString());
				
				if (deuda.getEsExcentaPago()){
					liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setEsSeleccionable(false);
					liqDeudaVO.setEsExentoPago(true);
					
					liqDeudaVO.setObservacion(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_EXENTA));
					
				} else if (deuda.getEsConvenio()){
					
					/*
					 *  Codigo anterior a la simulacion de saldo por caducidad
					 *  
					 * liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setEsSeleccionable(false);
					liqDeudaVO.setPoseeConvenio(true);
					String obsConvenio = deuda.getConvenio().getNroConvenio() + " - " + deuda.getConvenio().getPlan().getDesPlan();
					liqDeudaVO.setObservacion(obsConvenio);
					liqDeudaVO.setIdLink(deuda.getIdConvenio());*/
										
					
					// Valores comunes
					liqDeudaVO.setPoseeConvenio(true);
					
					String obsConvenio = deuda.getConvenio().getNroConvenio() + " - " + deuda.getConvenio().getPlan().getDesPlan();
					liqDeudaVO.setObservacion(obsConvenio);
					liqDeudaVO.setIdLink(deuda.getIdConvenio());
					
					// Valores particulares					
					if (simularSaldoPorCaducidad){
						
						// Corte de control por convenio.
						if (convenioForSimulacion == null || convenioForSimulacion.getId().longValue() != deuda.getIdConvenio().longValue())
							convenioForSimulacion = deuda.getConvenio();
						
						Deuda deudaSimSalPorCad = convenioForSimulacion.obtenerDeudaSimulaSalPorCad(deuda);
						
						// Si la simulacion de saldo por caducidad arroja error o mensaje, lo pasamos a la cuenta.
						// NOTA: en la simulacion se calcula para el primer registro de deuda y ya sabemos si todo el convenio tiene error
						if (!deudaSimSalPorCad.hasError()){
							
							liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(GdeSecurityConstants.MTD_SELECT_DEUDA, null, null));
						
							//calculo actualizacion deuda
							DeudaAct deudaAct = deudaSimSalPorCad.actualizacionSaldo(fechaActualizacion);
							liqDeudaVO.setImporte(deudaSimSalPorCad.getImporte());
							liqDeudaVO.setSaldo(deudaSimSalPorCad.getSaldo());
							liqDeudaVO.setActualizacion(deudaAct.getRecargo());
							liqDeudaVO.setTotal(deudaAct.getImporteAct());
						
						} else {
							// Si la simulacion arroja errores, seteamos bandera.
							errorEnSimulacion = true;
						}
						
					} 
					
					if (!simularSaldoPorCaducidad || errorEnSimulacion){
						liqDeudaVO.setPoseeObservacion(true);
						liqDeudaVO.setEsSeleccionable(false);
					}
					
					if (recurso.getEsAutoliquidable().intValue()== SiNo.SI.getId().intValue()){
						liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(GdeSecurityConstants.MTD_SELECT_DEUDA, null, null));
					}
				

				// issue 5573, - federico luna
				} else if (getEsOsirisPagoPendiente(deuda)){ //nota: no lo podemos poner en la deuda por cuestiones de performance.
					liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setEsSeleccionable(true);
					liqDeudaVO.setEsOsirisPagoPendiente(true); // se debe mostrar igual que deuda indeterminada 
					liqDeudaVO.setObservacion(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_OSIRISPAGOPENDIENTE));
					
				} else if (deuda.getEsIndeterminada()){
					liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setEsSeleccionable(false);
					liqDeudaVO.setEsIndeterminada(true);
					liqDeudaVO.setObservacion(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_INDETERMINADA));

				} else if (deuda.getEsReclamada()){
					liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setEsSeleccionable(false);
					liqDeudaVO.setEsReclamada(true);
					liqDeudaVO.setObservacion(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_RECLAMADA));

				} else if (deuda.getEstaEnAsentamiento() ){
					liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setEsSeleccionable(false);
					liqDeudaVO.setEsReclamada(true);
					liqDeudaVO.setObservacion("En proceso de Asentamiento");

				} else {
					//calculo actualizacion deuda
					DeudaAct deudaAct = deuda.actualizacionSaldo(fechaActualizacion);

					liqDeudaVO.setImporte(deuda.getImporte());
					liqDeudaVO.setSaldo(deuda.getSaldo());
					liqDeudaVO.setActualizacion(deudaAct.getRecargo());
					liqDeudaVO.setTotal(deudaAct.getImporteAct());
					Double actPaga = (deuda.getActualizacion()!=null)?deuda.getActualizacion().doubleValue():0D;
					liqDeudaVO.setImportePago((deuda.getSaldo().doubleValue()<= deuda.getImporte().doubleValue())?(deuda.getImporte()-deuda.getSaldo()+actPaga):0D);
					
					liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(GdeSecurityConstants.MTD_SELECT_DEUDA, null, null));
					
					subtotal += deudaAct.getImporteAct();
				}
				
				/* - Mantis #6659:
				 * 	Si el recurso es: DReI o ETur (original) 
				 * 					  Y anio/periodo >= AnioInicioRS/MesInicioRS 
				 * 		- Determino si se trata de deuda rectificativa.
				 * 			· Es Rectif :: muestro "-" en la liquidacion de la deuda
				 * 		- SiNo, Determino si es deuda original
				 * 				Y fechaVto <= Fecha Ultimo Envio Procesado de Recurso  
				 * 				- Determino si existe una DJ para la cuenta, periodo y anio
				 * 						· Existe    :: importe = total declarado en DJ.
				 * 						· No existe :: muestro "N/D" (no declarado).
				 */
				Date fechaDeuda = DateUtil.getDate(deuda.getAnio().intValue(),deuda.getPeriodo().intValue(), 1);
				if ((esEtur||esDrei) && esRegimenGeneral && DateUtil.isDateAfterOrEqual(fechaDeuda, fechaInicioRS)) {
					// Deuda Rectificativa
					if (deuda.getRecClaDeu().getId().longValue() == RecClaDeu.ID_DDJJ_RECTIFICATIVA_DREI 
							|| deuda.getRecClaDeu().getId().longValue() == RecClaDeu.ID_DDJJ_RECTIFICATIVA_ETUR) {

						// Seteo un importeCurrency negativo (<> -999) así muestro "-" por pantalla.
						liqDeudaVO.setImporteCurrency(-1D);

						// Deuda Original
					}else if((deuda.getRecClaDeu().getId().longValue() == RecClaDeu.ID_DDJJ_ORIGINAL_ETUR 
								|| deuda.getRecClaDeu().getId().longValue() == RecClaDeu.ID_DDJJ_ORIGINAL)
								&& DateUtil.isDateBeforeOrEqual(deuda.getFechaVencimiento(),recurso.getFecUltEnvPro())) {
						
						DecJur decJur = DecJur.getLastByCuentaPeriodoYAnio(deuda.getCuenta(), 
																		   deuda.getPeriodo().intValue(), 
																		   deuda.getAnio().intValue());
						if (null == decJur)
							// Si la deuda no tiene una DJ relacionada, seteo el
							// importeCurrency en -999 así muestro "N/D" por pantalla.
							liqDeudaVO.setImporteCurrency(-999D);
						else
							// Si tiene DJ asociada, muestro el total declarado
							liqDeudaVO.setImporte(decJur.getTotalDeclarado());
					}
				}

				// Seteo de permisos
				liqDeudaVO.setVerDetalleDeudaEnabled(this.getPermisoRegDeuda(GdeSecurityConstants.MTD_VER_DETALLE_DEUDA, null, null)); 
				
				log.debug(funcName + " 13:: DeudaAdmin: " +
						" periodo=" + liqDeudaVO.getPeriodoDeuda() +
						" fVto=" + liqDeudaVO.getFechaVto() +
						" ipte=" + liqDeudaVO.getImporte() +
						" saldo=" + liqDeudaVO.getSaldo() +
						" actualiz=" + liqDeudaVO.getActualizacion() +
						" total=" + liqDeudaVO.getTotal() +
						" obs.=" + liqDeudaVO.getObservacion() +
						" f.pag= " + liqDeudaVO.getFechaPago());
				
				// Bug# 781:
				// Si el recurso esta configurado como Deuda "NO" es Exigible una vez Vencida, 
				// Y la deuda NO esta en Convenio.
				// no la mostramos. 
				if (cuenta.getLiqCuentaFilter() != null && !cuenta.getLiqCuentaFilter().getDeuExiVen() && deuda.getIdConvenio()==null){
					if (DateUtil.isDateAfterOrEqual(deuda.getFechaVencimiento(), new Date())){
						liqDeudaAdminVO.getListDeuda().add(liqDeudaVO);
					}	
					
				} else {
				
					// Bug# 678:
					// Aplicacion de logica deuda sigue titular.
					// Si tenemos seteado idContribuyente (por acceder desde "Deuda por Contribuyente") y
					// La bandera DeudaSigueTitular = "true"
					// Obtuvimos el cuentaTitular y solo permitimos seleccionar deuda que se encuentre 
					// Dentro del rango fechaDesde/Hasta
					if (cuentaTitular != null){
						if (DateUtil.isDateInRange(deuda.getFechaVencimiento(), cuentaTitular.getFechaDesde(), cuentaTitular.getFechaHasta())){
							liqDeudaAdminVO.getListDeuda().add(liqDeudaVO);
						}					
					} else {
						liqDeudaAdminVO.getListDeuda().add(liqDeudaVO);	
					}
				
				}
			}				

			// Lista con un unico elemento.
			bloqueDeudaAdmin.add(liqDeudaAdminVO);					

		}

		log.info(dt.stop("LiqDeuda 13:: Deuda en Vias Administrativa"));

		return bloqueDeudaAdmin;

	}

	/*
	 * Indica si existen pagos pendientes en osiris.
	 * Para deudas con clasificacion PagoOriginal o Regimen Simplificado
	 *
	 * Nota 1: Por el momento soporta los recursos DreI y Etur
	 * 
	 * Nota 2: Si la deuda no es Drei o Etur retorna false
	 *         Si la deuda no es Clasificacion Original o Regimen Simplificado retorna false
	 *         Si la deuda esta paga retorna false
	 * 
	 * Retorna true, si para este periodo, anio, recurso, cuenta
	 * esite un registro en bal_detallePago con estado !(ERROR, ANULADO) 
	 * con mismo anio, periodo, cuenta e impuesto correpondiente a recurso
	 * que el de esta deuda.
	 *
	 * issue: 5573;federico luna
	 */
	private boolean getEsOsirisPagoPendiente(Deuda deuda) throws Exception {
		String abrClaDeu, codRecurso;
		
		// debe estar activa la opcbion
		if (SiatParam.getInteger(SiatParam.VERIFICA_PAGO_OSIRIS, 0) != 1) {
			return false;
		}

		// recurso tiene que ser Drei o Etur
		codRecurso = cuenta.getRecurso().getCodRecurso();
		if (!Recurso.COD_RECURSO_DReI.equals(codRecurso) && !Recurso.COD_RECURSO_ETuR.equals(codRecurso)) {
			return false;
		}
		
		// debe ser deuda original o regimen simplificado 
		abrClaDeu = deuda.getRecClaDeu().getAbrClaDeu();		
		if (!RecClaDeu.ABR_ORIG.equals(abrClaDeu) && !RecClaDeu.ABR_RS.equals(abrClaDeu)) {
			return false;
		}
		
		// la deuda no debe estar paga (Mantis#6580)
		if (null != deuda.getFechaPago()) {
			return false;
		}
		
		//debemos inicializar?
		if (detallePagos == null) {
			initDetallesPagoOsiris();
		}

		//buscamos por pago por anio-periodo
		String clave = deuda.getAnio() + ";" + deuda.getPeriodo();
		return detallePagos.containsKey(clave);
	}

	private boolean initDetallesPagoOsiris() {
		String impuestos, codRecurso;
	
		if (cuenta == null) { 
			return false;
		}
		
		codRecurso = cuenta.getRecurso().getCodRecurso();		
		//ahora determinamos los impuestos por los que estamos interesados 
		if (codRecurso.equals(Recurso.COD_RECURSO_DReI)) { //DReI
			impuestos = String.format("%d,%d", ImpuestoAfip.DREI.getId(), ImpuestoAfip.RS_DREI.getId()); 
		} else if (codRecurso.equals(Recurso.COD_RECURSO_ETuR)) { //Etur
			impuestos = String.format("%d,%d", ImpuestoAfip.ETUR.getId(), ImpuestoAfip.RS_ETUR.getId()); 
		} else {
			return false;
		}
		
		detallePagos = BalDAOFactory.getBalanceDAO().getMapDetallePagoValido(cuenta.getNumeroCuenta(), impuestos);
		
		return true;
	}


	/**
	 * Obtiene la lista de deuda en via Administrativa actualizada a la fecha pasada como parametro.
	 */
	public List<LiqDeudaAdminVO> getDeudaAdmin(Date fechaAct) throws Exception {
		this.fechaActualizacion = fechaAct;
		List<LiqDeudaAdminVO> listDeudaAdmin = this.getDeudaAdmin();
		this.fechaActualizacion = new Date();
		return listDeudaAdmin;
	}
	

	/**
	 * Obtiene la lista de deuda en via Administrativa.
	 */
	public List<LiqDeudaAnuladaVO> getDeudaAnulada() throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		DemodaTimer dt = new DemodaTimer();

		List<LiqDeudaAnuladaVO> bloqueDeudaAnulada = new ArrayList<LiqDeudaAnuladaVO>();

		List<DeudaAnulada> listDeudaAnulada = cuenta.getListDeudaAnulada();
		log.debug(funcName + " 13:: Deuda Anulada: " + listDeudaAnulada.size() + " Registros de deuda encontradas");

		if (listDeudaAnulada.size() > 0){
			// Bloque de un solo elemento de Gestion Deuda Admin
			LiqDeudaAnuladaVO liqDeudaAnuladaVO = new LiqDeudaAnuladaVO();

			Double subtotal = new Double(0);

			// Por cada registro de deuda recuperado
			for(DeudaAnulada deudaAnulada:listDeudaAnulada){
				
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 

				// Paso los datos de cada registro de deuda al LiqDeudaVO
				liqDeudaVO.setIdDeuda(deudaAnulada.getId());
				liqDeudaVO.setIdViaDeuda(deudaAnulada.getViaDeuda().getId());
				liqDeudaVO.setDesViaDeuda(deudaAnulada.getViaDeuda().getDesViaDeuda());
				liqDeudaVO.setIdEstadoDeuda(deudaAnulada.getEstadoDeuda().getId());
				liqDeudaVO.setDesEstado(deudaAnulada.getEstadoDeuda().getDesEstadoDeuda());
				liqDeudaVO.setPeriodoDeuda(deudaAnulada.getStrPeriodo());
				liqDeudaVO.setCodRefPag(deudaAnulada.getCodRefPag().toString());
				liqDeudaVO.setFechaVencimiento(deudaAnulada.getFechaVencimiento());
				liqDeudaVO.setImporte(deudaAnulada.getImporte());
				liqDeudaVO.setSaldo(deudaAnulada.getSaldo());
				liqDeudaVO.setActualizacion(deudaAnulada.getActualizacion());
				liqDeudaVO.setTotal(deudaAnulada.getImporte());
				
				liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(GdeSecurityConstants.MTD_SELECT_DEUDA, deudaAnulada.getIdProcurador(), null));
					
				subtotal += deudaAnulada.getImporte();
				
				// Seteo de permisos
				liqDeudaVO.setVerDetalleDeudaEnabled(this.getPermisoRegDeuda(GdeSecurityConstants.MTD_VER_DETALLE_DEUDA, null, null)); 
				
				// Si es una anulacion por cambio de plan cdm, no se puede realizar la vuelta atras
				if (deudaAnulada.getMotAnuDeu().getId() != null &&
						deudaAnulada.getMotAnuDeu().getId().equals(MotAnuDeu.ID_CAMBIOPLAN_CDM)){
				
					liqDeudaVO.setVueltaAtrasAnularEnabled(false);
				}
				
				// Motivo de Anualcion
				liqDeudaVO.setObservacion(deudaAnulada.getMotAnuDeu().getDesMotAnuDeu());
								
				log.debug(funcName + " 13:: DeudaAnulada: " +
						" periodo=" + liqDeudaVO.getPeriodoDeuda() +
						" fVto=" + liqDeudaVO.getFechaVto() +
						" saldo=" + liqDeudaVO.getSaldo() +
						" actualiz=" + liqDeudaVO.getActualizacion() +
						" total=" + liqDeudaVO.getTotal() +
						" obs.=" + liqDeudaVO.getObservacion());

				liqDeudaAnuladaVO.getListDeuda().add(liqDeudaVO);					
			}				

			// Lista con un unico elemento.
			bloqueDeudaAnulada.add(liqDeudaAnuladaVO);					

		}

		log.info(dt.stop("LiqDeuda 13:: Deuda Anulata"));

		return bloqueDeudaAnulada;

	}
	
	
	/**
	 * - Devuelve el permiso para un registro de Deuda segun sea el usuario: 
	 * 		Operador Judicial, Procurador, Anonimo o usuario CMD
	 * - Si se pasan las reglas de permiso por negocio, se chequea en SWE  
	 * -  Si el idProcurador recibido no es nulo,  
	 *	  es porque estamos asignado permiso al bloque de Deuda Judicial
	 * @author Cristian
	 * @param sweMethodName
	 * @param idProcurador
	 * @return
	 * @throws Exception
	 */
	public boolean getPermisoRegDeuda(String sweMethodName, Long idProcurador, Long idProcedimientoCyq) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		UserContext userContext = DemodaUtil.currentUserContext();
		
		log.debug(funcName + " enter: tpoUsr=" + userContext.getStrTipoUsuario() + 
									" idProcurador=" + idProcurador +
									" idProcedim=" + idProcedimientoCyq +
									" sweMethodName=" + sweMethodName);
		
		// No se permite realizar ninguna accion sobre la deuda
		if (forzarNoPermitirAccionSobreDeuda)
			return false;
		
		// Anonimo
		if (userContext.getEsAnonimo()){
			// Estamos en Bloque Ddeuda Judicial o CyQ
			if (idProcurador != null || idProcedimientoCyq != null){
				// El usuario anonimo no puede operar sobre Deuda en Via Judicial
				if (GdeSecurityConstants.MTD_SELECT_DEUDA.equals(sweMethodName) || 
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName))
					return false;
				else
					return getPermisoSwe(sweMethodName);
				
			} else {			
				return getPermisoSwe(sweMethodName);
			}
		
		// Usuario CMD	
		} else if (userContext.getEsUsuarioCMD()){
			
			// Estamos en Bloque Ddeuda Judicial o Cyq
			if (idProcurador != null || idProcedimientoCyq != null){
				// El usuario CMD no opera sobre Deuda Judicial en Liquidacion Deuda.
				if (GdeSecurityConstants.MTD_SELECT_DEUDA.equals(sweMethodName) || 
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName))
					return false;					
				else 
					return getPermisoSwe(sweMethodName);
				
			} else {
				return getPermisoSwe(sweMethodName);
			}
	
		// Procurador	
		} else if (userContext.getEsProcurador()){
			
			// Estamos en Bloque Ddeuda Judicial
			if (idProcurador != null){
				// Solo puede operar sobre la deuda Asignada a El mismo.
				if (GdeSecurityConstants.MTD_SELECT_DEUDA.equals(sweMethodName) || 
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName)){
					if (idProcurador.equals(userContext.getIdProcurador()))
						return true;
					else
						return false;

				} else {
					return getPermisoSwe(sweMethodName);
				}
				
			} else {
				// Fuera del Bloque de deuda Judicial
				// El Procurador No puede Si es un procurador y el metodo es "selectDeuda" no permito seleccion.
				if (GdeSecurityConstants.MTD_SELECT_DEUDA.equals(sweMethodName) ||
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName))
					return false;
				
				return getPermisoSwe(sweMethodName);
			}
		
		// Operador Judicial (Puede operar sobre la deuda de los procurares en su nombre)
		} else if (userContext.getEsOperadorJudicial()){
			
			// Estamos en Bloque Ddeuda Judicial
			if (idProcurador != null){
				// Puede operar sobre la deuda Asignada a cualquier procurador.
				if (GdeSecurityConstants.MTD_SELECT_DEUDA.equals(sweMethodName) || 
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName)){
					return true;
				} else {
					return getPermisoSwe(sweMethodName);
				}
				
			} else {
				// Fuera del Bloque de deuda Judicial
				// El Procurador No puede Si es un procurador y el metodo es "selectDeuda" no permito seleccion.
				if (GdeSecurityConstants.MTD_SELECT_DEUDA.equals(sweMethodName) ||
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName))
					return false;
				
				return getPermisoSwe(sweMethodName);
			}
		
		// Usuario de Concurso y Quiebras	
		} else if (userContext.getEsUsuarioCyq()){	
			
			// Estamos en Bloque Ddeuda CyQ
			if (idProcedimientoCyq != null){
				// Puede operar sobre la deuda Asignada a cualquier procurador.
				if (GdeSecurityConstants.MTD_SELECT_DEUDA.equals(sweMethodName) || 
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName)){
					return true;
				} else {
					return getPermisoSwe(sweMethodName);
				}
				
			} else {
				// Fuera del Bloque de deuda CyQ
				// No puede seleccionar deuda de otra via.
				if (GdeSecurityConstants.MTD_SELECT_DEUDA.equals(sweMethodName) ||
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName))
					return false;
				
				return getPermisoSwe(sweMethodName);
			}
			
		// Un usuario distinto a los anteriores
	    } else {
			return getPermisoSwe(sweMethodName);			
		}		
	}

	/**
	 * Devuelve el permiso de operacion sobre registro de cuota impaga.
	 * 
	 * 
	 * @author Cristian
	 * @param sweMethodName
	 * @param idProcuradorConvenio
	 * @return
	 * @throws Exception
	 */
	public boolean getPermisoRegCuota(String sweMethodName, Long idProcuradorConvenio) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		UserContext userContext = DemodaUtil.currentUserContext();
		
		log.debug(funcName + " enter: tipoUsuario=" + userContext.getStrTipoUsuario() + 
									" idProcuradorConvenio=" + idProcuradorConvenio +
									" sweMethodName=" + sweMethodName);
		
		// Anonimo
		if (userContext.getEsAnonimo()){
			// Estamos en Bloque Ddeuda Judicial
			if (idProcuradorConvenio != null){
				// El usuario anonimo no puede operar sobre Deuda en Via Judicial
				if (GdeSecurityConstants.MTD_SELECT_CUOTA.equals(sweMethodName) || 
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName))
					return false;
				else
					return getPermisoSwe(sweMethodName);
				
			} else {			
				return getPermisoSwe(sweMethodName);
			}
		
		// Usuario CMD	
		} else if (userContext.getEsUsuarioCMD()){
			
			// Estamos en Bloque Ddeuda Judicial
			if (idProcuradorConvenio != null){
				// El usuario CMD no opera sobre Deuda Judicial en Liquidacion Deuda.
				if (GdeSecurityConstants.MTD_SELECT_CUOTA.equals(sweMethodName) || 
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName))
					return false;					
				else 
					return getPermisoSwe(sweMethodName);
				
			} else {
				return getPermisoSwe(sweMethodName);
			}
	
		// Procurador	
		} else if (userContext.getEsProcurador()){
			
			// Estamos en Convenio en via Judicial
			if (idProcuradorConvenio != null){
				// Solo puede operar sobre la deuda Asignada a El mismo.
				if (GdeSecurityConstants.MTD_SELECT_CUOTA.equals(sweMethodName) || 
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName)){
					// Solo si el convenio le pertenece al procurador, puede operar sobre el
					if (idProcuradorConvenio.equals(userContext.getIdProcurador()))
						return true;
					else
						return false;

				} else {
					return getPermisoSwe(sweMethodName);
				}
				
			} else {
				// Fuera del Bloque de deuda Judicial
				// El Procurador No puede operar sobre convenio en via administrativa.
				if (GdeSecurityConstants.MTD_SELECT_CUOTA.equals(sweMethodName) ||
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName))
					return false;
				else
					return getPermisoSwe(sweMethodName);
			}
		
		// Operador Judicial (Puede operar sobre la deuda de los procurares en su nombre)
		} else if (userContext.getEsOperadorJudicial()){
			
			// Estamos en Bloque Ddeuda Judicial
			if (idProcuradorConvenio != null){
				// Puede operar sobre la deuda Asignada a cualquier procurador.
				if (GdeSecurityConstants.MTD_SELECT_CUOTA.equals(sweMethodName) || 
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName)){
					return true;
				} else {
					return getPermisoSwe(sweMethodName);
				}
				
			} else {
				// Fuera del Bloque de deuda Judicial
				// El Procurador No puede Si es un procurador y el metodo es "selectDeuda" no permito seleccion.
				if (GdeSecurityConstants.MTD_SELECT_CUOTA.equals(sweMethodName) ||
						GdeSecurityConstants.MTD_RECLAMAR_ACENT.equals(sweMethodName))
					return false;
				
				return getPermisoSwe(sweMethodName);
			}
		
		// Un usuario distinto a los anteriores
	    } else {
			return getPermisoSwe(sweMethodName);			
		}		
	}
	/**
	 * Devuelve los permisos para las "Acciones" del Adapter de Deuda o 
	 * el Adapter de Convenio
	 * 
	 * seleccionableAdmin "Si existen registros de Deuda o Cuotas de Convenio en via Administrativa seleccionables"
	 * seleccionableJudicial "Si existen registros de Deuda o Cuotas de Convenio en via Administrativa seleccionables"
	 * perteneceAProcurador "Deuda o Convenio"
	 * 
	 * @author Cristian
	 * @param sweMethodName
	 * @param idProcurador
	 * @return
	 * @throws Exception
	 */
	public boolean getPermisoAccionesAdapter(String sweMethodName, 
										  boolean seleccionableCyq,
										  boolean seleccionableAdmin, 
										  boolean seleccionableJudicial, 	
										  boolean perteneceAProcurador ) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		UserContext userContext = DemodaUtil.currentUserContext();
		
		log.debug(funcName + " enter: tipoUsuario=" + userContext.getStrTipoUsuario() + 
				" sweMethodName=" + sweMethodName);
		
		
		// No se permite realizar ninguna accion sobre la deuda
		if (forzarNoPermitirAccionSobreDeuda)
			return false;
		
		/*
		 *  Reglas de negocio:
		 *  
		 *  Via Judicial
		 *    - Los Procuradores solo operan sobre deuda que le perteneza.
		 *    - Los Operadores Judiciales solo sobre esta via y sobre la deuda de cualquier Procurador 
		 *   
		 *  Via Administrativa
		 *	  - Usuarios Anonimo
		 *	  - Usuarios CMD	
		 * 
		 */
		
		// Anonimo
		if (userContext.getEsAnonimo()){
			// Si existe algun registro de deuda Administrativa "seleccionable" 
			if (seleccionableAdmin){
				return getPermisoSwe(sweMethodName);				
			} else {
				return false;
			}
		
			// Usuario CMD	
		} else if (userContext.getEsUsuarioCMD()){
			
			// Si existe algun registro de deuda Administrativa "seleccionable"
			if (seleccionableAdmin){
				return getPermisoSwe(sweMethodName);
				
			} else {
				return false;
			}
			
		// Procurador	
		} else if (userContext.getEsProcurador()){
			
			// Si existe algun registro de deuda Judicial "seleccionable" y que le pertenezca al Procurador Logueado.
			if (seleccionableJudicial && perteneceAProcurador){				
				return getPermisoSwe(sweMethodName);
			} else {
				return false;
			}
		
		// Operador Judicial (Puede operar sobre la deuda de los procurares en su nombre)
		} else if (userContext.getEsOperadorJudicial()){
			
			// Si existe algun registro de deuda Judicial "seleccionable" 
			if (seleccionableJudicial){
				return getPermisoSwe(sweMethodName);
			} else {
				return false;
			}
		
		// Usuario Cyq (Puede operar sobre la deuda cyq)
		} else if (userContext.getEsUsuarioCyq()){
			
			// Si existe algun registro de deuda Cyq "seleccionable" 
			if (seleccionableCyq){
				return getPermisoSwe(sweMethodName);
			} else {
				return false;
			}			
		// Un usuario distinto a los anteriores
	    } else {
			return getPermisoSwe(sweMethodName);			
		}
		
	}
	
	
	/**
	 * Obtiene Permiso de Swe para un metodo determinado de la accion para LiquidacionDeuda.  
	 * 
	 * 
	 * @author Cristian
	 * @param sweMethodName
	 * @return
	 */
	public boolean getPermisoSwe(String sweMethodName) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserContext userContext = DemodaUtil.currentUserContext();
		String actionName = userContext.getAccionSWE();
		
		log.debug(funcName + " enter: acionName=" + actionName + " sweMethodName=" + sweMethodName);

		// No se permite realizar ninguna accion sobre la deuda
		if (forzarNoPermitirAccionSobreDeuda)
			return false;
		
		
		Boolean  hasAccess = true;
		SweContext ctx = SiatCache.getInstance().getSweContext();
		long hasAccessLong = ctx.hasAccess(userContext.getIdsAccionesModuloUsuario(), actionName, sweMethodName);
		if (hasAccessLong==Common.HASACCESS_SINACCESO || hasAccessLong==Common.HASACCESS_NOMBRESNULOS_ERROR ) { 
			log.info("canAccess():Access Denied usr=" + userContext.getUserName() + " act=" + actionName + " mtd=" + sweMethodName);
			hasAccess = false;
		}

		return hasAccess;

	}
	
	public String getDesCuit() throws Exception{
		//cuenta.getCuitTitular();
		//hacer toda la logica para ver que devuelve dependiendo del usuario(ver test case 256)
		return null;
	}
	
	/**
	 * 
	 * Obtiene los datos de un convenio (formalizado).
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public LiqConvenioVO getConvenio() throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		LiqConvenioVO liqConvenio = new LiqConvenioVO();
		
		log.debug(funcName + " 2:: getConvenio");
		
		liqConvenio.setIdConvenio(this.convenio.getId());
		
		liqConvenio.setNroConvenio(this.convenio.getNroConvenio().toString());
		liqConvenio.setDesPlan(this.convenio.getPlan().getDesPlan());
		liqConvenio.setDesViaDeuda(this.convenio.getViaDeuda().getDesViaDeuda());
		liqConvenio.setDesEstadoConvenio(this.convenio.getEstadoConvenio().getDesEstadoConvenio());
		liqConvenio.setCanCuotasPlan(this.convenio.getCantidadCuotasPlan().toString());
		liqConvenio.setTotImporteConvenio(this.convenio.getTotImporteConvenio());
		liqConvenio.setIdSistema( this.convenio.getSistema()!=null?String.valueOf(this.convenio.getSistema().getId()):"");
		liqConvenio.setFechaFor(DateUtil.formatDate(this.convenio.getFechaFor(), DateUtil.ddSMMSYYYY_MASK));
		
				
		// Se controla nulidad de id per for, por migracion
		if (this.convenio.getIdPerFor() != null){
			Persona perFor = Persona.getById(this.convenio.getIdPerFor());
			PersonaVO personaVO = (PersonaVO) perFor.toVO(3);
			liqConvenio.setPersona(personaVO);
		} else {
			liqConvenio.setPoseeDatosPersona(false);			
		}
		
		// Si la via del convenio es judicial, seteamos el procurador		
		if (this.convenio.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL){
			liqConvenio.setProcurador((ProcuradorVO)convenio.getProcurador().toVO(0, false));
			liqConvenio.setPoseeProcurador(true);
		}
		
		// Si existe caso lo seteamos
		if (this.convenio.getIdCaso() != null){
			liqConvenio.setIdCaso(this.convenio.getIdCaso());
			liqConvenio.setPoseeCaso(true);
		}	
			
		// Se controla nulidad 
		if (this.convenio.getTipoPerFor() != null){
			liqConvenio.setTipoPerFor((TipoPerForVO) this.convenio.getTipoPerFor().toVO(0));
		} 
		
		if (this.convenio.getTipoDocApo() != null){
			liqConvenio.setTipoDocApo((TipoDocApoVO) this.convenio.getTipoDocApo().toVO(0));
		}
		
		liqConvenio.setObservacionFor(this.convenio.getObservacionFor());
		
		// Agente agente interviniente
		liqConvenio.setUsusarioFor(this.convenio.getUsuarioFor());
		
		// Periodos Incluidos
		liqConvenio.setListPeriodoIncluido(this.getListPeriodosIncluido());
		
		// Cuotas Pagas e Impagas
		liqConvenio.setListCuotaPaga(this.getListCuotasPagas());
		
		liqConvenio.setListCuotaInpaga(this.getListCuotasImpagas());
		
		// Si el convenio tiene sellado en la primer cuota seteo en que lista se encuentra para mostrar la leyenda
		if (convenio.isSelladoEnCuotaUno()){
			liqConvenio.setImporteSellado(convenio.getListConvenioCuota().get(0).getImporteSellado());
			if (convenio.getListCuotasPagas().indexOf(convenio.getListConvenioCuota().get(0))!=(-1)){
				liqConvenio.setCuoPagConSel(true);
			}else{
				liqConvenio.setCuoInpConSel(true);
			}
		}
				
		return liqConvenio;
	}
	
	/**
	 * 
	 * Devuelve los periodos incluidos en un convenio.
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public List<LiqDeudaVO> getListPeriodosIncluido() throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		List<ConvenioDeuda> listConvenioDeuda = convenio.getListConvenioDeuda(); 
		
		log.debug(funcName + " :: Periodos Incluidos encontrados: " +  listConvenioDeuda.size()); 
		
		List<LiqDeudaVO> listPeriodos = new ArrayList<LiqDeudaVO>();
		
		// Compara fechaVencimiento de ConvenioDeuda
		Comparator<ConvenioDeuda> compConvenioDeuda = new Comparator<ConvenioDeuda>() {
			public int compare(ConvenioDeuda cd1, ConvenioDeuda cd2) {
				if (DateUtil.isDateAfter(cd1.getFecVenDeuda(), cd2.getFecVenDeuda()) ){
					return 1;
				}else if (DateUtil.isDateBefore(cd1.getFecVenDeuda(), cd2.getFecVenDeuda())){
					return -1;
				}				
				return 0;
			}
		};
		
		Collections.sort(listConvenioDeuda, compConvenioDeuda);
		
		for(ConvenioDeuda cd:listConvenioDeuda){
			
			LiqDeudaVO periodo = new LiqDeudaVO();
			
			periodo.setSistema(String.valueOf(cd.getDeuda().getSistema()!=null?cd.getDeuda().getSistema().getId():""));
			
			periodo.setFechaVencimiento(cd.getDeuda().getFechaVencimiento());
			
			periodo.setSaldo(cd.getCapitalEnPlan());
			
			periodo.setActualizacion(cd.getActEnPlan());
			
			periodo.setTotal(cd.getTotalEnPlan());
			
			periodo.setFechaPago(DateUtil.formatDate(cd.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
			
			periodo.setIdDeuda(cd.getIdDeuda());
			
			// Si No es deuda privilegio
			if (cd.getEsDeudaPriv() == null || cd.getEsDeudaPriv().intValue() == 0){
				periodo.setPeriodoDeuda(cd.getDeuda().getStrPeriodo());
			
			//Es Deuda Privilego
			} else {
				periodo.setPeriodoDeuda(cd.getDeudaPrivilegio().getRecurso().getDesRecurso() + " " + 
						cd.getDeudaPrivilegio().getTipoPrivilegio().getDescripcion()); 
			}
			
			listPeriodos.add(periodo);
		}
		
		
		log.debug(funcName + " :: exit ");
		
		return listPeriodos;
	} 
	
	
	private List<LiqCuotaVO> getListCuotasPagas() throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		List<ConvenioCuota> listCuotasPagas = this.convenio.getListCuotasPagas(); 
		
		log.debug(funcName + " :: Cuotas Pagas: " +  listCuotasPagas.size()); 
		
		List<LiqCuotaVO> listCuotas = new ArrayList<LiqCuotaVO>();
		
		Long idViaDeuda = this.convenio.getViaDeuda().getId();
		
		UserContext userContext = DemodaUtil.currentUserContext();
		
		//Veo si la via es judicial
		Boolean esViaJudicial= false;
		if (idViaDeuda == ViaDeuda.ID_VIA_JUDICIAL)esViaJudicial=true;
		
		for(ConvenioCuota cta:listCuotasPagas){
			
			LiqCuotaVO cuotaPaga = new LiqCuotaVO();
			
			cuotaPaga.setIdCuota(cta.getId());
			
			String nroCuota = StringUtil.completarCerosIzq(cta.getNumeroCuota().toString(), 3);
			
			if (cta.getNumeroCuota().equals(1)&& cta.getSellado()!=null){
				nroCuota += "(*)";
			}
			
			cuotaPaga.setNroCuota(nroCuota);
			
			cuotaPaga.setCodRefPag(cta.getCodRefPag().toString());
			
			cuotaPaga.setFechaVto(DateUtil.formatDate( cta.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK));
			
			cuotaPaga.setCapital(cta.getCapitalCuota());
			
			cuotaPaga.setInteres(cta.getInteres());
			
			cuotaPaga.setActualizacion(cta.getActualizacion());
			
			cuotaPaga.setTotal(cta.calcularTotal()); // capitalCuota + interes + actualizacion
			
			cuotaPaga.setFechaPago(DateUtil.formatDate( cta.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
			
			String desEstadoCuota = cta.getEstadoConCuo().getDesEstadoConCuo();
			
			if (esViaJudicial && (userContext.getEsOperadorJudicial()) && cta.getIdLiqComPro()!=null){
				desEstadoCuota += " (Liquidada)";
			}
			
			cuotaPaga.setDesEstado(desEstadoCuota);
			
			if (cta.getBancoPago() != null)
				cuotaPaga.setCaja(Long.valueOf(cta.getBancoPago().getCodBanco()));
			
			cuotaPaga.setCodPago(cta.getCodPago());
			
			listCuotas.add(cuotaPaga);
		}
				
		log.debug(funcName + " :: exit ");
		
		return listCuotas;
	}
	
	/**
	 * 
	 * Devuelve la lista de las cuotas impagas del convenio.
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public List<LiqCuotaVO> getListCuotasImpagas() throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		List<ConvenioCuota> listCuotasImpagas = this.convenio.getListCuotasImpagas(); 
		
		log.debug(funcName + " :: Cuotas Impagas: " +  listCuotasImpagas.size()); 
		
		List<LiqCuotaVO> listCuotas = new ArrayList<LiqCuotaVO>();
		
		for(ConvenioCuota cta:listCuotasImpagas){
			
			LiqCuotaVO cuotaImpaga = new LiqCuotaVO();
			
			cuotaImpaga.setIdCuota(cta.getId());
			
			String nroCuota = StringUtil.completarCerosIzq(cta.getNumeroCuota().toString(), 3);
			
			if (cta.getNumeroCuota().equals(1)&& cta.getSellado()!=null){
				nroCuota += "(*)";
			}
			
			cuotaImpaga.setNroCuota(nroCuota);
			cuotaImpaga.setCodRefPag(cta.getCodRefPag().toString());
			cuotaImpaga.setFechaVto(DateUtil.formatDate( cta.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK));
			
			// Reclamada
			if (cta.getReclamada() != null && cta.getReclamada() == 1){
				
				cuotaImpaga.setObservacion(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_RECLAMADA));
				cuotaImpaga.setPoseeObservacion(true);
				cuotaImpaga.setEsReclamada(true);
			
				// Indeterminada
			} else if (IndeterminadoFacade.getInstance().getEsIndeterminada(cta)){
				
				cuotaImpaga.setObservacion(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_INDETERMINADA));
				cuotaImpaga.setPoseeObservacion(true);
				cuotaImpaga.setEsIndeterminada(true);
			} else {
				// Llamo a la clase que me calcula la actualizacion.
				DeudaAct deudaAct = cta.actualizacionImporteCuota();
				
				cuotaImpaga.setCapital(cta.getCapitalCuota());
				
				cuotaImpaga.setInteres(cta.getInteres());
				
				cta.setActualizacion(deudaAct.getRecargo());
				
				cuotaImpaga.setActualizacion(deudaAct.getRecargo());
				
				cuotaImpaga.setTotal(cta.calcularTotal()); // capitalCuota + interes + actualizacion(calculado)
				
			}
			
			listCuotas.add(cuotaImpaga);
		}
				
		log.debug(funcName + " :: exit ");
		
		return listCuotas;
	}

	/**
	 * Genera un LiqDeudaAdapter que contiene la cuenta y una lista de ConvenioCuentaAdapter, uno por cada convenio de la cuenta
	 * @return
	 * @throws Exception
	 */
	public LiqDeudaAdapter getLiqDeudaAdapterConConvenios() throws Exception {
		LiqDeudaAdapter lda = this.getLiqDeudaAdapter();
		List<Convenio> listConvenios = cuenta.getListConvenios();
		for(Convenio convenio: listConvenios){
			LiqDeudaBeanHelper ldaConvenio = new LiqDeudaBeanHelper(convenio);
			lda.getListConvenioCuentaAdapter().add(ldaConvenio.getLiqConvenioCuentaAdapter());			
		}
		return lda;
	}

	/**
	 * Genera un LiqDeudaAdapter que contiene la cuenta y una lista de ConvenioCuentaAdapter, uno por cada 
	 * convenio con estado VIGENTE, de la cuenta
	 * @return
	 * @throws Exception
	 */
	public LiqDeudaAdapter getLiqDeudaAdapterConConveniosVigentes() throws Exception {
		LiqDeudaAdapter lda = this.getLiqDeudaAdapter();
		List<Convenio> listConvenios = cuenta.getListConvenios();
		for(Convenio convenio: listConvenios){
			if(convenio.getEstadoConvenio().getId().longValue()==EstadoConvenio.ID_VIGENTE){
				LiqDeudaBeanHelper ldaConvenio = new LiqDeudaBeanHelper(convenio);
				lda.getListConvenioCuentaAdapter().add(ldaConvenio.getLiqConvenioCuentaAdapter());
			}
		}
		return lda;
	}
	

	/**
	 * 
	 * Obtiene los datos a mostrar en el CUS Anular/Desanular Deuda
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public LiqDeudaAdapter getByRecursoNroCuenta4AnularDeuda() throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				enter: " + funcName );
			log.debug( "##################################################################################################");		
		}

		LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter(); 

		liqDeudaAdapterVO.clearError();
		
		// Seteo de mensaje para procurador y bandera "PoseeDeduaProcurador"
		UserContext userContext = DemodaUtil.currentUserContext();
		
		// 1:: Traspaso de los datos a mostrar de la cuenta
		// 2:: Recupero los atributos del objeto imponible marcados como visible en consulta de deuda
		// 3:: Recuperacion de Titulares de la cuenta
		// 4:: Atributos de Contribuyente
		// 5:: Convenios Asociados
		// 6:: Desgloses y unificaciones
		liqDeudaAdapterVO.setCuenta(this.getCuenta());

		// 7:: Cuentas Relacionadas
		liqDeudaAdapterVO.setListCuentaRel(this.getCuentasRelacionadas());

		// 8:: Exenciones Vigentes - Caso si posee
		// 9:: Solicitudes de Exencion Denegadas - Caso si posee
		// 10:: Solicitudes de Exencion en tramite - Caso si posee
		liqDeudaAdapterVO.setExenciones(this.getExenciones());

		// 11:: Deuda en CyQ
		liqDeudaAdapterVO.setListProcedimientoCyQ(this.getDeudaCyQ());

		
		if (liqDeudaAdapterVO.getListProcedimientoCyQ() != null && 
				liqDeudaAdapterVO.getListProcedimientoCyQ().size() > 0){
			
			for (LiqDeudaCyQVO ldp:liqDeudaAdapterVO.getListProcedimientoCyQ()){
				if (ldp.poseeDeudaSeleccionable()){
					ldp.setMostrarChkAllCyQ(true);
				}
			}
		}
		
		// 12:: Deuda en via Judicial - Convenios si posee
		liqDeudaAdapterVO.setListProcurador(this.getDeudaProcurador());
		
		/* 
		 * Si Existe deuda en via judicial y el usuario es operador judicial
		 *   
		 * mostramos el chkAll 
		 */ 
		if (liqDeudaAdapterVO.getListProcurador() != null && 
				liqDeudaAdapterVO.getListProcurador().size() > 0){
			
			if (liqDeudaAdapterVO.isPoseeDeudaProcurador() ||
					userContext.getEsOperadorJudicial()){
				
				for (LiqDeudaProcuradorVO ldp:liqDeudaAdapterVO.getListProcurador()){
					if (ldp.poseeDeudaSeleccionable()){
						ldp.setMostrarChkAllJudicial(true);
					}
				}
			}
		} 
		
		
		// 13:: Deuda en Vias Administrativa - Estado de Analisis si posee
		liqDeudaAdapterVO.setListGestionDeudaAdmin(this.getDeudaAdmin());
		
		/*
		 * Si existe bloque deuda Admin y
		 * el usuario no es Procurador, Operador Judicial ni Escribano
		 * 
		 * y existe al menos un registro de deuda seleccionable
		 * 
		 */
		if (liqDeudaAdapterVO.getListGestionDeudaAdmin() != null &&
				liqDeudaAdapterVO.getListGestionDeudaAdmin().size() > 0) {
				
			if (!userContext.getEsProcurador() && 
					!userContext.getEsOperadorJudicial() &&
					!userContext.getEsEscribano()){
				
				for (LiqDeudaVO liqDeudaVO:liqDeudaAdapterVO.getListGestionDeudaAdmin().get(0).getListDeuda()) {					
					if (liqDeudaVO.getEsSeleccionable()){
						liqDeudaAdapterVO.setMostrarChkAllAdmin(true);
						break;
					}
				}
			}
		}

		// Recuperamos la deuda anulada
		liqDeudaAdapterVO.setListBlockDeudaAnulada(this.getDeudaAnulada());
		
		
		// 14:: Total de la deuda
		liqDeudaAdapterVO.setTotal(liqDeudaAdapterVO.calcularTotal());

		// 15:: Fecha Acentamiento
		Date fecUtlAseExito = cuenta.getRecurso().getFecUltPag();		
		liqDeudaAdapterVO.setFechaAcentamiento(DateUtil.formatDate(fecUtlAseExito, DateUtil.ddSMMSYYYY_MASK));
		
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				exit: " + funcName );
			log.debug( "##################################################################################################");		
		}
		return liqDeudaAdapterVO;
	}

	/**
	 * Obtiene la descripcion del titular principal, fijandose si el usuario esta logeado o no,
	 * Para retornar la descripcion correspondiente de Letra - CUIT o solo CUIT 
	 */
	private String getCuitTitularPrincipal() throws Exception {
		UserContext userContext = DemodaUtil.currentUserContext();
		String desCuit = "";
		if (userContext.getEsAnonimo()) {
			desCuit = cuenta.getCuitTitularPrincipalContr();
		} else {
			desCuit = cuenta.getCuitTitularPrincipal();
		}
		return desCuit; 
	}

	
	/**
	 * Obtiene la descripcion de la persona, fijandose si el usuario esta logeado o no,
	 * Para retornar la descripcion correspondiente de Letra - CUIT o solo CUIT mas la denominacio de la persona 
	 */
	private String getDesPersonaTitular(Persona persona) throws Exception {
		// determinamos la descripcion del cuit a utilizar.
		UserContext userContext = DemodaUtil.currentUserContext();
		String desPersona = "";
		if (userContext.getEsAnonimo()) {
			desPersona = persona.getCuitContr() + " " + persona.getRepresent();
		} else {
			desPersona = persona.getCuitFull() + " " + persona.getRepresent();
		}
		return desPersona;
	}
	
	/**
	 * Obtiene la descripcion de la persona formateada para mostrar al Contribuyente.
	 * Para retornar la descripcion correspondiente CUIT mas la denominacio de la persona 
	 */
	private String getDesPersonaTitularContr(Persona persona) throws Exception {
		String desPersona = persona.getCuitContr() + " " + persona.getRepresent();
		return desPersona;
	}

	
	/**
	 * Recupera toda la deuda Admiminstrativa incluida en el procedimiento con el cual es contruido este helper
	 * y ordenada por cuenta
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public List<LiqDeudaAdminVO> getDeudaAdminForCyqANTERIOR() throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		log.debug(funcName + " enter"); 
				
		List<LiqDeudaAdminVO> listBloqueDeudaAdmin = new ArrayList<LiqDeudaAdminVO>();

		// Obtenemos la deuda para el procedimiento y generamos un BloqueDeudaAdmin por cada cuenta.
		// Utilizamos para mostrar la actualizacionCyq
		
		List<DeudaAdmin> listDeudaAdmin = DeudaAdmin.getByProcedimientoCyq(this.procedimiento.getId());
		
		log.debug(funcName + " :: Deuda en Vias Administrativa para Cyq: " + listDeudaAdmin.size() + " Registros de deuda encontradas");

		if (listDeudaAdmin.size() > 0){
			// Bloque de un solo elemento de Gestion Deuda Admin
			LiqDeudaAdminVO liqDeudaAdminVO = new LiqDeudaAdminVO();
			
			Deuda deuAnt = listDeudaAdmin.get(0);
			
			// Seteamos el idCuenta de 
			Long idCuenta = listDeudaAdmin.get(0).getIdCuenta();
			
			// Por cada registro de deuda recuperado
			for(int i=0; i < listDeudaAdmin.size(); i++){ 
			
				DeudaAdmin deuda = listDeudaAdmin.get(i);
				
				log.debug(funcName + " i:" + i + " :: rec: " + deuda.getCuenta().getRecurso().getDesRecurso() + 
						" cta: " + deuda.getCuenta().getNumeroCuenta() +
						" da: " + deuda.getStrPeriodo());
				
				// Corte de control por cuenta
				if (deuda.getIdCuenta().longValue() != idCuenta.longValue()){
					// Si se trata del ulimo registro encontrado	
					liqDeudaAdminVO.setIdCuenta(deuAnt.getIdCuenta());
					liqDeudaAdminVO.setDesRecurso(deuAnt.getCuenta().getRecurso().getDesRecurso());
					liqDeudaAdminVO.setNumeroCuenta(deuAnt.getCuenta().getNumeroCuenta());
					
					listBloqueDeudaAdmin.add(liqDeudaAdminVO);	
					
					// Reset de valores.
					deuAnt = deuda;
					liqDeudaAdminVO = new LiqDeudaAdminVO();
					idCuenta = deuda.getIdCuenta(); 					
				}
				
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 
				
				double actualizCyQ = deuda.getActualizacionCyQ() == null ? 0 : deuda.getActualizacionCyQ().doubleValue();				
				double deudaTotal = deuda.getSaldo().doubleValue() + actualizCyQ;
				
				// Paso los datos de cada registro de deuda al LiqDeudaVO
				liqDeudaVO.setIdDeuda(deuda.getId());
				liqDeudaVO.setIdViaDeuda(deuda.getViaDeuda().getId());
				liqDeudaVO.setIdEstadoDeuda(deuda.getEstadoDeuda().getId());
				liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
				liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
				
				liqDeudaVO.setSaldo(deuda.getSaldo());
				liqDeudaVO.setActualizacion(actualizCyQ);
				liqDeudaVO.setTotal(deudaTotal);

				liqDeudaVO.setPoseeObservacion(false);
				liqDeudaVO.setEsSeleccionable(false);
				
				// Seteo de permisos
				liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(CyqSecurityConstants.MTD_QUITAR_DEUDA_ADMIN , null, null));

				liqDeudaAdminVO.getListDeuda().add(liqDeudaVO);	
				
				// ponchamos el ultimo elemento
				if (i == listDeudaAdmin.size() -1){
					liqDeudaAdminVO.setIdCuenta(deuda.getIdCuenta());
					liqDeudaAdminVO.setDesRecurso(deuda.getCuenta().getRecurso().getDesRecurso());
					liqDeudaAdminVO.setNumeroCuenta(deuda.getCuenta().getNumeroCuenta());
					listBloqueDeudaAdmin.add(liqDeudaAdminVO);						
				}
			}
		}
	
		log.debug(funcName + " exit");
		
		return listBloqueDeudaAdmin;
	}
	
	
	/**
	 * Recupera todos los datos de deuda Admiminstrativa incluida en el procedimiento con el cual es contruido este helper
	 * y ordenada por cuenta
	 * 
	 * @return List<LiqDeudaAdminVO>
	 * @throws Exception
	 */
	public List<LiqDeudaAdminVO> getDeudaAdminForCyq() throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		log.debug(funcName + " enter"); 
				
		List<LiqDeudaAdminVO> listBloqueDeudaAdmin = new ArrayList<LiqDeudaAdminVO>();

		// Obtenemos la deuda para el procedimiento y generamos un BloqueDeudaAdmin por cada cuenta.
		// Utilizamos para mostrar la actualizacionCyq
		
		//List<DeudaAdmin> listDeudaAdmin2 = DeudaAdmin.getByProcedimientoCyq(this.procedimiento);
		
		List<ProDet> listProDet = this.procedimiento.getListProDetDeudaAdmin();
		
		log.debug(funcName + " :: ProDet -> Deuda Administrativa para Cyq: " + listProDet.size() + " Registros de deuda encontradas");

		if (listProDet.size() > 0){
			// Bloque de un solo elemento de Gestion Deuda Admin
			LiqDeudaAdminVO liqDeudaAdminVO = new LiqDeudaAdminVO();
			
			ProDet proDetAnt = listProDet.get(0);
			
			// Seteamos el idCuenta de 
			Long idCuenta = listProDet.get(0).getCuenta().getId();
			
			// Por cada registro de deuda recuperado
			for(int i=0; i < listProDet.size(); i++){ 
			
				ProDet proDet = listProDet.get(i);
				
				log.debug(funcName + " i:" + i + " :: rec: " + proDet.getCuenta().getRecurso().getDesRecurso() + 
						" cta: " + proDet.getCuenta().getNumeroCuenta() +
						" da: " + proDet.getStrPeriodo());
				
				// Corte de control por cuenta
				if (proDet.getCuenta().getId().longValue() != idCuenta.longValue()){
					// Si se trata del ulimo registro encontrado	
					liqDeudaAdminVO.setIdCuenta(proDetAnt.getCuenta().getId());
					liqDeudaAdminVO.setDesRecurso(proDetAnt.getCuenta().getRecurso().getDesRecurso());
					liqDeudaAdminVO.setNumeroCuenta(proDetAnt.getCuenta().getNumeroCuenta());
					
					listBloqueDeudaAdmin.add(liqDeudaAdminVO);	
					
					// Reset de valores.
					proDetAnt = proDet;
					liqDeudaAdminVO = new LiqDeudaAdminVO();
					idCuenta = proDet.getCuenta().getId(); 					
				}
				
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 
				
				double actualizCyQ = proDet.getActualizacionCyq() == null ? 0 : proDet.getActualizacionCyq().doubleValue();				
				double deudaTotal = proDet.getSaldo().doubleValue() + actualizCyQ;
				
				// Paso los datos de cada registro de proDet al LiqDeudaVO
				liqDeudaVO.setIdDeuda(proDet.getIdDeuda());
				liqDeudaVO.setIdViaDeuda(proDet.getViaDeuda().getId());
				liqDeudaVO.setIdEstadoDeuda(proDet.getEstadoDeuda().getId());
				liqDeudaVO.setPeriodoDeuda(proDet.getStrPeriodo());
				liqDeudaVO.setCodRefPag(proDet.getCodRefPag().toString());
				liqDeudaVO.setFechaVencimiento(proDet.getFechaVencimiento());
				
				liqDeudaVO.setSaldo(proDet.getSaldo());
				liqDeudaVO.setActualizacion(actualizCyQ);
				liqDeudaVO.setTotal(deudaTotal);

				liqDeudaVO.setPoseeObservacion(false);
				liqDeudaVO.setEsSeleccionable(false);
				
				// Seteo de permisos
				liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(CyqSecurityConstants.MTD_QUITAR_DEUDA_ADMIN , null, null));

				liqDeudaAdminVO.getListDeuda().add(liqDeudaVO);	
				
				// ponchamos el ultimo elemento
				if (i == listProDet.size() -1){
					liqDeudaAdminVO.setIdCuenta(proDet.getCuenta().getId());
					liqDeudaAdminVO.setDesRecurso(proDet.getCuenta().getRecurso().getDesRecurso());
					liqDeudaAdminVO.setNumeroCuenta(proDet.getCuenta().getNumeroCuenta());
					listBloqueDeudaAdmin.add(liqDeudaAdminVO);						
				}
			}
		}
	
		log.debug(funcName + " exit");
		
		return listBloqueDeudaAdmin;
	}
	
	
	/**
	 * Recupera toda la deuda Judical  incluida en el procedimiento con el cual es contruido este helper
	 * y ordenada por cuenta y procurador
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public List<LiqDeudaProcuradorVO> getDeudaJudicialForCyqANTERIOR() throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		log.debug(funcName + " enter"); 
				
		List<LiqDeudaProcuradorVO> listBloqueDeudaJudicial = new ArrayList<LiqDeudaProcuradorVO>();

		// Obtenemos la deuda para el procedimiento y generamos un BloqueDeudaAdmin por cada cuenta.
		// Utilizamos para mostrar la actualizacionCyq
		
		List<DeudaJudicial> listDeudaJudicial = DeudaJudicial.getByProcedimientoCyq(this.procedimiento.getId());
		
		log.debug(funcName + " :: Deuda en Via Judicial para Cyq: " + listDeudaJudicial.size() + " Registros de deuda encontradas");
		
		
		if (listDeudaJudicial.size() > 0){
			// Bloque de un solo elemento de Gestion Deuda Admin
			LiqDeudaProcuradorVO liqDeudaProcuradorVO = new LiqDeudaProcuradorVO();
			
			Deuda deuAnt = listDeudaJudicial.get(0);
			
			// Seteamos el idCuenta de 
			Long idCuenta = listDeudaJudicial.get(0).getIdCuenta();
			String desProcurador = "";
			
			// Por cada registro de deuda recuperado
			for(int i=0; i < listDeudaJudicial.size(); i++){ 
			
				DeudaJudicial deuda = listDeudaJudicial.get(i);
				
				log.debug(funcName + " i:" + i + " :: rec: " + deuda.getCuenta().getRecurso().getDesRecurso() + 
						" cta: " + deuda.getCuenta().getNumeroCuenta() +
						" da: " + deuda.getStrPeriodo() +
						" pro: " + deuda.getProcurador().getDescripcion());
				
				// Corte de control por cuenta
				if (deuda.getIdCuenta().longValue() != idCuenta.longValue()){
					// Si se trata del ulimo registro encontrado	
					liqDeudaProcuradorVO.setIdCuenta(deuAnt.getIdCuenta());
					liqDeudaProcuradorVO.setDesRecurso(deuAnt.getCuenta().getRecurso().getDesRecurso());
					liqDeudaProcuradorVO.setNumeroCuenta(deuAnt.getCuenta().getNumeroCuenta());
					
					desProcurador = deuAnt.getProcurador().getDescripcion() + " - " + 
									deuAnt.getProcurador().getDomicilio() + " - " +
									deuAnt.getProcurador().getHorarioAtencion() + " - Tel: " +
									deuAnt.getProcurador().getTelefono();
					liqDeudaProcuradorVO.setIdProcurador(deuAnt.getIdProcurador());
					liqDeudaProcuradorVO.setDesProcurador(desProcurador);
					
					listBloqueDeudaJudicial.add(liqDeudaProcuradorVO);	
					
					// Reset de valores.
					deuAnt = deuda;
					liqDeudaProcuradorVO = new LiqDeudaProcuradorVO();
					idCuenta = deuda.getIdCuenta();
				}
				
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 
				
				double actualizCyQ = deuda.getActualizacionCyQ() == null ? 0 : deuda.getActualizacionCyQ().doubleValue();				
				double deudaTotal = deuda.getSaldo().doubleValue() + actualizCyQ;
				
				// Paso los datos de cada registro de deuda al LiqDeudaVO
				liqDeudaVO.setIdDeuda(deuda.getId());
				liqDeudaVO.setIdViaDeuda(deuda.getViaDeuda().getId());
				liqDeudaVO.setIdEstadoDeuda(deuda.getEstadoDeuda().getId());
				liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
				liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
				
				liqDeudaVO.setSaldo(deuda.getSaldo());
				liqDeudaVO.setActualizacion(actualizCyQ);
				liqDeudaVO.setTotal(deudaTotal);

				liqDeudaVO.setPoseeObservacion(false);
				liqDeudaVO.setEsSeleccionable(false);
				
				// Seteo de permisos
				liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(CyqSecurityConstants.MTD_QUITAR_DEUDA_JUDICIAL, null, null));

				liqDeudaProcuradorVO.getListDeuda().add(liqDeudaVO);	
				
				// ponchamos el ultimo elemento
				if (i == listDeudaJudicial.size() -1){
					liqDeudaProcuradorVO.setIdCuenta(deuda.getIdCuenta());
					liqDeudaProcuradorVO.setDesRecurso(deuda.getCuenta().getRecurso().getDesRecurso());
					liqDeudaProcuradorVO.setNumeroCuenta(deuda.getCuenta().getNumeroCuenta());
					
					desProcurador = deuda.getProcurador().getDescripcion() + " - " + 
									deuda.getProcurador().getDomicilio() + " - " +
									deuda.getProcurador().getHorarioAtencion() + " - Tel: " +
									deuda.getProcurador().getTelefono();
					
					liqDeudaProcuradorVO.setIdProcurador(deuda.getIdProcurador());
					liqDeudaProcuradorVO.setDesProcurador(desProcurador);
					
					listBloqueDeudaJudicial.add(liqDeudaProcuradorVO);						
				}
			}
		}
	
		log.debug(funcName + " exit");
		
		return listBloqueDeudaJudicial;
	}
	
	
	/**
	 * Recupera toda la deuda Judical  incluida en el procedimiento con el cual es contruido este helper
	 * y ordenada por cuenta y procurador
	 * 
	 * @return List<LiqDeudaProcuradorVO>
	 * @throws Exception
	 */
	public List<LiqDeudaProcuradorVO> getDeudaJudicialForCyq() throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		log.debug(funcName + " enter"); 
				
		List<LiqDeudaProcuradorVO> listBloqueDeudaJudicial = new ArrayList<LiqDeudaProcuradorVO>();

		// Obtenemos la deuda para el procedimiento y generamos un BloqueDeudaAdmin por cada cuenta.
		// Utilizamos para mostrar la actualizacionCyq
		
		//List<DeudaJudicial> listDeudaJudicial = DeudaJudicial.getByProcedimientoCyq(this.procedimiento);
		List<ProDet> listProDet = this.procedimiento.getListProDetDeudaJudicial();
		
		log.debug(funcName + " :: ProDet -> Deuda Judicial para Cyq: " + listProDet.size() + " Registros de deuda encontradas");
		
		
		if (listProDet.size() > 0){
			// Bloque de un solo elemento de Gestion Deuda Admin
			LiqDeudaProcuradorVO liqDeudaProcuradorVO = new LiqDeudaProcuradorVO();
			
			ProDet proDetAnt = listProDet.get(0);
			
			// Seteamos el idCuenta de 
			Long idCuenta = listProDet.get(0).getCuenta().getId();
			String desProcurador = "";
			
			// Por cada registro de deuda recuperado
			for(int i=0; i < listProDet.size(); i++){ 
			
				ProDet proDet = listProDet.get(i);
				
				log.debug(funcName + " i:" + i + " :: rec: " + proDet.getCuenta().getRecurso().getDesRecurso() + 
						" cta: " + proDet.getCuenta().getNumeroCuenta() +
						" da: " + proDet.getStrPeriodo() +
						" pro: " + proDet.getProcurador().getDescripcion());
				
				// Corte de control por cuenta
				if (proDet.getCuenta().getId().longValue() != idCuenta.longValue()){
					// Si se trata del ulimo registro encontrado	
					liqDeudaProcuradorVO.setIdCuenta(proDetAnt.getCuenta().getId());
					liqDeudaProcuradorVO.setDesRecurso(proDetAnt.getCuenta().getRecurso().getDesRecurso());
					liqDeudaProcuradorVO.setNumeroCuenta(proDetAnt.getCuenta().getNumeroCuenta());
					
					desProcurador = proDetAnt.getProcurador().getDescripcion() + " - " + 
									proDetAnt.getProcurador().getDomicilio() + " - " +
									proDetAnt.getProcurador().getHorarioAtencion() + " - Tel: " +
									proDetAnt.getProcurador().getTelefono();
					liqDeudaProcuradorVO.setIdProcurador(proDetAnt.getProcurador().getId());
					liqDeudaProcuradorVO.setDesProcurador(desProcurador);
					
					listBloqueDeudaJudicial.add(liqDeudaProcuradorVO);	
					
					// Reset de valores.
					proDetAnt = proDet;
					liqDeudaProcuradorVO = new LiqDeudaProcuradorVO();
					idCuenta = proDet.getCuenta().getId();
				}
				
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 
				
				double actualizCyQ = proDet.getActualizacionCyq() == null ? 0 : proDet.getActualizacionCyq().doubleValue();				
				double deudaTotal = proDet.getSaldo().doubleValue() + actualizCyQ;
				
				// Paso los datos de cada registro de proDet al LiqDeudaVO
				liqDeudaVO.setIdDeuda(proDet.getIdDeuda());
				liqDeudaVO.setIdViaDeuda(proDet.getViaDeuda().getId());
				liqDeudaVO.setIdEstadoDeuda(proDet.getEstadoDeuda().getId());
				liqDeudaVO.setPeriodoDeuda(proDet.getStrPeriodo());
				liqDeudaVO.setCodRefPag(proDet.getCodRefPag().toString());
				liqDeudaVO.setFechaVencimiento(proDet.getFechaVencimiento());
				
				liqDeudaVO.setSaldo(proDet.getSaldo());
				liqDeudaVO.setActualizacion(actualizCyQ);
				liqDeudaVO.setTotal(deudaTotal);

				liqDeudaVO.setPoseeObservacion(false);
				liqDeudaVO.setEsSeleccionable(false);
				
				// Seteo de permisos
				liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(CyqSecurityConstants.MTD_QUITAR_DEUDA_JUDICIAL, null, null));

				liqDeudaProcuradorVO.getListDeuda().add(liqDeudaVO);	
				
				// ponchamos el ultimo elemento
				if (i == listProDet.size() -1){
					liqDeudaProcuradorVO.setIdCuenta(proDet.getCuenta().getId());
					liqDeudaProcuradorVO.setDesRecurso(proDet.getCuenta().getRecurso().getDesRecurso());
					liqDeudaProcuradorVO.setNumeroCuenta(proDet.getCuenta().getNumeroCuenta());
					
					desProcurador = proDet.getProcurador().getDescripcion() + " - " + 
									proDet.getProcurador().getDomicilio() + " - " +
									proDet.getProcurador().getHorarioAtencion() + " - Tel: " +
									proDet.getProcurador().getTelefono();
					
					liqDeudaProcuradorVO.setIdProcurador(proDet.getProcurador().getId());
					liqDeudaProcuradorVO.setDesProcurador(desProcurador);
					
					listBloqueDeudaJudicial.add(liqDeudaProcuradorVO);						
				}
			}
		}
	
		log.debug(funcName + " exit");
		
		return listBloqueDeudaJudicial;
	}
	
	/**
	 * 
	 * ConstanciaDeudaAdapter para imprimir la constancia de deuda administrativa incluida en un Procedimiento cyq. 
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public ConstanciaDeudaAdapter getConstanciaDeudaAdminCyqANTERIOR() throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###			enter: " + funcName );
			log.debug( "##################################################################################################");		
		}
		
		ConstanciaDeudaAdapter constanciaDeudaAdapter = new ConstanciaDeudaAdapter();
		
		List<DeudaAdmin> listDeudaAdmin = DeudaAdmin.getByProcedimientoCyq(this.procedimiento.getId());
		
		log.debug(funcName + " :: Deuda en Vias Administrativa para Cyq: " + listDeudaAdmin.size() + " Registros de deuda encontradas");

		if (listDeudaAdmin.size() > 0){
			// Bloque de un solo elemento de Gestion Deuda Admin
			LiqDeudaAdminVO liqDeudaAdminVO = new LiqDeudaAdminVO();
			
			Deuda deuAnt = listDeudaAdmin.get(0);
			
			// Seteamos el idCuenta de 
			Long idCuenta = listDeudaAdmin.get(0).getIdCuenta();
			
			// Por cada registro de deuda recuperado
			for(int i=0; i < listDeudaAdmin.size(); i++){ 
			
				DeudaAdmin deuda = listDeudaAdmin.get(i);
				
				log.debug(funcName + " i:" + i + " :: rec: " + deuda.getCuenta().getRecurso().getDesRecurso() + 
						" cta: " + deuda.getCuenta().getNumeroCuenta() +
						" da: " + deuda.getStrPeriodo());
				
				// Corte de control por cuenta
				if (deuda.getIdCuenta().longValue() != idCuenta.longValue()){
					// Si se trata del ulimo registro encontrado
					
					liqDeudaAdminVO.setIdCuenta(deuAnt.getIdCuenta());
					liqDeudaAdminVO.setDesRecurso(deuAnt.getCuenta().getRecurso().getDesRecurso());
					liqDeudaAdminVO.setNumeroCuenta(deuAnt.getCuenta().getNumeroCuenta());
					
					LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter(); 
					LiqDeudaBeanHelper ldbh = new LiqDeudaBeanHelper(deuAnt.getCuenta());
					liqDeudaAdapterVO.setCuenta(ldbh.getCuenta());
					liqDeudaAdapterVO.getListGestionDeudaAdmin().add(liqDeudaAdminVO);	
					
					liqDeudaAdapterVO.setTotal(liqDeudaAdapterVO.calcularTotal());
					
					constanciaDeudaAdapter.getListLiqDeudaAdapter().add(liqDeudaAdapterVO);
					
					// Reset de valores.
					deuAnt = deuda;
					liqDeudaAdminVO = new LiqDeudaAdminVO();
					idCuenta = deuda.getIdCuenta(); 					
				}
				
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 
				
				double actualizCyQ = deuda.getActualizacionCyQ() == null ? 0 : deuda.getActualizacionCyQ().doubleValue();				
				double deudaTotal = deuda.getSaldo().doubleValue() + actualizCyQ;
				
				// Paso los datos de cada registro de deuda al LiqDeudaVO
				liqDeudaVO.setIdDeuda(deuda.getId());
				liqDeudaVO.setIdViaDeuda(deuda.getViaDeuda().getId());
				liqDeudaVO.setIdEstadoDeuda(deuda.getEstadoDeuda().getId());
				liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
				liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
				
				liqDeudaVO.setSaldo(deuda.getSaldo());
				liqDeudaVO.setActualizacion(actualizCyQ);
				liqDeudaVO.setTotal(deudaTotal);

				liqDeudaVO.setPoseeObservacion(false);
				liqDeudaVO.setEsSeleccionable(false);
				
				// Seteo de permisos
				liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(CyqSecurityConstants.MTD_QUITAR_DEUDA_ADMIN , null, null));

				liqDeudaAdminVO.getListDeuda().add(liqDeudaVO);	
				
				// ponchamos el ultimo elemento
				if (i == listDeudaAdmin.size() -1){
					liqDeudaAdminVO.setIdCuenta(deuda.getIdCuenta());
					liqDeudaAdminVO.setDesRecurso(deuda.getCuenta().getRecurso().getDesRecurso());
					liqDeudaAdminVO.setNumeroCuenta(deuda.getCuenta().getNumeroCuenta());
					
					LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter(); 
					LiqDeudaBeanHelper ldbh = new LiqDeudaBeanHelper(deuda.getCuenta());
					liqDeudaAdapterVO.setCuenta(ldbh.getCuenta());
					liqDeudaAdapterVO.getListGestionDeudaAdmin().add(liqDeudaAdminVO);
					
					liqDeudaAdapterVO.setTotal(liqDeudaAdapterVO.calcularTotal());
					
					constanciaDeudaAdapter.getListLiqDeudaAdapter().add(liqDeudaAdapterVO);
				}
			}
		}
				
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				exit: " + funcName );
			log.debug( "##################################################################################################");		
		}
		
		return constanciaDeudaAdapter;
	}
	
	
	/**
	 * 
	 * ConstanciaDeudaAdapter para imprimir la constancia de deuda administrativa incluida en un Procedimiento cyq. 
	 * 
	 * @return
	 * @throws Exception
	 */
	public ConstanciaDeudaAdapter getConstanciaDeudaAdminCyq(Map<String, Boolean> mapDeudaSeleccionada) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###			enter: " + funcName );
			log.debug( "##################################################################################################");		
		}
		
		ConstanciaDeudaAdapter constanciaDeudaAdapter = new ConstanciaDeudaAdapter();
		
		//List<DeudaAdmin> listDeudaAdmin = DeudaAdmin.getByProcedimientoCyq(this.procedimiento);
		List<ProDet> listProDet = this.procedimiento.getListProDetDeudaAdmin();
		
		List<ProDet> listProDetFiltrada = new ArrayList<ProDet>();
		for(int i=0; i < listProDet.size(); i++){
			ProDet proDet = listProDet.get(i);
			if(mapDeudaSeleccionada != null && !mapDeudaSeleccionada.containsKey(proDet.getIdDeuda().toString())){
				log.debug(funcName + " i:" + i + " idDeuda:"+ proDet.getIdDeuda().toString() + ", se ignora para la impresion por no estar seleccionada. ");
			}else{
				listProDetFiltrada.add(proDet);
			}
		}

		log.debug(funcName + " :: ProDet -> Deuda Administrativa para Cyq: " + listProDetFiltrada.size() + " Registros de deuda encontradas (o seleccionados)");

		if (listProDetFiltrada.size() > 0){
			// Bloque de un solo elemento de Gestion Deuda Admin
			LiqDeudaAdminVO liqDeudaAdminVO = new LiqDeudaAdminVO();
			
			ProDet proDetAnt = listProDetFiltrada.get(0);
			
			// Seteamos el idCuenta de 
			Long idCuenta = listProDetFiltrada.get(0).getCuenta().getId();
						
			// Por cada registro de deuda recuperado
			for(int i=0; i < listProDetFiltrada.size(); i++){ 
			
				ProDet proDet = listProDetFiltrada.get(i);
				
				log.debug(funcName + " i:" + i + " :: rec: " + proDet.getCuenta().getRecurso().getDesRecurso() + 
						" cta: " + proDet.getCuenta().getNumeroCuenta() +
						" da: " + proDet.getStrPeriodo());
				
				// Corte de control por cuenta
				if (proDet.getCuenta().getId().longValue() != idCuenta.longValue()){
					// Si se trata del ulimo registro encontrado
					
					liqDeudaAdminVO.setIdCuenta(proDetAnt.getCuenta().getId());
					liqDeudaAdminVO.setDesRecurso(proDetAnt.getCuenta().getRecurso().getDesRecurso());
					liqDeudaAdminVO.setNumeroCuenta(proDetAnt.getCuenta().getNumeroCuenta());
					
					LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter(); 
					LiqDeudaBeanHelper ldbh = new LiqDeudaBeanHelper(proDetAnt.getCuenta());
					liqDeudaAdapterVO.setCuenta(ldbh.getCuenta());
					liqDeudaAdapterVO.getListGestionDeudaAdmin().add(liqDeudaAdminVO);	
					
					liqDeudaAdapterVO.setTotal(liqDeudaAdapterVO.calcularTotal());
					
					constanciaDeudaAdapter.getListLiqDeudaAdapter().add(liqDeudaAdapterVO);
					
					// Reset de valores.
					proDetAnt = proDet;
					liqDeudaAdminVO = new LiqDeudaAdminVO();
					idCuenta = proDet.getCuenta().getId();
				}
				
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 
				
				double actualizCyQ = proDet.getActualizacionCyq() == null ? 0 : proDet.getActualizacionCyq().doubleValue();				
				double deudaTotal = proDet.getSaldo().doubleValue() + actualizCyQ;
				
				// Paso los datos de cada registro de deuda al LiqDeudaVO
				liqDeudaVO.setIdDeuda(proDet.getId());
				liqDeudaVO.setIdViaDeuda(proDet.getViaDeuda().getId());
				liqDeudaVO.setIdEstadoDeuda(proDet.getEstadoDeuda().getId());
				liqDeudaVO.setPeriodoDeuda(proDet.getStrPeriodo());
				liqDeudaVO.setCodRefPag(proDet.getCodRefPag().toString());
				liqDeudaVO.setFechaVencimiento(proDet.getFechaVencimiento());
				
				liqDeudaVO.setImporte(proDet.getImporte());
				liqDeudaVO.setSaldo(proDet.getSaldo());
				liqDeudaVO.setActualizacion(actualizCyQ);
				liqDeudaVO.setTotal(deudaTotal);

				liqDeudaVO.setPoseeObservacion(false);
				liqDeudaVO.setEsSeleccionable(false);
				
				// Seteo de permisos
				liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(CyqSecurityConstants.MTD_QUITAR_DEUDA_ADMIN , null, null));

				liqDeudaAdminVO.getListDeuda().add(liqDeudaVO);	
				
				// ponchamos el ultimo elemento
				if (i == listProDetFiltrada.size() -1){
					liqDeudaAdminVO.setIdCuenta(proDet.getCuenta().getId());
					liqDeudaAdminVO.setDesRecurso(proDet.getCuenta().getRecurso().getDesRecurso());
					liqDeudaAdminVO.setNumeroCuenta(proDet.getCuenta().getNumeroCuenta());
					
					LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter(); 
					LiqDeudaBeanHelper ldbh = new LiqDeudaBeanHelper(proDet.getCuenta());
					liqDeudaAdapterVO.setCuenta(ldbh.getCuenta());
					liqDeudaAdapterVO.getListGestionDeudaAdmin().add(liqDeudaAdminVO);
					
					liqDeudaAdapterVO.setTotal(liqDeudaAdapterVO.calcularTotal());
					
					constanciaDeudaAdapter.getListLiqDeudaAdapter().add(liqDeudaAdapterVO);
				}
			}
		}
				
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				exit: " + funcName );
			log.debug( "##################################################################################################");		
		}
		
		return constanciaDeudaAdapter;
	}
	
	/**
	 * 
	 * ConstanciaDeudaAdapter para imprimir la constancia de deuda judicial incluida en un Procedimiento cyq.
	 * 
	 * @return
	 * @throws Exception
	 */
	public ConstanciaDeudaAdapter getConstanciaDeudaJudicialCyq(Map<String, Boolean> mapDeudaSeleccionada) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###			enter: " + funcName );
			log.debug( "##################################################################################################");		
		}
		
		ConstanciaDeudaAdapter constanciaDeudaAdapter = new ConstanciaDeudaAdapter();
		
		//List<DeudaJudicial> listDeudaJudicial = DeudaJudicial.getByProcedimientoCyq(this.procedimiento.getId());
		List<ProDet> listProDet = this.procedimiento.getListProDetDeudaJudicial();
		
		log.debug(funcName + " :: Deuda en Via Judicial para Cyq: " + listProDet.size() + " Registros de deuda encontradas");
				
		if (listProDet.size() > 0){
			// Bloque de un solo elemento de Gestion Deuda Admin
			LiqDeudaProcuradorVO liqDeudaProcuradorVO = new LiqDeudaProcuradorVO();
			
			ProDet proDetAnt = listProDet.get(0);
			
			// Seteamos el idCuenta de 
			Long idCuenta = listProDet.get(0).getCuenta().getId();
			String desProcurador = "";
			
			List<ProDet> listProDetFiltrada = new ArrayList<ProDet>();
			for(int i=0; i < listProDet.size(); i++){
				ProDet proDet = listProDet.get(i);
				if(mapDeudaSeleccionada != null && !mapDeudaSeleccionada.containsKey(proDet.getIdDeuda().toString())){
					log.debug(funcName + " i:" + i + " idDeuda:"+ proDet.getIdDeuda().toString() + ", se ignora para la impresion por no estar seleccionada. ");
				}else{
					listProDetFiltrada.add(proDet);
				}
			}
			
			// Por cada registro de deuda recuperado
			for(int i=0; i < listProDetFiltrada.size(); i++){ 
			
				ProDet proDet = listProDetFiltrada.get(i);
			
				log.debug(funcName + " i:" + i + " :: rec: " + proDet.getCuenta().getRecurso().getDesRecurso() + 
						" cta: " + proDet.getCuenta().getNumeroCuenta() +
						" da: " + proDet.getStrPeriodo() +
						" pro: " + proDet.getProcurador().getDescripcion());
				
				// Corte de control por cuenta
				if (proDet.getCuenta().getId().longValue() != idCuenta.longValue()){
					// Si se trata del ulimo registro encontrado
					
					liqDeudaProcuradorVO.setIdCuenta(proDetAnt.getCuenta().getId());
					liqDeudaProcuradorVO.setDesRecurso(proDetAnt.getCuenta().getRecurso().getDesRecurso());
					liqDeudaProcuradorVO.setNumeroCuenta(proDetAnt.getCuenta().getNumeroCuenta());
					
					desProcurador = proDetAnt.getProcurador().getDescripcion() + " - " + 
									proDetAnt.getProcurador().getDomicilio() + " - " +
									proDetAnt.getProcurador().getHorarioAtencion() + " - Tel: " +
									proDetAnt.getProcurador().getTelefono();
					liqDeudaProcuradorVO.setIdProcurador(proDetAnt.getProcurador().getId());
					liqDeudaProcuradorVO.setDesProcurador(desProcurador);
					
					LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter(); 
					LiqDeudaBeanHelper ldbh = new LiqDeudaBeanHelper(proDetAnt.getCuenta());
					liqDeudaAdapterVO.setCuenta(ldbh.getCuenta());
					liqDeudaAdapterVO.getListProcurador().add(liqDeudaProcuradorVO);
					
					liqDeudaAdapterVO.setTotal(liqDeudaAdapterVO.calcularTotal());
					
					constanciaDeudaAdapter.getListLiqDeudaAdapter().add(liqDeudaAdapterVO);
					
					// Reset de valores.
					proDetAnt = proDet;
					liqDeudaProcuradorVO = new LiqDeudaProcuradorVO();
					idCuenta = proDet.getCuenta().getId();
				}
				
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 
				
				double actualizCyQ = proDet.getActualizacionCyq() == null ? 0 : proDet.getActualizacionCyq().doubleValue();				
				double deudaTotal = proDet.getSaldo().doubleValue() + actualizCyQ;
				
				// Paso los datos de cada registro de deuda al LiqDeudaVO
				liqDeudaVO.setIdDeuda(proDet.getId());
				liqDeudaVO.setIdViaDeuda(proDet.getViaDeuda().getId());
				liqDeudaVO.setIdEstadoDeuda(proDet.getEstadoDeuda().getId());
				liqDeudaVO.setPeriodoDeuda(proDet.getStrPeriodo());
				liqDeudaVO.setCodRefPag(proDet.getCodRefPag().toString());
				liqDeudaVO.setFechaVencimiento(proDet.getFechaVencimiento());
				
				liqDeudaVO.setImporte(proDet.getImporte());
				liqDeudaVO.setSaldo(proDet.getSaldo());
				liqDeudaVO.setActualizacion(actualizCyQ);
				liqDeudaVO.setTotal(deudaTotal);

				liqDeudaVO.setPoseeObservacion(false);
				liqDeudaVO.setEsSeleccionable(false);
				
				// Seteo de permisos
				liqDeudaVO.setEsSeleccionable(this.getPermisoRegDeuda(CyqSecurityConstants.MTD_QUITAR_DEUDA_JUDICIAL, null, null));

				liqDeudaProcuradorVO.getListDeuda().add(liqDeudaVO);	
				
				// ponchamos el ultimo elemento
				if (i == listProDetFiltrada.size() -1){
					liqDeudaProcuradorVO.setIdCuenta(proDet.getCuenta().getId());
					liqDeudaProcuradorVO.setDesRecurso(proDet.getCuenta().getRecurso().getDesRecurso());
					liqDeudaProcuradorVO.setNumeroCuenta(proDet.getCuenta().getNumeroCuenta());
					
					desProcurador = proDet.getProcurador().getDescripcion() + " - " + 
									proDet.getProcurador().getDomicilio() + " - " +
									proDet.getProcurador().getHorarioAtencion() + " - Tel: " +
									proDet.getProcurador().getTelefono();
					
					liqDeudaProcuradorVO.setIdProcurador(proDet.getProcurador().getId());
					liqDeudaProcuradorVO.setDesProcurador(desProcurador);
					
					LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter(); 
					LiqDeudaBeanHelper ldbh = new LiqDeudaBeanHelper(proDet.getCuenta());
					liqDeudaAdapterVO.setCuenta(ldbh.getCuenta());
					liqDeudaAdapterVO.getListProcurador().add(liqDeudaProcuradorVO);
					
					liqDeudaAdapterVO.setTotal(liqDeudaAdapterVO.calcularTotal());
					
					constanciaDeudaAdapter.getListLiqDeudaAdapter().add(liqDeudaAdapterVO);
				}
			}
		}
				
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				exit: " + funcName );
			log.debug( "##################################################################################################");		
		}
		
		return constanciaDeudaAdapter;
	}

	
	/**
	 * Obtiene un LiqDeudaVO con los datos de la deuda de id pasado con los datos para visualizacion.
	 */
	public static LiqDeudaVO getLiqDeudaByIdDeuda(Long idDeuda) throws Exception {

		Deuda deuda = Deuda.getById(idDeuda);
		if(deuda == null)
			return null;
		
		LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 
		
		// Paso los datos del registro de deuda al LiqDeudaVO
		liqDeudaVO.setIdDeuda(deuda.getId());
		liqDeudaVO.setIdViaDeuda(deuda.getViaDeuda().getId());
		liqDeudaVO.setIdEstadoDeuda(deuda.getEstadoDeuda().getId());
		liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
		liqDeudaVO.setCodRefPag(deuda.getCodRefPag().toString());
		liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
		liqDeudaVO.setDesRecurso(deuda.getCuenta().getRecurso().getDesRecurso());
		liqDeudaVO.setNroCuenta(deuda.getCuenta().getNumeroCuenta());
		
		//calculo actualizacion deuda
		DeudaAct deudaAct = deuda.actualizacionSaldo(new Date());
		liqDeudaVO.setImporte(deuda.getImporte());
		liqDeudaVO.setSaldo(deuda.getSaldo());
		liqDeudaVO.setActualizacion(deudaAct.getRecargo());
		liqDeudaVO.setTotal(deudaAct.getImporteAct());

		return liqDeudaVO;
	}

	
	public List<LiqDeudaAdminVO> getDeudaAdminForCierreComercio() throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		// obtener la lista de cuentas relacionadas por objeto imponible, por cada cuenta obtener la deudaAdmin
		// cumpliendo las condiciones de la especificacion
		
		
		log.debug(funcName + " enter"); 
				
		List<LiqDeudaAdminVO> listBloqueDeudaAdmin = new ArrayList<LiqDeudaAdminVO>();

		List<Cuenta> listCuenta = this.cuenta.getListCuentaRelacionadas();
		
		listCuenta.add(this.cuenta);
		for(Cuenta cuenta: listCuenta){
			
			List<DeudaAdmin> listDeudaAdmin = cuenta.getDeudaForCierreComercio();// devuelve al lusta de deuda con lo pedido
			
			log.debug(funcName + " :: Deuda en Vias Administrativa para Cierre Comercio: " + listDeudaAdmin.size() + " Registros de deuda encontradas");

			LiqDeudaAdminVO liqDeudaAdminVO = new LiqDeudaAdminVO();
			liqDeudaAdminVO.setIdCuenta(cuenta.getId());
			liqDeudaAdminVO.setDesRecurso(cuenta.getRecurso().getDesRecurso());
			liqDeudaAdminVO.setNumeroCuenta(cuenta.getNumeroCuenta());
			
			// Por cada registro de deuda recuperado
			for(int i=0; i < listDeudaAdmin.size(); i++){ 
			
				DeudaAdmin deuda = listDeudaAdmin.get(i);
				
				log.debug(funcName + " i:" + i + " :: rec: " + deuda.getCuenta().getRecurso().getDesRecurso() + 
						" cta: " + deuda.getCuenta().getNumeroCuenta() +
						" da: " + deuda.getStrPeriodo());
				
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 
				
				// Paso los datos de cada registro de deuda al LiqDeudaVO
				liqDeudaVO.setIdDeuda(deuda.getId());
				liqDeudaVO.setIdViaDeuda(deuda.getViaDeuda().getId());
				liqDeudaVO.setIdEstadoDeuda(deuda.getEstadoDeuda().getId());
				liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
				liqDeudaVO.setCodRefPag(deuda.getCodRefPag().toString());
				liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
				
				if (deuda.getEsExcentaPago()){
					liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setEsSeleccionable(false);
					liqDeudaVO.setEsExentoPago(true);
					
					liqDeudaVO.setObservacion(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_EXENTA));
					
				} else if (deuda.getEsConvenio()){

					liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setPoseeConvenio(true);
					liqDeudaVO.setEsSeleccionable(false);
					
					String obsConvenio = deuda.getConvenio().getNroConvenio() + " - " + deuda.getConvenio().getPlan().getDesPlan();
					
					liqDeudaVO.setObservacion(obsConvenio);
					liqDeudaVO.setIdLink(deuda.getIdConvenio());

				} else if (deuda.getEsIndeterminada()){
					liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setEsSeleccionable(false);
					liqDeudaVO.setEsIndeterminada(true);
					liqDeudaVO.setObservacion(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_INDETERMINADA));

				} else if (deuda.getEsReclamada()){
					liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setEsSeleccionable(false);
					liqDeudaVO.setEsReclamada(true);
					liqDeudaVO.setObservacion(SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_RECLAMADA));

				} else if (deuda.getEstaEnAsentamiento() ){
					liqDeudaVO.setPoseeObservacion(true);
					liqDeudaVO.setEsSeleccionable(false);
					liqDeudaVO.setEsReclamada(true);
					liqDeudaVO.setObservacion("En proceso de Asentamiento");

				} else {
					//calculo actualizacion deuda
					DeudaAct deudaAct = deuda.actualizacionSaldo(fechaActualizacion);
					liqDeudaVO.setImporte(deuda.getImporte());
					liqDeudaVO.setSaldo(deuda.getSaldo());
					liqDeudaVO.setActualizacion(deudaAct.getRecargo());
					liqDeudaVO.setTotal(deudaAct.getImporteAct());
				}

				log.debug(funcName + " 13:: DeudaAdmin: " +
						" periodo=" + liqDeudaVO.getPeriodoDeuda() +
						" fVto=" + liqDeudaVO.getFechaVto() +
						" ipte=" + liqDeudaVO.getImporte() +
						" saldo=" + liqDeudaVO.getSaldo() +
						" actualiz=" + liqDeudaVO.getActualizacion() +
						" total=" + liqDeudaVO.getTotal() +
						" obs.=" + liqDeudaVO.getObservacion());
				
			    liqDeudaAdminVO.getListDeuda().add(liqDeudaVO);	
			}

			listBloqueDeudaAdmin.add(liqDeudaAdminVO);
		}

		log.debug(funcName + " exit");
		
		return listBloqueDeudaAdmin;
	}

	/**
    * Realiza un "toVO" de la deuda recibida.
    * 
    * fechaAct si viene nula, no se actualiza la deuda y se muestran los datos directo de la DB.
    * y si es no nula, se utiliza para actualizar la fecha.  
    * 
    * @param deuda
    * @param fechaAct
    * @return LiqDeudaVO
    * @throws Exception
    */
   public static LiqDeudaVO deudaToLiqDeudaVO(Deuda deuda, Date fechaAct) throws Exception{
           
           if(deuda == null)
                   return null;
           
           LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 
           
           // Paso los datos del registro de deuda al LiqDeudaVO
           liqDeudaVO.setIdDeuda(deuda.getId());
           liqDeudaVO.setIdViaDeuda(deuda.getViaDeuda().getId());
           liqDeudaVO.setIdEstadoDeuda(deuda.getEstadoDeuda().getId());
           liqDeudaVO.setDesViaDeuda(deuda.getViaDeuda().getDesViaDeuda());
           liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
           liqDeudaVO.setCodRefPag(deuda.getCodRefPag()==null?"":deuda.getCodRefPag().toString());
           liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
           liqDeudaVO.setDesRecurso(deuda.getCuenta().getRecurso().getDesRecurso());
           liqDeudaVO.setNroCuenta(deuda.getCuenta().getNumeroCuenta());
           
           //calculo actualizacion deuda
           if (fechaAct != null) { 
               DeudaAct deudaAct = deuda.actualizacionSaldo(fechaAct);
               liqDeudaVO.setActualizacion(deudaAct.getRecargo());
               liqDeudaVO.setTotal(deudaAct.getImporteAct());
           } else {
               liqDeudaVO.setActualizacion(deuda.getActualizacion());
               liqDeudaVO.setTotal(deuda.getImporte());
           }

           liqDeudaVO.setImporte(deuda.getImporte());
           liqDeudaVO.setSaldo(deuda.getSaldo());

           return liqDeudaVO;
           
   }

   
   /**
    * Devuelve un liqDeudaAdapter con los periodos de deuda de la cuenta, permitiendo seleccionar periodos que se encuentren dentro
    * del rango de la exencion seleccionada y no esten en Convenio, Indeterminada ni Reclamada.
    * 
    * 
    * @param liqDeudaAdapterVO
    * @param fechaDesde
    * @param fechaHasta
    * @return
    * @throws Exception
    */
   public LiqDeudaAdapter getLiqDeudaAdapter4DeudaExencion(LiqDeudaAdapter liqDeudaAdapterVO, Date fechaDesde, Date fechaHasta) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				enter: " + funcName );
			log.debug( "##################################################################################################");		
		}

		liqDeudaAdapterVO.clearError();

		// 1:: Traspaso de los datos a mostrar de la cuenta
		// 2:: Recupero los atributos del objeto imponible marcados como visible en consulta de deuda
		// 3:: Recuperacion de Titulares de la cuenta
		// 4:: Atributos de Contribuyente
		// 5:: Convenios Asociados
		// 6:: Desgloses y unificaciones
		liqDeudaAdapterVO.setCuenta(this.getCuenta());

		// 8:: Exenciones Vigentes - Caso si posee
		// 9:: Solicitudes de Exencion Denegadas - Caso si posee
		// 10:: Solicitudes de Exencion en tramite - Caso si posee
		liqDeudaAdapterVO.setExenciones(this.getExenciones());

		// 12:: Deuda en via Judicial - Convenios si posee
		liqDeudaAdapterVO.setListProcurador(this.getDeudaProcurador());

		// 13:: Deuda en Vias Administrativa - Estado de Analisis si posee
		List<LiqDeudaAdminVO> listBloqueDeudaAdmin = this.getDeudaAdmin();

		// Deshabilitamos la deuda posterior a la fechaAuto del Procedimiento
		if (listBloqueDeudaAdmin.size() > 0){
			LiqDeudaAdminVO bloqueDeudaAdmin = listBloqueDeudaAdmin.get(0);
			
			for (LiqDeudaVO liqDeudaVO:bloqueDeudaAdmin.getListDeuda()){
				if (DateUtil.isDateInRange(liqDeudaVO.getFechaVencimiento(), fechaDesde, fechaHasta)) {				
					log.debug(funcName  +  " ### periodo " + liqDeudaVO.getPeriodoDeuda() + " seleccionable");
					if (liqDeudaVO.getPoseeConvenio() || liqDeudaVO.isEsIndeterminada() || liqDeudaVO.isEsReclamada()){
						liqDeudaVO.setEsSeleccionable(false);
					} else {
						liqDeudaVO.setEsSeleccionable(true);
					}
				} else {
					liqDeudaVO.setEsSeleccionable(false);
				}
			}
		}
		
		liqDeudaAdapterVO.setListGestionDeudaAdmin(listBloqueDeudaAdmin);
		
		// 14:: Total de la deuda
		liqDeudaAdapterVO.setTotal(liqDeudaAdapterVO.calcularTotal());

		// 15:: Fecha Acentamiento
		Date fecUtlAseExito = cuenta.getRecurso().getFecUltPag();		
		liqDeudaAdapterVO.setFechaAcentamiento(DateUtil.formatDate(fecUtlAseExito, DateUtil.ddSMMSYYYY_MASK));
	
		if (log.isDebugEnabled()){
			log.debug( "##################################################################################################");
			log.debug( "###				exit: " + funcName );
			log.debug( "##################################################################################################");		
		}
		return liqDeudaAdapterVO;

	}

   /**
    * Retorna una lista con los atributos valorizados al momento de la emision
    */
   public static List<LiqAtrValorVO> getListAtributosEmision(Deuda deuda) throws Exception {
	   String funcName = DemodaUtil.currentMethodName();
	   log.debug(funcName + ": enter");
	   
	   List<LiqAtrValorVO> listLiqAtrEmi    = new ArrayList<LiqAtrValorVO>();
	   /*for (GenericAtrDefinition genAtrDef: deuda.getAtributosEmisionDefValue()) {
			LiqAtrValorVO liqAtrValorVO = new LiqAtrValorVO();
			liqAtrValorVO.setKey(genAtrDef.getAtributo().getCodAtributo());
			liqAtrValorVO.setLabel(genAtrDef.getAtributo().getDesAtributo());
			
			if (genAtrDef.getPoseeDominio()) {
				liqAtrValorVO.setValue(genAtrDef.getValorFromDominioView());
			} else {
				liqAtrValorVO.setValue(genAtrDef.getValorView());
			}

			log.debug(funcName +   " key:"  + liqAtrValorVO.getKey() 
							   + " label: " + liqAtrValorVO.getLabel() 
							   + " value:"  + liqAtrValorVO.getValue());			

			listLiqAtrEmi.add(liqAtrValorVO);
	   }*/

	   log.debug(funcName + ": exit");
		return listLiqAtrEmi;
   }

}