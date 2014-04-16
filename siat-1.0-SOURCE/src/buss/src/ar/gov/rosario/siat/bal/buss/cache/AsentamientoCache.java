//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.bean.AsentamientoSession;
import ar.gov.rosario.siat.bal.buss.bean.DisPar;
import ar.gov.rosario.siat.bal.buss.bean.DisParPla;
import ar.gov.rosario.siat.bal.buss.bean.DisParRec;
import ar.gov.rosario.siat.bal.buss.bean.ParSel;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.buss.bean.TipoImporte;
import ar.gov.rosario.siat.bal.buss.bean.Tolerancia;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.SerBanRec;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.DesGen;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanExe;
import ar.gov.rosario.siat.gde.buss.bean.PlanMotCad;
import ar.gov.rosario.siat.gde.buss.bean.Rescate;
import ar.gov.rosario.siat.gde.buss.bean.SerBanDesGen;
import ar.gov.rosario.siat.gde.buss.bean.TipoPago;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * 
 * Contiene las listas que requiere el asentamiento. Es un Singleton que podr'ia
 * compartirse por varios procesos de asentmaiento, cada uno correspondiente a
 * un servicioBanco distinto, e incluso podr'ia copartirse por m'ultiples hilos
 * de un mismo proceso, en caso que optemos por una soluci'on de procesamiento
 * paralelo. Por este motivo, en la inicializaci'on de las listas se tiene mucho
 * cuidado de: para las listas generales, no limpiarleas; y de las listas
 * especificas para un servicioBanco (o Asentamiento), de no eliminar ni
 * modificar nada que no corresponda a un asentamiento.
 * 
 * 
 * Al comienzo del mismo, se debe inicializar para un ServicioBanco. En ese
 * momento, se levantan las listas y quedan en un Cache. Estas listas se
 * implementan en mapas. Tambi'en se provee la misma sem'antica que se utilizaba
 * originalmente en el Asentamiento.
 * 
 * Existen: listas generales (ViaDeuda, Tolerancias, etc), listas que se
 * inicializan para un ServicioBanco (Recursos, DisPar, etc) y listas que se
 * arman durante el asentmaiento (AuxRecaudado, AuxPagos)
 * 
 * @author tecso
 * 
 */

public class AsentamientoCache {

	private static Logger log = Logger.getLogger(AsentamientoCache.class);

	private static AsentamientoCache INSTANCE = null;

	private Exception cacheException = null; // indica si el cache posee
												// algun error que lo hace
												// invalido.

	/* genericos */
	Map<String, Sistema> mapSistema = new HashMap<String, Sistema>();

	// vias deuda para todos
	Map<String, ViaDeuda> mapViaDeuda = new HashMap<String, ViaDeuda>();

	// vias estado deuda
	Map<String, EstadoDeuda> mapEstadoDeuda = new HashMap<String, EstadoDeuda>();

	// Bancos (con el Codigo como Clave)
	Map<String, Banco> mapBanco = new HashMap<String, Banco>();
	
	// Tipo Pago
	Map<String, TipoPago> mapTipoPago = new HashMap<String, TipoPago>();


	/* SERVICIO BANCO */

	// tolerancia para el servicio banco
	Map<String, Tolerancia> mapTolerancia = new HashMap<String, Tolerancia>();

	// recursos para el servicio banco: key=idServicioBanco-idRecurso
	Map<String, Recurso> mapRecurso = new HashMap<String, Recurso>();

	// listas para DisPar
	List<DisParRec> listDisParRec;

	List<DisParPla> listDisParPla;

	// Lista de DisPar Genericos por Recurso para Tipo Importe Indeterminado.
	List<DisPar> listDisParIndet;

	// Map<String, DisPar> mapDisPar = new HashMap<String, DisPar>();

	// lista de descuentos generales activos para el serviciobanco
	List<SerBanDesGen> listSerBanDesGen=new ArrayList<SerBanDesGen>();

	
	// distribuidor de sellado
	List<ParSel> listParSel;
	
	// Mapa con las Sesiones de Asentamiento
	Map<String, AsentamientoSession> mapAsentamientoSession = new HashMap<String, AsentamientoSession>();
	
	
	// Mapa con lista de Rescates por idPlan
	Map<Long, List<Rescate>> mapListRescateByPlan = new HashMap<Long, List<Rescate>>();

	// Mapa con lista de Motivos de Caducidad por idPlan
	Map<Long, List<PlanMotCad>> mapListPlanMotCadByPlan = new HashMap<Long, List<PlanMotCad>>();

	// Mapa con lista de PlanExe que no actualiza deuda por idPlan
	Map<Long, List<PlanExe>> mapListPlanExeNoActDeuByPlan = new HashMap<Long, List<PlanExe>>();

	// Mapa con clave Fecha para los Feriados
	Map<Date, Feriado> mapFeriado = new HashMap<Date, Feriado>();
	
	private AsentamientoCache() {
	}

	/**
	 * Devuelve unica instancia
	 */
	public synchronized static AsentamientoCache getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AsentamientoCache();
		}
		return INSTANCE;
	}

	/**
	 * Este metodo inicializa los mapas genericos
	 * 
	 * @throws Exception
	 */
	public synchronized boolean initialize() {
		String key;

		try { 
			// limpia la exception
			this.cacheException = null;

			log.info(DemodaUtil.currentMethodName() + "CACHE:  Inicio");
			// inicializa Sistemas
			// un ServicioBanco tiene muchos Sistemas. pero la clave:
			// idServicioBanco-nroSistema es unica
			List<Sistema> listSistema = Sistema.getList();
			mapSistema.clear();
			for (Sistema sistema : listSistema) {
				key = sistema.getServicioBanco().getId().toString() + "-"
						+ sistema.getNroSistema().toString();
				this.mapSistema.put(key, sistema);
				log.info(DemodaUtil.currentMethodName()+ "CACHE: mapSistema - " + key);
			}

			// inicializa viaDeuda
			List<ViaDeuda> listViaDeuda = ViaDeuda.getList();
			mapViaDeuda.clear();
			for (ViaDeuda viaDeuda : listViaDeuda) {
				key = viaDeuda.getId().toString();
				this.mapViaDeuda.put(key, viaDeuda);
				log.info(DemodaUtil.currentMethodName() + "CACHE: mapViaDeuda - " + key);
			}

			// inicializa estadoDeuda
			List<EstadoDeuda> listEstadoDeuda = EstadoDeuda.getList();
			mapEstadoDeuda.clear();
			for (EstadoDeuda estadoDeuda : listEstadoDeuda) {
				key = estadoDeuda.getId().toString();
				this.mapEstadoDeuda.put(key, estadoDeuda);
				log.info(DemodaUtil.currentMethodName() + "CACHE: mapEstadoDeuda - " + key);
			}
			
			// inicializa banco
			List<Banco> listBanco = Banco.getList();
			mapBanco.clear();
			for (Banco banco : listBanco) {
				key = banco.getCodBanco();
				this.mapBanco.put(key, banco);
				log.info(DemodaUtil.currentMethodName() + "CACHE: mapBanco - " + key);
			}
			
			// inicializa tipoPago
			List<TipoPago> listTipoPago = TipoPago.getList();
			mapTipoPago.clear();
			for (TipoPago tipoPago : listTipoPago) {
				key = tipoPago.getId().toString();
				this.mapTipoPago.put(key, tipoPago);
				log.info(DemodaUtil.currentMethodName()	+ "CACHE: mapTipoPago - " + key);
			}
			
			// tolerancia. key=idServicioBanco.
			// para cada servicioBanco busca y si no encuentra, genera una.
			for (ServicioBanco servicioBanco : ServicioBanco.getList()) {
				Tolerancia tolerancia = Tolerancia.getBySerBanYFecha(servicioBanco, new Date());
				if (tolerancia == null) {
					tolerancia = new Tolerancia();
					tolerancia.setServicioBanco(servicioBanco);
					tolerancia.setToleranciaDifer(0.1D);
					tolerancia.setToleranciaPartida(0.1D);
					tolerancia.setToleranciaSaldo(0.1D);
				}
				key = servicioBanco.getId().toString();
				this.mapTolerancia.put(key, tolerancia);
				log.info(DemodaUtil.currentMethodName()	+ "CACHE: mapTolerancia - " + key);
			}

			// recursos, key=idRecurso
			List<SerBanRec> listSerBanRec = SerBanRec.getList();
			for (SerBanRec serBanRec : listSerBanRec) {
				// del recurso vamos a utilizal el atributo de asentamiento
				key = serBanRec.getRecurso().getId().toString();
				this.mapRecurso.put(key, serBanRec.getRecurso());
				log.info(DemodaUtil.currentMethodName()	+ "CACHE: mapRecurso - " + key);
			}

			// Lista de DisPar Activos Genericos por Recurso para Tipo Importe
			// Indeterminados.
			// Luego, las busquedas son secuenciales dentro de la lista
			this.listDisParIndet = DisPar.getListActivosGenericos();
			for (DisPar disPar : listDisParIndet) {
				// Forzamos la levantada de los DisPar
				disPar.toVO(2);
				disPar.loadListDisParDetVigente();

				log.info(DemodaUtil.currentMethodName()	+ "CACHE: listDisParIndet - " + disPar.getDesDisPar());
			}

			// levanta la lista de SerBanDesGen
			// recorre los SerBanDesGen activos y por cada uno los carga en una lista
			// siempre y cuando el SerBan este activo
			for (SerBanDesGen serBanDesGen : SerBanDesGen.getListActivos()) {
				DesGen desGen = serBanDesGen.getDesGen();
				if (desGen.getEstado().equals(Estado.ACTIVO.getId())) {
					desGen.toVO(1);
					log.info(DemodaUtil.currentMethodName()	+ "CACHE: listSerBanDesGen - " + desGen.getDesDesGen());
					this.listSerBanDesGen.add(serBanDesGen);
				}

			}
			
			// levanta la lista de ParSel
			this.listParSel = ParSel.getListActivos();
			for (ParSel parSel : listParSel) {
				parSel.toVO(2);
				log.info(DemodaUtil.currentMethodName() + "CACHE: listParSel - " + parSel.getId().toString());
			}
			
			// inicializa Mapa con lista de Rescate y lista de PlanMotCad por Plan
			List<Plan> listPlan = Plan.getList();
			mapListRescateByPlan.clear();
			mapListPlanMotCadByPlan.clear();
			mapListPlanExeNoActDeuByPlan.clear();
			for (Plan plan : listPlan) {
				plan.toVO(0);
				List<Rescate> listRescate = Rescate.getListActivosByPlan(plan.getId());
				ListUtilBean.toVO(listRescate,2);
				this.mapListRescateByPlan.put(plan.getId(), listRescate);
				log.info(DemodaUtil.currentMethodName()	+ "CACHE: mapListRescateByPlan - " + plan.getId());
				List<PlanMotCad> listPlanMotCad = plan.getListPlanMotCad();
				ListUtilBean.toVO(listPlanMotCad,2);
				this.mapListPlanMotCadByPlan.put(plan.getId(), listPlanMotCad);
				log.info(DemodaUtil.currentMethodName()	+ "CACHE: mapListPlanMotCadByPlan - " + plan.getId());
				List<PlanExe> listPlanExe = GdeDAOFactory.getPlanExeDAO().getListNoActDeudaByPlan(plan);
				ListUtilBean.toVO(listPlanExe,0);
				this.mapListPlanExeNoActDeuByPlan.put(plan.getId(), listPlanExe);
				log.info(DemodaUtil.currentMethodName()	+ "CACHE: mapListPlanExeByPlan - " + plan.getId());				
			}
			
			// inicializa Mapa de Feriado
			List<Feriado> listFeriado = Feriado.getListActivos();
			mapFeriado.clear();
			for(Feriado feriado: listFeriado){
				feriado.toVO(0);
				this.mapFeriado.put(feriado.getFechaFeriado(), feriado);
				log.info(DemodaUtil.currentMethodName() + "CACHE: mapFeriado - " + feriado.getFechaFeriado());				
			}
			
			// inicializa lista distribuidor partida recurso
			listDisParRec = new ArrayList<DisParRec>();

			// inicializa lista distribuidor partida plan
			listDisParPla = new ArrayList<DisParPla>();
			
			return true;

		} catch (Exception e) {
			log.error(DemodaUtil.currentMethodName() + " Fallo cleanCache", e);
			cacheException = e;
			return false;
		}

	}

	/**
	 * Busca Sistema por ServicioBanco y Sistema
	 * 
	 * @param idServicioBanco
	 * @param nroSistema
	 * @return
	 * @throws Exception
	 */
	public synchronized Sistema getSistemaBySerBanSistema(Long idServicioBanco,
			Long nroSistema) throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);
		String key = idServicioBanco.toString() + "-" + nroSistema.toString();
		Sistema sistema = mapSistema.get(key);
		return sistema;
	}

	/**
	 * Busca el Recurso por id
	 * 
	 * @param idRecurso
	 * @return
	 * @throws Exception
	 */
	public synchronized Recurso getRecursoById(Long idRecurso) throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);
		String key = idRecurso.toString();
		Recurso recurso = mapRecurso.get(key);
		return recurso;
	}

	/**
	 * Obtiene el Distribuidor de Partida asociado al Recurso y Via Deuda
	 * indicados y para el Valor de Atributo de Asentamiento pasado. Devuelve
	 * una lista, para identificar los posibles errores: - Debe existir solo un
	 * valor. - Si no hay ninguno, no se encontr'o. - Si hay m'as de uno est'a
	 * mal configurado
	 * 
	 * @param recurso
	 * @param viaDeuda
	 * @param valorAtr
	 * @return
	 * @throws Exception
	 */
	public synchronized List<DisParRec> getListByRecursoViaDeudaValAtrAse(Recurso recurso, ViaDeuda viaDeuda, String valorAtr) throws Exception {
		if (cacheException != null) throw new Exception("AsentamientoCache invalido.", cacheException);

		List<DisParRec> listRet = new ArrayList<DisParRec>();
		// busca entre la lista por recurso y via
		for (DisParRec disParRec : listDisParRec) {
			if (StringUtil.isNullOrEmpty(valorAtr)) {
				// no se informa valor para el atributo
				// se busca por recurso-via
				if (disParRec.getRecurso().getId().equals(recurso.getId())
						&& disParRec.getViaDeuda().getId().equals(viaDeuda.getId())) {

					// se encuentra DisParRec para recurso-via-valor de atributo
					listRet.add(disParRec);
				}
			} else {
				// se informa valor de atributo
				// se verifica por recurso-via-valor
				if (disParRec.getRecurso().getId().equals(recurso.getId())
						&& disParRec.getViaDeuda().getId().equals(viaDeuda.getId())
						&& valorAtr.equals(disParRec.getValor())) {

					listRet.add(disParRec);
				}
			}
		}

		//issue 7807: BAL - Caché Proceso de asentamiento de pagos
		if(ListUtil.isNullOrEmpty(listRet)){
			listRet = DisParRec.getListByRecursoViaDeudaFechaAtrVal(recurso, viaDeuda, new Date(), valorAtr);
			if(null == listRet)	listRet = new ArrayList<DisParRec>();
			for (DisParRec disParRec : listRet) {
				DisPar disPar = disParRec.getDisPar();
				// forzamos la levantada de los DisPar
				disPar.toVO(3);
				disPar.loadListDisParDetVigente();

				log.info(DemodaUtil.currentMethodName()	+ "CACHE: listDisParRec - " + disPar.getDesDisPar());
			}
			listDisParRec.addAll(listRet);
		}

		// en listRet debe existir solo un valor. Si no hay ninguno, no se
		// encontr'o. Si hay m'as de uno est'a mal configurado
		// se retorna la lista para permitir identificar casos de mala
		// configuracion
		return listRet;
	}

	/**
	 * Obtiene el Distribuidor de Partida para un Plan, Recurso y Valor de atributo
	 * Debe existir solo un valor. 
	 *   - Si no hay ninguno, no se encontr'o. 
	 *   - Si hay m'as de uno est'a mal configurado
	 * 
	 * @throws Exception
	 */
	public synchronized List<DisParPla> getListByPlanRecValAtrAse(Plan plan, Recurso recurso, String valorAtr) throws Exception {
		if (cacheException != null)	throw new Exception("AsentamientoCache invalido.", cacheException);

		List<DisParPla> listRet = new ArrayList<DisParPla>();
		// Busca entre la lista por recurso y via
		for (DisParPla disParPla : listDisParPla) {
			if (StringUtil.isNullOrEmpty(valorAtr)) {
				// No se informa valor para el atributo

				// Se busca por plan y recurso
				if ( disParPla.getPlan().getId().equals(plan.getId()) 
						&&  disParPla.getDisPar().getRecurso().getId().equals(recurso.getId())) {

					// Se encuentra DisParPla para el Plan y Recurso
					listRet.add(disParPla);
				}
			} else {
				// Se informa valor de atributo
				// Se verifica por Plan-Valor
				if (disParPla.getPlan().getId().equals(plan.getId())
						&& disParPla.getDisPar().getRecurso().getId().equals(recurso.getId()) 
						&& valorAtr.equals(disParPla.getValor())) {

					// Se encuentra DisParPla para el Plan, Recurso y el valor de atributo
					listRet.add(disParPla);
				}
			}
		}

		//-issue 7807: BAL - Caché Proceso de asentamiento de pagos
		if(ListUtil.isNullOrEmpty(listRet)){
			listRet = DisParPla.getListByPlanRecursoFechaAtrVal(plan, recurso, new Date(), valorAtr);
			if(null == listRet) listRet = new ArrayList<DisParPla>();
			for (DisParPla disParPla : listRet) {
				DisPar disPar = disParPla.getDisPar();
				// forzamos la levantada de los DisPar
				disPar.toVO(3);
				disPar.loadListDisParDetVigente();
				log.info(DemodaUtil.currentMethodName()	+ "CACHE: listDisParPla  - " + disPar.getDesDisPar());
			}
			listDisParPla.addAll(listRet);
		}

		// En listRet debe existir solo un valor. Si no hay ninguno, no se
		// encontr'o. Si hay m'as de uno est'a mal configurado
		// Se retorna la lista para permitir identificar casos de mala
		// configuracion
		return listRet;
	}

	
	/**
	 * Obtiene el Distribuidor de Partidas Generico para el Tipo Importe Redondeo.
	 * 
	 * @return disPar
	 * @throws Exception
	 */
	public synchronized DisPar getDisParRedondeo()
			throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);

		// Busca entre la lista de generico por recurso
		for (DisPar disPar : listDisParIndet) {
			// Se busca por Recurso y Tipo Importe = Indeterminado
			if (disPar.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_GENERICO)
					&& disPar.getTipoImporte().getId().longValue() == TipoImporte.ID_REDONDEO) {
				// Se encuentra DisPar Generico, Tipo Importe Redondeo
				return disPar;
			}
		}
		return null;
	}
	
	/**
	 * Obtiene el Distribuidor de Partidas de Generico por Recurso para el Tipo
	 * Importe Indeterminado.
	 * 
	 * @param idRecurso
	 * @return disPar
	 * @throws Exception
	 */
	public synchronized DisPar getDisParGenericoByRecurso(String codRecurso)
			throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);

		// Busca entre la lista de generico por recurso
		for (DisPar disPar : listDisParIndet) {
			if (codRecurso != null) {
				// Se busca por Recurso y Tipo Importe = Indeterminado
				if (disPar.getRecurso().getCodRecurso().equals(codRecurso)
						&& disPar.getTipoImporte().getId().longValue() == TipoImporte.ID_INDETERMINADO) {
					// Se encuentra DisPar Generico, Tipo Importe Indeterminado
					// para el Recurso
					return disPar;
				}
			}
		}
		return null;
	}

	/**
	 * Retorna la lista de descuentos generales para el descuento general.
	 * Debe retornar solo uno.
	 * 
	 * @param serBanDesGen
	 * @return
	 * @throws Exception
	 */
	public synchronized List<SerBanDesGen> getSerBanDesGen(Long idSerBan, Date fechaAnalisis ) throws Exception {
		if (cacheException != null) throw new Exception("AsentamientoCache invalido.", cacheException);		

		List<SerBanDesGen> listSerBanDesGenRet = new ArrayList<SerBanDesGen>();
		
		for (SerBanDesGen serBanDesGen : this.listSerBanDesGen ) {
			if ( DateUtil.isDateBeforeOrEqual(serBanDesGen.getFechaDesde(), fechaAnalisis) && 
					DateUtil.isDateBeforeOrEqual(fechaAnalisis, serBanDesGen.getFechaHasta() ) ) {
				
				if (serBanDesGen.getServicioBanco().getId().longValue()==idSerBan)
					listSerBanDesGenRet.add(serBanDesGen);
			}
		}
		return listSerBanDesGenRet;
		

	}

	
	/**
	 * devuelve la lista de ParSel para el idSellado pasado como paramentro
	 * @param idSellado
	 * @return
	 * @throws Exception
	 */
	public synchronized List<ParSel> getListParSel(Long idSellado) throws Exception {
		if (cacheException != null) throw new Exception("AsentamientoCache invalido.", cacheException);		

		List<ParSel> listParSel = new ArrayList<ParSel>();
		for (ParSel parSel : this.listParSel){
			if (parSel.getSellado().getId().longValue() == idSellado.longValue() ) {
				listParSel.add(parSel);
			}
		}
		
		return listParSel;
	}
	
	
	public synchronized Tolerancia getToleranciaById(Long idTolerancia)
			throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);
		String key = idTolerancia.toString();
		Tolerancia tolerancia = mapTolerancia.get(key);
		return tolerancia;
	}
	
	public synchronized EstadoDeuda getEstadoDeudaById(Long idEstadoDeuda)
	throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);
		String key = idEstadoDeuda.toString();
		EstadoDeuda estadoDeuda = mapEstadoDeuda.get(key);
		return estadoDeuda;
	}	

	public synchronized Banco getBancoByCod(String codBanco)
	throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);
		String key = codBanco;
		Banco banco = mapBanco.get(key);
		return banco;
	}	
	
	public synchronized TipoPago getTipoPagoById(Long idTipoPago)
	throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);
		String key = idTipoPago.toString();
		TipoPago tipoPago = mapTipoPago.get(key);
		return tipoPago;
	}
	
	public synchronized Long get(String key) throws Exception {
		if (cacheException != null)
			throw new Exception("Cache .", cacheException);
		// Long ret = indet.get(key);
		// return ret == null ? 0L : ret;
		return null;
	}

	public Long get(Long nroSistema, String nroCuenta, Long nroClave, Long resto)
			throws Exception {
		String key = "";
		return get(key);
	}

	public synchronized void cleanCache() {
		try {
			// listas.clear(), etc, etc

			cacheException = null;
			log.info("cache borrado");
		} catch (Exception e) {
			log.error("Fallo cleanCache", e);
			cacheException = e;
		}
	}

	public synchronized Exception getCacheException() {
		return cacheException;
	}
	
	/**
	 *  Crea una Session de Asentamiento y la carga en el mapa
	 *  
	 * @param idAsentamiento
	 * @return
	 */
	public synchronized boolean createSession(Long idAsentamiento){
		try {
			// limpia la exception
			this.cacheException = null;
			AsentamientoSession asentamientoSession = new AsentamientoSession();

			mapAsentamientoSession.put(idAsentamiento.toString(), asentamientoSession);
		} catch (Exception e) {
			cacheException = e;
			return false;
		}
		return true;
	}

	/**
	 *  Cierra la Session de Asentamiento. La saca del mapa.
	 *  
	 * @param idAsentamiento
	 * @return
	 */
	public synchronized boolean closeSession(Long idAsentamiento){
		try {
			// limpia la exception
			this.cacheException = null;
			mapAsentamientoSession.remove(idAsentamiento.toString());

		} catch (Exception e) {
			cacheException = e;
			return false;
		}
		return true;
	}

	/**
	 *  Obtiene una Session de Asentamiento del mapa
	 *  
	 * @param idAsentamiento
	 * @return
	 */
	public synchronized AsentamientoSession getSession(Long idAsentamiento) throws Exception{
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);
		return mapAsentamientoSession.get(idAsentamiento.toString());
	}

	
	/**
	 * Busca la Lista de Rescates para el idPlan
	 * 
	 * @param idPlan
	 * @return
	 * @throws Exception
	 */
	public synchronized List<Rescate> getListRescateByIdPlan(Long idPlan) throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);
		return mapListRescateByPlan.get(idPlan);
	}

	/**
	 * Busca la Lista de Motivos de Caducidad para el idPlan
	 * 
	 * @param idPlan
	 * @return
	 * @throws Exception
	 */
	public synchronized List<PlanMotCad> getListPlanMotCadByIdPlan(Long idPlan) throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);
		return mapListPlanMotCadByPlan.get(idPlan);
	}

	/**
	 * Busca la Lista de Motivos de Caducidad para el idPlan
	 * 
	 * @param idPlan
	 * @return
	 * @throws Exception
	 */
	public synchronized Map<Date, Feriado> getMapFeriado() throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);
		return mapFeriado;
	}
	
	/**
	 * Busca la Lista de PlanExe que no actualiza deuda para el idPlan
	 * 
	 * @param idPlan
	 * @return listPlanExeNoActDeu
	 * @throws Exception
	 */
	public synchronized List<PlanExe> getListPlanExeNoActDeuByIdPlan(Long idPlan) throws Exception {
		if (cacheException != null)
			throw new Exception("AsentamientoCache invalido.", cacheException);
		return mapListPlanExeNoActDeuByPlan.get(idPlan);
	}
	
	
}
