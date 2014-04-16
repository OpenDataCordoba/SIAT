//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class CanalDAO extends GenericDAO {

	
	public CanalDAO() {
		super(Canal.class);
	}
	
	/*public List<Canal> getBySearchPage(CanalSearchPage canalSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Canal t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CanalSearchPage: " + canalSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (canalSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro canal excluidos
 		List<CanalVO> listCanalExcluidos = (List<CanalVO>) canalSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listCanalExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listCanalExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(canalSearchPage.getCanal().getCodCanal())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codCanal)) like '%" + 
				StringUtil.escaparUpper(canalSearchPage.getCanal().getCodCanal()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(canalSearchPage.getCanal().getDesCanal())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desCanal)) like '%" + 
				StringUtil.escaparUpper(canalSearchPage.getCanal().getDesCanal()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codCanal ";
		*/
	/*	
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Canal> listCanal = (ArrayList<Canal>) executeCountedSearch(queryString, canalSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCanal;
	}
*/
	/**
	 * Obtiene un Canal por su codigo
	 */
	public Canal getByCodigo(String codigo) throws Exception {
		Canal canal;
		String queryString = "from Canal t where t.codCanal = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		canal = (Canal) query.uniqueResult();	

		return canal; 
	}
	
}
