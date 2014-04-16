//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.afi.buss.bean.DecActLoc;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class DecActLocDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DecActLocDAO.class);
	
	public DecActLocDAO() {
		super(DecActLoc.class);
	}
	
//	public List<DecActLoc> getBySearchPage(DeclaracionActLocalSearchPage declaracionActLocalSearchPage) throws Exception {
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		String queryString = "from DecActLoc t ";
//	    boolean flagAnd = false;
//
//		if (log.isDebugEnabled()) { 
//			log.debug("log de filtros del DeclaracionActLocalSearchPage: " + declaracionActLocalSearchPage.infoString()); 
//		}
//	
//		// Armamos filtros del HQL
//		if (declaracionActLocalSearchPage.getModoSeleccionar()) {
//		  queryString += flagAnd ? " and " : " where ";
//	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
//	      flagAnd = true;
//		}
//		
//		// Filtros aqui
//		/* Ejemplos:
//		
//		// filtro declaracionActLocal excluidos
// 		List<DecActLocVO> listDeclaracionActLocalExcluidos = (List<DecActLocVO>) declaracionActLocalSearchPage.getListVOExcluidos();
// 		if (!ListUtil.isNullOrEmpty(listDeclaracionActLocalExcluidos)) {
// 			queryString += flagAnd ? " and " : " where ";
//
// 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDeclaracionActLocalExcluidos);
//			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
//			flagAnd = true;
//		}
//		
//		// filtro por codigo
// 		if (!StringUtil.isNullOrEmpty(declaracionActLocalSearchPage.getDeclaracionActLocal().getCodDeclaracionActLocal())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.codDeclaracionActLocal)) like '%" + 
//				StringUtil.escaparUpper(declaracionActLocalSearchPage.getDeclaracionActLocal().getCodDeclaracionActLocal()) + "%'";
//			flagAnd = true;
//		}
//
//		// filtro por descripcion
// 		if (!StringUtil.isNullOrEmpty(declaracionActLocalSearchPage.getDeclaracionActLocal().getDesDeclaracionActLocal())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.desDeclaracionActLocal)) like '%" + 
//				StringUtil.escaparUpper(declaracionActLocalSearchPage.getDeclaracionActLocal().getDesDeclaracionActLocal()) + "%'";
//			flagAnd = true;
//		}
// 		
// 		// Order By
//		queryString += " order by t.codDeclaracionActLocal ";
//		*/
//		
//	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
//
//		List<DecActLoc> listDeclaracionActLocal = (ArrayList<DecActLoc>) executeCountedSearch(queryString, declaracionActLocalSearchPage);
//		
//		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
//		return listDeclaracionActLocal;
//	}

	/**
	 * Obtiene un DecActLoc por su codigo
	 */
//	public DecActLoc getByCodigo(String codigo) throws Exception {
//		DecActLoc declaracionActLocal;
//		String queryString = "from DecActLoc t where t.codDeclaracionActLocal = :codigo";
//		Session session = SiatHibernateUtil.currentSession();
//
//		Query query = session.createQuery(queryString).setString("codigo", codigo);
//		declaracionActLocal = (DecActLoc) query.uniqueResult();	
//
//		return declaracionActLocal; 
//	}
	
}
