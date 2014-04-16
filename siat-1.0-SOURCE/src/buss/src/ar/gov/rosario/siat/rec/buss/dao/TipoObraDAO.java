//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.rec.buss.bean.TipoObra;
import ar.gov.rosario.siat.rec.iface.model.TipoObraSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipoObraDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoObraDAO.class);	
	
	public TipoObraDAO() {
		super(TipoObra.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoObra> getTipoObraBySearchPage(TipoObraSearchPage tipoObraSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipoObra t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipoObraSearchPage: " + tipoObraSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoObraSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro por Recurso
 		RecursoVO recurso = tipoObraSearchPage.getTipoObra().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}
		
		// filtro por Descripcion
 		if (!StringUtil.isNullOrEmpty(tipoObraSearchPage.getTipoObra().getDesTipoObra())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desTipoObra)) like '%" + 
				StringUtil.escaparUpper(tipoObraSearchPage.getTipoObra().getDesTipoObra()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.recurso.codRecurso, t.desTipoObra ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipoObra> listTipoObra = (ArrayList<TipoObra>) executeCountedSearch(queryString, tipoObraSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoObra;
	}

	/**
	 * Obtiene un TipoObra por su codigo
	 */
	public TipoObra getByCodigo(String codigo) throws Exception {
		TipoObra tipoObra;
		String queryString = "from TipoObra t where t.codigo = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tipoObra = (TipoObra) query.uniqueResult();	

		return tipoObra; 
	}
	
	/**
	 * Obtiene una lista TipoObra por su codigo
	 */
	public List<TipoObra> getListActivaByRecurso(Long idRecurso) throws Exception {
		
		String queryString = "from TipoObra t where t.recurso.id =  " + idRecurso +
							" and estado = " + Estado.ACTIVO.getId(); 	
		
		queryString += " order by t.desTipoObra "; 
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		
		return  (ArrayList<TipoObra>) query.list();
	}

	/**
	 * Obtiene un una lista de objetos TipoObra correspodientes 
	 * al recurso con idRecurso
	 */
	public List<TipoObra> getListByRecurso(Long idRecurso) throws Exception {
		
		String queryString = "from TipoObra t where t.recurso.id =  " + idRecurso;
 	
		queryString += " order by t.desTipoObra ";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		
		return  (ArrayList<TipoObra>) query.list();
	}
}
