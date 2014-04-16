//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.buss.bean.ImpMasDeu;
import ar.gov.rosario.siat.emi.iface.model.ImpMasDeuSearchPage;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ImpMasDeuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ImpMasDeuDAO.class);
	
	public ImpMasDeuDAO() {
		super(ImpMasDeu.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<ImpMasDeu> getBySearchPage(ImpMasDeuSearchPage impMasDeuSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ImpMasDeu t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ImpMasDeuSearchPage: " + impMasDeuSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (impMasDeuSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro por Recurso
 		RecursoVO servicioBancoVO = impMasDeuSearchPage.getImpMasDeu().getRecurso(); 
 		if (!ModelUtil.isNullOrEmpty(servicioBancoVO)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + servicioBancoVO.getId();
			flagAnd = true;
		}

		// filtro por Estado Corrida
 		EstadoCorridaVO estadoCorridaVO = impMasDeuSearchPage.getImpMasDeu().getCorrida().getEstadoCorrida(); 
 		if (!ModelUtil.isNullOrEmpty(estadoCorridaVO)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida.id = " + estadoCorridaVO.getId();
			flagAnd = true;
		}

 		
 		// Order By
		queryString += " order by t.fechaUltMdf desc";

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ImpMasDeu> listImpMasDeu = (ArrayList<ImpMasDeu>) executeCountedSearch(queryString, impMasDeuSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listImpMasDeu;
	}

	 /**
	  * Obtiene la lista de ids de cuenta (con o sin broches) para un 
	  * determinado recurso con deuda en el intervalo determinado por 
	  * periodoDesde y periodoHasta.
	  *
	  * - Si las cuentas tienen broche, ordenamos por: 
	  *		domicilio de envio.
	  * - Si las cuentas no tienen broche: 
	  * 	si el recurso lo soporta, por clave funcional del objeto 
	  * 	imponible, sino, por numero de cuenta.
  	  *
	  * @param idRecurso
	  * @param anio
	  * @param periodoDesde
	  * @param periodoHasta
	  * @param withBroches
	  * @return
	  */
	@SuppressWarnings("unchecked") 
	public List<Object[]> getListIdCuentaBy(Recurso recurso, Integer anio, Integer periodoDesde, 
			Integer periodoHasta, Atributo atributo, String atrValor, boolean withBroches) {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	 
		// Flag para determinar si se segmenta o no el conjunto de cuentas
		boolean atrFilter = atributo != null && !StringUtil.isNullOrEmpty(atrValor);
		
		String strQuery = ""; 
		
		// Si el recurso tiene configurado un Tipo de Objeto Imponible
		if (recurso.getTipObjImp() != null) {
		 	strQuery += "select distinct(cuenta.id), cuenta.broche.id, cuenta.desDomEnv, objImp.claveFuncional from ";
		 	strQuery += "Cuenta cuenta, DeudaAdmin deuda, ObjImp objImp, ObjImpAtrVal objImpAtrVal ";
		 	strQuery +=	"where cuenta.recurso.id = :idRecurso ";
		 	strQuery +=   "and cuenta.objImp.id = objImp.id ";
		 	strQuery +=	  "and objImp.id = cuenta.objImp.id ";
		 	strQuery +=	  "and objImp.id = objImpAtrVal.objImp.id ";
		 	strQuery +=	  "and deuda.cuenta.id = cuenta.id ";
		 	strQuery +=	  "and deuda.anio = :anio ";
		 	strQuery +=	  "and (deuda.periodo >= :periodoDesde and deuda.periodo <= :periodoHasta) ";

	
		 	// Atributo de Segmentacion
		 	// TODO: Analizar cuando el atributo no es del Objeto Imponible
		 	if (atrFilter) {
			 	strQuery +=	  " and objImpAtrVal.tipObjImpAtr.id = " + TipObjImpAtr.getByIdAtributo(atributo.getId()).getId();
			 	strQuery +=	  " and objImpAtrVal.strValor = " + atrValor;
		 	}
	
		 	if (withBroches) {
		 		strQuery += " and cuenta.broche.id is not null ";
		 		strQuery += " order by cuenta.broche.id, cuenta.desDomEnv";
		 	} else {
		 		strQuery += " and cuenta.broche.id is null ";
		 		strQuery += " order by objImp.claveFuncional ";
		 	}
		} else {
			strQuery += "select distinct(cuenta.id), cuenta.numeroCuenta, cuenta.broche.id, cuenta.desDomEnv from ";
		 	strQuery += "Cuenta cuenta, DeudaAdmin deuda ";
		 	strQuery +=	"where cuenta.recurso.id = :idRecurso ";
		 	strQuery +=	  "and deuda.cuenta.id = cuenta.id ";
		 	strQuery +=	  "and deuda.anio = :anio ";
		 	strQuery +=	  "and (deuda.periodo >= :periodoDesde and deuda.periodo <= :periodoHasta) ";

		 	if (withBroches) {
		 		strQuery += " and cuenta.broche.id is not null ";
		 		strQuery += " order by cuenta.broche.id, cuenta.desDomEnv";
		 	} else {
		 		strQuery += " and cuenta.broche.id is null ";
		 		strQuery += " order by cuenta.numeroCuenta ";
		 	}
		}
	 	
	 	if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + strQuery);
	 	
	 	Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("idRecurso", recurso.getId())
							 .setLong("anio", anio)
							 .setLong("periodoDesde", periodoDesde)
							 .setLong("periodoHasta", periodoHasta);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

		return (ArrayList<Object[]>) query.list(); 
	 }

	@SuppressWarnings("unchecked") 
	public List<DeudaAdmin> getListDeudaAdminBy(Recurso recurso, Long idCuenta, Integer anio, 
			Integer periodoDesde, Integer periodoHasta) {
		 
	 	String strQuery = ""; 
	 	strQuery += "select deuda from DeudaAdmin deuda ";
	 	strQuery +=	"where deuda.cuenta.id = :idCuenta ";
	 	strQuery +=	  "and deuda.anio = :anio ";
	 	strQuery +=	  "and deuda.codRefPag != 0 ";
	 	strQuery +=	  "and deuda.recClaDeu.id = " + recurso.getRecClaDeuOriginal(new Date()).getId();
	 	strQuery +=	  "and (deuda.periodo >= :periodoDesde and deuda.periodo <= :periodoHasta) ";
	 	strQuery +=	"order by deuda.periodo ";
	 	
	 	Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("idCuenta", idCuenta)
							 .setLong("anio", anio)
							 .setLong("periodoDesde", periodoDesde)
							 .setLong("periodoHasta", periodoHasta);

		return (ArrayList<DeudaAdmin>) query.list();
	 }
	
	 /**
	 * Obtiene una cuenta por id con el objeto 
	 * imponible seteado.
	 * 
	 * Metodo utilizado durante el Proceso de Emision de TGI
	 * para minimizar el numero de queries a la BD.
	 * 
	 * @param  idCuenta
	 * @return Cuenta
	 */
	public Cuenta getCuenta(Long idCuenta) {
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": enter");
		
		String strQuery = "";
		strQuery += "select cuenta from Cuenta cuenta ";
		strQuery +=	"left join fetch cuenta.domicilioEnvio ";
		strQuery +=	"left join fetch cuenta.objImp where cuenta.id = " + idCuenta;
				
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery);
	
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": exit");
	
		return (Cuenta) query.uniqueResult(); 
	}

	/**
	 * Retorna la lista de cuentas a excluir de cualquier impresion.
	 * 
	 * Nota: Por ahora implementa la exclusion de cuentas con  
	 * 		 Exencion Total de TGI. 
	 * 
	 * @return List<Long> listIdCuenta
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getListidCuentasExcluidas() {
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": enter");
		
		String strQuery = "";
		strQuery += "select cuenta.id from Cuenta cuenta, CueExe cueExe, Exencion exencion ";
		strQuery +=	"where cueExe.cuenta.id = cuenta.id ";
		strQuery +=	  "and cueExe.exencion.id = exencion.id ";
		strQuery +=   "and exencion.codExencion = '" + Exencion.COD_EXENCION_EXENTO_TOTAL + "' ";
		strQuery +=   "and cueExe.fechaHasta is null";
				
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery);
	
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": exit");
	
		return (List<Long>) query.list(); 
	}

	 /**
	  * 	
	  * Obtiene la lista de ids de cuenta para un determinado DReI, ETUR y Derecho Publicitario. 
	  * con deuda en el intervalo determinado por periodoDesde y periodoHasta.
	  * 
	  * Se utilizara temporalmente hasta que entre en vigencia el Regimen Simplificado. 
	  * 
	  */
	@SuppressWarnings("unchecked")
	public List<Object[]> getListIdCuentaForAutoliquidables(Recurso recurso, Integer anio, 
			Integer periodoDesde, Integer periodoHasta) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		 		
		String strQuery = ""; 
		Atributo atrCatastral = Atributo.getByCodigo(Atributo.COD_CATASTRAL);
		
		if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DReI) ||
			recurso.getCodRecurso().equals(Recurso.COD_RECURSO_ETuR)) {

			TipObjImpAtr tipObjImpAtr = TipObjImpAtr
				.getByIdTipObjImpyIdAtributo(TipObjImp.COMERCIO, atrCatastral.getId());
			RecClaDeu recClaDeu = recurso.getRecClaDeuOriginal(new Date());
			
			strQuery += "select distinct(cuenta.id), objImpAtrVal.strValor from ";
		 	strQuery += "Cuenta cuenta, ObjImpAtrVal objImpAtrVal, DeudaAdmin deuda ";
		 	strQuery += "where cuenta.recurso.id = :idRecurso";
		 	strQuery += " and cuenta.objImp.id = objImpAtrVal.objImp.id";
		 	strQuery += " and objImpAtrVal.tipObjImpAtr.id =  " + tipObjImpAtr.getId();
		 	strQuery += " and deuda.cuenta.id = cuenta.id";
		 	strQuery += " and deuda.recClaDeu.id = " + recClaDeu.getId();
		 	strQuery += " and deuda.fechaEmision >= :fechaAnalisis";
		 	strQuery += " and deuda.anio = :anio";
		 	strQuery += " and (deuda.periodo >= :periodoDesde and deuda.periodo <= :periodoHasta)";
		 	strQuery += " order by objImpAtrVal.strValor";
		}
		
		if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DPUB)) {

			RecAtrCue recAtrCue = RecAtrCue.getAbiertoByIdRecAtrCue(recurso.getId(), atrCatastral.getId());
			RecClaDeu recClaDeu = recurso.getRecClaDeuOriginal(new Date());

			strQuery += "select distinct(cuenta.id), recAtrCueV.valor from ";
			strQuery += "Cuenta cuenta, RecAtrCueV recAtrCueV, DeudaAdmin deuda ";
		 	strQuery += "where cuenta.recurso.id = :idRecurso";
		 	strQuery += " and cuenta.id = recAtrCueV.cuenta.id";
		 	strQuery += " and recAtrCueV.recAtrCue.id = " + recAtrCue.getId();
		 	strQuery += " and deuda.cuenta.id = cuenta.id";
		 	strQuery += " and deuda.recClaDeu.id =  " + recClaDeu.getId();
		 	strQuery += " and deuda.fechaEmision >= :fechaAnalisis";
		 	strQuery += " and deuda.anio = :anio";
		 	strQuery += " and (deuda.periodo >= :periodoDesde and deuda.periodo <= :periodoHasta)";
		 	strQuery += " order by recAtrCueV.valor";
		}

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("idRecurso", recurso.getId())
							 .setLong("anio", anio)
							 .setLong("periodoDesde", periodoDesde)
							 .setLong("periodoHasta", periodoHasta)
							 .setDate("fechaAnalisis", obtenerStrFechaUltimaImpresion(recurso));
		
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return (ArrayList<Object[]>) query.list();
	}
	
	private Date obtenerStrFechaUltimaImpresion(Recurso recurso) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String strQuery = "";
		strQuery += "select max(id) from ImpMasDeu impresion ";
	 	strQuery += "where impresion.recurso.id = :idRecurso";
	 	strQuery += " and impresion.corrida.estadoCorrida.id = " + EstadoCorrida.ID_PROCESADO_CON_EXITO;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("idRecurso", recurso.getId());

		Long maxId = (Long) query.uniqueResult();
		if (maxId != null) {
			ImpMasDeu impresion = ImpMasDeu.getById(maxId);
			return impresion.getFechaUltMdf();
		}
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

		// Retornamos la fecha de comienzo de SIAT
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 14);
		calendar.set(Calendar.MONTH, 9 - 1);
		calendar.set(Calendar.YEAR, 2009);
		return calendar.getTime();
	}
	
 }
