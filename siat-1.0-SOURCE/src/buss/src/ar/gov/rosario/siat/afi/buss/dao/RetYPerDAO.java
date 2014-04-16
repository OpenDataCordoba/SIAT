//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.afi.buss.bean.RetYPer;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class RetYPerDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RetYPerDAO.class);
	
	public RetYPerDAO() {
		super(RetYPer.class);
	}
	
//	public List<RetYPer> getBySearchPage(PercepcionSearchPage percepcionSearchPage) throws Exception {
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		String queryString = "from RetYPer t ";
//	    boolean flagAnd = false;
//
//		if (log.isDebugEnabled()) { 
//			log.debug("log de filtros del PercepcionSearchPage: " + percepcionSearchPage.infoString()); 
//		}
//	
//		// Armamos filtros del HQL
//		if (percepcionSearchPage.getModoSeleccionar()) {
//		  queryString += flagAnd ? " and " : " where ";
//	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
//	      flagAnd = true;
//		}
//		
//		// Filtros aqui
//		/* Ejemplos:
//		
//		// filtro percepcion excluidos
// 		List<RetYPerVO> listPercepcionExcluidos = (List<RetYPerVO>) percepcionSearchPage.getListVOExcluidos();
// 		if (!ListUtil.isNullOrEmpty(listPercepcionExcluidos)) {
// 			queryString += flagAnd ? " and " : " where ";
//
// 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listPercepcionExcluidos);
//			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
//			flagAnd = true;
//		}
//		
//		// filtro por codigo
// 		if (!StringUtil.isNullOrEmpty(percepcionSearchPage.getPercepcion().getCodPercepcion())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.codPercepcion)) like '%" + 
//				StringUtil.escaparUpper(percepcionSearchPage.getPercepcion().getCodPercepcion()) + "%'";
//			flagAnd = true;
//		}
//
//		// filtro por descripcion
// 		if (!StringUtil.isNullOrEmpty(percepcionSearchPage.getPercepcion().getDesPercepcion())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.desPercepcion)) like '%" + 
//				StringUtil.escaparUpper(percepcionSearchPage.getPercepcion().getDesPercepcion()) + "%'";
//			flagAnd = true;
//		}
// 		
// 		// Order By
//		queryString += " order by t.codPercepcion ";
//		*/
//		
//	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
//
//		List<RetYPer> listPercepcion = (ArrayList<RetYPer>) executeCountedSearch(queryString, percepcionSearchPage);
//		
//		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
//		return listPercepcion;
//	}

	/**
	 * Obtiene un RetYPer por su codigo
	 */
//	public RetYPer getByCodigo(String codigo) throws Exception {
//		RetYPer percepcion;
//		String queryString = "from RetYPer t where t.codPercepcion = :codigo";
//		Session session = SiatHibernateUtil.currentSession();
//
//		Query query = session.createQuery(queryString).setString("codigo", codigo);
//		percepcion = (RetYPer) query.uniqueResult();	
//
//		return percepcion; 
//	}
	
}
