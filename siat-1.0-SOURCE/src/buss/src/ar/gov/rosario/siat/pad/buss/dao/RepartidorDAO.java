//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import ar.gov.rosario.siat.pad.iface.model.RepartidorSearchPage;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class RepartidorDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RepartidorDAO.class);	
	
	public RepartidorDAO(){
		super(Repartidor.class);
	}
	
	public List<Repartidor> getListBySearchPage(RepartidorSearchPage repartidorSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Repartidor t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del RepartidorSearchPage: " + repartidorSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (repartidorSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro Repartidor excluidos
 		List<RepartidorVO> listRepartidorExcluidos = (ArrayList<RepartidorVO>) repartidorSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listRepartidorExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listRepartidorExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Recurso
 		if(!ModelUtil.isNullOrEmpty(repartidorSearchPage.getRepartidor().getRecurso())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso = " +  repartidorSearchPage.getRepartidor().getRecurso().getId();
			flagAnd = true;
		}
 		
 		// filtro por Tipo de Repartidor
 		if(!ModelUtil.isNullOrEmpty(repartidorSearchPage.getRepartidor().getTipoRepartidor())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoRepartidor = " +  repartidorSearchPage.getRepartidor().getTipoRepartidor().getId();
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.tipoRepartidor";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Repartidor> listRepartidor = (ArrayList<Repartidor>) executeCountedSearch(queryString, repartidorSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRepartidor;
	}

	/**
	 * Obtiene lista de Repartidor Activos para el Tipo de Repartidor cuyos id es pasado como parametro.
	 * @author tecso
	 * @param idTipoRepartidor
	 * @return List<Repartidor> 
	 */
	public List<Repartidor> getListActivosByIdTipoRepartidor(Long idTipoRepartidor) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Repartidor t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idTipoRepartidor: " + idTipoRepartidor); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.tipoRepartidor.id = " + idTipoRepartidor;

 		// Order By
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Repartidor> listRepartidor = (ArrayList<Repartidor>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRepartidor;
	}
	
	/** Obtiene la lista de repartidores activos
	 *  filtrados por recurso
	 * 
	 * @param idTipoRepartidor
	 * @return
	 */
	public List<Repartidor> getListActivosByIdRecurso(Long idRecurso) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Repartidor t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + idRecurso); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
        queryString += " and t.recurso.id = " + idRecurso;

 		// Order By
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    Query query = session.createQuery(queryString);
	    List<Repartidor> listRepartidor = (ArrayList<Repartidor>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRepartidor;
	}

	/**
	 * Obtiene la lista de Repartidores activos para un id de broche
	 * @param idBroche
	 * @return List<Repartidor>
	 */
	public List<Repartidor> getListActivosByIdBroche(Long idBroche) {			
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM Repartidor rep ";
	    queryString += " WHERE rep.estado = "+ Estado.ACTIVO.getId();
        queryString += " AND rep.broche.id = " + idBroche;
        
        Query query = session.createQuery(queryString);
        
 		return (ArrayList<Repartidor>) query.list();
	}

}
