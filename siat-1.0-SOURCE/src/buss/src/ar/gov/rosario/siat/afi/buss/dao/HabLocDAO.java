//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.afi.buss.bean.HabLoc;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class HabLocDAO extends GenericDAO {

	private Log log = LogFactory.getLog(HabLocDAO.class);
	
	public HabLocDAO() {
		super(HabLoc.class);
	}
	
//	public List<HabLoc> getBySearchPage(HabilitacionLocalSearchPage habilitacionLocalSearchPage) throws Exception {
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		String queryString = "from HabLoc t ";
//	    boolean flagAnd = false;
//
//		if (log.isDebugEnabled()) { 
//			log.debug("log de filtros del HabilitacionLocalSearchPage: " + habilitacionLocalSearchPage.infoString()); 
//		}
//	
//		// Armamos filtros del HQL
//		if (habilitacionLocalSearchPage.getModoSeleccionar()) {
//		  queryString += flagAnd ? " and " : " where ";
//	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
//	      flagAnd = true;
//		}
//		
//		// Filtros aqui
//		/* Ejemplos:
//		
//		// filtro habilitacionLocal excluidos
// 		List<HabLocVO> listHabilitacionLocalExcluidos = (List<HabLocVO>) habilitacionLocalSearchPage.getListVOExcluidos();
// 		if (!ListUtil.isNullOrEmpty(listHabilitacionLocalExcluidos)) {
// 			queryString += flagAnd ? " and " : " where ";
//
// 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listHabilitacionLocalExcluidos);
//			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
//			flagAnd = true;
//		}
//		
//		// filtro por codigo
// 		if (!StringUtil.isNullOrEmpty(habilitacionLocalSearchPage.getHabilitacionLocal().getCodHabilitacionLocal())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.codHabilitacionLocal)) like '%" + 
//				StringUtil.escaparUpper(habilitacionLocalSearchPage.getHabilitacionLocal().getCodHabilitacionLocal()) + "%'";
//			flagAnd = true;
//		}
//
//		// filtro por descripcion
// 		if (!StringUtil.isNullOrEmpty(habilitacionLocalSearchPage.getHabilitacionLocal().getDesHabilitacionLocal())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.desHabilitacionLocal)) like '%" + 
//				StringUtil.escaparUpper(habilitacionLocalSearchPage.getHabilitacionLocal().getDesHabilitacionLocal()) + "%'";
//			flagAnd = true;
//		}
// 		
// 		// Order By
//		queryString += " order by t.codHabilitacionLocal ";
//		*/
//		
//	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
//
//		List<HabLoc> listHabilitacionLocal = (ArrayList<HabLoc>) executeCountedSearch(queryString, habilitacionLocalSearchPage);
//		
//		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
//		return listHabilitacionLocal;
//	}

	/**
	 * Obtiene un HabLoc por su codigo
	 */
//	public HabLoc getByCodigo(String codigo) throws Exception {
//		HabLoc habilitacionLocal;
//		String queryString = "from HabLoc t where t.codHabilitacionLocal = :codigo";
//		Session session = SiatHibernateUtil.currentSession();
//
//		Query query = session.createQuery(queryString).setString("codigo", codigo);
//		habilitacionLocal = (HabLoc) query.uniqueResult();	
//
//		return habilitacionLocal; 
//	}
	
}
