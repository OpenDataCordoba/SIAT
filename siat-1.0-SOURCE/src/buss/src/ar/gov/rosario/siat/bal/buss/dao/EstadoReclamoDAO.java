//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.EstadoReclamo;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class EstadoReclamoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EstadoReclamoDAO.class);
	
	public EstadoReclamoDAO() {
		super(EstadoReclamo.class);
	}
	
	/*public List<EstadoReclamo> getBySearchPage(EstadoReclamoSearchPage estadoReclamoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from EstadoReclamo t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del EstadoReclamoSearchPage: " + estadoReclamoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (estadoReclamoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro estadoReclamo excluidos
 		List<EstadoReclamoVO> listEstadoReclamoExcluidos = (List<EstadoReclamoVO>) estadoReclamoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listEstadoReclamoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listEstadoReclamoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(estadoReclamoSearchPage.getEstadoReclamo().getCodEstadoReclamo())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codEstadoReclamo)) like '%" + 
				StringUtil.escaparUpper(estadoReclamoSearchPage.getEstadoReclamo().getCodEstadoReclamo()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(estadoReclamoSearchPage.getEstadoReclamo().getDesEstadoReclamo())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desEstadoReclamo)) like '%" + 
				StringUtil.escaparUpper(estadoReclamoSearchPage.getEstadoReclamo().getDesEstadoReclamo()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codEstadoReclamo ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<EstadoReclamo> listEstadoReclamo = (ArrayList<EstadoReclamo>) executeCountedSearch(queryString, estadoReclamoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEstadoReclamo;
	}*/

	/**
	 * Obtiene un EstadoReclamo por su codigo
	 */
	public EstadoReclamo getByCodigo(String codigo) throws Exception {
		EstadoReclamo estadoReclamo;
		String queryString = "from EstadoReclamo t where t.codEstadoReclamo = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		estadoReclamo = (EstadoReclamo) query.uniqueResult();	

		return estadoReclamo; 
	}
	
}
