//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.gde.buss.bean.MotivoCierre;
import ar.gov.rosario.siat.gde.iface.model.MotivoCierreSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class MotivoCierreDAO extends GenericDAO {

	private Log log = LogFactory.getLog(MotivoCierreDAO.class);
	
	public MotivoCierreDAO() {
		super(MotivoCierre.class);
	}
	
	public List<MotivoCierre> getBySearchPage(MotivoCierreSearchPage motivoCierreSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from MotivoCierre t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del MotivoCierreSearchPage: " + motivoCierreSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (motivoCierreSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro motivoCierre excluidos
 		List<MotivoCierreVO> listMotivoCierreExcluidos = (List<MotivoCierreVO>) motivoCierreSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listMotivoCierreExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listMotivoCierreExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(motivoCierreSearchPage.getMotivoCierre().getCodMotivoCierre())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codMotivoCierre)) like '%" + 
				StringUtil.escaparUpper(motivoCierreSearchPage.getMotivoCierre().getCodMotivoCierre()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(motivoCierreSearchPage.getMotivoCierre().getDesMotivoCierre())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desMotivoCierre)) like '%" + 
				StringUtil.escaparUpper(motivoCierreSearchPage.getMotivoCierre().getDesMotivoCierre()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codMotivoCierre ";
		*/
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<MotivoCierre> listMotivoCierre = (ArrayList<MotivoCierre>) executeCountedSearch(queryString, motivoCierreSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listMotivoCierre;
	}

	/**
	 * Obtiene un MotivoCierre por su codigo
	 */
	/*public MotivoCierre getByCodigo(String codigo) throws Exception {
		MotivoCierre motivoCierre;
		String queryString = "from MotivoCierre t where t.codMotivoCierre = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		motivoCierre = (MotivoCierre) query.uniqueResult();	

		return motivoCierre; 
	}*/
	
}
