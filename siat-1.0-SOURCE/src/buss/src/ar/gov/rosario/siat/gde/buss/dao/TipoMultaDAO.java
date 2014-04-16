//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.TipoMulta;
import ar.gov.rosario.siat.gde.iface.model.TipoMultaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.TipoMultaVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipoMultaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoMultaDAO.class);
	
	public TipoMultaDAO() {
		super(TipoMulta.class);
	}
	
public List<TipoMulta> getBySearchPage(TipoMultaSearchPage tipoMultaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipoMulta t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipoMultaSearchPage: " + tipoMultaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoMultaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		// Filtros aqui
		
		
		// filtro tipoMulta excluidos
 		List<TipoMultaVO> listTipoMultaExcluidos = (List<TipoMultaVO>) tipoMultaSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTipoMultaExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";
 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTipoMultaExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Recurso
		if (!ModelUtil.isNullOrEmpty(tipoMultaSearchPage.getTipoMulta().getRecurso())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + tipoMultaSearchPage.getTipoMulta().getRecurso().getId();
			flagAnd = true;
		}

 		//filtro por RecClaDeu
 		if(!ModelUtil.isNullOrEmpty(tipoMultaSearchPage.getTipoMulta().getRecClaDeu())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.recClaDeu.id = " + tipoMultaSearchPage.getTipoMulta().getRecClaDeu().getId();
			flagAnd = true;
 		}
 		
 		// Order By
		queryString += " order by t.id ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipoMulta> listTipoMulta = (ArrayList<TipoMulta>) executeCountedSearch(queryString, tipoMultaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoMulta;
	}

		
	/**
	 * Obtiene lista de TipoMulta Activos para el Recurso especificado
	 * @author tecso
	 * @param Long idCategoria	
	 * @return List<Recurso> 
	 */
	public List<TipoMulta> getListActivosByIdRecurso(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from TipoMulta t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + id); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.recurso.id = " + id;

 		// Order By
		queryString += " order by t.desTipoMulta ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<TipoMulta> listTipoMulta = (ArrayList<TipoMulta>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoMulta;
	}

}
