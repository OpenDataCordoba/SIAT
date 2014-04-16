//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.afi.buss.bean.ExeActLoc;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class ExeActLocDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ExeActLocDAO.class);
	
	public ExeActLocDAO() {
		super(ExeActLoc.class);
	}
	
//	public List<ExencionActividadLocal> getBySearchPage(ExencionActividadLocalSearchPage exencionActividadLocalSearchPage) throws Exception {
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		String queryString = "from ExencionActividadLocal t ";
//	    boolean flagAnd = false;
//
//		if (log.isDebugEnabled()) { 
//			log.debug("log de filtros del ExencionActividadLocalSearchPage: " + exencionActividadLocalSearchPage.infoString()); 
//		}
//	
//		// Armamos filtros del HQL
//		if (exencionActividadLocalSearchPage.getModoSeleccionar()) {
//		  queryString += flagAnd ? " and " : " where ";
//	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
//	      flagAnd = true;
//		}
//		
//		// Filtros aqui
//		/* Ejemplos:
//		
//		// filtro exencionActividadLocal excluidos
// 		List<ExencionActividadLocalVO> listExencionActividadLocalExcluidos = (List<ExencionActividadLocalVO>) exencionActividadLocalSearchPage.getListVOExcluidos();
// 		if (!ListUtil.isNullOrEmpty(listExencionActividadLocalExcluidos)) {
// 			queryString += flagAnd ? " and " : " where ";
//
// 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listExencionActividadLocalExcluidos);
//			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
//			flagAnd = true;
//		}
//		
//		// filtro por codigo
// 		if (!StringUtil.isNullOrEmpty(exencionActividadLocalSearchPage.getExencionActividadLocal().getCodExencionActividadLocal())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.codExencionActividadLocal)) like '%" + 
//				StringUtil.escaparUpper(exencionActividadLocalSearchPage.getExencionActividadLocal().getCodExencionActividadLocal()) + "%'";
//			flagAnd = true;
//		}
//
//		// filtro por descripcion
// 		if (!StringUtil.isNullOrEmpty(exencionActividadLocalSearchPage.getExencionActividadLocal().getDesExencionActividadLocal())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.desExencionActividadLocal)) like '%" + 
//				StringUtil.escaparUpper(exencionActividadLocalSearchPage.getExencionActividadLocal().getDesExencionActividadLocal()) + "%'";
//			flagAnd = true;
//		}
// 		
// 		// Order By
//		queryString += " order by t.codExencionActividadLocal ";
//		*/
//		
//	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
//
//		List<ExencionActividadLocal> listExencionActividadLocal = (ArrayList<ExencionActividadLocal>) executeCountedSearch(queryString, exencionActividadLocalSearchPage);
//		
//		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
//		return listExencionActividadLocal;
//	}

	/**
	 * Obtiene un ExencionActividadLocal por su codigo
	 */
//	public ExencionActividadLocal getByCodigo(String codigo) throws Exception {
//		ExencionActividadLocal exencionActividadLocal;
//		String queryString = "from ExencionActividadLocal t where t.codExencionActividadLocal = :codigo";
//		Session session = SiatHibernateUtil.currentSession();
//
//		Query query = session.createQuery(queryString).setString("codigo", codigo);
//		exencionActividadLocal = (ExencionActividadLocal) query.uniqueResult();	
//
//		return exencionActividadLocal; 
//	}
	
}
