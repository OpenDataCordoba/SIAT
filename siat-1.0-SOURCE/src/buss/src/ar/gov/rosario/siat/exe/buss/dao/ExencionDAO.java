//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.iface.model.ExencionSearchPage;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class ExencionDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ExencionDAO.class);	
	
	public ExencionDAO() {
		super(Exencion.class);
	}
	
	public List<Exencion> getBySearchPage(ExencionSearchPage exencionSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Exencion t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ExencionSearchPage: " + exencionSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (exencionSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro exencion excluidos
 		List<ExencionVO> listExencionExcluidos = (List<ExencionVO>) exencionSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listExencionExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listExencionExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
 		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(exencionSearchPage.getExencion().getCodExencion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codExencion)) like '%" + 
				StringUtil.escaparUpper(exencionSearchPage.getExencion().getCodExencion()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(exencionSearchPage.getExencion().getDesExencion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desExencion)) like '%" + 
				StringUtil.escaparUpper(exencionSearchPage.getExencion().getDesExencion()) + "%'";
			flagAnd = true;
		}
 		
 		// filtro por recurso
 		if (!ModelUtil.isNullOrEmpty(exencionSearchPage.getExencion().getRecurso())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + exencionSearchPage.getExencion().getRecurso().getId();
			flagAnd = true; 			
 		}
 		
 		// Order By
		queryString += " order by t.desExencion ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Exencion> listExencion = (ArrayList<Exencion>) executeCountedSearch(queryString, exencionSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listExencion;
	}

	/**
	 * Obtiene un Exencion por su codigo
	 */
	public Exencion getByCodigo(String codigo) throws Exception {
		Exencion exencion;
		String queryString = "from Exencion t where t.codExencion = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		exencion = (Exencion) query.uniqueResult();	

		return exencion; 
	}

	public List<Exencion> getListActivosByIdRecurso(Long idRecurso) {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		// Armamos filtros del HQL
		String queryString = "from Exencion exencion " +
							 " where exencion.estado = " + Estado.ACTIVO.getId() +
							 " and exencion.recurso.id = " + idRecurso;
	    
		// Order By
		queryString += " order by exencion.desExencion ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

	    Query query = session.createQuery(queryString);
	    List<Exencion> listExencion = (ArrayList<Exencion>) query.list();

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listExencion;
	}
	

	public List<Exencion> getListActivosByIdRecursoPerManPad(Long idRecurso, boolean permiteManPad) {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		// Armamos filtros del HQL
		String queryString = "from Exencion exencion " +
							 " where exencion.estado = " + Estado.ACTIVO.getId() +
							 " and exencion.recurso.id = " + idRecurso;
	    
		if(permiteManPad)
			queryString += " and exencion.permiteManPad = 1";
		
		// Order By
		queryString += " order by exencion.desExencion ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

	    Query query = session.createQuery(queryString);
	    List<Exencion> listExencion = (ArrayList<Exencion>) query.list();

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listExencion;
	}
	
	public List<Exencion> getListActivosByPlan(Plan plan) {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		// Armamos filtros del HQL
		String queryString = "from Exencion exencion " +
							 " where exencion.estado = " + Estado.ACTIVO.getId();
		queryString += " AND (";
		Boolean primeraPasada = true;
		for (Recurso recurso : plan.getListRecursos() ){
			if (!primeraPasada){
				queryString +=" AND ";
			}else{
				primeraPasada=false;
			}
			queryString += " exencion.recurso.id = " + recurso.getId();
		}
		
		queryString += ")";
	    
		// Order By
		queryString += " order by exencion.desExencion ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

	    Query query = session.createQuery(queryString);
	    List<Exencion> listExencion = (ArrayList<Exencion>) query.list();

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listExencion;
	}
	

	/**
	 * Obtiene la lista de Excenciones que permiten el envio a Judicial
	 * @return List<Exencion>
	 */
	public List<Exencion> getListPermitenEnvioJudicial() {

		Session session = SiatHibernateUtil.currentSession();
		
		// Armamos filtros del HQL
		String queryString = "FROM Exencion exencion WHERE " +
							 "exencion.enviaJudicial = :enviaJudicial ";
		// Order By
		queryString += " ORDER BY exencion.desExencion ";
		
	    Query query = session.createQuery(queryString).setLong("enviaJudicial", SiNo.SI.getId());
	    
	    return (ArrayList<Exencion>) query.list();
	}

	

}
